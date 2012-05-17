package com.collibra.dgc.core.service;

import java.util.Collection;
import java.util.List;

import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * Supports the methods in the meaning daos with extra validation logic
 * @author dtrog
 * 
 */
public interface MeaningService {

	/**
	 * Creates a new version of the given {@link ObjectType} in the persistent storage.
	 * @param objectType The {@link ObjectType} to persist.
	 * @return The persisted {@link ObjectType}
	 */
	ObjectType saveAndCascade(ObjectType objectType);

	/**
	 * Creates a new version of the given {@link Characteristic} in the persistent storage.
	 * @param characteristic The {@link Characteristic} to persist.
	 * @return The persisted {@link Characteristic}
	 */
	Characteristic saveAndCascade(Characteristic characteristic);

	/**
	 * Creates a new version of the given {@link BinaryFactType} in the persistent storage.
	 * @param binaryFactType The {@link BinaryFactType} to persist.
	 * @return The persisted {@link BinaryFactType}.
	 */
	BinaryFactType saveAndCascade(BinaryFactType binaryFactType);

	Category saveAndCascade(Category category);

	CategorizationType saveAndCascade(CategorizationType categorizationType);

	/**
	 * Updates this object without creating a new version for it.
	 * @param objectType The {@link ObjectType} to update.
	 * @return The updated {@link ObjectType}.
	 */
	ObjectType update(ObjectType objectType);

	/**
	 * Updates this object without creating a new version for it.
	 * @param characteristic The object to update
	 * @return The updated object
	 */
	Characteristic update(Characteristic characteristic);

	/**
	 * Updates this object without creating a new version for it.
	 * @param binaryFactType The object to update
	 * @return The updated object
	 */
	BinaryFactType update(BinaryFactType binaryFactType);

	/**
	 * Updates this object without creating a new version for it.
	 * @param concept The object to update
	 * @return The updated object
	 */
	Concept update(Concept concept);

	/**
	 * Finds all the {@link ObjectType}s that are represented by the {@link Term}s in the {@link Vocabulary}.
	 * @param vocabulary The {@link Vocabulary} to get all represented {@link ObjectType}s for.
	 * @return The {@link ObjectType}s in the {@link Vocabulary}
	 */
	List<ObjectType> findAllObjectTypesRepresentedInVocabulary(Vocabulary vocabulary);

	/**
	 * Finds all the {@link Concept}s that have as concept type the given {@link Concept}.
	 * @param conceptType The concept type of the to be returned Concepts.
	 * @return The List of Concepts with the given concept type.
	 */
	List<Concept> findAllConceptsWithType(Concept conceptType);

	/**
	 * Retrieves the general {@link Concept}s for the given {@link Concept}. Iteratively creates the list by going again
	 * to the each general {@link Concept}
	 * @param concept The {@link Concept} that is the general {@link Concept}.
	 * @return The list of generalized {@link Concept}s
	 */
	List<Concept> findTaxonomicalParentConcepts(Concept generalConcept);

	/**
	 * Finds the latest version of a {@link ObjectType} identified by resource id.
	 * @param resourceId The resource id.
	 * @return The latest version of the {@link ObjectType}
	 */
	ObjectType findObjectTypeByResourceId(String resourceId);

	/**
	 * @return The {@link Concept} for specified resource id.
	 */
	Concept findConceptByResourceId(String resourceId);

	/**
	 * 
	 * @param resourceId
	 * @return
	 */
	Category findCategoryByResourceId(String resourceId);

	/**
	 * 
	 * @param meaning
	 * @return
	 */
	Meaning saveAndCascade(Meaning meaning);

	/**
	 * Checks the type of the general concept and persists it accordingly. If unknown default to {@link ObjectType}.
	 * @param concept The general concept to decide the type for
	 * @return The persisted general concept
	 */
	Concept saveConcept(Concept concept);

