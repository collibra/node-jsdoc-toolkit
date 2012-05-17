/**
 * Core :: Internationalization
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
  var exceptions = core.debug.exceptions,
      array = core.array,
      string = core.string,
      object = core.object;

  /** @private */
  var _core = core.object.extend(core._private, {
    i18n: {
      // Storage of translations.
      trans: {},

      /**
       * @see core.i18n.add
       */
      add: function(args) {
        var _self = this,
            namesp = args[0],
            fqn0 = "core.i18n.add(namespace, ...)",
            fqn1 = "core.i18n.add(namespace, keys, values)",
            fqn2 = "core.i18n.add(namespace, object)",
            obj = args[1];

        if (!string.is(namesp)) {
          exceptions.illegalArgument(fqn0, "First argument should be of type string.");
        }

        if (args.length === 3) {
          var keys = args[1],
              vals = args[2];

          if (!array.is(keys) && !string.is(keys)) {
            exceptions.illegalArgument(fqn1, "Second argument should be of type array or string.");
          }

          if (!array.is(vals) && !string.is(vals)) {
            exceptions.illegalArgument(fqn1, "Third argument should be of type array or string.");
          }

          if (array.is(keys) || array.is(vals)) {
            if (!array.is(vals) || !array.is(keys)) {
              exceptions.illegalArgument(fqn1, "Second and third array should both be strings or arrays.");
            }

            if (keys.length !== vals.length) {
              exceptions.illegalArgument(fqn1, "Second and third argument should be equal in size.");
            }

            return _self.addArrays(namesp, keys, vals);
          } else {
            if (!string.is(vals) || !string.is(keys)) {
              exceptions.illegalArgument(fqn1, "Second and third array should both be strings or arrays.");
            }

            return _self.addStrings(namesp, keys, vals);
          }
        } else if (args.length === 2) {
          if (!object.is(obj, true)) {
            exceptions.illegalArgument(fqn2, "Second argument should be a simple key/value mapping object.");
          }

          return _self.addObject(namesp, obj);
        }

        exceptions.illegalNoOfArguments(fqn0, "2 or 3");
      },

      /**
       * Add translations with an array of keys and an array of values.
       * @param {String} namesp Namespace to place it in.
       * @param {Array} keys The keys to use.
       * @param {Array} vals The values to use.
       */
      addArrays: function(namesp, keys, vals) {
        /*global console:true */
        var _self = this,
            obj = object.createFromNS(namesp, function(obj) {
              array.each(keys, function(idx, item) {
                var firstPart = "",
                    lastPart = "",
                    indexOfLastDot = 0;

                if (item.indexOf(".") < 0) {
                  obj[item] = vals[idx];
                } else {
                  indexOfLastDot = item.lastIndexOf(".");
                  firstPart = item.substring(0, indexOfLastDot);
                  lastPart = item.substring(indexOfLastDot + 1);

                  if (namesp === "") {
                    _self.addStrings(firstPart, lastPart, vals[idx]);
                  } else {
                    _self.addStrings(namesp + "." + firstPart, lastPart, vals[idx]);
                  }
                }
              });

              return obj;
            });

        _self.trans = object.extend(_self.trans, obj);
      },

      /**
       * Add translations with an object of key/value pairs.
       * @param {String} namesp Namespace to place it in.
       * @param {Object} obje The object containing the key/value pairs or a deeper nested structure.
       */
      addObject: function(namesp, obje) {
        var _self = this,
            obj = object.createFromNS(namesp, function(obj) {
              return obje;
            });

        _self.trans = object.extend(_self.trans, obj);
      },

      /**
       * Add translation with a key and value into a certain namespace.
       * @param {String} namesp Namespace to place it in.
       * @param {String} key The key.
       * @param {String} val The value.
       */
      addStrings: function(namesp, key, val) {
        var _self = this,
            obj = object.createFromNS(namesp, function(obj) {
              obj[key] = val;
              return obj;
            });

        _self.trans = object.extend(_self.trans, obj);
      },

      /**
       * @see i18n.get
       */
      get: function() {
        var _self = this,
            result = "",
            args = Array.prototype.slice.call(arguments[0], 0),
            key = args[0],
            regex = false;

        // Remove the namespace from the arguments.
        args.splice(0, 1);

        if (!string.is(key)) {
          exceptions.illegalArgument("core.i18n.get(key, ...)", "First argument should be of type string.");
        }

        if (key === '') {
          exceptions.illegalArgument("core.i18n.get(key, ...)", "Please specify the key.");
        }

        result = _self.getTranslations(key);

        if (string.is(result)) {
          if (args.length > 1) {
            // Substitute all the {n} with their respective variable.
            core.array.each(args, function(idx, item) {
              var newVal = '',
                  strToRepl = '';

              strToRepl = "{" + idx + "}";

              if (item) {
                newVal = item;
              } else {
                newVal = '';
              }

              result = result.replace(strToRepl, newVal, 'gi');
            });
          }

          // Remove remaining {[0-...]}.
          regex = /\{[0-9]+\}/gi;
          result = result.replace(regex, '');

          return result;
        } else {
          return "";
        }
      },

      /**
       * @see core.i18n.getTranslations
       */
      getTranslations: function(namesp) {
        var _self = this,
            np = [],
            obj = _self.trans,
            found = true;

        if (!string.is(namesp)) {
          exceptions.illegalArgument("core.i18n.getTranslations(namespace)", "First argument should be of type string.");
        }

        np = namesp.split('.');

        array.each(np, function(idx, item) {
          if (obj && obj[item]) {
            obj = obj[item];
          } else {
            return;
          }
        });

        return obj;
      },
      
      /**
       * @see core.i18n.remove
       */
      remove: function(namesp, keys) {
        var _self = this,
            fqn0 = "core.i18n.add(namespace, key)",
            fqn1 = "core.i18n.add(namespace, keys)",
            counter = 0;

        if (!string.is(namesp)) {
          exceptions.illegalArgument("core.i18n.getTranslations(namespace)", "First argument should be of type string.");
        }
        
        if (!array.is(keys) && !string.is(keys)) {
          exceptions.illegalArgument(fqn0, "Second argument should be of type array or string.");
        }

        if (string.is(namesp) && (array.is(keys) || string.is(keys))) {
          if (array.is(keys)) {
            array.each(keys, function(idx, item) {
              if (_self.trans[namesp] && _self.trans[namesp][item]) {
                delete _self.trans[namesp][item];
                counter = counter + 1;
              }
            });
          } else {
            if (_self.trans[namesp] && _self.trans[namesp][keys]) {
              delete _self.trans[namesp][keys];
              counter = counter + 1;
            }
          }
        }

        return counter ? true : false;
      }
    }
  });

  /*************************************
   *               Public              *
   *************************************/

  return core.object.extend(core, /** @lends core */ {
    _private: _core,

    i18n: /** @lends i18n */ {
      /**
      * @description Internationalization methods. All internationalized strings are accessible through this API.
      * @alias i18n
      * @namespace core
      */


      /**
       * Add (a) translation key(s) to the internationalization module.
       *
       * @param {String} namespace='' The namespace of the keys.
       * @param {Object/Array/String} keys An object containing the key/value mappings or an array with the keys or a single key.
       * @param {Array/String} [values] An array containing the values that map onto the keys array or a value that maps onto the single key.
       * @example
       * i18n.add('MyNamespace', 'MyKey', 'Some {0} Translation {1}');
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      add: function() {
        _core.i18n.add(arguments);
      }.defaults('', [], []),

      /**
       * Get the correct translation based on a key.
       *
       * @param {String} key='' The fully qualified key to seach. ex: [namespace].key
       * @param {String} [par1] The first substitution.
       * @param {String} [par2] The second substitution.
       * @param {String} [parN] The N-th substitution.
       * @returns {String} Returns an empty string if not present or the value that belongs to this key.
       * @example
       * i18n.get('MyNamespace.MyKey', 'New', 'Changed') //returns: "Some New Translation Changed"
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      get: function(key) {
        return _core.i18n.get.call(_core.i18n, arguments);
      }.defaults(''),

      /**
       * Get the correct translations based on a namespace.
       *
       * @param {String} namespace='' The namespace to get the translations from.
       * @returns {Object} Returns an empty object or an object filled with key/value pairs.
       * @example
       * i18n.get('MyNamespace'); //returns Object { MyKey="Some {0} Translation {1}"}
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      getTranslations: function(namespace) {
        return _core.i18n.getTranslations(namespace);
      }.defaults(''),

      /**
       * Remove translation key(s) from the module.
       *
       * @param {String} namespace The namespace of the keys.
       * @param {Array/String} keys An array of keys or a single key.
       * @returns {Boolean} Whether the keys (and their values) have been removed.
       * @example
       * i18n.remove('MyNamespace','MyKey');
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
       */
      remove: function(namespace, keys) {
        return _core.i18n.remove(namespace, keys);
      }.defaults('', [])
    }
  });
}(core));