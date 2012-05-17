package com.collibra.dgc.service.relations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.relation.Relation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.service.AbstractServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestRelationsService extends AbstractServiceTest {

	private static final String SEM_COMM_1_NAME = "Sem Com Name";
	private static final String SEM_COMM_1_URI = "Sem Com URI";
	private Community semComm;
	private Community spComm;
	private Vocabulary vocabulary;

	@Test
	public void testCreateRelationType() {
		initialize();

		Vocabulary vocabulary = representationService.findMetamodelExtensionsVocabulary();
		Term discipline = representationFactory.makeTerm(vocabulary, "Discipline");
		Term fwoCode = representationFactory.makeTerm(vocabulary, "fwoCode");
		representationService.saveVocabulary(vocabulary);

		BinaryFactTypeForm type = relationFactory.makeRelationType(discipline, "has code", "code of", fwoCode);
		representationService.saveBinaryFactTypeForm(type);

		resetTransaction();

		type = representationService.findBinaryFactTypeFormByResourceId(type.getId());

		discipline = representationService.findTermByResourceId(discipline.getId());
		fwoCode = representationService.findTermByResourceId(fwoCode.getId());
		spComm = communityService.findCommunity(spComm.getId());
		Vocabulary testVoc = representationFactory.makeVocabulary(spComm, "TEST", "Test Voc");
		Term biology = representationFactory.makeTermOfType(testVoc, "biology", discipline.getObjectType());
		Term fwo001 = representationFactory.makeTermOfType(testVoc, "fwo001", fwoCode.getObjectType());
		representationService.saveVocabulary(testVoc);
		resetTransaction();
		biology = representationService.findTermByResourceId(biology.getId());
		fwo001 = representationService.findTermByResourceId(fwo001.getId());
		type = representationService.findBinaryFactTypeFormByResourceId(type.getId());
		Relation r = relationFactory.makeRelation(biology, fwo001, type);
		r = relationService.saveRelation(r, true);

		resetTransaction();

		biology = representationService.findTermByResourceId(biology.getId());
		fwo001 = representationService.findTermByResourceId(fwo001.getId());
		discipline = representationService.findTermByResourceId(discipline.getId());
		fwoCode = representationService.findTermByResourceId(fwoCode.getId());

		Relation rel = relationService.getRelation(r.getId());
		assertNotNull(rel);
		assertTrue(rel.isSourceToTargetDirection());
		assertEquals(biology, rel.getSource());
		assertEquals(fwo001, rel.getTarget());
		assertEquals(discipline, rel.getType().getHeadTerm());
		assertEquals(fwoCode, rel.getType().getTailTerm());
	}

	@Test
	public void testRelationTypeInheritance() {

		initialize();

		Vocabulary vocabulary = representationService.findMetamodelExtensionsVocabulary();
		Term discipline = representationFactory.makeTerm(vocabulary, "Discipline");
		Term smallDiscipline = representationFactory.makeTerm(vocabulary, "Small Discipline");
		Term code = representationFactory.makeTerm(vocabulary, "code");
		Term fwoCode = representationFactory.makeTerm(vocabulary, "fwoCode");

		smallDiscipline.getObjectType().setGeneralConcept(discipline.getObjectType());
		fwoCode.getObjectType().setGeneralConcept(code.getObjectType());

		representationService.saveVocabulary(vocabulary);

		BinaryFactTypeForm type = relationFactory.makeRelationType(discipline, "has code", "code of", code);
		representationService.saveBinaryFactTypeForm(type);

		BinaryFactTypeForm type2 = relationFactory.makeRelationType(smallDiscipline, "has fwo code", "fwo code of",
				fwoCode);
		representationService.saveBinaryFactTypeForm(type2);

		resetTransaction();

		type = representationService.findBinaryFactTypeFormByResourceId(type.getId());
		type2 = representationService.findBinaryFactTypeFormByResourceId(type2.getId());

		spComm = communityService.findCommunity(spComm.getId());
		Vocabulary testVoc = representationFactory.makeVocabulary(spComm, "TEST", "Test Voc");

		smallDiscipline = representationService.findTermByResourceId(smallDiscipline.getId());
		fwoCode = representationService.findTermByResourceId(fwoCode.getId());
		code = representationService.findTermByResourceId(code.getId());
		discipline = representationService.findTermByResourceId(discipline.getId());

		Term biology = representationFactory.makeTermOfType(testVoc, "biology", smallDiscipline.getObjectType());
		Term fwo001 = representationFactory.makeTermOfType(testVoc, "fwo001", fwoCode.getObjectType());
		Term code001 = representationFactory.makeTermOfType(testVoc, "code 001", code.getObjectType());
		Term discipline01 = representationFactory.makeTermOfType(testVoc, "discipline 01", discipline.getObjectType());
		Term noDiscipline = representationFactory.makeTerm(testVoc, "no discipline");
		Term noCode = representationFactory.makeTerm(testVoc, "no code");

		representationService.saveVocabulary(testVoc);
		resetTransaction();
		biology = representationService.findTermByResourceId(biology.getId());
		fwo001 = representationService.findTermByResourceId(fwo001.getId());
		code001 = representationService.findTermByResourceId(code001.getId());
		discipline01 = representationService.findTermByResourceId(discipline01.getId());
		noDiscipline = representationService.findTermByResourceId(noDiscipline.getId());
		noCode = representationService.findTermByResourceId(noCode.getId());

		Relation r1 = relationFactory.makeRelation(biology, fwo001, type);
		r1 = relationService.saveRelation(r1, true);

		Relation r2 = relationFactory.makeRelation(biology, code001, type);
		r2 = relationService.saveRelation(r2, true);

		Relation r3 = relationFactory.makeRelation(discipline01, fwo001, type);
		r3 = relationService.saveRelation(r3, true);

		Relation r4 = relationFactory.makeRelation(discipline01, code001, type);
		r4 = relationService.saveRelation(r4, true);

		Relation r5 = null;
		try {
			r5 = relationFactory.makeRelation(noDiscipline, code001, type);
			relationService.saveRelation(r5, true);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(DGCErrorCodes.RELATION_SRC_TGT_INCOMPATIBLE, e.getErrorCode());
		}

		Relation r6 = null;
		try {
			r6 = relationFactory.makeRelation(discipline01, noCode, type);
			relationService.saveRelation(r6, true);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(DGCErrorCodes.RELATION_SRC_TGT_INCOMPATIBLE, e.getErrorCode());
		}

		Relation r7 = relationFactory.makeRelation(biology, fwo001, type2);
		r7 = relationService.saveRelation(r7, true);

		Relation r8 = null;
		try {
			r8 = relationFactory.makeRelation(discipline01, fwo001, type2);
			relationService.saveRelation(r8, true);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(DGCErrorCodes.RELATION_SRC_TGT_INCOMPATIBLE, e.getErrorCode());
		}

		resetTransaction();
		biology = representationService.findTermByResourceId(biology.getId());
		fwo001 = representationService.findTermByResourceId(fwo001.getId());
		code001 = representationService.findTermByResourceId(code001.getId());
		discipline01 = representationService.findTermByResourceId(discipline01.getId());
		noDiscipline = representationService.findTermByResourceId(noDiscipline.getId());
		noCode = representationService.findTermByResourceId(noCode.getId());

		// test find methods
		Collection<Relation> relations = null;

		relations = relationService.findBySource(biology);
		assertEquals(3, relations.size());
		assertTrue(relations.contains(r1));
		assertTrue(relations.contains(r2));
		assertTrue(relations.contains(r7));

		relations = relationService.findBySourceAndTarget(biology, code001);
		assertEquals(1, relations.size());
		assertTrue(relations.contains(r2));

		relations = relationService.findBySourceAndTargetAndType(type, discipline01, fwo001);
		assertEquals(1, relations.size());
		assertTrue(relations.contains(r3));

		relations = relationService.findBySourceAndType(type, discipline01);
		assertEquals(2, relations.size());
		assertTrue(relations.contains(r3));
		assertTrue(relations.contains(r4));

		relations = relationService.findByTarget(fwo001);
		assertEquals(3, relations.size());
		assertTrue(relations.contains(r1));
		assertTrue(relations.contains(r3));
		assertTrue(relations.contains(r7));

		relations = relationService.findByTargetAndType(type, code001);
		assertEquals(2, relations.size());
		assertTrue(relations.contains(r2));
		assertTrue(relations.contains(r4));

		relations = relationService.findByTargetAndType(type2, fwo001);
		assertEquals(1, relations.size());
		assertTrue(relations.contains(r7));

		relations = relationService.findByType(type);
		assertEquals(4, relations.size());

		relations = relationService.findByType(type2);
		assertEquals(1, relations.size());

		// test suggest methods

		Collection<BinaryFactTypeForm> relationTypes = null;

		relationTypes = relationService.suggestRelationTypesForSourceTerm(biology);
		assertEquals(2, relationTypes.size());
		assertTrue(relationTypes.contains(type));
		assertTrue(relationTypes.contains(type2));

		relationTypes = relationService.suggestRelationTypesForTargetTerm(fwo001);
		assertEquals(2, relationTypes.size());
		assertTrue(relationTypes.contains(type));
		assertTrue(relationTypes.contains(type2));

		relationTypes = relationService.suggestRelationTypesForTargetTerm(code001);
		assertEquals(1, relationTypes.size());
		assertTrue(relationTypes.contains(type));

		relationTypes = relationService.suggestRelationTypesForTargetTerm(discipline01);
		assertTrue(relationTypes.isEmpty());

		resetTransaction();

		r1 = relationService.getRelation(r1.getId());
		r2 = relationService.getRelation(r2.getId());
		r3 = relationService.getRelation(r3.getId());
		r4 = relationService.getRelation(r4.getId());

		// test remove
		relationService.remove(r4);

		relations = relationService.findByTargetAndType(type, code001);
		assertEquals(1, relations.size());
		assertTrue(relations.contains(r2));
		assertFalse(relations.contains(r4));

		resetTransaction();

		r1 = relationService.getRelation(r1.getId());
		r2 = relationService.getRelation(r2.getId());
		r3 = relationService.getRelation(r3.getId());

		assertNull(relationService.getRelation(r4.getId()));

		relations = relationService.findBySourceAndType(type, discipline01);
		assertEquals(1, relations.size());
		assertTrue(relations.contains(r3));
		assertFalse(relations.contains(r4));

		// remove a term should also remove the relations

		resetTransaction();

		discipline01 = representationService.findTermByResourceId(discipline01.getId());
		r2 = relationService.getRelation(r2.getId());
		fwo001 = representationService.findTermByResourceId(fwo001.getId());

		representationService.remove(fwo001);

		assertNull(representationService.findTermByResourceId(fwo001.getId()));
		assertNull(relationService.getRelation(r1.getId()));
		assertNull(relationService.getRelation(r3.getId()));

		relations = relationService.findBySourceAndType(type, discipline01);
		assertEquals(0, relations.size());

		// remove a relation type should also remove all its relations

		resetTransaction();

		type = representationService.findBinaryFactTypeFormByResourceId(type.getId());
		representationService.remove(type);

		assertNull(representationService.findBinaryFactTypeFormByResourceId(type.getId()));
		assertNull(relationService.getRelation(r2.getId()));
		assertTrue(relationService.findByType(type).isEmpty());
		assertTrue(relationService.findBySource(discipline).isEmpty());
	}

	private void initialize() {
		semComm = communityFactory.makeCommunity("SC", "SC URI");
		assertNotNull(semComm);

		spComm = communityFactory.makeCommunity(semComm, "SP", "SP URI");
		assertNotNull(spComm);

		vocabulary = representationFactory.makeVocabulary(spComm, "Communities Test URI", "Voc comm test");
		communityService.save(semComm);

		resetTransaction();

		semComm = communityService.findCommunity(semComm.getId());
		spComm = communityService.findCommunity(spComm.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
	}
}
