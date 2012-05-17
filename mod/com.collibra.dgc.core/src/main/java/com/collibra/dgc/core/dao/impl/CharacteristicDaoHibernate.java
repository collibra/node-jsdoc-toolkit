package com.collibra.dgc.core.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.CharacteristicDao;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.meaning.facttype.impl.CharacteristicImpl;

@Service
public class CharacteristicDaoHibernate extends AbstractDaoHibernate<Characteristic, CharacteristicImpl> implements
		CharacteristicDao {

	@Autowired
	private ObjectTypeDao objectTypeDao;

	@Autowired
	public CharacteristicDaoHibernate(SessionFactory sessionFactory) {
		super(Characteristic.class, CharacteristicImpl.class, sessionFactory);
	}

	@Override
	public Characteristic save(Characteristic object) {
		if (object.getType() == null) {
			object.setType(objectTypeDao.getMetaCharacteristic());
		}

		if (object.getGeneralConcept() == null) {
			object.setGeneralConcept(objectTypeDao.getMetaThing());
		}
		return super.save(object);
	}
}
