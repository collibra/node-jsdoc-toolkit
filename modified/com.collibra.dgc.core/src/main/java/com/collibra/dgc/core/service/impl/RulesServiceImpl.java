package com.collibra.dgc.core.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.BinaryFactTypeFormDao;
import com.collibra.dgc.core.dao.RepresentationDao;
import com.collibra.dgc.core.dao.RuleSetDao;
import com.collibra.dgc.core.dao.RuleStatementDao;
import com.collibra.dgc.core.dao.SimpleStatementDao;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;
import com.collibra.dgc.core.model.rules.FrequencyRuleStatement;
import com.collibra.dgc.core.model.rules.RuleFactory;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.RuleStatement;
import com.collibra.dgc.core.model.rules.SemiparsedRuleStatement;
import com.collibra.dgc.core.model.rules.SimpleRuleStatement;
import com.collibra.dgc.core.observer.ObservationManager;
import com.collibra.dgc.core.observer.events.BinaryFactTypeFormEventData;
import com.collibra.dgc.core.observer.events.EventType;
import com.collibra.dgc.core.observer.events.GlossaryEventCategory;
import com.collibra.dgc.core.observer.events.RuleStatementEventData;
import com.collibra.dgc.core.observer.events.RulesetEventData;
import com.collibra.dgc.core.security.authorization.AuthorizationHelper;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.RulesService;

/**
 * 
 * @author amarnath
 * 
 */
@Service
public class RulesServiceImpl extends AbstractService implements RulesService {
	private static final Logger log = LoggerFactory.getLogger(RulesServiceImpl.class);

	@Autowired
	private RepresentationDao representationDao;
	@Autowired
	private RuleFactory ruleFactory;
	@Autowired
	private RuleServiceHelper ruleServiceHelper;
	@Autowired
	private RuleSetDao ruleSetDao;
	@Autowired
	private RuleStatementDao ruleStatementDao;
	@Autowired
	private SimpleStatementDao simpleStatementDao;
	@Autowired
	private BinaryFactTypeFormDao binaryFactTypeFormDao;
	@Autowired
	private RepresentationFactory representationFactory;
	@Autowired
	private AuthorizationHelper authorizationHelper;

	@Override
	public RuleSet saveRuleSet(RuleSet ruleSet) {

		final boolean isNew = !ruleSet.isPersisted();
		if (isNew) {
			authorizationHelper.checkAuthorization(getCurrentUser(), ruleSet.getVocabulary(), Permissions.RULESET_ADD,
					DGCErrorCodes.RESOURCE_NO_PERMISSION);
		}

		// Send adding event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RULESET,
				new RulesetEventData(ruleSet, isNew ? EventType.ADDING : EventType.CHANGING));
		ruleSet = ruleServiceHelper.saveRuleSet(ruleSet);

