/*global
window
core
TestCase
assertFunction
assertEquals
assertSame
assertTrue
assertFalse
assertObject
assertNotEquals
assertNotSame
sinon
expectAsserts
assertString
assertArray
assertUndefined
*/

(function() {
  "use strict";

  new TestCase("CORE :: parts :: mediator :: mediator.js :: mediator", {
    mediator: core.mediator,
    //////////
    ////////// Publish
    //////////
    testPublish: function() {
      var _self = this,
          func1 = function(){ window.testVar = true },
          func2 = function(){ core.dom.create('<div id="testDiv">testContent</div>', 'BODY')},
          test1 = false,
          test2 = false;

      assertFunction('core.mediator.publish() is not a function.', core.mediator.publish);
      
      // Subscribe function
      test1 = core.mediator.subscribe('test::publish1', func1);
      assertArray('core.mediator.subscribe() should return an array', test1);
      assertString("subscribe returned id which is not a string", test1[0]);
      assertFunction("subscribe returned callback which is not a function", test1[1]);
      
      // Subscribe 2st function
      test2 = core.mediator.subscribe('test::publish2', func2);
      assertArray('core.mediator.subscribe() should return an array', test2);
      assertString("subscribe returned id which is not a string", test2[0]);
      assertFunction("subscribe returned callback which is not a function", test2[1]);
      
      window.testVar = false;
      assertEquals('Test variable is not set.', false, window.testVar);
      assertFalse('Test element found!', core.dom.is(core.dom.select('#testDiv')));
      
      // Publish functions
      core.mediator.publish('test::publish1');
      core.mediator.publish('test::publish2');
      
      // Test results
      assertEquals('Test variable is not set.', true, window.testVar);
      assertTrue('Test element not found', core.dom.is(core.dom.select('#testDiv')));
    },
  
    //////////
    ////////// Subscribe
    //////////
    testSubscribe: function() {
      var _self = this,
          func1 = function(){ core.debug.show('This is func1'); },
          func2 = function(){ core.debug.show('This is func2'); },
          test1 = false,
          test2 = false;
      
      assertFunction('core.mediator.subscribe() is not a function.', core.mediator.subscribe);
      
      // Subscribe 1st function
      test1 = core.mediator.subscribe('test::subscribe', func1);
      assertArray('core.mediator.subscribe() should return an array', test1);
      assertString("subscribe returned id which is not a string", test1[0]);
      assertFunction("subscribe returned callback which is not a function", test1[1]);

      // Check is function stored in cache
      assertEquals('cache["test::subscribe"] length should be 1 long', 1, core._private.mediator.cache["test::subscribe"].length);
      assertFunction('cache["test::subscribe"][0] should be a function', core._private.mediator.cache["test::subscribe"][0]);
      
      // Subscribe 2st function
      test2 = core.mediator.subscribe('test::subscribe', func2);
      assertArray('core.mediator.subscribe() should return an array', test2);
      assertString("subscribe returned id which is not a string", test2[0]);
      assertFunction("subscribe returned callback which is not a function", test2[1]);
      
      // Check are two functions stored in cache
      assertEquals('cache["test::subscribe"] length should be 2 long', 2, core._private.mediator.cache["test::subscribe"].length);
      assertFunction('cache["test::subscribe"][1] should be a function', core._private.mediator.cache["test::subscribe"][1]);
    },
  
    //////////
    ////////// Unsubscribe
    //////////
    testUnsubscribe: function() {
      var _self = this,
          subscribe = _self.mediator.subscribe,
          unsubscribe = _self.mediator.unsubscribe,
          cache = core._private.mediator.cache,
          function_subscribed = function(){};
      assertFunction('core.mediator.unsubscribe() is not a function.', unsubscribe);
      
      subscribe("testU", function_subscribed);
      unsubscribe(["testU", function_subscribed]);
      
      assertUndefined("cache[testU] is not a function", cache.testU[0]);
      assertNotSame("cache[testU] is not empty", cache.testU, []);
    },
  
    //////////
    ////////// UnsubscribeAll
    //////////
    testUnsubscribeAll: function() {
      var _self = this,
          unsubscribeAll = _self.mediator.unsubscribeAll,
          subscribe = _self.mediator.subscribe,
          cache = core._private.mediator.cache;
      
      assertFunction('core.mediator.unsubscribeAll() is not a function.', unsubscribeAll);
      
      subscribe("testAll", function(){});
      assertArray("Cache[test] doesnt exist", cache.testAll);
      unsubscribeAll("testAll");
      assertNotSame("Cache[test] is not empty", cache.testAll, []);
    }
  });
}());