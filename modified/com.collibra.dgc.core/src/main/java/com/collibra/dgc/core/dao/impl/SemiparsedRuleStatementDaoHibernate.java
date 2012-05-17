package com.collibra.dgc.core.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.SemiparsedRuleStatementDao;
import com.collibra.dgc.core.model.rules.SemiparsedRuleStatement;
import com.collibra.dgc.core.model.rules.impl.SemiparsedRuleStatementImpl;

@Service
public class SemiparsedRuleStatementDaoHibernate extends
		AbstractDaoHibernate<SemiparsedRuleStatement, SemiparsedRuleStatementImpl> implements
		SemiparsedRuleStatementDao {

	@Autowired
	public SemiparsedRuleStatementDaoHibernate(SessionFactory sessionFactory) {
		super(SemiparsedRuleStatement.class, SemiparsedRuleStatementImpl.class, sessionFactory);
	}

	@Override
	public SemiparsedRuleStatement save(SemiparsedRuleStatement newSemiparsedRuleStatement) {
		return super.save(newSemiparsedRuleStatement);
	}

}
