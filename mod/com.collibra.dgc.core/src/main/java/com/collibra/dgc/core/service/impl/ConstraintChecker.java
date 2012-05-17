package com.collibra.dgc.core.service.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.dao.BinaryFactTypeFormDao;
import com.collibra.dgc.core.dao.CharacteristicFormDao;
import com.collibra.dgc.core.dao.CommunityDao;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.dao.TermDao;
import com.collibra.dgc.core.dao.VocabularyDao;
import com.collibra.dgc.core.exceptions.CircularTaxonomyException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.DGCException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.exceptions.InconsistentTaxonomyException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.relation.Relation;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.DateTimeAttribute;
import com.collibra.dgc.core.model.representation.MultiValueListAttribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.service.AttributeService;

/**
 * The {@link ConstraintChecker} is a helper class to be used before persisting domain objects to the database. It will
 * check whether they do not break any of our consistency checks.
 * 
 * @author dtrog
 * @author pmalarme
 * 
 */
@Service
public class ConstraintChecker {

	private static final Logger log = LoggerFactory.getLogger(ConstraintChecker.class);

	@Autowired
	private CommunityDao communityDao;

	@Autowired
	private VocabularyDao vocabularyDao;

	@Autowired
	private TermDao termDao;

	@Autowired
	private ObjectTypeDao objectTypeDao;

	@Autowired
	private CharacteristicFormDao characteristicFormDao;

	@Autowired
	private BinaryFactTypeFormDao binaryFactTypeFormDao;

	@Autowired
	private AttributeService attributeService;

	/* COMMUNITY */

	/**
	 * Check community constraints.
	 * 
	 * @see ConstraintChecker#checkCommunityWithNameAlreadyExists(String, Community)
	 * @see ConstraintChecker#checkCommunityWithUriAlreadyExists(String, Community)
	 * @param community The {@link Community} for which constraints have to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#COMMUNITY_WITH_NAME_ALREADY_EXISTS} or with
	 *             error code {@link DGCErrorCodes#COMMUNITY_WITH_URI_ALREADY_EXISTS}.
	 */
	public void checkConstraints(final Community community) {

		checkCommunityWithNameAlreadyExists(null, community);
		checkCommunityWithUriAlreadyExists(null, community);
	}

	/**
	 * Check that a community with same does not already exist.
	 * 
	 * @param name The name for which constraints have to be checked. If name is null or empty,
	 *            {@code communityReference}'s name is checked
	 * @param communityReference The reference {@link Community}
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#COMMUNITY_WITH_NAME_ALREADY_EXISTS}
	 */
	public void checkCommunityWithNameAlreadyExists(String name, final Community communityReference) {

		if (name == null || name.isEmpty())
			name = communityReference.getName();

		final Community existingCommunity = communityDao.findByName(name);

		if (existingCommunity != null && !existingCommunity.getId().equals(communityReference.getId())) {

			log.error("Community '" + name + "' already exists.");

			throw new IllegalArgumentException(DGCErrorCodes.COMMUNITY_WITH_NAME_ALREADY_EXISTS, name);
		}
	}

	/**
	 * Check that a community with same URI does not already exist.
	 * 
	 * @param uri The URI for which constraints have to be checked. If URI is null, {@code communityReference}'s URI is
	 *            checked
	 * @param communityReference The reference {@link Community}
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#COMMUNITY_WITH_URI_ALREADY_EXISTS}.
	 */
	public void checkCommunityWithUriAlreadyExists(String uri, final Community communityReference) {

		if (uri == null || uri.isEmpty())
			uri = communityReference.getUri();

		final Community existingCommunity = communityDao.findByURI(uri);

		if (existingCommunity != null && !existingCommunity.getId().equals(communityReference.getId())) {

			log.error("Community with URI '" + uri + "' already exists.");

			throw new IllegalArgumentException(DGCErrorCodes.COMMUNITY_WITH_URI_ALREADY_EXISTS, uri);
		}
	}

	/* VOCABULARY */

	/**
	 * Check vocabulary constraints.
	 * 
	 * @see ConstraintChecker#checkVocabularyWithNameAlreadyExistsConstraint(String, Vocabulary)
	 * @see ConstraintChecker#checkVocabularyWihtURIAlreadyExistsConstraint(String, Vocabulary)
	 * @see ConstraintChecker#checkVocabularyTypeConstraint(Vocabulary)
	 * @param vocabulary The {@link Vocabulary} for which constraints have to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#VOCABULARY_WITH_NAME_ALREADY_EXISTS} or
	 *             with error code {@link DGCErrorCodes#VOCABULARY_WITH_URI_ALREADY_EXISTS} or with error code
	 *             {@link DGCErrorCodes#VOCABULARY_TYPE_INCONSISTENT}
	 */
	public void checkConstraints(final Vocabulary vocabulary) {

		checkVocabularyWithNameAlreadyExistsConstraint(null, vocabulary);
		checkVocabularyWihtURIAlreadyExistsConstraint(null, vocabulary);
		checkVocabularyTypeConstraint(vocabulary);

	}

	/**
	 * Check that a vocabulary with same name does not exist for the same community ({@code vocabularyReference}'s
	 * community}.
	 * 
	 * @param name The name for which constraints have to be checked. If name is null or empty,
	 *            {@code vocabularyReference}'s name is checked
	 * @param vocabularyReference The reference {@link Vocabulary}
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#VOCABULARY_WITH_NAME_ALREADY_EXISTS}
	 */
	public void checkVocabularyWithNameAlreadyExistsConstraint(String name, final Vocabulary vocabularyReference) {

		if (name == null || name.isEmpty())
			name = vocabularyReference.getName();

		final Vocabulary existingVocabulary = vocabularyDao.findByVocabularyName(vocabularyReference.getCommunity(),
				name);

		if (existingVocabulary != null && !existingVocabulary.getId().equals(vocabularyReference.getId())) {

			log.error("Vocabulary with name '" + name + "' already exists for community '"
					+ vocabularyReference.getCommunity().getName() + "' (" + vocabularyReference.getCommunity().getId()
					+ ").");

			// TODO Pierre: update to add the community parameter
			throw new IllegalArgumentException(DGCErrorCodes.VOCABULARY_WITH_NAME_ALREADY_EXISTS, name);
		}
	}

