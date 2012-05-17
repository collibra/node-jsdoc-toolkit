package com.collibra.dgc.core.service;

import java.util.Collection;

import com.collibra.dgc.core.model.relation.Relation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;

public interface RelationService {

	/**
	 * Save a new Relation Type {@link BinaryFactTypeForm} in the database.
	 * 
	 * @param type the {@link BinaryFactTypeForm} to be saved
	 * @return the saved {@link BinaryFactTypeForm}
	 */
	BinaryFactTypeForm saveRelationType(BinaryFactTypeForm type);

	/**
	 * Save a new {@link Relation} in the database.
	 * 
	 * @param rel the {@link Relation} to be saved
	 * @param isNew Set if the relation is created (new relation: {@code true}) or is updated ({@code false})
	 * @return the saved {@link Relation}
	 */
	Relation saveRelation(Relation rel, boolean isNew);

	/**
	 * Get a {@link Relation} by its resource id
	 * 
	 * @param rId the resource id of the {@link Relation}
	 * @return the {@link Relation}
	 */
	Relation getRelation(String rId);

	/**
	 * Get a {@link Relation} by its resource id {@link String}. Throw a {@link EntityNotFoundException} when no
	 * {@link Relation} is found.
	 * 
	 * @param rId the resource id of the {@link Relation}
	 * @return the {@link Relation}
	 */
	Relation getRelationWithError(String rId);

	/**
	 * Find all {@link Relation}s by the given relation type {@link BinaryFactTypeForm}.
	 * 
	 * @param type the relation type {@link BinaryFactTypeForm}
	 * @return a {@link Collection} of {@link Relation}s
	 */
	Collection<Relation> findByType(BinaryFactTypeForm type);

	/**
	 * Find all {@link Relation}s by the given source {@link Term}.
	 * 
	 * @param source the source {@link Term} of the {@link Relation}
	 * @return a {@link Collection} of {@link Relation}s
	 */
	Collection<Relation> findBySource(Term source);

	/**
	 * Find all {@link Relation}s by the given target {@link Term}.
	 * 
	 * @param target the target {@link Term} of the {@link Relation}
	 * @return a {@link Collection} of {@link Relation}s
	 */
	Collection<Relation> findByTarget(Term target);

	/**
	 * Find all {@link Relation}s by the given source {@link Term} and target {@link Term}.
	 * 
	 * @param soure the source {@link Term} of the {@link Relation}
	 * @param target the target {@link Term} of the {@link Relation}
	 * @return a {@link Collection} of {@link Relation}s
	 */
	Collection<Relation> findBySourceAndTarget(Term soure, Term target);

	/**
	 * Find all {@link Relation}s by the given source {@link Term} and relation type {@link BinaryFactTypeForm}.
	 * 
	 * @param type the relation type {@link BinaryFactTypeForm} of the {@link Relation}
	 * @param source the source {@link term} of the {@link Relation}
	 * @return a {@link Collection} of {@link Relation}s
	 */
	Collection<Relation> findBySourceAndType(BinaryFactTypeForm type, Term source);

	/**
	 * Find all {@link Relation}s by the given target {@link Term} and relation type {@link BinaryFactTypeForm}.
	 * 
	 * @param type the relation type {@link BinaryFactTypeForm} of the {@link Relation}
	 * @param target the target {@link Term} of the {@link Relation}
	 * @return a {@link Collection} of {@link Relation}s
	 */
	Collection<Relation> findByTargetAndType(BinaryFactTypeForm type, Term target);

	/**
	 * Find all {@link Relation}s by the given source {@link Term}, target {@link Term} and relation type
	 * {@link BinaryFactTypeForm}.
	 * 
	 * @param type the relation type {@link BinaryFactTypeForm} of the {@link Relation}
	 * @param source the source {@link Term} of the {@link Relation}
	 * @param target the target {@link Term} of the {@link Relation}
	 * @return a {@link Collection} of {@link Relation}s
	 */
	Collection<Relation> findBySourceAndTargetAndType(BinaryFactTypeForm type, Term source, Term target);

	/**
	 * Remove the given {@link Relation} from the database.
	 * 
	 * @param rel the {@link Relation} to remove
	 */
	void remove(Relation rel);

	/**
	 * Suggest the possible relation type {@link BinaryFactTypeForm}s where the head is of the given type {@link Term}.
	 * 
	 * @param typeTerm the {@link Term} that represents the type of the source of the relation type
	 *            {@link BinaryFactTypeForm}. </br> e.g. the {@link Term} 'Object Type'
	 * @return a {@link Collection} of relation type {@link BinaryFactTypeForm}s
	 */
	Collection<BinaryFactTypeForm> suggestRelationTypesForSourceType(Term typeTerm);

	/**
	 * Suggest the possible relation type {@link BinaryFactTypeForm}s where the head is the type of the given
	 * {@link Term}.
	 * 
	 * @param typeTerm the {@link Term} of which the concept type {@link Term} is the source of the relation type
	 *            {@link BinaryFactTypeForm}.
	 * @return a {@link Collection} of relation type {@link BinaryFactTypeForm}s
	 */
	Collection<BinaryFactTypeForm> suggestRelationTypesForSourceTerm(Term t);

	/**
	 * Suggest the possible relation type {@link BinaryFactTypeForm}s where the tail is the type of the given
	 * {@link Term}.
	 * 
	 * @param typeTerm the {@link Term} of which the concept type {@link Term} is the target of the relation type
	 *            {@link BinaryFactTypeForm}. </br> e.g. the {@link Term} 'Object Type'
	 * @return a {@link Collection} of relation type {@link BinaryFactTypeForm}s
	 */
	Collection<BinaryFactTypeForm> suggestRelationTypesForTargetType(Term typeTerm);

	/**
	 * Suggest the possible relation type {@link BinaryFactTypeForm}s where the tail is of the given type {@link Term}.
	 * 
	 * @param typeTerm the {@link Term} that represents the type of the target of the relation type
	 *            {@link BinaryFactTypeForm}
	 * @return a {@link Collection} of relation type {@link BinaryFactTypeForm}s
	 */
	Collection<BinaryFactTypeForm> suggestRelationTypesForTargetTerm(Term t);

}
