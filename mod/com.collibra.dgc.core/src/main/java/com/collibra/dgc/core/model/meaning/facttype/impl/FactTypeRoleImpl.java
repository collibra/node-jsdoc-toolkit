package com.collibra.dgc.core.model.meaning.facttype.impl;

import java.util.Set;

import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.FactTypeRole;
import com.collibra.dgc.core.model.meaning.impl.ConceptImpl;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.Placeholder;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;
import com.collibra.dgc.core.model.representation.facttypeform.impl.PlaceholderImpl;

public class FactTypeRoleImpl extends ConceptImpl implements FactTypeRole {

	/**
	 * The ObjectType over which this FactTypeRole ranges.
	 */
	private ObjectType objectType;

	private BinaryFactType binaryFactType;

	private Set<Placeholder> placeholders;

	public FactTypeRoleImpl() {

	}

	public FactTypeRoleImpl(BinaryFactType bftf, ObjectType objectType) {
		this.binaryFactType = bftf;
		this.objectType = objectType;

	}

	public Set<? extends Representation> getRepresentations() {
		return getPlaceholders();
	}

	public void addRepresentation(Representation representation) {
		if (!(representation instanceof ReadingDirection)) {
			throw new IllegalArgumentException("Expected a Placeholder");
		}
		addPlaceholder((Placeholder) representation);
	}

	public void removeRepresentation(Representation representation) {
		placeholders.remove(representation);
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		// bail out if nothing changes
		if (this.equals(objectType)) {
			return;
		}
		this.objectType = objectType;
	}

	public BinaryFactType getBinaryFactType() {
		return binaryFactType;
	}

	protected void setBinaryFactType(BinaryFactType factType) {
		this.binaryFactType = factType;
	}

	public Set<Placeholder> getPlaceholders() {
		if (placeholders.isEmpty()) {
			for (Term term : objectType.getTerms()) {
				placeholders.add(new PlaceholderImpl(this, term));
			}
		}
		return placeholders;
	}

	protected void setPlaceholders(Set<Placeholder> placeholders) {
		this.placeholders = placeholders;
	}

	public void addPlaceholder(Placeholder placeholder) {
		boolean didChange = placeholders.add(placeholder);
	}

	public void removePlaceholder(Placeholder placeholder) {
		boolean didChange = placeholders.remove(placeholder);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((binaryFactType == null) ? 0 : binaryFactType.hashCode());
		result = prime * result + ((objectType == null) ? 0 : objectType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof FactTypeRole))
			return false;
		FactTypeRole other = (FactTypeRole) obj;
		if (binaryFactType == null) {
			if (other.getBinaryFactType() != null)
				return false;
		} else if (!binaryFactType.equals(other.getBinaryFactType()))
			return false;
		if (objectType == null) {
			if (other.getObjectType() != null)
				return false;
		} else if (!objectType.equals(other.getObjectType()))
			return false;
		return true;
	}
}
