package com.collibra.dgc.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.DateTimeAttribute;
import com.collibra.dgc.core.model.representation.MultiValueListAttribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * Service for CRUD of {@link Attribute}s
 * 
 * @author amarnath
 * 
 */
public interface AttributeService {

	/**
	 * @return the description attribute label {@Term}
	 */
	Term findMetaDescription();

	/**
	 * @return the definition attribute label {@Term}
	 */
	Term findMetaDefinition();

	/**
	 * @return the example attribute label {@Term}
	 */
	Term findMetaExample();

	/**
	 * @return the note attribute label {@Term}
	 */
	Term findMetaNote();

	/**
	 * The Concept Type of the label of a {@link SingleListValueAttribute} is the {@link ObjectType} of this static list
	 * {@link Term}
	 * 
	 * @return the static list type {@Term}
	 */
	Term findMetaSingleStaticListType();

	/**
	 * The Concept Type of the label of a {@link ListValueAttribute} is the {@link ObjectType} of this static list
	 * {@link Term}
	 * 
	 * @return the static list type {@Term}
	 */
	Term findMetaMultiStaticListType();

	/**
	 * The Concept Type of the label of the attribute that contains the allowed values of a {@link ListValueAttribute}
	 * is the {@link ObjectType} of this static list {@link Term}
	 * 
	 * @return the static list type {@Term}
	 */
	Term findMetaStaticListAllowedValuesType();

	/**
	 * The Concept Type of the label of a {@link StringAttribute} is the {@link ObjectType} of this static list
	 * {@link Term}
	 * 
	 * @return the static list type {@Term}
	 */
	Term findMetaStringAttributeTypeLabel();

	/**
	 * Find the attribute type label term by its signifier.
	 * @param labelSignifier The signifier that expresses the custom attribute type
	 * @return The {@link Term} that represents the attribute type.
	 */
	Term findAttributeTypeLabel(String labelSignifier);

	/**
	 * Find the Term representing the given attributeLabelName.
	 * @param attributeLabelName The attribute label name (e.g. Definition, Descriptive Example, Note) but also custom
	 *            attributes
	 * @return The Attribute Label {@link Term}
	 */
	Term findAttributeLabel(final String attributeLabelName);

	/**
	 * The default labels for the {@link Attribute} types.
	 * @return The list of {@link Term}s that representation the custom {@link Attribute} types.
	 * @warning This returns only custom attributes !!! TODO the implementation of this method should be changed to
	 *          include all attribute type labels (eg. definition, description, ...)
	 */
	List<Term> findAttributeTypeLabels();

	/**
	 * The default labels for the {@link Attribute} types.
	 * @param rep The representation for which these attribute types must be visible.
	 * @return The list of {@link Term}s that representation the custom {@link Attribute} types.
	 * @warning This returns only custom attributes !!! TODO the implementation of this method should be changed to
	 *          include all attribute type labels (eg. definition, description, ...)
	 */
	List<Term> findAttributeTypeLabels(final Representation rep);

	/**
	 * Find all attributes for a given representation of a given type (label name)
	 * @param attributeLabel The {@Term} label of the type of attribute to return.
	 * @param representation The {@link Representation} object that contains the attributes
	 * @return A {@link List} of {@link Attribute}s.
	 */
	List<Attribute> findAttributesForRepresentation(final Term attributeLabel, final Representation representation);

	/**
	 * Find all attributes for a given representation of a given type (label name)
	 * @param attributeLabel The label name of the type of attribute to return. e.g. "Definition",
	 *            "Descriptive Example", "Source System", ...
	 * @param representation The {@link Representation} object that contains the attributes
	 * @return A {@link List} of {@link Attribute}s.
	 */
	List<Attribute> findAttributesForRepresentation(final String attributeLabel, final Representation representation);

	/**
	 * Find all attribute for a given representation of certain types (label term that represents attribute type).
	 * 
	 * @param representation The {@link Representation} object that contains the {@link Attributes}
	 * @param labels The {@code Label} {@link Term} array
	 * @return A {@link Map} of {@link String} that represents the {@code Label} {@link Term} resource id and
	 *         {@link List} of {@link Attribute}
	 */
	Map<String, Collection<Attribute>> findAttributesForRepresentation(Representation representation, Term... labels);

