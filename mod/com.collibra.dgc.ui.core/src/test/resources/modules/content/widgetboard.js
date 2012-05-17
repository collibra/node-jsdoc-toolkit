(function() {
  "use strict";

  var widgetboard = '<div id="widgetboard"><div class="column" style="width: 100%"></div></div>',
      widget = '<div id="widget-test" class="widget state-default" name="widget-name" col="0" row="0" remote="true" path="widgets/helloWorld">'+
      '<div class="widget-container">'+
        '<div class="widget-head">'+
            '<div class="widget-title">' +
                'widget Title' +
            '</div>' +
            '<div class="widget-menu">' +
              '<ul class="widget-menu-items">' +
                '<li class="widget-menu-item widget-config"><i class="icon-cogwheel"></i></li>' +
                '<li class="widget-menu-item widget-handle"><i class="icon-move"></i></li>' +
                '<li class="widget-menu-item widget-remove"><i class="icon-delete"></i></li>' +
              '</ul>' +
            '</div>' +
        '</div>' +
        '<div class="widget-content">' +
            '<div class="widget-loading-content">Loading ...<i class="loading-indicator"></i></div>' +
        '</div>' +
        '<div class="widget-settings">' +
          '<div class="widget-settings-content">' +
            '<div class="widget-settings-common">' +
              '<input type="checkbox" setting="default.locked" name="widget-locked"/><label for="widget-locked">Locked</label>' +
            '</div>' +
            '<fieldset class="widget-settings-custom">' +
              '<legend>Content</legend><p>' +
                    '<input setting="$key" placeholder="$map.placeholder" type="$map.type" id="widget-${hashCode}-setting-${key}" />' +
                    '<label for="widget-${hashCode}-setting-${key}">$map.label</label></p>' +
            '</fieldset>' +
          '</div>' +
          '<div class="widget-settings-buttons">' +
            '<button class="btn-success no-label"><i class="icon-ok white"></i></button>' +
            '<button class="btn-danger no-label"><i class="icon-delete"></i></button>' +
          '</div>' +
        '</div>' +
      '</div>' +
      '</div>';

  AsyncTestCase("modules :: content :: widgetboard.js", {

    //////////
    ////////// create
    //////////
    testCreate: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(WidgetBoard) {
          var w = callbacks.add(function(Widget) {
            var callback = sinon.spy();
            core.mediator.subscribe("widgetboard::#widgetboard::widgets::loaded", callback);
            
            assertFunction('create of the Widget module is not a function!', Widget.create);
            assertFunction('create of the WidgetBoard module is not a function!', WidgetBoard.create);
            
            core.dom.create(widgetboard).appendTo("BODY");
            core.dom.select(".column").append(widget);
            
            //check for overall creation
            assertTrue("WidgetBoard wasn't created.", WidgetBoard.create("#widgetboard"));
            assertFalse("WidgetBoard is created.", WidgetBoard.create("#widgetboard"));
            
            //check for sortable
            assertTrue("Sortable isn't applied", core.dom.select(".column").hasClass("ui-sortable"));
            
            //check if all widget were loaded
            //assertTrue("Widget weren't loaded.", callback.calledOnce);
            
          });
          core.module.use('Widget', w);
        });
        
        core.module.use('WidgetBoard', moduleLoaded);
      });
      
      //check if all widgets were loaded
      queue.call(function() {
        
        
      });
    },
    
    //////////
    ////////// disable
    //////////
    testDisable: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(WidgetBoard) {
          var disable = WidgetBoard.disable;
          
          assertFunction('disable of the WidgetBoard module is not a function!', disable);
          core.dom.create(widgetboard).appendTo("BODY");
          core.dom.select(".column").append(widget);
          
          //check for overall creation
          assertTrue("WidgetBoard wasn't created.", WidgetBoard.create("#widgetboard"));
          
          //check if all widgets have overlay applied
          core.dom.each(core.dom.select(".widget"), function(widget) {
            assertFalse("Widget is already disabled.", core.dom.is(core.dom.select(widget).find(".widget-overlay")));
          });
          
          assertTrue("WidgetBoard widgets disabling failed.", disable("#widgetboard"));
          
          core.dom.each(core.dom.select(".widget"), function(widget) {
            assertTrue("Widget isn't disabled.", core.dom.is(core.dom.select(widget).find(".widget-overlay")));
          });
          
        });
        
        core.module.use('WidgetBoard', moduleLoaded);
      });
    },
    
    //////////
    ////////// enable
    //////////
    testEnable: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(WidgetBoard) {
          var enable = WidgetBoard.enable;
          
          assertFunction('enable of the WidgetBoard module is not a function!', enable);
          core.dom.create(widgetboard).appendTo("BODY");
          core.dom.select(".column").append(widget);
          
          //check for overall creation
          assertTrue("WidgetBoard wasn't created.", WidgetBoard.create("#widgetboard"));
          
          //check if all widgets have overlay applied
          
          assertTrue("WidgetBoard widgets disabling failed.", WidgetBoard.disable("#widgetboard"));
          
          core.dom.each(core.dom.select(".widget"), function(widget) {
            assertTrue("Widget is already enabled.", core.dom.is(core.dom.select(widget).find(".widget-overlay")));
          });
          
          assertTrue("WidgetBoard widgets enabling failed.", enable("#widgetboard"));
          
          core.dom.each(core.dom.select(".widget"), function(widget) {
            assertFalse("Widget isn't enabled.", core.dom.is(core.dom.select(widget).find(".widget-overlay")));
          });
        });
        
        core.module.use('WidgetBoard', moduleLoaded);
      });
    },
    
    //////////
    ////////// is
    //////////
    testIs: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(WidgetBoard) {
          var is = WidgetBoard.is;
          
          assertFunction('is of the WidgetBoard module is not a function!', is);
          
          core.dom.create(widgetboard).appendTo("BODY");
          core.dom.select(".column").append(widget);
          
          //check for overall creation
          assertFalse("WidgetBoard is already created.", WidgetBoard.is("#widgetboard"));
          assertTrue("WidgetBoard wasn't created.", WidgetBoard.create("#widgetboard"));
          assertTrue("WidgetBoard checked and it's not created.", WidgetBoard.is("#widgetboard"));
          
        });
        
        core.module.use('WidgetBoard', moduleLoaded);
      });
    },
    
    //////////
    ////////// getWidgets
    //////////
    testGetWidgets: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(WidgetBoard) {
          var getWidgets = WidgetBoard.getWidgets,
              widgets = [];
          
          assertFunction('getWidgets of the WidgetBoard module is not a function!', getWidgets);
          core.dom.create(widgetboard).appendTo("BODY");
          core.dom.select(".column").append(widget);
          
          //check for overall creation
          assertTrue("WidgetBoard wasn't created.", WidgetBoard.create("#widgetboard"));
          widgets = getWidgets("#widgetboard");
          assertObject("Collected widgets[0] is not widget object info.", widgets);
          assertNotUndefined("Collected widgets are wrong.", widgets["widget-test"]);
          
        });
        
        core.module.use('WidgetBoard', moduleLoaded);
      });
    },
    
    //////////
    ////////// getWidgetsMode
    //////////
    testGetWidgetsMode: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(WidgetBoard) {
          var getWidgetsMode = WidgetBoard.getWidgetsMode;
          
          assertFunction('getWidgetsMode of the WidgetBoard module is not a function!', getWidgetsMode);
          core.dom.create(widgetboard).appendTo("BODY");
          core.dom.select(".column").append(widget);
          
          //check for overall creation
          assertTrue("WidgetBoard wasn't created.", WidgetBoard.create("#widgetboard"));

          assertEquals("Widgets mode are wrong.", [0], getWidgetsMode("#widgetboard"));
          
        });
        
        core.module.use('WidgetBoard', moduleLoaded);
      });
    },
    
    //////////
    ////////// setWidgetsMode
    //////////
    testSetWidgetsMode: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(WidgetBoard) {
          var setWidgetsMode = WidgetBoard.setWidgetsMode;
          
          assertFunction('setWidgetsMode of the WidgetBoard module is not a function!', setWidgetsMode);
          core.dom.create(widgetboard).appendTo("BODY");
          core.dom.select(".column").append(widget);
          
          //check for overall creation
          assertTrue("WidgetBoard wasn't created.", WidgetBoard.create("#widgetboard"));

          //check all states
          assertTrue("Widgets mode is not in default state.", core.dom.select("#widget-test").hasClass("state-default"));
          assertTrue("Setting widgets mode failed.", setWidgetsMode("#widgetboard", 1));
          assertTrue("Widgets mode is not changed to settings.", core.dom.select("#widget-test").hasClass("state-settings"));
          assertTrue("Setting widgets mode failed.", setWidgetsMode("#widgetboard", 0));
          assertTrue("Widgets mode is not changed to default.", core.dom.select("#widget-test").hasClass("state-default"));
          assertTrue("Setting widgets mode failed.", setWidgetsMode("#widgetboard", 2));
          assertTrue("Widgets mode is not changed to editting.", core.dom.select("#widget-test").hasClass("state-editing"));
          
        });
        
        core.module.use('WidgetBoard', moduleLoaded);
      });
    }
  });
}());