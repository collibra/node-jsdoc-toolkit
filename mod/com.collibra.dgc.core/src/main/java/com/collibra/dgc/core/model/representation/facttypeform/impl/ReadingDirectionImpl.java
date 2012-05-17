package com.collibra.dgc.core.model.representation.facttypeform.impl;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Target;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
@Table(name = "READINGDIRECTIONS")
public class ReadingDirectionImpl implements ReadingDirection {
	private Long id;

	private BinaryFactTypeForm binaryFactTypeForm;

	private boolean isLeft;

	public ReadingDirectionImpl() {
		super();
	}

	public ReadingDirectionImpl(BinaryFactTypeForm binaryFactTypeForm, boolean isLeft) {
		setBinaryFactTypeForm(binaryFactTypeForm);
		this.isLeft = isLeft;
	}

	@ManyToOne
	@JoinColumn(name = "BFTF")
	@Access(value = AccessType.FIELD)
	@Target(value = BinaryFactTypeFormImpl.class)
	public BinaryFactTypeForm getBinaryFactTypeForm() {
		return binaryFactTypeForm;
	}

	public void setBinaryFactTypeForm(BinaryFactTypeForm binaryFactType) {
		this.binaryFactTypeForm = binaryFactType;
	}

	@Transient
	public Term getHeadTerm() {
		if (isLeft) {
			return binaryFactTypeForm.getHeadTerm();
		} else {
			return binaryFactTypeForm.getTailTerm();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (getIsLeft() ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (!(obj instanceof ReadingDirection)) {
			return false;
		}

		ReadingDirection other = (ReadingDirection) obj;
		if (binaryFactTypeForm == null) {
			if (other.getBinaryFactTypeForm() != null)
				return false;
		} else if (!binaryFactTypeForm.equals(other.getBinaryFactTypeForm()))
			return false;
		if (isLeft != other.getIsLeft())
			return false;
		return true;
	}

	@Transient
	public Term getTailTerm() {
		if (isLeft) {
			return binaryFactTypeForm.getTailTerm();
		} else {
			return binaryFactTypeForm.getHeadTerm();
		}

	}

	@Transient
	public String getRole() {
		if (isLeft) {
			return binaryFactTypeForm.getRole();
		} else {
			return binaryFactTypeForm.getCoRole();
		}
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "DGC_RD_GENERATOR")
	@SequenceGenerator(name = "DGC_RD_GENERATOR", sequenceName = "READINGDIRECTIONS_SEQUENCE")
	public Long getId() {
		return id;
	}

	protected void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ISLEFT", nullable = false)
	public boolean getIsLeft() {
		return isLeft;
	}

	protected void setIsLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}

	public String verbalise() {
		StringBuilder sb = new StringBuilder();
		if (isLeft) {
			sb.append(binaryFactTypeForm.getHeadTerm().getSignifier()).append(" ").append(binaryFactTypeForm.getRole())
					.append(" ").append(binaryFactTypeForm.getTailTerm().getSignifier());
		} else {
			sb.append(binaryFactTypeForm.getTailTerm().getSignifier()).append(" ")
					.append(binaryFactTypeForm.getCoRole()).append(" ")
					.append(binaryFactTypeForm.getHeadTerm().getSignifier());
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		return verbalise();
	}
}
