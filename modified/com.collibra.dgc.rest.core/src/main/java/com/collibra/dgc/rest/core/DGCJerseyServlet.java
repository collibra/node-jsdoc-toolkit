/**
 * 
 */
package com.collibra.dgc.rest.core;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.WebApplication;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 * @author dieterwachters
 * 
 */
public class DGCJerseyServlet extends SpringServlet {

	private static final long serialVersionUID = 5164531278860963701L;

	private static DGCJerseyServlet instance;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jersey.spi.spring.container.servlet.SpringServlet#initiate(com.sun.jersey.api.core.ResourceConfig,
	 * com.sun.jersey.spi.container.WebApplication)
	 */
	@Override
	protected void initiate(ResourceConfig rc, WebApplication wa) {
		instance = this;
		super.initiate(rc, wa);
	}

	public static void reloadInstance() {
		instance.reload();
	}
}
