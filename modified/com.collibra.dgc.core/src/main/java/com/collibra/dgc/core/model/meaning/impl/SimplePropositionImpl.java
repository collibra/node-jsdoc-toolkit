package com.collibra.dgc.core.model.meaning.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.meaning.SimpleProposition;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Statement;
import com.collibra.dgc.core.model.representation.impl.SimpleStatementImpl;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
@DiscriminatorValue(value = "SI")
public class SimplePropositionImpl extends PropositionImpl implements SimpleProposition {

	private Set<SimpleStatement> simpleStatements = new HashSet<SimpleStatement>();

	public SimplePropositionImpl() {
	}

	@Transient
	public Set<Statement> getStatements() {
		return new HashSet<Statement>(simpleStatements);
	}

	@Transient
	public Set<? extends Representation> getRepresentations() {
		return getSimpleStatements();
	}

	@OneToMany(cascade = { CascadeType.MERGE }, targetEntity = SimpleStatementImpl.class)
	@JoinColumn(name = "MEANING")
	@AuditJoinTable(name = "SMPPROP_SMPSTM_AUD")
	public Set<SimpleStatement> getSimpleStatements() {
		return simpleStatements;
	}

	protected void setSimpleStatements(Set<SimpleStatement> simpleStatements) {
		this.simpleStatements = simpleStatements;
	}

	public void addSimpleStatement(SimpleStatement ss) {
		simpleStatements.add(ss);
	}

	public void addRepresentation(Representation representation) {
		if (!(representation instanceof SimpleStatement)) {
			throw new IllegalArgumentException("Expected a SimpleStatement");
		}
		addSimpleStatement((SimpleStatement) representation);
	}

	public void removeRepresentation(Representation representation) {
		simpleStatements.remove(representation);
	}

	@Override
	protected void initializeRelations() {
		simpleStatements = new HashSet<SimpleStatement>();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;

		if (!(obj instanceof SimpleProposition)) {
			return false;
		}
		return true;
	}
}
