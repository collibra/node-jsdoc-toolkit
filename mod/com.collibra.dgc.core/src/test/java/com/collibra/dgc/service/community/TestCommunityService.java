package com.collibra.dgc.service.community;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.rules.FrequencyRuleStatement;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.RuleStatement;
import com.collibra.dgc.core.service.CommunityService;
import com.collibra.dgc.service.AbstractBootstrappedServiceTest;

/**
 * JUnit test cases for {@link CommunityService}
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCommunityService extends AbstractBootstrappedServiceTest {
	private static final String SEM_COMM_1_NAME = "SemComm1";
	private static final String SEM_COMM_1_URI = "http://www.collibra.com/SemComm1";

	private static final String SUB_COMM_1_NAME = "SubComm1";
	private static final String SUB_COMM_1_URI = "http://www.collibra.com/SubComm1";

	private static final String SEM_COMM_2_NAME = "SemComm2";
	private static final String SEM_COMM_2_URI = "http://www.collibra.com/SemComm2";

	private static final String SUB_COMM_2_NAME = "SubComm2";
	private static final String SUB_COMM_2_URI = "http://www.collibra.com/SPComm2";

	@Test
	public void testCreateCommunity() throws Exception {
		createCommunity(SEM_COMM_1_NAME, SEM_COMM_1_URI);
		Community semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		assertEquals(semComm.getName(), SEM_COMM_1_NAME);
	}

	@Test
	public void testCreateCommunityUsingFactory() throws Exception {
		Community semComm = communityFactory.makeCommunity(SEM_COMM_1_NAME, SEM_COMM_1_URI);
		communityService.save(semComm);
		resetTransaction();
		semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		assertForCommunity(semComm, SEM_COMM_1_NAME, SEM_COMM_1_URI);
	}

	@Test
	public void testCreateCommunityDuplicate() throws Exception {
		createCommunity(SEM_COMM_1_NAME, SEM_COMM_1_URI);
		Community semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);

		try {
			createCommunity(SEM_COMM_1_NAME, SEM_COMM_1_URI);
			fail();
		} catch (IllegalArgumentException ex) {
			// Success;
		}
	}

	@Test
	public void testCreateSubCommunity() throws Exception {
		createCommunity(SEM_COMM_1_NAME, SEM_COMM_1_URI);
		Community semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);

		createCommunity(semComm, SUB_COMM_1_NAME, SUB_COMM_1_URI);

		Community spComm = communityService.findCommunityByUri(SUB_COMM_1_URI);
		assertEquals(spComm.getName(), SUB_COMM_1_NAME);
	}

	@Test
	public void testCreateSubCommunityWithFactory() throws Exception {
		createCommunity(SEM_COMM_1_NAME, SEM_COMM_1_URI);
		Community semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);

		Community spComm = communityFactory.makeCommunity(semComm, SUB_COMM_1_NAME, SUB_COMM_1_URI);
		communityService.save(spComm);
		resetTransaction();
		spComm = communityService.findCommunityByUri(SUB_COMM_1_URI);
		assertEquals(SUB_COMM_1_NAME, spComm.getName());
	}

	@Test
	public void testCreateSubCommunityDuplicate() throws Exception {
		createCommunity(SEM_COMM_1_NAME, SEM_COMM_1_URI);
		Community semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);

		createCommunity(semComm, SUB_COMM_1_NAME, SUB_COMM_1_URI);

		Community spComm = communityService.findCommunityByUri(SUB_COMM_1_URI);

		createCommunity(SEM_COMM_2_NAME, SEM_COMM_2_URI);
		semComm = communityService.findCommunityByUri(SEM_COMM_2_URI);

		try {
			createCommunity(semComm, SUB_COMM_1_NAME, SUB_COMM_1_URI);
			fail();
		} catch (IllegalArgumentException ex) {
			// Success
			rollback();
		}
	}

	@Test
	public void testCreateMultipleCommunitiesInOneTransaction() {
		Community semComm1 = communityFactory.makeCommunity(SEM_COMM_1_NAME, SEM_COMM_1_URI);
		assertNotNull(semComm1);

		Community semComm2 = communityFactory.makeCommunity(SEM_COMM_2_NAME, SEM_COMM_2_NAME);
		assertNotNull(semComm2);

		Community spComm1 = communityFactory.makeCommunity(semComm1, SUB_COMM_1_NAME, SUB_COMM_1_URI);
		assertNotNull(spComm1);

		Community spComm2 = communityFactory.makeCommunity(semComm2, SUB_COMM_2_NAME, SUB_COMM_2_NAME);
		assertNotNull(spComm2);

		communityService.save(semComm1);
		communityService.save(semComm2);
	}

	@Test
	public void testCreateFullHierarchy() {
		createHierarchy(SEM_COMM_1_NAME, SEM_COMM_1_URI, SUB_COMM_1_NAME, SUB_COMM_1_URI, SUB_COMM_2_NAME,
				SUB_COMM_2_URI);

		Community semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		assertNotNull(semComm);
		assertEquals(SEM_COMM_1_NAME, semComm.getName());

		Community spComm = communityService.findCommunityByUri(SUB_COMM_2_URI);
		assertEquals(SUB_COMM_2_NAME, spComm.getName());

		assertEquals(1, spComm.getVocabularies().size());
		Vocabulary vocabulary = null;
		for (Vocabulary voc : spComm.getVocabularies()) {
			vocabulary = voc;
			break;
		}

		assertNotNull(vocabulary);

		Vocabulary persisted = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertEquals(vocabulary, persisted);

		assertEquals(2, vocabulary.getTerms().size());
		assertEquals(1, vocabulary.getBinaryFactTypeForms().size());
		assertEquals(1, persisted.getRuleSets().size());

		RuleSet ruleSet = persisted.getRuleSets().iterator().next();
		assertEquals(1, ruleSet.getRuleStatements().size());

		RuleStatement persistedRS = ruleSet.getRuleStatements().iterator().next();
		assertEquals(1, persistedRS.getSimpleStatements().size());

		SimpleStatement persistedSS = persistedRS.getSimpleStatements().iterator().next();
		assertEquals(1, persistedSS.getReadingDirections().size());
		assertEquals(vocabulary.getBinaryFactTypeForms().iterator().next(), persistedSS.getReadingDirections()
				.iterator().next().getBinaryFactTypeForm());
	}

	@Test
	public void testRename() {
		createHierarchy(SEM_COMM_1_NAME, SEM_COMM_1_URI, SUB_COMM_1_NAME, SUB_COMM_1_URI, SUB_COMM_2_NAME,
				SUB_COMM_2_URI);

		Community semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		assertNotNull(semComm);
		assertEquals(SEM_COMM_1_NAME, semComm.getName());

		Community spComm = communityService.findCommunityByUri(SUB_COMM_1_URI);
		assertEquals(SUB_COMM_1_NAME, spComm.getName());

		assertEquals(0, spComm.getVocabularies().size());

		// Change the semantic community name.
		String CHANGED_SEM_COMM_NAME = "Changed Semantic Community 1 Name";
		communityService.changeName(semComm, CHANGED_SEM_COMM_NAME);
		resetTransaction();

		semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		assertNotNull(semComm);
		assertEquals(CHANGED_SEM_COMM_NAME, semComm.getName());

		spComm = communityService.findCommunityByUri(SUB_COMM_2_URI);
		assertEquals(SUB_COMM_2_NAME, spComm.getName());

		assertEquals(1, spComm.getVocabularies().size());
		Vocabulary vocabulary = spComm.getVocabularies().iterator().next();

		// Change the community name.
		String CHANGED_SP_COMM_NAME = "Changed Community 1 Name";
		spComm = communityService.findCommunityByUri(SUB_COMM_2_URI);
		communityService.changeName(spComm, CHANGED_SP_COMM_NAME);
		resetTransaction();

		semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		assertNotNull(semComm);
		assertEquals(CHANGED_SEM_COMM_NAME, semComm.getName());

		spComm = communityService.findCommunityByUri(SUB_COMM_2_URI);
		assertEquals(CHANGED_SP_COMM_NAME, spComm.getName());

		assertEquals(1, spComm.getVocabularies().size());

		// Change semantic community URI
		semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		String CHANGED_SEM_COMM_URI = "http://www.collibra.com/SemComm1Changed";
		semComm = communityService.changeURI(semComm, CHANGED_SEM_COMM_URI);
		resetTransaction();
		assertNotNull(semComm);
		assertEquals(CHANGED_SEM_COMM_NAME, semComm.getName());

		spComm = communityService.findCommunityByUri(SUB_COMM_2_URI);
		assertEquals(CHANGED_SP_COMM_NAME, spComm.getName());

		assertEquals(1, spComm.getVocabularies().size());
		vocabulary = spComm.getVocabularies().iterator().next();

		// Change community URI
		spComm = communityService.findCommunityByUri(SUB_COMM_2_URI);
		String CHANGED_SP_COMM_URI = "http://www.collibra.com/SPComm1Changed";
		spComm = communityService.changeURI(spComm, CHANGED_SP_COMM_URI);

		semComm = communityService.findCommunityByUri(CHANGED_SEM_COMM_URI);
		assertNotNull(semComm);
		assertEquals(CHANGED_SEM_COMM_NAME, semComm.getName());

		assertEquals(CHANGED_SP_COMM_NAME, spComm.getName());

		assertEquals(1, spComm.getVocabularies().size());
		vocabulary = spComm.getVocabularies().iterator().next();
	}

	@Test
	public void testDuplicateURI() {
		createHierarchy(SEM_COMM_1_NAME, SEM_COMM_1_URI, SUB_COMM_1_NAME, SUB_COMM_1_URI, SUB_COMM_2_NAME,
				SUB_COMM_2_URI);

		Community semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		assertNotNull(semComm);
		assertEquals(SEM_COMM_1_NAME, semComm.getName());

		Community spComm = communityService.findCommunityByUri(SUB_COMM_1_URI);
		assertEquals(SUB_COMM_1_NAME, spComm.getName());

		assertEquals(0, spComm.getVocabularies().size());

		// Try to set the URI of Semantic Community to URI of community.
		try {
			communityService.changeURI(semComm, SUB_COMM_1_URI);
			fail();
		} catch (IllegalArgumentException ex) {
			// Success
		}

		// Try to set the URI of Community to URI of semantic community.
		try {
			communityService.changeURI(spComm, SEM_COMM_1_URI);
			fail();
		} catch (IllegalArgumentException ex) {
			// Success
		}
	}

	@Test
	public void testRemoveOrDeleteCommunity() {
		createHierarchy(SEM_COMM_1_NAME, SEM_COMM_1_URI, SUB_COMM_1_NAME, SUB_COMM_1_URI, SUB_COMM_2_NAME,
				SUB_COMM_2_URI);

		Community semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		assertNotNull(semComm);
		assertEquals(SEM_COMM_1_NAME, semComm.getName());

		communityService.remove(semComm);

		resetTransaction();

		assertNull(communityDao.findByURI(SEM_COMM_1_URI));
		assertNull(communityDao.findByURI(SUB_COMM_1_URI));

		semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		assertNull(semComm);

		Community spComm = communityService.findCommunityByUri(SUB_COMM_1_URI);
		assertNull(spComm);
	}

	@Test
	public void testRemoveOrDeleteCommunityWithVersions() {
		createHierarchy(SEM_COMM_1_NAME, SEM_COMM_1_URI, SUB_COMM_1_NAME, SUB_COMM_1_URI, SUB_COMM_2_NAME,
				SUB_COMM_2_URI);

		Community semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		assertNotNull(semComm);
		assertEquals(SEM_COMM_1_NAME, semComm.getName());

		// Change the semantic community name.
		String CHANGED_SEM_COMM_NAME = "Changed Semantic Community 1 Name";
		semComm.setName(CHANGED_SEM_COMM_NAME);
		communityService.save(semComm);
		resetTransaction();

		semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		assertNotNull(semComm);
		assertEquals(CHANGED_SEM_COMM_NAME, semComm.getName());

		// Change semantic community URI
		semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		String CHANGED_SEM_COMM_URI = "http://www.collibra.com/SemComm1Changed";
		semComm = communityService.changeURI(semComm, CHANGED_SEM_COMM_URI);

		assertNotNull(semComm);
		assertEquals(CHANGED_SEM_COMM_NAME, semComm.getName());

		resetTransaction();

		semComm = communityService.findCommunityByUri(CHANGED_SEM_COMM_URI);
		communityService.remove(semComm);

		assertNull(communityDao.findById(semComm.getId()));
	}

	@Test
	public void testRemoveSubCommunity() {
		createHierarchy(SEM_COMM_1_NAME, SEM_COMM_1_URI, SUB_COMM_1_NAME, SUB_COMM_1_URI, SUB_COMM_2_NAME,
				SUB_COMM_2_URI);

		Community spComm = communityService.findCommunityByUri(SUB_COMM_1_URI);
		assertEquals(SUB_COMM_1_NAME, spComm.getName());

		// Change the sub community name.
		String CHANGED_SP_COMM_NAME = "Changed Community 1 Name";
		spComm = communityService.findCommunityByUri(SUB_COMM_1_URI);
		spComm.setName(CHANGED_SP_COMM_NAME);
		communityService.save(spComm);
		resetTransaction();

		spComm = communityService.findCommunityByUri(SUB_COMM_1_URI);
		assertEquals(CHANGED_SP_COMM_NAME, spComm.getName());

		// Change community URI
		spComm = communityService.findCommunityByUri(SUB_COMM_1_URI);
		String CHANGED_SP_COMM_URI = "http://www.collibra.com/SPComm1Changed";
		spComm = communityService.changeURI(spComm, CHANGED_SP_COMM_URI);

		assertEquals(CHANGED_SP_COMM_NAME, spComm.getName());

		// Remove the community.
		communityService.remove(spComm);

		resetTransaction();

		// Should not find the latest.
		Community spCommRemoved = communityService.findCommunity(spComm.getId());
		assertNull(spCommRemoved);
	}

	@Test
	public void testDeleteCommunity() {
		createHierarchy(SEM_COMM_1_NAME, SEM_COMM_1_URI, SUB_COMM_1_NAME, SUB_COMM_1_URI, SUB_COMM_2_NAME,
				SUB_COMM_2_URI);
		resetTransaction();

		Community parentComm = communityService.findCommunityByUri(SEM_COMM_1_URI);

		resetTransaction();
		parentComm = communityService.findCommunityByUri(SEM_COMM_1_URI);

		communityService.remove(parentComm);

		resetTransaction();

		// Should not find the latest.
		Community semantiCommunityRemoved = communityService.findCommunity(parentComm.getId());
		assertNull(semantiCommunityRemoved);
	}

	@Test
	public void testChangeParentCommunity() {
		createHierarchy(SEM_COMM_1_NAME, SEM_COMM_1_URI, SUB_COMM_1_NAME, SUB_COMM_1_URI, SUB_COMM_2_NAME,
				SUB_COMM_2_URI);

		Community semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		assertNotNull(semComm);
		assertEquals(SEM_COMM_1_NAME, semComm.getName());

		Community parentComm = communityService.createCommunity("Parent Comm", "Parent Comm URI");
		resetTransaction();

		parentComm = communityService.findCommunity(parentComm.getId());
		semComm = communityService.findCommunity(semComm.getId());

		communityService.changeParent(parentComm, semComm);

		resetTransaction();

		parentComm = communityService.findCommunity(parentComm.getId());
		semComm = communityService.findCommunity(semComm.getId());

		assertEquals(parentComm, semComm.getParentCommunity());
		assertTrue(parentComm.getSubCommunities().contains(semComm));
	}

	@Test
	public void testSearchCommunitiesByName() {
		createCommunity("SemComm1", "SemComm1 URI");
		Community semComm = communityService.findCommunityByUri("SemComm1 URI");
		createCommunity(semComm, "SubComm1", "SubComm1 URI");

		createCommunity("SemComm2", "SemComm2 URI");
		semComm = communityService.findCommunityByUri("SemComm2 URI");
		createCommunity(semComm, "SubComm2", "SubComm2 URI");

		createCommunity("SeCom", "SM URI");
		semComm = communityService.findCommunityByUri("SM URI");
		createCommunity(semComm, "SP", "SP URI");

		List<Community> results = communityService.searchCommunity("Se", 0, 100);
		assertEquals(3, results.size());

		results = communityService.searchCommunity("SemComm", 0, 100);
		assertEquals(2, results.size());

		results = communityService.searchCommunity("cm", 0, 100);
		assertEquals(0, results.size());

		results = communityService.searchCommunity("mCom", 0, 100);
		assertEquals(2, results.size());
	}

	@Test
	public void testFindAllCommunities() {
		createCommunity("SemComm1", "SemComm1 URI");
		Community semComm = communityService.findCommunityByUri("SemComm1 URI");
		createCommunity(semComm, "SubComm1", "SubComm1 URI");
		Community specComm1 = communityService.findCommunityByUri("SubComm1 URI");

		createCommunity("SemComm2", "SemComm2 URI");
		semComm = communityService.findCommunityByUri("SemComm2 URI");
		createCommunity(semComm, "SubComm2", "SubComm2 URI");
		Community specComm2 = communityService.findCommunityByUri("SubComm2 URI");

		createCommunity("SeCom", "SM URI");
		semComm = communityService.findCommunityByUri("SM URI");
		createCommunity(semComm, "SP", "SP URI");
		Community specComm3 = communityService.findCommunityByUri("SP URI");

		Collection<Community> results = communityService.findCommunities();
		assertEquals(6 + 3 /* 3 in Metamodel community */, results.size());
		assertTrue(results.contains(specComm1));
		assertTrue(results.contains(specComm2));
		assertTrue(results.contains(specComm3));

		results = communityService.findNonMetaCommunities();
		assertEquals(6, results.size());
		assertTrue(results.contains(specComm1));
		assertTrue(results.contains(specComm2));
		assertTrue(results.contains(specComm3));

		results = communityService.findNonSBVRCommunities();
		assertEquals(6 + 2 /* 2 in metamodel community */
		, results.size());
		assertTrue(results.contains(specComm1));
		assertTrue(results.contains(specComm2));
		assertTrue(results.contains(specComm3));
		resetTransaction();
		final Community collibraExt = communityService.findCommunityByUri(Constants.ADMIN_COMMUNITY_URI);
		specComm3 = communityService.findCommunity(specComm3.getId());
		communityService.changeParent(collibraExt, specComm3);
		resetTransaction();

		results = communityService.findCommunities();
		assertEquals(5 + 4 /* 4 in Metamodel community */, results.size());
		assertTrue(results.contains(specComm1));
		assertTrue(results.contains(specComm2));
		assertTrue(results.contains(specComm3));

		results = communityService.findNonMetaCommunities();
		assertEquals(5, results.size());
		assertTrue(results.contains(specComm1));
		assertTrue(results.contains(specComm2));
		assertFalse(results.contains(specComm3));

		results = communityService.findNonSBVRCommunities();
		assertEquals(5 + 3 /* 5 in Collibra meta community */
		, results.size());
		assertTrue(results.contains(specComm1));
		assertTrue(results.contains(specComm2));
		assertTrue(results.contains(specComm3));
	}

	@Test
	public void testFindCommunitiesByName() {
		createCommunity("SemComm1", "SemComm1 URI");
		Community semComm = communityService.findCommunityByUri("SemComm1 URI");
		createCommunity(semComm, "SubComm1", "SubComm1 URI");

		createCommunity("SemComm2", "SemComm2 URI");
		semComm = communityService.findCommunityByUri("SemComm2 URI");
		createCommunity(semComm, "SubComm2", "SubComm2 URI");

		Community reloadedSemComm = communityService.findCommunityByUri("SemComm1 URI");

		Community result = communityService.findCommunityByName("SemComm1");
		assertNotNull(result);
		assertEquals(reloadedSemComm.getId(), result.getId());
		assertEquals(reloadedSemComm.getName(), result.getName());
		assertEquals(reloadedSemComm.getUri(), result.getUri());
	}

	@Test
	public void testFindCommunityByUri() {
		createCommunity("SemComm1", "SemComm1 URI");
		Community semComm = communityService.findCommunityByUri("SemComm1 URI");
		createCommunity(semComm, "SubComm1", "SubComm1 URI");

		createCommunity("SemComm2", "SemComm2 URI");
		semComm = communityService.findCommunityByUri("SemComm2 URI");
		createCommunity(semComm, "SubComm2", "SubComm2 URI");

		Community result = communityService.findCommunityByUri("SubComm2 URI");
		assertNotNull(result);
		assertTrue(result.getUri().equals("SubComm2 URI"));
		assertFalse(result.getUri().equals("SemComm1 URI"));

	}

	@Test
	public void testFindLatestCommunityByUUID() {
		Community semComm = createCommunity("SemComm1", "SemComm1 URI");
		createCommunity(semComm, "SubComm1", "SubComm1 URI");
		semComm = communityService.findCommunity(semComm.getId());
		createCommunity(semComm, "SubComm2", "SubComm2 URI");
		semComm = communityService.findCommunity(semComm.getId());
		createCommunity(semComm, "SubComm3", "SubComm3 URI");

		resetTransaction();

		Community result = communityService.findCommunity(semComm.getId());
		assertNotNull(result);
		assertTrue(result.getUri().equals("SemComm1 URI"));
		assertTrue(result.getName().equals("SemComm1"));
	}

	@Test
	public void testFindCommunityByUUID() {
		createCommunity("SemComm1", "SemComm1 URI");
		Community semComm = communityService.findCommunityByUri("SemComm1 URI");
		Community spComm1 = createCommunity(semComm, "SubComm1", "SubComm1 URI");

		createCommunity("SemComm2", "SemComm2 URI");
		semComm = communityService.findCommunityByUri("SemComm2 URI");
		Community spComm2 = createCommunity(semComm, "SubComm2", "SubComm2 URI");

		Community result = communityService.findCommunity(spComm1.getId());
		assertNotNull(result);
		assertTrue(result.getUri().equals("SubComm1 URI"));
		assertTrue(result.getName().equals("SubComm1"));

		result = communityService.findCommunity(spComm2.getId());
		assertNotNull(result);
		assertTrue(result.getUri().equals("SubComm2 URI"));
		assertTrue(result.getName().equals("SubComm2"));

	}

	@Test
	public void testMultiLevelSubcommunitis() {
		Community sc1 = communityFactory.makeCommunity("SC1", "SC1 URI");
		communityService.save(sc1);
		resetTransaction();

		sc1 = communityService.findCommunity(sc1.getId());
		Community sc2 = communityFactory.makeCommunity("SC2", "SC2 URI");
		sc1.addSubCommunity(sc2);
		communityService.save(sc1);
		resetTransaction();

		sc2 = communityService.findCommunity(sc2.getId());
		assertNotNull(sc2);
		assertNotNull(sc2.getParentCommunity());
		assertEquals(sc1, sc2.getParentCommunity());

		Community sc3 = communityFactory.makeCommunity("SC3", "SC3 URI");
		sc2.addSubCommunity(sc3);
		communityService.save(sc2);
		resetTransaction();

		sc3 = communityService.findCommunity(sc3.getId());
		assertNotNull(sc3);
		assertNotNull(sc3.getParentCommunity());
		assertEquals(sc2, sc3.getParentCommunity());

		Community sc4 = communityFactory.makeCommunity("SC4", "SC4 URI");
		sc3.addSubCommunity(sc4);
		communityService.save(sc3);
		resetTransaction();

		sc4 = communityService.findCommunity(sc4.getId());
		assertNotNull(sc4);
		assertNotNull(sc4.getParentCommunity());
		assertEquals(sc3, sc4.getParentCommunity());
	}

	@Test
	public void testMoveCommunityWithoutVocabularies() {
		Community semComm1 = createCommunity("SemComm1", "SemComm1 URI");
		Community speComm1 = createCommunity(semComm1, "SubComm1", "SubComm1 URI");
		Community semComm2 = createCommunity("SemComm2", "SemComm2 URI");
		Community speComm2 = createCommunity(semComm2, "SubComm2", "SubComm2 URI");

		resetTransaction();

		semComm1 = communityService.findCommunity(semComm1.getId());
		semComm2 = communityService.findCommunity(semComm2.getId());
		speComm1 = communityService.findCommunity(speComm1.getId());
		speComm2 = communityService.findCommunity(speComm2.getId());

		assertEquals(null, semComm1.getParentCommunity());
		assertEquals(null, semComm2.getParentCommunity());

		semComm2.setParentCommunity(semComm1);

		communityService.save(semComm2);

		resetTransaction();

		semComm1 = communityService.findCommunity(semComm1.getId());
		semComm2 = communityService.findCommunity(semComm2.getId());
		speComm1 = communityService.findCommunity(speComm1.getId());
		speComm2 = communityService.findCommunity(speComm2.getId());

		assertEquals(null, semComm1.getParentCommunity());
		assertEquals(semComm1, semComm2.getParentCommunity());
	}

	@Test
	public void testChangeLanguage() {
		Community semComm = createCommunity("SemComm1", "SemComm1 URI");
		Community speComm = createCommunity(semComm, "SubComm1", "SubComm1 URI");
		resetTransaction();

		speComm = communityService.findCommunity(speComm.getId());
		assertNotNull(speComm.getLanguage());

		// change the language to French
		communityService.changeLanguage(speComm, "French");
		resetTransaction();

		speComm = communityService.findCommunity(speComm.getId());
		assertNotNull(speComm.getLanguage());
		assertEquals("French", speComm.getLanguage());
	}

	@Test
	public void renameCommunity() {
		Community sc = communityFactory.makeCommunity("SC1", "SC1URI");
		Community sp = communityFactory.makeCommunity(sc, "SP", "SPRUI");
		communityService.save(sc);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());
		assertNotNull(sp);

		String newName = "SP renamed";
		communityService.changeName(sp, newName);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());
		assertNotNull(sp);
		assertEquals(newName, sp.getName());
	}

	@Test
	public void testRemoveCommunity() {
		createHierarchy(SEM_COMM_1_NAME, SEM_COMM_1_URI, SUB_COMM_1_NAME, SUB_COMM_1_URI, SUB_COMM_2_NAME,
				SUB_COMM_2_URI);

		Community semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		assertNotNull(semComm);

		communityService.remove(semComm);
		resetTransaction();

		String resourceId = semComm.getId();
		semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);
		assertNull(semComm);
	}

	private Community createCommunity(String name, String uri) {
		communityService.createCommunity(name, uri);
		resetTransaction();

		Community semComm = communityService.findCommunityByUri(uri);
		assertForCommunity(semComm, name, uri);
		return semComm;
	}

	private Community createCommunity(Community parent, String name, String uri) {
		communityService.createCommunity(parent, name, uri);
		resetTransaction();

		Community spComm = communityService.findCommunityByUri(uri);
		assertForCommunity(spComm, name, uri);
		return spComm;
	}

	private void createHierarchy(String scName, String scURI, String subName, String subURI, String spName, String spURI) {
		Community semComm = communityFactory.makeCommunity(scName, scURI);
		assertNotNull(semComm);

		Community subComm = communityFactory.makeCommunity(subName, subURI);
		semComm.addSubCommunity(subComm);

		Community spComm = communityFactory.makeCommunity(semComm, spName, spURI);
		assertNotNull(spComm);

		// Create vocabulary
		Vocabulary vocabulary = representationFactory.makeVocabulary(spComm, "Communities Test URI", "Voc comm test");

		// Create terms
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");

		// Make BFTF
		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives",
				"driven by", car);

		FrequencyRuleStatement rs = ruleFactory.makeFrequencyRuleStatement(vocabulary);
		SimpleStatement ss = ruleFactory.makeSimpleStatement(vocabulary);
		ss.addReadingDirection(bftf.getRightPlaceHolder());
		rs.addSimpleStatement(ss);
		rs.setMax(1);
		rs.setMin(1);

		vocabulary.getRuleSets().iterator().next().addRuleStatement(rs);

		communityService.save(semComm);

		resetTransaction();
	}

	private void assertForCommunity(Community semComm, String name, String uri) {
		assertNotNull(semComm);
		assertEquals(uri, semComm.getUri());
		assertEquals(name, semComm.getName());
	}
}
