package com.collibra.dgc.core.model.meaning.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.SimpleProposition;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.meaning.facttype.impl.BinaryFactTypeImpl;
import com.collibra.dgc.core.model.meaning.facttype.impl.CharacteristicImpl;

@Service
public class MeaningFactoryImpl implements MeaningFactory {

	public BinaryFactType makeBinaryFactType() {
		return makeBinaryFactType(UUID.randomUUID().toString());
	}

	public Characteristic makeCharacteristic() {
		return makeCharacteristic(UUID.randomUUID().toString());
	}

	public ObjectType makeObjectType() {
		return makeObjectType(UUID.randomUUID().toString());
	}

	public BinaryFactType makeBinaryFactType(String uuid) {
		return new BinaryFactTypeImpl(uuid);

	}

	public Characteristic makeCharacteristic(String uuid) {
		return new CharacteristicImpl(uuid);

	}

	public ObjectType makeObjectType(String uuid) {
		return new ObjectTypeImpl(uuid);
	}

	public ObjectType makeObjectType(ObjectType objectType) {
		return new ObjectTypeImpl(objectType, UUID.randomUUID().toString());
	}

	public SimpleProposition makeSimpleProposition() {
		return makeSimpleProposition(UUID.randomUUID().toString());
	}

	public SimpleProposition makeSimpleProposition(String uuid) {
		return new SimplePropositionImpl();
	}

}
