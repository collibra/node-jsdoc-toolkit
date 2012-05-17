package com.collibra.dgc.core.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.RuleDao;
import com.collibra.dgc.core.model.rules.Rule;
import com.collibra.dgc.core.model.rules.impl.RuleImpl;

@Service
public class RuleDaoHibernate extends AbstractDaoHibernate<Rule, RuleImpl> implements RuleDao {

	@Autowired
	public RuleDaoHibernate(SessionFactory sessionFactory) {
		super(Rule.class, RuleImpl.class, sessionFactory);
	}

	@Override
	public Rule save(Rule object) {
		return super.save(object);
	}
}