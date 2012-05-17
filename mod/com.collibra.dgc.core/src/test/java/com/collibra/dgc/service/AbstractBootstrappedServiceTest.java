/**
 * 
 */
package com.collibra.dgc.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.collibra.dgc.AbstractDGCBootstrappedTest;
import com.collibra.dgc.core.component.VerbaliserComponent;
import com.collibra.dgc.core.dao.BinaryFactTypeFormDao;
import com.collibra.dgc.core.dao.CommunityDao;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.dao.RelationDao;
import com.collibra.dgc.core.dao.RuleSetDao;
import com.collibra.dgc.core.dao.TermDao;
import com.collibra.dgc.core.dao.VocabularyDao;
import com.collibra.dgc.core.model.categorizations.CategorizationFactory;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.relation.RelationFactory;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.rules.RuleFactory;
import com.collibra.dgc.core.service.AttributeService;
import com.collibra.dgc.core.service.CategorizationService;
import com.collibra.dgc.core.service.CommunityService;
import com.collibra.dgc.core.service.ConfigurationCategoryService;
import com.collibra.dgc.core.service.DataConsistencyService;
import com.collibra.dgc.core.service.HistoryService;
import com.collibra.dgc.core.service.MatchingService;
import com.collibra.dgc.core.service.MeaningService;
import com.collibra.dgc.core.service.RelationService;
import com.collibra.dgc.core.service.ReportService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.RightsService;
import com.collibra.dgc.core.service.RulesService;
import com.collibra.dgc.core.service.SchedulerService;
import com.collibra.dgc.core.service.SearchService;
import com.collibra.dgc.core.service.StatisticsService;
import com.collibra.dgc.core.service.SuggesterService;
import com.collibra.dgc.core.service.UserService;
import com.collibra.dgc.core.service.WorkflowService;

/**
 * @author dieterwachters
 * 
 */
@Ignore
public abstract class AbstractBootstrappedServiceTest extends AbstractDGCBootstrappedTest {
	@Override
	public void bootstrap() throws Exception {
		super.bootstrap();
		beginTransaction();
	}

	@After
	public void cleanup() {
		try {
			commit();
		} catch (Throwable e) {
			// Ignore
		}
	}

	private TransactionStatus transactionStatus;

	protected void beginTransaction() {
		transactionStatus = txManager.getTransaction(transactionDefinition);
	}

	protected void commit() {
		if (transactionStatus != null) {
			txManager.commit(transactionStatus);
			transactionStatus = null;
		}
	}

	protected void rollback() {
		if (transactionStatus != null) {
			txManager.rollback(transactionStatus);
			transactionStatus = null;
		}
		beginTransaction();
	}

	protected void resetTransaction() {
		commit();
		beginTransaction();
	}

	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	protected VerbaliserComponent verbaliserComponent;

	// All the autowired services
	@Autowired
	protected CommunityService communityService;
	@Autowired
	protected AttributeService attributeService;
	@Autowired
	protected SearchService searchService;
	@Autowired
	protected RightsService rightsService;
	@Autowired
	protected MeaningService meaningService;
	@Autowired
	protected RepresentationService representationService;
	@Autowired
	protected StatisticsService statisticsService;
	@Autowired
	protected SuggesterService suggesterService;
	@Autowired
	protected RulesService rulesService;
	@Autowired
	protected MatchingService matchingService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected DataConsistencyService dataConsistencyService;
	@Autowired
	protected CategorizationService categorizationService;
	@Autowired
	protected RelationService relationService;
	@Autowired
	protected ReportService reportService;
	@Autowired
	protected ConfigurationCategoryService configurationCategoryService;

	// All autowired DAOs
	@Autowired
	protected RuleSetDao ruleSetDao;
	@Autowired
	protected CommunityDao communityDao;
	@Autowired
	protected VocabularyDao vocabularyDao;
	@Autowired
	protected ObjectTypeDao objectTypeDao;
	@Autowired
	protected BinaryFactTypeFormDao binaryFactTypeFormDao;
	@Autowired
	protected TermDao termDao;
	@Autowired
	protected RelationDao relationDao;

	// All autowired factories
	@Autowired
	protected CommunityFactory communityFactory;
	@Autowired
	protected MeaningFactory meaningFactory;
	@Autowired
	protected RuleFactory ruleFactory;
	@Autowired
	protected RepresentationFactory representationFactory;
	@Autowired
	protected UserService userService;
	@Autowired
	protected CategorizationFactory categorizationFactory;
	@Autowired
	protected RelationFactory relationFactory;

	@Autowired
	protected SchedulerService schedulerService;

	@Autowired
	protected WorkflowService workflowService;

	protected PlatformTransactionManager txManager;
	protected TransactionTemplate tt;

	private final TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();

	@Autowired
	public void setPlatformTransactionManager(final PlatformTransactionManager txManager) {
		this.txManager = txManager;
		tt = new TransactionTemplate(txManager);
	}
}
