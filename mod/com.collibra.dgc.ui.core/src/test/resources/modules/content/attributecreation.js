(function() {
  "use strict";
  
  var tabbarElement = '<div id="tabbarWrap">' +
                        '<div id="tabbar">' +
                          '<ul class="tabmenu">' +
                            '<li class="overviewclass active" id="overviewtab" data-key="overview" event="overview">' +
                              '<div class="tabsettings">' +
                                '<i class="icon-cogwheel lightgrey"></i>' +
                              '</div>' +
                              '<div module="../tabs/overview" class="label">overview</div>' +
                            '</li>' +
                            '<li id="attributes" data-key="attributes" event="attributes">' +
                              '<div class="tabsettings">' +
                                '<i class="icon-cogwheel lightgrey"></i>' +
                              '</div>' +
                              '<div module="../tabs/attributes" class="label">attributes</div>' +
                              '<div event="attributes::add" class="action addattribute ui-menu-initiator" data-key="add" style="display: none;" menuid="ui-menu-13342140755459">' +
                                '<i class="icon-plus"></i>' +
                                '<div module="#">add</div>' +
                              '</div>' +
                            '</li>' +
                            '<li event="more" class="moremenu">' +
                              '<i class="icon-list"></i>' +
                              '<div module="more" class="label">More</div>' +
                            '</li>' +
                          '</ul>' +
                          '<div class="active-tabmenu-item-wrapper" style="top: 10px;">' +
                            '<div class="active-tabmenu-item-bg" style="height: 33px;"></div>' +
                          '</div>' +
                        '</div>' +
                      '</div>',
      creationFormsElement = '<div id="attributecreation" style="display: block;">' +
                               '<div class="attributecreation-wrapper">' +
                                 '<div class="attributecreation-container">' +
                                   '<div class="attributecreation-content attributecreation-type-date" id="attributeCreationA" style="display:none;">' +
                                      '<h3 class="attributecreation-header">New Date Attribute</h3>' +
                                      '<div class="attributecreation-form">' +
                                        '<div class="field"><input type="text" placeholder="Date" class="textinput icon" /></div>' +
                                      '</div>' +
                                      '<div class="buttons">' +
                                        '<span class="btn-success attributecreation-button-save"><i class="icon-save"></i> Save</span>' +
                                        '<span class="btn-danger attributecreation-button-cancel"><i class="icon-delete white"></i> Cancel</span>' +
                                      '</div>' +
                                      '<div class="clearfloats"></div>' +
                                   '</div>' +
                                 '</div>' +
                               '</div>' +
                             '</div>';

  AsyncTestCase("modules :: content :: attributecreation :: attributecreation.js", {

    //////////
    ////////// addAddMenu
    //////////
    testAddAddMenu: function(attributecreationQueue) {
      attributecreationQueue.call(function(callbacks) {
        var attributecreationLoaded = callbacks.add(function(AttributeCreation) {
          var addAddMenu = AttributeCreation.addAddMenu;

          assertFunction("addAddMenu() is not a function.", addAddMenu);

          //Create and insert creation forms.
          core.dom.create(tabbarElement).appendTo("BODY");
          core.dom.create(creationFormsElement).appendTo("BODY");

          //Check if there is no menu on the page.
          assertFalse('Menu is already created.', core.dom.is(core.dom.select('.ui-menu')));

          addAddMenu('attributes::add', [
            {
              label: 'Date',
              type: AttributeCreation.CONTENT_TYPE_DATE
            }
          ]);
          core.dom.select('.addattribute').click();
          

          assertTrue('Menu has not been created.', core.dom.is(core.dom.select('.ui-menu')));
          assertTrue('Menu has not been created.', core.dom.select('.ui-menu').is(':visible'));
        });

        core.module.use('AttributeCreation', attributecreationLoaded);
      });
    },

    //////////
    ////////// create
    //////////
    testCreate: function(attributecreationQueue) {
      attributecreationQueue.call(function(callbacks) {
        var attributecreationLoaded = callbacks.add(function(AttributeCreation) {
          var create = AttributeCreation.create;

          assertFunction("create() is not a function.", create);

          //Create and insert creation forms.
          core.dom.create(tabbarElement).appendTo("BODY");
          core.dom.create(creationFormsElement).appendTo("BODY");

          //Check if there is no menu on the page.
          assertFalse('Menu is already created.', core.dom.is(core.dom.select('.ui-menu')));

          assertTrue('False from create().', create({
            addMenus: [
              {
                eventID: 'attributes::add',
                menuItems: [
                  {
                    label: 'Date',
                    type: AttributeCreation.CONTENT_TYPE_DATE
                  }
                ]
              }
            ]
          }));
          core.dom.select('.addattribute').click();

          assertTrue('Menu has not been created.', core.dom.is(core.dom.select('.ui-menu')));
          assertTrue('Menu has not been created.', core.dom.select('.ui-menu').is(':visible'));
        });

        core.module.use('AttributeCreation', attributecreationLoaded);
      });
    },

    //////////
    ////////// destroy
    //////////
    testDestroy: function(attributecreationQueue) {
      attributecreationQueue.call(function(callbacks) {
        var attributecreationLoaded = callbacks.add(function(AttributeCreation) {
          var destroy = AttributeCreation.destroy,
              creationForm;

          assertFunction("destroy() is not a function.", destroy);

          //Create and insert creation forms.
          core.dom.create(tabbarElement).appendTo("BODY");
          core.dom.create(creationFormsElement).appendTo("BODY");

          creationForm = core.dom.select('#attributeCreationA');

          assertTrue('There is no creation form to destroy.', core.dom.is(creationForm));

          //Check if there is no menu on the page.
          assertFalse('Menu is already created.', core.dom.is(core.dom.select('.ui-menu')));
          AttributeCreation.addAddMenu('attributes::add', [
            {
              label: 'Date',
              type: AttributeCreation.CONTENT_TYPE_DATE
            }
          ]);
          core.dom.select('.addattribute').click();
          assertTrue('Menu has not been created.', core.dom.is(core.dom.select('.ui-menu')));

          assertFalse("destroy() didn't return false when false was passed as an argument.", destroy(false));
          assertTrue("destroy() didn't return true.", destroy());
          assertFalse('Creation form was not destroyed.', core.dom.is(core.dom.select('#attributecreation')));
          assertFalse('Menu was not destroyed.', core.dom.is(core.dom.select('.ui-menu')));
        });

        core.module.use('AttributeCreation', attributecreationLoaded);
      });
    },

    //////////
    ////////// openCreationForm
    //////////
    testOpenCreationForm: function(attributecreationQueue) {
      attributecreationQueue.call(function(callbacks) {
        var checkCallback = callbacks.add(function() {
          assertTrue("Callback wasn't fired.", core.dom.is(core.dom.select("#xyz")));
        });

        var attributecreationLoaded = callbacks.add(function(AttributeCreation) {
          var openCreationForm = AttributeCreation.openCreationForm,
              creationForm;

          assertFunction("openCreationForm() is not a function.", openCreationForm);

          //Create and insert creation forms.
          core.dom.create(creationFormsElement).appendTo("BODY");

          creationForm = core.dom.select('#attributeCreationA');

          assertFalse("Creation form is visible.", creationForm.is(':visible'));
          assertTrue("openCreationForm() didn't return true.", openCreationForm({
            type: AttributeCreation.CONTENT_TYPE_DATE,
            callback: function() {
              core.dom.create('<div id="xyz"></div>').appendTo("BODY");
              checkCallback();
            }
          }));
          assertTrue("Creation form is not visible.", creationForm.is(':visible'));
        });

        core.module.use('AttributeCreation', attributecreationLoaded);
      });
    },

    //////////
    ////////// removeAddMenu
    //////////
    testRemoveAddMenu: function(attributecreationQueue) {
      attributecreationQueue.call(function(callbacks) {
        var attributecreationLoaded = callbacks.add(function(AttributeCreation) {
          var removeAddMenu = AttributeCreation.removeAddMenu;
          
          assertFunction("removeAddMenu() is not a function.", removeAddMenu);
          
          //Create and insert creation forms.
          core.dom.create(tabbarElement).appendTo("BODY");
          core.dom.create(creationFormsElement).appendTo("BODY");

          //Check if there is no menu on the page.
          assertFalse('Menu is already created.', core.dom.is(core.dom.select('.ui-menu')));

          AttributeCreation.addAddMenu('attributes::add', [
            {
              label: 'Date',
              type: AttributeCreation.CONTENT_TYPE_DATE
            }
          ]);
          core.dom.select('.addattribute').click();

          assertTrue('Menu has not been created.', core.dom.is(core.dom.select('.ui-menu')));
          assertTrue('Menu has not been created.', core.dom.select('.ui-menu').is(':visible'));

          assertFalse("removeAddMenu() with false id didn't return false.", removeAddMenu('abcd'));
          assertTrue("removeAddMenu() didn't return true.", removeAddMenu('attributes::add'));
          assertFalse('Menu has not been removed.', core.dom.is(core.dom.select('.ui-menu')));
        });

        core.module.use('AttributeCreation', attributecreationLoaded);
      });
    }
  });
}());
