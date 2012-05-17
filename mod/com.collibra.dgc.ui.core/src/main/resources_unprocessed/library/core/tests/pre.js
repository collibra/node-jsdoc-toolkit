/*global core, TestCase, assertEquals, assertNotEquals, assertNotUndefined, assertUndefined, assertObject, assertString, assertFunction, assertNumber, assertArray, assertBoolean, assertTrue, assertFalse */

(function() {
  "use strict";

  var testArray = [],
      testObject = {},
      testNumber = 3,
      testString = "",
      testBoolean = true,
      testNull = null,
      testUndefined;

  new TestCase("CORE :: pre_core.js", {
    //////////
    ////////// .defaults();
    //////////
    testFunctionDefaults: function() {
      var testFunction = function(a, b, c) {
            return a + b;
          }.defaults(1, 2, {aa:{bb:1}}),
          testFunctionString = function(a) {
            return a;
          }.defaults('Test'),
          testFunctionObject = function(c) {
            return c.aa.bb + c.aa.cc;
          }.defaults({
            aa:{
              bb:1,
              cc:2
            }
          });

      assertEquals(3, testFunction());
      assertEquals(7, testFunction(5));
      assertEquals(11, testFunction(3, 8));
      assertEquals(3, testFunctionObject());
      assertEquals(668, testFunctionObject({
        aa:{
          bb:666
        }
      }));

      assertEquals(testFunctionString(), 'Test');
    },

    //////////
    ////////// core
    //////////
    testCore: function() {
      assertNotUndefined(core);
    },

    //////////
    ////////// _private
    //////////
    testPrivate: function() {
      assertNotUndefined(core._private);
      assertNotUndefined(core._private.array);
    },

    //////////
    ////////// VERSION
    //////////
    testVERSION: function() {
      assertString(core.VERSION);
      assertNotEquals(0, core.VERSION.length);
    }
  });

  new TestCase("CORE :: pre_core.js :: array", {
    array: core.array,
    arrayOne: [1, 2, 3],
    arrayTwo: ['one', 'two', 'three'],
    arrayThree: [],

    //////////
    ////////// Each
    //////////
    testEach: function() {
      var output = 0,
          ctr = 0,
          array = this.array,
          _self = this;

      assertFunction(array.each);

      array.each(this.arrayOne, function(itm) {
        output = output + itm;
      });

      assertEquals(6, output);

      output = '';
      array.each(this.arrayTwo, function(idx, itm, arr) {
        ctr++;
        assertNumber(idx);
        assertArray(arr);
        assertEquals(itm, _self.arrayTwo[idx]);
        output = output + _self.arrayTwo[idx] + ' ';
      });

      assertEquals('one two three ', output);
      assertEquals(3, ctr);

      ctr = 0;
      array.each(this.arrayThree, function(idx, itm, arr) {
        ctr++;
      });

      assertEquals(0, ctr);
    },

    //////////
    ////////// Is
    //////////
    testIs: function() {
      var array = this.array;

      assertFunction(array.is);
      assertBoolean(array.is(this.arrayOne));

      assertTrue(array.is(this.arrayTwo));

      assertFalse(array.is(testString));
      assertFalse(array.is(testBoolean));
      assertFalse(array.is(testNumber));
      assertFalse(array.is(testObject));
      assertFalse(array.is(testNull));
      assertFalse(array.is(testUndefined));
    },

    //////////
    ////////// sort
    //////////
    testSort: function() {
      var array = this.array,
          testArray1 = [3,2,4,5,1],
          testArray2 = ["BB","AB","AA","CC","BA"];

      assertArray(testArray1);
      
      assertFunction(array.sort);
      array.sort(testArray1);
      assertArray(testArray1);
      assertEquals(1, testArray1[0]);
      assertEquals(5, testArray1[4]);
      
      array.sort(testArray2);
      assertArray(testArray2);
      assertEquals("AA", testArray2[0]);
      assertEquals("CC", testArray2[4]);
      
      array.sort(testArray2,true);
      assertEquals("CC", testArray2[0]);
      assertEquals("AA", testArray2[4]);
    },

    //////////
    ////////// sortBy
    //////////
    testSortBy: function() {
      var array = this.array
          testArray = ["AB","1D","ZA","AC"];

      assertFunction(array.sortBy);
      
      array.sortBy(testArray, function(a) {
        return a[1]; //get 2nd letter
      });
      assertEquals("ZA", testArray[0]);
      assertEquals("1D", testArray[3]);
    }
  });

  new TestCase("CORE :: pre_core.js :: boolean", {
    boolean: core.boolean,

    //////////
    ////////// Is
    //////////
    testIs: function() {
      var boolean = this.boolean;

      assertFunction(boolean.is);
      assertBoolean(boolean.is(testBoolean));

      assertTrue(boolean.is(testBoolean));

      assertFalse(boolean.is(testString));
      assertFalse(boolean.is(testArray));
      assertFalse(boolean.is(testNumber));
      assertFalse(boolean.is(testObject));
      assertFalse(boolean.is(testNull));
      assertFalse(boolean.is(testUndefined));
    }
  });

  new TestCase("CORE :: pre_core.js :: debug", {
    debug: core.debug,

    //////////
    ////////// disable
    //////////
    testDisable: function() {
      assertFunction(this.debug.disable);

      console.log(core._private.debug.enabled);
      core._private.debug.enabled = true;
      this.debug.disable();
      console.log(core._private.debug.enabled);
      assertFalse(core._private.debug.enabled);
    },

    //////////
    ////////// enable
    //////////
    testEnable: function() {
      assertFunction(this.debug.enable);

      core._private.debug.enabled = false;
      this.debug.enable();
      assertTrue(core._private.debug.enabled);
    },

    //////////
    ////////// show (can't be tasted)
    //////////
    
    //////////
    ////////// stopExecution
    //////////
    testStopExecution: function() {
      var stopExecution = this.debug.stopExecution;
      
      assertFunction(stopExecution);
      
    }
  });

  new TestCase("CORE :: pre_core.js :: debug :: exceptions", {
    exceptions: core.debug.exceptions,

    //////////
    ////////// illegalArgument
    //////////
    testIllegalArgument: function() {
      var exceptions = this.exceptions;

      assertFunction(exceptions.illegalArgument);
    },

    //////////
    ////////// enable
    //////////
    testIllegalNoOfArguments: function() {
      var exceptions = this.exceptions;

      assertFunction(exceptions.illegalNoOfArguments);
    }
  });

  new TestCase("CORE :: pre_core.js :: func", {
    func: core.func,

    //////////
    ////////// is
    //////////
    testIs: function() {
      var _self = this,
          is = _self.func.is,
          sampleFunction = function() {
              return 0;
            };

      assertFunction("core.func.is is not a function!", is);

      assertBoolean("A boolean value is not returned", is(sampleFunction));
      assertTrue("Sample function is not recognised", is(sampleFunction));

      assertFalse("A number is recognised as a function", is(testNumber));
      assertFalse("A string is recognised as a function", is(testString));
      assertFalse("An array is recognised as a function", is(testArray));
      assertFalse("An object is recognised as a function", is(testObject));
      assertFalse("A boolean value is recognised as a function", is(testBoolean));
      assertFalse("A null value is recognised as a function", is(testNull));
      assertFalse("An undefined value is recognised as a function", is(testUndefined));
    }
  });

  new TestCase("CORE :: pre_core.js :: libs", {
    libs: core.libs,

    //////////
    ////////// add
    //////////
    testAdd: function() {
      var _self = this,
          add = _self.libs.add,
          name = "abcd";

      assertFunction("core.libs.add is not a function!", add);

      add(name, testObject, false);
      assertUndefined("Library is an exposed one", core.libs.abcd);

      add(name, testObject, true);
      assertNotUndefined("Library is not exposed", core.libs.abcd);
    },

    //////////
    ////////// get
    //////////
    testGet: function() {
      var _self = this,
          get = _self.libs.get,
          name = "abcd";

      assertFunction("core.libs.get is not a function!", get);

      _self.libs.add(name, testObject, false);
      assertObject("Can't get the library", get(name));

      _self.libs.add(name, testObject, true);
      assertObject("Can't get the library", get(name));
    },

    //////////
    ////////// list
    //////////
    testList: function() {
      var _self = this,
          list = _self.libs.list;

      assertFunction("core.libs.list is not a function!", list);

      assertString("List of libraries is not a string", list());
    },

    //////////
    ////////// remove
    //////////
    testRemove: function() {
      var _self = this,
          remove = _self.libs.remove,
          name = "abcd";

      assertFunction("core.libs.remove is not a function!", remove);

      _self.libs.add(name, testObject, true);
      assertNotUndefined("Library is not exposed", core.libs.abcd);
      remove(name);
      assertUndefined("Library is an exposed one", core.libs.abcd);
    }
  });

  new TestCase("CORE :: pre_core.js :: object", {
    object: core.object,

    //////////
    ////////// copy
    //////////
    testCopy: function() {
      var _self = this,
          copy = _self.object.copy;

      assertFunction("core.object.copy is not a function!", copy);

      assertObject("Returned value is not an object", copy(testObject));
    },

    //////////
    ////////// createFromNS
    //////////
    testCreateFromNS: function() {
      var _self = this,
          createFromNS = _self.object.createFromNS,
          testNS = {};

      assertFunction("core.object.createFromNS is not a function!", createFromNS);

      testNS = createFromNS("one.two.three");
      assertObject("Returned value is not an object", testNS);
      assertObject("Returned value is not an object", testNS.one);
      assertObject("Returned value is not an object", testNS.one.two);

      testNS = createFromNS("one.two.three", function(i) {
        i = 5;
        return i;
      });

      assertEquals(5, testNS.one.two.three);
    },

    //////////
    ////////// each
    //////////
    testEach: function() {
      var _self = this,
          each = _self.object.each,
          objectOne = {one: 1, two: 2, three: 3},
          output = 0,
          ctr = 0;

      assertFunction("core.object.each is not a function!", each);

      each(objectOne, function(itm, val) {
        ctr++;
        output = output + val;
        assertNumber(val);
      });

      assertEquals(6, output);
      assertEquals(3, ctr);

      ctr = 0;
      each(testObject, function(itm) {
        ctr++;
      });

      assertEquals(0, ctr);
    },

    //////////
    ////////// extend
    //////////
    testExtend: function() {
      var _self = this,
          extend = _self.object.extend,
          objectOne = {one: 1},
          objectTwo = {two: 2};

      assertFunction("core.object.extend is not a function!", extend);

      var test = extend(objectOne, objectTwo);
      assertEquals(test, {one: 1, two: 2});

      var test2 = extend(objectTwo, objectOne);
      assertEquals(test2, {two: 2, one: 1});
    },

    //////////
    ////////// is
    //////////
    testIs: function() {
      var _self = this,
          is = _self.object.is;

      assertFunction("core.object.is is not a function!", is);

      assertBoolean("A boolean value is not returned", is(testObject));
      assertTrue("Sample object is not recognised", is(testObject));

      assertFalse("A number is recognised as an object", is(testNumber));
      assertFalse("A string is recognised as an object", is(testString));
      assertFalse("An array is recognised as an plain object", is(testArray));
      assertTrue("An array is not recognised as a general object", is(testArray, false));
      assertFalse("A boolean value is recognised as an object", is(testBoolean));
      assertFalse("A null value is recognised as an object", is(testNull));
      assertFalse("An undefined value is recognised as an object", is(testUndefined));
    }
  });

  new TestCase("CORE :: pre_core.js :: string", {
    string: core.string,

    //////////
    ////////// is
    //////////
    testIs: function() {
      var _self = this,
          is = _self.string.is;

      assertFunction("core.string.is is not a function!", is);

      assertBoolean("A boolean value is not returned", is(testString));
      assertTrue("A string is not recognised", is(testString));

      assertFalse("A number is recognised as a string", is(testNumber));
      assertFalse("A string is recognised as a string", is(testObject));
      assertFalse("An array is recognised as a string", is(testArray));
      assertFalse("A boolean value is recognised as a string", is(testBoolean));
      assertFalse("A null value is recognised as a string", is(testNull));
      assertFalse("An undefined value is recognised as a string", is(testUndefined));
    }
  });

}());