	/**
	 * Retrieves all the specialized {@link Concept}s for the given {@link Concept}. That means it will return all
	 * specialized concepts for any depth.
	 * @param concept The {@link Concept} that is the general {@link Concept}.
	 * @return The list of all specialized {@link Concept}s
	 */
	List<Concept> findAllSpecializedConcepts(Concept concept);

	/**
	 * Retrieves the direct specialized {@link Concept}s for the given {@link Concept}.
	 * @param concept the {@link Concept} that is the general {@link Concept}.
	 * @return The list of directly specialized {@link Concept}s.
	 */
	List<Concept> findSpecializedConcepts(final Concept concept);

	/**
	 * Finds all the {@link CategorizationScheme}s for the given {@link ObjectType}.
	 * @param generalConcept The {@link ObjectType} to find the {@link CategorizationScheme}s for.
	 * @return The {@link CategorizationScheme}s
	 */
	Collection<Concept> findAllCategorizationSchemes(ObjectType generalConcept);

	/**
	 * Removes the current {@link Meaning}.
	 * @param concept The {@link Meaning} which is to be removed.
	 */
	void remove(Meaning meaning);

	/**
	 * @return The meta {@link ObjectType} for the SBVR Thing.
	 */
	ObjectType findMetaThing();

	/**
	 * @return The meta {@link ObjectType} for the SBVR Object Type.
	 */
	ObjectType findMetaObjectType();

	/**
	 * @return The meta {@link ObjectType} for the Business Asset, a specialized concept of the SBVR Object Type.
	 */
	ObjectType findMetaBusinessAsset();

	/**
	 * @return The meta {@link ObjectType} for the Business Asset, a specialized concept of Business Asset
	 */
	ObjectType findMetaBusinessTerm();

	/**
	 * @return The meta {@link ObjectType} for the Business Asset, a specialized concept of the SBVR Object Type
	 */
	ObjectType findMetaTechnicalAsset();

	/**
	 * @return The meta {@link ObjectType} for the Business Asset, a specialized concept of Technical Asset
	 */
	ObjectType findMetaCode();

	/**
	 * @return The meta {@link ObjectType} for the Business Asset, a specialized concept of the SBVR Object Type
	 */
	ObjectType findMetaQualityAsset();

	/**
	 * @return The meta {@link ObjectType} for the SBVR Binary Fact Type.
	 */
	ObjectType findMetaBinaryFactType();

	/**
	 * @return The meta {@link ObjectType} for the SBVR Characteristic.
	 */
	ObjectType findMetaCharacteristic();

	/**
	 * @return The meta {@link ObjectType} for the SBVR Individual.
	 */
	ObjectType findMetaIndividualConcept();

	/**
	 * @return The meta {@link ObjectType} for the SBVR Category.
	 */
	ObjectType findMetaCategory();

	ObjectType findMetaDefinition();

	ObjectType findMetaDescription();

	ObjectType findMetaExample();

	ObjectType getMetaStatusType();

	ObjectType findMetaNote();

	/**
	 * @return
	 */
	ObjectType findMetaVocabularyType();

	/**
	 * @return
	 */
	ObjectType findMetaBusinessVocabularyType();

	/**
	 * @return
	 */
	ObjectType findMetaGlossaryVocabularyType();

	Characteristic findCharacteristicByResourceId(String resourceId);

	BinaryFactType findBinaryFactTypeByResourceId(String resourceId);

	/**
	 * Remove the general {@link Concept} and reset it to meta thing.
	 * @param specializedConcept The {@link Concept} whose general {@link Concept} needs to be removed.
	 */
	void removeGeneralConcept(Concept specializedConcept);

	Concept getConceptWithError(String resourceId);

	ObjectType getObjectTypeWithError(String resourceId);

	/**
	 * Return true if the given concept has a general concept that equals the given parent
	 * @param concept The {@link Concept} that must have as one of its general concepts, the given parent
	 *            {@link Concept}.
	 * @param parent The parent {@link Concept} that must equal one of the given {@link Concept}'s general concepts
	 * @return true if found, false if not
	 */
	boolean hasGeneralConcept(Concept concept, Concept parent);
}
