package com.collibra.dgc.core.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.dao.CommunityDao;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.impl.CommunityImpl;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * {@link CommunityDao} implementation for hibernate.
 * @author amarnath
 * 
 */
@Service
public class CommunityDaoHibernate extends AbstractDaoHibernate<Community, CommunityImpl> implements CommunityDao {

	@Autowired
	public CommunityDaoHibernate(SessionFactory sessionFactory) {
		super(Community.class, CommunityImpl.class, sessionFactory);
	}

	@Override
	public Community findByURI(String uri) {
		return uniqueResult(criteria().add(Restrictions.eq("uri", uri)));
	}

	@Override
	public Community find(String resourceId) {
		return findById(resourceId);
	}

	@Override
	public Community findByName(String name) {
		return uniqueResult(criteria().add(Restrictions.eq("name", name)));
	}

	@Override
	public List<Community> findAll() {
		return findCommunities(false, false);
	}

	@Override
	public List<Community> searchByName(String expression, int offset, int number) {
		String searchExpression = expression;

		if (!searchExpression.startsWith("%")) {
			searchExpression = "%" + searchExpression;
		}

		if (!searchExpression.endsWith("%")) {
			searchExpression = searchExpression + "%";
		}

		Criteria crit = criteria().add(Restrictions.ilike("name", searchExpression)).addOrder(Order.desc("id"));

		if (offset > 0) {
			crit.setFirstResult(offset);
		}

		if (number > 0) {
			crit.setMaxResults(number);
		}

		return list(crit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.dao.CommunityDao#findAll(com.collibra.dgc.core.model.representation.Vocabulary)
	 */
	@Override
	public List<Community> findAll(Vocabulary vocabulary) {
		Criteria query = criteria();
		Criteria subquery = query.createCriteria("vocabularies", "vocabulary");
		subquery.add(Expression.eq("vocabulary", vocabulary));
		query.setFlushMode(FlushMode.MANUAL);
		subquery.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.dao.CommunityDao#findAllWithoutParentCommuntiy()
	 */
	@Override
	public List<Community> findTopLevelCommmunities() {
		String query = "select spc from CommunityImpl spc where spc.parentCommunity is null order by spc.name";
		return getSession().createQuery(query).list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.dao.CommunityDao#findCommunitiesWithoutVocabularies()
	 */
	@Override
	public List<Community> findCommunitiesWithoutVocabularies() {
		Criteria query = criteria();
		Criteria subquery = query.createCriteria("vocabularies", "vocabulary");
		query.setFlushMode(FlushMode.MANUAL);
		subquery.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.dao.CommunityDao#findAll(boolean, boolean)
	 */
	@Override
	public List<Community> findCommunities(boolean excludeSBVR, boolean excludeMeta) {
		Criteria criteria = criteria().addOrder(Order.asc("name"));
		if (excludeSBVR) {
			criteria.add(Restrictions.eq("SBVR", false));
		}
		if (excludeMeta) {
			criteria.add(Restrictions.eq("meta", false));
		}
		return list(criteria);
	}

	public List<Community> findSBVRCommunities() {
		Criteria criteria = criteria().addOrder(Order.asc("name"));
		criteria.add(Restrictions.eq("SBVR", true));
		return list(criteria);
	}

	public List<Community> findMetaCommunities() {
		Criteria criteria = criteria().addOrder(Order.asc("name"));
		criteria.add(Restrictions.eq("meta", true));
		return list(criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.dao.impl.AbstractDaoHibernate#save(com.collibra.dgc.core.model.Resource)
	 */
	@Override
	public Community save(Community object) {
		if (object instanceof CommunityImpl) {
			// Here we calculate if this community is meta and/or SBVR.
			final CommunityImpl c = (CommunityImpl) object;
			c.setMeta(Constants.METAMODEL_COMMUNITY_URI.equals(c.getTopLevelCommunity().getUri()));
			c.setSBVR(Constants.SBVR_ENGLISH_COMMUNITY_URI.equals(c.getUri()));
		}
		return super.save(object);
	}
}
