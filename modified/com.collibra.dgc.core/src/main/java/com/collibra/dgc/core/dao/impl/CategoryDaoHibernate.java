package com.collibra.dgc.core.dao.impl;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.CategoryDao;
import com.collibra.dgc.core.exceptions.ConstraintViolationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.categorizations.impl.CategoryImpl;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * 
 * @author amarnath
 * 
 */
@Service
public class CategoryDaoHibernate extends AbstractDaoHibernate<Category, CategoryImpl> implements CategoryDao {
	private static final Logger log = LoggerFactory.getLogger(CategoryDaoHibernate.class);

	@Autowired
	public CategoryDaoHibernate(SessionFactory sessionFactory) {
		super(Category.class, CategoryImpl.class, sessionFactory);
	}

	@Override
	public Category save(Category object) {
		if (object.getType() == null) {
			String message = "A category must have the ConceptType as the type.";
			log.error(message);
			throw new ConstraintViolationException(message, object.getId(), object.getClass().getName(),
					DGCErrorCodes.CATEGORY_NO_GENERAL_CONCEPT);
		}

		if (object.getGeneralConcept() == null) {
			String message = "A category must have the concept being classified as the general concept";
			log.error(message);
			throw new ConstraintViolationException(message, object.getId(), object.getClass().getName(),
					DGCErrorCodes.CATEGORY_NO_GENERAL_CONCEPT);
		}

		return super.save(object);
	}

	public List<Category> find(CategorizationType catType) {
		Criteria query = getSession().createCriteria(CategoryImpl.class);
		query.add(Expression.eq("type", catType));
		return query.list();
	}

	public List<Term> findCategoryTerms(CategorizationType catType, Collection<Vocabulary> vocabularies) {
		String queryString = "SELECT term FROM TermImpl term inner join"
				+ " term.objectType as ot where term.vocabulary in (:vocabularies) and" + " ot.type = :catType"
				+ " order by term.signifier DESC";

		Query query = getSession().createQuery(queryString);
		query.setParameter("catType", catType);
		query.setParameterList("vocabularies", vocabularies);
		query.setFlushMode(FlushMode.MANUAL);

		return query.list();
	}

	public List<Category> findCategoriesForConcept(Concept forConcept) {
		Criteria query = getSession().createCriteria(CategoryImpl.class);
		query.add(Expression.eq("generalConcept", forConcept));

		return query.list();
	}
}
