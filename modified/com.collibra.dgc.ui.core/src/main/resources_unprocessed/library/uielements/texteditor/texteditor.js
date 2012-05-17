/**
 * WYSIWYG Text Editor.
 *
 * Copyright Collibra 2012.
 * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
 * @module library/uielements/texteditor
 * @alias TextEditor
 * @namespace controls
 */
/*global define,require,core */
define('library/uielements/texteditor',
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
  var _texteditor = {
     emptyContentRegex: /^<([\w]+)[^>]*>(<([\w]+)><\/([\w]+)>)?((<([\w]+)\/?>)|&nbsp;)?<\/\1>\W?$|^$|^(<([\w]+)\/?>\W?)$/,
     i18n: {},
     //global wysiwyg object needed for some controls
     wysiwygGlobalObject: null,

     //preset configuration types
     type: {
       FULL: {
        "bold":                 {group: 0},
        "italic":               {group: 0},
        "underline":            {group: 0},
        "strikeThrough":        {group: 0},
        //"fontSize":           {group: 1}, // For later !TODO: This should become special control later that opens a small context menu).
        //"fontColor":          {group: 1},
        "highlight":            {group: 1}, // doesnt work good yet(Chrome ok, FF ok, but not without refocus(?), IE ok)
        "justifyLeft":          {group: 2},
        "justifyCenter":        {group: 2},
        "justifyRight":         {group: 2},
        //"list":               {group: 3}, // For later !TODO: (will be a special control later that opens a small context menu).
        "indent":               {group: 3},
        "outdent":              {group: 3},
        "insertHorizontalRule": {group: 4},
        //"createLink":         {group: 4},
        //"insertImage":        {group: 4},
        //"insertTable":        {group: 4},
        "removeFormat":         {group: 5},
        "sourceCode":           {group: -5},
        "fullScreen":           {group: -5}
       },
       REDUCED: {
        "bold":                 {group: 0},
        "italic":               {group: 0},
        "underline":            {group: 0},
        "strikeThrough":        {group: 0}
        //"fontColor":          {group: 0},
        //"list":               {group: 1}, // For later !TODO: This should become list (will be a special control later that opens a small context menu).
        //"createLink":         {group: 2}
       }
     },

     //option to be set as default for every texteditor
     defaults_available: {
      html: '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="margin:0;"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></head><body style="margin:0; padding:0; font-family:\'Lucida Sans Unicode\',\'Lucida Grande\',sans-serif; font-size:12px;">INITIAL_CONTENT</body></html>',
      debug: false,
      controls: {},
      css: "",
      events: {},
      autoSave: false,
      brIE: false,          // http://code.google.com/p/jwysiwyg/issues/detail?id=15
      formHeight: 270,
      formWidth: 640,
      iFrameClass: "texteditor-iframe",
      label: "", //do not change, created from i18n files
      initialContent: "",
      maxLength: 0,
      messages: {
        nonSelection: "" //do not change, created from i18n files
      },
      markup: {
        toolbar: '<div role="menu" class="btn-toolbar"></div>',
        group: "<div class='btn-group'></div>",
        separator: "<div class='separator'></div>",
        item: "<a></a>",
        buttons: "<div class='wysiwyg-buttons'>" +
                  "<button class='btn-success no-label'><i class='icon-ok white'></i></button>" +
                  "<button class='btn-danger no-label'><i class='icon-cancel'></i></button>" +
               "</div>",
        disableOverlay: "<div id='texteditor-disabled-overlay' style='width:100%;height:100%;position:absolute;background:grey;opacity:0.3;top:0;'></div>"
      },
      itemClass: "btn",
      itemActiveClass: "active",
      itemDisabledClass: "btn-disabled",
      labelClass: "wysiwyg-label",
      removeHeadings: true,
      replaceDivWithP: false,
      resizeOptions: false,
      rmUnusedControls: false,  // https://github.com/akzhan/jwysiwyg/issues/52
      rmUnwantedBr: true,     // http://code.google.com/p/jwysiwyg/issues/detail?id=11
      tableFiller: "Lorem ipsum",
      initialMinHeight: null,
      controlImage: {
        forceRelativeUrls: false
      },
      controlLink: {
        forceRelativeUrls: false
      },
      plugins: { // placeholder for plugins settings
        i18n: { lang: "bsg" },
        rmFormat: {
          rmMsWordMarkup: true
        }
      },
      dialog : "default"
     },

     //all controls available
     //ADD CUSTOM CONTROL HERE |
     //                        v
     controls_available: {
       highlight     : { visible : false, groupIndex: 1, icon: "highlight"},
       bold          : { visible : false, groupIndex: 0 },
       italic        : { visible : false, groupIndex: 0 },
       underline     : { visible : false, groupIndex: 0 },
       strikeThrough : { visible : false, groupIndex: 0},
       justifyLeft   : { visible : false, groupIndex: 2, className: "align-left" },
       justifyCenter : { visible : false, groupIndex: 2, className: "align-center" },
       justifyRight  : { visible : false, groupIndex: 2, className: "align-right" },
       justifyFull   : { visible : false, groupIndex: 2 },
       indent  : { visible : false, groupIndex: 3, icon: "indent" },
       outdent : { visible : false, groupIndex: 3, icon: "outdent" },
       subscript   : { visible : false },
       superscript : { visible : false },
       undo : { visible : false },
       redo : { visible : false },
       insertOrderedList    : { visible : false, groupIndex: 3, className: "list"},
       insertUnorderedList  : { visible : false },
       insertHorizontalRule : { visible : false, className: "horizontal-rule" },
       h1 : {
         visible: false,
         className: 'h1',
         command: ((core.detect.browser.BROWSER === "ie" || core.detect.browser.BROWSER === "safari") ? 'formatBlock' : 'heading'),
         'arguments': ((core.detect.browser.BROWSER === "ie" || core.detect.browser.BROWSER === "safari") ? '<h4>' : 'h4'),
         tags: ['h1'],
         tooltip: 'Header 1'
       },
       h2 : {
         visible: false,
         className: 'h2',
         command: ((core.detect.browser.BROWSER === "ie" || core.detect.browser.BROWSER === "safari") ? 'formatBlock' : 'heading'),
         'arguments': ((core.detect.browser.BROWSER === "ie" || core.detect.browser.BROWSER === "safari") ? '<h4>' : 'h4'),
         tags: ['h2'],
         tooltip: 'Header 2'
       },
       h3 : {
         visible: false,
         className: 'h3',
         command: ((core.detect.browser.BROWSER === "ie" || core.detect.browser.BROWSER === "safari") ? 'formatBlock' : 'heading'),
         'arguments': ((core.detect.browser.BROWSER === "ie" || core.detect.browser.BROWSER === "safari") ? '<h4>' : 'h4'),
         tags: ['h3'],
         tooltip: 'Header 3'
       },
       h4: {
         visible: false,
         className: 'h4',
         command: ((core.detect.browser.BROWSER === "ie" || core.detect.browser.BROWSER === "safari") ? 'formatBlock' : 'heading'),
         'arguments': ((core.detect.browser.BROWSER === "ie" || core.detect.browser.BROWSER === "safari") ? '<h4>' : 'h4'),
         tags: ['h4'],
         tooltip: 'Header 4'
       },
       h5: {
         visible: false,
         className: 'h5',
         command: (core.detect.browser.BROWSER === "ie" || core.detect.browser.BROWSER === "safari") ? 'formatBlock' : 'heading',
         'arguments': (core.detect.browser.BROWSER === "ie" || core.detect.browser.BROWSER === "safari") ? '<h5>' : 'h5',
         tags: ['h5'],
         tooltip: 'Header 5'
       },
       h6: {
         visible: false,
         className: 'h6',
         command: (core.detect.browser.BROWSER === "ie" || core.detect.browser.BROWSER === "safari") ? 'formatBlock' : 'heading',
         'arguments': (core.detect.browser.BROWSER === "ie" || core.detect.browser.BROWSER === "safari") ? '<h6>' : 'h6',
         tags: ['h6'],
         tooltip: 'Header 6'
       },
       cut   : { visible : false },
       copy  : { visible : false },
       paste : { visible : false },
       sourceCode  : { visible: false, className: "source" },
       increaseFontSize : { visible : false, groupIndex: 1, className: "text-height" },
       decreaseFontSize : { visible : false, groupIndex: 1 },
       createLink : { visible : false, className: "link" },
       insertImage : { visible : false, className: "image" },
       insertTable : { visible : false, className: "table" },
       code : { visible : false},
       removeFormat : { visible : false, className: "trash"},
       colorpicker: {
         groupIndex: 1,
         className: "color",
         visible: false,
         css: {
           "color": function (cssValue, Wysiwyg) {
             var document = Wysiwyg.innerDocument(),
                 defaultTextareaColor = core.dom.select(document.body).css("color");
             return (cssValue !== defaultTextareaColor);
           }
         },
         exec: function() {
           if (_texteditor.wysiwygGlobalObject.controls.colorpicker) {
             _texteditor.wysiwygGlobalObject.controls.colorpicker.init(this);
           }
         }
       },
       paragraph : { visible : false },
       ltr : { visible : false },
       rtl : { visible : false },
       fullScreen: {
         groupIndex: 11,
         visible: false,
         className: "fullscreen",
         exec: function () {
           if (_texteditor.wysiwygGlobalObject.fullscreen) {

             if (this.element.hasClass("fullscreen")) {
               this.element.removeClass("fullscreen");
             } else {
               this.element.addClass("fullscreen");
             }
             _texteditor.wysiwygGlobalObject.fullscreen.init(this);
           }
         }
       },
       cssWrap : { visible : false }
     },

     //object containing fixes for controls
     controls_fix: {
         //ie9 highlight fix
         highlight: {
           exec: function () {
             var command, node, selection, args,
                 self = this;

             if (core.detect.browser.BROWSER === "ie" || core.detect.browser.BROWSER === "safari") {
               command = "backcolor";
             } else {
               command = "hilitecolor";
             }

             this.editorDoc.execCommand(command, false, "yellow");
           }
       },

       sourceCode: {
         exec: function () {
           var elementHeight,
               self = this,
               is_fullscreen = core.dom.select(this.element).hasClass("fullscreen");

           if (this.options.resizeOptions && core.dom.select().constructor.fn.resizable) {
             elementHeight = this.element.height();
           }

           if (this.viewHTML) { //textarea is shown
             this.setContent(this.original.value);
             core.dom.select(this.editor.get(0).contentWindow).trigger("blur",["reset"]);
             
             core.dom.select(this.element).after(core.dom.select(this.original));
             
             core.dom.select(this.original).hide();
             this.editor.show();

             if (this.options.resizeOptions && core.dom.select().constructor.fn.resizable) {
               // if element.height still the same after frame was shown
               if (elementHeight === this.element.height()) {
                 this.element.height(elementHeight + this.editor.height());
               }

               this.element.resizable(core.object.extend(true, {
                 alsoResize: this.editor
               }, this.options.resizeOptions));
             }
             this.ui.toolbar.find("." + this.options.itemClass).each(function () {
               var li = core.dom.select(this);

                 li.removeClass('disabled');
             });
             
             
             this.element.removeClass("source");
             
           } else { //wysiwyg is shown
             _texteditor.removeLabel(this.original);
             this.saveContent();

             core.dom.select(this.element).append(core.dom.select(this.original));

             core.dom.select(this.original).css({
               width:  this.element.outerWidth() - 12,
               height: this.element.height() - this.ui.toolbar.height() - 6,
               resize: "none"
             }).show();

             if (core.detect.browser.BROWSER === "ie") {
               core.dom.select(this.original).focus();
             }

             this.editor.hide();

             if (this.options.resizeOptions && core.dom.select().constructor.fn.resizable) {
               // if element.height still the same after frame was hidden
               if (elementHeight === this.element.height()) {
                 this.element.height(this.ui.toolbar.height());
               }

               this.element.resizable("destroy");
             }
             this.ui.toolbar.find("." + this.options.itemClass).each(function () {
               var li = core.dom.select(this);
               if (!li.hasClass("sourceCode")) {
                 if (false === li.hasClass("fullscreen")) {
                   li.removeClass(self.options.itemActiveClass).addClass('disabled');
                 }
               }
             });
             
             this.element.addClass("source");
           }

           this.viewHTML = !(this.viewHTML);
         }
       }
     },

    /**
     * @constructor
     */
    _init: function() {
      var translations = {};

      //get wysiwyg object from jquery plugin namespace
      _texteditor.wysiwygGlobalObject = core.libs.get("jquery").wysiwyg;
      this.i18n = core.i18n.getTranslations("library.uielements");
      if(!this.i18n.texteditor) {
        throw "i18n not found for TextEditor";
      } else {
        this.i18n = this.i18n.texteditor;
      }
      translations = this.createTranslations();

      // Apply fixes and translations for controls.
      this.controls_available = core.object.extend(this.controls_available, this.controls_fix);
      this.controls_available = core.object.extend(this.controls_available, translations);
    },

    /**
     * @see texteditor.getWysiwyg
     */
    getWysiwyg: function(selector) {
      return core.dom.select(selector).data("wysiwyg");
    },

    /**
     * Runs through options.controls and append a translation to the tooltip property from i18n object.
     * @returns {Object} Returns an object with translated controls using current i18n.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    createTranslations: function() {
      
      //apply translations for defaults
      this.defaults_available.label = _texteditor.i18n.defaults.label;
      this.defaults_available.nonSelection = _texteditor.i18n.defaults.nonSelection;
      
      //apply translations for controls
      var translated_controls = {};
      core.object.each(_texteditor.controls_available, function(index, value) {
        var tc = translated_controls[index] = {};
        tc.tooltip = _texteditor.i18n.controls[index];
      });
      return translated_controls;
    },

    /**
     * Creates options.controls version of controls depending on user configuration choice(FULL,BASIC etc.)
     * @param {String} t Type of preset config. i.e 'FULL', 'BASIC'
     * @returns {Object} Returns an object with chosen set of controls.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    createVersion: function(t) {
      var chosen_type = null,
          temp_controls_available = null;

      if (_texteditor.type.hasOwnProperty(t)) {
        chosen_type = _texteditor.type[t];
      } else {
        throw "Config preset: " + t + " doesn't exists.";
      }

      temp_controls_available = core.object.extend({}, _texteditor.controls_available);
      core.object.each(chosen_type, function(index, value) {
        
        temp_controls_available[index].visible = true;
        temp_controls_available[index].groupIndex = value.group;
      });
      return temp_controls_available;
    },

    /**
     * Sets save/discard buttons in disabled mode.
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @returns {Boolean} True on success, false on failure.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    setSaveButtonDisabled: function(selector, mode) {
      _texteditor.getWysiwyg(selector).element.children(".wysiwyg-buttons").children(".btn-success")[mode ? "addClass" : "removeClass" ]("disabled");
    },
    
    /**
     * Sets automatic mode to on or off.
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @param {Boolean} mode True to start automatic save, false to stop it.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    setAutomaticSave: function(selector, mode) {
      clearTimeout(_texteditor.getWysiwyg(selector).saveIntervalRef);

      if (mode) {
        _texteditor.getWysiwyg(selector).saveIntervalRef = setInterval(function(){
          _texteditor.save(selector);
        }, _texteditor.getWysiwyg(selector).options.save.saveInterval);
      }
    },

    /**
     * @see texteditor.create
     */
    create: function(selector, type, options) {
      var self_options = options,
          self_wys_div = null,
          _this = null,
          _self = this,
          iframe = null,
          self_wys = null,
          saveAction = null,
          control = null;

      selector = core.dom.select(selector);

      if (!_texteditor.is(selector) && core.dom.is(selector)) {
        //for each jQuery object in selector set
        core.dom.each(selector, function(_this){
          //choose FULL version as default
          if (type) {
            control = _self.createVersion(type);
          } else {
            control = _self.createVersion('FULL');
            type = "FULL";
          }

          if (options.controls) {
            options.controls = core.object.extend(control, options.controls);
          } else {
            options.controls = core.object.extend({},control);
          }

          options = core.object.extend(_texteditor.defaults_available, options);

          //autoGrow
          if (options.autoGrow.enabled) {
            options.maxHeight = options.autoGrow.maxHeight;
            options.autoGrow = true;
          } else {
            options.autoGrow = false;
          }
          //scrolls hack
          core.dom.ready(function(){
            //ie needs this little delay, other browser doesnt care
            setTimeout(function(){
            _this.prev().find("iframe.texteditor-iframe").css({
              width: (_this.prev().width() - 10)
            });
            }, 10);
          });
          //------ CREATING WYSIWYG----
          _this.wysiwyg(options);
          //---------------------------
          _this.addClass("with-texteditor");
  
          //adding 'type' class
          self_wys = _texteditor.getWysiwyg(_this);
          self_wys_div = self_wys.element.addClass(options.className + " texteditor " + type.toString().toLowerCase());
  
          //start saving loop with fixed interval
          if (options.save.automatic) {
            _texteditor.setAutomaticSave(_this, true);
          }
  
          //catch focus
          self_wys.isFocused = false;
          self_wys.isDisabled = false;
  
          core.dom.select(self_wys.editor.get(0).contentWindow)
            .on("focus" , function() {
              self_wys.isFocused = true;
  
              if (self_options.label) {
                _texteditor.removeLabel(_this);
              }
  
              if (!_texteditor.emptyContentRegex.test(_texteditor.getContent(_this))) {
                _texteditor.setSaveButtonDisabled(_this, false);
              }
            })
            .blur( function() {
              self_wys.isFocused = false;
  
              if (self_options.label && _texteditor.emptyContentRegex.test(_texteditor.getContent(_this))) {
                _texteditor.drawLabel(_this);
                _texteditor.setSaveButtonDisabled(_this, true);
              }
            })
            .on("keyup", function() {
              if (_texteditor.emptyContentRegex.test(_texteditor.getContent(_this))) {
                _texteditor.setSaveButtonDisabled(_this, true);
              } else {
                _texteditor.setSaveButtonDisabled(_this, false);
              }
            });
  
          //show label
          if (options.label && !options.content.trim()) {
            _texteditor.drawLabel(_this);
          }
          
          //show save/discard buttons
          if (options.save.buttons.show) {
            self_wys_div.append(_texteditor.defaults_available.markup.buttons);
            saveAction = options.save.buttons.onSave;
            self_wys_div.children(".wysiwyg-buttons").children(".btn-success:not(.disabled)").off("click").on("click", function(){
              if (!core.dom.select(this).hasClass("disabled")) {
                saveAction.apply(this, arguments);
              }
            });
            self_wys_div.children(".wysiwyg-buttons").children(".btn-danger").off("click").on("click", options.save.buttons.onDiscard);
            self_wys.element.addClass("buttons-" + options.save.buttons.position.toLowerCase());
  
            if (options.save.buttons.position === "top" || options.save.buttons.position === "bottom") {
              self_wys_div.children(".wysiwyg-buttons").children(".btn-success").removeClass('no-label').append("<span>"+_self.i18n.defaults.button.save.label+"</span>");
              self_wys_div.children(".wysiwyg-buttons").children(".btn-danger").removeClass('no-label').append("<span>"+_self.i18n.defaults.button.discard.label+"</span>");
              self_wys_div.children(".wysiwyg-buttons").find(".btn-success > i").removeClass().addClass('icon-save');
            }
          }
  
          //is content set
          if (options.content) {
            self_wys.initialContent = options.content;
            _texteditor.resetContent(_this);
          }
          options.controls = null;
        });
      } else {
        return false;
      }
    },

    /**
     * @see texteditor.is
     */
    is: function(selector) {
      selector = core.dom.select(selector);

      if (selector.hasClass("with-texteditor")) {
        return true;
      }

      return false;
    },

    /**
     * @see texteditor.isChanged
     */
    isChanged: function(selector) {
      selector = core.dom.select(selector);
      if (this.is(selector) && selector.hasClass("with-texteditor-unsaved")) {
          return true;
      }
      return false;
    },

    /**
     * @see texteditor.save
     */
    save: function(selector) {
      var url = "",
          data = "",
          wysiwyg = null;

      selector = core.dom.select(selector);

      if (this.is(selector)  && !_texteditor.isDisabled(selector)) {
        if (selector.wysiwyg("save")) {
          //REST
          wysiwyg = _texteditor.getWysiwyg(selector);
          url = wysiwyg.options.save.url;
          
          data = _texteditor.getContent(selector);
          core.REST.update(url, data, {
            onSuccess: function() {
              //when save is succeded, data sent becomes initalContent.
              //this way hitting discard will remove only all the changes made after last save action
              //not every changes from the begining.
              _texteditor.getWysiwyg(selector).initialContent = data;
              wysiwyg.options.save.onSuccess.apply(this, arguments);
            },
            onError: wysiwyg.options.save.onError,
            onComplete: function() { core.debug.show("completed"); },
            onRequest: function() { core.debug.show("started"); },
            onAbort: function() { core.debug.show("aborted"); }
          });

          selector.removeClass("with-texteditor-unsaved");
          
          return true;
        }
      }
      return false;
    },

    /**
     * @see texteditor.getContent
     */
    getContent: function(selector) {
      selector = core.dom.select(selector);

      if (this.is(selector) && core.dom.is(selector)) {
          if (!core.dom.is(core.dom.select(selector).wysiwyg('document').find("span." + _texteditor.defaults_available.markup.labelClass))) {
            return selector.wysiwyg('getContent');
          }

          return "";
      }

      return false;
    },

    /**
     * @see texteditor.setContent
     */
    setContent: function(selector, new_content, is_html) {
      selector = core.dom.select(selector);

      if (this.is(selector)  && !_texteditor.isDisabled(selector)) {
        if (new_content) {
          if (is_html) {
            selector.wysiwyg('insertHtml', new_content);

            return true;
          } else {
            selector.wysiwyg('setContent', new_content);

            return true;
          }
        }
      }
      return false;
    },

    /**
     * @see texteditor.destroy
     */
    destroy: function(selector, original) {
      var self_wys_btns = null,
          self = null;

      //for every selector
      core.dom.select(selector).each(function() {
        self = core.dom.select(this);

        //destroy only if texteditor instance
        if (_texteditor.is(self)) {
          //get save/discard btns
          self_wys_btns = _texteditor.getWysiwyg(this).element.find(".wysiwyg-buttons>button");

          //remove onlick
          if (self_wys_btns) {
            self_wys_btns.off("click");
          }

          //call internal wysiwyg destroy function
          self.wysiwyg('destroy');
          //remove previously set focus & blur events
          self.off("focus").off("blur");
          self.removeClass("with-texteditor");

          //remove the object itself from DOM
          if (original) {
            self.remove();
          }
        }
      });
    },

    /**
     * @see texteditor.destroy
     */
    resetContent: function(selector) {
      var ic = "";

      selector = core.dom.select(selector);

      if (this.is(selector) && !_texteditor.isDisabled(selector)) {
        ic = _texteditor.getWysiwyg(selector).initialContent;
        _texteditor.setContent(selector, ic ? "<span>" + ic + "</span>" : "<span><br></span> ");
        core.dom.select(_texteditor.getWysiwyg(selector).editor.get(0).contentWindow).trigger("blur",["reset"]);
        
        return true;
      }

      return false;
    },

    /**
     * @see texteditor.attachEvent
     */
    attachEvent: function(selector, type, callback) {
      type = type || 'click';
      selector = core.dom.select(selector);

      if (_texteditor.is(selector)) {
        if (selector.length) {
          selector.wysiwyg('document').on(type, function(evt) {
            evt.preventDefault();
            callback.apply(this, arguments);
          });

          return true;
        }
      } else {
        return false;
      }
    },

    /**
     * @see texteditor.attachEvent
     */
    removeEvent: function(selector, type) {
      type = type || 'click';
      selector = core.dom.select(selector);

      if (_texteditor.is(selector)) {
        if (selector.length) {
          selector.wysiwyg('document').off(type);
          return true;
        }
      } else {
        return false;
      }
    },

    /**
     * @see texteditor.disable
     */
    disable: function(selector) {
      var self_wys = null;

      selector = core.dom.select(selector);

      if (_texteditor.is(selector) && !_texteditor.isDisabled(selector) && core.dom.is(selector)) {
        self_wys = _texteditor.getWysiwyg(selector);
        self_wys.isDisabled = true;
        self_wys.element.find("btn").addClass("disabled").attr("disabled", "disabled");
        self_wys.element.append(_texteditor.defaults_available.markup.disableOverlay);
        
        return true;
      }

      return false;
    },

    /**
     * @see texteditor.enable
     */
    enable: function(selector) {
      var self_wys = null;

      if (_texteditor.is(selector)) {
        if (selector.length) {
          self_wys = _texteditor.getWysiwyg(selector);
          self_wys.isDisabled = false;
          self_wys.element.find("btn").removeClass("disabled").removeAttr("disabled");
          self_wys.element.find("#texteditor-disabled-overlay").remove();

          return true;
        }
      }

      return false;
    },

    /**
     * @see texteditor.getLabel
     */
    getLabel: function(selector) {
      if (_texteditor.is(selector)) {
        if (selector.length) {
          return _texteditor.getWysiwyg(selector).options.label;
        }
      }
    },

    /**
     * @see texteditor.isDisabled
     */
    isDisabled: function(selector) {
      if (_texteditor.is(selector)) {
        if (selector.length) {
          return _texteditor.getWysiwyg(selector).isDisabled;
        }
      }
    },

    /**
     * @see texteditor.isFocused
     */
    isFocused: function(selector) {
      if (_texteditor.is(selector)) {
        if (selector.length) {
          return _texteditor.getWysiwyg(selector).isFocused;
        }
      }
    },

    /**
     * @see texteditor.removeLabel
     */
    removeLabel: function(selector) {
      selector = core.dom.select(selector);
      if (_texteditor.is(selector) && !_texteditor.isDisabled(selector) && core.dom.is(selector)) {
        core.dom.select(selector).wysiwyg('document')
          .find("span." + _texteditor.defaults_available.markup.labelClass)
          .remove();
        return true;
      }
      return false;
    },

    /**
     * @see texteditor.setLabel
     */
    setLabel: function(selector, val) {
      selector = core.dom.select(selector);

      if (_texteditor.is(selector)) {
        _texteditor.getWysiwyg(selector).options.label = val;
        _texteditor.removeLabel(selector);

        if (_texteditor.emptyContentRegex.test(_texteditor.getContent(selector))) {
          _texteditor.drawLabel(selector);
        }

        return true;
      }

      return false;
    },

    /**
     * Draw label on texteditor area.
     *
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @returns {Boolean} True on success, false on failure.
     * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    drawLabel: function(selector) {
      var val = _texteditor.getWysiwyg(selector).options.label;

      selector = core.dom.select(selector);

      if (_texteditor.is(selector) && !_texteditor.isDisabled(selector) && core.dom.is(selector)) {
        _texteditor.setContent(selector, "<span style='color:#bbb' class='" + _texteditor.defaults_available.markup.labelClass + "'>" + val + "</span>", false);

        return true;
      }

      return false;
    },

    /**
     * @see texteditor.save
     */
    getSaveOptions: function(selector) {
      selector = core.dom.select(selector);

      if (this.is(selector)) {
        return _texteditor.getWysiwyg(selector).options.save;
      }

      return false;
    },

    /**
     * @see texteditor.save
     */
    setSaveOptions: function(selector, options) {
      selector = core.dom.select(selector);

      if (this.is(selector)) {
        core.object.extend(_texteditor.getWysiwyg(selector).options.save, options);
        _texteditor.getWysiwyg(selector).element.children(".wysiwyg-buttons")[(options.buttons.show ? "hide" : "show")]();
        _texteditor.setAutomaticSave(selector, options.automatic);
        
        return true;
      }

      return false;
    }
  };

  _texteditor._init();

  /*************************************
   *               Public              *
   *************************************/


  var TextEditor = /** @lends TextEditor */ {
    /**
     * This field is a set of predefined controls. It is used in {@link TextEditor.create} to facilitate choosing controls for TextEditor.
     * FULL contains the following controls:<br />
     * Bold, Italic, Underline, Strike-Through, Increase/decrease Font Size, Font Color, Hightlight, Justify Left/Center/Right, Insert List, Indent, Outdent, Insert Horizontal Rule,
     * Create Link, Insert Image, Insert Table, Remove Format, Show Source and Full Screen.
     * @example
     * TextEditor.create("textarea", TextEditor.FULL);
     * //or
     * TextEditor.create("textarea", "FULL");
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    FULL: "FULL",

    /**
     * Same as FULL only REDUCED contains smaller set of controls.
     * REDUCED contains the following controls:<br />
     * Bold, Italic, Underline, Strike-Through, Font Color, Insert List, Create Link.
     * @example
     * TextEditor.create("textarea", TextEditor.REDUCED);
     * //or
     * TextEditor.create("textarea", "REDUCED");
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    REDUCED: "REDUCED",

    /**
     * Create a nice WYSIWYG text editor out of a TEXTAREA element.
     * @param {CoreObj/Selector} selector The selector for TEXTAREA element.
     * @param {String} [type=texteditor.FULL|texteditor.REDUCED] Predefined set of tools. Either 'FULL' or 'REDUCED' for now.
     * @param {Object} [options] Options.
     *   @option {Object} controls List of controls to show. (Overrides type.) Input object should be formatted as follows:<br>
     *   <pre>{
     *     "&lt;Control ID&gt;": {
     *       group: "&lt;Group Index&gt;", // If group index is subzero, the group is aligned to the right.
     *       label: "&lt;Custom Label&gt;",
     *       tooltip: "&lt;Custom Tooltip&gt;",
     *       exec: "&lt;Custom Function(action)&gt;" //These action is defined in Wysiwyg itself(not if this control is custom).
     *                                               //Do not change this function, unless you want to change the functionality completely.
     *     }
     *   }</pre><br />
     *   Possible options for <Control ID> are bold, italic, strikeThrough, fontSize, underline, fontColor, highlight, justifyLeft, justifyCenter, justifyRight, list, sourceCode, indent, outdent,
     *   fullScreen, insertHorizontalRule, createLink, insertImage, insertTable and removeFormat.
     *   @option {Boolean} save.automatic=true Enable autosave?
     *   @option {Boolean} save.buttons.show=false Show the save/discard buttons.
     *   @option {Function} save.buttons.onSave The callback function executed upon saving.
     *   @option {Function} save.buttons.onDiscard The Callback function executed upon discard.
     *   @option {String} save.url='' The url to which the content should be saved to (apart from the original TEXTAREA).
     *   @option {Function} save.onSuccess The callback executed when the saving is a success and ajax REST call is success. function(data) {}
     *   @option {Function} save.onError The callback executed when the saving failed and/or ajax REST call failed. function(textStatus, exceptionObject) {}, textStatus = null/timeout/error/abort
     *   @option {Function} save.onComplete The callback on complete.
     *   @option {Function} save.onRequest The callback right before the request is made.
     *   @option {Function} save.onAbort The callback on request abort.
     *   @option {Boolean} autoGrow.enabled=false Enable auto growing of the WYSIWYG depending on the content.
     *   @option {Number} autoGrow.maxHeight=400 Maximum height the editor can grow to.
     *   @option {String} content='' The initial content displayed. (Interprets HTML.)
     *   @option {String} label="" The label displayed. This label will disappear when the user inputs text. When this value is empty, no label will be displayed.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleAPI <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    create: function(selector, type, options) {
      return _texteditor.create(selector, type, options);
    }.defaults('', _texteditor.REDUCED, {
      save: {
        automatic: false,
        saveInterval: 5000,
        buttons: {
          show: false,
          position: "right",
          onSave: function() {},
          onDiscard: function() {}
        },
        url: '',
        onSuccess: function() {},
        onError: function() {},
        onAbort: function() {},
        onComplete: function() {},
        onRequest: function() {}
      },
      autoGrow: {
        enabled: false,
        maxHeight: 400
      },
      className: '',
      content: '',
      label: _texteditor.defaults_available.label
    }),

    /**
     * Remove the WYSIWYG text editor from the TEXTAREA.
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @param {Boolean} [original=false] Do you want to remove the original TEXTAREA as well?
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    destroy: function(textEditor, original) {
      _texteditor.destroy(textEditor, original);
    }.defaults('', false),

    /**
     * Disable the WYSIWYG.
     *
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @returns {CoreObj} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    disable: function(textEditor) {
      return _texteditor.disable(textEditor);
    }.defaults(''),

    /**
     * Enable the WYSIWYG.
     *
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @returns {CoreObj} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    enable: function(textEditor) {
      return _texteditor.enable(textEditor);
    }.defaults(''),

    /**
     * Returns content of WYSIWYG editor.
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @returns {String/Boolean} Content or false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    getContent: function(textEditor) {
      return _texteditor.getContent(textEditor);
    }.defaults(''),

    /**
     * What is the label of the WYSIWYG editor?
     *
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @returns {String} The label of the WYSIWYG editor.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    getLabel: function(textEditor) {
      return _texteditor.getLabel(textEditor);
    }.defaults(''),

    /**
     * Return settings concerning saving used in texteditor.
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @returns {Object}
     * <pre>
     * {
     *   automatic {Boolean} Is the automatic saving enabled?
     *   buttons: {
     *     show {Boolean} Are the save and discard buttons displayed?
     *     onSave [Function] A function to be executed upon 'save' button click.
     *     onDiscard [Function] A function to be executed upon 'cancel' button click.
     *   },
     *   url [String] URL defining a place where data should be sent when saving.
     *   onSuccess [Function] The callback executed when the saving is a success and ajax REST call is success.
     *                        The function gets passed three arguments: The data returned from the server, formatted according to the dataType parameter;
     *                        a string describing the status; and the jqXHR (in jQuery 1.4.x, XMLHttpRequest) object function(data, textStatus, jqXHR) {}
     *   onError [Function] The callback executed when the saving failed and/or ajax REST call failed. function(data, textStatus, jqXHR) {}
     *   onComplete [Function] The callback executed when request is complete(Doesnt matter whether it has succeded or failed).
     *   onRequest [Function] The callback executed right before the request is made(Can be used to modlfy XHR object).
     *   onAbort [Function] The callback executed when request is aborted.
     * }</pre>
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    getSaveOptions: function(textEditor) {
      return _texteditor.getSaveOptions(textEditor);
    }.defaults(''),

    /**
     * Is this TEXTAREA a WYSIWYG editor?
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    is: function(textEditor) {
      return _texteditor.is(textEditor);
    }.defaults(''),

    /**
     * Did the content of this WYSIWYG editor change?
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    isChanged: function(textEditor) {
      return _texteditor.isChanged(textEditor);
    }.defaults(''),

    /**
     * Is this WYSIWYG disabled?
     *
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @returns {Boolean} Whether this wysiwyg is disabled or not.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    isDisabled: function(textEditor) {
      return _texteditor.isDisabled(textEditor);
    }.defaults(''),

    /**
     * Is this WYSIWYG focused?
     *
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @returns {Boolean} Whether this WYSIWYG is focused or not.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    isFocused: function(textEditor) {
      return _texteditor.isFocused(textEditor);
    }.defaults(''),

    /**
     * Removes an event handler from the WYSIWYG editor.
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @param {String} type Type/name of the event to be detached.
     * @param {Function} callback Callback function to be executed when event is triggered.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    off: function(textEditor, type) {
      return _texteditor.removeEvent(textEditor, type);
    }.defaults('', 'click'),

    /**
     * Attaches an event handler function to the WYSIWYG editor.
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @param {String} type Type/name of the event to be attached.
     * @param {Function} callback Callback function to be executed when event is triggered.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    on: function(textEditor, type, callback) {
      return _texteditor.attachEvent(textEditor, type, callback);
    }.defaults('', 'click', function() {}),

    /**
     * Reset content to initial content.
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    resetContent: function(textEditor) {
      return _texteditor.resetContent(textEditor);
    }.defaults(''),

    /**
     * Triggers save command. It updates hidden TEXTAREA with data from {@link TextEditor}.
     * When option autoSave.enabled is set to true this is done automatically and when autoSave.url has been set, it is automatically
     * saved to that location as well.
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    save: function(textEditor) {
      return _texteditor.save(textEditor);
    }.defaults(''),

    /**
     * Add/set a label to the WYSIWYG.
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @param {String/Boolean} label The label text. Turns off the label functionality if empty/false.
     * @returns {CoreObj} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:grzegorz@collibra.com">Grzegorz Pawlowski</a>
     */
    setLabel: function(textEditor, label) {
      return _texteditor.setLabel(textEditor, label);
    }.defaults('', ''),

    /**
     * Enable automatic saving to a specific url.
     * (This function does not trigger the save method, only setup's stuff. To save, use {@link TextEditor.save} or enable autosave with {@link TextEditor.setSaveOptions}.)
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @param {Object} [options] Options for autosaving.
     *   @option {Boolean} automatic=true Enable autosave?
     *   @option {Boolean} buttons.show=false Show the save/discard buttons.
     *   @option {Function} buttons.onSave The callback function executed upon saving.
     *   @option {Function} buttons.onDiscard The Callback function executed upon discard.
     *   @option {String} url='' The url to which the content should be saved to (apart from the original TEXTAREA).
     *   @option {Function} onSuccess The callback executed when the saving is a success and ajax REST call is success. function(data) {}
     *   @option {Function} onError The callback executed when the saving failed and/or ajax REST call failed. function(textStatus, exceptionObject) {}, textStatus = null/timeout/error/abort
     *   @option {Function} onComplete The callback on complete.
     *   @option {Function} onRequest The callback right before the request is made.
     *   @option {Function} onAbort The callback on request abort.
     * @returns {Boolean} True on success, false on failure.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    setSaveOptions: function(textEditor, options) {
      return _texteditor.setSaveOptions(textEditor, options);
    }.defaults('', {
      automatic: false,
      saveInterval: 5000,
      buttons: {
        show: false,
        onSave: function() {},
        onDiscard: function() {}
      },
      url: '',
      onSuccess: function() {},
      onError: function() {},
      onAbort: function() {},
      onComplete: function() {},
      onRequest: function() {}
    }),

    /**
     * Changes content of the WYSIWYG.
     * @param {CoreObj/Selector} textEditor A CoreObject or selector pointing to the {@link TextEditor}'s original TEXTAREA.
     * @param {String} [newContent=''] New content to be inserted.
     * @param {Boolean} [isHTML=true] Set true if inserted content is a html.
     * @returns {Boolean} True on success, false on failure after seting new content.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    setContent: function(textEditor, newContent, isHTML) {
      return _texteditor.setContent(textEditor, newContent, isHTML);
    }.defaults('', '', false)
  };

  return TextEditor;
});

core.module.alias.add('TextEditor', 'library/uielements/texteditor');
core.module.use('TextEditor');