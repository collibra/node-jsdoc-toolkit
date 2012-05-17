/**
 * 
 */
package com.collibra.dgc.rest.core.v1_0.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.component.attribute.AttributeTypeComponent;
import com.collibra.dgc.core.component.representation.BinaryFactTypeFormComponent;
import com.collibra.dgc.core.component.representation.CharacteristicFormComponent;
import com.collibra.dgc.core.component.representation.RepresentationComponent;
import com.collibra.dgc.core.model.user.GroupMembership;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.model.user.PhoneNumber;
import com.collibra.dgc.core.model.user.Role;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.rest.core.v1_0.dto.Address;
import com.collibra.dgc.rest.core.v1_0.dto.AddressCollection;
import com.collibra.dgc.rest.core.v1_0.dto.AddressType;
import com.collibra.dgc.rest.core.v1_0.dto.Attribute;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeAndRelationTypesConfigurationCategories;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeAndRelationTypesConfigurationCategory;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeReference;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeReferences;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeType;
import com.collibra.dgc.rest.core.v1_0.dto.AttributeTypes;
import com.collibra.dgc.rest.core.v1_0.dto.Attributes;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeForm;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeFormReference;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeFormReferences;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeForms;
import com.collibra.dgc.rest.core.v1_0.dto.CharacteristicForm;
import com.collibra.dgc.rest.core.v1_0.dto.CharacteristicFormReference;
import com.collibra.dgc.rest.core.v1_0.dto.CharacteristicFormReferences;
import com.collibra.dgc.rest.core.v1_0.dto.CharacteristicForms;
import com.collibra.dgc.rest.core.v1_0.dto.Communities;
import com.collibra.dgc.rest.core.v1_0.dto.Community;
import com.collibra.dgc.rest.core.v1_0.dto.CommunityReference;
import com.collibra.dgc.rest.core.v1_0.dto.CommunityReferences;
import com.collibra.dgc.rest.core.v1_0.dto.ConfigProperty;
import com.collibra.dgc.rest.core.v1_0.dto.Configuration;
import com.collibra.dgc.rest.core.v1_0.dto.Group;
import com.collibra.dgc.rest.core.v1_0.dto.GroupReference;
import com.collibra.dgc.rest.core.v1_0.dto.InstantMessagingAccount;
import com.collibra.dgc.rest.core.v1_0.dto.InstantMessagingAccountCollection;
import com.collibra.dgc.rest.core.v1_0.dto.InstantMessagingAccountType;
import com.collibra.dgc.rest.core.v1_0.dto.Job;
import com.collibra.dgc.rest.core.v1_0.dto.Jobs;
import com.collibra.dgc.rest.core.v1_0.dto.MemberReference;
import com.collibra.dgc.rest.core.v1_0.dto.MultiValueListAttribute;
import com.collibra.dgc.rest.core.v1_0.dto.MultiValueListAttributeReference;
import com.collibra.dgc.rest.core.v1_0.dto.ObjectFactory;
import com.collibra.dgc.rest.core.v1_0.dto.Phone;
import com.collibra.dgc.rest.core.v1_0.dto.PhoneCollection;
import com.collibra.dgc.rest.core.v1_0.dto.PhoneType;
import com.collibra.dgc.rest.core.v1_0.dto.Relation;
import com.collibra.dgc.rest.core.v1_0.dto.RelationReference;
import com.collibra.dgc.rest.core.v1_0.dto.Relations;
import com.collibra.dgc.rest.core.v1_0.dto.Representation;
import com.collibra.dgc.rest.core.v1_0.dto.RepresentationReference;
import com.collibra.dgc.rest.core.v1_0.dto.RepresentationReferences;
import com.collibra.dgc.rest.core.v1_0.dto.Representations;
import com.collibra.dgc.rest.core.v1_0.dto.Resource;
import com.collibra.dgc.rest.core.v1_0.dto.RoleReference;
import com.collibra.dgc.rest.core.v1_0.dto.SimpleTerm;
import com.collibra.dgc.rest.core.v1_0.dto.SimpleTerms;
import com.collibra.dgc.rest.core.v1_0.dto.SingleValueListAttribute;
import com.collibra.dgc.rest.core.v1_0.dto.SingleValueListAttributeReference;
import com.collibra.dgc.rest.core.v1_0.dto.StringAttribute;
import com.collibra.dgc.rest.core.v1_0.dto.StringAttributeReference;
import com.collibra.dgc.rest.core.v1_0.dto.Term;
import com.collibra.dgc.rest.core.v1_0.dto.TermReference;
import com.collibra.dgc.rest.core.v1_0.dto.TermReferences;
import com.collibra.dgc.rest.core.v1_0.dto.Terms;
import com.collibra.dgc.rest.core.v1_0.dto.User;
import com.collibra.dgc.rest.core.v1_0.dto.UserCollection;
import com.collibra.dgc.rest.core.v1_0.dto.UserReference;
import com.collibra.dgc.rest.core.v1_0.dto.Vocabularies;
import com.collibra.dgc.rest.core.v1_0.dto.Vocabulary;
import com.collibra.dgc.rest.core.v1_0.dto.VocabularyReference;
import com.collibra.dgc.rest.core.v1_0.dto.VocabularyReferences;
import com.collibra.dgc.rest.core.v1_0.dto.Website;
import com.collibra.dgc.rest.core.v1_0.dto.WebsiteCollection;
import com.collibra.dgc.rest.core.v1_0.dto.WebsiteType;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/**
 * @author fvdmaele
 * @author pmalarme
 * 
 */
@Service
public class RestModelBuilder {

	private final ObjectFactory factory = new ObjectFactory();

	@Autowired
	private AttributeTypeComponent attributeTypeComponent;

	@Autowired
	private CharacteristicFormComponent characteristicFormComponent;

	@Autowired
	private BinaryFactTypeFormComponent binaryFactTypeFormComponent;

	@Autowired
	@Qualifier("RepresentationComponentImpl")
	private RepresentationComponent representationComponent;

	public ObjectFactory getObjectFactory() {
		return factory;
	}

	/* JOB */

	public Job buildJob(com.collibra.dgc.core.model.job.Job input) {
		final Job job = new Job();
		job.setCancelable(input.isCancelable());
		job.setCreationDate(input.getCreationDate());
		job.setEndDate(input.getEndDate());
		job.setId(input.getId());
		job.setMessage(input.getMessage());
		job.setOwner(input.getOwner());
		job.setPercentage(input.getProgressPercentage());
		job.setStartDate(input.getStartDate());
		job.setState(input.getState().toString());

		return job;
	}

