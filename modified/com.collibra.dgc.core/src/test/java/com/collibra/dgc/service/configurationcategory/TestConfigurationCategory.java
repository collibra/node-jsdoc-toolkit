package com.collibra.dgc.service.configurationcategory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.configuration.AttributeAndRelationTypesConfigurationCategory;
import com.collibra.dgc.core.model.configuration.impl.AttributeAndRelationTypesConfigurationCategoryImpl;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.service.AbstractServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestConfigurationCategory extends AbstractServiceTest {

	Vocabulary vocabulary;
	
	private void setup() {
		vocabulary = representationFactory.makeVocabulary(sp, "My Test", "Synonyms Test");
		representationFactory.makeTerm(vocabulary, "Term1");
		representationFactory.makeTerm(vocabulary, "Term2");
		representationFactory.makeTerm(vocabulary, "Term3");

		communityService.save(sp);
		resetTransaction();
	}
	
	@Test
	public void testSaveAttributeCategory() {
		setup();
		
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Term t1 = vocabulary.getTerm("Term1");
		Term t2 = vocabulary.getTerm("Term2");
		Term t3 = vocabulary.getTerm("Term3");
		
		List<Representation> terms = new LinkedList<Representation>();
		terms.add(t1);
		terms.add(t2);
		
		AttributeAndRelationTypesConfigurationCategory cat1 = new AttributeAndRelationTypesConfigurationCategoryImpl("cat1", "test description", terms);
		cat1 = configurationCategoryService.saveAttributeAndRelationTypeCategory(cat1);
		
		assertEquals("cat1", cat1.getName());
		assertEquals("test description", cat1.getDescription());
		assertEquals(terms, cat1.getAttributeAndRelationTypes());
		assertEquals(terms.get(0), cat1.getAttributeAndRelationTypes().get(0));
		assertEquals(terms.get(1), cat1.getAttributeAndRelationTypes().get(1));
		
		resetTransaction();
		
		cat1 = configurationCategoryService.getCategoryByName("cat1");
		assertEquals("cat1", cat1.getName());
		assertEquals("test description", cat1.getDescription());
		assertEquals(terms, cat1.getAttributeAndRelationTypes());
		assertEquals(terms.get(0), cat1.getAttributeAndRelationTypes().get(0));
		assertEquals(terms.get(1), cat1.getAttributeAndRelationTypes().get(1));

		List<Representation> terms2 = new LinkedList<Representation>();
		terms2.add(t2);
		terms2.add(t1);
		
		AttributeAndRelationTypesConfigurationCategory cat2 = new AttributeAndRelationTypesConfigurationCategoryImpl("cat2", "test description 2", terms2);
		cat2 = configurationCategoryService.saveAttributeAndRelationTypeCategory(cat2);

		resetTransaction();
		
		cat2 = configurationCategoryService.getCategoryByName("cat2");
		assertEquals("cat2", cat2.getName());
		assertEquals("test description 2", cat2.getDescription());
		assertEquals(terms2, cat2.getAttributeAndRelationTypes());
		assertEquals(terms2.get(0), cat2.getAttributeAndRelationTypes().get(0));
		assertEquals(terms2.get(1), cat2.getAttributeAndRelationTypes().get(1));
		
		// change order
		cat2.changeAttributeOrRelationTypeOrder(t2, 0);
		cat2 = configurationCategoryService.saveAttributeAndRelationTypeCategory(cat1);

		resetTransaction();
		
		cat2 = configurationCategoryService.getCategoryByName("cat2");
		assertEquals("cat2", cat2.getName());
		assertEquals("test description 2", cat2.getDescription());
		assertEquals(terms2, cat2.getAttributeAndRelationTypes());
		assertEquals(t2, cat2.getAttributeAndRelationTypes().get(0));
		assertEquals(t1, cat2.getAttributeAndRelationTypes().get(1));
		
		// add term at location
		cat2.addAttributeOrRelationType(1, t3);
		cat2 = configurationCategoryService.saveAttributeAndRelationTypeCategory(cat2);

		resetTransaction();
		
		cat1 = configurationCategoryService.getCategoryByName("cat1");
		cat2 = configurationCategoryService.getCategoryByName("cat2");
		assertEquals("cat2", cat2.getName());
		assertEquals("test description 2", cat2.getDescription());
		assertEquals(t2, cat2.getAttributeAndRelationTypes().get(0));
		assertEquals(t3, cat2.getAttributeAndRelationTypes().get(1));
		assertEquals(t1, cat2.getAttributeAndRelationTypes().get(2));
		
		// test get all
		assertEquals(2, configurationCategoryService.findAllAttributeAndRelationTypesCategories().size());
		
		// test remove
		configurationCategoryService.removeConfigurationCategory(cat1);

		resetTransaction();
	
		assertNull(configurationCategoryService.getCategoryByName("cat1"));
	}
}
