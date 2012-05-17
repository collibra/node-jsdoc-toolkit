package com.collibra.dgc.service.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.ConstraintViolationException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.DateTimeAttribute;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.AttributeService;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * Tests for {@link AttributeService} implementation.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestAttributeService extends AbstractServiceTest {
	private static final String SEM_COMM_1_NAME = "Sem Com Name";
	private static final String SEM_COMM_1_URI = "Sem Com URI";
	private Community semComm;
	private Community spComm;
	private Vocabulary vocabulary;

	@Test
	public void testFindAttributeTypes() {
		initialize();

		// Define attribute 'Responsible'
		String labelSignifier = "Responsible";
		Term responsibleAT = attributeService.createStringAttributeType(labelSignifier, "Responsible for the Term");
		resetTransaction();
		findLabel(labelSignifier);

		Term result = attributeService.findAttributeTypeLabel("Responsible");
		assertEquals(responsibleAT, result);
	}

	@Test
	public void testFindAttributesForRepresentation() {
		initialize();

		// Define attribute 'Responsible'
		String labelSignifier = "Responsible";
		Term responsible = attributeService.createStringAttributeType(labelSignifier, "Responsible for the Term");
		resetTransaction();

		// create the term
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Term t = representationFactory.makeTerm(vocabulary, "Test Term");
		representationService.saveTerm(t);
		resetTransaction();

		t = representationService.findTermByResourceId(t.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		responsible = representationService.findTermByResourceId(responsible.getId());

		t = representationService.findTermByResourceId(t.getId());
		Attribute def = representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), t, "attr");
		t = representationService.saveTerm(t);
		Attribute descr = representationFactory.makeStringAttribute(attributeService.findMetaDescription(), t, "attr");
		t = representationService.saveTerm(t);
		Attribute ex = representationFactory.makeStringAttribute(attributeService.findMetaExample(), t, "attr");
		t = representationService.saveTerm(t);
		Attribute note = representationFactory.makeStringAttribute(attributeService.findMetaNote(), t, "attr");
		t = representationService.saveTerm(t);
		Attribute cust = representationFactory.makeStringAttribute(responsible, t, "custom attr");

		t = representationService.saveTerm(t);
		resetTransaction();

		t = representationService.findTermByResourceId(t.getId());

		assertEquals(1, attributeService.findAttributesForRepresentation("Definition", t).size());
		assertEquals(1, attributeService.findAttributesForRepresentation("Description", t).size());
		assertEquals(1, attributeService.findAttributesForRepresentation("Descriptive Example", t).size());
		assertEquals(1, attributeService.findAttributesForRepresentation("Note", t).size());
		assertEquals(1, attributeService.findAttributesForRepresentation("Responsible", t).size());
		assertEquals(cust.getId(), attributeService.findAttributesForRepresentation("Responsible", t)
				.iterator().next().getId());
	}

	@Test
	public void testVersionAttribute() {
		initialize();

		// create the term
		Term term = representationFactory.makeTerm(vocabulary, "TestCustomAttributeTerm");
		term = representationService.saveTerm(term);

		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());

		// create the definition
		StringAttribute at = representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), term,
				"Def 1");
		term = representationService.saveTerm(term);

		resetTransaction();

		// change the definition
		term = representationService.findTermByResourceId(term.getId());
		at = attributeService.getStringAttribute(at.getId());

		attributeService.update(at, "Def 2");

		resetTransaction();

		// make sure that we have the updated definitions and the versions are correct
		term = representationService.findTermByResourceId(term.getId());
		at = attributeService.getStringAttribute(at.getId());

		assertEquals(1, term.getAttributes().size());
		assertEquals("Def 2", at.getLongExpression());
	}

	@Test
	public void testDateTimeAttribute() {

		initialize();

		// Define attribute 'Valid To'
		String labelSignifier = "Valid To";
		Term validToType = attributeService.createDateTimeAttributeType(labelSignifier, "Valid to description");

		// Create a term
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Term term = representationFactory.makeTerm(vocabulary, "TestCustomAttributeTerm");
		representationService.saveTerm(term);

		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		validToType = representationService.findTermByResourceId(validToType.getId());
		term = representationService.findTermByResourceId(term.getId());

		Calendar date = new GregorianCalendar(2008, 6, 19);
		DateTimeAttribute attribute = representationFactory.makeDateTimeAttribute(validToType, term, date);
		assertEquals(date, attribute.getDateTime());
		assertEquals(validToType, attribute.getLabel());
		assertEquals(term, attribute.getOwner());
		representationService.save(term);

		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		validToType = representationService.findTermByResourceId(validToType.getId());
		term = representationService.findTermByResourceId(term.getId());

		assertEquals(1, term.getAttributes().size());
		attribute = (DateTimeAttribute) term.getAttributes().iterator().next();
		assertEquals(date, attribute.getDateTime());
		assertEquals(validToType, attribute.getLabel());
		assertEquals(term, attribute.getOwner());
		assertEquals(attribute, attributeService.getAttribute(attribute.getId()));
	}

	@Test
	public void testCustomAttribute() {
		initialize();

		// Define attribute 'Responsible'
		String labelSignifier = "Responsible";
		attributeService.createStringAttributeType(labelSignifier, "Responsible for the Term");
		resetTransaction();

		final Term foundLabel = findLabel(labelSignifier);

		// Create a term
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Term term = representationFactory.makeTerm(vocabulary, "TestCustomAttributeTerm");
		representationService.saveTerm(term);
		resetTransaction();

		// Create custom attribute instance
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		term = representationService.findTermByResourceId(term.getId());
		Term label = representationService.findTermByResourceId(foundLabel.getId());

		StringAttribute attribute = representationFactory.makeStringAttribute(label, term, "CA Value");
		assertNotNull(attribute);
		assertEquals("CA Value", attribute.getValue());
		assertEquals(label, attribute.getLabel());
		assertEquals(term, attribute.getOwner());
	}

	@Test
	public void testUserTypeAttributeValueConstraint() {
		// initialize();
		//
		// // Define attribute 'Responsible'
		// String labelSignifier = "Responsible";
		// attributeService.createAttributeType(labelSignifier, "Responsible for the Term", ValueConstraint.UserType);
		// resetTransaction();
		//
		// final Term foundLabel = findLabel(labelSignifier);
		// ValueConstraint constraint = attributeService.getValueConstraint(foundLabel);
		// assertNotNull(constraint);
		// assertEquals(ValueConstraint.UserType, constraint);
		//
		// vocabulary = representationService.findVocabularyByResourceId(vocabulary.getResourceId());
		// Term term = representationFactory.makeTerm(vocabulary, "TestUserTypeValue");
		// representationService.saveTerm(term);
		// resetTransaction();
		//
		// Term label = representationService.findTermByResourceId(foundLabel.getResourceId());
		// vocabulary = representationService.findVocabularyByResourceId(vocabulary.getResourceId());
		// term = representationService.findTermByResourceId(term.getResourceId());
		// assertNotNull(term);
		//
		// // Create custom attribute instance of user type. Note that it is allowed to have any value for this at this
		// // point in time. In future once the user management is in domain model the constraint check can be done on
		// the
		// // back end.
		// Attribute attribute = attributeService.addCustomAttribute(vocabulary, term, label, "INVALID_USER_VALUE");
		// assertNotNull(attribute);
		// assertEquals("INVALID_USER_VALUE", attribute.getLongExpression());
		// assertEquals(label, attribute.getLabel());
		// assertEquals(term, attribute.getOwner());
		// resetTransaction();
	}

	@Test
	public void testStaticListTypeAttributeValueConstraint() {
		initialize();

		// Define attribute 'Responsible'
		String labelSignifier = "Countries";
		List<String> countries = new LinkedList<String>();
		countries.add("INDIA");
		countries.add("BELGIUM");
		Term label = attributeService.createSingleValueListAttributeType(labelSignifier,
				"Countries included in the list", countries);
		resetTransaction();

		for (String value : attributeService.findConstraintValues(label)) {
			assertTrue(countries.contains(value));
		}
	}

	private Term findLabel(final String labelSignifier) {
		List<Term> labels = attributeService.findAttributeTypeLabels();
		Term label = null;
		for (final Term l : labels) {
			if (labelSignifier.equals(l.getSignifier())) {
				label = l;
				break;
			}
		}
		assertNotNull(label);
		return label;
	}

	@Test
	public void testAddRemoveConstraintValues() {
		initialize();

		// Define attribute 'Countries'
		String labelSignifier = "Countries";
		Set<String> countries = new HashSet<String>();
		countries.add("INDIA");
		countries.add("BELGIUM");
		attributeService
				.createSingleValueListAttributeType(labelSignifier, "Countries included in the list", countries);
		resetTransaction();

		Term label = findLabel(labelSignifier);

		// Add two more.
		countries.add("USA");
		countries.add("CANDA");
		attributeService.setConstraintValues(label, countries);
		resetTransaction();

		label = findLabel(labelSignifier);
		Collection<String> currentValues = attributeService.findConstraintValues(label);
		for (String value : countries) {
			assertTrue(currentValues.contains(value));
		}

		// Delete one country.
		int size = countries.size();
		countries = new HashSet<String>();
		countries.add("INDIA");
		countries.add("BELGIUM");
		countries.add("CANDA");
		attributeService.setConstraintValues(label, countries);
		resetTransaction();

		label = representationService.findTermByResourceId(label.getId());
		currentValues = attributeService.findConstraintValues(label);
		assertEquals(size - 1, currentValues.size());
	}

	@Test
	public void testStaticListTypeAttributeValueConstraintChecking() {
		initialize();

		// Define attribute 'Countries'
		String labelSignifier = "Countries";
		List<String> countries = new LinkedList<String>();
		countries.add("INDIA");
		countries.add("BELGIUM");
		attributeService
				.createSingleValueListAttributeType(labelSignifier, "Countries included in the list", countries);
		resetTransaction();

		final Term foundLabel = findLabel(labelSignifier);

		Term label = representationService.findTermByResourceId(foundLabel.getId());

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Term term = representationFactory.makeTerm(vocabulary, "TestStaticListValue");
		representationService.saveTerm(term);
		resetTransaction();

		label = representationService.findTermByResourceId(foundLabel.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		term = representationService.findTermByResourceId(term.getId());
		assertNotNull(term);

		// Create custom attribute instance of type static list.
		SingleValueListAttribute attribute = representationFactory.makeSingleValueListAttribute(label, term, "INDIA");
		representationService.saveTerm(term);
		assertNotNull(attribute);
		assertTrue(attribute.getValue().equals("INDIA"));
		assertEquals(label, attribute.getLabel());
		assertEquals(term, attribute.getOwner());
		resetTransaction();

		// Create custom attribute instance of type static list with value violating constraint.
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		term = representationService.findTermByResourceId(term.getId());
		try {
			representationFactory.makeSingleValueListAttribute(label, term, "INDIA");
		} catch (ConstraintViolationException ex) {
			// Setting wrong values should not fail because it is allowed to have wrong values.
			fail();
		}

		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());
		for (Attribute att : term.getAttributes()) {
			if (att.getLabel().equals(label)) {
				assertTrue(attributeService.checkValueConstraint((SingleValueListAttribute) att));
			}
		}
	}

	@Test
	public void testSearchAttributesByLongExpression() {
		initialize();

		Term t = representationFactory.makeTerm(vocabulary, "Test");
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), t, "definition");

		representationService.saveVocabulary(vocabulary);

		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		List<Attribute> attributes = representationService.searchAttributesByLongExpression("def", 0, 100);
		assertEquals(1, attributes.size());

		for (Attribute at : attributes)
			System.out.println(" > " + at.verbalise() + " - " + at.getId());

		// remove the vocabulary and check if the attribute is no longer returned
		representationService.remove(vocabulary);

		resetTransaction();

		attributes = representationService.searchAttributesByLongExpression("def", 0, 100);
		assertEquals(0, attributes.size());

		// create a new vocabulary because now we are going to try to remove the community
		spComm = communityService.findCommunity(spComm.getId());
		vocabulary = representationFactory.makeVocabulary(spComm, "Communities Test URI", "Voc comm test");
		t = representationFactory.makeTerm(vocabulary, "Test");
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), t, "definition");
		spComm = communityService.findCommunity(spComm.getId());
		communityService.save(spComm);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		spComm = communityService.findCommunity(spComm.getId());
		attributes = representationService.searchAttributesByLongExpression("def", 0, 100);
		assertEquals(1, attributes.size());

		for (Attribute at : attributes)
			System.out.println(" > " + at.verbalise() + " - " + at.getId());

		// remove the community
		communityService.remove(spComm);

		resetTransaction();

		attributes = representationService.searchAttributesByLongExpression("def", 0, 100);
		assertEquals(0, attributes.size());
	}

	@Test
	public void testSetStatus() {
		initialize();

		Term t = representationFactory.makeTerm(vocabulary, "Test");

		representationService.saveVocabulary(vocabulary);

		resetTransaction();

		t = representationService.findTermByResourceId(t.getId());

		assertNotNull(t.getStatus());

		Term accepted = representationService.findTermBySignifier(representationService.findStatusesVocabulary(),
				"Accepted");
		assertNotNull(accepted);

		representationService.changeStatus(t, accepted);
		resetTransaction();

		t = representationService.findTermByResourceId(t.getId());

		assertNotNull(t.getStatus());
		assertEquals("Accepted", t.getStatus().getSignifier());

	}

	/**
	 * Special case added for Bug 6093 Change status of a renamed term leads to inconsistent data
	 */
	@Test
	public void testSetStatusAfterRename() {
		initialize();

		Term t = representationFactory.makeTerm(vocabulary, "Test");
		representationService.saveVocabulary(vocabulary);

		resetTransaction();

		t = representationService.findTermByResourceId(t.getId());

		assertNotNull(t.getStatus());
		assertEquals("Candidate", t.getStatus().getSignifier());

		t.setSignifier("Test Renamed");
		representationService.save(t);

		resetTransaction();

		t = representationService.findTermByResourceId(t.getId());
		Term accepted = representationService.findTermBySignifier(representationService.findStatusesVocabulary(),
				"Accepted");

		assertNotNull(accepted);

		representationService.changeStatus(t, accepted);
		resetTransaction();

		t = representationService.findTermByResourceId(t.getId());

		assertNotNull(t.getStatus());
		assertEquals("Accepted", t.getStatus().getSignifier());
		assertEquals("Test Renamed", t.getSignifier());

	}

	@Test
	public void testRemoveUsedStatus() {
		initialize();

		Term t = representationFactory.makeTerm(vocabulary, "Test");

		representationService.saveVocabulary(vocabulary);

		resetTransaction();

		t = representationService.findTermByResourceId(t.getId());

		assertNotNull(t.getStatus());

		try {
			representationService.remove(t.getStatus());
			resetTransaction();
			fail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDuplicateNotallowedOnEdit() {
		initialize();

		// create the term
		Term term = representationFactory.makeTerm(vocabulary, "TestCustomAttributeTerm");
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), term, "Def 2");
		term = representationService.saveTerm(term);
		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());

		// create the definition
		StringAttribute at = representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), term,
				"Def 1");
		term = representationService.saveTerm(term);

		resetTransaction();

		// change the definition
		term = representationService.findTermByResourceId(term.getId());
		at = attributeService.getStringAttribute(at.getId());

		try {
			attributeService.update(at, "Def 2");
			fail();
		} catch (IllegalArgumentException ex) {
			// Success
		}

		resetTransaction();

		// Change to something not existing
		at = attributeService.getStringAttribute(at.getId());
		attributeService.update(at, "Def 3");
		resetTransaction();

		// make sure that we have the updated definitions and the versions are correct
		term = representationService.findTermByResourceId(term.getId());
		at = attributeService.getStringAttribute(at.getId());

		assertEquals(2, term.getAttributes().size());
		assertEquals("Def 3", at.getLongExpression());
	}

	@Test
	public void testNoAttributeAddForNonPreferredRepresentation() {
		initialize();
		Term original = representationFactory.makeTerm(vocabulary, "Original");
		Term synonym = representationFactory.makeTerm(vocabulary, "synonym");
		representationService.saveVocabulary(vocabulary);

		resetTransaction();

		original = representationService.findTermByResourceId(original.getId());
		synonym = representationService.findTermByResourceId(synonym.getId());
		representationService.createSynonym(original, synonym);
		resetTransaction();

		synonym = representationService.findTermByResourceId(synonym.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		resetTransaction();
	}

	/**
	 * Go to Semantic community page Add New Attribute through Term Attribute widget Then press cross icon near to
	 * attribute to delete it Confirm
	 * 
	 * The success message will be shown, reload page again, attribute wasn't deleted and shown in widget
	 * @throws Exception
	 */
	@Test
	public void testBug9560() throws Exception {
		Community semComm = communityFactory.makeCommunity(SEM_COMM_1_NAME, SEM_COMM_1_URI);
		communityService.save(semComm);
		resetTransaction();

		semComm = communityService.findCommunityByUri(SEM_COMM_1_URI);

		Term myCa = attributeService.createStringAttributeType("My CA", "My Custom Attributes");
		assertNotNull(myCa);

		resetTransaction();

		communityService.findCommunity(semComm.getId());
		final Term foundLabel = findLabel("My CA");
		assertEquals("My CA", foundLabel.getSignifier());
		myCa = representationService.findTermByResourceId(myCa.getId());

		// Remove attribute type.
		attributeService.remove(myCa);
		String CaOTResourceId = myCa.getObjectType().getId();
		resetTransaction();

		assertNull(meaningService.findObjectTypeByResourceId(CaOTResourceId));
	}

	private void initialize() {
		semComm = communityFactory.makeCommunity("SC", "SC URI");
		assertNotNull(semComm);

		spComm = communityFactory.makeCommunity(semComm, "SP", "SP URI");
		assertNotNull(spComm);

		vocabulary = representationFactory.makeVocabulary(spComm, "Communities Test URI", "Voc comm test");
		communityService.save(semComm);

		resetTransaction();

		semComm = communityService.findCommunity(semComm.getId());
		spComm = communityService.findCommunity(spComm.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
	}
}
