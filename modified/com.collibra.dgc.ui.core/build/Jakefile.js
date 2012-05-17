var path = require('path');

var paths = {
  base: '..',
  core: {
    src: '../src/main/resources_unprocessed/library/core',
    generated: '../src/generated/library/core'
  },
  tools: {
    base: '../../com.collibra.tools/',
    jshint: {
      options: '../../com.collibra.tools/js/jshint/options.js'
    },
    less: {
      globals: '../src/main/resources/library/less/global.less'
    },
    jstestdriver: {
      base: '../../com.collibra.tools/js/jstestdriver',
      jar: 'JsTestDriver-1.3.4.b.jar'
    }
  }
};

var config = {
  core: {
    parts: ['base', 'parts/detection', 'parts/i18n', 'parts/mediator', 'parts/utils']
  }
};

var fs = require('fs'),
    wrench = require('wrench'), // Module for recursive dir and file actions.
    events = require('events'),
    minimatch = require('minimatch'),
    jshint = require('jshint'),
    util = require('util'),
    _ = require('lodash'),
    exec = require('child_process').exec,
    glob = require('glob'); // Module to get files based on a pattern.


namespace('build', function() {
  /**
   * Build everyting.
   */
  task('all', [], function() {
    fcts.platform.check(arguments);
    jake.Task['build:core'].invoke();
    jake.Task['build:modules'].invoke();
  });

  /**
   * Build everything and minify core.
   */
  task('all.production', [], function() {
    fcts.platform.check(arguments);
    jake.Task['build:core.production'].invoke();
    jake.Task['build:modules'].invoke();
  });

  /**
   * Build the core.
   */
  task('core', [], function() {
    fcts.platform.check(arguments);
    fcts.core.compress = false;
    fcts.core.build(function() {
      fcts.exit();
    });
  });

  /**
   * Build and minify core.
   */
  task('core.production', [], function() {
    fcts.platform.check(arguments);
    fcts.core.compress = true;
    fcts.core.build();
  });

  /**
   * Build all modules in the project.
   */
  task('modules', [], function() {
    fcts.platform.check(arguments);
    _.each(fcts.module.getAll(), function(mod) {
      fcts.module.build(mod);
    });
  });
});

namespace('clean', function() {
  task('core', [], function() {
    fcts.core.clean();
  });
});

/**
 * Task that takes care of the building to execute depending on the files
 * that have been changed.
 */
task('filechanged', [], function() {
  var changedFiles = arguments[0],
      i = 0, file = '';
  
  changedFiles = arguments[0].replace(/___/g, ' ').split('::');
  changedFiles = minimatch.match(changedFiles, '**/*.*', {});
  
  fcts.platform.check(arguments);

  changedFiles = fcts.eclipse.splitChangedFiles(changedFiles);

  if (changedFiles.modules.length > 0) {
    for(; i < changedFiles.modules.length; i++) {
      file = fcts.module.basePath(changedFiles.modules[i]);

      mod = fcts.module.analyze(file);
      fcts.module.build(mod);
    }
  }

  if (changedFiles.core.length > 0) {
    fcts.core.build(function() {
      fcts.exit();
    });
  }
});

