package com.collibra.dgc.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
import com.collibra.dgc.core.exceptions.CircularTaxonomyException;
import com.collibra.dgc.core.exceptions.ConstraintViolationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.DGCException;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.relation.Relation;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.representation.facttypeform.impl.BinaryFactTypeFormImpl;
import com.collibra.dgc.core.model.representation.facttypeform.impl.CharacteristicFormImpl;
import com.collibra.dgc.core.model.representation.impl.RepresentationImpl;
import com.collibra.dgc.core.model.representation.impl.TermImpl;
import com.collibra.dgc.core.model.representation.impl.VocabularyImpl;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.events.BinaryFactTypeFormEventData;
import com.collibra.dgc.core.observer.events.CharacteristicFormEventData;
import com.collibra.dgc.core.observer.events.EventType;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.observer.events.TermEventData;
import com.collibra.dgc.core.observer.events.VocabularyEventData;
import com.collibra.dgc.core.security.authorization.AuthorizationHelper;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.AttributeService;
import com.collibra.dgc.core.service.MeaningService;
import com.collibra.dgc.core.service.RelationService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.RulesService;
import com.collibra.dgc.core.util.Defense;

@Service
public class RepresentationServiceImpl extends AbstractService implements RepresentationService {
	private final Logger log = LoggerFactory.getLogger(RepresentationServiceImpl.class);

	@Autowired
	private RulesService ruleService;
	@Autowired
	private MeaningService meaningService;
	@Autowired
	private RelationService relationService;
	@Autowired
	private RepresentationFactory representationFactory;
	@Autowired
	private MeaningFactory meaningFactory;
	@Autowired
	private RepresentationServiceHelper representationServiceHelper;
	@Autowired
	private AuthorizationHelper authorizationHelper;
	@Autowired
	private CategorizationServiceHelperImpl categorizationHelper;
	@Autowired
	private VocabularyDao vocabularyDao;
	@Autowired
	private ObjectTypeDao objectTypeDao;
	@Autowired
	private TermDao termDao;
	@Autowired
	private BinaryFactTypeFormDao binaryFactTypeFormDao;
	@Autowired
	private CharacteristicFormDao characteristicFormDao;
	@Autowired
	private RepresentationDao representationDao;
	@Autowired
	private AttributeDao attributeDao;
	@Autowired
	private ConceptDao conceptDao;
	@Autowired
	private ServiceUtility serviceUtility;
	@Autowired
	private ConstraintChecker constraintChecker;
	@Autowired
	private AttributeService attributeService;

	@Override
	public Vocabulary saveVocabulary(Vocabulary vocabulary) {
		incorporateSbvrVocabulary(vocabulary);

		if (vocabulary.getType() == null)
			((VocabularyImpl) vocabulary).setType(objectTypeDao.getMetaGlossaryType());

		final boolean isNew = !vocabulary.isPersisted();
		if (isNew) {
			// Perform authorization check for creation.
			authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary.getCommunity(),
					Permissions.COMMUNITY_ADD_VOCABULARY, DGCErrorCodes.RESOURCE_NO_PERMISSION);
		}

