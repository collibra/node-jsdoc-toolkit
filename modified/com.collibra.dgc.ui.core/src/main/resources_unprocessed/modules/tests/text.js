/*global
AsyncTestCase
assertFunction
assertEquals
assertTrue
jstestdriver
console
core
log
*/
(function() {
  "use strict";
  
      //some markup for the tests
  var markup = "<div id='d1' style='font-family: monospace;line-height: 16px;letter-spacing: 0'></div>",
      mt = ["test", "testte sttest", "testte stte sttest testte sttest"],
      spanTextStr = "<span id='spanCalculateTextHeight' style='filter: alpha(0);'></span>",
      $ = core._private.libs.libs.jquery,
      //a shortcut only for the sake of this test
      log = function(msg) {
        jstestdriver.console.log("WYPLUW:", msg);
      };
      
  new AsyncTestCase('modules :: text', {
    //////////
    ////////// autoSize
    //////////
    
    
    testAutoSize: function(textQueue) {
      textQueue.call(function(callbacks) {
        
        var textModuleLoaded = callbacks.add(function(text) {
          var autoSize = text.autoSize,
          el = null;
          
          assertFunction('autoSize of the text module is not a function!', autoSize);
          
          core.dom.create(markup, "BODY");
          el = core.dom.select("#d1");
          el.text(mt[2]).css({width: 60, height: 40});
          
          log(el.parent().html());
          autoSize(el, 8, 20, false);
          log(el.parent().html());
        });

        core.module.use('Text', textModuleLoaded);
      });
    },

    //////////
    ////////// height
    //////////
    testHeight: function(queue) {
      queue.call('twee', function(callbacks) {
        
        var moduleLoaded = callbacks.add(function(text) {
          var height = text.height,
              el = null;
          
          //we need to make sure dummy span is created so we can use text functions
          
          core.dom.create(spanTextStr, 'BODY');
          
          
          assertFunction('height of the text is not a function!', height);
          core.dom.create(markup, "BODY");
          el = core.dom.select("#d1");
          //tests for 3 different texts, 3 font sizes and 2 font weights
          
          el.css({"font-size": "16px","font-weight": "normal"});
          el.text(mt[0]);
          assertEquals(64, height(el, 10));
          assertEquals(16, height(el, 66));
          assertEquals(16, height(el, 133));
          
          el.css({"font-size": "10px","font-weight": "bold", "line-height": "10px"});
          assertEquals(40, height(el, 10));
          assertEquals(10, height(el, 66));
          assertEquals(10, height(el, 133));
          
          el.text(mt[1]);
          el.css({"font-size": "16px","font-weight": "normal", "line-height": "16px"});
          assertEquals(192, height(el, 10));
          assertEquals(32, height(el, 66));
          assertEquals(16, height(el, 133));
          
          el.css({"font-size": "12px","font-weight": "bold", "line-height": "12px"});
          assertEquals(144, height(el, 10));
          assertEquals(24, height(el, 66));
          assertEquals(12, height(el, 133));
          
          el.text(mt[2]);
          el.css({"font-size": "16px","font-weight": "normal", "line-height": "16px"});
          assertEquals(448, height(el, 10));
          assertEquals(80, height(el, 66));
          assertEquals(48, height(el, 133));
          
          el.css({"font-size": "12px","font-weight": "bold", "line-height": "12px"});
          assertEquals(336, height(el, 10));
          assertEquals(60, height(el, 66));
          assertEquals(24, height(el, 133));
            
        });
        
        core.module.use('Text', moduleLoaded);
      });
    },
    
    //////////
    ////////// methodName
    //////////
    testMaxSizes: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(module) {
          var maxSizes = module.maxSizes;
          
          assertFunction('maxSizes of the module is not a function!', maxSizes);
          
          
        });
        
        core.module.use('Text', moduleLoaded);
      });
    },
    
    //////////
    ////////// methodName
    //////////
    testTruncate: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(module) {
          var truncate = module.truncate;
          
          assertFunction('truncate of the module module is not a function!', truncate);
          
          
        });
        
        core.module.use('Text', moduleLoaded);
      });
    },
    
    //////////
    ////////// methodName
    //////////
    testWidth: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(module) {
          var width = module.width,
          el = null;
          
          assertFunction('width of the module module is not a function!', width);
          core.dom.create(markup, "BODY");
          el = core.dom.select("#d1");
          //tests for 3 different texts, 3 font sizes and 2 font weights
          
          el.css({"font-size": "16px","font-weight": "normal"});
          el.text(mt[0]);
          assertEquals("133px", width(el));
        });
        
        core.module.use('Text', moduleLoaded);
      });
    }
  });
}());