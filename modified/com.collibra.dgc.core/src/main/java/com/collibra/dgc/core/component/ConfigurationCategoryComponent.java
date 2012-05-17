/**
 * 
 */
package com.collibra.dgc.core.component;

import java.util.List;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.configuration.AttributeAndRelationTypesConfigurationCategory;
import com.collibra.dgc.core.model.configuration.ConfigurationCategory;
import com.collibra.dgc.core.model.representation.Representation;

/**
 * Public API for the management of {@link ConfigurationCategory}s. A {@link ConfigurationCatgory} is a grouping of
 * {@link Representation}s in a specific order, that is given a unique category name and description. The grouped
 * representations can represent different things. </br> </br> In the case of a
 * {@link AttributeAndRelationTypesConfigurationCategory}, they represent attribute types or relation types. </br> A
 * Configuration Category is uniquely referenced by its name.
 * 
 * @author fvdmaele
 * 
 */
public interface ConfigurationCategoryComponent {

	/**
	 * Create a new {@link AttributeAndRelationTypesConfigurationCategory} and persist it
	 * 
	 * @param name The name of the category. Must be unique!
	 * @param description The description of the category. Can be null (optional)
	 * @param representationRIds The list of resource id's of {@link Representation}s that represent Attribute Types
	 *            (Terms) or Relation Types (BinaryFactTypeForms)
	 * @return {@link AttributeAndRelationTypesConfigurationCategory}
	 * @throws {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#CONFCAT_ALREADY_EXISTS},
	 *         {@link DGCErrorCodes#CONFCAT_SAVE_ERROR}
	 */
	AttributeAndRelationTypesConfigurationCategory addAttributeAndRelationTypesConfigurationCategory(String name,
			String description, List<String> representationRIds);

	/**
	 * Remove the given {@link ConfigurationCategory}.
	 * 
	 * @param name The name that uniquely references the {@link ConfigurationCategory}
	 * @throws {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#CONFCAT_NAME_NULL},
	 *         {@link DGCErrorCodes#CONFCAT_NAME_EMPTY}
	 * @throws {@link EntityNotFoundException} with error codes {@link DGCErrorCodes#CONFCAT_NOT_FOUND}
	 */
	void removeConfigurationCategory(String name);

	/**
	 * Get the {@link AttributeAndRelationConfigurationCategory} by its category name.
	 * 
	 * @param name The name that uniquely references the {@link AttributeAndRelationConfigurationCategory}
	 * @return The {@link AttributeAndRelationConfigurationCategory}
	 * @throws {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#CONFCAT_NAME_NULL},
	 *         {@link DGCErrorCodes#CONFCAT_NAME_EMPTY}
	 * @throws {@link EntityNotFoundException} with error codes {@link DGCErrorCodes#CONFCAT_NOT_FOUND}
	 */
	AttributeAndRelationTypesConfigurationCategory getAttributeAndRelationTypesConfigurationCategory(String name);

	/**
	 * @return All {@link AttributeAndRelationTypesConfigurationCategory}
	 */
	List<AttributeAndRelationTypesConfigurationCategory> getAllAttributeAndRelationTypesCategories();

	/**
	 * Change the order of the given {@link Representation} in the given
	 * {@link AttributeAndRelationConfigurationCategory} to the given position
	 * 
	 * @param name The name that uniquely references the {@link AttributeAndRelationConfigurationCategory}
	 * @param representationRId the resource id of the {@link Representation}
	 * @param position the new position index
	 * @return the updated {@link AttributeAndRelationConfigurationCategory}
	 * @throws {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#CONFCAT_NAME_NULL},
	 *         {@link DGCErrorCodes#CONFCAT_NAME_EMPTY}
	 * @throws {@link EntityNotFoundException} with error codes {@link DGCErrorCodes#CONFCAT_NOT_FOUND},
	 *         {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 * @throws {@link DGCException} with error codes {@link DGCErrorCodes#CONFCAT_COULD_NOT_CHANGE_ORDER}
	 */
	AttributeAndRelationTypesConfigurationCategory changeRepresentationOrderInAttributeAndRelationConfigurationCategory(
			String name, String representationRId, int position);

	/**
	 * Add a new {@link Representation} to the given {@link AttributeAndRelationConfigurationCategory}.
	 * 
	 * @param name The name that uniquely references the {@link AttributeAndRelationConfigurationCategory}
	 * @param representationRId the resource id of the {@link Representation}
	 * @return the updated {@link AttributeAndRelationConfigurationCategory}
	 * @throws {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#CONFCAT_NAME_NULL},
	 *         {@link DGCErrorCodes#CONFCAT_NAME_EMPTY}
	 * @throws {@link EntityNotFoundException} with error codes {@link DGCErrorCodes#CONFCAT_NOT_FOUND},
	 *         {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	AttributeAndRelationTypesConfigurationCategory addRepresentationToAttributeAndRelationConfigurationCategory(
			String name, String representationRId);

	/**
	 * Add a new {@link Representation} to the given {@link AttributeAndRelationConfigurationCategory} on the specified
	 * position.
	 * 
	 * @param name The name that uniquely references the {@link AttributeAndRelationConfigurationCategory}
	 * @param representationRId the resource id of the {@link Representation}
	 * @param position the new position index
	 * @return the updated {@link AttributeAndRelationConfigurationCategory}
	 * @throws {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#CONFCAT_NAME_NULL},
	 *         {@link DGCErrorCodes#CONFCAT_NAME_EMPTY}
	 * @throws {@link EntityNotFoundException} with error codes {@link DGCErrorCodes#CONFCAT_NOT_FOUND},
	 *         {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 * @throws {@link DGCException} with error codes {@link DGCErrorCodes#CONFCAT_COULD_NOT_ADD_ON_POSITIONG}
	 */
	AttributeAndRelationTypesConfigurationCategory addRepresentationToAttributeAndRelationConfigurationCategory(
			String name, String representationRId, int position);

	/**
	 * Change the description of the given {@link AttributeAndRelationConfigurationCategory}.
	 * 
	 * @param name The name that uniquely references the {@link AttributeAndRelationConfigurationCategory}
	 * @param description The description of the category. Can be null (optional)
	 * @return the updated {@link AttributeAndRelationConfigurationCategory}
	 * @throws {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#CONFCAT_NAME_NULL},
	 *         {@link DGCErrorCodes#CONFCAT_NAME_EMPTY}
	 * @throws {@link EntityNotFoundException} with error codes {@link DGCErrorCodes#CONFCAT_NOT_FOUND},
	 *         {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	AttributeAndRelationTypesConfigurationCategory changeAttributeAndRelationConfigurationCategoryDescription(
			String name, String description);
}
