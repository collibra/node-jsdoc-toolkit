/**
 * 
 */
package com.collibra.dgc.core.service;

import java.util.List;

import com.collibra.dgc.core.model.configuration.AttributeAndRelationTypesConfigurationCategory;
import com.collibra.dgc.core.model.configuration.ConfigurationCategory;
import com.collibra.dgc.core.model.representation.Representation;

/**
 * @author fvdmaele
 *
 */
public interface ConfigurationCategoryService {

	void removeConfigurationCategory(ConfigurationCategory cat);
	
	AttributeAndRelationTypesConfigurationCategory saveAttributeAndRelationTypeCategory(AttributeAndRelationTypesConfigurationCategory cat);

	AttributeAndRelationTypesConfigurationCategory getCategoryByName(String name);

	AttributeAndRelationTypesConfigurationCategory getCategoryWithError(String catName);

	List<AttributeAndRelationTypesConfigurationCategory> findAttributeAndRelationTypesCategoryByRepresentation(Representation rep);

	List<AttributeAndRelationTypesConfigurationCategory> findAllAttributeAndRelationTypesCategories();
}
