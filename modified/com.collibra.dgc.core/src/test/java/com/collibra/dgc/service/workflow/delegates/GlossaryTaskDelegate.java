package com.collibra.dgc.service.workflow.delegates;

import org.activiti.engine.delegate.DelegateExecution;

import com.collibra.dgc.core.workflow.activiti.delegate.AbstractDelegate;

/**
 * 
 * @author amarnath
 * 
 */
public class GlossaryTaskDelegate extends AbstractDelegate {
	public static final String GLOSSARY_ACTIVITI_INTEGRATION_TEST_COMMUNITY_URI = "Glossary Activiti Transaction Integration Test URI";

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO PORT
		// CommunityFactory commFactory = CommunityFactoryImpl.getInstance();
		// SemanticCommunity sc = commFactory.makeSemanticCommunity("Test Semantic Community",
		// GLOSSARY_ACTIVITI_INTEGRATION_TEST_COMMUNITY_URI);
		// ComponentManagerUtil.getCommunityService().commit(sc);
	}
}
