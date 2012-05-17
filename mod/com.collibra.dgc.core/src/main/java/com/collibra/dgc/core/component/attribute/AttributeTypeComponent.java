package com.collibra.dgc.core.component.attribute;

import java.util.Collection;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.MultiValueListAttribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;

/**
 * Public API for the management of {@link Attribute} {@code Type}. An {@link Attribute} stores extra information about
 * a {@link Representation}. For each kind of {@link Attribute} (e.g. {@link StringAttribute},
 * {@link MultiValueListAttribute}), several {@link Attribute} {@code Types} can be defined in the Collibra <i>Attribute
 * Types Vocabulary</i>. This is a Collibra SBVR extension.
 * 
 * <br />
 * <br />
 * An {@link Attribute} {@code Type} is represented by a {@code Label} {@link Term} that contains an {@link Attribute}
 * of the type {@code Description}. Each {@link Attribute} {@code Type} is assigned to a certain kind of
 * {@link Attribute}.
 * 
 * <br />
 * <br />
 * The different kinds of {@link Attribute} are:
 * <ul>
 * <li>{@link StringAttribute}: an {@link Attribute} whose value is a {@link String} long expression</li>
 * <li>{@link SingleValueListAttribute}: an {@link Attribute} whose value is one of the value contained in a list of
 * allowed one</li>
 * <li>{@link MultiValueListAttribute}: an {@link Attribute} whose values could be one or more allowed values</li>
 * </ul>
 * 
 * <br />
 * <br />
 * Note: 'rId' represents the resource id of the {@code Label} {@link Term}. The resource id is a UUID. 'RId' at the end
 * of an argument name represents the resource id of the named resource.
 * 
 * @author pmalarme
 * 
 */
public interface AttributeTypeComponent {

	/**
	 * Add a new {@code Type} of {@link StringAttribute} and persist it.
	 * 
	 * @param signifier The signifier of the {@link Attribute} {@code Type}
	 * @param description The description of the {@link Attribute} {@code Type}
	 * @return The {@code Label} {@link Term} of the newly persisted {@link Attribute} {@code Type}
	 */
	Term addStringAttributeType(String signifier, String description);

	/**
	 * Add a new {@code Type} of {@link SingleValueListAttribute} or {@link MultiValueListAttribute} and persist it.
	 * 
	 * @param signifier The signifier of the {@link Attribute} {@code Type}
	 * @param description The description of the {@link Attribute} {@code Type}
	 * @param multipleValue {@code True} if more than one value can be set for the {@link Attribute} (i.e. if you want
	 *            to create a new type of {@link MultiValueListAttribute}), {@code false} otherwise (i.e. if you want to
	 *            create a new type of {@link SingleValueListAttribute})
	 * @param allowedValues The {@link Collection} of {@link String} value that are allowed for the {@link Attribute} of
	 *            this kind.
	 * @return The {@code Label} {@link Term} of the newly persisted {@link Attribute} {@code Type}
	 */
	Term addValueListAttributeType(String signifier, String description, boolean multipleValue,
			Collection<String> allowedValues);

	/**
	 * Get the {@code Label} {@link Term} of the {@link Attribute} for the given resource id.
	 * 
	 * @param rId The resource id of the {@code Label} {@link Term} of the {@link Attribute} {@code Type}
	 * @return The {@code Label} {@link Term} that represents the {@link Attribute} {@code Type}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Term getAttributeType(String rId);

	/**
	 * Get the {@code Label} {@link Term} of the {@link Attribute} for the given signifier.
	 * 
	 * @param signifier The signifier of the {@link Attribute} {@code Type}
	 * @return The {@code Label} {@link Term} that represents the {@link Attribute} {@code Type}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Term getAttributeTypeBySignifier(String signifier);

	/**
	 * Get the {@code Definition} {@link Attribute} {@code Type}.
	 * 
	 * @return The {@code Label} {@link Term} for the {@code Definition} {@link Attribute} {@code Type}
	 */
	Term getDefinitionAttributeType();

