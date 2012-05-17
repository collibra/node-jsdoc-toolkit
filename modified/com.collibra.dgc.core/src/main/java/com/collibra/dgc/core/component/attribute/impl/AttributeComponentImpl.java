package com.collibra.dgc.core.component.attribute.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.attribute.AttributeComponent;
import com.collibra.dgc.core.component.attribute.AttributeTypeComponent;
import com.collibra.dgc.core.component.representation.RepresentationComponent;
import com.collibra.dgc.core.dao.AttributeDao;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.MultiValueListAttribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.service.AttributeService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.impl.ServiceUtility;
import com.collibra.dgc.core.util.Defense;

/**
 * {@link Attribute} API implementation.
 * 
 * @author pmalarme
 * 
 */
@Service
public class AttributeComponentImpl implements AttributeComponent {

	@Autowired
	AttributeDao attributeDao;

	@Autowired
	AttributeService attributeService;

	@Autowired
	RepresentationService representationService;

	@Autowired
	@Qualifier("RepresentationComponentImpl")
	RepresentationComponent representationComponent;

	@Autowired
	AttributeTypeComponent attributeTypeComponent;

	@Autowired
	RepresentationFactory representationFactory;

	@Override
	@Transactional
	public Attribute addAttribute(String representationRId, String labelRId, Collection<String> values) {

		String kind = attributeTypeComponent.getAttributeTypeKind(labelRId);

		if (kind.equals(StringAttribute.class.getSimpleName()))
			return addStringAttribute(representationRId, labelRId, getSingleStringValue(values));

		else if (kind.equals(SingleValueListAttribute.class.getSimpleName()))
			return addSingleValueListAttribute(representationRId, labelRId, getSingleStringValue(values));

		else if (kind.equals(MultiValueListAttribute.class.getSimpleName()))
			return addMultiValueListAttribute(representationRId, labelRId, values);

		// TODO Pierre: Change to the right DGCErrorCodes
		else
			throw new IllegalArgumentException(DGCErrorCodes.ATTRIBUTE_TYPE_KIND_UNKNOWN, kind);
	}

	@Override
	@Transactional
	public StringAttribute addStringAttribute(String representationRId, String labelRId, String longExpression) {

		// The long expression of a StringAttribute can be null or empty

		final Representation representation = representationComponent.getRepresentation(representationRId);
		final Term label = attributeTypeComponent.getAttributeType(labelRId);

		final StringAttribute stringAttribute = representationFactory.makeStringAttribute(label, representation,
				longExpression);

		return (StringAttribute) attributeService.save(stringAttribute);
	}

	@Override
	@Transactional
	public SingleValueListAttribute addSingleValueListAttribute(String representationRId, String labelRId, String value) {

		// The value of a SingleValueListAttribute can be null or empty

		// The constraint value is not checked cause we allow the user to add a value that is not in the allowed one

		final Representation representation = representationComponent.getRepresentation(representationRId);
		final Term label = attributeTypeComponent.getAttributeType(labelRId);

		final SingleValueListAttribute singleValueListAttribute = representationFactory.makeSingleValueListAttribute(
				label, representation, value);

		return (SingleValueListAttribute) attributeService.save(singleValueListAttribute);
	}

	@Override
	@Transactional
	public MultiValueListAttribute addMultiValueListAttribute(String representationRId, String labelRId,
			Collection<String> values) {

		// The values of a MultiValueListAttribute can be null or empty

		// The constraint values are not checked cause we allow the user to add a value that is not in the allowed one

		final Representation representation = representationComponent.getRepresentation(representationRId);
		final Term label = attributeTypeComponent.getAttributeType(labelRId);

		final MultiValueListAttribute multiValueListAttribute = representationFactory.makeMultiValueListAttribute(
				label, representation, values);

		return (MultiValueListAttribute) attributeService.save(multiValueListAttribute);
	}

	@Override
	@Transactional
	public StringAttribute addDefinition(String representationRId, String definitionLongExpression) {

		// The values of the definition can be null or empty

		final Representation representation = representationComponent.getRepresentation(representationRId);

		final StringAttribute stringAttribute = representationFactory.makeStringAttribute(
				attributeService.findMetaDefinition(), representation, definitionLongExpression);

		return (StringAttribute) attributeService.save(stringAttribute);
	}