	/**
	 * Check that a vocabulary with same URI does not exist.
	 * 
	 * @param uri The URI for which constraints have to be checked. If it is null, {@code vocabularyReference's} URI is
	 *            checked
	 * @param vocabularyReference The reference {@link Vocabulary}
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#VOCABULARY_WITH_URI_ALREADY_EXISTS}
	 */
	public void checkVocabularyWihtURIAlreadyExistsConstraint(String uri, final Vocabulary vocabularyReference) {

		if (uri == null || uri.isEmpty())
			uri = vocabularyReference.getUri();

		final Vocabulary existingVocabulary = vocabularyDao.findByVocabularyUri(uri);

		if (existingVocabulary != null && !existingVocabulary.getId().equals(vocabularyReference.getId())) {

			log.error("Vocabulary with URI " + uri + " already exists");

			throw new IllegalArgumentException(DGCErrorCodes.VOCABULARY_WITH_URI_ALREADY_EXISTS, uri);
		}
	}

	/**
	 * Check the type of vocabulary and if it corresponds to an allowed one.
	 * 
	 * @param vocabulary The {@link Vocabulary} for which the type has to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#VOCABULARY_TYPE_INCONSISTENT}
	 */
	public void checkVocabularyTypeConstraint(final Vocabulary vocabulary) {

		// TODO Felix: This can be done faster by checking on the children of Vocabulary through a specific HQL query
		HashSet<ObjectType> allowedVocTypes = new HashSet<ObjectType>();
		allowedVocTypes.add(objectTypeDao.getMetaBusinessVocabularyType());
		allowedVocTypes.add(objectTypeDao.getMetaGlossaryType());
		allowedVocTypes.add(objectTypeDao.getMetaVocabularyType());

		final ObjectType vocType = vocabulary.getType();

		if (!checkVocabularyTypeInhertiance(vocType, allowedVocTypes, objectTypeDao.getMetaThing())) {

			String message;

			if (vocType == null || vocType.getTerms().isEmpty())
				message = "Vocabulary type does not belong to the vocabulary taxonomy";
			else
				message = "Vocabulary type " + vocType.getTerms().iterator().next().verbalise()
						+ " does not belong to the vocabulary taxonomy";

			log.error(message);

			throw new IllegalArgumentException(DGCErrorCodes.VOCABULARY_TYPE_INCONSISTENT, vocType.verbalise(),
					vocType.getId(), vocabulary.getName(), vocabulary.getId());
		}

	}

	/**
	 * 
	 * @param vocType
	 * @param allowedTypes
	 * @param stop
	 * @return
	 */
	// TODO Pierre: add description
	private boolean checkVocabularyTypeInhertiance(final Concept vocType, final Set<ObjectType> allowedTypes,
			final Concept stop) {

		if (vocType == null || vocType.equals(stop))
			return false;

		if (allowedTypes.contains(vocType))
			return true;
		else
			return checkVocabularyTypeInhertiance(vocType.getGeneralConcept(), allowedTypes, stop);
	}

	/**
	 * Check disincorporate vocabulary constraints:
	 * <ul>
	 * <li>The incorporated vocabulary is incorporated in the incorporating vocabulary.</li>
	 * <li>The incorporated vocabulary is not SBVR vocabulary.</li>
	 * </ul>
	 * 
	 * @param incorporatingVocabulary The incorporating {@link Vocabulary}
	 * @param incorporatedVocabulary The incorporated {@link Vocabulary}
	 * @Throws {@link IllegalArgumentException} with error codes
	 */
	public void checkDisincorporateVocabularyConstraints(Vocabulary incorporatingVocabulary,
			Vocabulary incorporatedVocabulary) {

		if (!incorporatingVocabulary.getIncorporatedVocabularies().contains(incorporatedVocabulary)) {

			log.error("Vocabulary '" + incorporatedVocabulary + "' is not incorporated in vocabulary '"
					+ incorporatingVocabulary + "'.");

			// TODO Pierre: Add the right DGCErrorCode
			throw new IllegalArgumentException(DGCErrorCodes.ARGUMENT_INVALID);
		}

		if (incorporatedVocabulary.getUri().equals(Constants.SBVR_VOC)) {

			log.error("SBVR Vocabulary cannot be disincorporated from vocabulary '" + incorporatingVocabulary + "'.");

			// TODO Pierre: Add the right DGCErrorCode
			throw new IllegalArgumentException(DGCErrorCodes.ARGUMENT_INVALID);
		}
	}

	/**
	 * Check incorporate vocabulary constraints:
	 * <ul>
	 * <li>Both vocabularies are identical (same resource id).</li>
	 * <li>The incorporated vocabulary incorporate the incorporating vocabulary.</li>
	 * <li>The incorporated vocabulary is already incorporated in the incorporating vocabulary.</li>
	 * </ul>
	 * 
	 * @param incorporatingVocabulary The incorporating {@link Vocabulary}
	 * @param incorporatedVocabulary The incorporated {@link Vocabulary}
	 * @Throws {@link IllegalArgumentException} with error codes
	 */
	public void checkIncorporateVocabularyConstraints(Vocabulary incorporatingVocabulary,
			Vocabulary incorporatedVocabulary) {

		if (incorporatingVocabulary.getId().equals(incorporatedVocabulary.getId())) {

			log.error("Vocabulary '" + incorporatedVocabulary + "' cannot incorporate itself.");

			// TODO Pierre: Add the right DGCErrorCode
			throw new IllegalArgumentException(DGCErrorCodes.ARGUMENT_INVALID);
		}

		if (incorporatingVocabulary.getIncorporatedVocabularies().contains(incorporatedVocabulary)) {

			log.error("Vocabulary '" + incorporatedVocabulary + "' is already incorporated in vocabulary '"
					+ incorporatingVocabulary + "'.");

			// TODO Pierre: Add the right DGCErrorCode
			throw new IllegalArgumentException(DGCErrorCodes.ARGUMENT_INVALID);
		}

		if (incorporatedVocabulary.getIncorporatedVocabularies().contains(incorporatingVocabulary)) {

			log.error("Vocabulary '" + incorporatingVocabulary + "' is incorporated in vocabulary '"
					+ incorporatingVocabulary + "'. Therefore, vocabulary '" + incorporatingVocabulary
					+ "' cannot incorporate vocabulary '" + incorporatedVocabulary + "'.");

			// TODO Pierre: Add the right DGCErrorCode
			throw new IllegalArgumentException(DGCErrorCodes.ARGUMENT_INVALID);
		}
	}

	/* MEANING */

