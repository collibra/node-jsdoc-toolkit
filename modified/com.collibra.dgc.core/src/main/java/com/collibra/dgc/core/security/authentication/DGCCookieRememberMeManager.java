package com.collibra.dgc.core.security.authentication;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.CookieRememberMeManager;

import com.collibra.dgc.core.security.TooManyConcurrentUsersException;

/**
 * Custom CookieRememeberMeManager so it calls it's observers whenever a successful login is achieved with a RememberMe
 * token
 * @author GKDAI63
 * 
 */
public class DGCCookieRememberMeManager extends CookieRememberMeManager {

	private Collection<RememberMeLoginListener> rememberMeLoginListeners = new ArrayList<RememberMeLoginListener>();

	@Override
	public PrincipalCollection getRememberedPrincipals(SubjectContext subjectContext) {
		PrincipalCollection pc = super.getRememberedPrincipals(subjectContext);
		if (pc != null) {
			// If the principalCollection isn't null we found a person who is logging in with a remember me token.
			// So next thing to do is notify our listners someone is logging in with a remember-me token.
			try {
				for (RememberMeLoginListener rememberMeLoginListener : rememberMeLoginListeners) {
					rememberMeLoginListener.onSuccessfulRememberMeLogin(pc);
				}
			} catch (TooManyConcurrentUsersException ex) {
				subjectContext.setPrincipals(pc);
				forgetIdentity(subjectContext);
				pc = null;
				subjectContext.setPrincipals(pc);
			}
		}
		return pc;
	}

	/**
	 * Sets the listners
	 * @param rememberMeLoginListeners
	 */
	public void setRememberMeLoginListeners(Collection<RememberMeLoginListener> rememberMeLoginListeners) {
		this.rememberMeLoginListeners = rememberMeLoginListeners;
	}

}
