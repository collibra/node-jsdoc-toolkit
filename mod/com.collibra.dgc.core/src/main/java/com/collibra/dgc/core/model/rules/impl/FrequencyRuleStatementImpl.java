package com.collibra.dgc.core.model.rules.impl;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;
import com.collibra.dgc.core.model.representation.impl.RepresentationImpl;
import com.collibra.dgc.core.model.rules.FrequencyRuleStatement;

/**
 * Concrete class for {@link FrequencyRuleStatement}
 * @author amarnath
 * @author pmalarme
 * 
 */
@Entity
@Audited
@DiscriminatorValue(value = "FQR")
public class FrequencyRuleStatementImpl extends RuleStatementImpl implements FrequencyRuleStatement {
	private int max = DO_NOT_USE;
	private int min = DO_NOT_USE;

	public FrequencyRuleStatementImpl() {
		super();
	}

	public FrequencyRuleStatementImpl(Vocabulary vocabulary) {
		super(vocabulary);
	}

	public FrequencyRuleStatementImpl(FrequencyRuleStatementImpl freqRuleSt) {
		super(freqRuleSt);
		this.max = freqRuleSt.getMax();
		this.min = freqRuleSt.getMin();
	}

	@Override
	public RepresentationImpl clone() {
		return new FrequencyRuleStatementImpl(this);
	}

	@Override
	public void setMeaning(Meaning meaning) {
		throw new RuntimeException("Not implemented yet");
	}

	public String verbalise() {
		List<String> keywords = new LinkedList<String>();
		if (min == max) {
			keywords.add("exactly " + min + " ");
		} else {
			if (min != DO_NOT_USE && min != 0) {
				keywords.add("at least " + min + " ");
			}

			if (max != DO_NOT_USE && max != 0) {
				keywords.add("at most " + max + " ");
			}
		}

		if (keywords.size() == 0) {
			return "Frequencey Rule Statement";
		}

		StringBuilder sb = new StringBuilder();

		for (SimpleStatement ss : getSimpleStatements()) {
			for (String keyword : keywords) {
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

				sb.append(". ");
			}
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		return verbalise();
	}

	@Column(name = "MAX")
	@Access(value = AccessType.FIELD)
	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	@Column(name = "MIN")
	@Access(value = AccessType.FIELD)
	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + max;
		result = prime * result + min;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null) {
			return false;
		}

		if (!(obj instanceof FrequencyRuleStatement)) {
			return false;
		}

		FrequencyRuleStatement other = (FrequencyRuleStatement) obj;
		if (max != other.getMax())
			return false;
		if (min != other.getMin())
			return false;

		if (getSimpleStatements() == null) {
			if (other.getSimpleStatements() != null)
				return false;
		} else if (!getSimpleStatements().equals(other.getSimpleStatements()))
			return false;

		return true;
	}

	@Override
	@Transient
	public String getRepresentationType() {
		return "FrequencyRuleStatement";
	}
}
