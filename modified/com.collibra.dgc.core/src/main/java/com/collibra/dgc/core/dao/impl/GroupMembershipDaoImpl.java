package com.collibra.dgc.core.dao.impl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.collibra.dgc.core.dao.GroupMembershipDao;
import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.GroupMembership;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.impl.GroupMembershipImpl;

@Repository
public class GroupMembershipDaoImpl extends AbstractDaoHibernate<GroupMembership, GroupMembershipImpl> implements
		GroupMembershipDao {

	@Autowired
	public GroupMembershipDaoImpl(SessionFactory sessionFactory) {
		super(GroupMembership.class, GroupMembershipImpl.class, sessionFactory);
	}

	@Override
	public GroupMembership getByGroupAndUser(Group group, User user) {
		Query q = getSession().createQuery("from GroupMembershipImpl gm where gm.user = :user AND gm.group = :group");
		q.setParameter("user", user);
		q.setParameter("group", group);
		return (GroupMembership) q.uniqueResult();
	}

}
