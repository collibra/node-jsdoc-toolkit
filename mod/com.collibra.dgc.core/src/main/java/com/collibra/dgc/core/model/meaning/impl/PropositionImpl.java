package com.collibra.dgc.core.model.meaning.impl;

import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.meaning.Proposition;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
public abstract class PropositionImpl extends MeaningImpl implements Proposition {
	public PropositionImpl() {
		super();
	}

	public PropositionImpl(String uuid) {
		super(uuid);
	}
}
