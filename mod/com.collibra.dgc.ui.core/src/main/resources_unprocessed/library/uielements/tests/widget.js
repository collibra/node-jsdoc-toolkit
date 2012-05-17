/*global
AsyncTestCase
assertFunction
assertEquals
assertTrue
assertObject
assertFalse
sinon
jstestdriver
console
core
*/
(function() {
  "use strict";
  
  seajs.config({ 
    base: location.protocol + '//' + location.host + '$!{contextPath}/resources',
    alias: {
      "core": "$!{contextPath}resources/library/core/core.js"
    }
  });
  
  var widgetboard = '<div id="widgetboard"><div class="column" style="width: 100%"></div></div>',
      widget = '<div id="widget-test" class="widget state-default" name="tested" col="0" row="0" remote="true" path="widgets/helloWorld">'+
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

  new AsyncTestCase('uielements :: widget', {

    //////////
    ////////// create
    //////////
    testCreate: function(queue) {
      var loadCallback = sinon.spy();
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Widget) {
          var create = Widget.create;
          assertFunction('create of the Widget module is not a function!', create);
          
          core.module.get("Widget");
          core.module.get("library/uielements/widget");
          core.module.get("widgets/helloWorld");
          
//          assertFunction('create of the Widget module is not a function!', create);
//          core.dom.create(widgetboard).appendTo("BODY");
//          core.dom.select(".column").append(widget);
//          
//          //subscribe to loaded event
//          core.mediator.subscribe("widget::tested::#widget-test::loaded", loadCallback);
//          
//          //create and load widget
//          assertTrue("Widget wasn't created.", create("#widget-test"));
//          assertTrue("Widget doesn't exsits.", Widget.is("#widget-test"));
          
        });
        
        core.module.use('Widget', moduleLoaded);
      });
      
      queue.call(function(callbacks) {
        //check if widgdet was loaded
        //assertTrue("Widget weren't loaded.", loadCallback.calledOnce);
      });
    },
      
    //////////
    ////////// destroy
    //////////
    testDestroy: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Widget) {
          var destroy = Widget.destroy;
          
          assertFunction('destroy of the Widget module is not a function!', destroy);
          
          
        });
        
        core.module.use('Widget', moduleLoaded);
      });
    },
    
    //////////
    ////////// disable
    //////////
    testDisable: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Widget) {
          var disable = Widget.disable;
          
          assertFunction('disable of the Widget module is not a function!', disable);
          
          
        });
        
        core.module.use('Widget', moduleLoaded);
      });
    },
    
    //////////
    ////////// enable
    //////////
    testEnable: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Widget) {
          var enable = Widget.enable;
          
          assertFunction('enable of the Widget module is not a function!', enable);
          
          
        });
        
        core.module.use('Widget', moduleLoaded);
      });
    },
    
    //////////
    ////////// getMode
    //////////
    testGetMode: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Widget) {
          var getMode = Widget.getMode;
          
          assertFunction('getMode of the Widget module is not a function!', getMode);
          
          
        });
        
        core.module.use('Widget', moduleLoaded);
      });
    },
    
    //////////
    ////////// getWidgetBoard
    //////////
    testGetWidgetBoard: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Widget) {
          var getWidgetBoard = Widget.getWidgetBoard;
          
          assertFunction('getWidgetBoard of the Widget module is not a function!', getWidgetBoard);
          
          
        });
        
        core.module.use('Widget', moduleLoaded);
      });
    },
    
    //////////
    ////////// hide
    //////////
    testHide: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Widget) {
          var hide = Widget.hide;
          
          assertFunction('hide of the Widget module is not a function!', hide);
          
          
        });
        
        core.module.use('Widget', moduleLoaded);
      });
    },
    
    //////////
    ////////// is
    //////////
    testIs: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Widget) {
          var is = Widget.is;
          
          assertFunction('is of the Widget module is not a function!', is);
          
          
        });
        
        core.module.use('Widget', moduleLoaded);
      });
    },
    
    //////////
    ////////// onSettingsChange
    //////////
    testOnSettingsChange: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Widget) {
          var onSettingsChange = Widget.onSettingsChange;
          
          assertFunction('onSettingsChange of the Widget module is not a function!', onSettingsChange);
          
          
        });
        
        core.module.use('Widget', moduleLoaded);
      });
    },
    
    //////////
    ////////// load
    //////////
    testLoad: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Widget) {
          var load = Widget.load;
          
          assertFunction('load of the Widget module is not a function!', load);
          
          
        });
        
        core.module.use('Widget', moduleLoaded);
      });
    },
    
    //////////
    ////////// refresh
    //////////
    testRefresh: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Widget) {
          var refresh = Widget.refresh;
          
          assertFunction('refresh of the Widget module is not a function!', refresh);
          
          
        });
        
        core.module.use('Widget', moduleLoaded);
      });
    },
    
    //////////
    ////////// setMode
    //////////
    testSetMode: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Widget) {
          var setMode = Widget.setMode;
          
          assertFunction('setMode of the Widget module is not a function!', setMode);
          
          
        });
        
        core.module.use('Widget', moduleLoaded);
      });
    },
    
    //////////
    ////////// show
    //////////
    testShow: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Widget) {
          var show = Widget.show;
          
          assertFunction('show of the Widget module is not a function!', show);
          
          
        });
        
        core.module.use('Widget', moduleLoaded);
      });
    }
  });
}());