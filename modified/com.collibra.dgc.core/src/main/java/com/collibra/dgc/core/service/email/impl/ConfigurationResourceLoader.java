/**
 * 
 */
package com.collibra.dgc.core.service.email.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import com.collibra.dgc.core.service.ConfigurationService;

/**
 * @author dieterwachters
 * 
 */
public class ConfigurationResourceLoader extends ResourceLoader {
	private ConfigurationService configService;

	@Override
	public void init(ExtendedProperties configuration) {
		configService = (ConfigurationService) configuration.getProperty("configService");
	}

	@Override
	public InputStream getResourceStream(String source) throws ResourceNotFoundException {
		if (!source.startsWith("/")) {
			source = "/" + source;
		}
		final String value = configService.getString("core/mail/templates" + source);
		if (value != null) {
			try {
				return new ByteArrayInputStream(value.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}

	@Override
	public boolean isSourceModified(Resource resource) {
		return false;
	}

	@Override
	public long getLastModified(Resource resource) {
		return 0;
	}
}
