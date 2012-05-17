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
  var tree = "<div id='t1'></div>",
      t = "#t1",
      $ = core.libs.get('jquery');

  new AsyncTestCase('uielements :: Tree', {

    //////////
    ////////// blur
    //////////
    testBlur: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var blur = Tree.blur;
          
          assertFunction('blur of the module Tree is not a function!', blur);
          core.dom.create(tree).appendTo("BODY");
          assertTrue('Tree wasnt correctly initilized.', Tree.create(t, {
            content: {
              source: Tree.SOURCE_HTML,
              html: '<ul><li id="phtml_1"><a href="#">Root node 1</a><ul><li id="phtml_2"><a href="#">Child node 1</a></li><li id="phtml_3"><a href="#">Child node 2</a></li></ul></li><li id="phtml_4"><a href="#">Root node 2</a></li></ul>'
            }
          }));
          
          assertTrue('Tree blur failed.', Tree.focus(t));
          assertTrue('Tree isnt focused for bluring.', Tree.isFocused(t));
          assertTrue('Tree blur failed.', blur(t));
          assertFalse('Tree is still focused after blue.', Tree.isFocused(t));
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// collapse
    //////////
    testCollapse: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var collapse = Tree.collapse,
              all_nodes = null,
              all_closed = true;
          
          all_nodes = Tree.node.get(t, 'all');
          assertFunction('collapse of the Tree module is not a function!', collapse);
          core.dom.create(tree).appendTo("BODY");
          
//          Tree.node.on(t, Tree.EVENT_AFTER_OPEN_NODE, function(){
//            
//          });
          
          assertTrue('Tree wasnt correctly initilized.', Tree.create(t, {
            nodes: {
              open: ["#phtml_1"]
            },
            content: {
              source: Tree.SOURCE_HTML,
              html: '<ul><li id="phtml_1"><a href="#">Root node 1</a><ul><li id="phtml_2"><a href="#">Child node 1</a></li><li id="phtml_3"><a href="#">Child node 2</a></li></ul></li><li id="phtml_4"><a href="#">Root node 2</a></li></ul>'
            }
          },
          function(){
          //test all nodes for opened/closed
            core.dom.each(all_nodes, function(k){
                if (!Tree.node.isLeaf(t, k) && Tree.node.isOpen(t, k)) {
                  all_closed = false;
                }
            });
            
            assertTrue("Everything is already closed. Collapse doesnt matter.", all_closed);
            
            collapse(t);
            
            //test all nodes for opened/closed
            core.dom.each(all_nodes, function(k){
                if (!Tree.node.isLeaf(t, k) && Tree.node.isOpen(t, k)) {
                  all_closed = false;
                }
            })
            
            assertTrue("Not Everything is closed. Collapse failed.", all_closed);
          }
          ));
          assertTrue('Tree create failed.', Tree.is(t));
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// create
    //////////
    testCreate: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var create = Tree.create,
              callbackOnSuccess = sinon.spy(),
              callbackOnError = sinon.spy();

          assertFunction('create of the Tree module is not a function!', create);
          core.dom.create(tree).appendTo("BODY");
          assertFalse('Tree is created failed.', Tree.is(t));
          assertTrue('Tree wasnt correctly initilized.', Tree.create(t, {
            content: {
              source: Tree.SOURCE_HTML,
              html: '<ul><li id="phtml_1"><a href="#">Root node 1</a><ul><li id="phtml_2"><a href="#">Child node 1</a></li><li id="phtml_3"><a href="#">Child node 2</a></li></ul></li><li id="phtml_4"><a href="#">Root node 2</a></li></ul>'
            }
          }));
          assertTrue('Tree is created failed.', Tree.is(t));
          //assertTrue('Tree wasnt correctly created(XHR onSuccess not fired)', callbackOnSuccess.calledOnce);
          //assertFalse('Tree wasnt correctly created(XHR onError fired)', callbackOnError.calledOnce);
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// expand
    //////////
    testExpand: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var expand = Tree.expand,
              all_nodes = null,
              all_open = false;
          
          assertFunction('expand of the Tree module is not a function!', expand);
          core.dom.create(tree).appendTo("BODY");
//          assertTrue('Tree wasnt correctly initilized.', Tree.create(t, {
//            content: {
//              source: Tree.SOURCE_HTML,
//              html: '<ul><li id="phtml_1"><a href="#">Root node 1</a><ul><li id="phtml_2"><a href="#">Child node 1</a></li><li id="phtml_3"><a href="#">Child node 2</a></li></ul></li><li id="phtml_4"><a href="#">Root node 2</a></li></ul>'
//            }
//          }));
//          assertTrue('Tree is created failed.', Tree.is(t));
//          
//          all_nodes = Tree.node.get(t, 'all');
//          core.dom.each(all_nodes, function(k){
//              if (Tree.node.isLeaf(t, k) || Tree.node.isOpen(t, k)) {
//                all_open = true;
//              }
//          })
//          
//          assertFalse("Everything is already open. Expand doesnt matter.", all_open);
//          
//          all_nodes = Tree.node.get(t, 'all');
//          core.dom.each(all_nodes, function(k){
//              if (Tree.node.isLeaf(t, k) || Tree.node.isOpen(t, k)) {
//                all_open = true;
//              }
//          })
//          
//          assertTrue("Not Everything is open. Expand failed.", all_open);
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// focus
    //////////
    testFocus: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var focus = Tree.focus;
          
          assertFunction('focus of the Tree module is not a function!', focus);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// getRollBackData
    //////////
    testGetRollBackData: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var getRollBackData = Tree.getRollBackData;
          
          assertFunction('getRollbackData of the Tree module is not a function!', getRollBackData);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// getSelected
    //////////
    testGetSelected: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var getSelected = Tree.getSelected;
          
          assertFunction('getSelected of the Tree module is not a function!', getSelected);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// is
    //////////
    testIs: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var is = Tree.is;
          
          assertFunction('is of the Tree module is not a function!', is);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// isFocused
    //////////
    testIsFocused: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var isFocused = Tree.isFocused;
          
          assertFunction('isFocused of the Tree module is not a function!', isFocused);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// isLocked
    //////////
    testIsLocked: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var isLocked = Tree.isLocked;
          
          assertFunction('isLocked of the Tree module is not a function!', isLocked);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// lock
    //////////
    testLock: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var lock = Tree.lock;
          
          assertFunction('lock of the Tree module is not a function!', lock);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// refresh
    //////////
    testRefresh: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var refresh = Tree.refresh;
          
          assertFunction('refresh of the Tree module is not a function!', refresh);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// remove
    //////////
    testRemove: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var remove = Tree.remove;
          
          assertFunction('remove of the Tree module is not a function!', remove);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// rollback
    //////////
    testRollBack: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var rollBack = Tree.rollBack;
          
          assertFunction('rollback of the Tree module is not a function!', rollBack);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// search
    //////////
    testSearch: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var search = Tree.search;
          
          assertFunction('search of the Tree module is not a function!', search);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// unlock
    //////////
    testUnlock: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var unlock = Tree.unlock;
          
          assertFunction('unlock of the Tree module is not a function!', unlock);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.add
    //////////
    testNodeAdd: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var add = Tree.node.add;
          
          assertFunction('node.add of the Tree module is not a function!', add);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.close
    //////////
    testNodeClose: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var close = Tree.node.close;
          
          assertFunction('node.close of the Tree module is not a function!', close);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.copy
    //////////
    testNodeCopy: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var copy = Tree.node.copy;
          
          assertFunction('node.copy of the Tree module is not a function!', copy);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.cut
    //////////
    testNodeCut: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var cut = Tree.node.cut;
          
          assertFunction('node.cut of the Tree module is not a function!', cut);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.get
    //////////
    testNodeGet: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var get = Tree.node.get;
          
          assertFunction('node.get of the Tree module is not a function!', get);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.isLeaf
    //////////
    testNodeIsLeaf: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var isLeaf = Tree.node.isLeaf;
          
          assertFunction('node.isLeaf of the Tree module is not a function!', isLeaf);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.isOpen
    //////////
    testNodeIsOpen: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var isOpen = Tree.node.isOpen;
          
          assertFunction('node.isOpen of the Tree module is not a function!', isOpen);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.move
    //////////
    testNodeMove: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var move = Tree.node.move;
          
          assertFunction('node.move of the Tree module is not a function!', move);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.name
    //////////
    testNodeName: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var name = Tree.node.name;
          
          assertFunction('node.name of the Tree module is not a function!', name);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.off
    //////////
    testNodeOff: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var off = Tree.node.off;
          
          assertFunction('node.off of the Tree module is not a function!', off);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.on
    //////////
    testNodeOn: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var on = Tree.node.on;
          
          assertFunction('node.on of the Tree module is not a function!', on);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.open
    //////////
    testNodeOpen: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var open = Tree.node.open;
          
          assertFunction('node.open of the Tree module is not a function!', open);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.paste
    //////////
    testNodePaste: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var paste = Tree.node.paste;
          
          assertFunction('node.paste of the Tree module is not a function!', paste);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.remove
    //////////
    testNodeRemove: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var remove = Tree.node.remove;
          
          assertFunction('node.remove of the Tree module is not a function!', remove);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.renameMode
    //////////
    testNodeRenameMode: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var renameMode = Tree.node.renameMode;
          
          assertFunction('node.renameMode of the Tree module is not a function!', renameMode);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.select
    //////////
    testNodeSelect: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var select = Tree.node.select;
          
          assertFunction('node.select of the Tree module is not a function!', select);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    },
    
    //////////
    ////////// node.sort
    //////////
    testNodeSort: function(queue) {
      queue.call(function(callbacks) {
        var moduleLoaded = callbacks.add(function(Tree) {
          var sort = Tree.node.sort;
          
          assertFunction('node.sort of the Tree module is not a function!', sort);
          
          
        });
        
        core.module.use('Tree', moduleLoaded);
      });
    }
  });
}());