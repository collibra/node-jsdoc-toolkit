package com.collibra.dgc.api.verbalizer;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.categorizations.impl.CategorizationTypeImpl;
import com.collibra.dgc.core.model.categorizations.impl.CategoryImpl;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.meaning.facttype.FactTypeRole;
import com.collibra.dgc.core.model.meaning.facttype.impl.BinaryFactTypeImpl;
import com.collibra.dgc.core.model.meaning.facttype.impl.CharacteristicImpl;
import com.collibra.dgc.core.model.meaning.facttype.impl.FactTypeRoleImpl;
import com.collibra.dgc.core.model.meaning.impl.ObjectTypeImpl;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;
import com.collibra.dgc.core.model.representation.impl.VocabularyImpl;
import com.collibra.dgc.core.model.rules.FrequencyRuleStatement;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.SemiparsedRuleStatement;
import com.collibra.dgc.core.model.rules.SimpleRuleStatement;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestVerbaliserComponentDefense extends AbstractDGCBootstrappedApiTest {
	private final Community nullCommunity = null;
	private final Attribute nullAttribute = null;
	private final Term nullTerm = null;
	private final BinaryFactTypeForm nullBinaryFactTypeForm = null;
	private final CharacteristicForm nullCharacteristicForm = null;
	private final FrequencyRuleStatement nullFrequencyRuleStatement = null;
	private final ReadingDirection nullReadingDirection = null;
	private final RuleSet nullRuleSet = null;
	private final SemiparsedRuleStatement nullSemiparsedRuleStatement = null;
	private final SimpleRuleStatement nullSimpleRuleStatement = null;
	private final SimpleStatement nullSimpleStatement = null;
	private final Vocabulary nullVocabulary = null;
	private final Vocabulary vocabulary = new VocabularyImpl();
	private final CategorizationType nullCategorizationType = null;
	private final CategorizationType categorizationType = new CategorizationTypeImpl("");
	private final BinaryFactType nullBinaryFactType = null;
	private final BinaryFactType binaryFactType = new BinaryFactTypeImpl();
	private final Category category = new CategoryImpl("", categorizationType);
	private final Category nullCategory = null;
	private final Characteristic characteristic = new CharacteristicImpl();
	private final Characteristic nullCharacteristic = null;
	private final FactTypeRole factTypeRole = new FactTypeRoleImpl();
	private final FactTypeRole nullFactTypeRole = null;
	private final ObjectType objecType = new ObjectTypeImpl();
	private final ObjectType nullObjecType = null;

	@Test
	public void testVerbaliseDefenseAttributeNull() {
		try {
			verbaliserComponent.verbalise(nullAttribute);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.ATTRIBUTE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbaliseDefenseBinaryFactTypeFormNull() {
		try {
			verbaliserComponent.verbalise(nullBinaryFactTypeForm);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbaliseDefenseCharacteristicFormNull() {
		try {
			verbaliserComponent.verbalise(nullCharacteristicForm);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CF_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbaliseDefenseCommunityNull() {
		try {
			verbaliserComponent.verbalise(nullCommunity);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbaliseDefenseFrequencyRuleStatementNull() {
		try {
			verbaliserComponent.verbalise(nullFrequencyRuleStatement);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULE_STATEMENT_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbaliseDefenseFrequencyReadingDirectionNull() {
		try {
			verbaliserComponent.verbalise(nullReadingDirection);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.ARGUMENT_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbaliseDefenseFrequencyRuleSetNull() {
		try {
			verbaliserComponent.verbalise(nullRuleSet);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbaliseDefenseSemiparsedRuleStatementNull() {
		try {
			verbaliserComponent.verbalise(nullSemiparsedRuleStatement);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULE_STATEMENT_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbaliseDefenseSimpleRuleStatementNull() {
		try {
			verbaliserComponent.verbalise(nullSimpleRuleStatement);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULE_STATEMENT_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbaliseDefenseSimpleStatementNull() {
		try {
			verbaliserComponent.verbalise(nullSimpleStatement);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULE_STATEMENT_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbaliseDefenseTermNull() {
		try {
			verbaliserComponent.verbalise(nullTerm);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbaliseDefenseVocabularyNull() {
		try {
			verbaliserComponent.verbalise(nullVocabulary);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbalise2DefenseVocabularyNull() {
		try {
			verbaliserComponent.verbalise(binaryFactType, nullVocabulary);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbalise2DefenseBinaryFactTypeNull() {
		try {
			verbaliserComponent.verbalise(nullBinaryFactType, vocabulary);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbalise3DefenseVocabularyNull() {
		try {
			verbaliserComponent.verbalise(categorizationType, nullVocabulary);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbalise3DefenseCategorizationTypeNull() {
		try {
			verbaliserComponent.verbalise(nullCategorizationType, vocabulary);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbalise4DefenseVocabularyNull() {
		try {
			verbaliserComponent.verbalise(category, nullVocabulary);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbalise4DefenseCategoryNull() {
		try {
			verbaliserComponent.verbalise(nullCategory, vocabulary);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbalise5DefenseVocabularyNull() {
		try {
			verbaliserComponent.verbalise(characteristic, nullVocabulary);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbalise5DefenseCharacteristicNull() {
		try {
			verbaliserComponent.verbalise(nullCharacteristic, vocabulary);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.ARGUMENT_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbalise6DefenseVocabularyNull() {
		try {
			verbaliserComponent.verbalise(factTypeRole, nullVocabulary);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbalise6DefenseConceptNull() {
		try {
			verbaliserComponent.verbalise(nullFactTypeRole, vocabulary);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONCEPT_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbalise7DefenseVocabularyNull() {
		try {
			verbaliserComponent.verbalise(objecType, nullVocabulary);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testVerbalise7DefenseObjectTypeNull() {
		try {
			verbaliserComponent.verbalise(nullObjecType, vocabulary);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.OBJECT_TYPE_NULL, ex.getErrorCode());
		}
	}
}
