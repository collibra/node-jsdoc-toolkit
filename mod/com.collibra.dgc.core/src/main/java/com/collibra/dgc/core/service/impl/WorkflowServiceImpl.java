package com.collibra.dgc.core.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.security.authorization.AuthorizationHelper;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.WorkflowService;
import com.collibra.dgc.core.workflow.WorkflowConfigurationManager;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.WorkflowEngine;
import com.collibra.dgc.core.workflow.WorkflowHistory;
import com.collibra.dgc.core.workflow.exception.WorkflowErrorCodes;
import com.collibra.dgc.core.workflow.exception.WorkflowException;

/**
 * Provides implementation for workflow APIs.
 * 
 * @author amarnath
 * 
 */
@Service
public class WorkflowServiceImpl extends AbstractService implements WorkflowService {
	private static final Logger log = LoggerFactory.getLogger(WorkflowServiceImpl.class);

	private static Properties authorizationKeys = new Properties();

	// Permission suffixes.
	private static final String START_SUFFIX = "_START";
	private static final String STOP_SUFFIX = "_STOP";

	static {
		try {
			authorizationKeys
					.load(WorkflowServiceImpl.class
							.getResourceAsStream("/com/collibra/dgc/core/authorization/workflowkey2authorizationkey.properties"));
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	@Autowired
	private AuthorizationHelper authorizationHelper;

	@Autowired
	private WorkflowEngine engine;

	@Autowired
	private WorkflowHistory history;

	@Autowired
	private WorkflowConfigurationManager configManager;

	@Override
	public WorkflowEngine getEngine() {
		return engine;
	}

	@Override
	public String deploy(ZipInputStream zipis) {
		authorizationHelper.checkAuthorization(getCurrentUser(), Permissions.WF_DEPLOY,
				DGCErrorCodes.GLOBAL_NO_PERMISSION);
		return engine.deploy(zipis);
	}

	@Override
	public void undeploy(String deploymentId) {
		authorizationHelper.checkAuthorization(getCurrentUser(), Permissions.WF_UNDEPLOY,
				DGCErrorCodes.GLOBAL_NO_PERMISSION);
		engine.undeploy(deploymentId);
	}

	@Override
	public String getProcessDefinitionIdByKey(String processDefKey) {
		ProcessDefinition def = engine.getProcessDefinitionByKey(processDefKey);
		if (def != null) {
			return def.getId();
		}

		return null;
	}

	@Override
	public ProcessDefinition getProcessDefinitionById(String processDefId) {
		return engine.getProcessDefinitionById(processDefId);
	}

	@Override
	public List<String> getDeploymentIds() {
		List<String> deployments = new ArrayList<String>();
		for (Deployment deployment : engine.getDeployments()) {
			deployments.add(deployment.getId());
		}

		return deployments;
	}

	@Override
	public String startProcessByKey(String key, Map<String, Object> variables) {
		if (variables == null) {
			variables = new HashMap<String, Object>();
		}

		authorizationHelper.checkAuthorizationAtleastForOne(getCurrentUser(), new String[] {
				getRightForStartingProcess(key), Permissions.WF_PROCESS_START },
				DGCErrorCodes.START_WORKFLOW_NO_PERMISSION);

		String resourceId = (String) variables.get(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID);
		if (resourceId != null) {
			configManager.checkStartConstraints(key, resourceId);
		}

		return engine.startProcessByKey(key, variables);
	}

	@Override
	public String startProcessByKey(String key, Representation representation, Map<String, Object> variables) {

		authorizationHelper.checkAuthorizationAtleastForOne(getCurrentUser(), new String[] {
				getRightForStartingProcess(key), Permissions.WF_PROCESS_START },
				DGCErrorCodes.START_WORKFLOW_NO_PERMISSION);

		configManager.checkStartConstraints(key, representation.getId());

		if (variables == null) {
			variables = new HashMap<String, Object>();
		}

		// Ensure the resource id always present in case does not exist.
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, representation.getId().toString());

		return engine.startProcessByKey(key, variables);
	}

	@Override
	public String startProcessByKey(String key, Vocabulary vocabulary, Map<String, Object> variables) {

		authorizationHelper.checkAuthorizationAtleastForOne(getCurrentUser(), vocabulary, new String[] {
				getRightForStartingProcess(key), Permissions.WF_PROCESS_START },
				DGCErrorCodes.START_WORKFLOW_NO_PERMISSION);

		configManager.checkStartConstraints(key, vocabulary.getId());

		if (variables == null) {
			variables = new HashMap<String, Object>();
		}

		return engine.startProcessByKey(key, variables);
	}

	@Override
	public String startProcessByKey(String key, Community community, Map<String, Object> variables) {
		authorizationHelper.checkAuthorizationAtleastForOne(getCurrentUser(), community, new String[] {
				getRightForStartingProcess(key), Permissions.WF_PROCESS_START },
				DGCErrorCodes.START_WORKFLOW_NO_PERMISSION);

		configManager.checkStartConstraints(key, community.getId());

		if (variables == null) {
			variables = new HashMap<String, Object>();
		}

		return engine.startProcessByKey(key, variables);
	}

	@Override
	public String startProcessByKey(String key, Resource resource, Map<String, Object> variables) {
		authorizationHelper.checkAuthorizationAtleastForOne(getCurrentUser(), resource, new String[] {
				getRightForStartingProcess(key), Permissions.WF_PROCESS_START },
				DGCErrorCodes.START_WORKFLOW_NO_PERMISSION);

		configManager.checkStartConstraints(key, resource.getId());

		if (variables == null) {
			variables = new HashMap<String, Object>();
		}

		return engine.startProcessByKey(key, variables);
	}

	@Override
	public void completeTask(String taskId) {
		try {
			engine.completeTask(taskId);
		} catch (Exception e) {
			if (e instanceof ActivitiException) {
				if (e.getCause() != null && e.getCause().getCause() instanceof WorkflowException) {
					throw ((WorkflowException) e.getCause().getCause());
				}
				throw (ActivitiException) e;
			} else if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
		}
	}

	@Override
	public Object getVariable(String taskId, String varName) {
		return engine.getVariable(taskId, varName);
	}

	@Override
	public void setVariable(String taskId, String varName, Object value) {
		engine.setVariable(taskId, varName, value);
	}

	@Override
	public List<Task> findTasksForUser(String username) {
		return engine.getTasksForUser(username);
	}

	@Override
	public List<Task> findTasksForUser(String username, Map<String, Object> variables) {
		return engine.getTasks(username, variables);
	}

	@Override
	public List<Task> findTasksForUser(String username, String resourceId) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, resourceId.toString());
		return findTasksForUser(username, variables);
	}

	@Override
	public List<Task> findTasksForCurrentUser() {
		return engine.getTasksForUser(getCurrentUser());
	}

	@Override
	public List<Task> findTasksForCurrentUser(Map<String, Object> variables) {
		return engine.getTasks(getCurrentUser(), variables);
	}

	@Override
	public List<Task> findTasksForCurrentUser(String resourceId) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, resourceId.toString());
		return findTasksForCurrentUser(variables);
	}

	@Override
	public List<Task> findTasks(Map<String, Object> variables) {
		return engine.getTasks(variables);
	}

	@Override
	public List<Task> getAllTasks() {
		return engine.getAllTasks();
	}

	@Override
	public List<ProcessInstance> getAllProcessInstances() {
		return engine.getAllProcessInstances();
	}

	@Override
	public void stopProcessInstance(String processInstanceId, String reason) {
		authorizationHelper.checkAuthorizationAtleastForOne(getCurrentUser(), new String[] {
				getRightForStoppingProcessInstance(processInstanceId), Permissions.WF_PROCESS_STOP },
				DGCErrorCodes.STOP_WORKFLOW_NO_PERMISSION);

		engine.stopProcessInstance(processInstanceId, reason);
	}

	@Override
	public void stopProcessInstance(String processInstanceId, Representation representation, String reason) {
		authorizationHelper.checkAuthorizationAtleastForOne(getCurrentUser(), representation, new String[] {
				getRightForStoppingProcessInstance(processInstanceId), Permissions.WF_PROCESS_STOP },
				DGCErrorCodes.STOP_WORKFLOW_NO_PERMISSION);

		engine.stopProcessInstance(processInstanceId, reason);
	}

	@Override
	public void stopProcessInstance(String processInstanceId, Vocabulary vocabulary, String reason) {
		authorizationHelper.checkAuthorizationAtleastForOne(getCurrentUser(), vocabulary, new String[] {
				getRightForStoppingProcessInstance(processInstanceId), Permissions.WF_PROCESS_STOP },
				DGCErrorCodes.STOP_WORKFLOW_NO_PERMISSION);

		engine.stopProcessInstance(processInstanceId, reason);
	}

	@Override
	public void stopProcessInstance(String processInstanceId, Community community, String reason) {
		authorizationHelper.checkAuthorizationAtleastForOne(getCurrentUser(), community, new String[] {
				getRightForStoppingProcessInstance(processInstanceId), Permissions.WF_PROCESS_STOP },
				DGCErrorCodes.STOP_WORKFLOW_NO_PERMISSION);

		engine.stopProcessInstance(processInstanceId, reason);
	}

	@Override
	public void stopProcessInstance(String processInstanceId, Resource resource, String reason) {
		authorizationHelper.checkAuthorizationAtleastForOne(getCurrentUser(), resource, new String[] {
				getRightForStoppingProcessInstance(processInstanceId), Permissions.WF_PROCESS_STOP },
				DGCErrorCodes.STOP_WORKFLOW_NO_PERMISSION);

		engine.stopProcessInstance(processInstanceId, reason);
	}

	@Override
	public ProcessDefinition getProcessDefinition(String businessProcessKey) {
		return engine.getProcessDefinition(businessProcessKey);
	}

	@Override
	public String getStartFormKey(String businessProcessKey) {
		return engine.getStartFormKey(businessProcessKey);
	}

	@Override
	public String getFormKey(String taskId) {
		return engine.getFormKey(taskId);
	}

	@Override
	public List<ProcessDefinition> getDeployedProcesses() {
		return engine.getDeployedProcesses();
	}

	@Override
	public String getRightForStartingProcess(String processKey) {
		String processAuthorizationKey = authorizationKeys.getProperty(processKey);
		if (processAuthorizationKey == null) {
			return null;
		}

		return processAuthorizationKey + START_SUFFIX;
	}

	@Override
	public String getRightForStoppingProcess(String processKey) {

		String processAuthorizationKey = authorizationKeys.getProperty(processKey);
		if (processAuthorizationKey == null) {
			return null;
		}

		return processAuthorizationKey + STOP_SUFFIX;
	}

	@Override
	public String getRightForStoppingProcessInstance(String processInstanceId) {
		ProcessInstance instance = engine.getProcessInstance(processInstanceId);
		if (instance == null) {
			throw new WorkflowException(processInstanceId, "No process running with instance id: " + processInstanceId,
					WorkflowErrorCodes.PROCESS_INSTANCE_NOT_FOUND);
		}

		ProcessDefinition procDef = engine.getProcessDefinitionById(instance.getProcessDefinitionId());

		return authorizationKeys.containsKey(procDef.getKey()) ? authorizationKeys.getProperty(procDef.getKey())
				+ STOP_SUFFIX : null;
	}

	@Override
	public Collection<String> getTaskOwners(String taskId) {
		return engine.getTaskOwners(taskId);
	}

	@Override
	public boolean removeTaskOwner(String taskId, String user) {
		return engine.removeTaskOwner(taskId, user);
	}

	@Override
	public String findProcessKey(Task task) {
		return engine.getProcessKey(task);
	}

	@Override
	public String findProcessKey(String taskId) {
		Task task = engine.getTask(taskId);
		if (task == null) {
			return null;
		}

		return engine.getProcessKey(task);
	}

	@Override
	public Task findTask(String taskId) {
		return engine.getTask(taskId);
	}

	@Override
	public void removeTask(String taskId, String reason) {
		Task task = engine.getTask(taskId);
		if (task == null) {
			throw new WorkflowException("Task with id '" + taskId + "' not found", DGCErrorCodes.WF_TASK_NOT_FOUND);
		}

		engine.stopProcessInstance(task.getProcessInstanceId(), reason);
	}

	@Override
	public Collection<ProcessInstance> findProcessInstances(Map<String, Object> variables) {
		return engine.getProcessInstances(variables);
	}

	@Override
	public String createTask(String assignee, String title, String status, String resolution, String date,
			String severity, String description) {
		Map<String, Object> variables = new HashMap<String, Object>();
		return createTask(assignee, title, status, resolution, date, severity, description, variables);
	}

	@Override
	public String createTask(Representation representation, String assignee, String title, String status,
			String resolution, String date, String severity, String description) {

		authorizationHelper.checkAuthorization(getCurrentUser(), representation, Permissions.WF_GENERIC_TASKS_START,
				DGCErrorCodes.START_WORKFLOW_NO_PERMISSION);

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, representation.getId().toString());
		return createTask(assignee, title, status, resolution, date, severity, description, variables);
	}

	private String createTask(String assignee, String title, String status, String resolution, String date,
			String severity, String description, Map<String, Object> variables) {
		variables.put(WorkflowConstants.VAR_ASSIGNEE, assignee);
		variables.put(WorkflowConstants.VAR_TASK_TITLE, title);
		variables.put(WorkflowConstants.VAR_TASK_STATUS, status);
		variables.put(WorkflowConstants.VAR_TASK_RESOLUTION, resolution);
		variables.put(WorkflowConstants.VAR_TASK_DUE_DATE, date);
		variables.put(WorkflowConstants.VAR_TASK_SEVERITY, severity);
		variables.put(WorkflowConstants.VAR_TASK_DESCRIPTION, description);
		variables.put(WorkflowConstants.VAR_TASK_REPORTER, getCurrentUser());
		variables.put(WorkflowConstants.VAR_TASK_CLOSED, false);
		String processInstanceId = startProcessByKey(WorkflowConstants.TASK_MANAGEMENT_PROCESS_KEY, variables);
		return engine.getTasksForProcessInstance(processInstanceId).get(0).getId();
	}

	@Override
	public Collection<HistoricTaskInstance> findCompletedTasks(int start, int count) {
		return history.getCompletedTasks(start, count);
	}

	@Override
	public Collection<HistoricTaskInstance> findDeletedTasks(int start, int count) {
		return history.getDeletedTasks(start, count);
	}

	@Override
	public Collection<HistoricTaskInstance> findHistoricUnfinishedTasks(int start, int count) {
		return history.getUnfinishedTasks(start, count);
	}

	@Override
	public Collection<HistoricProcessInstance> findCompletedProcesses(int start, int count) {
		return history.getCompletedProcesses(start, count);
	}

	@Override
	public Collection<HistoricProcessInstance> findHistoricUnfinishedProcesses(int start, int count) {
		return history.getUnfinishedProcesses(start, count);
	}
}
