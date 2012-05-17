package com.collibra.dgc.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Spring bean to share the {@link ApplicationContext} throughout the application.
 * @author amarnath
 * 
 */
@Service
public class ApplicationContextBean implements ApplicationContextAware {
	private ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
		AppContext.setContext(context);
	}

	public ApplicationContext getApplicationContext() {
		return context;
	}
}