	public Jobs buildJobs(Collection<com.collibra.dgc.core.model.job.Job> input) {
		final Jobs jobs = new Jobs();
		for (final com.collibra.dgc.core.model.job.Job job : input) {
			jobs.getJobs().add(buildJob(job));
		}
		return jobs;
	}

	/* RESOURCE */

	private void setResourceAttribute(Resource r, final com.collibra.dgc.core.model.Resource resource) {

		r.setLastModified(resource.getLastModified());
		r.setLastModifiedBy(resource.getLastModifiedBy());
		r.setCreatedOn(resource.getCreatedOn());
		r.setCreatedBy(resource.getCreatedBy());
		r.setResourceId(resource.getId());
		r.setLocked(resource.isLocked());
	}

	/* COMMUNITY */

	private void setCommunityReferenceAttribute(CommunityReference cr,
			final com.collibra.dgc.core.model.community.Community community) {

		setResourceAttribute(cr, community);

		cr.setName(community.getName());
		cr.setUri(community.getUri());
		cr.setLanguage(community.getLanguage());
		cr.setSbvr(community.isSBVR());
		cr.setMeta(community.isMeta());
	}

	public CommunityReference buildCommunityReference(final com.collibra.dgc.core.model.community.Community community) {

		if (community == null)
			return null;

		CommunityReference cr = factory.createCommunityReference();

		setCommunityReferenceAttribute(cr, community);

		return cr;
	}

	public CommunityReferences buildCommunityReferences(
			final Collection<com.collibra.dgc.core.model.community.Community> communities) {

		CommunityReferences crs = factory.createCommunityReferences();
		List<CommunityReference> crList = crs.getCommunityReferences();

		for (final com.collibra.dgc.core.model.community.Community community : communities) {

			CommunityReference cr = buildCommunityReference(community);
			crList.add(cr);
		}

		return crs;
	}

	public Community buildCommunity(final com.collibra.dgc.core.model.community.Community community) {

		if (community == null)
			return null;

		Community c = factory.createCommunity();

		setCommunityReferenceAttribute(c, community);

		c.setParentReference(buildCommunityReference(community.getParentCommunity()));
		c.setSubCommunityReferences(buildCommunityReferences(community.getSubCommunities()));
		c.setVocabularyReferences(buildVocabularyReferences(community.getVocabularies()));

		return c;
	}

	public Communities buildCommunities(final Collection<com.collibra.dgc.core.model.community.Community> communities) {

		Communities cs = factory.createCommunities();
		List<Community> cList = cs.getCommunities();

		for (final com.collibra.dgc.core.model.community.Community community : communities) {

			Community c = buildCommunity(community);
			cList.add(c);
		}

		return cs;
	}

	/* VOCABULARY */

	private void setVocabularyReferenceAttribute(VocabularyReference vr,
			final com.collibra.dgc.core.model.representation.Vocabulary vocabulary) {

		setResourceAttribute(vr, vocabulary);

		vr.setName(vocabulary.getName());
		vr.setUri(vocabulary.getUri());
		vr.setMeta(vocabulary.isMeta());

		if (vocabulary.getType() != null)
			vr.setTypeReference(buildTermReference(vocabulary.getType().findPreferredTerm()));
	}

	public VocabularyReference buildVocabularyReference(
			final com.collibra.dgc.core.model.representation.Vocabulary vocabulary) {

		VocabularyReference vr = factory.createVocabularyReference();

		setVocabularyReferenceAttribute(vr, vocabulary);

		return vr;
	}

	public VocabularyReferences buildVocabularyReferences(
			final Collection<com.collibra.dgc.core.model.representation.Vocabulary> vocabularies) {

		VocabularyReferences vrs = factory.createVocabularyReferences();
		List<VocabularyReference> vrList = vrs.getVocabularyReferences();

		for (com.collibra.dgc.core.model.representation.Vocabulary vocabulary : vocabularies) {

			VocabularyReference vr = buildVocabularyReference(vocabulary);
			vrList.add(vr);
		}

		return vrs;
	}

	public Vocabulary buildVocabulary(final com.collibra.dgc.core.model.representation.Vocabulary vocabulary) {

		if (vocabulary == null)
			return null;

		Vocabulary v = factory.createVocabulary();

		setVocabularyReferenceAttribute(v, vocabulary);

		v.setCommunityReference(buildCommunityReference(vocabulary.getCommunity()));

		v.setTermReferences(buildTermReferences(vocabulary.getTerms()));
		v.setBinaryFactTypeFormReferences(buildBinaryFactTypeFormReferences(vocabulary.getBinaryFactTypeForms()));
		v.setCharacteristicFormReferences(buildCharacteristicFormReferences(vocabulary.getCharacteristicForms()));

		// TODO add rule set

		return v;
	}

	public Vocabularies buildVocabularies(
			final Collection<com.collibra.dgc.core.model.representation.Vocabulary> vocabularies) {

		Vocabularies vs = factory.createVocabularies();
		List<Vocabulary> vList = vs.getVocabularies();

		for (final com.collibra.dgc.core.model.representation.Vocabulary vocabulary : vocabularies) {

			final Vocabulary v = buildVocabulary(vocabulary);
			vList.add(v);
		}

		return vs;
	}

	/* REPRESENTATION */

	private void setRepresentationReferenceAttribute(RepresentationReference rr,
			final com.collibra.dgc.core.model.representation.Representation representation) {

		setResourceAttribute(rr, representation);

		rr.setPreferred(representation.getIsPreferred());
	}

	private void setRepresentationAttribute(Representation r,
			final com.collibra.dgc.core.model.representation.Representation representation) {

		setRepresentationReferenceAttribute(r, representation);

		String rId = representation.getId();

		r.setStatusReference(buildTermReference(representation.getStatus()));
		r.setVocabularyReference(buildVocabularyReference(representation.getVocabulary()));
		r.setConceptRId(representation.getMeaning().getId());
		r.setConceptType(buildSimpleTerm(representationComponent.getConceptType(rId)));
		r.setAttributeReferences(buildAttributeReferences(representation.getAttributes()));
	}

