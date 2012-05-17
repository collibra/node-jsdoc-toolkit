package com.collibra.dgc.core.dao.impl;

import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.CharacteristicFormDao;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.representation.facttypeform.impl.CharacteristicFormImpl;

@Service
public class CharacteristicFormDaoHibernate extends AbstractDaoHibernate<CharacteristicForm, CharacteristicFormImpl>
		implements CharacteristicFormDao {

	@Autowired
	public CharacteristicFormDaoHibernate(SessionFactory sessionFactory) {
		super(CharacteristicForm.class, CharacteristicFormImpl.class, sessionFactory);
	}

	@Override
	public CharacteristicForm findByRepresentation(Vocabulary vocabulary, Term term, String roleExpression) {
		return uniqueResult(criteria().add(Restrictions.eq("term", term)).add(Restrictions.eq("role", roleExpression))
				.add(Restrictions.eq("vocabulary", vocabulary)));
	}

	@Override
	public CharacteristicForm findPreferred(Characteristic characteristic, Vocabulary vocabulary) {
		return uniqueResult(criteria().add(Restrictions.eq("characteristic", characteristic))
				.add(Restrictions.eq("vocabulary", vocabulary)).add(Restrictions.eq("isPreferred", Boolean.TRUE)));
	}

	@Override
	public List<CharacteristicForm> find(Term term) {
		String query = "select cf from CharacteristicFormImpl as cf where cf.term = :term order by cf.id desc";
		Query q = getSession().createQuery(query);
		q.setParameter("term", term);
		q.setFlushMode(FlushMode.MANUAL);
		return q.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CharacteristicForm> searchByVerbalisation(Vocabulary vocabulary, String startsWith) {
		if (!startsWith.startsWith("%")) {
			startsWith = "%" + startsWith;
		}
		if (!startsWith.endsWith("%")) {
			startsWith = startsWith + "%";
		}

		String query = "select cform from CharacteristicFormImpl as cform where  cform.vocabulary = :vocabulary and lower(cform.term.signifier) like :startsWith";

		Query q = getSession().createQuery(query);
		q.setParameter("vocabulary", vocabulary);
		q.setParameter("startsWith", startsWith.toLowerCase());
		q.setFlushMode(FlushMode.MANUAL);
		return q.list();
	}

	@Override
	public List<CharacteristicForm> findSynonymousForms(CharacteristicForm cForm) {
		Query query = getSession()
				.createQuery(
						"from CharacteristicFormImpl characteristicForm where characteristicForm <> :cf and characteristicForm.characteristic = :meaning");
		query.setParameter("cf", cForm);
		query.setParameter("meaning", cForm.getCharacteristic());
		query.setFlushMode(FlushMode.MANUAL);
		return list(query);
	}

	public List<CharacteristicForm> findCharacteristicFormsContainingTerm(Term term) {
		return list(criteria().add(Restrictions.eq("term", term)));

	}

	@Override
	public List<CharacteristicForm> find(Vocabulary vocabulary) {
		return list(criteria().add(Restrictions.eq("vocabulary", vocabulary)));
	}

	@Override
	public CharacteristicForm findByExpression(Vocabulary vocabulary, String termSignifier, String roleExpression) {
		String query = "select cform from CharacteristicFormImpl as cform where cform.vocabulary = :vocabulary and cform.term.signifier = :termSignifier and cform.role = :role order by cform.id desc";
		Query q = getSession().createQuery(query);
		q.setParameter("vocabulary", vocabulary);
		q.setParameter("termSignifier", termSignifier);
		q.setParameter("role", roleExpression);

		q.setFlushMode(FlushMode.MANUAL);
		return (CharacteristicForm) q.uniqueResult();
	}

	@Override
	public CharacteristicForm find(Vocabulary vocabulary, Term term, String roleExpression) {
		String query = "select cform from CharacteristicFormImpl as cform where cform.vocabulary = :vocabulary and cform.term = :term and cform.role = :role order by cform.id desc";
		Query q = getSession().createQuery(query);
		q.setParameter("vocabulary", vocabulary);
		q.setParameter("term", term);
		q.setParameter("role", roleExpression);

		q.setFlushMode(FlushMode.MANUAL);
		return (CharacteristicForm) q.uniqueResult();
	}

	@Override
	public CharacteristicForm save(CharacteristicForm newObject) {

		return super.save(newObject);
	}

}
