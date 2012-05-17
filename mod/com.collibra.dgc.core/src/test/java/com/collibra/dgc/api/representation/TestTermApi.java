package com.collibra.dgc.api.representation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.component.representation.TermComponent;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * {@link TermComponent} API tests.
 * 
 * @author pmalarme
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestTermApi extends AbstractDGCBootstrappedApiTest {

	@Test
	public void testAddTerm() {

		Vocabulary voc = createVocabulary();
		Term term = termComponent.addTerm(voc.getId().toString(), "Country");

		Assert.assertNotNull(term);
		Assert.assertEquals("Country", term.getSignifier());
	}

	@Test
	public void testChangeSignifier() {

		Term term = termComponent.addTerm(createVocabulary().getId().toString(), "Term");

		Term result = termComponent.changeSignifier(term.getId().toString(), "New Term");

		Assert.assertNotNull(result);
		Assert.assertEquals("New Term", result.getSignifier());
	}

	@Test
	public void testFindTermsContainingSignifier() {

		Term term = termComponent.addTerm(createVocabulary().getId().toString(), "TermNameToSearch");

		Collection<Term> terms = termComponent.findTermsContainingSignifier("TermNameToSea", 0, 100);

		assertNotNull(terms);
		assertEquals(1, terms.size());
		assertEquals(term.getId(), terms.iterator().next().getId());
	}

	@Test
	public void testGetTerm() {

		Term country = termComponent.addTerm(createVocabulary().getId().toString(), "Country");

		Term result = termComponent.getTerm(country.getId().toString());

		Assert.assertNotNull(result);
		Assert.assertEquals(country.getId(), result.getId());
	}

	@Test
	public void testGetTermBySignifier() {

		Vocabulary voc = createVocabulary();
		Term country = termComponent.addTerm(voc.getId().toString(), "Country");

		Term result = termComponent.getTermBySignifier(voc.getId().toString(), country.getSignifier());

		Assert.assertNotNull(result);
		Assert.assertEquals(country.getId(), result.getId());
	}

	@Test
	public void testGetTermBySignifierInIncorporatedVocabularies() {

		Vocabulary voc = createVocabulary();
		Vocabulary incVoc = vocabularyComponent.addVocabulary(voc.getCommunity().getId().toString(),
				"Inc. Voc.", "inc.voc");
		vocabularyComponent
				.addIncorporatedVocabulary(voc.getId().toString(), incVoc.getId().toString());
		Term country = termComponent.addTerm(incVoc.getId().toString(), "Country");

		Term result = termComponent.getTermBySignifierInIncorporatedVocabularies(voc.getId().toString(),
				country.getSignifier());

		Assert.assertNotNull(result);
		Assert.assertEquals(country.getId(), result.getId());
	}

	@Test
	public void testRemoveTerms() {

		Vocabulary voc = createVocabulary();
		Term country = termComponent.addTerm(voc.getId().toString(), "Country");
		Term nation = termComponent.addTerm(voc.getId().toString(), "Nation");

		List<String> resourceIds = new ArrayList<String>(2);
		resourceIds.add(country.getId().toString());
		resourceIds.add(nation.getId().toString());

		termComponent.removeTerms(resourceIds);

		boolean doesntExist = false;

		try {
			termComponent.getTerm(nation.getId().toString());

		} catch (EntityNotFoundException ex) {

			doesntExist = true;
		}

		assertTrue(doesntExist);

		doesntExist = false;

		try {
			termComponent.getTerm(country.getId().toString());

		} catch (EntityNotFoundException ex) {

			doesntExist = true;
		}

		assertTrue(doesntExist);
	}
}
