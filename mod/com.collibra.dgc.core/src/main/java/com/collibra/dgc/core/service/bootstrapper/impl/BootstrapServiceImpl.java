package com.collibra.dgc.core.service.bootstrapper.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.ext.mssql.MsSqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.ext.oracle.Oracle10DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.SessionFactory;
import org.hibernate.TransientObjectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.DGCException;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.security.authorization.AuthorizationsFactory;
import com.collibra.dgc.core.service.CommunityService;
import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.core.service.MeaningService;
import com.collibra.dgc.core.service.SearchService;
import com.collibra.dgc.core.service.bootstrapper.BootstrapService;
import com.collibra.dgc.core.service.bootstrapper.impl.meanings.SbvrBootstrapperOptionsProvider;
import com.collibra.dgc.core.service.bootstrapper.impl.meanings.SbvrMeaningBootstrapper;
import com.collibra.dgc.core.service.bootstrapper.impl.user.UsersManagementBootstrapper;
import com.collibra.dgc.core.service.exchanger.SbvrExchanger;
import com.collibra.dgc.core.service.exchanger.options.SbvrImporterOptions;
import com.collibra.dgc.core.service.job.IJobProgressHandler;

/**
 * The SBVR Bootstrapper takes care of loading the SBVR metamodel and creating the required communities. CAUTION:
 * running bootstrapper more than once will delete everything and just recreate SBVR vocabularies.
 * 
 * Important: the bootstrapper needs to be run before the glossary can be used.
 * 
 * @author dtrog
 * 
 */
@Service
public class BootstrapServiceImpl implements BootstrapService {
	private transient static final Logger log = LoggerFactory.getLogger(BootstrapServiceImpl.class);

	private static final String sbvrMeaningAndRepresentationLocation = "/com/collibra/dgc/core/bootstrapper/sbvr/MeaningAndRepresentation-model.xml";
	private static final String sbvrDescribingBusinesssRulesLocation = "/com/collibra/dgc/core/bootstrapper/sbvr/DescribingBusinessRules-model.xml";
	private static final String sbvrDescribingBusinesssVocabulariesLocation = "/com/collibra/dgc/core/bootstrapper/sbvr/DescribingBusinessVocabularies-model.xml";
	private static final String sbvrLogicalFormulationOfSemanticsLocation = "/com/collibra/dgc/core/bootstrapper/sbvr/LogicalFormulationOfSemantics-model.xml";

	private static final String collibraExtensions = "/com/collibra/dgc/core/bootstrapper/sbvr/CollibraExtensions-model.xml";
	private static final String statuses = "/com/collibra/dgc/core/bootstrapper/sbvr/Statuses-model.xml";
	private static final String collibraUserRoles = "/com/collibra/dgc/core/bootstrapper/sbvr/CollibraUserRoles-model.xml";

	private static final Map<String, IDataSet> dataSets = new HashMap<String, IDataSet>();

	@Autowired
	private SbvrExchanger sbvrExchanger;

	@Autowired
	private ObjectTypeDao objectTypeDao;

	@Autowired
	private MeaningService meaningService;

	@Autowired
	private MeaningFactory meaningFactory;

	@Autowired
	private CommunityService communityService;

	@Autowired
	private RepresentationFactory representationFactory;

	@Autowired
	private CommunityFactory communityFactory;

	@Autowired
	private AuthorizationsFactory authorizationsFactory;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private SearchService searchService;

	private SbvrImporterOptions options;
	private Vocabulary sbvrVocabulary;
	private SbvrMeaningBootstrapper meaningBootstrapper;

