/**
 * 
 */
package com.collibra.dgc.core.component.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.collibra.dgc.core.component.ConfigurationCategoryComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.DGCException;
import com.collibra.dgc.core.model.configuration.AttributeAndRelationTypesConfigurationCategory;
import com.collibra.dgc.core.model.configuration.impl.AttributeAndRelationTypesConfigurationCategoryImpl;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.service.ConfigurationCategoryService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.util.Defense;

/**
 * @author fvdmaele
 * 
 */
@Component
public class ConfigurationCategoryComponentImpl implements ConfigurationCategoryComponent {

	@Autowired
	RepresentationService representationService;

	@Autowired
	ConfigurationCategoryService confCategoryService;

	@Override
	public AttributeAndRelationTypesConfigurationCategory addAttributeAndRelationTypesConfigurationCategory(
			String name, String description, List<String> representationRIds) {

		Defense.notEmpty(name, DGCErrorCodes.CONFCAT_NAME_NULL, DGCErrorCodes.CONFCAT_NAME_EMPTY, "name");

		List<Representation> reprs = new LinkedList<Representation>();
		if (representationRIds != null)
			for (String tRId : representationRIds)
				reprs.add(representationService.getRepresentationWithError(tRId));

		AttributeAndRelationTypesConfigurationCategory cat = new AttributeAndRelationTypesConfigurationCategoryImpl(
				name, description, reprs);
		return confCategoryService.saveAttributeAndRelationTypeCategory(cat);
	}

	@Override
	public void removeConfigurationCategory(String name) {

		Defense.notEmpty(name, DGCErrorCodes.CONFCAT_NAME_NULL, DGCErrorCodes.CONFCAT_NAME_EMPTY, "name");

		AttributeAndRelationTypesConfigurationCategory cat = confCategoryService.getCategoryWithError(name);

		confCategoryService.removeConfigurationCategory(cat);
	}

	@Override
	public AttributeAndRelationTypesConfigurationCategory getAttributeAndRelationTypesConfigurationCategory(String name) {
		Defense.notEmpty(name, DGCErrorCodes.CONFCAT_NAME_NULL, DGCErrorCodes.CONFCAT_NAME_EMPTY, "name");

		return confCategoryService.getCategoryWithError(name);
	}

	@Override
	public List<AttributeAndRelationTypesConfigurationCategory> getAllAttributeAndRelationTypesCategories() {
		return confCategoryService.findAllAttributeAndRelationTypesCategories();
	}

	@Override
	public AttributeAndRelationTypesConfigurationCategory changeRepresentationOrderInAttributeAndRelationConfigurationCategory(
			String name, String representationRId, int position) {

		Defense.notEmpty(name, DGCErrorCodes.CONFCAT_NAME_NULL, DGCErrorCodes.CONFCAT_NAME_EMPTY, "name");
		Defense.notEmpty(representationRId, DGCErrorCodes.REPRESENTATION_ID_NULL,
				DGCErrorCodes.REPRESENTATION_ID_EMPTY, "representationRId");

		AttributeAndRelationTypesConfigurationCategory cat = confCategoryService.getCategoryWithError(name);
		Representation r = representationService.getRepresentationWithError(representationRId);

		if (!cat.changeAttributeOrRelationTypeOrder(r, position))
			throw new DGCException(DGCErrorCodes.CONFCAT_COULD_NOT_CHANGE_ORDER, name, r.verbalise(), position);

		return confCategoryService.saveAttributeAndRelationTypeCategory(cat);
	}

	@Override
	public AttributeAndRelationTypesConfigurationCategory addRepresentationToAttributeAndRelationConfigurationCategory(
			String name, String representationRId) {

		Defense.notEmpty(name, DGCErrorCodes.CONFCAT_NAME_NULL, DGCErrorCodes.CONFCAT_NAME_EMPTY, "name");
		Defense.notEmpty(representationRId, DGCErrorCodes.REPRESENTATION_ID_NULL,
				DGCErrorCodes.REPRESENTATION_ID_EMPTY, "representationRId");

		AttributeAndRelationTypesConfigurationCategory cat = confCategoryService.getCategoryWithError(name);
		Representation r = representationService.getRepresentationWithError(representationRId);

		cat.addAttributeOrRelationType(r);
		return confCategoryService.saveAttributeAndRelationTypeCategory(cat);
	}

	@Override
	public AttributeAndRelationTypesConfigurationCategory addRepresentationToAttributeAndRelationConfigurationCategory(
			String name, String representationRId, int position) {

		Defense.notEmpty(name, DGCErrorCodes.CONFCAT_NAME_NULL, DGCErrorCodes.CONFCAT_NAME_EMPTY, "name");
		Defense.notEmpty(representationRId, DGCErrorCodes.REPRESENTATION_ID_NULL,
				DGCErrorCodes.REPRESENTATION_ID_EMPTY, "representationRId");

		AttributeAndRelationTypesConfigurationCategory cat = confCategoryService.getCategoryWithError(name);
		Representation r = representationService.getRepresentationWithError(representationRId);

		try {
			cat.addAttributeOrRelationType(position, r);
		} catch (Exception e) {
			throw new DGCException(e, DGCErrorCodes.CONFCAT_COULD_NOT_ADD_ON_POSITIONG, name, r.verbalise(), position);
		}
		return confCategoryService.saveAttributeAndRelationTypeCategory(cat);
	}

	@Override
	public AttributeAndRelationTypesConfigurationCategory changeAttributeAndRelationConfigurationCategoryDescription(
			String name, String description) {

		Defense.notEmpty(name, DGCErrorCodes.CONFCAT_NAME_NULL, DGCErrorCodes.CONFCAT_NAME_EMPTY, "name");

		AttributeAndRelationTypesConfigurationCategory cat = getAttributeAndRelationTypesConfigurationCategory(name);

		((AttributeAndRelationTypesConfigurationCategoryImpl) cat).setDescription(description);

		return confCategoryService.saveAttributeAndRelationTypeCategory(cat);

	}

}
