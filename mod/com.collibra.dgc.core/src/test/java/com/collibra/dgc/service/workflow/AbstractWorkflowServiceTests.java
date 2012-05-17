package com.collibra.dgc.service.workflow;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.service.UserService;
import com.collibra.dgc.core.service.WorkflowService;
import com.collibra.dgc.core.workflow.WorkflowEngine;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * Abstract class for {@link WorkflowService} tests.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public abstract class AbstractWorkflowServiceTests extends AbstractServiceTest {
	protected static final String APPROVER = "approver";
	protected static final String DELEGATEE = "delegatee";
	protected static final String DELEGATEE2 = "delegatee2";
	protected static final String STAKEHOLDER = "stakeholder";

	protected static final String USER_1 = "user1";
	protected static final String USER_2 = "user2";
	protected static final String USER_3 = "user3";
	protected static final String USER_4 = "user4";
	protected static final String USER_5 = "user5";
	protected static final String USER_OWNER = "owner";

	protected static UserData USER_1_OBJ = null;
	protected static UserData USER_2_OBJ = null;
	protected static UserData USER_3_OBJ = null;
	protected static UserData USER_4_OBJ = null;
	protected static UserData USER_5_OBJ = null;
	protected static UserData USER_OWNER_OBJ = null;
	@Autowired
	protected WorkflowEngine workflowEngine;
	@Autowired
	private UserService userService;

	protected String deploy(String fileName) {
		ProcessEngine engine = workflowEngine.getInternalEngine();
		String deploymentId = engine.getRepositoryService().createDeployment().addClasspathResource(fileName).deploy()
				.getId();
		Assert.assertNotNull(deploymentId);

		return deploymentId;
	}

	protected List<String> getSelectedTerms() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "Decommission WF URI", "Decommission WF");
		Term t1 = representationFactory.makeTerm(vocabulary, "Term1");
		Term t2 = representationFactory.makeTerm(vocabulary, "Term2");
		Term t3 = representationFactory.makeTerm(vocabulary, "Term3");
		Term t4 = representationFactory.makeTerm(vocabulary, "Term4");

		communityService.save(sp);

		// Set term stewards
		rightsService.addMember(USER_1, Constants.STEWARD, t1);
		rightsService.addMember(USER_2, Constants.STEWARD, t2);
		rightsService.addMember(USER_3, Constants.STEWARD, t3);
		rightsService.addMember(USER_4, Constants.STEWARD, t4);

		// Set stake holders
		rightsService.addMember(USER_2, Constants.STAKEHOLDER, t1);
		rightsService.addMember(USER_3, Constants.STAKEHOLDER, t2);
		rightsService.addMember(USER_4, Constants.STAKEHOLDER, t3);
		rightsService.addMember(USER_1, Constants.STAKEHOLDER, t4);

		// Set other users
		rightsService.addMember(USER_3, Constants.ADMIN, t1);

		resetTransaction();

		List<String> termResourceIds = new LinkedList<String>();
		termResourceIds.add(t1.getId().toString());
		termResourceIds.add(t2.getId().toString());
		termResourceIds.add(t3.getId().toString());
		termResourceIds.add(t4.getId().toString());

		return termResourceIds;
	}

	protected Term buildTestDataWithOneTerm() {
		// Create the data
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "Workflow Test URI", "Workflow Vocabulary");
		Term term = representationFactory.makeTerm(vocabulary, "Term for Articulation");
		communityService.save(sp);
		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());
		Assert.assertNotNull(term);
		return term;
	}

	protected void assertForStatus(String resourceId, String statusSignifier) {
		Representation representation = representationService.findRepresentationByResourceId(resourceId);
		Assert.assertEquals(statusSignifier, representation.getStatus().getSignifier());
	}

	protected void printAllTasks() {
		List<Task> allTasks = workflowService.getAllTasks();
		for (Task t : allTasks) {
			System.out.println(t.getTaskDefinitionKey() + "   " + t.getAssignee() + "  " + t.getDescription());
		}
	}

	@Before
	public void intializeEngine() throws Exception {
		USER_1_OBJ = userService.addUser(USER_1, USER_1, null, null, "dgc-test@collibra.com");
		USER_2_OBJ = userService.addUser(USER_2, USER_2, null, null, "dgc-test@collibra.com");
		USER_3_OBJ = userService.addUser(USER_3, USER_3, null, null, "dgc-test@collibra.com");
		USER_4_OBJ = userService.addUser(USER_4, USER_4, null, null, "dgc-test@collibra.com");
		USER_5_OBJ = userService.addUser(USER_5, USER_5, null, null, "dgc-test@collibra.com");
		resetTransaction();

		// Rest the DbId of Activiti.
		// ProcessEngineConfigurationImpl procEngineConfig = ((ProcessEngineImpl) workflowService.getEngine()
		// .getInternalEngine()).getProcessEngineConfiguration();
		// ((GlossaryDbIdGenerator) procEngineConfig.getIdGenerator()).reset();
	}

	protected void waitForJobExecutorToProcessAllJobs(long maxMillisToWait, long intervalMillis) {
		ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl) workflowService.getEngine()
				.getInternalEngine()).getProcessEngineConfiguration();
		JobExecutor jobExecutor = processEngineConfiguration.getJobExecutor();

		jobExecutor.start();

		try {

			Timer timer = new Timer();
			InteruptTask task = new InteruptTask(Thread.currentThread());
			timer.schedule(task, maxMillisToWait);
			boolean areJobsAvailable = true;
			try {
				while (areJobsAvailable && !task.isTimeLimitExceeded()) {
					Thread.sleep(intervalMillis);
					areJobsAvailable = areJobsAvailable();
				}
			} catch (InterruptedException e) {
			} finally {
				timer.cancel();
			}
			// if (areJobsAvailable) {
			// throw new ActivitiException("time limit of " + maxMillisToWait + " was exceeded");
			// }

		} finally {
			jobExecutor.shutdown();
		}
	}

	protected boolean areJobsAvailable() {
		ManagementService managementService = workflowService.getEngine().getInternalEngine().getManagementService();
		return !managementService.createJobQuery().executable().list().isEmpty();
	}

	private static class InteruptTask extends TimerTask {
		protected boolean timeLimitExceeded = false;
		protected Thread thread;

		public InteruptTask(Thread thread) {
			this.thread = thread;
		}

		public boolean isTimeLimitExceeded() {
			return timeLimitExceeded;
		}

		@Override
		public void run() {
			timeLimitExceeded = true;
			thread.interrupt();
		}
	}

	protected void assertForHisotry() {
		Collection<HistoricTaskInstance> tasks = workflowService.findCompletedTasks(0, 1000);
		for (HistoricTaskInstance task : tasks) {
			Assert.assertTrue(task.getAssignee() != null);
		}

		Collection<HistoricProcessInstance> processes = workflowService.findCompletedProcesses(0, 100);
		for (HistoricProcessInstance process : processes) {
			Assert.assertTrue(process.getStartUserId() != null);
		}
	}
}
