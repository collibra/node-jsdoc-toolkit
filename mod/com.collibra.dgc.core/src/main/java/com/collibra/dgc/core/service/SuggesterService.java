package com.collibra.dgc.core.service;

import java.util.Collection;
import java.util.List;

import org.activiti.engine.repository.ProcessDefinition;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Designation;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.user.Member;

public interface SuggesterService {

	/**
	 * Returns a list of {@link Term}s that represent the suggested synonym for the given {@link Term} from all the
	 * latest {@link Vocabulary}.
	 * 
	 * @param term {@link Term} to get the suggestions for.
	 * @param signifierStartsWith partial signifier for synonym
	 * @return The list of suggested {@link Term}s.
	 */
	List<Term> suggestSynonymsForTerm(Term term, String signifierStartsWith);

	/**
	 * Returns a list of {@link BinaryFactTypeForm}s that represent the suggested synonym for the given
	 * {@link BinaryFactTypeForm} from all the latest {@link Vocabulary}.
	 * 
	 * @param bftForm {@link BinaryFactTypeForm} to get the suggestions for.
	 * @param signifierStartsWith partial signifier for synonym
	 * @return The list of suggested {@link BinaryFactTypeForm}s.
	 */
	List<BinaryFactTypeForm> suggestSynonymousFormsForBinaryFactTypeForm(BinaryFactTypeForm bftForm,
			String signifierStartsWith);

	/**
	 * Returns a list of {@link CharacteristicForm}s that represent the suggested synonym for the given
	 * {@link CharacteristicForm} from all the latest {@link Vocabulary}.
	 * 
	 * @param cForm {@link CharacteristicForm} to get the suggestions for.
	 * @param signifierStartsWith partial signifier for synonym
	 * @return The list of suggested {@link CharacteristicForm}s.
	 */
	List<CharacteristicForm> suggestSynonymousFormsForCharacteristicForm(CharacteristicForm cForm,
			String signifierStartsWith);

	/**
	 * Returns a list of {@link BinaryFactTypeForm}s that are possible candidates to specialize.
	 * @param binaryFactTypeForm The {@link BinaryFactTypeForm} you would like to specialize
	 * @return THe list list of specializable {@link BinaryFactTypeForm}s.
	 */
	List<BinaryFactTypeForm> suggestSpecializableBinaryFactTypeForms(BinaryFactTypeForm binaryFactTypeForm);

	/**
	 * Returns a list of {@link Term}s that represent the suggested concept type for the given {@link Term}
	 * 
	 * @param term The {@link Term} to get the suggestions for.
	 * @param signifierStartsWith partial verbalisation for a concept
	 * @return The list of suggested {@link Term}s.
	 */
	List<Term> suggestConceptTypeTerms(Term term, String signifierStartsWith);

	/**
	 * Returns a list of {@link Term}s that represent the suggested concept type for the given
	 * {@link CharacteristicForm}
	 * 
	 * @param cForm The {@link CharacteristicForm} to get the suggestions for.
	 * @param signifierStartsWith partial verbalisation for a concept
	 * @return The list of suggested {@link Term}s.
	 */
	List<Term> suggestConceptTypeTerms(CharacteristicForm cForm, String signifierStartsWith);

	/**
	 * Returns a list of {@link BinaryFactTypeForm}s that represent the suggested concept type for the given
	 * {@link BinaryFactTypeForm}
	 * 
	 * @param bftf The {@link BinaryFactTypeForm} to get the suggestions for.
	 * @param signifierStartsWith partial verbalisation for a concept
	 * @return The list of suggested {@link Term}s.
	 */
	List<Term> suggestConceptTypeTerms(BinaryFactTypeForm bftf, String signifierStartsWith);

	/**
	 * Returns a list of {@link Term}s that represent the suggested specialized concept for the given {@link Term}
	 * excludes the {@link Term}s from SBVR and its incorporated {@link Vocabulary}s.
	 * @param term {@link Term} to get the suggestions for.
	 * @param signifierStartsWith partial signifier for general concept
	 * @return The list of suggested {@link Term}s.
	 */
	List<Term> suggestSpecializedTerms(Term term, String signifierStartsWith);

	/**
	 * Returns a list of {@link BinaryFactTypeForm}s that represent the suggested specialized concept for the given
	 * {@link BinaryFactTypeForm} excludes the {@link Term}s from SBVR and its incorporated {@link Vocabulary}s.
	 * @param bftForm {@link BinaryFactTypeForm} to get the suggestions for.
	 * @param signifierStartsWith partial signifier for general concept
	 * @return The list of suggested {@link BinaryFactTypeForm}s.
	 */
	List<BinaryFactTypeForm> suggestSpecializedBinaryFactTypeForms(BinaryFactTypeForm bftForm,
			String signifierStartsWith);

