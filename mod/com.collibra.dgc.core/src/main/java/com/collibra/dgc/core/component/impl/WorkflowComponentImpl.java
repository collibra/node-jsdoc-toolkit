package com.collibra.dgc.core.component.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.WorkflowComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.service.CommunityService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.WorkflowService;
import com.collibra.dgc.core.util.Defense;

/**
 * Workflow API implementation.
 * @author amarnath
 * 
 */
@Service
public class WorkflowComponentImpl implements WorkflowComponent {
	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private RepresentationService representationService;

	@Autowired
	private CommunityService communityService;

	@Override
	@Transactional
	public boolean completeTask(String taskId) {
		workflowService.completeTask(taskId);
		return true;
	}

	@Override
	@Transactional
	public String createTask(String assignee, String title, String status, String resolution, String date,
			String severity, String description) {
		Defense.notEmpty(assignee, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "assignee");

		return workflowService.createTask(assignee, title, status, resolution, date, severity, description);
	}

	@Override
	@Transactional
	public String createTask(String resourceId, String assignee, String title, String status, String resolution,
			String date, String severity, String description) {
		return workflowService.createTask(representationService.getRepresentationWithError(resourceId), assignee,
				title, status, resolution, date, severity, description);
	}

	@Override
	@Transactional
	public String deploy(ZipInputStream zipis) {
		Defense.notNull(zipis, DGCErrorCodes.FILE_INPUT_STREAM_NULL);
		return workflowService.deploy(zipis);
	}

	@Override
	@Transactional
	public boolean undeploy(String deploymentId) {
		workflowService.undeploy(deploymentId);
		return false;
	}

	@Override
	@Transactional
	public Collection<HistoricProcessInstance> findCompletedProcesses(int start, int count) {
		return workflowService.findCompletedProcesses(start, count);
	}

	@Override
	@Transactional
	public Collection<HistoricTaskInstance> findCompletedTasks(int start, int count) {
		return workflowService.findCompletedTasks(start, count);
	}

	@Override
	@Transactional
	public Collection<HistoricTaskInstance> findDeletedTasks(int start, int count) {
		return workflowService.findDeletedTasks(start, count);
	}

	@Override
	@Transactional
	public Collection<HistoricProcessInstance> findHistoricUnfinishedProcesses(int start, int count) {
		return workflowService.findHistoricUnfinishedProcesses(start, count);
	}

	@Override
	@Transactional
	public Collection<HistoricTaskInstance> findHistoricUnfinishedTasks(int start, int count) {
		return workflowService.findHistoricUnfinishedTasks(start, count);
	}

	@Override
	@Transactional
	public Collection<Task> findTasks(Map<String, Object> variables) {
		return workflowService.findTasks(variables);
	}

	@Override
	@Transactional
	public Collection<Task> findTasksForCurrentUser() {
		return workflowService.findTasksForCurrentUser();
	}

	@Override
	@Transactional
	public Collection<Task> findTasksForCurrentUser(Map<String, Object> variables) {
		return workflowService.findTasksForCurrentUser(variables);
	}

	@Override
	@Transactional
	public Collection<Task> findTasksForCurrentUser(String resourceId) {
		if (resourceId == null) {
			return new LinkedList<Task>();
		}
		return workflowService.findTasksForCurrentUser(resourceId);
	}

	@Override
	@Transactional
	public Collection<Task> findTasksForUser(String username) {
		return workflowService.findTasksForUser(username);
	}

	@Override
	@Transactional
	public Collection<Task> findTasksForUser(String username, Map<String, Object> variables) {
		return workflowService.findTasksForUser(username, variables);
	}

	@Override
	@Transactional
	public Collection<Task> findTasksForUser(String username, String resourceId) {
		if (resourceId == null) {
			return new LinkedList<Task>();
		}
		return workflowService.findTasksForUser(username, resourceId);
	}

	@Override
	@Transactional
	public Collection<ProcessInstance> getAllProcessInstances() {
		return workflowService.getAllProcessInstances();
	}

	@Override
	@Transactional
	public Collection<Task> getAllTasks() {
		return workflowService.getAllTasks();
	}

	@Override
	@Transactional
	public Collection<ProcessDefinition> getDeployedProcesses() {
		return workflowService.getDeployedProcesses();
	}

