package com.collibra.dgc.api.representation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;

/**
 * {@link CharacteristicForm} API tests.
 * 
 * @author pmalarme
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCharacteristicFormApi extends AbstractDGCBootstrappedApiTest {

	@Test
	public void testAddCharacteristicForm() {

		Vocabulary voc = createVocabulary();

		CharacteristicForm cf = characteristicFormComponent.addCharacteristicForm(voc.getId().toString(),
				"Person", "has name");

		assertNotNull(cf);
		assertEquals("Person", cf.getTerm().getSignifier());
		assertEquals("has name", cf.getRole());
	}

	@Test
	public void testChangeCharacteristicForm() {

		CharacteristicForm cf = characteristicFormComponent.addCharacteristicForm(createVocabulary().getId()
				.toString(), "Human", "has name");

		CharacteristicForm changedCF = characteristicFormComponent.changeCharacteristicForm(cf.getId()
				.toString(), "Person", "has address");

		assertNotNull(changedCF);
		assertEquals("Person", changedCF.getTerm().getSignifier());
		assertEquals("has address", changedCF.getRole());
	}

	@Test
	public void testGetCharacteristicForm() {

		CharacteristicForm cf = characteristicFormComponent.addCharacteristicForm(createVocabulary().getId()
				.toString(), "Person", "has name");

		CharacteristicForm result = characteristicFormComponent.getCharacteristicForm(cf.getId().toString());

		assertNotNull(result);
		assertEquals(cf.getId(), result.getId());
	}

	@Test
	public void testGetCharacteristicFormsContainingTerm() {

		Vocabulary voc = createVocabulary();
		Term term = termComponent.addTerm(voc.getId().toString(), "Person");

		CharacteristicForm cf = characteristicFormComponent.addCharacteristicForm(voc.getId().toString(),
				term.getSignifier(), "has name");

		Collection<CharacteristicForm> results = characteristicFormComponent.getCharacteristicFormsContainingTerm(term
				.getId().toString());

		Assert.assertEquals(1, results.size());
		Assert.assertEquals(cf.getId(), results.iterator().next().getId());
	}
}
