/**
 * 
 */
package com.collibra.dgc.core.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.configuration.AttributeAndRelationTypesConfigurationCategoryDao;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.DGCException;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.configuration.AttributeAndRelationTypesConfigurationCategory;
import com.collibra.dgc.core.model.configuration.ConfigurationCategory;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.service.ConfigurationCategoryService;

/**
 * @author fvdmaele
 * 
 */
@Service
public class ConfigurationCategoryServiceImpl implements ConfigurationCategoryService {
	private final Logger log = LoggerFactory.getLogger(ConfigurationCategoryServiceImpl.class);

	@Autowired
	AttributeAndRelationTypesConfigurationCategoryDao arConfDao;

	@Override
	public AttributeAndRelationTypesConfigurationCategory saveAttributeAndRelationTypeCategory(
			AttributeAndRelationTypesConfigurationCategory cat) {
		try {
			return arConfDao.save(cat);
		} catch (org.hibernate.exception.ConstraintViolationException e) {
			log.error(e.getMessage());
			if (e.getErrorCode() == 1062)
				throw new IllegalArgumentException(e, DGCErrorCodes.CONFCAT_ALREADY_EXISTS, cat.getName());
			else
				throw new DGCException(e, DGCErrorCodes.CONFCAT_SAVE_ERROR, cat.getName());
		}
	}

	@Override
	public void removeConfigurationCategory(ConfigurationCategory cat) {
		arConfDao.remove(cat);
	}

	@Override
	public AttributeAndRelationTypesConfigurationCategory getCategoryByName(String catName) {
		return arConfDao.getCategoryByName(catName);
	}

	@Override
	public AttributeAndRelationTypesConfigurationCategory getCategoryWithError(String catName) {
		AttributeAndRelationTypesConfigurationCategory cat = getCategoryByName(catName);
		if (cat == null) {
			String message = "Term with resource id '" + catName + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.CONFCAT_NOT_FOUND, catName);
		}
		return cat;
	}

	@Override
	public List<AttributeAndRelationTypesConfigurationCategory> findAttributeAndRelationTypesCategoryByRepresentation(
			Representation rep) {
		return arConfDao.findByRepresentation(rep);
	}

	@Override
	public List<AttributeAndRelationTypesConfigurationCategory> findAllAttributeAndRelationTypesCategories() {
		return arConfDao.findAll();
	}
}
