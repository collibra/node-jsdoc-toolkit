package com.collibra.dgc.core.component.representation.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.representation.RepresentationComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.service.AttributeService;
import com.collibra.dgc.core.service.MeaningService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.util.Defense;

/**
 * {@link Representation} API implementation.
 * 
 * @author amarnath
 * @author pmalarme
 * 
 */
@Service(value = "RepresentationComponentImpl")
public class RepresentationComponentImpl implements RepresentationComponent {

	@Autowired
	protected RepresentationFactory representationFactory;

	@Autowired
	protected RepresentationService representationService;

	@Autowired
	protected MeaningService meaningService;

	@Autowired
	protected AttributeService attributeService;

	@Override
	@Transactional
	public Representation getRepresentation(String rId) {

		Defense.notEmpty(rId, DGCErrorCodes.REPRESENTATION_ID_NULL, DGCErrorCodes.REPRESENTATION_ID_EMPTY, "rId");

		return representationService.getRepresentationWithError(rId);
	}

	@Override
	@Transactional
	public Term getConceptType(String rId) {

		return representationService.findConceptTypeRepresentation(getRepresentation(rId));
	}

	@Override
	@Transactional
	public Representation getGeneralConcept(String rId) {

		return representationService.findGeneralConceptRepresentation(getRepresentation(rId));

	}

	@Override
	@Transactional
	public Collection<Representation> getSpecializedConcepts(String rId, int limit) {

		return representationService.findSpecializedConceptRepresentations(getRepresentation(rId), limit);
	}

	@Override
	@Transactional
	public Collection<Representation> getSynonyms(String rId) {

		return representationService.findSynonyms(getRepresentation(rId));
	}

	@Override
	@Transactional
	public Collection<Term> findPossibleStatuses(String rId) {

		// TODO update the service method
		// TODO implement the unit test
		return representationService.findStatusTerms(getRepresentation(rId));
	}

	@Override
	@Transactional
	public Collection<Term> findPossibleAttributeTypes(String rId) {

		// TODO update the service method
		// TODO implement the unit test
		return attributeService.findAttributeTypeLabels(getRepresentation(rId));
	}

	@Override
	@Transactional
	public Collection<Term> findPossibleConceptTypes(String rId) {
		// TODO put defense statements
		// TODO update to the right method
		// TODO implement the unit test
		return representationService.findMetamodelExtensionsVocabulary().getTerms();
	}

	@Override
	@Transactional
	public Representation changeConceptType(String rId, String conceptTypeTermRId) {

		Defense.notEmpty(conceptTypeTermRId, DGCErrorCodes.CONCEPT_TYPE_TERM_ID_NULL,
				DGCErrorCodes.CONCEPT_TYPE_TERM_ID_EMPTY, "conceptTypeTermRId");

		return representationService.setConceptTypeRepresentation(getRepresentation(rId),
				representationService.getTermWithError(conceptTypeTermRId));
	}

	@Override
	@Transactional
	public Representation changeGeneralConcept(String specializedRepresentationRId,
			String generalRepresentationResourceId) {

		return representationService.setGeneralConceptRepresentation(getRepresentation(specializedRepresentationRId),
				getRepresentation(generalRepresentationResourceId));
	}

	@Override
	@Transactional
	public Representation changeStatus(String rId, String statusTermRId) {

		Defense.notEmpty(statusTermRId, DGCErrorCodes.STATUS_TERM_ID_NULL, DGCErrorCodes.STATUS_TERM_ID_EMPTY,
				"statusTermRId");

		return representationService.changeStatus(getRepresentation(rId),
				representationService.getTermWithError(statusTermRId));
	}

	@Override
	@Transactional
	public Representation addSynonym(String currentRepresentationRId, String selectedRepresentationRId) {

		return representationService.addSynonym(getRepresentation(selectedRepresentationRId),
				getRepresentation(currentRepresentationRId));
	}

	@Override
	@Transactional
	public Representation removeGeneralConcept(String rId) {

		meaningService.removeGeneralConcept((Concept) getRepresentation(rId).getMeaning());

		return representationService.findRepresentationByResourceId(rId);
	}

	@Override
	@Transactional
	public Representation removeSynonym(String rId) {

		return representationService.removeSynonym(getRepresentation(rId));
	}

	@Override
	@Transactional
	public void remove(String rId) {

		representationService.remove(getRepresentation(rId));
	}

}
