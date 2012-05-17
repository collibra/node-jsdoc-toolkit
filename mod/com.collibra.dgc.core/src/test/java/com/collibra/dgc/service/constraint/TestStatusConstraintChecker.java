package com.collibra.dgc.service.constraint;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.meaning.ObjectType;
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
public class TestStatusConstraintChecker extends AbstractServiceTest {

	@Autowired
	private ConstraintChecker constraintChecker;

	@Test
	public void testStatusConstraintsOk() {

		try {

			constraintChecker.checkStatusConstraints(createStatuses(false, false, false));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testStatusAlreadyExistsConstraint() {

		try {

			constraintChecker.checkStatusConstraints(createStatuses(true, false, false));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: add error code verification
		}
	}

	@Test
	public void testStatusNotStatusVocConstraint() {

		try {

			constraintChecker.checkStatusConstraints(createStatuses(false, true, false));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: add error code verification
		}
	}

	@Test
	public void testStatusNotStatusTypeConstraint() {

		try {

			constraintChecker.checkStatusConstraints(createStatuses(false, false, true));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: add error code verification
		}
	}

	// TODO Pierre: when the save will be implemented, the constraint will be checked automatically thus the constraint
	// checker call will not be needed
	private Term createStatuses(boolean sameSignifier, boolean notStatus, boolean notStatusType) {

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");
		Vocabulary vocabulary = representationFactory.makeVocabulary(community, "http://vocabulary.com", "Vocabulary");
		communityService.save(community);
		resetTransaction();

		Vocabulary statusVocabulary = vocabularyDao.findStatusesVocabulary();

		Term labelTerm1 = representationFactory.makeTermOfType(statusVocabulary, "Test Status 1",
				meaningFactory.makeObjectType());
		labelTerm1.getObjectType().setType(
				meaningService.getObjectTypeWithError(MeaningConstants.META_STATUS_TYPE_UUID));
		// TODO add the save status method
		representationService.save(labelTerm1);
		resetTransaction();

		Term labelTerm2;
		String signifier2;
		ObjectType statusConceptType2 = meaningFactory.makeObjectType();

		if (sameSignifier)
			signifier2 = labelTerm1.getSignifier();
		else
			signifier2 = "Test Status 2";

		if (notStatusType)
			statusConceptType2.setType(meaningFactory.makeObjectType());
		else
			statusConceptType2.setType(meaningService.getObjectTypeWithError(MeaningConstants.META_STATUS_TYPE_UUID));

		// Todo use factory
		if (notStatus)
			labelTerm2 = new TermImpl(vocabulary, signifier2, statusConceptType2);
		else
			labelTerm2 = new TermImpl(statusVocabulary, signifier2, statusConceptType2);

		return labelTerm2;
	}
}
