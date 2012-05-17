package com.collibra.dgc.core.model.rules.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Target;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.impl.ObjectTypeImpl;
import com.collibra.dgc.core.model.meaning.impl.PropositionImpl;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Statement;
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
@DiscriminatorValue(value = "RU")
public class RuleImpl extends PropositionImpl implements Rule {

	private Set<RuleStatement> ruleStatements;

	private ObjectType constraintType;

	private String glossaryConstraintType;

	public RuleImpl() {
		this(UUID.randomUUID().toString());
	}

	public RuleImpl(String uuid) {
		super(uuid);
	}

	@Transient
	public Set<Statement> getStatements() {
		Set<Statement> statements = new HashSet<Statement>();
		statements.addAll(ruleStatements);
		return statements;
	}

	@ManyToOne
	@JoinColumn(name = "CONSTRAINTTYPE")
	@Target(value = ObjectTypeImpl.class)
	public ObjectType getConstraintType() {
		return constraintType;
	}

	public void setConstraintType(ObjectType constraintType) {
		this.constraintType = constraintType;
	}

	public void setRuleStatements(Set<RuleStatement> ruleStatements) {
		this.ruleStatements = ruleStatements;
	}

	@Transient
	public Set<? extends Representation> getRepresentations() {
		return getRuleStatements();
	}

	@OneToMany(cascade = { CascadeType.MERGE }, targetEntity = RuleStatementImpl.class)
	@JoinColumn(name = "MEANING")
	public Set<RuleStatement> getRuleStatements() {
		return ruleStatements;
	}

	@Transient
	public ObjectType getContraintType() {
		return constraintType;
	}

	public void setContraintType(ObjectType constraintType) {
		this.constraintType = constraintType;
	}

	@Override
	protected void initializeRelations() {
		ruleStatements = new HashSet<RuleStatement>();
	}

	@Column(name = "RULETYPE")
	public String getGlossaryConstraintType() {
		return glossaryConstraintType;
	}

	public void setGlossaryConstraintType(String glossaryConstraintType) {
		this.glossaryConstraintType = glossaryConstraintType;
	}

	public void addRepresentation(Representation representation) {
		if (!(representation instanceof RuleStatement)) {
			throw new IllegalArgumentException("Expected a RuleStatement");
		}
		ruleStatements.add((RuleStatement) representation);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getGlossaryConstraintType() == null) ? 0 : getGlossaryConstraintType().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;

		Rule other = (Rule) obj;

		if (glossaryConstraintType == null) {
			if (other.getGlossaryConstraintType() != null)
				return false;
		} else if (!glossaryConstraintType.equals(other.getGlossaryConstraintType()))
			return false;
		return true;
	}
}
