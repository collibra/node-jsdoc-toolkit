package com.collibra.dgc.core.model.representation.impl;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.Proposition;
import com.collibra.dgc.core.model.representation.Statement;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
public abstract class StatementImpl extends RepresentationImpl implements Statement {

	private Proposition proposition;

	public StatementImpl() {
		super();
	}

	public StatementImpl(Vocabulary vocabulary) {
		super(vocabulary, true);
	}

	public StatementImpl(Vocabulary vocabulary, Proposition proposition) {
		super(vocabulary, true);
		setProposition(proposition);
	}

	public StatementImpl(Statement statement) {
		super(statement);
		setProposition(statement.getProposition());
	}

	@Transient
	public Proposition getProposition() {
		return proposition;
	}

	protected void setProposition(Proposition proposition) {
		checkConstraint();
		this.proposition = proposition;
	}

	@Transient
	public Meaning getMeaning() {
		return proposition;
	}

	@Override
	protected void initializeRelations() {
		super.initializeRelations();
	}
}
