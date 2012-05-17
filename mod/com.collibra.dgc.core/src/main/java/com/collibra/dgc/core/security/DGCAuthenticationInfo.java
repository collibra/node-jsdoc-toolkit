/**
 * 
 */
package com.collibra.dgc.core.security;

import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

import com.collibra.dgc.core.model.user.User;

/**
 * @author dieterwachters
 *
 */
public class DGCAuthenticationInfo implements SaltedAuthenticationInfo {
	private static final long serialVersionUID = 2530138035233708838L;
	
	private final User user;
	private final PrincipalCollection principalCollection;
	
	public DGCAuthenticationInfo(final User user, final String realmName) {
		this.user = user;
		this.principalCollection = new SimplePrincipalCollection(user.getUserName(), realmName);
	}
	
	
	/* (non-Javadoc)
	 * @see org.apache.shiro.authc.AuthenticationInfo#getPrincipals()
	 */
	@Override
	public PrincipalCollection getPrincipals() {
		return principalCollection;
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.authc.AuthenticationInfo#getCredentials()
	 */
	@Override
	public Object getCredentials() {
		return user.getPasswordHash();
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.authc.SaltedAuthenticationInfo#getCredentialsSalt()
	 */
	@Override
	public ByteSource getCredentialsSalt() {
		return new SimpleByteSource(Base64.decode(user.getSalt()));
	}

}
