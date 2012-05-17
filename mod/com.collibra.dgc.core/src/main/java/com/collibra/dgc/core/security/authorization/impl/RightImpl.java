package com.collibra.dgc.core.security.authorization.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.security.authorization.GlossaryRight;
import com.collibra.dgc.core.security.authorization.Right;
import com.collibra.dgc.core.security.authorization.RightCategory;

/**
 * Default {@link Right} implementation.
 * 
 * @author amarnath
 * 
 */
public class RightImpl implements GlossaryRight {
	private Boolean global = null;
	private String id;
	private String name;
	private boolean impactOnLock;
	private RightCategory parent;

	// Only internal to glossary.
	private String[] supportedByRoles;

	// This right depends on rights.
	private final Set<String> dependsOnRights = new HashSet<String>();
	private boolean active;

	public RightImpl(String id, String name, RightCategory parent) {
		this(id, name, parent, null);
	}

	public RightImpl(String id, String name, RightCategory parent, String[] supportedByRoles) {
		this.id = id;
		this.name = name;
		this.parent = parent;
		this.supportedByRoles = supportedByRoles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RightCategory getParentCategory() {
		return parent;
	}

	public void setParent(RightCategory parent) {
		this.parent = parent;
	}

	public boolean isGlobal() {
		if (global != null) {
			return global.booleanValue();
		}
		if (parent != null) {
			return parent.isGlobal();
		}
		return false;
	}

	public void setGlobal(final Boolean global) {
		this.global = global;
	}

	// NOTE: The following methods will be used while bootstrapping user roles
	// as a flexible way of adding rights for
	// BSG defined roles.
	/**
	 * Get {@link Role} names having the {@link Right}.
	 * 
	 * @return The role names
	 */
	public String[] getSupportedByRoles() {
		if (supportedByRoles == null) {
			supportedByRoles = new String[0];
		}
		return supportedByRoles;
	}

	/**
	 * Set {@link Role} names having the {@link Right}.
	 * 
	 * @param supportedByRoles The role names.
	 */
	public void setSupportedByRoles(String[] supportedByRoles) {
		this.supportedByRoles = supportedByRoles;
	}

	public boolean hasImpactOnLock() {
		return impactOnLock;
	}

	public void setImpactOnLock(boolean impactOnLock) {
		this.impactOnLock = impactOnLock;
	}

	public Collection<String> getDependencies() {
		return dependsOnRights;
	}

	public void setDependsOnRights(Collection<String> dependsOn) {
		this.dependsOnRights.addAll(dependsOn);
	}

	// public Collection<String> getAllDependencies() {
	// Set<String> dependencies = new HashSet<String>();
	// getAllDependencies(this, dependencies);
	// return dependencies;
	// }
	//
	// private void getAllDependencies(Right right, Collection<String> dependencies) {
	// dependencies.addAll(right.getDependencies());
	// for (String rightId : right.getDependencies()) {
	// Right depRight = AuthorizationsFactoryImpl.getInstance().getRight(rightId);
	// getAllDependencies(depRight, dependencies);
	// }
	// }

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
