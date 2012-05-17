package com.collibra.dgc.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.CategorizationTypeDao;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.impl.CategorizationTypeImpl;
import com.collibra.dgc.core.model.meaning.Concept;

/**
 * 
 * @author amarnath
 * 
 */
@Service
public class CategorizationTypeDaoHibernate extends AbstractDaoHibernate<CategorizationType, CategorizationTypeImpl>
		implements CategorizationTypeDao {

	@Autowired
	private ObjectTypeDao objectTypeDao;

	@Autowired
	public CategorizationTypeDaoHibernate(SessionFactory sessionFactory) {
		super(CategorizationType.class, CategorizationTypeImpl.class, sessionFactory);
	}

	@Override
	public CategorizationType save(CategorizationType object) {
		if (object.getType() == null) {
			object.setType(objectTypeDao.getMetaCategorizationType());
		}
		if (object.getGeneralConcept() == null) {
			object.setGeneralConcept(objectTypeDao.getMetaThing());
		}

		return super.save(object);
	}

	@Override
	public List<CategorizationType> findForConcept(Concept concept) {

		String queryString = "from CategorizationTypeImpl ct where ct.isForConcept = :concept";
		Query query = getSession().createQuery(queryString);

		query.setParameter("concept", concept);

		return query.list();
	}
}
