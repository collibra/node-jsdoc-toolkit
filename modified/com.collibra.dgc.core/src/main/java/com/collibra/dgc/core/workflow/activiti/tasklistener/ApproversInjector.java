package com.collibra.dgc.core.workflow.activiti.tasklistener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.AppContext;
import com.collibra.dgc.core.workflow.WorkflowConstants;

/**
 * {@link TaskListener} to set candidate users on 'Approve' user task start in Approval process.
 * @author amarnath
 * 
 */
public class ApproversInjector implements TaskListener {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
	 */
	@Override
	@Transactional
	public void notify(DelegateTask delegateTask) {
		AppContext.getWorkflowUtility().setCandidateUsers(delegateTask, WorkflowConstants.VAR_ACCOUNTABLE_ROLES);
	}
}
