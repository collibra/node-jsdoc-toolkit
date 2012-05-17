/*global
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

  new TestCase("CORE :: parts :: utils :: utils.js :: object :: template", {
    object: core.object,
    template: core.template,
    
    testObjectNull: function() {
      var _self = this,
          isNull = _self.object.isNull;
      
      assertFalse( isNull({}) );
      assertFalse( isNull([]) );
      assertFalse( isNull(undefined) );
      assertFalse( isNull(0) );
      assertFalse( isNull(1) );
      assertFalse( isNull("") );
      assertFalse( isNull('') );
      assertFalse( isNull(7.5e-3) );
      assertFalse( isNull(0x34) );
      assertFalse( isNull(true) );
      assertFalse( isNull(NaN) );
      assertFalse( isNull(new Date()) );
      assertFalse( isNull(Number.MAX_VALUE) );
      
      assertTrue( isNull(null) );
      
    },
    
    testObjectUndefined: function() {
      var _self = this,
          isUndefined = _self.object.isUndefined;

      assertFalse( isUndefined({}) );
      assertFalse( isUndefined([]) );
      assertFalse( isUndefined(null) );
      assertFalse( isUndefined(0) );
      assertFalse( isUndefined(1) );
      assertFalse( isUndefined("") );
      assertFalse( isUndefined('') );
      assertFalse( isUndefined(7.5e-3) );
      assertFalse( isUndefined(0x34) );
      assertFalse( isUndefined(true) );
      assertFalse( isUndefined(NaN) );
      assertFalse( isUndefined(new Date()) );
      assertFalse( isUndefined(Number.MAX_VALUE) );
      
      assertTrue( isUndefined(undefined) );
    },
    
    testTemplateCreate: function() {
      var _self = this,
          create = _self.template.create;
      
      assertFunction("Created template is not correct function", create("hello: <%= name %>"));
    },
    
    testTemplateGetDelimiter: function() {
      var _self = this,
          get = _self.template.getDelimiter;
      
      assertTrue(get() instanceof RegExp);
    },
    
    testTemplateSetDelimiter: function() {
      var _self = this,
          set = _self.template.setDelimiter,
          get = _self.template.getDelimiter;
      
      set(null);
      assertNull(get());
      
      set(/\{\{(.+?)\}\}/g);
      assertTrue(get() instanceof RegExp);
    },
    
    testTemplateUse: function() {
      var _self = this,
          use= _self.template.use,
          create= _self.template.create,
          get = _self.template.getDelimiter,
          set = _self.template.setDelimiter,
          tpl = null;
      
      set(/\{\{(.+?)\}\}/g);
      assertTrue(get() instanceof RegExp);
      
      tpl = create( "Hello {{ name }}!");
      assertEquals( use(tpl, {name : "World"}), "Hello World!");
    }
  });
}());