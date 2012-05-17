package com.collibra.dgc.api.attribute;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.representation.Term;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestAttributeComponentDefense extends AbstractDGCBootstrappedApiTest {

	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";

	@Test
	public void testAddDefintionDefenseRepresentationRIdNull() {
		try {
			attributeComponent.addDefinition(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddDefintionDefenseRepresentationRIdEmpty() {
		try {
			attributeComponent.addDefinition(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddDefintionDefenseRepresentationRIdNotFound() {
		try {
			attributeComponent.addDefinition(NON_EXISTANT, NOT_EMPTY);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_NOT_FOUND, ex.getErrorCode());
		}
	}

	@Test
	public void testAddDescriptionDefenseRepresentationRIdNull() {
		try {
			attributeComponent.addDescription(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddDescriptionDefenseRepresentationRIdEmpty() {
		try {
			attributeComponent.addDescription(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddDescriptionDefenseRepresentationRIdNotFound() {
		try {
			attributeComponent.addDescription(NON_EXISTANT, NOT_EMPTY);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_NOT_FOUND, ex.getErrorCode());
		}
	}

	@Test
	public void testAddExampleDefenseRepresentationRIdNull() {
		try {
			attributeComponent.addExample(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddExampleDefenseRepresentationRIdEmpty() {
		try {
			attributeComponent.addExample(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddExampleDefenseRepresentationRIdNotFound() {
		try {
			attributeComponent.addExample(NON_EXISTANT, NOT_EMPTY);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_NOT_FOUND, ex.getErrorCode());
		}
	}

	@Test
	public void testAddMultiValueListAttributeDefenseRepresentationRIdNull() {
		try {
			attributeComponent.addMultiValueListAttribute(NULL, NOT_EMPTY, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddMultiValueListAttributeDefenseRepresentationRIdEmpty() {
		try {
			attributeComponent.addMultiValueListAttribute(EMPTY, NOT_EMPTY, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddMultiValueListAttributeDefenseRepresentationRIdNotFound() {
		try {
			attributeComponent.addMultiValueListAttribute(NON_EXISTANT, NOT_EMPTY, null);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_NOT_FOUND, ex.getErrorCode());
		}
	}

	@Test
	public void testAddMultiValueListAttributeDefenseLabelRIdNull() {
		try {
			Term term = createTerm();
			attributeComponent.addMultiValueListAttribute(term.getId(), NULL, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddMultiValueListAttributeDefenseLabelRIdEmpty() {
		try {
			Term term = createTerm();
			attributeComponent.addMultiValueListAttribute(term.getId(), EMPTY, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddMultiValueListAttributeDefenseLabelRIdNotFound() {
		try {
			Term term = createTerm();
			attributeComponent.addMultiValueListAttribute(term.getId(), NON_EXISTANT, null);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.TERM_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testAddNoteDefenseRepresentationRIdNull() {
		try {
			attributeComponent.addNote(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddNoteDefenseRepresentationRIdEmpty() {
		try {
			attributeComponent.addNote(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddNoteDefenseRepresentationRIdNotFound() {
		try {
			attributeComponent.addNote(NON_EXISTANT, NOT_EMPTY);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_NOT_FOUND, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSingleValueListAttributeDefenseRepresentationRIdNull() {
		try {
			attributeComponent.addSingleValueListAttribute(NULL, NOT_EMPTY, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSingleValueListAttributeDefenseRepresentationRIdEmpty() {
		try {
			attributeComponent.addSingleValueListAttribute(EMPTY, NOT_EMPTY, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSingleValueListAttributeDefenseRepresentationRIdNotFound() {
		try {
			attributeComponent.addSingleValueListAttribute(NON_EXISTANT, NOT_EMPTY, null);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_NOT_FOUND, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSingleValueListAttributeDefenseLabelRIdNull() {
		try {
			Term term = createTerm();
			attributeComponent.addSingleValueListAttribute(term.getId(), NULL, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSingleValueListAttributeDefenseLabelRIdEmpty() {
		try {
			Term term = createTerm();
			attributeComponent.addSingleValueListAttribute(term.getId(), EMPTY, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSingleValueListAttributeDefenseLabelRIdNotEmpty() {
		try {
			Term term = createTerm();
			attributeComponent.addSingleValueListAttribute(term.getId(), NON_EXISTANT, null);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.TERM_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testAddStringAttributeDefenseRepresentationRIdNull() {
		try {
			attributeComponent.addStringAttribute(NULL, NOT_EMPTY, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddStringAttributeDefenseRepresentationRIdEmpty() {
		try {
			attributeComponent.addStringAttribute(EMPTY, NOT_EMPTY, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddStringAttributeDefenseRepresentationRIdNotFound() {
		try {
			attributeComponent.addStringAttribute(NON_EXISTANT, NOT_EMPTY, null);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_NOT_FOUND, ex.getErrorCode());
		}
	}

	@Test
	public void testAddStringAttributeDefenseLabelRIdNull() {
		try {
			Term term = createTerm();
			attributeComponent.addStringAttribute(term.getId(), NULL, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddStringAttributeDefenseLabelRIdEmpty() {
		try {
			Term term = createTerm();
			attributeComponent.addStringAttribute(term.getId(), EMPTY, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddStringAttributeDefenseLabelRIdNotFound() {
		try {
			Term term = createTerm();
			attributeComponent.addStringAttribute(term.getId(), NON_EXISTANT, null);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.TERM_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeMultiValueListAttributeValuesDefenseMultiValueListAttributeRIdNull() {
		try {
			attributeComponent.changeMultiValueListAttributeValues(NULL, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeMultiValueListAttributeValuesDefenseMultiValueListAttributeRIdEmpty() {
		try {

			attributeComponent.changeMultiValueListAttributeValues(EMPTY, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeMultiValueListAttributeValuesDefenseMultiValueListAttributeRIdNotFound() {
		try {

			attributeComponent.changeMultiValueListAttributeValues(NON_EXISTANT, null);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.MVL_ATTRIBUTE_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeSingleValueListAttributeValueDefenseSingleValueListAttributeRIdNull() {
		try {
			attributeComponent.changeSingleValueListAttributeValue(NULL, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeSingleValueListAttributeValueDefenseSingleValueListAttributeRIdEmpty() {
		try {
			attributeComponent.changeSingleValueListAttributeValue(EMPTY, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeSingleValueListAttributeValueDefenseSingleValueListAttributeRIdNotFound() {
		try {
			attributeComponent.changeSingleValueListAttributeValue(NON_EXISTANT, null);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.SVL_ATTRIBUTE_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeStringAttributeLongExpressionDefenseStringAttributeRIdNull() {
		try {
			attributeComponent.changeStringAttributeLongExpression(NULL, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeStringAttributeLongExpressionDefenseStringAttributeRIdEmpty() {
		try {
			attributeComponent.changeStringAttributeLongExpression(EMPTY, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeStringAttributeLongExpressionDefenseStringAttributeRIdNotFound() {
		try {
			attributeComponent.changeStringAttributeLongExpression(NON_EXISTANT, null);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.STRING_ATTRIBUTE_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeDefenseRIdNull() {
		try {
			attributeComponent.getAttribute(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeDefenseRIdEmpty() {
		try {
			attributeComponent.getAttribute(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeDefenseRIdNotFound() {
		try {
			attributeComponent.getAttribute(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributesOfTypeForRepresentationDefenseRepresentationRIdNull() {
		try {
			attributeComponent.getAttributesOfTypeForRepresentation(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributesOfTypeForRepresentationDefenseRepresentationRIdEmpty() {
		try {
			attributeComponent.getAttributesOfTypeForRepresentation(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributesOfTypeForRepresentationDefenseRepresentationRIdNotFound() {
		try {
			attributeComponent.getAttributesOfTypeForRepresentation(NON_EXISTANT, NOT_EMPTY);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_NOT_FOUND, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributesOfTypeForRepresentationDefenseLabelRIdNull() {
		try {
			Term t = createTerm();
			attributeComponent.getAttributesOfTypeForRepresentation(t.getId(), NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributesOfTypeForRepresentationDefenseLabelRIdEmpty() {
		try {
			Term t = createTerm();
			attributeComponent.getAttributesOfTypeForRepresentation(t.getId(), EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributesOfTypeForRepresentationDefenseLabelRIdNotFound() {
		try {
			Term t = createTerm();
			attributeComponent.getAttributesOfTypeForRepresentation(t.getId(), NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.TERM_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributesOfTypesForRepresentationInCollectionDefenseRepresentationRIdNull() {
		try {
			attributeComponent.getAttributesOfTypesForRepresentationInCollection(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributesOfTypesForRepresentationInCollectionDefenseRepresentationRIdEmpty() {
		try {
			attributeComponent.getAttributesOfTypesForRepresentationInCollection(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributesOfTypesForRepresentationInCollectionDefenseRepresentationRIdNotFound() {
		try {
			attributeComponent.getAttributesOfTypesForRepresentationInCollection(NON_EXISTANT, NOT_EMPTY);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_NOT_FOUND, ex.getErrorCode());
		}
	}

	@Test
	public void testGetDefinitionsForRepresentationDefenseRepresentationRIdNull() {
		try {
			attributeComponent.getDefinitionsForRepresentation(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetDefinitionsForRepresentationDefenseRepresentationRIdEmpty() {
		try {
			attributeComponent.getDefinitionsForRepresentation(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetDefinitionsForRepresentationDefenseRepresentationRIdNotFound() {
		try {
			attributeComponent.getDefinitionsForRepresentation(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_NOT_FOUND, ex.getErrorCode());
		}
	}

	@Test
	public void testGetDescriptionsForRepresentationDefenseRepresentationRIdNull() {
		try {
			attributeComponent.getDescriptionsForRepresentation(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetDescriptionsForRepresentationDefenseRepresentationRIdEmpty() {
		try {
			attributeComponent.getDescriptionsForRepresentation(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetDescriptionsForRepresentationDefenseRepresentationRIdNotFound() {
		try {
			attributeComponent.getDescriptionsForRepresentation(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_NOT_FOUND, ex.getErrorCode());
		}
	}

	@Test
	public void testGetExamplesForRepresentationDefenseRepresentationRIdNull() {
		try {
			attributeComponent.getExamplesForRepresentation(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetExamplesForRepresentationDefenseRepresentationRIdEmpty() {
		try {
			attributeComponent.getExamplesForRepresentation(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetExamplesForRepresentationDefenseRepresentationRIdNotFound() {
		try {
			attributeComponent.getExamplesForRepresentation(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_NOT_FOUND, ex.getErrorCode());
		}
	}

	@Test
	public void testGetMultiValueListAttributeDefenseMultiValueListAttributeRIdNull() {
		try {
			attributeComponent.getMultiValueListAttribute(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetMultiValueListAttributeDefenseMultiValueListAttributeRIdEmpty() {
		try {
			attributeComponent.getMultiValueListAttribute(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetMultiValueListAttributeDefenseMultiValueListAttributeRIdNotFound() {
		try {
			attributeComponent.getMultiValueListAttribute(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.MVL_ATTRIBUTE_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testGetNotesForRepresentationDefenseSingleValueListAttributeRIdNull() {
		try {
			attributeComponent.getNotesForRepresentation(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetNotesForRepresentationDefenseSingleValueListAttributeRIdEmpty() {
		try {
			attributeComponent.getNotesForRepresentation(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetNotesForRepresentationDefenseSingleValueListAttributeRIdNotFound() {
		try {
			attributeComponent.getNotesForRepresentation(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.REPRESENTATION_NOT_FOUND, ex.getErrorCode());
		}
	}

	@Test
	public void testGetSingleValueListAttributeDefenseSingleValueListAttributeRIdNull() {
		try {
			attributeComponent.getSingleValueListAttribute(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetSingleValueListAttributeDefenseSingleValueListAttributeRIdEmpty() {
		try {
			attributeComponent.getSingleValueListAttribute(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetSingleValueListAttributeDefenseSingleValueListAttributeRIdNotFound() {
		try {
			attributeComponent.getSingleValueListAttribute(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.SVL_ATTRIBUTE_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStringAttributeDefenseStringAttributeRIdNull() {
		try {
			attributeComponent.getStringAttribute(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStringAttributeDefenseStringAttributeRIdEmpty() {
		try {
			attributeComponent.getStringAttribute(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStringAttributeDefenseStringAttributeRIdNotFound() {
		try {
			attributeComponent.getStringAttribute(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.STRING_ATTRIBUTE_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveAttributeDefenseRIdNull() {
		try {
			attributeComponent.removeAttribute(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveAttributeDefenseRIdEmpty() {
		try {
			attributeComponent.removeAttribute(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveAttributeDefenseRIdNotFound() {
		try {
			attributeComponent.removeAttribute(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testValidateMultiValueListConstraintDefenseMultiValueListAttributeRIdNull() {
		try {
			attributeComponent.validateMultiValueListConstraint(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testValidateMultiValueListConstraintDefenseMultiValueListAttributeRIdEmpty() {
		try {
			attributeComponent.validateMultiValueListConstraint(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testValidateMultiValueListConstraintDefenseMultiValueListAttributeRIdNotFound() {
		try {
			attributeComponent.validateMultiValueListConstraint(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.MVL_ATTRIBUTE_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testValidateSingleValueListConstraintDefenseSingleValueListAttributeRIdNull() {
		try {
			attributeComponent.validateSingleValueListConstraint(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testValidateSingleValueListConstraintDefenseSingleValueListAttributeRIdEmpty() {
		try {
			attributeComponent.validateSingleValueListConstraint(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testValidateSingleValueListConstraintDefenseSingleValueListAttributeRIdNotFound() {
		try {
			attributeComponent.validateSingleValueListConstraint(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.SVL_ATTRIBUTE_NOT_FOUND_ID, ex.getErrorCode());
		}
	}
}