		// Send added event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RULESET,
				new RulesetEventData(ruleSet, isNew ? EventType.ADDED : EventType.CHANGED));

		return ruleSet;
	}

	@Override
	public SimpleRuleStatement saveSimpleRuleStatement(final RuleSet ruleSet, SimpleRuleStatement ruleStatement) {

		final boolean isNew = !ruleStatement.isPersisted();
		// Send adding event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RULE_STATEMENT,
				new RuleStatementEventData(ruleSet, ruleStatement, isNew ? EventType.ADDING : EventType.CHANGING));

		ruleStatement = ruleServiceHelper.saveSimpleRuleStatement(ruleSet, ruleStatement);

		// Send added event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RULE_STATEMENT,
				new RuleStatementEventData(ruleSet, ruleStatement, isNew ? EventType.ADDED : EventType.CHANGED));

		return ruleStatement;
	}

	@Override
	public FrequencyRuleStatement saveFrequencyRuleStatement(final RuleSet ruleSet, FrequencyRuleStatement ruleStatement) {

		final boolean isNew = !ruleStatement.isPersisted();
		// Send adding event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RULE_STATEMENT,
				new RuleStatementEventData(ruleSet, ruleStatement, isNew ? EventType.ADDING : EventType.CHANGING));

		ruleStatement = ruleServiceHelper.saveFrequencyRuleStatement(ruleSet, ruleStatement);

		// Send added event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RULE_STATEMENT,
				new RuleStatementEventData(ruleSet, ruleStatement, isNew ? EventType.ADDED : EventType.CHANGED));

		return ruleStatement;
	}

	@Override
	public SemiparsedRuleStatement saveSemiparsedRuleStatement(final RuleSet ruleSet,
			SemiparsedRuleStatement ruleStatement) {

		final boolean isNew = !ruleStatement.isPersisted();
		// Send adding event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RULE_STATEMENT,
				new RuleStatementEventData(ruleSet, ruleStatement, isNew ? EventType.ADDING : EventType.CHANGING));

		ruleStatement = ruleServiceHelper.saveSemiparsedRuleStatement(ruleSet, ruleStatement);

		// Send added event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RULE_STATEMENT,
				new RuleStatementEventData(ruleSet, ruleStatement, isNew ? EventType.ADDED : EventType.CHANGED));

		return ruleStatement;
	}

	@Override
	public RuleSet findRuleSetByName(final String rulesetName, final Vocabulary vocabulary) {
		return ruleSetDao.findByName(vocabulary, rulesetName);
	}

	@Override
	public RuleSet findRuleSetByResourceId(String resourceId) {
		return ruleSetDao.findById(resourceId);
	}

	@Override
	public RuleStatement findLatestRuleStatementByResourceId(String resourceId) {
		return ruleStatementDao.findById(resourceId);
	}

	public SimpleStatement findSimpleStatement(String resourceId) {
		return simpleStatementDao.findById(resourceId);
	}

	@Override
	public List<SimpleStatement> findStatements(Term term) {
		return simpleStatementDao.find(term);
	}

	@Override
	public List<SimpleStatement> findStatements(BinaryFactTypeForm bftf) {
		return simpleStatementDao.find(bftf);
	}

	@Override
	public List<RuleStatement> findRuleStatements(SimpleStatement ss) {
		return ruleStatementDao.find(ss);
	}

	@Override
	public List<RuleStatement> findRuleStatements(BinaryFactTypeForm bftf) {
		return ruleStatementDao.find(bftf);
	}

	@Override
	public List<RuleStatement> findRuleStatements(Term term) {
		return ruleStatementDao.find(term);
	}

	@Override
	public List<RuleSet> findRuleSets(RuleStatement rs) {
		return ruleSetDao.find(rs);
	}

	@Override
	public List<SimpleStatement> findAllStatements(Term term) {
		return simpleStatementDao.find(term);
	}

	@Override
	public List<SimpleStatement> findAllStatements(BinaryFactTypeForm bftf) {
		return simpleStatementDao.find(bftf);
	}

	@Override
	public List<RuleStatement> findAllRuleStatements(SimpleStatement ss) {
		return ruleStatementDao.find(ss);
	}

	@Override
	public List<RuleStatement> findAllRuleStatements(BinaryFactTypeForm bftf) {
		return ruleStatementDao.find(bftf);
	}

	@Override
	public List<RuleStatement> findAllRuleStatements(Term term) {
		return ruleStatementDao.find(term);
	}

	@Override
	public List<RuleSet> findAllRuleSets(RuleStatement rs) {
		return ruleSetDao.find(rs);
	}

	@Override
	public MultiValueMap findReferences(BinaryFactTypeForm bftf) {
		List<SimpleStatement> statements = findStatements(bftf);
		return findReferences(statements);
	}

	@Override
	public MultiValueMap findReferences(List<SimpleStatement> statements) {
		MultiValueMap references = new MultiValueMap();
		for (SimpleStatement statement : statements) {
			List<RuleStatement> ruleStatements = findRuleStatements(statement);
			for (RuleStatement rs : ruleStatements) {
				List<RuleSet> ruleSets = findRuleSets(rs);

				for (RuleSet ruleSet : ruleSets) {
					MultiValueMap map = null;
					Collection maps = references.getCollection(ruleSet);
					if (maps != null) {
						for (Object mapKey : maps) {
							if (((MultiValueMap) mapKey).keySet().contains(rs)) {
								map = (MultiValueMap) mapKey;
								break;
							}
						}
					}

					if (map == null) {
						map = new MultiValueMap();
						references.put(ruleSet, map);
					}

					(map).put(rs, statement);
				}
			}
		}

		return references;
	}

	@Override
	public RuleSet getRuleSetWithError(String resourceId) {
		RuleSet ruleSet = findRuleSetByResourceId(resourceId);
		if (ruleSet == null) {
			String message = "RuleSet with resource id '" + resourceId + "' not found.";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.RULESET_NOT_FOUND, resourceId);
		}

		return ruleSet;
	}

	@Override
	public RuleStatement getRuleStatementWithError(String resourceId) {
		RuleStatement rs = findLatestRuleStatementByResourceId(resourceId);
		if (rs == null) {
			String message = "RuleStatement with resource id '" + resourceId + "' not found.";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.RULE_STATEMENT_NOT_FOUND, resourceId);
		}

		return rs;
	}

	@Override
	public SimpleStatement getSimpleStatementWithError(String resourceId) {
		SimpleStatement rs = findSimpleStatement(resourceId);
		if (rs == null) {
			String message = "SimpleStatement with resource id '" + resourceId + "' not found.";
			log.error(message);
			throw new EntityNotFoundException(DGCErrorCodes.SIMPLE_STATEMENT_NOT_FOUND, resourceId);
		}

		return rs;
	}

	@Override
	public Set<RuleStatement> createRuleStatement(RuleSet ruleSet, Term head, String role, String corole, Term tail,
			String type, int frequency, boolean isUniquenessConstraint) {

		List<BinaryFactTypeForm> bftfs = binaryFactTypeFormDao.find(head, role, corole, tail);
		ReadingDirection rd = null;
		if (bftfs.size() > 0) {
			BinaryFactTypeForm bftf = bftfs.iterator().next();
			if (bftf.getHeadTerm().equals(head)) {
				rd = bftf.getLeftPlaceHolder();
			} else {
				rd = bftf.getRightPlaceHolder();
			}
		} else {
			BinaryFactTypeForm bftf = createBftf(ruleSet.getVocabulary(), head, role, corole, tail);
			rd = bftf.getLeftPlaceHolder();
		}

		return createRuleStatement(ruleSet, rd, type, frequency, isUniquenessConstraint);
	}

	@Override
	public Set<RuleStatement> createRuleStatement(RuleSet ruleSet, ReadingDirection placeHolder, String type,
			int frequency, boolean isUniquenessConstraint) {

		// If the BFTF is not persisted then it can be derived. Hence first create the BFTF.
		BinaryFactTypeForm temp = placeHolder.getBinaryFactTypeForm();
		if (!temp.isPersisted()) {
			BinaryFactTypeForm bftf = createBftf(ruleSet.getVocabulary(), temp.getHeadTerm(), temp.getRole(),
					temp.getCoRole(), temp.getTailTerm());

			if (placeHolder.getIsLeft()) {
				placeHolder = bftf.getLeftPlaceHolder();
			} else {
				placeHolder = bftf.getRightPlaceHolder();
			}
		}

		SimpleStatement simpleStatement = ruleFactory.makeSimpleStatement(ruleSet.getVocabulary());
		simpleStatement.addReadingDirection(placeHolder);

		Set<RuleStatement> addedRuleStatements = new HashSet<RuleStatement>();
		ConstraintType constraintType = ConstraintType.valueOf(type);
		if (isUniquenessConstraint) {
			if (constraintType.equals(ConstraintType.EXACTLY)) {
				SemiparsedRuleStatement semiparsedRuleStatement = ruleFactory.makeSemiparsedRuleStatement(ruleSet
						.getVocabulary());
				StringBuilder sb = new StringBuilder();
				sb.append(placeHolder.getHeadTerm().getSignifier()).append(" ").append(placeHolder.getRole())
						.append(" exactly one unique ").append(placeHolder.getTailTerm().getSignifier());
				semiparsedRuleStatement.setUnparsed(sb.toString());
				semiparsedRuleStatement.addSimpleStatement(simpleStatement);
				semiparsedRuleStatement = saveSemiparsedRuleStatement(ruleSet, semiparsedRuleStatement);
				addedRuleStatements.add(semiparsedRuleStatement);
			}
		} else {
			FrequencyRuleStatement freqRuleStatement = ruleFactory.makeFrequencyRuleStatement(ruleSet.getVocabulary());
			if (constraintType.equals(ConstraintType.EXACTLY)) {
				freqRuleStatement.setMax(frequency);
				freqRuleStatement.setMin(frequency);
			} else if (constraintType.equals(ConstraintType.ATLEAST)) {
				freqRuleStatement.setMin(frequency);
			} else {
				freqRuleStatement.setMax(frequency);
			}

			freqRuleStatement.addSimpleStatement(simpleStatement);
			freqRuleStatement = saveFrequencyRuleStatement(ruleSet, freqRuleStatement);
			addedRuleStatements.add(freqRuleStatement);
		}

		return addedRuleStatements;
	}

	private void removeInternal(Term term) {
		// Remove all versions referring to it.
		List<RuleStatement> ruleStatements = ruleStatementDao.find(term);
		for (RuleStatement rs : ruleStatements) {
			for (SimpleStatement ss : rs.getSimpleStatements()) {
				removeSimpleStatement(rs, ss);
			}

			// Remove the rule statement if needed
			if (rs.getSimpleStatements().size() == 0) {

				List<RuleSet> ruleSets = ruleSetDao.find(rs);
				for (RuleSet ruleSet : ruleSets) {
					removeRuleStatement(ruleSet, rs);
				}
			}
		}
	}

	private void removeInternal(BinaryFactTypeForm bftf) {
		// Find all references.
		MultiValueMap references = findReferences(bftf);

		for (Object ruleSetKey : references.keySet()) {
			RuleSet ruleSet = (RuleSet) ruleSetKey;

			Collection maps = references.getCollection(ruleSetKey);
			for (Object ruleStatementMap : maps) {
				MultiValueMap map = (MultiValueMap) ruleStatementMap;
				for (Object ruleStatementKey : map.keySet()) {
					RuleStatement ruleStatement = (RuleStatement) ruleStatementKey;
					// if (ruleStatement.getSimpleStatements().size() == 1) {
					Collection statements = map.getCollection(ruleStatementKey);
					for (Object value : statements) {
						SimpleStatement ss = (SimpleStatement) value;
						removeSimpleStatement(ruleStatement, ss);
					}

					removeRuleStatement(ruleSet, ruleStatement);
				}
			}

			// Persist the ruleset changes.
			ruleSetDao.save(ruleSet);
		}

		// Remove all versions referring to it.
		List<RuleStatement> ruleStatements = ruleStatementDao.find(bftf);
		for (RuleStatement rs : ruleStatements) {
			for (SimpleStatement ss : rs.getSimpleStatements()) {
				removeSimpleStatement(rs, ss);
			}

			// Remove the rule statement if needed
			if (rs.getSimpleStatements().size() == 0) {

				List<RuleSet> ruleSets = ruleSetDao.find(rs);
				for (RuleSet ruleSet : ruleSets) {
					removeRuleStatement(ruleSet, rs);
				}

				getCurrentSession().delete(rs);
			}
		}

		// Forcefully remove the readingDirections in case they are present because of versioning.
		List<ReadingDirection> readingDirections = simpleStatementDao.findReadingDirections(bftf);
		for (ReadingDirection ph : readingDirections) {
			getCurrentSession().delete(ph);
		}

		getCurrentSession().flush();
	}

	private enum ConstraintType {
		EXACTLY, ATMOST, ATLEAST
	}

	private BinaryFactTypeForm createBftf(Vocabulary vocabulary, Term head, String role, String corole, Term tail) {
		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(vocabulary, head, role, corole, tail);
		BinaryFactTypeFormEventData eventData = new BinaryFactTypeFormEventData(bftf, EventType.ADDED);
		eventData.setIsDerived(true);
		eventData.setDerivedForTerm(head);
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.SERVICE_BFTF, eventData);
		return binaryFactTypeFormDao.findById(bftf.getId());
	}

	/***************************
	 * All remove functionality
	 ***************************/

	@Override
	public void removeRuleSet(RuleSet ruleSet) {

		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RULESET,
				new RulesetEventData(ruleSet, EventType.REMOVING));

		ruleServiceHelper.delete(ruleSet);

		getCurrentSession().flush();

		// Send removed event.
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RULESET,
				new RulesetEventData(ruleSet, EventType.REMOVED));
	}

	@Override
	public void removeRuleStatement(RuleSet ruleSet, RuleStatement ruleStatement) {
		// Send removing event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RULE_STATEMENT,
				new RuleStatementEventData(ruleSet, ruleStatement, EventType.REMOVING));

		ruleServiceHelper.deleteRuleStatement(ruleSet, ruleStatement);

		// Send removed event
		ObservationManager.getInstance().notifyEvent(GlossaryEventCategory.RULE_STATEMENT,
				new RuleStatementEventData(ruleSet, ruleStatement, EventType.REMOVED));
	}

	@Override
	public void removeSimpleStatement(final RuleStatement ruleStatement, final SimpleStatement ss) {

		ruleStatement.getSimpleStatements().remove(ss);
		ruleStatementDao.save(ruleStatement);
		ruleServiceHelper.deleteSimpleStatement(ss);
	}

	@Override
	public void removeReferences(BinaryFactTypeForm bftf) {
		removeInternal(bftf);
	}

	@Override
	public void removeReferences(Term term) {
		removeInternal(term);
		getCurrentSession().flush();
	}
}