	@Override
	@Transactional
	public Collection<String> getDeploymentIds() {
		return workflowService.getDeploymentIds();
	}

	@Override
	@Transactional
	public String getFormKey(String taskId) {
		return workflowService.getFormKey(taskId);
	}

	@Override
	@Transactional
	public ProcessDefinition getProcessDefinition(String businessProcessKey) {
		return workflowService.getProcessDefinition(businessProcessKey);
	}

	@Override
	@Transactional
	public ProcessDefinition getProcessDefinitionById(String processDefId) {
		return workflowService.getProcessDefinitionById(processDefId);
	}

	@Override
	@Transactional
	public String getProcessKey(String taskId) {
		return workflowService.findProcessKey(taskId);
	}

	@Override
	@Transactional
	public String getRightForStartingProcess(String processKey) {
		return workflowService.getRightForStartingProcess(processKey);
	}

	@Override
	@Transactional
	public String getRightForStoppingProcess(String processKey) {
		return workflowService.getRightForStoppingProcess(processKey);
	}

	@Override
	@Transactional
	public String getRightForStoppingProcessInstance(String processInstanceId) {
		return workflowService.getRightForStoppingProcessInstance(processInstanceId);
	}

	@Override
	@Transactional
	public String getStartFormKey(String businessProcessKey) {
		return workflowService.getStartFormKey(businessProcessKey);
	}

	@Override
	@Transactional
	public Task getTask(String taskId) {
		return workflowService.findTask(taskId);
	}

	@Override
	@Transactional
	public Collection<String> getTaskOwners(String taskId) {
		return workflowService.getTaskOwners(taskId);
	}

	@Override
	@Transactional
	public Object getVariable(String taskId, String varName) {
		return workflowService.getVariable(taskId, varName);
	}

	@Override
	@Transactional
	public boolean removeTask(String taskId, String reason) {
		workflowService.removeTask(taskId, reason);
		return true;
	}

	@Override
	@Transactional
	public void setVariable(String taskId, String varName, Object value) {
		workflowService.setVariable(taskId, varName, value);
	}

	@Override
	@Transactional
	public String startProcess(String key, Map<String, Object> variables) {
		return workflowService.startProcessByKey(key, variables);
	}

	@Override
	@Transactional
	public String startProcessForRepresentation(String key, String resourceId) {
		return workflowService.startProcessByKey(key, representationService.getRepresentationWithError(resourceId),
				new HashMap<String, Object>());
	}

	@Override
	@Transactional
	public String startProcessForRepresentation(String key, String resourceId, Map<String, Object> variables) {
		return workflowService.startProcessByKey(key, representationService.getRepresentationWithError(resourceId),
				variables);
	}

	@Override
	@Transactional
	public String startProcessForCommunity(String key, String communityResourceId, Map<String, Object> variables) {
		return workflowService.startProcessByKey(key, communityService.getCommunityWithError(communityResourceId),
				variables);
	}

	@Override
	@Transactional
	public String startProcessForVocabulary(String key, String vocabularyResourceId, Map<String, Object> variables) {
		return workflowService.startProcessByKey(key,
				representationService.getVocabularyWithError(vocabularyResourceId), variables);
	}

	@Override
	@Transactional
	public boolean stopProcessInstance(String processInstanceId, String reason) {
		workflowService.stopProcessInstance(processInstanceId, reason);
		return true;
	}

	@Override
	@Transactional
	public boolean stopProcessInstanceForRrepresentation(String processInstanceId, String resourceId, String reason) {
		workflowService.stopProcessInstance(processInstanceId,
				representationService.getRepresentationWithError(resourceId), reason);
		return true;
	}

	@Override
	@Transactional
	public boolean stopProcessInstanceForCommunity(String processInstanceId, String communityResourceId, String reason) {
		workflowService.stopProcessInstance(processInstanceId,
				communityService.getCommunityWithError(communityResourceId), reason);
		return true;
	}

	@Override
	@Transactional
	public boolean stopProcessInstanceForVocabulary(String processInstanceId, String vocabularyResourceId, String reason) {
		workflowService.stopProcessInstance(processInstanceId,
				representationService.getVocabularyWithError(vocabularyResourceId), reason);
		return true;
	}

}
