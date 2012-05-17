/**
 * Menu.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
 * @module library/uielements/menu
 * @alias Menu
 * @namespace controls
 */
/*global define,require,core */
define('library/uielements/menu',
       ['core', 'library/uielements/radiocheck'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      RadioCheck = require('library/uielements/radiocheck');

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _menu = {
    menuClass: 'ui-menu',
    menuOverlayId: 'menu-bgoverlay',
    counter: 0,
    actions: {},
    actionsForSelection: {},
    actionsButtons: {},
    debugMode: false, // set true to show overlay and initiator field
    position: {
      x: 0,
      y: 0
    },
    menuItems: [],
    menuId: false,
    
    /**
     * @see Menu.create
     */
    create: function(initiator, menu, options) {
      var _self = this,
          newMenu = false,
          initiatorPosition = {},
          bg = core.dom.select('#' + _self.menuOverlayId),
          menuItemsActions = {},
          args = null;

      initiator = core.dom.select(initiator);
      initiator.addClass(_self.menuClass + '-initiator');
      initiatorPosition = initiator.offset();
      _self.menuItems = menu.slice(0);
      
      if (!bg.length) {
        bg = core.dom.create('<div id="' + _self.menuOverlayId + '">','BODY');
      }
      
      newMenu = core.dom.create(_self.generateMenuHtml(options),'BODY');
      _self.menuId = core.dom.generateID(newMenu, {prefix: 'ui-menu-'});
      initiator.attr('menuId', _self.menuId);
      newMenu.hide();
      
      // placement
      _self.setMenuPosition(newMenu, initiator);
      
      //store options
      newMenu.data("trigger", options.show.trigger);
      newMenu.data("showCallback", options.show.on);
      newMenu.data("hideCallback", options.hide.on);
      newMenu.data("menuItems", _self.menuItems);
      
//      TODO: Michal check this out !
      core.array.each(_self.menuItems, function(i, val) {
        if(val.action) {
          menuItemsActions[val.label] = val.action;
        }
      });
//      --
      initiator[0].actions = menuItemsActions;
      // events
      if (options.show.trigger === 'click') {
        // toggle on click
        initiator.off('click').on('click', function(ev) {
          _self.initToggleMenu(core.dom.select(this), function() {
          }, ev);
        });
      } else if (options.show.trigger === 'hover' || options.show.trigger === 'mouseover'|| options.show.trigger === 'mouseenter') {
        // show on mouse over
        initiator.off('mouseenter').on('mouseenter', function() {
          initiator.addClass("hover");
          setTimeout(function(){
            if (initiator.hasClass("hover")) {
              // do magic after delay
              _self.initOpenMenu(initiator, function(){
                // close on mouse out
                core.dom.select('#' + _self.menuOverlayId).one('mouseenter', function() {
                  _self.closeAllMenus();
                });
              });
            }
          }, options.show.delay);
        });
        
        initiator.mouseleave(function () {
          initiator.removeClass("hover");
        });
      }
      
      //assign action on a complete menu
      if(core.object.is(options.on)){
        core.object.each(options.on, function(actionType, actionFunction) {
          // assign function defined by user
          if (core.string.is(actionType) && core.func.is(actionFunction)) {
            newMenu.on(actionType, actionFunction);
          }
        });
      }
      
      //assign action on each item
      if(core.object.is(options.menuitem.on)){
        core.object.each(options.menuitem.on, function(actionType, actionFunction) {
          // assign function defined by user
          if (core.string.is(actionType) && core.func.is(actionFunction)) {
            newMenu.on(actionType, ".btn:not(.disabled)", actionFunction);
          }
        });
      }
      
      // assign actions
      core.object.each(_self.actions, function(index, actionsObject) {
        var item = newMenu.find('SPAN[data-itemName="' + index + '"]');
        if (item && actionsObject && !item.hasClass('disabled')) {
          core.object.each(actionsObject, function(actionType, actionFunction) {
            // assign function defined by user
            if (core.string.is(actionType) && core.func.is(actionFunction)) {
              item.on(actionType, actionFunction);
            }
          });
        }
      });
      
      // debug mode
      if (_self.debugMode) {
        bg.css({
          background: '#000',
          opacity: '0.67'
        });
        newMenu.css({
          background: 'red'
        });
      }
      
      // show submenu on mouse over
      newMenu.on('mouseenter', 'LI', function(){
        var submenu = core.dom.select(this).children('.ui-menu-items-wrapper'),
            submenuDefaultPosition;
        
        if(core.dom.is(submenu)) {
          submenu.css({
            left: core.dom.select(this).width()
          });
          
          submenu.show();
          
          // check is there enough space to show menu on the right side
          submenuDefaultPosition = submenu.offset();
          if (submenuDefaultPosition.left + submenu.width() > core.dom.select('BODY').width()) {
            submenu.css({
              left: -(core.dom.select(this).width())
            });
          }
        }
      });
      newMenu.on('mouseleave', 'LI', function(){
        var submenu = core.dom.select(this).children('.ui-menu-items-wrapper');
        if(core.dom.is(submenu)) {
          submenu.hide();
        }
      });
      
      // generate pretty checkboxes
      RadioCheck.create(newMenu.find('INPUT.ui-menu-item-selectable-input'));
      
      // prevent bubbling when clicking on checkboxes
      newMenu.find('.radioCheck.checkboxInput').click( function(event){
        var rc = core.dom.select(this);
        
        if (!rc.hasClass('disabled')) {
          RadioCheck.toggle(rc.prev());
        }
        
        event.stopPropagation();
      });
      
      // assign selections actions
      core.object.each(_self.actionsForSelection, function(index, actionsObject) {
        var item = newMenu.find('SPAN[data-itemName="' + index + '"] > .radioCheck');
        if (item && actionsObject && !item.hasClass('disabled')) {
          core.object.each(actionsObject, function(actionType, actionFunction) {
            // assign function defined by user
            if (core.string.is(actionType) && core.func.is(actionFunction)) {
              if(actionType === 'change'){
                RadioCheck.onChange(item.prev(), actionFunction);
              } else {
                item.on(actionType, actionFunction);
              }
            }
          });
        }
      });
      
      // prevent bubbling when clicking on sortable handler
      newMenu.find('.ui-menu-item-sortable-handler').click( function(event){
        event.stopPropagation();
      });
      
      // sortable
      newMenu.find('.ui-menu-items-wrapper > ul').sortable({
        handle: '.ui-menu-item-sortable-handler',
        placeholder: "ui-sortable-placeholder",
        forcePlaceholderSize: true,
        opacity: 0.7,
        revert: 100,
        update: function() {
          args = Array.prototype.slice.call(arguments);
          args.push(options.onSort);
          _self.onSortUpdate.apply(this, args);
        }
      });
      
      
      // assign action buttons actions
      core.object.each(_self.actionsButtons, function(index, actionsObject) {
        var item = newMenu.find('SPAN[data-actionId="' + index + '"]');
        if (item && actionsObject) {
          core.object.each(actionsObject, function(actionType, actionFunction) {
            item.on(actionType, actionFunction);
          });
        }
      });
      
      return newMenu ? true : false;
    },
    
    /**
     * Return Html skeleton for whole menu
     */
    generateMenuHtml: function(options){
      var _self = this,
          html = false;
      
      _self.counter = 0;
      
      html = '<div class="' + _self.menuClass + '">';
      html += _self.generateMenuItemsHtml(_self.menuItems, options);
      html += '</div/>';
      
      return html;
    },
    
    /**
     * Return Html skeleton for menu items
     */
    generateMenuItemsHtml: function(menuItems, options) {
      var _self = this,
          html = false,
          itemHtml = false,
          lastGroupIndex = 0;
      
      if (!options) {
        options = {};
      }
      html = '<div class="' + _self.menuClass + '-items-wrapper"><ul>';
      
      // sort items by groups
      core.array.sortBy(menuItems, function(el) {
        if (!el.group) {
          el.group = 0;
        }
        return parseInt(el.group, 10);
      });
      
      core.array.each(menuItems, function(menuItem) {
        // add separators between different groups
        if (lastGroupIndex !== menuItem.group) {
          html += _self.generateMenuItemHtml({separator:true});
          lastGroupIndex = menuItem.group;
        }
        html += _self.generateMenuItemHtml(menuItem, {sortable: options.sortable});
      });
      html += '</ul></div>';
      
      return html;
    },
    
    
    /**
     * Return Html skeleton for menu item
     */
    generateMenuItemHtml: function(menuItem, options) {
      var _self = this,
          html = false,
          placeholderHtml = '',
          additionalClasses = '',
          newName = false;
      
      if (!core.object.is(options)) {
        options = {};
      }
      
      //generate id if not set
      if (!menuItem.name) {
        newName = core.dom.generateID(['item'], {
          changeDOM: false,
          prefix: 'ui-menu-item-'
        });
        
        menuItem.name = newName[0];
      }

      additionalClasses += (menuItem.enabled === false) ? ' disabled' : '';
      additionalClasses += menuItem['default'] ? ' default' : '';
      
      html = '<li>';

      if (menuItem.separator) {
        // Separator
        html += '<span class="ui-menu-item-separator"></span>';
      } else {
        // Start of the menu item
        html += '<span class="btn' + additionalClasses + '" data-itemName="' + menuItem.name + '">';
        html += menuItem.icon ? '<i class="'+ menuItem.icon +'"></i>' : '';
        
        // Sortable
        if (options.sortable) {
          html += '<span class="ui-menu-item-sortable-handler"></span>';
        }
        
        // Selectable
        if (menuItem.selectable) {
          html += '<input class="ui-menu-item-selectable-input" type="checkbox" name="' + menuItem.name + '"';
          if (menuItem.selection) {
            if (menuItem.selection.checked) {
              html += ' checked="checked" ';
            }
            if (menuItem.selection.disabled) {
              html += ' disabled="disabled" ';
            }
            if (menuItem.selection.on) {
              _self.actionsForSelection[menuItem.name] =  menuItem.selection.on;
            }
          }
          html +=' />';
        }
        
        if (menuItem.actionButtonsPosition !== 'left') {
          // Label
          html += '<span class="label">';
          html += menuItem.label ? menuItem.label : '';
          html += '</span>';
        }
        

        // Additional actions:
        if (core.array.is(menuItem.actionButtons) && menuItem.actionButtons.length > 0) {
          if (menuItem.actionButtonsPosition === 'left') {
            html += '<span class="additional-actions left">';
          } else {
            html += '<span class="additional-actions">';
          }
          placeholderHtml += '<span class="additional-actions-placeholders">';
          
            // Action button
            core.array.each(menuItem.actionButtons, function(idx, actionButton) {
              var actionId = menuItem.name + '-action-' + idx;
              // generate uniqe action id
              _self.actionsButtons[actionId] = actionButton.on;
              html += '<span class="' + actionButton.icon +' additional-action" data-actionId="' + actionId + '"></span>';
              placeholderHtml += '<span class="additional-actions-placeholder"></span>';
            });
          placeholderHtml += '</span>';
          html += '</span>' + placeholderHtml;
        }
        
        if (menuItem.actionButtonsPosition === 'left') {
          // Label
          html += '<span class="label">';
          html += menuItem.label ? menuItem.label : '';
          html += '</span>';
        }
        
        // End of menu item
        html += '</span>';
              
        _self.counter = _self.counter + 1;
        
        // Submenu
        if (core.array.is(menuItem.menu) && menuItem.menu.length) {
          html += '<div class="' + _self.menuClass + '-submenu-icon"><i class="triangle-right"></i></div>';
          html += _self.generateMenuItemsHtml(menuItem.menu, {sortable: menuItem.sortable});
        }
      }
      html += '</li>';
      
      // store action
      _self.actions[menuItem.name] =  menuItem.on;

      return html;
    },
    
    /**
     * Set possiotion of the menu
     */
    setMenuPosition: function(menuSelector, initiatorSelector) {
      var _self = this,
          initiatorPosition = initiatorSelector.offset();
      
      menuSelector.css({
        left: initiatorPosition.left,
        top: initiatorPosition.top,
        width: initiatorSelector.outerWidth(),
        height: initiatorSelector.outerHeight()
      });

      menuSelector.children().first().css({
        top: initiatorSelector.outerHeight()
      });
    },
    
    /**
     * Open linked menu by initiator
     */
    initOpenMenu: function(initiator, callback) {
      var _self = this,
          menuId = core.dom.select(initiator).attr('menuId');
      
      return _self.openMenu('#' + menuId, callback);
    },
    
    /**
     * Close linked menu by initiator
     */
    initCloseMenu: function(initiator, callback) {
      var _self = this,
          menuId = core.dom.select(initiator).attr('menuId');
      
      return _self.closeMenu('#' + menuId, callback);
    },
    
    /**
     * Open linked menu by initiator
     */
    initToggleMenu: function(initiator, callback, ev) {
      var _self = this,
          menuId = core.dom.select(initiator).attr('menuId');
      
      _self.toggleMenu('#' + menuId, callback, ev);
    },
    
    /**
     * Delete linked menu by initiator
     */
    initDeleteMenu: function(initiator) {
      var _self = this,
          menuId = core.dom.select(initiator).attr('menuId'),
          menu = core.dom.select('#' + menuId);

      if (menu.length) {
        initiator.removeClass(_self.menuClass + '-initiator');
        initiator.removeAttr('menuId').unbind('click mouseover');
        menu.remove();
        return true;
      }

      return false;
    },
    
    /**
     * Has initiator linked menu?
     */
    initHasMenu: function(initiator) {
      var _self = this,
          menuId = core.dom.select(initiator).attr('menuId'),
          menu = core.dom.select('#' + menuId);
      
      return (menu.length) ? true : false;
    },
    
    
    /**
     * Open menu
     */
    openMenu: function(selector, callback, ev) {
      var _self = this,
          counter = 0,
          initiator = false;
      
      selector = core.dom.select(selector);
      core.dom.select('#' + _self.menuOverlayId).show();
      
      core.dom.each(selector, function(element) {
        initiator = core.dom.select('.ui-menu-initiator[menuid=' + element.attr('id') + ']');
        _self.setMenuPosition(element, initiator);
        element.show();
        element.children().first().show(0, function(){
          if (core.func.is(callback)) {
            callback();
          }
          if (core.func.is(element.data('showCallback'))) {
            element.data('showCallback')(ev);
          }
        });
        counter = counter + 1;
      });
      
      // close on blur
      core.dom.select('#' + _self.menuOverlayId).off('click').on('click', function() {
        _self.closeAllMenus();
      });
      
      // close on click
      core.dom.select('.ui-menu').one('click', function(){
        _self.closeAllMenus();
      });
      
      return (counter > 0) ? true : false;
    },
    
    /**
     * Close menu
     */
    closeMenu: function(selector, callback) {
      var _self = this,
          counter = 0;
      
      selector = core.dom.select(selector);
      core.dom.select('#' + _self.menuOverlayId).hide();

      core.dom.each(selector, function(element) {
        element.children().first().hide(0,function(){
          if (core.func.is(callback)) {
            callback();
          }
          if (core.func.is(element.data('hideCallback'))) {
            element.data('hideCallback')();
          }
        });
        element.hide();
        counter = counter + 1;
      });
      return (counter > 0) ? true : false;
    },
    
    /**
     * Close all menus
     */
    closeAllMenus: function(callback) {
      var _self = this,
          selector = core.dom.select('.' + _self.menuClass + ':visible');
      
      _self.closeMenu(selector, callback);
    },
    
    /**
     * Toggle menu
     */
    toggleMenu: function(selector, callback, ev) {
      var _self = this,
          menu = core.dom.select(selector).children();
      
      if (menu.is(':visible')) {
        _self.closeMenu(selector, callback, ev);
      } else {
        _self.openMenu(selector, callback, ev);
      }
      
    },
    
    /**
     * Get menu by initiator
     */
    getMenuByInitiator: function(initiator) {
      var menuObject = false;
      
      initiator = core.dom.select(initiator);
      menuObject = core.dom.select('#' + initiator.attr('menuId'));
      
      if(core.dom.is(menuObject)) {
        return menuObject;
      }
      
      return false;
    },
    
    /**
     * Get menu items array
     */
    getMenuItems: function(menuSelector) {
      var _self = this,
          menuObject = core.dom.select(menuSelector);
      
      return core.dom.select(menuObject).data('menuItems');
    },
    
    /**
     * Set menu items array
     */
    setMenuItems: function(menuSelector, menuItems) {
      var _self = this,
          menuObject = core.dom.select(menuSelector);
      
      if (core.dom.is(menuObject)) {
        menuObject.data('menuItems', menuItems);
        return true;
      }
      
      return false;
    },
    
    /**
     * After reordering menu items
     */
    onSortUpdate: function(event, ui, onSort) {
      var _self = _menu,
          updatedItem = ui.item.children(':first'),
          updatedItemName = updatedItem.attr('data-itemName'),
          updatedItemNextName = ui.item.next().children(':first').attr('data-itemName'),
          menuObject = updatedItem.parents('.ui-menu'),
          menuItems = menuObject.data('menuItems'),
          id = null,
          indx = null;
      
      // reorder items
      indx = _self.reorderItems(menuItems, updatedItemName, updatedItemNextName);
      // update menu data
      _self.setMenuItems(menuObject, menuItems);
      
      //Fire mediator event when menu is updated.
      id = "menu::" + core.dom.select(".ui-menu-initiator[menuid="+menuObject.attr("id")+"]").attr("id") + "::updated";
      core.mediator.publish(id, [_self.getMenuItems(menuObject)]);
      core.mediator.promise(id);
      
      //Fire user defined callback onSort([items, oldIndex, newIndex]).
      onSort.apply(this, [_self.getMenuItems(menuObject), indx, ui.item.index()]);
    },
    
    /**
     * Reorder menu items saved in array
     */
    reorderItems: function(submenuItems, udpatedItemName, updatedItemNextname, isSubmenu) {
      var _self = this,
          oldIndex = false,
          newIndex = false;
      
      core.array.each(submenuItems, function(menuItem) {
        if(menuItem) {
          // find old index
          if (menuItem.name === udpatedItemName) {
            oldIndex = submenuItems.indexOf(menuItem);
          }
          // find new index
          if (menuItem.name === updatedItemNextname) {
            newIndex = submenuItems.indexOf(menuItem);
          }
          // check submenu if needed
          if (core.array.is(menuItem.menu) && oldIndex === false && newIndex === false) {
            oldIndex = _self.reorderItems(menuItem.menu, udpatedItemName, updatedItemNextname, true);
          }
        }
      });
      
      // move updated element to new place
      if (oldIndex !== false) {
        if (oldIndex < newIndex) {
          newIndex = newIndex - 1;
        }
        core.array.moveElement(submenuItems, oldIndex, newIndex);
      }
      
      return oldIndex;
    }
  };

  /*************************************
   *               Public              *
   *************************************/

  var Menu = /** @lends Menu */{
    /**
     * Create a menu or split button.
     *
     * @param  {CoreObj/Selector} initiator The button that should be converted to a split/menu button.
     * @param  {Array} [menu] An array of menu items to show.
     *   @option {String} (item).name=false Unique name for the menu item. If FALSE, then unique name will be generated.
     *   @option {String} (item).label A label for the menu item.
     *   @option {String} (item).icon An icon for the menu item.
     *   @option {Boolean} (item).default=false Is this menu option default? This will show a focus on this option when the menu is opened.
     *   @option {Boolean} (item).enabled=true Is this menu option enabled?
     *   @option {Number} (item).group=0 The group the menu option belongs to on the current level. (Starts at 0.)
     *   @option {Function} (item).on Attach an event handler to this menu item.
     *   @option {Array} (item).menu=[] The submenu. Empty array or false result in no submenu.
     *   @option {Boolean} (item).sortable=false True, if user can reorder menu item. Applies on the submenu items only!
     *   @option {Boolean} (item).selectable=false True, if checkbox should be added to menu item.
     *   @option {Boolean} (item).selection.checked=false True, if checkbox should be checked.
     *   @option {Boolean} (item).selection.disabled=false True, if checkbox should be disabled.
     *   @option {Object} (item).selection.on  Event for selection like: { 'change': function() { core.debug.show('changed!'); } }
     *   @option {Function} (item).action Action, which is perform on second click(after selection fe. by Advanced Button)
     *   @option {Array} (item).actionButtons Array in which programer can define additional action buttons as a objects like: { icon: 'icon-edit', on: { 'click': editFunction(){} }}
     *   @option {String} (item).actionButtonsPosition Button can be placed on the 'left' or by default on the 'right'
     * @param {Object} [options] Additional options.
     *   @option {String} show.trigger="click" When should the pop-over be shown? Use the name of an event.
     *   @option {Number} show.delay=0 The delay before it is actually shown.
     *   @option {Function} show.on Function executed when the menu is shown.
     *   @option {Function} hide.on Function executed when the menu is hidden.
     *   @option {Function} menuitem.on Attach event handlers on every menu item.
     *   @option {Function} on Attach event handler on the complete menu.
     *   @option {Boolean} sortable=false True, if user can reorder main menu items.
     * @returns {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    create: function(initiator, menu, options) {
      return _menu.create(initiator, menu, options);
    }.defaults('', [], {
      show: {
        trigger: 'click',
        delay: 0,
        on: function(ev) {}
      },
      hide: {
        on: function(ev) {}
      },
      onSort: function(){},
      menuitem: {
        on: {}
      },
      on: {}
    }),

    /**
     * Destroys the menu.
     *
     * @param  {CoreObj/Selector} initiator The menu initiator.
     * @returns {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    destroy: function(initiator) {
      return _menu.initDeleteMenu(initiator);
    }.defaults(''),
    
    /**
     * Get the menu items array
     *
     * @param {CoreObj/Selector} initiator The menu initiator
     * @returns {Array} Menu items array
     * @responsibleAPI <a href="mailto:michal@collibra.com">Michal Hans</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    getItems: function(initiator) {
      return _menu.getMenuItems(_menu.getMenuByInitiator(initiator));
    }.defaults(''),

    /**
     * Does this initiator has a menu attached?
     *
     * @param  {CoreObj/Selector} initiator The initiator.
     * @returns {Boolean} Whether the initiator has a menu attached.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    has: function(initiator) {
      return _menu.initHasMenu(initiator);
    }.defaults(''),

    /**
     * Hide the menu.
     *
     * @param {CoreObj/Selector} initiator The initiator.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    hide: function(initiator) {
      return _menu.initCloseMenu(initiator);
    }.defaults(''),

    /**
     * Show the menu.
     *
     * @param {CoreObj/Selector} initiator The initiator.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:michal@collibra.com">Michal Hans</a>
     */
    show: function(initiator) {
      return _menu.initOpenMenu(initiator);
    }.defaults('')
  };

  return Menu;
});

core.module.alias.add('Menu', 'library/uielements/menu');