	/**
	 * Check constraints for a concept.
	 * 
	 * @param concept The {@link Concept}
	 */
	public void checkConstraints(final Concept concept) {

		// Create a Stack that contains the generalisation path from the concept to its parent, the parent of its
		// parent,
		// ... until the general concept is null.
		// The linked list is chosen because it acts like a stack but also the access to the first element is easy
		// and needed for exception throwing.
		final LinkedList<Concept> generalizationPath = new LinkedList<Concept>();

		// Add the concept into the path
		generalizationPath.add(concept);

		// loop until the general concept is null to check that the taxonomy is not circular
		while (checkGeneralConceptConstraints(generalizationPath))
			/* Do nothing */;
	}

	/**
	 * Check the general concept of the concept and its corresponding constraints.
	 * 
	 * @param concept The {@link Concept}
	 * @return {@code false} is the general concept is null, {@code false} otherwise
	 */
	private boolean checkGeneralConceptConstraints(LinkedList<Concept> generalizationPath) {

		// Get the concept and deproxy it to check of which class it is an instance of
		final Concept concept = ServiceUtility.deproxy(generalizationPath.peekLast(), Concept.class);

		// Check if a specific implementation is needed
		if (concept instanceof BinaryFactType)
			return checkGeneralConceptConstraints((BinaryFactType) concept, generalizationPath);

		else if (concept instanceof Characteristic)
			return checkGeneralConceptConstraints((Characteristic) concept, generalizationPath);

		// Check other concept => only circular taxonomy
		final Concept generalConcept = concept.getGeneralConcept();

		if (generalConcept == null)
			return false;

		checkCircularTaxonomyConstraint(generalConcept, generalizationPath);

		return true;
	}

	/**
	 * Check the general concept of a characteristic and its general constraints.
	 * 
	 * @param characteristic The {@link Characteristic}
	 * @param generalizationPath {@code false} if the general concept is null, {@code true} otherwise.
	 * @return
	 */
	private boolean checkGeneralConceptConstraints(final Characteristic characteristic,
			LinkedList<Concept> generalizationPath) {

		final Concept generalConcept = ServiceUtility.deproxy(characteristic.getGeneralConcept(), Concept.class);

		// If the general concept is null it is set to thing => it's ok
		if (generalConcept == null)
			return false;

		// If the general concept is an object type, there is nothing to check and it can be returned
		if (generalConcept instanceof ObjectType) {

			checkCircularTaxonomyConstraint(generalConcept, generalizationPath);

			return true;
		}

		// TODO Pierre: what to do when a concept is not referred by any representation?
		// TODO Pierre: do we need to remove the concept? Keep it? Stop the loop ?
		// If the concept is not referred by any representation
		if (characteristic.getCharacteristicForms().isEmpty()) {

			checkCircularTaxonomyConstraint(generalConcept, generalizationPath);

			return true;
		}

		// If the general concept is not null, not an object type, is referred by representation and not a
		// characteristic thus throw an illegal argument exception
		if (!(generalConcept instanceof Characteristic)) {

			log.error("The taxonomy is in an illegal state, expected a characteristic for general concept: "
					+ generalConcept);

			// TODO Pierre: throw an IllegalArgumentException
			throw new IllegalStateException(generalConcept.verbalise());
		}

		// Check that taxonomy is not circular
		checkCircularTaxonomyConstraint(generalConcept, generalizationPath);

		// Get the term concept
		final Concept termConcept = characteristic.getCharacteristicForms().iterator().next().getTerm().getObjectType();

		// Check if the general concept contains characteristic forms, if it not the case, there is nothing to compare
		// TODO Pierre: throw an exception, do nothing ?
		Characteristic generalCharacteristic = (Characteristic) generalConcept;

		if (generalCharacteristic.getCharacteristicForms().isEmpty())
			return true;

		final Concept generalTermConcept = generalCharacteristic.getCharacteristicForms().iterator().next().getTerm()
				.getObjectType();

		// Check taxonomical relation for the term of the characteristic
		if (checkTaxonomicalRelation(generalTermConcept, termConcept))
			return true;

		// Otherwise throw an IllegalArgument exception: inconsistent taxonomy.
		log.error("'" + characteristic + "' inconsistent taxonomy");

		// TODO Pierre: throw an illegal argument exception
		throw new InconsistentTaxonomyException(characteristic.getId(), generalCharacteristic.getClass()
				.getSimpleName(), DGCErrorCodes.INCONSISTENT_TAXONOMY);
	}

	/**
	 * Check the general concept of a binary fact type and its general constraints.
	 * 
	 * @param binaryFactType The {@link BinaryFactType}
	 * @return {@code false} if the general concept is null, {@code true} otherwise
	 */
	private boolean checkGeneralConceptConstraints(final BinaryFactType binaryFactType,
			LinkedList<Concept> generalizationPath) {

		final Concept generalConcept = ServiceUtility.deproxy(binaryFactType.getGeneralConcept(), Concept.class);

		// If the general concept is null it is set to thing => it's ok
		if (generalConcept == null)
			return false;

		// If the general concept is an object type, there is nothing to check and it can be returned
		if (generalConcept instanceof ObjectType) {

			checkCircularTaxonomyConstraint(generalConcept, generalizationPath);

			return true;
		}

		// TODO Pierre: what to do when a concept is not referred by any representation?
		// TODO Pierre: do we need to remove the concept? Keep it? Stop the loop ?
		// If the concept is not referred by any representation
		if (binaryFactType.getBinaryFactTypeForms().isEmpty()) {

			checkCircularTaxonomyConstraint(generalConcept, generalizationPath);

			return true;
		}

		// If the general concept is not null, not an object type, is referred by representation and not a binary fact
		// type thus throw an illegal argument exception
		if (!(generalConcept instanceof BinaryFactType)) {

			log.error("The taxonomy is in an illegal state, expected a binary fact type for general concept: "
					+ generalConcept + ".");

			// TODO Pierre: throw an IllegalArgumentException
			throw new IllegalStateException(binaryFactType.getGeneralConcept().verbalise());
		}

		// Check that taxonomy is not circular
		checkCircularTaxonomyConstraint(generalConcept, generalizationPath);

		// Get the head and tail terms' concept
		final Concept headTermConcept = binaryFactType.getHeadFactTypeRole().getObjectType();
		final Concept tailTermConcept = binaryFactType.getTailFactTypeRole().getObjectType();

		// Get general concept head and tail terms' concepts
		final BinaryFactType generalBinaryFactType = (BinaryFactType) generalConcept;
		final Concept generalHeadTermConcept = generalBinaryFactType.getHeadFactTypeRole().getObjectType();
		final Concept generalTailTermConcept = generalBinaryFactType.getTailFactTypeRole().getObjectType();

		// If the taxonomical relation are checked for both head and tail terms in the same sense than the original BFT
		// (head -> tail) or the inverse sense (tail -> head), the constraints are respected
		if ((checkTaxonomicalRelation(generalHeadTermConcept, headTermConcept) && checkTaxonomicalRelation(
				generalTailTermConcept, tailTermConcept))
				|| (checkTaxonomicalRelation(generalHeadTermConcept, tailTermConcept) && checkTaxonomicalRelation(
						generalTailTermConcept, headTermConcept)))
			return true;

		// Otherwise throw an IllegalArgument exception: inconsistent taxonomy.
		log.error("'" + binaryFactType + "' inconsistent taxonomy");

		// TODO Pierre: throw an illegal argument exception
		throw new InconsistentTaxonomyException(binaryFactType.getId(), generalBinaryFactType.getClass()
				.getSimpleName(), DGCErrorCodes.INCONSISTENT_TAXONOMY);
	}

