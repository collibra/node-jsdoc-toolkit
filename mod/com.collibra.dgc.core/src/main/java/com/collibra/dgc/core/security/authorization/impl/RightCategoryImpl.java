package com.collibra.dgc.core.security.authorization.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.collibra.dgc.core.security.authorization.Right;
import com.collibra.dgc.core.security.authorization.RightCategory;

public class RightCategoryImpl implements RightCategory {
	private String id;
	private String name;
	private Collection<Right> rights = new ArrayList<Right>();
	private RightCategory parent;
	private Collection<RightCategory> subcategories = new ArrayList<RightCategory>();
	private boolean active = true;
	private Boolean global = null;

	public RightCategoryImpl(String id, String name) {
		this(id, name, null, null, null);
	}

	public RightCategoryImpl(String id, String name, Collection<Right> rights) {
		this(id, name, rights, null, null);
	}

	public RightCategoryImpl(String id, String name, RightCategory parent) {
		this(id, name, null, null, parent);
	}

	public RightCategoryImpl(String id, String name, Collection<RightCategory> subcategories, RightCategory parent) {
		this(id, name, null, subcategories, parent);
	}

	public RightCategoryImpl(String id, String name, Collection<Right> rights, Collection<RightCategory> subcategories) {
		this(id, name, rights, subcategories, null);
	}

	public RightCategoryImpl(String id, String name, Collection<Right> rights, Collection<RightCategory> subcategories,
			RightCategory parent) {
		this.id = id;
		this.name = name;
		if (rights != null) {
			this.rights = rights;
		}
		if (subcategories != null) {
			this.subcategories = subcategories;
		}
		this.parent = parent;
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

	public Collection<Right> getRights() {
		return rights;
	}

	public boolean add(Right right) {
		return rights.add(right);
	}

	public boolean remove(Right right) {
		return rights.remove(right);
	}

	public Collection<RightCategory> getSubcategories() {
		return subcategories;
	}

	public boolean add(RightCategory rightCategory) {
		return subcategories.add(rightCategory);
	}

	public boolean remove(RightCategory rightCategory) {
		return subcategories.remove(rightCategory);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Collection<String> getPermissionStrings() {
		List<String> permissions = new LinkedList<String>();
		for (Right right : getRights()) {
			permissions.add(right.getId());
		}

		return permissions;
	}

	public Collection<String> getCompleteHierarchyPermissionStrings() {
		List<String> permissions = new LinkedList<String>();
		getCompleteHierarchyPermissionStrings(this, permissions);
		return permissions;
	}

	private void getCompleteHierarchyPermissionStrings(RightCategory rc, List<String> permissions) {
		permissions.addAll(rc.getPermissionStrings());
		for (RightCategory subCategory : rc.getSubcategories()) {
			getCompleteHierarchyPermissionStrings(subCategory, permissions);
		}
	}

	// public Collection<String> getAllDependencies() {
	// Set<String> dependencies = new HashSet<String>();
	// getAllDependencies(this, getCompleteHierarchyPermissionStrings(), dependencies);
	// return dependencies;
	// }
	//
	// private void getAllDependencies(RightCategory rc, Collection<String> rightsTobeExcluded, Collection<String>
	// dependencies) {
	// for (Right right : getRights()) {
	// for (String depRight : right.getAllDependencies()) {
	// if (!rightsTobeExcluded.contains(depRight)) {
	// dependencies.add(depRight);
	// }
	// }
	// }
	// }

	public void setGlobal(Boolean global) {
		this.global = global;
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
}
