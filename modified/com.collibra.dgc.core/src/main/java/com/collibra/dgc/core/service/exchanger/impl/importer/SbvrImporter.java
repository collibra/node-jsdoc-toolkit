package com.collibra.dgc.core.service.exchanger.impl.importer;

import hub.sam.mof.Repository;
import hub.sam.mof.instancemodel.MetaModelException;
import hub.sam.mof.xmi.XmiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmof.Association;
import cmof.ElementImport;
import cmof.PackageableElement;
import cmof.Property;
import cmof.UmlClass;
import cmof.common.ReflectiveCollection;
import cmof.common.ReflectiveSequence;
import cmof.reflection.Extent;

import com.collibra.dgc.core.dao.BinaryFactTypeDao;
import com.collibra.dgc.core.dao.CharacteristicDao;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.service.exchanger.exceptions.SbvrMissingVocabularyException;
import com.collibra.dgc.core.service.exchanger.exceptions.SbvrXmiException;
import com.collibra.dgc.core.service.exchanger.options.SbvrImporterOptions;
import com.collibra.dgc.core.service.impl.ConstraintChecker;

import core.abstractions.ownerships.Element;

/**
 * Imports an XMI as inputstream and creates a vocabulary object. Known limitation: does not support >2-ary fact types.
 * 
 * @author dtrog
 * 
 */
public class SbvrImporter {
	private transient static final Logger log = LoggerFactory.getLogger(SbvrImporter.class);

	private InputStream sbvrExchangeInputStream;
	private final RepresentationFactory representationFactory;
	private final MeaningFactory meaningFactory;
	private final BinaryFactTypeDao binaryFactTypeDao;
	private final CharacteristicDao characteristicDao;
	private final ConstraintChecker constraintChecker;

	private Vocabulary vocabulary;

	private final Map<String, Term> signifierToTerm = new HashMap<String, Term>();
	private final Map<Association, BinaryFactTypeForm> assocationToBftft = new HashMap<Association, BinaryFactTypeForm>();
	private final Map<UmlClass, Term> classToTerm = new HashMap<UmlClass, Term>();
	private final SbvrImporterOptions options;
	private final Community community;

	public SbvrImporter(InputStream xmiInputStream, Community community,
			final RepresentationFactory representationFactory, final MeaningFactory meaningFactory,
			final BinaryFactTypeDao binaryFactTypeDao, final CharacteristicDao characteristicDao,
			final ConstraintChecker constraintChecker) {
		this(xmiInputStream, community, new SbvrImporterOptions(), representationFactory, meaningFactory,
				binaryFactTypeDao, characteristicDao, constraintChecker);
	}

	private final List<TermProperty> termProperties = new LinkedList<TermProperty>();

	private static class TermProperty {
		public Term term;
		public Property property;
	}

	public SbvrImporter(InputStream is, Community community, SbvrImporterOptions options,
			final RepresentationFactory representationFactory, final MeaningFactory meaningFactory,
			final BinaryFactTypeDao binaryFactTypeDao, final CharacteristicDao characteristicDao,
			final ConstraintChecker constraintChecker) {
		super();
		sbvrExchangeInputStream = is;
		this.options = options;
		this.community = community;
		this.representationFactory = representationFactory;
		this.meaningFactory = meaningFactory;
		this.binaryFactTypeDao = binaryFactTypeDao;
		this.characteristicDao = characteristicDao;
		this.constraintChecker = constraintChecker;
	}

	/**
	 * Remove the hrefs from the xmi, since the MOF2 java library does not support them.
	 * 
	 * @param is The XMI {@link InputStream}
	 */
	private void preProcessHrefs(InputStream is) {
		SbvrXmiPreprocesor preprocessor = new SbvrXmiPreprocesor(is);
		sbvrExchangeInputStream = preprocessor.preprocess();
	}