	/**
	 * Check if the taxonomy is circular or not. If the taxonomy is not circular, the concept is added to the
	 * generalization path.
	 * 
	 * @param generalConcept The {@code General} {@link Concept}
	 * @param generalizationPath The generalization path
	 */
	private void checkCircularTaxonomyConstraint(final Concept generalConcept, LinkedList<Concept> generalizationPath) {

		if (generalizationPath.contains(generalConcept)) {

			log.error("Circular taxonomy encountered: " + generalConcept.verbalise() + " already exists in "
					+ generalizationPath);

			// Original Concept for which we check constraints
			Concept originalConcept = generalizationPath.peekFirst();

			// TODO Pierre: update to IllegalArgumentException cause it is a constraint violation
			throw new CircularTaxonomyException(originalConcept.getId(), originalConcept.getClass().getSimpleName());
		}

		// Add the general concept to the path
		generalizationPath.add(generalConcept);

	}

	/**
	 * Check that the sub concept is one of the specialized concepts of the super concept or the super concept itself.
	 * 
	 * @param superConcept The super {@link Concept}
	 * @param subConcept The sub {@link Concept}
	 * @return {@code True} if there is a specialization relation or if both concept are equal, {@code false} otherwise
	 */
	private boolean checkTaxonomicalRelation(final Concept superConcept, final Concept subConcept) {

		return superConcept.equals(subConcept) || ServiceUtility.hasSuperConcept(subConcept, superConcept);
	}

	/* REPRESENTATION */

	public void checkSynonymConstraints(Concept original, Concept synonym) {

		// TODO Pierre: implement synonym constraint

	}

	/**
	 * Checks constraint and throws {@link DGCException} with error code {@link DGCErrorCodes#REPRESENTATION_LOCKED} if
	 * the {@link Representation} is locked.
	 * @param representation The {@link Representation}.
	 */
	public static void checkLockConstraint(Representation representation) {

		if (representation.isLocked()) {

			log.error("Representation '" + representation.verbalise() + "' is locked");

			throw new IllegalArgumentException(DGCErrorCodes.REPRESENTATION_LOCKED, representation.verbalise(),
					representation.getId());
		}
	}

	/**
	 * Check that characteristic form does not already exist in the same vocabulary for same term and role.
	 * 
	 * @param characteristicForm The {@link CharacteristicForm} for which constraints have to be checked.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#CF_ALREADY_EXISTS}
	 */
	public void checkConstraints(final CharacteristicForm characteristicForm) {

		final CharacteristicForm existingCF = characteristicFormDao.find(characteristicForm.getVocabulary(),
				characteristicForm.getTerm(), characteristicForm.getRole());

		if (existingCF != null && !existingCF.getId().equals(characteristicForm.getId())) {

			log.error("Characteristic form '" + characteristicForm + "' already exists.");

			throw new IllegalArgumentException(DGCErrorCodes.CF_ALREADY_EXISTS, characteristicForm.getTerm()
					.getSignifier(), characteristicForm.getTerm().getId(), characteristicForm.getRole());
		}

	}

	/**
	 * Check that binary fact type form does not already exist in the same vocabulary for same role, coRole, head and
	 * tail terms.
	 * 
	 * @param binaryFactTypeForm The {@link BinaryFactTypeForm} for which constraints have to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#BFTF_ALREADY_EXISTS}
	 */
	public void checkConstraints(final BinaryFactTypeForm binaryFactTypeForm) {

		final BinaryFactTypeForm existingBFTF = binaryFactTypeFormDao.find(binaryFactTypeForm.getVocabulary(),
				binaryFactTypeForm.getHeadTerm(), binaryFactTypeForm.getRole(), binaryFactTypeForm.getCoRole(),
				binaryFactTypeForm.getTailTerm());

		if (existingBFTF != null && !existingBFTF.getId().equals(binaryFactTypeForm.getId())) {

			log.error("Binary fact type form '" + existingBFTF + "' already exists");

			throw new IllegalArgumentException(DGCErrorCodes.BFTF_ALREADY_EXISTS, binaryFactTypeForm.getHeadTerm()
					.getSignifier(), binaryFactTypeForm.getHeadTerm().getId(), binaryFactTypeForm.getRole(),
					binaryFactTypeForm.getCoRole(), binaryFactTypeForm.getTailTerm().getSignifier(), binaryFactTypeForm
							.getTailTerm().getId());
		}

	}

	/**
	 * Check term constraints: it does not already exist.
	 * 
	 * @see ConstraintChecker#checkTermAlreadyExistConstraint(String, Term, String)
	 * @param term The {@link Term} for which constraints have to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#TERM_ALREADY_EXISTS}
	 */
	public void checkConstraints(final Term term) {

		checkTermAlreadyExistConstraint(null, term, null);
	}

	/**
	 * Check that a term does not already exist in the vocabulary with the same signifier.
	 * 
	 * @param vocabulary The {@link Vocabulary} for which the constraint has to be checked
	 * @param signifier The signifier for which constraints have to be checked
	 * @param errorCode The {@link DGCErrorCode}. If the error code is null or empty,
	 *            {@link DGCErrorCodes#TERM_ALREADY_EXISTS} is used.
	 * @throws IllegalArgumentException
	 * 
	 * <br />
	 * <br />
	 *             Note: i18n parameters of your error code have be identical to the parameters of
	 *             {@link DGCErrorCodes#TERM_ALREADY_EXISTS}.
	 */
	public void checkTermAlreadyExistConstraint(final Vocabulary vocabulary, final String signifier, String errorCode) {

		if (errorCode == null || errorCode.isEmpty())
			errorCode = DGCErrorCodes.TERM_ALREADY_EXISTS;

		final Term existingTerm = termDao.findTermBySignifier(vocabulary, signifier);

		if (existingTerm != null) {

			log.error("Term'" + existingTerm + "' already exists.");

			throw new IllegalArgumentException(errorCode, signifier, vocabulary.getName(), vocabulary.getId());
		}
	}

