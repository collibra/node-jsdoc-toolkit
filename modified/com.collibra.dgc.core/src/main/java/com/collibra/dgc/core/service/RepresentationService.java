package com.collibra.dgc.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.user.Role;

/**
 * Supports the methods of the representation daos with extra business logic for validation.
 * 
 * @author dtrog
 * 
 */

public interface RepresentationService {

	/**
	 * Makes the two terms synonymous.
	 * @param original The term that keeps its original meaning
	 * @param syononym The term which meaning is changed to the original term's meaning.
	 * @return The synonym
	 */
	Term createSynonym(Term original, Term synonym);

	/**
	 * Makes the two {@link Representation}s synonymous.
	 * @param original The {@link Representation} that keeps its original meaning
	 * @param syononym The {@link Representation} which meaning is changed to the original term's meaning.
	 * @return The synonym
	 */
	Representation addSynonym(Representation originalRepresentation, Representation synonymRepresentation);

	/**
	 * Sets the concept type of a {@link Representation} with the selected {@link Representation}.
	 * @param specializedRepresentation The specialized {@link Representation} which general concept is to be set.
	 * @param generalRepresentation The {@link Representation} as general concept.
	 * @return The updated specialized {@link Representation}
	 */
	Representation setGeneralConceptRepresentation(Representation specializedRepresentation,
			Representation generalRepresentation);

	// /**
	// * Move {@link Term} to another {@link Vocabulary}.
	// * @param term The {@link Term} to be moved.
	// * @param destVocabulary The destination {@link Vocabulary}.
	// * @return The new moved {@link Term}.
	// */
	// Term moveTerm(Term term, Vocabulary destVocabulary);
	//
	// /**
	// * Move {@link Term}s to {@link Vocabulary}.
	// * @param terms The {@link Term}s to be moved.
	// * @param destVocabulary The destination {@link Vocabulary}.
	// * @return The moved {@link Term}s.
	// */
	// Collection<Term> moveTerms(Collection<Term> terms, Vocabulary destVocabulary);

	/**
	 * Disincorporate {@link Vocabulary}.
	 * @param currentVoc The {@link Vocabulary}.
	 * @param incorporatedVoc The {@link Vocabulary} to be disincorporated.
	 * @return The incorporating {@link Vocabulary}
	 */
	Vocabulary disincorporateVocabulary(Vocabulary currentVoc, Vocabulary incorporatedVoc);

	/**
	 * Sets the concept type of a {@link Representation} with the selected {@link Term}.
	 * @param currentRepresentation The current {@link Representation} which concept type is to be changed.
	 * @param selectedTerm The selected {@link Term} as concept type.
	 * @return The updated current {@link Representation}
	 */
	Representation setConceptTypeRepresentation(Representation currentRepresentation, Term selectedTerm);

	/**
	 * Incorporate {@link Vocabulary}.
	 * @param incorporating Incorporating {@link Vocabulary}.
	 * @param incorporate {@link Vocabulary} to be incorporated.
	 * @return The incorporating {@link Vocabulary}.
	 */
	Vocabulary incorporate(Vocabulary incorporating, Vocabulary incorporate);

	/***********************
	 * All the change methods
	 ***********************/

	/**
	 * Change signifier of the specified {@link Term}.
	 * @param term The {@link Term}
	 * @param newSignifier The new signifier.
	 * @return The {@link Term} with modified signifier
	 * @throw {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#TERM_NULL},
	 *        {@link DGCErrorCodes#TERM_SIGNIFIER_NULL}, {@link DGCErrorCodes#TERM_SIGNIFIER_EMPTY}
	 */
	Term changeSignifier(Term term, String newSignifier);

	/**
	 * Change the status of the given representation.
	 * @param representation The {@link Representation} to change the status for.
	 * @param newStatus The new status for the representations.
	 * @return The {@link Representation} with modified status
	 */
	Representation changeStatus(final Representation representation, Term newStatus);

	/**
	 * Rename {@link Vocabulary}.
	 * @param vocabulary The {@link Vocabulary}.
	 * @param newName The new name.
	 * @return Renamed {@link Vocabulary}.
	 * @throw {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#VOCABULARY_NULL},
	 *        {@link DGCErrorCodes#VOCABULARY_NAME_NULL}, {@link DGCErrorCodes#VOCABULARY_NAME_EMPTY}
	 */
	Vocabulary changeName(Vocabulary vocabulary, String newName);

