package com.collibra.dgc.core.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.exception.LockAcquisitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.collibra.dgc.core.dao.AbstractDao;
import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.impl.ResourceImpl;

/**
 * An abstract DAO for hibernate persistence that groups common behaviour for all DAOs. Makes use of generics to avoid
 * typecasting by clients.
 * 
 * @author dtrog
 * 
 * @param <I> The interface of the Business Object the DAO is for.
 * @param <C> The implementation of the Business Object the DAO is for.
 */
public abstract class AbstractDaoHibernate<I extends Resource, C extends ResourceImpl> extends HibernateDaoSupport
		implements AbstractDao<I>, InitializingBean {

	private final Class<I> entityInterface;
	private final Class<C> entityClass;
	private static final Logger log = LoggerFactory.getLogger(AbstractDaoHibernate.class);

	public enum AttributeComparator {
		EQUALS {
			@Override
			SimpleExpression getRestriction(String property, String value) {
				return Restrictions.eq(property, value);
			}
		},
		CONTAINS {
			@Override
			SimpleExpression getRestriction(String property, String value) {
				return Restrictions.like(property, "%" + value + "%");
			}
		},
		SMALLER {
			@Override
			SimpleExpression getRestriction(String property, String value) {
				return Restrictions.lt(property, new Integer(value));
			}
		},
		LARGER {
			@Override
			SimpleExpression getRestriction(String property, String value) {
				return Restrictions.gt(property, new Integer(value));
			}
		};

		abstract SimpleExpression getRestriction(String property, String value);

	}

	// this will keep track of each saveNewVersion of a representation,
	// which will be used to set the correct resource ID later on.

	public AbstractDaoHibernate(Class<I> entityInterface, Class<C> entityClass, SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
		Assert.notNull(entityClass);

		this.entityInterface = entityInterface;
		this.entityClass = entityClass;
	}

	@Override
	public I findById(String id) {
		return get(id);
	}

	@Override
	public List<I> findAll() {
		return all();
	}

	@Override
	public I save(I object) {
		if (object instanceof Resource) {
			((Resource) object).updateLastModified();
			((Resource) object).saved();
		}
		getSession().saveOrUpdate(object);
		return object;
	}

	@Override
	public void delete(I object) {
		getSession().delete(object);
	}

	public Class<I> getEntityInterface() {
		return entityInterface;
	}

	public Class<C> getEntityClass() {
		return entityClass;
	}

	protected Criteria criteria() {
		return getSession().createCriteria(entityClass);
	}

	protected Query query(String hql) {
		return getSession().createQuery(hql);
	}

	protected List<I> all() {
		return list(criteria());
	}

	/* === BEGIN GENERICS SUPPRESSION WRAPPERS === */

	@SuppressWarnings("unchecked")
	protected List<I> list(Criteria criteria) {
		criteria.setFlushMode(FlushMode.MANUAL);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	protected List<I> list(Query query) {
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	protected I uniqueResult(Criteria criteria) {
		criteria.setFlushMode(FlushMode.MANUAL);
		return (I) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	protected I uniqueResult(Query query) {
		query.setFlushMode(FlushMode.MANUAL);
		return (I) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	protected I get(Serializable id) {
		try {
			return (I) getSession().get(entityClass, id);
		} catch (Exception e) {
			log.error("Error while retrieving '" + id + "' as '" + entityClass + "'.", e);
		}
		return null;
	}

	@Override
	public void flush() {
		getSession().flush();
	}

	@Override
	public void evict(I object) {
		getSession().evict(object);
	}

	@Override
	public void refresh(Object object) {
		getSession().refresh(object);
	}

	/* === BEGIN RESOURCE LOCKING IMPLEMENTATION === */

	/**
	 * This method will lock a resource based on its resource id That means that all versions of this resource will be
	 * locked for writing or updating
	 * 
	 * @param resource The resource to lock
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.dao.AbstractDao#lockResource(java.lang.Class, java.lang.String)
	 */
	@Override
	public void lockResource(Class clazz, String resourceId) throws Exception {
		final Session currentSession = getSession();
		try {
			// make lock query
			String queryString = "FROM " + clazz.getName() + " res WHERE res.id = ?";
			Query query = currentSession.createQuery(queryString);
			query.setParameter(0, resourceId);
			query.setFlushMode(FlushMode.MANUAL);
			query.setLockMode("res", LockMode.PESSIMISTIC_WRITE);

			log.debug("Acquiring Lock on " + clazz.getName() + ": " + resourceId + " - from transaction "
					+ currentSession.getTransaction().hashCode());
			query.list();
			log.debug("Locked " + clazz.getName() + ": " + resourceId + " - from transaction "
					+ currentSession.getTransaction().hashCode());
		} catch (LockAcquisitionException le) {
			log.error(" > Could not get lock for " + clazz.getName() + ": " + resourceId + " - from transaction "
					+ currentSession.getTransaction().hashCode());
			throw le;
		} catch (Exception e) {
			log.error(" > Could not get lock for " + clazz.getName() + ": " + resourceId + " - from transaction "
					+ currentSession.getTransaction().hashCode());
			throw e;
		}
	}

	@Override
	public Resource findById(Class clazz, String resourceId) {
		final String queryString = "FROM " + clazz.getSimpleName() + " res WHERE res.id = '" + resourceId + "'";
		final Query query = getSession().createQuery(queryString);
		query.setFlushMode(FlushMode.MANUAL);
		return (Resource) query.uniqueResult();
	}
}