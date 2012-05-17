package com.collibra.dgc.core.dao.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.map.MultiValueMap;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.dao.AttributeDao;
import com.collibra.dgc.core.dao.VocabularyDao;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.representation.impl.VocabularyImpl;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.user.Role;

@Service
public class VocabularyDaoHibernate extends AbstractDaoHibernate<Vocabulary, VocabularyImpl> implements VocabularyDao {

	private static final Logger log = LoggerFactory.getLogger(VocabularyDaoHibernate.class);

	@Autowired
	private AttributeDao attributeDao;

	@Autowired
	public VocabularyDaoHibernate(SessionFactory sessionFactory) {
		super(Vocabulary.class, VocabularyImpl.class, sessionFactory);
	}

	@Override
	public List<Vocabulary> findAll() {
		return list(criteria().addOrder(Order.asc("name")));
	}

	@Override
	public Vocabulary findByVocabularyUri(String uri) {
		return uniqueResult(criteria().add(Restrictions.eq("uri", uri)));
	}

	@Override
	public List<Vocabulary> findVocabulariesContainingTerm(Term term) {
		return list(criteria().createCriteria("terms").add(Restrictions.eq("id", term.getId()))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public List<Vocabulary> findVocabulariesContainingBinaryFactTypeForm(BinaryFactTypeForm binaryFactTypeForm) {
		return list(criteria().createCriteria("binaryFactTypeForms").add( //$NON-NLS-1$
				Restrictions.eq("id", binaryFactTypeForm.getId()))); //$NON-NLS-1$
	}

	@Override
	public List<Vocabulary> findVocabulariesContainingCharacteristicForm(CharacteristicForm characteristicForm) {
		return list(criteria().createCriteria("characteristicForms").add( //$NON-NLS-1$
				Restrictions.eq("id", characteristicForm.getId()))); //$NON-NLS-1$
	}

	@Override
	public List<Vocabulary> findVocabulariesContainingAttribute(Attribute attribute) {
		return list(criteria().createCriteria("attributes").add( //$NON-NLS-1$
				Restrictions.eq("id", attribute.getId()))); //$NON-NLS-1$
	}

	@Override
	public List<Vocabulary> findVocabulariesByMember(String ownerId, Role role, Integer start, Integer end) {
		String queryString = "SELECT voc FROM VocabularyImpl as voc, MemberImpl as memb WHERE "
				+ "memb.ownerId = :ownerId AND voc.id = memb.resourceId AND memb.role = :role "
				+ "order by voc.name DESC";

		Query query = getSession().createQuery(queryString);

		// set parameters
		query.setParameter("ownerId", ownerId);
		query.setParameter("role", role);

		// set limit
		if (end != 0 || (start > end) || (end != null && start != null)) {
			query.setMaxResults(end - start);
			query.setFirstResult(start);
		}

		// config query for performance
		query.setReadOnly(true);
		query.setCacheable(true);

		return query.list();
	}

	@Override
	public List<Vocabulary> findVocabulariesByMember(String username, Integer start, Integer end) {
		String queryString = "SELECT voc FROM VocabularyImpl as voc, MemberImpl as memb WHERE "
				+ "memb.name = :memberName AND voc.id = memb.resourceId order by voc.name DESC";

		Query query = getSession().createQuery(queryString);

		// set parameters
		query.setParameter("memberName", username);

		// set limit
		if (end != 0 || (start > end) || (end != null && start != null)) {
			query.setMaxResults(end - start);
			query.setFirstResult(start);
		}

		// config query for performance
		query.setReadOnly(true);
		query.setCacheable(true);

		return query.list();
	}

	@Override
	public List<Vocabulary> find(RuleSet ruleSet) {
		Criteria query = getSession().createCriteria(VocabularyImpl.class);
		Criteria subquery = query.createCriteria("ruleSets", "ruleSet"); //$NON-NLS-1$ //$NON-NLS-2$
		subquery.add(Restrictions.and(Restrictions.eq("ruleSet.id", ruleSet.getId()), Restrictions //$NON-NLS-1$
				.eq("ruleSet.id", ruleSet.getId()))); //$NON-NLS-1$
		query.setFlushMode(FlushMode.MANUAL);
		subquery.setFlushMode(FlushMode.MANUAL);
		return query.list();
	}

	@Override
	public List<String> getTermsWithChildren(List<String> termResourceIds) {
		StringBuilder queryString = new StringBuilder(
				"select term.id from TermImpl term where ( exists (select 1 from ObjectTypeImpl ot where (ot.generalConcept = term.objectType)))");

		if (termResourceIds != null)
			queryString.append(" and term.id in (:termResourceIds)");

		Query query;
		query = getSession().createQuery(queryString.toString());

		if (termResourceIds != null)
			query.setParameterList("termResourceIds", termResourceIds);

		query.setReadOnly(true);
		query.setCacheable(true);

		return query.list();
	}

	@Override
	public List findAllVocabularyNames() {
		List result;

		StringBuilder queryString;
		queryString = new StringBuilder("select" + " voc.id, voc.name " + " from VocabularyImpl voc");

		Query query = getSession().createQuery(queryString.toString());

		query.setReadOnly(true);
		query.setCacheable(true);

		return query.list();
	}

	@Override
	public List<Vocabulary> findAllIncorporatingVocabularies(Vocabulary vocabulary) {
		Criteria query = getSession().createCriteria(VocabularyImpl.class);
		Criteria subquery = query.createCriteria("incorporatedVocabularies", "incpVoc"); //$NON-NLS-1$ //$NON-NLS-2$
		query.setFlushMode(FlushMode.MANUAL);
		subquery.setFlushMode(FlushMode.MANUAL);
		subquery.add(Expression.eq("incpVoc.id", vocabulary.getId())); //$NON-NLS-1$
		List<Vocabulary> vocabularies = query.list();
		List<Vocabulary> result = new LinkedList<Vocabulary>();
		for (Vocabulary temp : vocabularies) {
			result.add(temp);
		}

		return result;
	}

	@Override
	public List<String> findMetaVocabularyIDs() {

		StringBuilder queryString = new StringBuilder("SELECT voc.id from VocabularyImpl voc where voc.meta=true");
		Query query = getSession().createQuery(queryString.toString());

		query.setReadOnly(true);
		query.setCacheable(true);

		return query.list();
	}

	@Override
	public List<Vocabulary> findVocabularies(boolean excludeMeta) {
		final Criteria c = criteria();
		if (excludeMeta) {
			c.add(Restrictions.eq("meta", false));
		}
		return c.list();
	}

	@Override
	public Vocabulary save(Vocabulary newVoc) {
		if (newVoc instanceof VocabularyImpl) {
			// Check if this vocabulary is meta or not + update it to be sure.
			((VocabularyImpl) newVoc).setMeta(newVoc.getCommunity().isMeta());
		}
		return super.save(newVoc);
	}

	@Override
	public Vocabulary findByVocabularyName(String name) {
		return uniqueResult(criteria().add(Restrictions.eq("name", name)) //$NON-NLS-1$ //$NON-NLS-2$
				.setMaxResults(1));
	}

	@Override
	public Vocabulary findByVocabularyName(Community community, String name) {

		Query query = getSession().createQuery(
				"from VocabularyImpl vocabulary where vocabulary.community = :com and vocabulary.name = :name");
		query.setParameter("name", name);
		query.setParameter("com", community);

		return uniqueResult(query);
	}

	@Override
	public Vocabulary findSbvrVocabulary() {
		return findByVocabularyUri(Constants.SBVR_VOC);
	}

	@Override
	public Vocabulary findSbvrBusinessVocabulary() {
		return findByVocabularyUri(Constants.BUSINESS_VOC);
	}

	@Override
	public Vocabulary findSbvrMeaningAndRepresentationVocabulary() {
		return findByVocabularyUri(Constants.MEANING_AND_REPRESENTATION_VOC);
	}

	@Override
	public Vocabulary findSbvrBusinessRulesVocabulary() {
		return findByVocabularyUri(Constants.BUSINESS_RULES_VOC);
	}

	@Override
	public Vocabulary findSbvrCollibraExtensionsVocabulary() {
		return findByVocabularyUri(Constants.SBVR_EXTENSIONS_VOC);
	}

	@Override
	public Vocabulary findAttributeTypesVocabulary() {
		return findByVocabularyUri(Constants.ATTRIBUTETYPES_VOCABULARY_URI);
	}

	@Override
	public Vocabulary findStatusesVocabulary() {
		return findByVocabularyUri(Constants.STATUSES_VOCABULARY_URI);
	}

	@Override
	public Vocabulary findMetamodelExtensionsVocabulary() {
		return findByVocabularyUri(Constants.METAMODEL_EXTENSIONS_VOCABULARY_URI);
	}

	@Override
	public Vocabulary findSbvrLogicalFormulationsVocabulary() {
		return findByVocabularyUri(Constants.LOGICAL_FORMULATIONS_VOC);

	}

	@Override
	public Vocabulary findRolesAndResponsibilitiesVocabulary() {
		return findByVocabularyUri(Constants.COLLIBRA_ROLE_AND_RESPONSIBILITIES_VOCABULARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Vocabulary> searchVocabulariesForName(String name, int offset, int number) {
		// not good to modify value of parameters, even if string is immutable, because we lose track of params
		String nameExpression = name;
		if (!nameExpression.startsWith("%")) { //$NON-NLS-1$
			nameExpression = "%" + nameExpression; //$NON-NLS-1$
		}

		if (!nameExpression.endsWith("%")) { //$NON-NLS-1$
			nameExpression = nameExpression + "%"; //$NON-NLS-1$
		}

		Criteria crit = criteria().add(Restrictions.ilike("name", nameExpression)).addOrder(Order.asc("name"));

		if (offset > 0) {
			crit.setFirstResult(offset);
		}

		if (number > 0) {
			crit.setMaxResults(number);
		}
		return list(crit);
	}

	@Override
	public List<Term> findAllTermsWithCategories(Collection<Vocabulary> vocabularies, String startsWith) {

		String queryString = "SELECT term from TermImpl term inner join"
				+ " term.objectType as ot where term.vocabulary in (:vocabularies) and"
				+ " exists (select c from CategoryImpl c where c.generalConcept = ot) ";

		if (startsWith != null && startsWith != "") {
			queryString += " and upper(term.signifier) like :matchString";
		}

		queryString += " order by term.signifier DESC";

		Query query = getSession().createQuery(queryString);
		query.setParameterList("vocabularies", vocabularies);
		query.setFlushMode(FlushMode.MANUAL);

		if (startsWith != null && startsWith != "") {
			query.setParameter("matchString", "%" + startsWith);
		}

		return query.list();
	}

	@Override
	public List<Vocabulary> findAllWithoutParentCommuntiy() {
		String query = "select voc from VocabularyImpl voc where voc.community is null order by voc.name";
		return getSession().createQuery(query).list();
	}

	@Override
	public MultiValueMap findVocabulariesWithoutTerms() {
		MultiValueMap vocabularyToTerms = new MultiValueMap();
		String query = "select voc, term from VocabularyImpl voc inner join voc.terms as term order by voc.name";
		for (Object result : getSession().createQuery(query).list()) {
			Object[] values = (Object[]) result;
			vocabularyToTerms.put(values[0], values[1]);
		}

		return vocabularyToTerms;
	}

	// @Override
	// public boolean isPersisted(final Vocabulary vocabulary) {
	// Query query = getSession().createQuery(
	// "SELECT vocabulary.id from VocabularyImpl vocabulary where vocabulary = :voc");
	// query.setParameter("voc", vocabulary);
	// String result = (String) query.uniqueResult();
	//
	// return result != null;
	// }
}