	/**
	 * Change {@link Vocabulary} URI.
	 * @param vocabulary The {@link Vocabulary}.
	 * @param newUri The new URI.
	 * @return Changed {@link Vocabulary}.
	 */
	Vocabulary changeUri(Vocabulary vocabulary, String newUri);

	/**
	 * Change the {@link Community} of the {@link Vocabulary}.
	 * @param vocabulary The {@link Vocabulary}
	 * @param newCommunity The new {@link Community}
	 * @return Updated {@link Vocabulary}
	 */
	Vocabulary changeCommunity(Vocabulary vocabulary, Community newCommunity);

	/**
	 * Change specified {@link CharacteristicForm}.
	 * @param cf The {@link CharacteristicForm}.
	 * @param term The {@link Term}.
	 * @param roleSignifier The role name.
	 * @return Modified {@link CharacteristicForm}.
	 * @throw {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#CF_NULL},
	 *        {@link DGCErrorCodes#CF_ROLE_NULL}, {@link DGCErrorCodes#CF_ROLE_EMPTY}
	 */
	CharacteristicForm changeCharacteristicForm(CharacteristicForm cf, Term term, String roleSignifier);

	/**
	 * Change the specified {@link BinaryFactTypeForm}.
	 * @param bftf The {@link BinaryFactTypeForm}.
	 * @param headTerm The head {@link Term}.
	 * @param role The role.
	 * @param corole The corole.
	 * @param tailTerm The tail {@link Term}.
	 * @return Modified {@link BinaryFactTypeForm}.
	 * @throw {@link IllegalArgumentException} with error codes {@link DGCErrorCodes#BFTF_NULL},
	 *        {@link DGCErrorCodes#TERM_NULL}, {@link DGCErrorCodes#BFTF_ROLE_NULL},
	 *        {@link DGCErrorCodes#BFTF_ROLE_EMPTY}, {@link DGCErrorCodes#BFTF_COROLE_NULL},
	 *        {@link DGCErrorCodes#BFTF_COROLE_EMPTY}
	 */
	BinaryFactTypeForm changeBinaryFactTypeForm(BinaryFactTypeForm bftf, Term headTerm, String role, String corole,
			Term tailTerm);

	/***********************
	 * All the save methods
	 ***********************/

	/**
	 * Persists the {@link Representation}, takes care of the concrete type of the representation.
	 * @param rep The {@link Representation} to be persisted.
	 * @return The persisted {@link Representation}.
	 */
	Representation save(Representation rep);

	/**
	 * Allows you to pass your newly created vocabulary and will persist it.
	 * 
	 * @param vocabulary Vocabulary to persist as a new vocabulary.
	 * @return Vocabulary with the identifier set.
	 */
	Vocabulary saveVocabulary(Vocabulary vocabulary);

	/**
	 * Persists a newly created {@link Term}.
	 * 
	 * @param term The {@link Term} to persist.
	 * @return A Term that is designated for the general concept.
	 */
	Term saveTerm(Term term);

	/**
	 * Persists a newly created {@link CharacteristicForm}.
	 * @param characteristicForm The {@link CharacteristicForm} to persist.
	 * @return The persisted {@link CharacteristicForm}.
	 */
	CharacteristicForm saveCharacteristicForm(CharacteristicForm characteristicForm);

	/**
	 * Persists a newly created {@link BinaryFactTypeForm}.
	 * 
	 * @param binaryFactTypeForm The {@link BinaryFactTypeForm} to persist.
	 * @return The persisted {@link BinaryFactTypeForm}.
	 */
	BinaryFactTypeForm saveBinaryFactTypeForm(BinaryFactTypeForm binaryFactTypeForm);

	/*************************
	 * All the remove methods
	 *************************/

	/**
	 * Remove the representation as a synonym (i.e. changes its {@link Concept} to a new {@link Concept}).
	 * @param representation The {@link Representation}
	 * @return The updated {@link Representation}
	 */
	Representation removeSynonym(Representation representation);

	/**
	 * Removes the given vocabulary from the persistent storage.
	 * 
	 * @param vocabulary Vocabulary to remove.
	 */
	void remove(Vocabulary vocabulary);

	/**
	 * Removes the given term.
	 * @param term {@link Term} to remove
	 */
	void remove(Term term);

	/**
	 * Removes specified {@link Term}s.
	 * @param terms The {@link Term}s.
	 */
	void remove(Collection<Term> terms);

