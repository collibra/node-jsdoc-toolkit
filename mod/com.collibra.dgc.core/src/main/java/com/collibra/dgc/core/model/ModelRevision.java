/**
 * 
 */
package com.collibra.dgc.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

/**
 * Represents a revision in the model.
 * @author dieterwachters
 */
@Entity
@Table(name = "REVISIONS")
@RevisionEntity(ModelRevisionListener.class)
public class ModelRevision extends DefaultRevisionEntity {
	private static final long serialVersionUID = 7340736760002738967L;

	private String username;

	@Column(name = "user")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
