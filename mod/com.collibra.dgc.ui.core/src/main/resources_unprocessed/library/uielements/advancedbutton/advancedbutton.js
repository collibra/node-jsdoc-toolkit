/**
 * Advanced Buttons
 *
 * Copyright Collibra NV/SA
 * @module library/uielements/advancedbutton
 * @alias AdvancedButton
 * @namespace controls
 */
/*global define,require,core */
define('library/uielements/advancedbutton',
       ['core', 'library/uielements/menu', 'library/uielements/radiocheck'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      Menu = require('library/uielements/menu'),
      RadioCheck = require('library/uielements/radiocheck');

  /*************************************
   *              Private              *
   *************************************/

  // !TODO: Use data more wisely! http://jsperf.com/jquery-data-vs-attr/21
  // - For complex data, either save it here as an object, linked to the id.
  // - For simple data use attributes.
  // - In case you have to use data, at least do a direct data set.
  //
  // The impact of this choice is highly visible on IE7 and IE8 (most used browsers of our user base).
  // I'm going to close the US and create a new task in jira to solve this issue, so you don't have to solve it now.

  /** @private */
  var _advancedButton = {
    TYPE_MENU: 'TYPE_MENU',
    TYPE_SPLIT: 'TYPE_SPLIT',
    labelClass: 'label',
    triggerClass: 'menu-trigger',
    splitClass: 'split',
    separedClass: 'separated',
    advancedClass: 'advanced',
    checkboxClass: 'radioCheck',
    hiddenCheckboxClass: 'hiddenInput',
    selectableInput: '<input type="checkbox" name="sss" class="radioCheck" >',
    label: '',
    action: '',
    splitButton: {
      // perform an action related to the button or menu item
      onClick: function(selector, buttonObj) {
        var sel = core.dom.select(selector).find('.' + _advancedButton.triggerClass)[0];
        if(core.func.is(sel.actions[buttonObj.attr('label')])) {
          sel.actions[buttonObj.attr('label')]();
        }
      }
    },
    
    /**
     * @see AdvancedButton.create
     */
    create: function(button, options) {
      var _self = this,
          buttonObj = core.dom.select(button),
          trigger = '';
      
      // save and clear label
      buttonObj.attr('label', buttonObj.text()).text('').addClass(this.advancedClass);
      // if button has menu
      if(options.menu.length) {
        buttonObj.addClass(this.splitClass);

        switch(options.type) {
          case this.TYPE_MENU:
            // create advanced button elements
            buttonObj.append('<span class="' + this.labelClass +'">' + buttonObj.attr('label') +'</span>');
            buttonObj.append('<span class="' + this.triggerClass + '"><i class="triangle-down"></i></span>');
            
            trigger = core.dom.select(button);
            buttonObj.on('click', function(ev) {
              ev.stopPropagation();
            });
            
            break;
          case this.TYPE_SPLIT:
            // if button doesn`t have a checkbox, set its label to the first label of menu item
            if(!options.checkbox.show){
              buttonObj.attr('label', options.menu[0].label);
            }

            // create advanced button elements
            buttonObj.append('<span class="' + this.labelClass +'">' + buttonObj.attr('label') +'</span>');
            buttonObj.append('<span class="' + this.triggerClass + '"><i class="triangle-down"></i></span>');
            
            trigger = core.dom.select(button).find('.' + this.triggerClass);
            buttonObj.on('click', function(ev) {
              _self.splitButton.onClick(this, buttonObj);
              ev.stopPropagation();
            });
            buttonObj.addClass(this.separedClass);
            
            break;
          default:
            core.debug.show('Wrong type: ' + options.type);
            return false;
        }
      
        // create menu
        Menu.create(trigger, options.menu, {
            show: {
              trigger: 'click',
              on: function(ev) {
                trigger.find('.menu-trigger').addClass('hover');
                core.debug.show('menu createe');
                ev.stopPropagation();
                
                // move menu to the left side of button
                (function() {
                  var outerWidth = buttonObj.outerWidth() - trigger.outerWidth(),
                      menuid = trigger.attr('menuid');
                  core.dom.select('#' + menuid).find('.ui-menu-items-wrapper').css({'left': -outerWidth + 'px' });
                }());
              }
            },
            hide: {
              on: function(ev) {
                trigger.removeClass('active').find('.menu-trigger').removeClass('active');
              }
            },
            menuitem: {
              on: {
                'click': function() {
                  // if button doesn`t have checkbox and menu item also doesn`t have a checkbox
                  if(!options.checkbox.show && !core.dom.select(this).find('.radioCheck.checkboxInput').length){
                    // apply new content to the button
                    buttonObj.find('.' + _advancedButton.labelClass).html(core.dom.select(this).html());
                    // save label(it can have an action)
                    buttonObj.attr('label', buttonObj.find('.' + _advancedButton.labelClass).text());
                  }
                }
              }
            }
        });
      } else {
        if(options.action) {
          buttonObj.on('click', function () {
            options.action();
          });
        }
      }
      
      // if button has checkbox
      if(options.checkbox.show){
        var radioCheck = '',
            radioCheckFull = '';
        
        // create checkbox
        buttonObj.prepend(this.selectableInput);
        radioCheck = buttonObj.find('.radioCheck');
        RadioCheck.create(radioCheck);
        buttonObj.children('input.radioCheck:first').attr('name', buttonObj.attr('id')).find('.radioCheck.checkboxInput').attr('originalname', buttonObj.attr('id'));
        
        if(!options.menu.length) {
          buttonObj.append('<span class="' + this.labelClass +'">' + buttonObj.attr('label') +'</span>');
        }
        
        // if checkbox should be checked
        if(options.checkbox.check) {
          RadioCheck.check(buttonObj.find('.radioCheck.hiddenInput'));
        }
        
        // toggle checkbox
        buttonObj.find('.radioCheck.checkboxInput').on('click', function(ev) {
          RadioCheck.toggle(buttonObj.find('.radioCheck.hiddenInput'));
          ev.stopPropagation();
        });
        
        RadioCheck.onChange(buttonObj.find('.radioCheck.hiddenInput'), options.checkbox.onChange);
      }
      
      // events for manipulating active and hover states
      buttonObj.on({
        'mouseenter': function(ev) {
          core.dom.select(this).addClass('hover');
          if(!buttonObj.hasClass('separated')) {
            buttonObj.find('.menu-trigger').addClass('hover');
          }
        },
        'mouseleave': function(ev) {
          core.dom.select(this).removeClass('hover');
          if(!buttonObj.hasClass('separated')) {
            buttonObj.find('.menu-trigger').removeClass('hover');
          }
        },
        'mousedown': function(ev) {
          core.dom.select(this).addClass('active');
          ev.stopPropagation();
        },
        'mouseup': function(ev) {
          if(buttonObj.hasClass('separated') || !options.menu.length) {
            core.dom.select(this).removeClass('active');
            ev.stopPropagation();
          }

        }
      });
      if(trigger) {
        trigger.on({
          'mouseenter': function(ev) {
            buttonObj.removeClass('hover');
            core.dom.select(this).addClass('hover');
            ev.stopPropagation();
          },
          'mouseleave': function(ev) {
            core.dom.select(this).removeClass('hover');
            if(buttonObj.hasClass('separated')) {
              buttonObj.addClass('hover');
            }
          },
          'mousedown': function(ev) {
            core.dom.select(this).removeClass('hover');
            trigger.find('.menu-trigger').addClass('active');
            core.dom.select(this).addClass('active');
            ev.stopPropagation();
          }
        });
      }
      buttonObj.find('.radioCheck.checkboxInput').on({
        'mouseenter': function(ev) {
          buttonObj.removeClass('hover').removeClass('active');
          buttonObj.find('.menu-trigger').removeClass('hover').removeClass('active');
          ev.stopPropagation();
        },
        'mousedown': function(ev) {
          buttonObj.removeClass('hover').removeClass('active');
          ev.stopPropagation();
        },
        'mouseleave': function(ev) {
           buttonObj.addClass('hover');
        }
      });
      
      return true;
    },
    
    /**
     * @see AdvancedButton.getCheckbox
     */
    getCheckbox: function(button) {
      var buttonObj = core.dom.select(button),
          checkbox = buttonObj.find('.radioCheck.checkboxInput');
      
      if(checkbox.length) {
        return checkbox;
      } else {
        return false;
      }
    },
    
    /**
     * @see AdvancedButton.destroy
     */
    destroy: function(button) {
      var buttonObj = core.dom.select(button),
          trigger = buttonObj.find('.' + this.triggerClass);
      
      buttonObj.text(buttonObj.attr('label')).removeClass(this.splitClass).removeClass(this.separedClass);
      trigger.remove();
      if(this.disable(button) && !buttonObj.find('.' + this.triggerClass).length) {
        return true;
      }
      return false;
    },
    
    isDisabled: function(buttonObj) {
      if (core.dom.is(buttonObj.next('.dropdown-overlay-disabled'))) {
        return true;
      } else {
        return false;
      }
    },
    
    /**
     * @see AdvancedButton.disable
     */
    disable: function(button) {
      var buttonObj = core.dom.select(button);
      
      if(this.isDisabled(buttonObj)) {
        return true;
      } else {
        buttonObj.after('<div class="dropdown-overlay-disabled"></div>');
        var position = buttonObj.position(),
            overlay = buttonObj.next('.dropdown-overlay-disabled');
        overlay.css({top: position.top, left: position.left, position:'absolute', width: buttonObj.outerWidth(true), height: buttonObj.outerHeight(true), background: 'none repeat scroll 0 0 white', opacity: 0.1});
        
        return true;
      }
      return false;
    },
    
    /**
     * @see AdvancedButton.enable
     */
    enable: function(button) {
      var buttonObj = core.dom.select(button),
          overlay = buttonObj.next('.dropdown-overlay-disabled');
      
      overlay.remove();
      
      if(this.isDisabled(buttonObj)) {
        return false;
      }
      return true;
    },

    /**
     * @see AdvancedButton.focus
     */
    focus: function(button) {
      var activeElemTabIndex = document.activeElement.tabIndex,
          buttonObj = core.dom.select(button),
          buttonObjTabIndex =  core.dom.select(button)[0].tabIndex;
      
      if(activeElemTabIndex === -1) {
        core.dom.select(button)[0].tabIndex = 1;
      } else if(activeElemTabIndex === buttonObjTabIndex) {
        return true;
      } else {
        core.dom.select(button)[0].tabIndex = activeElemTabIndex + 1;
      }
      
      return true;
    },

    /**
     * @see AdvancedButton.is
     */
    is: function(button, type) {
      if(type === 'TYPE_SPLIT') {
        return core.dom.select(button).hasClass(this.separedClass);
      }
      else if(type === 'TYPE_MENU') {
        return !core.dom.select(button).hasClass(this.separedClass);
      } else {
        core.debug.show(type + ': is not defined');
      }
      
      return undefined;
      
    }.defaults('')
    
  };

  /*************************************
   *               Public              *
   *************************************/

  var AdvancedButton = /** @lends AdvancedButton */{
    /**
     * Menu button. When you click on the button it automatically shows a menu to choose from.
     *
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    TYPE_MENU: _advancedButton.TYPE_MENU,
    
    /**
     * Split button. When you click on the drop-down icon you get the menu, the rest of the button will execute the default shown option.
     *
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    
    TYPE_SPLIT: _advancedButton.TYPE_SPLIT,

    /**
     * Create a menu or split button.
     *
     * @param {CoreObj/Selector} button The button that should be converted to a split/menu button.
     * @param {Object} [options] Options for the button.
     *   @option {Boolean} type=TYPE_MENU The type of button. Can be {@link Button.TYPE_MENU} and {@link Button.TYPE_SPLIT}.
     *   @option {Array} menu An array of menu items to show.
     *   @option {String} menu.(item).label A label for the menu item.
     *   @option {String} menu.(item).icon An icon for the menu item.
     *   @option {Boolean} menu.(item).default=false Is this menu option default? For a split button, this option will be shown on the button as a label.
     *   @option {Boolean} menu.(item).enabled=true Is this menu option enabled?
     *   @option {Number} menu.(item).group=0 The group the menu option belongs to on the current level. (Starts at 0.)
     *   @option {Function} menu.(item).onClick Click callback function.
     *   @option {Array} menu.(item).menu=[] The submenu. Empty array or false result in no submenu.
     *   @option {Boolean} checkbox.show=false Show a checkbox on the button.
     *   @option {Boolean} checkbox.checked=false Is the checkbox checked?
     *   @option {Function} checkbox.onChange Function executed when the state of the checkbox changes.
     * @return {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    create: function(button, options) {
      return _advancedButton.create(button, options);
    }.defaults('', {
      type: _advancedButton.TYPE_MENU,
      menu: [],
      checkbox: {
        show: false,
        checked: false,
        onChange: function() {}
      }
    }),

    /**
     * Get the checkbox displayed on the button.
     *
     * @param {CoreObj/Selector} button The button that should be converted to a split/menu button.
     * @returns {CoreObj} The checkbox as a CoreObj and false on failure. Use RadioCheck to manipulate it.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    getCheckbox: function(button) {
      return _advancedButton.getCheckbox(button);
    }.defaults(''),

    /**
     * Destroys the button menu.
     *
     * @param {CoreObj/Selector} button The button.
     * @return {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    destroy: function(button) {
      return _advancedButton.destroy(button);
    }.defaults(''),

    /**
     * Disable the button (and it's menu).
     *
     * @param {CoreObj/Selector} button The button.
     * @return {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    disable: function(button) {
      return _advancedButton.disable(button);
    },

    /**
     * Enable the button (and it's menu).
     *
     * @param {CoreObj/Selector} button The button.
     * @return {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    enable: function(button) {
      return _advancedButton.enable(button);
    },

    /**
     * Focus the button.
     *
     * @param {CoreObj/Selector} button The button.
     * @return {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    focus: function(button) {
      return _advancedButton.focus(button);
    },

    /**
     * Is this button a split/menu button?
     *
     * @param  {CoreObj/Selector} button The button.
     * @return {Boolean} Whether the button is a split or menu button.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:lukasz@collibra.com">Lukasz Zazulak</a>
     */
    is: function(button, type) {
      return _advancedButton.is(button, type);
    }.defaults('')
  };

  return AdvancedButton;
});

core.module.alias.add('AdvancedButton', 'library/uielements/advancedbutton');