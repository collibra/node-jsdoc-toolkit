package com.collibra.dgc;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.collibra.dgc.annotation.Bootstrap;
import com.collibra.dgc.core.service.bootstrapper.BootstrapService;
import com.collibra.dgc.core.service.job.IJobProgressHandler;

/**
 * Abstract DGC test class that can do bootstrapping and used by both component APIs and Services.
 * 
 * @author amarnath
 * 
 */
@Ignore
public class AbstractDGCBootstrappedTest extends AbstractDGCTest {
	private static final Logger log = LoggerFactory.getLogger(AbstractDGCBootstrappedTest.class);

	@Autowired
	private BootstrapService bootstrapService;
	@Autowired
	private PlatformTransactionManager txManager;

	@Before
	public void bootstrap() throws Exception {
		// Set the security manager.
		SecurityUtils.setSecurityManager((org.apache.shiro.mgt.SecurityManager) applicationContext
				.getBean("securityManager"));
		Bootstrap bootstrap = getClass().getAnnotation(Bootstrap.class);

		String bs = null;
		if (bootstrap != null) {
			bs = bootstrap.value();
		}
		if (bs == null || bs.isEmpty()) {
			bs = "basic";
		}
		bootstrapService.bootstrap(bs, new IJobProgressHandler() {
			@Override
			public boolean isCanceled() {
				return false;
			}

			@Override
			public void reportProgress(double progress, String message) {
			}

			@Override
			public void reportCompleted(String message) {
			}

			@Override
			public void reportError(String message) {
				log.error(message);
			}
		});

		new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				login("Admin", "admin1906");
			}
		});
	}

	protected final void logout() {
		final Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout();
		}
	}

	protected final void login(String userName, String password) {
		final Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout();
		}
		subject.login(new UsernamePasswordToken(userName, password));
	}
}
