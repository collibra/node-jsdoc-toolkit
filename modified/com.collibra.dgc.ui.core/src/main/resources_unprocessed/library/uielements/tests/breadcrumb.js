/*global
AsyncTestCase
assertFunction
assertEquals
assertNotEquals
assertTrue
assertFalse
core
*/
(function() {
  "use strict";
  var testBreadcrumb = "<div id='breadcrumbtest'><ul><li class='home'><a href='#'>Home</a></li><li><a href='#'>A</a></li><li><a href='#'>B</a></li><li><a href='#'>C</a></li></ul></div>",
      breadcrumbID = "#breadcrumbtest";

  new AsyncTestCase('uielements :: breadcrumb', {
    
    //////////
    ////////// addEntry
    //////////
    testAddEntry: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(BreadCrumb) {
          var addEntry = BreadCrumb.addEntry,
              breadcrumbElement,
              func = function(item){
                       item.text('A');
                     };

          core.dom.create(testBreadcrumb).appendTo("BODY");

          assertFunction('removelast of the breadcrumb module is not a function!', addEntry);

          BreadCrumb.create(breadcrumbID);
          breadcrumbElement = core.dom.select(breadcrumbID);

          addEntry(breadcrumbID, 'abcd', "http://www.collibra.com", 0);
          assertEquals('abcd', breadcrumbElement.find('li.last').text());
          assertEquals('http://www.collibra.com', breadcrumbElement.find('li.last a').attr('href'));

          addEntry(breadcrumbID, 'dcba', "http://www.123.com", 99);
          assertEquals('dcba', breadcrumbElement.find('li.last').text());
          assertEquals('http://www.123.com', breadcrumbElement.find('li.last a').attr('href'));

          addEntry(breadcrumbID, 'xyz', "http://www.xyz.com", 2);
          assertEquals('xyz', breadcrumbElement.find('li:nth-child(3)').text());
          assertEquals('http://www.xyz.com', breadcrumbElement.find('li:nth-child(3) a').attr('href'));

          addEntry(breadcrumbID, 'abcd', func, 0);
          assertEquals('A', breadcrumbElement.find('li.last').text());

          addEntry(breadcrumbID, 'dcba', func, 99);
          assertEquals('A', breadcrumbElement.find('li.last').text());

          addEntry(breadcrumbID, 'xyz', func, 2);
          assertEquals('A', breadcrumbElement.find('li:nth-child(3)').text());
        });
        
        core.module.use('BreadCrumb', moduleLoaded);
      });
    },
    
    //////////
    ////////// create
    //////////
    testCreate: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(BreadCrumb) {
          var create = BreadCrumb.create;

          core.dom.create(testBreadcrumb).appendTo("BODY");

          assertFunction('create of the breadcrumb module is not a function!', create);

          create(breadcrumbID);
          assertTrue('Home element does not have class "first".', core.dom.select(breadcrumbID).find('li').first().hasClass('first'));
          
        });
        
        core.module.use('BreadCrumb', moduleLoaded);
      });
    },
    
    //////////
    ////////// changeAction
    //////////
    testChangeAction: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(BreadCrumb) {
          var changeAction = BreadCrumb.changeAction,
              breadcrumbElement,
              func = function(item){
                       item.text('A');
                     };

          core.dom.create(testBreadcrumb).appendTo("BODY");

          assertFunction('create of the breadcrumb module is not a function!', changeAction);

          BreadCrumb.create(breadcrumbID);
          breadcrumbElement = core.dom.select(breadcrumbID);

          changeAction(breadcrumbID, 2, "http://www.collibra.com");
          assertEquals('http://www.collibra.com', breadcrumbElement.find('li:nth-child(2) a').attr('href'));

          changeAction(breadcrumbID, 0, "http://www.abcd.com");
          assertEquals('http://www.abcd.com', breadcrumbElement.find('li.last a').attr('href'));

          changeAction(breadcrumbID, 0, func);
          assertEquals('A', breadcrumbElement.find('li.last').text());

          changeAction(breadcrumbID, 2, func);
          assertEquals('A', breadcrumbElement.find('li:nth-child(2)').text());
        });
        
        core.module.use('BreadCrumb', moduleLoaded);
      });
    },
    
    //////////
    ////////// changeLabel
    //////////
    testChangeLabel: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(BreadCrumb) {
          var changeLabel = BreadCrumb.changeLabel,
              breadcrumbElement;

          core.dom.create(testBreadcrumb).appendTo("BODY");

          assertFunction('create of the breadcrumb module is not a function!', changeLabel);

          BreadCrumb.create(breadcrumbID);
          breadcrumbElement = core.dom.select(breadcrumbID);

          changeLabel(breadcrumbID, 2, "123456789");
          assertEquals('123456789', breadcrumbElement.find('li:nth-child(2) a').text());
          
          changeLabel(breadcrumbID, 0, "last one");
          assertEquals('last one', breadcrumbElement.find('li.last a').text());
        });
        
        core.module.use('BreadCrumb', moduleLoaded);
      });
    },
    
    //////////
    ////////// is
    //////////
    testIs: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(BreadCrumb) {
          var is = BreadCrumb.is;

          core.dom.create(testBreadcrumb).appendTo("BODY");

          assertFunction('create of the breadcrumb module is not a function!', is);

          assertFalse("Unstyled list is recognised as breadcrumb.", is(breadcrumbID));
          BreadCrumb.create(breadcrumbID);
          assertTrue("Styled list is not recognised as breadcrumb.", is(breadcrumbID));
        });
        
        core.module.use('BreadCrumb', moduleLoaded);
      });
    },
    
    //////////
    ////////// pop
    //////////
    testPop: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(BreadCrumb) {
          var pop = BreadCrumb.pop,
              breadcrumbElement;

          core.dom.create(testBreadcrumb).appendTo("BODY");

          assertFunction('create of the breadcrumb module is not a function!', pop);

          BreadCrumb.create(breadcrumbID);
          breadcrumbElement = core.dom.select(breadcrumbID);

          assertEquals('C', breadcrumbElement.find('li.last').text());
          pop(breadcrumbID);
          assertNotEquals('C', breadcrumbElement.find('li.last').text());
        });
        
        core.module.use('BreadCrumb', moduleLoaded);
      });
    },
    
    //////////
    ////////// push
    //////////
    testPush: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(BreadCrumb) {
          var push = BreadCrumb.push,
              breadcrumbElement,
              func = function(item){
                       item.text('A');
                     };

          core.dom.create(testBreadcrumb).appendTo("BODY");

          assertFunction('create of the breadcrumb module is not a function!', push);

          BreadCrumb.create(breadcrumbID);
          breadcrumbElement = core.dom.select(breadcrumbID);

          assertEquals('C', breadcrumbElement.find('li.last').text());
          push(breadcrumbID, 'Z', 'www.abc.com');
          assertEquals('Z', breadcrumbElement.find('li.last').text());

          push(breadcrumbID, 'BBB', func);
          assertEquals('A', breadcrumbElement.find('li.last').text());
        });
        
        core.module.use('BreadCrumb', moduleLoaded);
      });
    },
    
    //////////
    ////////// remove
    //////////
    testRemove: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(BreadCrumb) {
          var remove = BreadCrumb.remove,
              breadcrumbElement;

          core.dom.create(testBreadcrumb).appendTo("BODY");

          assertFunction('create of the breadcrumb module is not a function!', remove);

          BreadCrumb.create(breadcrumbID);
          breadcrumbElement = core.dom.select(breadcrumbID);

          remove(breadcrumbID, true);
          assertEquals(0, breadcrumbElement.find('ul').length);
        });
        
        core.module.use('BreadCrumb', moduleLoaded);
      });
    },

    //////////
    ////////// removeAllEntries
    //////////
    testRemoveAllEntries: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(BreadCrumb) {
          var removeAllEntries = BreadCrumb.removeAllEntries,
              breadcrumbElement;

          core.dom.create(testBreadcrumb).appendTo("BODY");

          assertFunction('removelast of the breadcrumb module is not a function!', removeAllEntries);

          BreadCrumb.create(breadcrumbID);
          breadcrumbElement = core.dom.select(breadcrumbID);

          assertEquals(4, breadcrumbElement.find('ul li').length);
          removeAllEntries(breadcrumbID);
          assertEquals(1, breadcrumbElement.find('ul li').length);
          
        });
        
        core.module.use('BreadCrumb', moduleLoaded);
      });
    },

    //////////
    ////////// removeEntry
    //////////
    testRemoveEntry: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(BreadCrumb) {
          var removeEntry = BreadCrumb.removeEntry,
              breadcrumbElement;

          core.dom.create(testBreadcrumb).appendTo("BODY");

          assertFunction('removelast of the breadcrumb module is not a function!', removeEntry);

          BreadCrumb.create(breadcrumbID);
          breadcrumbElement = core.dom.select(breadcrumbID);

          assertEquals('C', breadcrumbElement.find('li.last a').text());
          removeEntry(breadcrumbID, 4);
          assertNotEquals('C', breadcrumbElement.find('li.last a').text());
        });
        
        core.module.use('BreadCrumb', moduleLoaded);
      });
    },

    //////////
    ////////// replaceEntry
    //////////
    testReplaceEntry: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(BreadCrumb) {
          var replaceEntry = BreadCrumb.replaceEntry,
              breadcrumbElement,
              func = function(item){
                       item.text('A');
                     };

          core.dom.create(testBreadcrumb).appendTo("BODY");

          assertFunction('removelast of the breadcrumb module is not a function!', replaceEntry);

          BreadCrumb.create(breadcrumbID);
          breadcrumbElement = core.dom.select(breadcrumbID);

          assertEquals('C', breadcrumbElement.find('li.last a').text());
          replaceEntry(breadcrumbID, 4, 'AAA', 'www.aaa.pl');
          assertEquals('AAA', breadcrumbElement.find('li.last a').text());
          assertEquals('www.aaa.pl', breadcrumbElement.find('li.last a').attr('href'));
          
          replaceEntry(breadcrumbID, 2, 'BBB', func);
          assertEquals('A', breadcrumbElement.find('li:nth-child(2)').text());
        });

        core.module.use('BreadCrumb', moduleLoaded);
      });
    }
  });
}());