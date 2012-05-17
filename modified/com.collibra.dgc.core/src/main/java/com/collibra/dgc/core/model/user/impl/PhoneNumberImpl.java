package com.collibra.dgc.core.model.user.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.user.PhoneNumber;
import com.collibra.dgc.core.model.user.PhoneType;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "PHONE")
@Entity
public class PhoneNumberImpl extends ResourceImpl implements PhoneNumber {
	private PhoneType type;
	private String phoneNumber;

	public PhoneNumberImpl() {
	}

	public PhoneNumberImpl(String phoneNumber, PhoneType type) {
		setPhoneNumber(phoneNumber);
		setPhoneType(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.model.user.impl.PhoneNumber#getNumber()
	 */
	@Override
	@Column(name = "PHONE", nullable = false)
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.model.user.impl.PhoneNumber#getPhoneType()
	 */
	@Override
	@Column(name = "PHONE_TYPE")
	@Enumerated(EnumType.STRING)
	public PhoneType getPhoneType() {
		return type;
	}

	@Override
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public void setPhoneType(PhoneType phoneType) {
		this.type = phoneType;

	}

}
