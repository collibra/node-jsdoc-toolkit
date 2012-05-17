package com.collibra.dgc.core.model.meaning.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.impl.TermImpl;

/**
 * 
 * @author ...
 * @author pmalarme
 * 
 */
@Entity
@Audited
@DiscriminatorValue(value = "OT")
public class ObjectTypeImpl extends ConceptImpl implements ObjectType {

	private Set<Term> terms;

	public ObjectTypeImpl() {
		super();
	}

	public ObjectTypeImpl(String uuid) {
		super(uuid);
	}

	public ObjectTypeImpl(ObjectType objectType, String uuid) {
		super(objectType);
		setResourceId(uuid);
		terms.addAll(objectType.getTerms());
	}

	public void addTerm(Term term) {
		terms.add(term);
	}

	public void removeTerm(Term term) {
		terms.remove(term);
	}

	@OneToMany(targetEntity = TermImpl.class, mappedBy = "objectType")
	public Set<Term> getTerms() {
		return terms;
	}

	protected void setTerms(Set<Term> terms) {
		this.terms = terms;
	}

	@Override
	protected void initializeRelations() {
		super.initializeRelations();
		terms = new HashSet<Term>();
	}

	@Transient
	public Set<? extends Representation> getRepresentations() {
		return getTerms();
	}

	@Override
	public String toString() {
		return "ObjectTypeImpl [getId()=" + getId() + "]";
	}

	public void addRepresentation(Representation representation) {
		if (!(representation instanceof Term)) {
			throw new IllegalArgumentException("Expected a Term");
		}
		addTerm((Term) representation);
	}

	@Override
	public ObjectTypeImpl clone() {
		return new ObjectTypeImpl(this, UUID.randomUUID().toString());
	}

	public boolean hasDefaultTerm() {
		return hasDefaultRepresentation();
	}

	@Transient
	@Override
	public Term findPreferredTerm() {
		for (Term term : getTerms())
			if (term.getIsPreferred())
				return term;

		return null;
	}
}