	@Override
	@Transactional
	public void bootstrap(IJobProgressHandler progressHandler) {
		try {
			// TODO PORT
			// if (!LicenceValidationStatus.getInstance().isValid()) {
			// throw new RuntimeException("License invalid");
			// }

			Community metaModelCommunity = bootstrapMetamodelCommunity();
			log.info("Bootstrapped MetaModel Community");
			Community sbvrCommunity = bootstrapSbvrCommunity(metaModelCommunity);
			log.info("Bootstrapped SBVR English Community");
			Community adminCommunity = bootstrapAdminCommunity(metaModelCommunity);
			log.info("Bootstrapped Admin Community");

			// Commit the SBVR community for bootstrapping.
			metaModelCommunity = communityService.save(metaModelCommunity);

			progressHandler.reportProgress(10, null);

			bootstrapMeaning();

			progressHandler.reportProgress(30, null);

			prepareOptions();
			sbvrVocabulary = representationFactory.makeVocabulary(sbvrCommunity,
					"http://www.omg.org/spec/SBVR/20070901/SBVR.xml", "SBVR");
			Vocabulary meaningAndRepresentationVocabulary = bootstrapMeaningAndRepresentation(sbvrCommunity);
			options.addIncorporatedVocabulary(meaningAndRepresentationVocabulary);

			progressHandler.reportProgress(40, null);

			Vocabulary sbvrLogicalFormulationOfSemanticsVocabulay = bootstrapLogicalFormulation(sbvrCommunity);
			Vocabulary businessVocabulary = bootstrapBusinessVocabularies(sbvrCommunity);

			progressHandler.reportProgress(50, null);

			options.addIncorporatedVocabulary(businessVocabulary);
			Vocabulary businessRulesVocabulary = bootstrapBusinessRules(sbvrCommunity);

			progressHandler.reportProgress(60, null);

			options.addIncorporatedVocabulary(businessRulesVocabulary);
			Vocabulary collibraExtensionsVocabulary = bootstrapCollibraExtensions(sbvrCommunity);

			progressHandler.reportProgress(70, null);

			options.addIncorporatedVocabulary(collibraExtensionsVocabulary);
			// Bootstrap user roles
			Vocabulary collibraUserRoles = bootstrapCollibraUserRoles(adminCommunity);

			progressHandler.reportProgress(80, null);

			// incorporate all into sbvr vocabulary
			sbvrVocabulary.addIncorporatedVocabulary(meaningAndRepresentationVocabulary);
			sbvrVocabulary.addIncorporatedVocabulary(sbvrLogicalFormulationOfSemanticsVocabulay);
			sbvrVocabulary.addIncorporatedVocabulary(businessRulesVocabulary);
			sbvrVocabulary.addIncorporatedVocabulary(businessVocabulary);
			sbvrVocabulary.addIncorporatedVocabulary(collibraExtensionsVocabulary);

			businessVocabulary.addIncorporatedVocabulary(meaningAndRepresentationVocabulary);
			businessRulesVocabulary.addIncorporatedVocabulary(businessVocabulary);
			collibraExtensionsVocabulary.addIncorporatedVocabulary(businessRulesVocabulary);

			final Vocabulary attributeTypes = representationFactory.makeVocabulary(adminCommunity,
					Constants.ATTRIBUTETYPES_VOCABULARY_URI, Constants.ATTRIBUTETYPES_VOCABULARY_NAME);
			attributeTypes.addIncorporatedVocabulary(sbvrVocabulary);

			final Vocabulary statusses = bootstrapStatuses(adminCommunity);
			statusses.addIncorporatedVocabulary(sbvrVocabulary);

			final Vocabulary conceptTypes = representationFactory.makeVocabulary(adminCommunity,
					Constants.METAMODEL_EXTENSIONS_VOCABULARY_URI, Constants.METAMODEL_EXTENSIONS_VOCABULARY_NAME);
			conceptTypes.addIncorporatedVocabulary(sbvrVocabulary);

			// Bootstrap roles
			UsersManagementBootstrapper userBootStrapper = new UsersManagementBootstrapper(
					sessionFactory.getCurrentSession(), collibraUserRoles, collibraExtensionsVocabulary,
					representationFactory, authorizationsFactory);
			final User adminUser = userBootStrapper.bootstrap();

			progressHandler.reportProgress(85, null);

			// Commit the SBVR community for bootstrapping.
			metaModelCommunity = communityService.save(metaModelCommunity);

			progressHandler.reportProgress(95, null);

			// Add administrator as member
			userBootStrapper.addAdminAsMember(adminUser, metaModelCommunity);

			log.info("Bootstrapped sbvr vocabulary");

			log.info("Finished bootstrap");
		} catch (TransientObjectException e) {
			log.error("Bootstrap failed: " + e.getMessage(), e);
			throw new DGCException(e, DGCErrorCodes.BOOTSTRAP_FAILED);
		} catch (Exception e) {
			log.error("Bootstrap failed: " + e.getMessage(), e);
			throw new DGCException(e, DGCErrorCodes.BOOTSTRAP_FAILED);
		} finally {
			options = null;
		}
	}

