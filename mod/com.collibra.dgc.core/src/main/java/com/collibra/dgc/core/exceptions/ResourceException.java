package com.collibra.dgc.core.exceptions;

import java.io.Serializable;

import com.collibra.dgc.core.model.Resource;

/**
 * Exception for when something goes wrong with {@link Resource}s.
 * @author dtrog
 * 
 */
public class ResourceException extends DGCException {

	/**
	 * Generated
	 */
	private static final long serialVersionUID = 7978302865217333031L;

	private Serializable identifier;
	private String entityName;

	public Serializable getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Serializable identifier) {
		this.identifier = identifier;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public ResourceException(String message, Serializable id, String clazz, String errorCode) {
		super(errorCode, message);
		this.entityName = clazz;
		this.identifier = id;
	}
}
