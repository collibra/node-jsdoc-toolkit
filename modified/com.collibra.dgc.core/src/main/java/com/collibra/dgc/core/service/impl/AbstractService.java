package com.collibra.dgc.core.service.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;

/**
 * Abstract helper class for services
 * 
 * @author dtrog
 * 
 */
public abstract class AbstractService {

	@Autowired
	protected SessionFactory sessionFactory;

	protected String getCurrentUser() {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.getPrincipal() == null) {
			return Constants.GUEST_USER;
		}
		return currentUser.getPrincipal().toString();
	}

	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
}
