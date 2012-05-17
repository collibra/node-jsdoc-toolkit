package com.collibra.dgc.core.component.representation;

import java.util.Collection;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;

/**
 * Public API for the management of {@link BinaryFactTypeForm}. The {@link Representation} for a {@link BinaryFactType}.
 * Correspond to a 'Lexon' in our platform implementation.
 * 
 * <br />
 * <br />
 * Note: 'rId' represents the resource id of the {@link BinaryFactTypeForm}. The resource id is a uuid. 'RId' at the end
 * of an argument name represents the resource id of the named resource.
 * 
 * @author pmalarme
 * 
 */
public interface BinaryFactTypeFormComponent extends RepresentationComponent {

	/**
	 * Create a new {@link BinaryFactTypeForm} and persist it.
	 * 
	 * @param vocabularyRId The resource id of the {@link Vocabulary} that contains the {@link BinaryFactTypeForm}
	 * @param headSignifier The signifier for the head {@link Term} of the {@link BinaryFactTypeForm}
	 * @param role The name of the role of the {@link BinaryFactTypeForm}
	 * @param coRole The name of the coRole of the {@link BinaryFactTypeForm}
	 * @param tailSignifier The signifier for the tail {@link Term} of the {@link BinaryFactTypeForm}
	 * @return The newly persisted {@link BinaryFactTypeForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	BinaryFactTypeForm addBinaryFactTypeForm(String vocabularyRId, String headSignifier, String role, String coRole,
			String tailSignifier);

	/**
	 * Create a new {@link BinaryFactTypeForm} and persist it.
	 * 
	 * @param vocabularyRId The resource id of the {@link Vocabulary} that contains the {@link BinaryFactTypeForm}
	 * @param headTermRId Head {@link Term} resource id
	 * @param role The name of the role of the {@link BinaryFactTypeForm}
	 * @param coRole The name of the coRole of the {@link BinaryFactTypeForm}
	 * @param tailTermRId Tail {@link Term} resource id
	 * @return The newly persisted {@link BinaryFactTypeForm}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#VOCABULARY_NOT_FOUND} and
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	BinaryFactTypeForm addBinaryFactTypeFormOnExistingTerms(String vocabularyRId, String headTermRId, String role,
			String coRole, String tailTermRId);

	/**
	 * Create a new {@link BinaryFactTypeForm} and persist it.
	 * 
	 * @param vocabularyRId The resource id of the {@link Vocabulary} that contains the {@link BinaryFactTypeForm}
	 * @param headTermRId Head {@link Term} resource id
	 * @param role The name of the role of the {@link BinaryFactTypeForm}
	 * @param coRole The name of the coRole of the {@link BinaryFactTypeForm}
	 * @param tailSignifier The signifier for the tail {@link Term} of the {@link BinaryFactTypeForm}
	 * @return The newly persisted {@link BinaryFactTypeForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	BinaryFactTypeForm addBinaryFactTypeFormOnExistingHeadTerm(String vocabularyRId, String headTermRId, String role,
			String coRole, String tailSignifier);

	/**
	 * Create a new {@link BinaryFactTypeForm} and persist it.
	 * 
	 * @param vocabularyRId The resource id of the {@link Vocabulary} that contains the {@link BinaryFactTypeForm}
	 * @param headSignifier The signifier for the head {@link Term} of the {@link BinaryFactTypeForm}
	 * @param role The name of the role of the {@link BinaryFactTypeForm}
	 * @param coRole The name of the coRole of the {@link BinaryFactTypeForm}
	 * @param tailTermRId Tail {@link Term} resource id
	 * @return The newly persisted {@link BinaryFactTypeForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	BinaryFactTypeForm addBinaryFactTypeFormOnExistingTailTerm(String vocabularyRId, String headSignifier, String role,
			String coRole, String tailTermRId);

	/**
	 * Get the {@link BinaryFactTypeForm} with the given resource id.
	 * 
	 * @param rId The resource id of the {@link BinaryFactTypeForm}
	 * @return The {@link BinaryFactTypeForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#BFTF_NOT_FOUND}
	 */
	BinaryFactTypeForm getBinaryFactTypeForm(String rId);

	/**
	 * Get the binary fact type forms that contain the given {@link Term} as head {@link Term}.
	 * 
	 * @param headTermRId The head {@link Term} resource id
	 * @return A {@link Collection} of {@link BinaryFactTypeForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Collection<BinaryFactTypeForm> getBinaryFactTypeFormsContainingHeadTerm(String headTermRId);

	/**
	 * Get the binary fact type forms that contain the given {@link Term} as tail {@link Term}.
	 * 
	 * @param tailTermRId The tail {@link Term} resource id
	 * @return A {@link Collection} of {@link BinaryFactTypeForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Collection<BinaryFactTypeForm> getBinaryFactTypeFormsContainingTailTerm(String tailTermRId);

	/**
	 * Get the binary fact type forms that contain the given {@link Term} as either head or tail {@link Term}.
	 * 
	 * @param termRId The resource id of the {@link Term} that can be either the head or tail {@link Term} of a
	 *            {@link BinaryFactTypeForm}
	 * @return A {@link Collection} of {@link BinaryFactTypeForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Collection<BinaryFactTypeForm> getBinaryFactTypeFormsContainingTerm(String termRId);

	/**
	 * Get all the derived binary fact type forms involving the given {@link Term}.
	 * 
	 * @param termRId The {@link Term} resource id
	 * @return A {@link Collection} of derived {@link BinaryFactTypeForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Collection<BinaryFactTypeForm> getDerivedFacts(String termRId);

	/**
	 * Update the specified {@link BinaryFactTypeForm}.
	 * 
	 * @param rId The {@link BinaryFactTypeForm} resource id
	 * @param newHeadSignifier The new head {@link Term} signifier
	 * @param newRole The new role
	 * @param newCoRole The new coRole
	 * @param newTailSignifier The new tail {@link Term} signifier.
	 * @return The updated {@link BinaryFactTypeForm}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#BFTF_NOT_FOUND}
	 */
	BinaryFactTypeForm changeBinaryFactTypeForm(String rId, String newHeadSignifier, String newRole, String newCoRole,
			String newTailSignifier);
}
