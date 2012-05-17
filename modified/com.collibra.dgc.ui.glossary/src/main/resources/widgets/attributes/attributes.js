/**
 * This is the JavaScript module for the attributes widget
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:michal@collibra.com">Michal Hans</a>
 * @module widgets/attributes/attributes
 * @name Attributes
 * @namespace widgets/attributes
 */
/*global define,require,core */
define('widgets/attributes', ['core', 'library/uielements/texteditor', 'library/uielements/widget', 'model/attribute', 'library/uielements/notifications/notification'], function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      $j = core.libs.get('jquery'),
      TextEditor = require('library/uielements/texteditor'),
      Widget = require('library/uielements/widget'),
      AttributeModel = require('model/attribute'),
      Notification = require('library/uielements/notifications/notification');

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _attributes = {
    selectors: {
      widget: '.widget[path="widgets/attributes"]',
      typesContainter: ".attributes-type-container",
      container: ".attribute-container",
      content: ".attribute-content",
      author: ".attribute-info-author",
      date: ".attribute-info-date",
      modes: {
        view: ".attribute-mode-view",
        edit: ".attribute-mode-edit",
        remove: ".attribute-mode-remove"
      },
      buttons: {
        menu: {
          button: ".attribute-menu-item",
          edit: ".attribute-menu-item.edit",
          remove: ".attribute-menu-item.delete"
        },
        removeConfirmation: {
          ok: ".attribute-remove-button-confirm",
          cancel: ".attribute-remove-button-cancel"
        }
      }
    },
    
    /**
     * @see attributes.create
     */
    _init: function() {
      var _self = this;
      
      //register loaded callback for this widget
      core.dom.each(core.dom.select(".widget[name=attributes]"), function(id){
        id = core.dom.select(id).attr("id");

        core.mediator.subscribe("widget::attributes::" + id + "::loaded", function(widget) {
          //This widget is loaded. Lets start!
          _self.run(widget);
        });
      });
      
      core.mediator.subscribe("attribute::delete", function(data) {
        if (data && data.resourceID) {
          var attr_model_object = AttributeModel.create(data.resourceID);
          attr_model_object.remove(function() {
            //Notify user.
            Notification.create({
              type: Notification.TYPE_SUCCESS,
              content: {
                message: "Attribute deleted."
              }
            });
            core.mediator.publish("attribute::deleted", [data]);
          });
        }
      });
      
      // subscribe attribute events
      core.mediator.subscribe("attribute::deleted", function(data) {
        if (data && data.resourceID) {
          _self.removedAttribute(data);
        }
      });
      
      core.mediator.subscribe("attribute::change", function(data) {
        if (data && data.resourceID) {
          var attr_model_object = AttributeModel.create(data.resourceID);
          attr_model_object.set("value", data.attributeContent).save(function() {
          //Notify user.
            Notification.create({
              type: Notification.TYPE_SUCCESS,
              content: {
                message: "Attribute updated."
              }
            });
            core.mediator.publish("attribute::changed", [data]);
          });
        }
      });
      
      core.mediator.subscribe("attribute::changed", function(data) {
        if (data && data.resourceID) {
          _self.editedAttribute(data);
        }
      });
      
      core.mediator.subscribe("attribute::added", function(data) {
        if (data && data.resourceID) {
          _self.addedAttribute(data);
        }
      });
    },
    
    //main method to begin with
    run: function(widget) {
      var _self = this;
      
      widget = core.dom.select(widget);

      // Click on menu buttons
      widget.on('click', _self.selectors.buttons.menu.edit, _self.events.onEditButtonClick);
      widget.on('click', _self.selectors.buttons.menu.remove, _self.events.onRemoveButtonClick);

      // Click on remove confirm buttons
      widget.on('click', _self.selectors.buttons.removeConfirmation.ok, _self.events.onRemoveAttributeOkButtonClick);
      widget.on('click', _self.selectors.buttons.removeConfirmation.cancel, _self.events.onRemoveAttributeCancelButtonClick);
    },
    
    events: {
      
      /**
       * MENU Event: On edit button click
       */
      onEditButtonClick: function(e) {
        var _self = _attributes,
            editModeContainer =  core.dom.select(this).parents(_self.selectors.container).find(_self.selectors.modes.edit),
            viewModeContainer =  core.dom.select(this).parents(_self.selectors.container).find(_self.selectors.modes.view);
        
        if (editModeContainer.is(":visible")) {
          _self.disableEditMode(viewModeContainer, editModeContainer);
        } else {
          _self.enableEditMode(viewModeContainer, editModeContainer);
        }
      },
      
      /**
       * MENU Event: On remove button click
       */
      onRemoveButtonClick: function() {
        var _self = _attributes,
            removeModeContainer =  core.dom.select(this).parents(_self.selectors.container).find(_self.selectors.modes.remove);
        
        _self.enableRemoveMode(removeModeContainer);
      },
      
      /**
       * EDIT Event: On edit save button click
       */
      onEditSaveButtonClick: function(data) {
        var _self = _attributes;
        
        _self.editAttribute(data);
      },
      
      
      /**
       * EDIT Event: On edit cancel button click
       */
      onEditCancelButtonClick: function(viewModeContainer, editModeContainer) {
        var _self = _attributes;
        
        _self.disableEditMode(viewModeContainer,editModeContainer);
      },
      
      /**
       * REMOVE: On remove attribute OK button click
       */
      onRemoveAttributeOkButtonClick: function() {
        var _self = _attributes,
            attributeContainer = core.dom.select(this).parents(_self.selectors.container),
            removeModeContainer =  attributeContainer.find(_self.selectors.modes.remove),
            attributeResourceId = attributeContainer.attr('data-resourceid');
        
        _self.removeAttribute({resourceID: attributeResourceId});
        _self.disableRemoveMode(removeModeContainer);
      },
      
      /**
       * REMOVE: On remove attribute Cancel button click
       */
      onRemoveAttributeCancelButtonClick: function() {
        var _self = _attributes,
            removeModeContainer =  core.dom.select(this).parents(_self.selectors.container).find(_self.selectors.modes.remove);
        
        _self.disableRemoveMode(removeModeContainer);
      }
    },

    
    /**
     * Enable edit mode
     */
    enableEditMode: function(viewModeContainer, editModeContainer) {
      var _self = this,
          currentTextEditor = editModeContainer.find('TEXTAREA'),
          resourceId = editModeContainer.parents(_self.selectors.container).attr('data-resourceid');
      
      //add TextEditor
      TextEditor.create( currentTextEditor, TextEditor.FULL, {
        save:{
          buttons: {
            position: "bottom",
            show: true,
            onSave: function(e) {
              _self.events.onEditSaveButtonClick({
                resourceID: resourceId, 
                attributeContent: TextEditor.getContent(currentTextEditor)
              });
            },
            onDiscard: function(e) {
              TextEditor.resetContent(currentTextEditor);
              _self.events.onEditCancelButtonClick(viewModeContainer, editModeContainer);
            }
          }
        },
        content: currentTextEditor.val()
      });
      
      viewModeContainer.slideUp(function() {
        editModeContainer.slideDown();
      });
    },
    
    /**
     * Disable edit mode
     */
    disableEditMode: function(viewModeContainer, editModeContainer) {
      var _self = this;
      
      editModeContainer.slideUp(function() {
        viewModeContainer.slideDown();
      });
    },
    
    /**
     * Enable remove mode
     */
    enableRemoveMode: function(removeModeContainer) {
      var _self = this;
      
      core.dom.select(removeModeContainer).show();
    },
    
    /**
     * Disable remove mode
     */
    disableRemoveMode: function(removeModeContainer) {
      var _self = this;
      
      core.dom.select(removeModeContainer).hide();
    },
    
    /**
     * Added new attribute
     */
    addedAttribute: function(attrData) {
      var _self = this;
      
      //refresh widget when new attribute is added
      core.debug.show('attribute::added');
      core.debug.show(attrData);
      
      core.dom.each(core.dom.select(_self.selectors.widget), function(w){
        Widget.refresh(w);
      });
    },
    
    /**
     * Edit attribute by its data
     */
    editAttribute: function(attrData) {
      var _self = this,
          data = false;
      
      if (attrData.resourceID) {
        data = {
          'resourceID': attrData.resourceID,
          'oldAttributeContent': attrData.oldAttributeContent,
          'attributeContent': attrData.attributeContent
        };
        
        // edit attribute
        core.mediator.publish('attribute::change', [data]);
      }
    },
    
    /**
     * Attribute edited in Model
     */
    editedAttribute: function(attrData) {
      var _self = this,
          viewModeContainer = false,
          editModeContainer = false;
      
      if (attrData.resourceID) {
        
        // attribute can be visible several times so update all at once
        core.dom.each(core.dom.select('.attribute-' + attrData.resourceID), function(attr) {
          editModeContainer =  attr.find(_self.selectors.modes.edit),
          viewModeContainer =  attr.find(_self.selectors.modes.view);
          
          // disable edit mode
          _self.disableEditMode(viewModeContainer, editModeContainer);
          // refresh content, author and date
          viewModeContainer.find(_self.selectors.content).html(attrData.attributeContent);
          viewModeContainer.find(_self.selectors.author).html(attrData.attributeAuthor);
          viewModeContainer.find(_self.selectors.date).html(attrData.attributeDate);
          TextEditor.setContent(editModeContainer.find('TEXTAREA'), attrData.attributeContent);
        });
      }
    },
    
    /**
     * Remove attribute by data
     */
    removeAttribute: function(attrData) {
      var _self = this,
          data = false;
      
      if (attrData.resourceID) {
        data = {
          resourceID: attrData.resourceID
        };
        
        // remove attribute
        core.mediator.publish('attribute::delete', [data]);
      }
    },
    
    /**
     * Remove attribute HTML by its data
     */
    removedAttribute: function(attrData) {
      var _self = this;
      
      if (attrData.resourceID) {
        // attribute can be visible several times so update all at once
        core.dom.each(core.dom.select('.attribute-' + attrData.resourceID), function(attr){
          attr.fadeOut(function(){
            var currentAttribute = core.dom.select(this),
                currentAttributeWrapper = currentAttribute.parent(),
                currentAttributeTypeContainer = currentAttributeWrapper.parent();
            
            // remove attribute DOM object
            core.dom.select(this).remove();
            
            // remove whole group (title) if last element removed
            if(!currentAttributeWrapper.children().length) {
              currentAttributeTypeContainer.fadeOut(function() {
                core.dom.select(this).remove();
              });
            }
            
          });
        });
        
        return true;
      } else {
        return false;
      }
    }
  };
  
  //Initialize
  _attributes._init();

  /*************************************
   *               Public              *
   *************************************/

  var Attributes = /** @lends Attributes */{
    run: function() {
      _attributes._init();
    }
  };

  return Attributes;
});

core.module.alias.add('Attributes', 'widgets/attributes');
core.module.use('Attributes');
