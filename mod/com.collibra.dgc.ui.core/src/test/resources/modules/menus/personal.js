(function() {
  "use strict";

  AsyncTestCase("modules :: menus :: personal :: personal.js", {
    //////////
    ////////// collapse
    //////////
    testCollapse: function(personalQueue) {
      personalQueue.call(function(callbacks) {
        var personalModuleLoaded = callbacks.add(function(personalMenu) {
          var collapse = personalMenu.collapse;

          assertFunction("collapse() is not a function.", collapse);
        });

        core.module.use('personalMenu', personalModuleLoaded);
      });
    },

    //////////
    ////////// expand
    //////////
    testExpand: function(personalQueue) {
      personalQueue.call(function(callbacks) {
        var personalModuleLoaded = callbacks.add(function(personalMenu) {
          var expand = personalMenu.expand;

          assertFunction("expand() is not a function.", expand);
        });

        core.module.use('personalMenu', personalModuleLoaded);
      });
    }
  });
}());
