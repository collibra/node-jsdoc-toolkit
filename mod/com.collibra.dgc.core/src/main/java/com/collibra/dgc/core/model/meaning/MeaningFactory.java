package com.collibra.dgc.core.model.meaning;

import org.springframework.stereotype.Service;

import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;

/**
 * Factory methods for creating different kinds of {@link Meaning}.
 * @author dtrog
 * 
 */
@Service
public interface MeaningFactory {

	ObjectType makeObjectType();

	ObjectType makeObjectType(String uuid);

	BinaryFactType makeBinaryFactType();

	BinaryFactType makeBinaryFactType(String uuid);

	Characteristic makeCharacteristic();

	Characteristic makeCharacteristic(String uuid);

	SimpleProposition makeSimpleProposition();

	SimpleProposition makeSimpleProposition(String uuid);

	/**
	 * Makes a new {@link ObjectType} based on the given {@link ObjectType}. When persisted this will become a new
	 * version of the given {@link ObjectType}.
	 * @param objectType {@link ObjectType} to base on.
	 * @return The copy of the {@link ObjectType}.
	 */
	ObjectType makeObjectType(ObjectType objectType);

}
