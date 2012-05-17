package com.collibra.dgc.api.representation;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.representation.Term;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestRepresenationComponentDefense extends AbstractDGCBootstrappedApiTest {

	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";
	private static final Collection<String> NULL_COLLECTION = null;
	private static final Collection<String> EMPTY_COLLECTION = new ArrayList<String>();

	@Test
	public void testAddSynonymDefenseCurrentRepresentationRIdNull() {
		try {
			Term t = createTerm();
			representationComponent.addSynonym(NULL, t.getId());

			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSynonymDefenseCurrentRepresentationRIdEmpty() {
		try {
			Term t = createTerm();
			representationComponent.addSynonym(EMPTY, t.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSynonymDefenseSelectedRepresentationRIdNull() {
		try {
			representationComponent.addSynonym(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSynonymDefenseSelectedRepresentationRIdEmpty() {
		try {
			representationComponent.addSynonym(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeConceptTypeDefenseRIdNull() {
		try {
			representationComponent.changeConceptType(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeConceptTypeDefenseRIdEmpty() {
		try {
			representationComponent.changeConceptType(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeConceptTypeDefenseConceptTypeTermRIdNull() {
		try {
			representationComponent.changeConceptType(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONCEPT_TYPE_TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeConceptTypeDefenseConceptTypeTermRIdEmpty() {
		try {
			representationComponent.changeConceptType(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONCEPT_TYPE_TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeGeneralConceptDefenseSpecializedRepresentationRIdNull() {
		try {
			representationComponent.changeGeneralConcept(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeGeneralConceptDefenseSpecializedRepresentationRIdEmpty() {
		try {
			representationComponent.changeGeneralConcept(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeGeneralConceptDefenseGeneralRepresentationRIdNull() {
		try {
			Term t = createTerm();
			representationComponent.changeGeneralConcept(t.getId(), NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeGeneralConceptDefenseGeneralRepresentationRIdEmpty() {
		try {
			Term t = createTerm();
			representationComponent.changeGeneralConcept(t.getId(), EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeStatusDefenseRIDNull() {
		try {
			representationComponent.changeStatus(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeStatusDefenseRIDEmpty() {
		try {
			representationComponent.changeStatus(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindPossibleAttributeTypesDefenseRIDNull() {
		try {
			representationComponent.findPossibleAttributeTypes(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindPossibleAttributeTypesDefenseRIDEmpty() {
		try {
			representationComponent.findPossibleAttributeTypes(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindPossibleStatusesDefenseRIDNull() {
		try {
			representationComponent.findPossibleStatuses(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindPossibleStatusesDefenseRIDEmpty() {
		try {
			representationComponent.findPossibleStatuses(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetConceptTypeDefenseRIdNull() {
		try {
			representationComponent.getConceptType(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetConceptTypeDefenseRIdEmpty() {
		try {
			representationComponent.getConceptType(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetGeneralConceptDefenseRIdNull() {
		try {
			representationComponent.getGeneralConcept(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetGeneralConceptDefenseRIdEmpty() {
		try {
			representationComponent.getGeneralConcept(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetRepresentationDefenseRIdNull() {
		try {
			representationComponent.getRepresentation(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetRepresentationDefenseRIdEmpty() {
		try {
			representationComponent.getRepresentation(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetSpecializedConceptsDefenseRIdNull() {
		try {
			representationComponent.getSpecializedConcepts(NULL, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetSpecializedConceptsDefenseRIdEmpty() {
		try {
			representationComponent.getSpecializedConcepts(EMPTY, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetSynonymsDefenseRIdNull() {
		try {
			representationComponent.getSynonyms(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetSynonymsDefenseRIdEmpty() {
		try {
			representationComponent.getSynonyms(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveDefenseRIdNull() {
		try {
			representationComponent.remove(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveDefenseRIdEmpty() {
		try {
			representationComponent.remove(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveGeneralConceptDefenseRIdNull() {
		try {
			representationComponent.removeGeneralConcept(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveGeneralConceptDefenseRIdEmpty() {
		try {
			representationComponent.removeGeneralConcept(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveSynonymDefenseRIdNull() {
		try {
			representationComponent.removeSynonym(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveSynonymDefenseRIdEmpty() {
		try {
			representationComponent.removeSynonym(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

}
