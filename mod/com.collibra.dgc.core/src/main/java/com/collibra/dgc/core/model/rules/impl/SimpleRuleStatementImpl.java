package com.collibra.dgc.core.model.rules.impl;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;
import com.collibra.dgc.core.model.rules.Rule;
import com.collibra.dgc.core.model.rules.SimpleRuleStatement;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
@DiscriminatorValue(value = "SRS")
public class SimpleRuleStatementImpl extends RuleStatementImpl implements SimpleRuleStatement {
	public SimpleRuleStatementImpl() {
		super();
	}

	public SimpleRuleStatementImpl(Vocabulary vocabulary) {
		super(vocabulary);
	}

	public SimpleRuleStatementImpl(SimpleRuleStatement ruleStatement) {
		super(ruleStatement);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (!(obj instanceof SimpleRuleStatement)) {
			return false;
		}

		SimpleRuleStatement other = (SimpleRuleStatement) obj;

		// Check the rule type as well.
		if (!getRule().equals(other.getRule())) {
			return false;
		}

		if (getSimpleStatements() == null) {
			if (other.getSimpleStatements() != null)
				return false;
		} else if (!getSimpleStatements().equals(other.getSimpleStatements()))
			return false;

		// Check the rule type as well. This is needed as and when extend on the constraint type.
		if (!getRule().equals(other.getRule())) {
			return false;
		}

		return true;
	}

	@Override
	public SimpleRuleStatementImpl clone() {
		return new SimpleRuleStatementImpl(this);
	}

	@Override
	public void setMeaning(Meaning meaning) {
		throw new RuntimeException("Not implemented yet");
	}

	public String verbalise() {
		String keyword = "";
		if (Rule.UNIQUENESS.equals(getRule().getGlossaryConstraintType())) {
			keyword = "at most one";
		} else if (Rule.MANDATORY.equals(getRule().getGlossaryConstraintType())) {
			keyword = "at least one";
		}

		StringBuilder sb = new StringBuilder();
		int count = 1;
		for (SimpleStatement ss : getSimpleStatements()) {
			List<ReadingDirection> readingDirections = ss.getReadingDirections();
			if (readingDirections.size() > 0) {
				ReadingDirection firstPh = readingDirections.get(0);
				sb.append(firstPh.getHeadTerm().getSignifier()).append(" ").append(firstPh.getRole()).append(" ")
						.append(keyword).append(" ").append(firstPh.getTailTerm().getSignifier()).append(" ");
			}

			for (int i = 1; i < readingDirections.size(); i++) {
				ReadingDirection ph = readingDirections.get(i);
				sb.append(ph.verbalise()).append(" ");
			}

			if (count < getSimpleStatements().size()) {
				sb.append(ss.verbalise()).append(". ");
			}

			count++;
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return verbalise();
	}

	@Override
	@Transient
	public String getRepresentationType() {
		return "SimpleRuleStatement";
	}
}
