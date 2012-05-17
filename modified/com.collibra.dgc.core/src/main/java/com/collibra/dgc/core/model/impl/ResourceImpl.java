package com.collibra.dgc.core.model.impl;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.DGCException;
import com.collibra.dgc.core.model.Resource;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@MappedSuperclass
public abstract class ResourceImpl implements Resource {

	protected String id;
	private Boolean lock = Boolean.FALSE;

	private String creator;
	private Long creationDate;
	private String lastModifiedBy;
	private Long lastModified;

	private boolean isPersisted = false;

	public ResourceImpl() {
		this(UUID.randomUUID().toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Resource other = (Resource) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	public ResourceImpl(String resourceId) {
		super();
		this.id = resourceId;
		initializeRelations();

		creator = getCurrentUser();
		creationDate = System.currentTimeMillis();
	}

	/**
	 * @return the lastModifiedBy
	 */
	@Override
	@Column(name = "MODIFIEDBY")
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy the lastModifiedBy to set
	 */
	public void setLastModifiedBy(String lastmodifiedBy) {
		this.lastModifiedBy = lastmodifiedBy;
	}

	@Override
	public void updateLastModified() {
		lastModified = System.currentTimeMillis();
		lastModifiedBy = getCurrentUser();
	}

	/**
	 * @return the lastModified
	 */
	@Override
	@Column(name = "LASTMODIFIED")
	public Long getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	protected void setLastModified(Long lastmodified) {
		this.lastModified = lastmodified;
	}

	/**
	 * @return the creator
	 */
	@Override
	@Column(name = "CREATOR")
	public String getCreatedBy() {
		return creator;
	}

	/**
	 * @return the creationDate
	 */
	@Override
	@Column(name = "CREATIONDATE")
	public Long getCreatedOn() {
		return creationDate;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreatedBy(String creator) {
		this.creator = creator;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreatedOn(Long creationDate) {
		this.creationDate = creationDate;
	}

	@Transient
	protected String getCurrentUser() {
		try {
			Subject currentUser = SecurityUtils.getSubject();
			if (currentUser.getPrincipal() == null) {
				return Constants.GUEST_USER;
			}
			return currentUser.getPrincipal().toString();
		} catch (Exception e) {
			return "";
		}
	}

	public ResourceImpl(Resource vr) {
		this(vr.getId());
	}

	/**
	 * This method should be overridden to initialize the collections from the default constructor
	 */
	protected void initializeRelations() {
	}

	@Override
	@Id
	@Column(name = "ID", nullable = false, length = 40)
	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
		isPersisted = true;
	}

	public void saved() {
		isPersisted = true;
	}

	@Transient
	@Override
	public boolean isPersisted() {
		return isPersisted;
	}

	public void setResourceId(String id) {
		this.id = id;
	}

	@Override
	@Transient
	public boolean isLocked() {
		return (lock == null) ? false : lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	/**
	 * Copies the resource id to the given {@link Resource}
	 * 
	 * @param vr The {@link Resource} to copy the resource id to.
	 */
	protected void copyResourceId(ResourceImpl vr) {
		vr.setId(getId());
	}

	@Override
	public void checkConstraint() {
		if (isLocked()) {
			throw new DGCException(DGCErrorCodes.RESOURCE_LOCKED, id);
		}
	}
}
