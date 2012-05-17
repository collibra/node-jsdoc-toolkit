/**
 * 
 */
package com.collibra.dgc.core.dao;

import java.util.List;

import com.collibra.dgc.core.model.relation.Relation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;

/**
 * @author fvdmaele
 *
 */
public interface RelationDao extends AbstractDao<Relation> {

	public Relation save(Relation relation);

	List<Relation> findByType(BinaryFactTypeForm type);

	List<Relation> findBySource(Term source);

	List<Relation> findByTarget(Term target);

	List<Relation> findBySourceAndType(BinaryFactTypeForm type, Term source);

	List<Relation> findByTargetAndType(BinaryFactTypeForm type, Term target);

	List<Relation> findBySourceAndTarget(Term source, Term target);

	List<Relation> findBySourceAndTargetAndType(Term source, Term target, BinaryFactTypeForm type);
}
