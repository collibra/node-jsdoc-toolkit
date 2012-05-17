package com.collibra.dgc.core.component.representation.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.representation.BinaryFactTypeFormComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.util.Defense;

/**
 * {@link BinaryFactTypeForm} API implementation.
 * 
 * @author pmalarme
 * 
 */
@Service
public class BinaryFactTypeFormComponentImpl extends RepresentationComponentImpl implements BinaryFactTypeFormComponent {

	private BinaryFactTypeForm addBinaryFactTypeForm(Vocabulary vocabulary, Term headTerm, String role, String coRole,
			Term tailTerm) {

		Defense.notEmpty(role, DGCErrorCodes.BFTF_ROLE_NULL, DGCErrorCodes.BFTF_ROLE_EMPTY, "role");
		Defense.notEmpty(coRole, DGCErrorCodes.BFTF_COROLE_NULL, DGCErrorCodes.BFTF_COROLE_EMPTY, "coRole");

		BinaryFactTypeForm binaryFactTypeForm = representationFactory.makeBinaryFactTypeForm(vocabulary, headTerm,
				role, coRole, tailTerm);

		return representationService.saveBinaryFactTypeForm(binaryFactTypeForm);
	}

	private Vocabulary getVocabulary(String vocabularyRId) {

		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");

		return representationService.getVocabularyWithError(vocabularyRId);
	}

	@Override
	@Transactional
	public BinaryFactTypeForm addBinaryFactTypeForm(String vocabularyRId, String headSignifier, String role,
			String coRole, String tailSignifier) {

		final Vocabulary vocabulary = getVocabulary(vocabularyRId);

		Defense.notEmpty(headSignifier, DGCErrorCodes.BFTF_HEAD_SIGNIFIER_NULL,
				DGCErrorCodes.BFTF_HEAD_SIGNIFIER_EMPTY, "headSignifier");
		Defense.notEmpty(tailSignifier, DGCErrorCodes.BFTF_TAIL_SIGNIFIER_NULL,
				DGCErrorCodes.BFTF_TAIL_SIGNIFIER_EMPTY, "tailSignifier");

		Term headTerm, tailTerm;

		if (headSignifier.equals(tailSignifier)) {

			headTerm = tailTerm = representationService.findTermBySignifierAndCreateIfNotExists(vocabulary,
					tailSignifier);

		} else {

			headTerm = representationService.findTermBySignifierAndCreateIfNotExists(vocabulary, headSignifier);
			tailTerm = representationService.findTermBySignifierAndCreateIfNotExists(vocabulary, tailSignifier);
		}

		return addBinaryFactTypeForm(vocabulary, headTerm, role, coRole, tailTerm);
	}

	@Override
	@Transactional
	public BinaryFactTypeForm addBinaryFactTypeFormOnExistingTerms(String vocabularyRId, String headTermRId,
			String role, String coRole, String tailTermRId) {

		Defense.notEmpty(headTermRId, DGCErrorCodes.BFTF_HEAD_ID_NULL, DGCErrorCodes.BFTF_HEAD_ID_EMPTY, "headTermRId");
		Defense.notEmpty(tailTermRId, DGCErrorCodes.BFTF_TAIL_ID_NULL, DGCErrorCodes.BFTF_TAIL_ID_EMPTY, "tailTermRId");

		return addBinaryFactTypeForm(getVocabulary(vocabularyRId), representationService.getTermWithError(headTermRId),
				role, coRole, representationService.getTermWithError(tailTermRId));
	}

	@Override
	@Transactional
	public BinaryFactTypeForm addBinaryFactTypeFormOnExistingHeadTerm(String vocabularyRId, String headTermRId,
			String role, String coRole, String tailSignifier) {

		final Vocabulary vocabulary = getVocabulary(vocabularyRId);

		Defense.notEmpty(headTermRId, DGCErrorCodes.BFTF_HEAD_ID_NULL, DGCErrorCodes.BFTF_HEAD_ID_EMPTY, "headTermRId");
		Defense.notEmpty(tailSignifier, DGCErrorCodes.BFTF_TAIL_SIGNIFIER_NULL,
				DGCErrorCodes.BFTF_TAIL_SIGNIFIER_EMPTY, "tailSignifier");

		return addBinaryFactTypeForm(vocabulary, representationService.getTermWithError(headTermRId), role, coRole,
				representationService.findTermBySignifierAndCreateIfNotExists(vocabulary, tailSignifier));
	}

