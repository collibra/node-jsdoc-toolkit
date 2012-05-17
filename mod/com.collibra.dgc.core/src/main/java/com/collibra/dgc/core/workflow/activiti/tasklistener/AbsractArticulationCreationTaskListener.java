package com.collibra.dgc.core.workflow.activiti.tasklistener;

import org.activiti.engine.delegate.DelegateTask;

import com.collibra.dgc.core.util.CsvUtility;

/**
 * 
 * @author amarnath
 * 
 */
abstract class AbsractArticulationCreationTaskListener {
	/**
	 * 
	 * @param delegateTask
	 * @param varName
	 */
	protected void addCandidateUsers(DelegateTask delegateTask, String varName) {
		String candidateusers = (String) delegateTask.getVariable(varName);
		for (String user : CsvUtility.getList(candidateusers)) {
			delegateTask.addCandidateUser(user);
		}
	}
}
