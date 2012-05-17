package com.collibra.dgc.core.dao.impl;

import java.util.Collection;
import java.util.Map.Entry;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.UserDao;
import com.collibra.dgc.core.dao.filter.impl.FilterVisitorHibernate;
import com.collibra.dgc.core.dto.filters.ResourceFilter;
import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.model.user.impl.UserImpl;

@Service
public class UserDaoHibernate extends HibernateDaoSupport implements InitializingBean, UserDao {

	@Autowired
	public UserDaoHibernate(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public User findByUserName(String userName) {
		return (User) getSession().createCriteria(UserImpl.class).add(Restrictions.eq("userName", userName))
				.setFlushMode(FlushMode.MANUAL).uniqueResult();
	}

	@Override
	public Class getOwnerType(String userName) {
		Class c = null;

		final Query q = getSession().createQuery("SELECT count(user) FROM UserImpl user WHERE user.id = ?");
		q.setParameter(0, userName);

		final Query q2 = getSession().createQuery("SELECT count(*) FROM GroupImpl g WHERE g.id = ?");
		q2.setParameter(0, userName);

		if (((Number) q.uniqueResult()).longValue() > 0) {
			c = User.class;
		} else if (((Number) q2.uniqueResult()).longValue() > 0) {
			c = Group.class;
		}
		return c;
	}

	@Override
	public User save(User user) {
		getSession().persist(user);
		return user;
	}

	@Override
	public void remove(User user) {
		getSession().delete(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.dao.UserDao#get(long)
	 */
	@Override
	public User get(String id) {
		return (User) getSession().get(UserImpl.class, id);
	}

	@Override
	public Collection<UserData> findUsers(ResourceFilter filter, int offset, int max) {
		StringBuffer query = new StringBuffer("FROM UserImpl as u");
		final FilterVisitorHibernate visitor = new FilterVisitorHibernate("u", query);
		filter.accept(visitor);

		final Query q = getSession().createQuery(query.toString());

		for (Entry<String, Object> paramEntry : visitor.getHqlParameters().entrySet()) {
			q.setParameter(paramEntry.getKey(), paramEntry.getValue());
		}

		if (max > 0) {
			q.setMaxResults(max);
		}
		if (offset > 0) {
			q.setFirstResult(offset);
		}

		return q.list();
	}
}
