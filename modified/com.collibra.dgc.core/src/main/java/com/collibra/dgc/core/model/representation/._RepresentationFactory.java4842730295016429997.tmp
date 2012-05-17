package com.collibra.dgc.core.model.representation;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;

/**
 * Factory methods for creating different kinds of {@link Representation}.
 * @author dtrog
 * 
 */
@Service
public interface RepresentationFactory {

	/**
	 * Creates a new {@link Term} and new {@link ObjectType} which it represents.
	 * @param vocabulary The {@link Vocabulary} to add the {@link Term} to
	 * @param signifier The expression that signifies the {@link Term}
	 * @return The new {@link Term}
	 */
	Term makeTerm(Vocabulary vocabulary, String signifier);

	/**
	 * Creates a new list {@link Term} and new {@link ObjectType} which it represents for each signifier.
	 * @param vocabulary The {@link Vocabulary} to add the list of {@link Term} to
	 * @param signifiersDotSeparated The expression that signifies a list of {@link Term} with dot separated.
	 * @return The new {@link List} representing newly created {@link Term}
	 */
	List<Term> makeTerms(Vocabulary vocabulary, String signifiersDotSeparated);

	/**
	 * Creates a new {@link Term}
	 * @param vocabulary The {@link Vocabulary} to add the {@link Term} to
	 * @param signifier The expression that signifies the {@link Term}
	 * @param objectType The {@link ObjectType} this {@link Term} represents
	 * @return The new {@link Term}
	 */
	Term makeTerm(Vocabulary vocabulary, String signifier, ObjectType objectType);

	/**
	 * Creates a new Term
	 * @param vocabulary The {@link Vocabulary} in which the {@link Term} should be stored.
	 * @param signifier The signfifier for the term.
	 * @param objectType The type of the concept for the type.
	 * @return A new {@link Term}
	 */
	Term makeTermOfType(Vocabulary vocabulary, String signifier, ObjectType objectType);

	/**
	 * Copies a {@link Term} to specified {@link Vocabulary}.
	 * @param term The {@link Term} to be copied.
	 * @param vocabulary The destination {@link Vocabulary}.
	 * @return The newly copied {@link Term}.
	 */
	Term copyTerm(Term term, Vocabulary vocabulary);

	/**
	 * Creates a new {@link Term} that is a synonym for the given {@link Term} in the {@link Vocabulary}.
	 * @param vocabulary The {@link Vocabulary} in which the synonym is created.
	 * @param term The {@link Term} to create a synonym for.
	 * @param signifier The signifier that expresses the synonym.
	 * @return The synonym {@link Term}.
	 */
	Term makeSynonym(Vocabulary vocabulary, Term term, String signifier);

	/**
	 * Creates a new {@link CharacteristicForm} that is a synonymous form for the given {@link CharacteristicForm} in
	 * the {@link Vocabulary}.
	 * @param vocabulary The {@link Vocabulary} in which the synonymous form is created.
	 * @param cForm The {@link CharacteristicForm} to create a synonymous form for.
	 * @param termExpression The expression for the term in the {@link CharacteristicForm}
	 * @param role The expression for the role in the {@link CharacteristicForm}
	 * @return The synonymous form {@link Characteristic}.
	 */
	CharacteristicForm makeSynonymousForm(Vocabulary vocabulary, CharacteristicForm cForm, Term term, String role);

	/**
	 * Creates a new {@link BinaryFactTypeForm} that is a synonymous form for the given {@link BinaryFactTypeForm} in
	 * the {@link Vocabulary}.
	 * @param vocabulary The {@link Vocabulary} in which the synonymous form is created.
	 * @param bftForm The {@link BinaryFactTypeForm} to create a synonymous form for.
	 * @param headTermExpression The expression for the head {@link Term} of the {@link BinaryFactTypeForm}
	 * @param role The expression for the role in the {@link BinaryFactTypeForm}
	 * @param coRole The expression for the coRole in the {@link BinaryFactTypeForm}
	 * @param tailTermExpression The expression for the tail {@link Term} of the {@link BinaryFactTypeForm}
	 * @return The synonymous form {@link Characteristic}.
	 */
	BinaryFactTypeForm makeSynonymousForm(Vocabulary vocabulary, BinaryFactTypeForm bftForm, Term headTerm,
			String role, String coRole, Term tailTerm);

	/**
	 * Creates a new {@link CharacteristicForm} and {@link Characteristic} which it represents
	 * @param vocabulary The {@link Vocabulary} to add the name to.
	 * @param termExpression The expression for the term in the {@link CharacteristicForm}
	 * @param role The expression for the role in the {@link CharacteristicForm}
	 * @return The new {@link CharacteristicForm}
	 */
	CharacteristicForm makeCharacteristicForm(Vocabulary vocabulary, Term term, String role);