	/**
	 * Looks up the definition {@link Attribute}s for the {@link Meaning} represented by the {@link Representation}.
	 * This is limited to those {@link Attribute}s in the same {@link Vocabulary} as the {@link Representation}.
	 * 
	 * @param representation The {@link Representation} of the {@link Meaning} for which to find the definition
	 *            {@link Attribute}s.
	 * @return A list of definition {@link Attribute}s.
	 */
	List<StringAttribute> findDefinitionsForRepresentation(Representation representation);

	/**
	 * Looks up the description {@link Attribute}s for the {@link Meaning} represented by the {@link Representation}.
	 * This is limited to those {@link Attribute}s in the same {@link Vocabulary} as the {@link Representation}.
	 * 
	 * @param representation The {@link Representation} of the {@link Meaning} for which to find the description
	 *            {@link Attribute}s.
	 * @return A list of description {@link Attribute}s.
	 */
	List<StringAttribute> findDescriptionsForRepresentation(Representation representation);

	/**
	 * Looks up the example {@link Attribute}s for the {@link Meaning} represented by the {@link Representation}. This
	 * is limited to those {@link Attribute}s in the same {@link Vocabulary} as the {@link Representation}.
	 * 
	 * @param representation The {@link Representation} of the {@link Meaning} for which to find the example
	 *            {@link Attribute}s.
	 * @return A list of example {@link Attribute}s.
	 */
	List<StringAttribute> findExamplesForRepresentation(Representation representation);

	/**
	 * Looks up the note {@link Attribute}s for the {@link Meaning} represented by the {@link Representation}. This is
	 * limited to those {@link Attribute}s in the same {@link Vocabulary} as the {@link Representation}.
	 * 
	 * @param representation The {@link Representation} of the {@link Meaning} for which to find the note
	 *            {@link Attribute}s.
	 * @return A list of note {@link Attribute}s.
	 */
	List<StringAttribute> findNotesForRepresentation(Representation representation);

	/**
	 * To add constraining values to an attribute.
	 * @param label The {@link Term} of the {@link Attribute}.
	 * @param values The {@link List} of constraining values.
	 * @return The constraining values after adding.
	 */
	Collection<String> setConstraintValues(Term label, Collection<String> values);

	/**
	 * Get all the allowed values for ValueConstraint.StaticListType.
	 * @param label The {@link Attribute} label.
	 * @return
	 */
	Collection<String> findConstraintValues(Term label);

	/**
	 * Looks up the {@link Term} for the specified label
	 * @param label The attribute label.
	 * @return The {@link Term} for the label.
	 */
	Term findTermForLabel(String label);

	/**
	 * Get {@link Attribute} type label.
	 * @param typeOT The {@link ObjectType} of the label.
	 * @return The {@link Term}.
	 */
	Term findAttributeTypeLabel(ObjectType typeOT);

	/**
	 * Get the label term of the attribute type. This method check that the term correspond to an attribute type label
	 * term: right vocabulary and right concept type.
	 * 
	 * @param rId The resource id of the attribute type label term
	 * @return The {@code Label} {@link Term}
	 */
	Term getAttributeTypeWithError(String rId);

	/**
	 * Get the {@code kind} of {@link Attribute} the {@link Attribute} {@code Type} is defined for (e.g.
	 * {@link StringAttribute}).
	 * 
	 * @param attributeTypeLabel The {@code Label} {@link Term} of the {@link Attribute} {@code Type}
	 * @return A {@link String} that represents the name of the java class of the {@link Attribute} that can be defined
	 *         using this {@link Attribute} {@code Type}
	 */
	String getAttributeTypeKind(Term attributeTypeLabel);

	/**
	 * Get {@link Attribute}, if not found throw exception.
	 * @param resourceId The {@link Attribute} resource id.
	 * @return {@link Attribute}
	 * @throws {@link EntityNotFoundException} with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND} when the
	 *         attribute is not found
	 */
	Attribute getAttributeWithError(String resourceId);