	public RepresentationReference buildRepresentationReference(
			final com.collibra.dgc.core.model.representation.Representation representation) {

		// For each kind of representation create the right REST model representation reference
		if (representation instanceof com.collibra.dgc.core.model.representation.Term)
			return buildTermReference((com.collibra.dgc.core.model.representation.Term) representation);

		else if (representation instanceof com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm)
			return buildCharacteristicFormReference((com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm) representation);

		else if (representation instanceof com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm)
			return buildBinaryFactTypeFormReference((com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm) representation);

		return null;
	}

	public RepresentationReferences buildRepresentationReferences(
			final Collection<com.collibra.dgc.core.model.representation.Representation> representations) {

		RepresentationReferences rrs = factory.createRepresentationReferences();
		List<RepresentationReference> rrList = rrs.getRepresentationReferences();

		for (final com.collibra.dgc.core.model.representation.Representation representation : representations) {

			final RepresentationReference rr = buildRepresentationReference(representation);
			rrList.add(rr);
		}

		return rrs;
	}

	public Representation buildRepresentation(
			final com.collibra.dgc.core.model.representation.Representation representation) {

		// For each kind of representation create the right REST model representation reference
		if (representation instanceof com.collibra.dgc.core.model.representation.Term)
			return buildTerm((com.collibra.dgc.core.model.representation.Term) representation);

		else if (representation instanceof com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm)
			return buildCharacteristicForm((com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm) representation);

		else if (representation instanceof com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm)
			return buildBinaryFactTypeForm((com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm) representation);

		return null;
	}

	public Representations buildRepresentations(
			final Collection<com.collibra.dgc.core.model.representation.Representation> representations) {

		Representations rs = factory.createRepresentations();
		List<Representation> rList = rs.getRepresentations();

		for (final com.collibra.dgc.core.model.representation.Representation representation : representations) {

			Representation r = buildRepresentation(representation);
			rList.add(r);
		}

		return rs;
	}

	/* TERM */

	private void setTermReferenceAttribute(TermReference tr, final com.collibra.dgc.core.model.representation.Term term) {

		setRepresentationReferenceAttribute(tr, term);

		tr.setSignifier(term.getSignifier());
	}

	public TermReference buildTermReference(final com.collibra.dgc.core.model.representation.Term term) {

		if (term == null)
			return null;

		TermReference tr = factory.createTermReference();

		setTermReferenceAttribute(tr, term);

		return tr;
	}

	public TermReferences buildTermReferences(final Collection<com.collibra.dgc.core.model.representation.Term> terms) {

		TermReferences trs = factory.createTermReferences();
		List<TermReference> trList = trs.getTermReferences();

		for (final com.collibra.dgc.core.model.representation.Term term : terms) {

			TermReference tr = buildTermReference(term);
			trList.add(tr);
		}

		return trs;
	}

	public SimpleTerm buildSimpleTerm(final com.collibra.dgc.core.model.representation.Term term) {

		SimpleTerm st = factory.createSimpleTerm();

		setTermReferenceAttribute(st, term);

		st.setVocabularyReference(buildVocabularyReference(term.getVocabulary()));

		return st;
	}

	public SimpleTerms buildSimpleTerms(final Collection<com.collibra.dgc.core.model.representation.Term> terms) {

		SimpleTerms sts = factory.createSimpleTerms();
		List<SimpleTerm> stList = sts.getSimpleTerms();

		for (final com.collibra.dgc.core.model.representation.Term term : terms) {

			SimpleTerm st = buildSimpleTerm(term);
			stList.add(st);
		}

		return sts;
	}

	public Term buildTerm(final com.collibra.dgc.core.model.representation.Term term) {

		Term t = factory.createTerm();
		String rId = term.getId();

		setRepresentationAttribute(t, term);

		t.setSignifier(term.getSignifier());
		t.setCharacteristicFormReferences(buildCharacteristicFormReferences(characteristicFormComponent
				.getCharacteristicFormsContainingTerm(rId)));
		t.setBinaryFactTypeFormReferences(buildBinaryFactTypeFormReferences(binaryFactTypeFormComponent
				.getBinaryFactTypeFormsContainingTerm(rId)));

		/* BUILD GENERAL CONCEPT, SPECIALIZED CONCEPTS, SYNONYMS */
		// Get resources from the component
		final com.collibra.dgc.core.model.representation.Representation generalConcept = representationComponent
				.getGeneralConcept(rId);
		final Collection<com.collibra.dgc.core.model.representation.Representation> specializedConcepts = representationComponent
				.getSpecializedConcepts(rId, 10);
		final Collection<com.collibra.dgc.core.model.representation.Representation> synonyms = representationComponent
				.getSynonyms(rId);

		// Create REST model variables
		RepresentationReference generalConceptRR = factory.createRepresentationReference();
		RepresentationReferences specializedConceptRRS = factory.createRepresentationReferences();
		List<RepresentationReference> specializedConceptRRList = specializedConceptRRS.getRepresentationReferences();
		RepresentationReferences synonymRRS = factory.createRepresentationReferences();
		List<RepresentationReference> synonymRRList = synonymRRS.getRepresentationReferences();

		// General Concept
		generalConceptRR = buildSimpleTerm((com.collibra.dgc.core.model.representation.Term) generalConcept);

		// Specialized Concepts
		for (final com.collibra.dgc.core.model.representation.Representation specializedConcept : specializedConcepts) {

			RepresentationReference rr = buildSimpleTerm((com.collibra.dgc.core.model.representation.Term) specializedConcept);
			specializedConceptRRList.add(rr);
		}

		// Synonyms
		for (final com.collibra.dgc.core.model.representation.Representation synonym : synonyms) {

			RepresentationReference rr = buildSimpleTerm((com.collibra.dgc.core.model.representation.Term) synonym);
			synonymRRList.add(rr);
		}

		// Set the compute REST objects to the representation REST object
		t.setGeneralConceptReference(generalConceptRR);
		t.setSpecializedConceptReferences(specializedConceptRRS);
		t.setSynonymReferences(synonymRRS);

		return t;
	}

	public Terms buildTerms(final Collection<com.collibra.dgc.core.model.representation.Term> terms) {

		Terms ts = factory.createTerms();
		List<Term> tList = ts.getTerms();

		for (final com.collibra.dgc.core.model.representation.Term term : terms) {

			Term t = buildTerm(term);
			tList.add(t);
		}

		return ts;
	}

