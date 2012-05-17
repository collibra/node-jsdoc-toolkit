package com.collibra.dgc.core.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.SimplePropositionDao;
import com.collibra.dgc.core.model.meaning.SimpleProposition;
import com.collibra.dgc.core.model.meaning.impl.SimplePropositionImpl;

@Service
public class SimplePropositionDaoHibernate extends AbstractDaoHibernate<SimpleProposition, SimplePropositionImpl>
		implements SimplePropositionDao {

	@Autowired
	public SimplePropositionDaoHibernate(SessionFactory sessionFactory) {
		super(SimpleProposition.class, SimplePropositionImpl.class, sessionFactory);
	}

	// @Override
	// public boolean isPersisted(final SimpleProposition simpleProposition) {
	//
	// Query query = getSession()
	// .createQuery(
	// "SELECT simpleProposition.id from SimplePropositionImpl simpleProposition where simpleProposition = :sp");
	// query.setParameter("sp", simpleProposition);
	// String result = (String) query.uniqueResult();
	//
	// return result != null;
	// }
}
