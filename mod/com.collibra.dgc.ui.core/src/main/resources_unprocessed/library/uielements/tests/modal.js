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
  
  var html = '<div class="test">'
    + '<button id="modalInit">Show modal</button>'
    + '</div>',
      modalOptions = {
        title: 'Test title',
        description: 'Test description',
        content: 'Test content'
      };

  new AsyncTestCase('uielements :: modal', {
    
    //////////
    ////////// create
    //////////
    testCreate: function(queue) {
      queue.call(function(callbacks) {
        core.dom.create(html).appendTo("BODY");
        
        var moduleLoaded = callbacks.add(function(Modal) {
          var fn = Modal.create,
              initiator = core.dom.select('#modalInit');
          
          assertFunction("This function doesn't exists", fn);
          
          assertTrue("Modal is not created", fn(initiator, modalOptions));
          assertTrue("Can't find created modal", core.dom.select('#' + initiator.attr('modalid')).hasClass('ui-dialog-content'));
          assertEquals("Content of modal is not equal", modalOptions.content, core.dom.select('#' + initiator.attr('modalid')).text());
          
        });
        core.module.use('ModalWindow', moduleLoaded);
      });
    },
    
    //////////
    ////////// destroy
    //////////
    
    testDestroy: function(queue) {
      queue.call(function(callbacks) {
        core.dom.create(html).appendTo("BODY");
        
        var moduleLoaded = callbacks.add(function(Modal) {
          var fn = Modal.destroy,
              initiator = core.dom.select('#modalInit');
          
          assertFunction("This function doesn't exists", fn);
          
          assertTrue("Modal is not created", Modal.create(initiator, modalOptions));
          assertTrue("Can't find created modal", core.dom.select('#' + initiator.attr('modalid')).hasClass('ui-dialog-content'));
          
          assertTrue("Initiator doesn't have modal", Modal.has(initiator));
          
          assertTrue("Modal still exist!", Modal.destroy(initiator));
          assertFalse("I've found destroyed modal.", core.dom.select('#' + initiator.attr('modalid')).hasClass('ui-dialog-content'));
          
        });
        core.module.use('ModalWindow', moduleLoaded);
      });
    },
    
  
    //////////
    ////////// has
    //////////
    testHas: function(queue) {
      queue.call(function(callbacks) {
        core.dom.create(html).appendTo("BODY");
        
        var moduleLoaded = callbacks.add(function(Modal) {
          var fn = Modal.has,
              initiator = core.dom.select('#modalInit');
          
          assertFunction("This function doesn't exists", fn);
          
          assertTrue("Modal is not created", Modal.create(initiator, modalOptions));
          assertTrue("Can't find created modal", core.dom.select('#' + initiator.attr('modalid')).hasClass('ui-dialog-content'));
          
          assertTrue("Initiator doesn't have modal", Modal.has(initiator));
          
        });
        core.module.use('ModalWindow', moduleLoaded);
      });
    },

    //////////
    ////////// hide
    //////////
    testHide: function(queue) {
      queue.call(function(callbacks) {
        core.dom.create(html).appendTo("BODY");
        
        var moduleLoaded = callbacks.add(function(Modal) {
          var fn = Modal.hide,
              initiator = core.dom.select('#modalInit');
          
          assertFunction("This function doesn't exists", fn);
          
          assertTrue("Modal is not created", Modal.create(initiator, modalOptions));
          assertTrue("Can't find created modal", core.dom.select('#' + initiator.attr('modalid')).hasClass('ui-dialog-content'));
          
          assertTrue("Can't show modal", Modal.show(initiator));
          assertTrue("Showed modal is not visible...", core.dom.select('#' + initiator.attr('modalid')).is(':visible'));
          
        });
        core.module.use('ModalWindow', moduleLoaded);
      });
      
      queue.call(function(callbacks) {
        var checkHidden = callbacks.add(function() {
          var initiator = core.dom.select('#modalInit');
          assertFalse("Hided modal is visible...", core.dom.select('#' + initiator.attr('modalid')).is(':visible'));
        });
        
        var afterHide = callbacks.add(function(Modal) {
          var initiator = core.dom.select('#modalInit');
          
          assertTrue("Can't hide modal", Modal.hide(initiator));
          setTimeout(function() {
            checkHidden();
          }, 1000);
        });

        core.module.use('ModalWindow', afterHide);
      });
    },
    
    //////////
    ////////// show
    //////////
    testShow: function(queue) {
      queue.call(function(callbacks) {
        core.dom.create(html).appendTo("BODY");
        
        var moduleLoaded = callbacks.add(function(Modal) {
          var fn = Modal.show,
              initiator = core.dom.select('#modalInit');
          
          assertFunction("This function doesn't exists", fn);
          
          assertTrue("Modal is not created", Modal.create(initiator, modalOptions));
          assertTrue("Can't find created modal", core.dom.select('#' + initiator.attr('modalid')).hasClass('ui-dialog-content'));
          
          assertFalse("Hidden modal is visible...", core.dom.select('#' + initiator.attr('modalid')).is(':visible'));
          
          assertTrue("Can't show modal", Modal.show(initiator));
          
          assertTrue("Showed modal is not visible...", core.dom.select('#' + initiator.attr('modalid')).is(':visible'));
          
        });
        core.module.use('ModalWindow', moduleLoaded);
      });
    }
    
  });
}());