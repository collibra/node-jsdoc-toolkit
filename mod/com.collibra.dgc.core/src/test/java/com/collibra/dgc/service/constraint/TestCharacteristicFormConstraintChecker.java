package com.collibra.dgc.service.constraint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.representation.facttypeform.impl.CharacteristicFormImpl;
import com.collibra.dgc.core.service.impl.ConstraintChecker;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * 
 * @author pmalarme
 * 
 */
@Transactional
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCharacteristicFormConstraintChecker extends AbstractServiceTest {

	@Autowired
	private ConstraintChecker constraintChecker;

	@Test
	public void testCharacteristicFormOkConstraint() {

		try {

			constraintChecker.checkConstraints(createCharacteristicForms(false, false));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testCharacteristicFormsWithSameRole() {

		try {

			constraintChecker.checkConstraints(createCharacteristicForms(true, false));

		} catch (Exception e) {

			e.printStackTrace();

			fail();
		}
	}

	@Test
	public void testCharacteristicFormsWithSameTerm() {

		try {

			constraintChecker.checkConstraints(createCharacteristicForms(false, true));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testCharacteristicFormsWithSameRoleAndSameTerm() {

		try {

			constraintChecker.checkConstraints(createCharacteristicForms(true, true));
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.CF_ALREADY_EXISTS, e.getErrorCode());
		}
	}

	// TODO Pierre: when the save will be implemented, the constraint will be checked automatically thus the constraint
	// checker call will not be needed
	private CharacteristicForm createCharacteristicForms(boolean sameRole, boolean sameTerm) {

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");
		Vocabulary vocabulary = representationFactory.makeVocabulary(community, "http://vocabulary.com", "Vocabulary");
		communityService.save(community);
		resetTransaction();

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

		term1 = representationService.findTermByResourceId(term1.getId());

		String role1 = "Role 1";
		String role2;

		if (sameRole)
			role2 = role1;
		else
			role2 = "Role 2";

		CharacteristicForm cf1 = representationFactory.makeCharacteristicForm(vocabulary, term1, role1);
		representationService.save(cf1);
		resetTransaction();

		term2 = representationService.findTermByResourceId(term2.getId());

		// TODO Pierre: use the factory
		CharacteristicForm cf2 = new CharacteristicFormImpl(vocabulary, term2, role2,
				meaningFactory.makeCharacteristic());
		// representationService.save(cf2);
		// resetTransaction();

		return cf2;
	}
}
