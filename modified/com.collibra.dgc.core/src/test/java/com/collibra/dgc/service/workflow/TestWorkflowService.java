package com.collibra.dgc.service.workflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.service.WorkflowService;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.WorkflowProcesses;
import com.collibra.dgc.service.workflow.delegates.GlossaryTaskDelegate;

/**
 * {@link WorkflowService} tests.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestWorkflowService extends AbstractWorkflowServiceTests {
	@Test
	public void testTransactionIntegration() {
		// Deploy the file with work flows.
		String deploymentId = deploy("com/collibra/dgc/test/workflow/ActivitiTransactionIntegrationTest.bpmn20.xml");
		Assert.assertNotNull(deploymentId);
		try {
			workflowService.startProcessByKey("TransactionIntegration", new HashMap<String, Object>());
			fail();
		} catch (Exception ex) {
			// Success
			rollback();
		}

		resetTransaction();

		Community community = communityService
				.findCommunityByUri(GlossaryTaskDelegate.GLOSSARY_ACTIVITI_INTEGRATION_TEST_COMMUNITY_URI);
		Assert.assertNull(community);
		Assert.assertEquals(0, workflowService.getAllProcessInstances().size());

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testDeployment() {
		// Deploy the zip file with work flows.
		String deploymentId = workflowService.deploy(new ZipInputStream(getClass().getResourceAsStream(
				"/com/collibra/dgc/workflow/workflow.zip")));
		Assert.assertNotNull(deploymentId);
		resetTransaction();

		// Assert for process definitions
		String processDefId = workflowService.getProcessDefinitionIdByKey("Articulation_Process");
		Assert.assertNotNull(processDefId);

		processDefId = workflowService.getProcessDefinitionIdByKey("Decommissioning");
		Assert.assertNotNull(processDefId);

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	/**
	 * Testing engine API.
	 */
	@Test
	public void testEngine() {
		String deploymentId = deploy("com/collibra/dgc/test/workflow/ApprovalProcess.bpmn20.xml");
		resetTransaction();
		ProcessDefinition def = workflowEngine.getInternalEngine().getRepositoryService()
				.createProcessDefinitionQuery().processDefinitionKey("Approval_Process").singleResult();

		Assert.assertNotNull(def);
		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testGetProcessDefinitionById() {
		String deploymentId = workflowService.deploy(new ZipInputStream(getClass().getResourceAsStream(
				"/com/collibra/dgc/workflow/workflow.zip")));
		for (ProcessDefinition processDef : workflowService.getDeployedProcesses()) {
			ProcessDefinition procDefFromQuery = workflowService.getProcessDefinitionById(processDef.getId());
			Assert.assertNotNull(procDefFromQuery);
			Assert.assertEquals(processDef.getDeploymentId(), procDefFromQuery.getDeploymentId());
			Assert.assertEquals(processDef.getKey(), procDefFromQuery.getKey());
			Assert.assertEquals(processDef.getResourceName(), procDefFromQuery.getResourceName());
			Assert.assertEquals(processDef.getVersion(), procDefFromQuery.getVersion());
		}

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testTaskManagementWorkflow() {
		// Deploy
		String deploymentId = deploy("com/collibra/dgc/test/workflow/TaskManagement.bpmn20.xml");
		Assert.assertNotNull(deploymentId);

		ProcessEngine engine = workflowService.getEngine().getInternalEngine();
		ProcessDefinition processDef = engine.getRepositoryService().createProcessDefinitionQuery()
				.processDefinitionKey(WorkflowConstants.TASK_MANAGEMENT_PROCESS_KEY).list().get(0);
		Assert.assertNotNull(processDef);
		Assert.assertTrue(processDef.hasStartFormKey());

		String processInstanceId = workflowService.createTask(DELEGATEE, "Testing Task Management in Activiti", "New",
				"None", new Date().toString(), "Blocker", "Unit test");
		Assert.assertNotNull(processInstanceId);

		List<Task> tasks = workflowService.findTasksForUser(DELEGATEE);
		Assert.assertEquals(1, tasks.size());

		// Complete the task by changing status and assign to approver
		String taskId = tasks.get(0).getId();
		workflowService.setVariable(taskId, WorkflowConstants.VAR_TASK_RESOLUTION, "Fixed");
		workflowService.setVariable(taskId, WorkflowConstants.VAR_TASK_STATUS, "Resolved");
		workflowService.setVariable(taskId, WorkflowConstants.VAR_ASSIGNEE, APPROVER);
		workflowService.completeTask(taskId);

		resetTransaction();

		// Now approver has to verify the task.
		tasks = workflowService.findTasksForUser(APPROVER);
		Assert.assertEquals(1, tasks.size());

		taskId = tasks.get(0).getId();
		workflowService.setVariable(taskId, WorkflowConstants.VAR_TASK_STATUS, "Verified");
		workflowService.completeTask(taskId);

		resetTransaction();

		// Now approver has to close the process instance of the task.
		tasks = workflowService.findTasksForUser(APPROVER);
		Assert.assertEquals(1, tasks.size());

		taskId = tasks.get(0).getId();
		workflowService.setVariable(taskId, WorkflowConstants.VAR_TASK_STATUS, "Done");
		workflowService.setVariable(taskId, WorkflowConstants.VAR_TASK_CLOSED, true);
		workflowService.completeTask(taskId);

		resetTransaction();

		// No tasks for approver and delegatee now
		tasks = workflowService.findTasksForUser(APPROVER);
		Assert.assertEquals(0, tasks.size());
		tasks = workflowService.findTasksForUser(DELEGATEE);
		Assert.assertEquals(0, tasks.size());

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testFormKeys() {
		String deploymentId = deploy("com/collibra/dgc/test/workflow/TestFormKeys.bpmn20.xml");

		// Process with start form key.
		ProcessDefinition processDefinition = workflowService.getProcessDefinition("ProcessWithStartFormKey");
		Assert.assertNotNull(processDefinition);
		Assert.assertEquals("ProcessWithStartFormKey", processDefinition.getKey());

		String startFormKey = workflowService.getStartFormKey("ProcessWithStartFormKey");
		Assert.assertEquals("startformkey.form", startFormKey);

		String processInstanceId = workflowService.startProcessByKey("ProcessWithStartFormKey",
				new HashMap<String, Object>());
		Assert.assertNotNull(processInstanceId);

		List<Task> tasks = workflowService.findTasksForUser(USER_1);
		Assert.assertEquals(1, tasks.size());

		String formKey = workflowService.getFormKey(tasks.get(0).getId());
		Assert.assertNull(formKey);

		// Process with a user task with form key
		startFormKey = workflowService.getStartFormKey("ProcessWithFormKey");
		Assert.assertNull(startFormKey);

		processInstanceId = workflowService.startProcessByKey("ProcessWithFormKey", new HashMap<String, Object>());
		Assert.assertNotNull(processInstanceId);

		tasks = workflowService.findTasksForUser(USER_2);
		Assert.assertEquals(1, tasks.size());

		formKey = workflowService.getFormKey(tasks.get(0).getId());
		Assert.assertEquals("usertask.form", formKey);

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testGetDeployedProcesses() {
		String deploymentId1 = deploy("com/collibra/dgc/test/workflow/TaskManagement.bpmn20.xml");
		String deploymentId2 = deploy("com/collibra/dgc/test/workflow/TermsDecommissioning.bpmn20.xml");
		List<ProcessDefinition> procDefs = workflowService.getDeployedProcesses();
		assertEquals(2, procDefs.size());

		String[] processKeys = { WorkflowProcesses.DECOMMISSIONING, WorkflowConstants.TASK_MANAGEMENT_PROCESS_KEY };
		for (String key : processKeys) {
			boolean found = false;
			for (ProcessDefinition def : procDefs) {
				if (def.getKey().equals(key)) {
					found = true;
				}
			}

			if (!found) {
				fail("Failed to find process definition '" + key + "'");
			}
		}

		// Undeploy
		workflowService.undeploy(deploymentId1);
		workflowService.undeploy(deploymentId2);
	}

	@Test
	public void testFindTasks() {
		Term term = buildTestDataWithOneTerm();

		resetTransaction();

		rightsService.addMember(USER_1_OBJ.getId(), Constants.ADMIN, term); // Responsible
		rightsService.addMember(USER_2_OBJ.getId(), Constants.STEWARD, term.getVocabulary().getCommunity()); // Accountable
		rightsService.addMember(USER_3_OBJ.getId(), Constants.STAKEHOLDER, term); // Consulted
		rightsService.addMember(USER_4_OBJ.getId(), Constants.STAKEHOLDER, term); // Consulted
		resetTransaction();

		String deploymentId = deploy("com/collibra/dgc/test/workflow/ApprovalProcess.bpmn20.xml");
		Assert.assertNotNull(deploymentId);

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, term.getId().toString());
		workflowService.startProcessByKey(WorkflowProcesses.APPROVAL, variables);

		resetTransaction();

		// Test with variables map
		printAllTasks();
		List<Task> tasks = workflowService.findTasksForUser(USER_2, variables);
		assertEquals(1, tasks.size());

		// Test with resource id
		tasks = workflowService.findTasksForUser(USER_2, term.getId());
		assertEquals(1, tasks.size());

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testFindTasksWithVariables() {
		String deploymentId = deploy("com/collibra/dgc/test/workflow/TestFormKeys.bpmn20.xml");

		String processInstanceId = workflowService.startProcessByKey("ProcessWithStartFormKey",
				new HashMap<String, Object>());
		Assert.assertNotNull(processInstanceId);

		List<Task> tasks = workflowService.getAllTasks();
		workflowService.setVariable(tasks.get(0).getId(), "MyVAR", "MyVal");
		workflowService.setVariable(tasks.get(0).getId(), "MyVAR2", "MyVal2");
		resetTransaction();
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("MyVAR", "MyVal");
		variables.put("MyVAR2", "MyVal2");
		tasks = workflowService.findTasks(variables);
		assertEquals(1, tasks.size());

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testFindProcessKeyFromTask() {
		String deploymentId = deploy("com/collibra/dgc/test/workflow/TestFormKeys.bpmn20.xml");

		String processInstanceId = workflowService.startProcessByKey("ProcessWithStartFormKey",
				new HashMap<String, Object>());
		Assert.assertNotNull(processInstanceId);

		List<Task> tasks = workflowService.getAllTasks();
		assertEquals("ProcessWithStartFormKey", workflowService.findProcessKey(tasks.get(0)));
		assertEquals("ProcessWithStartFormKey", workflowService.findProcessKey(tasks.get(0).getId()));

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testFindTask() {
		String deploymentId = deploy("com/collibra/dgc/test/workflow/TestFormKeys.bpmn20.xml");
		resetTransaction();
		String processInstanceId = workflowService.startProcessByKey("ProcessWithStartFormKey",
				new HashMap<String, Object>());
		Assert.assertNotNull(processInstanceId);

		List<Task> tasks = workflowService.getAllTasks();

		Task task = workflowService.findTask(tasks.get(0).getId());
		assertNotNull(task);
		assertEquals(tasks.get(0).getProcessDefinitionId(), task.getProcessDefinitionId());

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testRemoveTask() {
		String deploymentId = deploy("com/collibra/dgc/test/workflow/TestFormKeys.bpmn20.xml");

		String processInstanceId = workflowService.startProcessByKey("ProcessWithStartFormKey",
				new HashMap<String, Object>());
		Assert.assertNotNull(processInstanceId);

		List<Task> tasks = workflowService.getAllTasks();
		workflowService.removeTask(tasks.get(0).getId(), "Testing");
		Task task = workflowService.findTask(tasks.get(0).getId());
		Assert.assertNull(task);
		Assert.assertNull(workflowService.getEngine().getProcessInstance(processInstanceId));

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testSuggestWorkflowProcesses() {
		// Deploy the zip file with work flows.
		String deploymentId = workflowService.deploy(new ZipInputStream(getClass().getResourceAsStream(
				"/com/collibra/dgc/workflow/workflow.zip")));
		Assert.assertNotNull(deploymentId);
		resetTransaction();

		// Articulation, Review and Approval will be matched.
		Collection<ProcessDefinition> processes = suggesterService.suggestWorkflowProcesses("Process");
		Assert.assertEquals(3, processes.size());

		// Decommissioning will be matched.
		processes = suggesterService.suggestWorkflowProcesses("comm");
		Assert.assertEquals(1, processes.size());
		Assert.assertEquals(WorkflowProcesses.DECOMMISSIONING, processes.iterator().next().getKey());

		// No match.
		processes = suggesterService.suggestWorkflowProcesses("xyz");
		Assert.assertEquals(0, processes.size());

		// Checking for null argument.
		processes = suggesterService.suggestWorkflowProcesses(null);
		Assert.assertEquals(0, processes.size());

		// Undeploy
		workflowService.undeploy(deploymentId);
	}
}
