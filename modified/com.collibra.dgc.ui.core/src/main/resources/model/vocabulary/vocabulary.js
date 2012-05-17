/**
 * Vocabulary model
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
 * @module model/vocabulary
 * @alias Vocabulary
 */
/*global define,require,core */
define('model/vocabulary', [ 'core', 'model/modelImpl'], function(require, exports, module) {
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
  var _vocabulary = {
    //The name of the Model. Used mainly in the registering of mediator events.
    type: "Vocabulary",
    
    //Urls used when working with REST interface.
    urls: {
      //Create, retrieve, update, delete community.
      crud: "/vocabulary/[rid]",
      
      //Find Community(s) by name.
      find: "/vocabulary/find",
      
      //Get, remove parent vocabulary.
      parentCommunity: "/vocabulary/[rid]/parent"
    },
    
    //Array of properties that are allowed to be altered/updated on this Model.
    updatableProperties: [
      "name",
      "uri"
    ],

    references: {
      "communityReference": {
        type: "community",
        alias: ["parentCommunity", "parent"]
      },
      "vocabulary": {
        type: "vocabulary"
      },
      "vocabularyReference": {
        type: "vocabulary"
      },
      "vocabularyReferences": {
        type: "vocabulary"
      }
    }
    
  };

  /*************************************
   *               Public              *
   *************************************/

  /**
   * @module model/vocabulary
   */
  var Vocabulary = {
    /**
     * @private
     */
    _private: _vocabulary,

    /**
     * Create a vocabulary model object.
     *
     * @param {String} rid Resource ID of the vocabulary you want to create a model object for.
     * @param {Function} callback Function to be triggered when creating is fully finished (after possible update call). First parameter is the vocabulary model object.
     * @returns {Object} Vocabulary model object.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    create : function(rid, callback) {
      //Create basic object(or fetch internally it from repo if exists).
      return ModelImpl.create(rid, Vocabulary, callback);
    }.defaults('', function() {}),
    
    /**
     * Search for a vocabulary by its name.
     *
     * @param {String} searchName Name to search for.
     * @param {Function} callback Function triggered when searching finishes. Provides an object with the data found as a param.
     * @param {Object} [options] Options for the find method.
     *   @option {Boolean} full=false Whether to fetch full information about object or just only references.
     *   @option {Boolean} offset=0 The position of the first result to return. // !TODO: What is this?
     *   @option {Boolean} number=0 The maximum number of vocabularies to retrieve; if 0 then all results will be retrieved.
     * @returns {Array<Object>} An array of found vocabulary model objects.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    find : function(searchName, callback, options) {
      return ModelImpl.find(this, searchName, callback, options);
    }
  };

  return Vocabulary;
});

core.module.alias.add('Vocabulary', 'model/vocabulary');
core.module.use('Vocabulary');
