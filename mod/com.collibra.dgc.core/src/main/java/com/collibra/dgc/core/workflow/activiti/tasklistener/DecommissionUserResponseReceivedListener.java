package com.collibra.dgc.core.workflow.activiti.tasklistener;

import java.util.Collection;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.WorkflowEngine;

/**
 * {@link TaskListener} fired after getting each user response. Adds the user response for {@link Term} decommissioning
 * as threaded comment on the page. If all the responsible users have responded then ignore the other users and proceed
 * with decommissioning.
 * @author amarnath
 * 
 */
public class DecommissionUserResponseReceivedListener implements TaskListener {
	@Autowired
	protected WorkflowEngine workflowEngine;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
	 */
	@Transactional
	public void notify(DelegateTask delegateTask) {
		execute(delegateTask.getExecution());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Transactional
	public void execute(DelegateExecution execution) {
		// TODO PORT
	}

	/**
	 * Kill all the other user response tasks if all responsbile users have responded.
	 * @param execution
	 */
	private void closeAllTasksIfAllResonsibleUsersHaveResponded(DelegateExecution execution) {
		for (String user : (Collection<String>) execution.getVariable(WorkflowConstants.VAR_RESPONSIBLE_LIST)) {
			for (String resourceId : (Collection<String>) execution.getVariable(user)) {
				Object response = execution.getVariable(user + resourceId);
				if (response == null) {
					// This user has not responded yet, so cannot close other tasks.
					return;
				}

				break;
			}
		}

		// Delete all other user tasks pending for user response as all responsible users have responded.
		for (Task task : workflowEngine.getInternalEngine().getTaskService().createTaskQuery()
				.processInstanceId(execution.getProcessInstanceId()).list()) {
			// **************************************************************************************************************
			// NOTE: Completing task is not an option because it is resulting in error: Invalid access of stack red zone
			// WorkflowEngineImpl.getInstance().getInternalEngine().getTaskService().complete(task.getId());
			// **************************************************************************************************************
			workflowEngine.getInternalEngine().getTaskService().deleteTask(task.getId());
		}

		execution.setVariable(WorkflowConstants.VAR_ALL_RESPONSIBLE_USERS_RESPONDED, Boolean.TRUE);
	}
}
