package com.collibra.dgc.core.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.RuleSetDao;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.RuleStatement;
import com.collibra.dgc.core.model.rules.impl.RuleSetImpl;

@Service
public class RuleSetDaoHibernate extends AbstractDaoHibernate<RuleSet, RuleSetImpl> implements RuleSetDao {

	@Autowired
	public RuleSetDaoHibernate(SessionFactory sessionFactory) {
		super(RuleSet.class, RuleSetImpl.class, sessionFactory);
	}

	@Override
	public RuleSet findByName(Vocabulary vocabulary, String name) {
		return uniqueResult(criteria().add(Restrictions.eq("name", name))
				.add(Restrictions.eq("vocabulary", vocabulary)).addOrder(Order.desc("id")).setMaxResults(1));
	}

	@Override
	public List<RuleSet> find(RuleStatement rs) {
		Criteria query = getSession().createCriteria(RuleSetImpl.class);
		Criteria subquery = query.createCriteria("ruleStatements", "rs");
		subquery.add(Expression.eq("rs.id", rs.getId()));
		query.setFlushMode(FlushMode.MANUAL);
		subquery.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	@Override
	public List<RuleSet> findAll(Vocabulary vocabulary) {
		Query query = getSession().createQuery("from RuleSetImpl ruleSet where ruleSet.vocabulary = :vocabulary");
		query.setParameter("vocabulary", vocabulary);
		return list(query);
	}

	@Override
	protected List<RuleSet> all() {
		return list(criteria().setProjection(Projections.max("id")));
	}

	@Override
	public RuleSet save(RuleSet newRuleSet) {
		return super.save(newRuleSet);
	}

	// @Override
	// public boolean isPersisted(final RuleSet ruleSet) {
	//
	// Query query = getSession().createQuery("SELECT ruleSet.id from RuleSetImpl ruleSet where ruleSet = :rs");
	// query.setParameter("rs", ruleSet.getId());
	// String result = (String) query.uniqueResult();
	//
	// return result != null;
	// }
}
