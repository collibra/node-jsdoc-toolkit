package com.collibra.dgc.core.model.representation.facttypeform.impl;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Target;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.meaning.facttype.impl.CharacteristicImpl;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.representation.impl.RepresentationImpl;
import com.collibra.dgc.core.model.representation.impl.TermImpl;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
@DiscriminatorValue(value = "CF")
public class CharacteristicFormImpl extends RepresentationImpl implements CharacteristicForm {

	private Term term;
	private String role;
	private Characteristic characteristic;

	public CharacteristicFormImpl() {
		super();
	}

	public CharacteristicFormImpl(CharacteristicForm characteristicForm) {
		super(characteristicForm);
		setTerm(characteristicForm.getTerm());
		setRole(characteristicForm.getRole());
		setCharacteristic(characteristicForm.getCharacteristic());
		setLock(characteristicForm.isLocked());
	}

	public CharacteristicFormImpl(Vocabulary vocabulary, Term term, String role, Characteristic characteristic) {
		this(vocabulary, term, role, characteristic, true);
	}

	public CharacteristicFormImpl(Vocabulary vocabulary, Term term, String role, Characteristic characteristic,
			Boolean isPreferred) {
		super(vocabulary, isPreferred);
		setTerm(term);
		this.role = role;
		setCharacteristic(characteristic);
	}

	@ManyToOne
	@JoinColumn(name = "TERM")
	@Access(value = AccessType.FIELD)
	@Target(value = TermImpl.class)
	public Term getTerm() {
		return term;
	}

	@Column(name = "ROLE")
	@Access(value = AccessType.FIELD)
	public String getRole() {
		return role;
	}

	@Transient
	public Meaning getMeaning() {
		return characteristic;
	}

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "CHARACTERISTIC")
	@Access(value = AccessType.FIELD)
	@Target(value = CharacteristicImpl.class)
	public Characteristic getCharacteristic() {
		return characteristic;
	}

	public void setTerm(Term term) {
		checkConstraint();
		this.term = term;
	}

	public void setRole(String role) {
		checkConstraint();
		this.role = role;
	}

	public void setCharacteristic(Characteristic characteristic) {
		checkConstraint();
		this.characteristic = characteristic;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((term == null) ? 0 : term.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		if (!(obj instanceof CharacteristicForm))
			return false;
		CharacteristicForm other = (CharacteristicForm) obj;
		if (term == null) {
			if (other.getTerm() != null)
				return false;
		} else if (!term.equals(other.getTerm()))
			return false;
		if (role == null) {
			if (other.getRole() != null)
				return false;
		} else if (!role.equals(other.getRole()))
			return false;
		if (getVocabulary() == null) {
			if (other.getVocabulary() != null)
				return false;
		} else if (!getVocabulary().equals(other.getVocabulary())) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "CharacteristicFormImpl [getRole()=" + this.role + ", term=" + this.term + ", getResourceId()="
				+ this.getId() + "]";
	}

	public String verbalise() {
		StringBuilder sb = new StringBuilder();
		sb.append(term.getSignifier());
		sb.append(" ");
		sb.append(role);
		return sb.toString();
	}

	public void update(Term term, String role) {
		checkConstraint();
		setTerm(term);
		setRole(role);
	}

	@Override
	protected void initializeRelations() {
		super.initializeRelations();
	}

	@Override
	public CharacteristicFormImpl clone() {
		return new CharacteristicFormImpl(this);
	}

	public void setMeaning(Meaning meaning) {
		if (!(meaning instanceof Characteristic)) {
			throw new IllegalArgumentException("Expected a Characteristic");
		}
		setCharacteristic((Characteristic) meaning);
	}

	@Override
	@Transient
	public String getRepresentationType() {
		return "CharacteristicForm";
	}
}