	/* CHARACTERISTIC FORM */

	public CharacteristicFormReference buildCharacteristicFormReference(
			final com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm characteristicForm) {

		CharacteristicFormReference cfr = factory.createCharacteristicFormReference();

		setRepresentationReferenceAttribute(cfr, characteristicForm);

		cfr.setTerm(buildSimpleTerm(characteristicForm.getTerm()));
		cfr.setRole(characteristicForm.getRole());

		return cfr;
	}

	public CharacteristicFormReferences buildCharacteristicFormReferences(
			final Collection<com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm> characteristicForms) {

		CharacteristicFormReferences cfrs = factory.createCharacteristicFormReferences();
		List<CharacteristicFormReference> cfrList = cfrs.getCharacteristicFormReferences();

		for (final com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm characteristicForm : characteristicForms) {

			CharacteristicFormReference cfr = buildCharacteristicFormReference(characteristicForm);
			cfrList.add(cfr);
		}

		return cfrs;
	}

	public CharacteristicForm buildCharacteristicForm(
			final com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm characteristicForm) {

		CharacteristicForm cf = factory.createCharacteristicForm();
		String rId = characteristicForm.getId();

		setRepresentationAttribute(cf, characteristicForm);

		cf.setTerm(buildSimpleTerm(characteristicForm.getTerm()));
		cf.setRole(characteristicForm.getRole());

		/* BUILD GENERAL CONCEPT, SPECIALIZED CONCEPTS, SYNONYMS */
		// Get resources from the component
		final com.collibra.dgc.core.model.representation.Representation generalConcept = representationComponent
				.getGeneralConcept(rId);
		final Collection<com.collibra.dgc.core.model.representation.Representation> specializedConcepts = representationComponent
				.getSpecializedConcepts(rId, 10);
		final Collection<com.collibra.dgc.core.model.representation.Representation> synonyms = representationComponent
				.getSynonyms(rId);

		// Create REST model variables
		RepresentationReference generalConceptRR = factory.createRepresentationReference();
		RepresentationReferences specializedConceptRRS = factory.createRepresentationReferences();
		List<RepresentationReference> specializedConceptRRList = specializedConceptRRS.getRepresentationReferences();
		RepresentationReferences synonymRRS = factory.createRepresentationReferences();
		List<RepresentationReference> synonymRRList = synonymRRS.getRepresentationReferences();

		// General Concept
		generalConceptRR = buildCharacteristicFormReference((com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm) generalConcept);

		// Specialized Concepts
		for (final com.collibra.dgc.core.model.representation.Representation specializedConcept : specializedConcepts) {

			RepresentationReference rr = buildCharacteristicFormReference((com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm) specializedConcept);
			specializedConceptRRList.add(rr);
		}

		// Synonyms
		for (final com.collibra.dgc.core.model.representation.Representation synonym : synonyms) {

			RepresentationReference rr = buildCharacteristicFormReference((com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm) synonym);
			synonymRRList.add(rr);
		}

		// Set the compute REST objects to the representation REST object
		cf.setGeneralConceptReference(generalConceptRR);
		cf.setSpecializedConceptReferences(specializedConceptRRS);
		cf.setSynonymReferences(synonymRRS);

		return cf;
	}

	public CharacteristicForms buildCharacteristicForms(
			final Collection<com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm> characteristicForms) {

		CharacteristicForms cfs = factory.createCharacteristicForms();
		List<CharacteristicForm> cfList = cfs.getCharacteristicForms();

		for (final com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm characteristicForm : characteristicForms) {

			CharacteristicForm cf = buildCharacteristicForm(characteristicForm);
			cfList.add(cf);
		}

		return cfs;
	}

	/* BINARY FACT TYPE FORM */

	public BinaryFactTypeFormReference buildBinaryFactTypeFormReference(
			final com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm binaryFactTypeForm) {

		BinaryFactTypeFormReference bftfr = factory.createBinaryFactTypeFormReference();

		setRepresentationReferenceAttribute(bftfr, binaryFactTypeForm);

		bftfr.setHeadTerm(buildSimpleTerm(binaryFactTypeForm.getHeadTerm()));
		bftfr.setRole(binaryFactTypeForm.getRole());
		bftfr.setCoRole(binaryFactTypeForm.getCoRole());
		bftfr.setTailTerm(buildSimpleTerm(binaryFactTypeForm.getTailTerm()));

		return bftfr;
	}

	public BinaryFactTypeFormReferences buildBinaryFactTypeFormReferences(
			final Collection<com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm> binaryFactTypeForms) {

		BinaryFactTypeFormReferences bftfrs = factory.createBinaryFactTypeFormReferences();
		List<BinaryFactTypeFormReference> bftfrList = bftfrs.getBinaryFactTypeFormReferences();

		for (final com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm binaryFactTypeForm : binaryFactTypeForms) {

			BinaryFactTypeFormReference bftfr = buildBinaryFactTypeFormReference(binaryFactTypeForm);
			bftfrList.add(bftfr);
		}

		return bftfrs;
	}

	public BinaryFactTypeForm buildBinaryFactTypeForm(
			final com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm binaryFactTypeForm) {

		BinaryFactTypeForm bftf = factory.createBinaryFactTypeForm();
		String rId = binaryFactTypeForm.getId();

		setRepresentationAttribute(bftf, binaryFactTypeForm);

		bftf.setHeadTerm(buildSimpleTerm(binaryFactTypeForm.getHeadTerm()));
		bftf.setRole(binaryFactTypeForm.getRole());
		bftf.setCoRole(binaryFactTypeForm.getCoRole());
		bftf.setTailTerm(buildSimpleTerm(binaryFactTypeForm.getTailTerm()));

		/* BUILD GENERAL CONCEPT, SPECIALIZED CONCEPTS, SYNONYMS */
		// Get resources from the component
		final com.collibra.dgc.core.model.representation.Representation generalConcept = representationComponent
				.getGeneralConcept(rId);
		final Collection<com.collibra.dgc.core.model.representation.Representation> specializedConcepts = representationComponent
				.getSpecializedConcepts(rId, 10);
		final Collection<com.collibra.dgc.core.model.representation.Representation> synonyms = representationComponent
				.getSynonyms(rId);

		// Create REST model variables
		RepresentationReference generalConceptRR = factory.createRepresentationReference();
		RepresentationReferences specializedConceptRRS = factory.createRepresentationReferences();
		List<RepresentationReference> specializedConceptRRList = specializedConceptRRS.getRepresentationReferences();
		RepresentationReferences synonymRRS = factory.createRepresentationReferences();
		List<RepresentationReference> synonymRRList = synonymRRS.getRepresentationReferences();

		// General Concept
		generalConceptRR = buildBinaryFactTypeFormReference((com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm) generalConcept);

		// Specialized Concepts
		for (final com.collibra.dgc.core.model.representation.Representation specializedConcept : specializedConcepts) {

			RepresentationReference rr = buildBinaryFactTypeFormReference((com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm) specializedConcept);
			specializedConceptRRList.add(rr);
		}

		// Synonyms
		for (final com.collibra.dgc.core.model.representation.Representation synonym : synonyms) {

			RepresentationReference rr = buildBinaryFactTypeFormReference((com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm) synonym);
			synonymRRList.add(rr);
		}

		// Set the compute REST objects to the representation REST object
		bftf.setGeneralConceptReference(generalConceptRR);
		bftf.setSpecializedConceptReferences(specializedConceptRRS);
		bftf.setSynonymReferences(synonymRRS);

		return bftf;
	}

