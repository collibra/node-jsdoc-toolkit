package com.collibra.dgc.core.workflow.activiti;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.DGCException;
import com.collibra.dgc.core.workflow.WorkflowEngine;

/**
 * {@link WorkflowEngine} implementation using Activiti engine.
 * @author amarnath
 * 
 */
@Service
public class WorkflowEngineImpl implements WorkflowEngine {
	private final Logger log = LoggerFactory.getLogger(WorkflowEngineImpl.class);

	@Autowired
	private ProcessEngine processEngine;

	protected String getCurrentUser() {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.getPrincipal() == null) {
			return Constants.GUEST_USER;
		}
		return currentUser.getPrincipal().toString();
	}

	@Override
	public ProcessEngine getInternalEngine() {
		return processEngine;
	}

	@Override
	public String deploy(ZipInputStream zipis) {
		try {
			return processEngine.getRepositoryService().createDeployment().addZipInputStream(zipis).deploy().getId();
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new DGCException(e, DGCErrorCodes.WF_DEPLOY_ERROR);
		}
	}

	@Override
	public void undeploy(String deploymentId) {
		// Cascade delete
		processEngine.getRepositoryService().deleteDeployment(deploymentId, true);
	}

	@Override
	public String findFormKeyForTask(String taskId) {
		return processEngine.getFormService().getTaskFormData(taskId).getFormKey();
	}

	@Override
	public String getTaskTargetResourceId(String taskId) {
		Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		String resourceId = (String) processEngine.getRuntimeService().getVariable(task.getExecutionId(), "term");
		return resourceId;
	}

	@Override
	public void completeTask(String taskId, Map<String, Object> variables) {
		Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		setAssigneeIfDoesnotExist(task, getCurrentUser());
		String processInstanceId = task.getExecutionId();
		processEngine.getRuntimeService().setVariables(processInstanceId, variables);
		processEngine.getTaskService().complete(taskId);
	}

	@Override
	public ProcessDefinition getProcessDefinitionByKey(String processDefKey) {
		return processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(processDefKey)
				.singleResult();
	}

	@Override
	public List<Deployment> getDeployments() {
		return processEngine.getRepositoryService().createDeploymentQuery().list();
	}

	@Override
	public String startProcessByKey(String key, Map<String, Object> variables) {
		// Set the current user as authenticated user for starting workflow.
		processEngine.getIdentityService().setAuthenticatedUserId(getCurrentUser());

		ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key, variables);
		return processInstance.getId();
	}

	@Override
	public String getProcessInstanceId(String key, String varName, String value) {
		List<ProcessInstance> instances = processEngine.getRuntimeService().createProcessInstanceQuery()
				.variableValueEquals(varName, value).list();
		for (ProcessInstance instance : instances) {
			if (instance != null && instance.getBusinessKey() != null && instance.getBusinessKey().equals(key)) {
				return instance.getId();
			}
		}
		return null;
	}

	public String getExecutionId(String taskId) {
		Task task = getTask(taskId);
		if (task == null) {
			return null;
		}

		return task.getExecutionId();
	}

	@Override
	public Object getVariable(String taskId, String varName) {
		String executionId = getExecutionId(taskId);
		if (executionId == null) {
			return null;
		}

		Object value = processEngine.getRuntimeService().getVariable(executionId, varName);
		if (value == null) {
			return null;
		}
		return value;
	}

	@Override
	public void setVariable(String taskId, String varName, Object value) {
		String executionId = getExecutionId(taskId);
		if (executionId == null) {
			return;
		}

		processEngine.getRuntimeService().setVariable(executionId, varName, value);
	}

	@Override
	public Map<String, Object> getVariables(String taskId, Collection<String> varNames) {
		String executionId = getExecutionId(taskId);
		if (executionId == null) {
			return new HashMap<String, Object>();
		}

		return processEngine.getRuntimeService().getVariablesLocal(executionId, varNames);
	}

	@Override
	public void setVariables(String taskId, Map<String, Object> variables) {
		String executionId = getExecutionId(taskId);
		if (executionId == null) {
			return;
		}

		processEngine.getRuntimeService().setVariables(executionId, variables);
	}

	public Object getVariableByExecutionId(String executionId, String varName) {
		return processEngine.getRuntimeService().getVariable(executionId, varName);
	}

	public void setVariableByExecutionId(String executionId, String varName, String value) {
		processEngine.getRuntimeService().setVariable(executionId, varName, value);
	}

	public Map<String, Object> getVariablesByExecutionId(String executionId, Collection<String> varNames) {
		return processEngine.getRuntimeService().getVariablesLocal(executionId, varNames);
	}

	public void setVariablesByExecutionId(String executionId, Map<String, String> variables) {
		processEngine.getRuntimeService().setVariables(executionId, variables);
	}

	@Override
	public Task getTask(String taskId) {
		return processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
	}

	@Override
	public void completeTask(String taskId) {
		setAssigneeIfDoesnotExist(taskId);
		processEngine.getTaskService().complete(taskId);
	}

	@Override
	public List<Task> getTasksForUser(String username) {
		List<Task> tasks = processEngine.getTaskService().createTaskQuery().taskCandidateUser(username).list();
		tasks.addAll(processEngine.getTaskService().createTaskQuery().taskAssignee(username).list());
		return tasks;
	}

	@Override
	public List<Task> getAllTasks() {
		return processEngine.getTaskService().createTaskQuery().list();
	}

	@Override
	public List<ProcessInstance> getAllProcessInstances() {
		return processEngine.getRuntimeService().createProcessInstanceQuery().list();
	}

	@Override
	public void stopProcessInstance(String processInstanceId, String reason) {
		// Set the user who deleted the tasks.
		for (Task task : processEngine.getTaskService().createTaskQuery().processInstanceId(processInstanceId).list()) {
			setAssigneeIfDoesnotExist(task, getCurrentUser());
		}

		processEngine.getRuntimeService().deleteProcessInstance(processInstanceId, reason);
	}

	@Override
	public ProcessDefinition getProcessDefinition(String businessProcessKey) {
		return processEngine.getRepositoryService().createProcessDefinitionQuery()
				.processDefinitionKey(businessProcessKey).singleResult();
	}

	@Override
	public ProcessDefinition getProcessDefinitionById(String processDefId) {
		return processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processDefId)
				.singleResult();
	}

	@Override
	public String getStartFormKey(String businessProcessKey) {
		ProcessDefinition processDefinition = getProcessDefinition(businessProcessKey);
		if (processDefinition != null) {
			StartFormData startFormData = processEngine.getFormService().getStartFormData(processDefinition.getId());
			if (startFormData != null && startFormData instanceof FormData) {
				return ((FormData) startFormData).getFormKey();
			}
		}

		return null;
	}

	@Override
	public String getFormKey(String taskId) {
		return processEngine.getFormService().getTaskFormData(taskId).getFormKey();
	}

	@Override
	public List<Task> getTasksForProcessInstance(String processInstanceId) {
		return processEngine.getTaskService().createTaskQuery().processInstanceId(processInstanceId).list();
	}

	@Override
	public List<Task> getTasks(Map<String, Object> variables) {
		return getTaskQuery(variables).list();
	}

	@Override
	public List<ProcessDefinition> getDeployedProcesses() {
		return processEngine.getRepositoryService().createProcessDefinitionQuery().list();
	}

	@Override
	public List<ProcessDefinition> getDeployedProcessesLike(String match) {
		if (match == null) {
			return new LinkedList<ProcessDefinition>();
		}

		// Build the SQL type match string.
		match = "%" + match + "%";
		return processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKeyLike(match)
				.list();
	}

	@Override
	public ProcessInstance getProcessInstance(String processInstanceId) {
		return processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
	}

	@Override
	public List<String> getTaskOwners(String taskId) {
		List<IdentityLink> identityLinks = processEngine.getTaskService().getIdentityLinksForTask(taskId);
		List<String> users = new LinkedList<String>();
		for (IdentityLink idLink : identityLinks) {
			String userId = idLink.getUserId();
			if (userId == null) {
				userId = idLink.getGroupId();
			}

			users.add(userId);
		}

		return users;
	}

	@Override
	public boolean removeTaskOwner(String taskId, String user) {
		Task task = getTask(taskId);
		if (task == null) {
			return false;
		}

		if (user != null && user.equals(task.getAssignee())) {
			processEngine.getTaskService().setAssignee(taskId, null);
		}

		processEngine.getTaskService().deleteCandidateUser(taskId, user);
		return true;
	}

	@Override
	public String getProcessKey(Task task) {
		ProcessDefinition procDefinition = getProcessDefinitionById(task.getProcessDefinitionId());
		if (procDefinition == null) {
			return null;
		}
		return procDefinition.getKey();
	}

	@Override
	public List<ProcessInstance> getProcessInstances(Map<String, Object> variables) {
		ProcessInstanceQuery query = processEngine.getRuntimeService().createProcessInstanceQuery();
		for (String key : variables.keySet()) {
			query.variableValueEquals(key, variables.get(key));
		}

		return query.list();
	}

	@Override
	public List<Task> getTasks(String user, Map<String, Object> variables) {
		// Get tasks for candidate user with variables.
		TaskQuery query = getTaskQuery(variables);
		query.taskCandidateUser(user);
		List<Task> tasks = query.list();

		// Get tasks for assignee with variables.
		query = getTaskQuery(variables);
		query.taskAssignee(user);
		tasks.addAll(query.list());
		return tasks;
	}

	private TaskQuery getTaskQuery(Map<String, Object> variables) {
		TaskQuery query = processEngine.getTaskService().createTaskQuery();

		for (String key : variables.keySet()) {
			query.processVariableValueEquals(key, variables.get(key));
		}

		return query;
	}

	private void setAssigneeIfDoesnotExist(String taskId) {
		Task task = getTask(taskId);
		if (task != null) {
			setAssigneeIfDoesnotExist(task, getCurrentUser());
		}
	}

	private void setAssigneeIfDoesnotExist(Task task, String user) {
		if (task.getAssignee() == null) {
			processEngine.getTaskService().claim(task.getId(), user);
		}
	}
}
