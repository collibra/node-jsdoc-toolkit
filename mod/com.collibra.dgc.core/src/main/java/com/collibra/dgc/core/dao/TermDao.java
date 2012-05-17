package com.collibra.dgc.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

public interface TermDao extends AbstractDao<Term> {

	/**
	 * Use for increased performance (scalar query)
	 * 
	 * @see #findTermBySignifier(Vocabulary, String)
	 */
	Long findTermIdBySignifier(Vocabulary vocabulary, String signifier);

	/**
	 * Looks up a {@link Term} by its Signifier.
	 * @param vocabulary The vocabulary.
	 * @param signifier The expression that is being represented in string form.
	 * 
	 * @return The {@link Term}.
	 */
	Term findTermBySignifier(Vocabulary vocabulary, String signifier);

	/**
	 * Search for all {@link Term} that have (part of) the signifier. In contrast to {@link findAllTermsBySignifier}
	 * which will return the list of terms that have an exact expression match, this method will return all the terms
	 * that have a partial string match. The results can be paginated using the last 2 parameters.
	 * @param expression The expression that is being represented in string form
	 * @param offset the index in the list of results (0-based) of the first result retrieved by this function
	 * @param number the maximum number of results to return; if 0, then all results will be returned
	 * @return The list of matching Terms.
	 */
	List<Term> searchTermsBySignifier(String expression, int offset, int number);

	/**
	 * Find a list of terms by matching a partial signifier in a list of vocabularies
	 * 
	 * @param vocs a list of {@link Vocabulary}s that the terms should be part of
	 * @param partialSignifier match to a signifier
	 * @return return a list of terms that match the partial signifier
	 */
	List<Term> searchTermsBySignifier(Collection<Vocabulary> vocs, String partialSignifier);

	/**
	 * List all {@link Term} that have the exact same signifier.
	 * @param expression The expression that is being represented in string form
	 * 
	 * @return The list of matching Terms.
	 */
	List<Term> findAllTermsBySignifier(String expression, boolean capitalSensitive);

	List<Term> findTermsByObjectType(ObjectType oType);

	/**
	 * Find the preferred {@link Term} in the {@link Vocabulary} for the given {@link ObjectType}.
	 * @param objectType {@link ObjectType} to find the {@link Term} for.
	 * @param vocabulary The {@link Vocabulary} that should contain the {@link Term}.
	 * @return The preferred {@link Term}
	 */
	Term findPreferredTerm(ObjectType objectType, Vocabulary vocabulary);

	/**
	 * Find a list of terms by matching a partial signifier
	 * 
	 * @param vocabulary search for the terms in a vocabulary
	 * @param partialSignifier match to a signifier
	 * @return return a list of terms that match the partial signifier
	 */
	List<Term> searchTermsBySignifier(Vocabulary vocabulary, String partialSignifier);

	/**
	 * Find a list of terms by matching a partial signifier in the vocabulary and all of its non-sbvr incorporating
	 * vocabularies
	 * 
	 * @param vocabulary search for the terms in a vocabulary and all of its non-sbvr incorporating vocabularies
	 * @param partialSignifier match to a signifier
	 * @return return a list of terms that match the partial signifier
	 */
	List<Term> searchTermInAllNonSBVRIncorporatedVocabularies(Vocabulary vocabulary, String partialSignifier);

	List<Term> findSynonyms(Term term);

	/**
	 * Finds all the terms in the {@link Vocabulary}.
	 * @param vocabulary The {@link Vocabulary} to get the terms for.
	 * @return The {@link Term}s in the {@link Vocabulary}.
	 */
	List<Term> findTermsByVocabulary(Vocabulary vocabulary);

	/**
	 * Finds all the terms in the given {@link Vocabulary}s.
	 * @param vos a {@link Collection} of vocabularies
	 * @return a list of {@link Term}s.
	 */
	List<Term> findTermsByVocabularies(Collection<Vocabulary> vocs);

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

	List<Term> findPureTermsByVocabulary(Vocabulary vocabulary);

	String findTermResourceIdBySignifier(Vocabulary vocabulary, String signifier);

	Map<Term, Integer> findMembersCount(String roleName);

	Map<Term, Integer> findMembersCount(Vocabulary vocabulary);

	Map<Term, Integer> findMembersCount(Vocabulary vocabulary, String roleName);

	Map<Term, Integer> findMembersCount();

	List<Term> searchTermsBySignifier(Vocabulary voc, String partialSignifier, ObjectType type);

	List<Term> searchTermInAllNonSBVRIncorporatedVocabularies(Vocabulary voc, String partialSignifier, ObjectType type);

	List<Object[]> findNumberOfSpecializedConceptsPerTerm(Vocabulary vocabulary);

	List<Term> findTermsByType(ObjectType type);

	/**
	 * Get all {@link Term} that do not have concept type.
	 * @return The {@link Term}s.
	 */
	Collection<Term> findTermsWithoutConceptType();

	/**
	 * Get all {@link Term}s having no {@link Meaning}.
	 * @return The {@link Term}s.
	 */
	Collection<Term> findTermsWithoutMeaning();

	/**
	 * 
	 * @return
	 */
	Collection<Term> findTermsWithNoMeaning();
}