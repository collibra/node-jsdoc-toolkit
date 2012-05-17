package com.collibra.dgc.service.constraint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.impl.BinaryFactTypeFormImpl;
import com.collibra.dgc.core.service.impl.ConstraintChecker;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * 
 * @author pmalarme
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestBFTFConstraintChecker extends AbstractServiceTest {

	@Autowired
	private ConstraintChecker constraintChecker;

	@Test
	public void testBinaryFactTypeFormOkConstraint() {

		try {

			constraintChecker.checkConstraints(createBinaryFactTypeForms(false, false));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testBinaryFactTypeFormsWithSameRole() {

		try {

			constraintChecker.checkConstraints(createBinaryFactTypeForms(true, false));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testBinaryFactTypeFormsWithSameTerm() {

		try {

			constraintChecker.checkConstraints(createBinaryFactTypeForms(false, true));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testBinaryFactTypeFormsWithSameRoleAndSameTerm() {

		try {

			constraintChecker.checkConstraints(createBinaryFactTypeForms(true, true));
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.BFTF_ALREADY_EXISTS, e.getErrorCode());
		}
	}

	// TODO Pierre: when the save will be implemented, the constraint will be checked automatically thus the constraint
	// checker call will not be needed
	private BinaryFactTypeForm createBinaryFactTypeForms(boolean sameRole, boolean sameTerm) {

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");
		Vocabulary vocabulary = representationFactory.makeVocabulary(community, "http://vocabulary.com", "Vocabulary");
		communityService.save(community);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Term term1 = representationFactory.makeTerm(vocabulary, "Test Term 1");
		representationService.save(term1);
		resetTransaction();

		term1 = representationService.findTermByResourceId(term1.getId());

		Term term2;

		if (sameTerm)
			term2 = term1;
		else
			term2 = representationFactory.makeTerm(vocabulary, "Test Term 2");

		representationService.save(term2);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		term1 = representationService.findTermByResourceId(term1.getId());

		String role1 = "Role 1";
		String role2;

		if (sameRole)
			role2 = role1;
		else
			role2 = "Role 2";

		BinaryFactTypeForm bftf1 = representationFactory.makeBinaryFactTypeForm(vocabulary, term1, role1, role1, term1);
		representationService.save(bftf1);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		term2 = representationService.findTermByResourceId(term2.getId());

		// TODO Pierre: use the factory
		BinaryFactTypeForm bftf2 = new BinaryFactTypeFormImpl(vocabulary, term2, role2, role2, term2,
				meaningFactory.makeBinaryFactType());
		// representationService.save(bftf2);
		// resetTransaction();

		return bftf2;
	}

}
