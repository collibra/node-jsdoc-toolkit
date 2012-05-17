/*global
AsyncTestCase
assertFunction
assertEquals
assertTrue
assertFalse
assertBoolean
assertObject
assertUndefined
core
*/
(function() {
  "use strict";
  var textarea = "<textarea id='t'>aaa</textarea>" +
                 "<textarea id='t1'>bbb</textarea>" +
                 "<textarea id='t2'></textarea>"

  new AsyncTestCase('uielements :: TextEditor', {



    //////////
    ////////// Create
    //////////
    testCreate: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var create = TextEditor.create,
              callbackOnSave = sinon.spy(),
              callbackOnDiscard = sinon.spy(),
              callbackonComplete = sinon.spy(),
              fi = 0,
              ri = 0;

          assertFunction('create of the TextEditor uielement is not a function!', create);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor t1 wasnt correctly created.', create('#t1', TextEditor.FULL));
          assertTrue('TextEditor t wasnt correctly created.', create('#t', TextEditor.REDUCED, {
            save: {
              automatic: false,
              buttons: {
                show: true,
                position: "bottom",
                onSave: callbackOnSave,
                onDiscard: callbackOnDiscard
              },
              url: "http://localhost/treeGET.php"
            },
            content: "precontent",
            label: 'prelabel'
          }));
          
          //check if the texteditor was created(by api and manually)
          assertTrue('Textarea is not TextEditor.', TextEditor.is('#t'));
          assertTrue('TextEditor is not created.', core.dom.select('#t').hasClass("with-texteditor"));
          
          //content and label check
          assertEquals("TextEditor predefined label is wrong", "prelabel", TextEditor.getLabel("#t"));
          assertEquals("TextEditor predefined content is wrong", "<span>precontent</span>", TextEditor.getContent("#t"));
          
          //check for existence of save/discard buttons
          assertTrue("TextEditor btn-success should be visible.", core.dom.is(core.dom.select("#t").prev().find(".btn-success")));
          assertTrue("TextEditor btn-danger should be visible.", core.dom.is(core.dom.select("#t").prev().find(".btn-danger")));
          
          //click buttons
          core.dom.select("#t").prev().find(".wysiwyg-buttons>button").trigger("click");
          
          //check if the buttons were clicked
          assertTrue("callbackOnSave wasnt called", callbackOnSave.calledOnce);
          assertTrue("callbackOnDiscard wasnt called", callbackOnDiscard.calledOnce);
          
          //count control buttons which are rendered for REDUCED texteditor
          var a = core.dom.select("#t").data("wysiwyg").options.controls;
          for(var c in a){
            if(a[c].visible) {
              ri++;
            }
          }
          //count control buttons which are rendered for FULL texteditor
          var a = core.dom.select("#t1").data("wysiwyg").options.controls;
          for(var c in a){
            if(a[c].visible) {
              fi++;
            }
          }
          //chech if there is a difference in controls number between FULL and REDUCED texteditor
          assertNotSame("There are the same number of rendered controls for REDUCED and FULL mode.", ri, fi)
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// Destroy
    //////////
    testDestroy: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var destroy = TextEditor.destroy;

          assertFunction('destroy of the TextEditor uielement is not a function!', destroy);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor wasnt correctly created for destroying.', TextEditor.create('#t'));
          assertTrue('Textarea is not TextEditor.', TextEditor.is('#t'));
          assertTrue('TextEditor is not created.', core.dom.select('#t').hasClass("with-texteditor"));
          destroy('#t');
          assertFalse('Textarea is still a TextEditor.', TextEditor.is('#t'));
          
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// Disable
    //////////
    testDisable: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var disable = TextEditor.disable;

          assertFunction('disable of the TextEditor uielement is not a function!', disable);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor wasnt correctly created for destroying.', TextEditor.create('#t'));
          assertTrue('Textarea is not TextEditor.', TextEditor.is('#t'));
          
          assertFalse('TextEditor is already disabled.', core.dom.is(core.dom.select("#t").prev().find("#texteditor-disabled-overlay")));
          assertTrue('disabling TextEditor failed.', disable('#t'));
          assertTrue('TextEditor is still disabled.', core.dom.is(core.dom.select("#t").prev().find("#texteditor-disabled-overlay")));
          
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// Enable
    //////////
    testEnable: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var enable = TextEditor.enable;

          assertFunction('enable of the TextEditor uielement is not a function!', enable);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor wasnt correctly created for destroying.', TextEditor.create('#t'));
          assertTrue('Textarea is not TextEditor.', TextEditor.is('#t'));
          
          assertTrue('disabling TextEditor failed.', TextEditor.disable('#t'));
          assertTrue('TextEditor is not disabled for enabling.', core.dom.is(core.dom.select("#t").prev().find("#texteditor-disabled-overlay")));
          assertTrue('enabling TextEditor failed.', enable('#t'));
          assertFalse('Textarea is still disabled after enabling.', core.dom.is(core.dom.select("#t").prev().find("#texteditor-disabled-overlay")));
          
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// getContent
    //////////
    testGetContent: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var getContent = TextEditor.getContent;

          assertFunction('getContent of the TextEditor uielement is not a function!', getContent);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor t wasnt correctly created.', TextEditor.create('#t'));
          assertTrue('Textarea is not TextEditor t.', TextEditor.is('#t'));
          assertTrue('TextEditor t1 wasnt correctly created.', TextEditor.create('#t1'));
          assertTrue('Textarea is not TextEditor t1.', TextEditor.is('#t1'));
          
          assertEquals('TextEditor t content is wrong.', "<p>aaa</p>", getContent('#t'));
          assertEquals('TextEditor t1 content is wrong.', "<p>bbb</p>", getContent('#t1'));
          
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// getLabel
    //////////
    testGetLabel: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var getLabel = TextEditor.getLabel;

          assertFunction('getLabel of the TextEditor uielement is not a function!', getLabel);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor t wasnt correctly created.', TextEditor.create('#t', TextEditor.REDUCED, {
            label: 'labelka'
          }));
          assertTrue('Textarea is not TextEditor t.', TextEditor.is('#t'));
          
          assertEquals('TextEditor t label is wrong.', "labelka", getLabel('#t'));
          
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// getSaveOptions
    //////////
    testGetSaveOptions: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var getSaveOptions = TextEditor.getSaveOptions,
              so = {
                  automatic: true,
                  saveInterval: 1234,
                  buttons: {
                    show: true,
                    position: "bogus",
                    onSave: function() { return 6;},
                    onDiscard: function() { return 7;}
                  },
                  url: '#',
                  onSuccess: function() { return 1;},
                  onError: function() { return 2;},
                  onAbort: function() { return 3;},
                  onComplete: function() { return 4;},
                  onRequest: function() { return 5;}
                }, 
                gso = null;

          assertFunction('getSaveOptions of the TextEditor uielement is not a function!', getSaveOptions);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor t wasnt correctly created.', TextEditor.create('#t', TextEditor.REDUCED, {
            save: {
              automatic: true,
              saveInterval: 1234,
              buttons: {
                show: true,
                position: "bogus",
                onSave: function() { return 6;},
                onDiscard: function() { return 7;}
              },
              url: '#',
              onSuccess: function() { return 1;},
              onError: function() { return 2;},
              onAbort: function() { return 3;},
              onComplete: function() { return 4;},
              onRequest: function() { return 5;}
            }
          }));
          assertTrue('Textarea is not TextEditor t.', TextEditor.is('#t'));
          gso = getSaveOptions('#t');
          assertObject('TextEditor t saveOptions is not an object.', gso);
          assertEquals('TextEditor t saveOptions are wrong.', so.automatic, gso.automatic);
          assertEquals('TextEditor t saveOptions are wrong.', so.saveInterval, gso.saveInterval);
          assertEquals('TextEditor t saveOptions are wrong.', so.buttons.show, gso.buttons.show);
          assertEquals('TextEditor t saveOptions are wrong.', so.buttons.position, gso.buttons.position);
          assertEquals('TextEditor t saveOptions are wrong.', so.buttons.onSave.toString(), gso.buttons.onSave.toString());
          assertEquals('TextEditor t saveOptions are wrong.', so.buttons.onDiscard.toString(), gso.buttons.onDiscard.toString());
          assertEquals('TextEditor t saveOptions are wrong.', so.url, gso.url);
          assertEquals('TextEditor t saveOptions are wrong.', so.onSuccess, gso.onSuccess.toString());
          assertEquals('TextEditor t saveOptions are wrong.', so.onError, gso.onError.toString());
          assertEquals('TextEditor t saveOptions are wrong.', so.onAbort, gso.onAbort.toString());
          assertEquals('TextEditor t saveOptions are wrong.', so.onComplete, gso.onComplete.toString());
          assertEquals('TextEditor t saveOptions are wrong.', so.onRequest, gso.onRequest.toString());
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// is
    //////////
    testIs: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var is = TextEditor.is;

          assertFunction('is of the TextEditor uielement is not a function!', is);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor wasnt correctly created.', TextEditor.create('#t'));
          assertTrue('Textarea is not TextEditor.', core.dom.select('#t').hasClass("with-texteditor"));
          assertTrue('Textarea is not TextEditor.', is('#t'));
          
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// isChanged
    //////////
    testIsChanged: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var isChanged = TextEditor.isChanged;

          assertFunction('isChanged of the TextEditor uielement is not a function!', isChanged);
          //this test is to be skipped
          //proper functionality is not yet implemented
          
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// isDisabled
    //////////
    testIsDisabled: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var isDisabled = TextEditor.isDisabled;

          assertFunction('isDisabled of the TextEditor uielement is not a function!', isDisabled);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor wasnt correctly created for destroying.', TextEditor.create('#t'));
          assertTrue('Textarea is not TextEditor.', TextEditor.is('#t'));
          //is already disabled?
          assertFalse('TextEditor is already disabled.', isDisabled('#t'));
          //is already disabled(manual check)?
          assertFalse('TextEditor is already disabled.', core.dom.is(core.dom.select("#t").prev().find("#texteditor-disabled-overlay")));
          //disable it now
          assertTrue('disabling TextEditor failed.', TextEditor.disable('#t'));
          //is already disabled?
          assertTrue('TextEditor is not disabled.', isDisabled('#t'));
          //is already disabled(manual check)?
          assertTrue('TextEditor is not disabled.', core.dom.is(core.dom.select("#t").prev().find("#texteditor-disabled-overlay")));
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// isFocused
    //////////
    testIsFocused: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var isFocused = TextEditor.isFocused;

          assertFunction('isFocused of the TextEditor uielement is not a function!', isFocused);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor wasnt correctly created for destroying.', TextEditor.create('#t'));
          assertTrue('Textarea is not TextEditor.', TextEditor.is('#t'));
          assertFalse('TextEditor is already focused.', isFocused('#t'));
          core.dom.select(core.dom.select("#t").data("wysiwyg").editor.get(0).contentWindow).focus();
          assertTrue('TextEditor is not focused.', isFocused('#t'));
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    
    //////////
    ////////// off
    //////////
    testOff: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var off = TextEditor.off,
              callback = sinon.spy();

          assertFunction('off of the TextEditor uielement is not a function!', off);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor wasnt correctly created for destroying.', TextEditor.create('#t'));
          assertTrue('Textarea is not TextEditor.', TextEditor.is('#t'));
          
          assertFalse("Callback was already called.", callback.calledOnce);
          TextEditor.on("#t", "customEventOff", callback);
          core.dom.select("#t").wysiwyg('document').trigger("customEventOff");
          assertTrue("Callback wasnt called", callback.calledOnce);
          off("#t", "customEventOff");
          core.dom.select("#t").wysiwyg('document').trigger("customEventOff");
          assertFalse("Callback was called after texteditor.off", callback.calledTwice);
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    
    //////////
    ////////// on
    //////////
    testOn: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var on = TextEditor.on,
              callback = sinon.spy();

          assertFunction('on of the TextEditor uielement is not a function!', on);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor wasnt correctly created for destroying.', TextEditor.create('#t'));
          assertTrue('Textarea is not TextEditor.', TextEditor.is('#t'));
          
          on("#t", "customEvent", callback);
          core.dom.select("#t").wysiwyg('document').trigger("customEvent");
          assertTrue("Callback wasnt called", callback.calledOnce);
          
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// resetContent
    //////////
    testResetContent: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var resetContent = TextEditor.resetContent;

          assertFunction('resetContent of the TextEditor uielement is not a function!', resetContent);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor wasnt correctly created for destroying.', TextEditor.create('#t', TextEditor.REDUCED, {content: "test"}));
          assertTrue('Textarea is not TextEditor.', TextEditor.is('#t'));
          assertTrue('TextEditor wasnt correctly created for destroying.', TextEditor.create('#t2', TextEditor.REDUCED, {label: "test label"}));
          assertTrue('Textarea is not TextEditor.', TextEditor.is('#t2'));
          
          //when content is predefined
          assertEquals("Texteditor t content is wrong", "<span>test</span>", TextEditor.getContent("#t"));
          assertTrue('TextEditor resetContent failed.', resetContent('#t'));
          assertEquals("Texteditor t content is wrong", "<span>test</span>", TextEditor.getContent("#t"));
          
          //when content is predefined and changed
          assertTrue('TextEditor setContent failed.', TextEditor.setContent('#t', "<span>some new content</span>"));
          assertEquals("Texteditor t content is wrong", "<span>some new content</span>", TextEditor.getContent("#t"));
          assertTrue('TextEditor resetContent failed.', resetContent('#t'));
          assertEquals("Texteditor t content is wrong", "<span>test</span>", TextEditor.getContent("#t"));
          
          //when theres no content but label is shown
          assertEquals("Texteditor t2 content is wrong", "", TextEditor.getContent("#t2"));
          assertTrue('TextEditor setContent failed.', TextEditor.setContent('#t2', "<span>some new content</span>"));
          assertEquals("Texteditor t2 content is wrong", "<span>some new content</span>", TextEditor.getContent("#t2"));
          assertTrue('TextEditor resetContent failed.', resetContent('#t2'));
          assertEquals("Texteditor t2 content is wrong", "", TextEditor.getContent("#t2"));
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// save (POSTPONED, because REST is unusable) 
    //////////
    testSave: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var save = TextEditor.save,
              completeCallback = sinon.spy(),
              startCallback = sinon.spy();

          assertFunction('save of the TextEditor uielement is not a function!', save);
          core.dom.create(textarea).appendTo("BODY");
