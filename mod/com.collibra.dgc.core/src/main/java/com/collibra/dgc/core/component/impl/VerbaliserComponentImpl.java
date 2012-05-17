package com.collibra.dgc.core.component.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.component.VerbaliserComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
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
import com.collibra.dgc.core.model.rules.Rule;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.SemiparsedRuleStatement;
import com.collibra.dgc.core.model.rules.SimpleRuleStatement;
import com.collibra.dgc.core.util.CategorizationUtil;
import com.collibra.dgc.core.util.Defense;

@Service
public class VerbaliserComponentImpl implements VerbaliserComponent {
	@Override
	public String verbalise(Community community) {
		Defense.notNull(community, DGCErrorCodes.COMMUNITY_NULL);
		return community.verbalise();
	}

	@Override
	public String verbalise(Term term) {
		Defense.notNull(term, DGCErrorCodes.TERM_NULL);
		return term.verbalise();
	}

	@Override
	public String verbalise(BinaryFactTypeForm binaryFactTypeForm) {
		Defense.notNull(binaryFactTypeForm, DGCErrorCodes.BFTF_NULL);
		StringBuilder sb = new StringBuilder();
		sb.append(verbalise(binaryFactTypeForm.getHeadTerm()));
		sb.append(" ");
		sb.append(binaryFactTypeForm.getRole());
		sb.append(" / ");
		sb.append(binaryFactTypeForm.getCoRole());
		sb.append(" ");
		sb.append(verbalise(binaryFactTypeForm.getTailTerm()));
		return sb.toString();
	}

	@Override
	public String verbalise(CharacteristicForm characteristicForm) {
		Defense.notNull(characteristicForm, DGCErrorCodes.CF_NULL);
		StringBuilder sb = new StringBuilder();
		sb.append(verbalise(characteristicForm.getTerm()));
		sb.append(" ");
		sb.append(characteristicForm.getRole());
		return sb.toString();
	}

	@Override
	public String verbalise(Vocabulary vocabulary) {
		Defense.notNull(vocabulary, DGCErrorCodes.VOCABULARY_NULL);
		return vocabulary.verbalise();
	}

	private String paranthesize(String element) {
		return "(" + element + ")";
	}

	@Override
	public String verbalise(FrequencyRuleStatement frequencyRuleStatement) {
		Defense.notNull(frequencyRuleStatement, DGCErrorCodes.RULE_STATEMENT_NULL);

		List<String> keywords = new LinkedList<String>();
		int min = frequencyRuleStatement.getMin();
		int max = frequencyRuleStatement.getMax();
		if (min == max) {
			keywords.add("exactly " + min + " ");
		} else {
			if (min != FrequencyRuleStatement.DO_NOT_USE && min != 0) {
				keywords.add("at least " + min + " ");
			}

			if (max != FrequencyRuleStatement.DO_NOT_USE && max != 0) {
				keywords.add("at most " + max + " ");
			}
		}

		StringBuilder sb = new StringBuilder();
		Iterator<String> keywordIter = keywords.iterator();

		while (keywordIter.hasNext()) {
			String keyword = keywordIter.next();
			if (frequencyRuleStatement.getSimpleStatements().size() != 1) {
				// FIXME multi statement freq constraint unsupported
				return null;
			}
			String statement = verbalise(frequencyRuleStatement.getSimpleStatements().iterator().next(), keyword);

			if (keywords.size() > 1) {
				statement = paranthesize(statement);
				sb.append(statement);
				if (keywordIter.hasNext()) {
					sb.append(" and ");
				}
			} else {
				sb.append(statement);
			}
		}
		sb.append(".");

		return sb.toString();
	}

	@Override
	public String verbalise(RuleSet ruleSet) {
		Defense.notNull(ruleSet, DGCErrorCodes.RULESET_NULL);
		return ruleSet.verbalise();
	}

	@Override
	public String verbalise(SemiparsedRuleStatement semiparsedRuleStatement) {
		Defense.notNull(semiparsedRuleStatement, DGCErrorCodes.RULE_STATEMENT_NULL);
		return semiparsedRuleStatement.verbalise();
	}

	@Override
	public String verbalise(SimpleRuleStatement simpleRuleStatment) {
		Defense.notNull(simpleRuleStatment, DGCErrorCodes.RULE_STATEMENT_NULL);
		String keyword = "";

		if (simpleRuleStatment.getSimpleStatements().size() != 1) {
			// FIXME combined constraints not supported yet
			return null;
		}

		if (Rule.UNIQUENESS.equals(simpleRuleStatment.getRule().getGlossaryConstraintType())) {
			keyword = "at most one";
		} else if (Rule.MANDATORY.equals(simpleRuleStatment.getRule().getGlossaryConstraintType())) {
			keyword = "at least one";
		}
		return verbalise(simpleRuleStatment.getSimpleStatements().iterator().next(), keyword) + ".";
	}

	@Override
	public String verbalise(Attribute attribute) {
		Defense.notNull(attribute, DGCErrorCodes.ATTRIBUTE_NULL);
		StringBuilder sb = new StringBuilder();
		sb.append(verbalise(attribute.getLabel()));
		sb.append(": ");
		sb.append(attribute.getValue());
		return sb.toString();
	}

