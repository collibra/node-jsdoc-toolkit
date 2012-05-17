/**
 * Widget UIElement.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
 * @module library/uielements/widget
 * @alias Widget
 * @namespace controls
 */
/*global define,require,core, console,confirm */
define('library/uielements/widget',
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
  var _widget = {

    /**
     * Structure markup for HTML.
     */
    markup: {
      remove: '<div class="widget-overlay-remove"><div class="widget-overlay-remove-label">Are you sure?</div>'+
              '<div class="widget-overlay-remove-buttons"><button class="widget-remove-button-confirm btn-success no-label"><i class="icon-ok white"></i></button>'+
              '<button class="widget-remove-button-cancel btn-danger no-label"><i class="icon-delete white"></i></button>'+
              '</div></div>'
    },

    /**
     * All the CSS classes used by this module.
     */
    classes: {
      widgetConfigBtn: "widget-config",
      widgetRemoveBtn: "widget-remove",
      widgetBtnActive: "active",
      widgetContent: "widget-content",
      widgetSettings: "widget-settings",
      widgetContainer: "widget-container",
      widgetOverlay: "widget-overlay",
      widgetOverlayRemove: "widget-overlay-remove",
      widgetLoadingDiv: "widget-loading-content"
    },

    /**
     * Available Widget's modes.
     */
    modes: {
      "state-default": 0,
      "state-settings": 1,
      "state-editing": 2
    },

    /**
     * @see Widget.initialize
     */
    initialize: function(widget) {
      var settingsBox = null;
      
      widget = core.dom.select(widget);
      
      if (this.is(widget)) {
        
        settingsBox = widget.find("." + _widget.classes.widgetSettings);
        //Bindings of the 'config', 'remove' buttons
        //'move' button is auto-binded by $.sortable.

        //Settings bindings.
        settingsBox.find(".btn-success").on("click", function () {
          //Confirm.
           core.debug.show("settings confirmed");
          _widget.getSettings(widget);

          //Flip back to content, without menu.
          _widget.setMode(widget, 0);
          _widget.setMode(widget, 2);
        });

        settingsBox.find(".btn-danger").on("click", function () {
          //Cancel.
          //Reset settings.

          //Flip back to content.
          widget.find("." + _widget.classes.widgetConfigBtn).trigger("click");
        });

        //Config.
        widget.find("." + _widget.classes.widgetConfigBtn).on("click", function() {
          //Toggle settings styles.
          if (!widget.hasClass("state-settings")) {
            //Show settings.
            _widget.setMode(widget, 1);
          } else {
            //Show content.
            _widget.setMode(widget, 0);
            _widget.setMode(widget, 2);
          }
        });

        //Remove.
        widget.find("." + _widget.classes.widgetRemoveBtn).on("click", function() {
          if (!_widget.isDisabled(widget)) {
            _widget.disable(widget);

            widget.find("." + _widget.classes.widgetOverlay)
                  .after(_widget.markup.remove);

            widget.find(".widget-remove-button-confirm").unbind().bind("click", function() {
              _widget.destroy(widget);
              core.debug.show("destroy started");
            });

            widget.find(".widget-remove-button-cancel").unbind().bind("click", function() {
              _widget.enable(widget);
              widget.find("." + _widget.classes.widgetOverlayRemove).remove();
            });
          }
        });

        //Loading of the remote content if needed.
        //core.mediator.unsubscribeAll("widget::" + widget.attr("name") + "::" + widget.attr("id") + "::loaded");
        if (!widget.attr("name")) {
          widget.attr("name", "gen-" + new Date().getTime());
        }
        
        var deferred = core.mediator.promise("widget::" + widget.attr("name") + "::" + widget.attr("id") + "::loaded");
        //Make a call if there's some path specified.
        if (widget.attr("remote") === "true") {
          _widget.load(widget);
        } else {
          //widget.show();
          core.mediator.publish("widget::" + widget.attr("name") + "::" + widget.attr("id") + "::loaded", [widget]);
        }

        //Radio Check inputs.
        RadioCheck.create(widget.find('input:checkbox, input:radio'));
        
        return true;
      }
      
      return false;
    },

    /**
     * @see Widget.destroy
     */
    destroy: function(widget) {

      //Destroy can only happen if there's confirmation from back-end.
      if (this.is(widget)) {

        core.REST.update("users widget list url", "some id data", {
          onSuccess: function() {
            //We have confirmation from database, lets remove from UI.
            widget.off().remove();
            //Notify WidgetBoard about the need to refresh widgets list.
            core.mediator.publish("widgetboard::widgets::collect");
          },
          onError: function() {
            //Cannot delete.
            //Notify user and leave widget alone.
          }
        });
        return true;
      }
      return false;
    },

    /**
     * @see Widget.disable
     */
    disable: function(widget) {

      if (this.is(widget)) {
        //Create disable overlay.
        core.dom.select("<div></div>")
          .addClass(_widget.classes.widgetOverlay)
          .appendTo(widget.find("." + _widget.classes.widgetContainer));
        widget.addClass("disabled");
        return true;
      }
      return false;
    },

    /**
     * @see Widget.enable
     */
    enable: function(widget) {

      if (this.is(widget)) {
        //Remove disable overlay.
        widget.find("." + _widget.classes.widgetOverlay).remove();
        widget.removeClass("disabled");
        return true;
      }
      return false;
    },

    /**
     * @see Widget.getMode
     */
    getMode: function(widget) {
      var self = this,
          classes = null,
          modes = [];

      if (this.is(widget)) {
        //Collect all applied classes. Because widget can be in edit and default view
        //at the same time.
        classes = widget.attr("class").replace("widget", "").trim().split(/\s+/);
        core.array.each(classes, function(k, v) {
          if (self.modes.hasOwnProperty(v)) {
            modes.push(self.modes[v]);
          }
        });

        //Return mode number or array of modes number(if more then one).
        return modes.length === 1? modes[0] : modes;
      }
      return false;
    },

    /**
     * Get current settings for Widget(states and values of inputs in Widget's settings mode).
     *
     * @param {Selector/CoreObj} widget The widget.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    getSettings: function(widget) {
      var params = {
            common: {}
          },
          elem = null,
          propType = "",
          param = null;

      //Iterate over available controls in settings
      //and collect their values.
      widget.find(".widget-settings-content *[setting]'").each(function(){
        elem = core.dom.select(this);

        if (elem.attr("setting").match(/default\./)) {
          param = params.common;
        } else {
          param = params;
        }

        propType = (elem.attr("type") === "checkbox" || elem.attr("type") === "radio") ? "checked" : "value";
        param[elem.attr("setting")] = elem.prop( propType );
      });

      widget.trigger("onSettingsChange", [params]);
    },

    /**
     * Get parent WidgetBoard container selector for Widget.
     *
     * @param {Selector/CoreObj} widget The widget.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    getWidgetBoard: function(widget) {

      if (this.is(widget)) {
        //This is not good, because structure can change.
        //Yet too early to decide.
        return widget.parent().parent();
      }
      return false;
    },

    /**
     * @see Widget.hide
     */
    hide: function(widget) {
   
      if (this.is(widget)) {
        //Used animation here, but its disabled for now anyway.
        widget.animate({
          opacity: '0'
        }, "fast", function() {
          core.dom.select(this).hide();
        });
        return true;
      }

      return false;
    },

    /**
     * @see Widget.is
     */
    is: function(widget) {
      widget = core.dom.select(widget);

      //This function not only checks if selector is a widget,
      //but also make sure widget is always a jQuery object.
      if (core.dom.is(widget) && widget.hasClass("widget")) {
        return true;
      }

      return false;
    },

    /**
     * @see Widget.isDisabled
     */
    isDisabled: function(widget) {
      widget = core.dom.select(widget);

      if (core.dom.is(widget) && widget.hasClass("disabled")) {
        return true;
      }

      return false;
    },

    /**
     * Make remote call using data from widget attribute.
     *
     * @param {Selector/CoreObj} widget The widget.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    load: function(widget) {
      var id = "widget::" + widget.attr("name") + "::" + widget.attr("id") + "::loaded";
        core.module.get(widget.attr("path"), function (data) {
          widget.removeClass("state-loading").find("." + _widget.classes.widgetLoadingDiv).remove();
          //Set content to whatever was fetched.
          _widget.setContent(widget, data);
          core.debug.show(widget);

          //resolve promise
          core.mediator.publish(id, [widget]);

        }, function(mod) {
        }, {},{
          vm: true,
          js: false,
          css: true
        });
      
    }.defaults('', function(){}),

    onSettingsChange: function(widget, callback) {
      widget.bind("onSettingsChange", callback);
    },

    /**
     * @see Widget.refresh
     */
    refresh: function(widget, options) {

      if (this.is(widget)) {
        if (options.onlyContent) {
          //Load content again.
          _widget.load(widget);
        }
      }
    },

    /**
     * @see Widget.setMode
     */
    setMode: function(widget, mode, callbackOnEnd) {
      var self = this;

      if (this.is(widget)) {
        callbackOnEnd = callbackOnEnd || function(){};
        switch (mode) {
          case 1: //SETTINGS VIEW
            if (!widget.hasClass("state-settings")) {
              
             //Resize Widgets settings so that its the same size as content.
              widget.find("." + _widget.classes.widgetSettings).css({
                height: widget.find("." + _widget.classes.widgetContent).height()
              });
              
              //Flip to settings.
              widget.flip({
                direction: 'lr',
                color: "#F6F6F6",
                speed: 300,
                onEnd: function() {
                  widget.find("." + _widget.classes.widgetContent).hide();
                  widget.find("." + _widget.classes.widgetSettings).show();
                  
                  callbackOnEnd.apply(this, arguments);
                }
              });
              widget.find("." + _widget.classes.widgetConfigBtn).addClass(_widget.classes.widgetBtnActive);
              widget.removeClass("state-default").addClass("state-settings").addClass("state-editing");
            }
            break;
          case 2: //EDITTING VIEW
            if (!widget.hasClass("state-editing") && !widget.hasClass("state-settings")) {
              //Change to edit(show menu).
              widget.removeClass("state-default").addClass("state-editing");
            }
            break;
          default: //DEFAULT VIEW and any others unspecified.
            //Change to view. remove any other states.
            //If in settings then flip back.
            if (widget.hasClass("state-settings")) {
              widget.flip({
                direction: 'rl',
                color: "#F6F6F6",
                speed: 300,
                onEnd: function() {
                    widget.find("." + _widget.classes.widgetContent).show();
                    widget.find("." + _widget.classes.widgetSettings).hide();
                    callbackOnEnd.apply(this, arguments);
                  }
              });
            }
            //Remove any other states.
            widget.removeClass(widget.attr("class").match(/state-.*/)[0]).addClass("state-default");
            widget.find("." + _widget.classes.widgetConfigBtn).removeClass(_widget.classes.widgetBtnActive);
            break;
        }
        return true;
      }

      return false;
    },

    /**
     * Set the content of the widget. This overwrites existing content.
     *
     * @param {Selector} widgetboard WidgetBoard selector.
     * @param {String} content New content for the Widget(can be HTML).
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    setContent: function(widget, content) {
      var self = this;
 
      if (this.is(widget)) {
        //Replace widget content.
        widget.find("." + _widget.classes.widgetContent).html(content);
        return true;
      }

      return false;
    },

    /**
     * @see Widget.show
     */
    show: function(widget, mode) {

      if (this.is(widget)) {
        //Again, animation used for future probable use.
        core.dom.select(widget).show();
        widget.animate({
          opacity: '1'
        }, "fast", function() {
        });
        return true;
      }

      return false;
    }
  };

  /*************************************
   *               Public              *
   *************************************/

  var Widget = /** @lends Widget */{
    /**
     * Edit mode of the widget.
     *
     * @type {Number}
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    MODE_EDIT: _widget.modes["state-editing"],

    /**
     * Move mode of the widget.
     *
     * @type {Number}
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    MODE_MOVE: _widget.modes["state-moving"],

    /**
     * Configuration mode of the widget.
     *
     * @type {Number}
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    MODE_SETTINGS: _widget.modes["state-settings"],

    /**
     * View mode of the widget.
     *
     * @type {Number}
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    MODE_VIEW: _widget.modes["state-default"],

    /**
     * Initialize widget functionality of a widget. You can use the #renderWidget velocity macro to include the html for a widget and it's content.
     *
     * @param {CoreObj/Selector} selector The selector for widget element.
     * @return {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    create: function(selector) {
      return _widget.initialize(selector);
    }.defaults(''),

    /**
     * Remove widget completely from WidgetBoard.
     *
     * @param {Selector/CoreObj} widget The widget.
     * @return {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    destroy: function(widget) {
      //original?
      return _widget.destroy(widget);
    }.defaults(''),
    
    /**
     * Disable the widget.
     *
     * @param {Selector/CoreObj} widget The widget.
     * @return {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    disable: function(widget) {
      return _widget.disable(widget);
    }.defaults(''),
    
    /**
     * Enable the widget.
     *
     * @param {Selector/CoreObj} widget The widget.
     * @return {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    enable: function(widget) {
      return _widget.enable(widget);
    }.defaults(''),

    /**
     * Get the mode the widget is currently in.
     *
     * @param {Selector/CoreObj} widget The widget.
     * @return {Number} The mode the widget is currently in. Can be {@link Widget.MODE_VIEW}, {@link Widget.MODE_SETTINGS}, {@link Widget.MODE_MOVE} and {@link Widget.MODE_EDIT}.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    getMode: function(widget) {
      return _widget.getMode(widget);
    }.defaults(''),
    
    /**
     * Get selector of the parent WidgetBoard for this selector.
     *
     * @param {Selector/CoreObj} widget The widget.
     * @return {Selector} The parent WidgetBoard selector.
     * @responsibleAPI <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    getWidgetBoard: function(widget) {
      return _widget.getWidgetBoard(widget);
    }.defaults(''),

    /**
     * Hide the widget(does not remove from widgetboard).
     *
     * @param {Selector/CoreObj} widget The widget.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    hide: function(widget) {
      return _widget.hide(widget);
    }.defaults(''),

    /**
     * Is given element, a widget?
     *
     * @param {Selector/CoreObj} widget The widget.
     * @returns {Boolean} True if a valid selector and its a Widget, false otherwise.
     * @responsibleAPI <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    is: function(widget) {
      return _widget.is(widget);
    }.defaults(''),
    
    /**
     * Load Widget module.
     *
     * @param {Selector/CoreObj} widget The widget.
     * @responsibleAPI <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    load: function(widget) {
      _widget.load(widget);
    }.defaults(''),

    /**
     * Register callback to aquire current settings for widgets.
     * This is triggered when user confirms settings(clicks 'Ok' button).
     *
     * @param {Selector/CoreObj} widget The widget.
     * @param {Function} callback Callback that provides Event object and settings data.
     * @example
     * Widget.onSettingsChange("#widget", function(evt, settingsData){
         if (settingsData.disabled) {
           Widget.disable("#widget");
         }
       });
     * @responsibleAPI <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    onSettingsChange: function(widget, callback) {
      _widget.onSettingsChange(widget, callback);
    }.defaults('', function(evt, params){}),

    /**
     * Refresh the entire widget.
     *
     * @param {Selector/CoreObj} widget The widget.
     * @param {Object} options The options for the refresh.
     * @option {Function} callback The function executed on finish.
     * @option {Boolean} onlyContent=true Only refresh the content.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    refresh: function(widget, options) {
      return _widget.refresh(widget, options);
    }.defaults('', {
      onlyContent: true,
      callback: function() {}
    }),

    /**
     * Switch to a certain mode.
     *
     * @param {Selector/CoreObj} widget The widget.
     * @param {Number} mode The mode you want the widget to switch to. Can be {@link Widget.MODE_VIEW}, {@link Widget.MODE_SETTINGS} and {@link Widget.MODE_EDIT}.
     * @param {Function} onEnd Callback to be fired when fliping animation finish.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    setMode: function(widget, mode, onEnd) {
      return _widget.setMode(widget, mode, onEnd);
    }.defaults('', _widget.MODE_VIEW, function(){}),

    /**
     * Show the widget.
     *
     * @param {Selector/CoreObj} widget The widget.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    show: function(widget) {
      return _widget.show(widget);
    }
  };

  return Widget;
});

core.module.alias.add('Widget', 'library/uielements/widget');
core.module.use('Widget');
