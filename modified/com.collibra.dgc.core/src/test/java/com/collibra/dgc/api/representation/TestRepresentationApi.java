package com.collibra.dgc.api.representation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.component.representation.RepresentationComponent;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * {@link RepresentationComponent} API tests.
 * 
 * @author pmalarme
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestRepresentationApi extends AbstractDGCBootstrappedApiTest {

	@Test
	public void testGetAddAndRemoveSynonym() {

		Vocabulary voc = createVocabulary();
		Term country = termComponent.addTerm(voc.getId().toString(), "Country");
		Term nation = termComponent.addTerm(voc.getId().toString(), "Nation");

		Representation synonym = representationComponent.addSynonym(nation.getId().toString(), country.getId()
				.toString());
		assertNotNull(synonym);

		Collection<Representation> synonyms = representationComponent.getSynonyms(country.getId().toString());
		assertEquals(1, synonyms.size());
		assertEquals(nation.getId(), synonyms.iterator().next().getId());

		nation = (Term) representationComponent.removeSynonym(nation.getId().toString());
		assertEquals(1, nation.getMeaning().getRepresentations().size());

		synonyms = representationComponent.getSynonyms(country.getId().toString());
		assertEquals(0, synonyms.size());
	}

	@Test
	public void testGetAndChangeConceptType() {

		Vocabulary voc = createVocabulary();
		Term term = termComponent.addTerm(voc.getId().toString(), "Term");
		Term typeTerm = termComponent.addTerm(voc.getId().toString(), "TypeTerm");

		Representation result = representationComponent.changeConceptType(term.getId().toString(), typeTerm.getId()
				.toString());
		assertNotNull(result);

		Term resultTypeTerm = representationComponent.getConceptType(term.getId().toString());
		assertEquals(typeTerm.getId(), resultTypeTerm.getId());
		assertEquals(term.getId(), result.getId());
	}

	@Test
	public void testGetChangeAndRemoveGeneralConcept() {

		Vocabulary voc = createVocabulary();
		Term parent = termComponent.addTerm(voc.getId().toString(), "Parent");
		Term child = termComponent.addTerm(voc.getId().toString(), "Child");

		Representation result = representationComponent.changeGeneralConcept(child.getId().toString(), parent.getId()
				.toString());
		assertNotNull(result);

		Representation generalRepresentation = representationComponent.getGeneralConcept(child.getId().toString());
		assertEquals(parent.getId(), generalRepresentation.getId());

		child = (Term) representationComponent.removeGeneralConcept(child.getId().toString());
		generalRepresentation = representationComponent.getGeneralConcept(child.getId().toString());
		assertEquals(MeaningConstants.META_THING_UUID, generalRepresentation.getMeaning().getId());
	}

	@Test
	public void testChangeStatus() {

		Vocabulary vocabulary = createVocabulary();
		Term term = termComponent.addTerm(vocabulary.getId().toString(), "Test Term");
		assertNotNull(term.getStatus());

		// Get the status term for 'Candidate'
		Term candidateStatus = termComponent.getTermBySignifier(
				vocabularyComponent.getVocabularyByUri("http://www.collibra.com/glossary/statuses").getId().toString(),
				"Rejected");
		assertNotNull(candidateStatus);

		// Update the status of the term
		term = (Term) representationComponent.changeStatus(term.getId().toString(), candidateStatus.getId().toString());
		assertNotNull(term.getStatus());
		assertEquals(candidateStatus.getId(), term.getStatus().getId());
	}

	@Test
	public void testGetRepresentation() {

		Term A = termComponent.addTerm(createVocabulary().getId().toString(), "A");

		assertNotNull(representationComponent.getRepresentation(A.getId().toString()));
	}

	@Test
	public void testGetSpecializedConcepts() {

		Vocabulary voc = createVocabulary();
		Term parent = termComponent.addTerm(voc.getId().toString(), "Parent");
		Term child = termComponent.addTerm(voc.getId().toString(), "Child");

		Representation result = representationComponent.changeGeneralConcept(child.getId().toString(), parent.getId()
				.toString());
		assertNotNull(result);

		Collection<Representation> specialized = representationComponent.getSpecializedConcepts(parent.getId()
				.toString(), 10);
		assertEquals(1, specialized.size());
		assertEquals(child.getId(), specialized.iterator().next().getId());
	}

	@Test
	public void testRemove() {

		Term country = termComponent.addTerm(createVocabulary().getId().toString(), "Country");
		String countryRId = country.getId().toString();

		representationComponent.remove(countryRId);

		boolean doesntExist = false;

		try {
			representationComponent.getRepresentation(countryRId);

		} catch (EntityNotFoundException ex) {

			doesntExist = true;
		}

		assertTrue(doesntExist);
	}

	@Test
	public void testRemoveGeneralConcept() {

		Vocabulary voc = createVocabulary();
		Term parent = termComponent.addTerm(voc.getId().toString(), "Parent");
		Term child = termComponent.addTerm(voc.getId().toString(), "Child");

		Representation result = representationComponent.changeGeneralConcept(child.getId().toString(), parent.getId()
				.toString());
		Assert.assertNotNull(result);

		Representation resultParent = representationComponent.getGeneralConcept(child.getId().toString());
		Assert.assertNotNull(resultParent);
		Assert.assertEquals(parent.getId(), resultParent.getId());
	}
}
