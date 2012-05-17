/**
 * 
 */
package com.collibra.dgc.core.dao.configuration.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.configuration.AttributeAndRelationTypesConfigurationCategoryDao;
import com.collibra.dgc.core.model.configuration.AttributeAndRelationTypesConfigurationCategory;
import com.collibra.dgc.core.model.configuration.ConfigurationCategory;
import com.collibra.dgc.core.model.configuration.impl.AttributeAndRelationTypesConfigurationCategoryImpl;
import com.collibra.dgc.core.model.representation.Representation;

/**
 * @author fvdmaele
 * 
 */
@Service
public class AttributeAndRelationTypesConfigurationCategoryDaoHibernate extends HibernateDaoSupport implements
		AttributeAndRelationTypesConfigurationCategoryDao, InitializingBean {

	private static final Logger log = LoggerFactory
			.getLogger(AttributeAndRelationTypesConfigurationCategoryDaoHibernate.class);

	@Autowired
	public AttributeAndRelationTypesConfigurationCategoryDaoHibernate(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public AttributeAndRelationTypesConfigurationCategory getCategoryByName(String name) {
		return (AttributeAndRelationTypesConfigurationCategory) getSession()
				.createCriteria(AttributeAndRelationTypesConfigurationCategoryImpl.class)
				.add(Restrictions.eq("name", name)).uniqueResult();
	}

	@Override
	public List<AttributeAndRelationTypesConfigurationCategory> findByRepresentation(Representation rep) {
		return getSession().createCriteria(AttributeAndRelationTypesConfigurationCategoryImpl.class)
				.add(Restrictions.eq("representations", rep)).list();
	}

	@Override
	public AttributeAndRelationTypesConfigurationCategory save(AttributeAndRelationTypesConfigurationCategory category) {
		getSession().saveOrUpdate(category);
		category.saved();
		return category;
	}

	@Override
	public void remove(ConfigurationCategory category) {
		getSession().delete(category);
	}

	@Override
	public List<AttributeAndRelationTypesConfigurationCategory> findAll() {
		return getSession().createCriteria(AttributeAndRelationTypesConfigurationCategoryImpl.class)
				.addOrder(Order.asc("name")).list();
	}
}
