package com.collibra.dgc.core.component.representation.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.representation.TermComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.util.Defense;

/**
 * {@link Term} API implementation.
 * 
 * @author pmalarme
 * 
 */
@Service
public class TermComponentImpl extends RepresentationComponentImpl implements TermComponent {

	private final Logger log = LoggerFactory.getLogger(TermComponentImpl.class);

	@Override
	@Transactional
	public Term addTerm(String vocabularyRId, String signifier) {

		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");
		Defense.notEmpty(signifier, DGCErrorCodes.TERM_SIGNIFIER_NULL, DGCErrorCodes.TERM_SIGNIFIER_EMPTY, "signifier");

		Vocabulary vocabulary = representationService.getVocabularyWithError(vocabularyRId);

		ObjectType businesstermOT = meaningService.findMetaBusinessTerm();

		if (businesstermOT == null) {
			String message = "Could not find 'Business Term' type";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.OBJECT_TYPE_NOT_FOUND_BUSINESS_TERM);
		}

		Term term = representationFactory.makeTermOfType(vocabulary, signifier, businesstermOT);

		return representationService.saveTerm(term);
	}

	@Override
	@Transactional
	public Term addTerm(String vocabularyRId, String signifier, String objectTypeRId) {

		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");
		Defense.notEmpty(signifier, DGCErrorCodes.TERM_SIGNIFIER_NULL, DGCErrorCodes.TERM_SIGNIFIER_EMPTY, "signifier");
		Defense.notEmpty(objectTypeRId, DGCErrorCodes.OBJECT_TYPE_ID_NULL, DGCErrorCodes.OBJECT_TYPE_ID_EMPTY,
				"objectTypeRId");

		Vocabulary vocabulary = representationService.getVocabularyWithError(vocabularyRId);

		ObjectType ot = meaningService.findObjectTypeByResourceId(objectTypeRId);

		if (ot == null) {
			String message = "Could not find Object Type with Resource ID: " + objectTypeRId;
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.OBJECT_TYPE_NOT_FOUND, objectTypeRId);
		}

		Term term = representationFactory.makeTermOfType(vocabulary, signifier, ot);

		return representationService.saveTerm(term);
	}

	@Override
	@Transactional
	public Term getTerm(String rId) {

		Defense.notEmpty(rId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "rId");

		return representationService.getTermWithError(rId);
	}

	@Override
	@Transactional
	public Term getTermBySignifier(String vocabularyRId, String signifier) {

		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");
		Defense.notEmpty(signifier, DGCErrorCodes.TERM_SIGNIFIER_NULL, DGCErrorCodes.TERM_SIGNIFIER_EMPTY, "signifier");

		final Term term = representationService.findTermBySignifier(
				representationService.getVocabularyWithError(vocabularyRId), signifier);

		if (term == null)
			throw new EntityNotFoundException(DGCErrorCodes.TERM_NOT_FOUND_SIGNIFIER, signifier, vocabularyRId);

		return term;
	}

	@Override
	@Transactional
	public Term getTermBySignifierInIncorporatedVocabularies(String vocabularyRId, String signifier) {

		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");
		Defense.notEmpty(signifier, DGCErrorCodes.TERM_SIGNIFIER_NULL, DGCErrorCodes.TERM_SIGNIFIER_EMPTY, "signifier");

		final Term term = representationService.findTermBySignifierInAllIncorporatedVocabularies(
				representationService.getVocabularyWithError(vocabularyRId), signifier);

		if (term == null)
			throw new EntityNotFoundException(DGCErrorCodes.TERM_NOT_FOUND_SIGNIFIER_INC_VOC, signifier, vocabularyRId);

		return term;
	}

	@Override
	@Transactional
	public Collection<Term> findTermsContainingSignifier(String searchSignifier, int offset, int number) {

		Defense.notEmpty(searchSignifier, DGCErrorCodes.TERM_SIGNIFIER_NULL, DGCErrorCodes.TERM_SIGNIFIER_EMPTY,
				"signifier");

		return representationService.searchTermsBySignifier(searchSignifier, offset, number);
	}

	@Override
	@Transactional
	public Term changeSignifier(String rId, String newSignifier) {

		Defense.notEmpty(newSignifier, DGCErrorCodes.TERM_SIGNIFIER_NULL, DGCErrorCodes.TERM_SIGNIFIER_EMPTY,
				"signifier");

		return representationService.changeSignifier(getTerm(rId), newSignifier);
	}

	@Override
	@Transactional
	public void removeTerms(Collection<String> termRIds) {

		Defense.notEmpty(termRIds, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "termRIds");

		Collection<Term> terms = new ArrayList<Term>();

		for (final String termRId : termRIds)
			terms.add(getTerm(termRId));

		representationService.remove(terms);
	}
}
