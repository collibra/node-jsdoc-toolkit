package com.collibra.dgc.api.vocabulary;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * Vocabulary Component API tests.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestVocabularyApi extends AbstractDGCBootstrappedApiTest {
	@Test
	public void testAddVocabulary() {
		Vocabulary voc = vocabularyComponent.addVocabulary(createCommunity().getId().toString(),
				VOCABULARY_NAME, VOCABULARY_URI);
		Assert.assertNotNull(voc);
	}

	@Test
	public void testGetVocabularies() {
		Community community = createCommunity();

		int count = vocabularyComponent.getVocabularies().size();
		Assert.assertTrue(count > 0);

		Vocabulary voc = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME,
				VOCABULARY_URI);
		Assert.assertNotNull(voc);
		Collection<Vocabulary> vocabularies = vocabularyComponent.getVocabularies();
		Assert.assertEquals(count + 1, vocabularies.size());
		assertVocabularyExists(vocabularies, VOCABULARY_NAME, VOCABULARY_URI);
	}

	@Test
	public void testGetVocabulariesToInCorporate() {
		Community community = createCommunity();
		Vocabulary voc = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME,
				VOCABULARY_URI);
		Collection<Vocabulary> vocabularies = vocabularyComponent.findPossibleVocabulariesToInCorporate(voc
				.getId().toString());
		Assert.assertEquals(0, vocabularies.size());

		Vocabulary voc2 = vocabularyComponent.addVocabulary(community.getId().toString(),
				VOCABULARY_NAME + "2", VOCABULARY_URI + "2");
		vocabularies = vocabularyComponent.findPossibleVocabulariesToInCorporate(voc.getId().toString());
		Assert.assertEquals(1, vocabularies.size());
		Assert.assertEquals(voc2.getUri(), vocabularies.iterator().next().getUri());
	}

	@Test
	public void testGetVocabulary() {
		Vocabulary voc = vocabularyComponent.addVocabulary(createCommunity().getId().toString(),
				VOCABULARY_NAME, VOCABULARY_URI);

		Vocabulary result = vocabularyComponent.getVocabulary(voc.getId().toString());
		Assert.assertEquals(voc.getUri(), result.getUri());
	}

	@Test
	public void testGetVocabularyByUri() {
		Vocabulary voc = vocabularyComponent.addVocabulary(createCommunity().getId().toString(),
				VOCABULARY_NAME, VOCABULARY_URI);

		Vocabulary result = vocabularyComponent.getVocabularyByUri(voc.getUri());
		Assert.assertEquals(voc.getId(), result.getId());
	}

	@Test
	public void testGetVocabularyByName() {
		Vocabulary voc = vocabularyComponent.addVocabulary(createCommunity().getId().toString(),
				VOCABULARY_NAME, VOCABULARY_URI);

		Vocabulary result = vocabularyComponent.getVocabularyByName(voc.getName());
		Assert.assertEquals(voc.getId(), result.getId());
	}

	@Test
	public void testRemove() {
		Vocabulary voc = vocabularyComponent.addVocabulary(createCommunity().getId().toString(),
				VOCABULARY_NAME, VOCABULARY_URI);
		voc = vocabularyComponent.getVocabulary(voc.getId().toString());
		Assert.assertNotNull(voc);

		vocabularyComponent.removeVocabulary(voc.getId().toString());

		boolean doesntExist = false;

		try {
			vocabularyComponent.getVocabulary(voc.getId().toString());
		} catch (EntityNotFoundException ex) {
			doesntExist = true;
		}

		Assert.assertTrue(doesntExist);
	}

	@Test
	public void testFindVocabulariesContainingName() {
		Community community = createCommunity();
		vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME, VOCABULARY_URI);
		vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME + "2", VOCABULARY_URI
				+ "2");
		vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME + "3", VOCABULARY_URI
				+ "3");
		vocabularyComponent.addVocabulary(community.getId().toString(), "Voc4", "Voc4 URI");

		Collection<Vocabulary> vocabularies = vocabularyComponent.findVocabulariesContainingName(VOCABULARY_NAME, 0,
				100);
		Assert.assertEquals(3, vocabularies.size());
		assertVocabularyExists(vocabularies, VOCABULARY_NAME, VOCABULARY_URI);
		assertVocabularyExists(vocabularies, VOCABULARY_NAME + "2", VOCABULARY_URI + "2");
		assertVocabularyExists(vocabularies, VOCABULARY_NAME + "3", VOCABULARY_URI + "3");
	}

	@Test
	public void testChangeUri() {
		Vocabulary voc = vocabularyComponent.addVocabulary(createCommunity().getId().toString(),
				VOCABULARY_NAME, VOCABULARY_URI);

		voc = vocabularyComponent.changeUri(voc.getId().toString(), VOCABULARY_URI + "NEW");
		Assert.assertNotNull(voc);
		Assert.assertEquals(voc.getUri(), VOCABULARY_URI + "NEW");
	}

	@Test
	public void testChangeCommunity() {
		final Community community1 = communityComponent.addCommunity("com1", "com.1");
		final Community community2 = communityComponent.addCommunity("com2", "com.2");
		final Vocabulary vocabulary = vocabularyComponent.addVocabulary(community1.getId().toString(),
				"testChangeCommunity", "com.collibra.ns");

		// Note: wrapping this in a transaction to be able to see changes when using oracle and able to retrieve lazy
		// stuff.
		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				Community c1 = communityComponent.getCommunity(community1.getId().toString());
				Community c2 = communityComponent.getCommunity(community2.getId().toString());

				Assert.assertNotNull(vocabulary.getCommunity());
				Assert.assertEquals(c1, vocabulary.getCommunity());
				Assert.assertTrue(c1.getVocabularies().contains(vocabulary));
				Assert.assertFalse(c2.getVocabularies().contains(vocabulary));

				Vocabulary voc = vocabularyComponent.changeCommunity(vocabulary.getId().toString(), community2
						.getId().toString());

				Assert.assertNotNull(voc.getCommunity());
				Assert.assertEquals(c2, voc.getCommunity());
				Assert.assertTrue(c2.getVocabularies().contains(voc));
				Assert.assertFalse(c1.getVocabularies().contains(voc));
			}
		});
	}

	@Test
	public void testGetPreferredTerm() {
		Community community = createCommunity();
		Vocabulary voc = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME,
				VOCABULARY_URI);
		Term term = termComponent.addTerm(voc.getId().toString(), "TERM");
		Assert.assertNotNull(term);

		Term prefTerm = vocabularyComponent.getPreferredTerm(voc.getId().toString(), term.getObjectType()
				.getId().toString());
		Assert.assertNotNull(prefTerm);
		Assert.assertEquals(term.getId(), prefTerm.getId());
	}

	@Test
	public void testGetPreferredTermInAllIncorporatedVocabularies() {
		Community community = createCommunity();
		Vocabulary voc = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME,
				VOCABULARY_URI);
		Term A = termComponent.addTerm(voc.getId().toString(), "A");

		Vocabulary voc2 = vocabularyComponent.addVocabulary(community.getId().toString(),
				VOCABULARY_NAME + "2", VOCABULARY_URI + "2");
		Term B = termComponent.addTerm(voc.getId().toString(), "B");
		vocabularyComponent.addIncorporatedVocabulary(voc2.getId().toString(), voc.getId().toString());

		Term result = vocabularyComponent.getPreferredTermInIncorporatedVocabularies(voc2.getId().toString(), A
				.getMeaning().getId().toString());
		Assert.assertNotNull(result);
		Assert.assertEquals(A.getId(), result.getId());

		result = vocabularyComponent.getPreferredTermInIncorporatedVocabularies(voc2.getId().toString(), B
				.getMeaning().getId().toString());
		Assert.assertNotNull(result);
		Assert.assertEquals(B.getId(), result.getId());
	}

	@Test
	public void testGetAllPreferredTerms() {
		Vocabulary voc = createVocabulary();

		Term A = termComponent.addTerm(voc.getId().toString(), "A");
		Term B = termComponent.addTerm(voc.getId().toString(), "B");

		Collection<Term> preferredTerms = vocabularyComponent.getPreferredTerms(voc.getId().toString());

		Assert.assertEquals(2, preferredTerms.size());
		Assert.assertTrue(preferredTerms.contains(A));
		Assert.assertTrue(preferredTerms.contains(B));
	}

	@Test
	public void testGetAllPreferredTermsInAllIncorporatedVocabularies() {
		Community community = createCommunity();
		Vocabulary voc = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME,
				VOCABULARY_URI);
		Term A = termComponent.addTerm(voc.getId().toString(), "A");
		Vocabulary voc2 = vocabularyComponent.addVocabulary(community.getId().toString(),
				VOCABULARY_NAME + "2", VOCABULARY_URI + "2");
		Term B = termComponent.addTerm(voc2.getId().toString(), "B");

		vocabularyComponent.addIncorporatedVocabulary(voc2.getId().toString(), voc.getId().toString());

		Collection<Term> preferredTerms = vocabularyComponent.getPreferredTermsInIncorporatedVocabularies(voc2
				.getId().toString());
		Assert.assertTrue(preferredTerms.contains(A));
		Assert.assertTrue(preferredTerms.contains(B));
	}

	@Test
	public void testGetPreferredRepresentation() {
		Vocabulary voc = createVocabulary();
		Term term = termComponent.addTerm(voc.getId().toString(), "TERM");
		Assert.assertNotNull(term);

		Representation preferred = vocabularyComponent.getPreferredRepresentation(voc.getId().toString(), term
				.getMeaning().getId().toString());
		Assert.assertNotNull(preferred);
		Assert.assertEquals(term.getId(), preferred.getId());
	}

	@Test
	public void testGetPreferredRepresentationInAllIncorporatedVocabularies() {
		Community community = createCommunity();
		Vocabulary voc = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME,
				VOCABULARY_URI);
		Term A = termComponent.addTerm(voc.getId().toString(), "A");

		Vocabulary voc2 = vocabularyComponent.addVocabulary(community.getId().toString(),
				VOCABULARY_NAME + "2", VOCABULARY_URI + "2");
		Term B = termComponent.addTerm(voc.getId().toString(), "B");
		vocabularyComponent.addIncorporatedVocabulary(voc2.getId().toString(), voc.getId().toString());

		Representation result = vocabularyComponent.getPreferredRepresentationInIncorporatedVocabularies(voc2
				.getId().toString(), A.getMeaning().getId().toString());
		Assert.assertNotNull(result);
		Assert.assertEquals(A.getId(), result.getId());

		result = vocabularyComponent.getPreferredRepresentationInIncorporatedVocabularies(voc2.getId()
				.toString(), B.getMeaning().getId().toString());
		Assert.assertNotNull(result);
		Assert.assertEquals(B.getId(), result.getId());
	}

	@Test
	public void testGetAllIncorporatingVocabularies() {
		Community community = createCommunity();
		Vocabulary voc = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME,
				VOCABULARY_URI);

		Collection<Vocabulary> vocabularies = vocabularyComponent.getIncorporatingVocabularies(voc.getId()
				.toString());
		Assert.assertEquals(0, vocabularies.size());

		Vocabulary voc2 = vocabularyComponent.addVocabulary(community.getId().toString(),
				VOCABULARY_NAME + "2", VOCABULARY_URI + "2");
		Vocabulary result = vocabularyComponent.addIncorporatedVocabulary(voc2.getId().toString(), voc
				.getId().toString());
		Assert.assertNotNull(result);

		vocabularies = vocabularyComponent.getIncorporatingVocabularies(voc.getId().toString());
		Assert.assertEquals(1, vocabularies.size());
		Assert.assertEquals(voc2.getId(), vocabularies.iterator().next().getId());
	}

	@Test
	public void testGetAllIncorporatedVocabularies() {
		Community community = createCommunity();
		Vocabulary voc = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME,
				VOCABULARY_URI);

		Collection<Vocabulary> vocabularies = vocabularyComponent.getIncorporatingVocabularies(voc.getId()
				.toString());
		Assert.assertEquals(0, vocabularies.size());

		Vocabulary voc2 = vocabularyComponent.addVocabulary(community.getId().toString(),
				VOCABULARY_NAME + "2", VOCABULARY_URI + "2");
		Vocabulary result = vocabularyComponent.addIncorporatedVocabulary(voc2.getId().toString(), voc
				.getId().toString());
		Assert.assertNotNull(result);

		vocabularies = vocabularyComponent.getIncorporatedVocabularies(voc2.getId().toString(), true);
		Assert.assertEquals(1, vocabularies.size());
		Assert.assertEquals(voc.getId(), vocabularies.iterator().next().getId());
	}

	@Test
	public void testIncorporateVocabulary() {
		Community community = createCommunity();
		Vocabulary voc = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME,
				VOCABULARY_URI);

		Vocabulary voc2 = vocabularyComponent.addVocabulary(community.getId().toString(),
				VOCABULARY_NAME + "2", VOCABULARY_URI + "2");
		Vocabulary result = vocabularyComponent.addIncorporatedVocabulary(voc2.getId().toString(), voc
				.getId().toString());
		Assert.assertNotNull(result);

		Collection<Vocabulary> vocabularies = vocabularyComponent.getIncorporatingVocabularies(voc.getId()
				.toString());
		Assert.assertEquals(1, vocabularies.size());
		Assert.assertEquals(voc2.getId(), vocabularies.iterator().next().getId());
	}

	@Test
	public void testDisincorporateVocabulary() {
		Community community = createCommunity();
		Vocabulary voc = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME,
				VOCABULARY_URI);

		Vocabulary voc2 = vocabularyComponent.addVocabulary(community.getId().toString(),
				VOCABULARY_NAME + "2", VOCABULARY_URI + "2");
		Vocabulary result = vocabularyComponent.addIncorporatedVocabulary(voc2.getId().toString(), voc
				.getId().toString());
		Assert.assertNotNull(result);

		Collection<Vocabulary> vocabularies = vocabularyComponent.getIncorporatingVocabularies(voc.getId()
				.toString());
		Assert.assertEquals(1, vocabularies.size());

		Vocabulary resultVoc = vocabularyComponent.removeIncorporatedVocabulary(voc2.getId().toString(), voc
				.getId().toString());
		Assert.assertNotNull(resultVoc);

		vocabularies = vocabularyComponent.getIncorporatedVocabularies(resultVoc.getId().toString(), true);
		Assert.assertEquals(0, vocabularies.size());
	}

	@Test
	public void testChangeName() {
		Vocabulary voc = vocabularyComponent.addVocabulary(createCommunity().getId().toString(),
				VOCABULARY_NAME, VOCABULARY_URI);
		Vocabulary result = vocabularyComponent.changeName(voc.getId().toString(), VOCABULARY_NAME + "NEW");
		Assert.assertNotNull(result);

		voc = vocabularyComponent.getVocabulary(voc.getId().toString());
		Assert.assertEquals(VOCABULARY_NAME + "NEW", voc.getName());
	}

	private void assertVocabularyExists(Collection<Vocabulary> vocabularies, String name, String uri) {
		for (Vocabulary voc : vocabularies) {
			if (voc.getName().equals(name) && voc.getUri().equals(voc.getUri())) {
				return;
			}
		}

		Assert.fail("Vocabulary with name '" + name + "' and URI '" + uri + "' not found");
	}
}
