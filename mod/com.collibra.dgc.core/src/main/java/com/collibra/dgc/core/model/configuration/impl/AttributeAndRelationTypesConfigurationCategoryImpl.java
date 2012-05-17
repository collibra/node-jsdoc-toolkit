/**
 * 
 */
package com.collibra.dgc.core.model.configuration.impl;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.collibra.dgc.core.model.configuration.AttributeAndRelationTypesConfigurationCategory;
import com.collibra.dgc.core.model.representation.Representation;

/**
 * @author fvdmaele
 * 
 */
@Entity
@DiscriminatorValue(value = "attr_rel")
public class AttributeAndRelationTypesConfigurationCategoryImpl extends ConfigurationCategoryImpl implements
		AttributeAndRelationTypesConfigurationCategory {
	
	public AttributeAndRelationTypesConfigurationCategoryImpl() {
	}

	public AttributeAndRelationTypesConfigurationCategoryImpl(String name, String description, List<Representation> representations) {
		super(name, description, representations);
	}

	public AttributeAndRelationTypesConfigurationCategoryImpl(String name, List<Representation> representations) {
		super(name, representations);
	}

	public AttributeAndRelationTypesConfigurationCategoryImpl(String name) {
		super(name);
	}
	
	@Override
	@Transient
	public List<Representation> getAttributeAndRelationTypes() {
		return super.getRepresentations();
	}
	
	@Override
	@Transient
	public void addAttributeOrRelationType(Representation r) {
		addRepresentation(r);
	}
	
	@Override
	@Transient
	public void addAttributeOrRelationType(int index, Representation r) {
		addRepresentation(index, r);
	}
	
	@Override
	@Transient
	public boolean changeAttributeOrRelationTypeOrder(Representation r, int index) {
		return changeOrder(r, index);
	}
}
