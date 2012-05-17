package com.collibra.dgc.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.collibra.dgc.core.dao.GroupDao;
import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.impl.GroupImpl;

@Repository
public class GroupDaoImpl extends AbstractDaoHibernate<Group, GroupImpl> implements GroupDao {

	@Autowired
	public GroupDaoImpl(SessionFactory sessionFactory) {
		super(Group.class, GroupImpl.class, sessionFactory);
	}

	@Override
	public List<Group> getGroupsForUser(String userId) {
		String query = "select gm.group from GroupMembershipImpl gm where gm.user.id = :userId";
		Query q = getSession().createQuery(query);
		q.setParameter("userId", userId);
		return q.list();
	}

}