	/**
	 * Get {@link StringAttribute}, if not found throw exception.
	 * @param resourceId The {@link StringAttribute} resource id.
	 * @return {@link StringAttribute}
	 * @throws {@link EntityNotFoundException} with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND} when the
	 *         attribute is not found
	 */
	StringAttribute getStringAttributeWithError(String resourceId);

	/**
	 * Get {@link DateTimeAttribute}, if not found throw exception.
	 * 
	 * @param resourceId The {@link DateTimeAttribute} resource id
	 * @return the {@link DateTimeAttribute}
	 * @throws {@link EntityNotFoundException} with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND} when the
	 *         attribute is not found
	 */
	DateTimeAttribute getDateTimeAttributeWithError(String resourceId);

	/**
	 * Create a new Single Value constrained Attribute Type.
	 * 
	 * @param attributeTypeSignifier The name of the attribute type
	 * @param attributeTypeDescription The description of the attribute type
	 * @param allowedValues A {@link Collection} of allowed values as Strings
	 * @return The label {@link Term} representing the new attribute type
	 * @throws IllegalArgumentException with code {@link DGCErrorCodes#ATTRIBUTE_TYPE_SIGNIFIER_EXISTS} when a term with
	 *             the given attributeTypeSignifier already exists
	 */
	Term createSingleValueListAttributeType(String attributeTypeSignifier, String attributeTypeDescription,
			Collection<String> allowedValues);

	/**
	 * Create a new Multi Value constrained Attribute Type.
	 * 
	 * @param attributeTypeSignifier The name of the attribute type
	 * @param attributeTypeDescription The description of the attribute type
	 * @param allowedValues A {@link Collection} of allowed values as Strings
	 * @return The label {@link Term} representing the new attribute type
	 * @throws IllegalArgumentException with code {@link DGCErrorCodes#ATTRIBUTE_TYPE_SIGNIFIER_EXISTS} when a term with
	 *             the given attributeTypeSignifier already exists
	 */
	Term createMultiValueListAttributeType(String attributeTypeSignifier, String attributeTypeDescription,
			Collection<String> allowedValues);

	/**
	 * Create a new String Attribute Type.
	 * 
	 * @param attributeTypeSignifier The name of the attribute type
	 * @param attributeTypeDescription The description of the attribute type
	 * @return The label {@link Term} representing the new attribute type
	 * @throws IllegalArgumentException with code {@link DGCErrorCodes#ATTRIBUTE_TYPE_SIGNIFIER_EXISTS} when a term with
	 *             the given attributeTypeSignifier already exists
	 */
	Term createStringAttributeType(String attributeTypeSignifier, String attributeTypeDescription);

	/**
	 * Create a new DateTime Attribute Type
	 * 
	 * @param attributeTypeSignifier The name of the attribute type
	 * @param attributeTypeDescription the description of the attribute type
	 * @return The label {@link Term} representing the new attribute type
	 * @throws IllegalArgumentException with code {@link DGCErrorCodes#ATTRIBUTE_TYPE_SIGNIFIER_EXISTS} when a term with
	 *             the given attributeTypeSignifier already exists
	 */
	Term createDateTimeAttributeType(String attributeTypeSignifier, String attributeTypeDescription);

	/**
	 * Update the long expression of a {@link StringAttribute}.
	 * 
	 * @param attribute The {@link StringAttribute}
	 * @param longExpression The new long expression
	 * @return The updated {@link StringAttribute}
	 */
	StringAttribute update(StringAttribute attribute, String longExpression);

	/**
	 * Update the value of a {@link SingleValueListAttribute}.
	 * 
	 * @param singleValueListAttribute The {@link SingleValueListAttribute}
	 * @param value The new value
	 * @return The updated {@link SingleValueListAttribute}
	 */
	SingleValueListAttribute update(SingleValueListAttribute singleValueListAttribute, String value);

	/**
	 * Update the allowed values of a {@link MultiValueListAttribute}.
	 * 
	 * @param attribute The {@link MultiValueListAttribute}
	 * @param values a {@link Collection} of new values
	 * @return The updated {@link MultiValueListAttribute}
	 */
	MultiValueListAttribute update(MultiValueListAttribute attribute, Collection<String> values);