	@Override
	@Transactional
	public StringAttribute addDescription(String representationRId, String descriptionLongExpression) {

		// The values of the description can be null or empty

		final Representation representation = representationComponent.getRepresentation(representationRId);

		final StringAttribute stringAttribute = representationFactory.makeStringAttribute(
				attributeService.findMetaDescription(), representation, descriptionLongExpression);

		return (StringAttribute) attributeService.save(stringAttribute);
	}

	@Override
	@Transactional
	public StringAttribute addExample(String representationRId, String exampleLongExpression) {

		// The values of the example can be null or empty

		final Representation representation = representationComponent.getRepresentation(representationRId);

		final StringAttribute stringAttribute = representationFactory.makeStringAttribute(
				attributeService.findMetaExample(), representation, exampleLongExpression);

		return (StringAttribute) attributeService.save(stringAttribute);
	}

	@Override
	@Transactional
	public StringAttribute addNote(String representationRId, String noteLongExpression) {

		// The values of the note can be null or empty

		final Representation representation = representationComponent.getRepresentation(representationRId);

		final StringAttribute stringAttribute = representationFactory.makeStringAttribute(
				attributeService.findMetaNote(), representation, noteLongExpression);

		return (StringAttribute) attributeService.save(stringAttribute);
	}

	@Override
	@Transactional
	public Attribute getAttribute(String rId) {

		Defense.notEmpty(rId, DGCErrorCodes.ATTRIBUTE_ID_NULL, DGCErrorCodes.ATTRIBUTE_ID_EMPTY, "rId");

		return attributeService.getAttributeWithError(rId);
	}

	@Override
	@Transactional
	public StringAttribute getStringAttribute(String stringAttributeRId) {

		Defense.notEmpty(stringAttributeRId, DGCErrorCodes.ATTRIBUTE_ID_NULL, DGCErrorCodes.ATTRIBUTE_ID_EMPTY,
				"stringAttribute");

		return attributeService.getStringAttributeWithError(stringAttributeRId);
	}

	@Override
	@Transactional
	public SingleValueListAttribute getSingleValueListAttribute(String singleValueListAttributeRId) {

		Defense.notEmpty(singleValueListAttributeRId, DGCErrorCodes.ATTRIBUTE_ID_NULL,
				DGCErrorCodes.ATTRIBUTE_ID_EMPTY, "singleValueListAttributeRId");

		return attributeService.getSingleValueListAttributeWithError(singleValueListAttributeRId);
	}

	@Override
	@Transactional
	public MultiValueListAttribute getMultiValueListAttribute(String multiValueListAttributeRId) {

		Defense.notEmpty(multiValueListAttributeRId, DGCErrorCodes.ATTRIBUTE_ID_NULL, DGCErrorCodes.ATTRIBUTE_ID_EMPTY,
				"multiValueListAttributeRId");

		return attributeService.getMultiValueListAttributeWithError(multiValueListAttributeRId);
	}

	@Override
	@Transactional
	public Collection<Attribute> getAttributesOfTypeForRepresentation(String representationRId, String labelRId) {

		final Representation representation = representationComponent.getRepresentation(representationRId);
		final Term label = attributeTypeComponent.getAttributeType(labelRId);

		return attributeService.findAttributesForRepresentation(label, representation);
	}

	@Override
	@Transactional
	public Collection<StringAttribute> getDefinitionsForRepresentation(String representationRId) {

		final Representation representation = representationComponent.getRepresentation(representationRId);

		return attributeService.findDefinitionsForRepresentation(representation);
	}

	@Override
	@Transactional
	public Collection<StringAttribute> getDescriptionsForRepresentation(String representationRId) {

		final Representation representation = representationComponent.getRepresentation(representationRId);

		return attributeService.findDescriptionsForRepresentation(representation);
	}

	@Override
	@Transactional
	public Collection<StringAttribute> getExamplesForRepresentation(String representationRId) {

		final Representation representation = representationComponent.getRepresentation(representationRId);

		return attributeService.findExamplesForRepresentation(representation);
	}

