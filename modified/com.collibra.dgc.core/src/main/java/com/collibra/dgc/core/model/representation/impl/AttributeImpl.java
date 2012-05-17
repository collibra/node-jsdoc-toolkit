package com.collibra.dgc.core.model.representation.impl;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DiscriminatorOptions;
import org.hibernate.annotations.Target;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "ATTRIBUTES")
@DiscriminatorColumn(name = "ATTR_TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorOptions(force = true)
@Audited
public abstract class AttributeImpl extends ResourceImpl implements Attribute {

	private Term label;
	private Representation owner;
	private String value;

	public AttributeImpl() {
		super();
	}

	public AttributeImpl(Attribute attribute) {
		super(attribute);
		setLabel(attribute.getLabel());
		setOwner(attribute.getOwner());
	}

	public AttributeImpl(Term label, Representation representation) {
		setLabel(label);
		setOwner(representation);
	}

	@Override
	@ManyToOne
	@JoinColumn(name = "LABEL", nullable = false)
	@Access(value = AccessType.FIELD)
	@Target(value = TermImpl.class)
	public Term getLabel() {
		return label;
	}

	public void setLabel(Term term) {
		this.label = term;
	}

	/**
	 * @return the short expression
	 */
	@Override
	@Column(name = "EXPRESSION_SHORT")
	@Access(value = AccessType.FIELD)
	public String getValue() {
		return value;
	}

	protected void setValue(String val) {
		if (val != null && val.length() > 255)
			value = val.substring(0, 255);
		else
			value = val;
	}

	@Override
	@ManyToOne
	@JoinColumn(name = "OWNER")
	@Target(value = RepresentationImpl.class)
	public Representation getOwner() {
		return owner;
	}

	public void setOwner(Representation owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "AttributeImpl [stringExpression=" + getValue() + ", term=" + this.label + ", getResourceId()="
				+ this.getId() + "]";
	}

	@Override
	public String verbalise() {
		if (getLabel() != null)
			return getLabel().verbalise() + ": " + getValue();
		else
			return this.toString() + ": " + getValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.label == null) ? 0 : this.label.hashCode());
		result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Attribute))
			return false;
		Attribute other = (Attribute) obj;
		if (this.getLabel() == null) {
			if (other.getLabel() != null)
				return false;
		} else if (!this.getLabel().equals(other.getLabel()))
			return false;
		if (getValue() == null) {
			if (other.getValue() != null)
				return false;
		} else if (!getValue().equals(other.getValue()))
			return false;
		else if (!getId().equals(other.getId()))
			return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.model.ResourceImpl#initializeRelations()
	 */
	@Override
	protected void initializeRelations() {
	}
}
