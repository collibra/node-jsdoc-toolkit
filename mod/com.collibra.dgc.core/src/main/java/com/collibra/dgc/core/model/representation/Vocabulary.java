package com.collibra.dgc.core.model.representation;

import java.util.Set;

import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.Verbalisable;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.rules.RuleSet;

/**
 * A Vocabulary is a Set of {@link Designation}s and Fact Type Forms primarily drawn from a single {@link Language} to
 * express {@link Concept}s within a Body of Shared Meanings.
 * 
 * Vocabularies are versioned. The combination of version and id refer to a particular vocabulary of a specific version.
 * 
 * @author dtrog
 * 
 */
public interface Vocabulary extends Resource, Verbalisable {

	/**
	 * @return The name for this vocabulary
	 */
	String getName();

	/**
	 * @param name The name for this vocabulary
	 */
	void setName(String name);

	/**
	 * @return the {@link ObjectType} type of this vocabulary
	 */
	ObjectType getType();
	
	/**
	 * @return The uri for this vocabulary
	 */
	String getUri();

	/**
	 * @param uri The uri for this vocabulary
	 */
	void setUri(String namespace);

	/**
	 * Adds a {@link Term}
	 * 
	 * @param term {@link Term} to add.
	 */
	void addTerm(Term term);

	/**
	 * Adds a {@link BinaryFactTypeForm}.
	 * 
	 * @param bftf The {@link BinaryFactTypeForm} to add.
	 */
	void addBinaryFactTypeForm(BinaryFactTypeForm bftf);

	/**
	 * Adds a {@link CharacteristicForm}.
	 * 
	 * @param charForm {@link CharacteristicForm} to add.
	 */
	void addCharacteristicForm(CharacteristicForm charForm);

	/**
	 * 
	 * @return The designations (all {@link Term}s amd {@link Name}s for {@link Concept}s) contained in the vocabulary.
	 */
	Set<Designation> getDesignations();

	/**
	 * 
	 * @return All kinds of {@link Representation} in this vocabulary.
	 */
	Set<Representation> getRepresentations();

	/**
	 * 
	 * @return The {@link Term}s contained in the vocabulary.
	 */
	Set<Term> getTerms();

	/**
	 * @return The {@link Term}s contained in the vocabulary and all its incorporated vocabularies.
	 */
	Set<Term> getAllTerms();

	/**
	 * Looks up the preferred {@link Representation} in this {@link Vocabulary} for the given {@link Meaning}
	 * @param meaning The {@link Meaning} to find the preferred {@link Representation} for.
	 * @return The preferred {@link Representation}.
	 */
	Representation getPreferredRepresentation(Meaning meaning);

	/**
	 * @param meaning The meaning of the requested term
	 * @return The term in the vocabulary with the given meaning, null if no term is found.
	 */
	Term getPreferredTerm(Meaning meaning);

	/**
	 * recursive version of {@link #getPreferredTerm(Meaning)} that searches all incorporated vocabularies
	 */
	Term getPreferredTermInAllIncorporatedVocabularies(Meaning meaning);

	/**
	 * @param meaning The meaning of the requested characteristicForm
	 * @return The characteristicForm in the vocabulary with the given meaning, null if no characteristicForm is found.
	 */
	CharacteristicForm getPreferredCharacteristicForm(Meaning meaning);

	/**
	 * @param meaning The meaning of the requested binaryFactTypeForm
	 * @return The binaryFactTypeForm in the vocabulary with the given meaning, null if no binaryFactTypeForm is found.
	 */
	BinaryFactTypeForm getPreferredBinaryFactTypeForm(Meaning meaning);

	/**
	 * 
	 * @return The {@link Name}s and {@link Term}s contained in the vocabulary.
	 */
	Set<Representation> getNamesAndTerms();

	/**
	 * 
	 * @return The {@link BinaryFactTypeForm}s (all {@link VocabularyEntry}s for {@link BinaryFactType}s) in the
	 *         vocabulary.
	 */
	Set<BinaryFactTypeForm> getBinaryFactTypeForms();

