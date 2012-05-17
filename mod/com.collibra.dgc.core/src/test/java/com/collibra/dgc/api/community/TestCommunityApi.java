package com.collibra.dgc.api.community;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.community.Community;

/**
 * Community API tests
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCommunityApi extends AbstractDGCBootstrappedApiTest {

	@Test
	public void testAddCommunity() {
		Community community = createCommunity();
		Assert.assertNotNull(community);
		Assert.assertEquals(COMMUNITY_NAME, community.getName());
		Assert.assertEquals(COMMUNITY_URI, community.getUri());
	}

	@Test
	public void testAddCommunityWithLanguage() {
		Community community = communityComponent.addCommunity(COMMUNITY_NAME, COMMUNITY_URI, "Java");
		Assert.assertNotNull(community);
		Assert.assertEquals(COMMUNITY_NAME, community.getName());
		Assert.assertEquals(COMMUNITY_URI, community.getUri());
		Assert.assertEquals("Java", community.getLanguage());
	}

	@Test
	public void testAddSubCommunity() {
		Community community = createCommunity();
		Community subCommunity = communityComponent.addSubCommunity(community.getId().toString(), "sub-name",
				"sub-uri");
		Assert.assertNotNull(subCommunity);
		Assert.assertEquals("sub-name", subCommunity.getName());
		Assert.assertEquals("sub-uri", subCommunity.getUri());
	}

	@Test
	public void testAddSubCommunityWithLanguage() {
		Community community = createCommunity();
		Community subCommunity = communityComponent.addSubCommunity(community.getId().toString(), "sub-name",
				"sub-uri", "Java");
		Assert.assertNotNull(subCommunity);
		Assert.assertEquals("sub-name", subCommunity.getName());
		Assert.assertEquals("sub-uri", subCommunity.getUri());
		Assert.assertEquals("Java", subCommunity.getLanguage());
	}

	@Test
	public void testGetCommunitiesByName() {
		createCommunity();
		Community community = communityComponent.getCommunityByName(COMMUNITY_NAME);
		Assert.assertNotNull(community);
		Assert.assertEquals(COMMUNITY_NAME, community.getName());
		Assert.assertEquals(COMMUNITY_URI, community.getUri());
	}

	@Test
	public void testGetCommunity() {
		Community community = createCommunity();
		Community result = communityComponent.getCommunity(community.getId().toString());
		Assert.assertNotNull(result);
		Assert.assertEquals(community.getName(), result.getName());
		Assert.assertEquals(community.getUri(), result.getUri());
	}

	@Test
	public void testGetCommunityByUri() {
		Community community = createCommunity();
		Community result = communityComponent.getCommunityByUri(community.getUri());
		Assert.assertNotNull(result);
		Assert.assertEquals(community.getName(), result.getName());
		Assert.assertEquals(community.getId(), result.getId());
	}

	@Test
	public void testFindCommunitiesContainingName() {
		createCommunity();
		final Community c1 = communityComponent.addCommunity(COMMUNITY_NAME + "2", COMMUNITY_URI + "2");
		communityComponent.addSubCommunity(c1.getId().toString(), COMMUNITY_NAME + "3", COMMUNITY_URI + "3");
		communityComponent.addCommunity("SC4", "SC4 URI");

		Collection<Community> communities = communityComponent.findCommunitiesContainingName(COMMUNITY_NAME, 0, 100);
		Assert.assertEquals(3, communities.size());
		assertCommunityExists(communities, COMMUNITY_NAME, COMMUNITY_URI);
		assertCommunityExists(communities, COMMUNITY_NAME + "2", COMMUNITY_URI + "2");
		assertCommunityExists(communities, COMMUNITY_NAME + "3", COMMUNITY_URI + "3");
	}

	@Test
	public void testGetAllCommunities() {
		createCommunity();
		communityComponent.addCommunity(COMMUNITY_NAME + "2", COMMUNITY_URI + "2");

		Collection<Community> communities = communityComponent.getCommunities();
		Assert.assertEquals(2 + 3, communities.size());
		assertCommunityExists(communities, COMMUNITY_NAME, COMMUNITY_URI);
		assertCommunityExists(communities, COMMUNITY_NAME + "2", COMMUNITY_URI + "2");
		assertCommunityExists(communities, Constants.METAMODEL_COMMUNITY_NAME, Constants.METAMODEL_COMMUNITY_URI);

		// Exclude Meta and SBVR
		communities = communityComponent.getCommunities(true, true);
		Assert.assertEquals(2, communities.size());
		assertCommunityExists(communities, COMMUNITY_NAME, COMMUNITY_URI);
		assertCommunityExists(communities, COMMUNITY_NAME + "2", COMMUNITY_URI + "2");
	}

	@Test
	public void testRemoveCommunity() {
		Community community = createCommunity();
		Assert.assertNotNull(community);
		Assert.assertNotNull(communityComponent.getCommunity(community.getId().toString()));

		communityComponent.removeCommunity(community.getId().toString());

		boolean doesntExist = false;

		try {
			communityComponent.getCommunity(community.getId().toString());
		} catch (EntityNotFoundException ex) {
			doesntExist = true;
		}

		Assert.assertTrue(doesntExist);
	}

	@Test
	public void testChangeParentCommunity() {
		Community community = createCommunity();
		Community parent = communityComponent.addCommunity(COMMUNITY_NAME + " PARENT", COMMUNITY_URI + " PARENT ");
		community = communityComponent.changeParentCommunity(parent.getId().toString(), community
				.getId().toString());

		Community parentResult = community.getParentCommunity();
		Assert.assertNotNull(parentResult);
		Assert.assertEquals(parent.getName(), parentResult.getName());
		Assert.assertEquals(parent.getUri(), parentResult.getUri());
	}

	@Test
	public void testRemoveParentCommunity() {
		Community community = createCommunity();
		final Community parent = communityComponent
				.addCommunity(COMMUNITY_NAME + " PARENT", COMMUNITY_URI + " PARENT ");
		final Community c = communityComponent.changeParentCommunity(parent.getId().toString(), community
				.getId().toString());

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				// Check that the parent community is not null
				Assert.assertNotNull(c.getParentCommunity());
				Assert.assertEquals(c.getParentCommunity().getId(), parent.getId());
				Community p = communityComponent.getCommunity(parent.getId().toString());
				Assert.assertTrue(p.getSubCommunities().iterator().next().equals(c));

				// Remove the parent community
				Community c2 = communityComponent.removeParentCommunity(c.getId().toString());
				Assert.assertNull(c2.getParentCommunity());
				Assert.assertTrue(parent.getSubCommunities().isEmpty());
			}
		});
	}

	@Test
	public void testchangeName() {
		Community community = createCommunity();
		community = communityComponent.changeName(community.getId().toString(), COMMUNITY_NAME + "NEW");

		Assert.assertEquals(COMMUNITY_NAME + "NEW", community.getName());
	}

	@Test
	public void testchangeUri() {
		Community community = createCommunity();
		community = communityComponent.changeUri(community.getId().toString(), COMMUNITY_URI + "NEW");

		Assert.assertEquals(COMMUNITY_URI + "NEW", community.getUri());
	}

	private void assertCommunityExists(Collection<? extends Community> communities, String name, String uri) {
		for (Community community : communities) {
			if (community.getName().equals(name) && community.getUri().equals(uri)) {
				return;
			}
		}

		Assert.fail("Community with name '" + name + "' and URI '" + uri + "' not found");
	}
}
