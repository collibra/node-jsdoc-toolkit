package com.collibra.dgc.core.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.RuleStatementDao;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.rules.RuleStatement;
import com.collibra.dgc.core.model.rules.impl.RuleStatementImpl;

/**
 * 
 * @author amarnath
 * 
 */
@Service
public class RuleStatementDaoHibernate extends AbstractDaoHibernate<RuleStatement, RuleStatementImpl> implements
		RuleStatementDao {

	@Autowired
	public RuleStatementDaoHibernate(SessionFactory sessionFactory) {
		super(RuleStatement.class, RuleStatementImpl.class, sessionFactory);
	}

	public List<RuleStatement> find(SimpleStatement ss) {
		Criteria query = getSession().createCriteria(RuleStatementImpl.class);
		Criteria subquery = query.createCriteria("simpleStatements", "statement");
		subquery.add(Expression.eq("statement.id", ss.getId()));
		query.setFlushMode(FlushMode.MANUAL);
		subquery.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	public List<RuleStatement> find(BinaryFactTypeForm bftf) {
		Criteria query = getSession().createCriteria(RuleStatementImpl.class);
		Criteria subquery = query.createCriteria("simpleStatements", "statement").createCriteria("readingDirections",
				"ph");
		subquery.add(Expression.eq("ph.binaryFactTypeForm", bftf));
		query.setFlushMode(FlushMode.MANUAL);
		subquery.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	public List<RuleStatement> find(Term term) {
		Criteria query = getSession().createCriteria(RuleStatementImpl.class);
		Criteria subquery = query.createCriteria("simpleStatements", "statement")
				.createCriteria("readingDirections", "ph").createCriteria("binaryFactTypeForm", "bftf");
		subquery.add(Restrictions.or(Expression.eq("bftf.headTerm", term), Expression.eq("bftf.tailTerm", term)));
		query.setFlushMode(FlushMode.MANUAL);
		subquery.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	// @Override
	// public boolean isPersisted(final RuleStatement ruleStatement) {
	//
	// Query query = getSession().createQuery(
	// "SELECT ruleStatement.id from RuleStatementImpl ruleStatement where ruleStatement = :rs");
	// query.setParameter("rs", ruleStatement);
	// String result = (String) query.uniqueResult();
	//
	// return result != null;
	// }
}