	/**
	 * Extracts the CMOF Package from the XMI inputstream
	 * @return The CMOF package that represents the vocabulary.
	 */
	private cmof.Package extractCmofPackage() throws IOException, JDOMException, XmiException, MetaModelException {
		Repository repository = Repository.getLocalRepository();

		Extent cmofExtent = repository.getExtent(Repository.CMOF_EXTENT_NAME);
		cmof.Package cmofPackage = (cmof.Package) cmofExtent.query("Package:cmof");

		Extent businessVocabulariesMetaExtent = repository.createExtent("businessVocabulariesMetaExtent");
		repository.loadXmiIntoExtent(businessVocabulariesMetaExtent, cmofPackage, sbvrExchangeInputStream);

		ReflectiveCollection<? extends cmof.reflection.Object> objects = businessVocabulariesMetaExtent
				.outermostComposites();

		// Find the package element which equals a vocabulary
		cmof.Package vocabularyPackage = null;
		for (cmof.reflection.Object o : objects) {
			if (o.getMetaClass().getQualifiedName().equals("cmof.Package")) {
				vocabularyPackage = (cmof.Package) o;
			}
		}

		if (vocabularyPackage == null) {
			throw new SbvrMissingVocabularyException("There is no package that contains the vocabulary");
		}
		return vocabularyPackage;
	}

	/**
	 * Prepares a {@link Vocabulary} from the {@link cmof.Package}.
	 * @param vocabularyPackage The {@link cmof.Package} that represents the {@link Vocabulary}
	 */
	private void makeVocabulary(cmof.Package vocabularyPackage, String vocabularyName, Community community) {
		String uri = vocabularyPackage.getUri();

		try {

			vocabulary = representationFactory.makeVocabulary(community, uri, vocabularyName);
			constraintChecker.checkVocabularyWihtURIAlreadyExistsConstraint(uri, vocabulary);
			constraintChecker.checkVocabularyWithNameAlreadyExistsConstraint(vocabularyName, vocabulary);

		} catch (IllegalArgumentException e) {
			// take the existing vocabulary
			for (Vocabulary voc : community.getVocabularies())
				if (voc.getUri().equals(uri))
					vocabulary = voc;
		}
	}

	/**
	 * Traverses the elements in the {@link cmof.Package} and creates {@link Representation}s
	 * 
	 * @param vocabularyPackage The {@link cmof.Package} that represents the {@link Vocabulary}
	 */
	private void makeRepresentations(cmof.Package vocabularyPackage) {
		ReflectiveCollection<? extends Element> contents = vocabularyPackage.getOwnedElement();
		// we handle the element imports later because we need to be sure that the terms they alias are created first
		List<ElementImport> elementImports = new LinkedList<ElementImport>();
		List<Association> associations = new LinkedList<Association>();

		// when minimal import, ignore all but only the necessary concepts
		boolean shouldMinimizeImport = options.isShouldMinimizeImport();
		for (Element element : contents) {
			if (element.getMetaClass().getQualifiedName().equals("cmof.Class")) {
				handleClass((UmlClass) element);
			} else if (!shouldMinimizeImport && element.getMetaClass().getQualifiedName().equals("cmof.Association")) {
				// add assocations afterwards
				associations.add((Association) element);
			} else if (element.getMetaClass().getQualifiedName().equals("cmof.ElementImport")) {
				// add import to handle afterwards
				elementImports.add((ElementImport) element);
			}
		}
		for (Association association : associations) {
			handleAssociation(association);
		}

		for (ElementImport elementImport : elementImports) {
			handleElementImport(elementImport);
		}

		// needs to run last because otherwise model will be polluted with "has / of" properties
		// now we can filter them on preexisting associations
		for (TermProperty termProperty : termProperties) {
			handleTermProperty(termProperty);
		}
	}

	private void handleElementImport(ElementImport element) {
		log.debug("Handling element import: " + element);
		PackageableElement importedElement = element.getImportedElement();
		if (importedElement.getMetaClass().getQualifiedName().equals("cmof.Class")) {
			handleClassImport((UmlClass) importedElement, element);
		} else if (importedElement.getMetaClass().getQualifiedName().equals("cmof.Association")) {
			handleAssociationImport((Association) importedElement, element);
		} else {
			log.warn("unhandled element import " + element);
		}
	}

