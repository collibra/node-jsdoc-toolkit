/**
 * 
 */
package com.collibra.dgc.core.dto.cmatch;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.collibra.dgc.core.model.representation.Term;
import com.collibra.matcher.common.model.DMEntity;
import com.collibra.matcher.common.model.DMModel;

/**
 * @author fvdmaele
 * 
 */
public class CMModel implements DMModel {

	protected List<DMEntity> entities;

	public CMModel(Collection<Term> terms) {
		entities = new LinkedList<DMEntity>();
		for (Term t : terms)
			entities.add(new CMTermEntity(t.getSignifier().toLowerCase(), t.getId().toString()));
	}

	public CMModel(List<DMEntity> entities) {
		this.entities = entities;
	}

	public CMModel(DMEntity entity) {
		entities = new LinkedList<DMEntity>();
		entities.add(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.cmatch.model.DMModel#getEntities()
	 */
	public List<? extends DMEntity> getEntities() {
		return entities;
	}

}
