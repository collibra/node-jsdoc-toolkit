/**
 * Advanced Buttons
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
 * @module library/uielements/notifications
 * @alias AdvancedButton
 * @namespace controls
 */
/*global define,require,core */
define('library/uielements/notifications/activity',
       ['core'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core');

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _notifications = {
    TYPE_DEFAULT: 'TYPE_DEFAULT',
    TYPE_SUCCESS: 'TYPE_SUCCESS',
    TYPE_WARNING: 'TYPE_WARNING',
    TYPE_ERROR: 'TYPE_ERROR',
    
    queue: {
      elems: [],
      addElem: function () {
        
      },
      removeElem: function () {
        
      },
      clear: function () {
        
      }
    },
    
    show: function (options) {
      
      return true;
    }
  };

  /*************************************
   *               Public              *
   *************************************/

  var Notifications = /** @lends Notifications */{
      TYPE_DEFAULT: _notifications.TYPE_DEFAULT,
      TYPE_SUCCESS: _notifications.TYPE_SUCCESS,
      TYPE_WARNING: _notifications.TYPE_WARNING,
      TYPE_ERROR: _notifications.TYPE_ERROR,

      // !TODO:
      // - New line between description and first param.
      // - Watch your punctuation! End a sentence with a dot.
      // - An indent of an option is 2 spaces, not 1. Every tab is exactly 2 spaces.
      // - It's 'returns'.
      // - If you are both responsibleAPI and responsibleImplementation, you can use author instead.
      // - Don't re-add qtip. Just include the module shared/qtip and add the plugin to the libs folder.
      //   Make sure you add qtip as a dependency in the properties file.
      
      /**
       * Shows notification.
       *
       * @param {Object} [options] Options for the notification.
       *   @option {String} type The type of the notification.
       *   @option {Object} content A content of the notification.
       *   @option {String} content.title A title of the notification.
       *   @option {String} content.message A text/html of the notification.
       *   @option {String} content.additionalMessage An additional message for types:error, warning.
       * @returns {Boolean} True on success and false on failure.
       * @responsibleAPI <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
       * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
       */
      show: function (options) {
        return _notifications.show(options);
      },
      
      /**
       * Shows messages from history.
       *
       * @returns {Boolean} True on success and false on failure
       * @responsibleAPI <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
       * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
       */
      showPastMessages: function () {
        
      },
      
      /**
       * Hide messages from history.
       *
       * @returns {Boolean} True on success and false on failure
       * @responsibleAPI <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
       * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
       */
      hidePastMessages: function () {
        
      }
  };
    
  return Notifications;
});

core.module.alias.add('Activity', 'library/uielements/notifications/activity');