	/**
	 * Removes the given {@link CharacteristicForm} from the persistent storage.
	 * @param cForm The {@link CharacteristicForm} to remove
	 */
	void remove(CharacteristicForm cForm);

	/**
	 * Removes the given {@link BinaryFactTypeForm} from the persistent storage.
	 * @param bftf The {@link BinaryFactTypeForm} to remove
	 */
	void remove(BinaryFactTypeForm bftf);

	/**
	 * Removes specified {@link Representation}.
	 * @param representation The {@link Representation} to be removed.
	 */
	void remove(Representation representation);

	/**********************************
	 * All the find/get/search methods
	 **********************************/

	/**
	 * Gets all the vocabularies that exist.
	 * 
	 * @return All {@link Vocabulary}s.
	 */
	List<Vocabulary> findVocabularies();

	/**
	 * Gets the {@link Vocabulary} with the given uri.
	 * 
	 * @param uri The URI of the {@link Vocabulary}.
	 * @return The {@link Vocabulary}.
	 */
	Vocabulary findVocabularyByUri(String uri);

	/**
	 * Gets the {@link Vocabulary} with the given name
	 * 
	 * @param name The name of the {@link Vocabulary}
	 * @return The {@link Vocabulary}
	 */
	Vocabulary findVocabularyByName(String name);

	/**
	 * Returns a list of vocabularies where a given user plays a given role on the vocabulary, sorted DESC by vocabulary
	 * name
	 * @param username The name of the user
	 * @param role The role that the user plays in the vocabulary
	 * @param start The start index of vocabularies to retrieve
	 * @param end The end index of vocabularies to retrieve. Set end to 0 to return all results
	 * @return
	 */
	List<Vocabulary> findVocabulariesByMember(String username, Role role, int start, int end);

	/**
	 * Returns a list of vocabularies where a given user plays any role on the vocabulary, sorted DESC by vocabulary
	 * name
	 * @param username The name of the user
	 * @param role The role that the user plays in the vocabulary
	 * @param start The start index of vocabularies to retrieve
	 * @param end The end index of vocabularies to retrieve. Set end to 0 to return all results
	 * @return
	 */
	List<Vocabulary> findVocabulariesByMember(String username, int start, int end);

	/**
	 * Returns a {@link List} of {@link Vocabulary} to incorporate.
	 * @param currentVocabulary The {@link Vocabulary}
	 * @return {@link List} of {@link Vocabulary}
	 */
	List<Vocabulary> findVocabulariesToInCorporate(Vocabulary currentVocabulary);

	/**
	 * Get the target {@link Vocabulary}s to move {@link Representation}.
	 * @param represenation The {@link Representation} to be moved.
	 * @return The {@link Vocabulary}s
	 */
	Collection<Vocabulary> findVocabulariesToMove(Representation represenation);

	/**
	 * Gets the {@link Vocabulary} by its resource identifier.
	 * 
	 * @param id The resource identifier for the {@link Vocabulary}.
	 * @return the {@link Vocabulary}
	 */
	Vocabulary findVocabularyByResourceId(String id);

	/**
	 * Retrieves all the vocabularies in which the given vocabulary is incorporated in.
	 * @param vocabulary The vocabulary that is being incorporated.
	 * @return The {@link Vocabulary}s in which the given vocabulary is incorporated in.
	 */
	List<Vocabulary> findAllIncorporatingVocabularies(Vocabulary vocabulary);

	/**
	 * Retrieves the bootstrapped SBVR Meaning and Representation vocabulary. If this returns null, make sure
	 * bootstrapper was run.
	 * @return SBVR Meaning and Representation vocabulary
	 */
	Vocabulary findSbvrMeaningAndRepresentationVocabulary();

	/**
	 * Finds the {@link Term} by its resource identifier.
	 * 
	 * @param id The resource identifier of the {@link Term}.
	 * @return The {@link Term}.
	 */
	Term findTermByResourceId(String id);

	List<Term> findPureTermsByVocabulary(Vocabulary vocabulary);

	/**
	 * Search for all {@link Term} that have (part of) the expression as signifier. In contrast to
	 * {@link findAllTermsBySignifier} which will return the list of terms that have an exact expression match, this
	 * method will return all the terms that have a partial string match. Pagination of the results can be done using
	 * the 2 last parameters.
	 * 
	 * @param expression The expression that is being represented in string form
	 * @param offset the index in the list of results (0-based) of the first result retrieved by this function
	 * @param number the maximum number of results to return; if 0, then all results will be returned
	 * @return The list of matching Terms.
	 */
	public List<Term> searchTermsBySignifier(String expression, int offset, int number);

