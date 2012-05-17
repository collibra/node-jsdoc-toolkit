package com.collibra.dgc.core.security.authentication;

import org.apache.shiro.subject.PrincipalCollection;

/**
 * Observer interface for Apache Shiro RememberMe logins
 * @author GKDAI63
 * 
 */
public interface RememberMeLoginListener {

	/**
	 * Listner method that get's called whenever someone successfully logs in with a RememberMe token
	 * @param pc
	 */
	public void onSuccessfulRememberMeLogin(PrincipalCollection pc);

}
