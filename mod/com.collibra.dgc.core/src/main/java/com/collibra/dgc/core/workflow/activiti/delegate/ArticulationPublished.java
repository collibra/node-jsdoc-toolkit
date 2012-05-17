package com.collibra.dgc.core.workflow.activiti.delegate;

import java.util.LinkedList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.util.CsvUtility;
import com.collibra.dgc.core.workflow.WorkflowConstants;

/**
 * 
 * @author amarnath
 * 
 */
public class ArticulationPublished extends AbstractAritculationDelegate {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Transactional
	public void execute(DelegateExecution execution) throws Exception {
		// Change the state
		changeState(execution);

		// Add all the parties involved for email notification.
		List<String> users = new LinkedList<String>();
		users.addAll(CsvUtility.getList((String) execution.getVariable(WorkflowConstants.VAR_LIST_DELEGATES)));
		users.addAll(CsvUtility.getList((String) execution.getVariable(WorkflowConstants.VAR_LIST_COMMUNITY_USERS)));

		// TODO PORT
		// List<String> emails = ComponentManagerUtil.getUserComponent().getEmailAddress(users);
		List<String> emails = new LinkedList<String>();
		execution.setVariable(WorkflowConstants.VAR_MESSAGE_TO, CsvUtility.getCSV(emails));
	}
}
