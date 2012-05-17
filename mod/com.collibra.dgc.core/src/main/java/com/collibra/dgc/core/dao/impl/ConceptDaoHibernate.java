package com.collibra.dgc.core.dao.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.ConceptDao;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.impl.ConceptImpl;

@Service
public class ConceptDaoHibernate extends AbstractDaoHibernate<Concept, ConceptImpl> implements ConceptDao {

	@Autowired
	public ConceptDaoHibernate(SessionFactory sessionFactory) {
		super(Concept.class, ConceptImpl.class, sessionFactory);
	}

	@Override
	public Concept save(Concept object) {
		return super.save(object);
	}

	public List<Concept> findAllSpecializedConcepts(Concept concept) {
		HashSet<Concept> specializedConcepts = new HashSet<Concept>();
		addSpecializedConcepts(concept, specializedConcepts);
		return new LinkedList<Concept>(specializedConcepts);
	}

	protected void addSpecializedConcepts(Concept concept, Set<Concept> results) {
		for (Concept c : findSpecializedConcepts(concept))
			if (results.add(c))
				addSpecializedConcepts(c, results);
	}

	public List<Concept> findSpecializedConcepts(Concept concept) {
		Query query = getSession().createQuery(
				"from ConceptImpl concept where concept.generalConcept = :generalConcept");
		query.setParameter("generalConcept", concept);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	public List<Concept> findAllConceptsWithConceptType(Concept concept) {
		Query query = getSession().createQuery("from ConceptImpl concept where concept.type = :type");
		query.setParameter("type", concept);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	public Concept findCategorizationSchemeByResourceId(String resourceId) {
		Concept scheme = findById(resourceId);
		if (!scheme.isCategorizationScheme()) {
			return null;
		}
		return scheme;
	}

	public List<Concept> findCategorizationSchemes(Category category) {
		Criteria query = getSession().createCriteria(ConceptImpl.class);
		query.add(Expression.eq("categorizationScheme", true));
		query.createCriteria("categories", "category").add(Expression.eq("id", category.getId()));
		return query.list();
	}

	public List<Concept> findCategorizationSchemesForConcept(Concept categorizedConcept) {
		Criteria query = getSession().createCriteria(ConceptImpl.class);
		query.add(Expression.eq("categorizationScheme", true));
		query.createCriteria("conceptsCategorized", "concept").add(Expression.eq("id", categorizedConcept.getId()));
		return query.list();
	}

	// @Override
	// public boolean isPersisted(final Concept concept) {
	// Query query = getSession().createQuery("SELECT concept.id from ConceptImpl concept where concept = :c");
	// query.setParameter("c", concept);
	// String result = (String) query.uniqueResult();
	//
	// return result != null;
	// }
}
