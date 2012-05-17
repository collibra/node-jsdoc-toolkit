package com.collibra.dgc.core.model.representation.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.DateTimeAttribute;
import com.collibra.dgc.core.model.representation.MultiValueListAttribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.representation.facttypeform.impl.BinaryFactTypeFormImpl;
import com.collibra.dgc.core.model.representation.facttypeform.impl.CharacteristicFormImpl;
import com.collibra.dgc.core.model.rules.RuleFactory;
import com.collibra.dgc.core.service.impl.ConstraintChecker;

@Service
public class RepresentationFactoryImpl implements RepresentationFactory {
	private static final Logger log = LoggerFactory.getLogger(RepresentationFactoryImpl.class);
	@Autowired
	private MeaningFactory meaningFactory;
	@Autowired
	private RuleFactory ruleFactory;

	@Override
	public BinaryFactTypeForm makeBinaryFactTypeForm(Vocabulary vocabulary, Term headTerm, String role, String coRole,
			Term tailTerm, BinaryFactType binaryFactType) {

		// This constraints check cannot be moved
		// If the head/tail term is locked then binary fact type form should not be created.
		ConstraintChecker.checkLockConstraint(headTerm);
		ConstraintChecker.checkLockConstraint(tailTerm);

		// we have a sentinel type here
		BinaryFactTypeForm bftf = new BinaryFactTypeFormImpl(vocabulary, headTerm, role, coRole, tailTerm,
				binaryFactType);
		binaryFactType.addBinaryFactTypeForm(bftf);
		vocabulary.addBinaryFactTypeForm(bftf);
		return bftf;
	}

	@Override
	public CharacteristicForm makeCharacteristicForm(Vocabulary vocabulary, Term term, String role,
			Characteristic characteristic) {

		// This constraints check cannot be moved
		// If the head/tail term is locked then characteristic form should not be created.
		ConstraintChecker.checkLockConstraint(term);

		CharacteristicForm cForm = new CharacteristicFormImpl(vocabulary, term, role, characteristic);
		characteristic.addCharacteristicForm(cForm);
		vocabulary.addCharacteristicForm(cForm);
		return cForm;
	}

	@Override
	public Term makeTerm(Vocabulary vocabulary, String signifier, ObjectType objectType) {

		TermImpl term = new TermImpl(vocabulary, signifier, objectType);
		vocabulary.addTerm(term);
		objectType.addTerm(term);
		return term;
	}

	@Override
	public Term makeTerm(Vocabulary vocabulary, String signifier) {
		ObjectType meaning = meaningFactory.makeObjectType();
		return makeTerm(vocabulary, signifier, meaning);
	}

	@Override
	public List<Term> makeTerms(Vocabulary vocabulary, String signifiersDotSeparated) {
		StringTokenizer strTokenizer = new StringTokenizer(signifiersDotSeparated, ".");
		List<Term> terms = new ArrayList<Term>();
		while (strTokenizer.hasMoreTokens()) {
			String signifier = strTokenizer.nextToken().trim();
			Term term = makeTerm(vocabulary, signifier);
			if (term != null) {
				terms.add(term);
			}
		}
		return terms;
	}

	@Override
	public Term copyTerm(Term term, Vocabulary vocabulary) {
		return null;
		// TODO implement
	}

	@Override
	public BinaryFactTypeForm makeBinaryFactTypeForm(Vocabulary vocabulary, Term headTerm, String role, String coRole,
			Term tailTerm) {
		BinaryFactType meaning = meaningFactory.makeBinaryFactType();
		return makeBinaryFactTypeForm(vocabulary, headTerm, role, coRole, tailTerm, meaning);
	}

	@Override
	public CharacteristicForm makeCharacteristicForm(Vocabulary vocabulary, Term term, String role) {
		Characteristic meaning = meaningFactory.makeCharacteristic();
		return makeCharacteristicForm(vocabulary, term, role, meaning);
	}

	// TODO remove
	@Override
	public Term makeCustomAttributeType(Vocabulary voc, String signifier) {
		ObjectType meaning = meaningFactory.makeObjectType();
		return makeCustomAttributeType(voc, signifier, meaning);
	}

	// TODO remove
	@Override
	public Term makeCustomAttributeType(Vocabulary voc, String signifier, ObjectType objectType) {
		return makeTerm(voc, signifier, objectType);
	}

	// TODO remove
	@Override
	public Term makeCustomAttributeType(Vocabulary voc, Term sharedTerm, String signifier) {
		return makeSynonym(voc, sharedTerm, signifier);
	}

