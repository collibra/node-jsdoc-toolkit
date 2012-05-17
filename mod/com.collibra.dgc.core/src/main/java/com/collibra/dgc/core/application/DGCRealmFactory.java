/**
 * 
 */
package com.collibra.dgc.core.application;

import org.apache.shiro.realm.Realm;
import org.springframework.beans.factory.annotation.Autowired;

import com.collibra.dgc.core.security.DGCRealm;
import com.collibra.dgc.core.service.ConfigurationService;

/**
 * @author dieterwachters
 * 
 */
public class DGCRealmFactory {
	@Autowired
	ConfigurationService config;

	/**
	 * Creates the realm by getting the needed information from the configuration.
	 * 
	 * @return
	 */
	public Realm createRealm() {
		// TODO implement this!
		DGCRealm dgcrealm = new DGCRealm();
		dgcrealm.setAuthorizationCache(null);
		dgcrealm.setAuthorizationCachingEnabled(false);
		return dgcrealm;
		// return new PropertiesRealm();
	}
}