	public BinaryFactTypeForms buildBinaryFactTypeForms(
			final Collection<com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm> binaryFactTypeForms) {

		BinaryFactTypeForms bftfs = factory.createBinaryFactTypeForms();
		List<BinaryFactTypeForm> bftfList = bftfs.getBinaryFactTypeForms();

		for (final com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm binaryFactTypeForm : binaryFactTypeForms) {

			BinaryFactTypeForm bftf = buildBinaryFactTypeForm(binaryFactTypeForm);
			bftfList.add(bftf);
		}

		return bftfs;
	}

	/* ATTRIBUTE TYPE */

	public AttributeType buildAttributeType(final com.collibra.dgc.core.model.representation.Term term) {

		AttributeType at = factory.createAttributeType();
		String rId = term.getId();
		String kind = attributeTypeComponent.getAttributeTypeKind(rId);

		setTermReferenceAttribute(at, term);

		at.setKind(kind);
		at.setDescriptionReference(buildStringAttributeReference(attributeTypeComponent
				.getAttributeTypeDescription(rId)));

		if (kind.equals(com.collibra.dgc.core.model.representation.SingleValueListAttribute.class.getSimpleName())
				|| kind.equals(com.collibra.dgc.core.model.representation.MultiValueListAttribute.class.getSimpleName())) {

			Collection<String> allowedValues = attributeTypeComponent.getAllowedValues(rId);

			if (allowedValues != null && !allowedValues.isEmpty())
				at.getAllowedValues().addAll(allowedValues);
		}

		return at;
	}

	public AttributeTypes buildAttributeTypes(final Collection<com.collibra.dgc.core.model.representation.Term> terms) {

		AttributeTypes ats = factory.createAttributeTypes();
		List<AttributeType> atList = ats.getAttributeTypes();

		for (final com.collibra.dgc.core.model.representation.Term term : terms) {

			AttributeType at = buildAttributeType(term);
			atList.add(at);
		}

		return ats;
	}

	/* ATTRIBUTE */

	private void setAttributeReferenceAttribute(AttributeReference ar,
			final com.collibra.dgc.core.model.representation.Attribute attribute) {

		setResourceAttribute(ar, attribute);

		ar.setLabelReference(buildTermReference(attribute.getLabel()));
		ar.setShortStringValue(attribute.getValue());
	}

	private void setAttributeAttribute(Attribute a, final com.collibra.dgc.core.model.representation.Attribute attribute) {

		setAttributeReferenceAttribute(a, attribute);

		a.setOwner(buildRepresentationReference(attribute.getOwner()));
	}

	public AttributeReference buildAttributeReference(
			final com.collibra.dgc.core.model.representation.Attribute attribute) {

		if (attribute instanceof com.collibra.dgc.core.model.representation.StringAttribute)
			return buildStringAttributeReference((com.collibra.dgc.core.model.representation.StringAttribute) attribute);

		else if (attribute instanceof com.collibra.dgc.core.model.representation.SingleValueListAttribute)
			return buildSingleValueListAttributeReference((com.collibra.dgc.core.model.representation.SingleValueListAttribute) attribute);

		else if (attribute instanceof com.collibra.dgc.core.model.representation.MultiValueListAttribute)
			return buildMultiValueListAttributeReference((com.collibra.dgc.core.model.representation.MultiValueListAttribute) attribute);

		return null;
	}

	public StringAttributeReference buildStringAttributeReference(
			final com.collibra.dgc.core.model.representation.StringAttribute stringAttribute) {

		if (stringAttribute == null)
			return null;

		StringAttributeReference sar = factory.createStringAttributeReference();

		setAttributeReferenceAttribute(sar, stringAttribute);

		sar.setKind(com.collibra.dgc.core.model.representation.StringAttribute.class.getSimpleName());

		sar.setLongExpression(stringAttribute.getLongExpression());

		return sar;
	}

	public SingleValueListAttributeReference buildSingleValueListAttributeReference(
			final com.collibra.dgc.core.model.representation.SingleValueListAttribute singleValueListAttribute) {

		SingleValueListAttributeReference svlar = factory.createSingleValueListAttributeReference();

		setAttributeReferenceAttribute(svlar, singleValueListAttribute);

		svlar.setKind(com.collibra.dgc.core.model.representation.SingleValueListAttribute.class.getSimpleName());

		svlar.setValue(singleValueListAttribute.getValue());

		return svlar;
	}

	public MultiValueListAttributeReference buildMultiValueListAttributeReference(
			final com.collibra.dgc.core.model.representation.MultiValueListAttribute multiValueListAttribute) {

		MultiValueListAttributeReference mvlar = factory.createMultiValueListAttributeReference();

		setAttributeReferenceAttribute(mvlar, multiValueListAttribute);

		mvlar.setKind(com.collibra.dgc.core.model.representation.MultiValueListAttribute.class.getSimpleName());

		Collection<String> allowedValues = multiValueListAttribute.getValues();

		if (allowedValues != null && !allowedValues.isEmpty())
			mvlar.getValues().addAll(multiValueListAttribute.getValues());

		return mvlar;
	}