	/**
	 * Find a list of terms by matching a partial signifier
	 * 
	 * @param vocabulary search for the terms in a vocabulary
	 * @param partialSignifier match to a signifier
	 * @return return a list of terms that match the partial signifier
	 */
	List<Term> searchTermsBySignifier(Vocabulary voc, final String partialSignifier);

	/**
	 * Find a list of terms by matching a partial signifier in the vocabulary and all of its non-sbvr incorporating
	 * vocabularies
	 * 
	 * @param vocabulary search for the terms in a vocabulary and all of its non-sbvr incorporating vocabularies
	 * @param partialSignifier match to a signifier
	 * @return return a list of terms that match the partial signifier
	 */
	List<Term> searchTermsBySignifier(Vocabulary voc, final String partialSignifier, boolean includeIncorporatedVocs);

	/**
	 * Search for all {@link Attribute}s that have (part of) the expression as longExpression. Pagination of the results
	 * can be done using the 2 last parameters.
	 * 
	 * @param partialExpression The expression that is being represented in string form
	 * @param offset the index in the list of results (0-based) of the first result retrieved by this function
	 * @param number the maximum number of results to return; if 0, then all results will be returned
	 * @return The list of matching Attributes.
	 */
	public List<Attribute> searchAttributesByLongExpression(final String partialExpression, int offset, int number);

	/**
	 * Searches for all {@link Vocabulary} that have (part of) {@code name} as a name. Pagination of the results can be
	 * done using the 2 last parameters.
	 * 
	 * @param name the (partial) name to search for
	 * @param offset the index in the list of results (0-based) of the first result retrieved by this function
	 * @param number the maximum number of results to return; if 0, then all results will be returned
	 * @return the list of vocabularies whose name match {@code name}
	 */
	public List<Vocabulary> searchVocabulariesForName(String name, int offset, int number);

	/**
	 * Search for all {@link Term} that have the exact expression as signifier.
	 * @param expression The expression that is being represented in string form
	 * 
	 * @return The list of matching Terms.
	 */
	List<Term> findAllTermsBySignifier(String expression, boolean capitalSensitive);

	/**
	 * Return all the status terms for a given representation. The visibility is determined by the concept type,
	 * vocabulary and communities this representation is part of.
	 * @param rep the representation to find the status terms for.
	 * @return The list of status {@link Term}s.
	 */
	List<Term> findStatusTerms(Representation rep);

	/**
	 * Return all the status terms in the glossary.
	 * @return The list of status {@link Term}s.
	 */
	List<Term> findStatusTerms();

	/**
	 * @return A List containing the vocabulary IDs of all the "meta" vocabularies. Meta-vocabularies are vocabularies
	 *         that are located under the Metamodel community.
	 */
	List<String> findMetaVocabularyIDs();

	/**
	 * @param excludeMeta If true, all the 'meta' vocabularies are excluded. Meta-vocabularies are vocabularies that are
	 *            located under the Metamodel community.
	 * @return A List of vocabularies
	 */
	List<Vocabulary> findVocabularies(boolean excludeMeta);

	/**
	 * Find the preferred {@link Term} in the {@link Vocabulary} for the given {@link ObjectType}.
	 * @param objectType {@link ObjectType} to find the {@link Term} for.
	 * @param vocabulary The {@link Vocabulary} that should contain the {@link Term}.
	 * @return The preferred {@link Term}
	 */
	Term findPreferredTerm(ObjectType objectType, Vocabulary vocabulary);

	/**
	 * Find the preferred {@link Term} in the {@link Vocabulary} and all the {@link Vocabulary}s it incorporates for the
	 * given {@link ObjectType}.
	 * @param objectType {@link ObjectType} to find the {@link Term} for.
	 * @param vocabulary The {@link Vocabulary} that should contain the {@link Term}.
	 * @return The preferred {@link Term}
	 */
	Term findPreferredTermInAllIncorporatedVocabularies(ObjectType objectType, Vocabulary vocabulary);

	/**
	 * Get preferred {@link Term} for the specified {@link ObjectType}.
	 * @param objectType The {@link ObjectType}.
	 * @return The preferred {@link Term}.
	 */
	Term findPreferredTerm(ObjectType objectType);

