package com.collibra.dgc.core.model.meaning.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Target;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.categorizations.impl.CategoryImpl;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.ReferenceScheme;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
@DiscriminatorValue(value = "CO")
public abstract class ConceptImpl extends MeaningImpl implements Concept {

	private Concept generalConcept;
	private ObjectType type;

	private Set<Category> categories;
	private boolean categorizationScheme;

	private Set<Concept> conceptsCategorized;

	public ConceptImpl(String uuid) {
		super(uuid);
	}

	public ConceptImpl() {
		super();
	}

	public ConceptImpl(Concept concept) {
		super(concept);
		setGeneralConcept(concept.getGeneralConcept());
		setType(concept.getType());
		categories.addAll(concept.getCategories());
		conceptsCategorized.addAll(concept.getConceptsCategorized());
		setCategorizationScheme(concept.isCategorizationScheme());
	}

	@Override
	protected void initializeRelations() {
		super.initializeRelations();
		categories = new HashSet<Category>();
		conceptsCategorized = new HashSet<Concept>();
	}

	public void addReferenceScheme(ReferenceScheme referenceScheme) {
		throw new RuntimeException("Not implemented yet");
	}

	@ManyToOne
	@JoinColumn(name = "GENERAL_CONCEPT")
	@Access(value = AccessType.FIELD)
	@Target(value = ConceptImpl.class)
	@ForeignKey(name = "FK_GENERAL_CONCEPT")
	public Concept getGeneralConcept() {
		return generalConcept;
	}

	@Transient
	public Set<ReferenceScheme> getReferenceSchemes() {
		throw new RuntimeException("Not implemented yet");
	}

	@ManyToOne
	@JoinColumn(name = "CONCEPT_TYPE")
	@Access(value = AccessType.FIELD)
	@Target(value = ObjectTypeImpl.class)
	@ForeignKey(name = "FK_CONCEPT_TYPE")
	public ObjectType getType() {
		return type;
	}

	/**
	 * Also adds this concept as a specialized concept of generalConcept.
	 * 
	 * (non-Javadoc)
	 * @see com.collibra.dgc.core.model.meaning.Concept#setGeneralConcept(com.collibra.dgc.core.model.meaning.Concept)
	 */
	public void setGeneralConcept(Concept generalConcept) {
		this.generalConcept = generalConcept;
	}

	public void setType(ObjectType type) {
		this.type = type;
		String typeRID = null;
		if (type != null) {
			typeRID = type.getId();
		}
	}

	@OneToMany(cascade = { CascadeType.MERGE }, targetEntity = CategoryImpl.class)
	@JoinTable(name = "CONCEPT_CATEGORIES", joinColumns = { @JoinColumn(name = "CONCEPT_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID") })
	@Access(value = AccessType.FIELD)
	public Set<Category> getCategories() {
		return categories;
	}

	protected void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public void addCategory(Category category) {
		categories.add(category);

		// Since category added it must be a categorization scheme.
		setCategorizationScheme(true);
		addCategorizedConcept(category.getGeneralConcept());
	}

	public boolean removeCategory(Category category) {
		return categories.remove(category);
	}

	@Column(name = "IS_CATEGORIZATION_SCHEME")
	@Type(type = "boolean")
	@Access(value = AccessType.FIELD)
	public boolean isCategorizationScheme() {
		return categorizationScheme;
	}

	public void setCategorizationScheme(boolean categorizationScheme) {
		this.categorizationScheme = categorizationScheme;
	}

	@OneToMany(cascade = { CascadeType.MERGE }, targetEntity = ConceptImpl.class)
	@JoinTable(name = "CON_CATEGORIZED_CON", joinColumns = { @JoinColumn(name = "CAT_SCHEME_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "CATEGORIZED_CONCEPT_ID", referencedColumnName = "ID") })
	@Access(value = AccessType.FIELD)
	public Set<Concept> getConceptsCategorized() {
		return conceptsCategorized;
	}

	public void setConceptsCategorized(Set<Concept> conceptsCategorized) {
		this.conceptsCategorized = conceptsCategorized;
	}

	public void addCategorizedConcept(Concept concept) {
		this.conceptsCategorized.add(concept);
	}

	public boolean removeCategorizedConcept(Concept concept) {
		return this.conceptsCategorized.remove(concept);
	}
}
