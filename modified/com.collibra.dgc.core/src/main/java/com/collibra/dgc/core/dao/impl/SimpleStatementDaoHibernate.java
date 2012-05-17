package com.collibra.dgc.core.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.SimpleStatementDao;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;
import com.collibra.dgc.core.model.representation.impl.SimpleStatementImpl;

@Service
public class SimpleStatementDaoHibernate extends AbstractDaoHibernate<SimpleStatement, SimpleStatementImpl> implements
		SimpleStatementDao {

	@Autowired
	public SimpleStatementDaoHibernate(SessionFactory sessionFactory) {
		super(SimpleStatement.class, SimpleStatementImpl.class, sessionFactory);
	}

	@Override
	public SimpleStatement save(SimpleStatement object) {
		return super.save(object);
	}

	public List<SimpleStatement> find(BinaryFactTypeForm bftf) {
		Criteria query = getSession().createCriteria(SimpleStatementImpl.class);
		Criteria subquery = query.createCriteria("readingDirections", "ph");
		subquery.add(Expression.eq("ph.binaryFactTypeForm", bftf));
		query.setFlushMode(FlushMode.MANUAL);
		subquery.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	public List<SimpleStatement> find(Term term) {
		Criteria query = getSession().createCriteria(SimpleStatementImpl.class);
		Criteria subquery = query.createCriteria("readingDirections", "ph")
				.createCriteria("binaryFactTypeForm", "bftf");
		subquery.add(Restrictions.or(Expression.eq("bftf.headTerm", term), Expression.eq("bftf.tailTerm", term)));
		query.setFlushMode(FlushMode.MANUAL);
		subquery.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	public List<ReadingDirection> findReadingDirections(BinaryFactTypeForm bftf) {
		Criteria query = getSession().createCriteria(ReadingDirection.class);
		// Criteria subquery = query.createCriteria("readingDirections", "ph").createCriteria("binaryFactTypeForm",
		// "bftf");
		query.add(Expression.eq("binaryFactTypeForm", bftf));
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}
}
