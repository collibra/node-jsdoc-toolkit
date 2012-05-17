package com.collibra.dgc.core.util;

import org.apache.commons.collections.map.MultiValueMap;

import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.impl.ServiceUtility;

/**
 * Utility methods for Categorization feature.
 * @author amarnath
 * 
 */
public class CategorizationUtil {
	/**
	 * Get {@link MultiValueMap} of {@link CategorizationType} to {@link Category}s in the given {@link Vocabulary} for
	 * the specified Categorization Scheme.
	 * @param categorizationScheme The categorization scheme.
	 * @param vocabulary The {@link Vocabulary}.
	 * @return {@link MultiValueMap} of {@link CategorizationType} to {@link Category}s.
	 */
	public static MultiValueMap getCategorizationTypeToCategory(Concept categorizationScheme, Vocabulary vocabulary) {
		MultiValueMap catTypeToCategory = new MultiValueMap();
		for (Category category : categorizationScheme.getCategories()) {
			Term catTypeTerm = vocabulary.getPreferredTerm(category.getType());
			if (catTypeTerm != null) {
				CategorizationType catType = (CategorizationType) ServiceUtility.deproxy(catTypeTerm.getObjectType(),
						ObjectType.class);
				catTypeToCategory.put(catType, category);
			}
		}
		return catTypeToCategory;
	}
}
