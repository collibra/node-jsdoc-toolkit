package com.collibra.dgc.core.model.user.impl;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.user.Group;
import com.collibra.dgc.core.model.user.GroupMembership;

@Entity
@Audited
@Table(name = "GROUPS")
public class GroupImpl extends ResourceImpl implements Group {
	private Collection<GroupMembership> memberships;
	private String groupName;

	public GroupImpl() {
	}

	public GroupImpl(String groupName) {
		this.groupName = groupName;
	}

	@Override
	@OneToMany(targetEntity = GroupMembershipImpl.class, mappedBy = "group")
	public Collection<GroupMembership> getGroupMemberships() {
		return memberships;
	}

	@Override
	@Column(name = "GROUP_NAME")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupMemberships(Collection<GroupMembership> memberships) {
		this.memberships = memberships;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	protected void initializeRelations() {
		super.initializeRelations();
		memberships = new ArrayList<GroupMembership>();
	}
}
