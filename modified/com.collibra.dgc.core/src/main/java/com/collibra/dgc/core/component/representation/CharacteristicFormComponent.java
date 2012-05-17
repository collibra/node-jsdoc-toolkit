package com.collibra.dgc.core.component.representation;

import java.util.Collection;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;

/**
 * Public API for the management of {@link CharacteristicForm}. A {@link CharacteristicForm} is a {@link Representation}
 * of a {@link Characteristic}.
 * 
 * <br />
 * <br />
 * Note: 'rId' represents the resource id of the {@link CharacteristicForm}. The resource id is a UUID. 'RId' at the end
 * of an argument name represents the resource id of the named resource.
 * 
 * @author pmalarme
 * 
 */
public interface CharacteristicFormComponent extends RepresentationComponent {

	/**
	 * Create a new {@link CharacteristicForm} and persist it.
	 * 
	 * @param vocabularyRId The resource id of the {@link Vocabulary} that contains the {@link CharacteristicForm}
	 * @param termSignifier The signifier of the {@link Term} of the {@link CharacteristicForm}
	 * @param role The name for the role of the {@link CharacteristicForm}
	 * @return The newly persisted {@link CharacteristicForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	CharacteristicForm addCharacteristicForm(String vocabularyRId, String termSignifier, String role);

	/**
	 * Get the {@link CharacteristicForm} with the given resource id.
	 * 
	 * @param rId The resource id of the {@link CharacteristicForm}
	 * @return The {@link CharacteristicForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#CF_NOT_FOUND}
	 */
	CharacteristicForm getCharacteristicForm(String rId);

	/**
	 * Get all the characteristic forms involving the given {@link Term}.
	 * 
	 * @param termRId the {@link Term} resource id
	 * @return A {@link Collection} of {@link CharacteristicForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Collection<CharacteristicForm> getCharacteristicFormsContainingTerm(String termRId);

	/**
	 * Update specified {@link CharacteristicForm}.
	 * 
	 * @param rId The {@link CharacteristicForm} resource id
	 * @param newTermSignifier The {@link Term} signifier
	 * @param newRole The role name
	 * @return The updated {@link CharacteristicForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#CF_NOT_FOUND}
	 */
	CharacteristicForm changeCharacteristicForm(String rId, String newTermSignifier, String newRole);
}
