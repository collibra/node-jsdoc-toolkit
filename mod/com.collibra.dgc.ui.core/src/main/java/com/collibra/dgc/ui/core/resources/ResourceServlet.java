/**
 * 
 */
package com.collibra.dgc.ui.core.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.ui.core.modules.ExternalModuleProvider;
import com.collibra.dgc.ui.core.modules.IModuleProvider;

/**
 * Servlet to retrieve files from the classpath or the external widget directory structure.
 * @author dieterwachters
 */
public class ResourceServlet extends HttpServlet {
	private static final long serialVersionUID = -2895299569751144739L;

	private static final Map<String, String> typeMapping = new HashMap<String, String>();
	static {
		// JavaScript
		typeMapping.put(".js", "text/javascript");
		// CSS
		typeMapping.put(".css", "text/css");
		typeMapping.put(".less", "text/css");
		// Image formats
		typeMapping.put(".png", "image/png");
		typeMapping.put(".jpg", "image/jpeg");
		typeMapping.put(".jpeg", "image/jpeg");
		typeMapping.put(".svg", "image/svg");
		typeMapping.put(".gif", "image/gif");
		typeMapping.put(".ico", "image/x-icon");
		typeMapping.put(".txt", "plain/text");
		// Fonts
		typeMapping.put(".eot", "application/vnd.ms-fontobject");
	}

	private static final Logger log = LoggerFactory.getLogger(ResourceServlet.class);

	private static final String PREFIX = "resources";

	private long resourcesLastModified = 0;

	private static ServletContext servletContext;

	public ResourceServlet() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		resourcesLastModified = System.currentTimeMillis();
		servletContext = config.getServletContext();
		super.init(config);
	}

	public static final InputStream getFileData(String path) {
		InputStream is = null;
		if (path.startsWith("library/")) {
			is = ResourceServlet.class.getResourceAsStream("/" + path);
		}

		if (is == null) {
			if (path.startsWith("generated/")) {
				final File generatedFile = new File(Application.USER_HOME, "cache/" + path);
				if (generatedFile.exists()) {
					try {
						is = new FileInputStream(generatedFile);
					} catch (Exception e) {
						log.error("Unable to find cached resource '" + path + "'.", e);
					}
				}
			}
		}

		if (is == null) {
			final ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			final Map<String, IModuleProvider> providers = ac.getBeansOfType(IModuleProvider.class);
			for (final IModuleProvider provider : providers.values()) {
				try {
					is = provider.getInputStream(path);
					if (is != null) {
						return is;
					}
				} catch (IOException e) {
					log.error("Error while trying to read '" + path + "' with provider '"
							+ provider.getClass().getName() + "'.", e);
				}
			}
		}

		if (is == null) {
			if (path.startsWith(ExternalModuleProvider.PREFIX)) {
				path = path.substring(ExternalModuleProvider.PREFIX.length());
				final File widgetFolder = new File(Application.USER_HOME, "widgets");
				try {
					is = new FileInputStream(new File(widgetFolder, path));
				} catch (Exception e) {
					log.error("Unable to find external resource '" + path + "'.", e);
				}
			} else {
				is = ResourceServlet.class.getResourceAsStream("/" + path);
			}
		}
		return is;
	}

	private final String suffixMatch(final String path) {
		for (final String suffix : typeMapping.keySet()) {
			if (path.endsWith(suffix))
				return suffix;
		}
		return null;
	}

	public static long getLastModified(final String path) {
		if (path.startsWith("library/")) {
			return Application.STARTUP_DATE;
		}

		if (path.startsWith("generated/")) {
			final File generatedFile = new File(Application.USER_HOME, "cache/" + path);
			if (generatedFile.exists()) {
				return generatedFile.lastModified();
			}
		}

		final ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		final Map<String, IModuleProvider> providers = ac.getBeansOfType(IModuleProvider.class);
		for (final IModuleProvider provider : providers.values()) {
			final long lm = provider.getLastModified(path);
			if (lm >= 0) {
				return lm;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#getLastModified(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected long getLastModified(HttpServletRequest req) {
		String path = req.getRequestURI();
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
		}
		if (suffixMatch(path) == null) {
			return super.getLastModified(req);
		}

		if (path.startsWith(req.getContextPath())) {
			path = path.substring(req.getContextPath().length() + 1);
		}
		if (path.startsWith(PREFIX + "/")) {
			path = path.substring(PREFIX.length() + 1);
		}

		final long lm = getLastModified(path);
		if (lm > 0) {
			return lm;
		}

		if (path.startsWith(ExternalModuleProvider.PREFIX)) {
			path = path.substring(ExternalModuleProvider.PREFIX.length());
			final File widgetFolder = new File(Application.USER_HOME, "widgets");
			final File widgetFile = new File(widgetFolder, path);
			if (widgetFile.exists()) {
				return widgetFile.lastModified();
			}
		} else {
			if (getClass().getResource("/" + path) != null) {
				return resourcesLastModified;
			}
		}

		return super.getLastModified(req);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getRequestURI();
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
		}
		final String suffix = suffixMatch(path);
		if (suffix == null) {
			resp.sendError(404, "Requested resource not found.");
			return;
		}

		if (path.startsWith(req.getContextPath())) {
			path = path.substring(req.getContextPath().length() + 1);
		}
		if (path.startsWith(PREFIX + "/")) {
			path = path.substring(PREFIX.length() + 1);
		}
		final InputStream is = getFileData(path);
		if (is == null) {
			resp.sendError(404, "The requested reousrce file was not found.");
		} else {
			try {
				resp.setContentType(typeMapping.get(suffix));
				IOUtils.copy(is, resp.getOutputStream());
			} finally {
				is.close();
				resp.getOutputStream().close();
			}
		}
	}
}
