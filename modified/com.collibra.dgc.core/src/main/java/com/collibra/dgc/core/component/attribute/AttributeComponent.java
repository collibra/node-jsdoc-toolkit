package com.collibra.dgc.core.component.attribute;

import java.util.Collection;
import java.util.Map;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.MultiValueListAttribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.StringAttribute;

/**
 * Public API for the management of {@link Attribute}. An {@link Attribute} stores extra information about a
 * {@link Representation}. The {@link Attribute} is a not a defined as in SBVR as a {@link Representation}.
 * 
 * <br />
 * <br />
 * There are different kinds of {@link Attribute}:
 * <ul>
 * <li>{@link StringAttribute}: an {@link Attribute} whose value is a {@link String}</li>
 * <li>{@link SingleValueListAttribute}: an {@link Attribute} whose value is one of the value contained in a list of
 * allowed one</li>
 * <li>{@link MultiValueListAttribute}: an {@link Attribute} whose values could be one or more allowed values</li>
 * </ul>
 * 
 * <br />
 * Every kind of {@link Attribute} has:
 * <ul>
 * <li>Owner: the {@link Representation} that owns the attribute</li>
 * <li>Label: the {@code Label} {@link Term} that represents the {@link Attribute} {@code Type}</li>
 * </ul>
 * 
 * <br />
 * Note: 'rId' represents the resource id of the {@link Attribute}. The resource id is a UUID. 'RId' at the end of an
 * argument name represents the resource id of the named resource. 'labelRId' represents the resource id of the
 * {@link Attribute} {@code Type} {@code Label} {@link Term}.
 * 
 * @author pmalarme
 * 
 */
public interface AttributeComponent {

	/**
	 * Add an {@link Attribute} of a certain kind defined by it's {@link Attribute} {@code Type} {@code Label}.
	 * 
	 * @param representationRId The resource id of the owner {@link Representation}
	 * @param lableRId The resource id of the {@link Attribute} {@code} {@code Label} {@link Term}
	 * @param values The {@link Collection} of the {@link String} representation(s) of the value(s). For certain kind of
	 *            attributes, the value is unique or null
	 * @return The newly persisted {@link Attribute}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND} and
	 *             {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Attribute addAttribute(String representationRId, String lableRId, Collection<String> values);

	/**
	 * Add a new {@link StringAttribute} and persist it.
	 * 
	 * @param representationRId The resource id of the {@link Representation} that the {@link StringAttribute} is for
	 * @param labelRId The resource id of the {@link Attribute} {@code Type} {@code Label} {@link Term}
	 * @param longExpression The long expression for the {@link StringAttribute}
	 * @return The newly persisted {@link StringAttribute}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND} and
	 *             {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	StringAttribute addStringAttribute(String representationRId, String labelRId, String longExpression);

	/**
	 * Add a new {@link SingleValueListAttribute} and persist it.
	 * 
	 * @param representationRId The resource id of the {@link Representation} that the {@link SingleValueListAttribute}
	 *            is for
	 * @param labelRId The resource id of the {@link Attribute} {@code Type} {@code Label} {@link Term}
	 * @param value The value for the {@link SingleValueListAttribute}
	 * @return The newly persisted {@link SingleValueListAttribute}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND} and
	 *             {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	SingleValueListAttribute addSingleValueListAttribute(String representationRId, String labelRId, String value);

	/**
	 * Add a new {@link MultiValueListAttribute} and persist it.
	 * 
	 * @param representationRId The resource id of the {@link Representation} that the {@link MultiValueListAttribute}
	 *            is for
	 * @param labelRId The resource id of the {@link Attribute} {@code Type} {@code Label} {@link Term}
	 * @param values The {@link Collection} of value for the {@link MultiValueListAttribute}
	 * @return The newly persisted {@link MultiValueListAttribute}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND} and
	 *             {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	MultiValueListAttribute addMultiValueListAttribute(String representationRId, String labelRId,
			Collection<String> values);

	/**
	 * Create a new definition {@link StringAttribute} and persist it.
	 * 
	 * @param representationRId The resource id of the {@link Representation} for which the definition will be added
	 * @param definitionLongExpresseion The definition long expression ({@link String}) for the {@link Representation}
	 * @return The newly persisted definition {@link StringAttribute}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	StringAttribute addDefinition(String representationRId, String definitionLongExpression);

	/**
	 * Create a new description {@link StringAttribute} and persist it.
	 * 
	 * @param representationRId The resource id of the {@link Representation} for which the description will be added
	 * @param descriptionLongExpression The description long expression ({@link String}) for the {@link Representation}
	 * @return The newly persisted description {@link StringAttribute}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	StringAttribute addDescription(String representationRId, String descriptionLongExpression);

	/**
	 * Create a new example {@link StringAttribute} and persist it.
	 * 
	 * @param representationRId The resource id of the {@link Representation} for which the example will be added
	 * @param exampleLongExpression The example long expression ({@link String}) for the {@link Representation}
	 * @return The newly persisted example {@link StringAttribute}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	StringAttribute addExample(String representationRId, String exampleLongExpression);

	/**
	 * Create a new note {@link StringAttribute} and persist it.
	 * 
	 * @param representationRId The resource id of the {@link Representation} for which the example will be added
	 * @param noteLongExpression The note long expression ({@link String}) for the {@link Representation}
	 * @return The newly persisted note {@link StringAttribute}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	StringAttribute addNote(String representationRId, String noteLongExpression);

	/**
	 * Get the {@link Attribute} with the given resource id.
	 * 
	 * @param rId The resource id of the {@link Attribute}
	 * @return The {@link Attribute}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 */
	Attribute getAttribute(String rId);

