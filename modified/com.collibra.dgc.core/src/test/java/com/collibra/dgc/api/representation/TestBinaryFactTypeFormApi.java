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
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;

/**
 * {@link BinaryFactTypeForm} API tests.
 * 
 * @author pmalarme
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestBinaryFactTypeFormApi extends AbstractDGCBootstrappedApiTest {

	@Test
	public void testAddBinaryFactTypeForm() {

		Vocabulary voc = createVocabulary();

		BinaryFactTypeForm bftf = binaryFactTypeFormComponent.addBinaryFactTypeForm(voc.getId().toString(),
				"Car", "driven by", "drives", "Person");

		assertNotNull(bftf);
		assertEquals("Car", bftf.getHeadTerm().getSignifier());
		assertEquals("driven by", bftf.getRole());
		assertEquals("drives", bftf.getCoRole());
		assertEquals("Person", bftf.getTailTerm().getSignifier());
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTerms() {

		Vocabulary voc = createVocabulary();
		Term head = termComponent.addTerm(voc.getId().toString(), "Head");
		Term tail = termComponent.addTerm(voc.getId().toString(), "Tail");

		BinaryFactTypeForm bftf = binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTerms(voc.getId()
				.toString(), head.getId().toString(), "role", "corole", tail.getId().toString());

		assertNotNull(bftf);
		assertEquals(head.getId(), bftf.getHeadTerm().getId());
		assertEquals(tail.getId(), bftf.getTailTerm().getId());
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingHeadTerm() {

		Vocabulary voc = createVocabulary();
		Term head = termComponent.addTerm(voc.getId().toString(), "Head");

		BinaryFactTypeForm bftf = binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingHeadTerm(voc
				.getId().toString(), head.getId().toString(), "role", "corole", "Tail");

		assertNotNull(bftf);
		assertEquals(head.getId(), bftf.getHeadTerm().getId());
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTailTerm() {

		Vocabulary voc = createVocabulary();
		Term tail = termComponent.addTerm(voc.getId().toString(), "Tail");

		BinaryFactTypeForm bftf = binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTailTerm(voc
				.getId().toString(), "Head", "role", "corole", tail.getId().toString());

		assertNotNull(bftf);
		assertEquals(tail.getId(), bftf.getTailTerm().getId());
	}

	@Test
	public void testChangeBinaryFactTypeForm() {

		BinaryFactTypeForm bftf = binaryFactTypeFormComponent.addBinaryFactTypeForm(createVocabulary().getId()
				.toString(), "Automobile", "conduct by", "conducts", "Person");
		assertNotNull(bftf);

		BinaryFactTypeForm changedBftf = binaryFactTypeFormComponent.changeBinaryFactTypeForm(bftf.getId()
				.toString(), "Car", "driven by", "drives", "Driver");

		assertNotNull(changedBftf);
		assertEquals("Car", changedBftf.getHeadTerm().getSignifier());
		assertEquals("driven by", changedBftf.getRole());
		assertEquals("drives", changedBftf.getCoRole());
		assertEquals("Driver", changedBftf.getTailTerm().getSignifier());
	}

	@Test
	public void testGetBinaryFactTypeForm() {

		BinaryFactTypeForm bftf = binaryFactTypeFormComponent.addBinaryFactTypeForm(createVocabulary().getId()
				.toString(), "Car", "driven by", "drives", "Person");

		BinaryFactTypeForm result = binaryFactTypeFormComponent.getBinaryFactTypeForm(bftf.getId().toString());

		assertNotNull(result);
		assertEquals(bftf.getId(), result.getId());
	}

	@Test
	public void testGetBinaryFactTypeFormsContainingHeadTerm() {

		Vocabulary voc = createVocabulary();
		Term car = termComponent.addTerm(voc.getId().toString(), "Car");

		BinaryFactTypeForm bftf = binaryFactTypeFormComponent.addBinaryFactTypeForm(voc.getId().toString(),
				car.getSignifier(), "driven by", "drives", "Person");

		Collection<BinaryFactTypeForm> bftfs = binaryFactTypeFormComponent.getBinaryFactTypeFormsContainingHeadTerm(car
				.getId().toString());

		Assert.assertEquals(1, bftfs.size());
		Assert.assertEquals(bftf.getId(), bftfs.iterator().next().getId());
	}

	@Test
	public void testGetBinaryFactTypeFormsContainingTailTerm() {

		Vocabulary voc = createVocabulary();
		Term person = termComponent.addTerm(voc.getId().toString(), "Person");

		BinaryFactTypeForm bftf = binaryFactTypeFormComponent.addBinaryFactTypeForm(voc.getId().toString(),
				"Car", "driven by", "drives", person.getSignifier());

		Collection<BinaryFactTypeForm> bftfs = binaryFactTypeFormComponent
				.getBinaryFactTypeFormsContainingTailTerm(person.getId().toString());

		Assert.assertEquals(1, bftfs.size());
		Assert.assertEquals(bftf.getId(), bftfs.iterator().next().getId());
	}

	@Test
	public void testGetBinaryFactTypeFormsContainingTerm() {

		Vocabulary voc = createVocabulary();
		Term person = termComponent.addTerm(voc.getId().toString(), "Person");

		binaryFactTypeFormComponent.addBinaryFactTypeForm(voc.getId().toString(), "Car", "driven by", "drives",
				person.getSignifier());
		binaryFactTypeFormComponent.addBinaryFactTypeForm(voc.getId().toString(), person.getSignifier(), "eat",
				"eaten by", "Apple");

		Collection<BinaryFactTypeForm> bftfs = binaryFactTypeFormComponent.getBinaryFactTypeFormsContainingTerm(person
				.getId().toString());

		Assert.assertEquals(2, bftfs.size());
	}

	@Test
	public void testGetDerivedFacts() {

		Vocabulary voc = createVocabulary();
		Term person = termComponent.addTerm(voc.getId().toString(), "Person");
		Term driver = termComponent.addTerm(voc.getId().toString(), "Driver");

		representationComponent.changeGeneralConcept(driver.getId().toString(), person.getId()
				.toString());

		Term car = termComponent.addTerm(voc.getId().toString(), "Car");

		binaryFactTypeFormComponent.addBinaryFactTypeForm(voc.getId().toString(), person.getSignifier(),
				"drives", "driven by", car.getSignifier());

		Collection<BinaryFactTypeForm> derivedFacts = binaryFactTypeFormComponent.getDerivedFacts(car.getId()
				.toString());

		Assert.assertEquals(1, derivedFacts.size());
		Assert.assertEquals(driver.getId(), derivedFacts.iterator().next().getTailTerm().getId());
	}

}