	public AttributeReferences buildAttributeReferences(
			final Collection<com.collibra.dgc.core.model.representation.Attribute> attributes) {

		AttributeReferences ars = factory.createAttributeReferences();
		List<AttributeReference> arList = ars.getAttributeReferences();

		for (final com.collibra.dgc.core.model.representation.Attribute attribute : attributes) {

			AttributeReference ar = buildAttributeReference(attribute);
			arList.add(ar);
		}

		return ars;
	}

	public AttributeReferences buildStringAttributeReferences(
			final Collection<com.collibra.dgc.core.model.representation.StringAttribute> attributes) {

		AttributeReferences ars = factory.createAttributeReferences();
		List<AttributeReference> arList = ars.getAttributeReferences();

		for (final com.collibra.dgc.core.model.representation.StringAttribute attribute : attributes) {

			AttributeReference ar = buildStringAttributeReference(attribute);
			arList.add(ar);
		}

		return ars;
	}

	public Attribute buildAttribute(final com.collibra.dgc.core.model.representation.Attribute attribute) {

		if (attribute instanceof com.collibra.dgc.core.model.representation.StringAttribute)
			return buildStringAttribute((com.collibra.dgc.core.model.representation.StringAttribute) attribute);

		else if (attribute instanceof com.collibra.dgc.core.model.representation.SingleValueListAttribute)
			return buildSingleValueListAttribute((com.collibra.dgc.core.model.representation.SingleValueListAttribute) attribute);

		else if (attribute instanceof com.collibra.dgc.core.model.representation.MultiValueListAttribute)
			return buildMultiValueListAttribute((com.collibra.dgc.core.model.representation.MultiValueListAttribute) attribute);

		return null;
	}

	public StringAttribute buildStringAttribute(
			final com.collibra.dgc.core.model.representation.StringAttribute stringAttribute) {

		if (stringAttribute == null)
			return null;

		StringAttribute sa = factory.createStringAttribute();

		setAttributeAttribute(sa, stringAttribute);

		sa.setKind(com.collibra.dgc.core.model.representation.StringAttribute.class.getName());

		sa.setLongExpression(stringAttribute.getLongExpression());

		return sa;
	}

	public SingleValueListAttribute buildSingleValueListAttribute(
			final com.collibra.dgc.core.model.representation.SingleValueListAttribute singleValueListAttribute) {

		SingleValueListAttribute svla = factory.createSingleValueListAttribute();

		setAttributeAttribute(svla, singleValueListAttribute);

		svla.setKind(com.collibra.dgc.core.model.representation.SingleValueListAttribute.class.getName());

		svla.setValue(singleValueListAttribute.getValue());

		return svla;
	}

	public MultiValueListAttribute buildMultiValueListAttribute(
			final com.collibra.dgc.core.model.representation.MultiValueListAttribute multiValueListAttribute) {

		MultiValueListAttribute mvla = factory.createMultiValueListAttribute();

		setAttributeAttribute(mvla, multiValueListAttribute);

		mvla.setKind(com.collibra.dgc.core.model.representation.MultiValueListAttribute.class.getName());

		Collection<String> allowedValues = multiValueListAttribute.getValues();

		if (allowedValues != null && !allowedValues.isEmpty())
			mvla.getValues().addAll(multiValueListAttribute.getValues());

		return mvla;
	}

	public Attributes buildAttributes(final Collection<com.collibra.dgc.core.model.representation.Attribute> attributes) {

		Attributes as = factory.createAttributes();
		List<Attribute> aList = as.getAttributes();

		for (final com.collibra.dgc.core.model.representation.Attribute attribute : attributes) {

			Attribute a = buildAttribute(attribute);
			aList.add(a);
		}

		return as;
	}

	public RelationReference buildRelationReference(final com.collibra.dgc.core.model.relation.Relation relation) {

		if (relation == null)
			return null;

		RelationReference rel = factory.createRelationReference();

		rel.setTypeReference(buildBinaryFactTypeFormReference(relation.getType()));
		rel.setSourceReference(buildTermReference(relation.getSource()));
		rel.setTargetReference(buildTermReference(relation.getTarget()));
		rel.setStatusReference(buildTermReference(relation.getStatus()));

		if (rel.getStartingDate() != null)
			rel.setStartingDate(getXMLCalendar(relation.getStartingDate()));

		if (rel.getEndingDate() != null)
			rel.setEndingDate(getXMLCalendar(relation.getEndingDate()));

		rel.setIsGenerated(relation.getIsGenerated());

		return rel;
	}

	public Relation buildRelation(final com.collibra.dgc.core.model.relation.Relation relation) {

		if (relation == null)
			return null;

		Relation rel = factory.createRelation();

		rel.setTypeReference(buildBinaryFactTypeFormReference(relation.getType()));
		rel.setSourceReference(buildTermReference(relation.getSource()));
		rel.setTargetReference(buildTermReference(relation.getTarget()));
		rel.setStatusReference(buildTermReference(relation.getStatus()));

		if (rel.getStartingDate() != null)
			rel.setStartingDate(getXMLCalendar(relation.getStartingDate()));

		if (rel.getEndingDate() != null)
			rel.setEndingDate(getXMLCalendar(relation.getEndingDate()));

		rel.setIsGenerated(relation.getIsGenerated());

		return rel;
	}

	public Relations buildRelations(final Collection<com.collibra.dgc.core.model.relation.Relation> relations) {
		Relations rels = factory.createRelations();
		List<Relation> xmlRelations = rels.getRelations();

		for (com.collibra.dgc.core.model.relation.Relation rel : relations) {
			xmlRelations.add(buildRelation(rel));
		}

		return rels;
	}

	/* CONFIGURATION */

	public Configuration buildConfiguration(Map<String, String> properties) {
		Configuration c = factory.createConfiguration();
		List<ConfigProperty> configProperties = c.getConfigElements();

		for (Entry<String, String> e : properties.entrySet()) {
			ConfigProperty p = factory.createConfigProperty();
			p.setKey(e.getKey());
			p.getValues().add(e.getValue());
			configProperties.add(p);
		}

		return c;
	}

	public Configuration buildConfiguration(MultivaluedMap<String, String> properties) {
		Configuration c = factory.createConfiguration();
		List<ConfigProperty> configProperties = c.getConfigElements();

		for (Entry<String, List<String>> e : properties.entrySet()) {
			ConfigProperty p = factory.createConfigProperty();
			p.setKey(e.getKey());
			p.getValues().addAll(e.getValue());
			configProperties.add(p);
		}

		return c;
	}

