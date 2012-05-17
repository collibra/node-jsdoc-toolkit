package com.collibra.dgc.core.model.meaning.impl;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.DiscriminatorOptions;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.representation.Representation;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
@Table(name = "MEANINGS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// TODO change to one table per class ?
@DiscriminatorColumn(name = "MEAN_TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorOptions(force = true)
public abstract class MeaningImpl extends ResourceImpl implements Meaning {

	public MeaningImpl() {
		super();
	}

	public MeaningImpl(String uuid) {
		super(uuid);
	}

	public MeaningImpl(Meaning meaning) {
		super(meaning);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getId().hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Meaning))
			return false;
		Meaning other = (Meaning) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	protected void initializeRelations() {
	}

	@Override
	public String toString() {
		return "MeaningImpl [resourceId=" + id + "]";
	}

	public String verbalise() {
		return id.toString();
	}

	public boolean hasDefaultRepresentation() {
		boolean found = false;
		for (Representation rep : getRepresentations()) {
			if (rep.getIsPreferred()) {
				found = true;
			}
		}
		return found;
	}
}
