/**
 * Term model
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
 * @module model/term
 * @alias Term
 */
/*global define,require,core */
define('model/term', [ 'core', 'model/modelImpl'], function(require, exports, module) {
  "use strict";
  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      ModelImpl = require('model/modelImpl');

  /*************************************
   *          Url definitions          *
   *************************************/

  /** @private */
  var _term = {
    // The name of the Model.
    type: "Term",
    
    // Urls used when working with REST interface.
    urls: {
      // Create, retrieve, update, delete term.
      crud: "/term/[rid]",
      
      // Find Term(s) by name.
      find: "/term/find",
      
      attributeTypes: "/term/[rid]/possible_attribute_types"
    },
    
    updatableProperties: [
      "name",
      "uri",
      "language"
    ],

    references: {
      "vocabularyReference": {
        type: "vocabulary",
        alias: "vocabulary"
      },
      "attributeReferences": {
        type: "attribute",
        alias: "attributes"
      },
      "term": {
        type: "term"
      },
      "termReference": {
        type: "term"
      },
      "termReferences": {
        type: "term"
      },
      "characteristicFormReferences": {
        type: "unknown",
        alias: "characteristicFormReferences"
      },
      "binaryFactTypeFormReferences": {
        type: "unknown",
        alias: "characteristicFormReferences"
      },
      "synonymReferences": {
        type: "unknown",
        alias: "synonym"
      },
      "statusReference": {
        type: "unknown",
        alias: "status"
      }
    },
    
    //This object contains all the logic that will be shared within model object object of type Term.
    //Internal shared logic.
    //'this' refers to created model object, so all its properties can be accessed through it.
    methods: {
      getAttributesTypes: function(callback) {
        core.REST.get(this._private.urls.attributeTypes, {}, {
          onSuccess: callback
        });
      }
    }
  };

  /*************************************
   *               Public              *
   *************************************/

  /**
   * @module model/term
   */
  var Term = {
    /**
     * @private
     */
    _private: _term,

    /**
     * Create a term model object.
     *
     * @param {String} rid Resource ID of the term you want to create a model object for.
     * @param {Function} callback Function to be triggered when creating is fully finished (after possible update call). First parameter is the term model object.
     * @returns {Object} Term model object.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    create : function(rid, callback) {
      //Create basic object(or internally fetch it from repo if exists).
      return ModelImpl.create(rid, Term, callback);
    }.defaults('', function() {}),
    
    /**
     * Search for a term by its name.
     *
     * @param {String} searchName Name to search for.
     * @param {Function} callback Function triggered when searching finishes. Provides an object with the data found as a param.
     * @param {Object} [options] Options for the find method.
     *   @option {Boolean} full=false Whether to fetch full information about object or just only references.
     *   @option {Boolean} offset=0 The position of the first result to return. // !TODO: What is this?
     *   @option {Boolean} number=0 The maximum number of communities to retrieve; if 0 then all results will be retrieved.
     * @returns {Array<Object>} An array of found term model objects.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    find: function(searchName, callback, options) {
      ModelImpl.find(this, searchName, callback, options);
    }.defaults('', function() {}, {
      full: false,
      offset: 0,
      number: 0
    })
  };

  return Term;
});

core.module.alias.add('Term', 'model/term');
core.module.use('Term');
