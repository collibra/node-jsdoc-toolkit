package com.collibra.dgc.api.configurationcategotry;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestConfigurationCategoryComponentDefense extends AbstractDGCBootstrappedApiTest {
	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";
	private static final List<String> NOT_EMPTY_COLLECTION = Arrays.asList("value", "value");

	@Test
	public void testAddAttributeAndRelationTypesConfigurationCategoryDefenseNameNull() {
		try {
			configurationCategoryComponent.addAttributeAndRelationTypesConfigurationCategory(NULL, NOT_EMPTY,
					NOT_EMPTY_COLLECTION);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFCAT_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAttributeAndRelationTypesConfigurationCategoryDefenseNameEmpty() {
		try {
			configurationCategoryComponent.addAttributeAndRelationTypesConfigurationCategory(EMPTY, NOT_EMPTY,
					NOT_EMPTY_COLLECTION);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFCAT_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRepresentationToAttributeAndRelationConfigurationCategoryDefenseNameNull() {
		try {
			configurationCategoryComponent
					.addRepresentationToAttributeAndRelationConfigurationCategory(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFCAT_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRepresentationToAttributeAndRelationConfigurationCategoryDefenseNameEmpty() {
		try {
			configurationCategoryComponent.addRepresentationToAttributeAndRelationConfigurationCategory(EMPTY,
					NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFCAT_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRepresentationToAttributeAndRelationConfigurationCategoryDefenseRepresentationRIdNull() {
		try {
			configurationCategoryComponent
					.addRepresentationToAttributeAndRelationConfigurationCategory(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRepresentationToAttributeAndRelationConfigurationCategoryDefenseRepresentationRIdEmpty() {
		try {
			configurationCategoryComponent.addRepresentationToAttributeAndRelationConfigurationCategory(NOT_EMPTY,
					EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRepresentationToAttributeAndRelationConfigurationCategory2DefenseNameNull() {
		try {
			configurationCategoryComponent.addRepresentationToAttributeAndRelationConfigurationCategory(NULL,
					NOT_EMPTY, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFCAT_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRepresentationToAttributeAndRelationConfigurationCategory2DefenseNameEmpty() {
		try {
			configurationCategoryComponent.addRepresentationToAttributeAndRelationConfigurationCategory(EMPTY,
					NOT_EMPTY, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFCAT_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRepresentationToAttributeAndRelationConfigurationCategory2DefenseRepresentationRIdNull() {
		try {
			configurationCategoryComponent.addRepresentationToAttributeAndRelationConfigurationCategory(NOT_EMPTY,
					NULL, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRepresentationToAttributeAndRelationConfigurationCategory2DefenseRepresentationRIdEmpty() {
		try {
			configurationCategoryComponent.addRepresentationToAttributeAndRelationConfigurationCategory(NOT_EMPTY,
					EMPTY, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeAttributeAndRelationConfigurationCategoryDescriptionDefenseNameNull() {
		try {
			configurationCategoryComponent.changeAttributeAndRelationConfigurationCategoryDescription(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFCAT_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeAttributeAndRelationConfigurationCategoryDescriptionDefenseNameEmpty() {
		try {
			configurationCategoryComponent.changeAttributeAndRelationConfigurationCategoryDescription(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFCAT_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeRepresentationOrderInAttributeAndRelationConfigurationCategoryDefenseNameNull() {
		try {
			configurationCategoryComponent.changeRepresentationOrderInAttributeAndRelationConfigurationCategory(NULL,
					NOT_EMPTY, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFCAT_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeRepresentationOrderInAttributeAndRelationConfigurationCategoryDefenseNameEmpty() {
		try {
			configurationCategoryComponent.changeRepresentationOrderInAttributeAndRelationConfigurationCategory(EMPTY,
					NOT_EMPTY, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFCAT_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeRepresentationOrderInAttributeAndRelationConfigurationCategoryDefenseRepresentarionRIdNull() {
		try {
			configurationCategoryComponent.changeRepresentationOrderInAttributeAndRelationConfigurationCategory(
					NOT_EMPTY, NULL, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeRepresentationOrderInAttributeAndRelationConfigurationCategoryDefenseRepresentarionRIdEmpty() {
		try {
			configurationCategoryComponent.changeRepresentationOrderInAttributeAndRelationConfigurationCategory(
					NOT_EMPTY, EMPTY, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.REPRESENTATION_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeAndRelationTypesConfigurationCategoryDefenseNameNull() {
		try {
			configurationCategoryComponent.getAttributeAndRelationTypesConfigurationCategory(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFCAT_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeAndRelationTypesConfigurationCategoryDefenseNameEmpty() {
		try {
			configurationCategoryComponent.getAttributeAndRelationTypesConfigurationCategory(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFCAT_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveConfigurationCategoryDefenseNameNull() {
		try {
			configurationCategoryComponent.removeConfigurationCategory(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFCAT_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveConfigurationCategoryDefenseNameEmpty() {
		try {
			configurationCategoryComponent.removeConfigurationCategory(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFCAT_NAME_EMPTY, ex.getErrorCode());
		}
	}
}
