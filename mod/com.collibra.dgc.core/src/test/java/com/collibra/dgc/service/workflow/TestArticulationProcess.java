package com.collibra.dgc.service.workflow;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.WorkflowProcesses;

/**
 * Articulation workflow process tests.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestArticulationProcess extends AbstractWorkflowServiceTests {
	@Test
	public void testNormalScenario() {
		Term term = buildTestDataWithOneTerm();

		rightsService.addMember(USER_1_OBJ.getId(), Constants.ADMIN, term.getVocabulary().getCommunity()); // Responsible
		rightsService.addMember(USER_2_OBJ.getId(), Constants.STEWARD, term.getVocabulary().getCommunity()); // Accountable
		resetTransaction();

		String deploymentId = deploy("com/collibra/dgc/test/workflow/ArticulationProcess.bpmn20.xml");
		Assert.assertNotNull(deploymentId);
		Assert.assertEquals("WorkflowCode.VocabularyEntryWorkflowStart",
				workflowService.getStartFormKey(WorkflowProcesses.ARTICULATION));

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, term.getId().toString());
		workflowService.startProcessByKey(WorkflowProcesses.ARTICULATION, variables);
		resetTransaction();

		// STEP 1: Articulate
		List<Task> tasks = workflowService.findTasksForUser(USER_1);
		Assert.assertEquals(1, tasks.size());
		assertForStatus(term.getId(), "Candidate");

		// The owners should include all assignees and candidate users.
		Collection<String> owners = workflowService.getTaskOwners(tasks.get(0).getId());
		Assert.assertEquals(2, owners.size());
		Assert.assertTrue(owners.contains(USER_1));
		Assert.assertTrue(owners.contains(USER_2));

		term = representationService.findTermByResourceId(term.getId());
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), term, "Atricutlation done!");
		representationService.saveTerm(term);
		resetTransaction();

		// Indicate that the aritculation task is completed.
		workflowService.completeTask(tasks.get(0).getId());

		// 2. Notification will be sent ...
		tasks = workflowService.getAllTasks();
		Assert.assertEquals(0, tasks.size());

		// Assert for history tasks to have a assignee.
		assertForHisotry();

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testTimeoutScenarioNoReassignment() {
		testTimeoutScenario(false);
	}

	@Test
	public void testTimeoutScenarioWithReassignment() {
		testTimeoutScenario(true);
	}

	@Test
	public void testGetStartFormKeyWhenDeployedAsZip() {
		// Deploy the zip file with work flows.
		String deploymentId = workflowService.deploy(new ZipInputStream(getClass().getResourceAsStream(
				"/com/collibra/dgc/workflow/workflow.zip")));
		Assert.assertNotNull(deploymentId);
		Assert.assertEquals("WorkflowCode.VocabularyEntryWorkflowStart",
				workflowService.getStartFormKey(WorkflowProcesses.ARTICULATION));

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	public void testTimeoutScenario(boolean reassign) {
		Term term = buildTestDataWithOneTerm();

		rightsService.addMember(USER_1_OBJ.getId(), Constants.ADMIN, term.getVocabulary().getCommunity()); // Responsible
		rightsService.addMember(USER_2_OBJ.getId(), Constants.STEWARD, term.getVocabulary().getCommunity()); // Accountable
		rightsService.addMember(USER_3_OBJ.getId(), Constants.ADMIN, term.getVocabulary().getCommunity()); // Reassignment

		resetTransaction();

		String deploymentId = deploy("com/collibra/dgc/test/workflow/ArticulationProcess.bpmn20.xml");
		Assert.assertNotNull(deploymentId);
		Assert.assertEquals("WorkflowCode.VocabularyEntryWorkflowStart",
				workflowService.getStartFormKey(WorkflowProcesses.ARTICULATION));

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, term.getId().toString());
		workflowService.startProcessByKey(WorkflowProcesses.ARTICULATION, variables);

		// STEP 1: Articulate
		List<Task> tasks = workflowService.findTasksForUser(USER_1);
		Assert.assertEquals(1, tasks.size());
		assertForStatus(term.getId(), "Candidate");

		resetTransaction();

		// After 7 days the timer event should fire.
		List<Job> jobs = workflowService.getEngine().getInternalEngine().getManagementService().createJobQuery()
				.processInstanceId(tasks.get(0).getProcessInstanceId()).list();
		Assert.assertEquals(1, jobs.size());
		ClockUtil.setCurrentTime(new Date(new Date().getTime() + (7 * 24 * 60 * 60 * 1000) + 5000));
		waitForJobExecutorToProcessAllJobs(5000, 1000);

		printAllTasks();

		// After the timeout accountable gets the task.
		tasks = workflowService.findTasksForUser(USER_2);
		Assert.assertEquals(1, tasks.size());

		// There should be one reassignment user.
		Set<String> reassignableUsers = (Set<String>) workflowService.getVariable(tasks.get(0).getId(),
				WorkflowConstants.VAR_ALL_USERS_FOR_REASSIGNMENT);
		Assert.assertEquals(2, reassignableUsers.size());
		Assert.assertTrue(reassignableUsers.contains(USER_3));

		// Step 2: Reassign to other user or can close the task.
		workflowService.setVariable(tasks.get(0).getId(), WorkflowConstants.VAR_CLOSE, !reassign);
		resetTransaction();
		if (reassign) {
			// Set the new assignee.
			workflowService.setVariable(tasks.get(0).getId(), WorkflowConstants.VAR_REASSIGN_TO_USER, USER_3);
			workflowService.completeTask(tasks.get(0).getId());

			// Step 3 : User3 get the articulation task.
			tasks = workflowService.findTasksForUser(USER_3);
			Assert.assertEquals(1, tasks.size());

			// The owners should include all assignees and candidate users. In this case there is only one reassigned
			// user.
			Collection<String> owners = workflowService.getTaskOwners(tasks.get(0).getId());
			Assert.assertEquals(1, owners.size());
			Assert.assertTrue(owners.contains(USER_3));

			// Indicate that the aritculation task is completed.
			workflowService.completeTask(tasks.get(0).getId());

			// Step 4. Notification will be sent ...

			tasks = workflowService.getAllTasks();
			Assert.assertEquals(0, tasks.size());
		} else {
			workflowService.completeTask(tasks.get(0).getId());
			printAllTasks();
			// All tasks closed No more tasks should be left.
			tasks = workflowService.getAllTasks();
			Assert.assertEquals(0, tasks.size());
		}

		ClockUtil.reset();

		// Assert for history tasks to have a assignee.
		assertForHisotry();

		// Undeploy
		workflowService.undeploy(deploymentId);
	}
}