	/**
	 * Verbalises the simplestatement.
	 * 
	 * @param simpleStatement The statement to verbalise
	 * @param keyword The keyword to put after the role (at least one, exactly one, etc.)
	 * @param isFinal
	 * @return
	 */
	public String verbalise(SimpleStatement simpleStatement, String keyword) {
		Defense.notNull(simpleStatement, DGCErrorCodes.SIMPLE_STATEMENT_NULL);

		StringBuilder sb = new StringBuilder();
		Iterator<ReadingDirection> placeholderIter = simpleStatement.getReadingDirections().iterator();
		int count = 1;
		while (placeholderIter.hasNext()) {
			ReadingDirection placeholder = placeholderIter.next();
			sb.append(verbalise(placeholder.getHeadTerm()));
			sb.append(" ");
			sb.append(placeholder.getRole());
			sb.append(" ");
			if (count == 1 && keyword != null) {
				sb.append(keyword);
				sb.append(" ");
			}
			// avoid repeating head and tail when they are the same and end with a .
			if (!placeholderIter.hasNext()) {
				sb.append(verbalise(placeholder.getTailTerm()));
			}
			count++;
		}
		return sb.toString();
	}

	@Override
	public String verbalise(SimpleStatement simpleStatement) {
		Defense.notNull(simpleStatement, DGCErrorCodes.RULE_STATEMENT_NULL);
		return verbalise(simpleStatement, null) + ".";
	}

	@Override
	public String verbalise(ReadingDirection placeholder) {
		Defense.notNull(placeholder, DGCErrorCodes.ARGUMENT_NULL, "placeholder");
		StringBuilder sb = new StringBuilder();
		sb.append(verbalise(placeholder.getHeadTerm()));
		sb.append(" ");
		sb.append(placeholder.getRole());
		sb.append(" ");
		sb.append(verbalise(placeholder.getTailTerm()));
		return sb.toString();
	}

	@Override
	public String verbalise(ObjectType objecType, Vocabulary vocabulary) {
		Defense.notNull(objecType, DGCErrorCodes.OBJECT_TYPE_NULL);
		Defense.notNull(vocabulary, DGCErrorCodes.VOCABULARY_NULL);
		Term term = vocabulary.getPreferredTerm(objecType);
		return verbalise(term);
	}

	@Override
	public String verbalise(BinaryFactType binaryFactType, Vocabulary vocabulary) {
		Defense.notNull(vocabulary, DGCErrorCodes.VOCABULARY_NULL);
		Defense.notNull(binaryFactType, DGCErrorCodes.BFTF_NULL);
		BinaryFactTypeForm bftf = vocabulary.getPreferredBinaryFactTypeForm(binaryFactType);
		return verbalise(bftf);
	}

	@Override
	public String verbalise(Characteristic characteristic, Vocabulary vocabulary) {
		Defense.notNull(vocabulary, DGCErrorCodes.VOCABULARY_NULL);
		Defense.notNull(characteristic, DGCErrorCodes.ARGUMENT_NULL, "characteristic");
		CharacteristicForm cForm = vocabulary.getPreferredCharacteristicForm(characteristic);
		return verbalise(cForm);
	}

	@Override
	public String verbalise(Category category, Vocabulary vocabulary) {
		Defense.notNull(vocabulary, DGCErrorCodes.VOCABULARY_NULL);
		Defense.notNull(category, DGCErrorCodes.CATEGORY_NULL);
		Term term = vocabulary.getPreferredTerm(category);
		return verbalise(term);
	}

	@Override
	public String verbalise(Concept categorizationScheme, Vocabulary vocabulary) {
		Defense.notNull(vocabulary, DGCErrorCodes.VOCABULARY_NULL);
		Defense.notNull(categorizationScheme, DGCErrorCodes.CONCEPT_NULL);
		StringBuilder sb = new StringBuilder();
		Term schemeTerm = vocabulary.getPreferredTerm(categorizationScheme);
		MultiValueMap catTypeTermToCategory = CategorizationUtil.getCategorizationTypeToCategory(categorizationScheme,
				vocabulary);

		sb.append(verbalise(schemeTerm));

		for (Object obj : catTypeTermToCategory.keySet()) {
			CategorizationType categorizationType = (CategorizationType) obj;
			ObjectType forObjectType = categorizationType.getIsForConcept();
			sb.append(" : Categorization scheme of the concept ");
			sb.append(verbalise(forObjectType, vocabulary));
			sb.append(" categorized by ");
			sb.append(verbalise(categorizationType, vocabulary));
			sb.append(" into categories ");
			for (Object category : catTypeTermToCategory.getCollection(obj)) {
				sb.append(verbalise((Category) category, vocabulary));
				sb.append(", ");
			}
			// cleanup the last comma if there were any categories
			if (!catTypeTermToCategory.getCollection(obj).isEmpty()) {
				sb.delete(sb.length() - 2, sb.length() - 1);
			}
		}
		return sb.toString();
	}

	@Override
	public String verbalise(CategorizationType categorizationType, Vocabulary vocabulary) {
		Defense.notNull(vocabulary, DGCErrorCodes.VOCABULARY_NULL);
		Defense.notNull(categorizationType, DGCErrorCodes.CATEGORIZATION_TYPE_NULL);
		Term term = vocabulary.getPreferredTerm(categorizationType);
		return verbalise(term);
	}
}
