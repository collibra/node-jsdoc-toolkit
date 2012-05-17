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
  
  var html = '<div class="test">'
    + '<button id="menuInit">Show menu</button>'
    + '</div>',
      menuItems = [
                   {
                     label: 'Action',
                     icon: false,
                     'default': true,
                     enabled: true,
                     group: 0,
                     on: {'click': function() { core.debug.show('Click! 0'); }},
                     menu: false
                   },
                           {
                     label: 'Fade Action',
                     group: 1,
                     on: {'click': function() { core.dom.select('BODY').fadeOut().fadeIn(); }}
                   },
                           {
                     label: 'Iconed Action',
                     icon: 'icon-add',
                     group: 1,
                     on: {'click': function() { core.debug.show('Click! 2'); }}
                   },
                           {
                     label: 'Subdivision',
                     icon: false,
                     group: 0,
                     on: {'click': function() { core.debug.show('Click! 3'); }},
                     menu: [
                       {
                         label: 'Subdivision',
                         icon: false,
                         'default': false,
                         enabled: true,
                         group: 0,
                         on: {'click': function() { core.debug.show('Click! 4'); }},
                         menu: false
                       },
                       {
                         label: 'Subdivision',
                         icon: false,
                         'default': false,
                         enabled: true,
                         group: 1,
                         on: {'click': function() { core.debug.show('Click! 5'); }},
                         menu: false
                       }
                     ]
                     }
                   ];

  new AsyncTestCase('uielements :: menu', {
    
    //////////
    ////////// create
    //////////
    testCreate: function(queue) {
      queue.call(function(callbacks) {
        core.dom.create(html).appendTo("BODY");
        
        var moduleLoaded = callbacks.add(function(Menu) {
          var fn = Menu.create,
              initiator = core.dom.select('#menuInit');
          
          assertFunction("This function doesn't exists", fn);
          
          assertTrue("Menu is not created", fn(initiator, menuItems));
          assertTrue("Can't find created menu", core.dom.select('#' + initiator.attr('menuid')).hasClass('ui-menu'));
          
        });
        core.module.use('Menu', moduleLoaded);
      });
    },
    
    //////////
    ////////// testShowOn
    //////////
    testShowOn: function(queue) {
      queue.call(function(callbacks) {
        core.dom.create(html).appendTo("BODY");
        
        var checkOn = callbacks.add(function() {
          //check are settings visible?
          assertTrue("Test element is not created", core.dom.is(core.dom.select('#testElement')));
        });
        
        var after = callbacks.add(function(Menu) {
          var initiator = core.dom.select('#menuInit');
          //create
          Menu.create(initiator, menuItems, {
            show: {
              on: function() {
                core.dom.create('<div id="testElement"/>', 'BODY');
                checkOn();
              }
            }
          });
          
          //show
          Menu.show(initiator);
        });

        core.module.use('Menu', after);
      });
    },
    
    //////////
    ////////// destroy
    //////////
    testDestroy: function(queue) {
      queue.call(function(callbacks) {
        core.dom.create(html).appendTo("BODY");
        
        var moduleLoaded = callbacks.add(function(Menu) {
          var fn = Menu.destroy,
              initiator = core.dom.select('#menuInit');
          
          assertFunction("This function doesn't exists", fn);
          
          assertTrue("Menu is not created", Menu.create(initiator, menuItems));
          assertTrue("Can't find created menu", core.dom.select('#' + initiator.attr('menuid')).hasClass('ui-menu'));
          
          assertTrue("Menu still exist!", Menu.destroy(initiator));
          assertFalse("I've found destroyed menu.", core.dom.select('#' + initiator.attr('menuid')).hasClass('ui-menu'));
          
        });
        core.module.use('Menu', moduleLoaded);
      });
    },
    
    //////////
    ////////// has
    //////////
    testHas: function(queue) {
      queue.call(function(callbacks) {
        core.dom.create(html).appendTo("BODY");
        
        var moduleLoaded = callbacks.add(function(Menu) {
          var fn = Menu.has,
              initiator = core.dom.select('#menuInit');
          
          assertFunction("This function doesn't exists", fn);
          
          assertTrue("Menu is not created", Menu.create(initiator, menuItems));
          assertTrue("Can't find created menu", core.dom.select('#' + initiator.attr('menuid')).hasClass('ui-menu'));
          
          assertTrue("Initiator doesn't have menu", Menu.has(initiator));
          
        });
        core.module.use('Menu', moduleLoaded);
      });
    },
    
    //////////
    ////////// hide
    //////////
    testHide: function(queue) {
      queue.call(function(callbacks) {
        core.dom.create(html).appendTo("BODY");
        
        var moduleLoaded = callbacks.add(function(Menu) {
          var fn = Menu.hide,
              initiator = core.dom.select('#menuInit');
          
          assertFunction("This function doesn't exists", fn);
          
          assertTrue("Menu is not created", Menu.create(initiator, menuItems));
          assertTrue("Can't find created menu", core.dom.select('#' + initiator.attr('menuid')).hasClass('ui-menu'));
          
          assertTrue("Can't show menu", Menu.show(initiator));
          assertTrue("Showed menu is not visible...", core.dom.select('#' + initiator.attr('menuid')).is(':visible'));
          
          assertTrue("Can't hide menu", Menu.hide(initiator));
          assertFalse("Hidden menu is still visible...", core.dom.select('#' + initiator.attr('menuid')).is(':visible'));
           
        });
        core.module.use('Menu', moduleLoaded);
      });
    },
    
    //////////
    ////////// show
    //////////
    testShow: function(queue) {
      queue.call(function(callbacks) {
        core.dom.create(html).appendTo("BODY");
        
        var moduleLoaded = callbacks.add(function(Menu) {
          var fn = Menu.show,
              initiator = core.dom.select('#menuInit');
          
          assertFunction("This function doesn't exists", fn);
          
          assertTrue("Menu is not created", Menu.create(initiator, menuItems));
          assertTrue("Can't find created menu", core.dom.select('#' + initiator.attr('menuid')).hasClass('ui-menu'));
          
          assertTrue("Can't show menu", Menu.show(initiator));
          
          assertTrue("Showed menu is not visible...", core.dom.select('#' + initiator.attr('menuid')).is(':visible'));
          
        });
        core.module.use('Menu', moduleLoaded);
      });
    },
    
    //////////
    ////////// show on click
    //////////
    testShowOnClick: function(queue) {
      queue.call(function(callbacks) {
        core.dom.create(html).appendTo("BODY");
        
        var moduleLoaded = callbacks.add(function(Menu) {
          var initiator = core.dom.select('#menuInit');
          
          assertTrue("Menu is not created", Menu.create(initiator, menuItems, {
            show: {
              trigger: 'click'
            }
          }));
          
          assertTrue("Can't find created menu", core.dom.select('#' + initiator.attr('menuid')).hasClass('ui-menu'));
          
          initiator.click();
          
          assertTrue("Showed menu is not visible...", core.dom.select('#' + initiator.attr('menuid')).is(':visible'));
          
        });
        core.module.use('Menu', moduleLoaded);
      });
    }
    
  });
  
}());