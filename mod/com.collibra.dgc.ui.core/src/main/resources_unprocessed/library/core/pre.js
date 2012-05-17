/*global
console
*/

/**
 * First part of the core library. Contains basic functions and library management.
 *
 * Copyright Collibra 2012.
 * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
 */

var core = {};

/**
 * This way we can add defaults to a function:
 * var a = function(a, b) {
 *   // code
 * }.defaults(default_a, default_b);
 *
 * @private
 */
Function.prototype.defaults = function() {
  /*jshint noarg:false,strict:false */
  //"use strict";

  var _f = this,
      _l = _f.length - arguments.length,
      _a = new Array(_l>0?_l:0).concat(Array.prototype.slice.apply(arguments)),
      $ = false;

  return function() {
    var i = 0,
        args = Array.prototype.slice.apply(arguments).concat(_a.slice(arguments.length, _a.length));

    for (;i < args.length; i = i + 1) {
      if (typeof args[i] === 'object') {
        if (!$) {
          $ = core.libs.get('jquery');
        }

        if ($.isPlainObject(args[i])) {
          args[i] = $.extend(true, {}, _a[i], arguments[i]);
        }
      }
    }

    return _f.apply(_f, args);
  };
};

// Array Remove - By John Resig (MIT Licensed)
Array.prototype.remove = function(from, to) {
  "use strict";

  var rest = this.slice((to || from) + 1 || this.length);
  this.length = from < 0 ? this.length + from : from;
  return this.push.apply(this, rest);
};

// Array alphanumSort - By Brian Huisman (GNU LGPL Licensed)
Array.prototype.alphanumSort = function(caseInsensitive) {
  /*jshint eqeqeq:false,plusplus:false,strict:false,boss:true,nonew:false  */

  for (var z = 0, t; t = this[z]; z++) {
    this[z] = [];
    var x = 0, y = -1, n = 0, i, j;

    while (i = (j = t.charAt(x++)).charCodeAt(0)) {
      var m = (i == 46 || (i >=48 && i <= 57));
      if (m !== n) {
        this[z][++y] = "";
        n = m;
      }
      this[z][y] += j;
    }
  }

  this.sort(function(a, b) {
    for (var x = 0, aa, bb; (aa = a[x]) && (bb = b[x]); x++) {
      if (caseInsensitive) {
        aa = aa.toLowerCase();
        bb = bb.toLowerCase();
      }
      if (aa !== bb) {
        var c = Number(aa), d = Number(bb);
        if (c == aa && d == bb) {
          return c - d;
        } else {
          return (aa > bb) ? 1 : -1;
        }
      }
    }
    return a.length - b.length;
  });

  for (z = 0; z < this.length; z++) {
    this[z] = this[z].join("");
  }
};

/**
 * @ignore
 */