	/**
	 * Creates a new {@link CharacteristicForm}
	 * @param vocabulary The {@link Vocabulary} to add the {@link Name} to.
	 * @param termExpression The expression for the term in the {@link CharacteristicForm}
	 * @param role The expression for the role in the {@link CharacteristicForm}
	 * @param characteristic The {@link Characteristic} this {@link CharacteristicForm} represents
	 * @return The new {@link CharacteristicForm}
	 */
	CharacteristicForm makeCharacteristicForm(Vocabulary vocabulary, Term term, String role,
			Characteristic characteristic);

	/**
	 * Creates a new {@link BinaryFactTypeForm} and {@link BinaryFactType} which it represents.
	 * @param vocabulary The vocabulary that contains the {@link BinaryFactTypeForm}
	 * @param headTermExpression The expression for the head {@link Term} of the {@link BinaryFactTypeForm}
	 * @param role The expression for the role in the {@link BinaryFactTypeForm}
	 * @param coRole The expression for the coRole in the {@link BinaryFactTypeForm}
	 * @param tailTermExpression The expression for the tail {@link Term} of the {@link BinaryFactTypeForm}
	 * @return The {@link BinaryFactTypeForm}
	 */
	BinaryFactTypeForm makeBinaryFactTypeForm(Vocabulary vocabulary, Term headTerm, String role, String coRole,
			Term tailTerm);

	/**
	 * Creates a new {@link BinaryFactTypeForm}
	 * @param vocabulary The vocabulary that contains the {@link BinaryFactTypeForm}
	 * @param headTermExpression The expression for the head {@link Term} of the {@link BinaryFactTypeForm}
	 * @param role The expression for the role in the {@link BinaryFactTypeForm}
	 * @param coRole The expression for the coRole in the {@link BinaryFactTypeForm}
	 * @param tailTermExpression The expression for the tail {@link Term} of the {@link BinaryFactTypeForm}
	 * @param binaryFactType The {@link BinaryFactType} this {@link BinaryFactTypeForm} represents
	 * @return The {@link BinaryFactTypeForm}
	 */
	BinaryFactTypeForm makeBinaryFactTypeForm(Vocabulary vocabulary, Term headTerm, String role, String coRole,
			Term tailTerm, BinaryFactType binaryFactType);

	StringAttribute makeStringAttribute(Term label, Representation representation, String value);

	/**
	 * Creates a new custom attribute type.
	 * 
	 * @param voc The Collibra SBVR extensions vocabulary.
	 * @param signifier The signifier of the term that represents the custom attribute type.
	 * @return The Term that represents the custom attribute type
	 */
	Term makeCustomAttributeType(Vocabulary voc, String signifier);

	/**
	 * Creates a new custom attribute type. Since you specify the objectType, this is intended for creating synonyms.
	 * 
	 * @param voc The Collibra SBVR extensions vocabulary.
	 * @param signifier The signifier of the term that represents the custom attribute type.
	 * @param objectType The meaning of the custom attribute type
	 * @return The Term that represents the custom attribute type
	 */
	Term makeCustomAttributeType(Vocabulary voc, String signifier, ObjectType objectType);

	/**
	 * Creates a translation of the given shared term for the custom attribute type.
	 * 
	 * @param voc The Collibra SBVR extensions vocabulary.
	 * @param sharedTerm The term that is shared from the vocabulary that we want to translate.
	 * @param signifier The translation of the custom attribute type.
	 * @return The Term that represents the custom attribute type.
	 */
	Term makeCustomAttributeType(Vocabulary voc, Term sharedTerm, String signifier);

	/** makes a vocabulary with a bulk list of terms given **/
	Vocabulary makeVocabularyBulkTerms(Community community, String uri, String name, String... signifiers);

	SingleValueListAttribute makeSingleValueListAttribute(Term label, Representation representation, String value);

	MultiValueListAttribute makeMultiValueListAttribute(Term label, Representation representation,
			Collection<String> values);

	/**
	 * Creates a new {@link Vocabulary} of type Glossary.
	 * @param uri The URI for the {@link Vocabulary}
	 * @param name The name of the {@link Vocabulary}
	 * @param community The {@link Community} that owns the {@link Vocabulary}.
	 * @return The {@link Vocabulary}.
	 */
	Vocabulary makeVocabulary(Community community, String uri, String name);

	/**
	 * Creates a new {@link Vocabulary} of a given vocabulary type.
	 * @param uri The URI for the {@link Vocabulary}
	 * @param name The name of the {@link Vocabulary}
	 * @param community The {@link Community} that owns the {@link Vocabulary}.
	 * @param type The {@link ObjectType} type of the {@link Vocabulary}.
	 * @return The {@link Vocabulary}.
	 */
	Vocabulary makeVocabularyOfType(Community community, String uri, String name, ObjectType type);

	/**
	 * Make a {@link DateTimeAttribute}
	 * @param label The {@link Term} that is the label for the {@link DateTimeAttribute}.
	 * @param representation The {@link Representation} for which the {@link DateTimeAttribute} is created.
	 * @param value The {@link Calendar} value for the {@link DateTimeAttribute}.
	 * @return The {@link DateTimeAttribute}.
	 */
	DateTimeAttribute makeDateTimeAttribute(Term label, Representation representation, Calendar value);
}
