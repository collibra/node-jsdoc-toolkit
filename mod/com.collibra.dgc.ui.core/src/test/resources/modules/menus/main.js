(function() {
  "use strict";

  AsyncTestCase("modules :: menus :: main :: main.js", {

    //////////
    ////////// collapse
    //////////
    testCollapse: function(mainMenuQueue) {
      mainMenuQueue.call(function(callbacks) {
        var mainMenuLoaded = callbacks.add(function(mainMenu) {
          assertObject("collapse() is not an object.", mainMenu);
        });

        core.module.use('mainMenu', mainMenuLoaded);
      });
    }
  });
}());
