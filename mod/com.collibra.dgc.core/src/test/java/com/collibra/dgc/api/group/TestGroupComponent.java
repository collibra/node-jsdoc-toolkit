package com.collibra.dgc.api.group;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.UserData;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestGroupComponent extends AbstractDGCBootstrappedApiTest {

	@Test
	public void testAddGroup() {
		Group g = groupComponent.addGroup("Admins");
		Group g2 = groupComponent.getGroup(g.getId());
		Assert.assertEquals(g.getId(), g2.getId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testRemoveGroup() {
		Group g = groupComponent.addGroup("Admins");
		groupComponent.removeGroup(g.getId());
		groupComponent.getGroup(g.getId());
	}

	@Test
	public void testAddUserToGroup() {
		Group g = groupComponent.addGroup("Admins");
		UserData u = userService.getUser("Admin");
		g = groupComponent.addUserToGroup(g.getId(), u.getId());
		Assert.assertEquals(1, groupComponent.getGroupsForUser(u.getId()).size());
	}

	@Test
	public void testRemoveUserFromGroup() {
		Group g = groupComponent.addGroup("Admins");
		UserData u = userService.getUser("Admin");
		g = groupComponent.addUserToGroup(g.getId(), u.getId());
		Assert.assertEquals(1, groupComponent.getGroupsForUser(u.getId()).size());
		groupComponent.removeUserFromGroup(g.getId(), u.getId());
		Assert.assertEquals(0, groupComponent.getGroupsForUser(u.getId()).size());
	}
}
