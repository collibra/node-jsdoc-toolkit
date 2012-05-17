package com.collibra.dgc.service.workflow;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.workflow.WorkflowConstants;
import com.collibra.dgc.core.workflow.WorkflowProcesses;
import com.collibra.dgc.core.workflow.exception.WorkflowException;

/**
 * Test for Review process.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestReviewProcess extends AbstractWorkflowServiceTests {
	private String deploymentId;

	@Test
	public void testNormalScenario() {
		Term term = startProcess();

		// Assert for status.
		assertForStatus(term.getId(), "Under Review");

		// STEP 1: Review by User3, User4, User5
		List<Task> tasks = workflowService.findTasksForUser(USER_3);
		Assert.assertEquals(1, tasks.size());
		workflowService.completeTask(tasks.get(0).getId());

		tasks = workflowService.findTasksForUser(USER_4);
		Assert.assertEquals(1, tasks.size());
		workflowService.completeTask(tasks.get(0).getId());

		tasks = workflowService.findTasksForUser(USER_5);
		Assert.assertEquals(1, tasks.size());
		workflowService.completeTask(tasks.get(0).getId());

		printAllTasks();

		// STEP 2: Consolidate
		tasks = workflowService.findTasksForUser(USER_1);
		Assert.assertEquals(1, tasks.size());
		workflowService.completeTask(tasks.get(0).getId());

		// Assert for status.
		assertForStatus(term.getId(), "Reviewed");

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testTimeoutWithCloseScenario() {
		testTimeoutScenario(true);
	}

	@Test
	public void testTimeoutWithConsolidationScenario() {
		testTimeoutScenario(false);
	}

	@Test
	public void testNoResponsibleUsersAvailable() {
		Term term = buildTestDataWithOneTerm();

		rightsService.addMember(USER_3_OBJ.getId(), Constants.STAKEHOLDER, term.getVocabulary().getCommunity()); // Consulted
		rightsService.addMember(USER_4_OBJ.getId(), Constants.STAKEHOLDER, term.getVocabulary().getCommunity()); // Consulted
		rightsService.addMember(USER_5_OBJ.getId(), Constants.STAKEHOLDER, term.getVocabulary().getCommunity()); // Consulted
		resetTransaction();

		String deploymentId = deploy("com/collibra/dgc/test/workflow/ReviewProcess.bpmn20.xml");
		Assert.assertNotNull(deploymentId);

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, term.getId().toString());
		try {
			workflowService.startProcessByKey(WorkflowProcesses.REVIEW, variables);
			Assert.fail();
		} catch (WorkflowException ex) {
			// Success
			rollback();
		}
		resetTransaction();

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	@Test
	public void testNoConsultedUsersAvailable() {
		Term term = buildTestDataWithOneTerm();

		rightsService.addMember(USER_3_OBJ.getId(), Constants.STEWARD, term); // Responsible
		resetTransaction();

		String deploymentId = deploy("com/collibra/dgc/test/workflow/ReviewProcess.bpmn20.xml");
		Assert.assertNotNull(deploymentId);

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, term.getId().toString());
		try {
			workflowService.startProcessByKey(WorkflowProcesses.REVIEW, variables);
			Assert.fail();
		} catch (WorkflowException ex) {
			// Success
			rollback();
		}
		resetTransaction();

		// Undeploy
		workflowService.undeploy(deploymentId);
	}

	private Term startProcess() {
		Term term = buildTestDataWithOneTerm();

		rightsService.addMember(USER_1_OBJ.getId(), Constants.ADMIN, term.getVocabulary().getCommunity()); // Responsible
		rightsService.addMember(USER_2_OBJ.getId(), Constants.STEWARD, term.getVocabulary().getCommunity()); // Accountable
		rightsService.addMember(USER_3_OBJ.getId(), Constants.STAKEHOLDER, term.getVocabulary().getCommunity()); // Consulted
		rightsService.addMember(USER_4_OBJ.getId(), Constants.STAKEHOLDER, term.getVocabulary().getCommunity()); // Consulted
		rightsService.addMember(USER_5_OBJ.getId(), Constants.STAKEHOLDER, term.getVocabulary().getCommunity()); // Consulted
		resetTransaction();

		deploymentId = deploy("com/collibra/dgc/test/workflow/ReviewProcess.bpmn20.xml");
		Assert.assertNotNull(deploymentId);

		Assert.assertEquals("WorkflowCode.VocabularyEntryWorkflowStart",
				workflowService.getStartFormKey(WorkflowProcesses.REVIEW));

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(WorkflowConstants.VAR_REPRESENTATION_RESOURCE_ID, term.getId().toString());
		workflowService.startProcessByKey(WorkflowProcesses.REVIEW, variables);
		resetTransaction();

		return representationService.findTermByResourceId(term.getId());
	}

	private void testTimeoutScenario(boolean close) {
		startProcess();

		// STEP 1: Review by User3, User4, User5
		List<Task> tasks = workflowService.findTasksForUser(USER_3);
		Assert.assertEquals(1, tasks.size());

		tasks = workflowService.findTasksForUser(USER_4);
		Assert.assertEquals(1, tasks.size());

		tasks = workflowService.findTasksForUser(USER_5);
		Assert.assertEquals(1, tasks.size());
		resetTransaction();

		// Do not complete and trigger timeout.
		// After 7 days the timer event should fire.
		ClockUtil.setCurrentTime(new Date(new Date().getTime() + (7 * 24 * 60 * 60 * 1000) + 5000));
		waitForJobExecutorToProcessAllJobs(5000, 1000);
		resetTransaction();

		// STEP 2: Review not done. Now the responsible will have the task either to close the review process or
		// consolidate.
		tasks = workflowService.findTasksForUser(USER_1);
		Assert.assertEquals(1, tasks.size());

		workflowService.setVariable(tasks.get(0).getId(), WorkflowConstants.VAR_CLOSE, close);
		workflowService.completeTask(tasks.get(0).getId());

		// If process is not stopped by responsible user then proceed further.
		if (!close) {
			// STEP 3 : Consolidation.
			tasks = workflowService.findTasksForUser(USER_1);
			Assert.assertEquals(1, tasks.size());
			workflowService.completeTask(tasks.get(0).getId());

			// STEP 5: Notify....
		}

		// Process stopped
		tasks = workflowService.getAllTasks();
		Assert.assertEquals(0, tasks.size());

		ClockUtil.reset();

		// Undeploy
		workflowService.undeploy(deploymentId);
	}
}