	private void handleAssociationImport(Association assocation, ElementImport element) {
		if (options.isShouldMinimizeImport()) {
			return;
		}
		BinaryFactTypeForm preferredBftf = assocationToBftft.get(assocation);
		if (preferredBftf == null) {
			return;
		}
		if (!preferredBftf.getCoRole().equals(Constants.SBVR_DEFAULT_COROLE)) {
			return;
		}

		String coRole = stripToRole(preferredBftf.getTailTerm(), preferredBftf.getHeadTerm(), element.getAlias()
				.toLowerCase());
		preferredBftf.setCoRole(coRole);
		assocationToBftft.put(assocation, preferredBftf);
	}

	private void handleClassImport(UmlClass clazz, ElementImport element) {
		log.debug("Handling class import " + clazz.getName());

		Term preferredTerm = classToTerm.get(clazz);
		String alias = finishTermExpression(element.getAlias());
		if (preferredTerm == null) {
			log.error("The alias " + alias + " refers an unexisting term " + clazz.getName());
			return;
		}

		if (signifierToTerm.containsKey(alias)) {
			return;
		}

		// avoid unnecessary creation of synonyms
		if (findTermInIncorporatedVocabularies(alias) != null) {
			return;
		}

		Term synonym = representationFactory.makeSynonym(vocabulary, preferredTerm, finishTermExpression(alias));
		signifierToTerm.put(alias, synonym);
	}

	private Term getTerm(String signifier) {
		return getTerm(signifier, null);
	}

	private Term getTerm(String signifier, ObjectType objectType) {
		signifier = finishTermExpression(signifier);

		// lookup in our cache if we already have a term for this signifier
		Term term = signifierToTerm.get(signifier);
		if (term != null) {
			return term;
		}

		// lookup the term in the incorporated vocabularies
		term = findTermInIncorporatedVocabularies(signifier);
		if (term != null) {
			signifierToTerm.put(signifier, term);
			return term;
		}

		if (objectType == null) {
			// no objectttype was specified so we need to create one
			// look up the objecttype is already mapped for this signifier in the options
			objectType = options.getObjectTypeForSignifier(signifier);
		}
		// only create the terms from the options in a minimal import
		if (options.isShouldMinimizeImport() && objectType == null) {
			return null;
		}
		if (objectType == null) {
			objectType = meaningFactory.makeObjectType();
			// we add the objecttype to the options such that in consecutive imports we reuse the same objecttype for
			// synonyms
			options.mapSignifierToObjectType(signifier, objectType);
		}
		// not cached yet, so we need to create a new term
		term = representationFactory.makeTerm(vocabulary, signifier, objectType);
		signifierToTerm.put(signifier, term);
		return term;
	}

	private Term findTermInIncorporatedVocabularies(String signifier) {
		Term term = vocabulary.getTerm(signifier);
		if (term != null)
			return term;

		for (Vocabulary vocabulary : options.getIncorporatedVocabularies()) {
			term = vocabulary.getTerm(signifier);
			if (term != null) {
				// put the term in our own cache for performance
				return term;
			}
		}
		return null;
	}

