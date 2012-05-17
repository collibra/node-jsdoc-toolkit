/**
 * 
 */
package com.collibra.dgc.ui.core.velocity;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.collibra.dgc.core.application.Application;

/**
 * @author dieterwachters
 * 
 */
public class CachingClasspathResourceLoader extends ClasspathResourceLoader {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#setCachingOn(boolean)
	 */
	@Override
	public void setCachingOn(boolean value) {
		super.setCachingOn(value && !Application.DEVELOPER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.velocity.runtime.resource.loader.ResourceLoader#commonInit(org.apache.velocity.runtime.RuntimeServices
	 * , org.apache.commons.collections.ExtendedProperties)
	 */
	@Override
	public void commonInit(RuntimeServices rs, ExtendedProperties configuration) {
		super.commonInit(rs, configuration);
		isCachingOn = isCachingOn && !Application.DEVELOPER;
	}
}
