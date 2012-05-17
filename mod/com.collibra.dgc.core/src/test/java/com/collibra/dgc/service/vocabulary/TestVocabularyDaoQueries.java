package com.collibra.dgc.service.vocabulary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.dao.VocabularyDao;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.service.AbstractBootstrappedServiceTest;

/**
 * Tests for vocabularies searching in {@link VocabularyDao}
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestVocabularyDaoQueries extends AbstractBootstrappedServiceTest {
	private Vocabulary vocabulary1;
	private UserData u1 = null;

	@Test
	public void testGetVocabularies() throws Exception {
		List<Vocabulary> vocabularies = representationService.findVocabularies();
		int oldsize = vocabularies.size();

		// create vocabulary 1
		setup();

		// create vocabulary 2
		sp = communityService.findCommunity(sp.getId());
		Vocabulary vocabulary2 = representationFactory.makeVocabulary(sp, "Voc 2", "Voc 2");
		representationFactory.makeTerm(vocabulary2, "Term1");

		communityService.save(sp);
		resetTransaction();

		vocabularies = representationService.findVocabularies();

		assertEquals(2, vocabularies.size() - oldsize);
	}

	Community sp;

	private void setup() {
		sp = communityFactory.makeCommunity("SP", "SPRUI");
		vocabulary1 = representationFactory.makeVocabulary(sp, "My Test", "Voc Query Test");
		representationFactory.makeTerm(vocabulary1, "Term1");
		representationFactory.makeTerm(vocabulary1, "Term2");

		communityService.save(sp);
		resetTransaction();

		// Assert for version.
		vocabulary1 = representationService.findVocabularyByName(vocabulary1.getName());
		assertNotNull(vocabulary1);

		// Add term.
		representationFactory.makeTerm(vocabulary1, "Term3");
		representationService.saveVocabulary(vocabulary1);
		resetTransaction();

		// Assert for version.
		vocabulary1 = representationService.findVocabularyByName(vocabulary1.getName());
		assertNotNull(vocabulary1);

		// Add term.
		representationFactory.makeTerm(vocabulary1, "Term4");
		representationService.saveVocabulary(vocabulary1);
		resetTransaction();

		// Assert for version.
		vocabulary1 = representationService.findVocabularyByName(vocabulary1.getName());
		assertNotNull(vocabulary1);

		u1 = userService.addUser("User 1", "password");
	}

	@Test
	public void testFindVocabulariesByMember() {
		setup();

		Member member1 = rightsService.addMember(u1.getId(), Constants.STEWARD, vocabulary1);

		resetTransaction();

		Role steward = rightsService.findRoleByName(Constants.STEWARD);
		List<Vocabulary> memberVocs = representationService.findVocabulariesByMember(member1.getOwnerId(), steward, 0,
				10);

		for (Vocabulary v : memberVocs) {
			System.out.println(v.verbalise());
		}
		assertEquals(1, memberVocs.size());
	}
}
