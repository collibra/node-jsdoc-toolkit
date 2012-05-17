/**
 * Notification Manager.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
 * @module modules/content/notificationmanager
 * @alias NotificationManager
 * @namespace modules/content/notificationmanager
 */
/*global define,require,core */
define('modules/content/notificationmanager',
       ['core', 'library/uielements/shared/qtip'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      qtip = require('library/uielements/shared/qtip');

  /*************************************
   *              Private              *
   *************************************/


  /** @private */
  var _notificationManager = {
      
    notiboxId: 'notibox',
    notiId: 0,
    closeClass: 'icon-close-thin',
    
    /**
     * @constructor
     */
    
    QUEUE_MAX_SIZE: 10,
    
    _init: function() {
      // NOTES:
      // - When no notification are shown and no history is present, don't show anything.
      // - If at least 1 notification is shown, show the history and the helper text for about 2s.
    },
    
    /**
     * Get noti id.
     * @param {String} Id selector of notification.
     * @returns {String} Notification id.
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    getIntId: function(selector) {
      return selector.split('-')[1];
    },
    
    /**
     * Will be used later.
     */
    showG: function(id, html) {
      var qLen = this.queue.length,
          notiBox = core.dom.select('#' + this.notiboxId),
          elem = '',
          i = 0;
      
      for(i; i < qLen; i += 1) {
        elem = core.dom.select('#' + this.queue[i].selector);

        if (this.queue[i].selector === id) {
          // If is not already generated.
          if (!elem.length) {
            // Generate this elem.
            notiBox.prepend(this.queue[i].html);
          } else {
            // Only show this elem.
            elem.show();
          }
          this.queue[i].visible = true;
          continue;
        }
        // If should be visible.
        if (this.queue[i].visible) {
          // If is not already generated.
          if (!elem.length) {
            // Generate this elem.
            notiBox.prepend(this.queue[i].html);
          } else {
            // Only show this elem.
            elem.show();
          }
          this.queue[i].visible = true;
        }
      }
    },
    
    /**
     * Show specific notification.
     *
     * @param {NotificationObject} A notification object.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    showOne: function(notiObj) {
      var noti = core.dom.select('#' + notiObj.selector);
      if (noti.length) {
        noti.slideDown();
        notiObj.visible = true;
        return true;
      }
      
      return false;
    },
    
    /**
     * Hide specific notification.
     *
     * @param {String} id Notification id.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    hideOne: function(id) {
      var noti = core.dom.select('#' + id);
      if (noti.length) {
        noti.slideUp();
        return true;
      }
      
      return false;
    },
    
    /**
     * Queue of NotificationObjects.
     */
    queue: [],
    
    /**
     * @see NotificationManager.showNotifications.
     */
    showNotifications: function(x) {
      var i = 0;
      // If x is higher than queue length, show all notis, else show x notis.
      x = (x >= this.queue.length) ? this.queue.length : x;
      // Hide every notis.
      core.dom.select('#' + this.notiboxId).children().hide();
      // Show notis from queue.
      for(i; i < x; i += 1) {
        this.queue[i].show();
      }
      
      return true;
    },
    
    /**
     * @see NotificationManager.hideNotifications.
     */
    hideNotifications: function(x) {
      var i = this.queue.length - 1;

      // While we don`t hide x notifications, and while in queue are notis.
      while(x > 0 && i >=0) {
        // If noti is visible.
        if (core.dom.select('#' + this.queue[i].selector).is(':visible')) {
          // Hide noti.
          this.queue[i].hide();
          this.queue[i].visible = false;
          x -= 1;
        }
        i -= 1;
      }
      
      return true;
    },
    
    /**
     * @see NotificationManager.create.
     */
    create: function(options) {
        var tooltip = '',
            _self = this,
            wrapper = '<div id="' + this.notiboxId + '" class="notifications-wrapper"></div>',
            notibox = core.dom.select('#' + this.notiboxId),
            qlen = this.queue.length,
            notiSelector = 'noti-' + this.notiId,
            html = '',
            src = '',
            moreMessage = '',
            moreOnClick = null,
            template = core.template.create('<div id="<%= NOTISELECTOR %>" class="notification <%= TYPE %>" style="display:none"><div class="avatar-wrapper"><div class="avatar"><%= SRC %></div><div class="separator"></div></div><div class="content-wrapper"><div class="title"><%= TITLE %></div><div class="close"><span class="<%= CLOSECLASS %>"></div><div class="content"><%= MESSAGE %></div>');
        
        switch (options.type) {
          case 'TYPE_DEFAULT':
            html = core.template.use(template, {NOTISELECTOR: notiSelector, TYPE : 'default', SRC: src, TITLE: options.content.title, CLOSECLASS: this.closeClass, MESSAGE: options.content.message}) + '</div></div>';
            break;
          case 'TYPE_SUCCESS':
            html = core.template.use(template, {NOTISELECTOR: notiSelector, TYPE : 'success', SRC: src, TITLE: options.content.title, CLOSECLASS: this.closeClass, MESSAGE: options.content.message}) + '</div></div>';
            break;
          case 'TYPE_ERROR':
            html = core.template.use(template, {NOTISELECTOR: notiSelector, TYPE : 'error', SRC: src, TITLE: options.content.title, CLOSECLASS: this.closeClass, MESSAGE: options.content.message}) + '<div class="separator"></div><div class="more"><span class="icon-question white"></span>What can I do?</div><div class="more-info">' + moreMessage + '</div></div></div>';
            break;
          case 'TYPE_WARNING':
            html = core.template.use(template, {NOTISELECTOR: notiSelector, TYPE : 'warning', SRC: src, TITLE: options.content.title, CLOSECLASS: this.closeClass, MESSAGE: options.content.message}) + '<div class="separator"></div><div class="more"><span class="icon-eye"></span>Show me the warnings.</div> <div class="more-info">' + moreMessage + '</div></div></div>';
            break;
          default: break;
        }
        
        if (!notibox.length) {
          core.dom.select('body').prepend(wrapper);
        }
        
        var no = {
          selector: notiSelector,
          visible: false,
          sticky: options.sticky,
          html: html,
          show: function() {
            _self.showOne(this);
          },
          hide: function() {
            _self.hideOne(this.selector);
          },
          showMoreMessage: function() {
            
          },
          hideMoreMessage: function() {
            
          }
          };
        
        // When queue is full, pop last elem from queue array, and remove from the DOM.
        if (qlen >= this.QUEUE_MAX_SIZE) {
          var delNoti = this.queue.pop();
          core.dom.select('#' + delNoti.selector).remove();
        }
        
        // Prepend new noti.
        core.dom.select('#' + this.notiboxId).prepend(html);
        
        if (options.more.enabled) {
          if (options.more.message) {
            core.dom.select('#' + this.notiboxId).find('.more-info').html(options.more.message);
          }
          
          // Default behaviour after click on more button.
          moreOnClick = function() {
            core.dom.select(this).next('.more-info').toggle();
          };
          
          // If onclick specyfied by the programmer, use it.
          if (core.func.is(options.more.onClick)) {
            moreOnClick = options.more.onClick;
          }
          
          // Attach onClick event.
          var moreBtn =  core.dom.select('#notibox').find('.more');
          moreBtn.on('click', moreOnClick);
        }
        
        this.notiId += 1;
        
        // If user avatar specified.
        if (options.content.user) {
          src = '<img src="' + options.content.user + '" alt="" title="" />';
          core.dom.select('#' + notiSelector + ' .avatar').append(src);
          core.dom.select('#' + notiSelector + ' .avatar-wrapper').show();
          core.dom.select('#' + notiSelector).addClass('with-avatar');
        }
        
        if (!options.sticky) {
          // Hide noti after few seconds.
          setTimeout(function() {
            no.hide(notiSelector);
            no.visible = false;
          }, 2000);
        }
        
        this.queue.unshift(no);
        no.show();
        var closeButton = core.dom.select('#notibox').find('.' + this.closeClass);
        
        // Attach onClick event for closeButton.
        closeButton.on('click', function() {
          _self.hideOne(core.dom.select(this).parents('.notification').attr('id'));
        });
        
        closeButton.hover(function() {
          core.dom.select(this).addClass('black');
        },function() {
          core.dom.select(this).removeClass('black');
        });
        
        return no;
      }
  };

  _notificationManager._init();

  /*************************************
   *               Public              *
   *************************************/

  var NotificationManager = /** @lends NotificationManager */{
    /**
     * @private
     */
    _private: _notificationManager,
    
    /**
     * Hide the entire notification manager. Including more and history feature.
     *
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    hide: function() {
      // !TODO: Implement.
    },

    /**
     * Hide the x visible notifications.
     *
     * @param {Number} x The number of eldest notifications to hide. If 0, it will hide all notifications visible.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    hideNotifications: function(x) {
      return _notificationManager.hideNotifications(x);
    }.defaults(0),

    /**
     * Show the entire notification manager. Including more and history feature.
     *
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    show: function() {
      // !TODO: Implement.
    },

    /**
     * Show the last x number of notifications.
     *
     * @param {Number} x The number of notifications to show.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    showNotifications: function(x) {
      return _notificationManager.showNotifications(x);
    }.defaults(5),
    
    /**
     * Create one notification.
     *
     * @returns {NotificationObject} A notification object.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    create: function(options) {
      return _notificationManager.create(options);
    }
  };
    
  return NotificationManager;
});

core.module.alias.add('NotificationManager', 'modules/content/notificationmanager');
core.module.use('NotificationManager');