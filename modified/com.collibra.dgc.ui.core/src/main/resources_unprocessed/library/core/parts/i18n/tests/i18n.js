/*global
TestCase
assertFunction
console
core
assertEquals
assertBoolean
assertUndefined
assertNotUndefined
assertObject
*/
(function() {
  "use strict";

  new TestCase('core :: parts :: i18n :: i18n.js :: i18n', {
    i18n: core.i18n,
    //////////
    ////////// add
    //////////
    testAdd: function() {
      var _self = this;

      assertFunction('Add of the i18n is not a function!', _self.i18n.add);

      _self.i18n.add('test1', 'test2', 'testTrans');
      assertEquals('testTrans', _self.i18n.get('test1.test2'));
 
      _self.i18n.add('test', 'test1', '{0} {1} {2}');
      assertEquals('nul een twee', _self.i18n.get('test.test1', 'nul', 'een', 'twee'));

      _self.i18n.add('test', 'test', '{0 {1} 2} {}');
      assertEquals('{0 een 2} {}', _self.i18n.get('test.test', 'nul', 'een', 'twee', 'drie'));

    },

    //////////
    ////////// get
    //////////
    testGet: function() {
      var _self = this,
          get = _self.i18n.get;

      assertFunction('get of the i18n is not a function!', get);

      _self.i18n.add('test', 'test1', '{0} {1} {2}');
      assertEquals('nul een twee', get('test.test1', 'nul', 'een', 'twee'));

      _self.i18n.add('test', 'test', '{0 {1} 2} {}');
      assertEquals('{0 een 2} {}', get('test.test', 'nul', 'een', 'twee', 'drie'));

      assertEquals('nul een ', get('test.test1', 'nul', 'een'));

    },

    //////////
    ////////// getTranslations
    //////////
    testGetTranslations: function() {
      var _self = this,
          getTranslations = _self.i18n.getTranslations,
          translationObject = {};

      assertFunction('getTranslations of the i18n is not a function!', getTranslations);

      _self.i18n.add('test', 'test1', '{0} {1} {2}');
      assertObject('getTranslations: returned value is not an object', getTranslations('test'));

      _self.i18n.add('test', 'test', '{0 {1} 2} {}');
      translationObject = getTranslations('test');
      assertNotUndefined(translationObject.test);
      assertNotUndefined(translationObject.test1);
    },

    //////////
    ////////// remove
    //////////
    testRemove: function() {
      var _self = this,
          remove = _self.i18n.remove;

      assertFunction('remove of the i18n is not a function!', remove);
      
      _self.i18n.add('test1', 'test2', 'testTrans');
      assertEquals('testTrans', _self.i18n.get('test1.test2'));
      _self.i18n.remove('test1','test2');
      assertNotEquals('testTrans', _self.i18n.get('test1.test2'));
    }
  });
}());