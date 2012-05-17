package com.collibra.dgc.core.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.TermDao;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.impl.TermImpl;
import com.collibra.dgc.core.model.user.Member;

@Service
public class TermDaoHibernate extends AbstractDaoHibernate<Term, TermImpl> implements TermDao {

	@Autowired
	public TermDaoHibernate(SessionFactory sessionFactory) {
		super(Term.class, TermImpl.class, sessionFactory);
	}

	// can't figure out why I can't combine the scalar query...
	@Override
	public String findTermResourceIdBySignifier(Vocabulary vocabulary, String signifier) {
		Query query = getSession().createQuery(
				"select term.id from TermImpl term where term.vocabulary = :voc and term.signifier = :signifier");
		query.setParameter("voc", vocabulary);
		query.setParameter("signifier", signifier);
		System.err
				.println("Looking for terms with signifier '" + signifier + "' in voc '" + vocabulary.getName() + "'");
		return (String) query.uniqueResult();
	}

	@Override
	public Term findTermBySignifier(Vocabulary vocabulary, String signifier) {
		Query query = getSession().createQuery(
				"from TermImpl term where term.vocabulary = :voc and term.signifier = :signifier");
		query.setParameter("signifier", signifier);
		query.setParameter("voc", vocabulary);

		return uniqueResult(query);
	}

	@Override
	public List<Term> findAllTermsBySignifier(String expression, boolean capitalSensitive) {
		Query query;
		if (!capitalSensitive) {
			query = getSession().createQuery(
					"from TermImpl term where lower(term.signifier) = :signifier order by term.signifier");
			query.setParameter("signifier", expression.toLowerCase());
		} else {
			query = getSession().createQuery(
					"from TermImpl term where term.signifier = :signifier order by term.signifier");
			query.setParameter("signifier", expression);
		}

		return list(query);
	}

	@Override
	public List<Term> searchTermsBySignifier(String expression, int offset, int number) {
		String searchExpression = expression;

		if (!searchExpression.startsWith("%")) {
			searchExpression = "%" + searchExpression;
		}

		if (!searchExpression.endsWith("%")) {
			searchExpression = searchExpression + "%";
		}

		Query query = getSession().createQuery(
				"from TermImpl term where lower(term.signifier) like :expression order by term.signifier ");
		query.setParameter("expression", searchExpression.toLowerCase());

		if (offset > 0) {
			query.setFirstResult(offset);
		}

		if (number > 0) {
			query.setMaxResults(number);
		}

		return list(query);
	}

	@Override
	public Term findPreferredTerm(ObjectType objectType, Vocabulary vocabulary) {
		Query query = getSession()
				.createQuery(
						"from TermImpl term where term.objectType = :ot and term.vocabulary = :vocabulary and term.isPreferred = 1");
		query.setFlushMode(FlushMode.MANUAL);
		query.setParameter("ot", objectType);
		query.setParameter("vocabulary", vocabulary);
		return (Term) query.uniqueResult();
	}

	@Override
	public List<Term> searchTermsBySignifier(Vocabulary vocabulary, String partialSignifier) {

		List<Vocabulary> vocs = new ArrayList<Vocabulary>(1);
		vocs.add(vocabulary);

		return searchTermsBySignifier(vocs, partialSignifier);
	}

	@Override
	public List<Term> searchTermsBySignifier(Collection<Vocabulary> vocs, String partialSignifier) {

		if (!partialSignifier.endsWith("%")) {
			partialSignifier = partialSignifier + "%";
		}

		Query query = getSession().createQuery(
				"from TermImpl term where term.vocabulary in (:vocs) and lower(term.signifier) like :signifier");
		query.setParameter("signifier", partialSignifier.toLowerCase());
		query.setParameterList("vocs", vocs);

		return list(query);
	}

