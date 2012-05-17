package com.collibra.dgc.service.exchanger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hub.sam.mof.instancemodel.MetaModelException;
import hub.sam.mof.xmi.XmiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.jdom.JDOMException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.AbstractDGCTest;
import com.collibra.dgc.core.dao.BinaryFactTypeDao;
import com.collibra.dgc.core.dao.CharacteristicDao;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.impl.TermImpl;
import com.collibra.dgc.core.service.exchanger.impl.importer.SbvrImporter;
import com.collibra.dgc.core.service.exchanger.options.SbvrImporterOptions;
import com.collibra.dgc.core.service.impl.ConstraintChecker;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
@Ignore
public class TestSbvrImporter extends AbstractDGCTest {

	private static final String sbvrMeaningAndRepresentationLocation = "src/test/resources/sbvr-meta/MeaningAndRepresentation-model.xml";
	private static final String sbvrDescribingBusinesssVocabulariesLocation = "src/test/resources/sbvr-meta/DescribingBusinessVocabularies-model.xml";
	private static final String collibraExtensions = "src/test/resources/sbvr-meta/CollibraExtensions-model.xml";

	@Autowired
	private MeaningFactory meaningFactory;
	@Autowired
	private RepresentationFactory representationFactory;
	@Autowired
	private CommunityFactory communityFactory;
	@Autowired
	private BinaryFactTypeDao binaryFactTypeDao;
	@Autowired
	private CharacteristicDao characteristicDao;
	@Autowired
	private ConstraintChecker constraintChecker;

	private InputStream locationToInputStream(String location) throws FileNotFoundException {
		File f = new File(location);
		return new FileInputStream(f);
	}

	@Test
	public void testImportSbvrMeaningAndRepresentation() throws IOException, JDOMException, XmiException,
			MetaModelException {
		Community semComm = communityFactory.makeCommunity("SBVR Test", "www.collibra.com/sbvr");
		Community spComm = communityFactory.makeCommunity(semComm, "SBVR Sub Test", "www.collibra.com/SBVRSub");

		InputStream sbvrMR = locationToInputStream(sbvrMeaningAndRepresentationLocation);
		SbvrImporterOptions options = new SbvrImporterOptions();
		options.setShouldRemoveSuffixIndices(true);
		final SbvrImporter sbvrImporter = new SbvrImporter(sbvrMR, spComm, options, representationFactory,
				meaningFactory, binaryFactTypeDao, characteristicDao, constraintChecker);

		Vocabulary vocabulary = sbvrImporter.importSbvr();
		assertEquals("Meaning and Representation Vocabulary", vocabulary.getName());
		assertEquals("http://www.omg.org/spec/SBVR/20070901/MeaningAndRepresentation.xml", vocabulary.getUri());
		assertFalse(vocabulary.getTerms().isEmpty());
		assertFalse(vocabulary.getCharacteristicForms().isEmpty());
		assertFalse(vocabulary.getBinaryFactTypeForms().isEmpty());
		Term meaning = new TermImpl(vocabulary, "Meaning", meaningFactory.makeObjectType());
		Term characteristic = new TermImpl(vocabulary, "Characteristic", meaningFactory.makeObjectType());
		Term nonExistentTerm = new TermImpl(vocabulary, "DoesNotExist", meaningFactory.makeObjectType());

		assertTrue(vocabulary.getTerms().contains(meaning));
		assertTrue(vocabulary.getTerms().contains(characteristic));
		assertFalse(vocabulary.getTerms().contains(nonExistentTerm));
		boolean foundVerbconcept = false;
		for (Term term : vocabulary.getTerms()) {
			if (term.getSignifier().equals("Verb Concept")) {
				assertTrue(term.getObjectType().getTerms()
						.contains(new TermImpl(vocabulary, "Fact Type", meaningFactory.makeObjectType())));
				foundVerbconcept = true;
			}
		}
		assertTrue(foundVerbconcept);

		boolean foundFactisinFactModel = false;
		for (BinaryFactTypeForm bftf : vocabulary.getBinaryFactTypeForms()) {
			if (bftf.verbalise().equals("Fact is in / includes Fact Model")
					|| bftf.verbalise().equals("Fact Model includes / is in Fact")) {
				foundFactisinFactModel = true;
			}
		}
		assertTrue(foundFactisinFactModel);
	}