	/**
	 * Check that a term does not already exist in the reference term vocabulary with the same signifier and a different
	 * resource id than the reference term.
	 * 
	 * @param signifier The signifier for which constraints have to be checked. If it is null or empty, the signifier of
	 *            the reference term is used
	 * @param referenceTerm The reference term
	 * @param errorCode The {@link DGCErrorCode}. If the error code is null or empty,
	 *            {@link DGCErrorCodes#TERM_ALREADY_EXISTS} is used.
	 * @throws IllegalArgumentException
	 * 
	 * <br />
	 * <br />
	 *             Note: i18n parameters of your error code have be identical to the parameters of
	 *             {@link DGCErrorCodes#TERM_ALREADY_EXISTS}.
	 */
	public void checkTermAlreadyExistConstraint(String signifier, final Term referenceTerm, String errorCode) {

		if (errorCode == null || errorCode.isEmpty())
			errorCode = DGCErrorCodes.TERM_ALREADY_EXISTS;

		// If it is the signifier of reference term that must be checked
		if (signifier == null || signifier.isEmpty())
			signifier = referenceTerm.getSignifier();

		final Vocabulary vocabulary = referenceTerm.getVocabulary();

		final Term existingTerm = termDao.findTermBySignifier(vocabulary, signifier);

		if (existingTerm != null && !existingTerm.getId().equals(referenceTerm.getId())) {

			log.error("Term with same signfier (" + signifier + ") in vocabulary " + vocabulary.getName()
					+ " and different resourceId already exists.");

			throw new IllegalArgumentException(errorCode, signifier, vocabulary.getName(), vocabulary.getId());
		}
	}

	/* STATUS */

	/**
	 * Check constraints for a status.
	 * 
	 * @param labelTerm Status label {@link Term} for which constraints has to be checked.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#STATUS_NOT_STATUS_LABEL_FOR_RID}
	 */
	// TODO Pierre: add the right error codes
	public void checkStatusConstraints(final Term labelTerm) {

		checkIsStatusLabelTermConstraint(labelTerm);
		checkStatusObjectTypeConstraint(labelTerm);
		checkStatusAlreadyExistsConstraint(null, labelTerm);
	}

	/**
	 * Check if the term corresponds to a status label term.
	 * 
	 * @param labelTerm The label {@link Term} to check
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#STATUS_NOT_STATUS_LABEL_FOR_RID}
	 */
	public void checkIsStatusLabelTermConstraint(final Term labelTerm) {

		if (!labelTerm.getVocabulary().getUri().equals(Constants.STATUSES_VOCABULARY_URI)) {

			log.error("Term " + labelTerm + " is not a status label term.");

			throw new IllegalArgumentException(DGCErrorCodes.STATUS_NOT_STATUS_LABEL_FOR_RID, labelTerm.getSignifier(),
					"rId", labelTerm.getId(), labelTerm.getVocabulary().getUri(), Constants.STATUSES_VOCABULARY_NAME,
					Constants.STATUSES_VOCABULARY_URI);
		}
	}

	/**
	 * Check that the concept type of the status label term correspond to the meta status concept type.
	 * 
	 * @param labelTerm The status label {@link Term} for which the constraint has to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#ARGUMENT_INVALID}
	 */
	public void checkStatusObjectTypeConstraint(final Term labelTerm) {

		if (labelTerm.getMeaning().getType() == null
				|| !labelTerm.getMeaning().getType().getId().equals(MeaningConstants.META_STATUS_TYPE_UUID)) {

			log.error("Concept type of the label term '" + labelTerm.getMeaning().getType()
					+ "' does not correspond to meta status concept type");

			// TODO Pierre: add an error code corresponding to this case
			throw new IllegalArgumentException(DGCErrorCodes.ARGUMENT_INVALID);
		}
	}

	/**
	 * Check that a status label term does not already exist with same signifier.
	 * 
	 * @param signifier The signifier for which constraints have to be checked. If it is null or empty, the signifier of
	 *            the reference term is used
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#TERM_ALREADY_EXISTS}
	 */
	public void checkStatusAlreadyExistsConstraint(String signifier) {

		final Vocabulary vocabulary = vocabularyDao.findStatusesVocabulary();

		final Term existingTerm = termDao.findTermBySignifier(vocabulary, signifier);

		if (existingTerm != null) {

			log.error("Status label term with signifier '" + signifier + "' already exists.");

			// TODO Pierre: add an status type specific error.
			throw new IllegalArgumentException(DGCErrorCodes.TERM_ALREADY_EXISTS, signifier, vocabulary.getName(),
					vocabulary.getId());
		}
	}

	/**
	 * Check that a status label term does not already exist with same signifier and different resource id.
	 * 
	 * @param signifier The signifier for which constraints have to be checked. If it is null or empty, the signifier of
	 *            the reference term is used
	 * @param labelTerm The label {@link Term}
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#TERM_ALREADY_EXISTS}
	 */
	public void checkStatusAlreadyExistsConstraint(String signifier, final Term labelTerm) {

		if (signifier == null || signifier.isEmpty())
			signifier = labelTerm.getSignifier();

		final Vocabulary vocabulary = vocabularyDao.findStatusesVocabulary();

		final Term existingTerm = termDao.findTermBySignifier(vocabulary, signifier);

		if (existingTerm != null && !existingTerm.getId().equals(labelTerm.getId())) {

			log.error("Status label term with signifier '" + signifier + "' already exists.");

			// TODO Pierre: add an status type specific error.
			throw new IllegalArgumentException(DGCErrorCodes.TERM_ALREADY_EXISTS, signifier, labelTerm.getVocabulary()
					.getName(), labelTerm.getVocabulary().getId());
		}
	}

	/* RELATION */

