package com.collibra.dgc.core.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.dao.ObjectTypeDao;
import com.collibra.dgc.core.dao.RepresentationDao;
import com.collibra.dgc.core.dao.VocabularyDao;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.meaning.impl.ConceptImpl;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Designation;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.representation.impl.DesignationImpl;
import com.collibra.dgc.core.model.representation.impl.RepresentationImpl;
import com.collibra.dgc.core.model.rules.RuleStatement;

/**
 * Hibernate DAO class for {@link Representation}.
 * <p>
 * @author amarnath
 * 
 */
@Service
public class RepresentationDaoHibernate extends AbstractDaoHibernate<Representation, RepresentationImpl> implements
		RepresentationDao {
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// NOTE: Oracle - ORA-00979: Not a GROUP BY Expression Tips
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Cause: The GROUP BY clause does not contain all the expressions in the SELECT clause.
	// SELECT expressions that are not included in a group function, such as AVG, COUNT, MAX, MIN, SUM,
	// STDDEV, or VARIANCE, must be listed in the GROUP BY clause.
	// Action: Include in the GROUP BY clause all SELECT expressions that are not group function arguments.
	// References: http://www.dba-oracle.com/t_ora_00979_not_a_group_by_expression.htm
	// http://www.techonthenet.com/oracle/errors/ora00979.php
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Autowired
	private ObjectTypeDao objectTypeDao;
	@Autowired
	private VocabularyDao vocabularyDao;

	@Autowired
	public RepresentationDaoHibernate(SessionFactory sessionFactory) {
		super(Representation.class, RepresentationImpl.class, sessionFactory);
	}

	public Representation findRepresentationByExpression(Vocabulary vocabulary, String expression) {
		return uniqueResult(criteria().add(Restrictions.eq("stringValue", expression)).add(
				Restrictions.eq("vocabulary", vocabulary)));
	}

	public List<Representation> findAllRepresentations() {
		Query query = getSession().createQuery("from RepresentationImpl representation");
		return list(query);
	}

	private String getMeaningField(Representation representation) {
		if (representation instanceof Term) {
			return "objectType";
		} else if (representation instanceof BinaryFactTypeForm) {
			return "binaryFactType";
		} else if (representation instanceof CharacteristicForm) {
			return "characteristic";
		} else if (representation instanceof RuleStatement) {
			return "rule";
		}
		return "meaning";
	}

	@Override
	public List<Representation> findSpecializedConceptRepresentations(Representation representation) {
		// Exclude the attributes from this list.
		Meaning meaning = representation.getMeaning();
		String queryString = "from RepresentationImpl rep where rep." + getMeaningField(representation)
				+ " in (from ConceptImpl concept where concept.generalConcept = ?)";

		Query query = getSession().createQuery(queryString);
		query.setParameter(0, meaning);

		return query.list();

		// List<Concept> subMeanings = getSession().createCriteria(ConceptImpl.class)
		// .add(Restrictions.eq("generalConcept", meaning)).setFlushMode(FlushMode.MANUAL).list();
		// if (subMeanings == null || subMeanings.isEmpty()) {
		// return null;
		// }
		//
		// // Find all attributes to exclude.
		// List<Attribute> attributes = getSession().createCriteria(AttributeImpl.class)
		// .add(Restrictions.in("meaning", subMeanings)).setFlushMode(FlushMode.MANUAL).list();
		// List<Long> idsOfAttributes = new ArrayList<Long>(attributes.size());
		// for (Attribute att : attributes) {
		// idsOfAttributes.add(att.getId());
		// }
		//
		// return list(criteria()
		// .add(Restrictions.ne("id", representation.getResourceId()))
		// .add(Restrictions.in("meaning", subMeanings))
		// .add(Restrictions.not(Restrictions.in("id", idsOfAttributes))));
	}

	@Override
	public List<Representation> findSpecializedConceptRepresentationsInSameVocabulary(Representation representation) {
		// FIXME this should be optimized to one query instead of storing intermediate collections
		Meaning meaning = representation.getMeaning();
		Vocabulary vocabulary = representation.getVocabulary();
		List<Concept> subMeanings = getSession().createCriteria(ConceptImpl.class)
				.add(Restrictions.eq("generalConcept", meaning)).setFlushMode(FlushMode.MANUAL).list();
		if (subMeanings.isEmpty()) {
			return null;
		}

		// Doing this manually as it is too complicated to do this with a query due to our model structure.
		// Specially, there is no general field name 'meaning' as every Representation has its own.
		final List<Representation> result = new ArrayList<Representation>();
		for (final Concept c : subMeanings) {
			final Set<? extends Representation> reps = c.getRepresentations();
			for (final Representation rep : reps) {
				if (rep.getVocabulary().equals(vocabulary) && !result.contains(rep)) {
					result.add(rep);
				}
			}
		}
		return result;

		// return list(criteria().add(Restrictions.in("meaning", subMeanings)).add(
		// Restrictions.eq("vocabulary", vocabulary)));
	}

	@Override
	public Representation findPreferredRepresentation(Concept concept, Vocabulary vocabulary) {
		// Doing this manually as it is too complicated to do this with a query due to our model structure.
		final Set<? extends Representation> reps = concept.getRepresentations();
		Representation best = null;
		for (final Representation rep : reps) {
			if (rep.getIsPreferred() && rep.getVocabulary().equals(vocabulary)) {
				return rep;
			} else if (rep.getIsPreferred()) {
				best = rep;
			}
		}
		return best;
	}

	@Override
	public List<Representation> findSpecializedRepresentations(Representation representation, int limit) {
		Query query = getSession()
				.createQuery(
						"from RepresentationImpl rep where rep.isPreferred = 1 and rep."
								+ getMeaningField(representation)
								+ ".id in "
								+ "(select concept.id from ConceptImpl concept where concept.generalConcept = :generalConcept)");

		query.setParameter("generalConcept", representation.getMeaning());
		query.setFlushMode(FlushMode.MANUAL);
		if (limit > 0) {
			query.setMaxResults(limit);
		}

		return list(query);
	}

	@Override
	public List<Representation> findSynonyms(Representation representation) {
		Query query = getSession().createQuery(
				"from RepresentationImpl representation where representation <> :rep and representation."
						+ getMeaningField(representation) + " = :meaning");
		query.setParameter("rep", representation);
		query.setParameter("meaning", representation.getMeaning());
		return list(query);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Representation> find(Attribute attribute) {
		Criteria query = getSession().createCriteria(Representation.class);
		query.setFlushMode(FlushMode.MANUAL);
		Criteria subquery = query.createCriteria("attributes", "att");
		subquery.setFlushMode(FlushMode.MANUAL);
		subquery.add(Restrictions.eq("att.id", attribute.getId()));
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Designation> searchInDesignationSignifiers(String signifier) {

		Criteria crit = getSession().createCriteria(DesignationImpl.class)
				.add(Restrictions.ilike("signifier", "%" + signifier + "%")).addOrder(Order.desc("id"));

		crit.setFlushMode(FlushMode.MANUAL);

		return crit.list();
	}

	@Override
	public Collection<Representation> findByVocabulary(Vocabulary voc) {
		Query query = getSession().createQuery(
				"from RepresentationImpl representation where representation.vocabulary = :voc");
		query.setParameter("voc", voc);
		return list(query);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Term> findStatusTerms() {
		String query = "select term from TermImpl term where term.vocabulary.uri='" + Constants.STATUSES_VOCABULARY_URI
				+ "' and term.objectType.type.id='" + MeaningConstants.META_STATUS_TYPE_UUID
				+ "' order by term.signifier";
		return getSession().createQuery(query).setReadOnly(true).list();
	}

	@Override
	public List<Representation> findAllWithoutVocabulary() {
		String query = "select rep from RepresentationImpl rep where rep.vocabulary is null "
				+ " and rep.class in (TermImpl, BinaryFactTypeFormImpl, CharacteristicFormImpl, NameImpl) ";
		return getSession().createQuery(query).setReadOnly(true).list();
	}

	@Override
	public MultiValueMap findRepresentationsWithoutAttributes() {
		Query query = getSession().createQuery(
				"select representation, att from RepresentationImpl representation, AttributeImpl att where "
						+ " att.owner.id = representation.id ");
		MultiValueMap repToAttributes = new MultiValueMap();
		for (Object object : query.list()) {
			Object[] tuple = (Object[]) object;
			repToAttributes.put(tuple[0], tuple[1]);
		}
		return repToAttributes;
	}

	private Map<Representation, Long> findRepresentationToCount(Query query) {
		Map<Representation, Long> representationToCount = new HashMap<Representation, Long>();
		for (Object object : query.list()) {
			Object[] tuple = (Object[]) object;
			Representation rep = findById((String) tuple[0]);
			representationToCount.put(rep, (Long) tuple[1]);
		}

		return representationToCount;
	}

	// @Override
	// public boolean isPersisted(final Representation representation) {
	//
	// Query query = getSession().createQuery(
	// "SELECT representation.id from RepresentationImpl representation where representation = :rep");
	// query.setParameter("rep", representation);
	// String result = (String) query.uniqueResult();
	//
	// return result != null;
	// }
}