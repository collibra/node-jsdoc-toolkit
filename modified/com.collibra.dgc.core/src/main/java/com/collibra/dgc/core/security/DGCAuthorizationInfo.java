/**
 * 
 */
package com.collibra.dgc.core.security;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;

/**
 * Holds the authorization info for a specific user.
 * 
 * @author dieterwachters
 */
public class DGCAuthorizationInfo implements AuthorizationInfo {
	private static final long serialVersionUID = -3704561976327732232L;

	private Set<String> permissions;
	private Set<String> roles;

	public DGCAuthorizationInfo(Set<String> permissions, Set<String> roles) {
		this.permissions = permissions;
		this.roles = roles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shiro.authz.AuthorizationInfo#getRoles()
	 */
	@Override
	public Collection<String> getRoles() {
		return roles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shiro.authz.AuthorizationInfo#getStringPermissions()
	 */
	@Override
	public Collection<String> getStringPermissions() {
		return permissions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shiro.authz.AuthorizationInfo#getObjectPermissions()
	 */
	@Override
	public Collection<Permission> getObjectPermissions() {
		return null;
	}

}
