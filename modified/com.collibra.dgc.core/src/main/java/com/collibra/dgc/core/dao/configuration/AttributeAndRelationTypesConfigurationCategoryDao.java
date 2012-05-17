/**
 * 
 */
package com.collibra.dgc.core.dao.configuration;

import java.util.List;

import com.collibra.dgc.core.model.configuration.AttributeAndRelationTypesConfigurationCategory;
import com.collibra.dgc.core.model.configuration.ConfigurationCategory;
import com.collibra.dgc.core.model.representation.Representation;

/**
 * @author fvdmaele
 * 
 */
public interface AttributeAndRelationTypesConfigurationCategoryDao {

	AttributeAndRelationTypesConfigurationCategory getCategoryByName(String name);

	List<AttributeAndRelationTypesConfigurationCategory> findByRepresentation(Representation rep);

	AttributeAndRelationTypesConfigurationCategory save(AttributeAndRelationTypesConfigurationCategory category);

	void remove(ConfigurationCategory category);

	List<AttributeAndRelationTypesConfigurationCategory> findAll();

}