	/**
	 * Check relation constraints (i.e. the generalization paths of source and target).
	 * 
	 * @param relation The {@link Relation} to be checked.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#RELATION_SRC_TGT_INCOMPATIBLE}
	 */
	public void checkConstraints(final Relation relation) {

		// TODO add the cardinality check of the relation

		final Term source = relation.getSource();
		final Term target = relation.getTarget();
		final BinaryFactTypeForm type = relation.getType();

		// TODO Pierre: add message and specify if it is the source or the target
		if (!checkTaxonomicalRelation(type.getHeadTerm().getObjectType(), source.getObjectType().getType()))
			throw new IllegalArgumentException(DGCErrorCodes.RELATION_SRC_TGT_INCOMPATIBLE, source.verbalise(), type
					.getHeadTerm().verbalise(), source.getObjectType().getType().getTerms().iterator().next());

		if (!checkTaxonomicalRelation(type.getTailTerm().getObjectType(), target.getObjectType().getType()))
			throw new IllegalArgumentException(DGCErrorCodes.RELATION_SRC_TGT_INCOMPATIBLE, target.verbalise(), type
					.getTailTerm().verbalise(), target.getObjectType().getType().getTerms().iterator().next());
	}

	/* ATTRIBUTE TYPE */

	/**
	 * Check attribute type constraints.
	 * 
	 * @param labelTerm Attribute type label {@link Term} for which constraints have to be checked.
	 * @throws IllegalArgumentException with error code
	 *             {@link DGCErrorCodes#ATTRIBUTE_TYPE_NOT_ATTRIBUTE_TYPE_LABEL_FOR_RESOURCE_ID}
	 */
	// TODO Pierre: add the other error codes
	public void checkAttributeTypeConstraints(final Term labelTerm) {

		checkIsAttributeTypeLabelTermConstraint(labelTerm);
		checkAttributeTypeConceptTypeGeneralConcept(labelTerm);
		checkAttributeTypeLabelTermAlreadyExistsConstraint(null, labelTerm);
	}

	/**
	 * Check if the term correspond to a attribute type label term.
	 * 
	 * @param labelTerm The label {@link Term} to check
	 * @throws IllegalArgumentException with error code
	 *             {@link DGCErrorCodes#ATTRIBUTE_TYPE_NOT_ATTRIBUTE_TYPE_LABEL_FOR_RESOURCE_ID}
	 */
	public void checkIsAttributeTypeLabelTermConstraint(final Term labelTerm) {

		final String labelVocabularyUri = labelTerm.getVocabulary().getUri();

		if (!labelVocabularyUri.equals(Constants.ATTRIBUTETYPES_VOCABULARY_URI)
				&& !labelVocabularyUri.equals(Constants.BUSINESS_VOC)
				&& !labelVocabularyUri.equals(Constants.MEANING_AND_REPRESENTATION_VOC)) {

			log.error("Term " + labelTerm + " is not a attribute type label term.");

			// TODO Pierre: remove the argument name
			throw new IllegalArgumentException(DGCErrorCodes.ATTRIBUTE_TYPE_NOT_ATTRIBUTE_TYPE_LABEL_FOR_RESOURCE_ID,
					labelTerm.getSignifier(), "rId", labelTerm.getId(), labelVocabularyUri,
					Constants.ATTRIBUTETYPES_VOCABULARY_NAME, Constants.ATTRIBUTETYPES_VOCABULARY_URI, attributeService
							.findMetaDefinition().getSignifier(),
					attributeService.findMetaDescription().getSignifier(), attributeService.findMetaExample()
							.getSignifier(), attributeService.findMetaNote().getSignifier());
		}
	}

	/**
	 * Check that the general concept of label term's concept type correspond to the concept attribute type
	 * 
	 * @param labelTerm The label {@link Term} for which constraints have to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#ARGUMENT_INVALID}
	 */
	public void checkAttributeTypeConceptTypeGeneralConcept(final Term labelTerm) {

		if (!ServiceUtility.hasSuperConcept(labelTerm.getObjectType().getType(),
				objectTypeDao.findById(MeaningConstants.META_ATTRIBUTE_TYPE_UUID))) {

			log.error("Term '" + labelTerm + "' is not an attribute type label term.");

			// TODO Pierre: add an error code corresponding to this case
			throw new IllegalArgumentException(DGCErrorCodes.ARGUMENT_INVALID);
		}
	}

	/**
	 * Check that a term does not already exist in the attribute type vocabulary with the same signifier and a different
	 * resource id than the reference term or than the signifier does not correspond to the signifier of a meta
	 * attribute type with a different resource id.
	 * 
	 * @param signifier The signifier for which constraints have to be checked. If it is null or empty, the signifier of
	 *            the reference term is used
	 * @param labelTerm The label {@link Term}
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#TERM_ALREADY_EXISTS}
	 */
	public void checkAttributeTypeLabelTermAlreadyExistsConstraint(String signifier, final Term labelTerm) {

		if (signifier == null || signifier.isEmpty())
			signifier = labelTerm.getSignifier();

		// Check meta attribute type
		checkMetaAttributeTypeLabelTermAlreadyExistsConstraint(signifier, labelTerm,
				attributeService.findMetaDefinition());
		checkMetaAttributeTypeLabelTermAlreadyExistsConstraint(signifier, labelTerm,
				attributeService.findMetaDescription());
		checkMetaAttributeTypeLabelTermAlreadyExistsConstraint(signifier, labelTerm, attributeService.findMetaExample());
		checkMetaAttributeTypeLabelTermAlreadyExistsConstraint(signifier, labelTerm, attributeService.findMetaNote());

		// Check attribute type label term from attribute type vocabulary
		final Vocabulary vocabulary = vocabularyDao.findAttributeTypesVocabulary();

		final Term existingTerm = termDao.findTermBySignifier(vocabulary, signifier);

		if (existingTerm != null && !existingTerm.getId().equals(labelTerm.getId())) {

			log.error("Attribute type label term with signifier '" + signifier + "' already exists.");

			// TODO Pierre: add an attribute type specific error.
			throw new IllegalArgumentException(DGCErrorCodes.TERM_ALREADY_EXISTS, signifier, labelTerm.getVocabulary()
					.getName(), labelTerm.getVocabulary().getId());
		}
	}

