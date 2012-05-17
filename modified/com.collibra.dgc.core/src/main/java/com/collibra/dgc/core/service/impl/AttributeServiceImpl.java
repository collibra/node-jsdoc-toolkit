package com.collibra.dgc.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.AttributeDao;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.DateTimeAttribute;
import com.collibra.dgc.core.model.representation.MultiValueListAttribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.impl.MultiValueListAttributeImpl;
import com.collibra.dgc.core.model.representation.impl.SingleValueListAttributeImpl;
import com.collibra.dgc.core.model.representation.impl.StringAttributeImpl;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.events.AttributeEventData;
import com.collibra.dgc.core.observer.events.EventType;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.security.authorization.AuthorizationHelper;
import com.collibra.dgc.core.service.AttributeService;
import com.collibra.dgc.core.service.MeaningService;
import com.collibra.dgc.core.service.RepresentationService;

@Service
public class AttributeServiceImpl extends AbstractService implements AttributeService {
	private static final Logger log = LoggerFactory.getLogger(AttributeServiceImpl.class);

	@Autowired
	private RepresentationFactory representationFactory;
	@Autowired
	private ObjectTypeDao objectTypeDao;
	@Autowired
	private AttributeDao attributeDao;
	@Autowired
	private AuthorizationHelper authorizationHelper;
	@Autowired
	private RepresentationService representationService;
	@Autowired
	private RepresentationServiceHelper representationServiceHelper;
	@Autowired
	private MeaningService meaningService;
	@Autowired
	private ConstraintChecker constraintChecker;

	@Override
	public Term findMetaSingleStaticListType() {
		ObjectType ot = objectTypeDao.findById(MeaningConstants.META_SINGLE_STATIC_LIST_TYPE_UUID);
		return representationService
				.findPreferredTerm(ot, representationService.findSbvrCollibraExtensionsVocabulary());
	}

	@Override
	public Term findMetaMultiStaticListType() {
		ObjectType ot = objectTypeDao.findById(MeaningConstants.META_MULTI_STATIC_LIST_TYPE_UUID);
		return representationService
				.findPreferredTerm(ot, representationService.findSbvrCollibraExtensionsVocabulary());
	}

	@Override
	public Term findMetaStaticListAllowedValuesType() {
		ObjectType ot = objectTypeDao.findById(MeaningConstants.META_STATIC_LIST_ALLOWED_VALUES_TYPE_UUID);
		return representationService
				.findPreferredTerm(ot, representationService.findSbvrCollibraExtensionsVocabulary());
	}

	@Override
	public Term findMetaStringAttributeTypeLabel() {
		ObjectType ot = objectTypeDao.findById(MeaningConstants.META_STRING_TYPE_UUID);
		return representationService
				.findPreferredTerm(ot, representationService.findSbvrCollibraExtensionsVocabulary());
	}

	@Override
	public Term findMetaDateTimeAttributeTypeLabel() {
		ObjectType ot = objectTypeDao.findById(MeaningConstants.META_DATE_TIME_TYPE_UUID);
		return representationService
				.findPreferredTerm(ot, representationService.findSbvrCollibraExtensionsVocabulary());
	}

	@Override
	public Term findMetaDescription() {
		return representationService.findPreferredTerm(meaningService.findMetaDescription(),
				representationService.findSbvrBusinessVocabulary());
	}

	@Override
	public Term findMetaDefinition() {
		return representationService.findPreferredTerm(meaningService.findMetaDefinition(),
				representationService.findSbvrMeaningAndRepresentationVocabulary());
	}

	@Override
	public Term findMetaExample() {
		return representationService.findPreferredTerm(meaningService.findMetaExample(),
				representationService.findSbvrBusinessVocabulary());
	}

	@Override
	public Term findMetaNote() {
		return representationService.findPreferredTerm(meaningService.findMetaNote(),
				representationService.findSbvrBusinessVocabulary());
	}

	@Override
	public Term findMetaCandidateStatus() {
		return representationService.findPreferredTerm(objectTypeDao.getMetaCandidateStatusType(),
				representationService.findStatusesVocabulary());
	}

	@Override
	public Term getAttributeTypeWithError(final String rId) {

		Term attributeTypeLabelTerm = representationService.getTermWithError(rId);
		constraintChecker.checkIsAttributeTypeLabelTermConstraint(attributeTypeLabelTerm);
		constraintChecker.checkAttributeTypeConceptTypeGeneralConcept(attributeTypeLabelTerm);

		return attributeTypeLabelTerm;
	}

