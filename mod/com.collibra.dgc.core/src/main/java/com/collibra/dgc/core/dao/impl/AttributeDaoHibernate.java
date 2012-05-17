package com.collibra.dgc.core.dao.impl;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.dao.AttributeDao;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.DateTimeAttribute;
import com.collibra.dgc.core.model.representation.MultiValueListAttribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.impl.AttributeImpl;
import com.collibra.dgc.core.model.representation.impl.DateTimeAttributeImpl;
import com.collibra.dgc.core.model.representation.impl.MultiValueListAttributeImpl;
import com.collibra.dgc.core.model.representation.impl.SingleValueListAttributeImpl;
import com.collibra.dgc.core.model.representation.impl.StringAttributeImpl;

@Service
public class AttributeDaoHibernate extends AbstractDaoHibernate<Attribute, AttributeImpl> implements AttributeDao {

	@Autowired
	public AttributeDaoHibernate(SessionFactory sessionFactory) {
		super(Attribute.class, AttributeImpl.class, sessionFactory);
	}

	@Override
	public List<Attribute> findAttributesByTypeAndOwner(Term label, Representation owner) {
		return list(criteria().add(Restrictions.eq("owner", owner)).add(Restrictions.eq("label", label)));
	}

	@Override
	public List<Attribute> findAttributesByTypesAndOwner(Term[] labels, Representation owner) {
		return list(criteria().add(Restrictions.eq("owner", owner)).add(Restrictions.in("label", labels))
				.addOrder(Order.asc("label")));
	}

	@Override
	public List<StringAttribute> findStringAttributesByTypeAndOwner(Term label, Representation owner) {
		return getSession().createCriteria(StringAttributeImpl.class).add(Restrictions.eq("owner", owner))
				.add(Restrictions.eq("label", label)).setFlushMode(FlushMode.MANUAL).list();
	}

	@Override
	public List<DateTimeAttribute> findDateTimeAttributesByOwner(Representation owner) {
		return getSession().createCriteria(DateTimeAttributeImpl.class).add(Restrictions.eq("owner", owner))
				.setFlushMode(FlushMode.MANUAL).list();
	}

	@Override
	public List<DateTimeAttribute> findDateTimeAttributesByTypeAndOwner(Term label, Representation owner) {
		return getSession().createCriteria(DateTimeAttributeImpl.class).add(Restrictions.eq("owner", owner))
				.add(Restrictions.eq("label", label)).setFlushMode(FlushMode.MANUAL).list();
	}

	@Override
	public List<StringAttribute> findStringAttributesByOwner(Representation owner) {
		return getSession().createCriteria(StringAttributeImpl.class).add(Restrictions.eq("owner", owner))
				.setFlushMode(FlushMode.MANUAL).list();
	}

	@Override
	public List<MultiValueListAttribute> findMultiValueListAttributesByOwner(Representation owner) {
		return getSession().createCriteria(MultiValueListAttributeImpl.class).add(Restrictions.eq("owner", owner))
				.setFlushMode(FlushMode.MANUAL).list();
	}

	@Override
	public List<MultiValueListAttribute> findMultiValueListAttributesByTypeAndOwner(Term label, Representation owner) {
		return getSession().createCriteria(MultiValueListAttributeImpl.class).add(Restrictions.eq("owner", owner))
				.add(Restrictions.eq("label", label)).setFlushMode(FlushMode.MANUAL).list();
	}

	@Override
	public List<SingleValueListAttribute> findSingleValueListAttributesByOwner(Representation owner) {
		return getSession().createCriteria(SingleValueListAttributeImpl.class).add(Restrictions.eq("owner", owner))
				.setFlushMode(FlushMode.MANUAL).list();
	}

	@Override
	public List<SingleValueListAttribute> findSingleValueListAttributesByTypeAndOwner(Term label, Representation owner) {
		return getSession().createCriteria(SingleValueListAttributeImpl.class).add(Restrictions.eq("owner", owner))
				.add(Restrictions.eq("label", label)).setFlushMode(FlushMode.MANUAL).list();
	}

