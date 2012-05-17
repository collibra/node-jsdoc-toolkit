package com.collibra.dgc.core.model.categorizations.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.model.categorizations.CategorizationFactory;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.impl.ConceptImpl;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.impl.ConstraintChecker;

/**
 * The services that take care of persisting the categorization scheme objects need to make sure that they also persist
 * the terms created implicitly.
 * @author dtrog
 * 
 */
@Service
public class CategorizationFactoryImpl implements CategorizationFactory {
	private static final Logger log = LoggerFactory.getLogger(CategorizationFactoryImpl.class);

	@Autowired
	private RepresentationFactory representationFactory;

	@Autowired
	ConstraintChecker constraintChecker;

	@Override
	public CategorizationType createCategorizationType(Vocabulary vocabulary, String signifier, ObjectType forConcept) {

		CategorizationType categorizationType = new CategorizationTypeImpl(forConcept);
		representationFactory.makeTerm(vocabulary, signifier, categorizationType);

		return categorizationType;
	}

	@Override
	public CategorizationType createCategorizationType(ObjectType ot, ObjectType forConcept) {
		return new CategorizationTypeImpl(ot, forConcept, UUID.randomUUID().toString());
	}

	@Override
	public Concept createCategorizationScheme(Concept concept, Vocabulary vocabulary, String name) {

		Term catSchemeTerm = representationFactory.makeTerm(vocabulary, name);
		ObjectType catScheme = catSchemeTerm.getObjectType();
		((ConceptImpl) catScheme).setCategorizationScheme(true);
		catScheme.addCategorizedConcept(concept);
		return catScheme;
	}

	@Override
	public Category createCategory(String name, Concept catScheme, CategorizationType catType, Vocabulary vocabulary) {

		// Create the category meaning.
		Category category = createCategory(name, catType, vocabulary);

		// Add the category to scheme.
		catScheme.addCategory(category);
		return category;
	}

	@Override
	public Category createCategory(String name, CategorizationType catType, Vocabulary vocabulary) {
		// Create the category meaning.
		Category category = new CategoryImpl(catType);

		// Create term for category.
		representationFactory.makeTerm(vocabulary, name, category);
		return category;
	}

	@Override
	public Category createCategoryForConcept(String name, Concept forConcept, CategorizationType catType,
			Vocabulary vocabulary) {
		// Create category with same concept as that of categorization type. However override the general concept by the
		// concept being classified. Note that the categorization type's classifying concept must be taxonomical parent
		// of the for concept.
		Category category = createCategory(name, catType, vocabulary);
		category.setGeneralConcept(forConcept);
		return category;
	}

	@Override
	public Category createCategoryForConcept(String name, Concept forConcept, Concept catScheme,
			CategorizationType catType, Vocabulary vocabulary) {

		// Create category with same concept as that of categorization type. However override the general concept by the
		// concept being classified. Note that the categorization type's classifying concept must be taxonomical parent
		// of the for concept.
		Category category = createCategory(name, catScheme, catType, vocabulary);
		category.setGeneralConcept(forConcept);
		return category;
	}

	@Override
	public Category createCategory(ObjectType ot, Concept catScheme, CategorizationType catType) {
		Category category = createCategory(ot, catType);

		// Add the category to scheme.
		catScheme.addCategory(category);
		return category;
	}

	@Override
	public Category createCategory(ObjectType ot, CategorizationType catType) {

		return new CategoryImpl(ot, catType, UUID.randomUUID().toString());
	}
}