	/**
	 * Check that a meta attribute type label term with same signifier does not already exist with a different resource
	 * id.
	 * 
	 * @param signifier The signifier to check
	 * @param labelTerm The label {@link Term}
	 * @param metaAttributeTypeLabelTerm The meta attribute type label {@link Term}
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#TERM_ALREADY_EXISTS}
	 */
	private void checkMetaAttributeTypeLabelTermAlreadyExistsConstraint(final String signifier, final Term labelTerm,
			final Term metaAttributeTypeLabelTerm) {

		if (signifier.equals(metaAttributeTypeLabelTerm.getSignifier())
				&& !labelTerm.getId().equals(metaAttributeTypeLabelTerm.getId())) {

			log.error("Meta attribute type label term with signifier '" + signifier + "' already exists.");

			// TODO Pierre: add an attribute type specific error.
			throw new IllegalArgumentException(DGCErrorCodes.TERM_ALREADY_EXISTS, signifier, labelTerm.getVocabulary()
					.getName(), labelTerm.getVocabulary().getId());
		}
	}

	/* ATTRIBUTE */

	/**
	 * Check attribute constraints.
	 * 
	 * @param attribute The {@link Attribute} for which constraints have to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#STRING_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS}
	 *             or with error code {@link DGCErrorCodes#SVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS} or with error code
	 *             {@link DGCErrorCodes#MVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS} or with error code
	 *             {@link DGCErrorCodes#DT_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS} regarding the kind of attribute
	 */
	public void checkConstraints(final Attribute attribute) {

		Attribute deproxyAttribute = ServiceUtility.deproxy(attribute, Attribute.class);

		if (deproxyAttribute instanceof StringAttribute)
			checkConstraints((StringAttribute) deproxyAttribute);

		else if (deproxyAttribute instanceof SingleValueListAttribute)
			checkConstraints((SingleValueListAttribute) deproxyAttribute);

		else if (deproxyAttribute instanceof MultiValueListAttribute)
			checkConstraints((MultiValueListAttribute) deproxyAttribute);

		else if (deproxyAttribute instanceof DateTimeAttribute)
			checkConstraints((DateTimeAttribute) deproxyAttribute);

		else {

			log.error("The kind of attribute is unknown");

			// TODO Pierre: set the right DGCErrorCodes
			throw new IllegalArgumentException(DGCErrorCodes.ARGUMENT_INVALID);
		}
	}

	/**
	 * Check that the concept type of the label of an attribute correspond to the kind of attribute (e.g.
	 * StringAttribute).
	 * 
	 * @param label Label {@link Term} to be checked. If the label is null, the label of the {@link Attribute} is
	 *            checked
	 * @param attribute The {@link Attribute} for which its label has to be checked
	 * @param labelConceptTypeRId The reference label concept type resource id (e.g.
	 *            {@link MeaningConstants#META_STRING_TYPE_UUID})
	 * @param errorCode {@link DGCErrorCodes} for the {@link IllegalArgumentException}. Parameters of the error code
	 *            have to be label signifier, attribute resource id, reference concept type preferred term signifier and
	 *            its resource id
	 * @throws IllegalArgumentException with error code {@code errorCode}
	 */
	public void checkAttributeLabelConceptTypeConstraint(Term label, final Attribute attribute,
			final String labelConceptTypeRId, final String errorCode) {

		if (label == null)
			label = attribute.getLabel();

		ObjectType attributeTypeConceptType = label.getObjectType().getType();

		final Term labelConceptTypePreferredTerm = objectTypeDao.findById(labelConceptTypeRId).findPreferredTerm();

		if (attributeTypeConceptType == null || !attributeTypeConceptType.getId().equals(labelConceptTypeRId)) {

			log.error("Label term '" + label + "' of attribute '" + attribute + "' is not a correct attribute type ("
					+ labelConceptTypeRId + ")");

			// TODO Pierre: add the right DGCErrorCodes
			throw new IllegalArgumentException(errorCode, label.getSignifier(), attribute.getId(),
					labelConceptTypePreferredTerm.getSignifier(), labelConceptTypeRId);
		}
	}

	/**
	 * Check string attribute constraints.
	 * 
	 * @param stringAttribute The {@link StringAttribute} for which constraints have to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#STRING_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS}
	 */
	public void checkConstraints(final StringAttribute stringAttribute) {

		ConstraintChecker.checkLockConstraint(stringAttribute.getOwner());
		checkAttributeAlreadyExistsConstraint(stringAttribute, null, null, stringAttribute.getLongExpression());
		// TODO Pierre: add the right DGCErrorCodes
		checkAttributeLabelConceptTypeConstraint(null, stringAttribute, MeaningConstants.META_STRING_TYPE_UUID,
				DGCErrorCodes.ARGUMENT_INVALID);
	}

	/**
	 * Check that a string attribute with same content does not already exist.
	 * 
	 * @param stringAttribute The {@link StringAttribute}
	 * @param representation The owner {@link Representation}. If the representation is null, the owner of
	 *            {@link StringAttribute} is used
	 * @param label The label term of the {@link StringAttribute}. If the label is null, the label of
	 *            {@link StringAttribute} is used
	 * @param longExpression The long expression for which constraints have to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#STRING_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS}
	 */
	public void checkAttributeAlreadyExistsConstraint(final StringAttribute stringAttribute,
			Representation representation, Term label, final String longExpression) {

		// value (long expression) can be null

		if (representation == null)
			representation = stringAttribute.getOwner();

		if (label == null)
			label = stringAttribute.getLabel();

		for (Attribute existingAttribute : representation.getAttributes()) {

			if (existingAttribute instanceof StringAttribute && existingAttribute.getLabel() != null
					&& existingAttribute.getLabel().getId().equals(label.getId())) {

				StringAttribute existingStringAttribute = (StringAttribute) existingAttribute;

				if (((longExpression == null && existingStringAttribute.getLongExpression() == null) || (longExpression != null && longExpression
						.equals(existingStringAttribute.getLongExpression())))
						&& !stringAttribute.getId().equals(existingStringAttribute.getId())) {

					log.error("String attribute '" + label + "' with long expression '" + longExpression
							+ "' already exists");

					throw new IllegalArgumentException(DGCErrorCodes.STRING_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS,
							label.getSignifier(), longExpression);
				}
			}
		}
	}

	/**
	 * Check single value list attribute constraints.
	 * 
	 * @param singleValueListAttribute The {@link SingleValueListAttribute} for which constraints have to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#SVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS}
	 */
	public void checkConstraints(final SingleValueListAttribute singleValueListAttribute) {

		ConstraintChecker.checkLockConstraint(singleValueListAttribute.getOwner());
		checkAttributeAlreadyExistsConstraint(singleValueListAttribute, null, null, singleValueListAttribute.getValue());
		// TODO Pierre: add the right DGCErrorCodes
		checkAttributeLabelConceptTypeConstraint(null, singleValueListAttribute,
				MeaningConstants.META_SINGLE_STATIC_LIST_TYPE_UUID, DGCErrorCodes.ARGUMENT_INVALID);
	}

