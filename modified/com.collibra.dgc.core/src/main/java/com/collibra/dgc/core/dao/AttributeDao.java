package com.collibra.dgc.core.dao;

import java.util.List;
import java.util.Map;

import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.DateTimeAttribute;
import com.collibra.dgc.core.model.representation.MultiValueListAttribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

public interface AttributeDao extends AbstractDao<Attribute> {

	/**
	 * Looks up the custom {@link Attribute}s of a certain {@link ObjectType} for a particular {@link Meaning} in the
	 * given {@link Vocabulary}.
	 * @param type {@link ObjectType} that denotes the custom type of the {@link Attribute}.
	 * @param owner {@link Representation} that attribute represents.
	 * @param vocabulary {@link Vocabulary} that contains the attributes.
	 * @return A list of {@link Attribute}s
	 */
	List<Attribute> findAttributesByTypeAndOwner(Term label, Representation owner);

	/**
	 * Find the attributes of a representation that are of one of the specified {@link Attribute} {@code Type}.
	 * 
	 * @param labels The {@code Label} {@link Term} of the {@link Attribute} {@code Types}
	 * @param owner The {@link Representation} for which {@link Attribute} are defined
	 * @return A {@link List} of {@link Attribute}
	 */
	List<Attribute> findAttributesByTypesAndOwner(Term[] labels, Representation owner);

	List<StringAttribute> findStringAttributesByTypeAndOwner(Term label, Representation owner);

	List<MultiValueListAttribute> findMultiValueListAttributesByTypeAndOwner(Term label, Representation owner);

	List<Attribute> findAttributesByVocabulary(Vocabulary vocabulary);

	/**
	 * Searches for attributes by their expression.
	 * 
	 * @param partialExpression the string that is part the of expression of the retrieved attributes
	 * @param limit the maximum number of attributes to retrieve; if 0 then all attributes will be retrieved
	 * @return the list of attributes that match {@code partialExpression}
	 */
	List<Attribute> searchAttributesForLongExpression(String partialExpression, int offset, int number);

	/**
	 * 
	 * @param owner
	 * @return
	 */
	List<Attribute> findAllAttributesByOwner(Representation owner);

	Map<ObjectType, List<Attribute>> findAttributesByOwnerInMap(Representation owner);

	/**
	 * 
	 * @return
	 */
	List<Attribute> findAllWithoutOwner();

	/**
	 * 
	 * @return
	 */
	List<Attribute> findAllWithoutLabel();

	List<Term> findAttributeTypeLabels();

	StringAttribute getStringAttribute(String rId);

	MultiValueListAttribute getMultiValueListAttribute(String rId);

	SingleValueListAttribute getSingleValueListAttribute(String rId);

	List<StringAttribute> findStringAttributesByOwner(Representation owner);

	List<MultiValueListAttribute> findMultiValueListAttributesByOwner(Representation owner);

	List<Attribute> findAttributesByType(Term attributeLabel);

	List<SingleValueListAttribute> findSingleValueListAttributesByOwner(Representation owner);

	List<SingleValueListAttribute> findSingleValueListAttributesByTypeAndOwner(Term label, Representation owner);

	DateTimeAttribute getDateTimeAttribute(String rId);

	List<DateTimeAttribute> findDateTimeAttributesByTypeAndOwner(Term label, Representation owner);

	List<DateTimeAttribute> findDateTimeAttributesByOwner(Representation owner);

	// /**
	// * Check if the attribute is persisted or not.
	// * @param attribute The {@link Attribute}
	// * @return {@code true} if it is persisted, {@code false} otherwise
	// */
	// boolean isPersisted(final Attribute attribute);
}
