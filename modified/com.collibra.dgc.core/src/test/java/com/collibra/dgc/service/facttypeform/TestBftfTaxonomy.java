package com.collibra.dgc.service.facttypeform;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.service.AbstractServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestBftfTaxonomy extends AbstractServiceTest {

	@Test
	public void testMultipleLevelTaxonomy() {
		Community sc = communityFactory.makeCommunity("SC", "SC URI");
		Community sp = communityFactory.makeCommunity(sc, "SP", "SP URI");
		Vocabulary vocabulary = representationFactory
				.makeVocabulary(sp, "Derived Fact Types URI", "Derived Fact Types");

		Term B1 = representationFactory.makeTerm(vocabulary, "B1");
		Term B2 = representationFactory.makeTerm(vocabulary, "B2");
		BinaryFactTypeForm F1 = representationFactory.makeBinaryFactTypeForm(vocabulary, B1, "b1", "b2", B2);

		communityService.save(sc);

		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Term C1 = representationFactory.makeTerm(vocabulary, "C1");
		representationService.save(C1);
		Term C2 = representationFactory.makeTerm(vocabulary, "C2");
		representationService.save(C2);
		BinaryFactTypeForm F2 = representationFactory.makeBinaryFactTypeForm(vocabulary, C1, "c1", "c2", C2);
		representationService.saveBinaryFactTypeForm(F2);
		resetTransaction();

		B1 = representationService.findTermByResourceId(B1.getId());
		C1 = representationService.findTermByResourceId(C1.getId());

		representationService.setGeneralConceptRepresentation(C1, B1);
		resetTransaction();

		B2 = representationService.findTermByResourceId(B2.getId());
		C2 = representationService.findTermByResourceId(C2.getId());

		representationService.setGeneralConceptRepresentation(C2, B2);
		resetTransaction();

		F1 = representationService.findBinaryFactTypeFormByResourceId(F1.getId());
		F2 = representationService.findBinaryFactTypeFormByResourceId(F2.getId());
		List<BinaryFactTypeForm> suggestedBftfs = suggesterService.suggestSpecializableBinaryFactTypeForms(F1);
		Assert.assertEquals(1, suggestedBftfs.size());
		Assert.assertEquals(F2, suggestedBftfs.get(0));

		F1 = representationService.findBinaryFactTypeFormByResourceId(F1.getId());
		F2 = representationService.findBinaryFactTypeFormByResourceId(F2.getId());
		representationService.setGeneralConceptRepresentation(F2, F1);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Term A1 = representationFactory.makeTerm(vocabulary, "A1");
		representationService.save(A1);
		Term A2 = representationFactory.makeTerm(vocabulary, "A2");
		representationService.save(A2);
		BinaryFactTypeForm F3 = representationFactory.makeBinaryFactTypeForm(vocabulary, A1, "a1", "a2", A2);
		representationService.saveBinaryFactTypeForm(F3);
		resetTransaction();

		A1 = representationService.findTermByResourceId(A1.getId());
		B1 = representationService.findTermByResourceId(B1.getId());
		representationService.setGeneralConceptRepresentation(B1, A1);
		resetTransaction();

		A2 = representationService.findTermByResourceId(A2.getId());
		B2 = representationService.findTermByResourceId(B2.getId());
		representationService.setGeneralConceptRepresentation(B2, A2);
		resetTransaction();

		F1 = representationService.findBinaryFactTypeFormByResourceId(F1.getId());
		suggestedBftfs = suggesterService.suggestGeneralizableBinaryFactTypeForms(F1);
		Assert.assertEquals(1, suggestedBftfs.size());
		Assert.assertEquals(F3, suggestedBftfs.get(0));
		System.out.println(suggestedBftfs);

		F3 = representationService.findBinaryFactTypeFormByResourceId(F3.getId());
		F1 = representationService.findBinaryFactTypeFormByResourceId(F1.getId());
		representationService.setGeneralConceptRepresentation(F1, F3);
		resetTransaction();

		F3 = representationService.findBinaryFactTypeFormByResourceId(F3.getId());
		F1 = representationService.findBinaryFactTypeFormByResourceId(F1.getId());
		F2 = representationService.findBinaryFactTypeFormByResourceId(F2.getId());

		assertEquals(F1.getBinaryFactType().getGeneralConcept(), F3.getBinaryFactType());
		assertEquals(F2.getBinaryFactType().getGeneralConcept(), F1.getBinaryFactType());
	}

}