	@Override
	public String getAttributeTypeKind(final Term attributeTypeLabel) {

		String attributeTypeOTTypeRId = attributeTypeLabel.getObjectType().getType().getId();

		if (attributeTypeOTTypeRId.equals(MeaningConstants.META_STRING_TYPE_UUID))
			return StringAttribute.class.getSimpleName();

		else if (attributeTypeOTTypeRId.equals(MeaningConstants.META_SINGLE_STATIC_LIST_TYPE_UUID))
			return SingleValueListAttribute.class.getSimpleName();

		else if (attributeTypeOTTypeRId.equals(MeaningConstants.META_MULTI_STATIC_LIST_TYPE_UUID))
			return MultiValueListAttribute.class.getSimpleName();

		// else if (attributeTypeOTTypeRId.equals(MeaningConstants.META_DATE_TIME_TYPE_UUID))
		// return DateTimeAttribute.class.getSimpleName();

		throw new IllegalArgumentException(DGCErrorCodes.ATTRIBUTE_TYPE_KIND_UNKNOWN, attributeTypeLabel
				.getObjectType().getType().verbalise());

	}

	@Override
	public StringAttribute getStringAttribute(String rId) {
		return attributeDao.getStringAttribute(rId);
	}

	@Override
	public StringAttribute getStringAttributeWithError(String resourceId) {
		StringAttribute att = getStringAttribute(resourceId);
		if (att == null) {
			String message = "String Attribute with resource id '" + resourceId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.STRING_ATTRIBUTE_NOT_FOUND_ID, resourceId);
		}

