package com.collibra.dgc.service.scheduling;

import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.service.AbstractBootstrappedServiceTest;

/**
 * Tests for scheduler service.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
@Ignore
public class TestSchedulerService extends AbstractBootstrappedServiceTest {
	// @Test
	// public void testGetScheduler() throws Exception {
	// Scheduler scheduler = schedulerService.getScheduler();
	// Assert.assertNotNull(scheduler);
	// Assert.assertTrue(scheduler.getJobGroupNames().size() == 0);
	// }
	//
	// @Test
	// public void testSceduleEmailNotification() throws Exception {
	// schedulerService.scheduleUserEmailNotification("Test_User", "0 * * * * ?");
	// Scheduler scheduler = schedulerService.getScheduler();
	// JobDetail job = scheduler.getJobDetail(new JobKey("Test_User", SchedulerConstants.EMAIL_NOTIFICATION_GROUP));
	// Assert.assertNotNull(job);
	// scheduler.triggerJob(new JobKey("Test_User", SchedulerConstants.EMAIL_NOTIFICATION_GROUP));
	// }
}
