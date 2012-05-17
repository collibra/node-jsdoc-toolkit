package com.collibra.dgc.core.model.user.impl;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Target;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.Role;

/**
 * Implementation for {@link Member}.
 * 
 * @author amarnath
 * @author pmalarme
 * 
 */
@Entity
@Audited
// TODO unique constraint is to big for some databases.
@Table(name = "MEMBERS"/*
						 * , uniqueConstraints = { @UniqueConstraint(columnNames = { "RESOURCEID", "USER_NAME", "ROLE"
						 * }) }
						 */)
public class MemberImpl implements Member {
	private String ownerId;
	private String resourceId;
	private Role role;
	private Long membershipId;
	private boolean group;

	public MemberImpl() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Member for owner with id '" + ownerId + "' with role '" + role.getName() + "'"
				+ (resourceId == null ? "." : " and resource '" + resourceId.toString() + "'.");
	}

	public MemberImpl(String ownerId, Resource resource, Role role) {
		setOwnerId(ownerId);
		setRole(role);
		setResourceId(resource == null ? null : resource.getId());
	}

	public MemberImpl(String ownerId, Resource resource, Role role, boolean group) {
		setOwnerId(ownerId);
		setRole(role);
		setResourceId(resource == null ? null : resource.getId());
		setGroup(group);
	}

	public MemberImpl(String ownerId, String resourceId, Role role) {
		setOwnerId(ownerId);
		setResourceId(resourceId);
		setRole(role);
	}

	public MemberImpl(String name, Member member) {
		this(name, member.getResourceId(), member.getRole());
	}

	public MemberImpl(Member member) {
		setResourceId(member.getResourceId());
		setRole(member.getRole());
		setOwnerId(member.getOwnerId());
	}

	@Override
	@Column(name = "OWNER_ID")
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	@Column(name = "RESOURCEID", nullable = true)
	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	@Override
	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "ROLE")
	@Target(value = RoleImpl.class)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "DGC_MEMBER_GENERATOR")
	@SequenceGenerator(name = "DGC_MEMBER_GENERATOR", sequenceName = "MEMBERS_SEQUENCE")
	public Long getMembershipId() {
		return membershipId;
	}

	protected void setMembershipId(Long membershipId) {
		this.membershipId = membershipId;
	}

	@Column(name = "IS_GROUP")
	@Override
	public boolean isGroup() {
		return group;
	}

	public void setGroup(boolean group) {
		this.group = group;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
		result = prime * result + ((resourceId == null) ? 0 : resourceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (!(obj instanceof Member)) {
			return false;
		}

		Member other = (Member) obj;

		if (ownerId == null) {
			if (other.getOwnerId() != null)
				return false;
		} else if (!ownerId.equals(other.getOwnerId()))
			return false;
		if (resourceId == null) {
			if (other.getResourceId() != null)
				return false;
		} else if (!resourceId.equals(other.getResourceId()))
			return false;
		return true;
	}

	@Override
	public Member clone() {
		return new MemberImpl(this);
	}

}
