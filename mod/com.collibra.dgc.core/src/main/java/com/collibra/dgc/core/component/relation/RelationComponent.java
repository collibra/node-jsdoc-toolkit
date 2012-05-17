package com.collibra.dgc.core.component.relation;

import java.util.Collection;

import com.collibra.dgc.core.model.relation.Relation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;

public interface RelationComponent {

	/**
	 * Get a {@link Relation} by its resource id {@link String}.
	 * 
	 * @param rId the resource id of the {@link Relation}
	 * @return the {@link Relation}
	 */
	Relation getRelation(String rId);

	/**
	 * Remove the given {@link Relation} from the database.
	 * 
	 * @param rId the resource id of the {@link Relation} to remove
	 */
	void removeRelation(String rId);

	/**
	 * Find all {@link Relation}s by the given relation type {@link BinaryFactTypeForm} resource id.
	 * 
	 * @param binaryFactTypeFormRId the resource id of the relation type {@link BinaryFactTypeForm}
	 * @return a {@link Collection} of {@link Relation}s
	 */
	Collection<Relation> findRelationsByType(String binaryFactTypeFormRId);

	/**
	 * Find all {@link Relation}s by the given source {@link Term} resource id.
	 * 
	 * @param sourceTermRId the resource id of the source {@link Term} of the {@link Relation}
	 * @return a {@link Collection} of {@link Relation}s
	 */
	Collection<Relation> findRelationsBySource(String sourceTermRId);

	/**
	 * Find all {@link Relation}s by the given target {@link Term} resource id.
	 * 
	 * @param targetTermRId the resource id of the target {@link Term} of the {@link Relation}
	 * @return a {@link Collection} of {@link Relation}s
	 */
	Collection<Relation> findRelationsByTarget(String targetTermRId);

	/**
	 * Find all {@link Relation}s by the given source {@link Term} and target {@link Term}.
	 * 
	 * @param sourceTermRId the resource id of the source {@link Term} of the {@link Relation}
	 * @param targetTermRId the resource if the target {@link Term} of the {@link Relation}
	 * @return a {@link Collection} of {@link Relation}s
	 */
	Collection<Relation> findRelationsBySourceAndTarget(String sourceTermRId, String targetTermRId);

	/**
	 * Find all {@link Relation}s by the given source {@link Term} and relation type {@link BinaryFactTypeForm}.
	 * 
	 * @param binaryFactTypeFormRId the relation type {@link BinaryFactTypeForm} of the {@link Relation}
	 * @param sourceTermRId the resource if of the source {@link term} of the {@link Relation}
	 * @return a {@link Collection} of {@link Relation}s
	 */
	Collection<Relation> findRelationsBySourceAndType(String binaryFactTypeFormRId, String sourceTermRId);

	/**
	 * Find all {@link Relation}s by the given target {@link Term} and relation type {@link BinaryFactTypeForm}.
	 * 
	 * @param binaryFactTypeFormRId the resource id of the relation type {@link BinaryFactTypeForm} of the
	 *            {@link Relation}
	 * @param targetTermRId the resource id of the target {@link Term} of the {@link Relation}
	 * @return a {@link Collection} of {@link Relation}s
	 */
	Collection<Relation> findRelationsByTargetAndType(String binaryFactTypeFormRId, String targetTermRId);

	/**
	 * Find all {@link Relation}s by the given source {@link Term}, target {@link Term} and relation type
	 * {@link BinaryFactTypeForm}.
	 * 
	 * @param binaryFactTypeFormRId the resource id of the relation type {@link BinaryFactTypeForm} of the
	 *            {@link Relation}
	 * @param sourceTermRId the resource id of the source {@link Term} of the {@link Relation}
	 * @param targetTermRId the resource id of the target {@link Term} of the {@link Relation}
	 * @return a {@link Collection} of {@link Relation}s
	 */
	Collection<Relation> findRelationsBySourceAndTargetAndType(String binaryFactTypeFormRId, String sourceTermRId,
			String targetTermRId);

	/**
	 * Create a new {@link Relation} and add it to the database.
	 * 
	 * @param sourceTermRId the resource id of the source {@Term} of the {@link Relation}
	 * @param binaryFactTypeFormRId the resource id of the type {@link BinaryFactTypeForm} of the {@link Relation}
	 * @param targetTermRId the resource id of the target {@link Term} of the {@link Relation}
	 * @return the newly created and persisted {@link Relation}
	 */
	Relation addRelation(String sourceTermRId, String binaryFactTypeFormRId, String targetTermRId);

	/**
	 * Create a new {@link Relation} and add it to the database.
	 * 
	 * @param sourceTermRId the resource id of the source {@Term} of the {@link Relation}
	 * @param binaryFactTypeFormRId the resource id of the type {@link BinaryFactTypeForm} of the {@link Relation}
	 * @param targetTermSignifier the signifier of the {@link Term} that will be created as target of the
	 *            {@link Relation}
	 * @param targetTermVocabularyRId the resource id of the {@link Vocabulary} of the target {@link Term} of the
	 *            {@link Relation}
	 * @return the newly created and persisted {@link Relation}
	 */
	Relation addRelation(String sourceTermRId, String binaryFactTypeFormRId, String targetTermSignifier,
			String targetTermVocabularyRId);
}
