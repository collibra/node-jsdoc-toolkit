package com.collibra.dgc.core.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.FrequencyRuleStatementDao;
import com.collibra.dgc.core.model.rules.FrequencyRuleStatement;
import com.collibra.dgc.core.model.rules.impl.FrequencyRuleStatementImpl;

@Service
public class FrequencyRuleStatementDaoHibernate extends
		AbstractDaoHibernate<FrequencyRuleStatement, FrequencyRuleStatementImpl> implements FrequencyRuleStatementDao {

	@Autowired
	public FrequencyRuleStatementDaoHibernate(SessionFactory sessionFactory) {
		super(FrequencyRuleStatement.class, FrequencyRuleStatementImpl.class, sessionFactory);
	}

	@Override
	public FrequencyRuleStatement save(FrequencyRuleStatement newObject) {
		return super.save(newObject);
	}

}
