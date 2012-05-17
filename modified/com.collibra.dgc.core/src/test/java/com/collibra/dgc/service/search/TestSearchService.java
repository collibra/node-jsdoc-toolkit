/**
 * 
 */
package com.collibra.dgc.service.search;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.component.VerbaliserComponent;
import com.collibra.dgc.core.dao.BinaryFactTypeFormDao;
import com.collibra.dgc.core.dao.CommunityDao;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.dao.TermDao;
import com.collibra.dgc.core.dao.VocabularyDao;
import com.collibra.dgc.core.dto.search.SearchResultItem;
import com.collibra.dgc.core.model.categorizations.CategorizationFactory;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.rules.RuleFactory;
import com.collibra.dgc.core.service.AttributeService;
import com.collibra.dgc.core.service.CategorizationService;
import com.collibra.dgc.core.service.CommunityService;
import com.collibra.dgc.core.service.DataConsistencyService;
import com.collibra.dgc.core.service.HistoryService;
import com.collibra.dgc.core.service.MatchingService;
import com.collibra.dgc.core.service.MeaningService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.RightsService;
import com.collibra.dgc.core.service.RulesService;
import com.collibra.dgc.core.service.SearchService;
import com.collibra.dgc.core.service.SearchService.EType;
import com.collibra.dgc.core.service.StatisticsService;
import com.collibra.dgc.core.service.SuggesterService;
import com.collibra.dgc.service.AbstractBootstrappedServiceTest;