	/**
	 * Get the {@code Description} {@link Attribute} {@code Type}.
	 * 
	 * @return The {@code Label} {@link Term} for the {@code Description} {@link Attribute} {@code Type}
	 */
	Term getDescriptionAttributeType();

	/**
	 * Get the {@code Example} {@link Attribute} {@code Type}.
	 * 
	 * @return The {@code Label} {@link Term} for the {@code Example} {@link Attribute} {@code Type}
	 */
	Term getExampleAttributeType();

	/**
	 * Get the {@code Note} {@link Attribute} {@code Type}.
	 * 
	 * @return The {@code Label} {@link Term} for the {@code Note} {@link Attribute} {@code Type}
	 */
	Term getNoteAttributeType();

	/**
	 * Get the {@code kind} of {@link Attribute} the {@link Attribute} {@code Type} is defined for (e.g.
	 * {@link StringAttribute}).
	 * 
	 * @param rId The resource id of the {@code Label} {@link Term} of the {@link Attribute} {@code Type}
	 * @return A {@link String} that represents the name of the java class of the {@link Attribute} that can be defined
	 *         using this {@link Attribute} {@code Type}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	String getAttributeTypeKind(String rId);

	/**
	 * Get the description of an {@link Attribute} {@code Type} for the given {@code Label} {@link Term} resource id.
	 * 
	 * @param rId The resource id of the {@code Label} {@link Term} of the {@link Attribute} {@code Type}
	 * @return The description as a {@link StringAttribute} that is {@code null} if the {@link Attribute} {@code Type}
	 *         has no description
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	StringAttribute getAttributeTypeDescription(String rId);

	/**
	 * Get the allowed values of a {@link SingleValueListAttribute} or a {@link MultiValueListAttribute} {@code Type}.
	 * 
	 * @param rId The resource id of the {@code Label} {@link Term} of the {@link SingleValueListAttribute} or the
	 *            {@link MultiValueListAttribute} {@code Type}
	 * @return A {@link Collection} of {@link String} value allowed for the {@link SingleValueListAttribute} or
	 *         {@link MultiValueListAttribute} {@code Type}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Collection<String> getAllowedValues(String rId);

	/**
	 * Get all the {@link Attribute} {@code Types} of the Collibra <i>Attribute Types Vocabulary</i>.
	 * @return A {@link Collection} of {@code Label} {@link Term} of the several {@link Attribute} {@code Types}
	 */
	Collection<Term> getAttributeTypes();

	/**
	 * Update the signifier of an {@link Attribute} {@code Type}.
	 * 
	 * @param rId The resource id of the {@code Label} {@link Term} of the {@link Attribute} {@code Type}
	 * @param newSignifier The new signifier
	 * @return The {@code Label} {@link Term} for the updated {@link Attribute} {@code Type}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Term changeSignifier(String rId, String newSignifier);

	/**
	 * Update the description of an {@link Attribute} {@code Type}.
	 * 
	 * @param rId The resource id of the {@code Label} {@link Term} of the {@link Attribute} {@code Type}
	 * @param newDescription The new description
	 * @return The {@link StringAttribute} that contains the description of the {@link Attribute} {@code Type}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	StringAttribute changeDescription(String rId, String newDescription);

	/**
	 * Update the allowed {@link String} values of a {@link SingleValueListAttribute} or a
	 * {@link MultiValueListAttribute} {@code Type}.
	 * 
	 * @param rId The resource id of the {@code Label} {@link Term} of the {@link SingleValueListAttribute} or the
	 *            {@link MultiValueListAttribute} {@code Type}
	 * @param newAllowedValues The {@link Collection} of new allowed values
	 * @return The updated {@link Collection} of allowed {@link String} values
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Collection<String> changeAllowedValues(String rId, Collection<String> newAllowedValues);

	/**
	 * Remove the {@link Attribute} {@code Type} with the given signifier.
	 * 
	 * @param rId The resource id of the {@code Label} {@link Term} of the {@link Attribute} {@code Type}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	void removeAttributeType(String rId);
}