	/**
	 * Get the {@link StringAttribute} with the given resource id.
	 * 
	 * @param stringAttributeRId The resource id of the {@link StringAttribute}
	 * @return The {@link StringAttribute}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 */
	StringAttribute getStringAttribute(String stringAttributeRId);

	/**
	 * Get the {@link SingleValueListAttribute} with the given resource id.
	 * 
	 * @param singleValueListAttributeRId The resource id of the {@link SingleValueListAttribute}
	 * @return The {@link SingleValueListAttribute}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 */
	SingleValueListAttribute getSingleValueListAttribute(String singleValueListAttributeRId);

	/**
	 * Get the {@link MultiValueListAttribute} with the given resource id.
	 * 
	 * @param multiValueListAttributeRId The resource id of the {@link MultiValueListAttribute}
	 * @return The {@link MultiValueListAttribute}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 */
	MultiValueListAttribute getMultiValueListAttribute(String multiValueListAttributeRId);

	/**
	 * Get the list of {@link Attribute} of a certain {@link Attribute} {@code Type} for a specified
	 * {@link Representation}.
	 * 
	 * @param representationRId The resource id of the {@link Representation}
	 * @param labelRId The resource id of the {@link Attribute} {@code Type} {@code Label} {@link Term}
	 * @return A {@link Collection} of {@link Attribute}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND} and
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Collection<Attribute> getAttributesOfTypeForRepresentation(String representationRId, String labelRId);

	/**
	 * Get the definitions ({@link StringAttribute}) of a specified {@link Representation}.
	 * 
	 * @param representationRId The resource id of the {@link Representation}
	 * @return A {@link Collection} of {@link StringAttribute} definition
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Collection<StringAttribute> getDefinitionsForRepresentation(String representationRId);

	/**
	 * Get the descriptions ({@link StringAttribute}) of a specified {@link Representation}.
	 * 
	 * @param representationRId The resource id of the {@link Representation}
	 * @return A {@link Collection} of {@link StringAttribute} description
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Collection<StringAttribute> getDescriptionsForRepresentation(String representationRId);

	/**
	 * Get the examples ({@link StringAttribute}) of a specified {@link Representation}.
	 * 
	 * @param representationRId The resource id of the {@link Representation}
	 * @return A {@link Collection} of {@link StringAttribute} example
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Collection<StringAttribute> getExamplesForRepresentation(String representationRId);

	/**
	 * Get the notes ({@link StringAttribute}) of a specified {@link Representation}.
	 * 
	 * @param representationRId The resource id of the {@link Representation}
	 * @return A {@link Collection} of {@link StringAttribute} note
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Collection<StringAttribute> getNotesForRepresentation(String representationRId);

	/**
	 * Get a list of {@link Attribute} of defined {@link Attribute} {@code Types} for a specified {@link Representation}
	 * .
	 * 
	 * @param representationRId The resource id of the {@link Representation}
	 * @param labelRIds The {@code Label} {@link Term} resource ids of the type of {@link Attribute} to retrieve for the
	 *            {@link Representation}
	 * @return A {@link Map} of a {@link String} that represent a {@code Label} {@link Term} resource id (
	 *         {@link Attribute} {@code Type}) and a {@link Collection} of {@link Attribute} (for the specified
	 *         representation and the current {@link Attribute} {@code Type})
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND} and
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Map<String, Collection<Attribute>> getAttributesOfTypesForRepresentation(String representationRId,
			String... labelRIds);

	/**
	 * Get a list of {@link Attribute} of defined {@link Attribute} {@code Types} for a specified {@link Representation}
	 * .
	 * 
	 * @param representationRId The resource id of the {@link Representation}
	 * @param labelRIds The {@code Label} {@link Term} resource ids of the type of {@link Attribute} to retrieve for the
	 *            {@link Representation}
	 * @return A {@link Collection} of {@link Attribute} ordered by {@link Attribute} {@code Type} (ASC)
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND} and
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Collection<Attribute> getAttributesOfTypesForRepresentationInCollection(String representationRId,
			String... labelRId);

	/**
	 * Update an {@link Attribute} of a certain kind defined by it's {@link Attribute} {@code Type} {@code Label}.
	 * 
	 * @param rId The resource id of the {@link Attribute}
	 * @param newValues The {@link Collection} of the {@link String} representation(s) of the new value(s). For certain
	 *            kind of attributes, the new value is unique or null
	 * @return The updated {@link Attribute}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 */
	Attribute changeAttribute(String rId, Collection<String> newValues);

