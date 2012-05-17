package com.collibra.dgc.core.model.user.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Target;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.impl.TermImpl;
import com.collibra.dgc.core.model.user.Role;

/**
 * Implementation for {@link Role}.
 * <p>
 * @author amarnath
 * @author pmalarme
 * 
 */
@Entity
@Audited
@Table(name = "ROLES")
public class RoleImpl implements Role {
	private Set<String> rights = new HashSet<String>();
	private Term term;
	private long roleId;
	private boolean global;

	public RoleImpl() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("ROLE ");
		buf.append(getTerm().getSignifier()).append(" has rights ");
		for (final String right : getRights()) {
			buf.append(right).append(", ");
		}
		return buf.toString();
	}

	public RoleImpl(Term roleTerm, boolean global) {
		this.term = roleTerm;
		this.global = global;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((term.getId() == null) ? 0 : term.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (!(obj instanceof Role)) {
			return false;
		}

		Role other = (Role) obj;
		if (term.getId() == null) {
			if (other.getTerm().getId() != null)
				return false;
		} else if (!term.getId().equals(other.getTerm().getId()))
			return false;
		return true;
	}

	@Override
	@Transient
	public String getName() {
		return term.getSignifier();
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "DGC_ROLE_GENERATOR")
	@SequenceGenerator(name = "DGC_ROLE_GENERATOR", sequenceName = "ROLES_SEQUENCE")
	public long getRoleId() {
		return roleId;
	}

	protected void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TERM", nullable = false)
	@Target(value = TermImpl.class)
	public Term getTerm() {
		return term;
	}

	protected void setTerm(Term term) {
		this.term = term;
	}

	@Override
	@ElementCollection
	@CollectionTable(name = "ROLE_RIGHTS", joinColumns = @JoinColumn(name = "ROLE"))
	@Column(name = "RIGHT_NAME")
	public Set<String> getRights() {
		return rights;
	}

	@Override
	public void setRights(Set<String> rights) {
		this.rights = rights;
	}

	@Override
	public boolean isPermitted(String right) {
		return getRights().contains(right);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.model.user.Role#isGlobal()
	 */
	@Override
	@Column(name = "global")
	public boolean isGlobal() {
		return global;
	}

	protected void setGlobal(final boolean global) {
		this.global = global;
	}
}
