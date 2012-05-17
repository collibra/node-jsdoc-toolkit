package com.collibra.dgc.api.relation;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestRelationComponentDefense extends AbstractDGCBootstrappedApiTest {
	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";

	@Test
	public void testAddRelationDefenseSourceTermRIdNull() {
		try {
			relationComponent.addRelation(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelationDefenseSourceTermRIdEmpty() {
		try {
			relationComponent.addRelation(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelationDefenseBinaryFactTypeFormRIdNull() {
		try {
			relationComponent.addRelation(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelationDefenseBinaryFactTypeFormRIdEmpty() {
		try {
			relationComponent.addRelation(NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelationDefenseTargetTermRIdNull() {
		try {
			relationComponent.addRelation(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelationDefenseTargetTermRIdEmpty() {
		try {
			relationComponent.addRelation(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelation2DefenseSourceTermRIdNull() {
		try {
			relationComponent.addRelation(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelation2DefenseSourceTermRIdEmpty() {
		try {
			relationComponent.addRelation(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelation2DefenseBinaryFactTypeFormRIdNull() {
		try {
			relationComponent.addRelation(NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelation2DefenseBinaryFactTypeFormRIdEmpty() {
		try {
			relationComponent.addRelation(NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelation2DefenseTargetTermRIdNull() {
		try {
			relationComponent.addRelation(NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelation2DefenseTargetTermRIdEmpty() {
		try {
			relationComponent.addRelation(NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelation2DefenseVocabularyRIdNull() {
		try {
			relationComponent.addRelation(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRelation2DefenseVocabularyRIdEmpty() {
		try {
			relationComponent.addRelation(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceDefenseSourceTermRIdNull() {
		try {
			relationComponent.findRelationsBySource(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceDefensSourceTermRIdEmpty() {
		try {
			relationComponent.findRelationsBySource(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceAndTargetDefenseSourceTermRIdNull() {
		try {
			relationComponent.findRelationsBySourceAndTarget(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceAndTargetDefenseSourceTermRIdEmpty() {
		try {
			relationComponent.findRelationsBySourceAndTarget(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceAndTargetDefenseTargetTermRIdNull() {
		try {
			relationComponent.findRelationsBySourceAndTarget(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceAndTargetDefenseTargetTermRIdEmpty() {
		try {
			relationComponent.findRelationsBySourceAndTarget(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceAndTargetAndTypeDefenseBinaryFactTypeFormRIdNull() {
		try {
			relationComponent.findRelationsBySourceAndTargetAndType(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceAndTargetAndTypeDefenseBinaryFactTypeFormRIdEmpty() {
		try {
			relationComponent.findRelationsBySourceAndTargetAndType(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceAndTargetAndTypeDefenseSourceTermRIdNull() {
		try {
			relationComponent.findRelationsBySourceAndTargetAndType(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceAndTargetAndTypeDefenseSourceTermRIdEmpty() {
		try {
			relationComponent.findRelationsBySourceAndTargetAndType(NOT_EMPTY, NULL, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceAndTargetAndTypeDefenseTargetTermRIdNull() {
		try {
			relationComponent.findRelationsBySourceAndTargetAndType(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceAndTargetAndTypeDefenseTargetTermRIdEmpty() {
		try {
			relationComponent.findRelationsBySourceAndTargetAndType(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceAndTypeDefenseBinaryFactTypeFormNull() {
		try {
			relationComponent.findRelationsBySourceAndType(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceAndTypeDefenseBinaryFactTypeFormEmpty() {
		try {
			relationComponent.findRelationsBySourceAndType(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceAndTypeDefenseSourceTermRIdNull() {
		try {
			relationComponent.findRelationsBySourceAndType(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsBySourceAndTypeDefenseSourceTermRIdEmpty() {
		try {
			relationComponent.findRelationsBySourceAndType(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsByTargetDefenseTargetTermRIdNull() {
		try {
			relationComponent.findRelationsByTarget(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsByTargetDefenseTargetTermRIdEmpty() {
		try {
			relationComponent.findRelationsByTarget(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsByTargetAndTypeDefenseBinaryFactTypeFormRIdNull() {
		try {
			relationComponent.findRelationsByTargetAndType(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsByTargetAndTypeDefenseBinaryFactTypeFormRIdEmpty() {
		try {
			relationComponent.findRelationsByTargetAndType(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsByTargetAndTypeDefenseTargetTermRIdNull() {
		try {
			relationComponent.findRelationsByTargetAndType(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsByTargetAndTypeDefenseTargetTermRIdEmpty() {
		try {
			relationComponent.findRelationsByTargetAndType(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsByTypeDefenseBinaryFactTypeFormRIdNull() {
		try {
			relationComponent.findRelationsByType(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRelationsByTypeDefenseBinaryFactTypeFormRIdEmpty() {
		try {
			relationComponent.findRelationsByType(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetRelationDefenseRIdNull() {
		try {
			relationComponent.getRelation(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RELATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetRelationDefenseRIdEmpty() {
		try {
			relationComponent.getRelation(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RELATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveRelationDefenseRIdNull() {
		try {
			relationComponent.removeRelation(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RELATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveRelationDefenseRIdEmpty() {
		try {
			relationComponent.removeRelation(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RELATION_ID_EMPTY, ex.getErrorCode());
		}
	}
}