	private Term handleClass(UmlClass clazz) {
		String clazzName = finishTermExpression(clazz.getName());
		log.debug("hanlding class: " + clazzName);
		Term term = getTerm(clazzName);
		if (term == null) {
			return null;
		}
		ObjectType objectType = term.getObjectType();

		// Set the general concept if mapped.
		ObjectType generalConcept = options.getGeneralConcept(term.getSignifier());
		if (generalConcept != null) {
			term.getObjectType().setGeneralConcept(generalConcept);
		} else {
			// check for a supertype
			ReflectiveCollection<? extends UmlClass> superClasses = clazz.getSuperClass();
			for (UmlClass superClass : superClasses) {
				log.debug(term.getSignifier() + " has superclass: " + superClass.getName());
				Term superTerm = handleClass(superClass);
				if (superTerm == null) {
					continue;
				}
				ObjectType superObjectType = superTerm.getObjectType();
				objectType.setGeneralConcept(superObjectType);
			}
		}

		if (!options.isShouldMinimizeImport()) {
			// get the properties
			ReflectiveSequence<? extends Property> properties = clazz.getOwnedAttribute();
			for (Property property : properties) {
				handleClassProperty(term, property);
			}
		}

		// store the term created for the term for handling aliases in handleElementImport
		classToTerm.put(clazz, term);
		return term;
	}

	private void handleTermProperty(TermProperty termProperty) {
		if (options.isShouldMinimizeImport()) {
			return;
		}
		Term term = termProperty.term;
		Property property = termProperty.property;

		if (property.getName().startsWith("is ")) {
			handleCharacteristicProperty(term, property);
			return;
		}
		Term tailTerm = getTerm(property.getName());
		// already defined in another vocabulary -> ignore
		if (bftfAlreadyExists(term, Constants.SBVR_PROPERTY_ROLE_EXPRESSION, tailTerm)) {
			return;
		}
		if (bftfAlreadyExists(tailTerm, Constants.SBVR_PROPERTY_ROLE_EXPRESSION, term)) {
			return;
		}
		representationFactory.makeBinaryFactTypeForm(vocabulary, term, Constants.SBVR_PROPERTY_ROLE_EXPRESSION,
				Constants.SBVR_PROPERTY_COROLE_EXPRESSION, tailTerm);
	}

	private void handleClassProperty(Term term, Property property) {
		// handle this later to avoid duplications
		TermProperty termProperty = new TermProperty();
		termProperty.property = property;
		termProperty.term = term;
		termProperties.add(termProperty);
	}

	private void handleCharacteristicProperty(Term term, Property property) {
		if (options.isShouldMinimizeImport()) {
			return;
		}
		String role = property.getName().toLowerCase();
		if (cFormAlreadyExists(term, role)) {
			return;
		}
		representationFactory.makeCharacteristicForm(vocabulary, term, role);
	}

	private String stripToRole(Term headTerm, Term tailTerm, String wholeFactName) {
		// associations only contains a whole string for the fact type in one reading direction
		// for example: "person drives car"
		// we need to get the role from this, thus we subtract the head and tailterm from this string
		int lengthHeadTerm = headTerm.getSignifier().length();
		int lengthTailTerm = tailTerm.getSignifier().length();
		CharSequence roleWithTailTerm = wholeFactName.subSequence(lengthHeadTerm + 1, wholeFactName.length());
		CharSequence roleExpression = roleWithTailTerm.subSequence(0, roleWithTailTerm.length() - lengthTailTerm - 1);
		return roleExpression.toString().trim();
	}

	private void handleAssociation(Association association) {
		if (options.isShouldMinimizeImport()) {
			return;
		}
		log.debug("handeling association: " + association);
		ReflectiveSequence<? extends Property> properties = association.getOwnedEnd();
		List<Term> terms = new ArrayList<Term>(2);
		for (Property property : properties) {
			terms.add(getTerm(property.getName()));
		}

		if (terms.size() != 2) {
			log.error("Association is for an n-ary fact type. Only binary supported currently.");
			return;
		}

		Term headTerm = terms.get(0);
		Term tailTerm = terms.get(1);
		String wholeFactName = association.getName().toLowerCase();
		String role = stripToRole(headTerm, tailTerm, wholeFactName);

		// already defined in another vocabulary -> ignore
		if (bftfAlreadyExists(headTerm, role, tailTerm)) {
			return;
		}

		// make the binary fact type with only the role and a default co-role, we might get it later if there's an alias
		// in the elementImport

		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(vocabulary, headTerm, role,
				Constants.SBVR_DEFAULT_COROLE, tailTerm);

		assocationToBftft.put(association, bftf);
	}

