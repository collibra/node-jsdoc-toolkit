package com.collibra.dgc.core.component.relation.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.relation.RelationComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.relation.Relation;
import com.collibra.dgc.core.model.relation.RelationFactory;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.service.RelationService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.util.Defense;

/**
 * @author fvdmaele
 * 
 */
@Component
public class RelationComponentImpl implements RelationComponent {

	@Autowired
	private RelationService relationService;

	@Autowired
	private RepresentationService representationService;

	@Autowired
	private RelationFactory relationFactory;

	@Autowired
	private RepresentationFactory representationFactory;

	@Override
	@Transactional
	public Relation getRelation(String rId) {
		Defense.notEmpty(rId, DGCErrorCodes.RELATION_ID_NULL, DGCErrorCodes.RELATION_ID_EMPTY, "rId");
		return relationService.getRelationWithError(rId);
	}

	@Override
	@Transactional
	public Relation addRelation(String sourceTermRId, String binaryFactTypeFormRId, String targetTermRId) {
		Defense.notEmpty(sourceTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "sourceTermRId");
		Defense.notEmpty(targetTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "targetTermRId");
		Defense.notEmpty(binaryFactTypeFormRId, DGCErrorCodes.BFTF_ID_NULL, DGCErrorCodes.BFTF_ID_EMPTY,
				"binaryFactTypeFormRId");

		Term source = representationService.getTermWithError(sourceTermRId);
		Term target = representationService.getTermWithError(targetTermRId);
		BinaryFactTypeForm type = representationService.getBftfWithError(binaryFactTypeFormRId);

		Relation rel = relationFactory.makeRelation(source, target, type);

		return relationService.saveRelation(rel, true);
	}

	@Override
	@Transactional
	public Relation addRelation(String sourceTermRId, String binaryFactTypeFormRId, String targetTermSignifier,
			String targetTermVocabularyRId) {

		Defense.notEmpty(sourceTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "sourceTermRId");
		Defense.notEmpty(targetTermSignifier, DGCErrorCodes.TERM_SIGNIFIER_NULL, DGCErrorCodes.TERM_SIGNIFIER_EMPTY,
				"targetTermSignifier");
		Defense.notEmpty(binaryFactTypeFormRId, DGCErrorCodes.BFTF_ID_NULL, DGCErrorCodes.BFTF_ID_EMPTY,
				"binaryFactTypeFormRId");
		Defense.notEmpty(targetTermVocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"targetTermVocabularyRId");

		Term source = representationService.getTermWithError(sourceTermRId);
		BinaryFactTypeForm type = representationService.getBftfWithError(binaryFactTypeFormRId);
		Vocabulary voc = representationService.getVocabularyWithError(targetTermVocabularyRId);

		// create new term
		Term targetTerm = representationFactory.makeTermOfType(voc, targetTermSignifier, type.getTailTerm()
				.getObjectType());
		targetTerm = representationService.saveTerm(targetTerm);

		Relation rel = relationFactory.makeRelation(source, targetTerm, type);

		return relationService.saveRelation(rel, true);
	}

	@Override
	@Transactional
	public void removeRelation(String rId) {
		Defense.notEmpty(rId, DGCErrorCodes.RELATION_ID_NULL, DGCErrorCodes.RELATION_ID_EMPTY, "rId");
		Relation rel = relationService.getRelationWithError(rId);
		relationService.remove(rel);
	}

	@Override
	@Transactional
	public Collection<Relation> findRelationsByType(String binaryFactTypeFormRId) {

		Defense.notEmpty(binaryFactTypeFormRId, DGCErrorCodes.BFTF_ID_NULL, DGCErrorCodes.BFTF_ID_EMPTY,
				"binaryFactTypeFormRId");

		BinaryFactTypeForm bftf = representationService.getBftfWithError(binaryFactTypeFormRId);
		return relationService.findByType(bftf);
	}

	@Override
	@Transactional
	public Collection<Relation> findRelationsBySource(String sourceTermRId) {
		Defense.notEmpty(sourceTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "sourceTermRId");
		Term source = representationService.getTermWithError(sourceTermRId);
		return relationService.findBySource(source);
	}

	@Override
	@Transactional
	public Collection<Relation> findRelationsByTarget(String targetTermRId) {
		Defense.notEmpty(targetTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "targetTermRId");
		Term target = representationService.getTermWithError(targetTermRId);
		return relationService.findByTarget(target);
	}

	@Override
	@Transactional
	public Collection<Relation> findRelationsBySourceAndTarget(String sourceTermRId, String targetTermRId) {
		Defense.notEmpty(sourceTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "sourceTermRId");
		Defense.notEmpty(targetTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "targetTermRId");
		Term source = representationService.getTermWithError(sourceTermRId);
		Term target = representationService.getTermWithError(targetTermRId);

		return relationService.findBySourceAndTarget(source, target);
	}

	@Override
	@Transactional
	public Collection<Relation> findRelationsBySourceAndType(String binaryFactTypeFormRId, String sourceTermRId) {
		Defense.notEmpty(sourceTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "sourceTermRId");
		Defense.notEmpty(binaryFactTypeFormRId, DGCErrorCodes.BFTF_ID_NULL, DGCErrorCodes.BFTF_ID_EMPTY,
				"binaryFactTypeFormRId");

		BinaryFactTypeForm bftf = representationService.getBftfWithError(binaryFactTypeFormRId);
		Term source = representationService.getTermWithError(sourceTermRId);

		return relationService.findBySourceAndType(bftf, source);
	}

	@Override
	@Transactional
	public Collection<Relation> findRelationsByTargetAndType(String binaryFactTypeFormRId, String targetTermRId) {
		Defense.notEmpty(binaryFactTypeFormRId, DGCErrorCodes.BFTF_ID_NULL, DGCErrorCodes.BFTF_ID_EMPTY,
				"binaryFactTypeFormRId");
		Defense.notEmpty(targetTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "targetTermRId");

		BinaryFactTypeForm bftf = representationService.getBftfWithError(binaryFactTypeFormRId);
		Term target = representationService.getTermWithError(targetTermRId);

		return relationService.findByTargetAndType(bftf, target);
	}

	@Override
	@Transactional
	public Collection<Relation> findRelationsBySourceAndTargetAndType(String binaryFactTypeFormRId,
			String sourceTermRId, String targetTermRId) {
		Defense.notEmpty(binaryFactTypeFormRId, DGCErrorCodes.BFTF_ID_NULL, DGCErrorCodes.BFTF_ID_EMPTY,
				"binaryFactTypeFormRId");
		Defense.notEmpty(targetTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "targetTermRId");
		Defense.notEmpty(sourceTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "sourceTermRId");

		BinaryFactTypeForm bftf = representationService.getBftfWithError(binaryFactTypeFormRId);
		Term source = representationService.getTermWithError(sourceTermRId);
		Term target = representationService.getTermWithError(targetTermRId);

		return relationService.findBySourceAndTargetAndType(bftf, source, target);
	}
}
