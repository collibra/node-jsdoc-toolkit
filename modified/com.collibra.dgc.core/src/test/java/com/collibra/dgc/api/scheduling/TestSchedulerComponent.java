package com.collibra.dgc.api.scheduling;

import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.component.SchedulerComponent;

/**
 * {@link SchedulerComponent} API tests.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
@Ignore
public class TestSchedulerComponent extends AbstractDGCBootstrappedApiTest {
	// @Test
	// public void testConfigureMailServer() {
	// boolean ok = schedulerComponent.configureMailServer();
	// Assert.assertTrue(ok);
	// }
	//
	// @Test
	// public void testScheduleDailyEmailNotification() {
	// // TODO PORT
	// }
	//
	// @Test
	// public void testScheduleUserEmailNotification() {
	// boolean ok = schedulerComponent.scheduleUserEmailNotification(Constants.ADMIN,
	// SchedulerComponent.DAILY_NOTIFICATION_CRON_EXPRESSION);
	// Assert.assertTrue(ok);
	// }
	//
	// @Test
	// public void testGetScheduler() {
	// Scheduler scheduler = schedulerComponent.getScheduler();
	// Assert.assertNotNull(scheduler);
	// }
	//
	// @Test
	// public void testUnscheduleFromDailyEmailNotification() {
	// // TODO PORT
	// }
	//
	// @Test
	// public void testUnscheduleFromUserEmailNotification() {
	// boolean ok = schedulerComponent.scheduleUserEmailNotification(Constants.ADMIN,
	// SchedulerComponent.DAILY_NOTIFICATION_CRON_EXPRESSION);
	// Assert.assertTrue(ok);
	// ok = schedulerComponent.unscheduleFromUserEmailNotification(Constants.ADMIN);
	// Assert.assertTrue(ok);
	// }
	//
	// @Test
	// public void reinitialize() {
	// boolean ok = schedulerComponent.reinitialize();
	// Assert.assertTrue(ok);
	// Assert.assertFalse(schedulerComponent.unscheduleFromUserEmailNotification(Constants.ADMIN));
	// }
}
