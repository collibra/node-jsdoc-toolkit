package com.collibra.dgc.core.component;

import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;
import com.collibra.dgc.core.model.rules.FrequencyRuleStatement;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.SemiparsedRuleStatement;
import com.collibra.dgc.core.model.rules.SimpleRuleStatement;

/**
 * The verbaliserComponent implementations can verbalise each of our domain objects suited for pure textual purposes or
 * browser purposes.
 * @see HyperLinkedVerbaliserImpl
 * 
 * @author dtrog
 * 
 */
public interface VerbaliserComponent {
	String verbalise(Community semanticCommunity);

	String verbalise(Term term);

	String verbalise(BinaryFactTypeForm binaryFactTypeForm);

	String verbalise(CharacteristicForm characteristicForm);

	String verbalise(Vocabulary vocabulary);

	String verbalise(FrequencyRuleStatement frequencyRuleStatement);

	String verbalise(RuleSet ruleSet);

	String verbalise(SemiparsedRuleStatement semiparsedRuleStatement);

	String verbalise(SimpleRuleStatement simpleRuleStatment);

	String verbalise(Attribute attribute);

	String verbalise(SimpleStatement simpleStatement);

	String verbalise(ReadingDirection placeHolder);

	String verbalise(ObjectType objecType, Vocabulary vocabulary);

	String verbalise(BinaryFactType binaryFactType, Vocabulary vocabulary);

	String verbalise(Characteristic characteristic, Vocabulary vocabulary);

	String verbalise(Category category, Vocabulary vocabulary);

	String verbalise(Concept categorizationScheme, Vocabulary vocabulary);

	String verbalise(CategorizationType categorizationType, Vocabulary vocabulary);
}
