package com.collibra.dgc.service.representation;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.service.AbstractServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestLockingInManager extends AbstractServiceTest {

	@Test
	public void testSaveVocabularyAndLockingStrategy() throws Exception {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		ObjectType ot = meaningFactory.makeObjectType();
		meaningService.saveAndCascade(ot);
		resetTransaction();

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(vocabulary);

		ot = meaningService.findObjectTypeByResourceId(ot.getId());

		representationFactory.makeTerm(vocabulary, "Desk", ot);

		representationService.findTermBySignifier(vocabulary, "Car");

		vocabulary = representationService.saveVocabulary(vocabulary);
	}

}
