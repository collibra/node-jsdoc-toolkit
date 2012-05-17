package com.collibra.dgc.core.dao;

import java.util.List;

import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;

public interface CharacteristicFormDao extends AbstractDao<CharacteristicForm> {

	/**
	 * Looks up the {@link CharacteristicForm} by the expression of its Term and role.
	 * @param vocabulary The {@link Vocabulary} that contains the {@link CharacteristicForm}.
	 * @param termSignifier The signifier of the Term.
	 * @param roleExpression The expression of the role.
	 * @return The {@link CharacteristicForm} that has these properties.
	 */
	CharacteristicForm findByExpression(Vocabulary vocabulary, String termSignifier, String roleExpression);

	/**
	 * Looks up a {@link CharacteristicForm} by its Representation.
	 * @param vocabulary The vocabulary.
	 * @param term the {@link Term} representation.
	 * @param roleExpression The expression that is being represented by the role.
	 * 
	 * @return The {@link CharacteristicForm}.
	 */
	CharacteristicForm findByRepresentation(Vocabulary vocabulary, Term term, String roleExpression);

	/**
	 * Find the preferred {@link CharacteristicForm} in the {@link Vocabulary} for the given {@link IndividualConcept}.
	 * @param characteristic {@link Characteristic} to find the {@link CharacteristicForm} for.
	 * @param vocabulary The {@link Vocabulary} that should contain the {@link CharacteristicForm}.
	 * @return The preferred {@link CharacteristicForm}
	 */
	CharacteristicForm findPreferred(Characteristic characteristic, Vocabulary vocabulary);

	/**
	 * Will search for {@link CharacteristicForm}s that match the verbalisation partially with startsWith
	 * 
	 * @param vocabulary the vocabulary in which to search for {@link CharacteristicForm}s
	 * @param startsWith a partial verbalisation of {@link CharacteristicForm}
	 * @return
	 */
	List<CharacteristicForm> searchByVerbalisation(Vocabulary vocabulary, String startsWith);

	/**
	 * 
	 * @param cForm
	 * @return
	 */
	List<CharacteristicForm> findSynonymousForms(CharacteristicForm cForm);

	/**
	 * 
	 * @param vocabulary
	 * @return
	 */
	List<CharacteristicForm> find(Vocabulary vocabulary);

	/**
	 * Finds all {@link CharacteristicForm}s that contain the given term.
	 * 
	 * @param term the Term to look for
	 * @return {@link CharacteristicForm}s containing the term
	 */
	List<CharacteristicForm> find(Term term);

	/**
	 * 
	 * @param vocabulary
	 * @param term
	 * @param roleExpression
	 * @return
	 */
	CharacteristicForm find(Vocabulary vocabulary, Term term, String roleExpression);
}
