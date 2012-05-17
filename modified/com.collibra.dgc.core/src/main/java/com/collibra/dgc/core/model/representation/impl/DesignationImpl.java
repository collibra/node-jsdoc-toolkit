package com.collibra.dgc.core.model.representation.impl;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.representation.Designation;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
public abstract class DesignationImpl extends RepresentationImpl implements Designation {

	protected String signifier;

	public DesignationImpl() {
		super();
	}

	public DesignationImpl(Vocabulary vocabulary, String signifier, Boolean isPreferred) {
		super(vocabulary, isPreferred);
		setSignifier(signifier);
	}

	public DesignationImpl(Vocabulary vocabulary, String signifier) {
		this(vocabulary, signifier, true);
	}

	public DesignationImpl(Designation designation) {
		super(designation);
		setSignifier(designation.getSignifier());
	}

	@Override
	@Column(name = "SIGNIFIER")
	@Access(value = AccessType.FIELD)
	public String getSignifier() {
		return signifier;
	}

	public void setSignifier(String signifier) {
		checkConstraint();
		checkSignifierConstraint(signifier);
		this.signifier = signifier;
	}

	@Override
	public abstract DesignationImpl clone();

	private void checkSignifierConstraint(String signifier) {

		if (signifier.length() > 255) {
			throw new IllegalArgumentException(DGCErrorCodes.TERM_SIGNIFIER_TOO_LARGE, signifier, signifier.length(),
					id);
		}
	}
}
