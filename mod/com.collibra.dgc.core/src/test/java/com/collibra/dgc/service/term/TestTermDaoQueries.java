package com.collibra.dgc.service.term;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.service.AbstractServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestTermDaoQueries extends AbstractServiceTest {
	private Vocabulary vocabulary1;

	@Test
	public void testWithAttributes() throws Exception {
		setup();

		List<Term> terms = representationService.searchTermsBySignifier("Term1", 0, 100);
		assertEquals(1, terms.size());
	}

	private void setup() {
		vocabulary1 = representationFactory.makeVocabulary(sp, "My Test", "Synonyms Test");
		representationFactory.makeTerm(vocabulary1, "Term1");
		representationFactory.makeTerm(vocabulary1, "Term2");

		communityService.save(sp);
		resetTransaction();

		vocabulary1 = representationService.findVocabularyByResourceId(vocabulary1.getId());

		// Find the term
		Term t1 = representationService.findTermBySignifier(vocabulary1, "Term1");
		assertNotNull(t1);

		// Add attribute
		representationFactory.makeStringAttribute(attributeService.findMetaDescription(), t1, "test");
		representationService.saveTerm(t1);
		resetTransaction();

		// Find the term
		t1 = representationService.findTermBySignifier(vocabulary1, "Term1");
		assertNotNull(t1);

		// Add attribute
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), t1, "test");
		representationService.saveTerm(t1);
		resetTransaction();

		// Find the term
		t1 = representationService.findTermBySignifier(vocabulary1, "Term1");
		assertNotNull(t1);
	}

}
