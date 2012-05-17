/**
 * 
 */
package com.collibra.dgc.core.dto.cmatch;

import com.collibra.matcher.common.model.DMEntity;

/**
 * @author fvdmaele
 * 
 */
public class CMTermEntity implements DMEntity {

	String term;
	String resourceId;
	String versionId = null;

	public CMTermEntity(String term, String resourceId) {
		this.term = term;
		this.resourceId = resourceId;
	}

	public CMTermEntity(String term, String resourceId, String versionId) {
		this.term = term;
		this.resourceId = resourceId;
		this.versionId = versionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.cmatch.model.DMEntity#getTerm()
	 */
	public String getTerm() {
		return term.toLowerCase();
	}

	public String getResourceId() {
		return resourceId;
	}
}