	private void bootstrapMeaning() {
		meaningBootstrapper = new SbvrMeaningBootstrapper(meaningFactory, sessionFactory.getCurrentSession());
		meaningBootstrapper.bootStrapMeaning();
	}

	private Community bootstrapMetamodelCommunity() {
		return communityFactory.makeCommunity(Constants.METAMODEL_COMMUNITY_NAME, Constants.METAMODEL_COMMUNITY_URI);
	}

	private Community bootstrapSbvrCommunity(Community community) {
		return communityFactory.makeCommunity(community, Constants.SBVR_ENGLISH_COMMUNITY_NAME,
				Constants.SBVR_ENGLISH_COMMUNITY_URI);
	}

	private Community bootstrapAdminCommunity(Community community) {
		return communityFactory.makeCommunity(community, Constants.ADMIN_COMMUNITY_NAME, Constants.ADMIN_COMMUNITY_URI);
	}

	private Vocabulary bootstrapMeaningAndRepresentation(Community community) {
		return sbvrExchanger.importSbvrVocabularyInputStream(
				getClass().getResourceAsStream(sbvrMeaningAndRepresentationLocation), community, options);
	}

	private Vocabulary bootstrapLogicalFormulation(Community community) {
		return sbvrExchanger.importSbvrVocabularyInputStream(
				getClass().getResourceAsStream(sbvrLogicalFormulationOfSemanticsLocation), community, options);
	}

	private Vocabulary bootstrapBusinessRules(Community community) {
		return sbvrExchanger.importSbvrVocabularyInputStream(
				getClass().getResourceAsStream(sbvrDescribingBusinesssRulesLocation), community, options);
	}

	private Vocabulary bootstrapBusinessVocabularies(Community community) {
		return sbvrExchanger.importSbvrVocabularyInputStream(
				getClass().getResourceAsStream(sbvrDescribingBusinesssVocabulariesLocation), community, options);
	}

	private Vocabulary bootstrapCollibraExtensions(Community community) {
		return sbvrExchanger.importSbvrVocabularyInputStream(getClass().getResourceAsStream(collibraExtensions),
				community, options);
	}

	private Vocabulary bootstrapStatuses(Community community) {
		return sbvrExchanger.importSbvrVocabularyInputStream(getClass().getResourceAsStream(statuses), community,
				options);
	}

	private Vocabulary bootstrapCollibraUserRoles(Community community) {
		return sbvrExchanger.importSbvrVocabularyInputStream(getClass().getResourceAsStream(collibraUserRoles),
				community, options);
	}

	private void prepareOptions() {
		SbvrBootstrapperOptionsProvider optionsProvider = new SbvrBootstrapperOptionsProvider(meaningService,
				objectTypeDao);
		options = optionsProvider.prepareOptions();
	}

	@Override
	@Transactional
	public void bootstrap(String bootstrap, InputStream dataInput, InputStream dtdInput,
			IJobProgressHandler progressHandler) {
		progressHandler.reportProgress(5, null);

		IDataSet dataSet = null;
		synchronized (dataSets) {
			dataSet = dataSets.get(bootstrap);
			if (dataSet == null) {
				try {
					final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
					builder.setMetaDataSetFromDtd(dtdInput);
					dataSet = builder.build(dataInput);

				} catch (Exception e) {
					log.error("Error loading bootstrap script.", e);
					throw new DGCException(e, DGCErrorCodes.BOOTSTRAP_SCRIPT_INVALID, bootstrap + ".xml");
				}
				dataSets.put(bootstrap, dataSet);
			}
		}

		progressHandler.reportProgress(50, null);

		final String driver = configurationService.getString("core/datasource/driver");
		final String dbName = configurationService.getString("core/datasource/database");

		try {
			// get connection
			Connection con = sessionFactory.getCurrentSession().connection();
			final IDatabaseConnection connection = new DatabaseConnection(con, dbName);
			final DatabaseConfig config = connection.getConfig();

			// Some different setting for different databases
			if (driver.toLowerCase().contains("mysql")) {
				config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
				config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
				con.createStatement().execute("SET foreign_key_checks = 0;");
			} else if (driver.toLowerCase().contains("hsql")) {
				config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
				con.createStatement().execute("SET DATABASE REFERENTIAL INTEGRITY FALSE");
			} else if (driver.toLowerCase().contains("h2")) {
				config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
				con.createStatement().execute("SET REFERENTIAL_INTEGRITY FALSE");
			} else if (driver.toLowerCase().contains("oracle")) {
				config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new Oracle10DataTypeFactory());
				config.setProperty(DatabaseConfig.FEATURE_SKIP_ORACLE_RECYCLEBIN_TABLES, Boolean.TRUE);
				con.createStatement().execute("ALTER TABLE MEANINGS DISABLE CONSTRAINT FK_CONCEPT_TYPE");
				con.createStatement().execute("ALTER TABLE MEANINGS DISABLE CONSTRAINT FK_GENERAL_CONCEPT");
			} else if (driver.toLowerCase().contains("mssql")) {
				config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MsSqlDataTypeFactory());
			}

