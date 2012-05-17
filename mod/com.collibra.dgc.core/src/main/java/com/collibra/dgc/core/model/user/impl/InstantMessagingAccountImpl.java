package com.collibra.dgc.core.model.user.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.user.InstantMessagingAccount;
import com.collibra.dgc.core.model.user.InstantMessagingAccountType;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "IM")
public class InstantMessagingAccountImpl extends ResourceImpl implements InstantMessagingAccount {
	private String account;
	private InstantMessagingAccountType instantMessagingAccountType;

	public InstantMessagingAccountImpl() {
	}

	public InstantMessagingAccountImpl(String account, InstantMessagingAccountType instantMessagingAccountType) {
		this.account = account;
		this.instantMessagingAccountType = instantMessagingAccountType;
	}

	@Column(name = "ACCOUNT")
	@Override
	public String getAccount() {
		return account;
	}

	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	@Override
	public InstantMessagingAccountType getInstantMessagingAccountType() {
		return instantMessagingAccountType;
	}

	@Override
	public void setAccount(String account) {
		this.account = account;
	}

	public void setInstantMessagingAccountType(InstantMessagingAccountType instantMessagingAccountType) {
		this.instantMessagingAccountType = instantMessagingAccountType;
	}

	@Override
	public void setAccountType(InstantMessagingAccountType accountType) {
		this.instantMessagingAccountType = accountType;
	}

}
