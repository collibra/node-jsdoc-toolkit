package com.collibra.dgc.core.model.relation.impl;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.relation.Relation;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.impl.BinaryFactTypeFormImpl;
import com.collibra.dgc.core.model.representation.impl.RepresentationImpl;
import com.collibra.dgc.core.model.representation.impl.TermImpl;

/**
 * 
 * @author fvdmaele
 * 
 */
@Entity
@Audited
@Table(name = "RELATIONS")
public class RelationImpl extends ResourceImpl implements Relation {

	private Term source;
	private BinaryFactTypeForm type;
	private Term target;

	private Representation status;
	private Date startingDate;
	private Date endingDate;
	private Boolean isGenerated;

	protected RelationImpl() {
		super();
	}

	public RelationImpl(BinaryFactTypeForm type, Term source, Term target) {
		setResourceId(UUID.randomUUID().toString());
		setType(type);
		setSource(source);
		setTarget(target);
	}

	@ManyToOne(targetEntity = BinaryFactTypeFormImpl.class)
	@JoinColumn(name = "TYPE", nullable = false)
	@Override
	public BinaryFactTypeForm getType() {
		return type;
	}

	protected void setType(BinaryFactTypeForm type) {
		this.type = type;
	}

	@ManyToOne(targetEntity = TermImpl.class)
	@JoinColumn(name = "SOURCE", nullable = false)
	@Override
	public Term getSource() {
		return source;
	}

	public void setSource(Term source) {
		this.source = source;
	}

	@ManyToOne(targetEntity = TermImpl.class)
	@JoinColumn(name = "TARGET", nullable = false)
	@Override
	public Term getTarget() {
		return target;
	}

	public void setTarget(Term target) {
		this.target = target;
	}

	@ManyToOne(targetEntity = RepresentationImpl.class)
	@JoinColumn(name = "STATUS", nullable = true)
	@Override
	public Representation getStatusRepresentation() {
		return status;
	}

	public void setStatusRepresentation(Representation status) {
		this.status = status;
	}

	@Transient
	@Override
	public Term getStatus() {
		return (Term) this.status;
	}

	@Column(name = "STARTINGDATE", nullable = true)
	@Override
	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	@Column(name = "ENDINGDATE", nullable = true)
	@Override
	public Date getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(Date endingDate) {
		this.endingDate = endingDate;
	}

	@Column(name = "ISGENERATED", nullable = true)
	@Type(type = "boolean")
	@Override
	public Boolean getIsGenerated() {
		return this.isGenerated;
	}

	public void setIsGenerated(Boolean isGenerated) {
		this.isGenerated = isGenerated;
	}

	@Override
	@Transient
	public boolean isSourceToTargetDirection() {
		return (source.getObjectType().getType().equals(type.getHeadTerm().getObjectType()) && target.getObjectType()
				.getType().equals(type.getTailTerm().getObjectType()));
	}

	@Override
	@Transient
	protected void initializeRelations() {
		// TODO Auto-generated method stub
	}

	@Override
	@Transient
	public String verbalise() {
		return source.verbalise() + getType().getRole() + getType().getCoRole() + target.verbalise();
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof RelationImpl))
			return false;
		RelationImpl other = (RelationImpl) arg0;

		return (other.getId().equals(this.getId()) && other.getType().equals(this.getType())
				&& other.getSource().equals(this.getSource()) && other.getTarget().equals(this.getTarget()));
	}
}
