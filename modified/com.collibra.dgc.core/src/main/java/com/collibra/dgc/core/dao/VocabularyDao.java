package com.collibra.dgc.core.dao;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.map.MultiValueMap;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.user.Role;

/**
 * DAO for {@link Vocabulary}s.
 * 
 * @author dtrog
 * 
 */
public interface VocabularyDao extends AbstractDao<Vocabulary> {

	/**
	 * Retrieves a vocabulary by its uri
	 * 
	 * @param uri The uri of the vocabulary.
	 * @return The {@link Vocabulary}.
	 */
	Vocabulary findByVocabularyUri(String uri);

	/**
	 * Retrieves all the vocabularies in which the given vocabulary is incorporated in.
	 * @param vocabulary The vocabulary that is being incorporated.
	 * @return The {@link Vocabulary}s in which the given vocabulary is incorporated in.
	 */
	List<Vocabulary> findAllIncorporatingVocabularies(Vocabulary vocabulary);

	/**
	 * Retrieves a vocabulary by its name (assuming this is unique, which holds in XWiki implementation)
	 * @param name Name of the {@link Vocabulary}
	 * @return The {@link Vocabulary}.
	 */
	Vocabulary findByVocabularyName(String name);

	Vocabulary findByVocabularyName(Community community, String name);

	/**
	 * Retrieves the bootstrapped SBVR Vocabulary. If this returns null, make sure bootstrapper was run.
	 * @return SBVR Vocabulary
	 */
	Vocabulary findSbvrVocabulary();

	/**
	 * Retrieves the bootstrapped SBVR Meaning and Representation vocabulary. If this returns null, make sure
	 * bootstrapper was run.
	 * @return SBVR Meaning and Representation vocabulary
	 */
	Vocabulary findSbvrMeaningAndRepresentationVocabulary();

	/**
	 * Retrieves the bootstrapped SBVR Business vocabulary. If this returns null, make sure bootstrapper was run.
	 * @return SBVR Business vocabulary
	 */
	Vocabulary findSbvrBusinessVocabulary();

	/**
	 * Retrieves the bootstrapped Collibra roles and responsibilities {@link Vocabulary}. If this returns null, make
	 * sure bootstrapper was run.
	 * @return Collibra roles and responsibilities {@link Vocabulary}.
	 */
	Vocabulary findRolesAndResponsibilitiesVocabulary();

	/**
	 * Retrieve the bootstrapped SBVR 'Vocabulary for Describing Business'.
	 * @return Vocabulary for Describing Business
	 */
	Vocabulary findSbvrBusinessRulesVocabulary();

	/**
	 * Retrieve the bootstrapped SBVR 'Logical Formulation of Semantics Vocabulary'.
	 * @return Logical Formulation of Semantics Vocabulary
	 */
	Vocabulary findSbvrLogicalFormulationsVocabulary();

	/**
	 * Retrieve the bootstrapped Collibra Extensions Vocabulary.
	 * @return Collibra Extensions Vocabulary
	 */
	Vocabulary findSbvrCollibraExtensionsVocabulary();

	/**
	 * Retrieve the bootstrapped Custom Attributes Vocabulary.
	 * @return Custom Attributes Vocabulary
	 */
	Vocabulary findAttributeTypesVocabulary();

	/**
	 * Retrieve the bootstrapped Statuses Vocabulary.
	 * @return Statuses Vocabulary
	 */
	Vocabulary findStatusesVocabulary();

	/**
	 * Retrieve the bootstrapped ConceptTypes Vocabulary
	 * @return Concept Types {@link Vocabulary}
	 */
	Vocabulary findMetamodelExtensionsVocabulary();

	/**
	 * Find all the vocabularies
	 * 
	 * @return All {@link Vocabulary}s
	 */
	@Override
	List<Vocabulary> findAll();

	List<Vocabulary> findVocabulariesContainingTerm(Term term);

	List<Vocabulary> findVocabulariesContainingBinaryFactTypeForm(BinaryFactTypeForm binaryFactTypeForm);

	List<Vocabulary> findVocabulariesContainingCharacteristicForm(CharacteristicForm characteristicForm);

	List<Vocabulary> findVocabulariesContainingAttribute(Attribute attribute);

	/**
	 * Returns a list of vocabularies where a given user plays a given role on the vocabulary, sorted DESC by vocabulary
	 * name
	 * @param username The name of the user
	 * @param role The role that the user plays in the vocabulary
	 * @param start The start index of vocabularies to retrieve
	 * @param end The end index of vocabularies to retrieve
	 * @return
	 */
	List<Vocabulary> findVocabulariesByMember(String username, Role role, Integer start, Integer end);

	/**
	 * Returns a list of vocabularies where a given user plays any role on the vocabulary, sorted DESC by vocabulary
	 * name
	 * @param username The name of the user
	 * @param start The start index of vocabularies to retrieve
	 * @param end The end index of vocabularies to retrieve
	 * @return
	 */
	List<Vocabulary> findVocabulariesByMember(String username, Integer start, Integer end);

	List<String> getTermsWithChildren(List<String> termResourceIds);

	/**
	 * 
	 * @param ruleSet
	 * @return
	 */
	List<Vocabulary> find(RuleSet ruleSet);

	/**
	 * Searches for the vocabularies whose names match {@code name}. Pagination of the results can be done using the 2
	 * last parameters.
	 * 
	 * @param name the name to match
	 * @param offset the index in the list of results (0-based) of the first result retrieved by this function
	 * @param number the maximum number of results to return; if 0, then all results will be returned
	 * @return the list of all vocabularies that match the passed name
	 */
	List<Vocabulary> searchVocabulariesForName(String name, int offset, int number);

	List<Vocabulary> findAllWithoutParentCommuntiy();

	/**
	 * @return All vocabularies in the form of the following tuple:
	 *         <ul>
	 *         <li>voc.id (UUID)</li>
	 *         <li>voc.name (String)</li>
	 *         </ul>
	 */
	List findAllVocabularyNames();

	MultiValueMap findVocabulariesWithoutTerms();

	/**
	 * @return A List containing the vocabulary ids of all the "meta" vocabularies. Meta-vocabularies are vocabularies
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
	 * Return a list of {@link Term}s that are classified by at least one categories.
	 * @param vocabularies The vocabularies in which to search for the terms.
	 * @param startsWith The String with which the returned terms must start with. if null or "", all terms will be
	 *            returned.
	 * @return a {@link List} of {@link Term}s that have one or more categories.
	 */
	List<Term> findAllTermsWithCategories(Collection<Vocabulary> vocabularies, String startsWith);

	// /**
	// * Check if the vocabulary is persisted or not.
	// * @param vocabulary The {@link Vocabulary}
	// * @return {@code true} if it is persisted, {@code false} otherwise
	// */
	// boolean isPersisted(final Vocabulary vocabulary);

}