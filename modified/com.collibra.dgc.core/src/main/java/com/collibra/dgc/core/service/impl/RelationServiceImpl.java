package com.collibra.dgc.core.service.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.RelationDao;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.relation.Relation;
import com.collibra.dgc.core.model.relation.impl.RelationFactoryImpl;
import com.collibra.dgc.core.model.relation.impl.RelationImpl;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.events.EventType;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.observer.events.RelationEventData;
import com.collibra.dgc.core.security.authorization.AuthorizationHelper;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.AttributeService;
import com.collibra.dgc.core.service.RelationService;
import com.collibra.dgc.core.service.RepresentationService;

@Service
public class RelationServiceImpl extends AbstractService implements RelationService {
	private static final Logger log = LoggerFactory.getLogger(RelationFactoryImpl.class);

	@Autowired
	private RepresentationService representationService;

	@Autowired
	private AttributeService attributeService;

	@Autowired
	private AuthorizationHelper authorizationHelper;

	@Autowired
	private RelationDao relationDao;

	@Autowired
	private ConstraintChecker constraintChecker;

	@Override
	public BinaryFactTypeForm saveRelationType(BinaryFactTypeForm type) {
		return representationService.saveBinaryFactTypeForm(type);
	}

	@Override
	public Relation saveRelation(Relation rel, boolean isNew) {

		// permissions for relations are equal to permissions for attributes
		// Note: in the UI perspective, relation permission are identical to attribute permission
		if (isNew) {

			authorizationHelper.checkAuthorization(getCurrentUser(), rel.getSource().getVocabulary(),
					Permissions.TERM_ADD_ATTRIBUTE, DGCErrorCodes.RESOURCE_NO_PERMISSION);

			constraintChecker.checkConstraints(rel);

		} else {

			authorizationHelper.checkAuthorization(getCurrentUser(), rel.getSource().getVocabulary(),
					Permissions.TERM_EDIT_ATTRIBUTE, DGCErrorCodes.RESOURCE_NO_PERMISSION);
		}

		// Send adding event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RELATION,
				new RelationEventData(rel, isNew ? EventType.ADDING : EventType.CHANGING));

		// add the default candidate status if none found
		if (rel.getStatus() == null)
			((RelationImpl) rel).setStatusRepresentation(attributeService.findMetaCandidateStatus());

		// TODO Pierre: Add check constraint

		Relation persistedRelation = relationDao.save(rel);

		// Send added event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RELATION,
				new RelationEventData(persistedRelation, isNew ? EventType.ADDED : EventType.CHANGED));

		return persistedRelation;
	}

	@Override
	public Relation getRelation(final String rId) {
		return relationDao.findById(rId);
	}

	@Override
	public Relation getRelationWithError(String rId) {

		Relation relation = getRelation(rId);

		if (relation == null) {
			String message = "Relation with resource id '" + rId + "' not found";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.RELATION_NOT_FOUND, rId);
		}

		return relation;
	}

	@Override
	public void remove(final Relation rel) {

		// Perform authorization check.
		authorizationHelper.checkAuthorization(getCurrentUser(), rel, Permissions.TERM_REMOVE_ATTRIBUTE,
				DGCErrorCodes.RESOURCE_NO_PERMISSION);

		// Send removing event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RELATION,
				new RelationEventData(rel, EventType.REMOVING));

		relationDao.delete(rel);

		getCurrentSession().flush();

		// Send removed event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RELATION,
				new RelationEventData(rel, EventType.REMOVED));
	}

	@Override
	public Collection<Relation> findByType(BinaryFactTypeForm type) {
		return relationDao.findByType(type);
	}

	@Override
	public Collection<Relation> findBySource(Term source) {
		return relationDao.findBySource(source);
	}

	@Override
	public Collection<Relation> findByTarget(Term target) {
		return relationDao.findByTarget(target);
	}

	@Override
	public Collection<Relation> findBySourceAndTarget(Term source, Term target) {
		return relationDao.findBySourceAndTarget(source, target);
	}

	@Override
	public Collection<Relation> findBySourceAndType(BinaryFactTypeForm type, Term source) {
		return relationDao.findBySourceAndType(type, source);
	}

	@Override
	public Collection<Relation> findByTargetAndType(BinaryFactTypeForm type, Term target) {
		return relationDao.findByTargetAndType(type, target);
	}

	@Override
	public Collection<Relation> findBySourceAndTargetAndType(BinaryFactTypeForm type, Term source, Term target) {
		return relationDao.findBySourceAndTargetAndType(source, target, type);
	}

	@Override
	public Collection<BinaryFactTypeForm> suggestRelationTypesForSourceType(Term typeTerm) {
		List<BinaryFactTypeForm> result = new LinkedList<BinaryFactTypeForm>();

		for (BinaryFactTypeForm relType : representationService.findBinaryFactTypeFormsContainingHeadTerm(typeTerm))
			if (relType.getVocabulary().isMeta())
				result.add(relType);

		for (Representation r : representationService.findAllGeneralConceptRepresentation(typeTerm))
			for (BinaryFactTypeForm relType : representationService.findBinaryFactTypeFormsContainingHeadTerm((Term) r))
				if (relType.getVocabulary().isMeta())
					result.add(relType);

		return result;
	}

	@Override
	public Collection<BinaryFactTypeForm> suggestRelationTypesForSourceTerm(Term t) {

		return suggestRelationTypesForSourceType(representationService.findConceptTypeRepresentation(t));
	}

	@Override
	public Collection<BinaryFactTypeForm> suggestRelationTypesForTargetType(Term typeTerm) {
		List<BinaryFactTypeForm> result = new LinkedList<BinaryFactTypeForm>();

		for (BinaryFactTypeForm relType : representationService.findBinaryFactTypeFormsContainingTailTerm(typeTerm))
			if (relType.getVocabulary().isMeta())
				result.add(relType);

		for (Representation r : representationService.findAllGeneralConceptRepresentation(typeTerm))
			for (BinaryFactTypeForm relType : representationService.findBinaryFactTypeFormsContainingTailTerm((Term) r))
				if (relType.getVocabulary().isMeta())
					result.add(relType);

		return result;
	}

	@Override
	public Collection<BinaryFactTypeForm> suggestRelationTypesForTargetTerm(Term t) {

		return suggestRelationTypesForTargetType(representationService.findConceptTypeRepresentation(t));
	}
}