var core = (function(){
  "use strict";

  /*************************************
   *              Private              *
   *************************************/

  /**
   * @ignore
   */
  var _core = {
    array: {
      /**
       * @see core.array.each
       */
      each: function(arr, iterator) {
        if (!core.array.is(arr)) {
          core.debug.exceptions.illegalArgument("core.array.each(array, iterator)", "First argument should be of type array.");
        }

        if (!core.func.is(iterator)) {
          core.debug.exceptions.illegalArgument("core.array.each(array, iterator)", "Second argument should be of type function.");
        }

        var i = 0,
          l = arr.length,
          a = iterator.length;

        for (; i < l; i = i + 1) {
          if (a === 3) {
            iterator(i, arr[i], arr);
          } else if (a === 2) {
            iterator(i, arr[i]);
          } else {
            iterator(arr[i]);
          }
        }
      },

      /**
       * @see core.array.is
       */
      is: Array.isArray || function(arr) {
        return Object.prototype.toString.call(arr) === '[object Array]';
      }
    },

    debug: {
      // Is debugging mode enabled?
      enabled: false,
      // Level of debugging.
      level: 'all',
      // Different levels of debugging.
      levels: ['all', 'warning', 'error'],

      /**
       * Show debug message based on message type.
       * @param {String} msg Message to show in the console.
       * @param {String} type The type of the console message.
       */
      _show: function(msg, type) {
        if (!window.console) { return; }

        switch(type) {
          case 'error':
            console.error(msg);
            break;
          case 'warning':
            console.warn(msg);
            break;
          case 'info':
            console.info(msg);
            break;
          default:
            console.log(msg);
        }
      },

      /**
       * Change the level of debugging.
       * @private
       * @param {String} New level of debugging.
       */
      changeLevel: function(level) {
        var _self = _core.debug;

        if (_self.levels.indexOf(level) >= 0) {
          _self.level = level;
          _self.show("core :: debug :: Level changed to '" + level + "'.", 'info');
        } else {
          _self.show("core :: debug :: Level does not exist!", 'warning');
        }
      },

      /**
       * @see core.debug.disable
       */
      disable: function() {
        var _self = this;

        _self.enabled = false;
      },

      /**
       * @see core.debug.enable
       */
      enable: function() {
        var _self = this;

        _self.enabled = true;
      },

      exceptions: {
        /**
         * @see core.debug.exceptions.illegalArgument
         */
        illegalArgument: function(api, msg) {
          //throw api + ": illegal argument: " + msg;
        },

        /**
         * @see core.debug.exceptions.illegalNoOfArguments
         */
        illegalNoOfArguments: function(api, no) {
          //throw api + ": illegal number of supplied arguments. The following number is expected: " + no;
        }
      },

      /**
       * @see core.debug.show
       */
      show: function(msg, type) {
        var _self = _core.debug;

        if (window.console !== undefined && _self.enabled) {
          if (_self.level === 'all') {
            _self._show(msg, type);
          } else if (_self.level === 'warning') {
            if (type === 'warning' || type === 'error') {
              _self._show(msg, type);
            }
          } else {
            if (type === 'error') {
              _self._show(msg, type);
            }
          }
        }
      },

      /**
       * @see core.debug.stopExecution
       */
      stopExecution: function() {
        /*jshint debug:true */
        debugger;
      }
    },

    func: {
      /**
       * @see core.func.is
       */
      is: function(func) {
        return typeof func === 'function';
      }
    },

    libs: {
      // Contains all the libraries used.
      libs: {},

      /**
       * @see core.libs.add
       */
      add: function(name, lib, expose) {
        var _self = _core.libs;

        expose = typeof expose !== 'undefined' ? expose : true;

        _self.libs[name] = lib;

        if (expose) {
          core.libs[name] = lib;
        }

        _core.debug.show("core :: libs :: adding " + name);
      },

      /**
       * @see core.libs.get
       */
      get: function(name) {
        return this.libs[name];
      },

      /**
       * @see core.libs.list
       */
      list: function() {
        var li = '', i = 0;

        core.object.each(this.libs, function(idx, item) {
          if (i !== 0) {
            li = li + ', ';
          }

          li = li + idx;
          i = 1;
        });

        return li;
      },

      /**
       * @see core.libs.remove
       */
      remove: function(name) {
        var _self = _core.libs;

        delete _self.libs[name];
        delete core.libs[name];

        _core.debug.show("core :: libs :: removing " + name);
      }
    },

    object: {
      /**
       * @see core.object.createFromNS
       */
      createFromNS: function(ns, callback) {
        var fqm = "core.object.createFromNS(ns, callback)",
          nsArr = [],
          obj = {},
          cr = function(arr, obj) {
            if (arr.length < 1) {
              return obj;
            }

            var newObj = {};
            newObj[arr.pop()] = obj;

            return cr(arr, newObj);
          };

        if (!core.string.is(ns)) {
          core.debug.exceptions.illegalArgument(fqm, "The first argument should be of type string.");
        }

        if (callback && !core.func.is(callback)) {
          core.debug.exceptions.illegalArgument(fqm, "The second argument should be of type function.");
        }

        nsArr = ns.split('.');
        if (callback) {
          obj = callback(obj);
        }

        return cr(nsArr, obj);
      },

      /**
       * @see core.object.each
       */
      each: function(obj, func) {
        for (var prop in obj) {
          if (obj.hasOwnProperty(prop)) {
            func(prop, obj[prop]);
          }
        }
      },

      /**
       * @see core.object.extend
       */
      extend: function(oldObj, newObj) {
        var _self = _core.object;

        if (typeof _core.libs.libs.jquery !== 'undefined') {
          return _core.libs.libs.jquery.extend(true, oldObj, newObj);
        } else {
          return _self.ownExtend(oldObj, newObj);
        }
      },

      /**
       * @see core.object.is
       */
      is: function(obj, plain) {
        if (!core.boolean.is(plain)) {
          core.debug.exceptions.illegalArgument("_core.object.is(obj, plain)", "2nd argument should be a boolean.");
        }

        if (plain) {
          return _core.object.isPlain(obj);
        } else {
          return typeof obj === 'object';
        }
      },

      /**
       * @see core.object.isEmpty
       */
      isEmpty: function(obj) {
        if (obj) {
          for (var prop in obj) {
            if (obj.hasOwnProperty(prop)) {
              return false;
            }
          }
          
          return true;
        }
      },

      /**
       * Is this object a plain JS object?
       * @param {Object} The object to check.
       * @returns {Boolean} Is this object a plain object?
       */
      isPlain: function(obj) {
        /*jshint noempty:false */
        // Must be an Object.
        // Because of IE, we also have to check the presence of the constructor property.
        // Make sure that DOM nodes and window objects don't pass through, as well
        if (!obj || typeof obj !== "object" || obj.nodeType) {
          return false;
        }

        var hasOwn = Object.prototype.hasOwnProperty;

        try {
          // Not own constructor property must be Object
          if (obj.constructor &&
            !hasOwn.call(obj, "constructor") &&
            !hasOwn.call(obj.constructor.prototype, "isPrototypeOf") ) {
            return false;
          }
        } catch (e) {
          // IE8,9 Will throw exceptions on certain host objects #9897
          return false;
        }

        // Own properties are enumerated firstly, so to speed up,
        // if last one is own, then all properties are own.

        var key;
        for ( key in obj ) {}

        return key === undefined || hasOwn.call( obj, key );
      }.defaults(false),

      /**
       * Own extend function in case jQuery is not available yet.
       * @param {Object} The object to extend.
       * @param {Object} The new object to extend it with.
       * @returns {Object} New object that is a combination of both.
       */
      ownExtend: function(oldObj, newObj) {
        /*jshint forin:false */
        var options, name, src, copy, copyIsArray, clone,
          target = oldObj || {},
          _self = _core.object;

        // Only deal with non-null/undefined values
        if ((options = newObj) !== null) {
          // Extend the base object
          for (name in options) {
            src = target[name];
            copy = options[name];

            // Prevent never-ending loop
            if (target === copy) {
              continue;
            }

            // Recurse if we're merging plain objects or arrays
            if (copy && (_self.isPlain(copy) || (copyIsArray = copy instanceof Array))) {
              if (copyIsArray) {
                copyIsArray = false;
                clone = src && src instanceof Array ? src : [];
              } else {
                clone = src && _self.isPlain(src) ? src : {};
              }

              // Never move original objects, clone them
              target[name] = _self.ownExtend(clone, copy);
            } else if (copy !== undefined) {
              target[name] = copy;
            }
          }
        }

        // Return the modified object
        return target;
      },

      /**
       * @see core.object.size
       */
      size: function(obj) {
        var size = 0, key;
        for (key in obj) {
            if (obj.hasOwnProperty(key)) {
              size += 1;
            }
        }
        
        return size;
      }
    },

    sort: {
      comparators: {
        chunkify: function(t) {
          /*jshint boss:true,plusplus:false */

          var tz = [],
              x = 0, y = -1, n = 0, i, j;

          while (i = (j = t.charAt(x++)).charCodeAt(0)) {
            var m = (i === 46 || (i >= 48 && i <= 57));
            
            if (m !== n) {
              tz[++y] = "";
              n = m;
            }
            
            tz[y] += j;
          }

          return tz;
        },

        /**
         * @see core.sort.comparators.natural
         */
        natural: function(a, b, caseSensitive) {
          var aa, bb, x = 0;

          if (!caseSensitive) {
            a = a.toLowerCase();
            b = b.toLowerCase();
          }

          aa = this.chunkify(a);
          bb = this.chunkify(b);

          for (; aa[x] && bb[x]; x = x + 1) {
            if (aa[x] !== bb[x]) {
              var c = Number(aa[x]), d = Number(bb[x]);
              
              if (c === aa[x] && d === bb[x]) {
                return c - d;
              } else {
                return (aa[x] > bb[x]) ? 1 : -1;
              }
            }
          }

          return aa.length - bb.length;
        }
      }
    }
  };

  /*************************************
   *               Public              *
   *************************************/


  var core = /** @lends core */ {
    /**
     * Return the private part of the core.
     * This can be used by the other parts that will
     * be merged with the core.
     * @public
     * @augment
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    _private: _core,

    /**
     * The JavaScript core version.<br />
     * Format: [version]{[A/B/M/RC][version]}, A = Alpha, B = Beta, M = Milestone, RC = Release Candidate<br />
     * Example: 1.1M1<br />
     */
    VERSION: '0M2',


    array: /** @lends array */ {
      /**
       * Helper methods for arrays.
       * @alias array
       * @namespace core
       */

      /**
       * Iterate over the elements of an array.
       * @param {Array} arr An array to iterate over.
       * @param {Function} iterator The iterator function. function([idx,] item [,arr]) {}
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      each: function(arr, iterator) {
        _core.array.each(arr, iterator);
      }.defaults([], function() {}),

      /**
       * Check if type is array.
       * @param arr Argument to do a type check on.
       * @returns {Boolean} Is arr of type array?
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      is: function(arr) {
        return _core.array.is(arr);
      }.defaults(''),

      /**
       * Sort an array.
       * @param {Array} arr The array to sort.
       * @param {Boolean} reverse Reverses the order of the elements in an array (makes the last element first, and the first element last)
       * @param {Function} [iterator] The sort function. Format: sortfunction(a, b) {  Compare and return -1, 0 or 1. }
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      sort: function(arr, reverse, iterator) {
        arr.sort(iterator);
        if (reverse) {
          arr.reverse();
        }
      }.defaults([], false, undefined),

      /**
       * Sort the elements of the array based on a property.
       * @param {Array} arr The array to sort.
       * @param {Function} [selectionFunction] The selection function. Format: selectionfunction(a) { Return property to sort on as numeric value. }
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
       */
      sortBy: function(arr, selectionFunction) {
        
        arr.sort(function(a, b) {
          if (core.string.is(selectionFunction(a))){
            return selectionFunction(a) <= selectionFunction(b) ? -1 : 1;
          } else {
            return selectionFunction(a) - selectionFunction(b);
          }
        });
      }.defaults([], function(a) { return a; })
    },


    boolean: /** @lends boolean */ {
      /**
       * Helper methods for booleans.
       * @alias boolean
       * @namespace core
       */

      /**
       * Check if type is boolean.
       * @param bool Argument to do a type check on.
       * @returns {Boolean} Is bool of type boolean?
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      is: function(bool) {
        return typeof bool === 'boolean';
      }.defaults('')
    },

    debug: /** @lends debug */ {
      /**
       * Debugging methods and vars.
       * @alias debug
       * @namespace core
       */

      /**
       * Disable debugging.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      disable: function() {
        _core.debug.disable();
      },

      /**
       * Enable debugging.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      enable: function() {
        _core.debug.enable();
      },


      exceptions: /** @lends exceptions */ {
        /**
         * Exceptions handling.
         * @alias exceptions
         * @namespace core/debug
         */

        /**
         * Throw an illegal argument exception.
         * @param {String} api The fully qualified method name.
         * @param {String} msg A custom message you can show.
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        illegalArgument: function(api, msg) {
          _core.debug.exceptions.illegalArgument(api, msg);
        }.defaults("Unknown method", "Please check the supplied arguments for errors."),

        /**
         * Throw an illegal number of arguments exception.
         * @param {String} api The fully qualified method name.
         * @param {Number} no The number of arguments that should be supplied.
         * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
         */
        illegalNoOfArguments: function(api, no) {
          _core.debug.exceptions.illegalNoOfArguments(api, no);
        }.defaults("??", 1)
      },

      /**
       * Show a debugging message. This will show a message in the console when the debugging mode is enable. For this you need to either use Firefox or Google Chrome. The type of the message can be 'error', 'warning', 'info' or blank.
       * @param {String} msg Message to show in the console.
       * @param {String} [type] Type of the message to display.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      show: function(msg, type) {
        _core.debug._show(msg, type);
      },

      /**
       * Stop the execution of the JS code. Two step overs will get you back to the place where you called this method.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      stopExecution: function() {
        _core.debug.stopExecution();
      }
    },

    func: /** @lends func */ {
      /**
       * Helper methods for functions.
       * @alias func
       * @namespace core
       */

      /**
       * Check if type is a function.
       * @param func Argument to do a type check on.
       * @returns {Boolean} Is func of type function?
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      is: function(func) {
        return _core.func.is(func);
      }
    },

    libs: /** @lends libs */ {
      /**
       * External libraries.
       * @alias libs
       * @namespace core
       */

      /**
       * Add a new library to the core.
       * @param {String} name Name of the library.
       * @param {Object} lib Library.
       * @param {Boolean} [expose=false] Make the library available in core.libs.[name].
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      add: function(name, lib, expose) {
        _core.libs.add(name, lib, expose);
      }.defaults('', {}, false),

      /**
       * Get a certain library. (even when it's not exposed)
       * @param {String} name Name of the library.
       * @returns {Object} The library.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      get: function(name) {
        return _core.libs.get(name);
      }.defaults(''),

      /**
       * List all available libraries.
       * @returns {String} A list of libraries that you can use.
       */
      list: function() {
        return _core.libs.list();
      },

      /**
       * Remove a library from the core.
       * @param {String} name Name of the library.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      remove: function(name) {
        _core.libs.remove(name);
      }
    },

    object: /** @lends object */ {
      /**
       * Helper methods for objects.
       * @alias object
       * @namespace core
       */

      /**
       * Copy/clone an object.
       * @param {Object} obj The object to be copied.
       * @returns {Object} A completely new copy/clone of the object. (not a reference to)
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      copy: function(obj) {
        return _core.object.extend({}, obj);
      },

      /**
       * Convert a string namespace to a real one and apply the callback onto the most inner object.
       * @param {String} ns The namespace to convert into an object.
       * @param {Function} callback The function applied on the most inner object. function(obj) { return obj; }
       * @returns {Object} A converted object from the supplied namespace.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      createFromNS: function(ns, callback) {
        return _core.object.createFromNS(ns, callback);
      },

      /**
       * Execute a function over each member of this object (not the prototyp members).
       * @param {Object} obj The object the function should be iterated over.
       * @param {Function} func The function that should be executed for each object member.
       * @example
       * core.object.each(exampleObject, function(index, value) {
       *   console.log("Member: " + index + ", value: " + value);
       * });
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      each: function(obj, func) {
        _core.object.each(obj, func);
      }.defaults({}, function(item, val) {}),

      /**
       * Extend an object with new properties. (deep copy)
       * @param {Object} oldObj The object to extend.
       * @param {Object} newObj The new object to extend it with.
       * @return {Object} New object that is a combination of both.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      extend: function(oldObj, newObj) {
        return _core.object.extend(oldObj, newObj);
      },

      /**
       * Check if this object is a (plain) object.
       * @param {Object} obj Argument to do a type check on.
       * @param {Boolean} plain Check if it is a plain object or a general object. (Array is a general object, not a plain.)
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      is: function(obj, plain) {
        return _core.object.is(obj, plain);
      }.defaults(false, true),

       /**
       * Check if this object is empty(has no children, equals to {}).
       * @param {Object} obj Argument to do a type check on.
       * @return {Boolean} Return true if empty, false otherwise.
       * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      isEmpty: function(obj) {
        return _core.object.isEmpty(obj);
      }.defaults(false),

      /**
       * Get the size(length) of the object. This counts all the properties filtered using hasOwnProperty method.
       * Be aware that this is flat count. Doesn't count properties in sub-objects.
       * @param {Object} obj Argument to do a type check on.
       * @return {Boolean} Return size of the object.
       * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      size: function(obj) {
        return _core.object.size(obj);
      }.defaults(false)
    },

    string: /** @lends string */ {
      /**
       * Helper methods for strings.
       * @alias string
       * @namespace core
       */

      /**
       * Check if type is a string.
       * @param str Argument to do a type check on.
       * @returns {Boolean} Is str of type string?
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      is: function(str) {
        return typeof str === 'string';
      }.defaults(false)
    },

    sort: /** @lends sort */ {
      /**
       * Sorting iterator functions.
       * @alias sort
       * @namespace core
       */
      
      comparators: /** @lends sort.comparators */ {
        /**
         * Natural sorting comparator. This makes sure that "Term 100" comes after "Term 2" and not before.
         *
         * @param {String} a Left side of comparison.
         * @param {String} b Right side of comparison.
         * @param {Boolean} caseSensitive Is comparison case sensitive?
         * @param {Boolean} reverse Reverse the comparison?
         * @returns {Number} Result of comparison.
         */
        natural: function(a, b, caseSensitive, reverse) {
          var result = _core.sort.comparators.natural(a, b, caseSensitive);

          if (reverse) {
            result = -1 * result;
          }

          return result;
        }.defaults('', '', false, false)
      }
    }
  };

  return core;
}());