	/**
	 * Find the preferred {@link CharacteristicForm} in the {@link Vocabulary} for the given {@link IndividualConcept}.
	 * @param characteristic {@link Characteristic} to find the {@link CharacteristicForm} for.
	 * @param vocabulary The {@link Vocabulary} that should contain the {@link CharacteristicForm}.
	 * @return The preferred {@link CharacteristicForm}
	 */
	CharacteristicForm findPreferredCharacteristicForm(Characteristic characteristic, Vocabulary vocabulary);

	/**
	 * Find the preferred {@link BinaryFactTypeForm} in the {@link Vocabulary} for the given {@link BinaryFactType}.
	 * @param binaryFactType {@link BinaryFactType} to find the {@link BinaryFactTypeForm} for.
	 * @param vocabulary The {@link Vocabulary} that should contain the {@link BinaryFactTypeForm}.
	 * @return The preferred {@link BinaryFactTypeForm}
	 */
	BinaryFactTypeForm findPreferredBinaryFactTypeForm(BinaryFactType binaryFactType, Vocabulary vocabulary);

	/**
	 * Finds all the synonyms for the given {@link Representation}.
	 * @param representation The representation to find synonyms for.
	 * @return A list of synonymous representations.
	 */
	List<Representation> findSynonyms(Representation representation);

	/**
	 * Finds all the synonyms for the given {@link Term}.
	 * @param term The term to find synonyms for.
	 * @return A list of synonymous terms.
	 */
	List<Term> findSynonyms(Term term);

	/**
	 * Finds the synonymous forms of the given {@link CharacteristicForm} across all {@link Vocabulary}s.
	 * @param cForm The {@link CharacteristicForm} to find synonyms for.
	 * @return The list of synonymous forms.
	 */
	List<CharacteristicForm> findSynonymousForms(CharacteristicForm cForm);

	/**
	 * Finds the synonymous forms of the given {@link BinaryFactTypeForm} across all {@link Vocabulary}s.
	 * @param bftf The {@link BinaryFactTypeForm} to find synonyms for.
	 * @return The list of synonymous forms.
	 */
	List<BinaryFactTypeForm> findSynonymousForms(BinaryFactTypeForm bftf);

	/**
	 * Finds the {@link Term} with the given expression.
	 * 
	 * @param vocabulary Vocabulary that contains the {@link Term}.
	 * @param signfifer The signifier that expresses the {@link Term}.
	 * @return The {@link Term}
	 */
	Term findTermBySignifier(Vocabulary vocabulary, String signifier);

	/**
	 * Finds the {@link Term} with the given expression in the specified {@link Vocabulary} and all its incorporated
	 * vocabularies.
	 * 
	 * @param vocabulary Vocabulary that contains the {@link Term} and its subsequent vocabularies.
	 * @param signfifer The signifier that expresses the {@link Term}.
	 * @return The {@link Term}
	 */
	Term findTermBySignifierInAllIncorporatedVocabularies(Vocabulary vocabulary, String signifier);

	/**
	 * Find the {@link Term} with the given signifier in the vocabulary and all its incorporated vocabularies. If no one
	 * is found, create a new {@link Term} in the specified {@link Vocabulary}.
	 * @param vocabulary The vocabulary that could contain the {@link Term} (or one of its incorporated vocabularies)
	 * @param signifier The {@link Term} signifier
	 * @return The {@link Term} found or created
	 */
	Term findTermBySignifierAndCreateIfNotExists(Vocabulary vocabulary, String signifier);

	/**
	 * Looks up the custom {@link Attribute}s for the {@link Meaning} represented by the {@link Representation}. This is
	 * limited to those {@link Attribute}s in the same {@link Vocabulary} as the {@link Representation}.
	 * @param representation The {@link Representation} of the {@link Meaning} for which to find the custom
	 *            {@link Attribute}s.
	 * 
	 * @param attributeTypes The {@link ObjectType}s that are the type of the custom attributes requested.
	 * @return A list of tuples {@link Attribute}s.
	 */
	List<List<Attribute>> findAttributesForRepresentation(Representation representation, ObjectType... types);

	/**
	 * Finds the {@link Term} by its resource identifier.
	 * 
	 * @param resourceId The resource identifier of the {@link Term}.
	 * @return The {@link Term}.
	 */
	CharacteristicForm findCharacteristicFormByResourceId(String resourceId);

