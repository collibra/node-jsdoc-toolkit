package com.collibra.dgc.core.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.SimpleRuleStatementDao;
import com.collibra.dgc.core.model.rules.SimpleRuleStatement;
import com.collibra.dgc.core.model.rules.impl.SimpleRuleStatementImpl;

@Service
public class SimpleRuleStatementDaoHibernate extends AbstractDaoHibernate<SimpleRuleStatement, SimpleRuleStatementImpl>
		implements SimpleRuleStatementDao {

	@Autowired
	public SimpleRuleStatementDaoHibernate(SessionFactory sessionFactory) {
		super(SimpleRuleStatement.class, SimpleRuleStatementImpl.class, sessionFactory);
	}

	@Override
	public SimpleRuleStatement save(SimpleRuleStatement newSimpleRuleStatement) {
		return super.save(newSimpleRuleStatement);
	}

}
