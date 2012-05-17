(function() {
  "use strict";

  var html = '<div class="term" id="titlebar">'
    + '<div class="titlebar-view">'
    + '<div class="resource-info">'
    + '<div class="resource-title">Driver</div>'
    + '<div title="Edit URL, type and status." class="resource-configuration tooltip"><i title="Edit URL, type and status." class="icon-cogwheel lightGrey tooltip"></i></div>'
    + '<div class="resource-type">Type: <a href="/com.collibra.dgc.war/term/7a258344-efbe-46e7-99ea-6a04e0342a85">Business Term</a></div>'
    + '<div class="resource-status">Status: <a href="/com.collibra.dgc.war/term/3d27aa52-fb6f-41d2-b06f-20c99d0ba71e">Candidate</a></div>'
    + '</div>'
    + '</div>'
    + '<div style="display:none" class="titlebar-settings">'
    + '<div class="titlebar-settings-field titlebar-uri">'
    + '<span class="label">URI:</span>'
    + '<span class="field">'
    + '<input type="text" value="10002" name="titlebar-uri" id="titlebar-uri">'
    + '</span>'
    + '</div>'
    + '<div class="titlebar-settings-field titlebar-type">'
    + '<span class="label">Type:</span>'
    + '<span class="field">'
    + '<select name="titlebar-type" id="titlebar-type"></select>'
    + '</span>'
    + '</div>'
    + '<div class="titlebar-settings-field titlebar-status">'
    + '<span class="label">Status:</span>'
    + '<span class="field">'
    + '<select name="titlebar-status" id="titlebar-status">'
    + '<option value="32aeaedd-8154-4d23-ac8b-df1f5182e1ab">Accepted</option>'
    + '<option value="d347d4c5-3aa7-4044-8d2b-8fa940d23fc8">Approval Pending</option>'
    + '<option selected="selected" value="3d27aa52-fb6f-41d2-b06f-20c99d0ba71e">Candidate</option>'
    + '<option value="091c7014-26b5-41f0-b8df-849db313a0c3">In Progress</option>'
    + '<option value="54b9a416-d5cb-4091-9d75-2b97d904befe">Obsolete</option>'
    + '<option value="3df4026a-8056-4177-b806-50722fce1899">Rejected</option>'
    + '<option value="e2840906-a5fa-49ff-baa1-63661e62f01d">Reviewed</option>'
    + '<option value="693e8c90-4d32-4753-a63e-d94d3d086429">Standard</option>'
    + '<option value="f1461d34-c26d-408c-b820-a4ecba83845d">Under Review</option>'
    + '</select>'
    + '</span>'
    + '</div>'
    + '<input type="hidden" value="1eeb6601-4841-4b9d-90e0-709993763b15" name="titlebar-id" id="titlebar-id" disabled="disabled">'
    + '<div class="titlebar-settings-field titlebar-savebuttons">'
    + '<div id="titlebar-settings-save" class="btn-success"><i class="icon-save"></i>Save</div>'
    + '<div id="titlebar-settings-cancel" class="btn-danger"><i class="icon-cancel"></i>Cancel</div>'
    + '</div>'
    + '</div>'
    + '</div>';

  new AsyncTestCase("modules :: content :: titlebar.js", {
    //////////
    ////////// showSettings
    //////////
    testShowSettings: function(queue) {
      queue.call(function(callbacks) {
        core.dom.create(html).appendTo("BODY");
        
        var moduleLoaded = callbacks.add(function(TitleBar) {
          
          assertFunction("showSettings is not a function", TitleBar.showSettings);
        
          //check for overall creation
          assertTrue("TitleBar is not created.", core.dom.is(core.dom.select('#titlebar')));
          assertTrue("TitleBar settings is not created.", core.dom.is(core.dom.select('.titlebar-settings')));
          
          //check are settings hidden?
          assertTrue("TitleBar settings are not hidden.", core.dom.is(core.dom.select('.titlebar-settings:hidden')));
          
          //show settings
          TitleBar.showSettings();
          
          //check are settings visible?
          assertTrue("TitleBar settings are not visible.", core.dom.is(core.dom.select('.titlebar-settings:visible')));
        });
        
        core.module.use('TitleBar', moduleLoaded);
      });
    },
    
    
    //////////
    ////////// hideSettings
    //////////
    testHideSettings: function(queue) {
      queue.call(function(callbacks) {
        core.dom.create(html).appendTo("BODY");

        var moduleLoaded = callbacks.add(function(TitleBar) {
          assertFunction("showSettings is not a function", TitleBar.showSettings);
          assertFunction("hideSettings is not a function", TitleBar.hideSettings);
        
          //check for overall creation
          assertTrue("TitleBar is not created.", core.dom.is(core.dom.select('#titlebar')));
          assertTrue("TitleBar settings is not created.", core.dom.is(core.dom.select('.titlebar-settings')));
          
          //show settings manually
          core.dom.select('.titlebar-settings').show();
          
          //check are settings visible?
          assertTrue("TitleBar settings are not visible.", core.dom.is(core.dom.select('.titlebar-settings:visible')));
        });
        
        core.module.use('TitleBar', moduleLoaded);
      });
      
      queue.call(function(callbacks) {
        var checkHidden = callbacks.add(function() {
          //check are settings visible?
          assertFalse("TitleBar settings are not hidden.", core.dom.is(core.dom.select('.titlebar-settings:visible')));
        });
        
        var afterHide = callbacks.add(function(TitleBar) {
          TitleBar.hideSettings(function(){
            checkHidden();
          });
        });

        core.module.use('TitleBar', afterHide);
      });

    }
    
  });
}());