	/**
	 * Finds the {@link CharacteristicForm} by the representation of it elements.
	 * @param vocabulary {@link Vocabulary} that contains the {@link CharacteristicForm}
	 * @param term The {@link Term} representation
	 * @param roleExpression The expression for the role.
	 * @return The {@link CharacteristicForm}
	 */
	CharacteristicForm findCharacteristicFormByRepresentation(Vocabulary vocabulary, Term term, String roleExpression);

	/**
	 * Finds the {@link BinaryFactTypeForm} by its resource identifier.
	 * 
	 * @param resourceId The resource identifier of the {@link BinaryFactTypeForm}.
	 * @return The {@link BinaryFactTypeForm}.
	 */
	BinaryFactTypeForm findBinaryFactTypeFormByResourceId(String resourceId);

	/**
	 * Find the {@link BinaryFactTypeForm}s that contain the given {@link Term} as head {@link Term}.
	 * @param term The head {@link Term}
	 * @return The list of {@link BinaryFactTypeForm}s.
	 */
	List<BinaryFactTypeForm> findBinaryFactTypeFormsContainingHeadTerm(Term term);

	/**
	 * Find the {@link BinaryFactTypeForm}s that contain the given {@link Term} as tail {@link Term}.
	 * @param term The tail {@link Term}
	 * @return The list of {@link BinaryFactTypeForm}s.
	 */
	List<BinaryFactTypeForm> findBinaryFactTypeFormsContainingTailTerm(Term term);

	/**
	 * Finds the {@link BinaryFactTypeForm} by the representation of its constituent parts.
	 * @param vocabulary {@link Vocabulary} that contains the {@link BinaryFactTypeForm}
	 * @param headTerm The head {@link Term}.
	 * @param roleExpression The expression for the role.
	 * @param coRoleExpression The expression for the corole.
	 * @param tailTerm The tail {@link Term}.
	 * @return The {@link BinaryFactTypeForm}.
	 */
	BinaryFactTypeForm findBinaryFactTypeFormByRepresentation(Vocabulary vocabulary, Term headTerm,
			String roleExpression, String coRoleExpression, Term tailTerm);

	/**
	 * Finds the representation by the given resource id.
	 * @param resourceId The resourceId of the {@link Representation}.
	 * @return The {@link Representation} with the given resourceId
	 */
	Representation findRepresentationByResourceId(String resourceId);

	/**
	 * Given a {@link Representation} this returns the signifiers of the general concepts of its {@link Concept} as
	 * preferred in this {@link Vocabulary}.
	 * 
	 * @param representation {@link Representation} to get the specialized concepts for as signifiers
	 * @return The {@link Representation} of the general concept
	 */
	Representation findGeneralConceptRepresentation(Representation representation);

	/**
	 * Given a {@link Representation} this returns the list of signifiers of the general concepts of its {@link Concept}
	 * up to {@link ObjectType}.
	 * 
	 * @param representation {@link Representation} to get the specialized concepts for as signifiers
	 * @return The list of {@link Representation}s of the general concepts
	 */
	List<Representation> findAllGeneralConceptRepresentation(Representation representation);

	/**
	 * Given a {@link Representation} this returns the signifiers of the specialized concepts of its {@link Concept} as
	 * preferred in this {@link Vocabulary}.
	 * 
	 * @param representation {@link Representation} to get the specialized concepts for as signifiers
	 * @param limit The result size limit.
	 * @return The list of {@link Representation}s of the specialized concepts
	 */
	List<Representation> findSpecializedConceptRepresentations(Representation representation, int limit);

	/**
	 * Given a {@link Representation} this returns the {@link Representation}s of the specialized concepts of its
	 * {@link Concept} as preferred in its {@link Vocabulary}. Limits results to only specialized concepts represesented
	 * in the same {@link Vocabulary}.
	 * 
	 * @param representation {@link Representation} to get the specialized concepts for as signifiers
	 * @return The list of {@link Representation}s of the specialized concepts in the same vocabulary as the given
	 *         {@link Representation}.
	 */
	List<Representation> findSpecializedConceptRepresentationsInSameVocabulary(Representation representation);

	/**
	 * Find the preferred {@link Representation} in the {@link Vocabulary} for the given {@link Concept}.
	 * @param concept {@link Concept} to find the {@link Representation} for.
	 * @param vocabulary The {@link Vocabulary} that should contain the {@link Representation}.
	 * @return The preferred {@link Representation}
	 */
	Representation findPreferredRepresentation(Concept concept, Vocabulary vocabulary);

