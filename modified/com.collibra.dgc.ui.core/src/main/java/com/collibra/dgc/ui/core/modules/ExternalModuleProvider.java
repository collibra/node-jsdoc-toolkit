/**
 * 
 */
package com.collibra.dgc.ui.core.modules;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.ui.core.UICore;

/**
 * Implementation of an {@link IModuleProvider} that will get its data from the user.home directory of the application.
 * @author dieterwachters
 */
@Component
public class ExternalModuleProvider implements IModuleProvider {
	private static final String TRANSLATIONS_FOLDER = "i18n";
	public static final String PREFIX = "external/";

	@Autowired
	private ConfigurationService configurationService;

	public Module getModule(String path) {
		String name = path;
		if (name.contains("/")) {
			name = name.substring(name.lastIndexOf("/") + 1);
		}

		final File modulesFolder = UICore.getModulesDirectory();
		if (!modulesFolder.exists()) {
			return null;
		}

		if (path.startsWith("/")) {
			path = path.substring(1);
		}

		final File moduleFolder = new File(modulesFolder, path);
		if (!modulesFolder.exists()) {
			return null;
		}

		// TODO caching
		final String prefix = PREFIX + path + "/";

		final File files[] = moduleFolder.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}
		long lastmodified = 0;
		List<String> javascripts = null;
		List<String> stylesheets = null;
		String template = null;
		String properties = null;
		Map<String, String> translations = null;
		for (final File file : files) {
			if (file.isFile()) {
				lastmodified = Math.max(lastmodified, file.lastModified());
				if (file.getName().endsWith(".js")) {
					if (javascripts == null)
						javascripts = new ArrayList<String>();
					javascripts.add(prefix + file.getName());
				} else if (file.getName().endsWith(".css") || file.getName().endsWith(".less")) {
					if (stylesheets == null)
						stylesheets = new ArrayList<String>();
					stylesheets.add(prefix + file.getName());
				} else if (file.getName().endsWith(".vm")) {
					template = prefix + file.getName();
				} else if (file.getName().endsWith(".properties")) {
					properties = prefix + file.getName();
				}
			} else if (file.isDirectory() && file.getName().equals(TRANSLATIONS_FOLDER)) {
				translations = new HashMap<String, String>();
				final File tfiles[] = file.listFiles();
				for (final File tfile : tfiles) {
					if (tfile.isFile() && tfile.getName().endsWith(".properties")) {
						lastmodified = Math.max(lastmodified, tfile.lastModified());
						String tname = tfile.getName();
						tname = tname.substring(0, tname.lastIndexOf("."));
						translations.put(tname, prefix + TRANSLATIONS_FOLDER + "/" + tfile.getName());
					}
				}
			}
		}
		if (template != null) {
			return new Module(path, prefix, properties, template, lastmodified, javascripts, stylesheets, translations,
					configurationService);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.ui.core.modules.IModuleProvider#getLastModified(java.lang.String)
	 */
	public long getLastModified(String path) {
		final File file = getFile(path);
		if (file != null) {
			return file.lastModified();
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.ui.core.modules.IModuleProvider#getInputStream(java.lang.String)
	 */
	public InputStream getInputStream(String path) throws IOException {
		final File file = getFile(path);
		if (file != null) {
			return new BufferedInputStream(new FileInputStream(file));
		}
		return null;
	}

	/**
	 * Helper method to retrieve the file behind the path.
	 */
	private final File getFile(String path) {
		if (path.startsWith(PREFIX)) {
			path = path.substring(PREFIX.length());
			final File file = new File(UICore.getModulesDirectory(), path);
			if (file.exists()) {
				return file;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.ui.core.modules.IModuleProvider#getOrder()
	 */
	@Override
	public int getOrder() {
		return 0;
	}
}