		// Send adding event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.VOCABULARY,
				new VocabularyEventData(vocabulary, isNew ? EventType.ADDING : EventType.CHANGING));

		vocabulary = representationServiceHelper.saveVocabulary(vocabulary);

		// Send added event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.VOCABULARY,
				new VocabularyEventData(vocabulary, isNew ? EventType.ADDED : EventType.CHANGED));
		return vocabulary;
	}

	@Override
	public Representation save(Representation rep) {
		rep = ServiceUtility.deproxy(rep, Representation.class);
		if (rep instanceof Term || rep instanceof TermImpl) {
			return saveTerm((Term) rep);
		} else if (rep instanceof CharacteristicForm || rep instanceof CharacteristicFormImpl) {
			return saveCharacteristicForm((CharacteristicForm) rep);
		} else if (rep instanceof BinaryFactTypeForm || rep instanceof BinaryFactTypeFormImpl) {
			return saveBinaryFactTypeForm((BinaryFactTypeForm) rep);
		}

		throw new IllegalArgumentException(DGCErrorCodes.REPRESENTATION_UKNOWN, rep.verbalise(), rep.getId());
	}

	private void incorporateSbvrVocabulary(Vocabulary vocabulary) {
		Vocabulary sbvrVocabulary = findSbvrVocabulary();
		// SBVR vocabulary exists and it is not not incorporating itself
		if (sbvrVocabulary == null) {
			return;
		}
		String vocabularyUri = vocabulary.getUri();
		if ((vocabularyUri.equals(Constants.SBVR_VOC) || vocabularyUri.equals(Constants.MEANING_AND_REPRESENTATION_VOC)
				|| vocabularyUri.equals(Constants.BUSINESS_RULES_VOC) || vocabularyUri.equals(Constants.BUSINESS_VOC) || vocabularyUri
					.equals(Constants.SBVR_EXTENSIONS_VOC))) {
			return;
		}
		vocabulary.addIncorporatedVocabulary(sbvrVocabulary);
	}

	@Override
	public List<Vocabulary> findVocabularies() {
		return vocabularyDao.findAll();
	}

	@Override
	public Vocabulary findVocabularyByUri(String uri) {
		return vocabularyDao.findByVocabularyUri(uri);
	}

	@Override
	public Vocabulary findVocabularyByName(String name) {
		return vocabularyDao.findByVocabularyName(name);
	}

	@Override
	public List<Vocabulary> findVocabulariesByMember(String username, Role role, int start, int end) {

		return vocabularyDao.findVocabulariesByMember(username, role, start, end);
	}

	@Override
	public List<Vocabulary> findVocabulariesByMember(String username, int start, int end) {

		// TODO: check this
		// Defense.notNull(username, DGCErrorCodes.USERNAME_NULL);

		return vocabularyDao.findVocabulariesByMember(username, start, end);
	}

	@Override
	public List<Vocabulary> findVocabulariesToInCorporate(Vocabulary currentVocabulary) {

		List<Vocabulary> allVocabularies = findVocabularies();

		for (Vocabulary voc : new LinkedList<Vocabulary>(allVocabularies)) {
			// remove all the list of incorporated vocabularies by the current vocabulary
			allVocabularies.removeAll(currentVocabulary.getIncorporatedVocabularies());

			// remove the current vocabulary
			allVocabularies.remove(currentVocabulary);

			// remove the meta vocabulary and its incorporated vocabularies.
			if (voc.getCommunity().getUri().equals(Constants.ADMIN_COMMUNITY_URI)
					|| voc.getCommunity().getUri().equals(Constants.SBVR_ENGLISH_COMMUNITY_URI)) {
				allVocabularies.remove(voc);
			}

			// remove the vocabularies which incorporated the current vocabulary and the other vocabularies to prevent
			// cyclic dependencies.
			if (voc.getIncorporatedVocabularies().contains(currentVocabulary)) {
				allVocabularies = removeNotRequiredVocabulariesToIncorporate(voc, allVocabularies);
				allVocabularies.remove(voc);
			}
		}
		return allVocabularies;
	}

	@Override
	public Collection<Vocabulary> findVocabulariesToMove(Representation represenation) {
		List<Vocabulary> allVocabularies = findVocabularies();

		allVocabularies.remove(represenation.getVocabulary());

		for (Vocabulary voc : new LinkedList<Vocabulary>(allVocabularies)) {
			// remove the sbvr vocabulary and its incorporated vocabularies.
			if (voc.getCommunity().getTopLevelCommunity().getUri().equals(Constants.METAMODEL_COMMUNITY_URI)) {
				allVocabularies.removeAll(voc.getIncorporatedVocabularies());
				allVocabularies.remove(voc);
			}
		}
		return allVocabularies;

	}

	private List<Vocabulary> removeNotRequiredVocabulariesToIncorporate(Vocabulary toRemoveVocabulary,
			List<Vocabulary> allVocabularies) {
		for (Vocabulary voc : new LinkedList<Vocabulary>(allVocabularies)) {
			if (voc.getIncorporatedVocabularies().contains(toRemoveVocabulary)) {
				removeNotRequiredVocabulariesToIncorporate(voc, allVocabularies);
			}

		}
		allVocabularies.remove(toRemoveVocabulary);
		return allVocabularies;
	}

	@Override
	public Vocabulary findSbvrMeaningAndRepresentationVocabulary() {
		return vocabularyDao.findSbvrMeaningAndRepresentationVocabulary();
	}

	@Override
	public Vocabulary findSbvrLogicalFormulationsVocabulary() {
		return vocabularyDao.findSbvrLogicalFormulationsVocabulary();
	}

	// representation

	@Override
	public Term findTermBySignifier(final Vocabulary vocabulary, final String signifier) {
		return termDao.findTermBySignifier(vocabulary, signifier);
	}

	@Override
	public List<Term> findAllTermsBySignifier(final String signifier, boolean capitalSensitive) {

		return termDao.findAllTermsBySignifier(signifier, capitalSensitive);
	}

	@Override
	public List<Term> findStatusTerms(Representation rep) {
		// TODO update the method
		return representationDao.findStatusTerms();
		// TODO do filtering on vocabulary.
	}

	@Override
	public List<Term> findStatusTerms() {
		return representationDao.findStatusTerms();
	}

	@Override
	public List<String> findMetaVocabularyIDs() {
		return vocabularyDao.findMetaVocabularyIDs();
	}

	@Override
	public List<Vocabulary> findVocabularies(boolean excludeMeta) {
		return vocabularyDao.findVocabularies(excludeMeta);
	}

	@Override
	public List<Term> searchTermsBySignifier(final String signifier, int offset, int number) {
		return termDao.searchTermsBySignifier(signifier, offset, number);
	}

	@Override
	public List<Term> searchTermsBySignifier(Vocabulary voc, final String partialSignifier) {
		return termDao.searchTermsBySignifier(voc, partialSignifier);
	}

	@Override
	public List<Term> findTermsByType(ObjectType type) {
		return termDao.findTermsByType(type);
	}

	public List<Term> searchTermsBySignifier(Vocabulary voc, final String partialSignifier,
			boolean includeIncorporatedVocs, ObjectType type) {

		if (!includeIncorporatedVocs)
			return termDao.searchTermsBySignifier(voc, partialSignifier, type);
		else
			return termDao.searchTermInAllNonSBVRIncorporatedVocabularies(voc, partialSignifier, type);
	}

	@Override
	public List<Term> searchTermsBySignifier(Vocabulary voc, final String partialSignifier,
			boolean includeIncorporatedVocs) {

		if (!includeIncorporatedVocs)
			return termDao.searchTermsBySignifier(voc, partialSignifier);
		else
			return termDao.searchTermInAllNonSBVRIncorporatedVocabularies(voc, partialSignifier);
	}

	@Override
	public List<Attribute> searchAttributesByLongExpression(final String partialExpression, int offset, int number) {
		return attributeDao.searchAttributesForLongExpression(partialExpression, offset, number);
	}

	@Override
	public List<Vocabulary> searchVocabulariesForName(String name, int offset, int number) {

		return vocabularyDao.searchVocabulariesForName(name, offset, number);
	}

	@Override
	public BinaryFactTypeForm findBinaryFactTypeFormByRepresentation(final Vocabulary vocabulary, final Term headTerm,
			final String role, final String coRole, final Term tailTerm) {

		return binaryFactTypeFormDao.findByRepresentation(vocabulary, headTerm, role, coRole, tailTerm);
	}

	@Override
	public CharacteristicForm findCharacteristicFormByRepresentation(final Vocabulary vocabulary, final Term term,
			final String role) {

		return characteristicFormDao.findByRepresentation(vocabulary, term, role);
	}

	@Override
	public List<CharacteristicForm> findCharacteristicFormsContainingTerm(final Term term) {
		return characteristicFormDao.find(term);
	}

	@Override
	public Term findTermByResourceId(String resourceId) {
		// TODO: check this
		// Defense.notNull(resourceId, "resourceId");
		return termDao.findById(resourceId);
	}

	@Override
	public Term findPreferredTerm(final ObjectType objectType, final Vocabulary vocabulary) {

		if (vocabulary == null)
			return null;

		Term preferredTerm = termDao.findPreferredTerm(objectType, vocabulary);

		if (preferredTerm == null) {
			Set<Vocabulary> incorporatedVocabularies = vocabulary.getAllIncorporatedVocabularies();
			for (Vocabulary voc : incorporatedVocabularies) {
				if (voc != null) {
					preferredTerm = termDao.findPreferredTerm(objectType, voc);
					if (preferredTerm != null) {
						return preferredTerm;
					}
				}
			}
		}

		return preferredTerm;
	}

	@Override
	public CharacteristicForm findPreferredCharacteristicForm(final Characteristic characteristic,
			final Vocabulary vocabulary) {
		return characteristicFormDao.findPreferred(characteristic, vocabulary);
	}

	@Override
	public BinaryFactTypeForm findPreferredBinaryFactTypeForm(final BinaryFactType binaryFactType,
			final Vocabulary vocabulary) {
		return binaryFactTypeFormDao.findPreferred(binaryFactType, vocabulary);
	}

	@Override
	public BinaryFactTypeForm findBinaryFactTypeFormByResourceId(final String id) {
		return binaryFactTypeFormDao.findById(id);
	}

	@Override
	public CharacteristicForm findCharacteristicFormByResourceId(final String id) {
		// TODO: check this
		// Defense.notNull(id, "id");
		return characteristicFormDao.findById(id);
	}

	@Override
	public Vocabulary findVocabularyByResourceId(final String id) {
		return vocabularyDao.findById(id);
	}

	public List<Attribute> findAttributesByRepresentation(final Term attributeLabel, final Representation representation) {

		return attributeDao.findAttributesByTypeAndOwner(attributeLabel, representation);
	}

	@Override
	public List<BinaryFactTypeForm> findBinaryFactTypeFormsContainingHeadTerm(final Term term) {
		return binaryFactTypeFormDao.findWithHeadTerm(term);
	}

	@Override
	public List<BinaryFactTypeForm> findBinaryFactTypeFormsContainingTailTerm(final Term term) {
		return binaryFactTypeFormDao.findWithTailTerm(term);
	}

	@Override
	public List<Representation> findSpecializedConceptRepresentations(final Representation representation, int limit) {
		List<Representation> results = representationDao.findSpecializedRepresentations(representation, limit);
		Collections.sort(results);
		return results;
	}

	@Override
	public List<Representation> findSpecializedConceptRepresentationsInSameVocabulary(
			final Representation representation) {

		return representationDao.findSpecializedConceptRepresentationsInSameVocabulary(representation);
	}

	@Override
	public Representation findGeneralConceptRepresentation(Representation representation) {
		Defense.assertTrue(representation.getMeaning() instanceof Concept, DGCErrorCodes.MEANING_NOT_A_CONCEPT,
				representation.verbalise(), representation.getId());
		Concept generalConcept = ((Concept) representation.getMeaning()).getGeneralConcept();
		Representation preferred = findPreferredRepresentationInAllIncorporatedVocabularies(generalConcept,
				representation.getVocabulary());

		return preferred;
	}

	@Override
	public List<Representation> findAllGeneralConceptRepresentation(Representation representation) {
		List<Representation> result = new LinkedList<Representation>();
		Representation repr = findGeneralConceptRepresentation(representation);
		while (repr != null && !repr.getMeaning().equals(meaningService.findMetaThing())) {
			result.add(repr);
			repr = findGeneralConceptRepresentation(repr);
		}
		return result;
	}

	@Override
	public Representation findPreferredRepresentation(final Concept concept, final Vocabulary vocabulary) {
		if (concept == null) {
			return null;
		}

		Representation preferredRepresentation = representationDao.findPreferredRepresentation(concept, vocabulary);
		return preferredRepresentation;
	}

	@Override
	public Representation findPreferredRepresentationInAllIncorporatedVocabularies(final Concept concept,
			final Vocabulary vocabulary) {
		if (concept == null) {
			return null;
		}

		Representation preferredRepresentation = representationDao.findPreferredRepresentation(concept, vocabulary);
		if (preferredRepresentation != null) {
			return preferredRepresentation;
		}

		Set<Vocabulary> incorporatedVocabularies = vocabulary.getAllIncorporatedVocabularies();
		if (incorporatedVocabularies == null) {
			return null;
		}

		for (Vocabulary voc : incorporatedVocabularies) {
			preferredRepresentation = findPreferredRepresentation(concept, voc);
			if (preferredRepresentation != null) {
				return preferredRepresentation;
			}
		}
		return null;
	}

	public List<Term> findTerms() {
		return termDao.findAll();
	}

	@Override
	public List<Term> findAllTermsForObjectType(ObjectType oType) {
		return termDao.findTermsByObjectType(oType);
	}

	@Override
	public List<BinaryFactTypeForm> findAllBinaryFactTypeForms() {
		return binaryFactTypeFormDao.findAll();
	}

	@Override
	public List<CharacteristicForm> findAllCharacteristicForms() {
		return characteristicFormDao.findAll();
	}

	@Override
	public Vocabulary findSbvrVocabulary() {
		return vocabularyDao.findSbvrVocabulary();
	}

	@Override
	public Vocabulary findSbvrBusinessVocabulary() {
		return vocabularyDao.findSbvrBusinessVocabulary();
	}

	@Override
	public Vocabulary findSbvrBusinessRulesVocabulary() {
		return vocabularyDao.findSbvrBusinessRulesVocabulary();
	}

	@Override
	public Vocabulary findSbvrCollibraExtensionsVocabulary() {
		return vocabularyDao.findSbvrCollibraExtensionsVocabulary();
	}

	@Override
	public Vocabulary findAttributeTypesVocabulary() {
		return vocabularyDao.findAttributeTypesVocabulary();
	}

	@Override
	public Vocabulary findStatusesVocabulary() {
		return vocabularyDao.findStatusesVocabulary();
	}

	@Override
	public Vocabulary findMetamodelExtensionsVocabulary() {
		return vocabularyDao.findMetamodelExtensionsVocabulary();
	}

	@Override
	public Vocabulary findRolesAndResponsibilitiesVocabulary() {
		return vocabularyDao.findRolesAndResponsibilitiesVocabulary();
	}

	@Override
	public List<Term> findPureTermsByVocabulary(Vocabulary vocabulary) {
		return termDao.findPureTermsByVocabulary(vocabulary);
	}

	@Override
	public List<Representation> findSynonyms(Representation representation) {
		return representationDao.findSynonyms(representation);
	}

	@Override
	public List<Term> findSynonyms(final Term term) {
		return termDao.findSynonyms(term);
	}

	@Override
	public List<CharacteristicForm> findSynonymousForms(CharacteristicForm cForm) {
		return characteristicFormDao.findSynonymousForms(cForm);
	}

	@Override
	public List<BinaryFactTypeForm> findSynonymousForms(BinaryFactTypeForm bftf) {
		return binaryFactTypeFormDao.findSynonymousForms(bftf);

	}

	@Override
	public List<Vocabulary> findAllIncorporatingVocabularies(final Vocabulary vocabulary) {
		return vocabularyDao.findAllIncorporatingVocabularies(vocabulary);
	}

	@Override
	public Term findPreferredTermInAllIncorporatedVocabularies(ObjectType objectType, Vocabulary vocabulary) {

		Term preferredTerm = termDao.findPreferredTerm(objectType, vocabulary);
		if (preferredTerm != null) {
			return preferredTerm;
		}

		Set<Vocabulary> incorporatedVocabularies = vocabulary.getAllIncorporatedVocabularies();
		if (incorporatedVocabularies == null) {
			return null;
		}

		for (Vocabulary voc : incorporatedVocabularies) {
			preferredTerm = findPreferredTerm(objectType, voc);
			if (preferredTerm != null) {
				return preferredTerm;
			}
		}
		return null;
	}

	@Override
	public Term findPreferredTerm(ObjectType objectType) {
		// If there are no terms referring to this meaning..
		if (objectType.getTerms().size() == 0) {
			return null;
		}

		Set<Term> terms = new TreeSet<Term>(new Comparator<Term>() {
			@Override
			public int compare(Term o1, Term o2) {
				// Sort on the ascending order of term versioned ids. The lowest versioned id term has highest chances
				// of consistent result.
				return (int) (o1.getCreatedOn() - o2.getCreatedOn());
			}
		});

		terms.addAll(objectType.getTerms());

		return terms.iterator().next();
	}

	@Override
	public Representation findRepresentationByResourceId(String resourceId) {
		return representationDao.findById(resourceId);
	}

	@Override
	public List<Term> findAllPreferredTerms(Vocabulary vocabulary) {
		// TODO: Optimize this later with query. Currently the previous implementation is not working.
		List<Term> preferredTerms = new LinkedList<Term>();
		for (Term term : vocabulary.getTerms()) {
			Term preferred = findPreferredTerm(term.getObjectType(), vocabulary);
			if (!preferredTerms.contains(preferred)) {
				preferredTerms.add(preferred);
			}
		}

		return preferredTerms;
	}

	@Override
	public List<Term> findAllPreferredTermsInAllIncorporatedVocabularies(Vocabulary vocabulary) {
		Set<ObjectType> allObjectTypes = new HashSet<ObjectType>();
		allObjectTypes.addAll(meaningService.findAllObjectTypesRepresentedInVocabulary(vocabulary));
		for (Vocabulary incorporatedVocabulary : vocabulary.getAllIncorporatedVocabularies()) {
			allObjectTypes.addAll(meaningService.findAllObjectTypesRepresentedInVocabulary(incorporatedVocabulary));
		}
		List<Term> preferredTerms = new LinkedList<Term>();
		for (ObjectType objectType : allObjectTypes) {
			preferredTerms.add(findPreferredTerm(objectType, vocabulary));
		}
		return preferredTerms;
	}

	@Override
	public List<Representation> findRepresentationsReferringTerm(Term term) {
		List<Representation> representations = new LinkedList<Representation>();
		representations.addAll(findBinaryFactTypeFormsReferringTerm(term));
		representations.addAll(findCharacteristicFormsReferringTerm(term));
		return representations;
	}

	@Override
	public List<BinaryFactTypeForm> findBinaryFactTypeFormsReferringTerm(Term term) {
		if (term == null) {
			return new LinkedList<BinaryFactTypeForm>();
		}

		return binaryFactTypeFormDao.find(term);
	}

	@Override
	public List<BinaryFactTypeForm> findAllBinaryFactTypeFormsReferringTerm(Term term) {
		if (term == null) {
			return new LinkedList<BinaryFactTypeForm>();
		}
		return binaryFactTypeFormDao.find(term);
	}

	@Override
	public List<CharacteristicForm> findCharacteristicFormsReferringTerm(Term term) {
		if (term == null) {
			return new LinkedList<CharacteristicForm>();
		}
		return characteristicFormDao.find(term);
	}

	@Override
	public List<BinaryFactTypeForm> findBinaryFactTypeForms(Term headTerm, String role, String corole, Term tailTerm) {
		if (headTerm == null || tailTerm == null) {
			return new LinkedList<BinaryFactTypeForm>();
		}

		return binaryFactTypeFormDao.find(headTerm, role, corole, tailTerm);
	}

	@Override
	// TODO remove
	public BinaryFactTypeForm findBinaryFactTypeForm(Vocabulary vocabulary, Term headTerm, String role, String corole,
			Term tailTerm) {
		if (headTerm == null || tailTerm == null) {
			return null;
		}

		return binaryFactTypeFormDao.find(vocabulary, headTerm, role, corole, tailTerm);
	}

	@Override
	public CharacteristicForm findCharacteristicForm(Vocabulary vocabulary, Term term, String roleExpression) {
		if (term == null) {
			return null;
		}

		return characteristicFormDao.find(vocabulary, term, roleExpression);
	}

	@Override
	public List<List<Attribute>> findAttributesForRepresentation(Representation representation, ObjectType... types) {
		Map<ObjectType, List<Attribute>> typeToAttributes = attributeDao.findAttributesByOwnerInMap(representation);
		List<List<Attribute>> result = new ArrayList<List<Attribute>>();
		for (ObjectType ot : types) {
			result.add(typeToAttributes.get(ot));
		}
		return result;

		// AMAR/DAMIEN : TODO : Remove this once performance issue is resolved using query.
		// return attributeDao.findAttributesByTypesAndOwner(
		// representation, types);
	}

	/**
	 * If there is no preferred representation then choose one as preferred among the synonyms.
	 * @param representations
	 */
	private void choosePreferred(List<Representation> representations) {
		for (Representation rep : representations) {
			if (rep.getIsPreferred()) {
				return;
			}
		}

		// If no preferred representation exists then choose then first one as the preferred one.
		for (Representation rep : representations) {
			if (rep instanceof Term) {
				Term term = (Term) rep;
				((RepresentationImpl) term).setIsPreferred(true);
				saveTerm(term);
				break;
			} else if (rep instanceof BinaryFactTypeForm) {
				BinaryFactTypeForm bftf = (BinaryFactTypeForm) rep;
				((RepresentationImpl) bftf).setIsPreferred(true);
				saveBinaryFactTypeForm(bftf);
				break;
			} else if (rep instanceof CharacteristicForm) {
				CharacteristicForm cf = (CharacteristicForm) rep;
				((RepresentationImpl) cf).setIsPreferred(true);
				saveCharacteristicForm(cf);
				break;
			}

		}
	}

	protected BinaryFactsDerivator createBinaryFactsDerivator() {
		return new BinaryFactsDerivator(this, representationFactory, meaningService, binaryFactTypeFormDao, conceptDao,
				serviceUtility);
	}

	@Override
	public Collection<BinaryFactTypeForm> findDerivedFacts(Term term) {
		return createBinaryFactsDerivator().findDerivedFacts(term);
	}

	@Override
	public BinaryFactTypeForm findBinaryFactTypeFormByExpression(Vocabulary vocabulary, String headTermSignifier,
			String roleExpression, String coRoleExpression, String tailTermSignifier) {
		return binaryFactTypeFormDao.findByExpression(vocabulary, headTermSignifier, roleExpression, coRoleExpression,
				tailTermSignifier);
	}

	@Override
	public CharacteristicForm findCharacteristicFormByExpression(Vocabulary vocabulary, String termSignifier,
			String role) {
		return characteristicFormDao.findByExpression(vocabulary, termSignifier, role);
	}

	@Override
	public Representation setGeneralConceptRepresentation(Representation specializedRepresentation,
			Representation generalRepresentation) {

		// Check if the meaning is category or categorization type. Block the changing of it using this API.
		Meaning meaning = ServiceUtility.deproxy(specializedRepresentation.getMeaning(), Meaning.class);
		if (meaning instanceof Category || meaning instanceof CategorizationType) {
			String message = "Invalid operation on representation '" + specializedRepresentation.verbalise()
					+ "' which is a Category/CategorizationType.";
			log.error(message);
			throw new ConstraintViolationException(message, specializedRepresentation.verbalise(),
					Representation.class.getName(), DGCErrorCodes.CATEGORIZATION_INVALID_OPERATION);
		}

		// Cannot proceed if the resource is locked.
		if (specializedRepresentation.isLocked()) {
			String message = "Representation '" + specializedRepresentation.verbalise() + "' is locked";
			log.error(message);
			throw new DGCException(DGCErrorCodes.REPRESENTATION_LOCKED, specializedRepresentation.verbalise(),
					specializedRepresentation.getId());
		}

		// caution: can be called for representations that are not representations for concepts
		Meaning specializedMeaning = specializedRepresentation.getMeaning();
		Meaning generalMeaning = generalRepresentation.getMeaning();

		// Avoid recursive taxonomy. Currently circular taxonomy detection is hard and not implemented.
		if (generalMeaning.equals(specializedMeaning)) {
			String message = "A concept cannot be parent of itself, '" + specializedRepresentation.verbalise()
					+ "' and '" + generalRepresentation.verbalise() + "' are synonyms";
			log.error(message);
			throw new CircularTaxonomyException(message, specializedMeaning.getId(), specializedRepresentation
					.getClass().getName());
		}

		if (!(specializedMeaning instanceof Concept) || !(generalMeaning instanceof Concept)) {
			String message = "Meaning of representation is not a Concept";
			log.error(message);
			if (!(specializedMeaning instanceof Concept))
				throw new IllegalArgumentException(DGCErrorCodes.MEANING_NOT_A_CONCEPT,
						specializedRepresentation.verbalise(), specializedRepresentation.getId());
			else
				throw new IllegalArgumentException(DGCErrorCodes.MEANING_NOT_A_CONCEPT,
						generalRepresentation.verbalise(), generalRepresentation.getId());
		}

		List<Concept> specializedConceptsForSpecializedMeaning = meaningService
				.findAllSpecializedConcepts(((Concept) specializedMeaning));
		List<Concept> specializedConceptsForGeneralMeaning = meaningService
				.findAllSpecializedConcepts(((Concept) generalMeaning));

		if (specializedConceptsForSpecializedMeaning.contains(generalMeaning)
				|| specializedConceptsForGeneralMeaning.contains(specializedMeaning)) {
			throw new IllegalArgumentException(DGCErrorCodes.CONCEPT_ALREADY_IN_TAXONOMY,
					specializedRepresentation.verbalise());
		}

		((Concept) specializedMeaning).setGeneralConcept((Concept) generalMeaning);
		meaningService.update((Concept) specializedMeaning);

		return specializedRepresentation;
	}

	@Override
	public Map<String, Term> getSignifierToTermMap(Vocabulary vocabulary) {
		return termDao.getSignifierToTermMap(vocabulary);
	}

	@Override
	public Map<String, Term> getSignifierToTermMap(Collection<Vocabulary> vocabularies) {
		return termDao.getSignifierToTermMap(vocabularies);
	}

	// @Override
	// public Term moveTerm(Term term, Vocabulary destVocabulary) {
	//
	// // TODO Check the right permission and constraints
	//
	// // Check authorization to remove source term.
	// authorizationHelper.checkAuthorization(getCurrentUser(), term, Permissions.TERM_REMOVE,
	// DGCErrorCodes.RESOURCE_NO_PERMISSION);
	//
	// // Check authorization to add term to destination vocabulary.
	// authorizationHelper.checkAuthorization(getCurrentUser(), destVocabulary, Permissions.VOCABULARY_ADD_TERM,
	// DGCErrorCodes.RESOURCE_NO_PERMISSION);
	//
	// // If the destination vocabulary is same as the term vocabulary then do not perform move.
	// if (destVocabulary.equals(term.getVocabulary())) {
	// return term;
	// }
	//
	// // TODO this needs to trigger events to notify (for example the indexer) about this move.
	//
	// Term existingTerm = findTermBySignifier(destVocabulary, term.getSignifier());
	// if (existingTerm != null) {
	// String message = "Vocabulary '" + destVocabulary + "' already contains the term with signifier '"
	// + term.getSignifier() + "', hence move not possible.";
	// log.error(message);
	// throw new ConstraintViolationException(message, destVocabulary.getId(),
	// destVocabulary.getClass().getName(),
	// DGCErrorCodes.REPRESENTATION_MOVE_TERM_ALREADY_EXISTS_IN_DESTINATION_VOCABULARY);
	// }
	//
	// Term newTerm = representationFactory.copyTerm(term, destVocabulary);
	// save(newTerm);
	// remove(term);
	//
	// return newTerm;
	// }
	//
	// @Override
	// public Collection<Term> moveTerms(Collection<Term> terms, Vocabulary destVocabulary) {
	// final List<Term> doneTerms = new ArrayList<Term>();
	// for (final Term term : terms) {
	// // Skip if this term is already in the destination vocabulary.
	// if (!destVocabulary.equals(term.getVocabulary())) {
	// doneTerms.add(moveTerm(term, destVocabulary));
	// }
	// }
	//
	// return doneTerms;
	// }

	@Override
	public List<Term> findAllTerms() {
		return termDao.findAll();
	}

	@Override
	public Collection<Representation> findAllRelatedRepresentations(Term term) {
		Collection<Representation> relatedRepresentations = new HashSet<Representation>();
		relatedRepresentations.addAll(findBinaryFactTypeFormsReferringTerm(term));
		relatedRepresentations.addAll(findCharacteristicFormsReferringTerm(term));
		relatedRepresentations.addAll(findSynonyms(term));
		relatedRepresentations.addAll(representationDao.findSpecializedConceptRepresentations(term));

		return relatedRepresentations;
	}

	@Override
	public Term findConceptTypeRepresentation(Representation representation) {
		// caution: can be called for representations that are not representations for concepts
		Meaning meaning = representation.getMeaning();
		if (!(meaning instanceof Concept)) {
			throw new IllegalArgumentException(DGCErrorCodes.MEANING_NOT_A_CONCEPT, representation.verbalise(),
					representation.getId());
		}

		ObjectType type = ((Concept) meaning).getType();
		Term conceptTypeRepresentation = findPreferredTermInAllIncorporatedVocabularies(type,
				representation.getVocabulary());

		if (conceptTypeRepresentation == null) {
			conceptTypeRepresentation = findPreferredTerm(type);
		}

		return conceptTypeRepresentation;
	}

	@Override
	public Vocabulary disincorporateVocabulary(Vocabulary currentVoc, Vocabulary incorporatedVoc) {

		// Authorization Check
		authorizationHelper.checkAuthorization(getCurrentUser(), currentVoc, Permissions.VOCABULARY_INCORPORATE,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// Check cosntraints
		constraintChecker.checkDisincorporateVocabularyConstraints(currentVoc, incorporatedVoc);

		// Send changing event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.VOCABULARY,
				new VocabularyEventData(currentVoc, EventType.CHANGING));

		currentVoc.removeIncorporatedVocabulary(incorporatedVoc);
		vocabularyDao.save(currentVoc);

		// Send changing event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.VOCABULARY,
				new VocabularyEventData(currentVoc, EventType.CHANGED));

		return currentVoc;
	}

	@Override
	public Representation setConceptTypeRepresentation(Representation currentRepresentation, Term selectedTerm) {

		// Check if the meaning is category or categorization type. Block the changing of it using this API.
		Meaning meaning = ServiceUtility.deproxy(currentRepresentation.getMeaning(), Meaning.class);
		if (meaning instanceof Category || meaning instanceof CategorizationType) {
			String message = "Invalid operation on representation '" + currentRepresentation.verbalise()
					+ "' which is a Category/CategorizationType.";
			log.error(message);
			throw new ConstraintViolationException(message, currentRepresentation.verbalise(),
					Representation.class.getName(), DGCErrorCodes.CATEGORIZATION_INVALID_OPERATION);
		}

		// Cannot proceed if the resource is locked.
		if (currentRepresentation.isLocked()) {
			String message = "Representation '" + currentRepresentation.verbalise() + "' is locked";
			log.error(message);
			throw new DGCException(DGCErrorCodes.REPRESENTATION_LOCKED, currentRepresentation.verbalise(),
					currentRepresentation.getId());
		}

		// Check authorization
		authorizationHelper.checkAuthorization(getCurrentUser(), currentRepresentation,
				Permissions.REPRESENTATION_CONCEPT_TYPE_UPDATE, DGCErrorCodes.RESOURCE_NO_PERMISSION);

		Meaning currentMeaning = currentRepresentation.getMeaning();

		Meaning newMeaning = selectedTerm.getMeaning();
		Concept currentConcept = ((Concept) currentMeaning);
		Concept selectedConcept = ((Concept) newMeaning);

		boolean isCompatible = isCompatibleTypes(currentConcept.getType(), selectedConcept);

		if (!isCompatible) {
			if (currentRepresentation instanceof Term) {
				if (selectedConcept.equals(meaningService.findMetaObjectType())) {
					isCompatible = true;
				} else if (selectedConcept.getId().equals(MeaningConstants.META_STATUS_TYPE_UUID)
						&& currentRepresentation.getVocabulary().getUri().equals(Constants.STATUSES_VOCABULARY_URI)) {
					isCompatible = true;
				} else if (selectedConcept.getId().equals(MeaningConstants.META_ATTRIBUTE_TYPE_UUID)
						&& currentRepresentation.getVocabulary().getUri()
								.equals(Constants.ATTRIBUTETYPES_VOCABULARY_URI)) {
					isCompatible = true;
				}
			} else if (currentRepresentation instanceof BinaryFactType) {
				if (selectedConcept.equals(meaningService.findMetaBinaryFactType())) {
					isCompatible = true;
				}
			} else if (currentRepresentation instanceof Characteristic) {
				if (selectedConcept.equals(meaningService.findMetaCharacteristic())) {
					isCompatible = true;
				}
			}
		}
		// check if the selected term belongs to the current vocabulary or its incorporated vocabularies excluding
		// the standard sbvr vocabularies.
		// FIXME this logic may cause inconsistencies in future. need to think of a better solution then.

		Vocabulary selectedVoc = selectedTerm.getVocabulary();
		Vocabulary currentVoc = currentRepresentation.getVocabulary();

		if (!isCompatible && currentRepresentation instanceof Term) {
			if ((selectedVoc.equals(currentVoc) || currentVoc.getAllNonSbvrIncorporatedVocabularies().contains(
					selectedVoc))
					&& !getAllSBVRVocabularies().contains(selectedVoc)) {
				isCompatible = true;
			}
		}

		if (!isCompatible) {
			// TODO add an error code
			throw new IllegalArgumentException(DGCErrorCodes.TERM_CONCEPTTYPE_INCOMPATIBLE,
					currentRepresentation.verbalise(), selectedTerm.verbalise());
		}

		if (!(currentMeaning instanceof Concept) || !(newMeaning instanceof Concept)) {
			throw new IllegalArgumentException(DGCErrorCodes.MEANING_NOT_A_CONCEPT, currentRepresentation.verbalise(),
					currentRepresentation.getId());
		}

		((Concept) currentMeaning).setType(selectedTerm.getObjectType());
		meaningService.update((Concept) currentMeaning);

		return currentRepresentation;
	}

	private List<Vocabulary> getAllSBVRVocabularies() {
		List<Vocabulary> vocabularies = new LinkedList<Vocabulary>();
		Vocabulary sbvrVocabulary = findVocabularyByUri(Constants.SBVR_VOC);
		vocabularies.add(sbvrVocabulary);
		vocabularies.addAll(sbvrVocabulary.getIncorporatedVocabularies());
		return vocabularies;

	}

	private boolean isCompatibleTypes(final Concept currentConcept, final Concept selectedConcept) {
		// if currentConcept == null for some reason, we can not asses whether it is compatible
		// so we return true. (fvdmaele)
		if (currentConcept == null || currentConcept.equals(selectedConcept)) {
			return true;
		} else {
			if (selectedConcept.equals(currentConcept.getGeneralConcept().getType())) {
				return true;
			}
			for (Concept generalizedConcept : meaningService.findTaxonomicalParentConcepts(currentConcept)) {
				boolean isCompatible = generalizedConcept.equals(selectedConcept);
				if (isCompatible) {
					return true;
				}
			}

			for (Concept specializedConcept : meaningService.findAllSpecializedConcepts(currentConcept)) {
				boolean isCompatible = specializedConcept.equals(selectedConcept);
				if (isCompatible) {
					return true;
				}
			}

			// add all the specialized concepts starting from "Noun Concept" to include integer, ...
			Concept nounConcept = meaningService.findMetaIndividualConcept().getGeneralConcept();
			for (Concept specializedConcept : meaningService.findAllSpecializedConcepts(nounConcept)) {
				boolean isCompatible = specializedConcept.equals(selectedConcept);
				if (isCompatible) {
					return true;
				}
			}

			// add all the specialized concepts starting from "Object Type" ...
			Concept objectType = meaningService.findMetaObjectType().getGeneralConcept();
			for (Concept specializedConcept : meaningService.findAllSpecializedConcepts(objectType)) {
				boolean isCompatible = specializedConcept.equals(selectedConcept);
				if (isCompatible) {
					return true;
				}
			}
		}
		return false;
	}

	private void checkForAttributeCreateAuthorization(Representation representation) {
		for (Attribute att : representation.getAttributes()) {
			if (!att.isPersisted()) {
				authorizationHelper.checkAuthorization(getCurrentUser(), att,
						authorizationHelper.OPERATION_ATTRIBUTE_ADD);
			}
		}
	}

	@Override
	public Term findTermBySignifierInAllIncorporatedVocabularies(Vocabulary vocabulary, String signifier) {
		Term result = findTermBySignifier(vocabulary, signifier);

		if (result == null) {
			Set<Vocabulary> incorporatedVocabularies = vocabulary.getAllIncorporatedVocabularies();
			for (Vocabulary voc : incorporatedVocabularies) {
				if (voc != null) {
					result = termDao.findTermBySignifier(voc, signifier);
					if (result != null) {
						return result;
					}
				}
			}
		}

		return result;
	}

	@Override
	public Term findTermBySignifierAndCreateIfNotExists(Vocabulary vocabulary, String signifier) {

		Term term = findTermBySignifierInAllIncorporatedVocabularies(vocabulary, signifier);

		if (term == null) {

			term = representationFactory.makeTerm(vocabulary, signifier);

			saveTerm(term);
		}

		return term;
	}

	@Override
	public Vocabulary getVocabularyWithError(String vocabularyResourceId) {

		Vocabulary vocabulary = findVocabularyByResourceId(vocabularyResourceId);
		if (vocabulary == null) {
			String message = "Vocabulary with resource id '" + vocabularyResourceId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.VOCABULARY_NOT_FOUND_ID, vocabularyResourceId);
		}
		return vocabulary;
	}

	@Override
	public Term getTermWithError(String resourceId) {

		Term term = findTermByResourceId(resourceId);
		if (term == null) {
			String message = "Term with resource id '" + resourceId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.TERM_NOT_FOUND_ID, resourceId);
		}
		return term;
	}

	@Override
	public BinaryFactTypeForm getBftfWithError(String resourceId) {

		BinaryFactTypeForm bftf = findBinaryFactTypeFormByResourceId(resourceId);

		if (bftf == null) {
			String message = "BinaryFactTypeForm with resource id '" + resourceId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.BFTF_NOT_FOUND, resourceId);
		}

		return bftf;
	}

	@Override
	public CharacteristicForm getCfWithError(String resourceId) {

		CharacteristicForm cf = findCharacteristicFormByResourceId(resourceId);

		if (cf == null) {
			String message = "CharacteristicForm with resource id '" + resourceId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.CF_NOT_FOUND, resourceId);
		}

		return cf;
	}

	@Override
	public Representation getRepresentationWithError(String resourceId) {

		Representation representation = findRepresentationByResourceId(resourceId);

		if (representation == null) {
			String message = "Representation with resource id '" + resourceId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.REPRESENTATION_NOT_FOUND, resourceId);
		}

		return representation;
	}

	@Override
	public Vocabulary incorporate(Vocabulary incorporating, Vocabulary incorporate) {

		// Authorization check
		authorizationHelper.checkAuthorization(getCurrentUser(), incorporating, Permissions.VOCABULARY_INCORPORATE,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// Constraints check
		constraintChecker.checkIncorporateVocabularyConstraints(incorporating, incorporate);

		// Send adding event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.VOCABULARY,
				new VocabularyEventData(incorporating, EventType.CHANGING));

		incorporating.addIncorporatedVocabulary(incorporate);
		vocabularyDao.save(incorporating);

		// Send added event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.VOCABULARY,
				new VocabularyEventData(incorporating, EventType.CHANGED));

		return incorporating;
	}

	/***************************
	 * All remove functionality
	 ***************************/

	@Override
	public void remove(Representation representation) {

		representation = ServiceUtility.deproxy(representation, Representation.class);
		if (representation instanceof Term) {
			Term term = (Term) representation;
			remove(term);
		} else if (representation instanceof BinaryFactTypeForm) {
			BinaryFactTypeForm bftf = (BinaryFactTypeForm) representation;
			remove(bftf);
		} else if (representation instanceof CharacteristicForm) {
			CharacteristicForm cf = (CharacteristicForm) representation;
			remove(cf);
		} else {
			throw new com.collibra.dgc.core.exceptions.IllegalArgumentException(DGCErrorCodes.REPRESENTATION_UKNOWN,
					representation.verbalise(), representation.getId());
		}
	}

	@Override
	public void remove(final Term term) {
		removeInternal(term, true);
	}

	@Override
	public void remove(final Collection<Term> terms) {
		for (final Term term : terms) {
			ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.TERM,
					new TermEventData(term, EventType.REMOVING));

			removeInternal(term, false);
		}

		getCurrentSession().flush();

		for (final Term term : terms) {
			ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.TERM,
					new TermEventData(term, EventType.REMOVED));

			removeInternal(term, false);
		}
	}

	/**
	 * Remove the term and all of its dependencies. This will also check the permissions.
	 * @param term The term to remove
	 * @param sendEvents If true, this will send REMOVING and REMOVED events and flush the session. If false, it won't.
	 */
	private void removeInternal(final Term term, final boolean sendEvents) {

		if (term.getVocabulary().getUri().equals(Constants.STATUSES_VOCABULARY_URI)
				&& term.getMeaning().getId().equals(MeaningConstants.META_CANDIDATE_STATUS_TYPE_UUID)) {
			throw new com.collibra.dgc.core.exceptions.IllegalArgumentException(
					DGCErrorCodes.CANNOT_REMOVE_CANDIDATE_STATUS);
		}

		if (termDao.findById(term.getId()) == null)
			return;

		// Perform authorization check.
		authorizationHelper.checkAuthorization(getCurrentUser(), term, Permissions.TERM_REMOVE,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// Remove dependencies.

		// remove all relations
		for (Relation rel : relationService.findBySource(term))
			relationService.remove(rel);

		for (Relation rel : relationService.findByTarget(term))
			relationService.remove(rel);

		// Remove all binary fact type forms referring to Term.
		List<BinaryFactTypeForm> bftfs = findBinaryFactTypeFormsReferringTerm(term);
		for (BinaryFactTypeForm bftf : bftfs) {
			removeInternal(bftf);
		}

		getCurrentSession().flush();

		// Remove all characteristic forms referring to Term.
		List<CharacteristicForm> cfs = findCharacteristicFormsReferringTerm(term);
		for (CharacteristicForm cf : cfs) {
			removeInternal(cf);
		}

		getCurrentSession().flush();

		// Remove all the attributes.
		removeAllAttributes(term);

		// Notify the removing event
		if (sendEvents) {
			ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.TERM,
					new TermEventData(term, EventType.REMOVING));
		}

		term.getObjectType().getTerms().remove(term);
		termDao.delete(term);

		final ObjectType ot = term.getObjectType();

		if (ot.getRepresentations().size() == 0) {
			removeCategorization(term);
			// Remove the specialized concept's taxonomy as this term is being removed.
			removeTaxonomy(term);
			getCurrentSession().flush();
			// Remove the meaning itself.
			meaningService.remove(ot);
		}

		getCurrentSession().flush();

		if (sendEvents) {
			// Notify the removed event
			ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.TERM,
					new TermEventData(term, EventType.REMOVED));
		}
	}

	private final void removeCategorization(final Representation rep) {
		if (rep.getMeaning() instanceof Concept) {
			List<Term> terms = categorizationHelper.findCategoriesTermsForConcept((Concept) rep.getMeaning());
			for (final Term term : terms) {
				removeInternal(term, true);
			}

			terms = categorizationHelper.findCategorizationTypeTermsForConcept((Concept) rep.getMeaning());
			for (final Term term : terms) {
				removeInternal(term, true);
			}
		}
	}

	private final void removeAllAttributes(final Representation rep) {
		final Set<Attribute> attrs = rep.getAttributes();
		for (final Attribute attr : attrs) {
			attributeService.removeWithoutSessionFlush(attr);
		}
	}

	@Override
	public void remove(final BinaryFactTypeForm bftf) {
		removeInternal(bftf);
		getCurrentSession().flush();
	}

	/**
	 * Internal method to remove a @{link BinaryFactTypeForm} from the database. This will do authorization checking but
	 * will NOT flush the session.
	 * @param attribute The attribute to remove.
	 */
	private void removeInternal(BinaryFactTypeForm bftf) {
		if (binaryFactTypeFormDao.findById(bftf.getId()) == null) {
			return;
		}

		// Perform authorization check.
		authorizationHelper.checkAuthorization(getCurrentUser(), bftf, Permissions.BFTF_REMOVE,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// Send removing event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.BINARY_FACT_TYPE_FORM,
				new BinaryFactTypeFormEventData(bftf, EventType.REMOVING));

		// Find all the references to the BFTF from rules and remove them.
		ruleService.removeReferences(bftf);

		// Remove the specialized concept's taxonomy as this BFTF is being removed.
		removeTaxonomy(bftf);

		// Remove all the attributes.
		removeAllAttributes(bftf);

		// remove all the relations
		for (Relation rel : relationService.findByType(bftf))
			relationService.remove(rel);

		binaryFactTypeFormDao.delete(bftf);

		bftf.getMeaning().getRepresentations().remove(bftf);
		if (bftf.getMeaning().getRepresentations().size() == 0) {
			meaningService.remove(bftf.getMeaning());
		}

		// Send removed event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.BINARY_FACT_TYPE_FORM,
				new BinaryFactTypeFormEventData(bftf, EventType.REMOVED));
	}

	@Override
	public void remove(final CharacteristicForm cForm) {
		// Perform authorization check.
		authorizationHelper.checkAuthorization(getCurrentUser(), cForm, Permissions.CF_REMOVE,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		removeInternal(cForm);

		getCurrentSession().flush();
	}

	private void removeInternal(final CharacteristicForm cForm) {
		// Notify the removing event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.CHARACTERISTIC_FORM,
				new CharacteristicFormEventData(cForm, EventType.REMOVING));

		// Remove the specialized concept's taxonomy as this CF is being removed.
		removeTaxonomy(cForm);

		// Remove all the attributes.
		removeAllAttributes(cForm);

		characteristicFormDao.delete(cForm);

		cForm.getMeaning().getRepresentations().remove(cForm);
		if (cForm.getMeaning().getRepresentations().size() == 0) {
			meaningService.remove(cForm.getMeaning());
		}

		// Notify the removing event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.CHARACTERISTIC_FORM,
				new CharacteristicFormEventData(cForm, EventType.REMOVED));
	}

	@Override
	public void remove(final Vocabulary vocabulary) {

		// Perform authorization check.
		authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_REMOVE,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// Send removing event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.VOCABULARY,
				new VocabularyEventData(vocabulary, EventType.REMOVING));

		List<Vocabulary> incorporatingVocabularies = vocabularyDao.findAllIncorporatingVocabularies(vocabulary);
		for (Vocabulary incorporatingVocabulary : incorporatingVocabularies) {
			incorporatingVocabulary.removeIncorporatedVocabulary(vocabulary);
			vocabularyDao.save(incorporatingVocabulary);
		}

		// Removing all the rulesets.
		for (RuleSet ruleSet : new LinkedList<RuleSet>(vocabulary.getRuleSets())) {
			ruleService.removeRuleSet(ruleSet);
		}
		// Removing all the binary fact type forms.
		for (BinaryFactTypeForm binaryFactTypeForm : new LinkedList<BinaryFactTypeForm>(
				vocabulary.getBinaryFactTypeForms())) {
			removeInternal(binaryFactTypeForm);
		}

		// Removing all the characteristic forms
		for (CharacteristicForm cForm : new LinkedList<CharacteristicForm>(vocabulary.getCharacteristicForms())) {
			removeInternal(cForm);
		}
		// Removing all the terms
		for (Term term : new LinkedList<Term>(vocabulary.getTerms())) {
			removeInternal(term, true);
		}

		vocabularyDao.delete(vocabulary);

		getCurrentSession().flush();

		// Send removed event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.VOCABULARY,
				new VocabularyEventData(vocabulary, EventType.REMOVED));
	}

	private void removeTaxonomy(Representation representation) {
		Concept concept = (Concept) representation.getMeaning();
		List<Concept> specializedConceptsForRepresentations = meaningService.findAllSpecializedConcepts(concept);
		for (Concept specializedConcept : specializedConceptsForRepresentations) {
			meaningService.removeGeneralConcept(specializedConcept);
		}
	}

	/*************************
	 * All the change methods
	 *************************/

	@Override
	public Term changeSignifier(Term term, String newSignifier) {

		// Perform authorization check for rename.
		authorizationHelper.checkAuthorization(getCurrentUser(), term, Permissions.TERM_EDIT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		if (term.getSignifier().equals(newSignifier))
			return term;

		// Check Constraints
		constraintChecker.checkTermAlreadyExistConstraint(newSignifier, term, null);

		// Send changing event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.TERM,
				new TermEventData(term, EventType.CHANGING));

		// Change the name.
		term.setSignifier(newSignifier);

		termDao.save(term);

		// Send changed event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.TERM,
				new TermEventData(term, EventType.CHANGED));
		return term;
	}

	@Override
	public Representation changeStatus(final Representation rep, final Term status) {

		Representation deproxyRepresentation = ServiceUtility.deproxy(rep, Representation.class);

		if (deproxyRepresentation instanceof Term)
			return changeStatus((Term) deproxyRepresentation, status);

		else if (deproxyRepresentation instanceof BinaryFactTypeForm)
			return changeStatus((BinaryFactTypeForm) deproxyRepresentation, status);

		else if (deproxyRepresentation instanceof CharacteristicForm)
			return changeStatus((CharacteristicForm) deproxyRepresentation, status);

		throw new IllegalArgumentException(DGCErrorCodes.REPRESENTATION_UKNOWN, rep.verbalise(), rep.getId());
	}

	private Term changeStatus(final Term term, final Term status) {

		// Authorization check
		authorizationHelper.checkAuthorization(getCurrentUser(), term, Permissions.TERM_STATUS_MODIFY,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		if (term.getStatus().getId() == status.getId()) {
			// Nothing to do.
			return term;
		}

		if (term.isLocked()) {
			authorizationHelper.checkAuthorization(getCurrentUser(), term, Permissions.TERM_LOCK,
					DGCErrorCodes.RESOURCE_NO_PERMISSION);
			((RepresentationImpl) term).setLock(false);
		}

		// Send changing event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.TERM,
				new TermEventData(term, EventType.CHANGING));

		term.setStatus(status);

		// Check if the representation needs to be locked based on new status.
		if (serviceUtility.shouldLock(status)) {
			((RepresentationImpl) term).setLock(true);
		}

		representationDao.save(term);

		// Send changed event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.TERM,
				new TermEventData(term, EventType.CHANGED));

		return term;
	}

	private BinaryFactTypeForm changeStatus(final BinaryFactTypeForm binaryFactTypeForm, final Term status) {

		// Authorization check
		authorizationHelper.checkAuthorization(getCurrentUser(), binaryFactTypeForm, Permissions.BFTF_STATUS_MODIFY,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		if (binaryFactTypeForm.getStatus().getId() == status.getId()) {
			// Nothing to do.
			return binaryFactTypeForm;
		}

		if (binaryFactTypeForm.isLocked()) {
			authorizationHelper.checkAuthorization(getCurrentUser(), binaryFactTypeForm, Permissions.BFTF_LOCK,
					DGCErrorCodes.RESOURCE_NO_PERMISSION);
			((RepresentationImpl) binaryFactTypeForm).setLock(false);
		}

		// Send changing event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.BINARY_FACT_TYPE_FORM,
				new BinaryFactTypeFormEventData(binaryFactTypeForm, EventType.CHANGING));

		binaryFactTypeForm.setStatus(status);

		// Check if the representation needs to be locked based on new status.
		if (serviceUtility.shouldLock(status)) {
			((RepresentationImpl) binaryFactTypeForm).setLock(true);
		}

		representationDao.save(binaryFactTypeForm);

		// Send changed event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.BINARY_FACT_TYPE_FORM,
				new BinaryFactTypeFormEventData(binaryFactTypeForm, EventType.CHANGED));

		return binaryFactTypeForm;
	}

	private CharacteristicForm changeStatus(final CharacteristicForm characteristicForm, final Term status) {

		// Authorization check
		authorizationHelper.checkAuthorization(getCurrentUser(), characteristicForm, Permissions.CF_STATUS_MODIFY,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		if (characteristicForm.getStatus().getId() == status.getId()) {
			// Nothing to do.
			return characteristicForm;
		}

		if (characteristicForm.isLocked()) {
			authorizationHelper.checkAuthorization(getCurrentUser(), characteristicForm, Permissions.CF_LOCK,
					DGCErrorCodes.RESOURCE_NO_PERMISSION);
			((RepresentationImpl) characteristicForm).setLock(false);
		}

		// Send changing event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.CHARACTERISTIC_FORM,
				new CharacteristicFormEventData(characteristicForm, EventType.CHANGING));

		characteristicForm.setStatus(status);

		// Check if the representation needs to be locked based on new status.
		if (serviceUtility.shouldLock(status)) {
			((RepresentationImpl) characteristicForm).setLock(true);
		}

		representationDao.save(characteristicForm);

		// Send changed event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.CHARACTERISTIC_FORM,
				new CharacteristicFormEventData(characteristicForm, EventType.CHANGED));

		return characteristicForm;
	}

	@Override
	public Vocabulary changeName(Vocabulary vocabulary, String newName) {

		// Perform authorization check
		authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_EDIT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// No change needed.
		if (vocabulary.getName().equals(newName)) {
			return vocabulary;
		}

		// Check Constraint
		constraintChecker.checkVocabularyWithNameAlreadyExistsConstraint(newName, vocabulary);

		// Send changed event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.VOCABULARY,
				new VocabularyEventData(vocabulary, EventType.CHANGING));

		// Update vocabulary
		vocabulary.setName(newName);

		vocabularyDao.save(vocabulary);

		// Send changed event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.VOCABULARY,
				new VocabularyEventData(vocabulary, EventType.CHANGED));

		return vocabulary;
	}

	@Override
	public Vocabulary changeUri(Vocabulary vocabulary, String newUri) {

		// Perform authorization check
		authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_EDIT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// No change needed.
		if (vocabulary.getUri().equals(newUri)) {
			return vocabulary;
		}

		// Check constraints
		constraintChecker.checkVocabularyWihtURIAlreadyExistsConstraint(newUri, vocabulary);

		// Send changed event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.VOCABULARY,
				new VocabularyEventData(vocabulary, EventType.CHANGING));

		// Update vocabulary
		vocabulary.setUri(newUri);
		vocabularyDao.save(vocabulary);

		// Send changed event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.VOCABULARY,
				new VocabularyEventData(vocabulary, EventType.CHANGED));

		return vocabulary;
	}

	@Override
	public Vocabulary changeCommunity(Vocabulary vocabulary, Community newCommunity) {

		// Perform authorization check
		authorizationHelper.checkAuthorization(getCurrentUser(), vocabulary, Permissions.VOCABULARY_EDIT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// No change needed
		if (vocabulary.getCommunity().equals(newCommunity))
			return vocabulary;

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.VOCABULARY,
				new VocabularyEventData(vocabulary, EventType.CHANGING));

		// Update the previous community
		Community community = vocabulary.getCommunity();
		community.getVocabularies().remove(vocabulary);

		// Update the new community
		newCommunity.getVocabularies().add(vocabulary);

		// Update the vocabulary
		vocabulary.setCommunity(newCommunity);
		vocabularyDao.save(vocabulary);

		// Send changed event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.VOCABULARY,
				new VocabularyEventData(vocabulary, EventType.CHANGED));

		return vocabulary;
	}

	@Override
	public CharacteristicForm changeCharacteristicForm(CharacteristicForm cf, Term term, String roleSignifier) {

		// Authorization Check
		authorizationHelper.checkAuthorization(getCurrentUser(), cf, Permissions.CF_EDIT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		if (!cf.getTerm().equals(term) || !cf.getRole().equals(roleSignifier)) {

			cf.update(term, roleSignifier);

			// Check Constraints
			constraintChecker.checkConstraints(cf);

			// Send changing event
			ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.CHARACTERISTIC_FORM,
					new CharacteristicFormEventData(cf, EventType.CHANGING));

			characteristicFormDao.save(cf);

			// Send changed event
			ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.CHARACTERISTIC_FORM,
					new CharacteristicFormEventData(cf, EventType.CHANGED));
		}

		return cf;
	}

	@Override
	public BinaryFactTypeForm changeBinaryFactTypeForm(BinaryFactTypeForm bftf, Term headTerm, String role,
			String corole, Term tailTerm) {

		// Authorization Check
		authorizationHelper.checkAuthorization(getCurrentUser(), bftf, Permissions.BFTF_EDIT,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// If nothing is changed then no need to change the BFTF.
		if (!bftf.getHeadTerm().equals(headTerm) || !bftf.getTailTerm().equals(tailTerm)
				|| !bftf.getRole().equals(role) || !bftf.getCoRole().equals(corole)) {

			// Change BFTF and create document for it.
			bftf.update(headTerm, role, corole, tailTerm);

			// Check constraints
			constraintChecker.checkConstraints(bftf);

			// Send changing event
			ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.BINARY_FACT_TYPE_FORM,
					new BinaryFactTypeFormEventData(bftf, EventType.CHANGING));

			binaryFactTypeFormDao.save(bftf);

			// Send changed event
			ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.BINARY_FACT_TYPE_FORM,
					new BinaryFactTypeFormEventData(bftf, EventType.CHANGED));
		}

		return bftf;
	}

	/****************************
	 * All synonym functionality
	 ****************************/

	@Override
	public Representation addSynonym(Representation originalRepresentation, Representation synonymRepresentation) {

		originalRepresentation = ServiceUtility.deproxy(originalRepresentation, Representation.class);
		synonymRepresentation = ServiceUtility.deproxy(synonymRepresentation, Representation.class);

		// Check lock constraint
		ConstraintChecker.checkLockConstraint(originalRepresentation);
		ConstraintChecker.checkLockConstraint(synonymRepresentation);

		// If already synonyms then don't do anything.
		if (synonymRepresentation.getMeaning().equals(originalRepresentation.getMeaning())) {
			return synonymRepresentation;
		}

		if (originalRepresentation instanceof Term) {
			if (synonymRepresentation instanceof Term) {
				return createSynonym((Term) originalRepresentation, (Term) synonymRepresentation);
			} else {
				String message = synonymRepresentation.verbalise() + " should be a Term to create synonym";
				log.error(message);
				throw new ConstraintViolationException(message, synonymRepresentation.getId(), synonymRepresentation
						.getClass().getName(), DGCErrorCodes.REPRESENTATION_SYNONYM_NOT_SAME_TYPE_AS_ORIGINAL);
			}
		} else if (originalRepresentation instanceof BinaryFactTypeForm) {
			if (synonymRepresentation instanceof BinaryFactTypeForm) {
				return createSynonym((BinaryFactTypeForm) originalRepresentation,
						(BinaryFactTypeForm) synonymRepresentation);
			} else {
				String message = synonymRepresentation.toString() + " should be a BinaryFactTypeForm to create synonym";
				log.error(message);
				throw new ConstraintViolationException(message, synonymRepresentation.getId(), synonymRepresentation
						.getClass().getName(), DGCErrorCodes.REPRESENTATION_SYNONYM_NOT_SAME_TYPE_AS_ORIGINAL);
			}
		} else if (originalRepresentation instanceof CharacteristicForm) {
			if (synonymRepresentation instanceof CharacteristicForm) {
				return createSynonym((CharacteristicForm) originalRepresentation,
						(CharacteristicForm) synonymRepresentation);
			} else {
				String message = synonymRepresentation.toString() + " should be a CharacteristicForm to create synonym";
				log.error(message);
				throw new ConstraintViolationException(message, synonymRepresentation.getId(), synonymRepresentation
						.getClass().getName(), DGCErrorCodes.REPRESENTATION_SYNONYM_NOT_SAME_TYPE_AS_ORIGINAL);
			}
		}

		throw new IllegalArgumentException(DGCErrorCodes.REPRESENTATION_UKNOWN, originalRepresentation.verbalise(),
				originalRepresentation.getId());
	}

	@Override
	public Term createSynonym(Term original, Term synonym) {

		// Perform authorization check.
		authorizationHelper.checkAuthorization(getCurrentUser(), original, Permissions.TERM_CREATE_SYNONYM,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		ObjectType originalOT = original.getObjectType();
		ObjectType synonymOT = synonym.getObjectType();

		// We are the only representation of this (old) meaning, so we remove the taxonomy for it.
		if (synonymOT.getRepresentations().size() == 1) {
			removeTaxonomy(synonym);
		}

		// check whether the terms are already synonyms (could be created via the factory)
		// if not we do it here
		if (!originalOT.equals(synonymOT)) {
			synonym.setObjectType(originalOT);
		}

		if (!original.isPersisted()) {
			saveTerm(original);
		}
		if (synonym.getVocabulary().getId().equals(original.getVocabulary().getId())) {
			((RepresentationImpl) synonym).setIsPreferred(false);
		}

		synonym = saveTerm(synonym);
		synonymOT.getRepresentations().remove(synonym);
		// If the synonym is the only preferred representation then choose another preferred representation.
		if (synonymOT.getRepresentations().size() > 0 && !synonymOT.hasDefaultRepresentation()) {
			final Representation firstRep = synonymOT.getRepresentations().iterator().next();
			((RepresentationImpl) firstRep).setIsPreferred(true);
			representationDao.save(firstRep);
		} else if (synonymOT.getRepresentations().size() == 0) {
			meaningService.remove(synonymOT);
		}

		return synonym;
	}

	public BinaryFactTypeForm createSynonym(BinaryFactTypeForm original, BinaryFactTypeForm synonym) {

		// Perform authorization check.
		authorizationHelper.checkAuthorization(getCurrentUser(), original, Permissions.BFTF_CREATE_SYNONYM,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		BinaryFactType originalBFT = original.getBinaryFactType();
		BinaryFactType synonymBFT = synonym.getBinaryFactType();

		// We are the only representation of this (old) meaning, so we remove the taxonomy for it.
		if (synonymBFT.getRepresentations().size() == 1) {
			removeTaxonomy(synonym);
		}

		// check whether the terms are already synonyms (could be created via the factory)
		// if not we do it here
		if (!originalBFT.equals(synonymBFT)) {
			synonym.setBinaryFactType(originalBFT);
		}

		if (!original.isPersisted()) {
			saveBinaryFactTypeForm(original);
		}

		if (synonym.getVocabulary().getId().equals(original.getVocabulary().getId())) {
			((RepresentationImpl) synonym).setIsPreferred(false);
		}

		save(synonym);
		synonymBFT.getRepresentations().remove(synonym);
		// If the synonym is the only preferred representation then choose another preferred representation.
		if (synonymBFT.getRepresentations().size() > 0 && !synonymBFT.hasDefaultRepresentation()) {
			Representation firstRep = synonymBFT.getRepresentations().iterator().next();
			((RepresentationImpl) firstRep).setIsPreferred(true);
			representationDao.save(firstRep);
		} else if (synonymBFT.getRepresentations().size() == 0) {
			meaningService.remove(synonymBFT);
		}

		return synonym;
	}

	public CharacteristicForm createSynonym(CharacteristicForm original, CharacteristicForm synonym) {

		// Perform authorization check.
		authorizationHelper.checkAuthorization(getCurrentUser(), original, Permissions.CF_CREATE_SYNONYM,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		Characteristic originalCharacteristic = original.getCharacteristic();
		Characteristic synonymCharacteristic = synonym.getCharacteristic();

		// We are the only representation of this (old) meaning, so we remove the taxonomy for it.
		if (synonymCharacteristic.getRepresentations().size() == 1) {
			removeTaxonomy(synonym);
		}

		// check whether the terms are already synonyms (could be created via the factory)
		// if not we do it here
		if (!originalCharacteristic.equals(synonymCharacteristic)) {
			synonym.setCharacteristic(originalCharacteristic);
		}

		if (!original.isPersisted()) {
			saveCharacteristicForm(original);
		}

		if (synonym.getVocabulary().getId().equals(original.getVocabulary().getId())) {
			((RepresentationImpl) synonym).setIsPreferred(false);
		}

		representationServiceHelper.saveCharacteristicForm(synonym);
		synonymCharacteristic.getRepresentations().remove(synonym);
		// If the synonym is the only preferred representation then choose another preferred representation.
		if (synonymCharacteristic.getRepresentations().size() > 0 && !synonymCharacteristic.hasDefaultRepresentation()) {
			Representation firstRep = synonymCharacteristic.getRepresentations().iterator().next();
			((RepresentationImpl) firstRep).setIsPreferred(true);
			representationDao.save(firstRep);
		} else if (synonymCharacteristic.getRepresentations().size() == 0) {
			meaningService.remove(synonymCharacteristic);
		}

		return synonym;
	}

	@Override
	public Representation removeSynonym(Representation representation) {

		// TODO set the preferred by type of representation ?

		// Get the list of the current synonyms of the representation
		// to choose a preferred one at the end of the method
		List<Representation> synonyms = findSynonyms(representation);

		// If there is no synonym return the representation
		if (synonyms.isEmpty())
			return representation;

		Representation result = null;

		if (representation instanceof Term) {
			result = removeSynonym((Term) representation);
		} else if (representation instanceof CharacteristicForm) {
			result = removeSynonym((CharacteristicForm) representation);
		} else if (representation instanceof BinaryFactTypeForm) {
			result = removeSynonym((BinaryFactTypeForm) representation);
		}

		if (result != null) {

			getCurrentSession().flush();
			// If there is no preferred representation the choose one.
			choosePreferred(synonyms);

			return result;
		}

		throw new IllegalArgumentException(DGCErrorCodes.REPRESENTATION_UKNOWN, representation.verbalise(),
				representation.getId());
	}

	private Term removeSynonym(Term term) {

		// Perform authorization check.
		authorizationHelper.checkAuthorization(getCurrentUser(), term, Permissions.TERM_REMOVE_SYNONYM,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		term.getObjectType().getRepresentations().remove(term);
		term.setObjectType(meaningFactory.makeObjectType());
		term.getObjectType().getTerms().add(term);
		((RepresentationImpl) term).setIsPreferred(true);

		return saveTerm(term);
	}

	private CharacteristicForm removeSynonym(CharacteristicForm characteristicForm) {

		// Perform authorization check.
		authorizationHelper.checkAuthorization(getCurrentUser(), characteristicForm, Permissions.CF_REMOVE_SYNONYM,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		characteristicForm.getCharacteristic().getRepresentations().remove(characteristicForm);
		characteristicForm.setCharacteristic(meaningFactory.makeCharacteristic());
		characteristicForm.getCharacteristic().getCharacteristicForms().add(characteristicForm);
		((RepresentationImpl) characteristicForm).setIsPreferred(true);

		return saveCharacteristicForm(characteristicForm);
	}

	private BinaryFactTypeForm removeSynonym(BinaryFactTypeForm binaryFactTypeForm) {

		// Perform authorization check.
		authorizationHelper.checkAuthorization(getCurrentUser(), binaryFactTypeForm, Permissions.BFTF_REMOVE_SYNONYM,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		binaryFactTypeForm.getBinaryFactType().getRepresentations().remove(binaryFactTypeForm);
		binaryFactTypeForm.setBinaryFactType(meaningFactory.makeBinaryFactType());
		binaryFactTypeForm.getBinaryFactType().getBinaryFactTypeForms().add(binaryFactTypeForm);
		((RepresentationImpl) binaryFactTypeForm).setIsPreferred(true);

		return saveBinaryFactTypeForm(binaryFactTypeForm);
	}

	@Override
	public Term createConceptType(String termSignifier) {
		Term term = representationFactory.makeTerm(findMetamodelExtensionsVocabulary(), termSignifier);
		term.getObjectType().setGeneralConcept(meaningService.findMetaObjectType());
		return saveTerm(term);
	}

	@Override
	public Term createConceptType(String termSignifier, ObjectType parent) {

		// make sure the given parent is a specialized concept of Meta ObjectType
		if (!meaningService.hasGeneralConcept(parent, meaningService.findMetaObjectType()))
			throw new IllegalArgumentException(DGCErrorCodes.TERM_CONCEPTTYPE_INCOMPATIBLE, termSignifier, parent
					.findPreferredTerm().getSignifier());

		// throw new IllegalArgumentException("Parent is not a specialized concept of ObjectType",
		// DGCErrorCodes.TERM_CONCEPTTYPE_INCOMPATIBLE);

		Term term = representationFactory.makeTerm(findMetamodelExtensionsVocabulary(), termSignifier);
		term.getObjectType().setGeneralConcept(parent);
		return saveTerm(term);
	}

	/*************************
	 * All save functionality
	 *************************/

	@Override
	public Term saveTerm(final Term term) {

		final boolean isNew = !term.isPersisted();
		if (isNew) {
			// Perform authorization check for creation.
			authorizationHelper.checkAuthorization(getCurrentUser(), term.getVocabulary(),
					Permissions.VOCABULARY_ADD_TERM, DGCErrorCodes.RESOURCE_NO_PERMISSION);
		} else {
			checkForAttributeCreateAuthorization(term);
		}

		// Send adding event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.TERM,
				new TermEventData(term, isNew ? EventType.ADDING : EventType.CHANGING));

		Term persistedTerm = representationServiceHelper.saveTerm(term);

		getCurrentSession().flush();

		// Send added event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.TERM,
				new TermEventData(term, isNew ? EventType.ADDED : EventType.CHANGED));
		return persistedTerm;
	}

	@Override
	public CharacteristicForm saveCharacteristicForm(CharacteristicForm characteristicForm) {

		final boolean isNew = !characteristicForm.isPersisted();

		// Should be checked only for non-persisted ones.
		if (isNew) {

			// Perform authorization check for creation.
			authorizationHelper.checkAuthorization(getCurrentUser(), characteristicForm.getVocabulary(),
					Permissions.VOCABULARY_ADD_CHARACTERISTIC_FORM, DGCErrorCodes.RESOURCE_NO_PERMISSION);

		} else {

			checkForAttributeCreateAuthorization(characteristicForm);
		}

		// Send adding event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.CHARACTERISTIC_FORM,
				new CharacteristicFormEventData(characteristicForm, isNew ? EventType.ADDING : EventType.CHANGING));

		characteristicForm = representationServiceHelper.saveCharacteristicForm(characteristicForm);

		getCurrentSession().flush();

		// Send adding event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.CHARACTERISTIC_FORM,
				new CharacteristicFormEventData(characteristicForm, isNew ? EventType.ADDED : EventType.CHANGED));

		return characteristicForm;
	}

	@Override
	public BinaryFactTypeForm saveBinaryFactTypeForm(BinaryFactTypeForm binaryFactTypeForm) {

		final boolean isNew = !binaryFactTypeForm.isPersisted();

		// This must be checked only for non-persisted BFTFs.
		if (isNew) {

			// Perform authorization check for creation.
			authorizationHelper.checkAuthorization(getCurrentUser(), binaryFactTypeForm.getVocabulary(),
					Permissions.VOCABULARY_ADD_FACT_TYPE, DGCErrorCodes.RESOURCE_NO_PERMISSION);

		} else {

			checkForAttributeCreateAuthorization(binaryFactTypeForm);
		}

		// Send adding event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.BINARY_FACT_TYPE_FORM,
				new BinaryFactTypeFormEventData(binaryFactTypeForm, isNew ? EventType.ADDING : EventType.CHANGING));

		binaryFactTypeForm = representationServiceHelper.saveBinaryFactTypeForm(binaryFactTypeForm);

		getCurrentSession().flush();

		// Send added event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.BINARY_FACT_TYPE_FORM,
				new BinaryFactTypeFormEventData(binaryFactTypeForm, isNew ? EventType.ADDED : EventType.CHANGED));

		return binaryFactTypeForm;
	}
}
