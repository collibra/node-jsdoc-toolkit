package com.collibra.dgc.service.constraint;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.exceptions.CircularTaxonomyException;
import com.collibra.dgc.core.exceptions.InconsistentTaxonomyException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.meaning.facttype.impl.BinaryFactTypeImpl;
import com.collibra.dgc.core.model.meaning.facttype.impl.CharacteristicImpl;
import com.collibra.dgc.core.model.meaning.impl.ObjectTypeImpl;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.service.impl.ConstraintChecker;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * 
 * @author pmalarme
 * 
 */
@Transactional
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCharacteristicConstraintChecker extends AbstractServiceTest {

	@Autowired
	private ConstraintChecker constraintChecker;

	/* NORMAL TESTS */

	@Test
	public void testCheckConstraintsCharacteristicOkSpecializedTermConcept() {

		constraintChecker.checkConstraints(initCharacteristic(null, false, false, true, false));
	}

	@Test
	public void testCheckConstraintsCharacteristicOkSpecializedTermConceptInverse() {

		constraintChecker.checkConstraints(initCharacteristic(null, false, false, true, false));
	}

	@Test
	public void testCheckConstraintsCharacteristicOkSameTermConcept() {

		constraintChecker.checkConstraints(initCharacteristic(null, false, true, false, false));
	}

	@Test
	public void testCheckConstraintsCharacteristicOkSameTermConceptInverse() {

		constraintChecker.checkConstraints(initCharacteristic(null, false, true, false, false));
	}

	@Test
	public void testCheckConstraintsCharacteristicOkSameTerm() {

		constraintChecker.checkConstraints(initCharacteristic(null, false, false, false, true));
	}

	@Test
	public void testCheckConstraintsCharacteristicOkSameTermInverse() {

		constraintChecker.checkConstraints(initCharacteristic(null, false, false, false, true));
	}

	@Test
	public void testCheckConstraintsCharacteristicOkSpecializedTermConceptOT() {

		ObjectType generalConcept = new ObjectTypeImpl();

		constraintChecker.checkConstraints(initCharacteristic(generalConcept, false, false, true, false));
	}

	@Test
	public void testCheckConstraintsCharacteristicOkSpecializedTermConceptOTInverse() {

		ObjectType generalConcept = new ObjectTypeImpl();

		constraintChecker.checkConstraints(initCharacteristic(generalConcept, false, false, true, false));
	}

	@Test
	public void testCheckConstraintsCharacteristicOkSameTermConceptOT() {

		ObjectType generalConcept = new ObjectTypeImpl();

		constraintChecker.checkConstraints(initCharacteristic(generalConcept, false, true, false, false));
	}

	@Test
	public void testCheckConstraintsCharacteristicOkSameTermConceptOTInverse() {

		ObjectType generalConcept = new ObjectTypeImpl();

		constraintChecker.checkConstraints(initCharacteristic(generalConcept, false, true, false, false));
	}

	@Test
	public void testCheckConstraintsCharacteristicOkSameTermOT() {

		ObjectType generalConcept = new ObjectTypeImpl();

		constraintChecker.checkConstraints(initCharacteristic(generalConcept, false, false, false, true));
	}

	@Test
	public void testCheckConstraintsCharacteristicOkSameTermOTInverse() {

		ObjectType generalConcept = new ObjectTypeImpl();

		constraintChecker.checkConstraints(initCharacteristic(generalConcept, false, false, false, true));
	}

	/* INCORRECT INPUT */

	@Test
	public void testCheckConstraintsCharacteristicCircularTaxonomy() {

		try {

			Characteristic c = initCharacteristic(null, true, false, true, false);

			constraintChecker.checkConstraints(c);
			fail();

		} catch (CircularTaxonomyException e) {

			// TODO Pierre: check DGCErrorCodes
		}
	}

	@Test
	public void testCheckConstraintCharacteristicIncorrectGeneralConcept() {

		BinaryFactType generalConcept = new BinaryFactTypeImpl();

		try {

			constraintChecker.checkConstraints(initCharacteristic(generalConcept, false, true, false, false));
			fail();

		} catch (IllegalStateException e) {

			// TODO Pierre: check that it is the right DGC Code
		}
	}

	@Test
	public void testCheckConstraintCharacteristicIncorrectTermGeneralization() {

		try {

			constraintChecker.checkConstraints(initCharacteristic(null, false, false, false, false));
			fail();

		} catch (InconsistentTaxonomyException e) {

			// TODO Pierre: check that it is the right DGC Code
		}
	}

	/* UTILS */

	private Characteristic initCharacteristic(Concept generalConcept, boolean circularTaxonomy,
			boolean sameTermConcept, boolean specializedTermConcept, boolean sameTerm) {

		Characteristic c = new CharacteristicImpl();
		Characteristic gC = new CharacteristicImpl();

		Community community = communityFactory.makeCommunity("SC", "SC URI");
		Vocabulary vocabulary = representationFactory.makeVocabulary(community, "Http://vocabulary,com", "Vocabulary");
		communityService.save(community);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		Term term = representationFactory.makeTerm(vocabulary, "term");

		representationService.save(term);
		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());

		Term generalTerm = null;

		if (sameTerm) {

			generalTerm = term;

		} else if (sameTermConcept) {

			generalTerm = representationFactory.makeTerm(vocabulary, "general term", term.getObjectType());

			representationService.save(generalTerm);
			resetTransaction();

		} else if (specializedTermConcept) {

			generalTerm = representationFactory.makeTerm(vocabulary, "general term");

			representationService.save(generalTerm);
			resetTransaction();

			term = representationService.findTermByResourceId(term.getId());
			generalTerm = representationService.findTermByResourceId(generalTerm.getId());

			representationService.setGeneralConceptRepresentation(term, generalTerm);

			representationService.save(generalTerm);
			resetTransaction();

		} else {

			generalTerm = representationFactory.makeTerm(vocabulary, "general term");

			representationService.save(generalTerm);
			resetTransaction();
		}

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		term = representationService.findTermByResourceId(term.getId());

		CharacteristicForm cf = representationFactory.makeCharacteristicForm(vocabulary, term, "role", c);

		representationService.save(cf);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		CharacteristicForm gCf = representationFactory.makeCharacteristicForm(vocabulary, generalTerm, "generalRole",
				gC);
		generalTerm = representationService.findTermByResourceId(generalTerm.getId());

		cf = representationService.findCharacteristicFormByResourceId(cf.getId());

		representationService.save(gCf);
		resetTransaction();

		// If we need to check a circular taxonomy
		if (circularTaxonomy)
			gC.setGeneralConcept(cf.getCharacteristic());
		else
			gC.setGeneralConcept(generalConcept);

		c.setGeneralConcept(gCf.getCharacteristic());

		return c;
	}
}
