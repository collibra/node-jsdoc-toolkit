/*global
core
sinon
TestCase
AsyncTestCase
assertFunction
assertEquals
assertNotEquals
assertTrue
assertObject
assertArray
assertUndefined
assertNotUndefined
*/

(function() {
  "use strict";

  var testArray = [],
      testObject = {},
      testNumber = 3,
      testString = "",
      testBoolean = true,
      testNull = null,
      testUndefined,
      $ = core._private.libs.libs.jquery,
      $obj = $("BODY"),
      obj = {};

  new AsyncTestCase("CORE :: base :: base.js :: dom-async", {
    dom: core.dom,

    //////////
    ////////// animate
    //////////
    testAnimate : function(queue){
      var position = false,
          animate = this.dom.animate,
          elements = false,
          checkPropertiesAfterAnimation = false;

      elements = $('<div id="anim" style="position:absolute; left:0; top:0; width:50px; height:50px;"></div>').appendTo('BODY');

      queue.call('Test 1', function(callbacks) {
        //check properties after animation
        checkPropertiesAfterAnimation = callbacks.add(function() {
          assertEquals(150, $('#anim').position().left);
          assertEquals(333, $('#anim').position().top);
          assertEquals(111, $('#anim').height());
          assertEquals(123, $('#anim').width());
        });

        //animate
        animate('#anim', {left: '150px', top: '333px', height: '111px', width: '123px'}, {duration: 'fast', complete: checkPropertiesAfterAnimation});
      });

      queue.call('Test 2', function(callbacks) {
        //check properties after animation
        checkPropertiesAfterAnimation = callbacks.add(function() {
          assertEquals(0, $('#anim').position().left);
          assertEquals(0, $('#anim').position().top);
          assertEquals(1, $('#anim').height());
          assertEquals(1, $('#anim').width());
        });

        //animate
        animate($('#anim'), {left: '0px', top: '0px', height: '1px', width: '1px'}, {duration: 50, complete: checkPropertiesAfterAnimation});
      });

      queue.call('Test 3', function(callbacks) {
        //check properties after animation
        checkPropertiesAfterAnimation = callbacks.add(function() {
          assertEquals(0, $('#anim').position().left);
          assertEquals(0, $('#anim').position().top);
          assertEquals(1, $('#anim').height());
          assertEquals(1, $('#anim').width());
        });

        //second animation should stop the first one
        animate('#anim', {left: '150px', top: '333px', height: '111px', width: '123px'}, {duration: 5000});
        animate($('#anim'), {left: '0px', top: '0px', height: '1px', width: '1px'}, {duration: 50, complete: checkPropertiesAfterAnimation});
      });

    }
  });

  new TestCase("CORE :: base :: base.js :: dom", {
    dom: core.dom,

    //////////
    ////////// create
    //////////
    testCreate: function() {
      var _self = this,
          create = this.dom.create,
          jVersion = $('BODY').jquery,
          elements = false,
          testElements = [],
          testElementsSize = 3,
          testResults = [],
          testReturns = [],
          testWrapper = $("BODY"),
          i = 0;

      assertFunction("core.dom.create is not a function.", create);

      elements = $('<div id="parent1" class="parents"></div><div id="parent2" class="parents"></div>').appendTo('BODY');

      testElements = [
        { 'element':'<div class="test1">TEST</div>', 'selector':'div.test1', 'parent':'#parent1' },
        { 'element':'<div class="test2">TEST</div>', 'selector':'div.test2', 'parent':$('#parent2') },
        { 'element':'<div class="test3">TEST</div>', 'selector':'div.test3', 'parent':'.parents' },
        { 'element':'<div id="test4">TEST</div>', 'selector':'#test4', 'parent':'body' },
        { 'element':$('<div class="test5">TEST</div>'), 'selector':'div.test5', 'parent':'body' },
        { 'element':$('<div class="test5">TEST</div>'), 'selector':'div.test5', 'parent': $('body') }
      ];

      for (i = 0; i < testElementsSize; i++) {
        testReturns[i] = create(testElements[i].element, testElements[i].parent);
        // Element should be created inside parent
        testResults[i] = testWrapper.find(testElements[i].selector).size();
        assertNotEquals('No created elements find', 0, testResults[i]);
        // Returned element should be a {CoreObj}
        assertEquals('The returned item is not CoreObj\'s.', jVersion, testReturns[i].jquery);
      }
    },

    //////////
    ////////// each
    //////////
    testEach: function() {
      var _self = this,
          each = this.dom.each,
          jVersion = $('BODY').jquery,
          elements = false,
          i = 0;

      assertFunction("core.dom.each is not a function.", each);

      elements = $('<ul><li class="item">item 1</li><li class="item">item 2</li><li class="item">item 3</li></ul>').appendTo('BODY');

      // Test if all the items are coreobjects.
      each('.item', function(item) {
        assertEquals('The items within the each loop are not CoreObj\'s.', jVersion, item.jquery);
        i = i + 1;
      });

      assertEquals('The number of items the function is executed upon is not equal to the real number.', 3, i);

      // Test if the each works properly on empty set of elements.
      i = 0;
      each('', function(item) {
        i = i + 1;
      });

      assertEquals('The each function should not execute on empty elements.', 0, i);
    },

    //////////
    ////////// generateID
    //////////
    testGenerateID: function() {
      var _self = this,
          generate = _self.dom.generateID,
          elements = false,
          ids1 = false,
          ids2 = false, ids3 = false;

      assertFunction("core.dom.generateID is not a function.", generate);

      elements = $('<ul><li class="item">item 1</li><li class="item">item 2</li><li class="item">item 3</li></ul>').appendTo('BODY');

      ids1 = generate(elements.children(), {
        prefix: "prefix_",
        suffix: "_suffix"
      });

      elements.children().each(function(idx, item) {
        assertNotNull("There is an item that doesn't have a id applied.", $(item).attr('id'));
      });

      assertEquals("Not all items have the prefix applied.", $('UL > LI[id^="prefix_"]').length, 3);
      assertEquals("Not all items have the suffix applied.", $('UL > LI[id$="_suffix"]').length, 3);

      ids2 = generate(elements.children());

      ids3 = generate(elements.children(), {
        changeDOM: false
      });

      assertNotEquals("First set equals second set of ids.", ids1[0], ids2[0]);
      assertNotEquals("changeDOM param doesn't function correctly.", ids2[0], $("UL:first-child").attr('id'));
    },

    //////////
    ////////// getOptions
    //////////
    testGetOptions: function() {
      var _self = this,
          returnedValue = false;

      assertFunction("core.dom.getOptions is not a function.", _self.dom.getOptions);
      returnedValue = _self.dom.getOptions();
      assertObject("The returned item is not an object", returnedValue);
      assertNotUndefined("The selection.cache item is not defined", returnedValue.selection.cache);

    },

    //////////
    ////////// is
    //////////
    testIs: function() {
      var _self = this,
          element = _self.dom.create('<div id="test"></div>','BODY'),
          returnedValue = false;

      assertFunction("core.dom.is is not a function.", _self.dom.is);
      assertTrue("The returned value is not true", _self.dom.is(_self.dom.select('#test')));
      assertFalse("The returned value is not false", _self.dom.is(_self.dom.select('#test-notexist')));

    },

    //////////
    ////////// scrollTo
    //////////
    testScrollTo : function(queue){

      assertFunction("core.dom.scrollTo is not a function.", core.dom.scrollTo);

    },

    //////////
    ////////// select
    //////////
    testSelect: function() {
      var _self = this,
          select = this.dom.select,
          jVersion = $('BODY').jquery,
          elements = false,
          testParameters = [],
          testResults = [],
          testCount = [],
          i = 0;

      assertFunction("core.dom.select is not a function.", select);

      elements = $('<ul id="testlist"><li class="item" resourceId="123">Icontains 1</li><li class="item">iContains 2</li><li class="item">icontains 3</li></ul>').appendTo('BODY');

      //Tests for empty results
      testParameters = [null, false,'','#idthatnotexist'];
      testResults = [];

      for (i = 0; i < testParameters.length; i++) {
        testResults[i] = select(testParameters[i]);
        assertEquals('[param=' + testParameters[i] + '] The returned item is not CoreObj\'s.', jVersion, testResults[i].jquery);
        assertEquals('[param=' + testParameters[i] + '] The returned item is not empty!', 0, testResults[i].size());
      }

      //Tests for non empty results
      testParameters = ['HTML','li','.item','li.item','*','ul > li','ul>li','#testlist','li[resourceId="123"]','li:first-child', '.item:icontains("icontains")'];
      testCount = [1, 3, 3, 3, false, 3, 3, 1, false, false, 3];
      testResults = [];

      for (i = 0; i < testParameters.length; i++) {
        testResults[i] = select(testParameters[i]);

        if (testCount[i] !== false) {
          assertEquals('[param=' + testParameters[i] + '] More or less icontain items were found than...', testCount[i], testResults[i].length);
        }

        assertEquals('[param=' + testParameters[i] + '] The returned item is not CoreObj\'s.', jVersion, testResults[i].jquery);
        assertNotEquals('[param=' + testParameters[i] + '] The returned item is empty!', 0, testResults[i].size());
      }
    },
    
    //////////
    ////////// setOptions
    //////////
    testSetOptions: function() {
      var _self = this,
          returnedValue = false;

      assertFunction("core.dom.setOptions is not a function.", _self.dom.setOptions);
      returnedValue = _self.dom.setOptions({selection: {cache: true}});
      assertTrue("The returned item is not true", returnedValue);
      
      returnedValue = _self.dom.getOptions();
      assertObject("The returned item is not an object", returnedValue);
      assertNotUndefined("The selection.cache item is not defined", returnedValue.selection.cache);
      assertTrue("The selection.cache item is not set", returnedValue.selection.cache);

    },
    
    //////////
    ////////// sort
    //////////
    testSort: function() {
      var _self = this,
          sort = _self.dom.sort,
          itemsArray = [];

      assertFunction("core.dom.sort is not a function.", sort);

      core.dom.create('<ul id="test"><li class="test-3">3</li><li class="test-1">1</li><li class="test-5">5</li><li class="test-4">4</li><li class="test-2">2</li></ul>', 'BODY');
      itemsArray = [
       core.dom.select('#test .test-3'),
       core.dom.select('#test .test-1'),
       core.dom.select('#test .test-5'),
       core.dom.select('#test .test-4'),
       core.dom.select('#test .test-2')
      ];
      
      assertArray("Items are not in array", itemsArray);
      
      core.dom.sort(itemsArray, function(a, b) { 
        return a.text() > b.text() ? 1 : -1; 
      });
      
      assertEquals('The DOM elements are not sorted.', '12345', $('#test').text());

    },
    
    //////////
    ////////// sortChildren
    //////////
    testSortChildren: function() {
      var _self = this,
          sortChildren = _self.dom.sortChildren;

      assertFunction("core.dom.sortChildren is not a function.", sortChildren);
      
      core.dom.select('BODY').html('');
      core.dom.create('<ul id="test"><li class="test-3">3</li><li class="test-1">1</li><li class="test-5">5</li><li class="test-4">4</li><li class="test-2">2</li></ul>', 'BODY');
      core.dom.sortChildren('#test');
      
      assertEquals('The DOM elements are not sorted.', '12345', $('#test').text());

    },
  });

  new TestCase("CORE :: base :: base.js :: event", {
    event: core.event,

    //////////
    ////////// blur
    //////////
    testBlur: function() {
      var _self = this,
          blur = this.event.blur,
          jVersion = $('BODY').jquery,
          elements = false,
          callbacks = [sinon.spy(), sinon.spy()],
          testReturns = [],
          testSelectors = [];

      assertFunction("core.event.blur is not a function.", blur);

      elements = $('<form id="testform"><input type="text" id="txt1" name="txt1" value=""/><input type="text" id="txt2" name="txt2" value=""/></form>').appendTo('BODY');
      testSelectors = [$('#txt1'), '#txt2'];

      testReturns[0] = blur(testSelectors[0], callbacks[0]);
      testReturns[1] = blur(testSelectors[1], callbacks[1]);

      //Test 1 - trigger blur
      testSelectors[0].focus();
      testSelectors[0].blur();
      assertTrue("T1: There was no callback call!", callbacks[0].calledOnce);

      //Test 2 - returned object
      assertEquals("Returned value is not a {CoreObj}", jVersion, testReturns[0].jquery);
      assertEquals("Returned value is not a {CoreObj}", jVersion, testReturns[1].jquery);
    },

    //////////
    ////////// hover
    //////////
    testHover: function() {
      var _self = this,
          hover = this.event.hover,
          jVersion = $('BODY').jquery,
          elements = false,
          callbacks = [sinon.spy(), sinon.spy(), sinon.spy(), sinon.spy()],
          testReturns = [],
          testSelectors = [];

      assertFunction("core.event.hover is not a function.", blur);

      elements = $('<form id="testform"><input type="text" id="txt1" name="txt1" value=""/><input type="text" id="txt2" name="txt2" value=""/></form>').appendTo('BODY');
      testSelectors = [$('#txt1'), '#txt2'];

      testReturns[0] = hover(testSelectors[0], callbacks[0], callbacks[1]);
      testReturns[1] = hover(testSelectors[1], callbacks[2], callbacks[3]);

      //Test 1 - trigger mouse enter
      testSelectors[0].trigger('mouseenter').trigger('mouseleave');
      assertTrue("T1: There was no callback call!", callbacks[0].calledOnce);
      assertTrue("T1: There was no callback call!", callbacks[1].calledOnce);

      //Test 2 - trigger mouse leave
      $(testSelectors[1]).trigger('mouseenter').trigger('mouseleave');
      assertTrue("T2: There was no callback call!", callbacks[2].calledOnce);
      assertTrue("T2: There was no callback call!", callbacks[3].calledOnce);

      //Test 3 - returned object
      assertEquals("Returned value is not a {CoreObj}", jVersion, testReturns[0].jquery);
      assertEquals("Returned value is not a {CoreObj}", jVersion, testReturns[1].jquery);
    },

    //////////
    ////////// ready
    //////////
    testReady: function() {
      var _self = this,
          ready = this.event.ready;

      assertFunction("core.event.ready is not a function.", ready);
      assertObject(ready(document));
    }
  });

  new TestCase("CORE :: base :: base.js :: object", {
    object: core.object,

    //////////
    ////////// createDeferred
    //////////
    testCreateDeferred: function() {
      var _self = this,
          createDeferred = this.object.createDeferred,
          obj = false;

      assertFunction("core.object.createDeferred is not a function.", createDeferred);
      obj = createDeferred();
      assertFunction(obj.then);
    }
  });
  
  new TestCase("CORE :: base :: base.js :: REST", {
    object: core.object,

    //////////
    ////////// create
    //////////
    testCreate: function() {
      var _self = this;

      assertFunction("core.REST.create is not a function.", core.REST.create);
    },
    
    //////////
    ////////// get
    //////////
    testGet: function() {
      var _self = this;

      assertFunction("core.REST.get is not a function.", core.REST.get);
    },
    
    //////////
    ////////// getManager
    //////////
    testGetManager: function() {
      var _self = this,
          manager = false;

      assertFunction("core.REST.getManager is not a function.", core.REST.getManager);
      manager = core.REST.getManager();
      assertEquals("defaultManager", manager.name);
    },
    
    //////////
    ////////// update
    //////////
    testUpdate: function() {
      var _self = this;

      assertFunction("core.REST.update is not a function.", core.REST.update);
    },
    
    //////////
    ////////// remove
    //////////
    testRemove: function() {
      var _self = this;

      assertFunction("core.REST.remove is not a function.", core.REST.remove);
    },
    
    //////////
    ////////// setManager
    //////////
    testSetManager: function() {
      var _self = this,
          manager = false,
          ajaxManager = false;

      assertFunction("core.REST.setManager is not a function.", core.REST.setManager);

      ajaxManager = core.AJAX.createManager('myManager');
      core.REST.setManager(ajaxManager);
      manager = core.REST.getManager();
      assertEquals("myManager", manager.name);
    }
  });
}());