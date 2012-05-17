package com.collibra.dgc.core.model.meaning.facttype;

import java.util.Set;

import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.facttypeform.Placeholder;
import com.collibra.dgc.core.model.user.Role;

/**
 * A FactTypeRole is a {@link Role} that specifically characterizes its instances by their involvement in an actuality
 * that is an instance of a given {@link FactType}. It can be seen as a reading direction of a {@link FactType}.
 * 
 * @author dtrog
 * 
 */
public interface FactTypeRole extends Concept {

	/**
	 * 
	 * @return The {@link ObjectType} over which this {@link FactTypeRole} ranges.
	 */
	ObjectType getObjectType();

	/**
	 * 
	 * @param objectType TThe {@link ObjectType} over which this {@link FactTypeRole} ranges.
	 */
	void setObjectType(ObjectType objectType);

	/**
	 * 
	 * @return The BinaryFactType for which this FactTypeRole is a reading direction of.
	 */
	BinaryFactType getBinaryFactType();

	/**
	 * 
	 * @return The list of {@link Placeholder}s that represent this {@link FactTypeRole}.
	 */
	Set<Placeholder> getPlaceholders();

	/**
	 * Adds a {@link Placeholder} to represent this {@link FactTypeRole}.
	 * @param placeholder The {@link Placeholder} to add.
	 */
	void addPlaceholder(Placeholder placeholder);
}