	@Override
	@Transactional
	public BinaryFactTypeForm addBinaryFactTypeFormOnExistingTailTerm(String vocabularyRId, String headSignifier,
			String role, String coRole, String tailTermRId) {

		final Vocabulary vocabulary = getVocabulary(vocabularyRId);

		Defense.notEmpty(headSignifier, DGCErrorCodes.BFTF_HEAD_SIGNIFIER_NULL,
				DGCErrorCodes.BFTF_HEAD_SIGNIFIER_EMPTY, "headSignifier");
		Defense.notEmpty(tailTermRId, DGCErrorCodes.BFTF_TAIL_ID_NULL, DGCErrorCodes.BFTF_TAIL_ID_EMPTY, "tailTermRId");

		return addBinaryFactTypeForm(vocabulary,
				representationService.findTermBySignifierAndCreateIfNotExists(vocabulary, headSignifier), role, coRole,
				representationService.getTermWithError(tailTermRId));
	}

	@Override
	@Transactional
	public BinaryFactTypeForm getBinaryFactTypeForm(String rId) {

		Defense.notEmpty(rId, DGCErrorCodes.BFTF_ID_NULL, DGCErrorCodes.BFTF_ID_EMPTY, "rId");

		return representationService.getBftfWithError(rId);
	}

	@Override
	@Transactional
	public Collection<BinaryFactTypeForm> getBinaryFactTypeFormsContainingHeadTerm(String headTermRId) {

		Defense.notEmpty(headTermRId, DGCErrorCodes.BFTF_HEAD_ID_NULL, DGCErrorCodes.BFTF_HEAD_ID_EMPTY, "headTermRId");

		return representationService.findBinaryFactTypeFormsContainingHeadTerm(representationService
				.getTermWithError(headTermRId));
	}

	@Override
	@Transactional
	public Collection<BinaryFactTypeForm> getBinaryFactTypeFormsContainingTailTerm(String tailTermRId) {

		Defense.notEmpty(tailTermRId, DGCErrorCodes.BFTF_TAIL_ID_NULL, DGCErrorCodes.BFTF_TAIL_ID_EMPTY, "tailTermRId");

		return representationService.findBinaryFactTypeFormsContainingTailTerm(representationService
				.getTermWithError(tailTermRId));
	}

	@Override
	@Transactional
	public Collection<BinaryFactTypeForm> getBinaryFactTypeFormsContainingTerm(String termRId) {

		Defense.notEmpty(termRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "termRId");

		return representationService.findBinaryFactTypeFormsReferringTerm(representationService
				.getTermWithError(termRId));
	}

	@Override
	@Transactional
	public Collection<BinaryFactTypeForm> getDerivedFacts(String termRId) {

		Defense.notEmpty(termRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "termRId");

		return representationService.findDerivedFacts(representationService.getTermWithError(termRId));

	}

	@Override
	@Transactional
	public BinaryFactTypeForm changeBinaryFactTypeForm(String rId, String newHeadSignifier, String newRole,
			String newCoRole, String newTailSignifier) {

		Defense.notEmpty(newHeadSignifier, DGCErrorCodes.BFTF_HEAD_SIGNIFIER_NULL,
				DGCErrorCodes.BFTF_HEAD_SIGNIFIER_EMPTY, "newHeadSignifier");
		Defense.notEmpty(newRole, DGCErrorCodes.BFTF_ROLE_NULL, DGCErrorCodes.BFTF_ROLE_EMPTY, "newRole");
		Defense.notEmpty(newCoRole, DGCErrorCodes.BFTF_COROLE_NULL, DGCErrorCodes.BFTF_COROLE_EMPTY, "newCoRole");
		Defense.notEmpty(newTailSignifier, DGCErrorCodes.BFTF_TAIL_SIGNIFIER_NULL,
				DGCErrorCodes.BFTF_TAIL_SIGNIFIER_EMPTY, "newTailSignifier");

		BinaryFactTypeForm binaryFactTypeForm = getBinaryFactTypeForm(rId);
		Vocabulary vocabulary = binaryFactTypeForm.getVocabulary();

		Term headTerm, tailTerm;

		if (newHeadSignifier.equals(newTailSignifier)) {

			headTerm = tailTerm = representationService.findTermBySignifierAndCreateIfNotExists(vocabulary,
					newTailSignifier);

		} else {

			headTerm = representationService.findTermBySignifierAndCreateIfNotExists(vocabulary, newHeadSignifier);
			tailTerm = representationService.findTermBySignifierAndCreateIfNotExists(vocabulary, newTailSignifier);
		}

		return representationService.changeBinaryFactTypeForm(binaryFactTypeForm, headTerm, newRole, newCoRole,
				tailTerm);
	}

}
