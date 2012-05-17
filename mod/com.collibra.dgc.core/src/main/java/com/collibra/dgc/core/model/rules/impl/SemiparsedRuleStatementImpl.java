package com.collibra.dgc.core.model.rules.impl;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.rules.SemiparsedRuleStatement;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
@DiscriminatorValue(value = "SPR")
public class SemiparsedRuleStatementImpl extends RuleStatementImpl implements SemiparsedRuleStatement {
	private String unparsed;

	public SemiparsedRuleStatementImpl() {
		super();
	}

	public SemiparsedRuleStatementImpl(Vocabulary vocabulary) {
		super(vocabulary);
	}

	public SemiparsedRuleStatementImpl(SemiparsedRuleStatement semiparsedRuleStatement) {
		super(semiparsedRuleStatement);
		setUnparsed(unparsed);
	}

	@Lob
	@Column(name = "UNPARSED")
	@Access(value = AccessType.FIELD)
	public String getUnparsed() {
		return unparsed;
	}

	public void setUnparsed(String unparsed) {
		this.unparsed = unparsed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getUnparsed() == null) ? 0 : getUnparsed().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (!(obj instanceof SemiparsedRuleStatement)) {
			return false;
		}

		SemiparsedRuleStatement other = (SemiparsedRuleStatement) obj;

		if (getSimpleStatements() == null) {
			if (other.getSimpleStatements() != null)
				return false;
		} else if (!getSimpleStatements().equals(other.getSimpleStatements()))
			return false;

		if (unparsed == null) {
			if (other.getUnparsed() != null)
				return false;
		} else if (!unparsed.equals(other.getUnparsed()))
			return false;
		return true;
	}

	@Override
	public SemiparsedRuleStatementImpl clone() {
		return new SemiparsedRuleStatementImpl(this);
	}

	@Override
	public void setMeaning(Meaning meaning) {
		throw new RuntimeException("Not implemented yet");
	}

	public String verbalise() {
		return unparsed;
	}

	@Override
	public String toString() {
		return verbalise();
	}

	@Override
	@Transient
	public String getRepresentationType() {
		return "SemiparsedRuleStatement";
	}
}
