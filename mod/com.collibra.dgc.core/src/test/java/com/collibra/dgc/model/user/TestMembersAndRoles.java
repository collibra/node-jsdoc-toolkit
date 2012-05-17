package com.collibra.dgc.model.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;

/**
 * JUnits for {@link Member}s and {@link Role}s.
 * @author amarnath
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestMembersAndRoles {
	@Autowired
	private CommunityFactory communityFactory;
	@Autowired
	private RepresentationFactory representationFactory;
	// @Autowired
	// private UserFactory userFactory;

	private Community sc;
	private Community sp;
	private Vocabulary voc;

	@Before
	public void setup() {
		sc = communityFactory.makeCommunity("SC", "SC URI");
		sp = communityFactory.makeCommunity(sc, "SP", "SP URI");
		voc = representationFactory.makeVocabulary(sp, "VOC URI", "VOC");
	}

	@Test
	public void testCreateAdmin() {
		// // Create administrator role.
		// Role adminRole = userFactory.createRole("Admin", true, true);
		// Assert.assertNotNull(adminRole);
		//
		// // Create semantic community administrator
		// Member adminMember = userFactory.createMember("Admin", sc.getResourceId(), adminRole);
		// Assert.assertNotNull(adminMember);
		// Assert.assertTrue(adminMember.canView());
		// Assert.assertTrue(adminMember.canModify());
		// Assert.assertTrue(adminMember.canDelete());
		// Assert.assertEquals(String.valueOf(0x7), adminMember.getPermissions());
		//
		// // Create community administrator
		// adminMember = userFactory.createMember("Admin", sp, adminRole);
		// Assert.assertNotNull(adminMember);
		// Assert.assertTrue(adminMember.canView());
		// Assert.assertTrue(adminMember.canModify());
		// Assert.assertTrue(adminMember.canDelete());
		// Assert.assertEquals(String.valueOf(0x7), adminMember.getPermissions());
		//
		// // Create vocabulary community administrator
		// adminMember = userFactory.createMember("Admin", voc, adminRole);
		// Assert.assertNotNull(adminMember);
		// Assert.assertTrue(adminMember.canView());
		// Assert.assertTrue(adminMember.canModify());
		// Assert.assertTrue(adminMember.canDelete());
		// Assert.assertEquals(String.valueOf(0x7), adminMember.getPermissions());
	}

	@Test
	public void testCreateSteward() {
		// // Create administrator role.
		// Role stewardRole = userFactory.createRole("Steward", true);
		// Assert.assertNotNull(stewardRole);
		//
		// // Create semantic community administrator
		// Member stewardMember = userFactory.createMember("Steward", sc.getResourceId(), stewardRole);
		// Assert.assertNotNull(stewardMember);
		// Assert.assertTrue(stewardMember.canView());
		// Assert.assertTrue(stewardMember.canModify());
		// Assert.assertFalse(stewardMember.canDelete());
		// Assert.assertEquals(String.valueOf(0x3), stewardMember.getPermissions());
		//
		// // Create community administrator
		// stewardMember = userFactory.createMember("Steward", sp, stewardRole);
		// Assert.assertNotNull(stewardMember);
		// Assert.assertTrue(stewardMember.canView());
		// Assert.assertTrue(stewardMember.canModify());
		// Assert.assertFalse(stewardMember.canDelete());
		// Assert.assertEquals(String.valueOf(0x3), stewardMember.getPermissions());
		//
		// // Create vocabulary community administrator
		// stewardMember = userFactory.createMember("Steward", voc, stewardRole);
		// Assert.assertNotNull(stewardMember);
		// Assert.assertTrue(stewardMember.canView());
		// Assert.assertTrue(stewardMember.canModify());
		// Assert.assertFalse(stewardMember.canDelete());
		// Assert.assertEquals(String.valueOf(0x3), stewardMember.getPermissions());
	}

	@Test
	public void testCreateNormal() {
		// // Create administrator role.
		// Role normalRole = userFactory.createRole("Normal");
		// Assert.assertNotNull(normalRole);
		//
		// // Create semantic community administrator
		// Member normalMember = userFactory.createMember("Normal", sc.getResourceId(), normalRole);
		// Assert.assertNotNull(normalMember);
		// Assert.assertTrue(normalMember.canView());
		// Assert.assertFalse(normalMember.canModify());
		// Assert.assertFalse(normalMember.canDelete());
		// Assert.assertEquals(String.valueOf(0x1), normalMember.getPermissions());
		//
		// // Create community administrator
		// normalMember = userFactory.createMember("Normal", sp, normalRole);
		// Assert.assertNotNull(normalMember);
		// Assert.assertTrue(normalMember.canView());
		// Assert.assertFalse(normalMember.canModify());
		// Assert.assertFalse(normalMember.canDelete());
		// Assert.assertEquals(String.valueOf(0x1), normalMember.getPermissions());
		//
		// // Create vocabulary community administrator
		// normalMember = userFactory.createMember("Normal", voc, normalRole);
		// Assert.assertNotNull(normalMember);
		// Assert.assertTrue(normalMember.canView());
		// Assert.assertFalse(normalMember.canModify());
		// Assert.assertFalse(normalMember.canDelete());
		// Assert.assertEquals(String.valueOf(0x1), normalMember.getPermissions());
	}

	@Test
	public void testCreateSimilarMember() {
		// // Create administrator role.
		// Role normalRole = userFactory.createRole("Normal");
		// Assert.assertNotNull(normalRole);
		//
		// // Create semantic community administrator
		// Member normalMember = userFactory.createMember("Normal", sc.getResourceId(), normalRole);
		// Assert.assertNotNull(normalMember);
		// Assert.assertTrue(normalMember.canView());
		// Assert.assertFalse(normalMember.canModify());
		// Assert.assertFalse(normalMember.canDelete());
		// Assert.assertEquals(String.valueOf(0x1), normalMember.getPermissions());
		//
		// Member anotherNormalUser = userFactory.createSimilarMember("AnotherNormalUser", normalMember);
		// Assert.assertNotNull(anotherNormalUser);
		// Assert.assertEquals(normalMember.getPermissions(), anotherNormalUser.getPermissions());
	}

	@Test
	public void testCreateSimilarRole() {
		// // Create administrator role.
		// Role adminRole = userFactory.createRole("Admin", true, true);
		// Assert.assertNotNull(adminRole);
		//
		// Role similarRole = userFactory.createSimilarRole("Similar Admin", adminRole);
		// Assert.assertNotNull(similarRole);
		// Assert.assertEquals(adminRole.getPermissions(), similarRole.getPermissions());
	}
}
