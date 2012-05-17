/**
 * 
 */
package com.collibra.dgc.service.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.component.CommunityComponent;
import com.collibra.dgc.core.component.GroupComponent;
import com.collibra.dgc.core.component.VocabularyComponent;
import com.collibra.dgc.core.component.representation.TermComponent;
import com.collibra.dgc.core.dao.UserDao;
import com.collibra.dgc.core.dto.filters.PropertyFilter;
import com.collibra.dgc.core.dto.filters.PropertyFilter.FilterOperator;
import com.collibra.dgc.core.dto.filters.ResourceFilter;
import com.collibra.dgc.core.dto.sorters.PropertyOrder;
import com.collibra.dgc.core.exceptions.AuthorizationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.AuthorizationService;
import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.core.service.RightsService;
import com.collibra.dgc.core.service.UserService;
import com.collibra.dgc.service.AbstractBootstrappedServiceTest;

/**
 * Testing of the security infrastructure.
 * 
 * @author dieterwachters
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestSecurity extends AbstractBootstrappedServiceTest {
	@Autowired
	private AuthorizationService authService;
	@Autowired
	private RightsService rightsService;
	@Autowired
	private AuthorizationService authorizationService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserService userService;
	@Autowired
	private CommunityComponent communityComponent;
	@Autowired
	private VocabularyComponent vocabularyComponent;
	@Autowired
	private TermComponent termComponent;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private GroupComponent groupComponent;

	private UserData ADMIN_OBJ = null;

	@Before
	public void getAdmin() {

		ADMIN_OBJ = userService.getUser("Admin");
	}

	// TODO
	// * When assigning a permissions to a global role, this permissions is available for all the resources.

	@Test
	public void testBootstrap() {
		final Subject adminSubject = SecurityUtils.getSubject();
		try {
			adminSubject.login(new UsernamePasswordToken("Admin", "admin1906"));
		} catch (AuthenticationException e) {
			e.printStackTrace();
			fail();
		}

		assertEquals("Admin", adminSubject.getPrincipal());
		final User adminUser = userDao.findByUserName((String) adminSubject.getPrincipal());
		assertNotNull(adminUser);
	}

	@Test
	public void testGlobalRoles() {
		final Role role = rightsService.findRoleByName("Admin");
		try {
			rightsService.addMember(ADMIN_OBJ.getId(), role);
			fail();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			assertEquals(DGCErrorCodes.MEMBER_WITHOUT_RESOURCE_AND_RESOURCE_ROLE, e.getErrorCode());
		}

		final Role testGlobalRole = rightsService
				.createRole("TestGlobalRole", "Global role for testing purposes", true);
		rightsService.addMember(ADMIN_OBJ.getId(), testGlobalRole);

		authService.grant(testGlobalRole, Permissions.BFTF_ADD_DESCRIPTION);
		authService.grant(testGlobalRole, Permissions.ADMIN);

		final Role testRole = rightsService.createRole("TestRole", "Role for testing purposes", false);
		try {
			rightsService.addMember(ADMIN_OBJ.getId(), testRole);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(DGCErrorCodes.MEMBER_WITHOUT_RESOURCE_AND_RESOURCE_ROLE, e.getErrorCode());
		}

		final Vocabulary voc = vocabularyDao.findAttributeTypesVocabulary();
		rightsService.addMember(ADMIN_OBJ.getId(), testRole, voc);

		authService.grant(testRole, Permissions.BFTF_ADD_ATTRIBUTE);
		try {
			authService.grant(testRole, Permissions.ADMIN);
			fail();
		} catch (IllegalArgumentException e) {
			// This is ok.
		}
	}

	/**
	 * A test that creates a new user 'myadmin', a new admin role 'myadminrole', assigns it to that user and tries it
	 * out.
	 */
	@Test
	public void testNewAdminUser() {
		// Create the new user.
		final UserData myAdminUser = userService.addUser("myadmin", "pwd123");

		// Create the new role
		final Role myAdminRole = rightsService.createRole("myadminrole", "another admin role", true);

		// Assign the admin permissions to the new role
		authService.grant(myAdminRole, Permissions.ADMIN);

		// Now assign the admin role to the admin user.
		rightsService.addMember(myAdminUser.getId(), myAdminRole);
		login("myadmin", "pwd123");

		assertTrue(authService.isPermitted(Permissions.ADMIN));

		// Now we remote the permission from the role
		authService.revoke(myAdminRole, Permissions.ADMIN);
		assertFalse(authService.isPermitted(Permissions.ADMIN));

		try {
			// We don't have rights anymore to give ourself roles
			authService.grant(myAdminRole, Permissions.ADMIN);
			fail();
		} catch (AuthorizationException e) {
			// This is normal
		}

		login("Admin", "admin1906");
		authService.grant(myAdminRole, Permissions.ADMIN);
		login("myadmin", "pwd123");

		resetTransaction();

		assertTrue(authService.isPermitted(Permissions.ADMIN));

		rightsService.removeMember(myAdminUser.getId(), myAdminRole);

		assertFalse(authService.isPermitted(Permissions.ADMIN));

		try {
			// We don't have rights anymore to give ourself roles
			rightsService.addMember(myAdminUser.getId(), myAdminRole);
			fail();
		} catch (AuthorizationException e) {
			// This is normal
		}

		login("Admin", "admin1906");
		rightsService.addMember(myAdminUser.getId(), myAdminRole);
		login("myadmin", "pwd123");

		assertTrue(authService.isPermitted(Permissions.ADMIN));

		login("Admin", "admin1906");

		Group admins = groupComponent.addGroup("Admins");
		rightsService.addMember(admins.getId(), myAdminRole);
		rightsService.removeMember(myAdminUser.getId(), myAdminRole);
		login("myadmin", "pwd123");
		assertFalse(authService.isPermitted(Permissions.ADMIN));
		login("Admin", "admin1906");
		groupComponent.addUserToGroup(admins.getId(), myAdminUser.getId());
		login("myadmin", "pwd123");
		assertTrue(authService.isPermitted(Permissions.ADMIN));
		groupComponent.removeUserFromGroup(admins.getId(), myAdminUser.getId());
		assertFalse(authService.isPermitted(Permissions.ADMIN));
	}

	/**
	 * A test that will add a new community, vocabulary; create a new user, give him rights to add terms; try to play
	 * with it.
	 */
	@Test
	public void testNormalRights() {
		final Community community = communityComponent.addCommunity("security-test", "security-test-uri");
		resetTransaction();
		final Vocabulary vocabulary = vocabularyComponent.addVocabulary(community.getId().toString(),
				"security-vocabulary-uri", "security-vocabulary");
		resetTransaction();

		// Create the new user.
		final UserData testUser = userService.addUser("testUser", "pwd123");
		resetTransaction();

		login("testUser", "pwd123");

		try {
			termComponent.addTerm(vocabulary.getId().toString(), "Test Term");
			// We don't have rights yet, so this should not work.
			fail();
		} catch (AuthorizationException e) {
			// This is normal
		}

		login("Admin", "admin1906");

		final Role adminRole = rightsService.findRoleByName("Admin");
		rightsService.addMember(testUser.getId(), adminRole, vocabulary);

		login("testUser", "pwd123");
		// We use term 2 as we are still in the same transaction and Test Term isn't fully rolled back yet.
		termComponent.addTerm(vocabulary.getId().toString(), "Test Term2");

		login("Admin", "admin1906");

		rightsService.removeMember(testUser.getId(), adminRole, vocabulary);

		login("testUser", "pwd123");
		try {
			termComponent.addTerm(vocabulary.getId().toString(), "Test Term3");
			// We don't have rights yet, so this should not work.
			fail();
		} catch (AuthorizationException e) {
			// This is normal
		}
	}

	@Test
	public void testRegistration() {
		// Create the new normal user.
		final UserData normalUser = userService.addUser("normalUser", "pwd123");
		resetTransaction();

		configurationService.setProperty("core/security/public-registration", "false");

		logout();

		try {
			userService.addUser("normalUser2", "pwd123");
			fail();
		} catch (AuthorizationException e) {
			assertEquals(e.getErrorCode(), DGCErrorCodes.PUBLIC_REGISTRATIONS_NOT_ALLOWED);
		}

		login("Admin", "admin1906");

		userService.addUser("normalUser2", "pwd123");

		login("normalUser", "pwd123");

		try {
			userService.addUser("normalUser3", "pwd123");
			fail();
		} catch (AuthorizationException e) {
			assertEquals(e.getErrorCode(), DGCErrorCodes.PUBLIC_REGISTRATIONS_NOT_ALLOWED);
		}

		login("Admin", "admin1906");

		configurationService.setProperty("core/security/public-registration", "true");

		userService.addUser("normalUser4", "pwd123");
		login("normalUser", "pwd123");
		userService.addUser("normalUser5", "pwd123");

		logout();
		userService.addUser("normalUser6", "pwd123");
	}

	/**
	 * Test for the password validation
	 */
	@Test
	public void testPassword() {
		try {
			userService.addUser("normaluser", "n");
			fail();
		} catch (IllegalArgumentException e) {
		}

		try {
			userService.addUser("normaluser", "");
			fail();
		} catch (IllegalArgumentException e) {
		}

		userService.addUser("normaluser", "blah123");
	}

	/**
	 * Test that we can't remove the last option to have admin permissions.
	 */
	// @Test
	// TODO what is wrong here? It always fails on MySQL (and hangs Oracle?)
	@Ignore
	public void testRemoveAdminPermission() {
		// First we try to delete the admin user.
		try {
			userService.removeUser("Admin");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(DGCErrorCodes.LAST_USER_WITH_ADMIN_RIGHTS, e.getErrorCode());
		}

		// Now try to remove the Sysadmin role
		try {
			rightsService.removeRole("Sysadmin");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(DGCErrorCodes.LAST_ROLE_WITH_ADMIN_RIGHTS, e.getErrorCode());
		}

		// Now try to remove the membership of Admin in Sysadmin role.
		try {
			rightsService.removeMember("Admin", rightsService.findRoleByName("Sysadmin"));
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(DGCErrorCodes.LAST_MEMBER_WITH_ADMIN_RIGHTS, e.getErrorCode());
		}

		// New try to revoke the admin permissions from the Sysadmin role
		try {
			authorizationService.revoke(rightsService.findRoleByName("Sysadmin"), Permissions.ADMIN);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(DGCErrorCodes.LAST_ROLE_WITH_ADMIN_RIGHTS, e.getErrorCode());
		}

		// Adding another user with the Sysadmin role, should be deletable.
		UserData myadmin = userService.addUser("myadmin", "pwd123");
		resetTransaction();
		rightsService.addMember(myadmin.getId(), rightsService.findRoleByName("Sysadmin"));
		resetTransaction();
		userService.removeUser("myadmin");
		resetTransaction();

		// Adding another role with ADMIN permissions should be deletable.
		Role testGlobalRole = rightsService.createRole("TestGlobalRole", "Global role for testing purposes", true);
		rightsService.addMember(ADMIN_OBJ.getId(), testGlobalRole);
		authService.grant(testGlobalRole, Permissions.ADMIN);
		resetTransaction();
		rightsService.removeRole("TestGlobalRole");
		resetTransaction();

		testGlobalRole = rightsService.createRole("TestGlobalRole", "Global role for testing purposes", true);
		rightsService.addMember(ADMIN_OBJ.getId(), testGlobalRole);
		authService.grant(testGlobalRole, Permissions.ADMIN);
		resetTransaction();
		authorizationService.revoke(rightsService.findRoleByName("TestGlobalRole"), Permissions.ADMIN);
		resetTransaction();
	}

	@Test
	public void testChangePassword() {
		userService.addUser("user1", "user1");
		resetTransaction();
		userService.addUser("user2", "user2");
		resetTransaction();

		logout();

		try {
			userService.changePassword("user1", "another_password");
			fail();
		} catch (AuthorizationException e) {
			assertEquals(DGCErrorCodes.CHANGE_PASSWORD, e.getErrorCode());
		}

		login("user2", "user2");

		try {
			userService.changePassword("user1", "another_password");
			fail();
		} catch (AuthorizationException e) {
			assertEquals(DGCErrorCodes.CHANGE_PASSWORD, e.getErrorCode());
		}

		login("user1", "user1");
		userService.changePassword("user1", "another_password");

		login("Admin", "admin1906");
		userService.changePassword("user1", "another_password");
	}

	@Test
	public void testUpdateUser() {
		userService.addUser("user1", "user1");
		resetTransaction();
		userService.addUser("user2", "user2");
		resetTransaction();

		logout();

		try {
			userService.changeUser("user1", "My Name1", null, null);
			fail();
		} catch (AuthorizationException e) {
			assertEquals(DGCErrorCodes.CHANGE_USER, e.getErrorCode());
		}

		login("user2", "user2");

		try {
			userService.changeUser("user1", "My Name1", null, null);
			fail();
		} catch (AuthorizationException e) {
			assertEquals(DGCErrorCodes.CHANGE_USER, e.getErrorCode());
		}

		login("user1", "user1");
		userService.changeUser("user1", "My Name1", null, null);

		login("Admin", "admin1906");
		userService.changeUser("user1", "My Other Name1", null, null);
	}

	@Test
	public void testRemoveUser() {
		userService.addUser("user1", "user1");
		resetTransaction();
		userService.addUser("user2", "user2");
		resetTransaction();

		logout();

		try {
			userService.removeUser("user1");
			fail();
		} catch (AuthorizationException e) {
			assertEquals(DGCErrorCodes.REMOVE_USER, e.getErrorCode());
		}

		login("user2", "user2");
		try {
			userService.removeUser("user1");
			fail();
		} catch (AuthorizationException e) {
			assertEquals(DGCErrorCodes.REMOVE_USER, e.getErrorCode());
		}

		login("user1", "user1");
		userService.removeUser("user1");

		login("Admin", "admin1906");
		userService.removeUser("user2");
		resetTransaction();

		try {
			userService.removeUser("user2");
			fail();
		} catch (EntityNotFoundException e) {

		}

		Assert.assertNull(userService.getUser("user1"));
		Assert.assertNull(userService.getUser("user2"));
	}

	@Test
	public void testUserSearch() {
		userService.addUser("dieter", "dieter", "Dieter", "Wachters", "dieter@collibra.com");
		userService.addUser("dieter2", "dieter2", "Dieter", "Anders", "dietera@collibra.com");
		userService.addUser("felix", "felix", "Felix", "Van de Maele", "felix@collibra.com");
		userService.addUser("stijn", "stijn", "Stijn", "Christiaens", "stijn@collibra.com");
		userService.addUser("pierre", "pierre", "Pierre", "Malarme", "pierre@collibra.com");
		userService.addUser("pieter", "pieter", "Pieter", "Deleenheer", "pieter@collibra.com");
		userService.addUser("clovis", "clovis", "Clovis", "Six", "clovis@collibra.com");
		resetTransaction();

		Collection<UserData> found = userService.findUsers(new ResourceFilter(), 0, 10);
		assertEquals(8, found.size());

		final List<PropertyFilter> filter1 = new ArrayList<PropertyFilter>();
		filter1.add(new PropertyFilter("firstName", FilterOperator.STARTS_WITH, "p"));

		final List<PropertyOrder> order1 = new ArrayList<PropertyOrder>();
		order1.add(new PropertyOrder("emailAddress", false));

		found = userService.findUsers(new ResourceFilter(filter1, order1), 0, 10);
		assertEquals(2, found.size());
		assertEquals("pierre", found.iterator().next().getUserName());

		final List<PropertyOrder> order2 = new ArrayList<PropertyOrder>();
		order2.add(new PropertyOrder("lastName", false));

		found = userService.findUsers(new ResourceFilter(filter1, order2), 0, 10);
		assertEquals(2, found.size());
		assertEquals("pieter", found.iterator().next().getUserName());

		final List<PropertyFilter> filter2 = new ArrayList<PropertyFilter>();
		filter2.add(new PropertyFilter("firstName", FilterOperator.ENDS_WITH, "eter"));

		found = userService.findUsers(new ResourceFilter(filter2, order2), 0, 10);
		assertEquals(3, found.size());
		final Iterator<UserData> it = found.iterator();
		assertEquals("dieter2", it.next().getUserName());
		assertEquals("pieter", it.next().getUserName());
		assertEquals("dieter", it.next().getUserName());

	}
}
