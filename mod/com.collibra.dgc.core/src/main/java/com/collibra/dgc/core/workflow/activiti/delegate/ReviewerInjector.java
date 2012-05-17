package com.collibra.dgc.core.workflow.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author amarnath
 * 
 */
public class ReviewerInjector extends AbstractAritculationDelegate {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Transactional
	public void execute(DelegateExecution execution) throws Exception {
		// change state
		changeState(execution);
	}
}
