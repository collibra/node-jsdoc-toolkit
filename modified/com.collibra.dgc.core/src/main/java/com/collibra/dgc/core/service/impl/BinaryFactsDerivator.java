package com.collibra.dgc.core.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.MultiValueMap;

import com.collibra.dgc.core.dao.BinaryFactTypeFormDao;
import com.collibra.dgc.core.dao.ConceptDao;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.service.MeaningService;
import com.collibra.dgc.core.service.RepresentationService;

/**
 * This class creates the derived BFTFs based on the taxonomy of {@link Concept}s for a given {@link Term}.
 * <p>
 * For example: Person drives / driven by Car is a BFTF and Driven is a Person, then it implies that BFTF Driver drives
 * / driven by Car is possible and becomes the derived BFTF.
 * <p>
 * NOTE: After deriving the BFTFs a filter is applied to remove all the existing BFTFs in case they appear in the
 * derived ones.
 * @author amarnath
 * 
 */
class BinaryFactsDerivator {
	private final RepresentationFactory representationFactory;
	private final MeaningService meaningService;
	private final BinaryFactTypeFormDao binaryFactTypeFormDao;
	private final ServiceUtility serviceUtility;
	private final RepresentationService representationService;
	private final ConceptDao conceptDao;
	private Map<BinaryFactTypeForm, BinaryFactTypeForm> bftfToGeneralBftf;

	/**
	 * 
	 * @param representationService
	 * @param meaningService
	 * @param session
	 */
	public BinaryFactsDerivator(RepresentationService representationService,
			RepresentationFactory representationFactory, final MeaningService meaningService,
			final BinaryFactTypeFormDao binaryFactTypeDao, final ConceptDao conceptDao,
			final ServiceUtility serviceUtility) {
		this.representationService = representationService;
		this.representationFactory = representationFactory;
		this.meaningService = meaningService;
		this.binaryFactTypeFormDao = binaryFactTypeDao;
		this.serviceUtility = serviceUtility;
		this.conceptDao = conceptDao;
	}

	/**
	 * 
	 * @return
	 */
	public Map<BinaryFactTypeForm, BinaryFactTypeForm> getGeneralBftfs() {
		return bftfToGeneralBftf;
	}

	/**
	 * Get the derived {@link BinaryFactTypeForm}s.
	 * @param term The {@link Term} for which derived {@link BinaryFactTypeForm}s to be created.
	 * @return {@link Collection} of {@link BinaryFactTypeForm}s.
	 */
	public Collection<BinaryFactTypeForm> findDerivedFacts(Term term) {
		// Get existing BFTFs.
		List<BinaryFactTypeForm> existingBFTFs = binaryFactTypeFormDao.find(term);
		MultiValueMap derivedBFTFs = new MultiValueMap();
		Vocabulary tempVocabulary = serviceUtility.makeTemporaryVocabulary();
		bftfToGeneralBftf = new HashMap<BinaryFactTypeForm, BinaryFactTypeForm>();

		if (term.getObjectType() != null && term.getObjectType().getGeneralConcept() != null) {
			// Compute derived BFTFs.
			ObjectType generalConcept = meaningService.findObjectTypeByResourceId(term.getObjectType()
					.getGeneralConcept().getId());
			getDerivedFacts(term, generalConcept, derivedBFTFs, tempVocabulary);
		}

		// For the existing BFTFs get the substitutable BFTFs.
		for (BinaryFactTypeForm bftf : existingBFTFs) {
			if (bftf.getHeadTerm().equals(term)) {
				getSubstitutedDerivedFacts(term, bftf.getTailTerm(), bftf.getRole(), bftf.getCoRole(), derivedBFTFs,
						tempVocabulary);
			} else {
				getSubstitutedDerivedFacts(term, bftf.getHeadTerm(), bftf.getCoRole(), bftf.getRole(), derivedBFTFs,
						tempVocabulary);
			}
		}

		// Filter to remove the existing BFTFs.
		filter(term, existingBFTFs, derivedBFTFs);

		return derivedBFTFs.values();
	}