	/**
	 * 
	 * @return The {@link CharacteristicForm}s (all {@link VocabularyEntry}s for {@link Characteristic}s in the
	 *         vocabulary.
	 */
	Set<CharacteristicForm> getCharacteristicForms();

	/**
	 * 
	 * @return The vocabularies that are directly incorporated by this {@link Vocabulary} (from a graph perspective, it
	 *         will return all the vocabularies that have a direct "incorporation" link with this vocabulary). For
	 *         example, if A --> B and B --> C. C.getIncorporatedVocabularies() will return (B)
	 */
	Set<Vocabulary> getIncorporatedVocabularies();

	/**
	 * Get incorporated {@link Vocabulary}s.
	 * @param excludeSBVR Flag if true to exclude {@link Vocabulary}s from SBVR communities.
	 * @return Incorporated {@link Vocabulary}s.
	 */
	Set<Vocabulary> getIncorporatedVocabularies(boolean excludeSBVR);

	/**
	 * 
	 * @return All the (also transitively) {@link Vocabulary}s that are incorporated by this {@link Vocabulary}, except
	 *         for the SBVR {@link Vocabulary}s.
	 */
	Set<Vocabulary> getAllNonSbvrIncorporatedVocabularies();

	/**
	 * 
	 * @return All the vocabularies that are incorporated by this {@link Vocabulary}. For example, if A --> B and B -->
	 *         C. C.getAllIncorporatedVocabularies() will return (A and B)
	 */
	Set<Vocabulary> getAllIncorporatedVocabularies();

	/**
	 * Disincorporates a vocabulary
	 * 
	 * @param incorporatedVoc The vocabulary to disincorporate.
	 */
	void removeIncorporatedVocabulary(Vocabulary incorporatedVoc);

	/**
	 * Incorporates a vocabulary.
	 * 
	 * @param incorporatedVoc The vocabulary to incorporate.
	 */
	void addIncorporatedVocabulary(Vocabulary incorporatedVoc);

	/**
	 * To get {@link Community} that owns this {@link Vocabulary}.
	 * @return The {@link Community}.
	 */
	public Community getCommunity();

	public void setCommunity(final Community community);

	/**
	 * 
	 * @return The {@link RuleSet}s owned by this vocabulary.
	 */
	Set<RuleSet> getRuleSets();

	/**
	 * Adds the {@link RuleSet} to the {@link Vocabulary}.
	 * @param ruleSet The {@link RuleSet} to add.
	 */
	void addRuleSet(RuleSet ruleSet);

	/**
	 * To get {@link Term} with specified signifier.
	 * @param signifier The signifer.
	 * @return The {@link Term}
	 */
	Term getTerm(final String signifier);

	/**
	 * To get {@link Term} with specified signifier, keeps searching in the incorporated vocabularies depth first and
	 * eagerly returns the result.
	 * 
	 * Note that this is a very costly operation since it will take in worst case O(n) where n is all the terms in this
	 * vocabulary and all its incorpated vocabularies.
	 * @param signifier The signifer.
	 * @return The {@link Term}
	 */
	Term getTermInAllIncorporatedVocabularies(final String signifier);

	/**
	 * 
	 * @return A field to field copy of this {@link Vocabulary}.
	 */
	Vocabulary clone();

	/**
	 * To change the name of the vocabulary.
	 * @param name The new name to be changed to.
	 */
	void changeName(String name);

	/**
	 * Not in use yet.
	 */

	/**
	 * Check if this vocabulary is an SBVR vocabulary e.g. The URI of the vocabulary will start with
	 * "http://www.omg.org/spec/SBVR/"
	 * 
	 * @return true of false
	 */
	boolean isSBVR();

	/**
	 * Check if this vocabulary is a meta vocabulary e.g. Inside the Metamodel community.
	 * 
	 * @return true of false
	 */
	boolean isMeta();
}
