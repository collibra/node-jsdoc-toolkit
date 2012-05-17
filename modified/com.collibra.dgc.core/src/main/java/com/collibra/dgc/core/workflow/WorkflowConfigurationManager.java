package com.collibra.dgc.core.workflow;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.service.WorkflowService;
import com.collibra.dgc.core.workflow.exception.WorkflowException;

/**
 * Workflow configuration manager provides APIs to manage configuration information related to workflow processes.
 * @author amarnath
 * 
 */
@Service
public class WorkflowConfigurationManager {
	private static final Logger log = LoggerFactory.getLogger(WorkflowConfigurationManager.class);
	private static Properties props;

	@Autowired
	private WorkflowService workflowService;

	/**
	 * @return Process keys configured for exclusive access on resource.
	 */
	public List<String> getExclusiveProcesses() {
		// FIXME PORT
		return new LinkedList<String>();
	}

	/**
	 * @return Process keys configured as mutually exclusive access on resource.
	 */
	public List<String> getMutuallyExclusiveProcesses() {
		// FIXME PORT
		return new LinkedList<String>();
	}

	/**
	 * @return True if the process with specified key is configured to have exclusive access on resource. Otherwise
	 *         false.
	 */
	public boolean isExclusive(String key) {
		return getExclusiveProcesses().contains(key);
	}

	/**
	 * @return True if the specified processes are mutually exclusive, otherwise false.
	 */
	public boolean isMutuallyExclusive(String processKey1, String processKey2) {
		List<String> processes = getMutuallyExclusiveProcesses();
		return (processes.contains(processKey1) && processes.contains(processKey2));
	}

	/**
	 * Check constraints if specified process can be started on the resource with specified id.
	 * @param processKey The process to be started.
	 * @param resourceId The {@link Resource} id.
	 * @throws WorkflowException if the exclusive or mutually exclusive constraint is violated.
	 */
	public void checkStartConstraints(String processKey, String resourceId) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, resourceId.toString());
		Collection<ProcessInstance> processInstances = workflowService.findProcessInstances(variables);

		List<String> exclusiveProcesses = getExclusiveProcesses();

		boolean cannotStart = false;
		String message = "";
		String key = "";
		if (exclusiveProcesses.contains(processKey) && processInstances.size() > 0) {
			message = "Failed to start exclusive process '" + processKey
					+ "' as other process instances are using the resource";
			key = DGCErrorCodes.WF_CANNOT_START_EXCLUSIVE_PROCESS;
			cannotStart = true;
		}

		if (isExclusiveProcessInstance(processInstances, exclusiveProcesses)) {
			message = "Failed to start process '" + processKey
					+ "' as another exclusive process instance(s) is using the resource";
			key = DGCErrorCodes.WF_CANNOT_START_ANOTHER_EXCLUSIVE_PROCESS_RUNNING;
			cannotStart = true;
		}

		if (cannotStart) {
			log.error(message);
			throw new WorkflowException(message, key);
		}

		List<String> mutuallyExclusiveProcesses = getMutuallyExclusiveProcesses();
		if (mutuallyExclusiveProcesses.contains(processKey)) {
			for (ProcessInstance instance : processInstances) {
				ProcessDefinition procDef = workflowService.getProcessDefinitionById(instance.getProcessDefinitionId());
				if (mutuallyExclusiveProcesses.contains(procDef.getKey())) {
					message = "Failed to start process '" + processKey + "' as mutually exclusive process '"
							+ instance.getProcessDefinitionId() + "' is using the resource";
					log.error(message);
					throw new WorkflowException(message, DGCErrorCodes.WF_CANNOT_START_MUTUALLY_EXCLUSIVE_PROCESS);
				}
			}
		}
	}

	private boolean isExclusiveProcessInstance(Collection<ProcessInstance> processInstances,
			List<String> exclusiveProcesses) {
		for (ProcessInstance instance : processInstances) {
			ProcessDefinition procDef = workflowService.getProcessDefinitionById(instance.getProcessDefinitionId());
			if (exclusiveProcesses.contains(procDef.getKey())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Used for testing if XWiki is not available to get the configuration.
	 * @return The {@link Properties}.
	 */
	private synchronized static Properties getInternalProperties() {
		if (props == null) {
			props = new Properties();
			try {
				props.load(WorkflowConfigurationManager.class.getResourceAsStream("/workflow/config.properties"));
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}

		return props;
	}
}
