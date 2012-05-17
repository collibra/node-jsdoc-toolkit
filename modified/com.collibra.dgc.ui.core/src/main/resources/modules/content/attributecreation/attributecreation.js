/**
 * Attribute Creation.
 *
 * Copyright Collibra NV/SA
 * @author Grzegorz Pawlowski <grzegorz@collibra.com>
 * @module modules/content/attributecreation
 * @alias AttributeCreation
 * @namespace modules/content
 */
/*global define,require,core */
define('modules/content/attributecreation',
       ['core', 'library/uielements/texteditor', 'library/uielements/dropdown', 'library/uielements/menu'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      menu = require('library/uielements/menu'),
      textEditor = require('library/uielements/texteditor'),
      dropdown = require('library/uielements/dropdown'),
      i18n = core.i18n.getTranslations('modules.content.attributecreation');

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _attributeCreation = {
    /** default event name **/
    addEventID: 'attributes::add',
    attributeHTMLframe: '#attributecreation',

    CONTENT_TYPE_WYSIWYG: 'StringAttribute',
    CONTENT_TYPE_DATE: 'A', //TODO: add initializer when uielement is available and change id in velocity
    CONTENT_TYPE_TIME: 'B', //TODO: add initializer when uielement is available and change id in velocity
    CONTENT_TYPE_DATETIME: 'C', //TODO: add initializer when uielement is available and change id in velocity
    CONTENT_TYPE_DYNAMIC_LIST: 'D', //TODO: add initializer when uielement is available and change id in velocity
    CONTENT_TYPE_SINGLE_VALUE_LIST: 'SingleValueListAttribute',
    CONTENT_TYPE_MULTI_VALUE_LIST: 'MultiValueListAttribute',

    _init: function() {
      var _self = this,
          options;

      core.mediator.subscribe('tabbar::tab::attributes::loaded', function() {
        //Content is loaded - initialize.
        _self.initializeTextEditor();
        _self.initializeDropdownSingle();
        _self.initializeDropdownMultiple();
        _self.initializeCancelButton();
        _self.initializeSaveButton();

        //Set default add attribute menu.
        options = [
                   {
                     label: 'Custom Attribute',
                     type: _self.CONTENT_TYPE_WYSIWYG
                   }, {
                     label: 'Single List',
                     type: _self.CONTENT_TYPE_SINGLE_VALUE_LIST
                   }, {
                     label: 'Multiple List',
                     type: _self.CONTENT_TYPE_MULTI_VALUE_LIST
                   }, {
                     label: 'Dynamic list',
                     type: _self.CONTENT_TYPE_DYNAMIC_LIST
                   }, {
                     label: 'Date',
                     type: _self.CONTENT_TYPE_DATE
                   }, {
                     label: 'Time',
                     type: _self.CONTENT_TYPE_TIME
                   }, {
                     label: 'DateTime',
                     type: _self.CONTENT_TYPE_DATETIME
                   }
                 ];

        _self.addAddMenu('', options);
      });
    },

    /**
     * Used to initialize WYSIWYG editor (for default type - CONTENT_TYPE_WYSIWYG).
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    initializeTextEditor: function() {
      var _self = this;

      textEditor.create('#attributecreation-custom-value', textEditor.FULL, {
        controls: {
          Pulldown: {
            groupIndex: -1,
            visible: true,
            className: "pulldown",
            exec: function() {
              _self.pullDownUp(this);
            }
          },
          sourceCode: {
            groupIndex: 5
          }
        },
        save: {
          automatic: false,
          buttons: {
            show: true,
            position: "bottom",
            onSave: function(e) {
              textEditor.save('#attributecreation-custom-value');
            },
            onDiscard: function(e) {
              textEditor.resetContent('#attributecreation-custom-value');
              core.dom.select('#attributecreation .attributecreation-type-custom').slideUp(function() {
                core.dom.select(this).closest('DIV#attributecreation').hide();
              });
            }
          },
          url: "#"
        },
        content: '',//TODO: add initial content if there is any - wait for "model".
        label: i18n.texteditor.defaultText
      });
    },

    /**
     * Functionality for Pulldown and Pullup button for texeditor.
     * @param {CoreObj} wysiwygeditor Wysiwyg editor object.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    pullDownUp: function(wysiwygeditor) {
      var creationForm = wysiwygeditor.element.closest('DIV.attributecreation-content'),
          pulldownClass = 'icon-pulldown',
          pullupClass = 'icon-pullup',
          buttons = core.dom.select('.texteditor .btn-group'),
          sizeBefore, buttonDown, buttonUp, theButton, wrapperMinSize, wrapperMaxSize, delta;

      buttonDown = buttons.find('.' + pulldownClass);               //Find button with pulldown class.
      buttonUp = buttons.find('.' + pullupClass);                   //Find button with pullup class.
      theButton = (core.dom.is(buttonDown)) ? buttonDown : buttonUp;//One var is empty jquery selector, so take the other.

      //If there is information about size (max and min size of creation form) - pull it, otherwise add it.
      if (theButton.data('wrapperMinSize')) {
        wrapperMinSize = theButton.data('wrapperMinSize');
        wrapperMaxSize = theButton.data('wrapperMaxSize');
      } else {
        wrapperMinSize = creationForm.height();
        wrapperMaxSize = core.dom.select('.twocolumnlayout').height() - (creationForm.outerHeight(true) - creationForm.height()); //Height of .pagecontent can be smaller than tabbar's height so we use wrapping div (.twocolumnlayout). We need to subtract the height of creation form's paddings.
        theButton.data('wrapperMinSize', wrapperMinSize).data('wrapperMaxSize', wrapperMaxSize);
      }

      delta = wrapperMaxSize - wrapperMinSize; //Used to resize texeditor inside creation form.
      //Animate resize and change icons.
      if (core.dom.is(buttons.find('.' + pulldownClass))) {
        creationForm.animate({
          height: wrapperMaxSize
        }).promise().done(function() {
          creationForm.find('IFRAME').animate({height: '+=' + delta});
        });

        //Change ico.
        theButton.removeClass(pulldownClass).addClass(pullupClass);
      } else {
        //Pullup
        creationForm.find('IFRAME').animate({
          height: '-=' + delta
        }).promise().done(function() {
          creationForm.animate({
            height: wrapperMinSize
          });
        });

        //Change ico.
        theButton.removeClass(pullupClass).addClass(pulldownClass);
      }
    },

    /**
     * Used to initialize Dropdown uielement (for CONTENT_TYPE_SINGLE_VALUE_LIST).
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    initializeDropdownSingle: function() {
      var _self = this;

      dropdown.create('#attributecreation-list-value', {
        autoComplete: {
          enabled: false,
          useAjax: false
        }
      });
    },

    /**
     * Used to initialize Dropdown uielement (for CONTENT_TYPE_MULTIPLE_VALUE_LIST).
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    initializeDropdownMultiple: function() {
      var _self = this;

      dropdown.create('#attributecreation-list-multiple-value', {
        multiple: true,
        autoComplete: {
          enabled: false,
          useAjax: false
        }
      });
    },

    /**
     * Add action to CANCEL button.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    initializeCancelButton: function() {
      var _self = this;

      core.dom.select('DIV#attributecreation').on('click', '.attributecreation-button-cancel', function() {
        core.dom.select(this).closest('DIV.attributecreation-content').slideUp()
                             .closest('DIV#attributecreation').hide();
      });
      //TODO: should I erase content if it's set? Maybe add mediator event to do it?
    },

    /**
     * Add action to SAVE button.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    initializeSaveButton: function() {
      var _self = this,
          temp;

      //TODO: add SAVE function - wait for "model"
      core.dom.select('div#attributecreation').on('click', '.attributecreation-button-save', function() {
        temp = core.dom.select(this).closest('.attributecreation-content').children('H3').text();
        core.debug.show('Content saved from ' + temp + ' !');
      });
    },

    /**
     * Use "type" property (from each menu item) to select icon and add method do display creation form.
     * @param {Array} menu An array with menu items.
     * @returns {Array} menu An array with menu items extended with icon and on() method callback. Passed to "Menu" uielement.
     * @author <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    bindFormToType: function(menu) {
      var _self = this,
          clickAction;

      core.array.each(menu, function(item) {
        switch(item.type) {
          case _self.CONTENT_TYPE_SINGLE_VALUE_LIST:
            clickAction = {
              icon: 'icon-single-list'
            };
            break;

          case _self.CONTENT_TYPE_MULTI_VALUE_LIST:
            clickAction = {
              icon: 'icon-multiple-list'
            };
            break;

          case _self.CONTENT_TYPE_DYNAMIC_LIST:
            clickAction = {
              icon: 'icon-dynamic-list'
            };
            break;

          case _self.CONTENT_TYPE_DATE:
            clickAction = {
              icon: 'icon-calendar'
            };
            break;

          case _self.CONTENT_TYPE_TIME:
            clickAction = {
              icon: 'icon-clock'
            };
            break;

          case _self.CONTENT_TYPE_DATETIME:
            clickAction = {
              icon: 'icon-calendar'
            };
            break;

          default:
            //type: _self.CONTENT_TYPE_WYSIWYG
            clickAction = {
              icon: 'icon-attribute'
            };
        }

        core.object.extend(clickAction, {
          on: {
            'click': function() {
              _self.openCreationForm({type: item.type});
            }
          }
        });

        //Extend menu object with an icon and a creation form accurate to the menu's type.
        core.object.extend(item, clickAction);
      });

      return menu;
    },

    /**
     * @see AttributeCreation.addAddMenu
     */
    addAddMenu: function(addMenuEventID, menuItems) {
      var _self = this,
          menuEventID = (addMenuEventID) ? addMenuEventID : _self.addEventID,
          tabbar = core.dom.select('#tabbar'),
          elem = tabbar.find('div[event="' + menuEventID + '"]'),
          publishMenuEvent = function(e) {
            core.mediator.publish('attributecreation::addmenu::clicked', [e]);
          };

      core.dom.each(elem, function(node) {
        //Add custom event to create menu after node was clicked.
        node.on('click', publishMenuEvent);
      });

      //Subscribe event to execute it after node was clicked.
      core.mediator.subscribe('attributecreation::addmenu::clicked', function(e) {
        var node = e.currentTarget,
            createOptions;

        core.dom.select(node).off('click', publishMenuEvent);//Unbind itself.

        if (!menu.has(node)) {
          //Menu is not created.
          createOptions = {
            addMenus : [
              {
                eventID: menuEventID,
                menuItems: menuItems
              }
            ]
          };

          _self.create(createOptions);
        }

        menu.show(node); //Show the menu.
      });
    },

    /**
     * @see AttributeCreation.create
     */
    create: function(options) {
      var _self = this,
          tabbar = core.dom.select('#tabbar'),
          nodePresent = false,
          eventID, node;

      if (core.dom.is(tabbar)) {
        //Iterate whole options.
        core.array.each(options.addMenus, function(singleMenu) {
          //Get event to find menu.
          eventID = (singleMenu.eventID) ? singleMenu.eventID : _self.addEventID;
          node = tabbar.find('div[event="' + eventID + '"]');
  
          //Use type property (from each menu item) to choose which creation form should be displayed.
          singleMenu.menuItems = _self.bindFormToType(singleMenu.menuItems);
  
          menu.create(node, singleMenu.menuItems, {
            show: {
              trigger: 'click'
            }
          });

          nodePresent = true;
        });

        return (nodePresent) ? true : false;
      }

      return false;
    },

    /**
     * @see AttributeCreation.destroy
     */
    destroy: function(original) {
      var _self = this,
          attributeTab = core.dom.select('#tabbar li[event="attributes"]'); //Clovis: can this be hard-coded like this?

      if (original && core.dom.is(attributeTab)) {
        //Destroy the menu, the action button and the creation form html.
        core.dom.each(attributeTab.find('div.action'), function(button) {
          menu.destroy(button);
          button.remove();
        });

        core.dom.select('#attributecreation').remove();

        return true;
      }

      return false;
    },

    /**
     * @see AttributeCreation.openCreationForm
     */
    openCreationForm: function(options) {
      var _self = this,
          frame = core.dom.select(_self.attributeHTMLframe),
          creationForm, resizers, initResizer, initialMinHeight;

      if (core.string.is(options.type) && core.dom.is(frame)) {
        creationForm = frame.find('div#attributeCreation' + options.type);
        resizers = frame.find('.attributecreation-resizer');

        //If the form we want to show is not visible
        if (!creationForm.is(':visible')) {
          if (options.type === _self.CONTENT_TYPE_WYSIWYG) {
            //Show resize icons.
            resizers.show();
            initResizer = true;
          } else {
            //Hide resize icons.
            resizers.hide();
          }

          //Hide visible creation form.
          frame.show().find('div.attributecreation-content:visible').slideUp('fast').promise().done(function() {
            //Open creation form.
            creationForm.slideDown(function() {
              //Set initial height used as minimum height for resizer.
              if (!creationForm.data('initialHeight')) {
                creationForm.data('initialHeight', creationForm.height());
              }

              //After creation form is completely displayed execute callback and assign the form as "this".
              if (core.func.is(options.callback)) {
                options.callback.call(this);
              }

              //Initialize resizer plugin? Do it here to get minHeight after content was shown.
              if (initResizer) {
                initialMinHeight = creationForm.data('initialHeight');

                creationForm.resizable({
                  handles: 's',
                  minHeight: initialMinHeight,
                  maxHeight: core.dom.select('.twocolumnlayout').height() - (creationForm.outerHeight(true) - creationForm.height()),
                  alsoResize: creationForm.find('IFRAME')
                });
              }
            });
          });

        return true;
        }
      }

      return false;
    },

    /**
     * @see AttributeCreation.removeAddMenu
     */
    removeAddMenu: function(addMenuEventID) {
      var _self = this,
          eventID = (addMenuEventID) ? addMenuEventID : _self.addEventID,
          tabbar = core.dom.select('#tabbar');

      if (core.dom.is(tabbar)) {
        return menu.destroy(tabbar.find('div[event="' + eventID + '"]'));
      }

      return false;
    }
  };

  core.dom.ready(function() {
    _attributeCreation._init();
  });

  /*************************************
   *               Public              *
   *************************************/

  var AttributeCreation = /** @lends AttributeCreation */{
    /**
     * Show a WYSIWYG editor to edit the content of this attribute.
     *
     * @type {String}
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    CONTENT_TYPE_WYSIWYG: _attributeCreation.CONTENT_TYPE_WYSIWYG,

    /**
     * Show a date picker to edit the content of this attribute.
     *
     * @type {String}
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    CONTENT_TYPE_DATE: _attributeCreation.CONTENT_TYPE_DATE,

    /**
     * Show a time picker to edit the content of this attribute.
     *
     * @type {String}
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    CONTENT_TYPE_TIME: _attributeCreation.CONTENT_TYPE_TIME,

    /**
     * Show a date & time picker to edit the content of this attribute.
     *
     * @type {String}
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    CONTENT_TYPE_DATETIME: _attributeCreation.CONTENT_TYPE_DATETIME,

    /**
     * Show an autocomplete to edit the content of this attribute.
     *
     * @type {String}
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    CONTENT_TYPE_DYNAMIC_LIST: _attributeCreation.CONTENT_TYPE_DYNAMIC_LIST,

    /**
     * Only one element can be selected (for autocomplete and drop-down).
     *
     * @type {String}
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    CONTENT_TYPE_SINGLE_VALUE_LIST: _attributeCreation.CONTENT_TYPE_SINGLE_VALUE_LIST,

    /**
     * Mutliple values can be selected (for autocomplete and drop-down).
     *
     * @type {String}
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    CONTENT_TYPE_MULTI_VALUE_LIST: _attributeCreation.CONTENT_TYPE_MULTI_VALUE_LIST,

    /**
     * Add a new menu to add attributes.
     *
     * @param {EventID} addMenuEventID The event ID that triggers the add menu.
     * @param {Array} menuItems Menu items to show.
     *   @option {String} (MenuItem).label The label of the menu item.
     *   @option {CONTENT_TYPE} (MenuItem).type The content type of the menu item. Can be one of the CONTENT_TYPE_... statics.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    addAddMenu: function(addMenuEventID, menuItems) {
      _attributeCreation.addAddMenu(addMenuEventID, menuItems);
    }.defaults('', [
      {
        label: '',
        type: _attributeCreation.CONTENT_TYPE_WYSIWYG
      }
    ]),

    /**
     * Create the functionality for the attribute creation. (This doesn't create the necessary form HTML that comes from attributecreation.vm).
     *
     * @param {Object} options Options.
     *   @option {Array<Object>} addMenus The menus to add for attribute creation.
     *   @option {EventID} addMenus.(Menu).eventID The event ID that triggers the menu.
     *   @option {Object} addMenus.(Menu).menuItems The items to show in the menu.
     *   @option {String} addMenus.(Menu).menuItems.(MenuItem).label The label of the menu item.
     *   @option {CONTENT_TYPE} addMenus.(Menu).menuItems.(MenuItem).type The content type of the menu item. Can be one of the CONTENT_TYPE_... statics.
     * @return {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    create: function(options) {
      return _attributeCreation.create(options);
    }.defaults({
      addMenus: [
        {
          eventID: '',
          menuItems: [
            {
              label: '',
              type: _attributeCreation.CONTENT_TYPE_WYSIWYG
            }
          ]
        }
      ]
    }),

    /**
     * Destroy all attribute creation functionality.
     *
     * @param {Boolean} original Remove the form HTML and menu's, so absolutely nothing is left.
     * @return {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    destroy: function(original) {
      return _attributeCreation.destroy(original);
    }.defaults(true),

    /**
     * Open the creation form to add a specific attribute.
     *
     * @param {Object} options The options.
     *   @option {CONTENT_TYPE} type The content type of the attribute to add. Can be one of the CONTENT_TYPE_... statics.
     *   @option {Function} callback The callback function after the form has been completely displayed.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    openCreationForm: function(options) {
      return _attributeCreation.openCreationForm(options);
    }.defaults({
      type: _attributeCreation.CONTENT_TYPE_WYSIWYG,
      callback: function() {}
    }),

    /**
     * Remove an add menu (add button is left).
     *
     * @param {EventID} addMenuEventID The event ID that triggers the add menu.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    removeAddMenu: function(addMenuEventID) {
      return _attributeCreation.removeAddMenu(addMenuEventID);
    }.defaults('')
  };

  return AttributeCreation;
});

core.module.alias.add('AttributeCreation', 'modules/content/attributecreation');
core.module.use('AttributeCreation');