	private boolean bftfAlreadyExists(Term headTerm, String role, Term tailTerm) {
		final List<BinaryFactType> bfts = binaryFactTypeDao.findAll();
		for (BinaryFactType bft : bfts) {
			for (BinaryFactTypeForm bftf : bft.getBinaryFactTypeForms()) {
				if (((bftf.getHeadTerm().equals(headTerm) && bftf.getRole().equals(role)) && bftf.getTailTerm().equals(
						tailTerm))
						|| ((bftf.getHeadTerm().equals(tailTerm)) && bftf.getCoRole().equals(role) && bftf
								.getTailTerm().equals(headTerm))) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean cFormAlreadyExists(Term term, String role) {
		final List<Characteristic> characteristics = characteristicDao.findAll();
		for (Characteristic characteristic : characteristics) {
			for (CharacteristicForm cForm : characteristic.getCharacteristicForms()) {
				if (cForm.getTerm().equals(term) && cForm.getRole().equals(role)) {
					return true;
				}
			}
		}

		return false;
	}

	public final static String getNonNumerics(String str) {

		if (str == null) {
			return null;
		}

		StringBuffer strBuff = new StringBuffer();
		char c;

		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);

			if (!Character.isDigit(c)) {
				strBuff.append(c);
			}
		}
		return strBuff.toString().trim();
	}

	/**
	 * This function makes sure that a given term satisfies the Collibra syntax for term names. This means that the term
	 * consists of a sequence of words separated by a space such that the first and last words are capitalized.
	 * 
	 * Precondition: no illegal characters are contained in the input.
	 * 
	 * @param term the input term
	 * @return a term that was created from the input, and satisfying the Collibra syntax
	 */
	public final String finishTermExpression(String term) {
		String result = "";

		String[] pieces = term.split("\\s"); // split on whitespace

		if (pieces.length == 0)
			return result; // this should not happen if the term argument is valid

		result += StringUtils.capitalize(pieces[0]);

		int i = 1;

		for (; i < pieces.length - 1; i++) {
			result += " " + StringUtils.capitalize(pieces[i]); // middle pieces copied without change
		}

		if (i < pieces.length)
			result += " " + StringUtils.capitalize(pieces[i]);

		if (options.shouldRemoveSuffixIndices()) {
			// TODO could be optimized by removing numerics inside above loops
			return getNonNumerics(result);
		}

		return result;
	}

	public Vocabulary importSbvr(String vocabularyName) {
		try {
			preProcessHrefs(sbvrExchangeInputStream);
			cmof.Package vocabularyPackage = extractCmofPackage();

			makeVocabulary(vocabularyPackage, vocabularyName, community);
			makeRepresentations(vocabularyPackage);
		} catch (IOException e) {
			throw new SbvrXmiException("Error reading file", e);
		} catch (JDOMException e) {
			throw new SbvrXmiException("Error building jdom", e);
		} catch (XmiException e) {
			throw new SbvrXmiException("Error parsing xmi", e);
		} catch (MetaModelException e) {
			throw new SbvrXmiException("Error bulding metamodel", e);
		}

		return vocabulary;
	}

	public Vocabulary importSbvr() {
		try {
			preProcessHrefs(sbvrExchangeInputStream);
			cmof.Package vocabularyPackage = extractCmofPackage();

			makeVocabulary(vocabularyPackage, vocabularyPackage.getName(), community);
			makeRepresentations(vocabularyPackage);
		} catch (IOException e) {
			throw new SbvrXmiException("Error reading file", e);
		} catch (JDOMException e) {
			throw new SbvrXmiException("Error building jdom", e);
		} catch (XmiException e) {
			throw new SbvrXmiException("Error parsing xmi", e);
		} catch (MetaModelException e) {
			throw new SbvrXmiException("Error bulding metamodel", e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return vocabulary;
	}
}