	/**
	 * Update the long expression of the specified {@link StringAttribute}.
	 * 
	 * @param stringAttributeRId The resource id of the {@link StringAttribute}
	 * @param newLongExpression The new long expression
	 * @return The updated {@link StringAttribute}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 */
	StringAttribute changeStringAttributeLongExpression(String stringAttributeRId, String newLongExpression);

	/**
	 * Update the value of the specified {@link SingleValueListAttribute}.
	 * 
	 * @param singleValueListAttributeRId The resource id of the {@link SingleValueListAttribute}
	 * @param newValue The new value
	 * @return The updated {@link SingleValueListAttribute}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 */
	SingleValueListAttribute changeSingleValueListAttributeValue(String singleValueListAttributeRId, String newValue);

	/**
	 * Update the values of the specified {@link MultiValueListAttribute}.
	 * 
	 * @param multiValueListAttributeRId The resource id of the {@link MultiValueListAttribute}
	 * @param newValues The {@link Collection} of new {@link String} value
	 * @return The updated {@link MultiValueListAttribute}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 */
	MultiValueListAttribute changeMultiValueListAttributeValues(String multiValueListAttributeRId,
			Collection<String> newValues);

	/**
	 * Remove the {@link Attribute} with the given resource id.
	 * 
	 * @param rId The resource id of the {@link Attribute}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 */
	void removeAttribute(String rId);

	/**
	 * Check if the {@link Attribute} is in violation with any value constraint.
	 * 
	 * @param rId Resource id of the {@link Attribute} to be checked
	 * @return {@code true} of if value constraint is satisfied, {@code false} otherwise
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 */
	boolean validateConstraint(String rId);

	/**
	 * Check if the {@link SingleValueListAttribute} is in violation with any value constraint.
	 * 
	 * @param singleValueListAttributeRId Resource id of the {@link SingleValueListAttribute} to be checked
	 * @return {@code true} of if value constraint is satisfied otherwise {@code false}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 * 
	 */
	boolean validateSingleValueListConstraint(String singleValueListAttributeRId);

	/**
	 * Check if the {@link MultiValueListAttribute} is in violation with any value constraint.
	 * 
	 * @param multiValueListAttributeRId Resource id of the {@link MultiValueListAttribute} to be checked
	 * @return {@code true} of if value constraint is satisfied otherwise {@code false}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#ATTRIBUTE_NOT_FOUND}
	 */
	boolean validateMultiValueListConstraint(String multiValueListAttributeRId);
}