	@Test
	public void testImportSbvrBusinessVocabulary() throws FileNotFoundException {
		Community semComm = communityFactory.makeCommunity("SBVR Test", "www.collibra.com/sbvr");
		Community spComm = communityFactory.makeCommunity(semComm, "SBVR SubTest", "www.collibra.com/SBVRSub");

		InputStream sbvrMR = locationToInputStream(sbvrDescribingBusinesssVocabulariesLocation);
		SbvrImporterOptions options = new SbvrImporterOptions();
		options.setShouldRemoveSuffixIndices(true);
		final SbvrImporter sbvrImporter = new SbvrImporter(sbvrMR, spComm, options, representationFactory,
				meaningFactory, binaryFactTypeDao, characteristicDao, constraintChecker);
		Vocabulary vocabulary = sbvrImporter.importSbvr();
		assertEquals("Vocabulary for Describing Business Vocabularies", vocabulary.getName());
		assertEquals("http://www.omg.org/spec/SBVR/20070901/DescribingBusinessVocabularies.xml", vocabulary.getUri());
		assertFalse(vocabulary.getTerms().isEmpty());
		assertFalse(vocabulary.getCharacteristicForms().isEmpty());
		assertFalse(vocabulary.getBinaryFactTypeForms().isEmpty());
		printVocabulary(vocabulary);

	}

	@Test
	public void testCollibraExtensionsToSBVR() throws FileNotFoundException {
		Community semComm = communityFactory.makeCommunity("SBVR Test", "www.collibra.com/sbvr");
		Community spComm = communityFactory.makeCommunity(semComm, "SBVR SubTest", "www.collibra.com/SBVRSub");

		InputStream sbvrMR = locationToInputStream(collibraExtensions);
		SbvrImporterOptions options = new SbvrImporterOptions();
		options.setShouldRemoveSuffixIndices(true);
		final SbvrImporter sbvrImporter = new SbvrImporter(sbvrMR, spComm, options, representationFactory,
				meaningFactory, binaryFactTypeDao, characteristicDao, constraintChecker);
		Vocabulary vocabulary = sbvrImporter.importSbvr();
		assertEquals("Collibra SBVR extensions Vocabulary", vocabulary.getName());
		assertEquals("http://www.collibra.com/glossary/sbvrextentions", vocabulary.getUri());
		assertFalse(vocabulary.getTerms().isEmpty());
		assertTrue(vocabulary.getCharacteristicForms().isEmpty());
		assertTrue(vocabulary.getBinaryFactTypeForms().isEmpty());
		printVocabulary(vocabulary);

		assertTermExists("Constraint Type", vocabulary);
		assertTermExists("Mandatory", vocabulary);
		assertTermExists("Uniqueness", vocabulary);
	}

	private void assertTermExists(String termName, Vocabulary vocabulary) {
		for (Term term : vocabulary.getTerms()) {
			if (term.getSignifier().equals(termName)) {
				return;
			}
		}

		Assert.fail("Failed to find term '" + termName + "'");
	}

	private void printVocabulary(Vocabulary vocabulary2) {
		System.out.println(vocabulary2.toString());

		for (Term term : vocabulary2.getTerms()) {
			Term superterm = null;
			if (term.getObjectType().getGeneralConcept() != null) {
				superterm = (Term) term.getObjectType().getGeneralConcept().getRepresentations().iterator().next();
			}
			System.out.print(term.getSignifier());
			while (superterm != null) {
				System.out.print(" > " + superterm.getSignifier());
				ObjectType objectType = superterm.getObjectType();
				if (objectType.getGeneralConcept() != null && objectType.getGeneralConcept() != objectType) {
					superterm = (Term) superterm.getObjectType().getGeneralConcept().getRepresentations().iterator()
							.next();
				} else {
					superterm = null;
				}
			}
			System.out.println();
		}

		for (BinaryFactTypeForm bftf : vocabulary2.getBinaryFactTypeForms()) {
			System.out.println(bftf.verbalise());
		}
	}
}
