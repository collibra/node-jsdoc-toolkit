package com.collibra.dgc.core.model.representation.facttypeform.impl;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Target;
import org.hibernate.envers.Audited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.impl.BinaryFactTypeImpl;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;
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
@DiscriminatorValue(value = "BI")
public class BinaryFactTypeFormImpl extends RepresentationImpl implements BinaryFactTypeForm {
	private static final Logger log = LoggerFactory.getLogger(BinaryFactTypeFormImpl.class);

	private Term headTerm;
	private Term tailTerm;
	private String role;
	private String coRole;
	private BinaryFactType binaryFactType;

	public BinaryFactTypeFormImpl() {
		super();
	}

	public BinaryFactTypeFormImpl(BinaryFactTypeForm binaryFactTypeForm) {
		super(binaryFactTypeForm);
		setHeadTerm(binaryFactTypeForm.getHeadTerm());
		setTailTerm(binaryFactTypeForm.getTailTerm());
		setRole(binaryFactTypeForm.getRole());
		setCoRole(binaryFactTypeForm.getCoRole());
		setBinaryFactType(binaryFactTypeForm.getBinaryFactType());
		setLock(binaryFactTypeForm.isLocked());
	}

	public BinaryFactTypeFormImpl(Vocabulary vocabulary, Term headTerm, String role, String coRole, Term tailTerm,
			BinaryFactType binaryFactType) {
		this(vocabulary, headTerm, role, coRole, tailTerm, binaryFactType, true);
	}

	public BinaryFactTypeFormImpl(Vocabulary vocabulary, Term headTerm, String role, String coRole, Term tailTerm,
			BinaryFactType binaryFactType, Boolean isPreferred) {
		super(vocabulary, isPreferred);
		setHeadTerm(headTerm);
		setTailTerm(tailTerm);
		this.role = role;
		this.coRole = coRole;
		setBinaryFactType(binaryFactType);
	}

	@Column(name = "CO_ROLE")
	@Access(value = AccessType.FIELD)
	public String getCoRole() {
		return coRole;
	}

	public void setHeadTerm(Term headTerm) {
		checkConstraint();
		((ResourceImpl) headTerm).checkConstraint();

		this.headTerm = headTerm;
	}

	public void setTailTerm(Term tailTerm) {
		checkConstraint();
		((ResourceImpl) tailTerm).checkConstraint();

		this.tailTerm = tailTerm;
	}

	public void setRole(String role) {
		checkConstraint();
		this.role = role;
	}

	public void setCoRole(String coRole) {
		checkConstraint();
		this.coRole = coRole;
	}

	public void setBinaryFactType(BinaryFactType binaryFactType) {
		checkConstraint();
		this.binaryFactType = binaryFactType;
	}

	@ManyToOne
	@JoinColumn(name = "HEAD")
	@Access(value = AccessType.FIELD)
	@Target(value = TermImpl.class)
	public Term getHeadTerm() {
		return headTerm;
	}

	@Column(name = "ROLE")
	@Access(value = AccessType.FIELD)
	public String getRole() {
		return role;
	}

	@ManyToOne
	@JoinColumn(name = "TAIL")
	@Access(value = AccessType.FIELD)
	@Target(value = TermImpl.class)
	public Term getTailTerm() {
		return tailTerm;
	}

	@ManyToOne
	@JoinColumn(name = "BINARYFACTTYPE")
	@Access(value = AccessType.FIELD)
	@Target(value = BinaryFactTypeImpl.class)
	public BinaryFactType getBinaryFactType() {
		return binaryFactType;
	}

	@Transient
	public Meaning getMeaning() {
		return binaryFactType;
	}

	@Transient
	public ReadingDirection getLeftPlaceHolder() {
		return new ReadingDirectionImpl(this, true);
	}

