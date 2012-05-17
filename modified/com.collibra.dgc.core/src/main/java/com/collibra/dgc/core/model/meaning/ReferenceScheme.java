package com.collibra.dgc.core.model.meaning;

import java.util.Set;

import com.collibra.dgc.core.model.meaning.facttype.FactTypeRole;

/**
 * A Reference Scheme is a chosen way of identifying instances of a given {@link Concept}.
 * 
 * @author dtrog
 * 
 */
public interface ReferenceScheme {

	/**
	 * 
	 * @return The {@link Concept} that is identified by the {@link ReferenceScheme}.
	 */
	Concept identifiedConcept();

	/**
	 * 
	 * @param concept The {@link Concept} to set that is identified by the {@link ReferenceScheme}.
	 */
	void setIdentifiedConcept(Concept concept);

	/**
	 * 
	 * @return The {@link FactTypeRole}s that identify the {@link Concept}.
	 */
	Set<FactTypeRole> identifyingFactTypeRoles();

	/**
	 * Add a {@link FactTypeRole} to identify this {@link Concept}.
	 * @param factTypeRole The {@link FactTypeRole} that helps identify the {@link Concept}.
	 */
	void addFactTypeRole(FactTypeRole factTypeRole);
}
