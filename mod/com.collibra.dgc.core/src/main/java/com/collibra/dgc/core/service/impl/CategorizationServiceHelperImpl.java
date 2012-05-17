/**
 * 
 */
package com.collibra.dgc.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.CategorizationTypeDao;
import com.collibra.dgc.core.dao.CategoryDao;
import com.collibra.dgc.core.dao.ConceptDao;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.util.SortUtil;

/**
 * Some helper method for categorization. Mostly to avoid cycles.
 * @author dieterwachters
 */
@Service
class CategorizationServiceHelperImpl extends AbstractService {
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private CategorizationTypeDao categorizationTypeDao;
	@Autowired
	private ConceptDao conceptDao;
	@Autowired
	private MeaningFactory meaningFactory;
	@Autowired
	private RepresentationService representationService;

	/**
	 * Cleanup the categories of the given categorization type.
	 * @param catType
	 */
	public void cleanupCategories(final CategorizationType catType) {
		final Collection<Category> categories = categoryDao.find(catType);
		for (Category category : categories) {
			Set<Term> terms = category.getTerms();
			if (terms.size() > 0) {
				// Create new object type for the category terms.
				for (Term term : terms) {
					ObjectType ot = meaningFactory.makeObjectType();
					ot.setGeneralConcept(category.getGeneralConcept());
					term.setObjectType(ot);
					term = representationService.saveTerm(term);
				}
			}

			// Remove the category meaning.
			removeInternal(category);
		}
	}

	public void cleanupSchemes(final Concept categorizedConcept) {
		// Remove the concept from the schemes.
		final Collection<Concept> schemes = conceptDao.findCategorizationSchemesForConcept(categorizedConcept);
		for (Concept scheme : schemes) {
			scheme.removeCategorizedConcept(categorizedConcept);
			conceptDao.save(scheme);
		}
	}

	/**
	 * Cleanup the categorization schemes for deletion of category
	 */
	public void cleanupSchemes(final Category category) {
		// From each of the scheme remove the category.
		final List<Concept> schemes = conceptDao.findCategorizationSchemes(category);
		for (Concept scheme : schemes) {
			scheme.removeCategory(category);
			conceptDao.save(scheme);
		}
	}

	public void removeInternal(Category category) {
		if (categoryDao.findById(category.getId()) == null) {
			return;
		}

		cleanupSchemes(category);

		// Save the category.
		getCurrentSession().delete(category);

		getCurrentSession().flush();
	}

	public List<Term> findCategoriesTermsForConcept(Concept concept) {
		List<Category> categories = categoryDao.findCategoriesForConcept(concept);

		List<Term> result = new LinkedList<Term>();
		for (Category c : categories) {
			Term t = representationService.findPreferredTerm(c);
			if (t != null) {
				result.add(t);
			}
		}

		return result;
	}

	public List<Term> findCategorizationTypeTermsForConcept(Concept concept) {
		List<Term> result = new ArrayList<Term>();

		List<CategorizationType> cTypes = categorizationTypeDao.findForConcept(concept);

		for (CategorizationType ct : cTypes) {
			Term t = representationService.findPreferredTerm(ct);
			if (t != null) {
				result.add(t);
			}
		}

		return (List<Term>) new SortUtil().sortRepresentations(result, true);
	}
}