	@Transient
	public ReadingDirection getRightPlaceHolder() {
		return new ReadingDirectionImpl(this, false);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((headTerm == null) ? 0 : headTerm.hashCode());
		result = prime * result + ((tailTerm == null) ? 0 : tailTerm.hashCode());
		result = prime * (result + ((role == null) ? 0 : role.hashCode()) + ((coRole == null) ? 0 : coRole.hashCode()));
		result = prime * result + ((getVocabulary() == null) ? 0 : getVocabulary().hashCode());
		return result;
	}

	public String verbalise() {
		StringBuilder sb = new StringBuilder();
		if (headTerm != null) {
			sb.append(headTerm.getSignifier());
		} else {
			log.error("Head term is null in BinaryFactTypeForm.");
		}
		sb.append(" ");
		sb.append(role);
		sb.append(" / ");
		sb.append(coRole);
		sb.append(" ");
		if (tailTerm != null) {
			sb.append(tailTerm.getSignifier());
		} else {
			log.error("Tail term is null in BinaryFactTypeForm.");
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return "BinaryFactTypeFormImpl [headTerm=" + this.headTerm + ", role=" + this.role + ", coRole=" + coRole
				+ ",  tailTerm=" + tailTerm + ", getResourceId()=" + this.getId() + ", getId()=" + this.getId()
				+ "]";
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
		if (!(obj instanceof BinaryFactTypeForm))
			return false;

		BinaryFactTypeForm other = (BinaryFactTypeForm) obj;
		if (!(isStraightEqual(other) || isReverseEqual(other))) {
			return false;
		}

		if (getVocabulary() == null) {
			if (other.getVocabulary() != null)
				return false;
		} else if (!getVocabulary().equals(other.getVocabulary())) {
			return false;
		}

		return true;
	}

	private boolean isStraightEqual(BinaryFactTypeForm other) {
		if (headTerm == null) {
			if (other.getHeadTerm() != null)
				return false;
		} else if (!headTerm.equals(other.getHeadTerm()))
			return false;

		if (tailTerm == null) {
			if (other.getTailTerm() != null)
				return false;
		} else if (!tailTerm.equals(other.getTailTerm()))
			return false;

		if (role == null) {
			if (other.getRole() != null)
				return false;
		} else if (!role.equals(other.getRole()))
			return false;

		if (coRole == null) {
			if (other.getCoRole() != null)
				return false;
		} else if (!coRole.equals(other.getCoRole()))
			return false;

		return true;
	}

	private boolean isReverseEqual(BinaryFactTypeForm other) {
		if (headTerm == null) {
			if (other.getTailTerm() != null)
				return false;
		} else if (!headTerm.equals(other.getTailTerm()))
			return false;

		if (tailTerm == null) {
			if (other.getHeadTerm() != null)
				return false;
		} else if (!tailTerm.equals(other.getHeadTerm()))
			return false;

		if (role == null) {
			if (other.getCoRole() != null)
				return false;
		} else if (!role.equals(other.getCoRole()))
			return false;

		if (coRole == null) {
			if (other.getRole() != null)
				return false;
		} else if (!coRole.equals(other.getRole()))
			return false;

		return true;
	}

	public void setRoles(String role, String coRole) {
		checkConstraint();
		setRole(role);
		setCoRole(coRole);
	}

	public void update(Term head, String role, String coRole, Term tail) {
		checkConstraint();
		setHeadTerm(head);
		setRole(role);
		setCoRole(coRole);
		setTailTerm(tail);
	}

	@Override
	protected void initializeRelations() {
		super.initializeRelations();
	}

	@Override
	public BinaryFactTypeFormImpl clone() {
		return new BinaryFactTypeFormImpl(this);
	}

	public void setMeaning(Meaning meaning) {
		checkConstraint();
		if (!(meaning instanceof BinaryFactType)) {
			throw new IllegalArgumentException("Expected BinaryFactType");
		}
		setBinaryFactType((BinaryFactType) meaning);
	}

	@Override
	@Transient
	public String getRepresentationType() {
		return "BinaryFactTypeForm";
	}
}
