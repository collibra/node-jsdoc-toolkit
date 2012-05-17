/**
 * 
 */
package com.collibra.dgc.core.model.relation.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.relation.Relation;
import com.collibra.dgc.core.model.relation.RelationFactory;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.service.RepresentationService;

/**
 * @author fvdmaele
 * 
 */
@Service
public class RelationFactoryImpl implements RelationFactory {
	private static final Logger log = LoggerFactory.getLogger(RelationFactoryImpl.class);

	@Autowired
	private RepresentationService representationService;

	@Autowired
	private RepresentationFactory representationFactory;

	@Override
	public BinaryFactTypeForm makeRelationType(Term sourceType, String role, String coRole, Term targetType) {
		Vocabulary metaVoc = representationService.findMetamodelExtensionsVocabulary();
		return representationFactory.makeBinaryFactTypeForm(metaVoc, sourceType, role, coRole, targetType);
	}

	@Override
	public Relation makeRelation(Term source, Term target, BinaryFactTypeForm type) {

		return new RelationImpl(type, source, target);
	}

	@Override
	public Relation makeRelation(Vocabulary sourceVoc, String sourceSignifier, Vocabulary targetVoc,
			String targetSignifier, BinaryFactTypeForm type) {
		Term source = representationService.findTermBySignifier(sourceVoc, sourceSignifier);
		if (source == null) {
			source = representationFactory.makeTerm(sourceVoc, sourceSignifier);
			source = representationService.saveTerm(source);
		}
		Term target = representationService.findTermBySignifier(targetVoc, targetSignifier);
		if (target == null) {
			target = representationFactory.makeTerm(targetVoc, targetSignifier);
			target = representationService.saveTerm(target);
		}
		return makeRelation(source, target, type);
	}

	private boolean findInGeneralConcepts(Concept type, ObjectType toFind) {
		if (type.getId().equals(toFind.getId()))
			return true;
		else if (!type.getId().equals(MeaningConstants.META_THING_UUID))
			return findInGeneralConcepts(type.getGeneralConcept(), toFind);
		else
			return false;
	}
}