	public Configuration buildIntegerConfiguration(MultivaluedMap<String, Integer> properties) {
		Configuration c = factory.createConfiguration();
		List<ConfigProperty> configProperties = c.getConfigElements();

		for (Entry<String, List<Integer>> e : properties.entrySet()) {
			ConfigProperty p = factory.createConfigProperty();
			p.setKey(e.getKey());
			for (Integer i : e.getValue())
				p.getValues().add(i.toString());
			configProperties.add(p);
		}

		return c;
	}

	public Configuration buildBooleanConfiguration(MultivaluedMap<String, Boolean> properties) {
		Configuration c = factory.createConfiguration();
		List<ConfigProperty> configProperties = c.getConfigElements();

		for (Entry<String, List<Boolean>> e : properties.entrySet()) {
			ConfigProperty p = factory.createConfigProperty();
			p.setKey(e.getKey());
			for (Boolean b : e.getValue())
				p.getValues().add(b.toString());
			configProperties.add(p);
		}

		return c;
	}

	public Configuration buildConfiguration(String path, String value) {
		Configuration c = factory.createConfiguration();
		List<ConfigProperty> configProperties = c.getConfigElements();

		ConfigProperty p = factory.createConfigProperty();
		p.setKey(path);
		p.getValues().add(value);
		configProperties.add(p);

		return c;
	}

	public Configuration buildConfiguration(String path, Collection<String> values) {
		Configuration c = factory.createConfiguration();
		List<ConfigProperty> configProperties = c.getConfigElements();

		ConfigProperty p = factory.createConfigProperty();
		p.setKey(path);
		p.getValues().addAll(values);
		configProperties.add(p);

		return c;
	}

	public Configuration buildConfiguration(String path, Integer value) {
		Configuration c = factory.createConfiguration();
		List<ConfigProperty> configProperties = c.getConfigElements();

		ConfigProperty p = factory.createConfigProperty();
		p.setKey(path);
		p.getValues().add(value.toString());
		configProperties.add(p);

		return c;
	}

	public Configuration buildConfiguration(String path, Boolean value) {
		Configuration c = factory.createConfiguration();
		List<ConfigProperty> configProperties = c.getConfigElements();

		ConfigProperty p = factory.createConfigProperty();
		p.setKey(path);
		if (value)
			p.getValues().add("true");
		else
			p.getValues().add("false");
		configProperties.add(p);

		return c;
	}

	public AttributeAndRelationTypesConfigurationCategory buildAttributeAndRelationTypesConfigurationCategory(
			com.collibra.dgc.core.model.configuration.AttributeAndRelationTypesConfigurationCategory cc) {
		AttributeAndRelationTypesConfigurationCategory confCat = factory
				.createAttributeAndRelationTypesConfigurationCategory();

		confCat.setName(cc.getName());
		confCat.setDescription(cc.getDescription());
		confCat.getRepresentationReferences().addAll(
				buildRepresentationReferences(cc.getAttributeAndRelationTypes()).getRepresentationReferences());

		return confCat;
	}

	public AttributeAndRelationTypesConfigurationCategories buildAttributeAndRelationTypesConfigurationCategories(
			List<com.collibra.dgc.core.model.configuration.AttributeAndRelationTypesConfigurationCategory> ccs) {
		AttributeAndRelationTypesConfigurationCategories confCats = factory
				.createAttributeAndRelationTypesConfigurationCategories();

		List<AttributeAndRelationTypesConfigurationCategory> cats = confCats
				.getAttributeAndRelationTypesConfigurationCategories();
		for (com.collibra.dgc.core.model.configuration.AttributeAndRelationTypesConfigurationCategory cc : ccs)
			cats.add(buildAttributeAndRelationTypesConfigurationCategory(cc));

		return confCats;
	}

	protected XMLGregorianCalendarImpl getXMLCalendar(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return new XMLGregorianCalendarImpl(cal);
	}

	/* User Phone */
	public Phone buildPhone(final com.collibra.dgc.core.model.user.PhoneNumber phoneNumber) {

		Phone phoneReference = factory.createPhone();
		setPhoneAttributes(phoneReference, phoneNumber);

		return phoneReference;
	}

	public void setPhoneAttributes(Phone phone, PhoneNumber phoneNumber) {
		setResourceAttribute(phone, phoneNumber);
		phone.setNumber(phoneNumber.getPhoneNumber());
		phone.setPhoneType(buildPhoneType(phoneNumber.getPhoneType()));

	}

	private PhoneType buildPhoneType(com.collibra.dgc.core.model.user.PhoneType phoneType) {
		return PhoneType.fromValue(phoneType.toString());
	}

	public PhoneCollection buildPhones(final Collection<PhoneNumber> phoneNumbers) {
		PhoneCollection phoneCollection = factory.createPhoneCollection();
		for (PhoneNumber phone : phoneNumbers) {
			phoneCollection.getPhones().add(buildPhone(phone));
		}
		return phoneCollection;
	}

	/* User Address */
	public Address buildAddress(final com.collibra.dgc.core.model.user.Address address) {

		Address addressReference = factory.createAddress();
		setAddressAttributes(addressReference, address);

		return addressReference;
	}

	public void setAddressAttributes(Address addressReference, com.collibra.dgc.core.model.user.Address address) {
		setResourceAttribute(addressReference, address);
		addressReference.setCity(address.getCity());
		addressReference.setCountry(address.getCountry());
		addressReference.setNumber(address.getNumber());
		addressReference.setProvince(address.getProvince());
		addressReference.setStreet(address.getStreet());
		addressReference.setAddressType(buildAddressType(address.getAddressType()));

	}

	private AddressType buildAddressType(com.collibra.dgc.core.model.user.AddressType addressType) {
		return AddressType.fromValue(addressType.toString());
	}

	public AddressCollection buildAddresses(final Collection<com.collibra.dgc.core.model.user.Address> addresses) {
		AddressCollection ac = factory.createAddressCollection();
		for (com.collibra.dgc.core.model.user.Address address : addresses) {
			ac.getAddresses().add(buildAddress(address));
		}
		return ac;
	}

	/* User Website */
	public Website buildWebsite(final com.collibra.dgc.core.model.user.Website website) {

		Website websiteReference = factory.createWebsite();
		setWebsiteAttributes(websiteReference, website);

		return websiteReference;
	}

