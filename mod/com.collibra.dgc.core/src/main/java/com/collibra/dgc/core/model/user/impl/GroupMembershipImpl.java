package com.collibra.dgc.core.model.user.impl;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.GroupMembership;
import com.collibra.dgc.core.model.user.User;

@Entity
@Audited
@Table(name = "GROUPMEMBERSHIPS", uniqueConstraints = { @UniqueConstraint(columnNames = { "GROUP_ID", "USER_ID" }) })
public class GroupMembershipImpl extends ResourceImpl implements GroupMembership, Resource {

	private Group group;
	private User user;

	@Override
	@ManyToOne(targetEntity = GroupImpl.class)
	@JoinColumn(name = "GROUP_ID")
	public Group getGroup() {
		return group;
	}

	@Override
	@ManyToOne(targetEntity = UserImpl.class)
	@JoinColumn(name = "USER_ID")
	public User getUser() {
		return user;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
