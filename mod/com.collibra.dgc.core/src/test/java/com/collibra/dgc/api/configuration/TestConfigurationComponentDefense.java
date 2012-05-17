package com.collibra.dgc.api.configuration;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
@Transactional
@Ignore
public class TestConfigurationComponentDefense extends AbstractDGCBootstrappedApiTest {
	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String VALID_USER = "Admin";

	@Test
	public void testClearDefensePathNull() {
		try {
			configurationComponent.clear(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getMessage());
		}
	}

	@Test
	public void testClearDefensePathEmpty() {
		try {
			configurationComponent.clear(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getMessage());
		}
	}

	@Test
	public void testClear2DefensePathNull() {
		try {
			configurationComponent.clear(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getMessage());
		}
	}

	@Test
	public void testClear2DefensePathEmpty() {
		try {
			configurationComponent.clear(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getMessage());
		}
	}

	@Test
	public void testClear2DefenseUserNameNull() {
		try {
			configurationComponent.clear(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testClear2DefenseUserNameEmpty() {
		try {
			configurationComponent.clear(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetBooleanDefensePathNull() {
		try {
			configurationComponent.getBoolean(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetBooleanDefensePathEmpty() {
		try {
			configurationComponent.getBoolean(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetBoolean2DefensePathNull() {
		try {
			configurationComponent.getBoolean(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetBoolean2DefensePathEmpty() {
		try {
			configurationComponent.getBoolean(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetBoolean3DefensePathNull() {
		try {
			configurationComponent.getBoolean(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetBoolean3DefensePathEmpty() {
		try {
			configurationComponent.getBoolean(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetConfigurationSectionDefensePathNull() {
		try {
			configurationComponent.getConfigurationSection(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetConfigurationSectionDefensePathEmpty() {
		try {
			configurationComponent.getConfigurationSection(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetConfigurationSection2DefensePathNull() {
		try {
			configurationComponent.getConfigurationSection(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetConfigurationSection2DefensePathEmpty() {
		try {
			configurationComponent.getConfigurationSection(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetConfigurationSection3DefensePathNull() {
		try {
			configurationComponent.getConfigurationSection(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetConfigurationSection3DefensePathEmpty() {
		try {
			configurationComponent.getConfigurationSection(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetIntegerDefensePathNull() {
		try {
			configurationComponent.getInteger(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetIntegerDefensePathEmpty() {
		try {
			configurationComponent.getInteger(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetInteger2DefensePathNull() {
		try {
			configurationComponent.getInteger(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetInteger2DefensePathEmpty() {
		try {
			configurationComponent.getInteger(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetInteger3DefensePathNull() {
		try {
			configurationComponent.getInteger(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetInteger3DefensePathEmpty() {
		try {
			configurationComponent.getInteger(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPropertiesDefensePathNull() {
		try {
			configurationComponent.getProperties(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPropertiesDefensePathEmpty() {
		try {
			configurationComponent.getProperties(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetProperties2DefensePathNull() {
		try {
			configurationComponent.getProperties(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetProperties2DefensePathEmpty() {
		try {
			configurationComponent.getProperties(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetProperties3DefensePathNull() {
		try {
			configurationComponent.getProperties(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetProperties3DefensePathEmpty() {
		try {
			configurationComponent.getProperties(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStringDefensePathNull() {
		try {
			configurationComponent.getString(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStringDefensePathEmpty() {
		try {
			configurationComponent.getString(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetString2DefensePathNull() {
		try {
			configurationComponent.getString(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetString2DefensePathEmpty() {
		try {
			configurationComponent.getString(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetString3DefensePathNull() {
		try {
			configurationComponent.getString(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetString3DefensePathEmpty() {
		try {
			configurationComponent.getString(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStringListDefensePathNull() {
		try {
			configurationComponent.getStringList(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStringListDefensePathEmpty() {
		try {
			configurationComponent.getStringList(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStringList2DefensePathNull() {
		try {
			configurationComponent.getStringList(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStringList2DefensePathEmpty() {
		try {
			configurationComponent.getStringList(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStringList3DefensePathNull() {
		try {
			configurationComponent.getStringList(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStringList3DefensePathEmpty() {
		try {
			configurationComponent.getStringList(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testSetPropertyDefensePathNull() {
		try {
			configurationComponent.setProperty(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testSetPropertyDefensePathEmpty() {
		try {
			configurationComponent.setProperty(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testSetPropertyDefenseValueNull() {
		try {
			configurationComponent.setProperty(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_VALUE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testSetProperty2DefensePathNull() {
		try {
			configurationComponent.setProperty(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testSetProperty2DefensePathEmpty() {
		try {
			configurationComponent.setProperty(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testSetProperty2DefenseValueNull() {
		try {
			configurationComponent.setProperty(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_VALUE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testSetProperty3DefensePathNull() {
		try {
			configurationComponent.setProperty(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testSetProperty3DefensePathEmpty() {
		try {
			configurationComponent.setProperty(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_PATH_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testSetProperty3DefenseValueNull() {
		try {
			configurationComponent.setProperty(NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONFIGURATION_VALUE_NULL, ex.getErrorCode());
		}
	}
}