/**
 * Tests for the {@link SearchService} implementation.
 * 
 * @author dieterwachters
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestSearchService extends AbstractBootstrappedServiceTest {
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestSearchService.class);
	private static Vocabulary vocabulary;

	@Before
	public void setUp() throws Exception {
		System.setProperty(SearchService.PROPERTY_FULL_REINDEX_SKIP, "false");
		System.setProperty(SearchService.PROPERTY_PAUSE_INDEXER, "false");
		searchService.doFullReIndex();
	}

	private final void createVocabulary() {
		Community com = communityService.createCommunity("Com1", "collibra.com/com1");
		resetTransaction();
		com = communityService.findCommunity(com.getId());
		vocabulary = representationFactory.makeVocabulary(com, "uri", "My Voc");
		representationService.saveVocabulary(vocabulary);
		resetTransaction();
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
	}

	@Test
	public void testSearchVocabularies() throws Exception {

		log.info("Starting testSearchVocabularies");
		Community com = communityService.createCommunity("Com1", "collibra.com/com1");
		resetTransaction();
		com = communityService.findCommunity(com.getId());
		Vocabulary voc1 = representationFactory.makeVocabulary(com, "collibra.com/defs", "Definitions");
		representationService.saveVocabulary(voc1);
		resetTransaction();
		com = communityService.findCommunity(com.getId());
		Vocabulary voc2 = representationFactory.makeVocabulary(com, "collibra.com/metadefs", "Meta Definitions");
		representationService.saveVocabulary(voc2);
		resetTransaction();

		voc1 = representationService.findVocabularyByResourceId(voc1.getId());
		voc2 = representationService.findVocabularyByResourceId(voc2.getId());

		List<SearchResultItem> found = searchService.search("Definitions", 10);
		assertEquals(2, found.size());
		assertEquals(EType.VC.name(), found.get(0).getType());
		assertEquals("Definitions", found.get(0).getName());
		assertEquals(EType.VC.name(), found.get(1).getType());
		assertEquals("Meta Definitions", found.get(1).getName());

		representationService.remove(voc1);

		found = searchService.search("Definitions", 10);
		assertEquals(1, found.size());
		assertEquals(EType.VC.name(), found.get(0).getType());
		assertEquals("Meta Definitions", found.get(0).getName());
		com = communityService.findCommunity(com.getId());
		voc1 = representationService.findVocabularyByResourceId(voc1.getId());
		representationService.changeName(voc2, "Meta data");

		found = searchService.search("Definitions", 10);
		assertEquals(0, found.size());
		found = searchService.search("data", 10);
		assertEquals(1, found.size());
		assertEquals("Meta data", found.get(0).getName());

		resetTransaction();

		// Adding terms
		addTermWithDescription("Test", "", voc2);
		addTermWithDescription("Another test", "", voc2);
		found = searchService.search("Test", 10);
		assertEquals(2, found.size());

		// Removing the vocabulary
		voc2 = representationService.findVocabularyByResourceId(voc2.getId());
		representationService.remove(voc2);

		// The containing terms should be gone too.
		found = searchService.search("Test", 10);
		assertEquals(0, found.size());

		com = communityService.findCommunity(com.getId());
		Vocabulary voc3 = representationFactory.makeVocabulary(com, "collibra.com/voc3", "Voc3");
		final Term v3t1 = representationFactory.makeTerm(voc3, "voc3term1");
		voc3.addTerm(v3t1);
		representationService.saveVocabulary(voc3);

		found = searchService.search("voc3term1", 10);
		assertEquals(1, found.size());
		assertEquals(EType.TE.name(), found.get(0).getType());
		assertEquals("voc3term1", found.get(0).getName());

		found = searchService.search("voc3", 10);
		assertEquals(1, found.size());

		// Testing with * wildcard
		found = searchService.search("voc3*", 10);
		assertEquals(2, found.size());

		// Testing with ? wildcard
		found = searchService.search("voc3term?", 10);
		assertEquals(1, found.size());

		log.info("Done testSearchVocabularies");
	}

	@Test
	public void testSearchTerms() throws Exception {
		createVocabulary();
		addTerm("Order");
		addTerm("Some order");
		addTerm("Something else");

		List<SearchResultItem> found = searchService.search("order", 1000);
		assertEquals(2, found.size());
		assertEquals(EType.TE.name(), found.get(0).getType());
		assertEquals("Order", found.get(0).getName());
		assertEquals(EType.VC.name(), found.get(0).getParent().getType());
		assertEquals("My Voc", found.get(0).getParent().getName());
		assertEquals("Com1", found.get(0).getParent().getParent().getName());
		assertEquals(EType.TE.name(), found.get(1).getType());
		assertEquals("Some order", found.get(1).getName());

		addTerm("Order something");

		found = searchService.search("order", 1000);
		assertEquals(3, found.size());
		found = searchService.search("something", 1000);
		assertEquals(2, found.size());

		found = searchService.search("Some order", 1000);
		assertEquals(3, found.size());
		assertEquals("Some order", found.get(0).getName());

		// Searching with quote in the middle (should get escaped)
		found = searchService.search("or\"der", 1000);
		assertEquals(0, found.size());

		// Testing searching between quotes (as 1)
		found = searchService.search("\"Some order\"", 1000);
		assertEquals(1, found.size());

		// Test deletion
		representationService.remove(representationService.findTermByResourceId(found.get(0).getResourceID()));
		found = searchService.search("order", 1000);
		assertEquals(2, found.size());
		assertEquals("Order", found.get(0).getName());
		assertEquals("Order something", found.get(1).getName());

		// Testing changing a term signifier
		Term term = representationService.findTermByResourceId(found.get(0).getResourceID());
		representationService.changeSignifier(term, "Bestelling");
		found = searchService.search("order", Arrays.asList(EType.TE), 1000);
		assertEquals(1, found.size());
		assertEquals("Order something", found.get(0).getName());

		found = searchService.search("bestelling", 1000);
		assertEquals(1, found.size());
		assertEquals("Bestelling", found.get(0).getName());

		resetTransaction();

		// We do a full re-index to see if everything is still working.
		searchService.doFullReIndex();

		found = searchService.search("bestelling", 1000);
		assertEquals(1, found.size());
		assertEquals("Bestelling", found.get(0).getName());
		assertEquals(EType.VC.name(), found.get(0).getParent().getType());
		assertEquals("My Voc", found.get(0).getParent().getName());
		assertEquals("Com1", found.get(0).getParent().getParent().getName());
	}

	// @Test
	// public void testStress() throws Exception {
	// final Community semcom = communityService.createCommunity("SemCom1",
	// "collibra.com/semcom1");
	// resetTransaction();
	// Community subcom = communityService.createCommunity(semcom, "SubCom1",
	// "collibra.com/subcom1");
	// resetTransaction();
	//
	// final String[] vocNames = new String[] { "Definitions", "Meta",
	// "Descriptions", "More", "Extra", "Special" };
	// final Vocabulary[] vocs = new Vocabulary[vocNames.length];
	// for (int i = 0; i < vocNames.length; i++) {
	// subcom = communityService.findCommunity(subcom.getResourceId());
	// vocs[i] = representationFactory.makeVocabulary(subcom, "collibra.com/" +
	// vocNames[i], vocNames[i]);
	// representationService.saveVocabulary(vocs[i]);
	// resetTransaction();
	// }
	//
	// final String[] termNames = new String[] { "Order", "Customer", "Item",
	// "Article", "OrderID", "CustomerID",
	// "ItemID", "ArticleID", "Store", "Support", "Case", "CaseID", "Issue",
	// "Bug", "New Feature", "Task",
	// "Meta", "Owner", "Developer", "Tester", "QA", "Assignee", "Description",
	// "Summary", "Fix Version",
	// "Affects Version", "Duplicate" };
	//
	// final long t1 = System.currentTimeMillis();
	// int duplicateCounter = 0;
	// final Random r = new Random();
	// for (int i = 0; i < 1000; i++) {
	// System.err.println("Adding " + i);
	// final int voc = r.nextInt(vocs.length);
	// final int term = r.nextInt(termNames.length);
	// if (termNames[term].equals("Duplicate")) {
	// duplicateCounter++;
	// }
	// addTermWithDescription(termNames[term] + " " + i,
	// "This is the description for " + termNames[term] + " "
	// + i, vocs[voc]);
	// }
	//
	// final long t2 = System.currentTimeMillis();
	// System.err.println("Adding 10000 terms took " + (t2 - t1) + "ms.");
	//
	// List<SearchResultItem> found = searchService.search("Duplicate", 100000);
	// final long t3 = System.currentTimeMillis();
	// System.err.println("Retrieving duplicates took " + (t3 - t2) + "ms.");
	// assertEquals(duplicateCounter * 2, found.size());
	//
	// found = searchService.search("Duplicate",
	// Collections.singleton(EType.TE), 100000);
	// final long t4 = System.currentTimeMillis();
	// System.err.println("Retrieving duplicate terms took " + (t4 - t3) +
	// "ms.");
	// assertEquals(duplicateCounter, found.size());
	// }

	@Test
	@Ignore
	public void testSearchAttributes() throws Exception {
		createVocabulary();
		final String orderDescription = "Represents what the customer orders. Consists of list of items.";
		addTermWithDescription("Order", orderDescription);
		addTermWithDescription("Customer", "The person buying our stuff.");
		final String itemDescription = "One item in an order. Has a count and price + test4 with number.";
		addTermWithDescription("Item", itemDescription);

		List<SearchResultItem> found = searchService.search("customer", 1000);
		// We find two things: the customer term itself and the description
		// attribute of the Order term.
		assertEquals(2, found.size());
		assertEquals("Customer", found.get(0).getName());
		assertEquals(orderDescription, found.get(1).getName());

		found = searchService.search("Order", 1000);
		Term term = representationService.findTermByResourceId(found.get(0).getResourceID());
		final String newOrderDescription = "New description for an order";
		attributeService.update(getDescriptionAttribute(term), newOrderDescription);
		resetTransaction();

		found = searchService.search("customer", 1000);
		// Now we only find one thing for 'customer', the term itself.
		assertEquals(1, found.size());
		assertEquals("Customer", found.get(0).getName());

		found = searchService.search("order", 1000);
		// Now we only find one thing for 'customer', the term itself.
		assertEquals(3, found.size());
		assertEquals("Order", found.get(0).getName());
		assertEquals(newOrderDescription, found.get(1).getName());
		assertEquals(itemDescription, found.get(2).getName());

		found = searchService.search("test4", 1000);
		assertEquals(1, found.size());

		// Testing wildcard
		found = searchService.search("test*", 1000);
		assertEquals(1, found.size());
		assertEquals(itemDescription, found.get(0).getName());

		// Testing special characters
		final String orderDescriptionWithSpecialChars = "New desc\\ription wi(th some special characters";
		attributeService.update(getDescriptionAttribute(term), orderDescriptionWithSpecialChars);
		resetTransaction();

		found = searchService.search("desc\\ription", 1000);
		assertEquals(orderDescriptionWithSpecialChars, found.get(0).getName());
		assertEquals(1, found.size());

		found = searchService.search("wi(th", 1000);
		assertEquals(orderDescriptionWithSpecialChars, found.get(0).getName());
		assertEquals(1, found.size());
		term = representationService.findTermByResourceId(term.getId());
		representationService.remove(term);
		found = searchService.search("desc\\ription", 1000);
		assertEquals(0, found.size());
		found = searchService.search("wi(th", 1000);
		assertEquals(0, found.size());

	}

	public void addTerm(String name) {
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		final Term term = representationFactory.makeTerm(vocabulary, name);
		representationService.saveTerm(term);
		resetTransaction();
	}

	public StringAttribute getDescriptionAttribute(final Term term) {
		List<StringAttribute> atts = attributeService.findDescriptionsForRepresentation(term);
		if (atts != null && !atts.isEmpty())
			return atts.get(0);

		return null;
	}

	public Term addTermWithDescription(String name, final String description, Vocabulary voc) {
		voc = representationService.findVocabularyByResourceId(voc.getId());
		final Term term = representationFactory.makeTerm(voc, name);
		term.addAttribute(representationFactory.makeStringAttribute(attributeService.findMetaDescription(), term,
				description));
		representationService.saveTerm(term);
		resetTransaction();
		return term;
	}

	public Term addTermWithDescription(String name, final String description) {
		return addTermWithDescription(name, description, vocabulary);
	}

	@Autowired
	protected VerbaliserComponent verbaliserComponent;

	// All the autowired services
	@Autowired
	protected CommunityService communityService;
	@Autowired
	protected AttributeService attributeService;
	@Autowired
	protected SearchService searchService;
	@Autowired
	protected RightsService rightsService;
	@Autowired
	protected MeaningService meaningService;
	@Autowired
	protected RepresentationService representationService;
	@Autowired
	protected StatisticsService statisticsService;
	@Autowired
	protected SuggesterService suggesterService;
	@Autowired
	protected RulesService rulesService;
	@Autowired
	protected MatchingService matchingService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected DataConsistencyService dataConsistencyService;
	@Autowired
	protected CategorizationService categorizationService;

	// All autowired DAOs
	@Autowired
	protected CommunityDao communityDao;
	@Autowired
	protected VocabularyDao vocabularyDao;
	@Autowired
	protected ObjectTypeDao objectTypeDao;
	@Autowired
	protected BinaryFactTypeFormDao binaryFactTypeFormDao;
	@Autowired
	protected TermDao termDao;

	// All autowired factories
	@Autowired
	protected CommunityFactory communityFactory;
	@Autowired
	protected MeaningFactory meaningFactory;
	@Autowired
	protected RuleFactory ruleFactory;
	@Autowired
	protected RepresentationFactory representationFactory;
	@Autowired
	protected CategorizationFactory categorizationFactory;
}