var fcts = {
    platform: {
      windows: false,
      check: function(args) {
        if (args && args.length > 0 && args[args.length - 1] === 'windows') this.windows = true;
      }
    },
  core: {
    test: true,
    compress: false,
    base: '',
    generated: '',
    files: {
      all: false,
      lint: false,
      tests: false,
      less: false,
      other: false,
      images: false
    },

    analyze: function() {
      var _self = this,
          parts = '';

      _.each(config.core.parts, function(item) {
        parts = parts + ',' + item;
      });

      parts = parts.substring(1);

      _self.base = paths.core.src;
      _self.generated = paths.core.generated;
      var rel = path.relative(process.cwd(), _self.base).replace(/\\/g, "/");
      _self.files.all = _self.filesOnly(glob.sync(rel + '/**/*.*'));
      _self.files.lint = minimatch.match(_self.files.all, '**/{' + parts + '}/*.js', {});
      _self.files.lint = _.union(_self.files.lint, minimatch.match(_self.files.all, '*.js', {}));
      _self.files.less = minimatch.match(_self.files.all, '**/*.less', {});
      _self.files.css = minimatch.match(_self.files.all, '**/*.css', {});
      _self.files.tests = minimatch.match(_self.files.all, '**/tests/**', {});
      _self.files.images = minimatch.match(_self.files.all, '**/*.?(jpg|jpeg|png|gif|bmp)', {});

      _self.files.other = _.difference(_self.files.all,
        _self.files.tests,
        _self.files.lint,
        _self.files.less, 
        _self.files.css, 
        _self.files.images,
        minimatch.match(_self.files.all, '**/libs/**/?(before|after|init).js', {}));
    },

    /**
     * Build the core!
     *
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    build: function(callback) {
      var copyFiles = [],
          _self = this;

      console.log("===== Build CORE =====");
      console.log(" ");

      console.log('===> ANALYZE');
      this.analyze();
      console.log(' ');

      console.log("====> LINT");
      _.each(_self.files.lint, function(file) {
        fcts.lint.file(_self.base + '/' + file);
      });
      console.log(' ');

      this.clean();
      this.prepare();

      console.log('====> BUILD');
      this.buildJS();
      this.buildCSS();
      console.log(' ');

      console.log("====> IMAGES");

      _.each(_self.files.images, function(item) {
        var src = _self.base + '/' + item,
            target = _self.generated + '/images/' + path.basename(item);

        if (path.existsSync(src)) {
          fcts.files.copy(src, target);
          console.log(target);
        }
      });

      console.log(" ");

      _self.runTests(function() {
        console.log('========================');
        console.log(' ');
        console.log(' ');
        console.log(' ');
        console.log(' ');

        if (callback) callback();
      });
    },

    buildCSS: function() {
      var buff = '',
          _self = this;

      _.each(_self.files.css, function(filePath) {
        buff = buff + fs.readFileSync(_self.base + '/' + filePath, 'utf-8');
      });

      _self.buildLESS(function(css) {
        var targetFile = _self.generated + '/core.css';

        buff = buff + '\n' + css;

        if (_self.compress) {
          buff = fcts.css.compress(buff);
        }

        fs.writeFileSync(targetFile, buff);
        console.log(targetFile);
      });
    },

    buildPartJS: function(part) {
      var _self = this,
          buff = '',
          before, after, pluginPaths, libPaths, init;

      // Libs
      before = _self.base + '/' + part + '/libs/plugins/before.js';
      if (path.existsSync(before)) {
        before = fs.readFileSync(before);
      } else {
        before = '';
      }

      after = _self.base + '/' + part + '/libs/plugins/after.js';
      if (path.existsSync(after)) {
        after = fs.readFileSync(after);
      } else {
        after = '';
      }
      
      init = _self.base + '/' + part + '/libs/init.js';
      if (path.existsSync(init)) {
        init = fs.readFileSync(init);
      } else {
        init = '';
      }

      libPaths = minimatch.match(_self.files.other, part + '/libs/*.js', {});
      pluginPaths = minimatch.match(_self.files.other, part + '/libs/plugins/*.js', {});

      _.each(libPaths, function(item) {
        buff = buff + fs.readFileSync(_self.base + '/' + item);
      });

      buff = buff + init + before;

      _.each(pluginPaths, function(item) {
        buff = buff + fs.readFileSync(_self.base + '/' + item);
      });

      buff = buff + after;

      return buff;
    },

    buildJS: function() {
      var _self = this,
          buff = '',
          target = _self.generated + '/core.js',
          parts;

      // PRE
      buff = buff + fs.readFileSync(_self.base + '/pre.js');

      // BASE
      buff = buff + _self.buildPartJS('base') + fs.readFileSync(_self.base + '/base/base.js');

      // PARTS
      parts = fs.readdirSync(_self.base + '/parts');
      _.each(parts, function(item) {
        buff = buff + _self.buildPartJS('parts/' + item) + fs.readFileSync(_self.base + '/parts/' + item + '/' + item + '.js');
      });

      // POST
      buff = buff + fs.readFileSync(_self.base + '/post.js');

      if (_self.compress) {
        buff = fcts.js.compress(buff);
      }

      fs.writeFileSync(target, buff);
      console.log(target);
    },

    buildLESS: function(callback) {
      var buff = '',
          _self = this;

      _.each(_self.files.less, function(filePath) {
        buff = buff + fs.readFileSync(_self.base + '/' + filePath, 'utf-8');
      });

      fcts.less.compile(buff, callback);
    },

    clean: function() {
      console.log("====> CLEAN");
      console.log(this.generated);
      wrench.rmdirSyncRecursive(this.generated, true);
      console.log(" ");
    },

    filesOnly: function(arr) {
      var _self = this;

      _.each(arr, function(item, idx) {
        arr[idx] = path.relative(_self.base, item);
      });

      return arr;
    },

    /**
     * Prepare core for building. This means creating the necessary directories.
     *
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    prepare: function() {
      console.log("====> PREPARE");
      wrench.mkdirSyncRecursive(this.generated + '/images');
      console.log(" ");
    },

    /** 
     * Run core unit testing.
     */
    runTests: function(callback) {
      var _self = this;

      if (this.test) {
        console.log("====> TEST");

        fcts.testing.server.start(function() {
          fcts.testing.client.start(function() {
            fcts.testing.test(path.resolve(_self.base + '/tests.conf'), paths.base + "/target/tests", function() {
              if (callback) callback();
            });
          });
        });
      } else {
        if (callback) callback();
      }
    }
  },

  css: {
    compress: function(stream) {
      var ncss = require('ncss');
      return ncss(stream);
    }
  },

  eclipse: {
    /**
     * Split the array of changed files based on the build process needed for it.
     *
     * @param {String} changedFiles String containing all changed files as passed from eclipse.
     * @returns {Object} An object containing 3 arrays with changed files for:
     *  {
     *    core: [],
     *    modules: [],
     *    testing: []
     *  }
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    splitChangedFiles: function(changedFiles) {
      return files = {
        core: minimatch.match(changedFiles, "**/resources_unprocessed/library/core/**/*.*", {}),
        modules: minimatch.match(changedFiles, "**/resources_unprocessed/{library/uielements,modules,library/js/compat}/**/*.*", {}),
        testing: minimatch.match(changedFiles, "**/resources_unprocessed/{library/uielements,modules,library/js/compat}/tests/**/*.*", {})
      };
    }
  },

  exit: function() {
    this.testing.server.stop();
    this.testing.client.stop();
    process.exit(0);
  },

  files: {
    /**
     * Concatenate files to a destination file. This function always overwrites the destination file.
     *
     * @param {Array<String>} fileList A lit of files to concatenate.
     * @param {String} distFile File to concatenat to.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    concat: function(fileList, distFile) {
      var out = fileList.map(function(filePath) {
            return fs.readFileSync(filePath, 'utf-8');
          });

      fs.writeFileSync(distFile, out.join('\n'));
    },

    /** 
     * Copy file.
     * 
     * @param {String} src Path to source file.
     * @param {String} target Path to target file.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    copy: function(src, target) {
      wrench.mkdirSyncRecursive(path.dirname(target));
      inStr = fs.createReadStream(src);
      outStr = fs.createWriteStream(target);
      inStr.pipe(outStr);
    },

    image: {
      compress: function(path, target) {
        switch(path.substring(-3)) {
          case 'png':
            this.png(path);
            break;
          case 'gif':
            this.gif(path);
            break;
        }
      },

      png: function(path, target) {
        console.log('Compress png.')
      },

      gif: function(path, target) {
        console.log('Compress gif.')
      }
    }
  },

  js: {
    compress: function(stream) {
      var jsp = require('uglify-js').parser,
          pro = require('uglify-js').uglify,
          ast;

      ast = jsp.parse(stream);
      ast = pro.ast_mangle(ast);
      ast = pro.ast_squeeze(ast);
      return pro.gen_code(ast);
    }
  },

  module: {
    /**
     * Analyse a module based on a base path and return a module object.
     *
     * @param {String} basePath The base path of the module.
     * @returns {Object} An object containing everything there is to know about a module.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    analyze: function(basePath) {
      var singletonFiles = [],
          exists = false,
          mod = {
        name: basePath.substring(basePath.lastIndexOf('/') + 1), // The name of the module.
        base: {
          src: basePath, // Base path to the source (resources_unprocessed).
          generated: basePath.replace('main/resources_unprocessed', 'generated') // Base path to the generated folder.
        },
        files: {
          all: false,
          main: false,
          less: false, // Contains paths to all less files.
          css: false, // Paths to all css files.
          libs: false, // The libraries. (Doesn't contain before.js and after.js.)
          i18n: false, // Paths to all translation files.
          images: false, // Paths to all images in images directory and child dirs.
          other: false
        }
      };
      
      var rel = path.relative(process.cwd(), mod.base.src).replace(/\\/g, "/");
      mod.files.all = this.filesOnly(mod, glob.sync(rel + '/**/*.*'));
      mod.files.less = minimatch.match(mod.files.all, '**/*.less', {});
      mod.files.css = minimatch.match(mod.files.all, '**/*.css', {});
      mod.files.libs = minimatch.match(mod.files.all, 'libs/**/!(before|after).js', {});
      mod.files.i18n = minimatch.match(mod.files.all, 'i18n/*.i18n', {});
      mod.files.images = minimatch.match(mod.files.all, 'images/**/*.?(jpg|jpeg|png|gif|bmp)', {});

      mod.files.main = mod.name + '.js';
      mod.files.vm = mod.name + '.vm';
      mod.files.props = mod.name + '.properties';

      exists = path.existsSync(mod.base.src + '/' + mod.files.vm);

      if (!exists) {
        mod.files.vm = false;
      } else {
        singletonFiles.push(mod.files.vm);
      }

      exists = path.existsSync(mod.base.src + '/' + mod.files.props);

      if (!exists) {
        mod.files.props = false;
      } else {
        singletonFiles.push(mod.files.props);
      }

      exists = path.existsSync(mod.base.src + '/' + mod.files.main);

      if (!exists) {
        mod.files.main = false;
      } else {
        singletonFiles.push(mod.files.main);
      }

      mod.files.other = _.difference(mod.files.all, 
        mod.files.less, 
        mod.files.css, 
        mod.files.libs, 
        mod.files.i18n, 
        mod.files.images, 
        singletonFiles, 
        minimatch.match(mod.files.all, 'libs/**/?(before|after).js', {}));

      return mod;
    },

    /**
     * Construct the base path of a module out of a file path.
     *
     * @param {String} path
     * @returns {String} Base path of the module. Which is the path leading up to the module including the dir itself. e.g.
     * /com.collibra.dgc.ui.core/src/main/resources_unprocessed/library/uielements/dropdown
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    basePath: function(pth) {
      var firstPart = path.dirname(pth);

      if (this.is(firstPart)) {
        return firstPart;
      } else {
        return this.basePath(firstPart);
      }
    },

    /**
     * Build a module.
     *
     * @param {Object} mod A module object.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    build: function(mod) {
      var copyFiles = [],
          _self = this;

      console.log('========================');
      console.log("===== Build Module =====");
      console.log("= Name: " + mod.name);
      console.log("= Path: " + mod.base.src);
      console.log(" ");

      this.lint(mod);
        _self.clean(mod);
        _self.prepare(mod);
        console.log("====> BUILD");
        _self.buildJS(mod);
  
        if (mod.files.css.length > 0 || mod.files.less.length > 0) {
          _self.buildCSS(mod);
        }
        console.log(" ");
  
        copyFiles = [];
        
  
        if (!mod.files.vm) {
          console.log('====> CREATE')
          fs.writeFileSync(mod.base.generated + '/' + mod.name + '.vm');
          console.log(mod.base.generated + '/' + mod.name + '.vm');
          console.log(' ');
        } else {
          copyFiles.push(mod.files.vm);
        }
  
        if (mod.files.props) {
          copyFiles.push(mod.files.props);
        }
  
        copyFiles = _.union(copyFiles, mod.files.i18n, mod.files.images, mod.files.other);
  
        if (copyFiles.length > 0) {
          console.log("====> COPY");
          _.each(copyFiles, function(item) {
            var src = mod.base.src + '/' + item,
                target = mod.base.generated + '/' + item;
  
            if (path.existsSync(src)) {
              fcts.files.copy(src, target);
              console.log(target);
            }
          });
        }
  
        console.log('========================');
        console.log(' ');
        console.log(' ');
        console.log(' ');
    },

    buildJS: function(mod) {
      var files = [];

      if (path.existsSync(mod.base.src + '/libs/before.js')) {
        files.push(mod.base.src + '/libs/before.js');
      }

      _.each(mod.files.libs, function(item) {
        files.push(mod.base.src + '/' + item);
      });

      if (path.existsSync(mod.base.src + '/libs/after.js')) {
        files.push(mod.base.src + '/libs/after.js');
      }

      if (mod.files.main) {
        files.push(mod.base.src + '/' + mod.files.main);
      }

      fcts.files.concat(files, mod.base.generated + '/' + mod.files.main);
      console.log(mod.base.generated + '/' + mod.files.main);
    },

    buildCSS: function(mod, callback) {
      var buff = '';

      _.each(mod.files.css, function(filePath) {
        buff = buff + fs.readFileSync(mod.base.src + '/' + filePath, 'utf-8');
      });

      this.buildLESS(mod, function(css) {
        var targetFile = mod.base.generated + '/' + mod.name + '.css';

        buff = buff + '\n' + css;
        fs.writeFileSync(targetFile, buff);
        console.log(targetFile);
      });
    },

    buildLESS: function(mod, callback) {
      var buff = '';

      _.each(mod.files.less, function(filePath) {
        buff = buff + fs.readFileSync(mod.base.src + '/' + filePath, 'utf-8');
      });

      fcts.less.compile(buff, callback);
    },

    /**
     * Clean previously generated files.
     *
     * @param {Object} mod A module object.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    clean: function(mod) {
      console.log("====> CLEAN");
      console.log(mod.base.generated);
      wrench.rmdirSyncRecursive(mod.base.generated, true);
      console.log(" ");
    },

    /**
     * Return only the file path relative to the module's path for this array of file paths.
     *
     * @param {Object} mod A module object.
     * @param {Array<String>} arr The path to the file.
     * @returns {Array<String>} The path relative to the module.
     */
    filesOnly: function(mod, arr) {
      _.each(arr, function(item, idx) {
        arr[idx] = path.relative(mod.base.src, item);
      });

      return arr;
    },

    /** 
     * Get all modules in the current project, based on the supplied basepaths in config.
     *
     * @returns {Array<Object>} An array of module objects (already analyzed).
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    getAll: function() {
      var mods = [],
          filePaths = [],
          _self = this;
      
      filePaths = _.filter(glob.sync('../**/resources_unprocessed/**/!(images|tests|core|libs|css|less|*.*)'), function(item) {
        return item.indexOf('core') < 0;
      });

      _.each(filePaths, function(item) {
        if (_self.is(item)) {
          mods.push(_self.analyze(item));
        }
      });

      return mods;
    },

    /**
     * Check whether a certain path is a module or not by checking if .js module file exists.
     *
     * @param {String} pth Path to the module base.
     * @returns {Boolean} Whether this is a module or not.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    is: function(pth) {
      var file = path.basename(pth);

      file = pth + '/' + file + '.js';
      return path.existsSync(file);
    },

    /**
     * Lint all the files that need to be linted.
     *
     * @param {Object} mod A module object.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    lint: function(mod) {
      console.log("====> LINT ");
      fcts.lint.file(mod.base.src + '/' + mod.files.main, function() {
        console.log(" ");
      });
    },

    /**
     * Prepare a module for building. This means creating the necessary directories.
     *
     * @param {Object} mod A module object.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    prepare: function(mod) {
      console.log("====> PREPARE");
      wrench.mkdirSyncRecursive(mod.base.generated);
      console.log(mod.base.generated);
      console.log(" ");
    }
  },

  less: {
    globals: fs.readFileSync(paths.tools.less.globals),

    /**
     * Compile less to css.
     *
     * @param {String} The text to compile.
     * @param {Function} callback Function executed upon completion. Function takes 2 params: e, css.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    compile: function(buff, callback) {
      var less = require('less');

      buff = this.globals + buff;

      less.render(buff, function(e, css) {
        callback(css);
      });
    },

    /**
     * Compile a less file to a css file.
     *
     * @param {String} src Path to the source file.
     * @param {String} target Path to the destination file.
     * @param {Function} callback Function executed upon completion.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    compileFile2File: function(src, target, callback) {
      var buff = fs.readFileSync(src);

      this.compile(buff, function(css) {
        fs.writeFileSync(target, css);
        callback();
      });
    }
  },

  lint: {
    /**
     * Lint a single JS file.
     * @param {String} src The path to the source file.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a> 
     */
    file: function(src) {
      var options = fs.readFileSync(paths.tools.jshint.options, 'utf-8'),
          buf = fs.readFileSync(src, 'utf-8'),
          nErrors = false;

      buf = buf.replace(/^\uFEFF/, '');
      buf = options + '\n' + buf;

      jshint.JSHINT(buf);

      nErrors = jshint.JSHINT.errors.length;
      
      console.log(src);
      if (nErrors) {
        console.log(" ");
        this.showErrors(jshint.JSHINT.errors, src, options.split('\n').length);
        process.exit(1);
      }
    },

    /**
     * Show linting errors in a human-readable way!
     *
     * @param {Array<Object>} errors Errors retrieved from JSHINT.
     * @param {Number} optionsLineCount The number of lines of added options. This is to substract from the error line.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    showErrors: function(errors, file, optionsLineCount) {
      if (fcts.platform.windows) fs.writeSync(1, "LINTING: " + file + '\n\n');

      _.each(errors, function(item, idx) {
        fs.writeSync(1, "ERROR " + (idx + 1) + ": line " + (item.line - optionsLineCount) + ": " + item.reason + '\n');
        fs.writeSync(1, "         " + item.evidence.trim() + '\n');
        fs.writeSync(1, " \n");
      });
    }
  },

  testing: {
    client: {
      _captured: false,
      _phantom: false,

      /**
       * Start the testing client. Be aware to only do this when a server is running.
       */
      start: function(callback) {
        var _self = this;

        if (_self._phantom && _self._captured) {
          callback();
          return;
        };

        var phantom = require('phantom');

        _self.phantom = phantom.create(function(ph) {
          ph.createPage(function(page) {
            var url = 'http://localhost:4444/capture',
                captureAttempts = 0,
                locked = false;

            var log = function(str) {
              var dt = new Date();
              console.log(dt.toString() + ': ' + str);
            };

            var pageLoaded = function() {
              var runnerFrame = page.evaluate(function() {
                  return document.getElementById('runner');
              }, function(runnerFrame) {
                if (!runnerFrame) {
                  locked = false;
                  setTimeout(capture, 1000);
                } else {
                  _self._captured = true;
                  console.log('Starting client...');
                  if (callback) callback();
                }
              });
            };

            var capture = function() {
              if (captureAttempts === 20) {
                log('Failed to capture JSTD after ' + captureAttempts + ' attempts.');
                _self._phantom.exit();
              }

              if (_self._captured || locked) {
                return;
              }

              captureAttempts += 1;
              locked = true;

              setTimeout(pageLoaded, 1000);

              page.open(url);
            };

            capture();
          });
        });
      },

      stop: function() {
        if (this._phantom) this._phantom.exit();
      }
    },

    test: function(config, output, callback) {
      var resultsPassed = true;
      console.log('Sending tests...');
      console.log(' ');
      var child = exec('java -jar ' + paths.tools.jstestdriver.base + '/' + paths.tools.jstestdriver.jar + ' --reset --tests all --server http://localhost:4444 --config "' + config + '" --testOutput "' + output + '"', {}, function(error, stdout, stderror) {
        if (resultsPassed) {
          if (callback) callback();
        } else {
          fcts.testing.server.stop();
          process.exit(1);
        }
      });
      child.stdout.on('data', function(chunk) {
        if (chunk[0] !== '.' && chunk[0] !== 'F' && chunk.indexOf('runnermode') < 0 && chunk.indexOf('Reset') < 0) {
          console.log(chunk);
        }
        if (chunk.indexOf('failed') >= 0) {
          resultsPassed = false;
        }
      });
      //child.stderr.on('data', function(chunk) {console.log(chunk);});
    },

    server: {
      _server: false,

      start: function(callback) {
        _self = this;

        if (!this._server) {
          this._server = true;
          console.log('Starting server...');
          this._server = exec("java -jar " + paths.tools.jstestdriver.base + '/' + paths.tools.jstestdriver.jar + " --port 4444 --runnerMode INFO");
          //this._server.stdout.on('data', function(chunk) { console.log(chunk);});
          //this._server.stderr.on('data', function(chunk) { console.log(chunk);});
          callback();
        } else {
          console.log('Testing server already running...');
          if (callback) callback();
        }
      },

      stop: function(callback) {
        if (this._server) this._server.kill();
      }
    }
  }
};
