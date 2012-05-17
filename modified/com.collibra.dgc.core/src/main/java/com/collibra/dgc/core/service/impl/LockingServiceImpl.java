package com.collibra.dgc.core.service.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.VocabularyDao;
import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.service.LockingService;

/**
 * @author fvdmaele
 * 
 *         Service that facilitates locking of resources
 * 
 */
@Service
public class LockingServiceImpl extends AbstractService implements LockingService {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private VocabularyDao vocabularyDao;

	@Override
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.LockingService#lockResource(java.lang.Class, java.lang.String)
	 */
	public void lockResource(Class<? extends Resource> clazz, String resourceId) throws Exception {
		vocabularyDao.lockResource(clazz, resourceId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.LockingService#lockAndUpdate(java.lang.Class, java.lang.String)
	 */
	public Resource lock(Class<? extends Resource> clazz, String resourceId) throws Exception {
		lockResource(clazz, resourceId);
		return vocabularyDao.findById(clazz, resourceId);
	}
}
