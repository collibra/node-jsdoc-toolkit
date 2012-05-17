/**
 * 
 */
package com.collibra.dgc.ui.core.velocity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.ui.core.UICore;
import com.collibra.dgc.ui.core.modules.ExternalModuleProvider;

/**
 * A custom file resource loader for our widgets.
 * 
 * @author dieterwachters
 */
public class WidgetFileResourceLoader extends ResourceLoader {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.velocity.runtime.resource.loader.FileResourceLoader#resourceExists (java.lang.String)
	 */
	@Override
	public boolean resourceExists(String name) {
		if (name.startsWith(ExternalModuleProvider.PREFIX)) {
			final File widgetFile = getFile(name);
			return widgetFile.exists() && widgetFile.canRead();
		}
		return super.resourceExists(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.velocity.runtime.resource.loader.FileResourceLoader# getResourceStream(java.lang.String)
	 */
	@Override
	public InputStream getResourceStream(String name) throws ResourceNotFoundException {
		final File widgetFile = getFile(name);
		if (widgetFile != null && widgetFile.exists() && widgetFile.canRead()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(widgetFile.getAbsolutePath());
				return new BufferedInputStream(fis);
			} catch (IOException e) {
				closeQuiet(fis);
				throw new VelocityException(e);
			}
		}
		return null;
	}

	private final File getFile(String name) {
		if (name.startsWith(ExternalModuleProvider.PREFIX)) {
			name = name.substring(ExternalModuleProvider.PREFIX.length());
			return new File(UICore.getModulesDirectory(), name);
		}
		return null;
	}

	private void closeQuiet(final InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException ioe) {
				// Ignore
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#init(org.apache
	 * .commons.collections.ExtendedProperties)
	 */
	@Override
	public void init(ExtendedProperties configuration) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#isSourceModified
	 * (org.apache.velocity.runtime.resource.Resource)
	 */
	@Override
	public boolean isSourceModified(Resource resource) {
		if (Application.DEVELOPER) {
			return true;
		}
		final long lm = getLastModified(resource);
		return lm > resource.getLastModified();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#getLastModified
	 * (org.apache.velocity.runtime.resource.Resource)
	 */
	@Override
	public long getLastModified(Resource resource) {
		final File widgetFile = getFile(resource.getName());
		if (widgetFile != null && widgetFile.canRead()) {
			return widgetFile.lastModified();
		}
		return 0;
	}
}