	/**
	 * Check that a single value list attribute with same content does not already exist.
	 * 
	 * @param singleValueListAttribute The {@link SingleValueListAttribute}
	 * @param representation The owner {@link Representation}. If the representation is null, the owner of
	 *            {@link SingleValueListAttribute} is used
	 * @param label The label term of the {@link SingleValueListAttribute}. If the label is null, the label of
	 *            {@link SingleValueListAttribute} is used
	 * @param value The value for which constraints have to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#SVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS}
	 */
	public void checkAttributeAlreadyExistsConstraint(final SingleValueListAttribute singleValueListAttribute,
			Representation representation, Term label, final String value) {

		// value can be null

		if (representation == null)
			representation = singleValueListAttribute.getOwner();

		if (label == null)
			label = singleValueListAttribute.getLabel();

		for (Attribute existingAttribute : representation.getAttributes()) {

			if (existingAttribute instanceof SingleValueListAttribute && existingAttribute.getLabel() != null
					&& existingAttribute.getLabel().getId().equals(label.getId())) {

				SingleValueListAttribute existingSVLAttribute = (SingleValueListAttribute) existingAttribute;

				if (((value == null && existingSVLAttribute.getValue() == null) || (value != null && value
						.equals(existingSVLAttribute.getValue())))
						&& !singleValueListAttribute.getId().equals(existingSVLAttribute.getId())) {

					log.error("Single value list attribute '" + label + "' with value '" + value + "' already exists");

					throw new IllegalArgumentException(DGCErrorCodes.SVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS,
							label.getSignifier(), value);
				}
			}
		}
	}

	/**
	 * Check multiple value list attribute constraints.
	 * 
	 * @param multiValueListAttribute The {@link MultiValueListAttribute} for which constraints have to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#MVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS}
	 */
	public void checkConstraints(final MultiValueListAttribute multiValueListAttribute) {

		ConstraintChecker.checkLockConstraint(multiValueListAttribute.getOwner());
		checkAttributeAlreadyExistsConstraint(multiValueListAttribute, null, null, multiValueListAttribute.getValues());
		// TODO Pierre: add the right DGCErrorCodes
		checkAttributeLabelConceptTypeConstraint(null, multiValueListAttribute,
				MeaningConstants.META_MULTI_STATIC_LIST_TYPE_UUID, DGCErrorCodes.ARGUMENT_INVALID);
	}

	/**
	 * Check that a multiple value list attribute with same content does not already exists.
	 * 
	 * @param multiValueListAttribute The {@link MultiValueListAttribute}
	 * @param representation The owner {@link Representation}. If the representation is null, the owner of
	 *            {@link MultiValueListAttribute} is used
	 * @param label The label term of the {@link MultiValueListAttribute}. If the label is null, the label of
	 *            {@link MultiValueListAttribute} is used
	 * @param values The values for which constraints have to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#MVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS}
	 */
	public void checkAttributeAlreadyExistsConstraint(final MultiValueListAttribute multiValueListAttribute,
			Representation representation, Term label, final Collection<String> values) {

		// values can be null or empty

		if (representation == null)
			representation = multiValueListAttribute.getOwner();

		if (label == null)
			label = multiValueListAttribute.getLabel();

		for (Attribute existingAttribute : representation.getAttributes()) {

			if (existingAttribute instanceof MultiValueListAttribute && existingAttribute.getLabel() != null
					&& existingAttribute.getLabel().getId().equals(label.getId())) {

				MultiValueListAttribute existingMVLAttribute = (MultiValueListAttribute) existingAttribute;

				if (ServiceUtility.haveSameContent(values, existingMVLAttribute.getValues())
						&& !multiValueListAttribute.getId().equals(existingMVLAttribute.getId())) {

					log.error("Multiple value list attribute '" + label + "' with values '" + values
							+ "' already exists");

					throw new IllegalArgumentException(DGCErrorCodes.MVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS,
							label.getSignifier(), values);
				}
			}
		}
	}

	/**
	 * Check date-time attribute constraints.
	 * 
	 * @param dateTimeAttribute The {@link DateTimeAttribute} for which constraints have to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#DT_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS}
	 */
	public void checkConstraints(final DateTimeAttribute dateTimeAttribute) {

		ConstraintChecker.checkLockConstraint(dateTimeAttribute.getOwner());
		checkAttributeAlreadyExistsConstraint(dateTimeAttribute, null, null, dateTimeAttribute.getDateTime());
		// TODO Pierre: add the right DGCErrorCodes
		checkAttributeLabelConceptTypeConstraint(null, dateTimeAttribute, MeaningConstants.META_DATE_TIME_TYPE_UUID,
				DGCErrorCodes.ARGUMENT_INVALID);
	}

	/**
	 * Check that a date-time attribute with same content does not already exist.
	 * 
	 * @param dateTimeAttribute The {@link DateTimeAttribute}
	 * @param representation The owner {@link Representation}. If the representation is null, the owner of
	 *            {@link DateTimeAttribute} is used
	 * @param label The label term of the {@link DateTimeAttribute}. If the label is null, the label of
	 *            {@link DateTimeAttribute} is used
	 * @param dateTime The date-time ({@link Calendar}) for which constraints have to be checked
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#DT_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS}
	 */
	public void checkAttributeAlreadyExistsConstraint(final DateTimeAttribute dateTimeAttribute,
			Representation representation, Term label, final Calendar dateTime) {

		// value (dateTime) can be null

		if (representation == null)
			representation = dateTimeAttribute.getOwner();

		if (label == null)
			label = dateTimeAttribute.getLabel();

		for (Attribute existingAttribute : representation.getAttributes()) {

			if (existingAttribute instanceof DateTimeAttribute && existingAttribute.getLabel() != null
					&& existingAttribute.getLabel().getId().equals(label.getId())) {

				DateTimeAttribute existingDateTimeAttribute = (DateTimeAttribute) existingAttribute;

				if (((dateTime == null && existingDateTimeAttribute.getDateTime() == null) || (dateTime != null && dateTime
						.equals(existingDateTimeAttribute.getDateTime())))
						&& !dateTimeAttribute.getId().equals(existingDateTimeAttribute.getId())) {

					log.error("Date-time attribute '" + label + "' with date-time '" + dateTime + "' already exists");

					throw new IllegalArgumentException(DGCErrorCodes.DT_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS,
							label.getSignifier(), dateTime);
				}
			}
		}
	}
}
