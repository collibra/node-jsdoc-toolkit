package com.collibra.dgc.core.model.representation.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Target;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.exceptions.ConstraintViolationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.impl.CommunityImpl;
import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.impl.ObjectTypeImpl;
import com.collibra.dgc.core.model.representation.Designation;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.representation.facttypeform.impl.BinaryFactTypeFormImpl;
import com.collibra.dgc.core.model.representation.facttypeform.impl.CharacteristicFormImpl;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.impl.RuleSetImpl;
import com.collibra.dgc.core.util.Defense;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
@Table(name = "VOCABULARIES")
public class VocabularyImpl extends ResourceImpl implements Vocabulary, Comparable<Vocabulary> {
	private static final Logger log = LoggerFactory.getLogger(VocabularyImpl.class);
	private String name;
	private String uri;

	private Set<Vocabulary> incorporatedVocabularies;
	private Set<Term> terms;
	private Set<BinaryFactTypeForm> binaryFactTypeForms;
	private Set<CharacteristicForm> characteristicForms;
	private Set<RuleSet> ruleSets;

	private ObjectType type = null;

	private boolean isMeta;

	private Community community;

	public VocabularyImpl() {
		super();
		// Dirty status is changed to true by: Terms, Names , BinaryFactTypeForms, CharacteristicForms
	}

	public VocabularyImpl(Vocabulary vocabulary) {
		super(vocabulary);
		setUri(vocabulary.getUri());
		setName(vocabulary.getName());
		setCommunity(vocabulary.getCommunity());
	}

	public VocabularyImpl(String uri, String name, Community community, ObjectType type) {
		super();
		this.uri = uri;
		this.name = name;
		this.type = type;
		setCommunity(community);
	}

	@Override
	@ManyToOne
	@JoinColumn(name = "TYPE")
	@Access(value = AccessType.FIELD)
	@Target(value = ObjectTypeImpl.class)
	public ObjectType getType() {
		return type;
	}

	public void setType(ObjectType type) {
		this.type = type;
	}

	@Override
	protected void initializeRelations() {
		this.incorporatedVocabularies = new HashSet<Vocabulary>();
		this.binaryFactTypeForms = new HashSet<BinaryFactTypeForm>();
		this.characteristicForms = new HashSet<CharacteristicForm>();
		this.terms = new HashSet<Term>();
		this.ruleSets = new HashSet<RuleSet>();
	}

	@Override
	@Column(name = "NAME", nullable = false, unique = true)
	@Access(value = AccessType.FIELD)
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	@Column(name = "URI", nullable = false, unique = true)
	@Access(value = AccessType.FIELD)
	public String getUri() {
		return uri;
	}

	@Override
	public void setUri(String namespace) {
		this.uri = namespace;
	}

	@Override
	@ManyToOne
	@JoinColumn(name = "COMM")
	@Target(value = CommunityImpl.class)
	public Community getCommunity() {
		return community;
	}

	@Override
	public void setCommunity(Community community) {
		this.community = community;
	}

	@Override
	public void addIncorporatedVocabulary(Vocabulary vocabulary) {
		if (getId().equals(vocabulary.getId())) {
			String message = "Vocabulary cannot incorporate itself.";
			log.error(message);
			throw new ConstraintViolationException(message, vocabulary.getId(), getClass().getName(),
					DGCErrorCodes.VOCABULARY_CANNOT_INCORPORATE_ITSELF);
		}

		incorporatedVocabularies.add(vocabulary);
	}

	@Override
	public void removeIncorporatedVocabulary(Vocabulary incorporatedVoc) {
		incorporatedVocabularies.remove(incorporatedVoc);
	}

	@Override
	@OneToMany(targetEntity = BinaryFactTypeFormImpl.class)
	@JoinColumn(name = "VOCABULARY", insertable = false, updatable = false)
	@AuditJoinTable(name = "VOC_BFTFS_AUD")
	@org.hibernate.annotations.Where(clause = "REP_TYPE='BI'")
	public Set<BinaryFactTypeForm> getBinaryFactTypeForms() {
		return binaryFactTypeForms;
	}

	@Override
	@OneToMany(targetEntity = CharacteristicFormImpl.class)
	@JoinColumn(name = "VOCABULARY", insertable = false, updatable = false)
	@AuditJoinTable(name = "VOC_CFS_AUD")
	@org.hibernate.annotations.Where(clause = "REP_TYPE='CF'")
	public Set<CharacteristicForm> getCharacteristicForms() {
		return characteristicForms;
	}

	@Override
	@Transient
	public Set<Designation> getDesignations() {
		HashSet<Designation> designations = new HashSet<Designation>();
		designations.addAll(getTerms());
		return designations;
	}

	@Override
	@Transient
	public Set<Representation> getRepresentations() {
		HashSet<Representation> representations = new HashSet<Representation>();
		representations.addAll(getTerms());
		representations.addAll(getCharacteristicForms());
		representations.addAll(getBinaryFactTypeForms());
		return representations;
	}

