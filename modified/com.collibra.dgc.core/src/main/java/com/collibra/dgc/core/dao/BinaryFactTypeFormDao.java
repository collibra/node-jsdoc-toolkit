package com.collibra.dgc.core.dao;

import java.util.List;
import java.util.Map;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;

public interface BinaryFactTypeFormDao extends AbstractDao<BinaryFactTypeForm> {
	/**
	 * Looks up the {@link BinaryFactTypeForm} by the expression of its Terms and roles.
	 * @param vocabulary The {@link Vocabulary} that contains the {@link BinaryFactTypeForm}.
	 * @param headTermSignifier The signifier of the head Term.
	 * @param roleExpression The expression of the role.
	 * @param coRoleExpression The expression of the corole.
	 * @param tailTermSignifier The signifier of the tail Term.
	 * @return
	 */
	BinaryFactTypeForm findByExpression(Vocabulary vocabulary, String headTermSignifier, String roleExpression,
			String coRoleExpression, String tailTermSignifier);

	/**
	 * Looks up a {@link BinaryFactTypeForm} by the representation of its constituent parts.
	 * @param vocabulary The vocabulary.
	 * @param headTerm The head {@link Term}
	 * @param roleExpression The expression that is being represented by the role.
	 * @param coRoleExpression The expression that is being represented by the co-role.
	 * @param tailTerm The tail {@link Term}.
	 * 
	 * @return
	 */
	BinaryFactTypeForm findByRepresentation(Vocabulary vocabulary, Term headTerm, String roleExpression,
			String coRoleExpression, Term tailTerm);

	/**
	 * Find the preferred {@link BinaryFactTypeForm} in the {@link Vocabulary} for the given {@link BinaryFactType}.
	 * @param binaryFactType {@link BinaryFactType} to find the {@link BinaryFactTypeForm} for.
	 * @param vocabulary The {@link Vocabulary} that should contain the {@link BinaryFactTypeForm}.
	 * @return The preferred {@link BinaryFactTypeForm}
	 */
	BinaryFactTypeForm findPreferred(BinaryFactType binaryFactType, Vocabulary vocabulary);

	/**
	 * Find the {@link BinaryFactTypeForm}s that contain the given {@link Term} as head {@link Term}.
	 * @param term The head {@link Term}
	 * @return The list of {@link BinaryFactTypeForm}s.
	 */
	List<BinaryFactTypeForm> findWithHeadTerm(Term term);

	/**
	 * Find the {@link BinaryFactTypeForm}s that contain the given {@link Term} as tail {@link Term}.
	 * @param term The tail {@link Term}
	 * @return The list of {@link BinaryFactTypeForm}s.
	 */
	List<BinaryFactTypeForm> findWithTailTerm(Term term);
	
	/**
	 * @return The number of BinaryFactTypeForms per Term.
	 */
	Map<String, Long> findNumberOfBinaryFactTypesPerTerm();

	/**
	 * @return The number of BinaryFactTypeForms per Term.
	 */
	Map<String, Long> findNumberOfBinaryFactTypesPerTerm(Community community);

	/**
	 * @return The number of BinaryFactTypeForms per Term.
	 */
	Map<String, Long> findNumberOfBinaryFactTypesPerTerm(Vocabulary vocabulary);

	/**
	 * Will search for {@link BinaryFactTypeForm}s that match the verbalisation partially with startsWith
	 * 
	 * @param vocabulary the vocabulary in which to search for {@link BinaryFactTypeForm}s
	 * @param startsWith a partial verbalisation of {@link BinaryFactTypeForm}
	 * @return
	 */
	List<BinaryFactTypeForm> searchByVerbalisation(Vocabulary vocabulary, String startsWith);

	List<BinaryFactTypeForm> findSynonymousForms(BinaryFactTypeForm bftf);

	List<BinaryFactTypeForm> find(Vocabulary vocabulary);

	/**
	 * Finds the {@link BinaryFactTypeForm}s that contain the given {@link Term} as either head or tail {@link Term}.
	 * @param term {@link Term} that is either head or tail of the {@link BinaryFactTypeForm}.
	 * @return The list of {@link BinaryFactTypeForm}s
	 */
	List<BinaryFactTypeForm> find(Term term);

	BinaryFactTypeForm find(Vocabulary vocabulary, Term headTerm, String role, String corole, Term tailTerm);

	/**
	 * Searches for all the {@link BinaryFactTypeForm}s that are the same but in different vocabularies.
	 */
	List<BinaryFactTypeForm> find(Term headTerm, String role, String corole, Term tailTerm);

	/**
	 * @return the list of {@link BinaryFactTypeForms}s that represent both the concepts, either as head or tail term.
	 */
	List<BinaryFactTypeForm> find(Concept specializedHeadConcept, Concept specializedTailConcept);

	List<BinaryFactTypeForm> find(Term headTerm, Term tailTerm);

}
