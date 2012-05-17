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
import com.collibra.dgc.core.model.representation.impl.TermImpl;
import com.collibra.dgc.core.service.impl.ConstraintChecker;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * 
 * @author pmalarme
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestTermConstraintChecker extends AbstractServiceTest {

	@Autowired
	private ConstraintChecker constraintChecker;

	@Test
	public void testTermConstraintsOk() {

		try {

			constraintChecker.checkConstraints(createTerms(false));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testTermAlreadyExistsConstraint() {

		try {

			constraintChecker.checkConstraints(createTerms(true));
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.TERM_ALREADY_EXISTS, e.getErrorCode());
		}
	}

	// TODO Pierre: when the save will be implemented, the constraint will be checked automatically thus the constraint
	// checker call will not be needed
	private Term createTerms(boolean sameSignifier) {

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");
		Vocabulary vocabulary = representationFactory.makeVocabulary(community, "http://vocabulary.com", "Vocabulary");
		communityService.save(community);
		resetTransaction();

		Term term1 = representationFactory.makeTerm(vocabulary, "Test Term 1");
		representationService.save(term1);
		resetTransaction();

		Term term2;

		// TODO Pierre: use the factory
		if (sameSignifier)
			term2 = new TermImpl(vocabulary, term1.getSignifier(), meaningFactory.makeObjectType());
		else
			term2 = representationFactory.makeTerm(vocabulary, "Test Term 2");

		// representationService.save(term2);
		// resetTransaction();

		return term2;
	}
}
