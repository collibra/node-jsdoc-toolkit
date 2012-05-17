package com.collibra.dgc.core.model.user.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.user.Website;
import com.collibra.dgc.core.model.user.WebsiteType;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "WEBSITE")
public class WebsiteImpl extends ResourceImpl implements Website {

	private String url;
	private WebsiteType type;

	public WebsiteImpl() {
	}

	public WebsiteImpl(String url, WebsiteType type) {
		setUrl(url);
		setWebsiteType(type);
	}

	public void setType(WebsiteType type) {
		this.type = type;
	}

	@Column(name = "URL")
	@Override
	public String getUrl() {
		return url;
	}

	@Override
	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	public WebsiteType getWebsiteType() {
		return type;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void setWebsiteType(WebsiteType type) {
		this.type = type;
	}

}
