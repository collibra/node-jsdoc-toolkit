package com.collibra.dgc.api;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;

import com.collibra.dgc.AbstractDGCBootstrappedTest;
import com.collibra.dgc.core.component.CategorizationComponent;
import com.collibra.dgc.core.component.CommunityComponent;
import com.collibra.dgc.core.component.ConfigurationCategoryComponent;
import com.collibra.dgc.core.component.ConfigurationComponent;
import com.collibra.dgc.core.component.GroupComponent;
import com.collibra.dgc.core.component.HistoryComponent;
import com.collibra.dgc.core.component.RightsComponent;
import com.collibra.dgc.core.component.RulesComponent;
import com.collibra.dgc.core.component.SchedulerComponent;
import com.collibra.dgc.core.component.SearchComponent;
import com.collibra.dgc.core.component.StatusComponent;
import com.collibra.dgc.core.component.UserComponent;
import com.collibra.dgc.core.component.VerbaliserComponent;
import com.collibra.dgc.core.component.VocabularyComponent;
import com.collibra.dgc.core.component.WorkflowComponent;
import com.collibra.dgc.core.component.attribute.AttributeComponent;
import com.collibra.dgc.core.component.attribute.AttributeTypeComponent;
import com.collibra.dgc.core.component.bootstrap.BootstrapComponent;
import com.collibra.dgc.core.component.i18n.I18nComponent;
import com.collibra.dgc.core.component.job.JobComponent;
import com.collibra.dgc.core.component.relation.RelationComponent;
import com.collibra.dgc.core.component.relation.RelationTypeComponent;
import com.collibra.dgc.core.component.representation.BinaryFactTypeFormComponent;
import com.collibra.dgc.core.component.representation.CharacteristicFormComponent;
import com.collibra.dgc.core.component.representation.RepresentationComponent;
import com.collibra.dgc.core.component.representation.TermComponent;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.dao.UserDao;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.impl.ObjectTypeImpl;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.service.MeaningService;
import com.collibra.dgc.core.service.UserService;

/**
 * Abstract test class for component APIs.
 * @author amarnath
 * 
 */
@Ignore
public class AbstractDGCBootstrappedApiTest extends AbstractDGCBootstrappedTest {
	@Autowired
	protected CommunityComponent communityComponent;

	@Autowired
	protected VocabularyComponent vocabularyComponent;

	@Autowired
	protected UserComponent userComponent;

	@Autowired
	protected AttributeTypeComponent attributeTypeComponent;

	@Autowired
	protected StatusComponent statusComponent;

	@Autowired
	@Qualifier("RepresentationComponentImpl")
	protected RepresentationComponent representationComponent;

	@Autowired
	protected TermComponent termComponent;

	@Autowired
	protected BinaryFactTypeFormComponent binaryFactTypeFormComponent;

	@Autowired
	protected CharacteristicFormComponent characteristicFormComponent;

	@Autowired
	protected AttributeComponent attributeComponent;

	@Autowired
	protected RulesComponent rulesComponent;

	@Autowired
	protected CategorizationComponent categorizationComponent;

	@Autowired
	protected RightsComponent rightsComponent;

	@Autowired
	protected SchedulerComponent schedulerComponent;

	@Autowired
	protected SearchComponent searchComponent;

	@Autowired
	protected WorkflowComponent workflowComponent;

	@Autowired
	protected HistoryComponent historyComponent;

	@Autowired
	protected MeaningService meaningService;

	@Autowired
	protected PlatformTransactionManager txManager;

	@Autowired
	protected ObjectTypeDao objectTypeDao;

	@Autowired
	protected BootstrapComponent bootstrapComponent;

	@Autowired
	protected I18nComponent i18nComponent;

	@Autowired
	protected JobComponent jobComponent;

	@Autowired
	protected RelationTypeComponent relationTypeComponent;

	@Autowired
	protected RelationComponent relationComponent;

	@Autowired
	protected ConfigurationCategoryComponent configurationCategoryComponent;

	@Autowired
	protected VerbaliserComponent verbaliserComponent;

	@Autowired
	protected ConfigurationComponent configurationComponent;

	@Autowired
	protected UserDao userDao;

	@Autowired
	protected UserService userService;

	@Autowired
	protected GroupComponent groupComponent;

	protected final String COMMUNITY_NAME = "Community Test";
	protected final String COMMUNITY_URI = "Community Test URI";

	protected final String VOCABULARY_NAME = "Vocabulary Test";
	protected final String VOCABULARY_URI = "Vocabulary Test URI";

	protected final String TERM_SIGNIFIER = "Term Test";

	protected final String ATTR_TYPE_SIGNIFIER = "MyAttribute";
	protected final String ATTR_TYPE_DESCRIPTION = "MyAttributeDescription";

	protected final String RULESET_NAME = "RuleSet";

	protected Community createCommunity() {
		return communityComponent.addCommunity(COMMUNITY_NAME, COMMUNITY_URI);
	}

	protected Vocabulary createVocabulary() {
		return vocabularyComponent.addVocabulary(createCommunity().getId().toString(), VOCABULARY_NAME, VOCABULARY_URI);
	}

	protected Term createTerm() {
		return termComponent.addTerm(createVocabulary().getId().toString(), TERM_SIGNIFIER);
	}

	protected Term createStringAttributeType() {
		return attributeTypeComponent.addStringAttributeType(ATTR_TYPE_SIGNIFIER + "String", ATTR_TYPE_DESCRIPTION);
	}

	protected Term createSingleValueListAttributeType() {
		return attributeTypeComponent.addValueListAttributeType(ATTR_TYPE_SIGNIFIER + "SingleValueList",
				ATTR_TYPE_DESCRIPTION, false, createAttributeAllowedValues());
	}

	protected Term createMultiValueListAttributeType() {
		return attributeTypeComponent.addValueListAttributeType(ATTR_TYPE_SIGNIFIER + "MultiValueList",
				ATTR_TYPE_DESCRIPTION, true, createAttributeAllowedValues());
	}

	protected List<String> createAttributeAllowedValues() {

		List<String> values = new ArrayList<String>(3);

		values.add("A");
		values.add("B");
		values.add("C");

		return values;
	}

	protected String createAttributeLongExpression() {
		return "Long expression. Long expression." + "Long expression. Long expression."
				+ "Long expression. Long expression." + "Long expression. Long expression."
				+ "Long expression. Long expression." + "Long expression. Long expression."
				+ "Long expression. Long expression." + "Long expression. Long expression."
				+ "Long expression. Long expression." + "Long expression. Long expression."
				+ "Long expression. Long expression." + "Long expression. Long expression."
				+ "Long expression. Long expression." + "Long expression. Long expression."
				+ "Long expression. Long expression." + "Long expression. Long expression."
				+ "Long expression. Long expression." + "Long expression. Long expression."
				+ "Long expression. Long expression." + "Long expression. Long expression."
				+ "Long expression. Long expression." + "Long expression. Long expression."
				+ "Long expression. Long expression." + "Long expression. Long expression."
				+ "Long expression. Long expression." + "Long expression. Long expression.";
	}

	protected RuleSet createRuleSet() {
		return rulesComponent.addRuleSet(createVocabulary().getId().toString(), RULESET_NAME);
	}

	protected ObjectType createObjectType() {
		return objectTypeDao.save(new ObjectTypeImpl());
	}
}
