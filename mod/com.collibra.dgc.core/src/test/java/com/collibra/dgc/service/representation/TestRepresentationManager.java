/**
 * 
 */
package com.collibra.dgc.service.representation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * @author fvdmaele
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestRepresentationManager extends AbstractServiceTest {

	@Test
	public void testAddAttributeToPersistentVocabulary() throws Exception {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "Vocabulary 1");
		Term label = representationFactory.makeTerm(vocabulary, "AttribLabelTerm1Vocabulary");
		Term termForAttributeMeaning = representationFactory.makeTerm(vocabulary, "AttribMeaningTerm2Vocabulary");

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertNotNull(vocabulary);
		assertTrue(vocabulary.isPersisted());

		resetTransaction();

		// add a custom attribute
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		label = representationService.findTermBySignifier(vocabulary, label.getSignifier());
		termForAttributeMeaning = representationService.findTermBySignifier(vocabulary,
				termForAttributeMeaning.getSignifier());

		representationFactory.makeStringAttribute(label, termForAttributeMeaning, "the value for this attribute");

		Vocabulary newVocabulary = representationService.saveVocabulary(vocabulary);

		resetTransaction();

		// test removing an attribute
		final Set<Attribute> attrs = termForAttributeMeaning.getAttributes();
		for (final Attribute attr : attrs) {
			attributeService.remove(attr);
		}

		resetTransaction();

		label = representationService.findTermByResourceId(label.getId());
		termForAttributeMeaning = representationService.findTermBySignifier(vocabulary,
				termForAttributeMeaning.getSignifier());
		assertEquals(0, termForAttributeMeaning.getAttributes().size());
		assertEquals(0, label.getAttributes().size());
	}

	@Test
	public void testAddAttributeToPersistentVocabulary2() throws Exception {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "Vocabulary.terms");
		representationFactory.makeTerm(vocabulary, "Term1Voc1");
		representationFactory.makeTerm(vocabulary, "Term2Voc1");

		communityService.save(sp);
		resetTransaction();

		Vocabulary loadedVocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertNotNull(loadedVocabulary);
		assertNotNull(loadedVocabulary.getTerms());
		assertEquals(2, loadedVocabulary.getTerms().size());
		Set<Term> terms = loadedVocabulary.getTerms();
		Iterator<Term> i = terms.iterator();
		Term t1 = i.next();
		Term t2 = i.next();
		assertNotNull(t1);
		assertNotNull(t2);

		Attribute attribute = representationFactory.makeStringAttribute(t1, t2, "the value for this attribute");

		communityService.save(loadedVocabulary.getCommunity());
		assertNotNull(attribute);
	}

	@Test
	public void testFindBinaryFactTypeFormsWithTerm() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term Human = representationFactory.makeTerm(vocabulary, "Human");

		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives",
				"driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", Human);

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Term personTerm = representationService.findTermByResourceId(person.getId());
		assertNotNull(personTerm);
		List<BinaryFactTypeForm> personRelations = representationService
				.findBinaryFactTypeFormsReferringTerm(personTerm);
		assertEquals(2, personRelations.size());
		BinaryFactTypeForm expectedBftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		assertTrue(personRelations.contains(expectedBftf));
		Term carTerm = representationService.findTermByResourceId(car.getId());

		BinaryFactTypeForm personOwnsCar = representationFactory.makeBinaryFactTypeForm(vocabulary, personTerm, "owns",
				"owned by", carTerm);
		personOwnsCar = representationService.saveBinaryFactTypeForm(personOwnsCar);

		resetTransaction();
		carTerm = representationService.findTermByResourceId(carTerm.getId());

		personRelations = representationService.findBinaryFactTypeFormsReferringTerm(carTerm);
		assertEquals(2, personRelations.size());
		assertTrue(personRelations.contains(personOwnsCar));
	}

	@Test
	public void testFindVocabulariesByCommunity() {
		Community community = communityFactory.makeCommunity("My Sem Com", "My Sem Com URI");
		Community subCommunity = communityFactory.makeCommunity(community, "My Community", "My Community URI");
		Vocabulary myVoc = representationFactory.makeVocabulary(subCommunity, "uri", "name");
		Vocabulary myVoc2 = representationFactory.makeVocabulary(subCommunity, "uri2", "name2");
		Vocabulary myVoc3 = representationFactory.makeVocabulary(subCommunity, "uri3", "name3");
		communityService.save(community);
		resetTransaction();

		subCommunity = communityService.findCommunity(subCommunity.getId());
		assertTrue(subCommunity.getVocabularies().contains(myVoc));
		assertTrue(subCommunity.getVocabularies().contains(myVoc2));
		assertTrue(subCommunity.getVocabularies().contains(myVoc3));
		assertEquals(3, subCommunity.getVocabularies().size()); // Includes meta vocabulary.
	}

	@Test
	public void testFindConceptType() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");

		communityService.save(sp);
		resetTransaction();

		Representation personTerm = representationService.findRepresentationByResourceId(person.getId());
		assertNotNull(personTerm);

		Meaning meaning = personTerm.getMeaning();
		if (!(meaning instanceof Concept)) {
			rollback();
			throw new IllegalArgumentException("Meaning of representation is not a Concept");
		}
		ObjectType type = ((Concept) meaning).getType();
		Term conceptTypeRepresentation = representationService.findPreferredTerm(type, personTerm.getVocabulary());
		assertNotNull(conceptTypeRepresentation);
	}

	@Test
	public void testFindLatestVocabularies() {
		Community community = communityFactory.makeCommunity("My Sem Com", "My Sem Com URI");
		Community subCommunity = communityFactory.makeCommunity(community, "My Community", "My Community URI");
		Vocabulary myVoc = representationFactory.makeVocabulary(subCommunity, "uri", "namev1");
		Vocabulary myVoc2 = representationFactory.makeVocabulary(subCommunity, "uri2", "otherNameV1");

		communityService.save(community);
		resetTransaction();

		Vocabulary savedMyVoc1 = representationService.findVocabularyByResourceId(myVoc.getId());
		Vocabulary savedMyVoc2 = representationService.findVocabularyByResourceId(myVoc2.getId());

		savedMyVoc1.setName("namev2");

		Vocabulary savedMyVoc2Again = representationService.saveVocabulary(savedMyVoc1);
		Vocabulary savedMyVoc3Again = representationService.saveVocabulary(savedMyVoc2);
		resetTransaction();
		List<Vocabulary> latestVocabularies = representationService.findVocabularies();
		assertTrue(latestVocabularies.contains(savedMyVoc2Again));
		assertTrue(latestVocabularies.contains(savedMyVoc3Again));

		int index = latestVocabularies.indexOf(savedMyVoc1);
		if (index >= 0) {
			assertFalse(latestVocabularies.get(index).getName().equals(myVoc.getName()));
		}
		// NOTE: Vocabulary equals checks for URI hence changing name will not be sufficient for the following
		// assertion. This assertion is not valid.
		// assertFalse(latestVocabularies.contains(savedMyVoc1));
	}

	@Test
	@Transactional
	public void testFindTermBySignifierAndCreateIfNotExists() {

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		communityService.save(sp);
		resetTransaction();

		Term term = representationService.findTermBySignifierAndCreateIfNotExists(vocabulary,
				"SignifierThatDoesntExists");
		assertNotNull(term);
		assertEquals("SignifierThatDoesntExists", term.getSignifier());
		resetTransaction();

		Term result = representationService.findTermBySignifierAndCreateIfNotExists(vocabulary,
				"SignifierThatDoesntExists");
		assertNotNull(result);
		assertEquals(term.getId(), result.getId());
	}

	@Test
	public void testFindPreferredTerm() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		ObjectType lookedUpOT = meaningService.findObjectTypeByResourceId(person.getObjectType().getId());
		Vocabulary lookedUpVoc = representationService.findVocabularyByResourceId(vocabulary.getId());
		Term lookedUpTerm = representationService.findPreferredTerm(lookedUpOT, lookedUpVoc);
		assertNotNull(lookedUpTerm);
		assertEquals(person, lookedUpTerm);
	}

	@Test
	public void testFindPreferredTermGlobal() {
		Vocabulary voc1 = representationFactory.makeVocabulary(sp, "findPreferredTermGlobal voc1.ns",
				"findPreferredTermGlobal voc1");
		Vocabulary voc2 = representationFactory.makeVocabulary(sp, "findPreferredTermGlobal voc2.ns",
				"findPreferredTermGlobal voc2");

		Term t1 = representationFactory.makeTerm(voc1, "Term1");
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());
		t1 = representationService.findTermByResourceId(t1.getId());
		voc2 = representationService.findVocabularyByResourceId(voc2.getId());
		Term t2 = representationFactory.makeTerm(voc2, "Term2", t1.getObjectType());
		representationService.saveVocabulary(voc2);
		resetTransaction();

		t1 = representationService.findTermByResourceId(t1.getId());
		voc1 = representationService.findVocabularyByResourceId(voc1.getId());
		Term t3 = representationFactory.makeTerm(voc1, "Term2", t1.getObjectType());
		representationService.saveTerm(t3);
		resetTransaction();

		t1 = representationService.findTermByResourceId(t1.getId());
		t2 = representationService.findTermByResourceId(t2.getId());
		t3 = representationService.findTermByResourceId(t3.getId());
		Term preferredTerm = representationService.findPreferredTerm(t3.getObjectType());
		assertNotNull(preferredTerm);
		assertEquals(t1, preferredTerm);
		assertEquals(t2.getMeaning(), preferredTerm.getMeaning());
		assertEquals(t3.getMeaning(), preferredTerm.getMeaning());
	}

	@Test
	public void testFindTermsByType() {
		List<Term> result = representationService.findTermsByType(meaningService.findMetaObjectType());
		assertFalse(result.isEmpty());
	}

	@Test
	public void testFindPreferredTermInAllIncorporatedVocabularies() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term human = representationFactory.makeTerm(vocabulary, "Human");
		communityService.save(sp);
		resetTransaction();

		person = representationService.findTermByResourceId(person.getId());
		human = representationService.findTermByResourceId(human.getId());
		representationService.addSynonym(person, human);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		ObjectType lookedUpOT = meaningService.findObjectTypeByResourceId(person.getObjectType().getId());
		Vocabulary lookedUpVoc = representationService.findVocabularyByResourceId(vocabulary.getId());
		Term lookedUpTerm = representationService.findPreferredTermInAllIncorporatedVocabularies(lookedUpOT,
				lookedUpVoc);
		human = representationService.findTermByResourceId(human.getId());
		assertNotNull(lookedUpTerm);
		assertEquals(person, lookedUpTerm);
		assertFalse(lookedUpTerm.equals(human));
		assertFalse(human.getIsPreferred());
	}

	@Test
	public void testCharacteristicForm() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "testCharacteristicForm");
		Term term1 = representationFactory.makeTerm(vocabulary, "Person");
		representationFactory.makeTerm(vocabulary, "A");
		representationFactory.makeTerm(vocabulary, "B");
		CharacteristicForm charForm = representationFactory.makeCharacteristicForm(vocabulary, term1, "is a driver");

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(vocabulary);
		assertTrue(vocabulary.isPersisted());

		resetTransaction();

		term1 = representationService.findTermByResourceId(term1.getId());
		charForm = representationService.findCharacteristicFormByResourceId(charForm.getId());
		assertNotNull(charForm);
		assertEquals(term1.getId(), charForm.getTerm().getId());

		representationService.saveTerm(term1);
		resetTransaction();

		term1 = representationService.findTermByResourceId(term1.getId());
		charForm = representationService.findCharacteristicFormByResourceId(charForm.getId());
		assertNotNull(charForm);
		assertEquals(term1.getId(), charForm.getTerm().getId());
	}

	@Test
	public void testFindGeneralAndSpecializedConcepts() {
		// SET UP
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns",
				"testFindAllGeneralConceptRepresentation");
		Term term1 = representationFactory.makeTerm(vocabulary, "Person");
		Term term2 = representationFactory.makeTerm(vocabulary, "A");
		Term term3 = representationFactory.makeTerm(vocabulary, "B");

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		term1 = representationService.findTermByResourceId(term1.getId());
		term2 = representationService.findTermByResourceId(term2.getId());
		term3 = representationService.findTermByResourceId(term3.getId());

		((ObjectType) term3.getMeaning()).setGeneralConcept(term2.getMeaning());
		((ObjectType) term2.getMeaning()).setGeneralConcept(term1.getMeaning());

		communityService.save(vocabulary.getCommunity());
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		term1 = representationService.findTermByResourceId(term1.getId());
		term2 = representationService.findTermByResourceId(term2.getId());
		term3 = representationService.findTermByResourceId(term3.getId());

		// DO TESTS
		List<Representation> generalReps = representationService.findAllGeneralConceptRepresentation(term3);
		assertTrue(generalReps.contains(term1));
		assertTrue(generalReps.contains(term2));
		generalReps = representationService.findAllGeneralConceptRepresentation(term2);
		assertTrue(generalReps.contains(term1));
		generalReps = representationService.findAllGeneralConceptRepresentation(term1);
		assertTrue(generalReps.isEmpty());

		Representation rep;
		rep = representationService.findGeneralConceptRepresentation(term3);
		assertEquals(term2, rep);
		rep = representationService.findGeneralConceptRepresentation(term2);
		assertEquals(term1, rep);
		rep = representationService.findGeneralConceptRepresentation(term1);
		assertTrue(rep.getMeaning().equals(meaningService.findMetaThing()));

	}

	/**
	 * This is a major impact bug fixed on truk and backported, needs a special attention. Steps: Add terms ST, PC,
	 * Applicant, Arrangement. Add PC as general concept of ST. Add Arr as general concept of App. Add BFTF ST role /
	 * corole Turnover. Now the taxonomical relation of Application is disappeared.
	 */
	@Test
	public void testBug5800() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "testCharacteristicForm");
		Term ST = representationFactory.makeTerm(vocabulary, "ST");
		Term PC = representationFactory.makeTerm(vocabulary, "PC");
		Term Arr = representationFactory.makeTerm(vocabulary, "Arr");
		Term App = representationFactory.makeTerm(vocabulary, "App");

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		ST = representationService.findTermByResourceId(ST.getId());
		PC = representationService.findTermByResourceId(PC.getId());
		ST.getObjectType().setGeneralConcept(PC.getObjectType());
		representationService.saveVocabulary(vocabulary);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Arr = representationService.findTermByResourceId(Arr.getId());
		App = representationService.findTermByResourceId(App.getId());
		App.getObjectType().setGeneralConcept(Arr.getObjectType());
		representationService.saveVocabulary(vocabulary);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		ST = representationService.findTermByResourceId(ST.getId());
		Term Turnover = representationFactory.makeTerm(vocabulary, "Turnover");
		representationFactory.makeBinaryFactTypeForm(vocabulary, ST, "role", "corole", Turnover);
		representationService.saveVocabulary(vocabulary);
		resetTransaction();

		Arr = representationService.findTermByResourceId(Arr.getId());
		App = representationService.findTermByResourceId(App.getId());
		assertEquals(Arr.getObjectType(), App.getObjectType().getGeneralConcept());
	}

	@Test
	public void testVocabulariesToMove() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "testVocabulariesToMove");
		Vocabulary destVocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns.destination",
				"testVocabulariesToMove destination");
		Term ST = representationFactory.makeTerm(vocabulary, "ST");
		communityService.save(sp);
		resetTransaction();

		destVocabulary = representationService.findVocabularyByResourceId(destVocabulary.getId());
		ST = representationService.findTermByResourceId(ST.getId());
		Collection<Vocabulary> vocabulariesForMove = representationService.findVocabulariesToMove(ST);
		assertEquals(1, vocabulariesForMove.size());
		assertEquals(destVocabulary, vocabulariesForMove.iterator().next());
	}

	/**
	 * BUG - #6889. Taxonomy shows deleted term as parent
	 */
	@Test
	public void testTaxonomyWithTermRemove() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns",
				"testTaxonomyWithTermRemove");
		Term parent = representationFactory.makeTerm(vocabulary, "Parent");
		Term child = representationFactory.makeTerm(vocabulary, "Child");
		Term grandChild = representationFactory.makeTerm(vocabulary, "Grand Child");

		// Build the taxonomy Parent -> Child -> Grand Child
		child.getObjectType().setGeneralConcept(parent.getObjectType());
		grandChild.getObjectType().setGeneralConcept(child.getObjectType());

		communityService.save(sp);
		resetTransaction();

		// Remove child
		assertBeforeRemove(parent, child, grandChild);
		child = representationService.findTermByResourceId(child.getId());
		representationService.remove(child);
		resetTransaction();
		assertAfterRemove(parent, child, grandChild);
	}

	@Test
	public void testTaxonomyWithBFTFRemove() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns",
				"testTaxonomyWithTermRemove");
		Term head1 = representationFactory.makeTerm(vocabulary, "H1");
		Term tail1 = representationFactory.makeTerm(vocabulary, "T1");
		BinaryFactTypeForm parent = representationFactory.makeBinaryFactTypeForm(vocabulary, head1, "has", "of", tail1);

		Term head2 = representationFactory.makeTerm(vocabulary, "H2");
		Term tail2 = representationFactory.makeTerm(vocabulary, "T2");
		BinaryFactTypeForm child = representationFactory.makeBinaryFactTypeForm(vocabulary, head2, "has", "of", tail2);

		Term head3 = representationFactory.makeTerm(vocabulary, "H3");
		Term tail3 = representationFactory.makeTerm(vocabulary, "T3");
		BinaryFactTypeForm grandChild = representationFactory.makeBinaryFactTypeForm(vocabulary, head3, "has", "of",
				tail3);

		// Build taxonomic consistency with terms.
		head2.getObjectType().setGeneralConcept(head1.getObjectType());
		head3.getObjectType().setGeneralConcept(head2.getObjectType());

		tail2.getObjectType().setGeneralConcept(tail1.getObjectType());
		tail3.getObjectType().setGeneralConcept(tail2.getObjectType());

		communityService.save(sp);

		resetTransaction();

		sp = communityService.findCommunity(sp.getId());
		parent = representationService.findBinaryFactTypeFormByResourceId(parent.getId());
		child = representationService.findBinaryFactTypeFormByResourceId(child.getId());
		grandChild = representationService.findBinaryFactTypeFormByResourceId(grandChild.getId());

		// Build the taxonomy Parent -> Child -> Grand Child
		child.getBinaryFactType().setGeneralConcept(parent.getBinaryFactType());
		grandChild.getBinaryFactType().setGeneralConcept(child.getBinaryFactType());

		communityService.save(sp);
		resetTransaction();

		// Remove child
		assertBeforeRemove(parent, child, grandChild);
		child = representationService.findBinaryFactTypeFormByResourceId(child.getId());
		representationService.remove(child);
		resetTransaction();
		assertAfterRemove(parent, child, grandChild);
	}

	@Test
	public void testTaxonomyWithCharacteristicRemove() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns",
				"testTaxonomyWithTermRemove");
		Term term1 = representationFactory.makeTerm(vocabulary, "Term1");
		CharacteristicForm parent = representationFactory.makeCharacteristicForm(vocabulary, term1, "is a CF1");

		Term term2 = representationFactory.makeTerm(vocabulary, "Term2");
		CharacteristicForm child = representationFactory.makeCharacteristicForm(vocabulary, term2, "is a CF2");

		Term term3 = representationFactory.makeTerm(vocabulary, "Term3");
		CharacteristicForm grandChild = representationFactory.makeCharacteristicForm(vocabulary, term3, "is a CF3");

		// Build the taxonomy Parent -> Child -> Grand Child
		child.getCharacteristic().setGeneralConcept(parent.getCharacteristic());
		grandChild.getCharacteristic().setGeneralConcept(child.getCharacteristic());

		term2.getObjectType().setGeneralConcept(term1.getObjectType());
		term3.getObjectType().setGeneralConcept(term2.getObjectType());

		communityService.save(sp);
		resetTransaction();

		// Remove child
		assertBeforeRemove(parent, child, grandChild);
		child = representationService.findCharacteristicFormByResourceId(child.getId());
		representationService.remove(child);
		resetTransaction();
		assertAfterRemove(parent, child, grandChild);
	}

	@Test
	public void testFindAllRelatedRepresentations() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns",
				"testFindAllRelatedRepresentations");
		Term term1 = representationFactory.makeTerm(vocabulary, "Term1");
		Term term2 = representationFactory.makeTerm(vocabulary, "Term2");
		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(vocabulary, term1, "has", "of", term2);
		CharacteristicForm cf = representationFactory.makeCharacteristicForm(vocabulary, term1, "term 1");
		Term child = representationFactory.makeTerm(vocabulary, "child");
		Term grandChild = representationFactory.makeTerm(vocabulary, "grand child");
		Term synTerm1 = representationFactory.makeTerm(vocabulary, "syn Term 1");
		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), bftf, "Definition for BFTF");

		cf = representationService.findCharacteristicFormByResourceId(cf.getId());
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), cf, "Defintion for CF");
		representationService.saveVocabulary(vocabulary);
		resetTransaction();

		term1 = representationService.findTermByResourceId(term1.getId());
		synTerm1 = representationService.findTermByResourceId(synTerm1.getId());
		representationService.createSynonym(term1, synTerm1);
		resetTransaction();

		term1 = representationService.findTermByResourceId(term1.getId());
		child = representationService.findTermByResourceId(child.getId());
		grandChild = representationService.findTermByResourceId(grandChild.getId());
		representationService.setGeneralConceptRepresentation(child, term1);
		representationService.setGeneralConceptRepresentation(grandChild, child);

		resetTransaction();

		term1 = representationService.findTermByResourceId(term1.getId());
		Collection<Representation> relatedRepresentations = representationService.findAllRelatedRepresentations(term1);
		Assert.assertEquals(4, relatedRepresentations.size());
	}

	@Test
	public void testRenameTerm() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "testRenameTerm");
		Term term = representationFactory.makeTerm(vocabulary, "Term");
		communityService.save(sp);
		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());
		Assert.assertNotNull(term);

		representationService.changeSignifier(term, "Term renamed");
		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());
		Assert.assertNotNull(term);
		Assert.assertEquals("Term renamed", term.getSignifier());
	}

	@Test
	public void testRenameVocabulary() {
		String originalName = "testRenameVocabulary";
		String newName = "testRenameVocabulary renamed";
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", originalName);
		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Assert.assertNotNull(vocabulary);

		representationService.changeName(vocabulary, newName);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Assert.assertNotNull(vocabulary);
		Assert.assertEquals(newName, vocabulary.getName());

		// Rename back to old name again.

		representationService.changeName(vocabulary, originalName);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Assert.assertNotNull(vocabulary);
		Assert.assertEquals(originalName, vocabulary.getName());
	}

	@Test
	public void testChangeCharacteristicForm() {
		String originalRole = "role";
		String newRole = "role renamed";
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns",
				"testChangeCharacteristicForm");
		Term term = representationFactory.makeTerm(vocabulary, "Term");
		CharacteristicForm cf = representationFactory.makeCharacteristicForm(vocabulary, term, originalRole);
		communityService.save(sp);
		resetTransaction();

		cf = representationService.findCharacteristicFormByResourceId(cf.getId());
		Assert.assertNotNull(cf);

		representationService.changeCharacteristicForm(cf, cf.getTerm(), newRole);
		resetTransaction();

		cf = representationService.findCharacteristicFormByResourceId(cf.getId());
		Assert.assertNotNull(cf);
		Assert.assertEquals(newRole, cf.getRole());
	}

	@Test
	public void testChangeBFTF() {
		String originalRole = "role";
		String newRole = "role renamed";
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns",
				"testChangeCharacteristicForm");
		Term headTerm = representationFactory.makeTerm(vocabulary, "Head");
		Term tailTerm = representationFactory.makeTerm(vocabulary, "Tail");
		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(vocabulary, headTerm, originalRole,
				"co role", tailTerm);
		communityService.save(sp);
		resetTransaction();

		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		Assert.assertNotNull(bftf);

		representationService.changeBinaryFactTypeForm(bftf, bftf.getHeadTerm(), newRole, bftf.getCoRole(),
				bftf.getTailTerm());
		resetTransaction();

		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		Assert.assertNotNull(bftf);
		Assert.assertEquals(newRole, bftf.getRole());
	}

	@Test
	public void testRemoveRepresentation() {
		communityService.save(sp);

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "testRemoveTermWithNames.ns",
				"testRemoveTermWithNames");
		representationService.saveVocabulary(vocabulary);
		Term term = representationFactory.makeTerm(vocabulary, "Term");
		ObjectType termOT = term.getObjectType();
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), term, "test");
		representationService.save(term);

		Term tail = representationFactory.makeTerm(vocabulary, "Tail");
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), tail, "test");
		representationService.save(tail);

		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(vocabulary, term, "has", "of", tail);
		BinaryFactType bft = bftf.getBinaryFactType();
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), bftf, "test");
		representationService.save(bftf);

		CharacteristicForm cf = representationFactory.makeCharacteristicForm(vocabulary, term, "role");
		Characteristic characteristic = cf.getCharacteristic();
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), cf, "test");
		representationService.save(cf);

		resetTransaction();

		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		representationService.remove(bftf);
		resetTransaction();

		bft = meaningService.findBinaryFactTypeByResourceId(bft.getId());
		assertNull(bft);

		cf = representationService.findCharacteristicFormByResourceId(cf.getId());
		representationService.remove(cf);
		resetTransaction();

		characteristic = meaningService.findCharacteristicByResourceId(characteristic.getId());
		assertNull(characteristic);

		term = representationService.findTermByResourceId(term.getId());
		representationService.remove(term);
		resetTransaction();

		termOT = meaningService.findObjectTypeByResourceId(termOT.getId());
		assertNull(termOT);
	}

	@Test
	public void testStatuses() {
		List<Term> terms = representationService.findStatusTerms();
		Assert.assertEquals(9, terms.size());

		ObjectType statusOT = meaningService.getObjectTypeWithError(MeaningConstants.META_STATUS_TYPE_UUID.toString());
		Term status = statusOT.getTerms().iterator().next();
		Vocabulary statusesVoc = representationService.findVocabularyByUri(Constants.STATUSES_VOCABULARY_URI);
		Term newStatus = representationFactory.makeTermOfType(statusesVoc, "MyStatus", statusOT);
		representationService.save(newStatus);

		resetTransaction();

		terms = representationService.findStatusTerms();
		Assert.assertEquals(10, terms.size());

		Term candidate = null;
		for (final Term t : terms) {
			if (t.getSignifier().equals("Candidate")) {
				candidate = t;
				break;
			}
		}
		assertNotNull(candidate);

		try {
			representationService.remove(candidate);
			fail();
		} catch (Exception e) {
			// This is ok!
		}

		resetTransaction();

		terms = representationService.findStatusTerms();
		Assert.assertEquals(10, terms.size());

		statusesVoc = representationService.findVocabularyByUri(Constants.STATUSES_VOCABULARY_URI);
		newStatus = representationService.findTermBySignifier(statusesVoc, "MyStatus");
		representationService.remove(newStatus);

		resetTransaction();

		terms = representationService.findStatusTerms();
		Assert.assertEquals(9, terms.size());
	}

	/**
	 * Test if when we remove a representation that also its meaning is deleted when it is possible.
	 */
	@Test
	public void testRemoveTermMeaning() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "testRemoveRepresentationMeaning.ns",
				"testRemoveRepresentationMeaning");
		Term term = representationFactory.makeTerm(vocabulary, "Term");
		ObjectType termOT = term.getObjectType();
		communityService.save(sp);
		resetTransaction();

		representationService.remove(representationService.findTermByResourceId(term.getId()));

		resetTransaction();

		termOT = meaningService.findObjectTypeByResourceId(termOT.getId());
		// Meaning should have been removed.
		assertNull(termOT);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		term = representationFactory.makeTerm(vocabulary, "Term");
		termOT = term.getObjectType();
		representationService.save(term);
		Term synonym = representationFactory.makeTerm(vocabulary, "Synonym");
		term.getObjectType();
		representationService.save(synonym);
		resetTransaction();
		representationService.createSynonym(representationService.findTermByResourceId(term.getId()),
				representationService.findTermByResourceId(synonym.getId()));
		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());
		synonym = representationService.findTermByResourceId(synonym.getId());
		assertEquals(term.getMeaning(), synonym.getMeaning());

		representationService.remove(term);
		resetTransaction();

		termOT = meaningService.findObjectTypeByResourceId(termOT.getId());
		// Meaning should not have been removed as it is still used by the Term.
		assertNotNull(termOT);
		assertEquals(termOT, representationService.findTermByResourceId(synonym.getId()).getMeaning());
	}

	@Test
	public void testCreateConceptTypes() {
		Term asset = representationService.createConceptType("Asset");

		assertEquals("Asset", asset.getSignifier());
		assertEquals(meaningService.findMetaObjectType(), asset.getObjectType().getGeneralConcept());
		assertEquals(1, representationService.findMetamodelExtensionsVocabulary().getTerms().size());
		assertEquals(asset, representationService.findMetamodelExtensionsVocabulary().getTerm("Asset"));

		resetTransaction();
		asset = representationService.findTermByResourceId(asset.getId());

		Term businessAsset = representationService.createConceptType("Business Asset", asset.getObjectType());

		assertEquals("Business Asset", businessAsset.getSignifier());
		assertEquals(asset.getObjectType(), businessAsset.getObjectType().getGeneralConcept());
		assertEquals(2, representationService.findMetamodelExtensionsVocabulary().getTerms().size());
		assertEquals(businessAsset, representationService.findMetamodelExtensionsVocabulary().getTerm("Business Asset"));

		// test suggest

		List<Term> terms = suggesterService.suggestConceptTypeTerms(asset, "");
		assertTrue(terms.contains(asset));
		assertTrue(terms.contains(businessAsset));
	}

	private void assertBeforeRemove(Representation parent, Representation child, Representation grandChild) {
		parent = representationService.findRepresentationByResourceId(parent.getId());
		child = representationService.findRepresentationByResourceId(child.getId());
		grandChild = representationService.findRepresentationByResourceId(grandChild.getId());
		Assert.assertEquals(parent.getMeaning().getId(), ((Concept) child.getMeaning()).getGeneralConcept().getId());
		Assert.assertEquals(child.getMeaning().getId(), ((Concept) grandChild.getMeaning()).getGeneralConcept().getId());
	}

	private void assertAfterRemove(Representation parent, Representation child, Representation grandChild) {
		parent = representationService.findRepresentationByResourceId(parent.getId());
		child = representationService.findRepresentationByResourceId(child.getId());
		grandChild = representationService.findRepresentationByResourceId(grandChild.getId());
		Assert.assertNull(child);
		Assert.assertEquals(objectTypeDao.getMetaThing().getId(), ((Concept) grandChild.getMeaning())
				.getGeneralConcept().getId());
	}
}
