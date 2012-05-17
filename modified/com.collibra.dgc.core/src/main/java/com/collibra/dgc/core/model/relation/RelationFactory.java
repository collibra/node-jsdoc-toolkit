/**
 * 
 */
package com.collibra.dgc.core.model.relation;

import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;

/**
 * Factory for creating {@link Relation} entities. You can relate two {@link Term}s by first defining their relation
 * type in the form of a {@link BinaryFactTypeForm}, as you can see in
 * {@link #makeRelationType(Term, String, String, Term)}
 * 
 * As soon as you have the type you can create relations between any two {@link Yrt,}s. You will still need the {@link RelationService}
 * to {@link RelationService#save(Relation)} persist the relations.
 * 
 * @author fvdmaele
 * 
 */
public interface RelationFactory {

	/**
	 * Make a new relation type that defines the allowed types that can have relation instances with each other.
	 * 
	 * @param sourceType the source type indicating the type of the source {@link Term} of the {@link Relation}
	 * @param role the role the source plays
	 * @param coRole the role the target plays
	 * @param targetType the target type indicating the type of the target {@link Term} of the {@link Relation}
	 * @return a {@link BinaryFactTypeForm} that is added to the Metamodel Extensions Vocabulary.
	 */
	BinaryFactTypeForm makeRelationType(Term sourceType, String role, String coRole, Term targetType);

	/**
	 * Make a new {@link Relation}.
	 * 
	 * @param source the source {@link Term} of the {@link Relation}
	 * @param target the target {@link Term} of the {@link Relation}
	 * @param type the type {@link BinaryFactTypeForm} of the {@link Relation}
	 * @return The newly created {@link Relation}
	 */
	Relation makeRelation(Term source, Term target, BinaryFactTypeForm type);

	/**
	 * Make a new {@link Relation}.
	 * 
	 * @param sourceVoc the {@link Vocabulary} that contains the source {@link Term} of the {@link Relation}
	 * @param sourceSignifier the signifier of the source {@link Term} of the {@link Relation}
	 * @param targetVoc the {@link Vocabulary} that contains the target {@link Term} of the {@link Relation}
	 * @param targetSignifier the signifier of the target {@link Term} of the {@link Relation}
	 * @param type the type {@link BinaryFactTypeForm} of the {@link Relation}
	 * @return The newly created {@link Relation}
	 */
	Relation makeRelation(Vocabulary sourceVoc, String sourceSignifier, Vocabulary targetVoc, String targetSignifier,
			BinaryFactTypeForm type);

}
