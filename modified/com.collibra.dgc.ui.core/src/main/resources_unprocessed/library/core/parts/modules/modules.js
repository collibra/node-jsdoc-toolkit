/*global define:true, require:true */

/**
 * Core :: Module Manager
 *
 * Copyright Collibra 2012
 * @author Clovis Six <clovis@collibra.com>
 */
var core = (function(core) {
  "use strict";

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var $ = core.libs.get('jquery'),
      sea = core.libs.get('seajs');

  /** @private */
  var _core = core.object.extend(core._private, {
    module: {
      /* Configuration object for seajs. */
      config: {
        alias: {},
        preload: ['plugin-text', 'plugin-less'],
        debug: true
      },

      /**
       * @constructor
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      _init: function() {
        sea.config(_core.module.config);
      },

      /**
       * Refresh the module managers configuration.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      _refreshConfig: function() {
        sea.config(_core.module.config);
      },

      alias: {
        /**
         * @see core.module.alias.add
         */
        add: function(al, path) {
          var result = false;

          if (_core.module.config.alias[al] !== path) {
            _core.module.config.alias[al] = path;
            result = true;
          }

          _core.module._refreshConfig();
          return result;
        },

        /**
         * @see core.module.alias.edit
         */
        edit: function(al, newPath) {
          var result = false;

          if (_core.module.config.alias[al]) {
            _core.module.config.alias[al] = newPath;
            result = true;
          }

          _core.module._refreshConfig();
          return result;
        },

        /**
         * @see core.module.alias.remove
         */
        remove: function(al) {
          delete _core.module.config.alias[al];
          _core.module._refreshConfig();
        }
      },

      /**
       * @see core.module.expose
       */
      expose: function(modname, name) {
        sea.use(modname, function(lib) {
          if (name === '') {
            name = modname;
          }

          window[name] = lib;
        });
      },

      /**
       * @see core.module.get
       */
      get: function(mod, callback1, callback2, params, cache) {
        sea.get(mod, callback1, callback2, params, cache);
      },

      region: {
        /**
         * @see core.module.region.get
         */
        get: function(region, callbackContent, params) {
          sea.getRegion(region, callbackContent, params);
        }
      },

      /**
       * @see core.module.use
       */
      use: function(mods, callback) {
        // Create a deferred object to return.
        var deferredObj = core.object.createDeferred(),
          // Create a new callback function that resolves the deferred object.
          newCallback = function() {
            if (core.func.is(callback)) {
              callback.apply(this, arguments);
            }

            deferredObj.resolve(arguments);
          };

        // Use sea as the module manager in the background.
        sea.use(mods, newCallback);

        return deferredObj;
      }
    }
  });

  _core.module._init();

  /*************************************
   *               Public              *
   *************************************/

  return core.object.extend(core, /** @lends core */ {
    _private: _core,

    module: /** @lends module */ {
      /**
       * Module Operations
       * @namespace core
       * @alias module
       */

      alias: /** @lends alias */ {
        /**
         * Manage aliasses for modules.
         * @namespace core/module
         * @alias alias
         */

        /**
         * Add an alias for a module to find it easier.
         * @param {String} alias The alias to add.
         * @param {String} path The path that points to the module.
         * @returns {Boolean} Returns false when it already exists (for another module) and true when it is added correctly.
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        add: function(alias, path) {
          return _core.module.alias.add(alias, path);
        }.defaults('', ''),

        /**
         * Edit an alias of a module.
         * @param {String} alias The alias to edit.
         * @param {String} newPath The new path of the module.
         * @returns {Boolean} Returns false when the alias was not found and true when it was edited corectly.
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        edit: function(alias, newPath) {
          return _core.module.alias.edit(alias, newPath);
        }.defaults('', ''),

        /**
         * Remove an alias from the module manager.
         *
         * @param {String} alias Alias to remove.
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        remove: function(alias) {
          _core.module.alias.remove(alias);
        }.defaults('')
      },

      /**
       * Expose a module to the global scope. ex: expose('i18n') will allow you to use i18n as variable everywhere.
       *
       * @note The exposal is only done once the module is loaded effectively. (This happens async.)
       * @param {String} modname The name of the module.
       * @param {String} name The name used in the global scope.
       * @example
       * core.module.expose('i18n', 'language');
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      expose: function(modname, name) {
        _core.module.expose(modname, name);
      },

      /**
       * Load a module and get the content.
       *
       * @param {String} mod Path to the module to fetch.
       * @param {Function} callbackContent This callback contains the velocity content in the first param.
       * @param {Function} callbackModule Function called when JS module is completely loaded.
       * @param {Object} params The parameters you want to pass to the module vm. (Current Resource ID is always passed!)
       * @param {Object} cachePolicy What is the cache policy for this?
       *   @option {Boolean} vm Do you want to take the velocity out of cache if possible?
       *   @option {Boolean} js Do you want to take the JavaScript out of cache if possible?
       *   @option {Boolean} css Do you want to take the CSS out of cache if possible?
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      get: function(mod, callbackContent, callbackModule, params, cachePolicy) {
        _core.module.get(mod, callbackContent, callbackModule, params, cachePolicy);
      }.defaults('', function() {}, function(mod) {}, {},{
        vm: false,
        js: true,
        css: true
      }),

      region: /** @lends core.module.region */ {
        /**
         * Manage regions of modules.
         * @namespace core/module/region
         * @alias region
         */

        /**
         * Get an entire region of modules.
         * @param {String} region ID of the region to get.
         * @param {Function} callbackContent This callback contains the velocity content in the first param.
         * @param {Object} params The parameters you want to pass to the module vm. (Current Resource ID is always passed!)
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        get: function(region, callbackContent, params) {
          _core.module.region.get(region, callbackContent, params);
        }.defaults('', function() {}, {})
      },

      /**
       * Use certain modules for a callback function.
       * @param {String/Array} mods String or array of (a) module(s) that are needed before executing the funciton.
       * @param {Function} callback A callback function that receives the modules as params.
       * @returns {Deferred Object} A deferred object.
       * @example
       * core.module.use('i18n', function(i18n) {
       *   alert(i18n.version);
       * });
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      use: function(mods, callback) {
        return _core.module.use(mods, callback);
      }
    }
  });
}(core));
