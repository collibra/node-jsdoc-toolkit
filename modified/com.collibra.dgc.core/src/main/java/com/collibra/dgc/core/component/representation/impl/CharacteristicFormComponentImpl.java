package com.collibra.dgc.core.component.representation.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.representation.CharacteristicFormComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.util.Defense;

/**
 * {@link CharacteristicForm} API implementation.
 * 
 * @author pmalarme
 * 
 */
@Service
public class CharacteristicFormComponentImpl extends RepresentationComponentImpl implements CharacteristicFormComponent {

	@Override
	@Transactional
	public CharacteristicForm addCharacteristicForm(String vocabularyRId, String termSignifier, String role) {

		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");
		Defense.notEmpty(termSignifier, DGCErrorCodes.CF_TERM_SIGNIFIER_NULL, DGCErrorCodes.CF_TERM_SIGNIFIER_EMPTY,
				"termSignifier");
		Defense.notEmpty(role, DGCErrorCodes.CF_ROLE_NULL, DGCErrorCodes.CF_ROLE_EMPTY, "role");

		Vocabulary vocabulary = representationService.getVocabularyWithError(vocabularyRId);
		Term term = representationService.findTermBySignifierAndCreateIfNotExists(vocabulary, termSignifier);

		CharacteristicForm characteristicForm = representationFactory.makeCharacteristicForm(vocabulary, term, role);

		return representationService.saveCharacteristicForm(characteristicForm);
	}

	@Override
	@Transactional
	public CharacteristicForm getCharacteristicForm(String rId) {

		Defense.notEmpty(rId, DGCErrorCodes.CF_ID_NULL, DGCErrorCodes.CF_ID_EMPTY, "rId");

		return representationService.getCfWithError(rId);
	}

	@Override
	@Transactional
	public Collection<CharacteristicForm> getCharacteristicFormsContainingTerm(String termRId) {

		Defense.notEmpty(termRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "termRId");

		return representationService.findCharacteristicFormsContainingTerm(representationService
				.getTermWithError(termRId));
	}

	@Override
	@Transactional
	public CharacteristicForm changeCharacteristicForm(String rId, String newTermSignifier, String newRole) {

		Defense.notEmpty(newTermSignifier, DGCErrorCodes.CF_TERM_SIGNIFIER_NULL, DGCErrorCodes.CF_TERM_SIGNIFIER_EMPTY,
				"newTermSignifier");
		Defense.notEmpty(newRole, DGCErrorCodes.CF_ROLE_NULL, DGCErrorCodes.CF_ROLE_EMPTY, "newRole");

		CharacteristicForm characteristicForm = getCharacteristicForm(rId);
		Vocabulary vocabulary = characteristicForm.getVocabulary();
		Term term = representationService.findTermBySignifierAndCreateIfNotExists(vocabulary, newTermSignifier);

		return representationService.changeCharacteristicForm(characteristicForm, term, newRole);
	}

}