			config.setProperty("http://www.dbunit.org/features/batchedStatements", Boolean.TRUE);

			progressHandler.reportProgress(55, null);

			// DatabaseOperation.DELETE_ALL.execute(connection,
			// getDataSet(driver));
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

			progressHandler.reportProgress(80, null);

			final Statement stm = con.createStatement();
			if (driver.toLowerCase().contains("oracle")) {
				fixOracleSequence(stm, "MEMBERS_SEQUENCE");
				fixOracleSequence(stm, "ROLES_SEQUENCE");
				fixOracleSequence(stm, "READINGDIRECTIONS_SEQUENCE");
				con.createStatement().execute("ALTER TABLE MEANINGS ENABLE CONSTRAINT FK_CONCEPT_TYPE");
				con.createStatement().execute("ALTER TABLE MEANINGS ENABLE CONSTRAINT FK_GENERAL_CONCEPT");
			} else if (driver.toLowerCase().contains("hsql")) {
				stm.execute("SET DATABASE REFERENTIAL INTEGRITY TRUE");
			} else if (driver.toLowerCase().contains("h2")) {
				stm.execute("SET REFERENTIAL_INTEGRITY TRUE");
			} else if (driver.toLowerCase().contains("mysql")) {
				stm.execute("SET foreign_key_checks = 1;");
			}
			progressHandler.reportProgress(90, null);
			stm.close();

			con.close();
			connection.close();
		} catch (Exception e) {
			throw new DGCException(e, DGCErrorCodes.BOOTSTRAP_FAILED);
		}

		searchService.doFullReIndex();

		progressHandler.reportCompleted(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.service.bootstrapper.BootstrapService#bootstrap(java.lang.String,
	 * com.collibra.dgc.core.service.job.IJobProgressHandler)
	 */
	@Override
	@Transactional
	public void bootstrap(String bootstrap, IJobProgressHandler progressHandler) {
		InputStream dataInput = BootstrapServiceImpl.class.getResourceAsStream("/com/collibra/dgc/core/bootstrapper/"
				+ bootstrap + ".xml");
		if (dataInput == null) {
			final File dataFile = new File(Application.BOOTSTRAP_DIR,
					bootstrap.toLowerCase().endsWith(".xml") ? bootstrap : bootstrap + ".xml");
			if (!dataFile.exists() || !dataFile.canRead()) {
				throw new EntityNotFoundException(DGCErrorCodes.BOOTSTRAP_NOT_FOUND, bootstrap + ".xml");
			}
			try {
				dataInput = new FileInputStream(dataFile);
			} catch (FileNotFoundException e) {
				throw new EntityNotFoundException(DGCErrorCodes.BOOTSTRAP_NOT_FOUND, bootstrap + ".xml");
			}
		}

		bootstrap(bootstrap, dataInput,
				BootstrapServiceImpl.class.getResourceAsStream("/com/collibra/dgc/core/bootstrapper/structure.dtd"),
				progressHandler);
	}

	private final void fixOracleSequence(final Statement stm, final String seq) throws Exception {
		stm.execute("ALTER SEQUENCE \"" + seq + "\" INCREMENT BY 10000");
		stm.execute("SELECT " + seq + ".NEXTVAL FROM dual");
		stm.execute("ALTER SEQUENCE \"" + seq + "\" INCREMENT BY 1");
		SecurityUtils.getSubject().logout();
	}
}
