//
// Override less-plugin load function to include default mixins.
//

/**
 * @fileoverview The LESS plugin.
 */

define('plugin-less', ['plugin-base'], function(require) {

  var plugin = require('plugin-base'),
      less = core.libs.get('less'),
      parser = new(less.Parser);

  plugin.add({
    name: 'less',

    ext: ['.less'],

    load: function(url, callback) {
      core.module.use('text!library/less/global.less', function(globals) {
        core.libs.get('jquery').ajax({
          url: url,
          dataType: 'text',
          success: function(data) {
            parser.parse(globals + data, function(e, tree) {
              if (e) { return console.error(); }
              createCSS(tree.toCSS(), url.replace(/[^\w]/g, '_'));
              callback();
            }, {});
          }
        });
      });
    }
  });


  function createCSS(cssText, id) {
    var elem = document.getElementById(id);
    if (elem) return;

    elem = document.createElement('style');
    elem.id = id;
    document.getElementsByTagName('head')[0].appendChild(elem);

    if (elem.styleSheet) { // IE
      elem.styleSheet.cssText = cssText;
    } else { // W3C
      elem.appendChild(document.createTextNode(cssText));
    }
  }

});

// Add seajs to the core and deny global access to it.
core.libs.add('seajs', seajs.noConflict());

// Add less to the core and deny global access.
core.libs.add('less', less);

if (!(core.detect.browser.IE && core.detect.browser.VERSION < 9)) {
  delete seajs;
  delete less;
}