package com.collibra.dgc.service.attribute;

import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * 
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestAttributes extends AbstractServiceTest {
	private Vocabulary vocabulary;

	@Test
	public void testWithAttributes() throws Exception {
		setup();

		// Find the term
		Term t1 = representationService.findTermBySignifier(vocabulary, "Term1");
		assertNotNull(t1);

		// Add attribute
		representationFactory.makeStringAttribute(attributeService.findMetaDescription(), t1, "test");
		t1 = representationService.saveTerm(t1);
		Set<Attribute> attrs = t1.getAttributes();
		boolean found = false;
		for (Attribute attr : attrs) {
			if (attr.getValue().equals("test")) {
				found = true;
				break;
			}
		}
		if (!found) {
			Assert.fail();
		}
		representationFactory.makeStringAttribute(attributeService.findMetaDescription(), t1, "test2");
		t1 = representationService.saveTerm(t1);
		attrs = t1.getAttributes();
		boolean found1 = false;
		boolean found2 = false;
		for (Attribute attr : attrs) {
			if (attr.getValue().equals("test")) {
				found1 = true;
			}
			if (attr.getValue().equals("test2")) {
				found2 = true;
			}
		}
		if (!found1 || !found2) {
			Assert.fail();
		}
	}

	@Test
	public void testValueListAttributes() throws Exception {
		setup();

		List<String> values = new LinkedList<String>();
		values.add("SalesForce");
		values.add("Siebel");
		values.add("Oracle");
		Term sourceSystemsAttrLabel = attributeService.createSingleValueListAttributeType("Source System",
				"This is a source system", values);

		Term t1 = representationService.findTermBySignifier(vocabulary, "Term1");

		SingleValueListAttribute attr = representationFactory.makeSingleValueListAttribute(sourceSystemsAttrLabel, t1,
				"SalesForce");
		representationService.save(t1);
	}

	/**
	 * I could reproduce with some very simple steps: 1. Create term. 2. Add two definitions. 3. Rename the term. 4.
	 * Delete one definition.
	 * 
	 * Goto to vocabulary page and the term disappears.
	 */
	@Test
	public void testBug6925() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "URI", "Test");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		communityService.save(sp);
		resetTransaction();

		car = representationService.findTermByResourceId(car.getId());
		Attribute def1 = representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), car,
				"Test Defition1");
		representationService.saveTerm(car);
		resetTransaction();

		car = representationService.findTermByResourceId(car.getId());
		Attribute def2 = representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), car,
				"Test Defition2");
		representationService.saveTerm(car);
		resetTransaction();

		car = representationService.findTermByResourceId(car.getId());
		car.setSignifier("Car Renamed");
		representationService.saveTerm(car);
		resetTransaction();

		car = representationService.findTermByResourceId(car.getId());
		Assert.assertNotNull(car);

		def1 = attributeService.getAttribute(def1.getId());
		Assert.assertNotNull(def1);

		attributeService.remove(def1);
		resetTransaction();

		car = representationService.findTermByResourceId(car.getId());
		Assert.assertNotNull(car);

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		// Map<UUID, List> result = representationService.getVocabularySummaryTable(vocabulary, attributeService
		// .findAttributeTypeLabels(vocabulary));

		List<String> attributeLabelStrings = new LinkedList<String>();
		attributeLabelStrings.add("Term");
	}

	private void setup() {
		vocabulary = representationFactory.makeVocabulary(sp, "My Test", "Synonyms Test");
		representationFactory.makeTerm(vocabulary, "Term1");
		representationFactory.makeTerm(vocabulary, "Term2");

		communityService.save(sp);
		resetTransaction();
	}
}
