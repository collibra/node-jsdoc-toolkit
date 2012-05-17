package com.collibra.dgc.core.model.representation;

import java.util.List;

import com.collibra.dgc.core.model.meaning.SimpleProposition;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;

public interface SimpleStatement extends Statement {

	/**
	 * 
	 * @return The sequence of {@link ReadingDirection}s that make up the {@link Statement}.
	 */
	List<ReadingDirection> getReadingDirections();

	/**
	 * Adds specified {@link ReadingDirection}.
	 * @param placeholder The {@link ReadingDirection} to be added.
	 */
	void addReadingDirection(ReadingDirection placeholder);

	/**
	 * @return To get the {@link SimpleProposition}.
	 */
	SimpleProposition getSimpleProposition();
}
