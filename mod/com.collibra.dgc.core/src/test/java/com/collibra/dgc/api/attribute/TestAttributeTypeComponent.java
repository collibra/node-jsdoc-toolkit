package com.collibra.dgc.api.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.component.attribute.AttributeTypeComponent;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.representation.MultiValueListAttribute;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;

/**
 * {@link AttributeTypeComponent} API tests.
 * 
 * @author pmalarme
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestAttributeTypeComponent extends AbstractDGCBootstrappedApiTest {

	@Test
	public void testAddStringAttributeType() {

		Term label = attributeTypeComponent.addStringAttributeType("Test Attribute", "This is a Test Attribute.");

		assertNotNull(label);
		assertEquals("Test Attribute", label.getSignifier());
	}

	@Test
	public void testAddSingleValueListAttributeTypeAndGetAllowedValues() {

		Collection<String> values = createAttributeAllowedValues();

		Term label = attributeTypeComponent.addValueListAttributeType("Test Attribute", "This is a Test Attribute",
				false, values);

		assertNotNull(label);

		Collection<String> results = attributeTypeComponent.getAllowedValues(label.getId().toString());
		assertNotNull(results);
		assertEquals(3, results.size());

		for (String value : values) {
			assertTrue(results.contains(value));
		}
	}

	@Test
	public void testAddMultipleValueListAttributeTypeAndGetAllowedValues() {

		Collection<String> values = createAttributeAllowedValues();

		Term label = attributeTypeComponent.addValueListAttributeType("Test Attribute", "This is a Test Attribute",
				false, values);

		assertNotNull(label);

		Collection<String> results = attributeTypeComponent.getAllowedValues(label.getId().toString());
		assertNotNull(results);
		assertEquals(3, results.size());

		for (String value : values) {
			assertTrue(results.contains(value));
		}
	}

	@Test
	public void testGetAttributeTypes() {

		Collection<Term> types = attributeTypeComponent.getAttributeTypes();

		assertNotNull(types);
		final int oldSize = types.size();

		attributeTypeComponent.addStringAttributeType("MyAttribute", "MyAttributeDescription");
		types = attributeTypeComponent.getAttributeTypes();

		assertNotNull(types);
		assertEquals(oldSize + 1, types.size());
	}

	@Test
	public void testGetAttributeTypeAndChangeSignifier() {

		Term label = attributeTypeComponent.addStringAttributeType("Test Attribute", "This is a Test Attribute.");

		assertNotNull(label);

		Term updated = attributeTypeComponent.changeSignifier(label.getId().toString(), "New Test Attribute");

		assertNotNull(updated);
		assertEquals(label.getId(), updated.getId());
		assertEquals("New Test Attribute", updated.getSignifier());

		Term result = attributeTypeComponent.getAttributeTypeBySignifier("New Test Attribute");

		assertNotNull(result);
		assertEquals(label.getId(), result.getId());
		assertEquals("New Test Attribute", result.getSignifier());
	}

	@Test
	public void testGetAttributeTypeKind() {

		// String attribute
		Term stringAttributeType = createStringAttributeType();
		assertEquals(StringAttribute.class.getSimpleName(),
				attributeTypeComponent.getAttributeTypeKind(stringAttributeType.getId().toString()));

		stringAttributeType = attributeTypeComponent.getDefinitionAttributeType();
		assertEquals(StringAttribute.class.getSimpleName(),
				attributeTypeComponent.getAttributeTypeKind(stringAttributeType.getId().toString()));

		stringAttributeType = attributeTypeComponent.getDescriptionAttributeType();
		assertEquals(StringAttribute.class.getSimpleName(),
				attributeTypeComponent.getAttributeTypeKind(stringAttributeType.getId().toString()));

		stringAttributeType = attributeTypeComponent.getExampleAttributeType();
		assertEquals(StringAttribute.class.getSimpleName(),
				attributeTypeComponent.getAttributeTypeKind(stringAttributeType.getId().toString()));

		stringAttributeType = attributeTypeComponent.getNoteAttributeType();
		assertEquals(StringAttribute.class.getSimpleName(),
				attributeTypeComponent.getAttributeTypeKind(stringAttributeType.getId().toString()));

		// Single value list attribute
		Term singleValueListAttributeType = createSingleValueListAttributeType();
		assertEquals(SingleValueListAttribute.class.getSimpleName(),
				attributeTypeComponent.getAttributeTypeKind(singleValueListAttributeType.getId().toString()));

		// Multi value list attribute
		Term multiValueListAttributeType = createMultiValueListAttributeType();
		assertEquals(MultiValueListAttribute.class.getSimpleName(),
				attributeTypeComponent.getAttributeTypeKind(multiValueListAttributeType.getId().toString()));
	}

	@Test
	public void testGetAndChangeDescription() {

		Term label = attributeTypeComponent.addStringAttributeType("Test Attribute", "This is a Test Attribute Type.");

		assertNotNull(label);

		StringAttribute newDescription = attributeTypeComponent.changeDescription(label.getId().toString(),
				"This is a new Test Attribute Type Description.");

		assertNotNull(newDescription);
		assertEquals("This is a new Test Attribute Type Description.", newDescription.getLongExpression());
		assertEquals(label.getId(), newDescription.getOwner().getId());

		StringAttribute result = attributeTypeComponent.getAttributeTypeDescription(label.getId().toString());

		assertNotNull(result);
		assertEquals(newDescription.getId(), result.getId());
		assertEquals("This is a new Test Attribute Type Description.", result.getLongExpression());
		assertEquals(label.getId(), result.getOwner().getId());
	}

	@Test
	public void testGetAndChangeNullDescription() {

		Term label = attributeTypeComponent.getDefinitionAttributeType();

		assertNotNull(label);

		String rId = label.getId().toString();

		assertNull(attributeTypeComponent.getAttributeTypeDescription(rId));

		StringAttribute newDescription = attributeTypeComponent
				.changeDescription(rId, "This is a Test Attribute Type.");

		assertNotNull(newDescription);
		assertEquals("This is a Test Attribute Type.", newDescription.getLongExpression());
		assertEquals(label.getId(), newDescription.getOwner().getId());
	}

	@Test
	public void testChangeAllowedValues() {

		Collection<String> values = createAttributeAllowedValues();

		Term label = attributeTypeComponent.addValueListAttributeType("Test Attribute", "This is a Test Attribute",
				false, values);

		assertNotNull(label);

		values = new ArrayList<String>(2);
		values.add("100");
		values.add("200");

		Collection<String> updatedValues = attributeTypeComponent.changeAllowedValues(label.getId().toString(),
				values);

		assertNotNull(updatedValues);
		assertEquals(2, updatedValues.size());

		Collection<String> results = attributeTypeComponent.getAllowedValues(label.getId().toString());

		assertNotNull(results);
		assertEquals(2, results.size());

		for (String value : values) {

			assertTrue(updatedValues.contains(value));
			assertTrue(results.contains(value));
		}
	}

	@Test
	public void testRemoveStringAttributeType() {

		Term label = attributeTypeComponent.addStringAttributeType("Test Attribute", "This is a Test Attribute.");

		assertNotNull(label);

		attributeTypeComponent.removeAttributeType(label.getId().toString());

		boolean doesntExist = false;

		try {
			attributeTypeComponent.getAttributeType(label.getId().toString());

		} catch (EntityNotFoundException ex) {

			doesntExist = true;
		}

		assertTrue(doesntExist);
	}

	@Test
	public void testRemoveValueListAttributeType() {

		Collection<String> values = new ArrayList<String>(3);
		values.add("A");
		values.add("B");
		values.add("C");

		Term label = attributeTypeComponent.addValueListAttributeType("Test Attribute", "This is a Test Attribute",
				false, values);

		assertNotNull(label);

		attributeTypeComponent.removeAttributeType(label.getId().toString());

		boolean doesntExist = false;

		try {
			attributeTypeComponent.getAttributeType(label.getId().toString());

		} catch (EntityNotFoundException ex) {

			doesntExist = true;
		}

		assertTrue(doesntExist);
	}
}
