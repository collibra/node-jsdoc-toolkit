/**
 * 
 */
package com.collibra.dgc.core.application;

import java.util.LinkedHashMap;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.core.service.license.LicenseService;

/**
 * Factory bean for creating the Shiro filter by taking the necessary information from the configuration.
 * @author dieterwachters
 */
public class DGCShiroFilterFactoryBean extends ShiroFilterFactoryBean implements InitializingBean {
	@Autowired
	private ConfigurationService config;
	@Autowired
	private LicenseService licenseService;

	private static AbstractShiroFilter firstFilter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		final LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

		final boolean guestAccess = config.getBoolean("core/security/guest-access")
				&& licenseService.isGuestAccessAllowed();

		filterChainDefinitionMap.put("/rest/**", "anon");
		filterChainDefinitionMap.put("/initialize", "anon");
		filterChainDefinitionMap.put("/bootstrap", "anon");
		filterChainDefinitionMap.put("/resources/**", "anon");
		filterChainDefinitionMap.put("/login", "authc");
		filterChainDefinitionMap.put("/**", guestAccess ? "anon" : "authc");
		setFilterChainDefinitionMap(filterChainDefinitionMap);

		// If we already created a filter for a previous startup, we update the security manager
		// and chain resolver.
		if (firstFilter != null && getSecurityManager() instanceof WebSecurityManager) {
			firstFilter.setSecurityManager((WebSecurityManager) getSecurityManager());

			FilterChainManager manager = createFilterChainManager();

			// Expose the constructed FilterChainManager by first wrapping it in a
			// FilterChainResolver implementation. The AbstractShiroFilter implementations
			// do not know about FilterChainManagers - only resolvers:
			PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
			chainResolver.setFilterChainManager(manager);

			firstFilter.setFilterChainResolver(chainResolver);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shiro.spring.web.ShiroFilterFactoryBean#createInstance()
	 */
	@Override
	protected AbstractShiroFilter createInstance() throws Exception {
		if (firstFilter == null) {
			firstFilter = super.createInstance();
		}
		return firstFilter;
	}
}