	public void setWebsiteAttributes(Website websiteReference, com.collibra.dgc.core.model.user.Website website) {
		setResourceAttribute(websiteReference, website);
		websiteReference.setUrl(website.getUrl());
		websiteReference.setType(buildWebsiteType(website.getWebsiteType()));

	}

	private WebsiteType buildWebsiteType(com.collibra.dgc.core.model.user.WebsiteType websiteType) {
		return WebsiteType.fromValue(websiteType.toString());
	}

	public WebsiteCollection buildWebsites(final Collection<com.collibra.dgc.core.model.user.Website> websites) {
		WebsiteCollection wc = factory.createWebsiteCollection();
		for (com.collibra.dgc.core.model.user.Website website : websites) {
			wc.getWebsites().add(buildWebsite(website));
		}
		return wc;
	}

	/* User IM */
	public InstantMessagingAccount buildInstantMessagingAccount(
			final com.collibra.dgc.core.model.user.InstantMessagingAccount imAccount) {

		InstantMessagingAccount instantMessagingAccount = factory.createInstantMessagingAccount();
		setInstantMessagingAccountAttributes(instantMessagingAccount, imAccount);

		return instantMessagingAccount;
	}

	public void setInstantMessagingAccountAttributes(InstantMessagingAccount instantMessagingAccount,
			com.collibra.dgc.core.model.user.InstantMessagingAccount imAccount) {
		setResourceAttribute(instantMessagingAccount, imAccount);
		instantMessagingAccount.setAccount(imAccount.getAccount());
		instantMessagingAccount.setInstantMessagingAccountType(buildInstantMessagingAccountType(imAccount
				.getInstantMessagingAccountType()));

	}

	private InstantMessagingAccountType buildInstantMessagingAccountType(
			com.collibra.dgc.core.model.user.InstantMessagingAccountType imatype) {
		return InstantMessagingAccountType.fromValue(imatype.toString());
	}

	public InstantMessagingAccountCollection buildInstantMessagingAccounts(
			final Collection<com.collibra.dgc.core.model.user.InstantMessagingAccount> instantMessagingAccounts) {
		InstantMessagingAccountCollection imac = factory.createInstantMessagingAccountCollection();
		for (com.collibra.dgc.core.model.user.InstantMessagingAccount ima : instantMessagingAccounts) {
			imac.getInstantMessagingAccounts().add(buildInstantMessagingAccount(ima));

		}
		return imac;
	}

	/* User */
	public User buildUser(final com.collibra.dgc.core.model.user.User user) {
		User u = factory.createUser();
		setUserAttributes(u, user);
		return u;
	}

	private void setUserAttributes(User u, com.collibra.dgc.core.model.user.User user) {
		// TODO RESOURCE STUFF WHEN user is a resource as well.
		if (user.getAddresses() != null)
			u.setAddresses(buildAddresses(user.getAddresses()));
		if (user.getPhoneNumbers() != null)
			u.setPhoneNumbers(buildPhones(user.getPhoneNumbers()));
		if (user.getInstantMessagingAccounts() != null)
			u.setInstantMessagingAccounts(buildInstantMessagingAccounts(user.getInstantMessagingAccounts()));
		if (user.getWebsites() != null)
			u.setWebsites(buildWebsites(user.getWebsites()));

		u.setUserName(user.getUserName());
		u.setEmailAddress(user.getEmailAddress());
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		u.setLanguage(user.getLanguage());
	}

	public UserReference buildUserReference(final UserData userData) {
		UserReference ur = factory.createUserReference();
		setUserReferenceAttributes(ur, userData);
		return ur;
	}

	private void setUserReferenceAttributes(UserReference ur, UserData userData) {
		ur.setFirstName(userData.getFirstName());
		ur.setLanguage(userData.getLanguage());
		ur.setLastName(userData.getLastName());
		ur.setEmailAddress(userData.getEmailAddress());
		ur.setUserName(userData.getUserName());
	}

	/* Role */

	public RoleReference buildRoleReference(com.collibra.dgc.core.model.user.Role role) {
		RoleReference roleReference = factory.createRoleReference();
		setRoleReferenceAttributes(roleReference, role);
		return roleReference;
	}

	private void setRoleReferenceAttributes(RoleReference roleReference, Role role) {
		roleReference.setGlobal(role.isGlobal());
		roleReference.setRoleName(role.getName());
	}

	public UserCollection buildUserCollection(Collection<com.collibra.dgc.core.model.user.User> list) {
		UserCollection uc = factory.createUserCollection();
		for (UserData user : list) {
			uc.getUsers().add(buildUserReference(user));
		}
		return uc;

	}

	/* Member */

	public MemberReference buildMemberReference(com.collibra.dgc.core.model.user.Member member) {
		MemberReference memberReference = factory.createMemberReference();
		setMemberReferenceAttributes(memberReference, member);
		return memberReference;
	}

	private void setMemberReferenceAttributes(MemberReference memberReference, Member member) {
		memberReference.setGroup(member.isGroup());
		memberReference.setResource(member.getResourceId());
		memberReference.setOwnerId(member.getResourceId());
		memberReference.setRole(buildRoleReference(member.getRole()));
	}

	/* Group */

	public GroupReference buildGroupReference(com.collibra.dgc.core.model.user.Group group) {
		GroupReference groupReference = factory.createGroupReference();
		setGroupReferenceAttributes(groupReference, group);
		return groupReference;
	}

	private void setGroupReferenceAttributes(GroupReference groupReference, com.collibra.dgc.core.model.user.Group group) {
		setResourceAttribute(groupReference, group);
		groupReference.setGroupName(group.getGroupName());
	}

	public Group buildGroup(com.collibra.dgc.core.model.user.Group group) {
		Group groupReference = factory.createGroup();
		setGroupAttributes(groupReference, group);
		return groupReference;
	}

	private void setGroupAttributes(Group group, com.collibra.dgc.core.model.user.Group gr) {
		setResourceAttribute(group, gr);
		group.setGroupName(gr.getGroupName());
		List<com.collibra.dgc.core.model.user.User> users = new ArrayList<com.collibra.dgc.core.model.user.User>();
		for (GroupMembership gm : gr.getGroupMemberships()) {
			users.add(gm.getUser());
		}
		group.setUsers(buildUserCollection(users));
	}
}
