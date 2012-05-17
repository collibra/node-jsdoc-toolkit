/*global
AsyncTestCase
assertFunction
assertEquals
assertTrue
assertObject
assertFalse
sinon
jstestdriver
console
core
*/
(function() {
  "use strict";
  
  var select = "<select id='s1'><option value='1'>1</option><option value='2'>2</option></select>",
      select_multi = "<select id='s2' multiple='multiple'><option selected='selected' value='1'>1</option><option selected='selected' value='2'>2</option></select>",
      
      div = "<div id='d1'></div>",
      
      //Unviversal loading/creating method.
      init = function(queue, nextCall) {
        var DropDown = null;
        queue.call(function(callbacks) {
          var moduleLoaded = callbacks.add(function(module) {
            DropDown = module;
            
            assertObject('modulemodule is not a Object!', DropDown);
            assertFunction('create of the DropDown module is not a function!', DropDown.create);
            core.dom.create(select).appendTo("body");
            core.dom.create(select_multi).appendTo("body");
            
            //Create single, dropdown
            assertTrue("DropDown single creation failed.", DropDown.create("#s1"));
            
            //Create multi, dropdown
            assertTrue("DropDown multi creation failed.", DropDown.create("#s2", { multiple: true }));
            
          });
          core.module.use('DropDown', moduleLoaded);
        });
        
        //All is loaded and created, do other stuff now.
        queue.call(function() {
          nextCall(DropDown);
        });
      };

  new AsyncTestCase('uielements :: DropDown', {
    
    //////////
    ////////// Create
    //////////
    testCreate: function(queue) {

      init(queue, function(DropDown) {
        //From here on we have loaded module and created method fired.
        
        assertObject("DropDown is not an object", DropDown);
        
        //Check if it was created.
        assertTrue('DropDown wasnt created. ', core.dom.select("#s1").hasClass("with-dropdown"));
        
      });
    },
    
    //////////
    ////////// Is
    //////////
    testIs: function(queue) {
      
      init(queue, function(DropDown) {
        
        //Check if it was created.
        assertTrue('DropDown wasnt created. ', DropDown.is("#s1"));
        
      });
    },
    
    //////////
    ////////// Open
    //////////
    testOpen: function(queue) {
      
      init(queue, function(DropDown) {
        
        //Check if it was created.
        assertTrue('DropDown wasnt created. ', core.dom.select("#s1").hasClass("with-dropdown"));
        
        //------------
        
        //Check if its open.
        assertFalse("DropDown is already open.", core.dom.select("#s1").next().hasClass("select2-dropdown-open"));
        
        //Open it.
        assertTrue("DropDown open failed.", DropDown.open("#s1"));
        
        //Check if its open.
        assertTrue("DropDown is not opened.", core.dom.select("#s1").next().hasClass("select2-dropdown-open"));
      });
    },
    
    //////////
    ////////// Close
    //////////
    testClose: function(queue) {
      
      init(queue, function(DropDown) {
        
        //Check if it was created.
        assertTrue('DropDown wasnt created. ', core.dom.select("#s1").hasClass("with-dropdown"));
        
        //------------
        
        //Open it.
        assertTrue("DropDown open for close failed.", DropDown.open("#s1"));
        
        //Check if its closed.
        assertTrue("DropDown is already closed.", core.dom.select("#s1").next().hasClass("select2-dropdown-open"));
        
        //Closed it.
        assertTrue("DropDown close failed.", DropDown.close("#s1"));
        
        //Check if its closed.
        assertFalse("DropDown is not closed.", core.dom.select("#s1").next().hasClass("select2-dropdown-open"));
      });
    },
    
    //////////
    ////////// Toggle
    //////////
    testToggle: function(queue) {
      
      init(queue, function(DropDown) {
        
        //Check if was created.
        assertTrue('DropDown wasnt created. ', core.dom.select("#s1").hasClass("with-dropdown"));
        
        //------------
        
        //Check if its closed.
        assertFalse("DropDown is not closed.", core.dom.select("#s1").next().hasClass("select2-dropdown-open"));
        
        //Toggle it(open).
        assertTrue("DropDown toggle failed.", DropDown.toggle("#s1"));
        
        //Check if its open.
        assertTrue("DropDown is not opened.", core.dom.select("#s1").next().hasClass("select2-dropdown-open"));
        
        //Toggle it(close).
        assertTrue("DropDown toggle failed.", DropDown.toggle("#s1"));
        
        //Check if its closed.
        assertFalse("DropDown is not closed.", core.dom.select("#s1").next().hasClass("select2-dropdown-open"));
      });
    },

    //////////
    ////////// getValue
    //////////
    testGetValue: function(queue) {
      
      init(queue, function(DropDown) {
        
        //Check if was created.
        assertTrue('DropDown wasnt created. ', core.dom.select("#s1").hasClass("with-dropdown"));
        
        //------------
        
        //Get values from single.
        assertEquals("DropDown getValues from single failed.", "1", DropDown.getValue("#s1"));
        
        //Get values from multi.
        assertEquals("DropDown getValues from multi failed.", ["1", "2"], DropDown.getValue("#s2"));
        
      });
    },

    //////////
    ////////// setValue
    //////////
    testSetValue: function(queue) {
      
      init(queue, function(DropDown) {
        
        //Check if was created.
        assertTrue('DropDown wasnt created. ', core.dom.select("#s1").hasClass("with-dropdown"));
        
        //------------
        
        //Get values from single before set.
        assertEquals("DropDown getValues from single failed.", "1", DropDown.getValue("#s1"));
        
        //Set values on single.
        assertTrue("DropDown setValues on single failed.", DropDown.setValue("#s1", "2"));
        
        //Get values from single after set.
        assertEquals("DropDown getValues from single failed.", "2", DropDown.getValue("#s1"));
        
        //------------
        
        //Get values from multi before set.
        assertEquals("DropDown getValues from multi failed.", ["1", "2"], DropDown.getValue("#s2"));
        
        //Set values on multi.
        assertTrue("DropDown getValues on multi failed.", DropDown.setValue("#s2", ["2"]));
        
        //Get values from multi after set.
        assertEquals("DropDown getValues from multi failed.", ["2"], DropDown.getValue("#s2"));
        
      });
    },
    
    //////////
    ////////// onChange
    //////////
    testOnChange: function(queue) {
      
      init(queue, function(DropDown) {
        
        var callbackChange = sinon.spy();
        
        //Check if was created.
        assertTrue('DropDown wasnt created. ', core.dom.select("#s1").hasClass("with-dropdown"));
        
        //------------
        
        //Attach onChange event to dropdown.
        assertTrue("DropDown onChange failed.", DropDown.onChange("#s1", callbackChange));
        
        //Set values on single.
        assertTrue("DropDown setValues on single failed.", DropDown.setValue("#s1", "2", true));
        
        //Check if our callback was fired.
        assertTrue("Callback onChange wasnt fired.", callbackChange.calledOnce);
        
      });
    },
    
    //////////
    ////////// destroy
    //////////
    testDestroy: function(queue) {
      
      init(queue, function(DropDown) {
        
        //Check if it was created.
        assertTrue('DropDown wasnt created. ', DropDown.is("#s1"));
       
        //------------
        
        //Now destroy it.
        assertTrue('DropDown destroy failed. ', DropDown.destroy("#s1"));
        
        //Check with 'DropDown.is'.
        assertFalse('DropDown is still applied. ', DropDown.is("#s1"));
        
        //Check created markup.
        assertFalse("Dropdown markup still exists.", core.dom.create("#s1").next().hasClass("select2-container"));
        
        //Check selector.
        assertFalse('DropDown still has dropdown classes. ', core.dom.create("#s1").hasClass("with-dropdown") ||
                                                            core.dom.create("#s1").hasClass("single") ||
                                                            core.dom.create("#s1").hasClass("multi") ||
                                                            core.dom.create("#s1").hasClass("autocomplete"));
        //Check onChange event,
        assertEquals("There are still change events attached to dropdown", 1, core.dom.create("#s1").data("events").change.length);
        assertEquals("There are others(not change) events attached to dropdown.", 1, Object.keys(core.dom.select("#s1").data("events")).length);
        
      });
    },
    
    //////////
    ////////// reset
    //////////
    testReset: function(queue) {
      init(queue, function(DropDown) {
        
        //Check if was created.
        assertTrue('DropDown wasnt created. ', core.dom.select("#s1").hasClass("with-dropdown"));
        
        //------------
        
        //Get values from single before reset.
        assertEquals("DropDown getValues from single failed.", "1", DropDown.getValue("#s1"));
        //Get values from multi before reset.
        assertEquals("DropDown getValues from multi failed.", ["1", "2"], DropDown.getValue("#s2"));
        
        //Set values on single before reset.
        assertTrue("DropDown setValues on single failed.", DropDown.setValue("#s1", "2"));
        //Set values on multi before reset.
        assertTrue("multi setValues on multi failed.", DropDown.setValue("#s2", "2"));
        
        //Get values from single before reset and after set.
        assertEquals("DropDown getValues from single failed.", "2", DropDown.getValue("#s1"));
        //Get values from multi before reset and after set.
        assertEquals("DropDown getValues from multi failed.", "2", DropDown.getValue("#s2"));
        
        //Set values on single before reset.
        assertTrue("DropDown reset on single failed.", DropDown.reset("#s1"));
        //Set values on multi before reset.
        assertTrue("DropDown reset on multi failed.", DropDown.reset("#s2"));
        
        //Get values from single after reset.
        assertEquals("DropDown getValues after reset from single failed.", "1", DropDown.getValue("#s1"));
        //Get values from multi after reset.
        assertEquals("DropDown getValues after reset from multi failed.", ["1", "2"], DropDown.getValue("#s2"));
        
      });
    },
    
    //////////
    ////////// refresh
    //////////
    testRefresh: function(queue) {
      init(queue, function(DropDown) {
        
        //Check if it was created.
        assertTrue('DropDown wasnt created. ', DropDown.is("#s1"));
       
        //------------
        
        //Now refresh it(regenerate).
        assertTrue('DropDown refresh failed. ', DropDown.refresh("#s1"));
        
        //Check with 'DropDown.is'.
        assertTrue('DropDown wasnt created. ', DropDown.is("#s1"));
        
        //Check created markup.
        assertTrue("Dropdown markup doesnt exists.", core.dom.create("#s1").next().hasClass("select2-container"));
        
        //Check selector.
        assertTrue('DropDown still has dropdown classes. ', core.dom.create("#s1").hasClass("with-dropdown") ||
                                                            core.dom.create("#s1").hasClass("single") ||
                                                            core.dom.create("#s1").hasClass("multi") ||
                                                            core.dom.create("#s1").hasClass("autocomplete"));
        //Check onChange event,
        assertEquals("There are still change events attached to dropdown", 2, core.dom.create("#s1").data("events").change.length);
        assertEquals("There are others(not change) events attached to dropdown.", 1, Object.keys(core.dom.select("#s1").data("events")).length);
        
      });
    },
  });
}());