	@Override
	public List<Term> searchTermInAllNonSBVRIncorporatedVocabularies(Vocabulary vocabulary, String partialSignifier) {
		Set<Vocabulary> vocs = vocabulary.getAllNonSbvrIncorporatedVocabularies();
		vocs.add(vocabulary);

		Criteria crit = criteria().add(Restrictions.ilike("signifier", partialSignifier, MatchMode.ANYWHERE))
				.add(Restrictions.in("vocabulary", vocs)).addOrder(Order.desc("id"));

		return list(crit);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Term> searchTermsBySignifier(Vocabulary voc, String partialSignifier, ObjectType type) {
		Query query = getSession()
				.createQuery(
						"select term from TermImpl where term.signifier like :partialSignifier and vocabulary = :voc and term.objectType.type = :type");
		query.setParameter("partialSignifier", partialSignifier);
		query.setParameter("voc", voc);
		query.setParameter("type", type);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Term> searchTermInAllNonSBVRIncorporatedVocabularies(Vocabulary voc, String partialSignifier,
			ObjectType type) {
		Query query = getSession()
				.createQuery(
						"select term from TermImpl where term.signifier like :partialSignifier and vocabulary = :voc and term.objectType.type = :type");
		query.setParameter("partialSignifier", partialSignifier);
		query.setParameter("voc", voc);
		query.setParameter("type", type);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();

	}

	@Override
	public List<Term> findSynonyms(Term term) {
		Query query = getSession().createQuery(
				"from TermImpl term where term.objectType = :meaning and term <> :resource order by term.signifier");
		query.setParameter("meaning", term.getObjectType());
		query.setParameter("resource", term);
		query.setFlushMode(FlushMode.MANUAL);
		return list(query);
	}

	@Override
	public Term save(Term newTerm) {
		return super.save(newTerm);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Term> findPureTermsByVocabulary(Vocabulary vocabulary) {
		Query query = getSession()
				.createQuery(
						"select term from TermImpl term where term.vocabulary = :vocabulary and term.objectType.type.id not in (:typeList) order by term.signifier");

		List<String> types = new ArrayList<String>();
		// types.add(MeaningConstants.META_CATEGORIZATION_SCHEME_UUID); NOTE: Schemes are deprecated.
		types.add(MeaningConstants.META_CATEGORIZATION_TYPE_UUID);
		types.add(MeaningConstants.META_CATEGORY_UUID);
		query.setParameterList("typeList", types);
		query.setParameter("vocabulary", vocabulary);
		return query.list();

	}

	@Override
	public List<Term> findTermsByVocabularies(Collection<Vocabulary> vocs) {
		return list(criteria().add(Restrictions.in("vocabulary", vocs)).addOrder(Order.asc("signifier")));
	}

	@Override
	public List<Term> findTermsByVocabulary(Vocabulary vocabulary) {
		return list(criteria().add(Restrictions.eq("vocabulary", vocabulary)).addOrder(Order.asc("signifier")));
	}

	@Override
	public List<Term> findTermsByObjectType(ObjectType oType) {
		return list(criteria().add(Restrictions.eq("objectType", oType)).addOrder(Order.asc("id")));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Term> getSignifierToTermMap(Vocabulary vocabulary) {
		Map<String, Term> mapping = new HashMap<String, Term>();
		Query query = getSession().createQuery(
				"select term.signifier, term from TermImpl term where term.vocabulary = :vocabulary");
		query.setParameter("vocabulary", vocabulary);
		Iterator<Object[]> result = query.iterate();
		while (result.hasNext()) {
			Object[] tuple = result.next();
			String signifier = (String) tuple[0];
			Term term = (Term) tuple[1];
			mapping.put(signifier, term);
		}
		return mapping;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Term> getSignifierToTermMap(Collection<Vocabulary> vocabularies) {
		Map<String, Term> mapping = new HashMap<String, Term>();
		Query query = getSession().createQuery(
				"select term.signifier, term from TermImpl term where term.vocabulary in (:vocabularies)");
		query.setParameterList("vocabularies", vocabularies);
		Iterator<Object[]> result = query.iterate();
		while (result.hasNext()) {
			Object[] tuple = result.next();
			String signifier = (String) tuple[0];
			Term term = (Term) tuple[1];
			mapping.put(signifier, term);
		}
		return mapping;
	}

	@Override
	public Long findTermIdBySignifier(Vocabulary vocabulary, String signifier) {
		Query query = getSession().createQuery(
				"term.id from TermImpl term where term.vocabulary = :voc and term.signifier = :signifier");
		query.setParameter("signifier", signifier);
		query.setParameter("voc", vocabulary);

		return (Long) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Term> findTermsByType(ObjectType type) {
		Query query = getSession().createQuery("select term from TermImpl term where term.objectType.type = :type");
		query.setParameter("type", type);
		query.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Term, Integer> findMembersCount(String roleName) {
		String queryString = "select mem from MemberImpl mem where mem.role.term.signifier = ? and mem.resourceId in (select term.id from TermImpl term)";
		Query query = getSession().createQuery(queryString);
		query.setParameter(0, roleName);
		return findMembersCountForRepresentation(query.list());
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Term, Integer> findMembersCount(Vocabulary vocabulary) {
		String queryString = "select mem from MemberImpl mem where mem.resourceId in (select term.id from TermImpl term where term.vocabulary = ?)";
		Query query = getSession().createQuery(queryString);
		query.setParameter(0, vocabulary);
		return findMembersCountForRepresentation(query.list());
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Term, Integer> findMembersCount(Vocabulary vocabulary, String roleName) {
		String queryString = "select mem from MemberImpl mem where mem.resourceId in (select term.id from TermImpl term where mem.role.term.signifier = ? and term.vocabulary = ?)";
		Query query = getSession().createQuery(queryString);
		query.setParameter(0, roleName);
		query.setParameter(1, vocabulary);
		return findMembersCountForRepresentation(query.list());
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Term, Integer> findMembersCount() {
		String queryString = "select mem from MemberImpl mem where mem.resourceId in (select term.id from TermImpl term)";
		Query query = getSession().createQuery(queryString);
		return findMembersCountForRepresentation(query.list());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> findNumberOfSpecializedConceptsPerTerm(Vocabulary vocabulary) {
		/**
		 * This is the only way I have to have a subselect count query be order and filtered since hql does not give a
		 * damn about the name of a count or a subquery. So I found out I could just say query 1 column 0 col_1_0_.
		 */
		Query query = getSession()
				.createQuery(
						"select term.signifier, (select count(*) from ConceptImpl concept where concept.generalConcept = term.objectType) from TermImpl term where term.vocabulary = :vocabulary");
		query.setParameter("vocabulary", vocabulary);
		final List<Object[]> ret = query.list();
		final List<Object[]> toRemove = new ArrayList<Object[]>();
		for (final Object[] o : ret) {
			if (((Number) o[1]).longValue() == 0) {
				toRemove.add(o);
			}
		}
		ret.removeAll(toRemove);

		Collections.sort(ret, new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
				return ((Number) o2[1]).intValue() - ((Number) o1[1]).intValue();
			}
		});

		return ret;
	}

	@Override
	public Collection<Term> findTermsWithoutConceptType() {
		Query query = getSession().createQuery(
				"select term from TermImpl term, VocabularyImpl vocabulary where " + " term.objectType.type is null "
						+ " and term.vocabulary = vocabulary");
		return list(query);
	}

	@Override
	public Collection<Term> findTermsWithoutMeaning() {
		Query query = getSession().createQuery(
				"select term from TermImpl term, VocabularyImpl vocabulary where term.vocabulary = vocabulary "
						+ " and term.objectType is null " + " order by term.signifier");
		return list(query);
	}

	@Override
	public Collection<Term> findTermsWithNoMeaning() {
		Query query = getSession().createQuery(
				"select distinct term from TermImpl term, MeaningImpl meaning where term.objectType = meaning "
						+ " order by term.signifier");
		return list(query);
	}

	private Map<Term, Integer> findMembersCountForRepresentation(List<Member> list) {
		final Map<Term, Integer> result = new HashMap<Term, Integer>();

		for (Member member : list) {
			final Term term = findById(member.getResourceId());
			int count = 0;
			if (result.containsKey(term)) {
				count = result.get(term);
			}

			result.put(term, ++count);
		}

		return result;
	}
}
