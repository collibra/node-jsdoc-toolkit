(function() {
  "use strict";
  
  var $ = core._private.libs.libs.jquery,
      data = {
        createRespond: '{"parentReference":null,"subCommunityReferences":{"communityReference":[{"name":"Admin Community","uri":"http://www.collibra.com/admin_community","language":"English","sbvr":false,"meta":true,"locked":false,"resourceId":"18a7194a-cefa-446e-aaed-085fc6dc2e0d","lastModified":1336238300989,"lastModifiedBy":"Guest","createdOn":1336238278397,"createdBy":"Guest"},{"name":"0.9180575203687741","uri":"http://www.collibra.com/SBVR_ENGLISH_COMMUNITY","language":"English","sbvr":true,"meta":true,"locked":false,"resourceId":"ee5bf21d-9589-4573-8cc3-c7758d39e34e","lastModified":1336564512292,"lastModifiedBy":"Admin","createdOn":1336238278397,"createdBy":"Guest"}]},"vocabularyReferences":{"vocabularyReference":[{"name":"Vocabulary for Describing Business Rules","uri":"http://www.omg.org/spec/SBVR/20070901/DescribingBusinessRules.xml","meta":true,"typeReference":{"signifier":"Glossary","locked":false,"resourceId":"3f10ce6a-3a85-4ab4-b312-9b2dbfc6020b","lastModified":1336238303521,"lastModifiedBy":"Guest","createdOn":1336238288799,"createdBy":"Guest","preferred":true},"locked":false,"resourceId":"219ac5fe-7178-490c-b2a4-ce05ea5e5e32","lastModified":1336238304726,"lastModifiedBy":"Guest","createdOn":1336238295594,"createdBy":"Guest"},{"name":"Logical Formulation of Semantics Vocabulary","uri":"http://www.omg.org/spec/SBVR/20070901/LogicalFormulationOfSemantics.xml","meta":true,"typeReference":{"signifier":"Glossary","locked":false,"resourceId":"3f10ce6a-3a85-4ab4-b312-9b2dbfc6020b","lastModified":1336238303521,"lastModifiedBy":"Guest","createdOn":1336238288799,"createdBy":"Guest","preferred":true},"locked":false,"resourceId":"2efbbd62-0347-4d65-9041-606bba14e7e4","lastModified":1336238302025,"lastModifiedBy":"Guest","createdOn":1336238284150,"createdBy":"Guest"}]},"locked":false,"resourceId":"2a4979a7-8faf-4fbe-b387-7e843369ad1b","lastModified":1336238300988,"lastModifiedBy":"Guest","createdOn":1336238278396,"createdBy":"Guest","name":"Metamodel Community","uri":"http://www.collibra.com/METAMODEL_COMMUNITY","language":"English","sbvr":false,"meta":true}',
        createNewRespond: '{"name":"newCommunity","uri":"http://www.collibra.com/newCommunity","language":"English","sbvr":false,"meta":false,"locked":false,"resourceId":"new-community-resource-id","lastModified":1336641781907,"lastModifiedBy":"Admin","createdOn":1336641781894,"createdBy":"Admin"}',
        findRespondBasic: '',
        findRespondFull: '',
        deleteRespond: ''
      },
      modelObjectCheck = function(mo, rid, name, uri) {
        assertTrue("Created object is not model object", mo.isModelObject);
        
        //Methods.
        assertFunction("There is no get", mo.get);
        assertFunction("There is no set", mo.set);
        assertFunction("There is no save", mo.save);
        assertFunction("There is no update", mo.update);
        assertFunction("There is no delete", mo['delete']);
        assertFunction("There is no listHandlers", mo.listHandlers);
        assertFunction("There is no fromJSON", mo.fromJSON);
        assertFunction("There is no toJSON", mo.toJSON);
        assertFunction("There is no onChange", mo.onChange);
        assertFunction("There is no isConsistent", mo.isConsistent);
        //Data.
        //ResourceId.
        assertEquals("Rid was not set", rid, mo.rid);
        assertEquals("resourceId was not set", rid, mo._private.data.resourceId);
        assertEquals("Property name is wrong", mo._private.data.name, name);
        assertEquals("Property uri is wrong", mo._private.data.uri, uri);
      },
      gmo = null;
  
  AsyncTestCase("model :: modelImpl :: modelImpl.js", {

  setUp: function () {
    this.fakeXhr = sinon.useFakeXMLHttpRequest();
    var requests = this.requests = [];

    this.fakeXhr.onCreate = function (xhr) {
      requests.push(xhr);
    };
  },

  tearDown: function () {
    this.fakeXhr.restore();
  },
    
//////////
////////// create
//////////
testCreate: function(queue) {
  var communityModel = null;
  
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(ddd) {
      
      communityModel = ddd;
      
    });
    
    core.module.use('model/community', moduleLoaded);
  });
  
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(ModelImpl) {
      var createCallback = sinon.spy(),
          createNewCallback = sinon.spy(),
          spy2 = sinon.spy(),
          mo = null;
      
      assertFunction('create of the ModelImpl module is not a function!', ModelImpl.create);
      
      //###############################
      //1. Create from resourceId
      
      //Create with update(REST GET) call.
      mo = ModelImpl.create("2a4979a7-8faf-4fbe-b387-7e843369ad1b", communityModel, createCallback);
      gmo = core.object.copy(mo);
      //fake the respond for create.
      this.requests[0].respond(200, {},
        //data to be returned:
        data.createRespond
      );
      assertTrue("Creation(1) failed.", createCallback.calledOnce);
      //assertTrue("Model object is wrong. ", mo.rid);
      
      //Check basic model object stuff.
      modelObjectCheck(mo, "2a4979a7-8faf-4fbe-b387-7e843369ad1b", "Metamodel Community", "http://www.collibra.com/METAMODEL_COMMUNITY");
      
      //Children and their data.
      assertArray("Property subcommunities is wrong", mo._private.data.subcommunities);
      assertTrue("Property subcommunities[0] is not a model object", mo._private.data.subcommunities[0].isModelObject);
      assertTrue("Property subcommunities[1] is not a model object", mo._private.data.subcommunities[1].isModelObject);
      assertEquals("Property subcommunities[1].name is wrong", mo._private.data.subcommunities[1]._private.data.name, "0.9180575203687741");
      assertArray("Property vocabularies is wrong", mo._private.data.vocabularies);
      assertTrue("Property vocabularies[0] is not a model object", mo._private.data.vocabularies[0].isModelObject);
      assertTrue("Property vocabularies[1] is not a model object", mo._private.data.vocabularies[1].isModelObject);
      assertEquals("Property vocabularies[0].name is wrong", mo._private.data.vocabularies[0]._private.data.name, "Vocabulary for Describing Business Rules");
      
      
      //###############################
      //2. Create from newly created(like create new community in the database).
      mo = {};
      mo = ModelImpl.create({
             name: "newCommunity",
             uri: "http://www.collibra.com/newCommunity"
           }, communityModel, createNewCallback);
      //fake the respond for create.
      this.requests[1].respond(200, {},
        //data to be returned:
        data.createNewRespond
      );
      assertTrue("Creation(2) failed.", createNewCallback.calledOnce);
      //Check basic model object stuff.
      modelObjectCheck(mo, "new-community-resource-id", "newCommunity", "http://www.collibra.com/newCommunity");
    });
    
    core.module.use('ModelImpl', moduleLoaded);
  });
},

