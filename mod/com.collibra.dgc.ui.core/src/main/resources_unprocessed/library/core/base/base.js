/**
 * Core :: Libraries
 *
 * Copyright Collibra 2011
 * @author Clovis Six <clovis@collibra.com>
 */
/*global domqueue,alert,require */
var core = (function(core) {
  "use strict";

  /*************************************
  *              Private              *
  *************************************/

  /** @private */
  var $ = core._private.libs.libs.jquery,
      exceptions = core.debug.exceptions;

  /** @private */
  var _core = core.object.extend(core._private, {
    array: {
      /**
       * @see core.array.contains
       */
      contains: function(arr, el) {
        return ($.inArray(el, arr) >= 0);
      }
    },

    dom: {
      jqache: false,

      /**
       * Constructor.
       */
      _init: function() {
        var _self = this;

        core.array.each(domqueue, function(callback) {
          _self.ready(callback);
        });
      },

      /**
       * @see core.dom.animate
       */
      animate: function(selector, properties, options) {
        if (!this.is(selector)) {
          selector = $(selector);
        }

        return selector.stop().animate(properties, options);
      },

      /**
       * @see core.dom.create
       */
      create: function(element, parent) {
        if (!this.is(element)) {
          element = $(element);
        }

        if (parent) {
          element.appendTo(parent);
        }

        return element;
      },

      /**
       * @see core.dom.each
       */
      each: function(selector, callback) {
        var _self = this,
            i = 0, len = 0,
            items = _self.select(selector);

        len = items.length;

        for (i = 0; i < len; i = i + 1) {
          if (items.hasOwnProperty(i)) {
            callback(_self.select(items[i]), i);
          }
        }

        return items;
      },

      /**
       * @see core.dom.generateID
       */
      generateID: function(selector, options) {
        var _self = this,
            ids = [];

        _self.each(selector, function(item) {
          var time = new Date().getTime(),
              id = options.prefix + time.toString() + Math.floor(Math.random() * 100).toString() + options.suffix;

          if (options.changeDOM) {
            item.attr('id', id);
          }

          ids.push(id);
        });

        return ids;
      },

      /**
       * @see core.dom.getOptions
       */
      getOptions: function() {
        var _self = this;

        return {
          selection: {
            cache: _self.jqache
          }
        };
      },

      /**
       * @see core.dom.is
       */
      is: function(element) {
        return (element !== undefined && element.jquery && element.length > 0);
      },

      /**
       * @see core.dom.ready
       */
      ready: function(callback) {
        if (core.func.is(callback)) {
          $(callback);
        }
      },

      /**
       * @see core.dom.select
       */
      select: function(selector, enableCache) {
        if (selector === undefined || selector === null) {
          return $();
        }

        if (!selector.jquery) {
          selector = (enableCache) ? $.q(selector) : $(selector);
        }

        return selector;
      },

      /**
       * @see core.dom.scrollTo
       */
      scrollTo: function(element, target, options) {
        element = core.dom.select(element);

        if (this.is(element)) {
          element.scrollTo(target, options);
        }
      },

      /**
       * @see core.dom.setOptions
       */
      setOptions: function(options) {
        var _self = this;

        if (core.boolean.is(options.selection.cache)) {
          _self.jqache = options.selection.cache;

          return true;
        }

        return false;
      },

      /**
       * @see core.dom.sort
       */
      sort: function(elements, sortFunction) {

        if (!core.array.is(elements)) { return; }

        core.array.each(elements.sort(sortFunction), function(index, item) {
          item.appendTo(elements[0].parent());
        });
      },

      /**
       * @see core.dom.sortChildren
       */
      sortChildren: function(selector, sortFunction) {
        var el = _core.dom.select(selector),
        children = el.children();
        children.sort(sortFunction).appendTo(el);
      }
    },

    event: {
      /**
       * @see core.event.blur
       */
      blur: function(el, blur) {
        if (!el.jquery) {
          el = $(el);
        }

        return el.blur(blur);
      },

      /**
       * @see core.event.hover
       */
      hover: function(el, hoverIn, hoverOut) {
        if (!el.jquery) {
          el = $(el);
        }

        return el.hover(hoverIn, hoverOut);
      },

      /**
       * @see core.event.ready
       */
      ready: function(el, callback) {
        if (!el.jquery) {
          el = $(el);
        }

        $(callback);
        return el;
      }
    },

    func: {
      /**
       * @see core.func.is
       */
      is: function(func) {
        return $.isFunction(func);
      }
    },

    object: {
      /**
       * @see core.object.isPlain
       */
      isPlain: function(obj) {
        return $.isPlainObject(obj);
      }
    },

    ajax: {

      /**
       * Max requests number created by ajax manager at a time.
       * This sets number of active(currently in progress) ajax requests in the queue.
       */
      MAX_REQUESTS: 1,

      /**
       * Max requests numbers for browser(engine, browser, version)
       * First engine is checked, then browser is checked, then version.
       * If theres a problem with recognition during this 3 steps, the default value for each
       * step is taken.
       * (This needs to stay up to date as new versions of browsers come out.)
       * Using http://www.browserscope.org
       */
      MAX_REQUESTS_ARR: {
        "default":      1,
        "gecko": {
          "default":    2,
          "firefox": {
            "default":  6
          },
          "camino": {
            "default":  2,
            "1":        2,
            "2":        6
          }
        },
        "webkit": {
          "default":    2,
          "chrome": {
            "default":  6
          },
          "safari": {
            "default":  4,
            "5":        6,
            "6":        5,
            "7":        6
          }
        },
        "trident": {
          "ie": {
            "default":  6,
            "6":        2,
            "7":        2,
            "8":        6,
            "9":        6,
            "10":       8
          }
        },
        "presto": {
          "default":    4,
          "opera": {
            "default":  4,
            "8":        8,
            "9":        4,
            "10":       8,
            "11":       6
          }
        }
      },

      /**
       * @see core.ajax.call
       */
      call: function(url, data, options) {
        core.object.extend(options, {
          url: url,
          data: data,
          abort: options.onAbort,
          beforeCreate: options.onRequest,
          complete: options.onComplete,
          error: options.onError,
          success: options.onSuccess
        });
        options.manager.add(options);
      },

      /**
       * @see core.ajax.clearQueue
       */
      clearQueue: function(manager, should_abort) {
        manager = manager || _core.REST.getManager();
        manager.clear(should_abort);
      },

      /**
       * @see core.ajax.clearCache
       */
      clearCache: function(call_id, manager) {
        manager = manager || _core.REST.getManager();
        manager.clearCache();
      },

      /**
       * @see core.ajax.abortAll
       */
      abortAll: function(manager) {
        manager = manager || _core.REST.getManager();
        manager.abort();
      },

      /**
       * @see core.ajax.abort
       */
      abort: function(call_id, manager) {
        manager = manager || _core.REST.getManager();
        manager.abort(call_id);
      },

      /**
       * @see core.ajax.createManager
       */
      createManager: function(manager_name, options) {
        options.abort = options.onAbort;
        options.beforeCreate = options.onBeforeCreate;
        return $.manageAjax.create(manager_name, options);
      }.defaults('', {
        onAbort: function(){},
        onBeforeCreate: function(){}
      }),

      /**
       * Helper function for creating unique name for manager.
       */
      generateUniqueName: function(prefix) {
        return prefix + new Date().getTime().toString() + Math.floor(Math.random() * 1500).toString();
      },

      /**
       * Get the number of max concurrent requests that can be made on user current browser.
       *
       * @param {Object} detected Browser detection object: core.detect.browser.
       * @returns {Integer} Returns number of max requests.
       * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       * @private
       */
      setBrowserMaxRequests: function(detected) {
        var engine = null,
            browser = null;

        //failsafe fallback
        if (!detected) {
          return false;
        }

        //check if we recognize user engine
        if (_core.ajax.MAX_REQUESTS_ARR.hasOwnProperty(detected.engine)) {
          //we found engine, lets descend
          engine = _core.ajax.MAX_REQUESTS_ARR[detected.engine];
          if (engine.hasOwnProperty(detected.browser)) {
            //we found browser, lets descend
            browser = engine[detected.browser];
            if (browser.hasOwnProperty(detected.majorVersion)) {
              //we found version, lets use version max requests number.
              _core.ajax.MAX_REQUESTS = browser[detected.majorVersion];
            //version is not recognized, use browser default
            } else {
              _core.ajax.MAX_REQUESTS = browser["default"];
            }
          //browser is not recognized, use engine default
          } else {
            _core.ajax.MAX_REQUESTS = engine["default"];
          }
        //engine is not recognized, use global default
        } else {
          _core.ajax.MAX_REQUESTS = _core.ajax.MAX_REQUESTS_ARR["default"];
        }
        
        //reasign because it was assigned earlier and now the value is correct.
        core.AJAX.MAX_REQUESTS = _core.ajax.MAX_REQUESTS;
        core.REST.getManager().opts.maxRequests = core.AJAX.MAX_REQUESTS;
      }
    },

    REST: {

      /**
       * Current manager that is used.
       * Can be fetched using core.REST.getManager().
       */
      current_manager: null,

      _init: function() {
        //create the default REST ajax manager
        _core.REST.current_manager = _core.ajax.createManager("defaultManager");
      },
      
      handleHttpStatus: function(data, origin) {
        var pre = (origin ? "XHR Warning " : "XHR Error ") +data.status+" ",
            msg = $.parseJSON(data.responseText);
        
        try {
          switch(data.status) {
            case 500: throw {title: "Bad request", message: msg.message};
            case 401: throw {title: "No permissions. Log in.", message: msg.message};
            case 400: throw {title: "Bad request", message: msg.message};
            case 404: throw {title: "Not found", message: msg.message};
            case 204: throw {title: "No content", message: msg.message};
          }
        } catch(err) {
          core.module.use("library/uielements/notifications/notification", function(notification) {
            notification.create({
              type: notification.TYPE_ERROR,
              content: err,
              sticky: true,
              more: {
                enabled: false
              }
            });
          });
          core.debug.show(err.title + ": " + err.message, 'error');
        }
      },

      /**
       * @see core.REST.create
       */
      create: function(url, data, options) {
        var defaults = {
          headers: { "method": "PUT"},
          type: "POST",
          url: url,
          data: {data: data},
          beforeCreate: options.onRequest,
          abort: options.onAbort,
          complete: options.onComplete,
          //REST call is successful
          success: function(data, textStatus, jqXHR) {
            if (data.success || true) { //true here only for debuging purposes
              //REST call is valid and successful(on the backend side)
              //call user defined callback onSuccess
              if (options.onSuccess) {
                options.onSuccess.apply(this, arguments);
              }
              
              _core.REST.handleHttpStatus(data);
            } else {
              //REST call is invalid and/or wasnt successful(on the backend side)
              //call user defined callback onError
              if (options.onError) {
                options.onError.apply(this, arguments);
                
                //Parse XHR status and throw appropriate error.
                _core.REST.handleHttpStatus(data);
              }
            }
          },
          //REST call failed
          error: function(data, textStatus, jqXHR) {
            //call user defined callback onError
            if (options.onError) {
              options.onError.apply(this, arguments);
              
              //Parse XHR status and throw appropriate error.
              _core.REST.handleHttpStatus(data);
            }
          }
        };
        _core.REST.getManager().add(core.object.extend({}, defaults));
      },

      /**
       * @see core.REST.get
       */
      get: function(url, data, options) {
        var defaults = {
          dataType: "json",
          type: "GET",
          url: url,
          data: data,
          beforeCreate: options.onRequest,
          abort: options.onAbort,
          complete: options.onComplete,
          //REST call is successful
          success: function(data, textStatus, jqXHR) {
            if ((data && data.success) || true) {
              //REST call is valid and successful(on the backend side)
              //call user defined callback onSuccess
              if (options.onSuccess) {
                options.onSuccess.apply(this, arguments);
              }
              
            } else {
              //REST call is invalid and/or wasnt successful(on the backend side)
              //call user defined callback onError
              if (options.onError) {
                options.onError.apply(this, arguments);
                
                //Parse XHR status and throw appropriate error.
                
              }
            }
          },
          //REST call failed
          error: function(data, textStatus, jqXHR) {
            //call user defined callback onError
            if (options.onError) {
              options.onError.apply(this, arguments);
              
              //Parse XHR status and throw appropriate error.
              _core.REST.handleHttpStatus(jqXHR);
            }
          }
        };
        _core.REST.getManager().add(core.object.extend({}, defaults));
      },

      /**
       * @see core.REST.update
       */
      update: function(url, data, options) {
        var defaults = {
          dataType: "json",
          headers: { "METHOD": "UPDATE"},
          type: "POST",
          url: url,
          data: data,
          beforeCreate: options.onRequest,
          abort: options.onAbort,
          complete: options.onComplete,
          //REST call is successful
          success: function(data, textStatus, jqXHR) {
            if (data.success || true) {
              //REST call is valid and successful(on the backend side)
              //call user defined callback onSuccess
              if (options.onSuccess) {
                options.onSuccess.apply(this, arguments);
              }
              
            } else {
              //REST call is invalid and/or wasnt successful(on the backend side)
              //call user defined callback onError
              if (options.onError) {
                options.onError.apply(this, arguments);
                
                //Parse XHR status and throw appropriate error.
                _core.REST.handleHttpStatus(data);
              }
            }
          },
          //REST call failed
          error: function(data, textStatus, jqXHR) {
            //call user defined callback onError
            if (options.onError) {
              options.onError.apply(this, arguments);
            
              //Parse XHR status and throw appropriate error.
              _core.REST.handleHttpStatus(data);
            }
          }
        };
        _core.REST.getManager().add(core.object.extend({}, defaults));
      },

      /**
       * @see core.REST.remove
       */
      remove: function(url, data, options) {
        var defaults = {
          dataType: "json",
          type: "DELETE",
          url: url,
          data: {data: data},
          beforeCreate: options.onRequest,
          abort: options.onAbort,
          complete: options.onComplete,
          //REST call is successful
          success: function(data, textStatus, jqXHR) {
            //It seems that DELETE http request doesn't return anything, so this validation check
            //need to be more sophisticated.
            if (data || true) {
              //REST call is valid and successful(on the backend side)
              //call user defined callback onSuccess
              if (options.onSuccess) {
                options.onSuccess.apply(this, arguments);
              }
              
            }
          },
          //REST call failed
          error: function(data, textStatus, jqXHR) {
            //call user defined callback onError
            if (options.onError) {
              options.onError.apply(this, arguments);
              
              //Parse XHR status and throw appropriate error.
              _core.REST.handleHttpStatus(data);
            }
          }
        };
        _core.REST.getManager().add(core.object.extend({}, defaults));
      },

      /**
       * @see core.REST.getManager
       */
      getManager: function() {
        return _core.REST.current_manager;
      },
      
      /**
       * @see core.REST.setManager
       */
      setManager: function(manager) {
        return _core.REST.current_manager = manager;
      }
    }
  });

  _core.dom._init();
  _core.REST._init();

  /*************************************
  *               Public              *
  *************************************/

  return core.object.extend(core, /** @lends core */ {
    /**
     * Return the private part of the core.
     * This can be used by the other parts that will
     * be merged with the core.
     * @public
     * @augments
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    _private: _core,

    AJAX: /** @lends AJAX */ {
      /**
       * @namespace core
       * @alias AJAX
       * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */

      /**
       * The maximum number of concurrent requests possible for the current browser.
       * (This value is reassigned in _core.ajax.setBrowserMaxRequests, because core.ajax is created
       * before core.detect and through detection this is determined. Changing this value here is futile.)
       */
      MAX_REQUESTS: 1,

      /**
       * Aborts an AJAX request.
       *
       * @param {Integer} callID Id of the ajax request.
       * @param {AJAX Manager} [manager] The AJAX manager this request belongs to.
       * @returns {Boolean} True on success and false on failure.
       */
      abort: function(callID, manager) {
        _core.ajax.abort(callID, manager);
      }.defaults(0, _core.REST.getManager()),

      /**
       * Aborts all AJAX requests.
       *
       * @param {AJAX Manager} [manager] The AJAX manager this request belongs to.
       * @returns {Boolean} True on success, false on failure.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      abortAll: function(manager) {
        _core.ajax.abortAll(manager);
      }.defaults(_core.REST.getManager()),

      /**
       * Make AJAX call.
       *
       * @param {String} url The url.
       * @param {Object} data The data to be send.
       * @param {Object} options The options for the AJAX request.
       *   @option {Function} onSuccess The callback on success.
       *   @option {Function} onError The callback on error.
       *   @option {Function} onComplete The callback on complete.
       *   @option {Function} onRequest The callback right before the request is made.
       *   @option {Function} onAbort The callback on request abort.
       *   @option {Function} async=true Whether this request is made asynchronously or not.
       *   @option {Function} dataType='json' The type of data that you're expecting back from the server.
       *   @option {AJAX Manager} [manager] The AJAX manager this request belongs to.
       * @returns {Boolean} True on success, false on failure.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      call: function(url, data, options) {
        _core.ajax.call(url, data, options);
      }.defaults("", {data:{}}, {
        /** @ignore */
        onSuccess: function(){},
        /** @ignore */
        onError: function(){},
        /** @ignore */
        onComplete: function(){},
        /** @ignore */
        onRequest: function(){},
        /** @ignore */
        onAbort: function(){},
        async: true,
        dataType: "json",
        manager: _core.REST.getManager()
      }),

      /**
       * Removes request data and return from (the current) AJAX manager cache.
       *
       * @param {Number} callID Id of the AJAX request.
       * @param {AJAX Manager} [manager] The AJAX manager this request belongs to.
       * @returns {Boolean} True on success, false on failure.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      clearCache: function(callID, manager) {
        return _core.ajax.clearCache(callID, manager);
      }.defaults(0, _core.REST.getManager()),

      /**
       * Clears the ajax queue of waiting requests.
       * If the shouldAbort parameter is true, all requests in proccess will be aborted, too.
       *
       * @param {Object} [manager] Previously created ajax manager instance.
       * @param {Boolean} [shouldAbort=true] Should we abort all requests in queue before clearing it?
       * @returns {Boolean} True on success, false on failure.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      clearQueue: function(manager, shouldAbort) {
        return _core.ajax.clearQueue(manager, shouldAbort);
      }.defaults(_core.REST.getManager(), true),

      /**
       * Create manager for the AJAX calls..
       *
       * @param {String} [uniqueName] Manager unique name. The name is autogenerated if not specified.
       * @param {Object} [options] Options for manager.
       *  @option {Function} onBeforeCreate='function(){}' Function that will be called, before the XHR-object is created. If you return false, the XHR wont be created.
       *  @option {Function} onAbort='function(){}' Callback, that will be called, if a XHR is aborted.
       *  @option {Number} maxRequests=4  Limits the number of simultaneous request in the queue. queue-option must be true or 'clear'.
       *  @option {Boolean} cacheResponse=false Caches the response data of succesfull responses (not the xhr-object!).
       *  @option {Boolean} async=true  Set call to be either asynchronous or synchronous.
       *  @option {Boolean} domCompleteTrigger=false Triggers the events uniqueName + "DOMComplete" and "DOMComplete" on the specified element.
       *  @option {Boolean} domSuccessTrigger=false Triggers the events uniqueName + "DOMSuccess" and "DOMSuccess" on the specified element.
       *  @option {Boolean} preventDoubleRequests=true Prevents multiple equal requests (compares url, data and type).
       *  @option {Boolean} queueDuplicateRequests=false Keeps duplicate requests in queue.
       *  @option {Number} cacheTTL=-1 Cache time to live.
       *  @option {Boolean/String} queue=true The queue-type specifies the queue-behaviour. The clear option clears the queue, before it adds a new ajax-task to the queue (similiar to: last in first out). // true, false, clear
       * @returns {object} Returns created AJAX manager object.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      createManager: function(uniqueName, options) {
        return _core.ajax.createManager(uniqueName, options);
      }.defaults(_core.ajax.generateUniqueName("manager-"), {
        abortIsNoSuccess: false,
        async: true,
        cacheResponse: false,
        cacheTTL: -1,
        domCompleteTrigger: false,
        domSuccessTrigger: false,
        maxRequests: _core.ajax.MAX_REQUESTS,
        onAbort: function() {},
        onBeforeCreate: function() {},
        preventDoubleRequests: true,
        queue: true,
        queueDuplicateRequests: false
      })
    },

    array: /** @lends array */ {
      /**
       * Does the array contain a certain element?
       *
       * @param {Array} arr The array to search.
       * @param {Object} el The element to search for.
       * @returns {Boolean} Whether or not the element is found.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      contains: function(arr, el) {
        return _core.array.contains(arr, el);
      }.defaults([], false),
      
      /**
       * Move an element of the array
       *
       * @param {Array} arr The array to search.
       * @param {Integer} old_index Old index.
       * @param {Integer} new_index New index.
       * @returns {Array} Changed array.
       * @author <a href="mailto:michal@collibra.com">Michal Hans</a>
       */
      moveElement: function(arr, old_index, new_index) {
        var arrLenght = arr.length;
        
        if (new_index === false) {
          new_index = arrLenght - 1;
        }
        if (new_index >= arrLenght) {
          var k = new_index - arrLenght;
          while (k + 1) {
            arr.push(undefined);
            k = k - 1;
          }
        }
        arr.splice(new_index, 0, arr.splice(old_index, 1)[0]);
        return arr;
      }.defaults([],0 , false)
    },

    dom: /** @lends dom */ {
      /**
       * @namespace core
       * @alias dom
       */

      /**
       * Animate an element on the page.
       * @param {CoreObj/Selector} selector The element to animate.
       * @param {Object} properties The CSS properties to modify to their respective values.
       * @param {Object} [options] The options of the animation.<br />
       * @option {String/Number} duration A string or number determining how long the animation will run..
       * @option {String} easing  A string indicating which easing function to use for the transition..
       * @option {Function} complete A function to call once the animation is complete..
       * @option {Function} step A function to be called after each step of the animation..
       * @option {Boolean} queue A Boolean indicating whether to place the animation in the effects queue. If false, the animation will begin immediately. As of jQuery 1.7, the queue option can also accept a string, in which case the animation is added to the queue represented by that string..
       * @option {Object} specialEasing A map of one or more of the CSS properties defined by the properties argument and their corresponding easing functions.
       */
      animate: function(selector, properties, options) {
        return _core.dom.animate(selector, properties, options);
      }.defaults('', {}, {}),

      /**
       * Create a new DOM element (and append it to an element).
       * @param {CoreObj/String} element The element to create in plain text or a CoreObj.
       * @param {CoreObj/Selector} parent The element in which to append it. The element will be added as the last child of the parent.
       * @returns {CoreObj} The element that was created.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      create: function(element, parent) {
        return _core.dom.create(element, parent);
      }.defaults('', ''),

      /**
       * Execute a function over a set of elements in the most performant way.
       * @param {CoreObj/Selector} selector The element(s) to iterate over.
       * @param {Function} func The function to execute on each element.
       * @returns {CoreObj} The core object containing all the elements the function is executed upon.
       */
      each: function(selector, func) {
        return _core.dom.each(selector, func);
      }.defaults('', function() {}),

      /**
       * Generate a unique ID for the elements.
       *
       * @param {CoreObj/Selector} selector The element(s) to give a unique ID.
       * @param  {Object} [options] The options.
       *   @option prefix="" Prefix for the ID(s).
       *   @option suffix="" Suffix for the ID(s).
       *   @option changeDOM=true Do you want to write the new ID to the element?
       * @returns {Array<String>} An array of ID(s).
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      generateID: function(selector, options) {
        return _core.dom.generateID(selector, options);
      }.defaults('', {
        prefix: '',
        suffix: '',
        changeDOM: true
      }),

      /**
       * Gets core.dom options
       *
       * @returns {Object} Options for DOM selection & manipulation.
       * <pre>
       * {
       *   selection: {
       *     cache: {Boolean} (Cache selections or not by default?) !!! Be careful, has glossary-wide impact and it can have unexpected results!
       *   }
       * }
       * </pre>
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      getOptions: function() {
        return _core.dom.getOptions();
      },

      /**
       * Is the element a real DOM element wrapped as a CoreObj.
       *
       * @param {Object} element The suspected DOM element.
       * @returns {Boolean} Whether the suspected DOM element is a DOM element.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      is: function(element) {
        return _core.dom.is(element);
      }.defaults(''),

      /**
       * Replacement of document.ready.
       * It also only executes when the core is fully loaded but is available at all time.
       * Even when core is not loaded yet.
       *
       * @param {Function} callback Callback function to be called when DOM is ready.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      ready: function(callback) {
        _core.dom.ready(callback);
      }.defaults(''),

      /**
       * Select a specific element.
       *
       * @param {Selector} selector The selection string/object.
       * @param {Boolean} [enableCache] Do you want to go look in the cache first? (Default value depends on config.)
       * @returns {CoreObj} The element wrapped as a CoreObj. When the selection was empty, an empty CoreObj will be handed back.
       */
      select: function(selector, enableCache) {
        return _core.dom.select(selector, enableCache);
      }.defaults('', _core.dom.jqache),

      /**
       * Sets options for _core.dom
       *
       * @param  {Object} [options] The options.
       *   @option {Object} selection.cache Use cache or not.
       * @returns {Boolean} Whether or not option was set.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
       */
      setOptions: function(options) {
        return _core.dom.setOptions(options);
      }.defaults({
        selection: {
          cache: false
        }
      }),

      /**
       * Scroll to a specific element on the page.
       * @param {CoreObj/Selector} element The element to scroll.
       * @param {CoreObj/Selector/String/Object} [target='0%'] Where to scroll to.
       *    The different options for target are:<br />
       *    <ul>
       *      <li>A number position (will be applied to all axes).</li>
       *      <li>A string position ('44', '100px', '+=90', etc) will be applied to all axes</li>
       *      <li>A CoreObj/DOM element (logically, child of the element to scroll)</li>
       *      <li>A string selector, that will be relative to the element to scroll ('li:eq(2)', etc)</li>
       *      <li>A hash { top:x, left:y }, x and y can be any kind of number/string like above.</li>
       *      <li>A percentage of the container's dimension/s, for example: 50% to go to the middle.</li>
       *      <li>The string 'max' for go-to-end.</li>
       *    </ul>
       * @param {Object} [settings] The settings.
       *   @option {String} axis='xy' Which axis must be scrolled, use 'x', 'y', 'xy' or 'yx'.
       *   @option {Number} duration=0 The OVERALL length of the animation.
       *   @option {String} easing='swing' The easing method for the animation.
       *   @option {Boolean} margin=false If true, the margin of the target element will be deducted from the final position.
       *   @option {Object/Number} offset=0 Add/deduct from the end position. One number for both axes or { top:x, left:y }.
       *   @option {Object/Number} over=0 Add/deduct the height/width multiplied by 'over', can be { top:x, left:y } when using both axes.
       *   @option {Boolean} queue=false If true, and both axis are given, the 2nd axis will only be animated after the first one ends.
       *   @option {Function} onAfter Function to be called after the scrolling ends.
       *   @option {Function} onAfterFirst If queuing is activated, this function will be called after the first scrolling ends.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      scrollTo: function(element, target, settings) {
        _core.dom.scrollTo(element, target, settings);
      }.defaults('BODY', '0%', {
        axis: 'xy',
        duration: 0,
        easing: 'swing',
        margin: false,
        offset: 0,
        over: 0,
        queue: false
      }),

      /**
       * Sort DOM elements.
       * @param {Array<CoreObj>} elements An array of CoreObj's to be sorted.
       * @param {Function} [sortFunction] The sort function. Format: sortfunction(a, b) {  Compare and return -1, 0 or 1. }
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      sort: function(elements, sortFunction) {
        _core.dom.sort(elements, sortFunction);
      }.defaults('', function(a, b) { return a.innerHTML > b.innerHTML ? 1 : -1; }),

      /**
       * Sort children elements of a DOM element.
       * @param {CoreObj/Selector} selector The parent element of which the children should be sorted.
       * @param {Function} [sortFunction] The sort function. Format: sortfunction(a, b) {  Compare and return -1, 0 or 1. }
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      sortChildren: function(selector, sortFunction) {
        _core.dom.sortChildren(selector, sortFunction);
      }.defaults('', function(a, b) { return a.innerHTML > b.innerHTML ? 1 : -1; })
    },

    detect: /** @lends detect */ {
      browser: /** @lends detect.browser */ {
        IE: $.browser.msie,
        VERSION: $.browser.version
      }
    },

    event: /** @lends event */ {
      /**
       * @namespace core
       * @alias event
       */

      /**
       * Attach a blur event to an element.
       * @param {CoreObj/Selector} el The element to attach the event to.
       * @param {Function} blur The blur callback.
       * @returns {CoreObj} The initial element with the blur event attached.
       */
      blur: function(el, blur) {
        return _core.event.blur(el, blur);
      }.defaults('', function() {}),

      /**
       * Attach a hover event to an element.
       * @param {CoreObj/Selector} el The element to attach the event to.
       * @param {Function} hoverIn The hover callback when mouse is over.
       * @param {Function} hoverOut The hover clalback when mouse is out.
       * @returns {CoreObj} The initial element with the hover event attached.
       */
      hover: function(el, hoverIn, hoverOut) {
        return _core.event.hover(el, hoverIn, hoverOut);
      }.defaults('', function() {}, function() {}),

      /**
       * Attach the ready event to an element. This event is fired when a certain
       * element is ready for JS/DOM manipulation.
       * @param {CoreObj/Selector} el The element to check for readiness.
       * @param {Function} ready The ready callback.
       * @returns {CoreObj} The initial element with the ready event attached.
       */
      ready: function(el, ready) {
        return _core.event.ready(el, ready);
      }.defaults(document, function() {})
    },


    object: /** @lends object */ {
      /**
       * @namespace core
       * @alias object
       * @augments
       */

      // Each deferred object has the following life:
      //   1. pending
      //   2. resolved or rejected

      /**
       * Create a deferred object.
       * @returns {Deferred Object} Returns a deferred object.
       */
      createDeferred: function() {
        return $.Deferred();
      }
    },

    REST: /** @lends REST */ {
      /**
       * @namespace core
       * @alias REST
       */

      /**
       * Make a REST POST call.
       *
       * @param {String} url The URL of the REST resource.
       * @param {Object} data The data to be send to the REST resource.
       * @param {Object} [options] Additional options.
       *   @option {Function} onSuccess The callback on success.
       *   @option {Function} onError The callback on error.
       *   @option {Function} onComplete The callback on complete.
       *   @option {Function} onRequest The callback right before the request is made.
       *   @option {Function} onAbort The callback on request abort.
       *   @option {Function} async=true Whether this request is made asynchronously or not.
       *   @option {Function} dataType='json' The type of data that you're expecting back from the server.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      create: function(url, data, options) {
        _core.REST.create(url, data, options);
      }.defaults('',  {data: {}}, {
          /** @ignore */
          onSuccess: function() {},
          /** @ignore */
          onError: function() {},
          /** @ignore */
          onComplete: function() {},
          /** @ignore */
          onRequest: function() {},
          /** @ignore */
          onAbort: function() {},
          async: true,
          dataType: "json"
        }),

      /**
       * Make a REST GET call.
       *
       * @param {String} url Url of the REST resource.
       * @param {Object} data The data to be send to the REST resource. This will be serialized and appended to GET request URL.
       * @param {Object} [options] Additional options.
       *   @option {Function} onSuccess The callback on success.
       *   @option {Function} onError The callback on error.
       *   @option {Function} onComplete The callback on complete.
       *   @option {Function} onRequest The callback right before the request is made.
       *   @option {Function} onAbort The callback on request abort.
       *   @option {Function} async=true Whether this request is made asynchronously or not.
       *   @option {Function} dataType='json' The type of data that you're expecting back from the server.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      get: function(url, data, options) {
        _core.REST.get(url, data, options);
      }.defaults('',  {data: {}}, {
        /** @ignore */
        onSuccess: function() {},
        /** @ignore */
        onError: function() {},
        /** @ignore */
        onComplete: function() {},
        /** @ignore */
        onRequest: function() {},
        /** @ignore */
        onAbort: function() {},
        async: true,
        dataType: "json"
      }),

      /**
       * Get the AJAX manager used for REST calls.
       *
       * @returns {AJAX Manager} manager The AJAX manager that should be used by default for the REST calls.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      getManager: function() {
        return _core.REST.getManager();
      },

      /**
       * Make a REST UPDATE call.
       *
       * @param {String} url Url of the REST resource.
       * @param {Object} data Data to be send to the REST resource.
       * @param {Object} [options] Additional options.
       *   @option {Function} onSuccess The callback on success.
       *   @option {Function} onError The callback on error.
       *   @option {Function} onComplete The callback on complete.
       *   @option {Function} onRequest The callback right before the request is made.
       *   @option {Function} onAbort The callback on request abort.
       *   @option {Function} async=true Whether this request is made asynchronously or not.
       *   @option {Function} dataType='json' The type of data that you're expecting back from the server.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      update: function(url, data, options) {
        _core.REST.update(url, data, options);
      }.defaults('',  {data: {}}, {
        /** @ignore */
        onSuccess: function() {},
        /** @ignore */
        onError: function() {},
        /** @ignore */
        onComplete: function() {},
        /** @ignore */
        onRequest: function() {},
        /** @ignore */
        onAbort: function() {},
        async: true,
        dataType: "json"
      }),

      /**
       * Make a REST DELETE call.
       *
       * @param {String} url Url of the REST resource.
       * @param {Object} data Data to be send to the REST resource.
       * @param {Object} [options] Additional options.
       *   @option {Function} onSuccess The callback on success.
       *   @option {Function} onError The callback on error.
       *   @option {Function} onComplete The callback on complete.
       *   @option {Function} onRequest The callback right before the request is made.
       *   @option {Function} onAbort The callback on request abort.
       *   @option {Function} async=true Whether this request is made asynchronously or not.
       *   @option {Function} dataType='json' The type of data that you're expecting back from the server.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      remove: function(url, data, options) {
        _core.REST.remove(url, data, options);
      }.defaults('',  {data: {}}, {
        /** @ignore */
        onSuccess: function() {},
        /** @ignore */
        onError: function() {},
        /** @ignore */
        onComplete: function() {},
        /** @ignore */
        onRequest: function() {},
        /** @ignore */
        onAbort: function() {},
        async: true,
        dataType: "json"
      }),

      /**
       * Set the AJAX manager used for REST calls.
       *
       * @param {AJAX Manager} manager The AJAX manager that should be used by default for the REST calls.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      setManager: function(manager) {
        return _core.REST.setManager(manager);
      }
    }
  });
}(core));
