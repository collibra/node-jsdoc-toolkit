package com.collibra.dgc.core.workflow.activiti.tasklistener;

import java.util.LinkedList;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.AppContext;

/**
 * Listens to creation of review task for the users and fires notification mails.
 * @author amarnath
 * 
 */
public class ReviewStartTaskListener implements TaskListener {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
	 */
	@Override
	@Transactional
	public void notify(DelegateTask delegateTask) {
		String user = delegateTask.getAssignee();
		List<String> users = new LinkedList<String>();
		users.add(user);
		AppContext.getWorkflowUtility().notifyTaskAssignment(delegateTask, users);
	}
}