	@Override
	@Transactional
	public Collection<StringAttribute> getNotesForRepresentation(String representationRId) {

		final Representation representation = representationComponent.getRepresentation(representationRId);

		return attributeService.findNotesForRepresentation(representation);
	}

	@Override
	@Transactional
	public Map<String, Collection<Attribute>> getAttributesOfTypesForRepresentation(String representationRId,
			String... labelRIds) {

		final Representation representation = representationComponent.getRepresentation(representationRId);

		int size = labelRIds.length;
		Term[] labels = new Term[size];

		for (int i = 0; i < size; i++) {

			labels[i] = attributeTypeComponent.getAttributeType(labelRIds[i]);
		}

		return attributeService.findAttributesForRepresentation(representation, labels);
	}

	@Override
	@Transactional
	public Collection<Attribute> getAttributesOfTypesForRepresentationInCollection(String representationRId,
			String... labelRIds) {

		final Representation representation = representationComponent.getRepresentation(representationRId);

		int size = labelRIds.length;
		Term[] labels = new Term[size];

		for (int i = 0; i < size; i++) {

			labels[i] = attributeTypeComponent.getAttributeType(labelRIds[i]);
		}

		return attributeDao.findAttributesByTypesAndOwner(labels, representation);
	}

	@Override
	@Transactional
	public Attribute changeAttribute(String rId, Collection<String> values) {

		Attribute deproxyAttribute = ServiceUtility.deproxy(getAttribute(rId), Attribute.class);

		if (deproxyAttribute instanceof StringAttribute)
			return attributeService.update((StringAttribute) deproxyAttribute, getSingleStringValue(values));

		else if (deproxyAttribute instanceof SingleValueListAttribute)
			return attributeService.update((SingleValueListAttribute) deproxyAttribute, getSingleStringValue(values));

		else if (deproxyAttribute instanceof MultiValueListAttribute)
			return attributeService.update((MultiValueListAttribute) deproxyAttribute, values);

		else
			// TODO Pierre: set the right DGCErrorCodes cf ConstraintChecker
			throw new IllegalArgumentException(DGCErrorCodes.ARGUMENT_INVALID);
	}

	@Override
	@Transactional
	public StringAttribute changeStringAttributeLongExpression(String stringAttributeRId, String newLongExpression) {

		return attributeService.update(getStringAttribute(stringAttributeRId), newLongExpression);
	}

	@Override
	@Transactional
	public SingleValueListAttribute changeSingleValueListAttributeValue(String singleValueListAttributeRId,
			String newValue) {

		return attributeService.update(getSingleValueListAttribute(singleValueListAttributeRId), newValue);
	}

	@Override
	@Transactional
	public MultiValueListAttribute changeMultiValueListAttributeValues(String multiValueListAttributeRId,
			Collection<String> newValues) {

		return attributeService.update(getMultiValueListAttribute(multiValueListAttributeRId), newValues);
	}

	@Override
	@Transactional
	public void removeAttribute(String rId) {

		attributeService.remove(getAttribute(rId));
	}

	@Override
	@Transactional
	public boolean validateConstraint(String rId) {

		Attribute deproxyAttribute = ServiceUtility.deproxy(getAttribute(rId), Attribute.class);

		if (deproxyAttribute instanceof SingleValueListAttribute)
			return attributeService.checkValueConstraint((SingleValueListAttribute) deproxyAttribute);

		else if (deproxyAttribute instanceof MultiValueListAttribute)
			return attributeService.checkValueConstraint((MultiValueListAttribute) deproxyAttribute);

		// No constraint => return true
		else
			return true;
	}

	@Override
	@Transactional
	public boolean validateSingleValueListConstraint(String singleValueListAttributeRId) {

		return attributeService.checkValueConstraint(getSingleValueListAttribute(singleValueListAttributeRId));
	}

	@Override
	@Transactional
	public boolean validateMultiValueListConstraint(String multiValueListAttributeRId) {

		return attributeService.checkValueConstraint(getMultiValueListAttribute(multiValueListAttributeRId));
	}

	/**
	 * Get the first value or null if no values are set.
	 */
	private String getSingleStringValue(Collection<String> values) {

		if (values == null || values.isEmpty())
			return null;
		else
			return values.iterator().next();
	}
}
