/**
 * Widget Board.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
 * @module modules/layout/widgetboard
 * @alias WidgetBoard
 * @namespace modules/layout
 */
/*global define,require,core, console */
define('modules/layout/widgetboard',
       ['core', 'library/uielements/widget'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      Widget = require('library/uielements/widget');

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _widgetBoard = {

    /**
     * @see WidgetBoard.create
     */
    create: function(widgetboard, options) {
      var _self = this,
          selector = widgetboard;

      widgetboard = core.dom.select(selector);

      if (!core.dom.is(widgetboard)) {
        throw "Widgetboard content is empty, cannot proceed.";
      }
      
      //Continue only if selector is not already a WidgetBoard.
      if (!this.is(widgetboard)) {

        //Add .widgetboard class to the selector.
        widgetboard.addClass("widgetboard");

        //Put data into $.data for future use.
        widgetboard.data({
          widgetboard: {
            module: this,
            selector: selector,
            widgets: []
          }
        });

        //Initialize $.sortable.
        this.makeSortable(widgetboard, options);

        //Collect rendered widgets.
        this.collectWidgets(widgetboard, options);

        //Iterate over found widgets and run its initialize method
        //Which binds events and start loading data(if necessary)
        this.initializeWidgets(widgetboard);
        
        //Subscribe to WidgetBoard events.
        
        //Tell WidgetBoard to load it's widget.
        core.mediator.subscribe('widgetboard::' + selector + '::widgets::load', function() {
          _self.runOnWidgets(widgetboard, function(widget) {
            Widget.load(widget.elem);
          });
        });
        
        //Refresh all widgets.
        core.mediator.subscribe('widgetboard::' + selector + '::widgets::refresh', function() {
          _self.collectWidgets(widgetboard, options);
          _self.runOnWidgets(widgetboard, function(widget) {
            Widget.refresh(widget.elem);
          });
        });
        
        //Enter the settings state of the widget board.
        //This will show the widget library and enable the user to move, remove and edit widgets.
        core.mediator.subscribe('widgetboard::' + selector + '::settings::enter', function() {
          _self.setWidgetsMode(widgetboard, 1, function() {
            //Notify when entered.
            core.mediator.publish('widgetboard::' + selector + '::settings::entered');
          });
        });
        
        //Exit the settings state.
        core.mediator.subscribe('widgetboard::' + selector + '::settings::exit', function() {
          _self.setWidgetsMode(widgetboard, 0, function() {
            //Notify when exited.
            core.mediator.publish('widgetboard::' + selector + '::settings::exited');
          });
        });

        return true;
      }
      
      return false;
    },

    /**
     * @see WidgetBoard.disable
     */
    disable: function(widgetboard) {
      var _self = this,
          widgets = null;

      widgetboard = core.dom.select(widgetboard);
      if (this.is(widgetboard)) {
        _self.runOnWidgets(widgetboard, function(widget) {
          Widget.disable(widget.elem);
          //Change mode to default view.
          Widget.setMode(widget.elem, 0);
        });

        return true;
      }
      
      return false;
    },

    /**
     * @see WidgetBoard.enable
     */
    enable: function(widgetboard) {
      var _self = this,
          widgets = null;

      widgetboard = core.dom.select(widgetboard);
      if (this.is(widgetboard)) {
        _self.runOnWidgets(widgetboard, function(widget) {
          Widget.enable(widget.elem);
        });
        
        return true;
      }
      
      return false;
    },

    /**
     * @see WidgetBoard.getWidgetsMode
     */
    getWidgetsMode: function(widgetboard) {
      var _self = this,
          widgets = null,
          modes = [];

      widgetboard = core.dom.select(widgetboard);
      if (this.is(widgetboard)) {
        _self.runOnWidgets(widgetboard, function(widget) {
          modes.push(Widget.getMode(widget.elem));
        });

        return modes;
      }

      return false;
    },

    /**
     * @see WidgetBoard.getWidgets
     */
    getWidgets: function(widgetboard) {

      widgetboard = core.dom.select(widgetboard);
      if (this.is(widgetboard)) {
        return widgetboard.data("widgetboard").widgets;
      }

      return false;
    },

    /**
     * @see WidgetBoard.is
     */
    is: function(widgetboard) {
      widgetboard = core.dom.select(widgetboard);

      if (core.dom.is(widgetboard) && widgetboard.hasClass("widgetboard")) {
        return true;
      }

      return false;
    },


    /**
     * Internal method for instantiating $.sortable plugin for WidgetBoard.
     *
     * @param {Selector} widgetboard WidgetBoard selector.
     * @param {Selector} options Options for sortable plugin(same as in {@link Widget.create}).
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    makeSortable: function(widgetboard, options) {
      //Apply sortable plugin
      widgetboard.find("." + options.column.className).sortable({
        // Specify those items which will be sortable:
        items: core.dom.select("." + options.widget.className),
        // Connect each column with every other column:
        connectWith: core.dom.select("." + options.column.className),
        // Set the handle to the top bar:
        handle: "." + options.widget.handle,
        // Define class of placeholder
        placeholder: options.placeholder.className,
        // Make sure placeholder size is retained:
        forcePlaceholderSize: options.placeholder.forceSize,
        //What is the tolerance for overlap? Either at pointer or at 50% overlap.
        tolerance: options.tolerance,
        // Animated revert, how long should it take?
        revert: options.revertSpeed,
        // Delay before action:
        delay: options.delay,
        // Opacity of 'helper' (the thing that's dragged):
        opacity: options.helperOpacity,
        containment: "document",
        // Function to be called when dragging starts:
        start: function (e, ui) {
            ui.helper.addClass('state-moving');
            ui.placeholder.css("height", "+=21");
            //ui.item.css("margin-top", "-20px");
            options.onStartDrag.apply(this, arguments);
        },
        // Function to be called when dragging stops:
        stop: function (e, ui) {
            // Reset width of units and remove dragging class:
            ui.item.css({width:''}).removeClass('state-moving');
            // Re-enable sorting.
            core.dom.select(options.column).sortable('enable');
            options.onStopDrag.apply(this, arguments);
        }
      });
    },

    /**
     * Internal method for collecting(searching through DOM) for widgets rendered in this Widgetboard.
     *
     * @param {Selector} widgetboard WidgetBoard selector.
     * @param {Selector} options Options for sortable plugin.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    collectWidgets: function(widgetboard, options) {
      var widgets = widgetboard.find("." + options.widget.className);

      widgetboard.data("widgetboard").widgets = {};
      //For each widget..
      core.dom.each(widgets, function(elem) {
        //..save it's data in WidgetBoard's $.data object.
        widgetboard.data("widgetboard").widgets[elem.attr("id")] = {
          elem: elem,
          col: elem.attr("col"),
          row: elem.attr("row"),
          path: elem.attr("path"),
          state: "pending"
        };
      });

      return true;
    },

    /**
     * Internal method for invoking Widget.create on every widget found in this WidgetBoard.
     *
     * @param {Selector} widgetboard WidgetBoard selector.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    initializeWidgets: function(widgetboard) {
      var _self = this,
          loaded = 0,
          selector = widgetboard.data("widgetboard").selector,
          widgets = widgetboard.data("widgetboard").widgets;

      _self.runOnWidgets(widgetboard, function(widget) {
        Widget.create(widget.elem);
        
        //Subscribe to every Widget load method.
        core.mediator.subscribe("widget::" + widget.elem.attr("name") + "::" + widget.elem.attr("id") + "::loaded", function(widget) {
          //widgetboard.data("widgetboard").widgets[widget.elem.attr("id")].state = "loaded";
          loaded = loaded + 1;

          //If all Widgets are loaded, fire "..widgets::loaded" event.
          if (loaded === Object.keys(widgets).length) {
            
            //Fire loaded event.
            core.mediator.publish("widgetboard::" + selector + "::widgets::loaded");
            //Make widgetboard visible.
            //widgetboard.show();
          }
        });
        
      });

      return true;
    },

    /**
     * Internal method for executing actions on Widgets in WidgetBoard.
     *
     * @param {Selector} widgetboard WidgetBoard selector.
     * @param {Function} action Function to be executed on each Widget.
     *  @option {Selector} widget jQuery selector object of the current Widget.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    runOnWidgets: function(widgetboard, action) {
      var _self = this,
          widgets = null;

      widgetboard = core.dom.select(widgetboard);
      if (core.dom.is(widgetboard)) {
        widgets = widgetboard.data("widgetboard").widgets;
        core.object.each(widgets, function(id, widget) {
          action.apply(this, [widget]);
        });
        return true;
      }
      return false;
    },

    /**
     * @see WidgetBoard.setWidgetsMode
     */
    setWidgetsMode: function(widgetboard, mode, onEnd) {
      var _self = this,
          widgets = null;

      widgetboard = core.dom.select(widgetboard);
      if (core.dom.is(widgetboard)) {
        _self.runOnWidgets(widgetboard, function(widget) {
          Widget.setMode(widget.elem, mode, onEnd);
        });

        return true;
      }

      return false;
    }
  };

  /*************************************
   *               Public              *
   *************************************/

  var WidgetBoard = /** @lends WidgetBoard */{

    /**
     * Initiate WidgetBoard functionality on a container selector.
     * This is where plug-ins are applied, events bindings are created
     * and also Widgets(in this WidgetBoard) are collected and initialized.
     *
     * @param {Selector} widgetboard WidgetBoard selector.
     * @param {Selector} [options] Options for WidgetBoard.
     *  @option {Integer} column.className="column"   Name of the column CSS class.
     *  @option {Integer} widget.className="widget"   Name of the Widget CSS class.
     *  @option {Integer} widget.handle="widget-handle"  Name of the Widget move handle(the middle move button) CSS class.
     *  @option {Integer} placeholder.className="widget-placeholder" Number of columns to render in WidgetBoard.
     *  @option {Integer} placeholder.forceSize=true  Force placeholder to change its size to the size of the Widget that is being moved.
     *  @option {Integer} tolerance="pointer"         How to predict which placeholder to use when moving Widget.
     *                                                When mouse pointer touches placeholder(pointer) or when Widget and placeholder
     *                                                overlap in 50% of their size.
     *  @option {Integer} revertSpeed=300             Speed of animation when reverting Widget back to its original position.
     *  @option {Integer} delay=0                     Delay in ms when moving should start(counting starts after grabing Widget by move button).
     *  @option {Integer} helperOpacity=0.8           Opacity of the Widget helper(the thing that's dragged) when moving.
     *  @option {Integer} containment="#widgetboard"  Constraint movement to containment dimensions.
     *  @option {Integer} onStartDrag=function(){}    Callback to be executed when dragging is started.
     *  @option {Integer} onStopDrag=function(){}     Callback to be executed when dragging is finished.
     * @returns {Boolean} True when success, false otherwise.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    create: function(widgetboard, options) {
      return _widgetBoard.create(widgetboard, options);
    }.defaults({
      column: {
        className: "column"
      },
      widget: {
        className: "widget",
        handle: "widget-handle"
      },
      placeholder: {
        className: "widget-placeholder",
        forceSize: true
      },
      tolerance: "pointer",
      revertSpeed: 300,
      delay: 0,
      helperOpacity: 0.8,
      containment: "#widgetboard",
      onStartDrag: function() {},
      onStopDrag: function() {}
    }),

    /**
     * Disable WidgetBoard functionality completely(this also disables all Widgets inside).
     *
     * @param {Selector} widgetboard WidgetBoard selector.
     * @returns {Boolean} True on success, false on failure.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    disable: function(widgetboard) {
      return _widgetBoard.disable(widgetboard);
    },

    /**
     * Enable WidgetBoard functionality completely(this also enables all Widgets inside).
     *
     * @param {Selector} widgetboard WidgetBoard selector.
     * @returns {Boolean} True on success, false on failure.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    enable: function(widgetboard) {
      return _widgetBoard.enable(widgetboard);
    },

    /**
     * Is given selector a created WidgetBoard?
     *
     * @param {Selector} widgetboard Widgetboard selector.
     * @returns {Boolean} Whether given selector is a Widgetboard and valid jQuery selector or not.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    is: function(widgetboard) {
      return _widgetBoard.is(widgetboard);
    },

    /**
     * Get each Widget's mode.
     *
     * @param {Selector} widgetboard Widgetboard selector.
     * @returns {Array} Returns array containing Widget's modes or false if failed.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    getWidgetsMode: function(widgetboard) {
      return _widgetBoard.getWidgetsMode(widgetboard);
    },

    /**
     * Retrieve all Widgets inside certain WidgetBoard.
     *
     * @param {Selector} widgetboard Widgetboard selector.
     * @returns {Array} Returns array containing WidgetBoard Widget's and info about them or false if failed.
     * <p>
     * [{
     *  col: 0,  //column where Widget is rendered
     *  row: 0,  //row where Widget is rendered
     *  elem: jQuery('div.widget'),  //reference to Widget's jQuery object.
     *  path: "widgets/someWidget"  //Widget's module path
     * }]
     * </p>
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    getWidgets: function(widgetboard) {
      return _widgetBoard.getWidgets(widgetboard);
    },

    /**
     * Set mode for all Widgets inside this WidgetBoard.
     * Modes are the same as in Widget: {@link Widget.MODE_VIEW}, {@link Widget.MODE_SETTINGS} and {@link Widget.MODE_EDIT}.
     * These are just enumeration values so you can use the following modes numbers respectively: 0, 1 and 2.
     *
     * @param {Selector} widgetboard WidgetBoard selector.
     * @param {Number} mode New mode for widgets.
     * @param {Function} onEnd Callback to be fired when fliping animation finishes.
     * @returns {Boolean} True on success, false on failure.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    setWidgetsMode: function(widgetboard, mode, onEnd) {
      return _widgetBoard.setWidgetsMode(widgetboard, mode, onEnd);
    }
  };

  return WidgetBoard;
});

core.module.alias.add('WidgetBoard', 'modules/layout/widgetboard');
core.module.use('WidgetBoard');
