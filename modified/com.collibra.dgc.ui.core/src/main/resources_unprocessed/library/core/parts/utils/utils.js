/**
 * Core :: Utilities
 *
 * Copyright Collibra 2012
 * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
 */
var core = (function(core) {
  "use strict";

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var $ = core.libs.get('jquery'),
      _ = core.libs.get('underscore');

  /** @private */
  var _core = core.object.extend(core._private, {
    object: {
      difference: function(obj1, obj2) {
        var ret = {},
            diff, _self = this;

        core.object.each(obj1, function(key, val) {
          if (key in obj2) {
            if (core.object.is(obj2[key]) && !core.array.is(obj2[key])) {
              diff = _self.difference(val, obj2[key]);
              if (!_.isEmpty(diff)) {
                ret[key] = diff;
              }
            } else if (!_.isEqual(val, obj2[key])) {
              ret[key] = obj2[key];
            }
          } else {
            ret[key] = val;
          }
        });

        return ret;
      },

      /**
       * @see core.object.isNull
       */
      isNull: function(variable) {
        return _.isNull(variable);
      },

      /**
       * @see core.object.isUndefined
       */
      isUndefined: function(variable) {
        return _.isUndefined(variable);
      }
    },

    template: {
      /**
       * @see core.template.create
       */
      create: function(str) {
        return _.template(str);
      },

      /**
       * @see core.template.setDelimiter
       */
      setDelimiter: function(delimiter) {
        _.templateSettings = {
          interpolate : delimiter
        };
      },

      /**
       * @see core.template.getDelimiter
       */
      getDelimiter: function(delimiter) {
        return _.templateSettings.interpolate;
      },

      /**
       * @see core.template.use
       */
      use: function(template, values) {
        return template(values);
      }
    },

    URL: {
      /**
       * @constructor
       */
      _init: function() {
        this.hash._init();
      },

      hash: {
        prevState: [],
        watched: {},

        _init: function() {
          var _self = this;

          _self.prevState = $.deparam.fragment(true);

          $(window).bind('hashchange', function(ev) {
            // Handle the hash change correctly.
            var newState = $.deparam.fragment(true),
                leftDiff = _core.object.difference(_self.prevState, newState),
                rightDiff = _core.object.difference(newState, _self.prevState);

            if (_.isEmpty(leftDiff) && _.isEmpty(rightDiff) && _.isEmpty(_self.watched)) {
              return;
            }

            _core.object.each(leftDiff, function(key, val) {
              if (rightDiff[key] !== undefined) {
                // Value has changed.
                if (_core.func.is(_self.watched[key])) {
                  _self.watched[key](newState[key], _self.prevState[key]);
                }
              } else {
                // Key is removed.
                if (_core.func.is(_self.watched[key])) {
                  _self.watched[key](undefined, _self.prevState[key]);
                  delete _self.watched[key];
                }
              }
            });

            _core.object.each(rightDiff, function(key, val) {
              if (leftDiff[key] === undefined) {
                // Key was added.
                if (_core.func.is(_self.watched[key])) {
                  _self.watched[key](newState[key]);
                }
              }
            });

            _self.prevState = newState;
          });
        },

        /**
         * @see core.URL.hash.get
         */
        get: function(key) {
          //var hash = $.deparam.fragment(true);

          //return hash[key];
          return $.bbq.getState(key);
        },

        /**
         * @see core.URL.hash.put
         */
        put: function(key, val) {
          var params = key + "=" + val;
          $.bbq.pushState(params, 0);
        },

        /**
         * @see core.URL.hash.remove
         */
        remove: function(key) {
          $.bbq.removeState(key);
        },

        /**
         * @see core.URL.hash.unwatch
         */
        unwatch: function(key) {
          delete this.watched[key];
        },

        /**
         * @see core.URL.hash.watch
         */
        watch: function(key, callback) {
          if (key === "" && !core.func.is(callback)) {
            return false;
          }

          this.watched[key] = callback;
          return true;
        }
      }
    }
  });

  _core.URL._init();

  /*************************************
   *               Public              *
   *************************************/

  return core.object.extend(core, /** @lends core */ {
    _private: _core,

    object: /** @lends object */ {
      /**
       * @alias object
       */
      
      /**
       * Get the difference between two objects. | obj1 - obj2 |
       * @param {Object} obj1 Object to substract from.
       * @param {Object} obj2 Object subsctracting.
       * @returns {Object} Difference between the two.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      difference: function(obj1, obj2) {
        return _core.object.difference(obj1, obj2);
      }.defaults({}, {}),

      /**
       * Check if variable/object is null.
       * @param {Object} variable The variable/object to check.
       * @returns {Boolean} Whether this variable is null or not.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      isNull: function(variable) {
        return _core.object.isNull(variable);
      }.defaults(undefined),

      /**
       * Check if variable/object is undefined.
       * @param {Object} variable The variable/object to check.
       * @returns {Boolean} Whether this variable is undefined or not.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      isUndefined: function(variable) {
        return _core.object.isUndefined(variable);
      }.defaults(null)
    },

    template: /** @lends template */ {
      /**
       * @namespace core
       * @alias template
       */

      /**
       * Create template from a string.
       * @param {String} str The string with template to parse.
       * @returns {Object} Template object.
       * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       * @example
       * template = core.template.create("hello <%= what %>");
       */
      create: function(str) {
        return _core.template.create(str);
      }.defaults(''),

      /**
       * Get the delimiter that is used to evaluate the templates.
       * @returns Returns template delimiter which is regexp object.
       * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      getDelimiter: function() {
        return _core.template.getDelimiter();
      },

      /**
       * Change the delimiter that is used to evaluate the templates.
       * @param {RegExp} [delimiter='/<%=([\s\S]+?)%>/g'] A regexp rule for delimiter.
       * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      setDelimiter: function(delimiter) {
        _core.template.setDelimiter(delimiter);
      }.defaults(/<%=([\s\S]+?)%>/g),

      /**
       * Use previously created template.
       * @param {coreObject} template Template to be used.
       * @param {Object} values Object with values to fill template with.
       * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       * @example
       * parsedTemplateString = core.template.use(template, {what : 'world'});
       * @returns {String} Created template with all values parsed.
       */
      use: function(template, values) {
        return _core.template.use(template, values);
      }.defaults(_.template(''), {})
    },

    URL: /** @lends URL */ {
      /**
       * @namespace core
       * @alias URL
       */
      
      hash: /** @lends URL.hash */ {
        /**
         * @namespace core.URL
         * @alias hash
         */
        
        /**
         * Retrieve a key value pair on the URL hash.
         *
         * @param {String} key The key.
         * @returns {Boolean/String/Number/null/undefined} The associated value. Undefined when there is none.
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        get: function(key) {
          return _core.URL.hash.get(key);
        }.defaults(''),
        
        /**
         * Put a key value pair on the URL hash.
         *
         * @param {String} key The associated key.
         * @param {Any} value The value to put on the hash.
         * @returns {Boolean} True on success, false on failure.
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        put: function(key, value) {
          return _core.URL.hash.put(key, value);
        },

        /**
         * Remove a key-value pair from the URL hash.
         *
         * @param {String} key The key of the pair to remove.
         * @returns {Boolean} True on success, false on failure.
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        remove: function(key) {
          return _core.URL.hash.remove(key);
        },

        /**
         * Stop watching a specific key for changes.
         *
         * @param {String} key [description]
         * @returns {Boolean} True on success, false on failure.
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        unwatch: function(key) {
          return _core.URL.hash.unwatch(key);
        },

        /**
         * Watch a specific key for changes. (Adding, removal and updates.)
         *
         * @param {String} key The key to watch.
         * @param {Function} callback Function executed when the value changes. It takes 2 params, the new value followed by the old value.
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        watch: function(key, callback) {
          return _core.URL.hash.watch(key, callback);
        }
      }
    }
  });
}(core));
