package com.collibra.dgc.service.constraint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.impl.ObjectTypeImpl;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.impl.VocabularyImpl;
import com.collibra.dgc.core.service.impl.ConstraintChecker;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * 
 * @author pmalarme
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestVocabularyConstraintChecker extends AbstractServiceTest {

	@Autowired
	private ConstraintChecker constraintChecker;

	@Test
	public void testVocabularyConstraintsOk() {

		try {

			constraintChecker.checkConstraints(createVocabularies(false, false, objectTypeDao.getMetaVocabularyType()));

		} catch (Exception e) {

			fail();
		}

	}

	@Test
	public void testVocabularyWithNameAlreadyExistsConstraint() {

		try {

			constraintChecker.checkConstraints(createVocabularies(true, false, objectTypeDao.getMetaVocabularyType()));
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.VOCABULARY_WITH_NAME_ALREADY_EXISTS, e.getErrorCode());
		}
	}

	@Test
	public void testVocabularyWithUriAlreadyExistsConstraint() {

		try {

			constraintChecker.checkConstraints(createVocabularies(false, true, objectTypeDao.getMetaVocabularyType()));
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.VOCABULARY_WITH_URI_ALREADY_EXISTS, e.getErrorCode());
		}
	}

	@Test
	public void testVocabularyInconsistentTypeConstraint() {

		try {

			constraintChecker.checkConstraints(createVocabularies(false, false, new ObjectTypeImpl()));
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.VOCABULARY_TYPE_INCONSISTENT, e.getErrorCode());
		}
	}

	@Test
	public void testIncorporateVocabularyConstraints() {

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");
		Vocabulary incorporatingVocabulary = representationFactory.makeVocabulary(community, "http://voc1", "voc1");
		Vocabulary incorporatedVocabulary = representationFactory.makeVocabulary(community, "http://voc2", "voc2");
		communityService.save(community);
		resetTransaction();

		incorporatingVocabulary = representationService.findVocabularyByResourceId(incorporatingVocabulary.getId());
		incorporatedVocabulary = representationService.findVocabularyByResourceId(incorporatedVocabulary.getId());

		incorporatingVocabulary.addIncorporatedVocabulary(incorporatedVocabulary);
		representationService.saveVocabulary(incorporatingVocabulary);
		resetTransaction();

		incorporatingVocabulary = representationService.findVocabularyByResourceId(incorporatingVocabulary.getId());
		incorporatedVocabulary = representationService.findVocabularyByResourceId(incorporatedVocabulary.getId());

		try {

			constraintChecker.checkIncorporateVocabularyConstraints(incorporatingVocabulary, incorporatedVocabulary);
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: add the right DGCErrorCodes
		}

		try {

			constraintChecker.checkIncorporateVocabularyConstraints(incorporatingVocabulary, incorporatingVocabulary);
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: add the right DGCErrorCodes
		}

		try {

			constraintChecker.checkIncorporateVocabularyConstraints(incorporatedVocabulary, incorporatingVocabulary);
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: add the right DGCErrorCodes
		}
	}

	@Test
	public void testDisincorporateVocabularyConstraints() {

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");
		Vocabulary incorporatingVocabulary = representationFactory.makeVocabulary(community, "http://voc1", "voc1");
		Vocabulary incorporatedVocabulary = representationFactory.makeVocabulary(community, "http://voc2", "voc2");
		communityService.save(community);
		resetTransaction();

		incorporatingVocabulary = representationService.findVocabularyByResourceId(incorporatingVocabulary.getId());
		incorporatedVocabulary = representationService.findVocabularyByResourceId(incorporatedVocabulary.getId());

		try {

			constraintChecker.checkDisincorporateVocabularyConstraints(incorporatingVocabulary, incorporatedVocabulary);
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: add the right DGCErrorCodes
		}

		try {

			constraintChecker.checkDisincorporateVocabularyConstraints(incorporatingVocabulary,
					representationService.findSbvrVocabulary());
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: add the right DGCErrorCodes
		}

	}

	// TODO Pierre: when the save will be implemented, the constraint will be checked automatically thus the constraint
	// checker call will not be needed
	private Vocabulary createVocabularies(boolean sameName, boolean sameUri, ObjectType voc2Type) {

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");

		String vocabulary1Name = "Test Vocabulary 1 Name";
		String vocabulary2Name;

		if (sameName)
			vocabulary2Name = vocabulary1Name;
		else
			vocabulary2Name = "Test Vocabulary 2 Name";

		String vocabulary1Uri = "http://vocabulary1.com";
		String vocabulary2Uri;

		if (sameUri)
			vocabulary2Uri = vocabulary1Uri;
		else
			vocabulary2Uri = "http://vocabulary2.com";

		Vocabulary vocabulary1 = representationFactory.makeVocabulary(community, vocabulary1Uri, vocabulary1Name);
		communityService.save(community);
		resetTransaction();

		// TODO Pierre: use the factory
		Vocabulary vocabulary2 = new VocabularyImpl(vocabulary2Uri, vocabulary2Name, community, voc2Type);

		return vocabulary2;
		// TODO Pierre: add the save
		// communityService.save(community2);
		// resetTransaction();
	}
}
