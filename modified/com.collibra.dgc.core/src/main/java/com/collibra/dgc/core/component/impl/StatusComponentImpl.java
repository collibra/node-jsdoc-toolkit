package com.collibra.dgc.core.component.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.StatusComponent;
import com.collibra.dgc.core.component.VocabularyComponent;
import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.MeaningService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.util.Defense;

/**
 * {@code Status} {@link Term} API implementation.
 * 
 * @author pmalarme
 * 
 */
@Service
public class StatusComponentImpl implements StatusComponent {

	@Autowired
	private VocabularyComponent vocabularyComponent;

	@Autowired
	private MeaningService meaningService;

	@Autowired
	private RepresentationService representationService;

	@Autowired
	private RepresentationFactory representationFactory;

	@Override
	@Transactional
	public Term addStatus(String signifier) {

		Defense.notEmpty(signifier, DGCErrorCodes.STATUS_TERM_SIGNIFIER_NULL,
				DGCErrorCodes.STATUS_TERM_SIGNIFIER_EMPTY, "signifier");

		final ObjectType statusOT = meaningService.getObjectTypeWithError(MeaningConstants.META_STATUS_TYPE_UUID
				.toString());
		// TODO if the SBVR and Collibra concepts are not locked, a method must be implemented to check that the status
		// type term and concept exist and if it is not the case, create it.
		final Vocabulary statusVocabulary = vocabularyComponent.getVocabularyByUri(Constants.STATUSES_VOCABULARY_URI);

		Term newStatus = representationFactory.makeTermOfType(statusVocabulary, signifier, statusOT);

		return representationService.saveTerm(newStatus);
	}

	@Override
	@Transactional
	public Term getStatus(String rId) {

		Defense.notEmpty(rId, DGCErrorCodes.STATUS_TERM_ID_NULL, DGCErrorCodes.STATUS_TERM_ID_EMPTY, "rId");

		final Term term = representationService.getTermWithError(rId);

		// Check that the term is a status label term (vocabulary)
		String labelVocabularyUri = term.getVocabulary().getUri();

		if (!labelVocabularyUri.equals(Constants.STATUSES_VOCABULARY_URI))
			throw new IllegalArgumentException(DGCErrorCodes.STATUS_NOT_STATUS_LABEL_FOR_RID, term.getSignifier(),
					"rId", rId, labelVocabularyUri, Constants.STATUSES_VOCABULARY_NAME,
					Constants.STATUSES_VOCABULARY_URI);

		return term;
	}

	@Override
	@Transactional
	public Term getStatusBySignifier(String signifier) {

		Defense.notEmpty(signifier, DGCErrorCodes.STATUS_TERM_SIGNIFIER_NULL,
				DGCErrorCodes.STATUS_TERM_SIGNIFIER_EMPTY, "signifier");

		final Term term = representationService.findTermBySignifier(
				vocabularyComponent.getVocabularyByUri(Constants.STATUSES_VOCABULARY_URI), signifier);

		if (term == null) {
			String message = "Satuts Term with signifier '" + signifier + "' not found for the Collibra '"
					+ Constants.STATUSES_VOCABULARY_NAME + "' (" + Constants.STATUSES_VOCABULARY_URI + ").";
			throw new EntityNotFoundException(DGCErrorCodes.STATUS_NOT_FOUND_SIGNIFIER, signifier);
		}

		return term;
	}

	@Override
	@Transactional
	public Collection<Term> getStatuses() {
		return representationService.findStatusTerms();
	}

	@Override
	@Transactional
	public Term changeSignifier(String rId, String newSignifier) {

		Defense.notEmpty(newSignifier, DGCErrorCodes.STATUS_TERM_SIGNIFIER_NULL,
				DGCErrorCodes.STATUS_TERM_SIGNIFIER_EMPTY, "newSignifier");

		return representationService.changeSignifier(getStatus(rId), newSignifier);
	}

	@Override
	@Transactional
	public void removeStatus(String rId) {
		representationService.remove(getStatus(rId));
	}

}
