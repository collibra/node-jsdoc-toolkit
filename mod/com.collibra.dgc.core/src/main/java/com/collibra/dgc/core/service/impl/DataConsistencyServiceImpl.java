package com.collibra.dgc.core.service.impl;

import org.springframework.stereotype.Service;

import com.collibra.dgc.core.service.DataConsistencyService;

/**
 * Service class providing the data violating the required glossary data consistency.
 * @author dtrog
 * 
 */
@Service
public class DataConsistencyServiceImpl extends AbstractService implements DataConsistencyService {
	// @Autowired
	// private RepresentationService representationService;
	// @Autowired
	// private SpeechCommunityDao speechCommunityDao;
	// @Autowired
	// private SemanticCommunityDao semanticCommunityDao;
	// @Autowired
	// private VocabularyDao vocabularyDao;
	// @Autowired
	// private AttributeDao attributeDao;
	// @Autowired
	// private CommunityDao communityDao;
	// @Autowired
	// private RepresentationDao representationDao;
	// @Autowired
	// private TermDao termDao;
	// @Autowired
	// private SessionFactory sessionFactory;
	//
	// private final Session getCurrentSession() {
	// return sessionFactory.getCurrentSession();
	// }
	//
	// public Collection<Term> findTermsHavingNoMemberWithSpecifiedRole(String roleName) {
	// // NOTE: Exclude all the terms from Collibra SBVR community as the users will be interested in their data.
	// String queryString = "select term from TermImpl term where "
	// +
	// " term.latest = 1 and term.vocabulary.speechCommunity.semanticCommunityResourceId <> ? and  term.id not in "
	// + " (select subqTerm.id from TermImpl subqTerm, MemberImpl mem where "
	// +
	// " subqTerm.latest = 1 and subqTerm.vocabulary.speechCommunity.semanticCommunityResourceId <> ? and subqTerm.id = mem.id and mem.role.term.signifier = ?) "
	// + " order by term.signifier";
	//
	// Query query = getCurrentSession().createQuery(queryString);
	//
	// UUID sbvrSemanticCommunityResourceId = representationService.findSbvrBusinessRulesVocabulary()
	// .getSpeechCommunity().getSemanticCommunity().getResourceId();
	// query.setParameter(0, sbvrSemanticCommunityResourceId);
	// query.setParameter(1, sbvrSemanticCommunityResourceId);
	// query.setParameter(2, roleName);
	// return query.list();
	// }
	//
	// public Collection<SpeechCommunity> findAllLatestSpeechCommunitiesWithoutParentCommuntiy() {
	// return speechCommunityDao.findAllWithoutParentCommuntiy();
	// }
	//
	// public Collection<Vocabulary> findAllLatestVocabulariesWithoutParentCommuntiy() {
	// return vocabularyDao.findAllWithoutParentCommuntiy();
	// }
	//
	// public Collection<Attribute> findAllLatestAttributesWithoutLabel() {
	// return attributeDao.findAllWithoutLabel();
	// }
	//
	// /**
	// * Removes duplicates from the speech community by only considering the latest version of the vocabulary
	// */
	// public void fixSpeechCommunity(SpeechCommunity speechCommunity) {
	// // step 1: check that the speech community has only one vocabulary with a given resource id
	// // and latest true
	// MultiValueMap uuidVocabularyMap = new MultiValueMap();
	// for (Vocabulary vocabulary : speechCommunity.getVocabularies()) {
	// uuidVocabularyMap.put(vocabulary.getResourceId(), vocabulary);
	// }
	//
	// // see which ones are bad and fix that for the speech community
	// for (Object resourceID : uuidVocabularyMap.keySet()) {
	// Collection vocabularies = uuidVocabularyMap.getCollection(resourceID);
	// if (vocabularies != null && vocabularies.size() > 1) {
	// fixVocabularyForSpeechCommunity(speechCommunity, vocabularies);
	// }
	// }
	// }
	//
	// public void fixVocabularyForSpeechCommunity(SpeechCommunity speechCommunity, Collection<Vocabulary> vocabularies)
	// {
	// // determine which vocabulary is the correct one
	// Vocabulary correctVocabulary = determineCorrectVocabularyForSpeechCommunity(speechCommunity, vocabularies);
	//
	// // compile a list of the bad vocabularies
	// List<Vocabulary> badVocabularies = new ArrayList<Vocabulary>();
	// for (Vocabulary vocabulary : vocabularies) {
	// if (vocabulary != correctVocabulary) {
	// badVocabularies.add(vocabulary);
	// }
	// }
	//
	// // for the correct vocabulary, we will copy over all the latest terms from the bad vocabularies
	// // to make sure that all terms are present in the vocabulary
	// List<Term> termsToAddList = determineTermsToAdd(correctVocabulary, badVocabularies);
	// for (Term termToAdd : termsToAddList) {
	// correctVocabulary.addTerm(termToAdd);
	// }
	//
	// // for all the bad vocabularies we will remove them from the speech community
	// for (Vocabulary badVocabulary : badVocabularies) {
	// speechCommunity.removeOwnedVocabulary(badVocabulary);
	// ((VocabularyImpl) badVocabulary).setLatest(false);
	// getCurrentSession().update(badVocabulary);
	// }
	// vocabularyDao.save(correctVocabulary);
	// // save the speech community
	// communityDao.save(speechCommunity);
	// }
	//
	// private List<Term> determineTermsToAdd(Vocabulary correctVocabulary, List<Vocabulary> badVocabularies) {
	// List<Term> termsToReturn = new ArrayList<Term>();
	// for (Vocabulary badVocabulary : badVocabularies) {
	// for (Term candidateTermToAdd : badVocabulary.getTerms()) {
	// if (candidateTermToAdd.isLatest()) {
	// if (correctVocabulary.getTerm(candidateTermToAdd.getSignifier()) != null) {
	// termsToReturn.add(candidateTermToAdd);
	// } else {
	// ((TermImpl) candidateTermToAdd).setLatest(false);
	// termDao.update(candidateTermToAdd);
	// }
	// }
	// }
	// }
	// return termsToReturn;
	// }
	//
	// /**
	// * @return The {@link Vocabulary} with the latest version number.
	// */
	// private Vocabulary determineCorrectVocabularyForSpeechCommunity(SpeechCommunity speechCommunity,
	// Collection<Vocabulary> vocabularies) {
	// Vocabulary vocToReturn = null;
	// for (Vocabulary vocabulary : vocabularies) {
	// if (vocToReturn == null) {
	// vocToReturn = vocabulary;
	// } else if (vocabulary.getVersion() > vocToReturn.getVersion()) {
	// vocToReturn = vocabulary;
	// }
	// }
	// return vocToReturn;
	// }
	//
	// /**
	// * Fixes {@link Vocabulary} by removing duplicate terms and merging their attributes
	// */
	// public void fixVocabulary(Vocabulary vocabulary) {
	// // step 1: check that the vocabulary contains duplicate terms
	// MultiValueMap uuidTermMap = new MultiValueMap();
	// // lookup the terms via a query, since the vocabulary has a set which will not return any duplicates
	//
	// // keep an inconsistenly versioned term list, which we process after removing the duplicates
	// List<Term> inconsistentTerms = new ArrayList<Term>();
	//
	// for (Term term : vocabulary.getTerms()) {
	// // put duplicates in the map
	// uuidTermMap.put(term.getResourceId(), term);
	// // put inconsistent versions in the list
	// if (hasInconsistentVersions(term)) {
	// inconsistentTerms.add(term);
	// }
	// }
	//
	// // remove the duplicates
	// for (Object resourceID : uuidTermMap.keySet()) {
	// Collection terms = uuidTermMap.getCollection(resourceID);
	// // look for duplicates
	// if (terms != null && terms.size() > 1) {
	// fixTermForVocabulary(vocabulary, terms);
	// }
	// }
	//
	// // fix versioning issues
	// for (Term inconsistentTerm : inconsistentTerms) {
	// fixInconsistentTerm(inconsistentTerm, vocabulary);
	// }
	//
	// // remove terms that are not latest from the vocabulary
	// for (Term term : new ArrayList<Term>(vocabulary.getTerms())) {
	// if (!term.isLatest()) {
	// vocabulary.removeTerm(term);
	// }
	// }
	//
	// // save a new version of the vocabulary
	// vocabularyDao.save(vocabulary);
	// }
	//
	// /**
	// * Fixes inconsistently versioned terms by taking the one that's in the given vocabulary as latest and set all
	// * others as latest false. Will create a new version of that correct term as well.
	// *
	// * @param inconsistentTerm The term with inconsistent versions
	// * @param vocabulary The vocabulary that contains the term
	// */
	// private void fixInconsistentTerm(Term inconsistentTerm, Vocabulary vocabulary) {
	// List<Term> terms = termDao.findByResourceId(inconsistentTerm.getResourceId());
	// Integer latestVersion = termDao.findLatestVersion(inconsistentTerm.getResourceId());
	// Term termInVocabulary = vocabulary.getTerm(inconsistentTerm.getSignifier());
	// for (Term termVersion : terms) {
	// if (termVersion.getId() == termInVocabulary.getId()) {
	// ((TermImpl) termVersion).setLatest(true);
	// ((TermImpl) termVersion).setVersion(latestVersion + 1);
	// termDao.save(termVersion);
	// } else {
	// ((TermImpl) termVersion).setLatest(false);
	// termDao.update(termVersion);
	// }
	// }
	//
	// }
	//
	// /**
	// * @return <code>true</code> when the term has inconsistencies in its versioning
	// */
	// private boolean hasInconsistentVersions(Term term) {
	// List<Term> terms = termDao.findByResourceId(term.getResourceId());
	// boolean latest = false;
	// for (Term termVersion : terms) {
	// if (latest == false && termVersion.isLatest()) {
	// latest = true;
	// } else if (latest == true && termVersion.isLatest()) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// private void fixTermForVocabulary(Vocabulary vocabulary, Collection<Term> terms) {
	// // determine which term is the correct one
	// Term correctTerm = determineCorrectTermForVocabulary(vocabulary, terms);
	//
	// // compile a list of the bad terms
	// List<Term> badTerms = new ArrayList<Term>();
	// for (Term term : terms) {
	// if (term != correctTerm) {
	// badTerms.add(term);
	// }
	// }
	//
	// // for the correct term, we will copy over all the latest attributes from the bad terms
	// List<Attribute> attributesToAddList = determineAttributesToAdd(correctTerm, badTerms);
	// for (Attribute attributesToAdd : attributesToAddList) {
	// correctTerm.addAttribute(attributesToAdd);
	// }
	//
	// // for all the bad terms we will remove them from the vocabulary
	// for (Term badTerm : badTerms) {
	// vocabulary.removeTerm(badTerm);
	// ((TermImpl) badTerm).setLatest(false);
	// getCurrentSession().update(badTerm);
	// }
	// termDao.save(correctTerm);
	// getCurrentSession().flush();
	// }
	//
	// /**
	// * @return The {@link Term} with the latest version
	// */
	// private Term determineCorrectTermForVocabulary(Vocabulary vocabulary, Collection<Term> terms) {
	// Term termToReturn = null;
	// for (Term term : terms) {
	// if (termToReturn == null) {
	// termToReturn = term;
	// } else if (term.getVersion() > termToReturn.getVersion()) {
	// termToReturn = term;
	// }
	// }
	// return termToReturn;
	// }
	//
	// /**
	// * Merges the attributes from the bad terms into the correct term's attributes
	// * @return The list of attributes for the correct term
	// */
	// private List<Attribute> determineAttributesToAdd(Term correctTerm, List<Term> badTerms) {
	// List<Attribute> attributesToReturn = new ArrayList<Attribute>();
	// for (Term badTerm : badTerms) {
	// for (Attribute candidateAttributeToAdd : badTerm.getAttributes()) {
	// if (candidateAttributeToAdd.isLatest()) {
	// List<Attribute> allVersions = attributeDao
	// .findByResourceId(candidateAttributeToAdd.getResourceId());
	// for (Attribute attrVersion : allVersions) {
	// if (attrVersion.getVersion() != candidateAttributeToAdd.getVersion()) {
	// ((AttributeImpl) attrVersion).setLatest(false);
	// attributeDao.update(attrVersion);
	// }
	// }
	// attributesToReturn.add(candidateAttributeToAdd);
	// ((AttributeImpl) candidateAttributeToAdd).setOwner(correctTerm);
	// attributeDao.update(candidateAttributeToAdd);
	// }
	// }
	// }
	// return attributesToReturn;
	// }
	//
	// public Collection<Term> findTermsWithoutConceptType() {
	// return termDao.findTermsWithoutConceptType();
	// }
	//
	// public Map<Representation, Long> findLatestRepresentationsWithMorethanOneStatus() {
	// return representationDao.findLatestRepresentationsWithMorethanOneStatus();
	// }
	//
	// public Map<Representation, Map<Long, Long>> findRepresentationsWithMorethanOneStatus() {
	// return representationDao.findRepresentationsWithMorethanOneStatus();
	// }
	//
	// public Collection<Representation> findLatestRepresentationsWithoutStatus() {
	// return representationDao.findRepresentationsWithoutStatus();
	// }
	//
	// public Collection<Term> findTermsWithoutMeaning() {
	// return termDao.findTermsWithoutMeaning();
	// }
	//
	// public Map<Representation, Long> findMultipleLatestVersions() {
	// return representationDao.findMultipleVersions();
	// }
	//
	// public Collection<SemanticCommunity> findAllSemanticCommunitiesWithoutMetaSpeechCommunity() {
	// return semanticCommunityDao.findAllWithoutMetaSpeechCommunity();
	// }
	//
	// public Collection<SemanticCommunity> findAllSemanticCommunitiesWithoutMetaVocabulary() {
	// return semanticCommunityDao.findAllWithoutMetaVocabulary();
	// }
	//
	// public Collection<SpeechCommunity> findAllSpeechCommunitiesWithoutParentCommuntiy() {
	// return speechCommunityDao.findAllWithoutParentCommuntiy();
	// }
	//
	// public Collection<Vocabulary> findAllVocabulariesWithoutParentCommuntiy() {
	// return vocabularyDao.findAllWithoutParentCommuntiy();
	// }
	//
	// public Collection<Representation> findAllRepresentationsWithoutVocabulary() {
	// return representationDao.findAllWithoutVocabulary();
	// }
	//
	// public Collection<Attribute> findAllAttributesWithoutOwner() {
	// return attributeDao.findAllWithoutOwner();
	// }
	//
	// public Collection<Attribute> findAllAttributesWithoutLabel() {
	// return attributeDao.findAllWithoutLabel();
	// }
	//
	// public Collection<Concept> findLatestConceptWithNonLatestType() {
	// String queryString =
	// "select concept from ConceptImpl concept where concept.latest = 1 and concept.type.latest = 0";
	// Query query = getCurrentSession().createQuery(queryString);
	// return query.list();
	// }
	//
	// public Collection<Term> findLatestTermsWithNonLatestMeaning() {
	// return termDao.findTermsWithNoMeaning();
	// }
	//
	// public MultiValueMap findLatestRepresentationsWithNonLatestAttributes() {
	// return representationDao.findRepresentationsWithoutAttributes();
	// }
	//
	// public MultiValueMap findVocabulariesWithNonLatestTerms() {
	// return vocabularyDao.findVocabulariesWithoutTerms();
	// }
	//
	// public Collection<SpeechCommunity> findSpeechCommunitiesWithDuplicateVocabularies() {
	// List<SpeechCommunity> spcs = speechCommunityDao.findAll();
	// List<SpeechCommunity> badSpeechCommunities = new LinkedList<SpeechCommunity>();
	// for (SpeechCommunity spc : spcs) {
	// if (hasDuplicates(spc)) {
	// badSpeechCommunities.add(spc);
	// }
	// }
	// return badSpeechCommunities;
	// }
	//
	// public Collection<Vocabulary> findNonlatestVocabulariesInLatestCommunity() {
	// List<Vocabulary> result = new LinkedList<Vocabulary>();
	// for (SpeechCommunity community : speechCommunityDao.findCommunityWithoutVocabularies()) {
	// result.addAll(getNonLatestVocabularies(community));
	// }
	//
	// return result;
	// }
	//
	// public Collection<Vocabulary> fixNonlatestVocabulariesInLatestCommunity() {
	// List<Vocabulary> result = new LinkedList<Vocabulary>();
	//
	// // For each latest community having non-latest vocabulary fix.
	// for (SpeechCommunity community : speechCommunityDao.findCommunityWithoutVocabularies()) {
	// boolean newCommunityVersionNeeded = false;
	// for (Vocabulary voc : getNonLatestVocabularies(community)) {
	// // Check if new version of community need to be created.
	// if (!newCommunityVersionNeeded) {
	// // If the previous version of the community also owns the non-latest vocabulary of the same version
	// // then new version for community is not needed. Otherwise create a new version.
	// SpeechCommunity previousVersion = speechCommunityDao.findByResourceId(community.getResourceId(),
	// community.getVersion() - 1);
	// if (previousVersion != null) {
	// for (Vocabulary previousVocabulary : previousVersion.getOwnedVocabularies()) {
	// if (previousVocabulary.getResourceId().equals(voc.getResourceId())
	// && previousVocabulary.getVersion() != voc.getVersion()) {
	// newCommunityVersionNeeded = true;
	// }
	// }
	// }
	// }
	//
	// community.removeOwnedVocabulary(voc);
	// result.add(voc);
	// }
	//
	// // If new version to be created for community.
	// if (newCommunityVersionNeeded) {
	// createVersionedResource(community, speechCommunityDao);
	// } else {
	// getCurrentSession().save(community);
	// }
	// }
	//
	// getCurrentSession().flush();
	//
	// return result;
	// }
	//
	// private boolean hasDuplicates(SpeechCommunity spc) {
	// Set<UUID> vocRIDs = new HashSet<UUID>();
	// for (Vocabulary voc : spc.getOwnedVocabularies()) {
	// if (vocRIDs.contains(voc.getResourceId())) {
	// return true;
	// }
	// vocRIDs.add(voc.getResourceId());
	// }
	// return false;
	// }
	//
	// private List<Vocabulary> getNonLatestVocabularies(SpeechCommunity community) {
	// List<Vocabulary> result = new LinkedList<Vocabulary>();
	// for (Vocabulary voc : community.getOwnedVocabularies()) {
	// if (!voc.isLatest()) {
	// result.add(voc);
	// }
	// }
	// return result;
	// }

}
