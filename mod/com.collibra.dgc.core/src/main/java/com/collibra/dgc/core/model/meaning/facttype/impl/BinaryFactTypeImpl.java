package com.collibra.dgc.core.model.meaning.facttype.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.FactTypeRole;
import com.collibra.dgc.core.model.meaning.impl.ConceptImpl;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.impl.BinaryFactTypeFormImpl;

/**
 * 
 * @author amarnath
 * @author pmalarme
 * 
 */
@Entity
@Audited
@DiscriminatorValue(value = "BF")
public class BinaryFactTypeImpl extends ConceptImpl implements BinaryFactType {

	private Set<BinaryFactTypeForm> binaryFactTypeForms;

	public BinaryFactTypeImpl() {
		super();
	}

	public BinaryFactTypeImpl(String uuid) {
		super(uuid);
	}

	@Transient
	public FactTypeRole getHeadFactTypeRole() {
		if (binaryFactTypeForms.size() == 0) {
			return null;
		}

		ObjectType headObjectType = binaryFactTypeForms.iterator().next().getHeadTerm().getObjectType();
		return new FactTypeRoleImpl(this, headObjectType);
	}

	@Transient
	public FactTypeRole getTailFactTypeRole() {
		if (binaryFactTypeForms.size() == 0) {
			return null;
		}

		ObjectType tailObjectType = binaryFactTypeForms.iterator().next().getTailTerm().getObjectType();
		return new FactTypeRoleImpl(this, tailObjectType);
	}

	@OneToMany(cascade = { CascadeType.MERGE }, targetEntity = BinaryFactTypeFormImpl.class, mappedBy = "binaryFactType")
	public Set<BinaryFactTypeForm> getBinaryFactTypeForms() {
		return binaryFactTypeForms;
	}

	public void addBinaryFactTypeForm(BinaryFactTypeForm bftf) {
		binaryFactTypeForms.add(bftf);
	}

	@Transient
	public Set<BinaryFactTypeForm> getFactTypeForms() {
		return getBinaryFactTypeForms();
	}

	protected void setBinaryFactTypeForms(Set<BinaryFactTypeForm> binaryFactTypeForms) {
		this.binaryFactTypeForms = binaryFactTypeForms;
	}

	@Transient
	public Set<? extends Representation> getRepresentations() {
		return getBinaryFactTypeForms();
	}

	@Override
	protected void initializeRelations() {
		super.initializeRelations();
		binaryFactTypeForms = new HashSet<BinaryFactTypeForm>();
	}

	@Override
	public String toString() {
		return "BinaryFactType [getResourceId()=" + getId() + "]";
	}

	public void addRepresentation(Representation representation) {
		if (!(representation instanceof BinaryFactTypeForm)) {
			throw new IllegalArgumentException("Expected a BinaryFactTypeForm");
		}
		addBinaryFactTypeForm((BinaryFactTypeForm) representation);
	}

	public void removeRepresentation(Representation representation) {
		binaryFactTypeForms.remove(representation);
	}

	public boolean hasDefaultBinaryFactTypeForm() {
		return hasDefaultRepresentation();
	}
}
