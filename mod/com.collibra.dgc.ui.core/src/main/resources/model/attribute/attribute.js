/**
 * Attribute model
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
 * @module model/attribute
 * @alias Attribute
 */
/*global define,require,core */
define('model/attribute', [ 'core', 'model/modelImpl'], function(require, exports, module) {
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
  var _attribute = {
    // The name of the Model.
    type: "Attribute",
    
    // Urls used when working with REST interface.
    urls: {
      // Create, retrieve, update, delete attribute.
      crud: "/attribute/[rid]",
      
      // Find Attribute(s) by name.
      find: "/attribute/find"
    },
    
    // Array of properties that are allowed to be altered/updated on this Model.
    updatableProperties: [
      "value"
    ],

    // Reference configuration object.
    // Each of the key corresponds to the reference object returned in the JSON data of this model.
    // This object should specify all the possible references for this model.
    references: {
      "owner": {
        //This tells ModelImpl to create model object of type 'Vocabulary' when found 'vocabularyReferences' object.
        type: "term",
        //Those model object will be available to be get as normal property under these names(both).
        alias: ["owner, parent"]
      },
      "attribute": {
        type: "attribute"
      },
      "attributeReference": {
        type: "attribute"
      },
      "attributeReferences": {
        type: "attribute"
      }
    }
  };

  /*************************************
   *               Public              *
   *************************************/

  /**
   * @module model/attribute
   */
  var Attribute = {
    /**
     * @private
     */
    _private: _attribute,

    /**
     * Create a attribute model object.
     *
     * @param {String} rid Resource ID of the attribute you want to create a model object for.
     * @param {Function} callback Function to be triggered when creating is fully finished (after possible update call). First parameter is the attribute model object.
     * @returns {Object} Attribute model object.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    create : function(rid, callback) {
      //Create basic object(or internally fetch it from repo if exists).
      return ModelImpl.create(rid, Attribute, callback);
    }.defaults('', function() {}),
    
    /**
     * Search for a attribute by its name.
     *
     * @param {String} searchName Name to search for.
     * @param {Function} callback Function triggered when searching finishes. Provides an object with the data found as a param.
     * @param {Object} [options] Options for the find method.
     *   @option {Boolean} full=false Whether to fetch full information about object or just only references.
     *   @option {Boolean} offset=0 The position of the first result to return. // !TODO: What is this?
     *   @option {Boolean} number=0 The maximum number of communities to retrieve; if 0 then all results will be retrieved.
     * @returns {Array<Object>} An array of found attribute model objects.
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

  return Attribute;
});

core.module.alias.add('Attribute', 'model/attribute');
core.module.use('Attribute');
