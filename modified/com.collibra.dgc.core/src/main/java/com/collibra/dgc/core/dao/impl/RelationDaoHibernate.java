/**
 * 
 */
package com.collibra.dgc.core.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.RelationDao;
import com.collibra.dgc.core.model.relation.Relation;
import com.collibra.dgc.core.model.relation.impl.RelationImpl;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;

@Service
public class RelationDaoHibernate extends AbstractDaoHibernate<Relation, RelationImpl> implements RelationDao {

	@Autowired
	public RelationDaoHibernate(SessionFactory sessionFactory) {
		super(Relation.class, RelationImpl.class, sessionFactory);
	}

	@Override
	public Relation save(Relation relation) {
		return super.save(relation);
	}

	@Override
	public List<Relation> findByType(final BinaryFactTypeForm type) {
		return list(criteria().add(Restrictions.eq("type", type)));
	}

	@Override
	public List<Relation> findBySource(final Term source) {
		return list(criteria().add(Restrictions.eq("source", source)));
	}

	@Override
	public List<Relation> findByTarget(final Term target) {
		return list(criteria().add(Restrictions.eq("target", target)));
	}

	@Override
	public List<Relation> findBySourceAndType(final BinaryFactTypeForm type, final Term source) {
		return list(criteria().add(Restrictions.eq("type", type)).add(Restrictions.eq("source", source)));
	}

	@Override
	public List<Relation> findByTargetAndType(final BinaryFactTypeForm type, final Term target) {
		return list(criteria().add(Restrictions.eq("type", type)).add(Restrictions.eq("target", target)));
	}

	@Override
	public List<Relation> findBySourceAndTarget(final Term source, final Term target) {
		return list(criteria().add(Restrictions.eq("source", source)).add(Restrictions.eq("target", target)));
	}

	@Override
	public List<Relation> findBySourceAndTargetAndType(final Term source, final Term target,
			final BinaryFactTypeForm type) {
		return list(criteria().add(Restrictions.eq("source", source)).add(Restrictions.eq("target", target))
				.add(Restrictions.eq("type", type)));
	}
}
