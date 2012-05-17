package com.collibra.dgc.core.service.impl;

import java.util.HashSet;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.dao.AttributeDao;
import com.collibra.dgc.core.dao.BinaryFactTypeFormDao;
import com.collibra.dgc.core.dao.CharacteristicFormDao;
import com.collibra.dgc.core.dao.ConceptDao;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.dao.RepresentationDao;
import com.collibra.dgc.core.dao.TermDao;
import com.collibra.dgc.core.dao.VocabularyDao;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.representation.impl.AttributeImpl;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.service.AttributeService;
import com.collibra.dgc.core.service.MeaningService;

/**
 * 
 * @author amarnath
 * 
 */
@Service
public class RepresentationServiceHelper extends AbstractService {
	private static Logger log = LoggerFactory.getLogger(RepresentationServiceHelper.class);

	@Autowired
	private ConceptDao conceptDao;
	@Autowired
	private RepresentationDao representationDao;
	@Autowired
	private VocabularyDao vocabularyDao;
	@Autowired
	private BinaryFactTypeFormDao binaryFactTypeFormDao;
	@Autowired
	private TermDao termDao;
	@Autowired
	private CharacteristicFormDao characteristicFormDao;
	@Autowired
	private AttributeDao attributeDao;
	@Autowired
	private ObjectTypeDao objectTypeDao;
	@Autowired
	private MeaningService meaningService;
	@Autowired
	private ConstraintChecker constraintChecker;
	@Autowired
	private RuleServiceHelper ruleServiceHelper;
	@Autowired
	private AttributeService attributeService;

	/**
	 * Create a vocabulary without transaction management
	 * 
	 * @param object
	 * @return
	 */
	final Vocabulary saveVocabulary(final Vocabulary vocabulary) {
		constraintChecker.checkConstraints(vocabulary);

		final Vocabulary persistedVocabulary = vocabularyDao.save(vocabulary);

		// Create the rule sets.
		for (RuleSet ruleSet : new LinkedList<RuleSet>(persistedVocabulary.getRuleSets())) {
			// log
			ruleServiceHelper.saveRuleSet(ruleSet);
		}

		for (Term term : new LinkedList<Term>(persistedVocabulary.getTerms())) {

			saveTermAndCascades(term);
		}

		for (BinaryFactTypeForm binaryFactTypeForm : new LinkedList<BinaryFactTypeForm>(
				persistedVocabulary.getBinaryFactTypeForms())) {
			saveBinaryFactTypeFormAndCascades(binaryFactTypeForm);
		}

		for (CharacteristicForm characteristicForm : new LinkedList<CharacteristicForm>(
				persistedVocabulary.getCharacteristicForms())) {
			saveCharacteristicFormAndCascades(characteristicForm);
		}

		return persistedVocabulary;
	}

	final BinaryFactTypeForm saveBinaryFactTypeForm(final BinaryFactTypeForm binaryFactTypeForm) {

		constraintChecker.checkConstraints(binaryFactTypeForm);
		return saveBinaryFactTypeFormAndCascades(binaryFactTypeForm);
	}

	private BinaryFactTypeForm saveBinaryFactTypeFormAndCascades(BinaryFactTypeForm binaryFactTypeForm) {
		if (binaryFactTypeForm.getStatus() == null) {
			binaryFactTypeForm.setStatus(attributeService.findMetaCandidateStatus());
		}

		saveTermAndCascades(binaryFactTypeForm.getHeadTerm());
		saveTermAndCascades(binaryFactTypeForm.getTailTerm());

		// First create attributes.
		saveAttributesInternal(binaryFactTypeForm);

		BinaryFactType binaryFactType = binaryFactTypeForm.getBinaryFactType();

		meaningService.saveAndCascade(binaryFactType);

		binaryFactTypeFormDao.save(binaryFactTypeForm);

		return binaryFactTypeForm;
	}

	final CharacteristicForm saveCharacteristicForm(final CharacteristicForm characteristicForm) {

		constraintChecker.checkConstraints(characteristicForm);
		return saveCharacteristicFormAndCascades(characteristicForm);
	}

	private CharacteristicForm saveCharacteristicFormAndCascades(CharacteristicForm characteristicForm) {

		if (characteristicForm.getStatus() == null) {
			characteristicForm.setStatus(attributeService.findMetaCandidateStatus());
		}

		if (!characteristicForm.getTerm().isPersisted()) {
			saveTermAndCascades(characteristicForm.getTerm());
		}

		// First create attributes
		saveAttributesInternal(characteristicForm);

		Characteristic characteristic = characteristicForm.getCharacteristic();

		meaningService.saveAndCascade(characteristic);

		characteristicFormDao.save(characteristicForm);

		return characteristicForm;
	}

	final Term saveTerm(final Term term) {

		if (isAttributeTypeLabel(term))
			constraintChecker.checkAttributeTypeConstraints(term);

		else if (isStatusTypeLabel(term))
			constraintChecker.checkStatusConstraints(term);

		else
			constraintChecker.checkConstraints(term);

		saveTermAndCascades(term);

		getCurrentSession().flush();
		return term;
	}

	private Term saveTermAndCascades(Term term) {
		// check if the term we are creating already has a standard candidate status attribute.
		// if not, we add create it here.
		// If a term is newly created and has no attributes yet, we can assume we must add the status attribute
		if (term.getStatus() == null) {
			term.setStatus(attributeService.findMetaCandidateStatus());
		}

		saveAttributesInternal(term);

		// do something if the term is not yet persistent or if it is dirty
		ObjectType concept = term.getObjectType();
		if (!concept.isPersisted()) {
			// TODO: Fix this switch case for 3.1.0 release.
			if (concept instanceof CategorizationType) {
				meaningService.saveAndCascade((CategorizationType) concept);
			} else if (concept instanceof Category) {
				meaningService.saveAndCascade((Category) concept);
			} else {
				meaningService.saveAndCascade(concept);
			}
		}

		termDao.save(term);

		return term;
	}

	private final void saveAttributesInternal(final Representation representation) {
		if (representation.getIsPreferred()) {
			for (Attribute attr : new HashSet<Attribute>(representation.getAttributes())) {
				saveAttributeInternal(representation, attr);
			}
		}
	}

	private final void saveAttributeInternal(Representation representation, Attribute attr) {
		((AttributeImpl) attr).setOwner(representation);

		attributeDao.save(attr);
	}

	/**
	 * Save only the attribute and check constraints.
	 * 
	 * @param attribute The {@link Attribute}
	 * @return The persisted attribute.
	 */
	final Attribute saveAttribute(final Attribute attribute) {

		constraintChecker.checkConstraints(attribute);

		attributeDao.save(attribute);

		return attribute;
	}

	/**
	 * Check if the term is an attribute type label term.
	 */
	public boolean isAttributeTypeLabel(final Term term) {

		if (term.getObjectType().getType() == null)
			return false;

		return ServiceUtility.hasSuperConcept(term.getObjectType().getType(),
				objectTypeDao.findById(MeaningConstants.META_ATTRIBUTE_TYPE_UUID));
	}

	/**
	 * Check if the term is a status label term.
	 */
	public boolean isStatusTypeLabel(final Term term) {

		return term.getVocabulary().getUri().equals(Constants.STATUSES_VOCABULARY_URI);
	}
}
