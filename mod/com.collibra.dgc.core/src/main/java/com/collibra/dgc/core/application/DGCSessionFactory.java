/**
 * 
 */
package com.collibra.dgc.core.application;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.envers.event.AuditEventListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

import com.collibra.dgc.core.model.ModelRevision;
import com.collibra.dgc.core.model.categorizations.impl.CategorizationTypeImpl;
import com.collibra.dgc.core.model.categorizations.impl.CategoryImpl;
import com.collibra.dgc.core.model.community.impl.CommunityImpl;
import com.collibra.dgc.core.model.configuration.impl.AttributeAndRelationTypesConfigurationCategoryImpl;
import com.collibra.dgc.core.model.configuration.impl.ConfigurationCategoryImpl;
import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.job.impl.JobImpl;
import com.collibra.dgc.core.model.meaning.facttype.impl.BinaryFactTypeImpl;
import com.collibra.dgc.core.model.meaning.facttype.impl.CharacteristicImpl;
import com.collibra.dgc.core.model.meaning.impl.ConceptImpl;
import com.collibra.dgc.core.model.meaning.impl.MeaningImpl;
import com.collibra.dgc.core.model.meaning.impl.ObjectTypeImpl;
import com.collibra.dgc.core.model.meaning.impl.PropositionImpl;
import com.collibra.dgc.core.model.meaning.impl.SimplePropositionImpl;
import com.collibra.dgc.core.model.relation.impl.RelationImpl;
import com.collibra.dgc.core.model.representation.facttypeform.impl.BinaryFactTypeFormImpl;
import com.collibra.dgc.core.model.representation.facttypeform.impl.CharacteristicFormImpl;
import com.collibra.dgc.core.model.representation.facttypeform.impl.ReadingDirectionImpl;
import com.collibra.dgc.core.model.representation.impl.AttributeImpl;
import com.collibra.dgc.core.model.representation.impl.DateTimeAttributeImpl;
import com.collibra.dgc.core.model.representation.impl.DesignationImpl;
import com.collibra.dgc.core.model.representation.impl.MultiValueListAttributeImpl;
import com.collibra.dgc.core.model.representation.impl.RepresentationImpl;
import com.collibra.dgc.core.model.representation.impl.SimpleStatementImpl;
import com.collibra.dgc.core.model.representation.impl.SingleValueListAttributeImpl;
import com.collibra.dgc.core.model.representation.impl.StatementImpl;
import com.collibra.dgc.core.model.representation.impl.StringAttributeImpl;
import com.collibra.dgc.core.model.representation.impl.TermImpl;
import com.collibra.dgc.core.model.representation.impl.VocabularyImpl;
import com.collibra.dgc.core.model.rules.impl.FrequencyRuleStatementImpl;
import com.collibra.dgc.core.model.rules.impl.RuleImpl;
import com.collibra.dgc.core.model.rules.impl.RuleSetImpl;
import com.collibra.dgc.core.model.rules.impl.RuleStatementImpl;
import com.collibra.dgc.core.model.rules.impl.SemiparsedRuleStatementImpl;
import com.collibra.dgc.core.model.rules.impl.SimpleRuleStatementImpl;
import com.collibra.dgc.core.model.user.impl.AddressImpl;
import com.collibra.dgc.core.model.user.impl.EmailImpl;
import com.collibra.dgc.core.model.user.impl.GroupImpl;
import com.collibra.dgc.core.model.user.impl.GroupMembershipImpl;
import com.collibra.dgc.core.model.user.impl.InstantMessagingAccountImpl;
import com.collibra.dgc.core.model.user.impl.MemberImpl;
import com.collibra.dgc.core.model.user.impl.PhoneNumberImpl;
import com.collibra.dgc.core.model.user.impl.RoleImpl;
import com.collibra.dgc.core.model.user.impl.UserImpl;
import com.collibra.dgc.core.model.user.impl.WebsiteImpl;
import com.collibra.dgc.core.service.ConfigurationService;

/**
 * The hibernate session factory that will get the necessary information from the configuration service.
 * @author dieterwachters
 */
public class DGCSessionFactory extends AnnotationSessionFactoryBean implements InitializingBean {