	@Override
	@OneToMany(targetEntity = TermImpl.class)
	@JoinColumn(name = "VOCABULARY", insertable = false, updatable = false)
	@AuditJoinTable(name = "VOC_TERMS_AUD")
	@org.hibernate.annotations.Where(clause = "REP_TYPE='TE'")
	public Set<Term> getTerms() {
		return terms;
	}

	@Override
	@Transient
	public Set<Term> getAllTerms() {
		Set<Vocabulary> vocs = getAllIncorporatedVocabularies();
		Set<Term> terms = new HashSet<Term>();

		for (Vocabulary voc : vocs)
			terms.addAll(voc.getTerms());

		return terms;
	}

	/**
	 * This is used for a new term version that does not create a new vocabulary version.
	 * @param oldTerm the old term
	 * @param newTerm the new term which replaces the old term.
	 */
	public void replaceTerm(TermImpl oldTerm, TermImpl newTerm) {
		terms.remove(oldTerm);
		terms.add(newTerm);
		newTerm.setVocabulary(this);
	}

	@Override
	@Transient
	public Set<Representation> getNamesAndTerms() {
		Set<Representation> result = new HashSet();
		result.addAll(terms);
		return result;
	}

	@Override
	@ManyToMany(targetEntity = VocabularyImpl.class)
	@JoinTable(name = "INC_VOCABULARIES", joinColumns = { @JoinColumn(name = "ID") }, inverseJoinColumns = { @JoinColumn(name = "INC_ID", referencedColumnName = "ID") })
	@AuditJoinTable(name = "VOC_INCVOC_AUD")
	public Set<Vocabulary> getIncorporatedVocabularies() {
		return incorporatedVocabularies;
	}

	@Override
	@Transient
	public Set<Vocabulary> getIncorporatedVocabularies(boolean excludeSBVR) {
		Set<Vocabulary> vocabularies = incorporatedVocabularies;
		if (excludeSBVR) {
			// Exclude all SBVR vocabularies.
			vocabularies = new HashSet<Vocabulary>(incorporatedVocabularies);
			for (Vocabulary voc : incorporatedVocabularies) {
				if (Constants.METAMODEL_COMMUNITY_URI.equals(voc.getCommunity().getTopLevelCommunity().getUri())) {
					vocabularies.remove(voc);
				}
			}
		}
		return vocabularies;
	}

	@Override
	@Transient
	public Set<Vocabulary> getAllIncorporatedVocabularies() {
		Set<Vocabulary> result = new HashSet<Vocabulary>();
		addIncorporatedVocabularies(this, result, true);
		return result;
	}

	@Override
	@Transient
	public Set<Vocabulary> getAllNonSbvrIncorporatedVocabularies() {
		Set<Vocabulary> result = new HashSet<Vocabulary>();
		addIncorporatedVocabularies(this, result, false);
		return result;
	}

	private void addIncorporatedVocabularies(Vocabulary v, Set<Vocabulary> incorporatedVocs, boolean includeSBVR) {
		for (Vocabulary voc : v.getIncorporatedVocabularies()) {
			if (includeSBVR || (!includeSBVR && !voc.isSBVR()))
				if (incorporatedVocs.add(voc))
					addIncorporatedVocabularies(voc, incorporatedVocs, includeSBVR);
		}
	}

	@Override
	public void addBinaryFactTypeForm(BinaryFactTypeForm bftf) {
		binaryFactTypeForms.add(bftf);
		((BinaryFactTypeFormImpl) bftf).setVocabulary(this);
	}

	@Override
	public void addCharacteristicForm(CharacteristicForm charForm) {
		characteristicForms.add(charForm);
		((CharacteristicFormImpl) charForm).setVocabulary(this);
	}

	@Override
	public void addTerm(Term term) {
		terms.add(term);
		// damien, stop deleting this and breaking tests
		// we need it for remove to work correctly

		// (damien) FIXME I think this is a liability, since representations are immutable objects.
		// Vocabulary is set in factory and should never change afterwards. If it does this will break the contract for
		// Sets, since the vocabulary is used in the equals and hashcode.
		((TermImpl) term).setVocabulary(this);
	}

	protected void setIncorporatedVocabularies(Set<Vocabulary> incorporatedVocabularies) {
		this.incorporatedVocabularies = incorporatedVocabularies;
	}

	protected void setTerms(Set<Term> terms) {
		this.terms = terms;
	}

	protected void setBinaryFactTypeForms(Set<BinaryFactTypeForm> binaryFactTypeForms) {
		this.binaryFactTypeForms = binaryFactTypeForms;
	}

	protected void setCharacteristicForms(Set<CharacteristicForm> characteristicForms) {
		this.characteristicForms = characteristicForms;
	}

