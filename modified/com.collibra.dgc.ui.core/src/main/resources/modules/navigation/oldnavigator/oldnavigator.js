/**
 * This is the JavaScript module for the
 * 123Navigator.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
 * @module modules/navigation/oldnavigator
 * @alias Navigator
 * @namespace modules/navigation
 */
/*global define,require,core */
define('modules/navigation/oldnavigator',
       ['core', 'library/uielements/breadcrumbs', 'library/uielements/tree'],
       function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      breadcrumb = require('BreadCrumb'),
      Tree = require('Tree');

  /*************************************
   *              Private              *
   *************************************/

  /** @private */
  var $ = core.libs.get('jquery');

  /** @private */
  var _navigator = {
    // Translations.
    i18n: {},

    /**
     * @constructor
     */
    _init: function() {
      var _self = this;

      // Get proper translations.
      _self.i18n = core.i18n.getTranslations('modules.navigation.navigator');

      // Add legacy plugin to jquery.
      _self._jPlugin();
    },

    /**
     * @private
     */
    _jPlugin: function() {
      var i18n = this.i18n;

      /**
       * Add OneTwoThreeNavigator as a member function to the main jQuery class.
       */
      $.fn.OneTwoThreeNavigator = function(options) {
        var isMethodCall = (typeof options === 'string'),
            args = Array.prototype.slice.call(arguments, 0),
            name = 'OneTwoThreeNavigator',
            jslint1, jslint2;

        // handle initialization and non-getter methods and return to chained call flow.
        return this.each(function() {
          var instance = $.data(this, name); // Store the name variable as data with the matched element.

          // prevent calls to internal methods
          if (isMethodCall && options.substring(0, 1) === '_') {
            return this;
          }

          // constructor
          jslint1 = (!instance && !isMethodCall && $.data(this, name, new $.OneTwoThreeNavigator(this))._init(args));

          // method call
          jslint2 = (instance && isMethodCall && $.isFunction(instance[options]) && instance[options].apply(instance, args.slice(1)));
        });
      };

      /**
       * Add 123Navigator as a static function to the main jQuery class.
       */
      $.OneTwoThreeNavigator = function(element) {
          var args = Array.prototype.slice.call(arguments, 0), $el;

          if (!element || !element.nodeType) {
              $el = $('<div />');
              return $el.OneTwoThreeNavigator.apply($el, args);
          }

          this.$container = $(element);
      };


      /**
       * Static members
       **/
      $.extend($.OneTwoThreeNavigator, {
          version: '0.1',
          count: 0,
          defaults: {
              //floatt: false, // Should the content float over the content underneath or should it push it down?
              opened: false, // Is the navigator opened by default? // !Has to be implemented!
              contentStyle: false, // Add a style for the content container (the t).
              showTree: false, // Does it show the tree initially? // !Should be tested!
              enableTreeView: true, // Should the tree view be visible? // !Has to be implemented!
              creator: false
          }
      });

      /**
       * Non-static members
       **/
      $.extend($.OneTwoThreeNavigator.prototype, {
        $container: false, // The container on which the navigator is applied upon.
        $floatt: false, // The float used to show the content.
        $bar: false, // The bar that shows the controls to access the navigator.
        $contentTree: false, // The content section with the tree.
        $contentJSTree: false,
        $contentLinear: false, // The content section with the linear data structure.
        floatID: 0, // The id of this navigator (to uniquely identify each navigator on the page).
        firstLoad: true, // Is this the first time the panel is opened?
        loadTree: true, // Is this the first time the tree is loaded?
        $semCol: false, $speCol: false, $vocCol: false, // The content column holders for the 123 nav.
        $semColFilter: false, $speColFilter: false, $vocColFilter: false, // The filters for the 123 nav.
        $semColActions: false, $speColActions: false, $vocColActions: false, // The actions for the navigator.
        $info: false, // The info content holder.
        linearData: false, // The json data object retrieved.
        $breadCrumb: false, // The container that contains the breadcrumb.
        passedSub: false, passedSpe: false, passedVoc: false, // Helper variables for selection of communities.
        TYPE_SEMANTICCOMMUNITY: 'SemanticCommunity', TYPE_SPEECHCOMMUNITY: 'SpeechCommunity',
        TYPE_SUBCOMMUNITY: 'SubCommunity', TYPE_VOCABULARY: 'Vocabulary', // Type constants.
        path: [], // The path that is defined and used for the breadcrumb.
        firstLoadPath: false, // Helper variable that stores the path when a page is loaded to use as default once the navigator is opened.
        options: {}, // The options passed by the user merged with the defaults.
        isMSIE: /*@cc_on!@*/false, // Check if this is a IE browser.


          /**
           * @constructor
           * @param {Object} args Object containing the arguments.
           */
          _init: function(args) {
            var _self = this,
                navbutton;
            _self.floatID = $.OneTwoThreeNavigator.count + 1;

            args = $.isArray(args) ? args : [];

            args.unshift($.OneTwoThreeNavigator.defaults);
            args.unshift({});

            _self.options = $.extend.apply($, args);

            // Add the floatt which will contain the content (both tree and linear navigation).
            if (_self.options.floatt && !_self.floatt) {
              _self.floatt = $('<div id="OneTwoThreeNavigatorfloat' + _self.floatID + '" class="OneTwoThreeNavigatorFloat"><div class="navigatorswitcher">' + i18n.bar.switcher.tree + '</div></div>').appendTo(document.body);
            } else {
              _self.floatt = $('<div id="OneTwoThreeNavigatorContent' + _self.floatID + '" class="OneTwoThreeNavigatorContent"><div class="navigatorswitcher">' + i18n.bar.switcher.tree + '</div></div>').appendTo(_self.$container);
            }

            // Apply content style to the content container.
            if (_self.options.contentStyle) {
              _self.floatt.css(_self.options.contentStyle);
            }

            // Enable expansion when clicked upon the main bar.
            _self.$container.click(function() {
              navbutton = $(this);
              if (navbutton.hasClass('expanded')) {
                _self.collapse();
                navbutton.removeClass('expanded');
              } else {
                _self.expand();
                navbutton.addClass('expanded');
              }
            });

            // Render the content of the navigator.
            _self._renderNavigatorContent();
          },

          /**
           * Position the navigator content container so (if it floats absolutely) it gets positioned right underneath the bar.
           */
          _positionNavigatorContent: function() {
            var _self = this,
                bar_y,
                bar_x;

            if ($('html').hasClass('ie7')) {
              bar_y = _self.$container.offset().top;
            } else {
              bar_y = _self.$container.offset().top + 30;
            }

            bar_x = _self.$container.offset().left;

            _self.floatt.css({
              top: bar_y,
              left: bar_x,
              width: 813,
              position: "absolute"
            });
          },


          /**
           * Renders the content of the navigator either right now or in the future.
           */
          _renderNavigatorContent: function() {
            var _self = this;

            // Add the content section for the linear navigation.
            _self.$contentLinear = $('<div class="OneTwoThreeNavigatorContentLinear"></div>').appendTo(_self.floatt);

            // Add the content section for the tree navigation.
            if (_self.options.enableTreeView) {
              _self.$contentTree = $('<div class="OneTwoThreeNavigatorContentTree"></div>').appendTo(_self.floatt);
            }

            // Add the breadcrumb section.
            _self.$breadCrumb = $(  '<div class="OneTwoThreeBreadCrumb">' +
                          '<div class="breadCrumbHolder breadCrumbmodule">' +
                            '<div class="OneTwoThreeBC breadCrumb breadCrumbmodule BC1">' +
                              '<ul><li><a href="#">Home</a></li></ul>' +
                            '</div>' +
                          '</div><div class="chevronOverlay main"></div>' +
                        '</div>').appendTo(_self.floatt);

            _self.$breadCrumb.find('.BC1 A').click(function() {
              _self._resetLinearNavigator();
            });

            // Add the info section.
            _self.$info = $('<div class="OneTwoThreeInfoBox"></div>').appendTo(_self.floatt);

            // Shows the collapser, the control at the bottom to collapse the navigation.
            $('<div class="Collapser"></div>').click(function() {
              _self.collapse();
            }).appendTo(_self.floatt);

            // Initialize the content of the linear structure.
            _self._initializeContentLinear();
          },

          /**
           * Initialize the linear navigation content.
           */
          _initializeContentLinear: function() {
            var _self = this,
                val,
                data; //!TODO: remove this line

            // Add all the filter fields.
            _self.$semColFilter = $(  '<div class="semFilter OneTwoThreeFilter"><input type="text" class="OTTSemFilter" value="' +
                          i18n.filter + '" /></div>').appendTo(_self.$contentLinear);
            _self.$speColFilter = $(  '<div class="speFilter OneTwoThreeFilter"><input type="text" class="OTTSpeFilter" value="' +
                          i18n.filter + '" /></div>').appendTo(_self.$contentLinear);
            _self.$vocColFilter = $(  '<div class="vocFilter OneTwoThreeFilter"><input type="text" class="OTTVocFilter" value="' +
                          i18n.filter + '" /></div>').appendTo(_self.$contentLinear);

            // Make sure the default text disappears when clicked upon that filter field.
            _self.$contentLinear.find('.OneTwoThreeFilter INPUT').focus(function(ev) {
              _self._onFocusFilterField(ev.target);
            }).blur(function(ev) {
              _self._onFocusFilterField(ev.target);
            });

            // Activate the filter for the semantic communities column.
            _self.$contentLinear.find('.OneTwoThreeFilter INPUT.OTTSemFilter').keyup(function(ev) {
              val = $(ev.target).val();

              if (val === '') {
                _self._resetSemCol();
              } else {
                _self.filterSemanticCommunities(val);
              }
            });

            // Activate the filter for the speech communities column.
            _self.$contentLinear.find('.OneTwoThreeFilter INPUT.OTTSpeFilter').keyup(function(ev) {
              val = $(ev.target).val();
              _self.filterSpeechCommunities(val);
            });

            // Activate the filter for the semantic communities column.
            _self.$contentLinear.find('.OneTwoThreeFilter INPUT.OTTVocFilter').keyup(function(ev) {
              val = $(ev.target).val();
              _self.filterVocabularies(val);
            });

            // Add all the action fields.
            //TODO: find better way to do this
            //Display semantic creation button if user has right to create community on whole glossary. User may have right to create speech
            //or vocabulary for specific semantic community so display other buttons for every user.
            if (i18n.rights.canCreateCommunity === true) {
              _self.$semColActions = $('<div class="semActions OTTActions">' +
                                          '<div class="OTTRemoveAction OTTAction" title="' +
                                          i18n.linear.actions.remove + '"></div>' +
                                          '<div class="OTTAddAction OTTAction" title="' + i18n.linear.actions.add +
                                          '"></div><div class="OTTActionLabel"></div></div>').appendTo(_self.$semColFilter);
            }

            _self.$speColActions = $('<div class="speActions OTTActions">' +
                                     '<div class="OTTRemoveAllAction OTTAction" title="' +
                                     i18n.linear.actions.removeall + '"></div>' +
                                     '<div class="OTTRemoveAction OTTAction" title="' +
                                     i18n.linear.actions.remove + '"></div>' +
                                     '<div class="OTTAddAction OTTAction" title="' + i18n.linear.actions.add +
                                     '"></div><div class="OTTActionLabel"></div></div>').appendTo(_self.$speColFilter);
            _self.$vocColActions = $('<div class="vocActions OTTActions">' +
                                     '<div class="OTTRemoveAllAction OTTAction" title="' +
                                     i18n.linear.actions.removeall + '"></div>' +
                                     '<div class="OTTRemoveAction OTTAction" title="' +
                                     i18n.linear.actions.remove + '"></div>' +
                                     '<div class="OTTAddAction OTTAction" title="' + i18n.linear.actions.add +
                                     '"></div><div class="OTTActionLabel"></div></div>').appendTo(_self.$vocColFilter);

            // Show label when hovered over an action.
            _self.$contentLinear.find('.OTTAction').mouseover(function(ev) {
              $(ev.target).parent().children('.OTTActionLabel').text($(ev.target).attr('title')).fadeIn(200);
            }).mouseout(function(ev) {
              $(ev.target).parent().children('.OTTActionLabel').fadeOut(200);
            });

              /*// !TODO: commented until creation is done
              // Create a speech community bubble.
              _self.options.creator.createSpeechCommunitySpecific(
                _self.$speColActions.find('.OTTAddAction'),
                false,
                false,
                'bottom',
                function(data) {
                  _self.addSpeechCommunity(data.name, data.resourceid, data.parentresourceid);
                }
              );

              // Create a vocabulary bubble.
              _self.options.creator.createVocabularySpecific(
                _self.$vocColActions.find('.OTTAddAction'),
                false,
                false,
                'bottom',
                function(data) {
                  _self.addVocabulary(data.name, data.resourceid, data.parentresourceid);
                }
              );

              // Create a semantic community bubble.
              if (i18n.rights.canCreateCommunity == true) {
                _self.options.creator.createSemanticCommunity(
                  _self.$semColActions.find('.OTTAddAction'),
                  false,
                  false,
                  'bottom',
                  function(data) {
                    _self.addSemanticCommunity(data.name, data.resourceid);
                  }
                );
              }*/

            // Add all the columns for the 123 Linear Navigator
            _self.$semCol = $('<div class="semCol OneTwoThreeColumn"><div class="emptyOTTColumn">' +
                      i18n.linear.empty.sem +
                      '</div><div class="loadingOTTColumn">' +
                      i18n.linear.loading +
                      '</div></div>').appendTo(_self.$contentLinear);
            _self.$speCol = $('<div class="speCol OneTwoThreeColumn"><div class="emptyOTTColumn">' +
                      i18n.linear.empty.spe +
                      '</div><div class="loadingOTTColumn">' +
                      i18n.linear.loading +
                      '</div></div>').appendTo(_self.$contentLinear);
            _self.$vocCol = $('<div class="vocCol OneTwoThreeColumn"><div class="emptyOTTColumn">' +
                      i18n.linear.empty.voc +
                      '</div><div class="loadingOTTColumn">' +
                      i18n.linear.loading +
                      '</div></div>').appendTo(_self.$contentLinear);

            // Request initial data to fill the 123 Linear Navigation.
/*            $.ajax({
              url: i18n.url.onetwothree,
              dataType: 'json',
              success: function(data) {
                _self.linearData = data;
                _self._addOneTwoThreeContent();
              },
              error: function() {
*/              /*
                $.achtung({
                  message: i18n.ajax.error,
                  className: 'error',
                  timeout: 0
                }); */
/*              }
            });
*/
//dummy data
            data = {
                    "semanticCommunities" : {
                      "01" : {
                        "title" : "My Semantic 01",
                        "type" : "semantic-community",
                        "parent" : ""
                      },
                      "02" : {
                        "title" : "My Semantic 02",
                        "type" : "semantic-community",
                        "parent" : ""
                      },
                      "03" : {
                        "title" : "My Semantic 03",
                        "type" : "semantic-community",
                        "parent" : ""
                      }
                    },
                    "speechCommunities" : {
                      "0101" : {
                        "title" : "My Speech 0101",
                        "type" : "speech-community",
                        "parent" : "01"
                      },
                      "0201" : {
                        "title" : "My Speech 0201",
                        "type" : "speech-community",
                        "parent" : "02"
                      },
                      "0202" : {
                        "title" : "My Speech 0202",
                        "type" : "speech-community",
                        "parent" : "02"
                      }
                    },
                    "vocabularies" : {
                      "010101" : {
                        "title" : "My vocab 010101",
                        "type" : " vocabulary ",
                        "parent" : "0101"
                      },
                      "010102" : {
                        "title" : "My Vocabulary 010102",
                        "type" : " vocabulary ",
                        "parent" : "0101"
                      },
                      "020201" : {
                        "title" : "My Vocabulary 020201",
                        "type" : " vocabulary ",
                        "parent" : "0202"
                      }
                    }
                    };
            _self.linearData = data;
            _self._addOneTwoThreeContent();
//end of dummy code

            $('<div class="clearfloats"></div>').appendTo(_self.$contentLinear);
          },

          /**
           * What happens when a filter field is focussed?
           * @param {Object} target The event object passed on the triggering event.
           */
          _onFocusFilterField: function(target) {
            if ($(target).val() === i18n.filter) {
              $(target).val('');
            } else {
              if ($(target).val() === '') {
                $(target).val(i18n.filter);
              }
            }
           },

          /**
           * Initialize the tree navigational structure.
           */
          _initializeContentTree: function() {
            var _self = this,
                entry = '',
                el,
                tree,
                node,
                sem,
                spe,
                voc,
                data = '';

            if (_self.path.length > 0) {
              entry = "&entry=" + encodeURIComponent(_self.path[_self.path.length - 1][1]);
            }

            //_self.$contentTree.load(i18n.url.tree + entry , function() {
              _self.$contentTree.append($('<div />', {
                id : "organizational-tree"
              })); //this was done by load, but since we don't use "load" we have to add it manually
              $('<div class="OTTTreeControls"></div>').prependTo(_self.$contentTree);

              el = _self.$contentTree.find('#tree-focused-node').parents('.semantic-community');
              tree = _self.$contentTree.find('#organizational-tree');

              if (el.length > 0 && !_self.isMSIE) {
                core.dom.scrollTo(_self.$contentTree, el, {
                  easing: 'easeOutBack',
                  duration: 600,
                  axis: 'y'
                });
              }

            //moved from v3 NavigationCode/OrganizationalTree
              Tree.create('#organizational-tree', {
                core : {
                  animation: 100,
                  strings: {
                    "loading": "Loading..."
                  }
                },
                plugins : [ "themes", "types", "ui", "lang", "json_data", "crrm", "dnd" ],
                json_data : {
                  //"ajax" : { "url" : "/bsg32/bin/get/NavigationCode/JSONOrganizationalTree?xpage=plain&outputSyntax=plain" }
                  //dummy data
                  "data" : [
                      {
                      "attr" : {
                        "resourceid": "01",
                        "title" : "My Semantic 01",
                        "class" : "semantic-community"
                      },
                      "data" : "My Semantic 01",
                      "state" : "closed",
                      "children": [ {
                        "attr" : {
                          "resourceid": "0101",
                          "title" : "My Speech 0101",
                          "class" : "speech-community"
                        },
                        "data" : "My Speech 0101",
                        "state" : "closed",
                        "children": [ {
                            "attr" : {
                              "resourceid": "010101",
                              "title" : "My vocab 010101",
                              "class" : "vocabulary"
                            },
                            "data" : "My vocab 010101"
                          },
                          {
                            "attr" : {
                              "resourceid": "010102",
                              "title" : "My vocabulary 010102",
                              "class" : "vocabulary"
                            },
                            "data" : "My vocabulary 010102"
                          }
                        ]}
                      ]},
                      {
                      "attr" : {
                        "resourceid": "02",
                        "title" : "My Semantic 02",
                        "class" : "semantic-community"
                      },
                      "data" : "My Semantic 02",
                      "state" : "closed",
                      "children": [ {
                      "attr" : {
                        "resourceid": "0201",
                        "title" : "My Speech 0201",
                        "class" : "speech-community"
                      },
                      "data" : "My Speech 0201"
                      },
                      {
                      "attr" : {
                        "resourceid": "0202",
                        "title" : "My Speech 0202",
                        "class" : "speech-community"
                      },
                      "data" : "My Speech 0202",
                      "state" : "closed",
                      "children": [ {
                      "attr" : {
                        "resourceid": "020201",
                        "title" : "My Vocab 020201",
                        "class" : "vocabulary"
                      },
                      "data" : "My Vocab 020201"
                      }
                      ]}
                      ]},
                      {
                      "attr" : {
                      "resourceid": "03",
                      "title" : "My Semantic 03",
                      "class" : "semantic-community"
                      },
                      "data" : "My Semantic 03"
                      }
                      ]
                  //end of dummy data
                },
                crrm : {
                  "move" : {
                    "check_move" : function (m) {
                      var movingObj = m.o[0],
                          newParent = m.np[0],
                          movingObjClass,
                          newParentClass;
                      if (!movingObj || !newParent) {
                        return false;
                      }

                      movingObjClass = movingObj.className;
                      newParentClass = newParent.className;

                      if((movingObjClass.indexOf("semantic-community") !== -1) && (newParentClass.indexOf("semantic-community") !== -1)) {
                        return true;
                      }

                      if((movingObjClass.indexOf("speech-community") !== -1) && (newParentClass.indexOf("semantic-community") !== -1)) {
                        return true;
                      }

                      if((movingObjClass.indexOf("vocabulary") !==-1) && (newParentClass.indexOf("speech-community") !== -1)) {
                        return true;
                      }

                      return false;
                    }
                  }
                },
                dnd : {
                  "drop_target" : false,
                  "drag_target" : false
                },
                ui: {
                  initially_select : ["tree-focused-node"]
                },
                types : {
                  "types" : {
                    "default" : {
                      "select_node" : function(node) {
                        $('#taxonomic-tree').trigger('nodeClicked', [node, this]);
                        this.toggle_node(node);
                        return true;
                      }
                    }
                  }
                }
              });

              tree.bind("select_node.jstree", function (e, data) {
                node = data.rslt.obj;
                e.stopImmediatePropagation();
                _self._showInfo($(node).attr('resourceid'));

                if ($(node).hasClass('semantic-community') || $(node).hasClass('semantic-sub-community')) {
                  sem = _self.$semCol.children('.OTTSemCom[node="' + $(node).attr('resourceid') + '"]');
                  _self._semComSelected(
                    $(node).attr('resourceid'),
                    _self.linearData.semanticCommunities[$(node).attr('resourceid')],
                    sem, true);
                } else if ($(node).hasClass('speech-community')) {
                  spe = _self.$speCol.children('.OTTSpeCom[node="' + $(node).attr('resourceid') + '"]');
                  _self._speComSelected(
                    $(node).attr('resourceid'),
                    _self.linearData.speechCommunities[$(node).attr('resourceid')],
                    spe, true);
                } else {
                    voc = _self.$vocCol.children('.OTTVoc[node="' + $(node).attr('resourceid') + '"]');
                    spe = _self.$speCol.children('.OTTSpeCom[node="' + voc.attr('parent') + '"]');
                    _self._vocSelected(
                      $(node).attr('resourceid'),
                      _self.linearData.vocabularies[$(node).attr('resourceid')],
                      voc, true);
                }
              });
            //});
          },

          /**
           * Add the content retreived to the linear navigation.
           */
          _addOneTwoThreeContent: function() {
            var _self = this,
                semComs = _self.linearData.semanticCommunities,
                htmlSemComs = [];

            _self.$semCol.children('.loadingOTTColumn').hide();
            _self.$speCol.children('.loadingOTTColumn').hide();
            _self.$vocCol.children('.loadingOTTColumn').hide();


            //
            // Arrays for string concatenation are used to get offline
            // DOM building and faster concatenation. Got a speed increase
            // of 60% by doing this...
            //


            //
            // Add Semantic Communities
            //
            for (var semKey in semComs) {
              if(typeof semComs[semKey] === 'object') {
                htmlSemComs.push('<div class="OTTSemCom" node="');
                htmlSemComs.push(semKey);
                htmlSemComs.push('"><div class="OTTSemComIcon"></div><div class="OTTSemComContent">');
                htmlSemComs.push(semComs[semKey].title);
                htmlSemComs.push('</div></div>');
              }
            }

            _self.$semCol.append(htmlSemComs.join('')).bind('click dblclick', function(ev) {
              var semCom = $(ev.target);

              if (!semCom.hasClass('OTTSemCom')) {
                semCom = semCom.parent();
                if (!semCom.hasClass('OTTSemCom')) {
                  return;
                }
              }

              var semKey = semCom.attr('node'),
                  semValue = _self.linearData.semanticCommunities[semKey];

              // If the user double click it should directly go to the community. Otherwise it should
              // show the info and select it properly.
              if (ev.type === 'dblclick') {
                window.location.href = i18n.docForward + '?resourceId=' + semKey + '&type=semanticcommunity';
              } else {
                _self._showInfo(semKey, 'semantic-community');
                _self._semComSelected(semKey, semValue, semCom, true);
              }
            });

            //
            // Add Speech Communities
            //
            var speComs = _self.linearData.speechCommunities,
                htmlSpeComs = [];

            for (var speKey in speComs) {
              if(typeof speComs[speKey] === 'object') {
                var spe = speComs[speKey].type === 'speech-community',
                    icon = spe?'OTTSpeComIcon':'OTTSubComIcon';

                htmlSpeComs.push('<div class="OTTSpeCom" node="');
                htmlSpeComs.push(speKey);
                htmlSpeComs.push('" parent="');
                htmlSpeComs.push(speComs[speKey].parent);
                htmlSpeComs.push('"><div class="');
                htmlSpeComs.push(icon);
                htmlSpeComs.push('"></div><div class="OTTSpeComContent">');
                htmlSpeComs.push(speComs[speKey].title);
                htmlSpeComs.push("</div></div>");
              }
            }

            _self.$speCol.append(htmlSpeComs.join('')).bind('click dblclick', function(ev) {
              var speCom = $(ev.target);

              if (!speCom.hasClass('OTTSpeCom')) {
                speCom = speCom.parent();
                if (!speCom.hasClass('OTTSpeCom')) {
                  return;
                }
              }

              var speKey = speCom.attr('node'),
                  isSpeCom = speCom.children('.OTTSpeComIcon').length > 0,
                  speValue = _self.linearData.speechCommunities[speKey];

              // If the user double click it should directly go to the community. Otherwise it should
              // show the info and select it properly.
              if (ev.type === 'dblclick') {
                window.location.href = i18n.docForward + '?resourceId=' + speKey + '&type=speechcommunity';
              } else {
                _self._showInfo(speKey, isSpeCom?'speech-community':'subcommunity');
                if (isSpeCom) {
                  _self._speComSelected(speKey, speValue, speCom, true);
                } else {
                  _self._subComSelected(speKey, speValue, speCom, true);
                }
              }
            });

            //
            // Add Vocabularies
            //
            var vocs = _self.linearData.vocabularies,
                htmlVocs = [];

            for (var vocKey in vocs) {
              if(typeof vocs[vocKey] === 'object') {
                htmlVocs.push('<div class="OTTVoc" node="');
                htmlVocs.push(vocKey);
                htmlVocs.push('" parent="');
                htmlVocs.push(vocs[vocKey].parent);
                htmlVocs.push('"><div class="OTTVocIcon"></div><div class="OTTVocContent">');
                htmlVocs.push(vocs[vocKey].title);
                htmlVocs.push('</div></div>');
              }
            }

            _self.$vocCol.append(htmlVocs.join(''));

            var soptions = _self.$vocCol.children();//.sorted();
            _self.$vocCol.html(soptions);

            _self.$vocCol.bind('click dblclick', function(ev) {
              var voc = $(ev.target);

              if (!voc.hasClass('OTTVoc')) {
                voc = voc.parent();
                if (!voc.hasClass('OTTVoc')) {
                  return;
                }
              }

              var vocKey = voc.attr('node'),
                  vocValue = _self.linearData.vocabularies[vocKey];

              // If the user double clicks, it should directly go to the vocabulary. Otherwise it should
              // show the info and select it properly.
              if (ev.type === 'dblclick') {
                window.location.href = i18n.docForward + '?resourceId=' + vocKey + '&type=vocabulary';
              } else {
                _self._showInfo(vocKey, 'vocabulary');
                _self._vocSelected(vocKey, vocValue, voc, true);
              }
            });

            return _self.firstLoadPath?_self.changePath(_self.firstLoadPath):_self._initialInfo();
          },

          /**
           * Filter the column of semantic communities on a certain search string.
           * @param {string} str The string to filter on.
           */
          filterSemanticCommunities: function(str) {
            var any = false,
                _self = this;

            _self.$semCol.children('.loadingOTTColumn').hide();
            _self.$semCol.find('.OTTSemCom').each(function(idx, item) {
              if ($(item).children('.OTTSemComContent:icontains(' + str + ')').length > 0) {
                $(item).show();
                any = true;
              } else {
                $(item).hide();
              }
            });

            if (any === false) {
              _self.$semCol.children('.emptyOTTColumn').fadeIn();
            } else {
              _self.$semCol.children('.emptyOTTColumn').fadeOut();
            }
          },

          /**
           * Filter the column of semantic communities on a certain search string.
           * @param {string} str The string to filter on.
           */
          filterSpeechCommunities: function(str) {
            this._filter(str, this.$speCol, '.OTTSpeCom', '.OTTSpeComContent');
          },

          /**
           * Filter the vocabularies.
           * @param  {string} str  Filter string.
           **/
          filterVocabularies: function(str) {
            this._filter(str, this.$vocCol, '.OTTVoc', '.OTTVocContent');
          },

          /**
           * Private filter function used by filterVocabularies and filterSpeechCommunities.
           *
           * @param   str                 Filter string.
           * @param   column              The column that should be filtered.
           * @param   entryClass          The class name of an entry in that column. (ex: .OTTSpeCom)
           * @param   entryContentClass   The class name of the content of an entry. (ex: .OTTSpeComContent)
           * @return  undefined
           **/
          _filter: function(str, column, entryClass, entryContentClass) {
            var any = false,
                _self = this;

            column.children('.loadingOTTColumn').hide();

            column.children(entryClass).each(function(idx, item) {
              // Reset if item is filtered or not.
              $(item).removeClass('filterin').removeClass('filterout');

              // If the search string is empty, don't filter and check if there is any that was initially displayed...
              if (str === '') {
                if ($(item).css('display') === 'block') {
                  any = true;
                }
                return;
              }

              // Check if the item contains the filter text, show if so, hide if not.
              if ($(item).children(entryContentClass + ':icontains(' + str + ')').length > 0) {
                $(item).addClass('filterin');
                any = true;
              } else {
                $(item).addClass('filterout');
              }
            });

            // If no entries are shown, show the notification!
            if (any === false) {
              column.children('.emptyOTTColumn').fadeIn();
            } else {
              column.children('.emptyOTTColumn').fadeOut();
            }
          },

          /**
           * Resets filter input field
           */
          resetFilter: function() {
            var _self = this;

            _self.$contentLinear.find('.OneTwoThreeFilter INPUT').val(i18n.filter);
          },

          /**
           * Mark this element as selected and scroll to it.
           * @param el (element) The DOM element that was selected.
           */
          _semComSelectElement: function(el) {
            var _self = this;
            // Remove the selected class of the previously selected semantic community.
            _self.$semCol.children('.selected').removeClass('selected');
            _self.$semCol.removeClass('selected');

            if (el === false) {
              return;
            }

            // Add the selected class to the currently selected element and the column.
            _self.$semCol.addClass('selected');
            $(el).addClass('selected');

            // Scroll the selected element to the top.
            if (!_self.isMSIE) {
              core.dom.scrollTo(_self.$semCol, el, {
                easing: 'easeOutBack',
                duration: 600,
                axis: 'y'
              });
            }

            // Give this community up as the default parent for the speech community creation.
             _self.$speColActions.find('.OTTAddAction').attr('defaultParent', $(el).attr('node'));
          },

          /**
           * Mark this element as selected and scroll to it.
           * @param el (element) The DOM element that was selected.
           */
          _speComSelectElement: function(el) {
            var _self = this;
            // Remove the selected class of the previously selected speech community.
            _self.$speCol.children('.selected').removeClass('selected');
            _self.$speCol.removeClass('selected');

            if (el === false) {
              return;
            }

            // Add the selected class to the currently selected element and the column.
            _self.$speCol.addClass('selected');
            $(el).addClass('selected');

            // Scroll the selected element to the top.
            if (!_self.isMSIE) {
              core.dom.scrollTo(_self.$speCol, el, {
                easing: 'easeOutBack',
                duration: 600,
                axis: 'y'
              });
            }

            // Give this community up as the default parent for the speech community creation.
             _self.$vocColActions.find('.OTTAddAction').attr('defaultParent', $(el).attr('node'));
          },

          /**
           * Mark this element as selected and scroll to it.
           * @param el (element) The DOM element that was selected.
           */
          _vocSelectElement: function(el) {
            var _self = this;
            // Remove the selected class of the previously selected vocabulary.
            _self.$vocCol.children('.selected').removeClass('selected');
            _self.$vocCol.removeClass('selected');

            if (el === false) {
              return;
            }

            // Add the selected class to the currently selected element and the column.
            _self.$vocCol.addClass('selected');
            $(el).addClass('selected');

            // Scroll the selected element to the top.
            if (!_self.isMSIE) {
              core.dom.scrollTo(_self.$vocColel, {
                easing: 'easeOutBack',
                duration: 600,
                axis: 'y'
              });
            }
          },

          /**
           * What happens when a certain semantic community is selected?
           * @param semKey (string) The semantic community fullname.
           * @param semVal (object) The data with all the children of this particular semantic community.
           * @param el (element) The DOM element.
           * @param adjustBreadCrumb (boolean) Should the breadcrumb be adjusted when a semantic community is selected?
           */
          _semComSelected: function(semKey, semVal, el, adjustBreadCrumb) {
            var _self = this;
            // Don't continue when the the selected element is already selected.
            if ($(el).hasClass('selected')) {
              return;
            }

          //!TODO:qtip| var addSemList = _self.$speColActions.find('.OTTAddAction').qtip('api').elements.content.find('SELECT.specomSemCom');
          //addSemList.children().filter(':selected').removeAttr('selected');
          //addSemList.children('[value="' + semKey + '"]').attr('selected', 'selected');

            _self._semComSelectElement(el);
            _self._speComSelectElement(false);
            _self._vocSelectElement(false);

            var spes = []; // All the speech communities whose vocabularies should be shown.
            var OTTSpe = false;

            // Go over all the speech communities and filter the wrong ones out.
            _self.$speCol.children('.OTTSpeCom').each(function(idx, item) {
              var $item = $(item);
              var node = $item.attr('node'); // The fullname of the selected speech community.

              $item.removeClass('selected'); // Make sure none of the speech communities is selected.

              // Check if the selected DOM element (AKA the speech community in the loop) is a child of the semantic community.
              if ($item.attr('parent') === semKey) {
                $item.show(); // Show the element if it is true.
                OTTSpe = true;

                var dataNode = _self.linearData.speechCommunities[node];
                // If the speech community is an actual speech community (and not a subcommunity), make sure the vocabularies will be shown.
                if (dataNode.type === 'speech-community') {
                  spes[node] = true;
                }
              } else {
                $item.hide(); // Hide the element if not.
              }
            });

            if (OTTSpe) {
              _self.$speCol.children('.emptyOTTColumn').slideUp();
            } else {
              _self.$speCol.children('.emptyOTTColumn').fadeIn();
            }

            var OTTVoc = false;

            // Go  over all the vocabularies and filter the wrong ones out.
            _self.$vocCol.children('.OTTVoc').each(function(idx, item) {
              // Make sure none of the vocabularies is selected.
              $(item).removeClass('selected');

              // Check if the vocabulary has to be shown or not and do so (check if the parent of the vocab is in the list of displayed spComms)
              if (spes[$(item).attr('parent')] === true) {
                $(item).show();
                OTTVoc = true;
              } else {
                $(item).hide();
              }
            });

            if (OTTVoc) {
              _self.$vocCol.children('.emptyOTTColumn').slideUp();
            } else {
              _self.$vocCol.children('.emptyOTTColumn').fadeIn();
            }

            if (adjustBreadCrumb) {
              if (_self.passedVoc) {
                _self._breadCrumbPop();
                _self._breadCrumbPop();
              }

              if (!_self.passedSub) {
                _self._breadCrumbReset();
                _self._breadCrumbPush(semVal.title, semKey, _self.TYPE_SEMANTICCOMMUNITY);
                _self._generateBreadCrumb();
              } else {
                if (_self.passedSpe) {
                  _self._breadCrumbChangeLast(semVal.title, semKey, _self.TYPE_SUBCOMMUNITY);
                } else {
                  _self._breadCrumbPush(semVal.title, semKey, _self.TYPE_SUBCOMMUNITY);
                  _self._generateBreadCrumb();
                }
                _self.passedSub = false;
              }
            }

            _self.passedSpe = false;
            _self.passedVoc = false;
          },

          /**
           * What happens when a certain speech community is selected?
           * @param speKey (string) The speech community fullname.
           * @param speVal (object) The data with all the children of this particular speech community.
           * @param el (element) The DOM element.
           * @param adjustBreadCrumb (boolean) Should the breadcrumb be adjusted?
           */
          _speComSelected: function(speKey, speVal, el, adjustBreadCrumb) {
            var _self = this;
            // If the selected element is equal to the currently selected one, exit function.
            if ($(el).hasClass('selected')) {
              return;
            }

            //Select this speech comm. as default for creating vocabulary
/*!TODO:qtip| var addSpeList = _self.$vocColActions.find('.OTTAddAction').qtip('api').elements.content.find('SELECT.vocSpeCom');
            addSpeList.children().filter(':selected').removeAttr('selected');
            addSpeList.children('[value="' + speKey + '"]').attr('selected', 'selected');
*/
            //Select parent semantic as default for creating new speech comm.
/*!TODO:qtip| var addSemList = _self.$speColActions.find('.OTTAddAction').qtip('api').elements.content.find('SELECT.specomSemCom');
            addSemList.children().filter(':selected').removeAttr('selected');
            addSemList.children('[value="' + speVal.parent + '"]').attr('selected', 'selected');
*/
            //updating selected semantic community
            var sem = _self.$semCol.children('.OTTSemCom[node="' + $(el).attr('parent') + '"]');
            _self._semComSelectElement(sem);
            _self._speComSelectElement(el);
            _self._vocSelectElement(false);

            //TODO: Fix breadcrumbs when semantic not selected
            if (!_self.$semCol.hasClass('selected')) {
              _self._breadCrumbPush(sem.text(), sem.attr('node'), _self.TYPE_SEMANTICCOMMUNITY);
            } else {
              _self._breadCrumbReset();
              _self._breadCrumbPush(sem.text(), sem.attr('node'), _self.TYPE_SEMANTICCOMMUNITY);
            }

            var OTTVoc = false;

            _self.$vocCol.children('.OTTVoc').each(function(idx, item) {
              $(item).removeClass('selected');
              if ($(item).attr('parent') === speKey) {
                $(item).show();
                OTTVoc = true;
              } else {
                $(item).hide();
              }
            });

            if (OTTVoc) {
              _self.$vocCol.children('.emptyOTTColumn').hide();
            } else {
              _self.$vocCol.children('.emptyOTTColumn').show();
            }

            if (adjustBreadCrumb) {
              if (_self.passedVoc) {
                _self._breadCrumbPop();
              }

              if (_self.passedSpe) {
                _self._breadCrumbChangeLast(speVal.title, speKey, _self.TYPE_SPEECHCOMMUNITY);
                if (_self.passedVoc) {
                  _self._generateBreadCrumb();
                }
              } else {
                if (_self.passedVoc) {
                  _self._breadCrumbPop();
                }
                _self._breadCrumbPush(speVal.title, speKey, _self.TYPE_SPEECHCOMMUNITY);
                _self._generateBreadCrumb();
              }
            }

            _self.passedSpe = true;
            _self.passedVoc = false;
          },

          /**
           * On selecting a subcommunity, do the following...
           * @param subKey (String) The fullname of the subcommunity.
           * @param el (element) The DOM element.
           * @param adjustBreadCrumb (boolean) Should the breadcrumb be adjusted?
           */
          _subComSelected: function(subKey, el, adjustBreadCrumb) {
            var _self = this;

            _self.passedSub = true;
            // Select the appropriate Semantic Community.
            _self._semComSelected(subKey, _self.linearData.semanticCommunities[subKey], _self.$semCol.children(".OTTSemCom[node='" + subKey + "']"), adjustBreadCrumb);
          },

          /**
           * On selecting a vocabulary, do the following...
           * @param vocKey (String) The fullname of the vocabulary.
           * @param vocVal (Object) The data of this particular vocabulary.
           * @param el (element) The DOM element.
           * @param adjustBreadCrumb (boolean) Should the breadcrumb be adjusted?
           */
          _vocSelected: function(vocKey, vocVal, el, adjustBreadCrumb) {
            var _self = this;

            // Check if the speech community was selected and if not, select the appropriate one.
            if (!_self.$speCol.hasClass('selected')) {
              var speCom = _self.$speCol.children('.OTTSpeCom[node="' + $(el).attr('parent') + '"]');
              _self._speComSelectElement(speCom);

              // Check if the semantic community was selected and if not, select the appropriate one.
              if(!_self.$semCol.hasClass('selected')) {
                var semCom = _self.$semCol.children('.OTTSemCom[node="' + speCom.attr('parent') + '"]');
                _self._semComSelectElement(semCom);

                _self._breadCrumbPush(semCom.text(), semCom.attr('node'), _self.TYPE_SEMANTICCOMMUNITY);
              }

              _self._breadCrumbPush(speCom.text(), speCom.attr('node'), _self.TYPE_SPEECHCOMMUNITY);
            }

            //Select parent speech comm. as default for creating vocabulary
/*!TODO:qtip| var addSpeList = _self.$vocColActions.find('.OTTAddAction').qtip('api').elements.content.find('SELECT.vocSpeCom');
            addSpeList.children().filter(':selected').removeAttr('selected');
            addSpeList.children('[value="' + vocVal.parent + '"]').attr('selected', 'selected');
*/
            // Select the correct vocabulary.
            _self._vocSelectElement(el);
            //updating selected semantic and speech comm
            var spe = _self.$speCol.children('.OTTSpeCom[node="' + $(el).attr('parent') + '"]'),
                sem = _self.$semCol.children('.OTTSemCom[node="' + $(spe).attr('parent') + '"]');
            _self._speComSelectElement(spe);
            _self._semComSelectElement(sem);

            if (adjustBreadCrumb) {
              if (_self.passedVoc) {
                _self._breadCrumbChangeLast(vocVal.title, vocKey, _self.TYPE_VOCABULARY);
              } else {
                _self._breadCrumbPush(vocVal.title, vocKey, _self.TYPE_VOCABULARY);
                _self._generateBreadCrumb();
              }
            }

            _self.passedVoc = true;
          },

          /**
           * Resets vocabulary column
           */
          _resetVocCol: function() {
            var _self = this;

            _self.$vocCol.removeClass('selected').children().each(function(idx, item) {
              if ($(item).hasClass('OTTVoc')) {
                $(item).removeClass('selected').show();
              } else {
                $(item).hide();
              }
            });
          },

          /**
           * Resets speech community column
           */
          _resetSpeCol: function() {
            var _self = this;

            _self.$speCol.removeClass('selected').children().each(function(idx, item) {
              if ($(item).hasClass('OTTSpeCom')) {
                $(item).removeClass('selected').show();
              } else {
                $(item).hide();
              }
            });
          },

          /**
           * Resets semantic community column
           */
          _resetSemCol: function() {
            var _self = this;

            _self.$semCol.removeClass('selected').children().each(function(idx, item) {
              if ($(item).hasClass('OTTSemCom')) {
                $(item).removeClass('selected').show();
              } else {
                $(item).hide();
              }
            });
          },

          /**
           * Resets vocabulary column
           */
          _resetLinearNavigator: function() {
            var _self = this;

            _self._resetSemCol();
            _self._resetSpeCol();
            _self._resetVocCol();
            _self._breadCrumbReset();
            _self._generateBreadCrumb();
            _self.passedSpe = _self.passedVoc = _self.passedSub = false;
            _self._initialInfo();
          },

          /**
           * Show info
           */
          _showInfo: function(resourceid, type) {
// !TODO            this.$info.load(i18n.url.info, { "resourceid": resourceid, "type": type, "xpage": "plain" });
          },

          /**
           * Resets the breadcrumb.
           */
          _breadCrumbReset: function() {
            this.path = [];
          },

          /**
           * Removes the last element of the breadcrumb.
           * @returns The removed element.
           */
          _breadCrumbPop: function() {
            return this.path.pop();
          },

          /**
           * Adds new element to the end of the breadcrumb.
           * @param {String} name The name of the element.
           * @param {String} fullName The fullname of the element.
           * @param {String} type The function executed when clicked upon.
           * @returns The new length of the breadcrumb.
           */
          _breadCrumbPush: function(name, fullName, type) {
            return this.path.push([name, fullName, type]);
          },

          /**
           * Breadcrumb change last
           * @param {String} name The name of the element.
           * @param {String} fullName The fullname of the element.
           * @param {String} type The function executed when clicked upon.
           */
          _breadCrumbChangeLast: function(name, fullName, type) {
            this._breadCrumbPop();
            this._breadCrumbPush(name, fullName, type);

            this.$breadCrumb.find('.breadCrumbHolder:visible LI.last A').html(name).attr('fullname', fullName).attr('type', type);
          },

          /**
           * Generates breadcrumb
           */
          _generateBreadCrumb: function() {
            var _self = this,
                $bc = this.$breadCrumb.find('.BC1');

            $bc.html('<ul><li><a href="#">Home</a></li></ul>').find('A').click(function() {
              _self._resetLinearNavigator();
            });

            for(var i = 0, s = _self.path.length; i < s; i = i + 1) {
              $('<li><a fullname="' + _self.path[i][1] + '" type="' + _self.path[i][2] + '">' + _self.path[i][0] + '</a></li>').appendTo($bc.children('UL')).click(_self._breadCrumbAction);
            }

            breadcrumb.create($bc);
          },

          /**
           * Updates breadcrumb
           * @param ev event
           */
          _breadCrumbAction: function(ev) {
            var _self = this,
                fullName = $(ev.target).attr('fullName'),
                type = $(ev.target).attr('type'),
                el;

            for (var i = _self.path.length - 1; i >= 0; i = i - 1) {
              if (fullName !== _self.path[i][1]) {
                _self.path.pop();
              } else {
                break;
              }
            }

            _self._generateBreadCrumb();

            switch(type) {
              case _self.TYPE_SEMANTICCOMMUNITY: case _self.TYPE_SUBCOMMUNITY:
                _self.$semCol.removeClass('selected').children('.OTTSemCom').each(function(idx, item) {
                  $(item).removeClass('selected');
                  if ($(item).attr('node') === fullName) {
                    el = $(item);
                  }
                });

                if (type === _self.TYPE_SUBCOMMUNITY) {
                  _self.passedSub = true;
                }

                _self._semComSelected(fullName, _self.linearData.semanticCommunities[fullName], el, false);
                _self.passedSpe = false;
                break;

              case _self.TYPE_SPEECHCOMMUNITY:
                el = _self.$speCol.children('.OTTSpeCom[node="' + fullName + '"]');

                _self.$speCol.removeClass('selected').children('.OTTSpeCom').each(function(idx, item) {
                  $(item).removeClass('selected');
                  if ($(item).attr('node') === fullName) {
                    el = $(item);
                  }
                });

                _self._speComSelected(fullName, _self.linearData.speechCommunities[fullName], el, false);

                _self.passedSpe = true;
                break;

              case _self.TYPE_VOCABULARY:
                break;

              default:
                break;
              }
          },

          /**
           * Show the initial process info in the info pane.
           */
          _initialInfo: function() {
            this.$info.html(
              '<div class="OTTProcessImage One"></div><div class="OTTProcessText">' +
              i18n.info.one +
              '</div><div class="OTTProcessImage Two"></div><div class="OTTProcessText">' +
              i18n.info.two +
              '</div><div class="OTTProcessImage Three"></div><div class="OTTProcessText">' +
              i18n.info.three +
              '</div>'
            );
          },

          /**
           * Change the path of the navigator.
           * @param {String} resourceId Resource ID of a document that has to be selected in the navigator.
           */
          changePath: function(resourceId) {
            var foundEl;

            if (!this.firstLoadPath) {
              this.firstLoadPath = resourceId;
              return;
            }

            foundEl = this.$contentLinear.find('[node="' + resourceId + '"]')[0];
            this.firstLoadPath = false;

            if (foundEl === undefined || foundEl.length <= 0) {
              return;
            }

            this._showInfo(resourceId);

            if ($(foundEl).hasClass('OTTSemCom')) {
              this._semComSelected(resourceId, this.linearData.semanticCommunities[resourceId], foundEl, true);
              this._showInfo(resourceId);
            }

            if ($(foundEl).hasClass('OTTSpeCom')) {
              this._speComSelected(resourceId, this.linearData.speechCommunities[resourceId], foundEl, true);
              this._showInfo(resourceId);
            }

            if ($(foundEl).hasClass('OTTVoc')) {
              var spe = this.$speCol.children('.OTTSpeCom[node="' + $(foundEl).attr('parent') + '"]');
              this._vocSelected(resourceId, this.linearData.vocabularies[resourceId], foundEl, true);
            }

            setTimeout(this._scrollToPosition, 500);
          },

          /**
           * Scrolls to selected position
           */
          _scrollToPosition: function() {
            var _self = this,
                sem,
                spe,
                voc;

            if (_self.$semCol === undefined || _self.$speCol === undefined || _self.$vocCol === undefined) {
              return;
            }

            sem = _self.$semCol.children('.selected');
            spe = _self.$speCol.children('.selected');
            voc = _self.$vocCol.children('.selected');

            if (sem.length > 0 && !_self.isMSIE) {
              core.dom.scrollTo(_self.$semCol, sem, {
                easing: 'easeOutBack',
                duration: 600,
                axis: 'y'
              });
            }

            if (spe.length > 0 && !_self.isMSIE) {
              core.dom.scrollTo(_self.$speCol, spe, {
                easing: 'easeOutBack',
                duration: 600,
                axis: 'y'
              });
            }

            if (voc.length > 0 && !_self.isMSIE) {
              core.dom.scrollTo(_self.$vocCol, voc, {
                easing: 'easeOutBack',
                duration: 600,
                axis: 'y'
              });
            }
          },

          /**
           * Expand action
           */
          expand: function() {
            var _self = this;

            if (_self.options.floatt) {
              _self._positionNavigatorContent();
            }

            // When the tree view is enabled, show the switcher.
            if (_self.options.enableTreeView) {
              _self.floatt.slideDown(function() {
                if (_self.firstLoad) {
                  // Show the correct navigator first.
                  if (_self.options.showTree && _self.options.enableTreeView) {
                    _self.switchToTree();
                  } else {
                    _self.switchToLinear();
                  }

                // Enable breadcrumbs.
                breadcrumb.create(_self.$breadCrumb.find('.OneTwoThreeBC'));
                _self.firstLoad = false;
                }
              });
            }
          },

          /**
           * Collapse action
           */
          collapse: function() {
            var _self = this;

            _self.floatt.slideUp();
          },

          /**
           * Switch to linear view
           */
          switchToLinear: function() {
            var _self = this;
            _self.floatt.find('.navigatorswitcher').html(i18n.bar.switcher.tree).unbind().click(function(ev) {
              ev.stopImmediatePropagation();
              _self.switchToTree();
            });

            _self.options.showTree = false;

            _self.$contentTree.fadeOut(function() {
              _self.$contentLinear.fadeIn();

              // Scroll the selected element to the top.
              var sem = _self.$semCol.children('.selected');

              if (sem.length > 0 && !_self.isMSIE) {
                core.dom.scrollTo(_self.$semCol, sem, {
                  easing: 'easeOutBack',
                  duration: 600,
                  axis: 'y'
                });
              }

              // Scroll the selected element to the top.
              var spe = _self.$speCol.children('.selected');

              if (spe.length > 0 && !_self.isMSIE) {
                core.dom.scrollTo(_self.$speCol, spe, {
                  easing: 'easeOutBack',
                  duration: 600,
                  axis: 'y'
                });
              }

              // Scroll the selected element to the top.
              var voc = _self.$vocCol.children('.selected');

              if (voc.length > 0 && !_self.isMSIE) {
                core.dom.scrollTo(_self.$vocCol, voc, {
                  easing: 'easeOutBack',
                  duration: 600,
                  axis: 'y'
                });
              }
            });
          },

          /**
           * Switch to tree view
           */
          switchToTree: function() {
            var _self = this;

            _self.floatt.find('.navigatorswitcher').html(i18n.bar.switcher.onetwothree).unbind().click(function(ev) {
              ev.stopImmediatePropagation();
              _self.switchToLinear();
            });

            _self.options.showTree = true;

            _self.$contentLinear.fadeOut(function() {
              _self._initializeContentTree();
            _self.$contentTree.fadeIn();
            });
          },

          /**
           * Add a semantic community to the navigator.
           * @param {String} name The name of the semantic community.
           * @param {String} fullname The resource id of the semantic community.
           */
          addSemanticCommunity: function(name, semResourceId) {
            var _self = this,
                newEl,
                addSemList,
                soptions;

            _self.linearData.semanticCommunities[semResourceId] = {
              title: name,
              type: "semantic-community",
              children: {}
            };

            _self.$speCol.removeClass('selected').children().each(function(idx, item) {
              if ($(item).hasClass('emptyOTTColumn')) {
                $(item).slideDown();
              } else {
                $(item).removeClass('selected').hide();
              }
            });

            _self.$vocCol.removeClass('selected').children().each(function(idx, item) {
              if ($(item).hasClass('emptyOTTColumn')) {
                $(item).slideDown();
              } else {
                $(item).removeClass('selected').hide();
              }
            });

            newEl = $('<div class="OTTSemCom" node="' + semResourceId + '">' +
                      '<div class="OTTSemComIcon"></div>' +
                      '<div class="OTTSemComContent">' + name + '</div>' +
                      '</div>')
                      .unbind()
                      .click(function() {
                        _self._showInfo(semResourceId);
                        _self._semComSelected(semResourceId, _self.linearData.semanticCommunities[semResourceId], this, true);
                    });
            _self._resetLinearNavigator();
            _self._insertSorted(_self.$semCol, '.OTTSemCom', newEl, name, _self.linearData.semanticCommunities);

            newEl.click();

/*!TODO:qtip| addSemList = _self.$speColActions.find('.OTTAddAction').qtip('api').elements.content.find('SELECT.specomSemCom');
            addSemList.children().filter(':selected').removeAttr('selected');
*/            $('<option value="' + semResourceId + '" selected="selected">' + name + '</option>').appendTo(addSemList);
            soptions = addSemList.children();//.sorted();
            addSemList.html(soptions);
          },

          /**
           * Add a speech community to the navigator.
           * @param {String} name The name of the speech community.
           * @param {String} speResourceId The resource id of the speech community.
           * @param {String} semResourceId The resource id of this speech community's semantic community.
           */
          addSpeechCommunity: function(name, speResourceId, semResourceId) {
            var _self = this,
                sem = _self.$semCol.children('.OTTSemCom[node="' + semResourceId + '"]');

            _self.linearData.speechCommunities[speResourceId] = {
              title: name,
              type: "speech-community",
              children: {}
            };

            _self.$vocCol.removeClass('selected').children().each(function(idx, item) {
              if ($(item).hasClass('emptyOTTColumn')) {
                $(item).slideDown();
              } else {
                $(item).removeClass('selected').hide();
              }
            });

            _self.$speCol.children('.emptyOTTColumn').slideUp();

            var newEl = $(  '<div class="OTTSpeCom" parent="' + semResourceId + '" node="' + speResourceId + '">' +
                      '<div class="OTTSpeComIcon"></div>' +
                      '<div class="OTTSpeComContent">' + name + '</div>' +
                    '</div>')
                    .unbind()
                    .click(function() {
                      _self._showInfo(speResourceId);
                      _self._speComSelected(speResourceId, _self.linearData.speechCommunities[speResourceId], this, true);
                    });

            _self._insertSorted(_self.$speCol, '.OTTSpeCom', newEl, name, _self.linearData.speechCommunities);

/*!TODO:qtip| var addVocList = _self.$vocColActions.find('.OTTAddAction').qtip('api').elements.content.find('SELECT.vocSpeCom');
            addVocList.children().filter(':selected').removeAttr('selected');
            $('<option value="' + speResourceId + '" selected="selected">' + name + '</option>').appendTo(addVocList);
            var soptions = addVocList.children();//.sorted();
            addVocList.html(soptions);
*/
            newEl.click();

            //Select parent semantic as default for creating new speech comm.
/*!TODO:qtip| var addSemList = _self.$speColActions.find('.OTTAddAction').qtip('api').elements.content.find('SELECT.specomSemCom');
            addSemList.children().filter(':selected').removeAttr('selected');
            addSemList.children('[value="' + semResourceId + '"]').attr('selected', 'selected');
*/          },

          /**
           * Add a vocabulary to the navigator.
           * @param {String} name The name of the vocabulary.
           * @param {String} vocResourceId The resource id of the vocabulary.
           * @param {String} speResourceId The resource id of this vocabulary's speech community.
           */
          addVocabulary: function(name, vocResourceId, speResourceId) {
            var _self = this;
            var spe = _self.$speCol.children('.OTTSpeCom[node="' + speResourceId + '"]');
            _self.linearData.vocabularies[vocResourceId] = {
              title: name,
              type: "vocabulary"
            };

            _self.$vocCol.children('.emptyOTTColumn').slideUp();

            var newEl = $(  '<div class="OTTVoc" parent="' + speResourceId + '" node="' + vocResourceId + '">' +
                      '<div class="OTTVocIcon"></div>' +
                      '<div class="OTTVocContent">' + name + '</div>' +
                    '</div>')
                    .unbind()
                    .click(function() {
                      _self._showInfo(vocResourceId);
                      _self._vocSelected(vocResourceId, _self.linearData.vocabularies[vocResourceId], this, true);
                    });
           _self._insertSorted(_self.$vocCol, '.OTTVoc', newEl, name, _self.linearData.vocabularies);

            newEl.click();

            //Select parent speech comm. as default for creating vocabulary
/*!TODO:qtip| var addSpeList = _self.$vocColActions.find('.OTTAddAction').qtip('api').elements.content.find('SELECT.vocSpeCom');
            addSpeList.children().filter(':selected').removeAttr('selected');
            addSpeList.children('[value="' + speResourceId + '"]').attr('selected', 'selected');
*/          },

          /**
           * Inserts node in container, so that it preserves alphabetical order among the children of container that match siblingsSelector,
           * where node is sorted by sortingValue and the siblings by the metadata found in metadataContainer.
           */
          _insertSorted : function(container, siblingsSelector, node, sortingValue, metadataContainer) {
            // find the proper place of this node, in alphabetical order
            var beforeNode = null;
            // look in all siblings and find the first node which is bigger than this one
            var siblings = container.children(siblingsSelector);
            for(var i = 0; i < siblings.length; i = i + 1) {
              var currentNode = $(siblings[i]);
              var thisTitle = metadataContainer[currentNode.attr('node')].title;
              if (thisTitle.toLowerCase() > sortingValue.toLowerCase()) {
                beforeNode = currentNode;
                break;
              }
            }
            // if such a node was found, great, insert before it, otherwise at the end of the list
            if (beforeNode) {
              beforeNode.before(node);
            } else {
              node.appendTo(container);
            }
          }
      });
    },

    /**
     * @see navigator.initialize
     */
    initialize: function(el) {
      var _self = this;

      // Create legacy container for the content.
      core.dom.create('<div id="navigationmenucontent"></div>').insertAfter('#navigationmenu');

      // Initialize the navigator on the element.
      core.dom.select(el).OneTwoThreeNavigator({
        floatt: true,
        contentStyle: {
          'background-color': '#51514c',
          '-moz-border-radius-bottomleft': '10px',
          '-moz-border-radius-bottomright': '10px',
          '-webkit-border-bottom-left-radius': '10px',
          '-webkit-border-bottom-right-radius': '10px'
        }
      });
    }
  };

  _navigator._init();

  /*************************************
   *               Public              *
   *************************************/

  var Navigator = /** @lends Navigator */ {
    /**
     * Initialize the navigator on a certain element.
     * Most of this is legacy code take up from v3 and not
     * complaint at all to the way things are done in v4.
     * Should be converted asap!
     * !TODO: Convert 123Navigator to the v4 way of doing things.
     * @param {CoreObj/Selector} el The handle that opens the navigator.
     * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
     */
    initialize: function(el) {
      _navigator.initialize(el);
    }
  };

  return Navigator;
});

core.module.alias.add('Navigator', 'modules/navigation/oldnavigator');
core.module.use('Navigator', function(nav) {
  "use strict";
  nav.initialize("#navigatortrigger");
});