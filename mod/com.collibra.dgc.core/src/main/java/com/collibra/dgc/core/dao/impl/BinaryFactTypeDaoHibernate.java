package com.collibra.dgc.core.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.BinaryFactTypeDao;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.impl.BinaryFactTypeImpl;

@Service
public class BinaryFactTypeDaoHibernate extends AbstractDaoHibernate<BinaryFactType, BinaryFactTypeImpl> implements
		BinaryFactTypeDao {

	@Autowired
	private ObjectTypeDao objectTypeDao;

	@Autowired
	public BinaryFactTypeDaoHibernate(SessionFactory sessionFactory) {
		super(BinaryFactType.class, BinaryFactTypeImpl.class, sessionFactory);
	}

	@Override
	public BinaryFactType save(BinaryFactType object) {
		if (object.getType() == null) {
			object.setType(objectTypeDao.getMetaBinaryFactType());
		}

		if (object.getGeneralConcept() == null) {
			object.setGeneralConcept(objectTypeDao.getMetaThing());
		}

		return super.save(object);
	}

}