	@Override
	public Map<ObjectType, List<Attribute>> findAttributesByOwnerInMap(Representation owner) {
		Map<ObjectType, List<Attribute>> attributesByType = new Hashtable<ObjectType, List<Attribute>>();
		final List<Attribute> attrs = findAllAttributesByOwner(owner);
		for (Attribute attribute : attrs) {
			ObjectType type = attribute.getLabel().getObjectType();
			List<Attribute> attributes = attributesByType.get(type);
			if (attributes == null) {
				attributes = new LinkedList<Attribute>();
				attributesByType.put(type, attributes);
			}
			attributes.add(attribute);
		}

		return attributesByType;
	}

	@Override
	public List<Attribute> findAttributesByType(Term attributeLabel) {
		return list(criteria().add(Restrictions.eq("label", attributeLabel)));
	}

	@Override
	public List<Attribute> findAllAttributesByOwner(Representation owner) {
		Query query = getSession().createQuery("from AttributeImpl att where att.owner.id = :resourceId");
		query.setParameter("resourceId", owner.getId());
		return list(query);
	}

	@Override
	public List<Attribute> findAttributesByVocabulary(Vocabulary vocabulary) {
		return list(criteria().add(Restrictions.eq("vocabulary", vocabulary)));
	}

	@Override
	public List<Attribute> searchAttributesForLongExpression(String partialExpression, int offset, int number) {
		if (!partialExpression.startsWith("%"))
			partialExpression = "%" + partialExpression;

		if (!partialExpression.endsWith("%"))
			partialExpression = partialExpression + "%";

		Query query = getSession().createQuery(
				"from AttributeImpl attr where lower(attr.longExpression) like :expression order by attr.id ");
		query.setParameter("expression", partialExpression.toLowerCase());

		if (offset > 0) {
			query.setFirstResult(offset);
		}

		if (number > 0) {
			query.setMaxResults(number);
		}

		return list(query);
	}

	@Override
	public List<Attribute> findAllWithoutOwner() {
		String query = "select att from AttributeImpl att where att.owner is null";
		return getSession().createQuery(query).list();
	}

	// FIXME: This does not work on MySQL -> However works fine on Oracle. The query is pure HQL with OR criterion.
	@Override
	public List<Attribute> findAllWithoutLabel() {
		String query = "select att from AttributeImpl att where att.label is null";
		return list(getSession().createQuery(query));
	}

	@Override
	public List<Term> findAttributeTypeLabels() {
		String query = "select term from TermImpl term where term.vocabulary.uri='"
				+ Constants.ATTRIBUTETYPES_VOCABULARY_URI
				+ "' and term.objectType.type.generalConcept.id = :attributeTypeRId order by term.signifier";
		Query q = getSession().createQuery(query);

		q.setReadOnly(true);
		q.setCacheable(true);
		q.setParameter("attributeTypeRId", MeaningConstants.META_ATTRIBUTE_TYPE_UUID);

		return q.list();
	}

	@Override
	public StringAttribute getStringAttribute(String rId) {
		return (StringAttribute) getSession().createCriteria(StringAttributeImpl.class).add(Restrictions.eq("id", rId))
				.setFlushMode(FlushMode.MANUAL).uniqueResult();
	}

	@Override
	public MultiValueListAttribute getMultiValueListAttribute(String rId) {
		return (MultiValueListAttribute) getSession().createCriteria(MultiValueListAttributeImpl.class)
				.add(Restrictions.eq("id", rId)).setFlushMode(FlushMode.MANUAL).uniqueResult();
	}

	@Override
	public SingleValueListAttribute getSingleValueListAttribute(String rId) {
		return (SingleValueListAttribute) getSession().createCriteria(SingleValueListAttribute.class)
				.add(Restrictions.eq("id", rId)).setFlushMode(FlushMode.MANUAL).uniqueResult();
	}

	@Override
	public DateTimeAttribute getDateTimeAttribute(String rId) {
		return (DateTimeAttribute) getSession().createCriteria(DateTimeAttribute.class).add(Restrictions.eq("id", rId))
				.setFlushMode(FlushMode.MANUAL).uniqueResult();
	}

	// @Override
	// public boolean isPersisted(final Attribute attribute) {
	//
	// Query query = getSession().createQuery(
	// "SELECT attribute.id from AttributeImpl attribute where attribute = :attr");
	// query.setParameter("attr", attribute);
	// String result = (String) query.uniqueResult();
	//
	// return result != null;
	// }
}
