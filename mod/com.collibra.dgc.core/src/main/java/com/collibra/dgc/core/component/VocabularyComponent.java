package com.collibra.dgc.core.component;

import java.util.Collection;
import java.util.Set;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * Public API for the management of {@link Vocabulary}. A Vocabulary is a Set of {@link Term} and Fact Type Forms
 * primarily drawn from a single language to express {@link Concept}s within a Body of Shared Meanings.
 * 
 * <br />
 * <br />
 * Note: 'rId' represents the resource id of the {@link Vocabulary}. The resource id is a uuid 'RId' at the end of an
 * argument name represents the resource id of the named resource.
 * 
 * @author amarnath
 * @author pmalarme
 */
public interface VocabularyComponent {
	/**
	 * Create a new {@link Vocabulary} with the given signifier and URI and persist it.
	 * 
	 * @param communityRId The resource id of the {@link Community} that owns the {@link Vocabulary}
	 * @param name The name of the {@link Vocabulary}
	 * @param uri The URI of the {@link Vocabulary}
	 * @return The newly persisted {@link Vocabulary}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#COMMUNITY_NOT_FOUND}
	 */
	Vocabulary addVocabulary(String communityRId, String name, String uri);

	/**
	 * Create a new {@link Vocabulary} with the given signifier, URI and type, and persist it.
	 * 
	 * @param communityRId The resource id of the {@link Community} that owns the {@link Vocabulary}
	 * @param name The name of the {@link Vocabulary}
	 * @param uri The URI of the {@link Vocabulary}
	 * @param typeRId the resource id of the vocabulary type {@link ObjectType}
	 * @return The newly persisted {@link Vocabulary}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#COMMUNITY_NOT_FOUND} or
	 *             {@link DGCErrorCodes#OBJECT_TYPE_NOT_FOUND}
	 */
	Vocabulary addVocabulary(String communityRId, String name, String uri, String typeRId);

	/**
	 * Get the {@link Vocabulary} with the given resource id.
	 * 
	 * @param rId The resource id of the {@link Vocabulary}
	 * @return The {@link Vocabulary}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Vocabulary getVocabulary(String rId);

	/**
	 * Get the {@link Vocabulary} with the given URI.
	 * 
	 * @param uri The URI of the {@link Vocabulary}
	 * @return The {@link Vocabulary}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Vocabulary getVocabularyByUri(String uri);

	/**
	 * Get the vocabulary with the given name.
	 * 
	 * @param name The name of the {@link Vocabulary}
	 * @return The {@link Vocabulary}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Vocabulary getVocabularyByName(String name);

	/**
	 * Return a {@link Set} of incorporated {@link Vocabulary}. The incorporation is transitional therefore if A-->B and
	 * B-->C, getIncorporatedVocabularies(C) will return A and B.
	 * 
	 * @param rId The id of the incorporating {@link Vocabulary}
	 * @param excludeSbvr <code>true</code> if you want to exclude SBVR vocabularies, <code>false</code> otherwise
	 * @return {@link Collection} of incorporated {@link Vocabulary}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Collection<Vocabulary> getIncorporatedVocabularies(String rId, boolean excludeSbvr);

	/**
	 * Finds all the vocabularies that incorporate the specified vocabulary.
	 * 
	 * @param rId The resource id {@link Vocabulary} that other vocabularies incorporate
	 * @return A {@link Collection} of {@link Vocabulary}s that incorporate the specified {@link Vocabulary}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Collection<Vocabulary> getIncorporatingVocabularies(String rId);

	/**
	 * Find a {@link Collection} of possible {@link Vocabulary} that can be incorporated in the specified
	 * {@link Vocabulary}.
	 * 
	 * @param rId The {@link Vocabulary} resource id
	 * @return A {@link Collection} of {@link Vocabulary}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Collection<Vocabulary> findPossibleVocabulariesToInCorporate(String rId);

	/**
	 * Get all vocabularies.
	 * 
	 * @return A {@link Collection} of {@link Vocabulary}
	 */
	Collection<Vocabulary> getVocabularies();

	/**
	 * Get all the non 'meta' vocabularies.
	 * 
	 * @return A {@link Collection} of non 'meta' {@link Vocabulary}
	 */
	Collection<Vocabulary> getNonMetaVocabularies();

	/**
	 * Retrieve vocabularies that match the specified name.
	 * 
	 * @param searchName The expression to search in vocabulary's name
	 * @param offset The position of the first result to return ({@code 0} based), to allow, together with
	 *            {@code number}, pagination
	 * @param number The maximum number of vocabularies to retrieve. If it is set to <code>0</code> then all results
	 *            will be retrieved
	 * @return A {@link Collection} of {@link Vocabulary} whose names match {@code searchName}
	 */
	Collection<Vocabulary> findVocabulariesContainingName(String searchName, int offset, int number);

	/**
	 * Find the preferred {@link Term} in the {@link Vocabulary} for the given {@link ObjectType}.
	 * 
	 * @param rId The {@link Vocabulary} resource id.
	 * @param objectTypeRId The resource id of the {@link ObjectType} of the {@link Term} (i.e. the {@link Concept} of
	 *            the {@link Term})
	 * @return The preferred {@link Term}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND} or with error code
	 *             {@link DGCErrorCodes#OBJECT_TYPE_NOT_FOUND}
	 */
	Term getPreferredTerm(String rId, String objectTypeRId);

