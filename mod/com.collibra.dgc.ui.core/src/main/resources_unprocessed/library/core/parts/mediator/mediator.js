/**
 * Core :: Mediator
 *
 * Copyright Collibra 2012
 * @author Clovis Six <clovis@collibra.com>
 * @alias mediator
 * @namespace core
 */

var core = (function(core) {
  "use strict";

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var $ = core.libs.get('jquery');

  /** @private */
  var _core = core.object.extend(core._private, {
    mediator: {
      cache: {},
      backupCache: {},
      unsubscribed: {},

      /**
       * Wrapper function for done callbacks attached to the promises. This has to be done
       * or we cannot determine anymore whether a callback should be executed or if it
       * was already unsubscribed.
       *
       * @param {String} id The event ID.
       * @param {Function} callback The callback to attach to the promise.
       * @param {Arguments} args The arguments passed from the outer function.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      done: function(id, callback, args) {
        var removeIndex = 0,
            _self = this;

        args = args || [];

        if (_self.unsubscribed[id] && _self.unsubscribed[id].size > 0) {
          // We have to check for every callback if it is still attached...
          removeIndex = _self.unsubscribed[id].indexOf(callback);

          if (removeIndex < 0) {
            callback.apply(this, args);
          } else {
            _self.unsubscribed[id].remove(removeIndex);
          }
        } else {
          callback.apply(this, args);
        }
        
      },

      /**
       * @see core.mediator.getPromise
       */
      getPromise: function(id) {
        var _self = this,
            promise,
            deferred = _self.cache[id];

        if (deferred && !core.array.is(deferred)) {
          promise = deferred.promise();
        }

        return promise;
      },

      /**
       * @see core.mediator.promise
       */
      promise: function(id, deferred) {
        var _self = this,
            content = _self.cache[id],
            removeIndex = 0;

        deferred = deferred || $.Deferred();

        _self.cache[id] = deferred;

        if (core.array.is(content)) {
          // The content is an array of callbacks. Convert to promise...
          _self.cache[id] = deferred;
          core.array.each(content, function(callback) {
            deferred.done(function(args) {
              _self.done(id, callback, args);
            });
            
            //Backup each callback.
            _self.saveCallback(id, callback);
          });
        }
        
        return deferred;
      },

      /**
       * @see core.mediator.publish
       */
      publish: function(id, args) {
        var _self = this,
            content = _self.cache[id];

        if (!content) {
          return;
        }

        if (core.array.is(content)) {
          var currentCache = content,
              currentArgs = args || [],
              i = 0, j = currentCache.length;

          for (; i < j; i = i + 1) {
            currentCache[i].apply(this, currentArgs);
          }
        } else {
          // It's a promise!

          //That can be optimized by checking if this promise should be renewed.
          //Checking isResolved() is not enough though.
          content = _self.renewPromise(id);

          //Resolve a promise.
          //The first or renewed one.
          content.resolve(args);
          
          return content;
        }
      },

      /**
       * Renew promise. Technically it destroy current promise with all its handlers, then
       * create a new deferred object and attach all previously added callbacks to it.
       *
       * @param {String} id The event ID.
       * @return {Deferred} Return newly created promise with handlers attached.
       * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      renewPromise: function(id) {
        var _self = this;
        
        //Remove old promise.
        delete _self.cache[id];

        //Restore all id's callbacks so the new promise could collect it.
        if (_self.backupCache[id]) {
          _self.cache[id] = _self.backupCache[id].slice(0);
        }
        
        //Now create a new promise and return it.
        return _self.promise(id);
      },

      /**
       * Save callback in a backupCache for future use.
       * The same callback(reference check) cannot be added twice.
       *
       * @param {String} id The event ID.
       * @param {Function} callback The callback to attach to the promise.
       * @return {Deferred} Return newly created promise with handlers attached.
       * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
       */
      saveCallback: function(id, callback, primaryCache) {
        var _self = this,
            currentCache = null,
            i = 0, j = 0,
            found = false;
        
        if (!_self.backupCache[id]) {
          _self.backupCache[id] = [];
        }
        
        if(primaryCache) {
          currentCache = _self.cache[id];
          for (j = currentCache.length; i < j; i = i + 1) {
            //Kind of hack for comparing functions.
            //TODO! Re-check and test that!
            if ((""+currentCache[i]) === (""+callback)) {
              found = true;
            }
          }
        } else {
          currentCache = _self.backupCache[id];
          for (j = currentCache.length; i < j; i = i + 1) {
            if (currentCache[i] === callback) {
              found = true;
            }
          }
        }
        
        //Callback doesn't exist in backupCache, lets backup it then.
        if (!found) {
          currentCache.push(callback);
        }
      },

      /**
       * @see core.mediator.subscribe
       */
      subscribe: function(id, callback) {
        var _self = this,
            content = _self.cache[id];

        if (!content) {
          _self.cache[id] = [];
          content = [];
        }

        if (core.array.is(content)) {
          //It's not a promise, lets remember this callback.
          _self.saveCallback(id, callback, true);

        } else {

          _self.saveCallback(id, callback);
          //It's a promise
          _self.cache[id].done(function(args) {
            _self.done(id, callback, args);
          });

        }

        return [id, callback];
      },

      /**
       * @see core.mediator.unsubscribe
       */
      unsubscribe: function(handle) {
        var _self = this,
            id = handle[0],
            currentCache = false,
            currentBackupCache = false,
            i = 0, j = 0;

        if (_self.cache[id]) {
          currentCache = _self.cache[id];
          currentBackupCache = _self.backupCache[id];

          if (core.array.is(currentCache)) {
            for (j = currentCache.length; i < j; i = i + 1) {
              if (currentCache[i] === handle[1]) {
                currentCache.splice(i, 1);
                currentBackupCache.splice(i, 1);
              }
            }
          } else {
            if (!_self.unsubscribed[id]) {
              _self.unsubscribed[id] = [];
            }

            // We unsubscribe to a promise.
            _self.unsubscribed[id].push(handle[1]);
          }
        }
      },

      /**
       * @see core.mediator.unsubscribeAll
       */
      unsubscribeAll: function(id) {
        var _self = this;

        if (_self.cache[id]) {
          _self.cache[id] = [];
          _self.backupCache[id] = [];
        }
      }
    }
  });

  /*************************************
   *               Public              *
   *************************************/

  return core.object.extend(core, /** @lends core */ {
    _private: _core,

    mediator: /** @lends mediator */ {
      /**
       * Get a promise that was given to the mediator.
       * @param {String} id The id of the event.
       * @return {Promise} A promise (same as Deferred object but without the ability to move it's state).
       */
      getPromise: function(id) {
        return _core.mediator.getPromise(id);
      },

      /**
       * Add a promised event to the mediator.<br />
       * When the mediator contains callback functions for a certain id, the callbacks will be
       * transferred to the promise. When another promise is there, both will be merged.
       *
       * @param {String} id The id of the event.
       * @param {Deferred} [deferred] Add a custom deferred object. A new one will be created if this argument is not supplied.
       * @return {Deferred} The deferred object.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      promise: function(id, deferred) {
        return _core.mediator.promise(id, deferred);
      }.defaults('', false),

      /**
       * Publishes a series of values to an event and triggers it. In case the id is linked to a promise, it is resolved.
       * @param {String} id The id of the event.
       * @param {Array} args The values to publish.
       * @return {Deferred} The deferred object.
       * @example
       * core.mediator.publish('glossary:term:add', [term]);
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      publish: function(id, args) {
        return _core.mediator.publish(id, args);
      }.defaults('', []),

      /**
       * Subscribe to an event with a certain id and callback.
       * @param {String} id The id of the event.
       * @param {Function} callback The callback to execute when the event triggers.
       * @example
       * core.mediator.subscribe('glossary:term:add', function(ev) {
       *   console.log(ev);
       * });
       * @returns {Array} An array containing the id and the callback, called the handle. This handle can be used to unsubscribe.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      subscribe: function(id, callback) {
        return _core.mediator.subscribe(id, callback);
      }.defaults('', function() {}),

      /**
       * Unsubscribe from an event.
       * @param {Array} handle An array containing the id and the callback function.
       * @example
       * core.mediator.unsubscribe(['glossary:term:add', function(ev) {
       *   console.log(ev);
       * }]);
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      unsubscribe: function(handle) {
        _core.mediator.unsubscribe(handle);
      }.defaults(['', function() {}]),

      /**
       * Unsubscribes all handlers from a certain event.
       * @param {String} id The id of the event.
       * @example
       * core.mediator.unsubscribeAll('glossary:term:add');
       * @warning Be very prudent with this function. You never know for sure what other parts might be using this particular event.
       * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
       */
      unsubscribeAll: function(id) {
        _core.mediator.unsubscribeAll(id);
      }
    }
  });
}(core));
