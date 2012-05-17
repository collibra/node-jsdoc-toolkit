package com.collibra.dgc.core.component;

import java.util.Collection;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.representation.Term;

/**
 * Public API for the management of {@code Status}. Each representation has a {@code Status} that is represented by a
 * {@code Label} {@link Term} contained in the Collibra <i>Statuses Vocabulary</i> (URI:
 * {@code http://www.collibra.com/glossary/statuses}). This is a Collibra SBVR extension.
 * 
 * <br />
 * <br />
 * Note: 'rId' represents the resource id of the {@code Label} {@link Term}. The resource id is a uuid 'RId' at the end
 * of an argument name represents the resource id of the named resource.
 * 
 * @author pmalarme
 * 
 */
public interface StatusComponent {

	/**
	 * Add a {@code Status} to the Collibra <i>Statuses Vocabulary</i> and persist it.
	 * 
	 * @param signifier The signifier of the {@code Label} {@link Term} of the {@code Status}
	 * @return The newly persisted {@code Status} {@code Label} {@link Term}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#VOCABULARY_NOT_FOUND} and
	 *             {@link DGCErrorCodes#OBJECT_TYPE_NOT_FOUND}
	 */
	Term addStatus(String signifier);

	/**
	 * Get the {@code Label} {@link Term} of the {@code Status} for the given resource id.
	 * 
	 * @param rId The resource id of {@code Status} {@code Label} {@link Term}
	 * @return The {@code Status} {@code Label} {@link Term}
	 * @throws EntityNotFoundException with error {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Term getStatus(String rId);

	/**
	 * Get the {@code Label} {@link Term} of the {@code Status} for the given signifier.
	 * 
	 * @param signifier The signifier of the {@code Label} {@link Term} of the {@code Status}
	 * @return The {@code Status} {@code Label} {@link Term}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#TERM_NOT_FOUND} and
	 *             {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Term getStatusBySignifier(String signifier);

	/**
	 * Get all statuses from the Collibra <i>Statuses Vocabulary</i>.
	 * 
	 * @return A {@link Collection} of {@code Status} {@code Label} {@link Term}
	 */
	Collection<Term> getStatuses();

	/**
	 * Update the signifier of a {@code Status} {@link Term}.
	 * 
	 * @param rId The resource id of {@code Status} {@code Label} {@link Term}
	 * @param newSignifier The new signifier
	 * @return The updated {@code Status} {@code Label} {@link Term}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Term changeSignifier(String rId, String newSignifier);

	/**
	 * Remove the {@code Status} {@link Term} from the Collibra <i>Statuses Vocabulary</i>.
	 * 
	 * @param rId The resource id of {@code Status} {@code Label} {@link Term}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	void removeStatus(String rId);
}
