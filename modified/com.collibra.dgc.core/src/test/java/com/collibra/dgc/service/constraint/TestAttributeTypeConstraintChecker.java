package com.collibra.dgc.service.constraint;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
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
public class TestAttributeTypeConstraintChecker extends AbstractServiceTest {

	@Autowired
	private ConstraintChecker constraintChecker;

	@Test
	public void testATConstraintsOkSA() {

		try {

			constraintChecker.checkAttributeTypeConstraints(createAttributeTypes(false, false, attributeService
					.findMetaStringAttributeTypeLabel().getObjectType()));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testATConstraintsOkSVLA() {

		try {

			constraintChecker.checkAttributeTypeConstraints(createAttributeTypes(false, false, attributeService
					.findMetaSingleStaticListType().getObjectType()));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testATConstraintsOkMVLA() {

		try {

			constraintChecker.checkAttributeTypeConstraints(createAttributeTypes(false, false, attributeService
					.findMetaMultiStaticListType().getObjectType()));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testATConstraintsOkDTA() {

		try {

			constraintChecker.checkAttributeTypeConstraints(createAttributeTypes(false, false, attributeService
					.findMetaDateTimeAttributeTypeLabel().getObjectType()));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testATConstraintsOkDefinition() {

		try {

			constraintChecker.checkAttributeTypeConstraints(createMetaAttributeType(
					attributeService.findMetaDefinition(), false, false, false));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testATConstraintsOkDescription() {

		try {

			constraintChecker.checkAttributeTypeConstraints(createMetaAttributeType(
					attributeService.findMetaDescription(), false, false, false));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testATConstraintsOkExample() {

		try {

			constraintChecker.checkAttributeTypeConstraints(createMetaAttributeType(attributeService.findMetaExample(),
					false, false, false));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testATConstraintsOkNote() {

		try {

			constraintChecker.checkAttributeTypeConstraints(createMetaAttributeType(attributeService.findMetaNote(),
					false, false, false));

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testATAlreadyexistsConstraint() {

		try {

			constraintChecker.checkAttributeTypeConstraints(createAttributeTypes(true, false, attributeService
					.findMetaStringAttributeTypeLabel().getObjectType()));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: check the error code
		}

		try {

			constraintChecker.checkAttributeTypeConstraints(createMetaAttributeType(
					attributeService.findMetaDefinition(), true, false, false));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: check the error code
		}

		try {

			constraintChecker.checkAttributeTypeConstraints(createMetaAttributeType(
					attributeService.findMetaDescription(), true, false, false));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: check the error code
		}

		try {

			constraintChecker.checkAttributeTypeConstraints(createMetaAttributeType(attributeService.findMetaExample(),
					true, false, false));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: check the error code
		}

		try {

			constraintChecker.checkAttributeTypeConstraints(createMetaAttributeType(attributeService.findMetaNote(),
					true, false, false));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: check the error code
		}
	}

	@Test
	public void testATNotATVocConstraint() {

		try {

			constraintChecker.checkAttributeTypeConstraints(createAttributeTypes(false, true, attributeService
					.findMetaDateTimeAttributeTypeLabel().getObjectType()));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: check the error code
		}

		try {

			constraintChecker.checkAttributeTypeConstraints(createMetaAttributeType(
					attributeService.findMetaDefinition(), false, true, false));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: check the error code
		}

		try {

			constraintChecker.checkAttributeTypeConstraints(createMetaAttributeType(
					attributeService.findMetaDescription(), false, true, false));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: check the error code
		}

		try {

			constraintChecker.checkAttributeTypeConstraints(createMetaAttributeType(attributeService.findMetaExample(),
					false, true, false));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: check the error code
		}

		try {

			constraintChecker.checkAttributeTypeConstraints(createMetaAttributeType(attributeService.findMetaNote(),
					false, true, false));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: check the error code
		}
	}

	@Test
	public void testATWrongConceptOrConceptTypeConstraint() {

		try {

			constraintChecker.checkAttributeTypeConstraints(createAttributeTypes(false, true,
					meaningFactory.makeObjectType()));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: check the error code
		}

		try {

			constraintChecker.checkAttributeTypeConstraints(createMetaAttributeType(
					attributeService.findMetaDefinition(), false, false, true));
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: check the error code
		}
	}

	// TODO Pierre: when the save will be implemented, the constraint will be checked automatically thus the constraint
	// checker call will not be needed
	private Term createAttributeTypes(boolean sameSignifier, boolean notAT, ObjectType conceptType) {

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");
		Vocabulary vocabulary = representationFactory.makeVocabulary(community, "http://vocabulary.com", "Vocabulary");
		communityService.save(community);
		resetTransaction();

		Vocabulary attributeTypeVocabulary = vocabularyDao.findAttributeTypesVocabulary();

		Term labelTerm1 = representationFactory.makeTermOfType(attributeTypeVocabulary, "Test AT 1",
				meaningFactory.makeObjectType());
		labelTerm1.getObjectType().setType(attributeService.findMetaStringAttributeTypeLabel().getObjectType());
		// TODO add the save status method
		representationService.save(labelTerm1);
		resetTransaction();

		Term labelTerm2;
		String signifier2;

		ObjectType atConceptType2 = meaningFactory.makeObjectType();
		atConceptType2.setType(conceptType);

		if (sameSignifier)
			signifier2 = labelTerm1.getSignifier();
		else
			signifier2 = "Test AT 2";

		// Todo use factory
		if (notAT)
			labelTerm2 = new TermImpl(vocabulary, signifier2, atConceptType2);
		else
			labelTerm2 = new TermImpl(attributeTypeVocabulary, signifier2, atConceptType2);

		return labelTerm2;
	}

	// TODO Pierre: when the save will be implemented, the constraint will be checked automatically thus the constraint
	// checker call will not be needed
	private Term createMetaAttributeType(Term metaATLabel, boolean sameSignifier, boolean notAT, boolean notSameConcept) {

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");
		Vocabulary vocabulary = representationFactory.makeVocabulary(community, "http://vocabulary.com", "Vocabulary");
		communityService.save(community);
		resetTransaction();

		Vocabulary attributeTypeVocabulary = vocabularyDao.findAttributeTypesVocabulary();

		Term labelTerm;
		String signifier;

		ObjectType atConcept;

		if (sameSignifier)
			signifier = metaATLabel.getSignifier();
		else
			signifier = "Test Meta AT";

		if (notSameConcept)
			atConcept = meaningFactory.makeObjectType();
		else
			atConcept = metaATLabel.getObjectType();

		// Todo use factory
		if (notAT)
			labelTerm = new TermImpl(vocabulary, signifier, atConcept);
		else
			labelTerm = new TermImpl(metaATLabel.getVocabulary(), signifier, atConcept);

		return labelTerm;
	}
}
