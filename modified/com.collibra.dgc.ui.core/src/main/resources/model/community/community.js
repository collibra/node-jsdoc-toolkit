/**
 * Community model
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
 * @module model/community
 * @alias Community
 */
/*global define,require,core */
define('model/community', [ 'core', 'model/modelImpl'], function(require, exports, module) {
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
  var _community = {
    // The name of the Model.
    type: "Community",
    
    // Urls used when working with REST interface.
    urls: {
      // Create, retrieve, update, delete community.
      crud: "/community/[rid]",
      
      // Find Community(s) by name.
      find: "/community/find",
      
      // Get, remove parent community.
      parentCommunity: "/community/[rid]/parent",
      // Get subcommunities of the community.
      subCommunities: "/community/[rid]/sub-communities",
      // Get vocabularies of the community.
      vocabularies: "/community/[rid]/vocabularies"
    },
    
    // Array of properties that are allowed to be altered/updated on this Model.
    updatableProperties: [
      "name",
      "uri",
      "language"
    ],

    // Reference configuration object.
    // Each of the key corresponds to the reference object returned in the JSON data of this model.
    // This object should specify all the possible references for this model.
    references: {
      "vocabularyReferences": {
        //This tells ModelImpl to create model object of type 'Vocabulary' when found 'vocabularyReferences' object.
        type: "vocabulary",
        //Those model object will be available to be get as normal property under these names(both).
        alias: ["vocabularies", "vocs"]
      },
      "subCommunityReferences": {
        type: "community",
        alias: ["subcommunities", "communities", "coms"]
      },
      "parentReference": {
        type: "community",
        alias: ["parentCommunity", "parent"]
      },
      "community": {
        type: "community"
      },
      "communityReference": {
        type: "community"
      },
      "communityReferences": {
        type: "community"
      }
    }
  };

  /*************************************
   *               Public              *
   *************************************/

  /**
   * @module model/community
   */
  var Community = {
    /**
     * @private
     */
    _private: _community,

    /**
     * Create a community model object.
     *
     * @param {String} rid Resource ID of the community you want to create a model object for.
     * @param {Function} callback Function to be triggered when creating is fully finished (after possible update call). First parameter is the community model object.
     * @returns {Object} Community model object.
     * @responsibleAPI <a href="mailto:clovis@collibra.com">Clovis Six</a>
     * @responsibleImplementation <a href="mailto:gkaczan@collibra.com">Grzegorz Kaczan</a>
     */
    create : function(rid, callback) {
      //Create basic object(or internally fetch it from repo if exists).
      return ModelImpl.create(rid, Community, callback);
    }.defaults('', function() {}),
    
    /**
     * Search for a community by its name.
     *
     * @param {String} searchName Name to search for.
     * @param {Function} callback Function triggered when searching finishes. Provides an object with the data found as a param.
     * @param {Object} [options] Options for the find method.
     *   @option {Boolean} full=false Whether to fetch full information about object or just only references.
     *   @option {Boolean} offset=0 The position of the first result to return. // !TODO: What is this?
     *   @option {Boolean} number=0 The maximum number of communities to retrieve; if 0 then all results will be retrieved.
     * @returns {Array<Object>} An array of found community model objects.
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

  return Community;
});

core.module.alias.add('Community', 'model/community');
core.module.use('Community');
