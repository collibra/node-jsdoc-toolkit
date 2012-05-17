package com.collibra.dgc.core.workflow.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author amarnath
 * 
 */
public class ArticulationRejected extends AbstractAritculationDelegate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Transactional
	public void execute(DelegateExecution execution) throws Exception {
		changeState(execution);
	}
}