	@Override
	@Transient
	public Representation getPreferredRepresentation(Meaning meaning) {
		if (meaning == null) {
			return null;
		}
		for (Representation representation : getRepresentations()) {
			if (representation.getIsPreferred() && meaning.equals(representation.getMeaning())) {
				return representation;
			}
		}
		return null;
	}

	@Override
	@Transient
	public Term getPreferredTerm(Meaning meaning) {
		if (meaning == null) {
			return null;
		}
		for (Term term : getTerms()) {
			if (term.getIsPreferred() && meaning.equals(term.getMeaning())) {
				return term;
			}

		}
		return null;
	}

	@Override
	@Transient
	public Term getPreferredTermInAllIncorporatedVocabularies(Meaning meaning) {
		if (meaning == null) {
			return null;
		}
		Term foundTerm = null;
		for (Term term : getTerms()) {
			if (term.getIsPreferred() && meaning.equals(term.getMeaning())) {
				return term;
			}

		}
		if (foundTerm == null) {
			for (Vocabulary incVocabulary : getIncorporatedVocabularies()) {
				foundTerm = incVocabulary.getPreferredTermInAllIncorporatedVocabularies(meaning);
				if (foundTerm != null) {
					return foundTerm;
				}
			}
		}
		return null;
	}

	@Override
	@Transient
	public CharacteristicForm getPreferredCharacteristicForm(Meaning meaning) {
		if (meaning == null) {
			return null;
		}
		for (CharacteristicForm characteristicForm : getCharacteristicForms())
			if (characteristicForm.getIsPreferred() && meaning.equals(characteristicForm.getMeaning())) {
				return characteristicForm;
			}
		return null;
	}

	@Override
	@Transient
	public BinaryFactTypeForm getPreferredBinaryFactTypeForm(Meaning meaning) {
		if (meaning == null) {
			return null;
		}
		for (BinaryFactTypeForm binaryFactTypeForm : getBinaryFactTypeForms())
			if (binaryFactTypeForm.getIsPreferred() && meaning.equals(binaryFactTypeForm.getMeaning())) {
				return binaryFactTypeForm;
			}
		return null;
	}

	@Override
	@OneToMany(targetEntity = RuleSetImpl.class, mappedBy = "vocabulary")
	@AuditJoinTable(name = "VOC_RULESETS_AUD")
	public Set<RuleSet> getRuleSets() {
		return this.ruleSets;
	}

	protected void setRuleSets(Set<RuleSet> ruleSets) {
		this.ruleSets = ruleSets;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Vocabulary))
			return false;
		Vocabulary other = (Vocabulary) obj;

		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;

		if (uri == null) {
			if (other.getUri() != null)
				return false;
		} else if (!uri.equals(other.getUri()))
			return false;

		if (name == null) {
			if (other.getName() != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;

		return true;
	}

	@Override
	public String toString() {
		// FIXME commented to check the lazy initialization problem.
		// return "VocabularyImpl [name=" + this.name + ", uri=" + this.uri + ", getResourceId()=" +
		// this.getResourceId()
		// + ", getVersion()=" + this.getVersion() + "]";
		return this.name;
	}

	@Override
	public VocabularyImpl clone() {
		return new VocabularyImpl(this);
	}

	@Override
	public void addRuleSet(RuleSet ruleSet) {
		ruleSets.add(ruleSet);
	}

	@Override
	@Transient
	public Term getTerm(final String signifier) {
		for (Term t : getTerms()) {
			if (t.getSignifier().equals(signifier)) {
				return t;
			}
		}

		return null;
	}

	@Override
	@Transient
	public Term getTermInAllIncorporatedVocabularies(final String signifier) {
		Term result = getTerm(signifier);
		if (result != null) {
			return result;
		}
		for (Vocabulary vocabulary : getAllIncorporatedVocabularies()) {
			result = vocabulary.getTermInAllIncorporatedVocabularies(signifier);
			if (result != null) {
				return result;
			}
		}
		return result;
	}

	@Override
	public String verbalise() {
		return getName();
	}

	@Override
	public void changeName(String name) {

		Defense.notEmpty(name, DGCErrorCodes.VOCABULARY_NAME_NULL, DGCErrorCodes.VOCABULARY_NAME_EMPTY, "name");

		this.name = name;
	}

	@Override
	@Transient
	public boolean isSBVR() {
		if (getUri() == null) {
			return false;
		}
		return getUri().startsWith(Constants.META_SBVR_URI_PREFIX);
	}

	/**
	 * @return the isMeta
	 */
	@Override
	public boolean isMeta() {
		return isMeta;
	}

	/**
	 * @param isMeta the isMeta to set
	 */
	public void setMeta(boolean isMeta) {
		this.isMeta = isMeta;
	}

	@Override
	public int compareTo(Vocabulary v) {
		return name.toLowerCase().compareTo(v.getName().toLowerCase());
	}
}
