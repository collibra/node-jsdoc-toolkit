package com.collibra.dgc.core;

import org.springframework.context.ApplicationContext;

import com.collibra.dgc.core.service.AttributeService;
import com.collibra.dgc.core.service.CategorizationService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.RightsService;
import com.collibra.dgc.core.service.WorkflowService;
import com.collibra.dgc.core.workflow.WorkflowEngine;
import com.collibra.dgc.core.workflow.WorkflowUtility;

/**
 * Provides {@link ApplicationContext} and other DGB beans access across the application.
 * @author amarnath
 * 
 */
public class AppContext {
	private static ApplicationContext context;

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext context) {
		AppContext.context = context;
	}

	public static RepresentationService getRepresentationService() {
		return context.getBean(RepresentationService.class);
	}

	public static WorkflowUtility getWorkflowUtility() {
		return context.getBean(WorkflowUtility.class);
	}

	public static RightsService getRightsService() {
		return context.getBean(RightsService.class);
	}

	public static WorkflowEngine getWorkflowEngine() {
		return context.getBean(WorkflowEngine.class);
	}

	public static AttributeService getAttributeService() {
		return context.getBean(AttributeService.class);
	}

	public static WorkflowService getWorkflowService() {
		return context.getBean(WorkflowService.class);
	}

	public static CategorizationService getCategorizationService() {
		return context.getBean(CategorizationService.class);
	}
}
