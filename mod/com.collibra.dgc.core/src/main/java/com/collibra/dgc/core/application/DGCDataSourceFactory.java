/**
 * 
 */
package com.collibra.dgc.core.application;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.collibra.dgc.core.service.ConfigurationService;

/**
 * The factory bean for creating the data source.
 * 
 * @author dieterwachters
 */
public class DGCDataSourceFactory {
	private static final Logger log = LoggerFactory.getLogger(DGCDataSourceFactory.class);

	@Autowired
	ConfigurationService config;

	private static boolean dataSourceCreated;

	/**
	 * Creates the data source by getting the needed information from the configuration.
	 * 
	 * @return
	 */
	public DataSource createDataSource() {
		dataSourceCreated = false;
		try {
			String url = config.getString("core/datasource/url");
			if (url == null || url.trim().length() == 0) {
				log.warn("No data connection has been set up.");
				return null;
			}
			url = url.replace("{home}", Application.USER_HOME.getAbsolutePath());
			log.info("Create database connection with " + url);
			final String username = config.getString("core/datasource/username");
			final String password = config.getString("core/datasource/password");
			final String driver = config.getString("core/datasource/driver");
			if (url != null) {
				final BasicDataSource ds = new BasicDataSource();
				ds.setInitialSize(config.getInteger("core/datasource/pool/initial"));
				ds.setMaxActive(config.getInteger("core/datasource/pool/max"));
				ds.setMaxIdle(config.getInteger("core/datasource/pool/max"));
				// ds.setRemoveAbandoned(true);
				// ds.setRemoveAbandonedTimeout(30000);
				// We wait maximum 20 seconds for a new connection.
				ds.setMaxWait(40000);
				ds.setUrl(url);
				ds.setUsername(username);
				ds.setPassword(password);
				ds.setDriverClassName(driver);
				ds.setDefaultTransactionIsolation(2);

				if (ds.getInitialSize() > 0) {
					ds.getLogWriter();
				}
				ds.getConnection().close();
				dataSourceCreated = true;
				return ds;
			}
		} catch (SQLException e) {
			log.error("Error while setting up datasource.", e);
		}
		return null;
	}

	public static boolean isDataSourceCreated() {
		return dataSourceCreated;
	}
}
