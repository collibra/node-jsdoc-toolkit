package com.collibra.dgc.core.component.representation;

import java.util.Collection;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.representation.Designation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * Public API for the management of {@link Term}. A {@link Term} is a verbal {@link Designation} of an
 * {@link ObjectType} in a specific subject field.
 * 
 * <br />
 * <br />
 * Note: 'rId' represents the resource id of the {@link Term}. The resource id is a UUID. 'RId' at the end of an
 * argument name represents the resource id of the named resource.
 * 
 * @author pmalarme
 * 
 */
public interface TermComponent extends RepresentationComponent {

	/**
	 * Create a new {@link Term} and persist it.
	 * 
	 * @param vocabularyRId The resource id of the {@link Vocabulary} that will contain the {@link Term}
	 * @param signifier The signifier of the {@link Term}
	 * @return The newly persisted {@link Term}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Term addTerm(String vocabularyRId, String signifier);

	/**
	 * Create a new {@link Term} and persist it.
	 * 
	 * @param vocabularyRId The resource id of the {@link Vocabulary} that will contain the {@link Term}
	 * @param signifier The signifier of the {@link Term}
	 * @param objectTypeRId The resource id of the {@link Object Type} that will be the {@link Concept} {@code Type} of
	 *            this {@link Term}
	 * @return The newly persisted {@link Term}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Term addTerm(String vocabularyRId, String signifier, String objectTypeRId);

	/**
	 * Get the {@link Term} with the given resource id.
	 * 
	 * @param rId The resource id of the {@link Term}
	 * @return The {@link Term}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Term getTerm(String rId);

	/**
	 * Get the {@link Term} of a specified {@link Vocabulary} with the given {@code signifier}.
	 * 
	 * @param vocabularyRId The resource id of the {@link Vocabulary} that contains the {@link Term}
	 * @param signfifer The signifier of the {@link Term}
	 * @return The {@link Term}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#VOCABULARY_NOT_FOUND} and
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Term getTermBySignifier(String vocabularyRId, String signifier);

	/**
	 * Get the {@link Term} of a specified {@link Vocabulary} (and its incorporated vocabularies) with the given
	 * {@code signifier}.
	 * 
	 * @param vocabularyRId The resource id of the {@link Vocabulary} that contains the {@link Term} (or one of its
	 *            incorporated vocabularies)
	 * @param signfifer The signifier of the {@link Term}
	 * @return The {@link Term}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#VOCABULARY_NOT_FOUND} and
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Term getTermBySignifierInIncorporatedVocabularies(String vocabularyRId, String signifier);

	/**
	 * Retrieve the {@link Terms} which match the specified signifier.
	 * 
	 * @param searchSignifier The expression to search in term's signifier
	 * @param offset The position of the first result to return ({@code 0} based), to allow, together with
	 *            {@code number}, pagination
	 * @param number The maximum number of terms to retrieve. If it is set to <code>0</code> then all results will be
	 *            returned
	 * @return A {@link Collection} of {@link Term} whose signifiers match {@code searchSignifier}
	 */
	Collection<Term> findTermsContainingSignifier(String searchSignifier, int offset, int number);

	/**
	 * Update the {@link Term} signifier.
	 * 
	 * @param rId The resource id of the {@link Term}
	 * @param newSignifier The new signifier
	 * @return The updated {@link Term}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Term changeSignifier(String rId, String newSignifier);

	/**
	 * Remove a {@link Collection} of {@link Term} specified by their resource id.
	 * 
	 * @param termRIds The {@link Collection} of {@link Term} resource id
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	void removeTerms(Collection<String> termRIds);
}
