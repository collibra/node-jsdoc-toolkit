package com.collibra.dgc.core.component.representation;

import java.util.Collection;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * Public API for the management of {@link Representation}. Representation is an actuality that a given expression
 * represents a given {@link Meaning} (i.e. {@link Concept}).
 * 
 * <br />
 * <br />
 * Note: 'rId' represents the resource id of the {@link Representation}. The resource id is a UUID. 'RId' at the end of
 * an argument name represents the resource id of the named resource.
 * 
 * @author amarnath
 * @author pmalarme
 * 
 */
public interface RepresentationComponent {

	/**
	 * Get {@link Representation} with specified resource id.
	 * 
	 * @param rId The resource id of the {@link Representation}
	 * @return {@link Representation}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Representation getRepresentation(String rId);

	/**
	 * Get the preferred {@link Term} that represents the type of the {@link Concept} (i.e. ConceptType) of a
	 * {@link Representation} from its {@link Vocabulary}.
	 * 
	 * @param rId The resource id of the {@link Representation} for which you want to get the {@link Concept} type as a
	 *            {@link Term}
	 * @return The {@link Term} that represents the concept type.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Term getConceptType(String rId);

	/**
	 * Get the preferred {@link Representation} of the general {@link Concept} for the {@link Concept} of a specified
	 * {@link Representation} from its {@link Vocabulary}.
	 * 
	 * @param rId The resource id of the {@link Representation} for which you want to get the general {@link Concept} of
	 *            its {@link Concept} as a {@link Representation}
	 * @return The {@link Representation} of the general {@link Concept}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Representation getGeneralConcept(String rId);

	/**
	 * Get specialized {@link Concept}'s {@link Representation}s for the specified {@link Representation}'s
	 * {@link Concept}.
	 * 
	 * @param rId The {@link Representation} resource id
	 * @param limit Maximum number of results
	 * @return A {@link Collection} of {@link Representation}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Collection<Representation> getSpecializedConcepts(String rId, int limit);

	/**
	 * Get synonyms of the {@link Concept} represented by the specified {@link Representation}.
	 * 
	 * @param rId The {@link Representation} resource id
	 * @return A {@link Collection} of {@link Representation}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Collection<Representation> getSynonyms(String rId);

	/**
	 * Find all possible {@code Status} {@link Term} for a specified {@link Representation}.
	 * 
	 * @param rId The resource id of the {@link Representation}
	 * @return A {@link Collection} of {@code Status} {@link Term}
	 * @EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Collection<Term> findPossibleStatuses(String rId);

	// TODO Pierre: update the documentation to explain how terms are linked to attribute type
	/**
	 * Find all possible {@link Attribute} {@code Types} represented as {@link Term} for a specified
	 * {@link Representation}.
	 * 
	 * @param rId The resource id of the {@link Representation}
	 * @return A {@link Collection} of {@link Term} that represent the {@link Attribute} types
	 * @EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Collection<Term> findPossibleAttributeTypes(String rId);

	// TODO Pierre: update the documentation to explain how terms are linked to concept type
	/**
	 * Find all possible {@link Concept} types represented as {@link Term} for a specified {@link Representation}.
	 * 
	 * @param rId The resource id of the {@link Representation}
	 * @return A {@link Collection} of {@link Term} that represent the {@link Concept} types
	 * @EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Collection<Term> findPossibleConceptTypes(String rId);

	/**
	 * Set the {@link Concept} type of a specified {@link Representation} to the {@link Concept} of the conceptType
	 * {@link Term}.
	 * 
	 * @param rId The {@link Representation} resource id for which the type of the {@link Concept} (i.e. ConceptType)
	 *            must to be changed
	 * @param conceptTypeTermRId The resource id of the {@link Term} for which the {@link Concept} will be set as the
	 *            ConceptType of the specified {@link Representation}
	 * @return The updated {@link Representation}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND} and
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Representation changeConceptType(String rId, String conceptTypeTermRId);

	/**
	 * Change the general {@link Concept} from the {@link Concept} of the specialized {@link Representation} to the
	 * {@link Concept} of the general {@link Representation}.
	 * 
	 * @param specializedRepresentationRId The resource id of the specialized {@link Representation} whose general
	 *            {@link Concept} must be changed
	 * @param generalRepresentationRId The general {@link Representation} resource whose {@link Concept} will be set as
	 *            the general {@link Concept} of the specialized {@link Representation}
	 * @return The updated specialized {@link Representation}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Representation changeGeneralConcept(String specializedRepresentationRId, String generalRepresentationRId);

	/**
	 * Change {@link Representation} status.
	 * 
	 * @param rId The {@link Representation} resource id
	 * @param statusTermRId The resource id of the {@link Term} that represents the status
	 * @return The updated {@link Representation}
	 * @throws EntityNotFoundException with error codes {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND} and
	 *             {@link DGCErrorCodes#TERM_NOT_FOUND}
	 */
	Representation changeStatus(String rId, String statusTermRId);

	/**
	 * Set the {@link Concept} of the current {@link Representation} to the {@link Concept} of the selected
	 * {@link Representation} (i.e. set both representations as synonyms for the {@link Concept} of the selected
	 * {@link Representation}).
	 * 
	 * @param currentRepresentationRId The resource id of the current {@link Representation} whose {@link Meaning} (i.e.
	 *            {@link Concept}) is changed to the selected {@link Representation}'s {@link Meaning} (i.e.
	 *            {@link Concept})
	 * @param selectedRepresentationRId The resource id of the selected {@link Representation} that keeps its original
	 *            {@link Meaning} (i.e. {@link Concept})
	 * @return The updated current {@link Representation}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Representation addSynonym(String currentRepresentationRId, String selectedRepresentationRId);

	/**
	 * Remove the general {@link Concept} of the {@link Concept} of the specified {@link Representation} (i.e. set the
	 * general {@link Concept} of the specified {@link Representation} as {@code Thing}).
	 * 
	 * @param rId The resourceId of the {@link Representation} for which the general concept has to be removed
	 * @return The updated {@link Representation}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Representation removeGeneralConcept(String rId);

	/**
	 * Remove the {@link Representation} as a synonym (i.e. change its {@link Concept} to a new {@link Concept})
	 * 
	 * @param rId The resource id of the {@link Representation}
	 * @return The updated {@link Representation}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	Representation removeSynonym(String rId);

	/**
	 * Remove the specified {@link Representation}
	 * 
	 * @param rId The {@link Representation} resource id
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#REPRESENTATION_NOT_FOUND}
	 */
	void remove(String rId);
}
