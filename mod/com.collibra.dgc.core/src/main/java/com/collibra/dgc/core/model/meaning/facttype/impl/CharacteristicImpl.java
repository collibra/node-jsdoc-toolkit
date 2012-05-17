package com.collibra.dgc.core.model.meaning.facttype.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.meaning.facttype.FactTypeRole;
import com.collibra.dgc.core.model.meaning.impl.ConceptImpl;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.representation.facttypeform.impl.CharacteristicFormImpl;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
@DiscriminatorValue(value = "CH")
public class CharacteristicImpl extends ConceptImpl implements Characteristic {

	private FactTypeRole factTypeRole = null;
	private Set<CharacteristicForm> characteristicForms;

	public CharacteristicImpl() {
		super();
	}

	public CharacteristicImpl(String uuid) {
		super(uuid);
	}

	@Transient
	public FactTypeRole getFactTypeRole() {
		return factTypeRole;
	}

	public void setFactTypeRole(FactTypeRole factTypeRole) {
		this.factTypeRole = factTypeRole;
	}

	@OneToMany(cascade = { CascadeType.MERGE }, targetEntity = CharacteristicFormImpl.class, mappedBy = "characteristic")
	public Set<CharacteristicForm> getCharacteristicForms() {
		return characteristicForms;
	}

	public void addCharacteristicForm(CharacteristicForm cForm) {
		characteristicForms.add(cForm);
	}

	@Transient
	public Set<CharacteristicForm> getFactTypeForms() {
		Set<CharacteristicForm> forms = new HashSet<CharacteristicForm>();
		forms.addAll(characteristicForms);
		return forms;
	}

	protected void setCharacteristicForms(Set<CharacteristicForm> characteristicForms) {
		this.characteristicForms = characteristicForms;
	}

	@Transient
	public Set<? extends Representation> getRepresentations() {
		return getCharacteristicForms();
	}

	@Override
	protected void initializeRelations() {
		super.initializeRelations();
		characteristicForms = new HashSet<CharacteristicForm>();
	}

	@Override
	public String toString() {
		return "CharacteristicImpl [getResourceId()=" + getId() + "]";
	}

	public void addRepresentation(Representation representation) {
		if (!(representation instanceof CharacteristicForm)) {
			throw new IllegalArgumentException("Expected a CharacteristicForm");
		}
		addCharacteristicForm((CharacteristicForm) representation);
	}

	public void removeRepresentation(Representation representation) {
		characteristicForms.remove(representation);
	}

	public boolean hasDefaultCharacteristicForm() {
		return hasDefaultRepresentation();
	}

}
