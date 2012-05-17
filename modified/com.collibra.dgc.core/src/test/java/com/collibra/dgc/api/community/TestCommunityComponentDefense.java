package com.collibra.dgc.api.community;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCommunityComponentDefense extends AbstractDGCBootstrappedApiTest {
	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";

	@Test
	public void testAddCommunityDefenseNameNull() {
		try {
			communityComponent.addCommunity(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCommunityDefenseNameEmpty() {
		try {
			communityComponent.addCommunity(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCommunityDefenseUriNull() {
		try {
			communityComponent.addCommunity(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_URI_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCommunityDefenseUriEmpty() {
		try {
			communityComponent.addCommunity(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_URI_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCommunity2DefenseNameNull() {
		try {
			communityComponent.addCommunity(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCommunity2DefenseNameEmpty() {
		try {
			communityComponent.addCommunity(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCommunity2DefenseUriNull() {
		try {
			communityComponent.addCommunity(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_URI_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCommunity2DefenseUriEmpty() {
		try {
			communityComponent.addCommunity(NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_URI_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCommunity2DefenseLanguageNull() {
		try {
			communityComponent.addCommunity(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_LANGUAGE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCommunity2DefenseLanguageEmpty() {
		try {
			communityComponent.addCommunity(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_LANGUAGE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSubCommunityDefenseParentRIdNull() {
		try {
			communityComponent.addSubCommunity(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSubCommunityDefenseParentRIdEmpty() {
		try {
			communityComponent.addSubCommunity(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSubCommunityDefenseNameNull() {
		try {
			communityComponent.addSubCommunity(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSubCommunityDefenseNameEmpty() {
		try {
			communityComponent.addSubCommunity(NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSubCommunityDefenseUriNull() {
		try {
			communityComponent.addSubCommunity(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_URI_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSubCommunityDefenseUriEmpty() {
		try {
			communityComponent.addSubCommunity(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_URI_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSubCommunity2DefenseParentRIdNull() {
		try {
			communityComponent.addSubCommunity(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSubCommunity2DefenseParentRIdEmpty() {
		try {
			communityComponent.addSubCommunity(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSubCommunity2DefenseNameNull() {
		try {
			communityComponent.addSubCommunity(NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSubCommunity2DefenseNameEmpty() {
		try {
			communityComponent.addSubCommunity(NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSubCommunity2DefenseUriNull() {
		try {
			communityComponent.addSubCommunity(NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_URI_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSubCommunity2DefenseUriEmpty() {
		try {
			communityComponent.addSubCommunity(NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_URI_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSubCommunity2DefenseLanguageNull() {
		try {
			communityComponent.addSubCommunity(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_LANGUAGE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddSubCommunity2DefenseLanguageEmpty() {
		try {
			communityComponent.addSubCommunity(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_LANGUAGE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeLanguageDefenseRIdNull() {
		try {
			communityComponent.changeLanguage(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeLanguageDefenseRIdEmpty() {
		try {
			communityComponent.changeLanguage(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeLanguageDefenseLanguageNull() {
		try {
			communityComponent.changeLanguage(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_LANGUAGE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeLanguageDefenseLanguageEmpty() {
		try {
			communityComponent.changeLanguage(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_LANGUAGE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeParentCommunityDefenseParentRIdNull() {
		try {
			communityComponent.changeParentCommunity(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeParentCommunityDefenseParentRIdEmpty() {
		try {
			communityComponent.changeParentCommunity(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeParentCommunityDefenseCommunityRIdNull() {
		try {
			Community c = createCommunity();
			communityComponent.changeParentCommunity(c.getId(), NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeParentCommunityDefenseCommunityRIdEmpty() {
		try {
			Community c = createCommunity();
			communityComponent.changeParentCommunity(c.getId(), EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeUriDefenseRIdNull() {
		try {
			communityComponent.changeUri(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeUriDefenseRIdEmpty() {
		try {
			communityComponent.changeUri(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeUriDefenseUriNull() {
		try {
			communityComponent.changeUri(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_URI_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeUriDefenseUriEmpty() {
		try {
			communityComponent.changeUri(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_URI_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindCommunitiesContainingNameDefenseSearchNameNull() {
		try {
			communityComponent.findCommunitiesContainingName(NULL, 0, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindCommunitiesContainingNameDefenseSearchNameEmpty() {
		try {
			communityComponent.findCommunitiesContainingName(EMPTY, 0, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCommunityDefenseRIdNull() {
		try {
			communityComponent.getCommunity(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCommunityDefenseRIdEmpty() {
		try {
			communityComponent.getCommunity(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCommunityByNameDefenseNameNull() {
		try {
			communityComponent.getCommunityByName(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCommunityByNameDefenseNameEmpty() {
		try {
			communityComponent.getCommunityByName(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCommunityByUriDefenseUriNull() {
		try {
			communityComponent.getCommunityByUri(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_URI_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCommunityByUriDefenseUriEmpty() {
		try {
			communityComponent.getCommunityByUri(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_URI_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveCommunityDefenseRIdNull() {
		try {
			communityComponent.removeCommunity(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveCommunityDefenseRIdEmpty() {
		try {
			communityComponent.removeCommunity(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveParentCommunityDefenseRIdNull() {
		try {
			communityComponent.removeParentCommunity(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveParentCommunityDefenseRIdEmpty() {
		try {
			communityComponent.removeParentCommunity(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_EMPTY, ex.getErrorCode());
		}
	}
}
