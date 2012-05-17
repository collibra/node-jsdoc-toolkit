package com.collibra.dgc.api.rights;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.component.RightsComponent;
import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.model.user.impl.RoleImpl;

/**
 * {@link RightsComponent} API tests.
 * 
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestRightsComponent extends AbstractDGCBootstrappedApiTest {
	private static final String USER1 = "user1";
	private static final String USER2 = "user2";
	private static final String USER3 = "user3";
	private static UserData USER1_OBJ;
	private static UserData USER2_OBJ;
	private static UserData USER3_OBJ;
	private static final String MY_ROLE = "MyRole";
	private static final String MY_ROLE_DESC = "MyRole Description";

	private Vocabulary voc;
	private Term term;

	@Before
	public void initialize() {
		voc = createVocabulary();
		term = termComponent.addTerm(voc.getId().toString(), "Term");

		USER1_OBJ = userComponent.addUser(USER1, USER1);
		USER2_OBJ = userComponent.addUser(USER2, USER2);
		USER3_OBJ = userComponent.addUser(USER3, USER3);
	}

	@Test
	public void testAddMember() {
		Member member = rightsComponent.addMember(USER1_OBJ.getId(), Constants.ADMIN, voc.getId().toString());
		Assert.assertNotNull(member);
		Assert.assertEquals(USER1_OBJ.getId(), member.getOwnerId());
		Assert.assertEquals(voc.getId(), member.getResourceId());
	}

	@Test
	public void testAddMemberOnGlossary() {
		Member member = rightsComponent.addMember(USER1_OBJ.getId(), Constants.SYSADMIN);
		Assert.assertNotNull(member);
		Assert.assertEquals(USER1_OBJ.getId(), member.getOwnerId());
		Assert.assertNull(member.getResourceId());
	}

	@Test
	public void testChangeMemberRole() {
		rightsComponent.addMember(USER1_OBJ.getId(), Constants.ADMIN, voc.getId().toString());
		Assert.assertNotNull(rightsComponent.getMember(USER1_OBJ.getId(), voc.getId().toString(), Constants.ADMIN));
		boolean result = rightsComponent.changeMemberRole(USER1_OBJ.getId(), Constants.ADMIN, voc.getId().toString(),
				Constants.STEWARD);
		Assert.assertTrue(result);
		Assert.assertNull(rightsComponent.getMember(USER1_OBJ.getId(), voc.getId().toString(), Constants.ADMIN));
		Assert.assertNotNull(rightsComponent.getMember(USER1_OBJ.getId(), voc.getId().toString(), Constants.STEWARD));
	}

	@Test
	public void testCreateRole() {
		Role role = rightsComponent.addRole(MY_ROLE, MY_ROLE_DESC, false);
		Assert.assertNotNull(role);
		Assert.assertEquals(MY_ROLE, role.getName());
	}

	@Test
	public void testGetMember() {
		rightsComponent.addMember(USER1_OBJ.getId(), Constants.ADMIN, voc.getId().toString());
		Assert.assertNotNull(rightsComponent.getMember(USER1_OBJ.getId(), voc.getId().toString(), Constants.ADMIN));
		Assert.assertNull(rightsComponent.getMember(USER1, voc.getId().toString(), Constants.STEWARD));
	}

	@Test
	public void testGetMemberRoles() {
		rightsComponent.addMember(USER1_OBJ.getId(), Constants.ADMIN, voc.getId().toString());
		rightsComponent.addMember(USER1_OBJ.getId(), Constants.STEWARD, voc.getId().toString());
		rightsComponent.addMember(USER1_OBJ.getId(), Constants.NORMAL, voc.getId().toString());

		Collection<Member> members = rightsComponent.getMemberRoles(USER1_OBJ.getId(), voc.getId().toString());
		Assert.assertEquals(3, members.size());
		assertRoleExists(members, Constants.ADMIN, Constants.STEWARD, Constants.NORMAL);
	}

	@Test
	public void testGetMembers() {
		rightsComponent.addMember(USER1_OBJ.getId(), Constants.ADMIN, voc.getId().toString());
		rightsComponent.addMember(USER2_OBJ.getId(), Constants.STEWARD, voc.getId().toString());
		Collection<Member> members = rightsComponent.getMembers(Collections.singleton(voc.getId().toString()));
		Assert.assertEquals(2, members.size());
		assertUsersExists(members, USER1_OBJ, USER2_OBJ);

		rightsComponent.addMember(USER2_OBJ.getId(), Constants.ADMIN, voc.getId().toString());
		rightsComponent.addMember(USER3_OBJ.getId(), Constants.STAKEHOLDER, voc.getId().toString());
		List<String> resourceIds = new ArrayList<String>(10);
		resourceIds.add(voc.getId().toString());
		resourceIds.add(term.getId().toString());
		members = rightsComponent.getMembers(resourceIds);
		Assert.assertEquals(4, members.size());
		assertUsersExists(members, USER1_OBJ, USER2_OBJ, USER3_OBJ);
	}

	@Test
	public void testGetMembersByResourceAndRole() {
		rightsComponent.addMember(USER1_OBJ.getId(), Constants.ADMIN, voc.getId().toString());
		rightsComponent.addMember(USER2_OBJ.getId(), Constants.ADMIN, voc.getId().toString());
		rightsComponent.addMember(USER2_OBJ.getId(), Constants.STAKEHOLDER, voc.getId().toString());
		rightsComponent.addMember(USER3_OBJ.getId(), Constants.STEWARD, voc.getId().toString());
		Collection<Member> members = rightsComponent.getMembersByResourceAndRole(voc.getId().toString(),
				Constants.ADMIN);
		Assert.assertEquals(2, members.size());
		assertUsersExists(members, USER1_OBJ, USER2_OBJ);
	}

	@Test
	public void testGetMembersByRole() {
		rightsComponent.addMember(USER1_OBJ.getId(), Constants.STAKEHOLDER, voc.getId().toString());
		rightsComponent.addMember(USER2_OBJ.getId(), Constants.STEWARD, voc.getId().toString());
		rightsComponent.addMember(USER2_OBJ.getId(), Constants.ADMIN, voc.getId().toString());
		rightsComponent.addMember(USER3_OBJ.getId(), Constants.STAKEHOLDER, term.getId().toString());
		Collection<Member> members = rightsComponent.getMembersByRole(Constants.STAKEHOLDER);
		Assert.assertEquals(2, members.size());
	}

	@Test
	public void testGetMembersByUser() {
		rightsComponent.addMember(USER1_OBJ.getId(), Constants.STAKEHOLDER, voc.getId().toString());
		rightsComponent.addMember(USER3_OBJ.getId(), Constants.STEWARD, voc.getId().toString());
		rightsComponent.addMember(USER3_OBJ.getId(), Constants.ADMIN, voc.getId().toString());
		rightsComponent.addMember(USER1_OBJ.getId(), Constants.STAKEHOLDER, term.getId().toString());
		Collection<Member> members = rightsComponent.getMembersByOwner(USER1_OBJ.getId());
		Assert.assertEquals(2, members.size());

		members = rightsComponent.getMembersByOwner(USER2_OBJ.getId());
		Assert.assertEquals(0, members.size());

		members = rightsComponent.getMembersByOwner(USER3_OBJ.getId());
		Assert.assertEquals(2, members.size());
	}

	@Test
	public void testGetRole() {
		Role role = rightsComponent.getRoleByName(Constants.ADMIN);
		Assert.assertNotNull(role);
		Assert.assertNull(rightsComponent.getRoleByName("NON EXISTENT"));
	}

	@Test
	public void testGetUsers() {
		rightsComponent.addMember(USER1_OBJ.getId(), Constants.STAKEHOLDER, voc.getId().toString());
		rightsComponent.addMember(USER2_OBJ.getId(), Constants.STEWARD, voc.getId().toString());
		rightsComponent.addMember(USER3_OBJ.getId(), Constants.ADMIN, voc.getId().toString());
		rightsComponent.addMember(USER1_OBJ.getId(), Constants.STAKEHOLDER, term.getId().toString());

		Collection<String> users = rightsComponent.getOwners(voc.getId().toString());
		Assert.assertEquals(3, users.size());
		Assert.assertTrue(users.contains(USER1_OBJ.getId()));
		Assert.assertTrue(users.contains(USER2_OBJ.getId()));
		Assert.assertTrue(users.contains(USER3_OBJ.getId()));

		users = rightsComponent.getOwners(term.getId().toString());
		Assert.assertEquals(1, users.size());
		Assert.assertTrue(users.contains(USER1_OBJ.getId()));

		List<String> resources = new ArrayList<String>(10);
		resources.add(voc.getId().toString());
		resources.add(term.getId().toString());
		users = rightsComponent.getOwners(resources);
		Assert.assertEquals(3, users.size());
		Assert.assertTrue(users.contains(USER1_OBJ.getId()));
		Assert.assertTrue(users.contains(USER2_OBJ.getId()));
		Assert.assertTrue(users.contains(USER3_OBJ.getId()));
	}

	@Test
	public void testRemoveMember() {
		rightsComponent.addMember(USER1_OBJ.getId(), Constants.STAKEHOLDER, voc.getId().toString());
		Assert.assertNotNull(rightsComponent.getMember(USER1_OBJ.getId(), voc.getId().toString(), Constants.STAKEHOLDER));

		rightsComponent.removeMember(USER1_OBJ.getId(), Constants.STAKEHOLDER, voc.getId().toString());
		Assert.assertNull(rightsComponent.getMember(USER1, voc.getId().toString(), Constants.STAKEHOLDER));
	}

	@Test
	public void testRemoveMemberFromGlossary() {
		rightsComponent.addMember(USER1_OBJ.getId(), Constants.SYSADMIN);
		Assert.assertNotNull(rightsComponent.getMember(USER1_OBJ.getId(), null, Constants.SYSADMIN));
		rightsComponent.removeMember(USER1_OBJ.getId(), Constants.SYSADMIN);
		Assert.assertNull(rightsComponent.getMember(USER1_OBJ.getId(), null, Constants.SYSADMIN));
	}

	@Test
	public void testRemoveRole() {
		// Remove role when no members present.
		rightsComponent.addRole(MY_ROLE, MY_ROLE_DESC, false);
		Assert.assertNotNull(rightsComponent.getRoleByName(MY_ROLE));

		rightsComponent.removeRole(MY_ROLE);
		Assert.assertNull(rightsComponent.getRoleByName(MY_ROLE));

		// Remove role and members but not the term.
		Role role = rightsComponent.addRole(MY_ROLE, MY_ROLE_DESC, false);
		Term roleTerm = termComponent.getTerm(role.getTerm().getId().toString());

		rightsComponent.addMember(USER1_OBJ.getId(), MY_ROLE, voc.getId().toString());
		rightsComponent.addMember(USER2_OBJ.getId(), MY_ROLE, term.getId().toString());
		rightsComponent.removeRole(MY_ROLE, true, false);

		Assert.assertNull(rightsComponent.getMember(USER1_OBJ.getId(), voc.getId().toString(), MY_ROLE));
		Assert.assertNull(rightsComponent.getMember(USER2_OBJ.getId(), term.getId().toString(), MY_ROLE));
		Assert.assertNotNull(termComponent.getTerm(roleTerm.getId().toString()));

		// Remove role, members and the term.
		role = rightsComponent.addRole(MY_ROLE, MY_ROLE_DESC, false);
		roleTerm = termComponent.getTerm(role.getTerm().getId().toString());

		rightsComponent.addMember(USER1_OBJ.getId(), MY_ROLE, voc.getId().toString());
		rightsComponent.removeRole(MY_ROLE, true, true);

		Assert.assertNull(rightsComponent.getMember(USER1_OBJ.getId(), voc.getId().toString(), MY_ROLE));
		try {
			termComponent.getTerm(roleTerm.getId().toString());
			Assert.fail();
		} catch (EntityNotFoundException e) {
		}

		// Remove fails if members exist.
		role = rightsComponent.addRole(MY_ROLE, MY_ROLE_DESC, false);
		roleTerm = termComponent.getTerm(role.getTerm().getId().toString());

		rightsComponent.addMember(USER1_OBJ.getId(), MY_ROLE, voc.getId().toString());
		Assert.assertNull(rightsComponent.removeRoleIfNoMembersExist(MY_ROLE));
	}

	private void assertUsersExists(Collection<Member> members, UserData... users) {
		for (UserData user : users) {
			boolean exists = false;
			for (Member mem : members) {
				if (user.getId().equals(mem.getOwnerId())) {
					exists = true;
					break;
				}
			}

			Assert.assertTrue(exists);
		}
	}

	private void assertRoleExists(Collection<Member> members, String... roles) {
		for (String roleString : roles) {
			Role role = rightsComponent.getRoleByName(roleString);
			boolean exists = false;
			for (Member mem : members) {
				if (((RoleImpl) mem.getRole()).getRoleId() == ((RoleImpl) role).getRoleId()) {
					exists = true;
					break;
				}
			}

			Assert.assertTrue(exists);
		}
	}
}
