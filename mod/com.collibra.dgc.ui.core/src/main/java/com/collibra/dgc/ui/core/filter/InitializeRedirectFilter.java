/**
 * 
 */
package com.collibra.dgc.ui.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.collibra.dgc.core.application.DGCDataSourceFactory;

/**
 * @author dieterwachters
 * 
 */
public class InitializeRedirectFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
	 * javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		if (response instanceof HttpServletResponse) {
			String path = ((HttpServletRequest) request).getRequestURI();
			if (path.startsWith(((HttpServletRequest) request).getContextPath())) {
				path = path.substring(((HttpServletRequest) request).getContextPath().length());
			}
			path = path.toLowerCase();
			if (!DGCDataSourceFactory.isDataSourceCreated() && !path.startsWith("/resources/")
					&& !path.startsWith("/rest/") && !path.equals("/initialize")) {
				((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/"
						+ "initialize");
				return;
			}
			if (path.equals("/") || path.isEmpty()) {
				((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/"
						+ "dashboard");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {

	}
}
