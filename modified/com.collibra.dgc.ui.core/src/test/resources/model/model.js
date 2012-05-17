(function() {
  "use strict";
  
  AsyncTestCase("model :: model repository :: model.js", {
    
//////////
////////// add
//////////
testAdd: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(Model) {
      var dmo = {};
      
      assertFunction('add of the Model module is not a function!', Model.add);
      
      //Create dummy model object.
      dmo = {
        rid: "some-rid"
      };
      
      //Check if repo is empty.
      assertTrue("Repo is not empty.", core.object.isEmpty(Model._private.repo));
      
      //Add to the repo.
      assertTrue("Adding of object failed.", Model.add(dmo));
      
      //Check if repo is empty after add.
      assertFalse("Repo is still empty.", core.object.isEmpty(Model._private.repo));
      
      //Is dmo added?
      assertEquals("Something in repo is not dummy model object.", Model._private.repo["some-rid"], dmo);
      
      //Now add existing object.
      assertFalse("Model was added again?.", Model.add(dmo));
      
    });
    
    core.module.use('Model', moduleLoaded);
  });
},

//////////
////////// cache
//////////
testCache: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(Model) {
      
      assertFunction('cache of the Model module is not a function!', Model.cache);
      
      //TODO! Not yet implemented.
      
    });
    
    core.module.use('Model', moduleLoaded);
  });
},

//////////
////////// get
//////////
testGet: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(Model) {
      var dmo = {};
      
      assertFunction('get of the Model module is not a function!', Model.get);
      
      //Create dummy model object.
      dmo = {
        rid: "some-rid"
      };
      
      //Purge Repo.
      Model._private.repo = {};
      
      //Check if repo is empty.
      assertTrue("Repo is not empty.", core.object.isEmpty(Model._private.repo));
      
      //Add to the repo.
      assertTrue("Adding of object failed.", Model.add(dmo));
      
      //Check if repo is empty after add.
      assertFalse("Repo is still empty.", core.object.isEmpty(Model._private.repo));
      
      //Get the dmo object.
      assertEquals("Something in repo is not dummy model object.", Model.get("some-rid"), dmo);
      
      //Assume getting nonexistent stuff.
      assertFalse("Get of nonexistesnt did not return false.", Model.get("nonexistent"));
      
    });
    
    core.module.use('Model', moduleLoaded);
  });
},

//////////
////////// remove
//////////
testRemove: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(Model) {
      var dmo = {};
      
      assertFunction('remove of the Model module is not a function!', Model.remove);
      
      //Create dummy model object.
      dmo = {
        rid: "some-rid"
      };
      
      //Purge Repo.
      Model._private.repo = {};
      
      //Check if repo is empty.
      assertTrue("Repo is not empty.", core.object.isEmpty(Model._private.repo));
      
      //Add to the repo.
      assertTrue("Adding of object failed.", Model.add(dmo));
      
      //Check if repo is empty after add.
      assertFalse("Repo is still empty.", core.object.isEmpty(Model._private.repo));
      
      //Get the dmo object.
      assertTrue("Removing of object failed.", Model.remove("some-rid"));
      
      //Now check the repo manualy.
      assertEquals("Something in repo is not dummy model object.", Model._private.repo["some-rid"], undefined);
      
      //And check using get method.
      assertFalse("Something in repo is not dummy model object.", Model.get("some-rid"));
      
    });
    
    core.module.use('Model', moduleLoaded);
  });
}
    
  });
}());