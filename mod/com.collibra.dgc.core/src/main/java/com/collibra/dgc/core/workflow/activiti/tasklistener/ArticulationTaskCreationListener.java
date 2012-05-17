package com.collibra.dgc.core.workflow.activiti.tasklistener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.workflow.WorkflowConstants;

/**
 * 
 * @author amarnath
 * 
 */
public class ArticulationTaskCreationListener extends AbsractArticulationCreationTaskListener implements TaskListener {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.impl.pvm.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
	 */
	@Transactional
	public void notify(DelegateTask delegateTask) {
		addCandidateUsers(delegateTask, WorkflowConstants.VAR_LIST_DELEGATES);
	}
}
