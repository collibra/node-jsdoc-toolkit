package com.collibra.dgc.core.component.impl;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.VocabularyComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.impl.VocabularyImpl;
import com.collibra.dgc.core.service.CommunityService;
import com.collibra.dgc.core.service.MeaningService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.util.Defense;

/**
 * {@link Vocabulary} API implementation.
 * 
 * @author amarnath
 * @author pmalarme
 * 
 */
@Service
public class VocabularyComponentImpl implements VocabularyComponent {
	@Autowired
	private RepresentationService representationService;

	@Autowired
	private RepresentationFactory representationFactory;

	@Autowired
	private CommunityService communityService;

	@Autowired
	private MeaningService meaningService;

	@Override
	@Transactional
	public Vocabulary addVocabulary(String communityRId, String name, String uri) {

		Defense.notEmpty(name, DGCErrorCodes.VOCABULARY_NAME_NULL, DGCErrorCodes.VOCABULARY_NAME_EMPTY, "name");
		Defense.notEmpty(uri, DGCErrorCodes.VOCABULARY_URI_NULL, DGCErrorCodes.VOCABULARY_URI_EMPTY, "uri");
		Defense.notEmpty(communityRId, DGCErrorCodes.COMMUNITY_ID_NULL, DGCErrorCodes.COMMUNITY_ID_EMPTY,
				"communityRId");

		ObjectType glossaryType = meaningService.findMetaGlossaryVocabularyType();

		Vocabulary vocabulary = representationFactory.makeVocabularyOfType(
				communityService.getCommunityWithError(communityRId), uri, name, glossaryType);

		return representationService.saveVocabulary(vocabulary);
	}

	@Override
	@Transactional
	public Vocabulary addVocabulary(String communityRId, String name, String uri, String typeRId) {

		Defense.notEmpty(name, DGCErrorCodes.VOCABULARY_NAME_NULL, DGCErrorCodes.VOCABULARY_NAME_EMPTY, "name");
		Defense.notEmpty(uri, DGCErrorCodes.VOCABULARY_URI_NULL, DGCErrorCodes.VOCABULARY_URI_EMPTY, "uri");
		Defense.notEmpty(communityRId, DGCErrorCodes.COMMUNITY_ID_NULL, DGCErrorCodes.COMMUNITY_ID_EMPTY,
				"communityRId");
		Defense.notEmpty(typeRId, DGCErrorCodes.VOCABULARY_TYPE_ID_NULL, DGCErrorCodes.VOCABULARY_TYPE_ID_EMPTY,
				"typeRId");

		ObjectType glossaryType = meaningService.getObjectTypeWithError(typeRId);

		Vocabulary vocabulary = representationFactory.makeVocabularyOfType(
				communityService.getCommunityWithError(communityRId), uri, name, glossaryType);

		return representationService.saveVocabulary(vocabulary);
	}

	@Override
	@Transactional
	public Vocabulary getVocabulary(String rId) {

		Defense.notEmpty(rId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY, "rId");

		return representationService.getVocabularyWithError(rId);
	}

	@Override
	@Transactional
	public Vocabulary getVocabularyByUri(String uri) {

		Defense.notEmpty(uri, DGCErrorCodes.VOCABULARY_URI_NULL, DGCErrorCodes.VOCABULARY_URI_EMPTY, "uri");

		final Vocabulary vocabulary = representationService.findVocabularyByUri(uri);

		if (vocabulary == null) {
			String message = "Vocabulary with URI '" + uri + "' not found.";
			throw new EntityNotFoundException(DGCErrorCodes.VOCABULARY_NOT_FOUND_URI, uri);
		}

		return vocabulary;
	}

	@Override
	@Transactional
	public Vocabulary getVocabularyByName(String name) {

		Defense.notEmpty(name, DGCErrorCodes.VOCABULARY_NAME_NULL, DGCErrorCodes.VOCABULARY_NAME_EMPTY, "name");

		final Vocabulary vocabulary = representationService.findVocabularyByName(name);

		if (vocabulary == null) {
			String message = "Vocabulary with name '" + name + "' not found.";
			throw new EntityNotFoundException(DGCErrorCodes.VOCABULARY_NOT_FOUND_NAME, name);
		}

		return vocabulary;
	}

	@Override
	@Transactional
	public Set<Vocabulary> getIncorporatedVocabularies(String rId, boolean excludeSbvr) {

		if (excludeSbvr)
			return getVocabulary(rId).getAllNonSbvrIncorporatedVocabularies();
		else
			return getVocabulary(rId).getAllIncorporatedVocabularies();
	}

	@Override
	@Transactional
	public Collection<Vocabulary> getIncorporatingVocabularies(String rId) {

		return representationService.findAllIncorporatingVocabularies(getVocabulary(rId));
	}

	@Override
	@Transactional
	public Collection<Vocabulary> findPossibleVocabulariesToInCorporate(String rId) {

		return representationService.findVocabulariesToInCorporate(getVocabulary(rId));
	}

	@Override
	@Transactional
	public Collection<Vocabulary> getVocabularies() {
		return representationService.findVocabularies();
	}

	@Override
	@Transactional
	public Collection<Vocabulary> getNonMetaVocabularies() {
		return representationService.findVocabularies(true);
	}