	private static final Class<?>[] annotatedClasses = new Class[] { ResourceImpl.class, CommunityImpl.class,
			MeaningImpl.class, ConceptImpl.class, ObjectTypeImpl.class, CategorizationTypeImpl.class,
			CategoryImpl.class, BinaryFactTypeImpl.class, CharacteristicImpl.class, PropositionImpl.class,
			SimplePropositionImpl.class, RuleImpl.class, RepresentationImpl.class, DesignationImpl.class,
			TermImpl.class, BinaryFactTypeFormImpl.class, CharacteristicFormImpl.class, AttributeImpl.class,
			StatementImpl.class, SimpleStatementImpl.class, RuleStatementImpl.class, SimpleRuleStatementImpl.class,
			SemiparsedRuleStatementImpl.class, FrequencyRuleStatementImpl.class, RuleSetImpl.class,
			ReadingDirectionImpl.class, MemberImpl.class, RoleImpl.class, VocabularyImpl.class, ModelRevision.class,
			StringAttributeImpl.class, MultiValueListAttributeImpl.class, DateTimeAttributeImpl.class,
			SingleValueListAttributeImpl.class, UserImpl.class, RelationImpl.class, JobImpl.class,
			ConfigurationCategoryImpl.class, AttributeAndRelationTypesConfigurationCategoryImpl.class, EmailImpl.class,
			PhoneNumberImpl.class, InstantMessagingAccountImpl.class, WebsiteImpl.class, AddressImpl.class,
			GroupImpl.class, GroupMembershipImpl.class };

	@Autowired
	private ConfigurationService config;
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private DataSource dataSource;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.orm.hibernate3.AbstractSessionFactoryBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		final String dialect = config.getString("core/datasource/dialect");
		setAnnotatedClasses(annotatedClasses);
		setDataSource(dataSource);
		final Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", dialect == null ? "org.hibernate.dialect.H2Dialect" : dialect);

		hibernateProperties.put("dbcp.defaultAutoCommit", "false");
		hibernateProperties.put("dbcp.maxActive", "50");
		hibernateProperties.put("dbcp.maxIdle", "5");
		hibernateProperties.put("dbcp.maxWait", "30000");
		hibernateProperties.put("dbcp.whenExhaustedAction", "1");
		hibernateProperties.put("dbcp.ps.whenExhaustedAction", "1");
		hibernateProperties.put("dbcp.ps.maxActive", "20");
		hibernateProperties.put("dbcp.ps.maxWait", "120000");
		hibernateProperties.put("dbcp.ps.maxIdle", "20");

		// hibernateProperties.put("hibernate.current_session_context_class", "thread");
		hibernateProperties.put("use_outer_join", "true");
		// Forcing to use cglib as proxy library instead of javassist to avoid duplicate method exception.
		System.setProperty("hibernate.bytecode.provider", "cglib");
		hibernateProperties.put("hibernate.bytecode.provider", "cglib");
		hibernateProperties.put("hibernate.connection.isolation", "2");

		final AuditEventListener enversListener = new AuditEventListener();
		final Map<String, Object> listeners = new HashMap<String, Object>();
		listeners.put("post-insert", enversListener);
		listeners.put("post-update", enversListener);
		listeners.put("post-delete", enversListener);
		listeners.put("pre-collection-update", enversListener);
		listeners.put("pre-collection-remove", enversListener);
		listeners.put("post-collection-recreate", enversListener);
		setEventListeners(listeners);

		// Override with the properties from the configuration.
		hibernateProperties.putAll(config.getProperties("core/datasource/hibernate-properties"));

		final Boolean dropCreate = config.getBoolean("/core/datasource/recreate");
		if (dropCreate != null && dropCreate) {
			hibernateProperties.put("hibernate.hbm2ddl.auto", "create");
		}
		// For developer edition, we show the SQL.
		if (Application.DEVELOPER) {
			if (dropCreate == null || !dropCreate) {
				hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
			}
			// hibernateProperties.put("hibernate.format_sql", "true");
			hibernateProperties.put("hibernate.show_sql", "true");
			hibernateProperties.put("use_sql_comments", "true");
		}
		setHibernateProperties(hibernateProperties);

		super.afterPropertiesSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.orm.hibernate3.LocalSessionFactoryBean#afterSessionFactoryCreation()
	 */
	@Override
	protected void afterSessionFactoryCreation() throws Exception {
		super.afterSessionFactoryCreation();
	}
}
