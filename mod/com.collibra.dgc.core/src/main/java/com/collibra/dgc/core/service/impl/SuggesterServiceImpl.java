package com.collibra.dgc.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.dao.BinaryFactTypeFormDao;
import com.collibra.dgc.core.dao.CategorizationTypeDao;
import com.collibra.dgc.core.dao.CharacteristicFormDao;
import com.collibra.dgc.core.dao.CommunityDao;
import com.collibra.dgc.core.dao.ConceptDao;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.dao.TermDao;
import com.collibra.dgc.core.dao.VocabularyDao;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Designation;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.service.CategorizationService;
import com.collibra.dgc.core.service.MeaningService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.RightsService;
import com.collibra.dgc.core.service.SuggesterService;
import com.collibra.dgc.core.util.SortUtil;
import com.collibra.dgc.core.workflow.WorkflowEngine;

@Service
public class SuggesterServiceImpl implements SuggesterService {
	private static final Logger log = LoggerFactory.getLogger(SuggesterServiceImpl.class);

	@Autowired
	private RepresentationService representationService;
	@Autowired
	private MeaningService meaningService;
	@Autowired
	private RightsService rightsService;
	@Autowired
	private CategorizationService categorizationService;
	@Autowired
	private TermDao termDao;
	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private CharacteristicFormDao characteristicFormDao;
	@Autowired
	private BinaryFactTypeFormDao binaryFactTypeFormDao;
	@Autowired
	private ConceptDao conceptDao;
	@Autowired
	private VocabularyDao vocabularyDao;
	@Autowired
	private CategorizationTypeDao categorizationTypeDao;
	@Autowired
	private ObjectTypeDao objectTypeDao;
	@Autowired
	private ServiceUtility serviceUtility;
	@Autowired
	private WorkflowEngine workflowEngine;

	private final SortUtil sortUtil = new SortUtil();

	@Override
	public List<Term> suggestSynonymsForTerm(final Term term, final String startsWith) {

		List<Vocabulary> allLatestVocabularies = representationService.findVocabularies();

		List<Term> result = new LinkedList<Term>();
		if (allLatestVocabularies != null && allLatestVocabularies.size() > 0) {
			for (Vocabulary vocabulary : allLatestVocabularies) {
				if (vocabulary != null
						&& !vocabulary.getCommunity().getTopLevelCommunity().getUri()
								.equals(Constants.METAMODEL_COMMUNITY_URI)) {
					result.addAll(termDao.searchTermsBySignifier(vocabulary, startsWith));
				}
			}
		}

		if (result.contains(term)) {
			result.remove(term);
		}
		return result;
	}

	@Override
	public List<CharacteristicForm> suggestSynonymousFormsForCharacteristicForm(final CharacteristicForm cForm,
			final String startsWith) {

		List<Vocabulary> allLatestVocabularies = representationService.findVocabularies();

		List<CharacteristicForm> suggestions = new LinkedList<CharacteristicForm>();
		if (allLatestVocabularies != null && allLatestVocabularies.size() > 0) {
			for (Vocabulary vocabulary : allLatestVocabularies) {
				if (vocabulary != null
						&& !vocabulary.getCommunity().getTopLevelCommunity().getUri()
								.equals(Constants.METAMODEL_COMMUNITY_URI)) {
					suggestions.addAll(characteristicFormDao.searchByVerbalisation(vocabulary, startsWith));
				}
			}
		}

		if (suggestions.contains(cForm)) {
			suggestions.remove(cForm);
		}
		return suggestions;
	}