	@Override
	@Transactional
	public Collection<Vocabulary> findVocabulariesContainingName(String searchName, int offset, int number) {

		Defense.notEmpty(searchName, DGCErrorCodes.VOCABULARY_NAME_NULL, DGCErrorCodes.VOCABULARY_NAME_EMPTY,
				"searchName");

		return representationService.searchVocabulariesForName(searchName, offset, number);
	}

	@Override
	@Transactional
	public Term getPreferredTerm(String rId, String objectTypeRId) {

		Defense.notEmpty(objectTypeRId, DGCErrorCodes.OBJECT_TYPE_ID_NULL, DGCErrorCodes.OBJECT_TYPE_ID_EMPTY,
				"objectTypeRId");

		return representationService.findPreferredTerm(meaningService.getObjectTypeWithError(objectTypeRId),
				getVocabulary(rId));
	}

	@Override
	@Transactional
	public Term getPreferredTermInIncorporatedVocabularies(String rId, String objectTypeRId) {

		Defense.notEmpty(objectTypeRId, DGCErrorCodes.OBJECT_TYPE_ID_NULL, DGCErrorCodes.OBJECT_TYPE_ID_EMPTY,
				"objectTypeRId");

		return representationService.findPreferredTermInAllIncorporatedVocabularies(
				meaningService.getObjectTypeWithError(objectTypeRId), getVocabulary(rId));

	}

	@Override
	@Transactional
	public Collection<Term> getPreferredTerms(String rId) {

		return representationService.findAllPreferredTerms(getVocabulary(rId));
	}

	@Override
	@Transactional
	public Collection<Term> getPreferredTermsInIncorporatedVocabularies(String rId) {

		return representationService.findAllPreferredTermsInAllIncorporatedVocabularies(getVocabulary(rId));
	}

	@Override
	@Transactional
	public Representation getPreferredRepresentation(String rId, String conceptRId) {

		Defense.notEmpty(conceptRId, DGCErrorCodes.CONCEPT_ID_NULL, DGCErrorCodes.CONCEPT_ID_EMPTY, "conceptRId");

		return representationService.findPreferredRepresentation(meaningService.getConceptWithError(conceptRId),
				getVocabulary(rId));
	}

	@Override
	@Transactional
	public Representation getPreferredRepresentationInIncorporatedVocabularies(String rId, String conceptRId) {

		Defense.notEmpty(conceptRId, DGCErrorCodes.CONCEPT_ID_NULL, DGCErrorCodes.CONCEPT_ID_EMPTY, "conceptRId");

		return representationService.findPreferredRepresentationInAllIncorporatedVocabularies(
				meaningService.getConceptWithError(conceptRId), getVocabulary(rId));
	}

	@Override
	@Transactional
	public Vocabulary changeName(String rId, String newName) {

		Defense.notEmpty(newName, DGCErrorCodes.VOCABULARY_NAME_NULL, DGCErrorCodes.VOCABULARY_NAME_EMPTY, "newName");

		return representationService.changeName(getVocabulary(rId), newName);
	}

	@Override
	@Transactional
	public Vocabulary changeUri(String rId, String newUri) {

		Defense.notEmpty(newUri, DGCErrorCodes.VOCABULARY_URI_NULL, DGCErrorCodes.VOCABULARY_URI_EMPTY, "newUri");

		return representationService.changeUri(getVocabulary(rId), newUri);
	}

	@Override
	@Transactional
	public Vocabulary changeType(String rId, String typeRId) {

		Defense.notEmpty(typeRId, DGCErrorCodes.VOCABULARY_TYPE_ID_NULL, DGCErrorCodes.VOCABULARY_TYPE_ID_EMPTY,
				"objectTypeRId");

		Vocabulary voc = getVocabulary(rId);
		ObjectType vocType = meaningService.getObjectTypeWithError(typeRId);

		((VocabularyImpl) voc).setType(vocType);

		return representationService.saveVocabulary(voc);
	}

	@Override
	@Transactional
	public Vocabulary changeCommunity(String rId, String newCommunityRId) {

		Defense.notEmpty(newCommunityRId, DGCErrorCodes.COMMUNITY_ID_NULL, DGCErrorCodes.COMMUNITY_ID_EMPTY,
				newCommunityRId);

		return representationService.changeCommunity(getVocabulary(rId),
				communityService.getCommunityWithError(newCommunityRId));
	}

	@Override
	@Transactional
	public Vocabulary addIncorporatedVocabulary(String rId, String vocabularyToIncorporateRId) {

		Vocabulary incorporatingVocabulary = getVocabulary(rId);
		Vocabulary vocabularyToIncorporate = getVocabulary(vocabularyToIncorporateRId);

		return representationService.incorporate(incorporatingVocabulary, vocabularyToIncorporate);
	}

	@Override
	@Transactional
	public Vocabulary removeIncorporatedVocabulary(String rId, String incorporatedVocabularyRId) {

		Vocabulary incorporatingVocabulary = getVocabulary(rId);
		Vocabulary incorporatedVocabulary = getVocabulary(incorporatedVocabularyRId);

		return representationService.disincorporateVocabulary(incorporatingVocabulary, incorporatedVocabulary);
	}

	@Override
	@Transactional
	public void removeVocabulary(String rId) {

		representationService.remove(getVocabulary(rId));
	}
}
