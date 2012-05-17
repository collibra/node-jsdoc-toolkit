package com.collibra.dgc.api.relation;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestRelationTypeComponentDefense extends AbstractDGCBootstrappedApiTest {
	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";

	@Test
	public void testAddRelationTypeDefenseSourceSignifierNull() {
		try {
			relationTypeComponent.addRelationType(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_HEAD_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelationTypeDefenseSourceSignifierEmpty() {
		try {
			relationTypeComponent.addRelationType(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_HEAD_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelationTypeDefenseroleNull() {
		try {
			relationTypeComponent.addRelationType(NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ROLE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelationTypeDefenseroleEmpty() {
		try {
			relationTypeComponent.addRelationType(NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ROLE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelationTypeDefenseCoRoleNull() {
		try {
			relationTypeComponent.addRelationType(NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_COROLE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelationTypeDefenseCoRoleEmpty() {
		try {
			relationTypeComponent.addRelationType(NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_COROLE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelationTypeDefenseTargetSignifierNull() {
		try {
			relationTypeComponent.addRelationType(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_TAIL_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelationTypeDefenseTargetSignifierEmpty() {
		try {
			relationTypeComponent.addRelationType(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_TAIL_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindPossibleRelationTypesForSourceTermDefenseSourceTermIdNull() {
		try {
			relationTypeComponent.findPossibleRelationTypesForSourceTerm(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindPossibleRelationTypesForSourceTermDefenseSourceTermIdEmpty() {
		try {
			relationTypeComponent.findPossibleRelationTypesForSourceTerm(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindPossibleRelationTypesForSourceTypeDefenseSourceConceptTypeTermRIdNull() {
		try {
			relationTypeComponent.findPossibleRelationTypesForSourceType(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONCEPT_TYPE_TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindPossibleRelationTypesForSourceTypeDefenseSourceConceptTypeTermRIdEmpty() {
		try {
			relationTypeComponent.findPossibleRelationTypesForSourceType(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONCEPT_TYPE_TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindPossibleRelationTypesForTargetTermDefenseTargetTermRIdNull() {
		try {
			relationTypeComponent.findPossibleRelationTypesForTargetTerm(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindPossibleRelationTypesForTargetTermDefenseTargetTermRIdEmpty() {
		try {
			relationTypeComponent.findPossibleRelationTypesForTargetTerm(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindPossibleRelationTypesForTargetTypeDefenseTargetConceptTypeTermRIdNull() {
		try {
			relationTypeComponent.findPossibleRelationTypesForTargetType(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONCEPT_TYPE_TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindPossibleRelationTypesForTargetTypeDefenseTargetConceptTypeTermRIdEmpty() {
		try {
			relationTypeComponent.findPossibleRelationTypesForTargetType(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONCEPT_TYPE_TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

}