	@Override
	public List<BinaryFactTypeForm> suggestSynonymousFormsForBinaryFactTypeForm(final BinaryFactTypeForm bftForm,
			final String startsWith) {
		List<Vocabulary> allLatestVocabularies = representationService.findVocabularies();

		List<BinaryFactTypeForm> suggestions = new LinkedList<BinaryFactTypeForm>();
		if (allLatestVocabularies != null && allLatestVocabularies.size() > 0) {
			for (Vocabulary vocabulary : allLatestVocabularies) {
				if (vocabulary != null
						&& !vocabulary.getCommunity().getTopLevelCommunity().getUri()
								.equals(Constants.METAMODEL_COMMUNITY_URI)) {
					suggestions.addAll(binaryFactTypeFormDao.searchByVerbalisation(vocabulary, startsWith));
				}
			}
		}

		if (suggestions.contains(bftForm)) {
			suggestions.remove(bftForm);
		}

		return suggestions;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Term> suggestCategories(Term categorizationTypeTerm, String startsWith) {

		// make it lower case to make autosuggest case independent
		startsWith = startsWith.toLowerCase();

		ObjectType metaCategorizationType = objectTypeDao.getMetaCategorizationType();
		CategorizationType categorizationType = categorizationTypeDao.findById(categorizationTypeTerm.getObjectType()
				.getId());

		Set<String> existingCategorySignifiers = new HashSet<String>();
		for (Term existingCategory : categorizationService.findCategoryTerms(categorizationTypeTerm.getVocabulary())) {
			existingCategorySignifiers.add(existingCategory.getSignifier());
		}

		if (categorizationType == null) {
			log.error("Could not get categorization type for given term: " + categorizationTypeTerm);
			return new ArrayList<Term>();
		}

		List<Term> terms = findPossibleTaxonomyTerms(categorizationTypeTerm, startsWith);
		List<Term> result = new ArrayList<Term>();
		result.addAll(terms);

		for (Term t : terms) {

			// we don't want terms from meta vocabularies
			if (t.getVocabulary().isMeta()) {
				result.remove(t);
				continue;
			}

			ObjectType termType = t.getMeaning().getType();

			// we don't want to return incorporated terms that already exist as categories with the same signifier.
			if (existingCategorySignifiers.contains(t.getSignifier())) {
				result.remove(t);
				continue;
			}

			// we don't want to return the term for which the given categorization type is categorizing
			if (t.getMeaning().equals(categorizationType.getIsForConcept())) {
				result.remove(t);
				// neither do we want its general concepts
				result.removeAll(representationService.findAllGeneralConceptRepresentation(t));
				continue;
			}

			// we don't want to return terms that are categories already
			if (termType.getType().equals(metaCategorizationType)) {
				result.remove(t);
				continue;
			}

			// we don't want to return terms that are category types
			if (termType.equals(metaCategorizationType))
				result.remove(t);
		}

		return (List<Term>) sortUtil.sortRepresentations(result, false);
	}

	@Override
	public List<Term> suggestCategorizationTypes(Term forConcept, String startsWith) {

		// make it lower case to make autosuggest case independent
		startsWith = startsWith.toLowerCase();

		ObjectType thing = objectTypeDao.getMetaThing();

		// get a list of all general concepts
		List<Representation> generalConcepts = representationService.findAllGeneralConceptRepresentation(forConcept);

		// for each general concept, get the categorization types
		List<Term> result = new ArrayList<Term>();
		for (Representation gc : generalConcepts) {
			for (Term catTypeTermForConcept : categorizationService.findCategorizationTypeTermsForConcept(((Term) gc)
					.getObjectType())) {
				if (startsWith == null || startsWith.length() == 0
						|| catTypeTermForConcept.getSignifier().toLowerCase().startsWith(startsWith))
					result.add(catTypeTermForConcept);
			}
		}

		return result;
	}

	@Override
	public List<Term> suggestConceptsForScheme(Designation scheme, String startsWith) {

		Set<Vocabulary> vocs = scheme.getVocabulary().getAllNonSbvrIncorporatedVocabularies();
		vocs.add(scheme.getVocabulary());

		List<Term> result = new LinkedList<Term>();
		List<Term> termsWithCategories = vocabularyDao.findAllTermsWithCategories(vocs, startsWith);

		// filter out the terms that are already categorized by the given scheme
		Set<Concept> conceptsCategorized = scheme.getMeaning().getConceptsCategorized();
		for (Term t : termsWithCategories)
			if (!conceptsCategorized.contains(t.getMeaning()))
				result.add(t);

		return result;
	}

	@Override
	public List<Term> suggestConceptTypeTerms(final CharacteristicForm cForm, String signifierStartsWith) {
		return findPossibleConceptTypeTerms(cForm, signifierStartsWith, meaningService.findMetaCharacteristic());
	}

	@Override
	public List<Term> suggestConceptTypeTerms(final BinaryFactTypeForm bftf, String signifierStartsWith) {
		return findPossibleConceptTypeTerms(bftf, signifierStartsWith, meaningService.findMetaBinaryFactType());
	}

	@Override
	public List<BinaryFactTypeForm> suggestSpecializableBinaryFactTypeForms(BinaryFactTypeForm binaryFactTypeForm) {
		ObjectType headOT = binaryFactTypeForm.getHeadTerm().getObjectType();
		ObjectType tailOT = binaryFactTypeForm.getTailTerm().getObjectType();
		List<Concept> specializedHeadConcepts = meaningService.findAllSpecializedConcepts(headOT);
		List<Concept> specializedTailConcepts = meaningService.findAllSpecializedConcepts(tailOT);
		List<BinaryFactTypeForm> suggestedBftfs = new LinkedList<BinaryFactTypeForm>();

		// suggest bftfs that have both terms as general concepts
		for (Concept specializedHeadConcept : specializedHeadConcepts) {
			for (Concept specializedTailConcept : specializedTailConcepts) {
				for (BinaryFactTypeForm suggestedBinaryFactTypeForm : binaryFactTypeFormDao.find(
						specializedHeadConcept, specializedTailConcept)) {
					// don't suggest already specialized concepts
					if (binaryFactTypeForm.getBinaryFactType().equals(
							suggestedBinaryFactTypeForm.getBinaryFactType().getGeneralConcept())) {
						continue;
					}
					suggestedBftfs.add(suggestedBinaryFactTypeForm);
				}
			}
		}

		// suggest bftfs which only have head term as specialized concept and the other one remains the same
		for (Concept specializedHeadConcept : specializedHeadConcepts) {
			for (BinaryFactTypeForm suggestedBinaryFactTypeForm : binaryFactTypeFormDao.find(specializedHeadConcept,
					tailOT)) {
				// don't suggest already specialized concepts
				if (binaryFactTypeForm.getBinaryFactType().equals(
						suggestedBinaryFactTypeForm.getBinaryFactType().getGeneralConcept())) {
					continue;
				}
				suggestedBftfs.add(suggestedBinaryFactTypeForm);
			}
		}

		// suggest bftfs which only have tail term as specialized concept and the other one remains the same
		for (Concept specializedTailConcept : specializedTailConcepts) {
			for (BinaryFactTypeForm suggestedBinaryFactTypeForm : binaryFactTypeFormDao.find(headOT,
					specializedTailConcept)) {
				// don't suggest already specialized concepts
				if (binaryFactTypeForm.getBinaryFactType().equals(
						suggestedBinaryFactTypeForm.getBinaryFactType().getGeneralConcept())) {
					continue;
				}
				suggestedBftfs.add(suggestedBinaryFactTypeForm);
			}
		}
		return suggestedBftfs;
	}

	@Override
	public List<Term> suggestGeneralTerms(Term term, String signifierStartsWith) {
		return findPossibleTaxonomyTerms(term, signifierStartsWith);
	}

	@Override
	public List<BinaryFactTypeForm> suggestGeneralBinaryFactTypeForms(BinaryFactTypeForm bftForm, String startsWith) {
		List<BinaryFactTypeForm> result = suggestConceptBinaryFactTypeForms(bftForm, startsWith,
				bftForm.getVocabulary(), new ArrayList<Vocabulary>(), false);
		if (result.contains(bftForm)) {
			result.remove(bftForm);
		}
		return result;
	}

	@Override
	public List<BinaryFactTypeForm> suggestGeneralizableBinaryFactTypeForms(BinaryFactTypeForm binaryFactTypeForm) {
		ObjectType headOT = binaryFactTypeForm.getHeadTerm().getObjectType();
		ObjectType tailOT = binaryFactTypeForm.getTailTerm().getObjectType();
		List<Concept> headConcepts = meaningService.findTaxonomicalParentConcepts(headOT);
		List<Concept> tailConcepts = meaningService.findTaxonomicalParentConcepts(tailOT);
		List<BinaryFactTypeForm> suggestedBftfs = new LinkedList<BinaryFactTypeForm>();
		for (Concept headConcept : headConcepts) {
			for (Concept tailConcept : tailConcepts) {
				for (BinaryFactTypeForm suggestedBinaryFactTypeForm : binaryFactTypeFormDao.find(headConcept,
						tailConcept)) {
					// don't suggest already generalized concepts
					if (suggestedBinaryFactTypeForm.equals(binaryFactTypeForm)
							|| binaryFactTypeForm.getBinaryFactType().getGeneralConcept()
									.equals(suggestedBinaryFactTypeForm.getBinaryFactType())
							|| serviceUtility.isSbvrVocabulary(suggestedBinaryFactTypeForm.getVocabulary())) {
						continue;
					}
					suggestedBftfs.add(suggestedBinaryFactTypeForm);
				}
			}
		}

		return suggestedBftfs;
	}

	@Override
	public List<CharacteristicForm> suggestGeneralCharacteristicForms(CharacteristicForm cForm, String startsWith) {
		List<CharacteristicForm> result = suggestConceptCharacteristicForms(cForm, startsWith, cForm.getVocabulary(),
				new ArrayList<Vocabulary>(), false);
		if (result.contains(cForm)) {
			result.remove(cForm);
		}
		return result;
	}

	@Override
	public List<Term> suggestSpecializedTerms(final Term term, final String signifierStartsWith) {
		return findPossibleTaxonomyTerms(term, signifierStartsWith);
	}

	@Override
	public List<BinaryFactTypeForm> suggestSpecializedBinaryFactTypeForms(BinaryFactTypeForm bftForm, String startsWith) {
		List<BinaryFactTypeForm> result = suggestConceptBinaryFactTypeForms(bftForm, startsWith,
				bftForm.getVocabulary(), new ArrayList<Vocabulary>(), true);
		if (result.contains(bftForm)) {
			result.remove(bftForm);
		}
		return result;
	}

	@Override
	public List<CharacteristicForm> suggestSpecializedCharacteristicForms(CharacteristicForm cForm, String startsWith) {
		List<CharacteristicForm> result = suggestConceptCharacteristicForms(cForm, startsWith, cForm.getVocabulary(),
				new ArrayList<Vocabulary>(), true);
		if (result.contains(cForm)) {
			result.remove(cForm);
		}
		return result;
	}

	@Override
	public Collection<Member> suggestUsersForRepresentation(String resourceId) {
		if (resourceId == null) {
			return new LinkedList<Member>();
		}

		Representation representation = representationService.findRepresentationByResourceId(resourceId);
		return suggestUsers(representation);
	}

	@Override
	public Collection<Member> suggestUsers(Representation representation) {
		if (representation == null) {
			return new LinkedList<Member>();
		}
		return rightsService.findMembers(representation.getVocabulary().getCommunity().getId());
	}

	@Override
	public Collection<Member> suggestUsers(Community spc) {
		if (spc == null) {
			return new LinkedList<Member>();
		}
		return rightsService.findMembers(spc.getId());
	}

	@Override
	public List<Term> suggestConceptTypeTerms(final Term t, final String signifierStartsWith) {
		return findPossibleConceptTypeTerms(t, signifierStartsWith, objectTypeDao.getMetaObjectType());
	}

	protected List<Term> findPossibleConceptTypeTerms(Representation r, String signifierStartsWith, Concept parentType) {
		Set<Vocabulary> vocs = findPossibleVocabularies(r, true);

		// get the terms & filter on concept
		if (signifierStartsWith == null || signifierStartsWith.length() == 0)
			return filterTermsByType(termDao.findTermsByVocabularies(vocs), parentType);
		else
			return filterTermsByType(termDao.searchTermsBySignifier(vocs, signifierStartsWith), parentType);
	}

	protected List<Term> findPossibleTaxonomyTerms(Representation r, String signifierStartsWith) {
		Set<Vocabulary> vocs = findPossibleVocabularies(r, true);
		List<Term> terms;

		// get the terms & filter on concept
		if (signifierStartsWith == null || signifierStartsWith.length() == 0)
			terms = termDao.findTermsByVocabularies(vocs);
		else
			terms = termDao.searchTermsBySignifier(vocs, signifierStartsWith);

		// remove the representation itself
		terms.remove(r);

		return terms;
	}

	protected Set<Vocabulary> findPossibleVocabularies(Representation r, boolean includeSBVR) {
		// vocabularies taken into account: meta concept type vocabulary + all incorporated vocabularies + vocabulary
		// from term
		Set<Vocabulary> vocs = new HashSet<Vocabulary>();

		// Add specific concept type meta vocabulary
		Vocabulary conceptTypeVocabulary = vocabularyDao.findMetamodelExtensionsVocabulary();
		vocs.add(conceptTypeVocabulary);

		// add SBVR vocabularies
		if (includeSBVR) {
			for (Community comm : communityDao.findSBVRCommunities())
				for (Vocabulary v : comm.getVocabularies())
					vocs.add(v);
		}

		// add vocabulary of representation and its incorporated vocabularies
		vocs.add(r.getVocabulary());
		addIncorporatedVocabularies(r.getVocabulary(), vocs, includeSBVR);

		return vocs;
	}

	private void addIncorporatedVocabularies(Vocabulary voc, Set<Vocabulary> vocs, boolean includeSBVR) {
		for (Vocabulary incorporatedVoc : voc.getIncorporatedVocabularies(!includeSBVR))
			if (vocs.add(incorporatedVoc))
				addIncorporatedVocabularies(incorporatedVoc, vocs, includeSBVR);
	}

	protected List<Term> filterTermsByType(List<Term> terms, Concept type) {
		List<Term> result = new LinkedList<Term>();
		HashSet<Concept> allowedTypes = new HashSet<Concept>(meaningService.findAllSpecializedConcepts(type));
		for (Term t : terms) {
			if (allowedTypes.contains(t.getObjectType()))
				result.add(t);
		}
		return result;
	}

	private List<CharacteristicForm> suggestConceptCharacteristicForms(final CharacteristicForm characteristicForm,
			final String startsWith, final Vocabulary vocabulary, final List<Vocabulary> processedVocabularies,
			boolean isExcludeSBVRVocabularies) {

		if (processedVocabularies.contains(vocabulary)) {
			return new ArrayList<CharacteristicForm>();
		}

		Vocabulary sbvrVocabulary = representationService.findVocabularyByUri(Constants.SBVR_VOC);

		if (vocabulary.equals(sbvrVocabulary) || sbvrVocabulary.getIncorporatedVocabularies().contains(vocabulary)) {
			if (isExcludeSBVRVocabularies) {
				return new ArrayList<CharacteristicForm>();
			}
		}
		processedVocabularies.add(vocabulary);

		Vocabulary latestVocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		List<CharacteristicForm> chFormSuggestions = characteristicFormDao.searchByVerbalisation(latestVocabulary,
				startsWith);

		Set<Vocabulary> incorporatedVocabularies = latestVocabulary.getIncorporatedVocabularies();
		if (incorporatedVocabularies != null && incorporatedVocabularies.size() > 0) {
			for (Vocabulary incVocabulary : incorporatedVocabularies) {
				if (incVocabulary != null) {
					if (incVocabulary.equals(sbvrVocabulary)
							|| sbvrVocabulary.getIncorporatedVocabularies().contains(incVocabulary)) {
						if (!isExcludeSBVRVocabularies) {
							chFormSuggestions.addAll(suggestConceptCharacteristicForms(characteristicForm, startsWith,
									incVocabulary, processedVocabularies, isExcludeSBVRVocabularies));
						}
					}
				}
			}
		}
		return chFormSuggestions;
	}

	private List<BinaryFactTypeForm> suggestConceptBinaryFactTypeForms(final BinaryFactTypeForm binaryFactTypeForm,
			final String startsWith, final Vocabulary vocabulary, final List<Vocabulary> processedVocabularies,
			boolean isExcludeSBVRVocabularies) {

		if (processedVocabularies.contains(vocabulary)) {
			return new ArrayList<BinaryFactTypeForm>();
		}
		Vocabulary sbvrVocabulary = representationService.findVocabularyByUri(Constants.SBVR_VOC);

		if (vocabulary.equals(sbvrVocabulary) || sbvrVocabulary.getIncorporatedVocabularies().contains(vocabulary)) {
			if (isExcludeSBVRVocabularies) {
				return new ArrayList<BinaryFactTypeForm>();
			}
		}
		processedVocabularies.add(vocabulary);

		Vocabulary latestVocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		List<BinaryFactTypeForm> bftfSuggestions = binaryFactTypeFormDao.searchByVerbalisation(latestVocabulary,
				startsWith);

		Set<Vocabulary> incorporatedVocabularies = latestVocabulary.getIncorporatedVocabularies();
		if (incorporatedVocabularies != null && incorporatedVocabularies.size() > 0) {
			for (Vocabulary incVocabulary : incorporatedVocabularies) {
				if (incVocabulary != null) {
					if (incVocabulary.equals(sbvrVocabulary)
							|| sbvrVocabulary.getIncorporatedVocabularies().contains(incVocabulary)) {
						if (!isExcludeSBVRVocabularies) {
							bftfSuggestions.addAll(suggestConceptBinaryFactTypeForms(binaryFactTypeForm, startsWith,
									incVocabulary, processedVocabularies, isExcludeSBVRVocabularies));
						}
					}
				}
			}
		}
		return bftfSuggestions;
	}

	private List<Vocabulary> getAllSBVRVocabularies() {
		List<Vocabulary> vocabularies = new LinkedList<Vocabulary>();
		Vocabulary sbvrVocabulary = representationService.findVocabularyByUri(Constants.SBVR_VOC);
		vocabularies.add(sbvrVocabulary);
		vocabularies.addAll(sbvrVocabulary.getIncorporatedVocabularies());
		return vocabularies;

	}

	@Override
	public Collection<BinaryFactTypeForm> suggestFactTypes(Term head, Term tail, Vocabulary voc) {
		Set<Vocabulary> vocs = voc.getAllNonSbvrIncorporatedVocabularies();
		vocs.add(voc);
		Set<BinaryFactTypeForm> result = new HashSet<BinaryFactTypeForm>();
		// Performance tweet. Only look for fact types between the given head, not all of its parents.
		// List<Representation> heads = representationService.findAllGeneralConceptRepresentation(head);
		// heads.add(head);
		// for(Representation reprHead : heads) {
		// if(reprHead instanceof Term) {
		for (BinaryFactTypeForm bftf : representationService.findBinaryFactTypeFormsContainingHeadTerm(head)) {
			if ((tail == null || bftf.getTailTerm().equals(tail)) && vocs.contains(bftf.getVocabulary()))
				result.add(bftf);
		}
		// }
		// }

		return result;
	}

	@Override
	public Collection<ProcessDefinition> suggestWorkflowProcesses(String match) {
		if (match == null) {
			return new LinkedList<ProcessDefinition>();
		}

		return workflowEngine.getDeployedProcessesLike(match);
	}
}
