package com.collibra.dgc.core.workflow;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * Glossary workflow engine API.
 * @author amarnath
 * 
 */
public interface WorkflowEngine {
	/**
	 * Get activiti {@link ProcessEngine}.
	 * @return
	 */
	ProcessEngine getInternalEngine();

	String findFormKeyForTask(String taskId);

	String getTaskTargetResourceId(String taskId);

	void completeTask(String taskId, Map<String, Object> variables);

	String deploy(ZipInputStream zipis);

	void undeploy(String deploymentId);

	ProcessDefinition getProcessDefinitionByKey(String processDefKey);

	List<Deployment> getDeployments();

	String startProcessByKey(String key, Map<String, Object> variables);

	String getProcessInstanceId(String key, String varName, String value);

	Object getVariable(String taskId, String varName);

	void setVariable(String taskId, String varName, Object value);

	Map<String, Object> getVariables(String taskId, Collection<String> varNames);

	void setVariables(String taskId, Map<String, Object> variables);

	Task getTask(String taskId);

	void completeTask(String taskId);

	List<Task> getTasksForUser(String username);

	List<Task> getAllTasks();

	List<ProcessInstance> getAllProcessInstances();

	void stopProcessInstance(String processInstanceId, String reason);

	List<Task> getTasks(Map<String, Object> variables);

	List<Task> getTasks(String user, Map<String, Object> variables);

	ProcessDefinition getProcessDefinition(String businessProcessKey);

	String getStartFormKey(String businessProcessKey);

	String getFormKey(String taskId);

	List<Task> getTasksForProcessInstance(String processInstanceId);

	List<ProcessDefinition> getDeployedProcesses();

	List<ProcessDefinition> getDeployedProcessesLike(String match);

	ProcessInstance getProcessInstance(String processInstanceId);

	ProcessDefinition getProcessDefinitionById(String processDefId);

	List<String> getTaskOwners(String taskId);

	String getProcessKey(Task task);

	List<ProcessInstance> getProcessInstances(Map<String, Object> variables);

	boolean removeTaskOwner(String taskId, String user);
}
