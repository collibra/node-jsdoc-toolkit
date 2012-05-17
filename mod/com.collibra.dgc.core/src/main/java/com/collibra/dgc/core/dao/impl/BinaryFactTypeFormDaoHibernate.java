package com.collibra.dgc.core.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.BinaryFactTypeFormDao;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.impl.BinaryFactTypeFormImpl;

@Service
public class BinaryFactTypeFormDaoHibernate extends AbstractDaoHibernate<BinaryFactTypeForm, BinaryFactTypeFormImpl>
		implements BinaryFactTypeFormDao {

	@Autowired
	public BinaryFactTypeFormDaoHibernate(SessionFactory sessionFactory) {
		super(BinaryFactTypeForm.class, BinaryFactTypeFormImpl.class, sessionFactory);
	}

	@Override
	public BinaryFactTypeForm findByRepresentation(Vocabulary vocabulary, Term headTerm, String roleExpression,
			String coRoleExpression, Term tailTerm) {
		return uniqueResult(criteria().add(Restrictions.eq("headTerm", headTerm))
				.add(Restrictions.eq("role", roleExpression)).add(Restrictions.eq("coRole", coRoleExpression))
				.add(Restrictions.eq("tailTerm", tailTerm)).add(Restrictions.eq("vocabulary", vocabulary)));
	}

	@Override
	public BinaryFactTypeForm findByExpression(Vocabulary vocabulary, String headTermSignifier, String roleExpression,
			String coRoleExpression, String tailTermSignifier) {
		String query = "select bftf from BinaryFactTypeFormImpl as bftf where bftf.vocabulary = :vocabulary and bftf.headTerm.signifier = :headSignifier and bftf.tailTerm.signifier = :tailSignifier and bftf.role = :role and bftf.coRole = :coRole order by bftf.id desc";

		Query q = getSession().createQuery(query);
		q.setParameter("vocabulary", vocabulary);
		q.setParameter("headSignifier", headTermSignifier);
		q.setParameter("tailSignifier", tailTermSignifier);
		q.setParameter("role", roleExpression);
		q.setParameter("coRole", coRoleExpression);
		q.setFlushMode(FlushMode.MANUAL);
		return (BinaryFactTypeForm) q.uniqueResult();

	}

	@Override
	public List<BinaryFactTypeForm> find(Term term) {
		String query = "from BinaryFactTypeFormImpl bftf where (bftf.headTerm = :term or bftf.tailTerm = :term) order by bftf.id desc";
		Query q = getSession().createQuery(query);
		q.setParameter("term", term);
		q.setFlushMode(FlushMode.MANUAL);
		return q.list();
	}

	@Override
	public BinaryFactTypeForm findPreferred(BinaryFactType binaryFactType, Vocabulary vocabulary) {
		return uniqueResult(criteria().add(Restrictions.eq("binaryFactType", binaryFactType))
				.add(Restrictions.eq("vocabulary", vocabulary)).add(Restrictions.eq("isPreferred", Boolean.TRUE)));
	}

	@Override
	public BinaryFactTypeForm find(Vocabulary vocabulary, Term headTerm, String role, String corole, Term tailTerm) {
		// search in both directions of bftf
		String query = "select bftf from BinaryFactTypeFormImpl as bftf where bftf.vocabulary = :vocabulary and ((bftf.headTerm = :head and bftf.tailTerm = :tail and bftf.role = :role and bftf.coRole = :coRole) or (bftf.headTerm = :tail and bftf.tailTerm = :head and bftf.role = :coRole and bftf.coRole = :role))";

		Query q = getSession().createQuery(query);
		q.setParameter("vocabulary", vocabulary);
		q.setParameter("head", headTerm);
		q.setParameter("tail", tailTerm);
		q.setParameter("role", role);
		q.setParameter("coRole", corole);
		q.setFlushMode(FlushMode.MANUAL);
		return (BinaryFactTypeForm) q.uniqueResult();

	}

	@Override
	public List<BinaryFactTypeForm> findWithHeadTerm(Term term) {
		return list(criteria().add(Restrictions.eq("headTerm", term)));
	}

	@Override
	public List<BinaryFactTypeForm> findWithTailTerm(Term term) {
		return list(criteria().add(Restrictions.eq("tailTerm", term)));
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BinaryFactTypeForm> searchByVerbalisation(Vocabulary vocabulary, String startsWith) {
		if (!startsWith.startsWith("%")) {
			startsWith = "%" + startsWith;
		}
		if (!startsWith.endsWith("%")) {
			startsWith = startsWith + "%";
		}
		try {
			String query = "select bftf from BinaryFactTypeFormImpl as bftf where bftf.vocabulary = :vocabulary and lower(bftf.headTerm.signifier) like :startsWith ";

			Query q = getSession().createQuery(query);
			q.setParameter("vocabulary", vocabulary);
			q.setParameter("startsWith", startsWith.toLowerCase());
			q.setFlushMode(FlushMode.MANUAL);
			return q.list();

		} catch (HibernateException e) {
			System.out.println("he: " + e);
		}
		return new ArrayList<BinaryFactTypeForm>();

	}

	@Override
	public List<BinaryFactTypeForm> findSynonymousForms(BinaryFactTypeForm bftf) {
		Query query = getSession()
				.createQuery(
						"from BinaryFactTypeFormImpl binaryFactTypeForm where binaryFactTypeForm <> :bftf and binaryFactTypeForm.binaryFactType = :meaning");
		query.setParameter("bftf", bftf);
		query.setParameter("meaning", bftf.getBinaryFactType());
		return list(query);
	}

	@Override
	public List<BinaryFactTypeForm> find(Vocabulary vocabulary) {
		return list(criteria().add(Restrictions.eq("vocabulary", vocabulary)));
	}

	@Override
	public BinaryFactTypeForm save(BinaryFactTypeForm newObject) {
		return super.save(newObject);
	}

	@Override
	public List<BinaryFactTypeForm> find(Term headTerm, String role, String corole, Term tailTerm) {
		// search in both directions of bftf
		String query = "select bftf from BinaryFactTypeFormImpl as bftf where ((bftf.headTerm = :head and bftf.tailTerm = :tail and bftf.role = :role and bftf.coRole = :coRole) or (bftf.headTerm = :tail and bftf.tailTerm = :head and bftf.role = :coRole and bftf.coRole = :role))";
		Query q = getSession().createQuery(query);
		q.setParameter("head", headTerm);
		q.setParameter("tail", tailTerm);
		q.setParameter("role", role);
		q.setParameter("coRole", corole);
		q.setFlushMode(FlushMode.MANUAL);
		return q.list();
	}

	@Override
	public List<BinaryFactTypeForm> find(Term headTerm, Term tailTerm) {
		// search in both directions of bftf
		String query = "select bftf from BinaryFactTypeFormImpl as bftf where ((bftf.headTerm = :head and bftf.tailTerm = :tail) or (bftf.headTerm = :tail and bftf.tailTerm = :head))";
		Query q = getSession().createQuery(query);
		q.setParameter("head", headTerm);
		q.setParameter("tail", tailTerm);
		q.setFlushMode(FlushMode.MANUAL);
		return q.list();
	}

	@Override
	public List<BinaryFactTypeForm> find(Concept specializedHeadConcept, Concept specializedTailConcept) {
		Query query = getSession()
				.createQuery(
						"from BinaryFactTypeFormImpl as bftf where ((bftf.headTerm.objectType.id = :hcRID and bftf.tailTerm.objectType.id = :tcRID) or (bftf.headTerm.objectType.id = :tcRID and bftf.tailTerm.objectType.id = :hcRID))");
		query.setParameter("hcRID", specializedHeadConcept.getId());
		query.setParameter("tcRID", specializedTailConcept.getId());
		query.setFlushMode(FlushMode.MANUAL);
		List result = query.list();
		return result;
	}

	@Override
	public Map<String, Long> findNumberOfBinaryFactTypesPerTerm() {
		StringBuilder sb = new StringBuilder();
		/**
		 * This is the only way I have found to let terms be counted in BFTFs. It consists of of 2 subqueries that look
		 * up the direct generalization with that of a term and count their occurrences. Since you cannot name your
		 * counts (or it ignores the names, some hacking needed to be done. col_1_0_ means column 1 in table 0 and and
		 * col_2_0_ means column 2 in table 0, thrown in a group by et voila.
		 */
		sb.append("select term.signifier, (select count(*) from BinaryFactTypeFormImpl bftfHead where bftfHead.headTerm = term) as count1, (select count(*) from BinaryFactTypeFormImpl bftfTail where bftfTail.tailTerm = term) as count2 from TermImpl term group by term.id, term.signifier");
		Query query = getSession().createQuery(sb.toString());
		query.setFlushMode(FlushMode.MANUAL);
		return countTerms(query);
	}

	@Override
	public Map<String, Long> findNumberOfBinaryFactTypesPerTerm(Vocabulary vocabulary) {
		StringBuilder sb = new StringBuilder();

		/** @see #findNumberOfBinaryFactTypesPerTerm() **/
		sb.append("select term.signifier, (select count(*) from BinaryFactTypeFormImpl bftfHead where bftfHead.headTerm = term) as count1, (select count(*) from BinaryFactTypeFormImpl bftfTail where bftfTail.tailTerm = term) as count2 from TermImpl term where term.vocabulary = :vocabulary group by term.id, term.signifier");
		Query query = getSession().createQuery(sb.toString());
		query.setParameter("vocabulary", vocabulary);
		query.setFlushMode(FlushMode.MANUAL);
		return countTerms(query);
	}

	@Override
	public Map<String, Long> findNumberOfBinaryFactTypesPerTerm(Community community) {
		StringBuilder sb = new StringBuilder();
		/** @see #findNumberOfBinaryFactTypesPerTerm() **/
		sb.append("select term.signifier, (select count(*) from BinaryFactTypeFormImpl bftfHead where bftfHead.headTerm = term) as count1, (select count(*) from BinaryFactTypeFormImpl bftfTail where bftfTail.tailTerm = term) from TermImpl term where term.vocabulary.community = :community group by term.id, term.signifier");
		Query query = getSession().createQuery(sb.toString());
		query.setParameter("community", community);
		query.setFlushMode(FlushMode.MANUAL);
		return countTerms(query);
	}

	private Map<String, Long> countTerms(Query query) {
		List<Object[]> result = query.list();
		Map<String, Long> resultMap = new HashMap<String, Long>();

		for (Object[] tuple : result) {
			String term = (String) tuple[0];
			Long occurrence1 = tuple[1] == null ? 0L : (Long) tuple[1];
			Long occurrence2 = tuple[2] == null ? 0L : (Long) tuple[2];
			Long occurrences = occurrence1 + occurrence2;
			if (occurrences > 0) {
				if (resultMap.containsKey(term)) {
					occurrences += resultMap.get(term);
				}
				resultMap.put(term, occurrences);
			}
		}
		return sortByValue(resultMap);
	}

	/**
	 * Sorts a map by its values
	 */
	private Map sortByValue(Map map) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
