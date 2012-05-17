package com.collibra.dgc.core.dao;

import java.util.List;

import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;

public interface SimpleStatementDao extends AbstractDao<SimpleStatement> {
	/**
	 * 
	 * @param bftf
	 * @return
	 */
	List<SimpleStatement> find(BinaryFactTypeForm bftf);

	/**
	 * 
	 * @param term
	 * @return
	 */
	List<SimpleStatement> find(Term term);

	/**
	 * 
	 * @param bftf
	 * @return
	 */
	List<ReadingDirection> findReadingDirections(BinaryFactTypeForm bftf);
}
