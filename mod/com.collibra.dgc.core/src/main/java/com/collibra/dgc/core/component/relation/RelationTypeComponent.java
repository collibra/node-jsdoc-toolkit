package com.collibra.dgc.core.component.relation;

import java.util.Collection;

import com.collibra.dgc.core.model.relation.Relation;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;

/**
 * Public API for the management of {@link Relation} {@code Type}. An {@link Relation} relates two {@link Representation}s. 
 * 
 * <br />
 * <br />
 * A {@link Relation} {@code Type} is represented by a {@code Type} {@link BinaryFactTypeForm}.
 * 
 * @author fvdmaele
 * 
 */
public interface RelationTypeComponent {
	
	/**
	 * Create a new relation type {@link BinaryFactTypeForm} and add it to the database.
	 * 
	 * @param sourceSignifier the signifier of the head {@link Term} of the relation type {@link BinaryFactTypeForm}
	 * @param role the role of the relation type {@link BinaryFactTypeForm}
	 * @param coRole the co role of the relation type {@link BinaryFactTypeForm}
	 * @param targetSignifier the signifier of the tail {@link Term} of the relation type {@link BinaryFactTypeForm}
	 * @return the newly created and persisted relation type {@link BinaryFactTypeForm}
	 */
	BinaryFactTypeForm addRelationType(String sourceSignifier, String role, String coRole, String targetSignifier);

	
	/**
	 * Suggest the possible relation type {@link BinaryFactTypeForm}s where the head is the type of the given {@link Term}.
	 * 
	 * @param typeTerm the resource id of the {@link Term} of which the concept type {@link Term} is the source of the relation type {@link BinaryFactTypeForm}.
	 * @return a {@link Collection} of relation type {@link BinaryFactTypeForm}s
	 */
	Collection<BinaryFactTypeForm> findPossibleRelationTypesForSourceTerm(String sourceTermRId);

	/**
	 * Suggest the possible relation type {@link BinaryFactTypeForm}s where the head is of the given type {@link Term}.
	 * 
	 * @param typeTerm the resource id of the {@link Term} that represents the type of the source of the relation type {@link BinaryFactTypeForm}. </br> e.g. the {@link Term} 'Object Type'
	 * @return a {@link Collection} of relation type {@link BinaryFactTypeForm}s
	 */
	Collection<BinaryFactTypeForm> findPossibleRelationTypesForSourceType(String sourceConceptTypeTermRId);
	
	/**
	 * Suggest the possible relation type {@link BinaryFactTypeForm}s where the tail is of the given type {@link Term}.
	 * 
	 * @param typeTerm the resource id of the {@link Term} that represents the type of the target of the relation type {@link BinaryFactTypeForm}
	 * @return a {@link Collection} of relation type {@link BinaryFactTypeForm}s
	 */
	Collection<BinaryFactTypeForm> findPossibleRelationTypesForTargetTerm(String targetTermRId);

	/**
	 * Suggest the possible relation type {@link BinaryFactTypeForm}s where the tail is the type of the given {@link Term}.
	 * 
	 * @param typeTerm the resource id of the {@link Term} of which the concept type {@link Term} is the target of the relation type {@link BinaryFactTypeForm}. </br> e.g. the {@link Term} 'Object Type'
	 * @return a {@link Collection} of relation type {@link BinaryFactTypeForm}s
	 */
	Collection<BinaryFactTypeForm> findPossibleRelationTypesForTargetType(String targetConceptTypeTermRId);

}
