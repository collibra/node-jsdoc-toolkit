package com.collibra.dgc;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.bootstrap.BootstrapScriptExtractor;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.ConfigurationService;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
@Ignore
public class TestData extends AbstractDGCBootstrappedApiTest {

	@Autowired
	private ConfigurationService configurationService;

	int aCommunities = 2;
	int aSubCommunities = 4;
	int aVocabularies = 4;
	int aAttributeTypes = 200;

	// per voc
	int aTerms = 100;
	int aCategories = 50;
	int aBinaryFactTypes = 50;
	int aCharacteristicFactTypes = 50;
	private final Random random = new Random();
	int cSubCommunities = 0;

	Community[] communities = new Community[aCommunities];
	Community[] subCommunities = new Community[(aSubCommunities * aCommunities) + 1];
	Vocabulary[] vocabularies = new Vocabulary[((aSubCommunities + 1) * aCommunities * aVocabularies) + 1];
	Term lastTerm = null;
	Term secondLastTerm = null;
	Term[] attributeTypes = new Term[aAttributeTypes + 1];

	@Test
	@Ignore
	public void data() {
		try {
			bootstrap();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 1; i <= aAttributeTypes; i++) {
			attributeTypes[i - 1] = attributeTypeComponent.addStringAttributeType(randomText(), randomText());
		}

		for (int i = 1; i <= aCommunities; i++) {
			communities[i - 1] = communityComponent.addCommunity(randomText(), "http://" + randomText());
			for (int j = 1; j <= aSubCommunities; j++) {
				subCommunities[cSubCommunities++] = communityComponent.addSubCommunity(communities[i - 1].getId(),
						randomText(), "http://" + randomText());
				for (int k = 1; k <= aVocabularies; k++) {
					if (k % 2 == 0) {
						vocabularies[k - 1] = vocabularyComponent.addVocabulary(
								subCommunities[cSubCommunities - 1].getId(), randomText(), "http://" + randomText());
					} else {
						vocabularies[k - 1] = vocabularyComponent.addVocabulary(communities[i - 1].getId(),
								randomText(), "http://" + randomText());
					}

					for (int l = 1; l <= aTerms; l++) {
						if (l % 2 == 1) {
							secondLastTerm = lastTerm;
							lastTerm = termComponent.addTerm(vocabularies[k - 1].getId(), randomText());

						} else {
							Term t = termComponent.addTerm(vocabularies[k - 1].getId(), randomText());
							termComponent.addSynonym(lastTerm.getId(), t.getId());
							secondLastTerm = lastTerm;
							lastTerm = t;
						}
					}

					for (int l = 1; l <= aTerms; l++) {
						secondLastTerm = lastTerm;
						lastTerm = termComponent.addTerm(vocabularies[k - 1].getId(), randomText());
						attributeComponent.addAttribute(secondLastTerm.getId(),
								attributeTypes[(int) Math.floor(Math.random() * aAttributeTypes)].getId(),
								Arrays.asList(randomText(), randomText(), randomText()));
						if (l % 2 == 0) {
							attributeComponent.addStringAttribute(lastTerm.getId(),
									attributeTypes[(int) Math.floor(Math.random() * aAttributeTypes)].getId(),
									randomText());
						} else {
							attributeComponent.addMultiValueListAttribute(lastTerm.getId(),
									attributeTypes[(int) Math.floor(Math.random() * aAttributeTypes)].getId(),
									Arrays.asList(randomText(), randomText(), randomText(), randomText()));
						}
					}

					for (int l = 0; l <= aCategories; l++) {
						Term concept = termComponent.addTerm(vocabularies[k - 1].getId(), randomText());
						Term catTypeTerm = categorizationComponent.addCategorizationType(vocabularies[k - 1].getId(),
								randomText(), concept.getId().toString());
						Term scheme = termComponent.addTerm(vocabularies[k - 1].getId(), randomText());
						Term category = categorizationComponent.addCategory(vocabularies[k - 1].getId(), randomText(),
								catTypeTerm.getId().toString());
					}

					for (int l = 0; l <= aBinaryFactTypes; l++) {
						Term head = termComponent.addTerm(vocabularies[k - 1].getId(), randomText());
						Term tail = termComponent.addTerm(vocabularies[k - 1].getId(), randomText());
						binaryFactTypeFormComponent.addBinaryFactTypeForm(vocabularies[k - 1].getId(),
								head.getSignifier(), "role", "coRole", tail.getSignifier());
					}

					for (int l = 0; l <= aCharacteristicFactTypes; l++) {
						Term head = termComponent.addTerm(vocabularies[k - 1].getId(), randomText());
						characteristicFormComponent.addCharacteristicForm(vocabularies[k - 1].getId(),
								head.getSignifier(), "role");
					}
				}
			}
		}
		BootstrapScriptExtractor.extractScript(new File("C:\\Users\\GKDAI63\\Desktop\\strap.xml"), new File(
				"C:\\Users\\GKDAI63\\Desktop\\strap.dtd"), configurationService);
	}

	public String randomText() {
		return new BigInteger(130, random).toString(32);
	}

}