	/**
	 * Update the allowed values of a {@link MultiValueListAttribute}.
	 * 
	 * @param attribute The {@link MultiValueListAttribute}
	 * @param value the new value
	 */
	void update(MultiValueListAttribute attribute, String value);

	/**
	 * Get the {@link Attribute} by its resource id
	 * 
	 * @param rId the resource id of the {@link Attribute}
	 * @return the {@link Attribute}
	 */
	Attribute getAttribute(String rId);

	/**
	 * Get the {@link StringAttribute} by its resource id
	 * 
	 * @param rId the resource id of the {@link StringAttribute}
	 * @return the {@link StringAttribute}
	 */
	StringAttribute getStringAttribute(String rId);

	/**
	 * Get the {@link DateTimeAttribute} by its resource id
	 * 
	 * @param rId the resource id of the {@link DateTimeAttribute}
	 * @return the {@link StringAttribute}
	 */
	DateTimeAttribute getDateTimeAttribute(String rId);

	/**
	 * Get the {@link MultiValueListAttribute} by its resource id
	 * 
	 * @param rId the resource id of the {@link MultiValueListAttribute}
	 * @return the {@link MultiValueListAttribute}
	 */
	MultiValueListAttribute getMultiValueListAttribute(String rId);

	/**
	 * Check if the {@link MultiValueListAttribute} complies with the allowed value constraint.
	 * 
	 * @param attribute the {@link MultiValueListAttribute}
	 * @return true if the attribute complies, false if not
	 */
	boolean checkValueConstraint(MultiValueListAttribute attribute);

	/**
	 * Check if the {@link SingleValueListAttribute} complies with the allowed value constraint.
	 * 
	 * @param attribute the {@link SingleValueListAttribute}
	 * @return true if the attribute complies, false if not
	 */
	boolean checkValueConstraint(SingleValueListAttribute attribute);

	/**
	 * Get {@link MultiValueListAttribute}, if not found throw exception.
	 * @param resourceId The {@link MultiValueListAttribute} resource id.
	 * @return {@link MultiValueListAttribute}
	 * @throws {@link EntityNotFoundException} with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND} when the
	 *         attribute is not found
	 */
	MultiValueListAttribute getMultiValueListAttributeWithError(String resourceId);

	/**
	 * Get the {@link SingleValueListAttribute} by its resource id
	 * 
	 * @param rId the resource id of the {@link SingleValueListAttribute}
	 * @return the {@link SingleValueListAttribute}
	 */
	SingleValueListAttribute getSingleValueListAttribute(String rId);

	/**
	 * Get {@link SingleValueListAttribute}, if not found throw exception.
	 * @param resourceId The {@link SingleValueListAttribute} resource id.
	 * @return {@link SingleValueListAttribute}
	 * @throws {@link EntityNotFoundException} with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND} when the
	 *         attribute is not found
	 */
	SingleValueListAttribute getSingleValueListAttributeWithError(String resourceId);

	/**
	 * Save and persist an attribute.
	 * 
	 * @param attribute The {@link Attribute} to persist
	 * @return The persisted Attribute
	 */
	Attribute save(Attribute attribute);

	/**
	 * Remove an attribute and flush Hibernate session.
	 * @param attribute The {@link Attribute} to remove
	 */
	void remove(Attribute attribute);

	/**
	 * Remove an attribute without flushing Hibernate session.
	 * 
	 * <br />
	 * <br />
	 * Note: The purpose of this method is to be used only in the representation service not at component level.
	 * 
	 * @param attribute The {@link Attribute} to remove
	 */
	void removeWithoutSessionFlush(Attribute attribute);

	/**
	 * Remove the given Attribute Label {@link Term}. This will remove all attribute with the given label from the
	 * entire glossary !
	 * 
	 * @param attributeLabel the Attribute Label {@link Term}
	 */
	void remove(Term attributeLabel);

	Term findMetaCandidateStatus();

	Term findMetaDateTimeAttributeTypeLabel();

}