	private void getDerivedFacts(Term term, ObjectType generalConcept, MultiValueMap derivedBFTFs,
			Vocabulary tempVocabulary) {
		Term preferredTerm = representationService.findPreferredTermInAllIncorporatedVocabularies(generalConcept,
				term.getVocabulary());

		// FIXME: Is it possible to have a null preferred term?
		if (preferredTerm == null) {
			return;
		}

		if (!serviceUtility.isSbvrVocabulary(preferredTerm.getVocabulary())) {
			List<BinaryFactTypeForm> parentBftfs = binaryFactTypeFormDao.find(preferredTerm);

			for (BinaryFactTypeForm bftf : parentBftfs) {
				BinaryFactTypeForm newbftf = null;
				if (bftf.getHeadTerm().equals(preferredTerm)) {
					newbftf = representationFactory.makeBinaryFactTypeForm(tempVocabulary, term, bftf.getRole(),
							bftf.getCoRole(), bftf.getTailTerm());
				} else {
					newbftf = representationFactory.makeBinaryFactTypeForm(tempVocabulary, term, bftf.getCoRole(),
							bftf.getRole(), bftf.getHeadTerm());
				}

				if (derivedBFTFs.getCollection(newbftf.getTailTerm()) == null
						|| !derivedBFTFs.getCollection(newbftf.getTailTerm()).contains(newbftf)) {
					derivedBFTFs.put(newbftf.getTailTerm(), newbftf);

					// Keep the map of BFTF to its general BFTF
					bftfToGeneralBftf.put(newbftf, bftf);

					// Get substitutable BFTFs.
					getSubstitutedDerivedFacts(term, newbftf.getTailTerm(), newbftf.getRole(), newbftf.getCoRole(),
							derivedBFTFs, tempVocabulary);
				}
			}

			if (generalConcept.getGeneralConcept() != null) {
				// Recursively go through taxonomy to get derived BFTFs.
				ObjectType preferredGeneralConceptOT = meaningService.findObjectTypeByResourceId(generalConcept
						.getGeneralConcept().getId());
				if (preferredGeneralConceptOT != null) {
					getDerivedFacts(term, preferredGeneralConceptOT, derivedBFTFs, tempVocabulary);
				}
			}
		}
	}

	private void getSubstitutedDerivedFacts(Term term, Term derivedTerm, String role, String corole,
			MultiValueMap derivedBFTFs, Vocabulary tempVocabulary) {
		ObjectType derivedOT = meaningService.findObjectTypeByResourceId(derivedTerm.getObjectType().getId());

		for (Concept child : conceptDao.findSpecializedConcepts(derivedOT)) {
			ObjectType childOT = meaningService.findObjectTypeByResourceId(child.getId());
			if (childOT == null) {
				continue;
			}

			Term preferredTerm = representationService.findPreferredTermInAllIncorporatedVocabularies(childOT,
					derivedTerm.getVocabulary());

			if (preferredTerm != null) {
				if (!serviceUtility.isSbvrVocabulary(preferredTerm.getVocabulary())) {
					BinaryFactTypeForm newbftf = representationFactory.makeBinaryFactTypeForm(tempVocabulary, term,
							role, corole, preferredTerm);

					if (derivedBFTFs.getCollection(newbftf.getTailTerm()) == null
							|| !derivedBFTFs.getCollection(newbftf.getTailTerm()).contains(newbftf)) {
						derivedBFTFs.put(newbftf.getTailTerm(), newbftf);

						// Recursively find the substitutable derived BFTFs.
						getSubstitutedDerivedFacts(term, preferredTerm, role, corole, derivedBFTFs, tempVocabulary);
					}
				}
			}
		}
	}

	private void filter(Term term, List<BinaryFactTypeForm> existingBFTFs, MultiValueMap derivedBFTFs) {
		for (BinaryFactTypeForm bftf : existingBFTFs) {
			if (bftf.getHeadTerm().equals(term)) {
				if (derivedBFTFs.keySet().contains(bftf.getTailTerm())) {
					LinkedList<BinaryFactTypeForm> tempList = new LinkedList<BinaryFactTypeForm>(
							derivedBFTFs.getCollection(bftf.getTailTerm()));
					for (BinaryFactTypeForm derived : tempList) {
						if (derived.getRole().equals(bftf.getRole()) && derived.getCoRole().equals(bftf.getCoRole())) {
							derivedBFTFs.remove(bftf.getTailTerm(), derived);
						}
					}
				}
			} else {
				if (derivedBFTFs.keySet().contains(bftf.getHeadTerm())) {
					LinkedList<BinaryFactTypeForm> tempList = new LinkedList<BinaryFactTypeForm>(
							derivedBFTFs.getCollection(bftf.getHeadTerm()));
					for (BinaryFactTypeForm derived : tempList) {
						if (derived.getRole().equals(bftf.getCoRole()) && derived.getCoRole().equals(bftf.getRole())) {
							derivedBFTFs.remove(bftf.getHeadTerm(), derived);
						}
					}
				}
			}
		}
	}
}
