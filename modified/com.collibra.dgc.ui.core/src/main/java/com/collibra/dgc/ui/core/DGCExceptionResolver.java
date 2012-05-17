/**
 * 
 */
package com.collibra.dgc.ui.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

/**
 * @author dieterwachters
 * 
 */
public class DGCExceptionResolver extends AbstractHandlerExceptionResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver#doResolveException(javax.servlet.http
	 * .HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("contextPath", request.getContextPath() + "/");
		final StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		model.put("exception", ex);
		model.put("message", ex.getLocalizedMessage() == null ? "null" : ex.getLocalizedMessage());
		String stacktrace = sw.getBuffer().toString().replace(System.getProperty("line.separator"), "<br/>\n");
		model.put("stacktrace", stacktrace);
		return new ModelAndView("pages/error/error", model);
	}
}
