package com.collibra.dgc.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import au.com.bytecode.opencsv.CSVReader;

import com.collibra.dgc.core.dto.cmatch.CMTermEntity;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.matcher.common.matcher.SearchSpace;

/**
 * @author fvdmaele
 * 
 *         Unit test for the MatchingService implementation
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestMatchingService extends AbstractBootstrappedServiceTest {

	private Community spc;
	private Vocabulary vocabulary;
	private String sourceCAUUID;
	private static String netappcsv = "src/test/resources/import/NetAppCorporateDictionary.csv";
	List<String> attributeLabelStrings = new LinkedList<String>();
	List<Term> attributeLabels;

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

		attributeLabels = attributeService.findAttributeTypeLabels();

		attributeLabelStrings.add("Children");
		attributeLabelStrings.add("Term");
		attributeLabelStrings.add("Incorporated");
		attributeLabelStrings.add("ResourceId");
		attributeLabelStrings.add("ConceptType");
		attributeLabelStrings.add("Depth");
		attributeLabelStrings.add("Parent");
		for (Term attributeLabel : attributeLabels) {
			attributeLabelStrings.add(attributeLabel.getSignifier());
		}

		resetTransaction();
	}

	@Before
	public void setUp() throws Exception {
		setUpNetApp();
	}

	@Test
	public void testFindSimilarTermsInGlossary() {
		long current = System.currentTimeMillis();

		SearchSpace<CMTermEntity> searchSpace = matchingService.findSimilarTerms("customer");

		long stop = System.currentTimeMillis();
		System.out.println("[Total time] " + (stop - current) + " ms");
	}

}
