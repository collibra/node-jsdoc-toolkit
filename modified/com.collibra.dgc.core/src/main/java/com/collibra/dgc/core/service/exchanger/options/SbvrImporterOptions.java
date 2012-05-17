package com.collibra.dgc.core.service.exchanger.options;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Vocabulary;

public class SbvrImporterOptions {

	private final Map<String, ObjectType> signifierToObjectType;

	private final Map<String, ObjectType> signifierToGeneralConcept;

	private final List<Vocabulary> incorporatedVocabularies = new ArrayList<Vocabulary>();

	private boolean shouldReplaceExistingVocabularies = false;

	private boolean shouldRemoveSuffixIndices = false;

	private boolean shouldPersistVocabulary = true;

	private boolean shouldMinimizeImport = false;

	/**
	 * @return the shouldMinimizeImport
	 */
	public boolean isShouldMinimizeImport() {
		return shouldMinimizeImport;
	}

	/**
	 * @param shouldMinimizeImport the shouldMinimizeImport to set
	 */
	public void setShouldMinimizeImport(boolean shouldMinimizeImport) {
		this.shouldMinimizeImport = shouldMinimizeImport;
	}

	public SbvrImporterOptions() {
		super();
		signifierToObjectType = new HashMap<String, ObjectType>();
		signifierToGeneralConcept = new HashMap<String, ObjectType>();
	}

	public void addIncorporatedVocabulary(Vocabulary vocabulary) {
		incorporatedVocabularies.add(vocabulary);
	}

	public void removeIncorporatedVocabulary(Vocabulary vocabulary) {
		incorporatedVocabularies.remove(vocabulary);
	}

	public List<Vocabulary> getIncorporatedVocabularies() {
		return Collections.unmodifiableList(incorporatedVocabularies);
	}

	public boolean shouldPersistVocabulary() {
		return this.shouldPersistVocabulary;
	}

	public void setShouldPersistVocabulary(boolean shouldPersistVocabulary) {
		this.shouldPersistVocabulary = shouldPersistVocabulary;
	}

	public boolean shouldReplaceExistingVocabularies() {
		return this.shouldReplaceExistingVocabularies;
	}

	public void setShouldReplaceExistingVocabularies(boolean shouldReplaceExistingVocabularies) {
		this.shouldReplaceExistingVocabularies = shouldReplaceExistingVocabularies;
	}

	public void mapSignifierToObjectType(String signifier, ObjectType concept) {
		signifierToObjectType.put(signifier, concept);
	}

	public void unMapSignifier(String signifier) {
		signifierToObjectType.remove(signifier);
		signifierToGeneralConcept.remove(signifier);
	}

	public ObjectType getObjectTypeForSignifier(String signifier) {
		return signifierToObjectType.get(signifier);
	}

	public Collection<ObjectType> getAllObjectTypes() {
		return signifierToObjectType.values();
	}

	public void mapSignifierToGeneralConcept(String signifier, ObjectType concept) {
		signifierToGeneralConcept.put(signifier, concept);
	}

	public ObjectType getGeneralConcept(String signifier) {
		return signifierToGeneralConcept.get(signifier);
	}

	public boolean shouldRemoveSuffixIndices() {
		return this.shouldRemoveSuffixIndices;
	}

	public void setShouldRemoveSuffixIndices(boolean shouldRemoveSuffixIndices) {
		this.shouldRemoveSuffixIndices = shouldRemoveSuffixIndices;
	}
}
