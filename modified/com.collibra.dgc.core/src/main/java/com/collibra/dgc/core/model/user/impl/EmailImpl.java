package com.collibra.dgc.core.model.user.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.user.Email;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "EMAIL")
public class EmailImpl extends ResourceImpl implements Email {
	private String emailAddress;

	public EmailImpl() {
	}

	public EmailImpl(String emailAddress) {
		setEmailAddress(emailAddress);
	}

	@Override
	@Column(name = "EMAIL")
	public String getEmailAddress() {
		return emailAddress;
	}

	@Override
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + new Long(id).hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof Email) {
			Email email = (Email) obj;
			return email.getId() == this.getId();
		}
		return false;
	}
}
