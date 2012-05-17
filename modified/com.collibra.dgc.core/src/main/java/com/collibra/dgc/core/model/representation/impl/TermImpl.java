package com.collibra.dgc.core.model.representation.impl;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Target;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.impl.ObjectTypeImpl;
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
@DiscriminatorValue(value = "TE")
public class TermImpl extends DesignationImpl implements Term {

	private ObjectType objectType;

	public TermImpl() {
		super();
	}

	public TermImpl(final Term term) {
		super(term);
		setObjectType(term.getObjectType());
		setLock(term.isLocked());
	}

	public TermImpl(final Vocabulary vocabulary, final String signifier, final ObjectType objectType) {
		super(vocabulary, signifier, true);
		setObjectType(objectType);
	}

	public TermImpl(final Vocabulary vocabulary, final String signifier, final ObjectType objectType,
			Boolean isPreferred) {
		super(vocabulary, signifier, isPreferred);
		setObjectType(objectType);
	}

	@Transient
	public ObjectType getMeaning() {
		return getObjectType();
	}

	@ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinColumn(name = "OBJECTTYPE")
	@Access(value = AccessType.FIELD)
	@Target(value = ObjectTypeImpl.class)
	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		checkConstraint();
		this.objectType = objectType;
	}

	public void setMeaning(Meaning meaning) {
		checkConstraint();
		if (!(meaning instanceof ObjectType)) {
			return;
		}
		setObjectType((ObjectType) meaning);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getSignifier() == null) ? 0 : getSignifier().hashCode());
		result = prime * result + ((getVocabulary() == null) ? 0 : getVocabulary().hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Term))
			return false;
		Term other = (Term) obj;
		if (getSignifier() == null) {
			if (other.getSignifier() != null)
				return false;
		} else if (!getSignifier().equals(other.getSignifier()))
			return false;
		if (getVocabulary() == null) {
			if (other.getVocabulary() != null)
				return false;
		} else if (!getVocabulary().equals(other.getVocabulary())) {
			return false;
		}

		return true;
	}

	public String verbalise() {
		return getSignifier();
	}

	@Override
	public String toString() {
		return "TermImpl [getSignifier()=" + this.getSignifier() + ", getResourceId()=" + this.getId() + "]";
	}

	@Override
	protected void initializeRelations() {
		super.initializeRelations();
	}

	@Override
	public TermImpl clone() {
		return new TermImpl(this);
	}

	@Override
	@Transient
	public String getRepresentationType() {
		return "Term";
	}
}
