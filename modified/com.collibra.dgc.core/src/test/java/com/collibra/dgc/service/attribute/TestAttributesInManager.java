package com.collibra.dgc.service.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.service.AbstractServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestAttributesInManager extends AbstractServiceTest {

	@Test
	public void testAddAttributeToNewVocabulary() {
		// persist communities in dbase
		sp = communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term status = representationFactory.makeTerm(vocabulary, "Status");
		Attribute reviewer1 = representationFactory.makeStringAttribute(status, car, "Car Reviewer 1");
		communityService.save(sp);

		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		// Assert that the attributes are preserved when vocabulary version is changed.
		assertEquals(1, car.getAttributes().size());

		// asking the owner of an attribute must be the latest version of the representation
		reviewer1 = car.getAttributes().iterator().next();

		car = representationService.findTermByResourceId(car.getId());
		status = representationService.findTermByResourceId(status.getId());
		Attribute reviewer2 = representationFactory.makeStringAttribute(status, car, "Car Reviewer 2");
		// car = representationService.createTerm(car);
		vocabulary = representationService.saveVocabulary(vocabulary);
		resetTransaction();

		car = representationService.findTermByResourceId(car.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		status = representationService.findTermByResourceId(status.getId());
		List<Attribute> atts = attributeService.findAttributesForRepresentation(status, car);
		assertEquals(2, atts.size());

		Set<Attribute> attributes = car.getAttributes();
		assertEquals(2, attributes.size());
		attributes = car.getAttributes();
		assertEquals(2, attributes.size());

		// Add one more attribute to car
		car = representationService.findTermByResourceId(car.getId());
		status = representationService.findTermByResourceId(status.getId());
		Attribute reviewer3 = representationFactory.makeStringAttribute(status, car, "Car Reviewer 3");
		// car = representationService.createTerm(car);
		vocabulary = representationService.saveVocabulary(vocabulary);
	}

	@Test
	public void testDeleteAttribute() {

		// set-up the attributes as in the called test method
		testAddAttributeToNewVocabulary();
		resetTransaction();

		// get the involved objects and check their version
		Vocabulary vocabulary = representationService.findVocabularyByName("Test");
		Term car = representationService.findTermBySignifier(vocabulary, "Car");
		Term status = representationService.findTermBySignifier(vocabulary, "Status");

		// remove all the attributes from car
		Object[] attributes = car.getAttributes().toArray();
		assertEquals(3, attributes.length);

		// Remove the attributes but ensure 'status type' is not removed. This is because the status will be added back
		// on removing when vocabulary is created.
		Set<Attribute> attrs = car.getAttributes();
		for (final Attribute attr : attrs) {
			attributeService.remove(attr);
		}

		resetTransaction();

		// re-initialize objects
		car = representationService.findTermByResourceId(car.getId());
		status = representationService.findTermByResourceId(status.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		// car should have no attributes left
		assertEquals(0, car.getAttributes().size());

		// create a new attribute
		Attribute reviewer4 = representationFactory.makeStringAttribute(status, car, "Car Reviewer 4");
		// persist
		vocabulary = representationService.saveVocabulary(vocabulary);
		resetTransaction();

		// re-initialize objects
		car = representationService.findTermByResourceId(car.getId());
		status = representationService.findTermByResourceId(status.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertEquals(1, car.getAttributes().size());

		attrs = car.getAttributes();
		for (final Attribute attr : attrs) {
			attributeService.remove(attr);
		}

		resetTransaction();

		// re-initialize objects
		car = representationService.findTermByResourceId(car.getId());
		status = representationService.findTermByResourceId(status.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		// car should have no attributes left
		assertEquals(0, car.getAttributes().size());
	}

	@Test
	public void testAddAttributeToNewVocabularyAddingPreferredTermFirst() {
		// persist communities in dbase
		sp = communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		vocabulary = car.getVocabulary();
		Term status = representationFactory.makeTerm(vocabulary, "Status");

		Attribute reviewer1 = representationFactory.makeStringAttribute(status, car, "Car Reviewer 1");

		communityService.save(sp);

		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		// Assert that the attributes are preserved when vocabulary version is changed.
		for (Term term : vocabulary.getTerms()) {
			if (term.equals(car)) {
				assertEquals(1, term.getAttributes().size());
			}
		}

		status = representationService.findTermByResourceId(status.getId());
		car = representationService.findTermByResourceId(car.getId());

		Attribute reviewer2 = representationFactory.makeStringAttribute(status, car, "Car Reviewer 2");

		representationService.saveTerm(car);

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		car = representationService.findTermByResourceId(car.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		status = representationService.findTermByResourceId(status.getId());

		assertEquals(2, car.getAttributes().size());
	}

	@Test
	public void testAddAttributeToNewVocabularyCreateVocabularyOnly() {
		// persist communities in dbase
		sp = communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term status = representationFactory.makeTerm(vocabulary, "Status");
		Attribute reviewer1 = representationFactory.makeStringAttribute(status, car, "Car Reviewer 1");
		Attribute reviewer2 = representationFactory.makeStringAttribute(status, car, "Car Reviewer 2");

		communityService.save(sp);

		resetTransaction();
		// re-initialize objects
		car = representationService.findTermByResourceId(car.getId());
		status = representationService.findTermByResourceId(status.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertEquals(2, car.getAttributes().size());
	}

	@Test
	public void testCreateAttribute() {
		// persist communities in dbase
		sp = communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
		Term carTerm = representationFactory.makeTerm(vocabulary, "Car");

		communityService.save(sp);

		resetTransaction();
		// re-initialize objects
		carTerm = representationService.findTermByResourceId(carTerm.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		// create an attribute
		Attribute carDefinition = representationFactory.makeStringAttribute(attributeService.findMetaDefinition(),
				carTerm, "This is a car definition");
		// representationService.createAttribute(carDefinition);
		carTerm = representationService.saveTerm(carTerm);

		resetTransaction();
		// re-initialize objects
		carTerm = representationService.findTermByResourceId(carTerm.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(carDefinition);

		carTerm.getAttributes();
		List<StringAttribute> definitions = attributeService.findDefinitionsForRepresentation(carTerm);
		assertEquals(1, definitions.size());

	}

	@Test
	public void testFindDefinitionsForRepresentation() {
		// persist communities in dbase
		sp = communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
		Term carTerm = representationFactory.makeTerm(vocabulary, "Car");

		communityService.save(sp);

		resetTransaction();
		// re-initialize objects
		carTerm = representationService.findTermBySignifier(vocabulary, "Car");
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(vocabulary);
		assertNotNull(carTerm);
		Attribute definition = representationFactory.makeStringAttribute(attributeService.findMetaDefinition(),
				carTerm, "A definition for a car");
		// representationService.createAttribute(definition);
		carTerm = representationService.saveTerm(carTerm);

		Set<Attribute> attributes = carTerm.getAttributes();
		assertEquals(1, attributes.size());

		definition = attributeService.getAttribute(definition.getId());
		List<StringAttribute> results = attributeService.findDefinitionsForRepresentation(carTerm);
		assertTrue(results.contains(definition));
	}

	@Test
	public void testFindDescriptionsForRepresentation() {
		// persist communities in dbase
		sp = communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
		Term carTerm = representationFactory.makeTerm(vocabulary, "Car");

		communityService.save(sp);

		resetTransaction();
		// re-initialize objects
		carTerm = representationService.findTermBySignifier(vocabulary, "Car");
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(vocabulary);
		assertNotNull(carTerm);
		Attribute description = representationFactory.makeStringAttribute(attributeService.findMetaDescription(),
				carTerm, "A description for a car");
		// representationService.createAttribute(description);
		carTerm = representationService.saveTerm(carTerm);

		Set<Attribute> attributes = carTerm.getAttributes();
		assertEquals(1, attributes.size());

		description = attributeService.getAttribute(description.getId());
		List<StringAttribute> results = attributeService.findDescriptionsForRepresentation(carTerm);
		assertTrue(results.contains(description));
		assertEquals(1, results.size());
	}

	@Test
	public void testFindExamplesForRepresentation() {
		// persist communities in dbase
		sp = communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
		Term carTerm = representationFactory.makeTerm(vocabulary, "Car");

		communityService.save(sp);

		resetTransaction();
		// re-initialize objects
		carTerm = representationService.findTermBySignifier(vocabulary, "Car");
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(vocabulary);
		assertNotNull(carTerm);
		Attribute example = representationFactory.makeStringAttribute(attributeService.findMetaExample(), carTerm,
				"An example for a car");
		// representationService.createAttribute(example);
		carTerm = representationService.saveTerm(carTerm);

		Set<Attribute> attributes = carTerm.getAttributes();
		assertEquals(1, attributes.size());

		example = attributeService.getAttribute(example.getId());
		List<StringAttribute> results = attributeService.findExamplesForRepresentation(carTerm);
		assertTrue(results.contains(example));
		assertEquals(1, results.size());
	}

	@Test
	public void testFindNotesForRepresentation() {
		// persist communities in dbase
		sp = communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
		Term carTerm = representationFactory.makeTerm(vocabulary, "Car");

		communityService.save(sp);

		resetTransaction();
		// re-initialize objects
		carTerm = representationService.findTermBySignifier(vocabulary, "Car");
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(vocabulary);
		assertNotNull(carTerm);
		Attribute note = representationFactory.makeStringAttribute(attributeService.findMetaNote(), carTerm,
				"A note for a car");
		// representationService.createAttribute(note);
		carTerm = representationService.saveTerm(carTerm);

		Set<Attribute> attributes = carTerm.getAttributes();
		assertEquals(1, attributes.size());

		note = attributeService.getAttribute(note.getId());
		List<StringAttribute> results = attributeService.findNotesForRepresentation(carTerm);
		assertTrue(results.contains(note));
		assertEquals(1, results.size());
	}

	@Test
	public void testOwnerUpdateWithChangeinOwnerVersionsForTerm() {
		// persist communities in dbase
		sp = communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
		Term carTerm = representationFactory.makeTerm(vocabulary, "Car");

		communityService.save(sp);

		resetTransaction();
		// re-initialize objects
		carTerm = representationService.findTermBySignifier(vocabulary, "Car");
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(vocabulary);
		assertNotNull(carTerm);

		// Create one attribute
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), carTerm,
				"A definition for a car");
		representationService.saveTerm(carTerm);
		resetTransaction();
		carTerm = representationService.findTermByResourceId(carTerm.getId());
		assertForOwnerUpdateVersions(carTerm);

		// Create second attribute
		representationFactory.makeStringAttribute(attributeService.findMetaDescription(), carTerm,
				"A definition for a car");
		representationService.saveTerm(carTerm);
		resetTransaction();
		carTerm = representationService.findTermByResourceId(carTerm.getId());
		assertForOwnerUpdateVersions(carTerm);

		// Create second attribute
		representationFactory.makeStringAttribute(attributeService.findMetaNote(), carTerm, "A definition for a car");
		representationService.saveTerm(carTerm);
		resetTransaction();
		carTerm = representationService.findTermByResourceId(carTerm.getId());
		assertForOwnerUpdateVersions(carTerm);

		int size = carTerm.getAttributes().size();

		attributeService.remove(carTerm.getAttributes().iterator().next());

		resetTransaction();
		carTerm = representationService.findTermByResourceId(carTerm.getId());
		assertEquals(size - 1, carTerm.getAttributes().size());
		assertForOwnerUpdateVersions(carTerm);
	}

	@Test
	public void testOwnerUpdateWithChangeinOwnerVersionsForBFTF() {
		// persist communities in dbase
		sp = communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
		Term head = representationFactory.makeTerm(vocabulary, "A");
		Term tail = representationFactory.makeTerm(vocabulary, "B");
		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(vocabulary, head, "a", "b", tail);

		communityService.save(sp);

		resetTransaction();
		// re-initialize objects
		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(vocabulary);
		assertNotNull(bftf);

		// Create one attribute
		representationFactory
				.makeStringAttribute(attributeService.findMetaDefinition(), bftf, "A definition for a car");
		representationService.saveBinaryFactTypeForm(bftf);
		resetTransaction();
		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		assertForOwnerUpdateVersions(bftf);

		// Create second attribute
		representationFactory.makeStringAttribute(attributeService.findMetaDescription(), bftf,
				"A definition for a car");
		representationService.saveBinaryFactTypeForm(bftf);
		resetTransaction();
		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		assertForOwnerUpdateVersions(bftf);

		// Create second attribute
		representationFactory.makeStringAttribute(attributeService.findMetaNote(), bftf, "A definition for a car");
		representationService.saveBinaryFactTypeForm(bftf);
		resetTransaction();
		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		assertForOwnerUpdateVersions(bftf);

		int size = bftf.getAttributes().size();
		attributeService.remove(bftf.getAttributes().iterator().next());

		resetTransaction();
		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		assertEquals(size - 1, bftf.getAttributes().size());
		assertForOwnerUpdateVersions(bftf);
	}

	@Test
	public void testOwnerUpdateWithChangeinOwnerVersionsForCF() {
		// persist communities in dbase
		sp = communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
		Term term = representationFactory.makeTerm(vocabulary, "A");
		CharacteristicForm cf = representationFactory.makeCharacteristicForm(vocabulary, term, "a");

		communityService.save(sp);

		resetTransaction();
		// re-initialize objects
		cf = representationService.findCharacteristicFormByResourceId(cf.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(vocabulary);
		assertNotNull(cf);

		// Create one attribute
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), cf, "A definition for a car");
		representationService.saveCharacteristicForm(cf);
		resetTransaction();
		cf = representationService.findCharacteristicFormByResourceId(cf.getId());
		assertForOwnerUpdateVersions(cf);

		// Create second attribute
		representationFactory.makeStringAttribute(attributeService.findMetaDescription(), cf, "A definition for a car");
		representationService.saveCharacteristicForm(cf);
		resetTransaction();
		cf = representationService.findCharacteristicFormByResourceId(cf.getId());
		assertForOwnerUpdateVersions(cf);

		// Create second attribute
		representationFactory.makeStringAttribute(attributeService.findMetaNote(), cf, "A definition for a car");
		representationService.saveCharacteristicForm(cf);
		resetTransaction();
		cf = representationService.findCharacteristicFormByResourceId(cf.getId());
		assertForOwnerUpdateVersions(cf);

		int size = cf.getAttributes().size();
		attributeService.remove(cf.getAttributes().iterator().next());

		resetTransaction();
		cf = representationService.findCharacteristicFormByResourceId(cf.getId());
		assertEquals(size - 1, cf.getAttributes().size());
		assertForOwnerUpdateVersions(cf);
	}

	/**
	 * #6142. Adding attribute after empty attribute of same type results in NPE. Steps:
	 * <p>
	 * 1. Add definition with null value.
	 * <p>
	 * 2. Add another definition with some value, it throws NPE.
	 */
	@Test
	public void testNullAttributesBug6142() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
		Term term = representationFactory.makeTerm(vocabulary, "A");
		communityService.save(sp);
		resetTransaction();

		// Null definition
		term = representationService.findTermByResourceId(term.getId());
		StringAttribute nullValueAttribute = representationFactory.makeStringAttribute(
				attributeService.findMetaDefinition(), term, null);
		representationService.saveTerm(term);
		resetTransaction();

		nullValueAttribute = attributeService.getStringAttribute(nullValueAttribute.getId());
		assertNotNull(nullValueAttribute);
		assertEquals(null, nullValueAttribute.getValue());

		term = representationService.findTermByResourceId(term.getId());
		StringAttribute nonNullValueAttribute = representationFactory.makeStringAttribute(
				attributeService.findMetaDefinition(), term, "Definition");
		representationService.saveTerm(term);
		resetTransaction();

		nonNullValueAttribute = attributeService.getStringAttribute(nonNullValueAttribute.getId());
		attributeService.update(nonNullValueAttribute, "Definition changed");
		resetTransaction();

		// Null note
		term = representationService.findTermByResourceId(term.getId());
		nullValueAttribute = representationFactory.makeStringAttribute(attributeService.findMetaNote(), term, null);
		representationService.saveTerm(term);
		resetTransaction();

		nullValueAttribute = attributeService.getStringAttribute(nullValueAttribute.getId());
		assertNotNull(nullValueAttribute);
		assertEquals(null, nullValueAttribute.getLongExpression());

		term = representationService.findTermByResourceId(term.getId());
		representationFactory.makeStringAttribute(attributeService.findMetaNote(), term, "Note");
		representationService.saveTerm(term);
		resetTransaction();
	}

	/**
	 * For asserting correct owner for the attributes.
	 * @param owner
	 */
	private void assertForOwnerUpdateVersions(Representation owner) {
		for (Attribute att : owner.getAttributes()) {
			assertEquals(att.getOwner().getId(), owner.getId());
		}
	}
}
