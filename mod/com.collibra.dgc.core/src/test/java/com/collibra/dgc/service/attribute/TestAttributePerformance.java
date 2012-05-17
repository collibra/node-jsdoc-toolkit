package com.collibra.dgc.service.attribute;

import static org.junit.Assert.assertEquals;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.hibernate.Query;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import au.com.bytecode.opencsv.CSVReader;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.service.AbstractServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestAttributePerformance extends AbstractServiceTest {

	private Community spc;
	private Vocabulary vocabulary;
	private String sourceCAUUID;
	private static String netappcsv = "src/test/resources/import/NetAppCorporateDictionary.csv";

	public void setUpNetApp() throws IOException {
		spc = communityFactory.makeCommunity("NetApp SPC", "netapp.com/spc");
		communityService.save(spc);
		resetTransaction();
		spc = communityService.findCommunityByUri(spc.getUri());

		vocabulary = representationFactory.makeVocabulary(spc, "netapp.com/voc", "Netapp Vocabulary");
		representationService.saveVocabulary(vocabulary);

		final Vocabulary metaVocabulary = representationService.findSbvrCollibraExtensionsVocabulary();
		Term sourceLabel = representationFactory.makeTerm(metaVocabulary, "Source");
		sourceCAUUID = sourceLabel.getObjectType().getId();
		representationService.save(sourceLabel);
		resetTransaction();
		sourceLabel = representationService.findTermByResourceId(sourceLabel.getId());
		spc = communityService.findCommunityByUri(spc.getUri());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		CSVReader reader = new CSVReader(new FileReader(netappcsv), ';');
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			String label = nextLine[0];
			String def = nextLine[1];
			String steward = nextLine[2];
			String source = nextLine[3];
			Term term = vocabulary.getTerm(label);
			if (term == null) {
				term = representationFactory.makeTerm(vocabulary, label);
			}
			representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), term, def);
			// avoid constraint violation exception
			if (term.getAttributes().size() < 3) {
				representationFactory.makeStringAttribute(sourceLabel, term, source);
			}
		}
		communityService.save(spc);
		resetTransaction();
	}

	@Before
	public void onSetUp() throws Exception {
		setUpNetApp();
	}

	@Test
	public void testNetAppDictionaryOneQuery() {
		long current = System.currentTimeMillis();

		ObjectType metaDef = meaningService.findMetaDefinition();
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		ObjectType sourceCA = meaningService.findObjectTypeByResourceId(sourceCAUUID);
		String queryStr = "select at from AttributeImpl at, TermImpl rep where at.owner.id = rep.id and at.label.objectType.id = :type and rep.vocabulary = :vocabulary order by rep.signifier";
		Query query = getCurrentSession().createQuery(queryStr);
		query.setParameter("vocabulary", vocabulary);
		query.setParameter("type", metaDef.getId());
		List<Attribute> result = query.list();
		assertEquals(466, result.size());
		for (Attribute attribute : result) {
			System.out.println(attribute.getValue());
		}

		long stop = System.currentTimeMillis();
		System.out.println("[Total time] " + (stop - current) / 1000);
	}

	@Test
	public void searchTermsBySignifier() {
		long current = System.currentTimeMillis();
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		List<Term> result = representationService.searchTermsBySignifier(vocabulary, "attr", false);
		long stop = System.currentTimeMillis();
		System.out.println("[Total time] " + (stop - current) + " [Size] " + result.size());
		// assertEquals(2, result.size());
		for (Term t : result)
			System.out.println(t.verbalise());

		current = System.currentTimeMillis();
		result = representationService.searchTermsBySignifier(vocabulary, "attr", true);
		stop = System.currentTimeMillis();
		System.out.println("[Total time] " + (stop - current) + " [Size] " + result.size());
		// assertEquals(2, result.size());

		for (Term t : result)
			System.out.println(t.verbalise());
	}

	@Test
	public void testNetAppDictionary() {
		long current = System.currentTimeMillis();
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		ObjectType sourceCA = meaningService.findObjectTypeByResourceId(sourceCAUUID);
		for (Term term : vocabulary.getTerms()) {
			List<List<Attribute>> attributes = representationService.findAttributesForRepresentation(term,
					meaningService.findMetaDefinition(), sourceCA);
			// System.out.println("\n\tDefinitions: ");
			for (Attribute definition : attributes.get(0)) {
				// System.out.print("\n");
				// System.out.println(definition.getLongExpression());
			}
			// System.out.println("\n\tSources: ");
			for (Attribute source : attributes.get(1)) {
				// System.out.print("\n");
				// System.out.println(source.getLongExpression());
			}
		}
		long stop = System.currentTimeMillis();
		System.out.println("[Total time] " + (stop - current) / 1000);
	}
}
