package com.collibra.dgc.core.model.rules.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang.NotImplementedException;
import org.hibernate.annotations.Target;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.Proposition;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.impl.SimpleStatementImpl;
import com.collibra.dgc.core.model.representation.impl.StatementImpl;
import com.collibra.dgc.core.model.rules.Rule;
import com.collibra.dgc.core.model.rules.RuleStatement;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
public abstract class RuleStatementImpl extends StatementImpl implements RuleStatement {

	private Rule rule;

	private Set<SimpleStatement> simpleStatements = new HashSet<SimpleStatement>();

	public RuleStatementImpl(RuleStatement ruleStatement) {
		super(ruleStatement);
		setRule(rule);

		simpleStatements = new HashSet<SimpleStatement>();
		for (SimpleStatement ss : ruleStatement.getSimpleStatements()) {
			simpleStatements.add((SimpleStatement) ss.clone());
		}
	}

	public RuleStatementImpl() {
		super();
	}

	public RuleStatementImpl(Vocabulary vocabulary) {
		super(vocabulary);
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	@ManyToOne
	@JoinColumn(name = "MEANING")
	@Access(value = AccessType.FIELD)
	@Target(value = RuleImpl.class)
	public Rule getRule() {
		return rule;
	}

	@Override
	@Transient
	public Proposition getProposition() {
		return rule;
	}

	@Override
	@Transient
	public Meaning getMeaning() {
		return rule;
	}

	@OneToMany(targetEntity = SimpleStatementImpl.class)
	@JoinTable(name = "RULESTM_SIMPLESTMS", joinColumns = { @JoinColumn(name = "RULESTM_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "SMPSTM_ID", referencedColumnName = "ID") })
	@Access(value = AccessType.FIELD)
	public Set<SimpleStatement> getSimpleStatements() {
		return simpleStatements;
	}

	public void setSimpleStatements(Set<SimpleStatement> simpleStatements) {
		this.simpleStatements = simpleStatements;
	}

	public void addSimpleStatement(SimpleStatement simpleStatement) {
		simpleStatements.add(simpleStatement);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		return result;
	}

	public Representation changeMeaning(Meaning meaning) {
		throw new NotImplementedException();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		RuleStatement other = (RuleStatement) obj;

		if (simpleStatements == null) {
			if (other.getSimpleStatements() != null)
				return false;
		} else if (!simpleStatements.equals(other.getSimpleStatements()))
			return false;
		return true;
	}
}
