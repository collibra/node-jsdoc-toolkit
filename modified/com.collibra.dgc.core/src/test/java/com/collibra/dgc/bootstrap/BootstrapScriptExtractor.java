/**
 * 
 */
package com.collibra.dgc.bootstrap;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.shiro.SecurityUtils;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.ext.mssql.MsSqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.ext.oracle.Oracle10DataTypeFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.service.ConfigurationService;

/**
 * This will fill the database by using the bootstrapper and extract the data using dbunit.
 * 
 * This should be called when our bootstrapping is changed.
 * 
 * The output is written in ${user.home}/full.xml. It should be copied from there to the full.xml in /src/test/resources
 * @author dieterwachters
 */
public class BootstrapScriptExtractor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("/com/collibra/dgc/applicationContext-test.xml");

		final ConfigurationService configurationService = context.getBean(ConfigurationService.class);

		// Set the security manager.
		SecurityUtils.setSecurityManager((org.apache.shiro.mgt.SecurityManager) context.getBean("securityManager"));

		extractScript(new File(System.getProperty("user.home"), "full.xml"), new File(System.getProperty("user.home"),
				"full.dtd"), configurationService);

		System.exit(0);
	}

	public static void extractScript(final File xmlOutput, final File dtdOutput,
			final ConfigurationService configurationService) {
		// Disabling security
		System.setProperty(Constants.DISABLE_SECURITY, "true");

		final String url = configurationService.getString("core/datasource/url");
		final String username = configurationService.getString("core/datasource/username");
		final String password = configurationService.getString("core/datasource/password");
		final String driver = configurationService.getString("core/datasource/driver");
		final String dbName = configurationService.getString("core/datasource/database");
		final BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);

		try {
			IDatabaseConnection connection = new DatabaseConnection(ds.getConnection(), dbName);
			final DatabaseConfig config = connection.getConfig();

			if (xmlOutput != null) {
				// Some different setting for different databases
				if (driver.toLowerCase().contains("mysql")) {
					config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
					config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
				} else if (driver.toLowerCase().contains("hsql")) {
					config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
				} else if (driver.toLowerCase().contains("oracle")) {
					config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new Oracle10DataTypeFactory());
					config.setProperty(DatabaseConfig.FEATURE_SKIP_ORACLE_RECYCLEBIN_TABLES, Boolean.TRUE);
				} else if (driver.toLowerCase().contains("mssql")) {
					config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MsSqlDataTypeFactory());
				}

				String[] tableNames = connection.createDataSet().getTableNames();
				List<String> tnl = new ArrayList<String>();
				for (final String tn : tableNames) {
					if (!tn.endsWith("_AUD") && !tn.endsWith("REVISIONS"))
						tnl.add(tn);
				}
				tableNames = tnl.toArray(new String[tnl.size()]);

				ITableFilter filter = new DatabaseSequenceFilter(connection, tableNames);
				IDataSet fullDataSet = new FilteredDataSet(filter, connection.createDataSet());
				FlatXmlWriter datasetWriter = new FlatXmlWriter(new FileOutputStream(xmlOutput));
				datasetWriter.setIncludeEmptyTable(true);
				datasetWriter.write(fullDataSet);
			}

			if (dtdOutput != null) {
				FlatDtdDataSet.write(connection.createDataSet(), new FileOutputStream(dtdOutput));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
