/**
 * 
 */
package com.collibra.dgc.core.model;

import org.hibernate.envers.RevisionListener;

/**
 * Listener for Hibernate Envers revisions. Here we set our own data per revision like the user that did the change.
 * @author dieterwachters
 */
public class ModelRevisionListener implements RevisionListener {
	public void newRevision(Object revisionEntity) {
		final ModelRevision rev = (ModelRevision) revisionEntity;

		// TODO PORT
		rev.setUsername("admin");
	}
}
