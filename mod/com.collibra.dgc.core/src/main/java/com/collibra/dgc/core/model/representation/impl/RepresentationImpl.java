package com.collibra.dgc.core.model.representation.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DiscriminatorOptions;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
@Table(name = "REPRESENTATIONS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "REP_TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorOptions(force = true)
public abstract class RepresentationImpl extends ResourceImpl implements Representation, Comparable<Representation> {

	private Representation status;
	private Vocabulary vocabulary;
	private Set<Attribute> attributes = new HashSet<Attribute>();

	/**
	 * Set by default to true. Operations that involve creating a synonym should take care of calling the right
	 * constructor.
	 */
	private Boolean isPreferred = false;

	public RepresentationImpl() {
		super();

	}

	public RepresentationImpl(Vocabulary vocabulary) {
		this(vocabulary, true);
	}

	public RepresentationImpl(Representation representation) {
		super(representation);
		setVocabulary(representation.getVocabulary());
		setIsPreferred(representation.getIsPreferred());

		// Copy attributes
		// new Set() is needed or Hibernate will not keep the old entries when persisting new ones!!!
		setAttributes(new HashSet<Attribute>(representation.getAttributes()));
	}

	public RepresentationImpl(Vocabulary vocabulary, Boolean isPreferred) {
		super();
		this.isPreferred = isPreferred;
		this.vocabulary = vocabulary;
	}

	@Column(name = "IS_LOCKED")
	@Type(type = "boolean")
	@Access(value = AccessType.FIELD)
	public boolean isLock() {
		return super.isLocked();
	}

	@ManyToOne(cascade = { CascadeType.MERGE }, targetEntity = VocabularyImpl.class)
	@JoinColumn(name = "VOCABULARY")
	public Vocabulary getVocabulary() {
		return vocabulary;
	}

	/**
	 * Should only called by the Hibernate DAOs. This breaks the object's immutabily contract.
	 */
	public void setVocabulary(Vocabulary vocabulary) {
		this.vocabulary = vocabulary;
	}

	public void clearVocabulary() {
		this.vocabulary = null;
	}

	@Column(name = "IS_PREFERRED")
	@Type(type = "boolean")
	@Access(value = AccessType.FIELD)
	public Boolean getIsPreferred() {
		return this.isPreferred;
	}

	public void setIsPreferred(Boolean isPreferred) {
		this.isPreferred = isPreferred;
	}

	protected void copyIsPreferred(RepresentationImpl representation) {
		representation.setIsPreferred(getIsPreferred());
	}

	@OneToMany(targetEntity = AttributeImpl.class, mappedBy = "owner")
	@Access(value = AccessType.FIELD)
	@Basic(fetch = FetchType.LAZY)
	public Set<Attribute> getAttributes() {
		return attributes;
	}

	protected void setAttributes(Set<Attribute> attributes) {
		this.attributes = attributes;
	}

	public void addAttribute(Attribute attribute) {
		checkConstraint();
		attributes.add(attribute);

	}

	protected void copyAttributes(Representation representation) {
		checkConstraint();

		for (Attribute att : getAttributes()) {
			representation.addAttribute(att);
			((AttributeImpl) att).setOwner(representation);
		}
	}

	/**
	 * @return String representation of the {@link Representation} type. Only use when absolutely necessary (e.g., in
	 *         certain cases in velocity scripts).
	 */
	@Transient
	public abstract String getRepresentationType();

	@Override
	public abstract RepresentationImpl clone();

	@Override
	protected void initializeRelations() {
		attributes = new HashSet<Attribute>();
	}

	public int compareTo(Representation o) {
		return verbalise().toLowerCase().compareTo(o.verbalise().toLowerCase());
	}

	@ManyToOne(targetEntity = RepresentationImpl.class)
	@JoinColumn(name = "status", nullable = true)
	// @Target(RepresentationImpl.class)
	protected Representation getStatusRepresentation() {
		return status;
	}

	protected void setStatusRepresentation(final Representation status) {
		this.status = status;
	}

	@Transient
	public Term getStatus() {
		return (Term) this.status;
	}

	public void setStatus(final Term status) {
		this.status = status;
	}
}