	/**
	 * Find the preferred {@link Representation} in the {@link Vocabulary} and all its incorporated {@link Vocabulary}s
	 * for the given {@link Concept}.
	 * @param concept {@link Concept} to find the {@link Representation} for.
	 * @param vocabulary The {@link Vocabulary} that should contain the {@link Representation} (or its incorporates
	 *            vocabularies).
	 * @return The preferred {@link Representation}
	 */
	Representation findPreferredRepresentationInAllIncorporatedVocabularies(Concept concept, Vocabulary vocabulary);

	List<Term> findAllTermsForObjectType(ObjectType oType);

	/**
	 * Find all {@link BinaryFactTypeForm}s
	 * @return All {@link BinaryFactTypeForm}s
	 */
	List<BinaryFactTypeForm> findAllBinaryFactTypeForms();

	/**
	 * Find all {@link CharacteristicForm}s
	 * @return All {@link CharacteristicForm}s
	 */
	List<CharacteristicForm> findAllCharacteristicForms();

	/**
	 * Find all the {@link Term}s defined in the glossary.
	 * @return all the {@link Term}s defined in the glossary.
	 */
	List<Term> findAllTerms();

	/**
	 * Find the preferred {@link Term}s in the {@link Vocabulary} for all {@link ObjectType} represented in the
	 * {@link Vocabulary}.
	 * @param vocabulary The {@link Vocabulary} to find all preferred terms for.
	 */
	List<Term> findAllPreferredTerms(Vocabulary vocabulary);

	/**
	 * Find the preferred {@link Term}s in the {@link Vocabulary} and all the {@link Vocabulary}s it incorporates for
	 * all {@link ObjectType} represented in the {@link Vocabulary} and the {@link Vocabulary}s it incorporates.
	 * @param vocabulary The {@link Vocabulary} to find all preferred terms for.
	 */
	List<Term> findAllPreferredTermsInAllIncorporatedVocabularies(Vocabulary vocabulary);

	/**
	 * Retrieves the bootstrapped SBVR Logical Formulations Vocabulary. If this returns null, make sure bootstrapper was
	 * run.
	 * @return SBVR Logical Formulations Vocabulary
	 */
	Vocabulary findSbvrLogicalFormulationsVocabulary();

	/**
	 * Retrieves the bootstrapped SBVR Vocabulary. If this returns null, make sure bootstrapper was run.
	 * @return SBVR Vocabulary
	 */
	Vocabulary findSbvrVocabulary();

	/**
	 * Retrieves the bootstrapped Collibra roles and responsibilities {@link Vocabulary}.
	 * @return Collibra roles and responsibilities {@link Vocabulary}.
	 */
	Vocabulary findRolesAndResponsibilitiesVocabulary();

	/**
	 * Finds all {@link CharacteristicForm}s that contain the given term.
	 * 
	 * @param term the Term to look for
	 * @return {@link CharacteristicForm}s containing the term
	 */
	List<CharacteristicForm> findCharacteristicFormsContainingTerm(Term term);

	List<Representation> findRepresentationsReferringTerm(Term term);

	/**
	 * Finds the {@link BinaryFactTypeForm}s that contain the given {@link Term} as either head or tail {@link Term}.
	 * @param term {@link Term} that is either head or tail of the {@link BinaryFactTypeForm}.
	 * @return The list of {@link BinaryFactTypeForm}s
	 */
	List<BinaryFactTypeForm> findBinaryFactTypeFormsReferringTerm(Term term);

	List<BinaryFactTypeForm> findAllBinaryFactTypeFormsReferringTerm(Term term);

	List<CharacteristicForm> findCharacteristicFormsReferringTerm(Term term);

	Vocabulary findSbvrBusinessRulesVocabulary();

	Vocabulary findSbvrCollibraExtensionsVocabulary();

	Vocabulary findStatusesVocabulary();

	Vocabulary findAttributeTypesVocabulary();

	Vocabulary findSbvrBusinessVocabulary();

	BinaryFactTypeForm findBinaryFactTypeForm(Vocabulary vocabulary, Term headTerm, String role, String corole,
			Term tailTerm);

