/*global
AsyncTestCase
assertFunction
core
*/
(function() {
  "use strict";

  new AsyncTestCase('modules :: novigation :: oldnavigator :: oldnavigator.js', {
    //////////
    ////////// initialize
    //////////
    testInitialize: function(navigatorQueue) {
      navigatorQueue.call(function(callbacks) {
        var navigatorLoaded = callbacks.add(function(navigator) {
          var initialize = navigator.initialize;

          assertFunction('initialize of the navigator is not a function!', initialize);
        });

        core.module.use('navigator', navigatorLoaded);
      });
    }

  });
}());