//////////
////////// createFromData
//////////
testCreateFromData: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(ModelImpl) {
      var createFromData = ModelImpl.createFromData;
      
      assertFunction('createFromData of the ModelImpl module is not a function!', createFromData);
      
      
    });
    
    core.module.use('ModelImpl', moduleLoaded);
  });
},

//////////
////////// delete
//////////
testDelete: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(ModelImpl) {
      
      assertFunction('delete of the ModelImpl module is not a function!', ModelImpl["delete"]);
      
      
      
    });
    
    core.module.use('ModelImpl', moduleLoaded);
  });
},

//////////
////////// isConsistent
//////////
testIsConsistent: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(ModelImpl) {
      
      assertFunction('isConsistent of the ModelImpl module is not a function!', ModelImpl.isConsistent);
      assertObject("globally defined model object is wrong", gmo);
      
      //Check consistency before.
      assertTrue("Gmo is not consistent.", gmo.isConsistent());
      
      //Now set sth.
      gmo.set("name", "aaa");
      
      //Check consistency after.
      assertFalse("Gmo is still consistent.", gmo.isConsistent());
      
    });
    
    core.module.use('ModelImpl', moduleLoaded);
  });
},

//////////
////////// getProperty
//////////
testGetProperty: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(ModelImpl) {
      var getProperty = ModelImpl.getProperty;
      
      assertFunction('getProperty of the ModelImpl module is not a function!', getProperty);
      
      
    });
    
    core.module.use('ModelImpl', moduleLoaded);
  });
},

//////////
////////// find
//////////
testFind: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(ModelImpl) {
      var results = null,
          resultCallback = sinon.spy();
      
      assertFunction('find of the ModelImpl module is not a function!', ModelImpl.find);
      //ModelImpl.find(communityModel, "Meta", function() {
        
      //});
      
    });
    
    core.module.use('ModelImpl', moduleLoaded);
  });
},

//////////
////////// fromJson
//////////
testFromJSON: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(ModelImpl) {
      var fromJson = ModelImpl.fromJSON;
      
      assertFunction('fromJson of the ModelImpl module is not a function!', fromJson);
      
    });
    
    core.module.use('ModelImpl', moduleLoaded);
  });
},

//////////
////////// onChange
//////////
testOnChange: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(ModelImpl) {
      var onChange = ModelImpl.onChange;
      
      assertFunction('onChange of the ModelImpl module is not a function!', onChange);
      
      
    });
    
    core.module.use('ModelImpl', moduleLoaded);
  });
},

//////////
////////// setProperty
//////////
testSetProperty: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(ModelImpl) {
      var setProperty = ModelImpl.setProperty;
      
      assertFunction('setProperty of the ModelImpl module is not a function!', setProperty);
      
      
    });
    
    core.module.use('ModelImpl', moduleLoaded);
  });
},

//////////
////////// save
//////////
testSave: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(ModelImpl) {
      var save = ModelImpl.save;
      
      assertFunction('save of the ModelImpl module is not a function!', save);
      
      
    });
    
    core.module.use('ModelImpl', moduleLoaded);
  });
},

//////////
////////// update
//////////
testUpdate: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(ModelImpl) {
      var update = ModelImpl.update;
      
      assertFunction('update of the ModelImpl module is not a function!', update);
      
      
    });
    
    core.module.use('ModelImpl', moduleLoaded);
  });
},

//////////
////////// toJson
//////////
testToJson: function(queue) {
  queue.call(function(callbacks) {
    var moduleLoaded = callbacks.add(function(ModelImpl) {
      var toJson = ModelImpl.toJSON;
      
      assertFunction('toJson of the ModelImpl module is not a function!', toJson);
      
      
    });
    
    core.module.use('ModelImpl', moduleLoaded);
  });
}

  });
}());