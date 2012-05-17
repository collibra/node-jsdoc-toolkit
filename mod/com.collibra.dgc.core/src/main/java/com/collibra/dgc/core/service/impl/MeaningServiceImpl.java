package com.collibra.dgc.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.BinaryFactTypeDao;
import com.collibra.dgc.core.dao.CategorizationTypeDao;
import com.collibra.dgc.core.dao.CategoryDao;
import com.collibra.dgc.core.dao.CharacteristicDao;
import com.collibra.dgc.core.dao.ConceptDao;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.dao.RuleDao;
import com.collibra.dgc.core.dao.SimplePropositionDao;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.SimpleProposition;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.rules.Rule;
import com.collibra.dgc.core.model.rules.impl.RuleImpl;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.events.EventType;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.observer.events.MeaningEventData;
import com.collibra.dgc.core.service.MeaningService;

@Service
public class MeaningServiceImpl extends AbstractService implements MeaningService {

	private final Logger log = LoggerFactory.getLogger(MeaningServiceImpl.class);

	@Autowired
	private ObjectTypeDao objectTypeDao;
	@Autowired
	private ConceptDao conceptDao;
	@Autowired
	private BinaryFactTypeDao binaryFactTypeDao;
	@Autowired
	private CharacteristicDao characteristicDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private CategorizationTypeDao categorizationTypeDao;
	@Autowired
	private SimplePropositionDao simplePropositionDao;
	@Autowired
	private RuleDao ruleDao;
	@Autowired
	private CategorizationServiceHelperImpl categorizationHelper;

	@Autowired
	private ConstraintChecker constraintChecker;

	@Override
	public void remove(Meaning meaning) {
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.MEANING,
				new MeaningEventData(meaning, EventType.REMOVING));

		meaning = ServiceUtility.deproxy(meaning, Meaning.class);

		if (meaning instanceof Concept) {
			// Remove taxonomy
			Concept concept = (Concept) meaning;
			List<Concept> specializedConceptsForRepresentations = findAllSpecializedConcepts(concept);
			for (Concept specializedConcept : specializedConceptsForRepresentations) {
				removeGeneralConcept(specializedConcept);
			}

			categorizationHelper.cleanupSchemes(concept);
		}

		// Remove categorization
		if (meaning instanceof CategorizationType) {
			categorizationHelper.cleanupCategories((CategorizationType) meaning);
		} else if (meaning instanceof Category) {
			categorizationHelper.cleanupSchemes((Category) meaning);
		}