//          assertTrue('TextEditor t wasnt correctly created.', TextEditor.create('#t', TextEditor.REDUCED, {
//            save: {
//              automatic: true,
//              url: 'http://localhost/treeGET.php',
//              onComplete: completeCallback,
//              onRequest: startCallback
//            }
//          }));
//          
//          save("#t");
//          assertTrue("startCallback wasnt called", startCallback.calledOnce);
//          assertTrue("completeCallback wasnt called", completeCallback.calledOnce);
          
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// setLabel
    //////////
    testSetLabel: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var setLabel = TextEditor.setLabel;

          assertFunction('setLabel of the TextEditor uielement is not a function!', setLabel);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor t wasnt correctly created.', TextEditor.create('#t', TextEditor.REDUCED, {
            label: 'labelka'
          }));
          assertTrue('Textarea is not TextEditor t.', TextEditor.is('#t'));
          
          assertEquals('TextEditor t label is wrong.', "labelka", TextEditor.getLabel('#t'));
          setLabel("#t", "new label");
          assertEquals('TextEditor t label is wrong.', "new label", TextEditor.getLabel('#t'));
          
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// setLabel
    //////////
    testSetSaveOptions: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var setSaveOptions = TextEditor.setSaveOptions,
              so = {
                  automatic: true,
                  saveInterval: 1234,
                  buttons: {
                    show: true,
                    position: "bogus",
                    onSave: function() { return 6;},
                    onDiscard: function() { return 7;}
                  },
                  url: '#',
                  onSuccess: function() { return 1;},
                  onError: function() { return 2;},
                  onAbort: function() { return 3;},
                  onComplete: function() { return 4;},
                  onRequest: function() { return 5;}
              },
              gso = null;

          assertFunction('setSaveOptions of the TextEditor uielement is not a function!', setSaveOptions);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor t wasnt correctly created.', TextEditor.create('#t'));
          assertTrue('Textarea is not TextEditor t.', TextEditor.is('#t'));
          
          setSaveOptions('#t', so);
          gso = TextEditor.getSaveOptions('#t');
          
          assertObject('TextEditor t saveOptions is not an object.', gso);
          assertEquals('TextEditor t saveOptions are wrong.', so.automatic, gso.automatic);
          assertEquals('TextEditor t saveOptions are wrong.', so.saveInterval, gso.saveInterval);
          assertEquals('TextEditor t saveOptions are wrong.', so.buttons.show, gso.buttons.show);
          assertEquals('TextEditor t saveOptions are wrong.', so.buttons.position, gso.buttons.position);
          assertEquals('TextEditor t saveOptions are wrong.', so.buttons.onSave.toString(), gso.buttons.onSave.toString());
          assertEquals('TextEditor t saveOptions are wrong.', so.buttons.onDiscard.toString(), gso.buttons.onDiscard.toString());
          assertEquals('TextEditor t saveOptions are wrong.', so.url, gso.url);
          assertEquals('TextEditor t saveOptions are wrong.', so.onSuccess, gso.onSuccess.toString());
          assertEquals('TextEditor t saveOptions are wrong.', so.onError, gso.onError.toString());
          assertEquals('TextEditor t saveOptions are wrong.', so.onAbort, gso.onAbort.toString());
          assertEquals('TextEditor t saveOptions are wrong.', so.onComplete, gso.onComplete.toString());
          assertEquals('TextEditor t saveOptions are wrong.', so.onRequest, gso.onRequest.toString());
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    },
    
    //////////
    ////////// setContent
    //////////
    testSetContent: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(TextEditor) {
          var setContent = TextEditor.setContent;

          assertFunction('setContent of the TextEditor uielement is not a function!', setContent);
          core.dom.create(textarea).appendTo("BODY");
          assertTrue('TextEditor wasnt correctly created for destroying.', TextEditor.create('#t', TextEditor.REDUCED, {content: "test"}));
          assertTrue('Textarea is not TextEditor.', TextEditor.is('#t'));
          
          assertEquals("Texteditor t content is wrong", "<span>test</span>", TextEditor.getContent("#t"));
          assertTrue('TextEditor setContent failed.', TextEditor.setContent('#t', "<span>some new content</span>"));
          assertEquals("Texteditor t content is wrong", "<span>some new content</span>", TextEditor.getContent("#t"));
        });

        core.module.use('TextEditor', moduleLoaded);
      });
    }

  });
}());