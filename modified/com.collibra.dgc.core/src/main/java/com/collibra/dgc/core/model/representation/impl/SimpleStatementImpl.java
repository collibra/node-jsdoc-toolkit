package com.collibra.dgc.core.model.representation.impl;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;

import org.hibernate.annotations.Target;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.Proposition;
import com.collibra.dgc.core.model.meaning.SimpleProposition;
import com.collibra.dgc.core.model.meaning.impl.SimplePropositionImpl;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;
import com.collibra.dgc.core.model.representation.facttypeform.impl.ReadingDirectionImpl;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
@DiscriminatorValue(value = "SS")
public class SimpleStatementImpl extends StatementImpl implements SimpleStatement {

	private List<ReadingDirection> readingDirections;

	private SimpleProposition simpleProposition;

	public SimpleStatementImpl() {
		super();
	}

	public SimpleStatementImpl(Vocabulary voc) {
		super(voc);
	}

	public SimpleStatementImpl(SimpleStatement simpleStatement) {
		super(simpleStatement);
		setSimpleProposition(simpleProposition);
		setLock(simpleStatement.isLocked());
	}

	@OneToMany(cascade = { CascadeType.ALL }, targetEntity = ReadingDirectionImpl.class)
	@JoinTable(name = "SIMPLESTM_READDIR", joinColumns = { @JoinColumn(name = "SMPSTM_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "READDIR_ID", referencedColumnName = "ID") })
	@OrderColumn(name = "READINGDIRECTIONSIDX")
	@Access(value = AccessType.FIELD)
	public List<ReadingDirection> getReadingDirections() {
		return readingDirections;
	}

	public void addReadingDirection(ReadingDirection rd) {
		checkConstraint(rd);
		readingDirections.add(rd);
	}

	protected void setReadingDirections(List<ReadingDirection> readingDirections) {
		this.readingDirections = readingDirections;
	}

	@Override
	protected void initializeRelations() {
		super.initializeRelations();
		readingDirections = new LinkedList<ReadingDirection>();
	}

	@ManyToOne
	@JoinColumn(name = "MEANING")
	@Access(value = AccessType.FIELD)
	@Target(value = SimplePropositionImpl.class)
	public SimpleProposition getSimpleProposition() {
		return simpleProposition;
	}

	public void setSimpleProposition(SimpleProposition simpleProposition) {
		checkConstraint();
		this.simpleProposition = simpleProposition;
	}

	@Override
	@Transient
	public Proposition getProposition() {
		return simpleProposition;
	}

	@Override
	@Transient
	public Meaning getMeaning() {
		return simpleProposition;
	}

	@Override
	public SimpleStatementImpl clone() {
		return new SimpleStatementImpl(this);
	}

	public void setMeaning(Meaning meaning) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getReadingDirections() == null) ? 0 : getReadingDirections().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (!(obj instanceof SimpleStatement)) {
			return false;
		}

		SimpleStatement other = (SimpleStatement) obj;
		if (readingDirections == null) {
			if (other.getReadingDirections() != null)
				return false;
		} else if (!readingDirections.equals(other.getReadingDirections()))
			return false;
		return true;
	}

	public String verbalise() {
		StringBuilder sb = new StringBuilder();
		for (ReadingDirection ph : readingDirections) {
			sb.append(ph.verbalise()).append(" ");
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
		return "SimpleStatement";
	}

	protected void checkConstraint(ReadingDirection rd) {
		checkConstraint();
		((ResourceImpl) rd.getBinaryFactTypeForm()).checkConstraint();
		((ResourceImpl) rd.getHeadTerm()).checkConstraint();
		((ResourceImpl) rd.getTailTerm()).checkConstraint();
	}
}