		return att;
	}

	@Override
	public DateTimeAttribute getDateTimeAttribute(String rId) {
		return attributeDao.getDateTimeAttribute(rId);
	}

	@Override
	public DateTimeAttribute getDateTimeAttributeWithError(String resourceId) {
		DateTimeAttribute att = getDateTimeAttribute(resourceId);
		if (att == null) {
			String message = "Date Time Attribute with resource id '" + resourceId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.DATE_TIME_ATTRIBUTE_NOT_FOUND_ID, resourceId);
		}

		return att;
	}

	@Override
	public Attribute getAttribute(String rId) {
		return attributeDao.findById(rId);
	}

	@Override
	public MultiValueListAttribute getMultiValueListAttribute(String rId) {
		return attributeDao.getMultiValueListAttribute(rId);
	}

	@Override
	public MultiValueListAttribute getMultiValueListAttributeWithError(String resourceId) {
		MultiValueListAttribute att = getMultiValueListAttribute(resourceId);
		if (att == null) {
			String message = "ValueList Attribute with resource id '" + resourceId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.MVL_ATTRIBUTE_NOT_FOUND_ID, resourceId);
		}

		return att;
	}

	@Override
	public SingleValueListAttribute getSingleValueListAttribute(String rId) {
		return attributeDao.getSingleValueListAttribute(rId);
	}

	@Override
	public SingleValueListAttribute getSingleValueListAttributeWithError(String resourceId) {
		SingleValueListAttribute att = getSingleValueListAttribute(resourceId);
		if (att == null) {
			String message = "ValueList Attribute with resource id '" + resourceId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.SVL_ATTRIBUTE_NOT_FOUND_ID, resourceId);
		}

		return att;
	}

	@Override
	public Term findAttributeTypeLabel(String labelSignifier) {

		final Vocabulary vocabulary = representationService.findAttributeTypesVocabulary();
		Term label = vocabulary.getTerm(labelSignifier);
		if (label == null) {
			return null;
		}

		return label;
	}

	@Override
	public Term createStringAttributeType(String attributeTypeSignifier, String attributeTypeDescription) {

		final Vocabulary vocabulary = representationService.findAttributeTypesVocabulary();
		Term defaultLabel = createLabel(vocabulary, attributeTypeSignifier, findMetaStringAttributeTypeLabel()
				.getObjectType(), attributeTypeDescription);

		defaultLabel = representationService.saveTerm(defaultLabel);
		return defaultLabel;
	}

	@Override
	public Term createDateTimeAttributeType(String attributeTypeSignifier, String attributeTypeDescription) {

		final Vocabulary vocabulary = representationService.findAttributeTypesVocabulary();
		Term defaultLabel = createLabel(vocabulary, attributeTypeSignifier, findMetaDateTimeAttributeTypeLabel()
				.getObjectType(), attributeTypeDescription);

		defaultLabel = representationService.saveTerm(defaultLabel);
		return defaultLabel;
	}

	@Override
	public Term createSingleValueListAttributeType(String attributeTypeSignifier, String attributeTypeDescription,
			Collection<String> allowedValues) {

		final Vocabulary vocabulary = representationService.findAttributeTypesVocabulary();
		Term defaultLabel = createLabel(vocabulary, attributeTypeSignifier, findMetaSingleStaticListType()
				.getObjectType(), attributeTypeDescription);

		Term allowedValuesLabel = findMetaStaticListAllowedValuesType();

		representationFactory.makeMultiValueListAttribute(allowedValuesLabel, defaultLabel, allowedValues);

		defaultLabel = representationService.saveTerm(defaultLabel);
		return defaultLabel;
	}

	@Override
	public Term createMultiValueListAttributeType(String attributeTypeSignifier, String attributeTypeDescription,
			Collection<String> allowedValues) {

		final Vocabulary vocabulary = representationService.findAttributeTypesVocabulary();
		Term defaultLabel = createLabel(vocabulary, attributeTypeSignifier, findMetaMultiStaticListType()
				.getObjectType(), attributeTypeDescription);

		Term allowedValuesLabel = findMetaStaticListAllowedValuesType();

		representationFactory.makeMultiValueListAttribute(allowedValuesLabel, defaultLabel, allowedValues);

		defaultLabel = representationService.saveTerm(defaultLabel);
		return defaultLabel;
	}

	@Override
	public Term findAttributeLabel(final String attributeLabelName) {
		final List<Term> labels = findAttributeTypeLabels();
		for (Term attrLabel : labels) {
			if (attrLabel.verbalise().equals(attributeLabelName)) {
				return attrLabel;
			}
		}
		return null;
	}

	@Override
	public List<Attribute> findAttributesForRepresentation(final Term attributeLabel,
			final Representation representation) {

		return attributeDao.findAttributesByTypeAndOwner(attributeLabel, representation);
	}

	@Override
	public List<Attribute> findAttributesForRepresentation(final String attributeLabel,
			final Representation representation) {

		Term attrLabel = findAttributeLabel(attributeLabel);
		if (attrLabel == null) {
			throw new IllegalArgumentException("Could not find attribute type for name '" + attributeLabel + "'");
		}

		return attributeDao.findAttributesByTypeAndOwner(attrLabel, representation);

	}

	@Override
	public Map<String, Collection<Attribute>> findAttributesForRepresentation(Representation representation,
			Term... labels) {

		Map<String, Collection<Attribute>> mapToReturn = new HashMap<String, Collection<Attribute>>();

		// Get the list of attributes
		List<Attribute> attributes = attributeDao.findAttributesByTypesAndOwner(labels, representation);

		for (Attribute attribute : attributes) {

			String labelRId = attribute.getLabel().getId().toString();

			Collection<Attribute> attributeCollection = mapToReturn.get(labelRId);

			if (attributeCollection == null) {

				attributeCollection = new ArrayList<Attribute>();
				mapToReturn.put(labelRId, attributeCollection);
			}

			attributeCollection.add(attribute);
		}

		return mapToReturn;
	}

	@Override
	public List<Term> findAttributeTypeLabels() {
		final List<Term> labels = new LinkedList<Term>();

		labels.add(findMetaDefinition());
		labels.add(findMetaDescription());
		labels.add(findMetaExample());

		labels.addAll(attributeDao.findAttributeTypeLabels());

		labels.add(findMetaNote());

		return labels;
	}

	@Override
	public List<Term> findAttributeTypeLabels(final Representation rep) {
		// TODO this filtering needs to be implemented
		return findAttributeTypeLabels();
	}

	@Override
	public Term findAttributeTypeLabel(ObjectType typeOT) {
		return representationService.findPreferredTerm(typeOT, representationService.findAttributeTypesVocabulary());
	}

	@Override
	public Collection<String> findConstraintValues(Term label) {
		return getAllowedValues(label);
	}

	@Override
	public Collection<String> setConstraintValues(Term label, Collection<String> values) {
		setAllowedValues(label, values);
		return values;
	}

	@Override
	public Term findTermForLabel(String label) {
		return representationService.findTermBySignifier(representationService.findSbvrCollibraExtensionsVocabulary(),
				label);
	}

	@Override
	public boolean checkValueConstraint(MultiValueListAttribute attribute) {
		Term label = attribute.getLabel();

		return checkValueConstraintInternal(label, attribute.getValues());
	}

	@Override
	public boolean checkValueConstraint(SingleValueListAttribute attribute) {
		Term label = attribute.getLabel();
		List<String> values = new LinkedList<String>();
		values.add(attribute.getValue());

		return checkValueConstraintInternal(label, values);
	}

	private boolean checkValueConstraintInternal(Term label, List<String> values) {

		// Get the MultiValueListAttribute that represents the allowed value of type of Attribute (both for
		// SingleValueListAttribute and MultiValueListAttribute)
		List<MultiValueListAttribute> vlAttributes = attributeDao.findMultiValueListAttributesByTypeAndOwner(
				findMetaStaticListAllowedValuesType(), label);

		if (vlAttributes.isEmpty())
			return false;

		List<String> allowedValues = vlAttributes.get(0).getValues();

		for (String value : values)
			if (!allowedValues.contains(value))
				return false;

		return true;
	}

	private Term createLabel(Vocabulary metaVocabulary, String defaultLabelSignifier, ObjectType labelTypeOT,
			String attributeDescription) {

		Term defaultLabel;

		if (labelTypeOT == null) {
			defaultLabel = representationFactory.makeTerm(metaVocabulary, defaultLabelSignifier);
		} else {
			defaultLabel = representationFactory.makeTermOfType(metaVocabulary, defaultLabelSignifier, labelTypeOT);
		}

		StringAttribute description = representationFactory.makeStringAttribute(findMetaDescription(), defaultLabel,
				attributeDescription);
		constraintChecker.checkConstraints(description);

		return defaultLabel;
	}

	private void setAllowedValues(Term representation, Collection<String> values) {
		// TODO other rights
		// Perform authorization check.
		// AuthorizationHelper.checkAuthorization(getCurrentUser(), semanticCommunity,
		// Permissions.COMMUNITY_CUSTOM_ATTRIBUTE_EDIT, MessageKeys.COMMUNITY_EDIT_CUSTOM_ATTRIBUTE_NO_PERMISSION);

		Term allowedValuesLabel = findMetaStaticListAllowedValuesType();
		List<MultiValueListAttribute> caValues = attributeDao.findMultiValueListAttributesByTypeAndOwner(
				allowedValuesLabel, representation);

		if (caValues == null || caValues.size() == 0) {
			representationFactory.makeMultiValueListAttribute(allowedValuesLabel, representation, values);
		} else {
			MultiValueListAttributeImpl caAttribute = (MultiValueListAttributeImpl) caValues.get(0);
			caAttribute.setValues(values);

		}
		representationService.saveVocabulary(representation.getVocabulary());
	}

	private List<String> getAllowedValues(Term representation) {
		Term allowedValuesLabel = findMetaStaticListAllowedValuesType();

		List<MultiValueListAttribute> attributeValues = attributeDao.findMultiValueListAttributesByTypeAndOwner(
				allowedValuesLabel, representation);

		// Ensure the result is sorted set
		for (MultiValueListAttribute attVal : attributeValues) {
			return attVal.getValues();
		}

		return new ArrayList<String>();
	}

	@Override
	public StringAttribute update(StringAttribute attribute, String longExpression) {

		// Perform authorization check.
		authorizationHelper.checkAuthorization(getCurrentUser(), attribute,
				AuthorizationHelper.OPERATION_ATTRIBUTE_EDIT);

		if (longExpression.equals(attribute.getLongExpression())) {
			// No need to update.
			return attribute;
		}

		// Check constraints
		ConstraintChecker.checkLockConstraint(attribute.getOwner());
		constraintChecker.checkAttributeAlreadyExistsConstraint(attribute, null, null, longExpression);

		// Changing Event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ATTRIBUTE,
				new AttributeEventData(attribute, EventType.CHANGING));

		((StringAttributeImpl) attribute).setLongExpression(longExpression);

		attributeDao.save(attribute);

		// Changed Event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ATTRIBUTE,
				new AttributeEventData(attribute, EventType.CHANGED));

		return attribute;
	}

	@Override
	public SingleValueListAttribute update(SingleValueListAttribute attribute, String value) {

		// Perform authorization check
		authorizationHelper.checkAuthorization(getCurrentUser(), attribute,
				AuthorizationHelper.OPERATION_ATTRIBUTE_EDIT);

		// No need to update
		if (value != null && value.equals(attribute.getValue()))
			return attribute;

		// Check constraints
		ConstraintChecker.checkLockConstraint(attribute.getOwner());
		constraintChecker.checkAttributeAlreadyExistsConstraint(attribute, null, null, value);

		// Changing Event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ATTRIBUTE,
				new AttributeEventData(attribute, EventType.CHANGING));

		((SingleValueListAttributeImpl) attribute).setValue(value);

		attributeDao.save(attribute);

		// Changed Event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ATTRIBUTE,
				new AttributeEventData(attribute, EventType.CHANGED));

		return attribute;
	}

	@Override
	public void update(MultiValueListAttribute attribute, String value) {

		ArrayList<String> values = new ArrayList<String>(1);
		values.add(value);
		update(attribute, values);
	}

	@Override
	public MultiValueListAttribute update(MultiValueListAttribute attribute, Collection<String> values) {

		// Perform authorization check.
		authorizationHelper.checkAuthorization(getCurrentUser(), attribute,
				AuthorizationHelper.OPERATION_ATTRIBUTE_EDIT);

		if (values != null && values.equals(attribute.getValues())) {
			// No need to update.
			return attribute;
		}

		// Check constraints
		ConstraintChecker.checkLockConstraint(attribute.getOwner());
		constraintChecker.checkAttributeAlreadyExistsConstraint(attribute, null, null, values);

		// Changing Event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ATTRIBUTE,
				new AttributeEventData(attribute, EventType.CHANGING));

		((MultiValueListAttributeImpl) attribute).setValues(values);

		attributeDao.save(attribute);

		// Changed Event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ATTRIBUTE,
				new AttributeEventData(attribute, EventType.CHANGED));

		return attribute;
	}

	@Override
	public Attribute getAttributeWithError(String resourceId) {
		Attribute att = getAttribute(resourceId);
		if (att == null) {
			String message = "Attribute with resource id '" + resourceId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.ATTRIBUTE_NOT_FOUND_ID, resourceId);
		}

		return att;
	}

	@Override
	public Attribute save(final Attribute attribute) {

		// Perform authorization check.
		authorizationHelper
				.checkAuthorization(getCurrentUser(), attribute, AuthorizationHelper.OPERATION_ATTRIBUTE_ADD);

		// Adding Event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ATTRIBUTE,
				new AttributeEventData(attribute, EventType.ADDING));

		Attribute persistedAttribute = representationServiceHelper.saveAttribute(attribute);
		getCurrentSession().flush();

		// Added Event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.ATTRIBUTE,
				new AttributeEventData(attribute, EventType.ADDED));

		return persistedAttribute;
	}

	@Override
	public void remove(final Attribute attribute) {
		removeWithoutSessionFlush(attribute);
		getCurrentSession().flush();
	}

	/**
	 * Internal method to remove an attribute from the database. This will do authorization checking but will NOT flush
	 * the session.
	 * @param attribute The attribute to remove.
	 */
	@Override
	public void removeWithoutSessionFlush(final Attribute attribute) {

		// Check authorization.
		authorizationHelper.checkAuthorization(getCurrentUser(), attribute,
				AuthorizationHelper.OPERATION_ATTRIBUTE_REMOVE);

		ConstraintChecker.checkLockConstraint(attribute.getOwner());

		attributeDao.delete(attribute);
	}

	@Override
	public void remove(Term attributeLabel) {

		// remove all attributes with the given label
		for (Attribute att : attributeDao.findAttributesByType(attributeLabel)) {
			removeWithoutSessionFlush(att);
		}

		// remove the label
		representationService.remove(attributeLabel);
	}

	@Override
	public List<StringAttribute> findDefinitionsForRepresentation(final Representation representation) {

		return attributeDao.findStringAttributesByTypeAndOwner(findMetaDefinition(), representation);
	}

	@Override
	public List<StringAttribute> findDescriptionsForRepresentation(final Representation representation) {

		return attributeDao.findStringAttributesByTypeAndOwner(findMetaDescription(), representation);
	}

	@Override
	public List<StringAttribute> findExamplesForRepresentation(final Representation representation) {

		return attributeDao.findStringAttributesByTypeAndOwner(findMetaExample(), representation);
	}

	@Override
	public List<StringAttribute> findNotesForRepresentation(final Representation representation) {

		return attributeDao.findStringAttributesByTypeAndOwner(findMetaNote(), representation);
	}
}