	@Override
	public Vocabulary makeVocabulary(Community community, String uri, String name) {
		return makeVocabularyOfType(community, uri, name, null);
	}

	@Override
	public Vocabulary makeVocabularyOfType(Community community, String uri, String name, ObjectType type) {

		Vocabulary vocabulary = new VocabularyImpl(uri, name, community, type);
		ruleFactory.makeRuleSet(vocabulary, Constants.DEFAULT_RULE_SET_NAME);

		community.addVocabulary(vocabulary);
		return vocabulary;
	}

	@Override
	public SingleValueListAttribute makeSingleValueListAttribute(Term label, Representation representation, String value) {

		// Null value is allowed

		// no check on label or type since they might need to be set later on in the services
		SingleValueListAttribute attribute = new SingleValueListAttributeImpl(label, representation, value);

		// vocabulary.addAttribute(attribute);
		representation.addAttribute(attribute);
		return attribute;
	}

	@Override
	public MultiValueListAttribute makeMultiValueListAttribute(Term label, Representation representation,
			Collection<String> values) {

		// Null values are allowed

		// no check on label or type since they might need to be set later on in the services
		MultiValueListAttribute attribute = new MultiValueListAttributeImpl(label, representation, values);

		// vocabulary.addAttribute(attribute);
		representation.addAttribute(attribute);
		return attribute;
	}

	/**
	 * Make an {@link Attribute}s
	 * @param label The {@link Term} that is the label for the {@link Attribute}. Can be <code>null</code> for built-in
	 *            attribute types.
	 * @param meaning The {@link Meaning} that the {@link Attribute} represents.
	 * @param value The textual value for the {@link Attribute}.
	 * @return The {@link Attribute}.
	 */
	@Override
	public StringAttribute makeStringAttribute(Term label, Representation representation, String value) {

		// Null value is allowed

		// no check on label or type since they might need to be set later on in the services
		StringAttribute attribute = new StringAttributeImpl(label, representation, value);

		// vocabulary.addAttribute(attribute);
		representation.addAttribute(attribute);
		return attribute;
	}

	@Override
	public DateTimeAttribute makeDateTimeAttribute(Term label, Representation representation, Calendar value) {

		// Null value is allowed

		// no check on label or type since they might need to be set later on in the services
		DateTimeAttribute attribute = new DateTimeAttributeImpl(label, representation, value);

		// vocabulary.addAttribute(attribute);
		representation.addAttribute(attribute);
		return attribute;
	}

	@Override
	public Term makeSynonym(Vocabulary vocabulary, Term term, String signifier) {

		Term synonym = new TermImpl(vocabulary, signifier, term.getObjectType(), isRepresentationInVocabulary(
				vocabulary, term));
		vocabulary.addTerm(synonym);
		term.getObjectType().addTerm(synonym);
		return synonym;
	}

	private boolean isRepresentationInVocabulary(Vocabulary vocabulary, Representation representation) {
		return !vocabulary.equals(representation.getVocabulary());
	}

	@Override
	public CharacteristicForm makeSynonymousForm(Vocabulary vocabulary, CharacteristicForm cForm, Term term, String role) {
		CharacteristicForm synonym = new CharacteristicFormImpl(vocabulary, term, role, cForm.getCharacteristic(),
				isRepresentationInVocabulary(vocabulary, cForm));
		vocabulary.addCharacteristicForm(synonym);
		cForm.getCharacteristic().addCharacteristicForm(synonym);
		return synonym;
	}

	@Override
	public BinaryFactTypeForm makeSynonymousForm(Vocabulary vocabulary, BinaryFactTypeForm bftForm, Term headTerm,
			String role, String coRole, Term tailTerm) {

		BinaryFactTypeForm synonym = new BinaryFactTypeFormImpl(vocabulary, headTerm, role, coRole, tailTerm,
				bftForm.getBinaryFactType(), isRepresentationInVocabulary(vocabulary, bftForm));
		vocabulary.addBinaryFactTypeForm(synonym);
		bftForm.getBinaryFactType().addBinaryFactTypeForm(synonym);
		return synonym;
	}

	@Override
	public Vocabulary makeVocabularyBulkTerms(Community community, String uri, String name, String... signifiers) {
		Vocabulary vocabulary = makeVocabulary(community, uri, name);
		for (String signifier : signifiers) {
			makeTerm(vocabulary, signifier);
		}
		return vocabulary;
	}

	@Override
	public Term makeTermOfType(Vocabulary vocabulary, String signifier, ObjectType objectType) {
		Term term = makeTerm(vocabulary, signifier);
		term.getObjectType().setType(objectType);
		return term;
	}
}
