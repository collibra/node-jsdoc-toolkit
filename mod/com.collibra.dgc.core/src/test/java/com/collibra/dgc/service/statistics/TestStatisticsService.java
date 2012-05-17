package com.collibra.dgc.service.statistics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.dto.stats.RepresentationStatsEntry;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.service.AbstractServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestStatisticsService extends AbstractServiceTest {

	private static final String USER_1 = "User1";
	private static final String USER_2 = "User2";
	private static final String USER_3 = "User3";

	@Test
	public void testFindBinaryFactTypeFormsPerTerm() {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term Human = representationFactory.makeTerm(vocabulary, "Human");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", Human);

		Community sp2 = communityFactory.makeCommunity("Community 2", "Community2");
		Vocabulary vocabulary2 = representationFactory
				.makeVocabulary(sp, "com.namespace.ns2", "vocaulary.create.term2");
		vocabulary2.addIncorporatedVocabulary(vocabulary);
		BinaryFactTypeForm bftf2 = representationFactory.makeBinaryFactTypeForm(vocabulary2, person, "owns",
				"owned by", car);

		communityService.save(sp);
		communityService.save(sp2);
		resetTransaction();

		Map<String, Long> result = binaryFactTypeFormDao.findNumberOfBinaryFactTypesPerTerm();
		for (java.util.Map.Entry<String, Long> entry : result.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		assertEquals(3, result.get("Person").longValue());
		assertEquals(1, result.get("Human").longValue());
		assertEquals(2, result.get("Car").longValue());
	}

	@Test
	public void testFindNumberOfBinaryFactTypesPerTermVocabulary() {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term Human = representationFactory.makeTerm(vocabulary, "Human");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", Human);

		Community sp2 = communityFactory.makeCommunity("Community 2", "Community2");
		Vocabulary vocabulary2 = representationFactory
				.makeVocabulary(sp, "com.namespace.ns2", "vocaulary.create.term2");
		vocabulary2.addIncorporatedVocabulary(vocabulary);
		BinaryFactTypeForm bftf2 = representationFactory.makeBinaryFactTypeForm(vocabulary2, person, "owns",
				"owned by", car);

		communityService.save(sp);
		communityService.save(sp2);
		resetTransaction();

		Map<String, Long> result = binaryFactTypeFormDao.findNumberOfBinaryFactTypesPerTerm(vocabulary);
		for (java.util.Map.Entry<String, Long> entry : result.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		assertEquals(3, result.get("Person").longValue());
		assertEquals(1, result.get("Human").longValue());
		assertEquals(2, result.get("Car").longValue());
	}

	@Test
	public void testFindNumberOfBinaryFactTypesPerTerm() {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term Human = representationFactory.makeTerm(vocabulary, "Human");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", Human);

		Community sp2 = communityFactory.makeCommunity("Community 2", "Community2");
		Vocabulary vocabulary2 = representationFactory
				.makeVocabulary(sp, "com.namespace.ns2", "vocaulary.create.term2");
		vocabulary2.addIncorporatedVocabulary(vocabulary);
		BinaryFactTypeForm bftf2 = representationFactory.makeBinaryFactTypeForm(vocabulary2, person, "owns",
				"owned by", car);

		communityService.save(sp);
		communityService.save(sp2);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());
		Map<String, Long> result = binaryFactTypeFormDao.findNumberOfBinaryFactTypesPerTerm(sp);
		for (java.util.Map.Entry<String, Long> entry : result.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		assertEquals(Long.valueOf(3L), result.get("Person"));
		assertEquals(Long.valueOf(1L), result.get("Human"));
		assertEquals(Long.valueOf(2L), result.get("Car"));
	}

	@Test
	public void testFindNumberOfBinaryFactTypesPerTermSemanticCommunity() {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term Human = representationFactory.makeTerm(vocabulary, "Human");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", Human);

		Community sp2 = communityFactory.makeCommunity("Community 2", "Community2");
		Vocabulary vocabulary2 = representationFactory
				.makeVocabulary(sp, "com.namespace.ns2", "vocaulary.create.term2");
		vocabulary2.addIncorporatedVocabulary(vocabulary);
		BinaryFactTypeForm bftf2 = representationFactory.makeBinaryFactTypeForm(vocabulary2, person, "owns",
				"owned by", car);

		communityService.save(sp);
		communityService.save(sp2);
		resetTransaction();

		Map<String, Long> result = binaryFactTypeFormDao.findNumberOfBinaryFactTypesPerTerm();
		for (java.util.Map.Entry<String, Long> entry : result.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		assertEquals(3, result.get("Person").longValue());
		assertEquals(1, result.get("Human").longValue());
		assertEquals(2, result.get("Car").longValue());
	}

	@Test
	public void testTermsWithStakeholderCount() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "stake holder count stat uti",
				"stakeholder count stat");
		Term t1 = representationFactory.makeTerm(vocabulary, "T1");
		Term t2 = representationFactory.makeTerm(vocabulary, "T2");
		Term t3 = representationFactory.makeTerm(vocabulary, "T3");
		communityService.save(sp);

		resetTransaction();

		UserData u1 = userService.addUser(USER_1, USER_1);
		UserData u2 = userService.addUser(USER_2, USER_2);
		UserData u3 = userService.addUser(USER_3, USER_3);

		resetTransaction();

		t1 = representationService.findTermByResourceId(t1.getId());
		t2 = representationService.findTermByResourceId(t2.getId());
		t3 = representationService.findTermByResourceId(t3.getId());

		rightsService.addMember(u1.getId(), Constants.STAKEHOLDER, t1);
		rightsService.addMember(u2.getId(), Constants.STAKEHOLDER, t1);

		rightsService.addMember(u1.getId(), Constants.STAKEHOLDER, t2);
		rightsService.addMember(u2.getId(), Constants.STAKEHOLDER, t2);
		rightsService.addMember(u3.getId(), Constants.STAKEHOLDER, t2);

		rightsService.addMember(u1.getId(), Constants.STAKEHOLDER, t3);

		resetTransaction();

		t1 = representationService.findTermByResourceId(t1.getId());
		t2 = representationService.findTermByResourceId(t2.getId());
		t3 = representationService.findTermByResourceId(t3.getId());
		Map<Representation, Integer> expectedCount = new HashMap<Representation, Integer>();
		expectedCount.put(t1, 2);
		expectedCount.put(t2, 3);
		expectedCount.put(t3, 1);

		List<RepresentationStatsEntry> list = statisticsService.findTermsWithMemberCount(Constants.STAKEHOLDER, true);
		assertCount(list, expectedCount, true);
		resetTransaction();

		// Create another vocabulary and assert for all members.
		sp = communityService.findCommunity(sp.getId());
		vocabulary = representationFactory.makeVocabulary(sp, "stat uri2", "stats voc2");
		Term t4 = representationFactory.makeTerm(vocabulary, "T1");
		Term t5 = representationFactory.makeTerm(vocabulary, "T2");
		communityService.save(sp);

		rightsService.addMember(u1.getId(), Constants.STAKEHOLDER, t4);
		rightsService.addMember(u2.getId(), Constants.ADMIN, t4);

		rightsService.addMember(u1.getId(), Constants.STAKEHOLDER, t5);
		rightsService.addMember(u2.getId(), Constants.STEWARD, t5);
		rightsService.addMember(u3.getId(), Constants.NORMAL, t5);

		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		t4 = representationService.findTermByResourceId(t4.getId());
		t5 = representationService.findTermByResourceId(t5.getId());
		expectedCount = new HashMap<Representation, Integer>();
		expectedCount.put(t4, 2);
		expectedCount.put(t5, 3);

		list = statisticsService.findTermsWithMemberCount(vocabulary, true);
		assertCount(list, expectedCount, true);
		resetTransaction();

		// Now assert for all terms in the glossary and for all members.
		t1 = representationService.findTermByResourceId(t1.getId());
		t2 = representationService.findTermByResourceId(t2.getId());
		t3 = representationService.findTermByResourceId(t3.getId());
		t4 = representationService.findTermByResourceId(t4.getId());
		t5 = representationService.findTermByResourceId(t5.getId());
		expectedCount = new HashMap<Representation, Integer>();
		expectedCount.put(t1, 2);
		expectedCount.put(t2, 3);
		expectedCount.put(t3, 1);
		expectedCount.put(t4, 2);
		expectedCount.put(t5, 3);

		list = statisticsService.findTermsWithMemberCount(false);
		assertCount(list, expectedCount, false);
		resetTransaction();

		// Now assert for all terms in the glossary and for stakeholders.
		t1 = representationService.findTermByResourceId(t1.getId());
		t2 = representationService.findTermByResourceId(t2.getId());
		t3 = representationService.findTermByResourceId(t3.getId());
		t4 = representationService.findTermByResourceId(t4.getId());
		t5 = representationService.findTermByResourceId(t5.getId());
		expectedCount = new HashMap<Representation, Integer>();
		expectedCount.put(t1, 2);
		expectedCount.put(t2, 3);
		expectedCount.put(t3, 1);
		expectedCount.put(t4, 1);
		expectedCount.put(t5, 1);

		list = statisticsService.findTermsWithMemberCount(Constants.STAKEHOLDER, false);
		assertCount(list, expectedCount, false);
		resetTransaction();
	}

	@Test
	public void testFindNumberOfSpecializedConceptsPerTerm() {
		List<Object[]> tuples = statisticsService.findNumberOfSpecializedConceptsPerTerm(representationService
				.findSbvrMeaningAndRepresentationVocabulary());
		for (Object[] tuple : tuples) {
			System.out.print(tuple[0]);
			System.out.println(": " + tuple[1]);
		}
		assertNotNull(tuples);
		assertFalse(tuples.isEmpty());
	}

	private void assertCount(List<RepresentationStatsEntry> list, Map<Representation, Integer> expectedCount,
			boolean asc) {
		Assert.assertEquals(expectedCount.size(), list.size());
		RepresentationStatsEntry previous = null;
		for (RepresentationStatsEntry result : list) {
			int count = expectedCount.get(result.getRepresentation());
			Assert.assertEquals(count, result.getCount());

			if (previous != null) {
				if (asc) {
					Assert.assertTrue(previous.getCount() <= result.getCount());
				} else {
					Assert.assertTrue(previous.getCount() >= result.getCount());
				}
			}

			previous = result;
		}
	}
}
