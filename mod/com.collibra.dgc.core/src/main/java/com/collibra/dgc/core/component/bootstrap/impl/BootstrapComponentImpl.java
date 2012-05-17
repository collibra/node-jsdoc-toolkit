/**
 * 
 */
package com.collibra.dgc.core.component.bootstrap.impl;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.core.application.DGCDataSourceFactory;
import com.collibra.dgc.core.component.bootstrap.BootstrapComponent;
import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.dao.VocabularyDao;
import com.collibra.dgc.core.exceptions.AuthorizationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.job.Job;
import com.collibra.dgc.core.security.authorization.Permissions;
import com.collibra.dgc.core.service.AuthorizationService;
import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.core.service.bootstrapper.BootstrapService;
import com.collibra.dgc.core.service.job.IJobProgressHandler;
import com.collibra.dgc.core.service.job.IJobRunner;
import com.collibra.dgc.core.service.job.JobService;
import com.collibra.dgc.core.service.restart.RestartService;
import com.collibra.dgc.core.util.Defense;

/**
 * @author dieterwachters
 */
@Service
public class BootstrapComponentImpl implements BootstrapComponent {
	private static final Logger log = LoggerFactory.getLogger(BootstrapComponentImpl.class);

	@Autowired
	private ConfigurationService config;
	@Autowired
	private DGCDataSourceFactory dataSourceFactory;
	@Autowired
	private AuthorizationService authorizationService;
	@Autowired
	private BootstrapService bootstrapper;
	@Autowired
	private JobService jobService;
	@Autowired
	private VocabularyDao vocabularyDao;
	@Autowired
	private RestartService restartService;

	@Override
	public void initialize(String driver, String url, String userName, String password, String database) {
		Defense.notEmpty(userName, DGCErrorCodes.BOOTSTRAP_USERNAME_NULL, DGCErrorCodes.BOOTSTRAP_USERNAME_EMPTY,
				"userName");
		Defense.notEmpty(url, DGCErrorCodes.BOOTSTRAP_URL_NULL, DGCErrorCodes.BOOTSTRAP_URL_EMPTY, "url");
		Defense.notEmpty(driver, DGCErrorCodes.BOOTSTRAP_DRIVER_NULL, DGCErrorCodes.BOOTSTRAP_DRIVER_EMPTY, "driver");
		Defense.notEmpty(database, DGCErrorCodes.BOOTSTRAP_DATABASE_NULL, DGCErrorCodes.BOOTSTRAP_DATABASE_EMPTY,
				"database");

		// driver url username datbase

		log.info("Request for initializing DGC with database url '" + url + "'.");

		if (DGCDataSourceFactory.isDataSourceCreated() && !authorizationService.isPermitted(Permissions.ADMIN)) {
			log.error("User is currently not allowed to do the initialization.");
			throw new AuthorizationException(DGCErrorCodes.AUTHORIZATION_FAILED, getCurrentUser(), Permissions.ADMIN);
		}

		String dialect = null;
		if (driver.toLowerCase().contains("h2")) {
			url = "jdbc:h2:{home}/database/dgc";
			database = "PUBLIC";
			userName = "sa";
			password = "";
			dialect = "org.hibernate.dialect.H2Dialect";
		} else if (driver.toLowerCase().contains("mysql")) {
			dialect = "org.hibernate.dialect.MySQL5Dialect";
		} else if (driver.toLowerCase().contains("oracle")) {
			dialect = "org.hibernate.dialect.Oracle10gDialect";
		} else if (driver.toLowerCase().contains(".sqlserver.")) {
			dialect = "org.hibernate.dialect.SQLServerDialect";
		}

		if (dialect == null) {
			log.error("The requested database type is not known.");
			throw new com.collibra.dgc.core.exceptions.IllegalArgumentException(
					DGCErrorCodes.COULD_NOT_DETERMINE_DIALECT, driver);
		}

		try {
			final BasicDataSource ds = new BasicDataSource();
			ds.setUrl(url.replace("{home}", Application.USER_HOME.getAbsolutePath()));
			ds.setUsername(userName);
			ds.setPassword(password);
			ds.setDriverClassName(driver);
			ds.getConnection().close();
		} catch (Exception e) {
			log.error("Could not connect to the database.", e);
			throw new IllegalArgumentException(DGCErrorCodes.COULD_NOT_CONNECT_TO_DATABASE, url);
		}

		config.setProperty("core/datasource/driver", driver);
		config.setProperty("core/datasource/url", url);
		config.setProperty("core/datasource/username", userName);
		config.setProperty("core/datasource/password", password);
		config.setProperty("core/datasource/recreate", "false");
		config.setProperty("core/datasource/dialect", dialect);
		config.setProperty("core/datasource/database", database);
		config.flush();

		restartService.restart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.bootstrap.BootstrapComponent#bootstrap(java.lang.String)
	 */
	@Override
	public String bootstrap(final String bootstrap) {
		log.info("Request for bootstrapping (" + bootstrap + ")");

		if (!authorizationService.isPermitted(Permissions.ADMIN) && vocabularyDao.findSbvrVocabulary() != null) {
			log.error("User is currently not allowed to do the bootstrapping.");
			throw new AuthorizationException(DGCErrorCodes.GLOBAL_NO_PERMISSION, getCurrentUser(), Permissions.ADMIN);
		}

		final Job job = jobService.runJob(new IJobRunner() {
			@Override
			public void run(IJobProgressHandler progressHandler) {
				System.setProperty(Constants.DISABLE_SECURITY, "true");
				try {
					bootstrapper.bootstrap(bootstrap, progressHandler);
				} finally {
					System.setProperty(Constants.DISABLE_SECURITY, "false");
				}
			}

			@Override
			public boolean isCancelable() {
				return false;
			}
		});

		log.info("Bootstrapping job with id '" + job.getId() + "' started.");
		return job.getId();
	}

	protected String getCurrentUser() {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.getPrincipal() == null) {
			return Constants.GUEST_USER;
		}
		return currentUser.getPrincipal().toString();
	}
}