		getCurrentSession().delete(meaning);

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.MEANING,
				new MeaningEventData(meaning, EventType.REMOVED));
	}

	@Override
	public ObjectType update(final ObjectType objectType) {
		objectTypeDao.save(objectType);
		return objectType;
	}

	@Override
	public Characteristic update(Characteristic characteristic) {
		characteristicDao.save(characteristic);
		return characteristic;
	}

	@Override
	public BinaryFactType update(BinaryFactType binaryFactType) {
		binaryFactTypeDao.save(binaryFactType);
		return binaryFactType;
	}

	private BinaryFactType saveAndCascade(BinaryFactType binaryFactType, final Set<String> done) {
		constraintChecker.checkConstraints(binaryFactType);
		cascadeConceptCreates(binaryFactType, done);
		binaryFactTypeDao.save(binaryFactType);
		return binaryFactType;
	}

	@Override
	public BinaryFactType saveAndCascade(BinaryFactType binaryFactType) {
		return saveAndCascade(binaryFactType, new HashSet<String>());
	}

	private Characteristic saveAndCascade(Characteristic characteristic, final Set<String> done) {
		constraintChecker.checkConstraints(characteristic);
		cascadeConceptCreates(characteristic, done);
		characteristicDao.save(characteristic);
		return characteristic;
	}

	@Override
	public Characteristic saveAndCascade(Characteristic characteristic) {
		return saveAndCascade(characteristic, new HashSet<String>());
	}

	private void cascadeConceptCreates(Concept concept, final Set<String> done) {
		done.add(concept.getId());
		if (concept.getGeneralConcept() != null && !done.contains(concept.getGeneralConcept().getId())
				&& !concept.getGeneralConcept().isPersisted()) {
			saveConcept(concept.getGeneralConcept(), done);
		}

		if (concept.getType() != null && !done.contains(concept.getType().getId()) && !concept.getType().isPersisted()
				&& !concept.getType().equals(concept)) {
			saveConcept(concept.getType(), done);
		}

	}

	@Override
	public Collection<Concept> findAllCategorizationSchemes(ObjectType generalConcept) {
		throw new NotImplementedException("This method needs to be implemented.");
	}

	@Override
	public Meaning saveAndCascade(Meaning meaning) {
		if (meaning instanceof Concept) {
			meaning = saveConcept((Concept) meaning);
		} else if (meaning instanceof SimpleProposition) {
			saveSimpleProposition((SimpleProposition) meaning);
		} else if (meaning instanceof Rule) {
			saveRule((Rule) meaning);
		}
		return meaning;
	}

	@Override
	public List<Concept> findTaxonomicalParentConcepts(Concept generalConcept) {
		List<Concept> results = new ArrayList<Concept>();
		findTaxonomicalParentConceptsHelper(generalConcept, results);
		return results;
	}

	private void findTaxonomicalParentConceptsHelper(Concept generalConcept, List<Concept> parents) {
		if (generalConcept == null || parents.contains(generalConcept)) {
			return;
		}
		parents.add(generalConcept);
		findTaxonomicalParentConceptsHelper(generalConcept.getGeneralConcept(), parents);
	}

	@Override
	public boolean hasGeneralConcept(Concept concept, Concept parent) {
		Concept generalConcept = concept;
		ObjectType metaThing = findMetaThing();
		while (generalConcept != null && !generalConcept.equals(metaThing)) {
			if (generalConcept.equals(parent))
				return true;
			else
				generalConcept = generalConcept.getGeneralConcept();
		}
		return false;
	}

	/**
	 * Checks the type of the general concept and persists it accordingly. If unknown default to {@link ObjectType}.
	 * @param concept The general concept to decide the type for
	 * @return The persisted general concept
	 */
	@Override
	public Concept saveConcept(Concept concept) {
		return saveConcept(concept, new HashSet<String>());
	}

	private Concept saveConcept(Concept concept, final Set<String> done) {
		if (concept instanceof CategorizationType) {
			return saveAndCascade((CategorizationType) concept, done);
		} else if (concept instanceof Category) {
			return saveAndCascade((Category) concept, done);
		} else if (concept instanceof ObjectType) {
			return saveAndCascade((ObjectType) concept, done);
		} else if (concept instanceof Characteristic) {
			return saveAndCascade((Characteristic) concept, done);
		} else if (concept instanceof BinaryFactType) {
			return saveAndCascade((BinaryFactType) concept, done);
		} else if (concept instanceof HibernateProxy) {
			return saveConcept(ServiceUtility.deproxy(concept, Concept.class), done);
		} else {
			throw new IllegalArgumentException("Unknown concept type");
		}
	}

	public Category saveAndCascade(Category category, final Set<String> done) {
		constraintChecker.checkConstraints(category);
		cascadeConceptCreates(category, done);
		categoryDao.save(category);
		return category;
	}

	@Override
	public Category saveAndCascade(Category category) {
		return saveAndCascade(category, new HashSet<String>());
	}

	public CategorizationType saveAndCascade(CategorizationType categorizationType, final Set<String> done) {
		constraintChecker.checkConstraints(categorizationType);
		cascadeConceptCreates(categorizationType, done);
		categorizationTypeDao.save(categorizationType);
		return categorizationType;
	}

	@Override
	public CategorizationType saveAndCascade(CategorizationType categorizationType) {
		return saveAndCascade(categorizationType, new HashSet<String>());
	}

	public ObjectType saveAndCascade(ObjectType objectType, final Set<String> done) {
		constraintChecker.checkConstraints(objectType);
		cascadeConceptCreates(objectType, done);
		objectTypeDao.save(objectType);
		return objectType;
	}

	@Override
	public ObjectType saveAndCascade(ObjectType objectType) {
		return saveAndCascade(objectType, new HashSet<String>());
	}

	public SimpleProposition saveSimpleProposition(SimpleProposition simpleProposition) {
		simplePropositionDao.save(simpleProposition);
		return simpleProposition;
	}

	public Rule saveRule(Rule rule) {
		if (Rule.MANDATORY.equals(rule.getGlossaryConstraintType())) {
			((RuleImpl) rule).setConstraintType(objectTypeDao.getMetaMandatoryConstraintType());
		} else if (Rule.UNIQUENESS.equals(rule.getGlossaryConstraintType())) {
			((RuleImpl) rule).setConstraintType(objectTypeDao.getMetaUniquenessConstraintType());
		} else {
			((RuleImpl) rule).setConstraintType(objectTypeDao.getMetaSemiparsedConstraintType());
		}

		ruleDao.save(rule);
		return rule;
	}

	@Override
	public ObjectType findMetaThing() {
		return objectTypeDao.getMetaThing();
	}

	@Override
	public ObjectType findMetaObjectType() {
		return objectTypeDao.getMetaObjectType();
	}

	@Override
	public ObjectType findMetaBusinessAsset() {
		return objectTypeDao.getMetaBusinessAsset();
	}

	@Override
	public ObjectType findMetaBusinessTerm() {
		return objectTypeDao.getMetaBusinessTerm();
	}

	@Override
	public ObjectType findMetaTechnicalAsset() {
		return objectTypeDao.getMetaTechnicalAsset();
	}

	@Override
	public ObjectType findMetaCode() {
		return objectTypeDao.getMetaCode();
	}

	@Override
	public ObjectType findMetaQualityAsset() {
		return objectTypeDao.getMetaQualityAsset();
	}

	@Override
	public ObjectType findMetaBinaryFactType() {
		return objectTypeDao.getMetaBinaryFactType();
	}

	@Override
	public ObjectType findMetaCharacteristic() {
		return objectTypeDao.getMetaCharacteristic();
	}

	@Override
	public ObjectType findMetaIndividualConcept() {
		return objectTypeDao.getMetaIndividualConcept();
	}

	@Override
	public ObjectType findMetaCategory() {
		return objectTypeDao.getMetaCategory();
	}

	@Override
	public ObjectType findMetaDefinition() {
		return objectTypeDao.getMetaDefinition();
	}

	@Override
	public ObjectType findMetaDescription() {
		return objectTypeDao.getMetaDescription();
	}

	@Override
	public ObjectType findMetaExample() {
		return objectTypeDao.getMetaExample();
	}

	@Override
	public ObjectType findMetaNote() {
		return objectTypeDao.getMetaNote();
	}

	@Override
	public List<ObjectType> findAllObjectTypesRepresentedInVocabulary(final Vocabulary vocabulary) {
		return objectTypeDao.findAllObjectTypesRepresentedInVocabulary(vocabulary);
	}

	@Override
	public List<Concept> findAllConceptsWithType(Concept conceptType) {
		return conceptDao.findAllConceptsWithConceptType(conceptType);
	}

	@Override
	public ObjectType getMetaStatusType() {
		return objectTypeDao.getMetaStatusType();
	}

	@Override
	public ObjectType findMetaVocabularyType() {
		return objectTypeDao.getMetaVocabularyType();
	}

	@Override
	public ObjectType findMetaBusinessVocabularyType() {
		return objectTypeDao.getMetaBusinessVocabularyType();
	}

	@Override
	public ObjectType findMetaGlossaryVocabularyType() {
		return objectTypeDao.getMetaGlossaryType();
	}

	@Override
	public ObjectType findObjectTypeByResourceId(final String fromString) {
		return objectTypeDao.findById(fromString);
	}

	@Override
	public Concept findConceptByResourceId(String resourceId) {
		return conceptDao.findById(resourceId);
	}

	@Override
	public Concept update(final Concept concept) {
		conceptDao.save(concept);
		return concept;
	}

	@Override
	public List<Concept> findSpecializedConcepts(final Concept concept) {
		return conceptDao.findSpecializedConcepts(concept);
	}

	@Override
	public List<Concept> findAllSpecializedConcepts(final Concept concept) {
		return conceptDao.findAllSpecializedConcepts(concept);
	}

	@Override
	public Characteristic findCharacteristicByResourceId(String resourceId) {
		return characteristicDao.findById(resourceId);
	}

	@Override
	public BinaryFactType findBinaryFactTypeByResourceId(String resourceId) {
		return binaryFactTypeDao.findById(resourceId);
	}

	@Override
	public Category findCategoryByResourceId(String resourceId) {
		// return DaoFactory.getCategoryDao(getCurrentSession()).findLatestByResourceId(resourceId);
		throw new NotImplementedException("Implement this method");
	}

	@Override
	public void removeGeneralConcept(Concept specializedConcept) {

		(specializedConcept).setGeneralConcept(objectTypeDao.getMetaThing());
		update(specializedConcept);
	}

	@Override
	public Concept getConceptWithError(String resourceId) {

		Concept concept = findConceptByResourceId(resourceId);
		if (concept == null) {
			String message = "Concept with resource id '" + resourceId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.CONCEPT_NOT_FOUND, resourceId);
		}

		return concept;
	}

	@Override
	public ObjectType getObjectTypeWithError(String resourceId) {

		ObjectType ot = findObjectTypeByResourceId(resourceId);
		if (ot == null) {
			String message = "ObjectType with resource id '" + resourceId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.OBJECT_TYPE_NOT_FOUND, resourceId);
		}

		return ot;
	}

}