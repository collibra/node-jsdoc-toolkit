package com.collibra.dgc.core.component.attribute.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.attribute.AttributeTypeComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.service.AttributeService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.util.Defense;

/**
 * {@link Attribute} {@code Type} API implementation.
 * 
 * @author pmalarme
 * 
 */
@Service
public class AttributeTypeComponentImpl implements AttributeTypeComponent {

	@Autowired
	private AttributeService attributeService;

	@Autowired
	private RepresentationService representationService;

	@Autowired
	private RepresentationFactory representationFactory;

	@Override
	@Transactional
	public Term addStringAttributeType(String signifier, String description) {

		Defense.notEmpty(signifier, DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_NULL,
				DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_EMPTY, "signifier");

		return attributeService.createStringAttributeType(signifier, description);
	}

	@Override
	@Transactional
	public Term addValueListAttributeType(String signifier, String description, boolean multipleValue,
			Collection<String> allowedValues) {

		Defense.notEmpty(signifier, DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_NULL,
				DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_EMPTY, "signifier");
		Defense.notEmpty(allowedValues, DGCErrorCodes.ATTRIBUTE_TYPE_VALUE_LIST_NULL,
				DGCErrorCodes.ATTRIBUTE_TYPE_VALUE_LIST_EMPTY, "allowedValues");

		if (multipleValue)
			return attributeService.createMultiValueListAttributeType(signifier, description, allowedValues);
		else
			return attributeService.createSingleValueListAttributeType(signifier, description, allowedValues);
	}

	@Override
	@Transactional
	public Term getAttributeType(String rId) {

		Defense.notEmpty(rId, DGCErrorCodes.ATTRIBUTE_TYPE_ID_NULL, DGCErrorCodes.ATTRIBUTE_TYPE_ID_EMPTY, "rId");

		return attributeService.getAttributeTypeWithError(rId);
	}

	@Override
	@Transactional
	public Term getAttributeTypeBySignifier(String signifier) {

		Defense.notEmpty(signifier, DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_NULL,
				DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_EMPTY, "signifier");

		Term label = attributeService.findAttributeTypeLabel(signifier);

		if (label == null) {

			// Check the meta attribute types
			label = getDefinitionAttributeType();
			if (label.getSignifier().equals(signifier))
				return label;

			label = getDescriptionAttributeType();
			if (label.getSignifier().equals(signifier))
				return label;

			label = getExampleAttributeType();
			if (label.getSignifier().equals(signifier))
				return label;

			label = getNoteAttributeType();
			if (label.getSignifier().equals(signifier))
				return label;

			throw new EntityNotFoundException(DGCErrorCodes.ATTRIBUTE_TYPE_NOT_FOUND_SIGNIFIER, signifier);
		}

		return label;
	}

	@Override
	@Transactional
	public Term getDefinitionAttributeType() {
		return attributeService.findMetaDefinition();
	}

	@Override
	@Transactional
	public Term getDescriptionAttributeType() {
		return attributeService.findMetaDescription();
	}

	@Override
	@Transactional
	public Term getExampleAttributeType() {
		return attributeService.findMetaExample();
	}

	@Override
	@Transactional
	public Term getNoteAttributeType() {
		return attributeService.findMetaNote();
	}

	@Override
	@Transactional
	public String getAttributeTypeKind(String rId) {

		return attributeService.getAttributeTypeKind(getAttributeType(rId));
	}

	@Override
	@Transactional
	public StringAttribute getAttributeTypeDescription(String rId) {

		List<StringAttribute> descriptions = attributeService.findDescriptionsForRepresentation(getAttributeType(rId));

		if (descriptions == null || descriptions.isEmpty())
			return null;

		return descriptions.iterator().next();
	}

	@Override
	@Transactional
	public Collection<String> getAllowedValues(String rId) {
		return attributeService.findConstraintValues(getAttributeType(rId));
	}

	@Override
	@Transactional
	public Collection<Term> getAttributeTypes() {

		return attributeService.findAttributeTypeLabels();
	}

	@Override
	@Transactional
	public Term changeSignifier(String rId, String newSignifier) {

		Defense.notEmpty(newSignifier, DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_NULL,
				DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_EMPTY, "newSignifier");

		return representationService.changeSignifier(getAttributeType(rId), newSignifier);
	}

	@Override
	@Transactional
	public StringAttribute changeDescription(String rId, String newDescription) {

		StringAttribute existingDescription = getAttributeTypeDescription(rId);

		// if there is no description, add one
		if (existingDescription == null) {

			Term attributeType = getAttributeType(rId);

			StringAttribute description = representationFactory.makeStringAttribute(
					attributeService.findMetaDescription(), attributeType, newDescription);

			return (StringAttribute) attributeService.save(description);

		} else {

			// else, update the existing one
			return attributeService.update(getAttributeTypeDescription(rId), newDescription);
		}
	}

	@Override
	@Transactional
	public Collection<String> changeAllowedValues(String rId, Collection<String> newAllowedValues) {

		Defense.notEmpty(newAllowedValues, DGCErrorCodes.ATTRIBUTE_TYPE_VALUE_LIST_NULL,
				DGCErrorCodes.ATTRIBUTE_TYPE_VALUE_LIST_EMPTY, "newAllowedValues");

		return attributeService.setConstraintValues(getAttributeType(rId), newAllowedValues);
	}

	@Override
	@Transactional
	public void removeAttributeType(String rId) {
		attributeService.remove(getAttributeType(rId));
	}
}
