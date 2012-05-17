package com.collibra.dgc.core.model.rules.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Target;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.impl.VocabularyImpl;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.RuleStatement;

@Entity
@Audited
@Table(name = "RULESETS")
public class RuleSetImpl extends ResourceImpl implements RuleSet, Comparable<RuleSet> {

	private Vocabulary vocabulary;

	private String name;

	private Set<RuleStatement> ruleStatements;

	public RuleSetImpl() {
		super();
	}

	@Override
	protected void initializeRelations() {
		ruleStatements = new HashSet<RuleStatement>();
	}

	public RuleSetImpl(Vocabulary vocabulary, String name) {
		super();
		this.vocabulary = vocabulary;
		this.name = name;
	}

	public RuleSetImpl(RuleSetImpl ruleSetImpl) {
		super(ruleSetImpl);
		setVocabulary(ruleSetImpl.getVocabulary());
		setName(ruleSetImpl.getName());
	}

	public void addRuleStatement(RuleStatement ruleStatement) {
		if (!ruleStatements.add(ruleStatement)) {
			System.out.println();
		}
	}

	@OneToMany(targetEntity = RuleStatementImpl.class)
	@JoinTable(name = "RULESET_RULESTMTS", joinColumns = { @JoinColumn(name = "RULESET", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "RULESTMT", referencedColumnName = "ID") })
	@NotFound(action = NotFoundAction.IGNORE)
	public Set<RuleStatement> getRuleStatements() {
		return ruleStatements;
	}

	public void setRuleStatements(Set<RuleStatement> ruleStatements) {
		this.ruleStatements = ruleStatements;
	}

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "VOCABULARY")
	@Target(value = VocabularyImpl.class)
	public Vocabulary getVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(Vocabulary vocabulary) {
		this.vocabulary = vocabulary;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof RuleSet)
			return false;
		RuleSetImpl other = (RuleSetImpl) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (vocabulary == null) {
			if (other.vocabulary != null)
				return false;
		} else if (!vocabulary.equals(other.vocabulary))
			return false;

		return true;
	}

	@Override
	public RuleSetImpl clone() {
		return new RuleSetImpl(this);
	}

	public String verbalise() {
		return getName();
	}

	public int compareTo(RuleSet o) {
		return name.compareTo(o.getName());
	}
}
