package com.collibra.dgc.core.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.FrequencyRuleStatementDao;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.dao.RuleDao;
import com.collibra.dgc.core.dao.RuleSetDao;
import com.collibra.dgc.core.dao.RuleStatementDao;
import com.collibra.dgc.core.dao.SemiparsedRuleStatementDao;
import com.collibra.dgc.core.dao.SimplePropositionDao;
import com.collibra.dgc.core.dao.SimpleRuleStatementDao;
import com.collibra.dgc.core.dao.SimpleStatementDao;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;
import com.collibra.dgc.core.model.rules.FrequencyRuleStatement;
import com.collibra.dgc.core.model.rules.Rule;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.RuleStatement;
import com.collibra.dgc.core.model.rules.SemiparsedRuleStatement;
import com.collibra.dgc.core.model.rules.SimpleRuleStatement;
import com.collibra.dgc.core.model.rules.impl.RuleImpl;

/**
 * Helper class for {@link RulesServiceImpl} as the DAOs need to be created for a specific {@link Session}.
 * @author amarnath
 * 
 */
@Service
class RuleServiceHelper extends AbstractService {
	@Autowired
	private SimpleRuleStatementDao simpleRuleStatementDao;
	@Autowired
	private SemiparsedRuleStatementDao semiparsedRuleStatementDao;
	@Autowired
	private FrequencyRuleStatementDao frequencyRuleStatementDao;
	@Autowired
	private SimplePropositionDao simplePropositionDao;
	@Autowired
	private SimpleStatementDao simpleStatementDao;
	@Autowired
	private RuleDao ruleDao;
	@Autowired
	private RuleSetDao ruleSetDao;
	@Autowired
	private ObjectTypeDao objectTypeDao;
	@Autowired
	private RuleStatementDao ruleStatementDao;
	@Autowired
	private SessionFactory sessionFactory;

	public RuleSet saveRuleSet(final RuleSet ruleSet) {
		// Save all the rule statements.
		for (RuleStatement st : new HashSet<RuleStatement>(ruleSet.getRuleStatements())) {
			if (st instanceof SimpleRuleStatement) {
				saveSimpleRuleStatement(ruleSet, (SimpleRuleStatement) st);
			} else if (st instanceof SemiparsedRuleStatement) {
				saveSemiparsedRuleStatement(ruleSet, (SemiparsedRuleStatement) st);
			} else if (st instanceof FrequencyRuleStatement) {
				saveFrequencyRuleStatement(ruleSet, (FrequencyRuleStatement) st);
			}
		}

		ruleSetDao.save(ruleSet);

		return ruleSet;
	}

	public SimpleRuleStatement saveSimpleRuleStatement(RuleSet ruleSet, SimpleRuleStatement ruleStatement) {
		ruleSet.getRuleStatements().add(ruleStatement);
		simpleRuleStatementDao.save(ruleStatement);
		saveSimpleStatements(ruleStatement, ruleStatement.getSimpleStatements());
		saveRule(ruleStatement.getRule());
		ruleSetDao.save(ruleSet);
		return ruleStatement;
	}

	public FrequencyRuleStatement saveFrequencyRuleStatement(RuleSet ruleSet, FrequencyRuleStatement ruleStatement) {
		ruleSet.getRuleStatements().add(ruleStatement);
		frequencyRuleStatementDao.save(ruleStatement);
		saveSimpleStatements(ruleStatement, ruleStatement.getSimpleStatements());
		saveRule(ruleStatement.getRule());
		ruleSetDao.save(ruleSet);
		return ruleStatement;
	}

	public SemiparsedRuleStatement saveSemiparsedRuleStatement(RuleSet ruleSet, SemiparsedRuleStatement ruleStatement) {
		ruleSet.getRuleStatements().add(ruleStatement);
		semiparsedRuleStatementDao.save(ruleStatement);
		saveSimpleStatements(ruleStatement, ruleStatement.getSimpleStatements());
		saveRule(ruleStatement.getRule());
		ruleSetDao.save(ruleSet);
		return ruleStatement;
	}

	private final void saveSimpleStatements(final RuleStatement rs, final Set<SimpleStatement> simpleStatements) {
		for (SimpleStatement st : simpleStatements) {
			if (st.getSimpleProposition() != null && !st.getSimpleProposition().isPersisted()) {
				simplePropositionDao.save(st.getSimpleProposition());
			}

			st = simpleStatementDao.save(st);
		}
	}

	private final void saveRule(final Rule rule) {
		if (rule.getGlossaryConstraintType() != null) {
			if (Rule.MANDATORY.equals(rule.getGlossaryConstraintType())) {
				((RuleImpl) rule).setConstraintType(objectTypeDao.getMetaMandatoryConstraintType());
			} else if (Rule.UNIQUENESS.equals(rule.getGlossaryConstraintType())) {
				((RuleImpl) rule).setConstraintType(objectTypeDao.getMetaUniquenessConstraintType());

			} else {
				((RuleImpl) rule).setConstraintType(objectTypeDao.getMetaSemiparsedConstraintType());

			}
		}

		ruleDao.save(rule);
	}

	public RuleSet findRuleSetsByResourceId(String resourceId) {
		return ruleSetDao.findById(resourceId);
	}

	public void delete(final RuleSet ruleSet) {
		final Set<RuleStatement> statements = new HashSet<RuleStatement>(ruleSet.getRuleStatements());
		ruleSet.getRuleStatements().clear();
		ruleSetDao.save(ruleSet);
		for (RuleStatement statement : statements) {
			deleteRuleStatement(statement);
		}

		ruleSetDao.delete(ruleSet);
	}

	public void deleteRuleStatement(RuleSet ruleSet, RuleStatement ruleStatement) {
		ruleSet.getRuleStatements().remove(ruleStatement);
		ruleSetDao.save(ruleSet);
		deleteRuleStatement(ruleStatement);
	}

	private void deleteRuleStatement(RuleStatement ruleStatement) {
		for (SimpleStatement ss : ruleStatement.getSimpleStatements()) {
			deleteSimpleStatement(ss);
		}

		ruleStatementDao.delete(ruleStatement);
	}

	public void deleteSimpleStatement(SimpleStatement simpleStatement) {
		for (ReadingDirection ph : simpleStatement.getReadingDirections()) {
			sessionFactory.getCurrentSession().delete(ph);
		}
		simpleStatementDao.delete(simpleStatement);
	}
}