	/**
	 * Find the preferred {@link Term} in the {@link Vocabulary} and all the vocabularies it incoporates for the given
	 * {@link ObjectType}.
	 * 
	 * @param rId The {@link Vocabulary} resource id.
	 * @param objectTypeRId The resource id of the {@link ObjectType} of the {@link Term} (i.e. the {@link Concept} of
	 *            the {@link Term})
	 * @return The preferred {@link Term}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND} or with error code
	 *             {@link DGCErrorCodes#OBJECT_TYPE_NOT_FOUND}
	 */
	Term getPreferredTermInIncorporatedVocabularies(String rId, String objectTypeRId);

	/**
	 * Get all the preferred {@link Term}s in the {@link Vocabulary} for all {@link ObjectType} ({@link Concept})
	 * 
	 * represented in the {@link Vocabulary}.
	 * @param rId The {@link Vocabulary} resource id
	 * @return A {@link Collection} of preferred {@link Term}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Collection<Term> getPreferredTerms(String rId);

	/**
	 * Get all the preferred {@link Term}s in the {@link Vocabulary} and all the {@link Vocabulary}s it incorporates for
	 * all {@link ObjectType} represented in the {@link Vocabulary} and the {@link Vocabulary}s it incorporates.
	 * 
	 * @param rId The {@link Vocabulary} resource id, to find all preferred terms for
	 * @return A {@link Collection} of preferred {@link Term}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Collection<Term> getPreferredTermsInIncorporatedVocabularies(String rId);

	/**
	 * Get all the preferred {@link Representation} in the {@link Vocabulary} for the given {@link Concept}.
	 * 
	 * @param rId The {@link Vocabulary} resource id
	 * @param conceptRId The id of the {@link Concept}
	 * @return The preferred {@link Representation}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND} or with error code
	 *             {@link DGCErrorCodes#CONCEPT_NOT_FOUND}
	 */
	Representation getPreferredRepresentation(String rId, String conceptRId);

	/**
	 * Get all the preferred {@link Representation} in the {@link Vocabulary} and all its incorporated
	 * {@link Vocabulary}s for the given {@link Concept}.
	 * 
	 * @param rId The {@link Vocabulary} resource id
	 * @param conceptRId The id of the {@link Concept}
	 * @return The preferred {@link Representation}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND} or with error code
	 *             {@link DGCErrorCodes#CONCEPT_NOT_FOUND}
	 */
	Representation getPreferredRepresentationInIncorporatedVocabularies(String rId, String conceptRId);

	/**
	 * Change {@link Vocabulary} name.
	 * 
	 * @param rId The {@link Vocabulary} resource id
	 * @param newName The new name
	 * @return The updated {@link Vocabulary}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Vocabulary changeName(String rId, String newName);

	/**
	 * Change the URI of {@link Vocabulary}
	 * 
	 * @param rId The resource id of the {@link Vocabulary}
	 * @param newUri The new URI
	 * @return The updated {@link Vocabulary}.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Vocabulary changeUri(String rId, String newUri);

	/**
	 * Change the type {@link ObjectType} of the {@link Vocabulary}.
	 * 
	 * @param rId The resource id of the {@link Vocabulary}
	 * @param typeRId The resource id of the new {@link ObjectType} type
	 * @return The updated {@link Vocabulary}.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#VOCABULARY_TYPE_INCONSISTENT} when a
	 *             constraint is violated.
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#OBJECT_TYPE_NOT_FOUND}
	 */
	Vocabulary changeType(String rId, String typeRId);

	/**
	 * Change the {@link Community} of the {@link Vocabulary}.
	 * 
	 * @param rId The resource id of the {@link Vocabulary}
	 * @param newCommunityRId The resource id of the new {@link Community}
	 * @return The updated {@link Vocabulary}
	 */
	Vocabulary changeCommunity(String rId, String newCommunityRId);

	/**
	 * Add an incorporated {@link Vocabulary} to the specified incorporating {@link Vocabulary} (i.e. incorporate a
	 * vocabulary in another one).
	 * 
	 * @param rId The incorporating {@link Vocabulary} resource id
	 * @param incorporateRId The resource id of the {@link Vocabulary} to incorporate
	 * @return The incorporating {@link Vocabulary}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Vocabulary addIncorporatedVocabulary(String rId, String vocabularyToIncorporateRId);

	/**
	 * Remove incorporated {@link Vocabulary} from a specified {@link Vocabulary} (i.e. disincorporate a vocabulary from
	 * another one).
	 * 
	 * @param rId The resource id of the incorporating {@link Vocabulary}
	 * @param incorporatedVocabularyRId The resource id of the incorporated {@link Vocabulary} that needs to be
	 *            disincorporated
	 * @return The incorporating {@link Vocabulary}
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	Vocabulary removeIncorporatedVocabulary(String rId, String incorporatedVocabularyRId);

	/**
	 * Removes the given {@link Vocabulary}.
	 * 
	 * @param rId The resource id of the {@link Vocabulary} to remove
	 * @throws EntityNotFoundException with error code {@link DGCErrorCodes#VOCABULARY_NOT_FOUND}
	 */
	void removeVocabulary(String rId);
}