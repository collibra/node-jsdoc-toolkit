/**
 * This is the JavaScript module for modal window
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:michal@collibra.com">Michal Hans</a>
 * @module library/uielements/modal
 * @alias ModalWindow
 * @namespace controls
 */
/*global define,require,core */
define('library/uielements/modal',
       ['core'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core');

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var _modal = {
    defaultOptions: {
      modal: true,  // set true to show modal
      fullOverlay: true,  // set true to fullscreen modal
      resizable: false,
      draggable: false,
      autoOpen: false,
      dialogClass: 'modalwindow',
      minLoadingTime: 1000  // minimum loading animation time [ms]
    },
    
    /**
     * @see modal.create
     */
    create: function(initiator, options) {
      var _self = this,
          modalSkeleton = false;
      
      //generate html code with content
      modalSkeleton = _self.generateModalSkeleton(options);
      //style modal window
      _self.generateModalWindow(modalSkeleton, options);
      
      // Link with modal window and add click event to initiator.
      initiator.attr('modalId', modalSkeleton.attr("id")).addClass('modalInitiator').unbind('click').bind('click', function() {
        _self.onInitiatorClick(core.dom.select(this));
      });
      
      return core.dom.is(core.dom.select('#' + initiator.attr('modalId')));
    },
    
    /**
     * @see modal.destroy
     */
    destroy: function(initiator) {
      var _self = this;
      
      if (_self.has(initiator)) {
        core.dom.select('#' + initiator.attr("modalid")).remove();
        initiator.removeClass('modalInitiator').removeAttr('modalid');
        return true;
      } else {
        return false;
      }
    },
    
    /**
     * @see modal.has
     */
    has: function(initiator) {
      return (initiator.hasClass("modalInitiator") && initiator.attr("modalid")) ? true : false;
    },
    
    /**
     * @see modal.hide
     */
    hide: function(initiator) {
      var _self = this;
      
      if (_self.has(initiator)) {
        return _self.closeModal('#' + initiator.attr("modalid"));
      } else {
        return false;
      }
    },
    
    /**
     * @see modal.show
     */
    show: function(initiator) {
      var _self = this;
      
      if (_self.has(initiator)) {
        return _self.openModal('#' + initiator.attr("modalid"));
      } else {
        return false;
      }
    },
    
    /**
     * Generate html skeleton for modal window
     *
     * @param {Object} options
     * @returns {CoreObj} Created object
     */
    generateModalSkeleton: function(options) {
      var _self = this,
          initElement = false,
          modalId = false,
          modalSkeleton = false,
          contentElement = false;
      
      modalSkeleton = core.dom.create('<div>', 'BODY');
      modalId = core.dom.generateID(modalSkeleton, {
        prefix: 'modal-',
        changeDOM: true
      });
      
      if (options.title) {
        modalSkeleton.attr('title', options.title);
      }
      
      if (options.subtitle) {
        modalSkeleton.attr('subtitle', options.subtitle);
      }
      
      if (options.description) {
        modalSkeleton.attr('description', options.description);
      }
      
      if (options.content ) {
        
        if (options.content.element){
          contentElement = core.dom.select(options.content.element);
          if (contentElement) {
            modalSkeleton.html(contentElement.html());
          }
        } else if (options.content.url) {
          modalSkeleton.html('');
        } else if (core.string.is(options.content)){
          modalSkeleton.html(options.content);
        }
      }
      
      modalSkeleton.hide();

      return modalSkeleton;
    },
    
    /**
     * Generate html skeleton for modal window
     *
     * @param modalSkeleton {CoreObj}
     * @param {Object} options
     * @returns {CoreObj} Modal Window
     */
    generateModalWindow: function(modalSkeleton, options) {
      var _self = this,
          dialogOptions = {};
      
      if(!options) {
        options = _self.defaultOptions;
      }

      //set up modal dialog options
      dialogOptions.modal = (_self.defaultOptions.modal && options.fullOverlay) ? true : false;
      dialogOptions.resizable = _self.defaultOptions.resizable;
      dialogOptions.draggable = _self.defaultOptions.draggable;
      dialogOptions.autoOpen = _self.defaultOptions.autoOpen;
      dialogOptions.dialogClass = _self.defaultOptions.dialogClass;
      dialogOptions.title = _self.createHeader(options.title, options.subtitle, options.description);
      dialogOptions.width = options.dimensions.width;
      dialogOptions.height = options.dimensions.height;
      dialogOptions.closeText = '';
      dialogOptions.close = options.hide.onClose;
      dialogOptions.onLoad = options.content.onLoad;
      dialogOptions.show = 'fade';
      dialogOptions.hide = 'fade';
      
      //store ajax content url in attribute
      if (options.content.url) {
        modalSkeleton.attr('contenturl', options.content.url);
        
        //load content by ajax
        modalSkeleton.bind( "dialogopen", function(event, ui) {
          var container = false,
              url = false;
          
          container = core.dom.select(event.currentTarget);
          url = container.attr('contenturl');
          _modal.loadContent(url, container, dialogOptions.onLoad);
        });
      }
      
      //create dialog
      modalSkeleton.dialog(dialogOptions);
      
      //disable close button
      if (!options.hide.enabled) {
        modalSkeleton.parent().find('.ui-dialog-titlebar-close').remove();
      }
      
      //remove overlay (inside) on close
      if (_self.defaultOptions.modal && !options.fullOverlay) {
        modalSkeleton.addClass('partialOverlay');
        
        modalSkeleton.bind( "dialogbeforeclose", function(event, ui) {
          core.dom.select(".modal-overlay").remove();
        });
      }
      
    },
    
    
    /**
     * Creates html code for header
     *
     * @param {String} title
     * @param {String} subtitle
     * @param {String} description
     */
    createHeader: function(title, subtitle, description){
      var _self = this,
          output = '';
      
      if (core.string.is(title)) {
        output = '<span class="maintitle">' + title + '</span>';
      }
      if ( (core.string.is(subtitle) && subtitle.length)  || (core.string.is(description) && subtitle.length)){
        output = output + '<span class="subsection">';
        if (subtitle) {
          output = output + '<span class="subtitle">' + subtitle + '</span>';
        }
        if (description) {
          output = output + '<span class="description">' + description + '</span>';
        }
        output = output + '</span>';
      } else {
        output = output + '<span class="subsection empty"></span>';
      }
      
      return output;
      
    },

    /**
     * Action after click on initiator
     *
     * @param {CoreObj} initiator
     */
    onInitiatorClick: function(initiator) {
      var _self = this,
          modalSelector = false;
      
      if (initiator.hasClass('modalInitiator')) {
        modalSelector = '#' + initiator.attr('modalid');
        return _self.openModal(modalSelector);
      } else {
        return false;
      }
    },
    
    /**
     * Load content by ajax
     *
     * @param {String} url
     * @param {CoreObj} modalWindowSelector
     * @parem {Function} [callback]
     */
    loadContent: function(url, modalWindowSelector, callback) {
      var _self = this,
          loader = false,
          date = new Date();
      modalWindowSelector.html('<div class="loader"></div>');
      loader = modalWindowSelector.find(".loader");
      loader.data('minLoadingTime', date.getTime() + _self.defaultOptions.minLoadingTime);

      core.AJAX.call(url, false, {
        onSuccess: function(data){
          var currentDate = new Date(),
              waitTime = 0;

          if (loader.data('minLoadingTime') > currentDate.getTime()) {
            waitTime = loader.data('minLoadingTime') - currentDate.getTime();
          }

          setTimeout(function(){
            loader.fadeOut(function(){
              modalWindowSelector.html('<div class="modal-content">'+ data + '</div>');
              modalWindowSelector.find('.modal-content').fadeIn();
            });
            
            // user callback
            if (core.func.is(callback)) {
              callback();
            }
          }, waitTime);
        },
        dataType: 'text'
      });
      
    },
    

    /**
     * Open modal window using selector that points on modal window
     *
     * @param {CoreObj} modalWindowSelector
     */
    openModal: function(modalWindowSelector) {
      var _self = this,
          modalWindow = false;

      modalWindow = core.dom.select(modalWindowSelector);

      if (modalWindow.dialog) {
        modalWindow.dialog("open");
        //add overlay (inside)
        if (!core.dom.select(".modal-overlay").length && modalWindow.hasClass('partialOverlay')) {
          core.dom.select("#contentcolumn").append('<div class="modal-overlay">');
        }
        return modalWindow.dialog("isOpen");
      } else {
        return false;
      }
    },

    /**
     * Close modal window using selector that points on modal window
     *
     * @param {CoreObj} modalWindowSelector
     */
    closeModal: function(modalWindowSelector) {
      var _self = this,
          modalWindow = false;

      modalWindow = core.dom.select(modalWindowSelector);
      
      if (modalWindow.dialog) {
        modalWindow.dialog("close");
      }
      
      return !modalWindow.dialog("isOpen");
    }

  };

  /*************************************
   *               Public              *
   *************************************/

  var Modal = /** @lends ModalWindow */{
    /**
     * Create a modal window.
     * @param  {CoreObj/Selector} initiator The element that shows/hides the modal window.
     * @param  {Object} [options] Options for the modal window.
     *   @option {Boolean} hide.enabled=true Show the close button.
     *   @option {Function} hide.onClose Function that is executed right before the window hides. When false is returned, it stays open.
     *   @option {CoreObj/Selector} hide.initiator An alternate initiator that hides the window.
     *   @option {CoreObj/Selector} show.initiator An alternate initiator that shows the windows.
     *   @option {Function} show.onShow Function that is executed right after the window is shown.
     *   @option {String} title="" The title of the modal window.
     *   @option {String} subtitle="" The subtitle of the modal windows.
     *   @option {String} description="" The description shown right underneath the subtitle.
     *   @option {CoreObj/Selector} content.element='' A CoreObj/Selector pointing to the element containing the content. This element will be extracted and put in the window.
     *   @option {String} content.url='' A URL to retreive the content from.
     *   @option {Function} content.onLoad Callback after loading content from URL
     *   @option {Number} dimensions.width=400 Custom width for the window.
     *   @option {Number} dimensions.height=300 Custom height for the window.
     *   @option {Boolean} fullOverlay=true Do you want the overlay to cover the full page or the content only?
     * @return {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    create: function(initiator, options) {
      return _modal.create(initiator, options);
    }.defaults('', {
      hide: {
        enabled: true,
        onClose: function() { return true; }
      },
      title: '',
      subtitle: '',
      description: '',
      content: {
        element: '',
        url: '',
        onLoad: function() { return true; }
      },
      fullOverlay: true,
      dimensions: {
        width: 400,
        height: 300
      }
    }),

    /**
     * Destroys a modal window.
     * @param  {CoreObj/Selector} initiator The element that shows/hides the modal window.
     * @return {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    destroy: function(initiator) {
      return _modal.destroy(initiator);
    }.defaults(''),

    /**
     * Does this initiator have a modal window?
     * @param  {CoreObj/Selector} initiator The element that shows/hides the modal window.
     * @return {Boolean} Whether the initiator has a modal window attached.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    has: function(initiator) {
      return _modal.has(initiator);
    }.defaults(''),

    /**
     * Hide the modal window.
     * @param  {CoreObj/Selector} initiator The element that shows/hides the modal window.
     * @return {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    hide: function(initiator) {
      return _modal.hide(initiator);
    }.defaults(''),

    /**
     * Show the modal window.
     * @param  {CoreObj/Selector} initiator The element that shows/hides the modal window.
     * @return {Boolean} True on success and false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    show: function(initiator) {
      return _modal.show(initiator);
    }.defaults('')
  };

  return Modal;
});

core.module.alias.add('ModalWindow', 'library/uielements/modal');