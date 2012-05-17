package com.collibra.dgc.api.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.component.attribute.AttributeComponent;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.MultiValueListAttribute;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;

/**
 * {@link AttributeComponent} API tests.
 * 
 * @author pmalarme
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestAttributeComponent extends AbstractDGCBootstrappedApiTest {

	@Test
	public void testValidateConstraintSingleValue() {

		Term term = createTerm();
		Term attributeTypeLabel = createSingleValueListAttributeType();

		Attribute attribute = attributeComponent.addSingleValueListAttribute(term.getId().toString(),
				attributeTypeLabel.getId().toString(), "A");
		boolean result = attributeComponent.validateSingleValueListConstraint(attribute.getId().toString());
		assertTrue(result);

		attribute = attributeComponent.addSingleValueListAttribute(term.getId().toString(), attributeTypeLabel.getId()
				.toString(), "D");
		result = attributeComponent.validateSingleValueListConstraint(attribute.getId().toString());
		assertFalse(result);

		result = attributeComponent.validateConstraint(attribute.getId());
		assertFalse(result);
	}

	@Test
	public void testValidateConstraintMultipleValue() {

		Term term = createTerm();
		Term attributeTypeLabel = createMultiValueListAttributeType();

		List<String> attributeValues = new ArrayList<String>(2);
		attributeValues.add("A");
		attributeValues.add("C");

		Attribute attribute = attributeComponent.addMultiValueListAttribute(term.getId().toString(), attributeTypeLabel
				.getId().toString(), attributeValues);
		boolean result = attributeComponent.validateMultiValueListConstraint(attribute.getId().toString());
		assertTrue(result);

		attributeValues.clear();
		attributeValues.add("B");
		attributeValues.add("D");

		attribute = attributeComponent.addMultiValueListAttribute(term.getId().toString(), attributeTypeLabel.getId()
				.toString(), attributeValues);
		result = attributeComponent.validateMultiValueListConstraint(attribute.getId().toString());
		assertFalse(result);

		result = attributeComponent.validateConstraint(attribute.getId());
		assertFalse(result);
	}

	@Test
	public void testAddAndRemoveStringAttribute() {

		Term term = createTerm();
		Term attributeTypeLabel = createStringAttributeType();

		Attribute attribute = attributeComponent.addStringAttribute(term.getId().toString(), attributeTypeLabel.getId()
				.toString(), createAttributeLongExpression());
		assertNotNull(attribute);

		// Remove attribute
		attributeComponent.removeAttribute(attribute.getId().toString());

		// Test the remove
		try {
			attributeComponent.getStringAttribute(attribute.getId().toString());
			fail();

		} catch (EntityNotFoundException ex) {

		}
	}

	@Test
	public void testAddAndRemoveStringAttributeGenericMethod() {

		Term term = createTerm();
		Term attributeTypeLabel = createStringAttributeType();

		ArrayList<String> values = new ArrayList<String>(1);
		values.add(createAttributeLongExpression());

		Attribute attribute = attributeComponent.addAttribute(term.getId().toString(), attributeTypeLabel.getId()
				.toString(), values);
		assertNotNull(attribute);

		// Remove attribute
		attributeComponent.removeAttribute(attribute.getId().toString());

		// Test the remove
		try {
			attributeComponent.getStringAttribute(attribute.getId().toString());
			fail();

		} catch (EntityNotFoundException ex) {

		}
	}

	@Test
	public void testAddAndRemoveSingleValueListAttribute() {

		Term term = createTerm();
		Term attributeTypeLabel = createSingleValueListAttributeType();

		Attribute attribute = attributeComponent.addSingleValueListAttribute(term.getId().toString(),
				attributeTypeLabel.getId().toString(), "B");
		assertNotNull(attribute);

		// Remove attribute
		attributeComponent.removeAttribute(attribute.getId().toString());

		// Test the remove
		try {
			attributeComponent.getSingleValueListAttribute(attribute.getId().toString());
			fail();

		} catch (EntityNotFoundException ex) {

		}
	}

	@Test
	public void testAddAndRemoveSingleValueListAttributeGenericMethod() {

		Term term = createTerm();
		Term attributeTypeLabel = createSingleValueListAttributeType();

		ArrayList<String> values = new ArrayList<String>(1);
		values.add("B");

		Attribute attribute = attributeComponent.addAttribute(term.getId().toString(), attributeTypeLabel.getId()
				.toString(), values);
		assertNotNull(attribute);

		// Remove attribute
		attributeComponent.removeAttribute(attribute.getId().toString());

		// Test the remove
		try {
			attributeComponent.getSingleValueListAttribute(attribute.getId().toString());
			fail();

		} catch (EntityNotFoundException ex) {

		}
	}

	@Test
	public void testAddAndRemoveMultiValueListAttribute() {

		Term term = createTerm();
		Term attributeTypeLabel = createMultiValueListAttributeType();

		Attribute attribute = attributeComponent.addMultiValueListAttribute(term.getId().toString(), attributeTypeLabel
				.getId().toString(), createAttributeAllowedValues());
		assertNotNull(attribute);

		// Remove attribute
		attributeComponent.removeAttribute(attribute.getId().toString());

		// Test the remove
		try {
			attributeComponent.getMultiValueListAttribute(attribute.getId().toString());
			fail();

		} catch (EntityNotFoundException ex) {

		}
	}

	@Test
	public void testAddAndRemoveMultiValueListAttributeGenericMethod() {

		Term term = createTerm();
		Term attributeTypeLabel = createMultiValueListAttributeType();

		Attribute attribute = attributeComponent.addAttribute(term.getId().toString(), attributeTypeLabel.getId()
				.toString(), createAttributeAllowedValues());
		assertNotNull(attribute);

		// Remove attribute
		attributeComponent.removeAttribute(attribute.getId().toString());

		// Test the remove
		try {
			attributeComponent.getMultiValueListAttribute(attribute.getId().toString());
			fail();

		} catch (EntityNotFoundException ex) {

		}
	}

	@Test
	public void testChangeAndGetStringAttributeLongExpression() {

		Term term = createTerm();
		Term attributeTypeLabel = createStringAttributeType();

		StringAttribute attribute = attributeComponent.addStringAttribute(term.getId().toString(), attributeTypeLabel
				.getId().toString(), createAttributeLongExpression());

		StringAttribute modifiedAttribute = attributeComponent.changeStringAttributeLongExpression(attribute.getId()
				.toString(), createAttributeLongExpression() + "2");
		assertNotNull(modifiedAttribute);
		assertEquals(attribute.getId(), modifiedAttribute.getId());

		attribute = (StringAttribute) attributeComponent.getAttribute(attribute.getId().toString());
		assertNotNull(attribute);

		assertEquals(createAttributeLongExpression() + "2", modifiedAttribute.getLongExpression());
		assertEquals(modifiedAttribute.getLongExpression(), attribute.getLongExpression());

		// Test generic method
		ArrayList<String> values = new ArrayList<String>(1);
		values.add(createAttributeLongExpression());

		modifiedAttribute = (StringAttribute) attributeComponent.changeAttribute(attribute.getId(), values);
		assertNotNull(modifiedAttribute);
		assertEquals(attribute.getId(), modifiedAttribute.getId());

		attribute = (StringAttribute) attributeComponent.getAttribute(attribute.getId().toString());
		assertNotNull(attribute);

		assertEquals(createAttributeLongExpression(), modifiedAttribute.getLongExpression());
		assertEquals(modifiedAttribute.getLongExpression(), attribute.getLongExpression());
	}

	@Test
	public void testChangeAndGetSingleValueListAttributeValue() {

		Term term = createTerm();
		Term attributeTypeLabel = createSingleValueListAttributeType();

		SingleValueListAttribute attribute = attributeComponent.addSingleValueListAttribute(term.getId().toString(),
				attributeTypeLabel.getId().toString(), "A");

		SingleValueListAttribute modifiedAttribute = attributeComponent.changeSingleValueListAttributeValue(attribute
				.getId().toString(), "B");
		assertNotNull(modifiedAttribute);
		assertEquals(attribute.getId(), modifiedAttribute.getId());

		attribute = (SingleValueListAttribute) attributeComponent.getAttribute(attribute.getId().toString());
		assertNotNull(attribute);

		assertEquals("B", modifiedAttribute.getValue());
		assertEquals(modifiedAttribute.getValue(), attribute.getValue());

		// Test Generic Method
		ArrayList<String> values = new ArrayList<String>(1);
		values.add("C");

		modifiedAttribute = (SingleValueListAttribute) attributeComponent.changeAttribute(attribute.getId(), values);
		assertNotNull(modifiedAttribute);
		assertEquals(attribute.getId(), modifiedAttribute.getId());

		attribute = (SingleValueListAttribute) attributeComponent.getAttribute(attribute.getId().toString());
		assertNotNull(attribute);

		assertEquals("C", modifiedAttribute.getValue());
		assertEquals(modifiedAttribute.getValue(), attribute.getValue());
	}

	@Test
	public void testChangeAndGetMultiValueListAttributeValues() {

		Term term = createTerm();
		Term attributeTypeLabel = createMultiValueListAttributeType();

		List<String> values = createAttributeAllowedValues();

		MultiValueListAttribute attribute = attributeComponent.addMultiValueListAttribute(term.getId().toString(),
				attributeTypeLabel.getId().toString(), values);

		// Update the values
		values.clear();
		values.add("A");
		values.add("D");

		MultiValueListAttribute modifiedAttribute = attributeComponent.changeMultiValueListAttributeValues(attribute
				.getId().toString(), values);
		assertNotNull(modifiedAttribute);
		assertEquals(attribute.getId(), modifiedAttribute.getId());

		attribute = (MultiValueListAttribute) attributeComponent.getAttribute(attribute.getId().toString());
		assertNotNull(attribute);

		assertEquals(values, modifiedAttribute.getValues());
		assertEquals(modifiedAttribute.getValues(), attribute.getValues());

		// Test Generic Method
		values.clear();
		values.add("x");
		values.add("z");
		values.add("z");

		modifiedAttribute = (MultiValueListAttribute) attributeComponent.changeAttribute(attribute.getId().toString(),
				values);
		assertNotNull(modifiedAttribute);
		assertEquals(attribute.getId(), modifiedAttribute.getId());

		attribute = (MultiValueListAttribute) attributeComponent.getAttribute(attribute.getId().toString());
		assertNotNull(attribute);

		assertEquals(values, modifiedAttribute.getValues());
		assertEquals(modifiedAttribute.getValues(), attribute.getValues());
	}

	@Test
	public void testAddDefinition() {

		Term term = createTerm();
		StringAttribute att = attributeComponent.addDefinition(term.getId().toString(), "Testing...");

		assertNotNull(att);
		assertEquals("Testing...", att.getLongExpression());
	}

	@Test
	public void testAddDescription() {

		Term term = createTerm();
		StringAttribute att = attributeComponent.addDescription(term.getId().toString(), "Testing...");

		assertNotNull(att);
		assertEquals("Testing...", att.getLongExpression());
	}

	@Test
	public void testAddExample() {

		Term term = createTerm();
		StringAttribute att = attributeComponent.addExample(term.getId().toString(), "Testing...");

		assertNotNull(att);
		assertEquals("Testing...", att.getLongExpression());
	}

	@Test
	public void testAddNote() {

		Term term = createTerm();
		StringAttribute att = attributeComponent.addNote(term.getId().toString(), "Testing...");

		assertNotNull(att);
		assertEquals("Testing...", att.getValue());
	}

	@Test
	public void testGetDefinitionsForRepresentation() {

		Term term = createTerm();

		attributeComponent.addDefinition(term.getId().toString(), "Test1");
		attributeComponent.addDefinition(term.getId().toString(), "Test2");
		attributeComponent.addDefinition(term.getId().toString(), "Test3");

		Collection<StringAttribute> attributes = attributeComponent.getDefinitionsForRepresentation(term.getId()
				.toString());

		assertNotNull(attributes);
		assertEquals(3, attributes.size());
	}

	@Test
	public void testGetDescriptionsForRepresentation() {

		Term term = createTerm();

		attributeComponent.addDescription(term.getId().toString(), "Test1");
		attributeComponent.addDescription(term.getId().toString(), "Test2");
		attributeComponent.addDescription(term.getId().toString(), "Test3");

		Collection<StringAttribute> attributes = attributeComponent.getDescriptionsForRepresentation(term.getId()
				.toString());

		assertNotNull(attributes);
		assertEquals(3, attributes.size());
	}

	@Test
	public void testGetExamplesForRepresentation() {

		Term term = createTerm();

		attributeComponent.addExample(term.getId().toString(), "Test1");
		attributeComponent.addExample(term.getId().toString(), "Test2");
		attributeComponent.addExample(term.getId().toString(), "Test3");

		Collection<StringAttribute> attributes = attributeComponent.getExamplesForRepresentation(term.getId()
				.toString());

		assertNotNull(attributes);
		assertEquals(3, attributes.size());
	}

	@Test
	public void testGetNotesForRepresentation() {

		Term term = createTerm();

		attributeComponent.addNote(term.getId().toString(), "Test1");
		attributeComponent.addNote(term.getId().toString(), "Test2");
		attributeComponent.addNote(term.getId().toString(), "Test3");

		Collection<StringAttribute> attributes = attributeComponent.getNotesForRepresentation(term.getId().toString());

		assertNotNull(attributes);
		assertEquals(3, attributes.size());
	}

	@Test
	public void testGetAttributesOfTypeForRepresentation() {

		Term term = createTerm();

		attributeComponent.addDefinition(term.getId().toString(), "Def1");
		attributeComponent.addDefinition(term.getId().toString(), "Def2");
		attributeComponent.addDefinition(term.getId().toString(), "Def3");

		Collection<Attribute> attributes = attributeComponent.getAttributesOfTypeForRepresentation(term.getId()
				.toString(), attributeTypeComponent.getDefinitionAttributeType().getId().toString());

		assertNotNull(attributes);
		assertEquals(3, attributes.size());
	}

	@Test
	public void testGetAttributesOfTypesForRepresentation() {

		Term term = createTerm();
		Term stringAttributeTypeLabel = createStringAttributeType();
		Term singleValueAttributeTypeLabel = createSingleValueListAttributeType();
		Term multiValueAttributeTypeLabel = createMultiValueListAttributeType();

		attributeComponent.addDefinition(term.getId().toString(), "Def1");
		attributeComponent.addDefinition(term.getId().toString(), "Def2");

		attributeComponent.addDescription(term.getId().toString(), "Desc1");
		attributeComponent.addDescription(term.getId().toString(), "Desc2");

		attributeComponent.addExample(term.getId().toString(), "Ex1");
		attributeComponent.addExample(term.getId().toString(), "Ex2");

		attributeComponent.addNote(term.getId().toString(), "Note1");
		attributeComponent.addNote(term.getId().toString(), "Note2");

		attributeComponent.addStringAttribute(term.getId().toString(), stringAttributeTypeLabel.getId().toString(),
				createAttributeLongExpression() + "1");
		attributeComponent.addStringAttribute(term.getId().toString(), stringAttributeTypeLabel.getId().toString(),
				createAttributeLongExpression() + "2");

		attributeComponent.addSingleValueListAttribute(term.getId().toString(), singleValueAttributeTypeLabel.getId()
				.toString(), "1");
		attributeComponent.addSingleValueListAttribute(term.getId().toString(), singleValueAttributeTypeLabel.getId()
				.toString(), "2");

		List<String> values = createAttributeAllowedValues();
		attributeComponent.addMultiValueListAttribute(term.getId().toString(), multiValueAttributeTypeLabel.getId()
				.toString(), values);
		values.remove(0);
		attributeComponent.addMultiValueListAttribute(term.getId().toString(), multiValueAttributeTypeLabel.getId()
				.toString(), values);

		Map<String, Collection<Attribute>> attributeMap = attributeComponent.getAttributesOfTypesForRepresentation(term
				.getId().toString(), attributeTypeComponent.getDefinitionAttributeType().getId().toString(),
				attributeTypeComponent.getDescriptionAttributeType().getId().toString(), attributeTypeComponent
						.getExampleAttributeType().getId().toString(), attributeTypeComponent.getNoteAttributeType()
						.getId().toString(), stringAttributeTypeLabel.getId().toString(), singleValueAttributeTypeLabel
						.getId().toString(), multiValueAttributeTypeLabel.getId().toString());

		assertNotNull(attributeMap);
		assertEquals(7, attributeMap.size());

		for (Collection<Attribute> attributes : attributeMap.values()) {

			assertNotNull(attributes);
			assertEquals(2, attributes.size());
		}
	}

	@Test
	public void testGetAttributesOfTypesForRepresentationInCollection() {

		Term term = createTerm();
		Term stringAttributeTypeLabel = createStringAttributeType();
		Term singleValueAttributeTypeLabel = createSingleValueListAttributeType();
		Term multiValueAttributeTypeLabel = createMultiValueListAttributeType();

		List<Attribute> attList = new ArrayList<Attribute>();

		attList.add(attributeComponent.addDefinition(term.getId().toString(), "Def1"));
		attList.add(attributeComponent.addDefinition(term.getId().toString(), "Def2"));

		attList.add(attributeComponent.addDescription(term.getId().toString(), "Desc1"));
		attList.add(attributeComponent.addDescription(term.getId().toString(), "Desc2"));

		attList.add(attributeComponent.addExample(term.getId().toString(), "Ex1"));
		attList.add(attributeComponent.addExample(term.getId().toString(), "Ex2"));

		attList.add(attributeComponent.addNote(term.getId().toString(), "Note1"));
		attList.add(attributeComponent.addNote(term.getId().toString(), "Note2"));

		attList.add(attributeComponent.addStringAttribute(term.getId().toString(), stringAttributeTypeLabel.getId()
				.toString(), createAttributeLongExpression() + "1"));
		attList.add(attributeComponent.addStringAttribute(term.getId().toString(), stringAttributeTypeLabel.getId()
				.toString(), createAttributeLongExpression() + "2"));

		attList.add(attributeComponent.addSingleValueListAttribute(term.getId().toString(),
				singleValueAttributeTypeLabel.getId().toString(), "1"));
		attList.add(attributeComponent.addSingleValueListAttribute(term.getId().toString(),
				singleValueAttributeTypeLabel.getId().toString(), "2"));

		List<String> values = createAttributeAllowedValues();
		attList.add(attributeComponent.addMultiValueListAttribute(term.getId().toString(), multiValueAttributeTypeLabel
				.getId().toString(), values));
		values.remove(0);
		attList.add(attributeComponent.addMultiValueListAttribute(term.getId().toString(), multiValueAttributeTypeLabel
				.getId().toString(), values));

		Collection<Attribute> attributes = attributeComponent.getAttributesOfTypesForRepresentationInCollection(term
				.getId().toString(), attributeTypeComponent.getDefinitionAttributeType().getId().toString(),
				attributeTypeComponent.getDescriptionAttributeType().getId().toString(), attributeTypeComponent
						.getExampleAttributeType().getId().toString(), attributeTypeComponent.getNoteAttributeType()
						.getId().toString(), stringAttributeTypeLabel.getId().toString(), singleValueAttributeTypeLabel
						.getId().toString(), multiValueAttributeTypeLabel.getId().toString());

		assertNotNull(attributes);
		assertEquals(14, attributes.size());

		for (Attribute attribute : attList)
			assertTrue(attributes.contains(attribute));
	}
}
