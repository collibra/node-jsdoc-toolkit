/**
 * 
 */
package com.collibra.dgc.core.component.relation.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.relation.RelationTypeComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.relation.RelationFactory;
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
public class RelationTypeComponentImpl implements RelationTypeComponent {

	@Autowired
	private RelationService relationService;

	@Autowired
	private RepresentationService representationService;

	@Autowired
	private RelationFactory relationFactory;

	@Override
	@Transactional
	public BinaryFactTypeForm addRelationType(String sourceSignifier, String role, String coRole, String targetSignifier) {

		Defense.notEmpty(sourceSignifier, DGCErrorCodes.BFTF_HEAD_SIGNIFIER_NULL,
				DGCErrorCodes.BFTF_HEAD_SIGNIFIER_EMPTY, "sourceSignifier");
		Defense.notEmpty(role, DGCErrorCodes.BFTF_ROLE_NULL, DGCErrorCodes.BFTF_ROLE_EMPTY, "role");
		Defense.notEmpty(coRole, DGCErrorCodes.BFTF_COROLE_NULL, DGCErrorCodes.BFTF_COROLE_EMPTY, "coRole");
		Defense.notEmpty(targetSignifier, DGCErrorCodes.BFTF_TAIL_SIGNIFIER_NULL,
				DGCErrorCodes.BFTF_TAIL_SIGNIFIER_EMPTY, "targetSignifier");

		Term source = getRelationTypeTerm(sourceSignifier);
		Term target = getRelationTypeTerm(targetSignifier);

		BinaryFactTypeForm type = relationFactory.makeRelationType(source, role, coRole, target);

		return relationService.saveRelationType(type);
	}

	@Override
	@Transactional
	public Collection<BinaryFactTypeForm> findPossibleRelationTypesForSourceTerm(String sourceTermRId) {
		Defense.notEmpty(sourceTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "sourceTermRId");

		Term source = representationService.getTermWithError(sourceTermRId);

		return relationService.suggestRelationTypesForSourceTerm(source);
	}

	@Override
	@Transactional
	public Collection<BinaryFactTypeForm> findPossibleRelationTypesForSourceType(String sourceConceptTypeTermRId) {

		Defense.notEmpty(sourceConceptTypeTermRId, DGCErrorCodes.CONCEPT_TYPE_TERM_ID_NULL,
				DGCErrorCodes.CONCEPT_TYPE_TERM_ID_EMPTY, "sourceConceptTypeTermRId");

		Term type = representationService.getTermWithError(sourceConceptTypeTermRId);

		return relationService.suggestRelationTypesForSourceType(type);
	}

	@Override
	@Transactional
	public Collection<BinaryFactTypeForm> findPossibleRelationTypesForTargetTerm(String targetTermRId) {
		Defense.notEmpty(targetTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "targetTermRId");

		Term target = representationService.getTermWithError(targetTermRId);

		return relationService.suggestRelationTypesForTargetTerm(target);
	}

	@Override
	@Transactional
	public Collection<BinaryFactTypeForm> findPossibleRelationTypesForTargetType(String targetConceptTypeTermRId) {
		Defense.notEmpty(targetConceptTypeTermRId, DGCErrorCodes.CONCEPT_TYPE_TERM_ID_NULL,
				DGCErrorCodes.CONCEPT_TYPE_TERM_ID_EMPTY, "targetConceptTypeTermRId");
		Term type = representationService.getTermWithError(targetConceptTypeTermRId);

		return relationService.suggestRelationTypesForTargetType(type);
	}

	private Term getRelationTypeTerm(String signifier) {

		Defense.notEmpty(signifier, DGCErrorCodes.TERM_SIGNIFIER_NULL, DGCErrorCodes.TERM_SIGNIFIER_NULL, "signifier");

		Term term = representationService.findTermBySignifier(
				representationService.findMetamodelExtensionsVocabulary(), signifier);

		// look for term in SBVR vocabularies if not found in meta model extension vocabulary
		if (term == null) {
			for (Vocabulary sbvrVoc : representationService.findSbvrVocabulary().getAllIncorporatedVocabularies())
				if (term == null)
					term = representationService.findTermBySignifier(sbvrVoc, signifier);
		}

		if (term == null) {
			String message = "Source Term for the Relation Type with signifier '" + signifier + "' not found.";
			throw new EntityNotFoundException(DGCErrorCodes.RELATION_TYPE_TERM_NOT_FOUND_SIGNIFIER, signifier);
		}

		return term;
	}
}