	/**
	 * Searches for all the {@link BinaryFactTypeForm}s that are the same but in different vocabularies.
	 */
	List<BinaryFactTypeForm> findBinaryFactTypeForms(Term headTerm, String role, String corole, Term tailTerm);

	CharacteristicForm findCharacteristicForm(Vocabulary vocabulary, Term term, String roleExpression);

	/**
	 * Get {@link Vocabulary}, if not found throw exception.
	 * @param vocabularyResourceId The vocabulary resource id.
	 * @return The {@link Vocabulary}.
	 */
	Vocabulary getVocabularyWithError(String vocabularyResourceId);

	/**
	 * Get {@link Term}, if not found throw exception.
	 * @param resourceId The resource id.
	 * @return {@link Term}.
	 */
	Term getTermWithError(String resourceId);

	/**
	 * Get {@link Representation}, if not found throw exception.
	 * @param resourceId The resource id.
	 * @return {@link Representation}.
	 */
	Representation getRepresentationWithError(String resourceId);

	/**
	 * Get {@link BinaryFactTypeForm}, if not found throw exception.
	 * @param resourceId The resource id.
	 * @return {@link BinaryFactTypeForm}.
	 */
	BinaryFactTypeForm getBftfWithError(String resourceId);

	/**
	 * Get {@link CharacteristicForm}, if not found throw exception.
	 * @param resourceId The resource id.
	 * @return {@link CharacteristicForm}.
	 */
	CharacteristicForm getCfWithError(String resourceId);

	/**
	 * To find all the derived {@link BinaryFactTypeForm}s involving the given {@link Term}.
	 * @param term The {@link Term} for which the derived {@link BinaryFactTypeForm}s should be get.
	 * @return The derived {@link BinaryFactTypeForm}s.
	 */
	Collection<BinaryFactTypeForm> findDerivedFacts(Term term);

	BinaryFactTypeForm findBinaryFactTypeFormByExpression(Vocabulary vocabulary, String signifier, String role,
			String coRole, String signifier2);

	CharacteristicForm findCharacteristicFormByExpression(Vocabulary vocabulary, String signifier, String role);

	/**
	 * Gets the mapping of signifiers to terms for the given vocabularies.
	 * @param vocabularies The vocabularies to get the map for.
	 * @return <Signifier, Term> Map
	 */
	Map<String, Term> getSignifierToTermMap(Collection<Vocabulary> vocabularies);

	/**
	 * Gets the mapping of signifiers to terms for the given vocabulary.
	 * @param vocabularies The vocabulary to get the map for.
	 * @return <Signifier, Term> Map
	 */
	Map<String, Term> getSignifierToTermMap(Vocabulary vocabulary);

	/**
	 * Each terms has an {@link ObjectType} as its meaning, that {@link ObjectType} also has a type. This returns the
	 * terms where the {@link ObjectType} is of the given type.
	 * 
	 * @return All terms whose objecttype's type is the given one
	 */
	List<Term> findTermsByType(ObjectType type);

	/**
	 * Get all {@link Representation}s that depend on the specified {@link Term}.
	 * <p>
	 * Includes all {@link BinaryFactTypeForm}s, {@link CharacteristicForm}s, {@link Name}s, specialized {@link Concept}
	 * {@link Representation}s and synonyms.
	 * @param term The {@link Term}
	 * @return The {@link Representation}s.
	 */
	Collection<Representation> findAllRelatedRepresentations(Term term);

	/**
	 * Given a {@link Representation} this returns the term of the concept type of its {@link Concept} as preferred in
	 * this {@link Vocabulary}.
	 * 
	 * @param representation {@link Representation} to get the concept type for as a {@link Representation}
	 * @return The {@link Term} of the concept type.
	 */
	Term findConceptTypeRepresentation(Representation representation);

	/**
	 * Retrieve the bootstrapped ConceptTypes Vocabulary
	 * @return Concept Types {@link Vocabulary}
	 */
	Vocabulary findMetamodelExtensionsVocabulary();

	/**
	 * Create a Concept Type with the given Signifier
	 * @param termSignifier The signifier for the Term that will become a concept type
	 * @return The created term
	 */
	Term createConceptType(String termSignifier);

	/**
	 * Create a Concept Type with the given Signifier and the given parent as General Concept.
	 * @param termSignifier The signifier for {@link Term} that will become a concept type
	 * @param parent The general concept of the term that will become a concept type.
	 * @return The created term
	 */
	Term createConceptType(String termSignifier, ObjectType parent);

}
