/**
 * Regular notification.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
 * @module library/uielements/notifications
 * @alias Notification
 * @namespace controls/notifications
 */
/*global define,require,core */
define('library/uielements/notifications/notification',
       ['core', 'modules/content/notificationmanager'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      notificationManager = require('modules/content/notificationmanager');

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _notification = {
    TYPE_DEFAULT: 'TYPE_DEFAULT', // !TODO: Usually it's better to use numbers here, they're way faster in comparison.
    TYPE_SUCCESS: 'TYPE_SUCCESS',
    TYPE_WARNING: 'TYPE_WARNING',
    TYPE_ERROR: 'TYPE_ERROR'
  };

  /*************************************
   *               Public              *
   *************************************/

  var Notification = /** @lends Notification */{
      TYPE_DEFAULT: _notification.TYPE_DEFAULT,
      TYPE_SUCCESS: _notification.TYPE_SUCCESS,
      TYPE_WARNING: _notification.TYPE_WARNING,
      TYPE_ERROR: _notification.TYPE_ERROR,
      
      /**
       * Create notification.
       *
       * @param {Object} [options] Options for the notification.
       *   @option {String} type=TYPE_DEFAULT The type of the notification. Can be {@link Notification.TYPE_DEFAULT}, {@link Notification.TYPE_SUCCESS}, {@link Notification.TYPE_WARNING} or {@link Notification.TYPE_ERROR}.
       *   @option {String} content.title='' Title.
       *   @option {String} content.message='' Body of the notification. Can be HTML or text?
       *   @option {String} content.user='' Is there a user linked to this message? Supply the username if so. When no username is supplied no avatar will be displayed.
       *   @option {Boolean} more.enabled=false Is the more functionality enabled? 'What can I do?' for error and 'Show me the warnings.' for warning.
       *   @option {Function} more.onClick The function executed when clicked on the more function. When no function is given, the text will be displayed.
       *   @option {String} more.message Message displayed when clicked on the more message.
       *   @option {Boolean} sticky=false Is this message sticky?
       * @returns {NotificationObject} A notification object.
       * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
       * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
       */
      create: function(options) {
        return notificationManager.create(options);
      }.defaults({
        type: _notification.TYPE_DEFAULT,
        content: {
          title: '',
          message: '',
          user: ''
        },
        managed: true,
        sticky: false,
        more: {
          enabled: false
        }
      })
  };
    
  return Notification;
});

core.module.alias.add('Notification', 'library/uielements/notifications/notification');