	/**
	 * Returns a list of {@link Term}s that represent the suggested specialized concept for the given
	 * {@link CharacteristicForm} excludes the {@link Term}s from SBVR and its incorporated {@link Vocabulary}s.
	 * 
	 * @param cForm {@link CharacteristicForm} to get the suggestions for.
	 * @param signifierStartsWith partial signifier for general concept
	 * @return The list of suggested {@link Term}s.
	 */
	List<CharacteristicForm> suggestSpecializedCharacteristicForms(CharacteristicForm cForm, String signifierStartsWith);

	/**
	 * Returns a list of {@link Term}s that represent the suggested general concept for the given {@link Term}
	 * 
	 * @param term {@link Term} to get the suggestions for.
	 * @param signifierStartsWith partial signifier for general concept
	 * @return The list of suggested {@link Term}s.
	 */
	List<Term> suggestGeneralTerms(Term term, String signifierStartsWith);

	/**
	 * Returns a list of {@link Term}s that can become categories based on the given {@link Term} categorization type.
	 * 
	 * @param categorizationType {@link Term} to get the category suggestions for
	 * @param signifierStartsWith partial signifier for categories
	 * @return The list of suggested {@link Term}s.
	 */
	List<Term> suggestCategories(Term categorizationType, String signifierStartsWith);

	/**
	 * Returns a list of {@link Term}s that can become category types based on the given {@link Term} concept for the
	 * type will be for.
	 * 
	 * @param forConcept {@link Term} to get the category suggestions for
	 * @param signifierStartsWith partial signifier for categories
	 * @return The list of suggested {@link Term}s.
	 */
	List<Term> suggestCategorizationTypes(Term forConcept, String signifierStartsWith);

	/**
	 * Returns a list of {@link Term}s that can become concepts for categorization schemes.
	 * 
	 * @param scheme The {@link Designation} (e.g. Term or Name) representing the categorization scheme
	 * @param signifierStartsWith partial signifier for categories
	 * @return The list of suggested {@link Term}s.
	 */
	List<Term> suggestConceptsForScheme(Designation scheme, String signifierStartsWith);

	/**
	 * 
	 * @param binaryFactTypeForm
	 * @return
	 */
	List<BinaryFactTypeForm> suggestGeneralizableBinaryFactTypeForms(BinaryFactTypeForm binaryFactTypeForm);

	/**
	 * Returns a list of {@link BinaryFactTypeForm}s that represent the suggested general concept for the given
	 * {@link BinaryFactTypeForm}
	 * 
	 * @param bftForm {@link BinaryFactTypeForm} to get the suggestions for.
	 * @param signifierStartsWith partial signifier for general concept
	 * @return The list of suggested {@link BinaryFactTypeForm}s.
	 */
	List<BinaryFactTypeForm> suggestGeneralBinaryFactTypeForms(BinaryFactTypeForm bftForm, String signifierStartsWith);

	/**
	 * Returns a list of {@link CharacteristicForm}s that represent the suggested general concept for the given
	 * {@link CharacteristicForm}
	 * 
	 * @param cForm {@link CharacteristicForm} to get the suggestions for.
	 * @param signifierStartsWith partial signifier for general concept
	 * @return The list of suggested {@link CharacteristicForm}s.
	 */
	List<CharacteristicForm> suggestGeneralCharacteristicForms(CharacteristicForm cForm, String signifierStartsWith);

	/**
	 * 
	 * @param resourceId
	 * @return
	 */
	Collection<Member> suggestUsersForRepresentation(String resourceId);

	/**
	 * 
	 * @param representation
	 * @return
	 */
	Collection<Member> suggestUsers(Representation representation);

	/**
	 * @param spc
	 * @return
	 */
	Collection<Member> suggestUsers(Community spc);

	/**
	 * Get all the {@link BinaryFactTypeForm}s between the given head- and tail-term in the given Vocabulary and
	 * potentially its incorporated vocabularies
	 * @param head The head term object type of the returned fact type forms
	 * @param tail The tail term object type of the returned fact type forms. If tail is null, it will not be taken into
	 *            account.
	 * @param voc The vocabulary and its incorporating vocabularies in which to look for the fact type forms
	 * @param includeMetaVocs include looking through meta vocabularies
	 * @return The collection of BinaryFactTypeForms.
	 */
	Collection<BinaryFactTypeForm> suggestFactTypes(Term head, Term tail, Vocabulary voc);

	/**
	 * Get all {@link ProcessDefinition}s with business key containing the search pattern.
	 * @param match The search pattern.
	 * @return {@link ProcessDefinition}s with matching business key.
	 */
	Collection<ProcessDefinition> suggestWorkflowProcesses(String match);
}
