package com.collibra.dgc.service.constraint;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

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
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.service.impl.ConstraintChecker;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * 
 * @author pmalarme
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestBFTConstraintChecker extends AbstractServiceTest {

	@Autowired
	private ConstraintChecker constraintChecker;

	/* NORMAL TESTS */

	@Test
	public void testCheckConstraintsBFTOkSpecializedTermConcept() {

		constraintChecker.checkConstraints(initBFT(null, false, false, true, false, false));
	}

	@Test
	public void testCheckConstraintsBFTOkSpecializedTermConceptInverse() {

		constraintChecker.checkConstraints(initBFT(null, false, false, true, false, true));
	}

	@Test
	public void testCheckConstraintsBFTOkSameTermConcept() {

		constraintChecker.checkConstraints(initBFT(null, false, true, false, false, false));
	}

	@Test
	public void testCheckConstraintsBFTOkSameTermConceptInverse() {

		constraintChecker.checkConstraints(initBFT(null, false, true, false, false, true));
	}

	@Test
	public void testCheckConstraintsBFTOkSameTerm() {

		constraintChecker.checkConstraints(initBFT(null, false, false, false, true, false));
	}

	@Test
	public void testCheckConstraintsBFTOkSameTermInverse() {

		constraintChecker.checkConstraints(initBFT(null, false, false, false, true, true));
	}

	@Test
	public void testCheckConstraintsBFTOkSpecializedTermConceptOT() {

		ObjectType generalConcept = new ObjectTypeImpl();

		constraintChecker.checkConstraints(initBFT(generalConcept, false, false, true, false, false));
	}

	@Test
	public void testCheckConstraintsBFTOkSpecializedTermConceptOTInverse() {

		ObjectType generalConcept = new ObjectTypeImpl();

		constraintChecker.checkConstraints(initBFT(generalConcept, false, false, true, false, true));
	}

	@Test
	public void testCheckConstraintsBFTOkSameTermConceptOT() {

		ObjectType generalConcept = new ObjectTypeImpl();

		constraintChecker.checkConstraints(initBFT(generalConcept, false, true, false, false, false));
	}

	@Test
	public void testCheckConstraintsBFTOkSameTermConceptOTInverse() {

		ObjectType generalConcept = new ObjectTypeImpl();

		constraintChecker.checkConstraints(initBFT(generalConcept, false, true, false, false, true));
	}

	@Test
	public void testCheckConstraintsBFTOkSameTermOT() {

		ObjectType generalConcept = new ObjectTypeImpl();

		constraintChecker.checkConstraints(initBFT(generalConcept, false, false, false, true, false));
	}

	@Test
	public void testCheckConstraintsBFTOkSameTermOTInverse() {

		ObjectType generalConcept = new ObjectTypeImpl();

		constraintChecker.checkConstraints(initBFT(generalConcept, false, false, false, true, true));
	}

	/* INCORRECT INPUT */

	@Test
	public void testCheckConstraintsBFTCircularTaxonomy() {

		try {

			BinaryFactType bft = initBFT(null, true, false, true, false, false);

			constraintChecker.checkConstraints(bft);
			fail();

		} catch (CircularTaxonomyException e) {

			// TODO Pierre: check DGCErrorCodes
		}
	}

	@Test
	public void testCheckConstraintBFTIncorrectGeneralConcept() {

		Characteristic generalConcept = new CharacteristicImpl();

		try {

			constraintChecker.checkConstraints(initBFT(generalConcept, false, true, false, false, false));
			fail();

		} catch (IllegalStateException e) {

			// TODO Pierre: check that it is the right DGC Code
		}
	}

	@Test
	public void testCheckConstraintBFTIncorrectTermGeneralization() {

		try {

			constraintChecker.checkConstraints(initBFT(null, false, false, false, false, false));
			fail();

		} catch (InconsistentTaxonomyException e) {

			// TODO Pierre: check that it is the right DGC Code
		}
	}

	/* UTILS */

	// TODO Pierre: when the save will be implemented, the constraint will be checked automatically thus the constraint
	// checker
	// call will not be needed
	private BinaryFactType initBFT(Concept generalConcept, boolean circularTaxonomy, boolean sameTermConcept,
			boolean specializedTermConcept, boolean sameTerm, boolean inverse) {

		BinaryFactType bft = new BinaryFactTypeImpl();
		BinaryFactType gBft = new BinaryFactTypeImpl();

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");
		Vocabulary vocabulary = representationFactory.makeVocabulary(community, "http://vocabulary.com", "Vocabulary");
		communityService.save(community);
		resetTransaction();

		Term headTerm = representationFactory.makeTerm(vocabulary, "head term");
		Term tailTerm = representationFactory.makeTerm(vocabulary, "tail term");

		representationService.save(headTerm);
		resetTransaction();

		representationService.save(tailTerm);
		resetTransaction();

		Term generalHeadTerm = null;
		Term generalTailTerm = null;

		if (sameTerm) {

			generalHeadTerm = headTerm;
			generalTailTerm = tailTerm;

		} else if (sameTermConcept) {

			generalHeadTerm = representationFactory.makeTerm(vocabulary, "general head term", headTerm.getObjectType());
			generalTailTerm = representationFactory.makeTerm(vocabulary, "general tail term", tailTerm.getObjectType());

			representationService.save(generalHeadTerm);
			resetTransaction();

			representationService.save(generalTailTerm);
			resetTransaction();

		} else if (specializedTermConcept) {

			generalHeadTerm = representationFactory.makeTerm(vocabulary, "general head term");

			representationService.save(generalHeadTerm);
			resetTransaction();

			headTerm = representationService.findTermByResourceId(headTerm.getId());
			generalHeadTerm = representationService.findTermByResourceId(generalHeadTerm.getId());

			representationService.setGeneralConceptRepresentation(headTerm, generalHeadTerm);

			representationService.save(headTerm);
			resetTransaction();

			generalTailTerm = representationFactory.makeTerm(vocabulary, "general tail term");

			representationService.save(generalTailTerm);
			resetTransaction();

			tailTerm = representationService.findTermByResourceId(tailTerm.getId());
			generalTailTerm = representationService.findTermByResourceId(generalTailTerm.getId());

			representationService.setGeneralConceptRepresentation(tailTerm, generalTailTerm);

			representationService.save(tailTerm);
			resetTransaction();

		} else {

			generalHeadTerm = representationFactory.makeTerm(vocabulary, "general head term");

			representationService.save(generalHeadTerm);
			resetTransaction();

			generalTailTerm = representationFactory.makeTerm(vocabulary, "general tail term");

			representationService.save(generalTailTerm);
			resetTransaction();
		}

		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(vocabulary, headTerm, "role", "coRole",
				tailTerm, bft);

		representationService.save(bftf);
		resetTransaction();

		BinaryFactTypeForm gBftf = null;

		generalHeadTerm = representationService.findTermByResourceId(generalHeadTerm.getId());
		generalTailTerm = representationService.findTermByResourceId(generalTailTerm.getId());

		bftf = representationService.getBftfWithError(bftf.getId());

		if (inverse)
			gBftf = representationFactory.makeBinaryFactTypeForm(vocabulary, generalTailTerm, "generalCoRole",
					"generalRole", generalHeadTerm, gBft);
		else
			gBftf = representationFactory.makeBinaryFactTypeForm(vocabulary, generalHeadTerm, "generalRole",
					"generalCorRole", generalTailTerm, gBft);

		representationService.save(gBftf);
		resetTransaction();

		// If we need to check a circular taxonomy
		if (circularTaxonomy)
			gBft.setGeneralConcept(bftf.getBinaryFactType());
		else
			gBft.setGeneralConcept(generalConcept);

		bft.setGeneralConcept(gBftf.getBinaryFactType());

		return bft;
	}
}
