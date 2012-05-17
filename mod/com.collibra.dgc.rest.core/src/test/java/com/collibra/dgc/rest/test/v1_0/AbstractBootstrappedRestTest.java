package com.collibra.dgc.rest.test.v1_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.collibra.dgc.core.component.CommunityComponent;
import com.collibra.dgc.core.component.ReportComponent;
import com.collibra.dgc.core.component.SearchComponent;
import com.collibra.dgc.core.component.VocabularyComponent;
import com.collibra.dgc.core.component.attribute.AttributeComponent;
import com.collibra.dgc.core.component.attribute.AttributeTypeComponent;
import com.collibra.dgc.core.component.representation.BinaryFactTypeFormComponent;
import com.collibra.dgc.core.component.representation.CharacteristicFormComponent;
import com.collibra.dgc.core.component.representation.RepresentationComponent;
import com.collibra.dgc.rest.core.v1_0.dto.Attribute;
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
import com.collibra.dgc.rest.core.v1_0.dto.MultiValueListAttribute;
import com.collibra.dgc.rest.core.v1_0.dto.MultiValueListAttributeReference;
import com.collibra.dgc.rest.core.v1_0.dto.ObjectFactory;
import com.collibra.dgc.rest.core.v1_0.dto.Representation;
import com.collibra.dgc.rest.core.v1_0.dto.RepresentationReference;
import com.collibra.dgc.rest.core.v1_0.dto.RepresentationReferences;
import com.collibra.dgc.rest.core.v1_0.dto.Representations;
import com.collibra.dgc.rest.core.v1_0.dto.Resource;
import com.collibra.dgc.rest.core.v1_0.dto.SearchResultItem;
import com.collibra.dgc.rest.core.v1_0.dto.SearchResultItems;
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
import com.collibra.dgc.rest.core.v1_0.dto.Vocabularies;
import com.collibra.dgc.rest.core.v1_0.dto.Vocabulary;
import com.collibra.dgc.rest.core.v1_0.dto.VocabularyReference;
import com.collibra.dgc.rest.core.v1_0.dto.VocabularyReferences;

@Ignore
public class AbstractBootstrappedRestTest extends AbstractBootstrappedTest {

	private final ObjectFactory factory = new ObjectFactory();

	// All the autowired components

	@Autowired
	protected CommunityComponent communityComponent;

	@Autowired
	protected BinaryFactTypeFormComponent binaryFactTypeFormComponent;

	@Autowired
	protected CharacteristicFormComponent characteristicFormComponent;

	@Autowired
	protected SearchComponent searchComponent;

	@Autowired
	protected VocabularyComponent vocabularyComponent;

	@Autowired
	protected AttributeComponent attributeComponent;

	@Autowired
	protected ReportComponent reportComponent;

	@Autowired
	@Qualifier("RepresentationComponentImpl")
	private RepresentationComponent representationComponent;

	@Autowired
	protected AttributeTypeComponent attributeTypeComponent;

	// Test Object methods

	/* SEARCH RESULT ITEM */

	/**
	 * Check if both search result item objects from the REST service and the public API (component) correspond.
	 * 
	 * @param sri The {@link SearchResultItem} object from the REST service
	 * @param searchResultItem The {@link com.collibra.dgc.core.dto.search.SearchResultItem searchResultItem} object
	 *            from the core DTO
	 * @param nullable Set if both objects can be null (true) or not (false)
	 */
	protected void testSearchResultItem(final SearchResultItem sri,
			final com.collibra.dgc.core.dto.search.SearchResultItem searchResultItem, final boolean nullable) {

		// Assert if both objects are null or not
		if (testNullOrNotNullObject(sri, searchResultItem, nullable))
			return;

		// Assert equals every attributes of both objects
		assertEquals(sri.getResourceId(), searchResultItem.getResourceID().toString());
		assertEquals(sri.getName(), searchResultItem.getName());
		assertEquals(sri.getType().value(), searchResultItem.getType());
		assertEquals(sri.getScore(), searchResultItem.getScore(), 0);
		assertEquals(sri.getAttribute(), searchResultItem.getAttribute());

		// Assert identical parent search result item
		testSearchResultItem(sri.getParent(), searchResultItem.getParent(), true);
	}

	/**
	 * Check if both collections of search result item objects correspond.
	 * 
	 * @param sris The {@link SearchResultItems} object from the REST service
	 * @param searchResultItems The {@link Collection} of {@link com.collibra.dgc.core.dto.search.SearchResultItem}
	 *            object from the core DTO
	 */
	protected void testSearchResultItems(final SearchResultItems sris,
			final Collection<com.collibra.dgc.core.dto.search.SearchResultItem> searchResultItems) {

		// Assert if both objects are null or not
		testNullOrNotNullObject(sris, searchResultItems, false);

		// Assert list of SearchResultItem is not null and the same size as the searchResultItems
		// collection
		assertNotNull(sris.getSearchResultItems());
		testCollectionsWithSameSize(sris.getSearchResultItems(), searchResultItems);

		// Convert SearchResultItems to a Map
		final Map<String, SearchResultItem> sriMap = convertToMap(sris);

		// For each search result item test if object represent the same resource
		for (final com.collibra.dgc.core.dto.search.SearchResultItem searchResultItem : searchResultItems) {

			String key = searchResultItem.getResourceID().toString();
			assertTrue(sriMap.containsKey(key));

			testSearchResultItem(sriMap.get(key), searchResultItem, false);
		}
	}

	/**
	 * Convert {@link SearchResultItems} to a map of ({@link String} id, {@link SearchResultItem}).
	 * 
	 * @param sris {@link SearchResultItems}
	 * @return A {@link Map} of ({@link String} id, {@link SearchResultItem})
	 */
	private Map<String, SearchResultItem> convertToMap(SearchResultItems sris) {

		Map<String, SearchResultItem> sriMap = new HashMap<String, SearchResultItem>();

		for (SearchResultItem sri : sris.getSearchResultItems())
			sriMap.put(sri.getResourceId(), sri);

		return sriMap;
	}

	/* RESOURCE */

	/**
	 * Test the attributes of a resource.
	 * 
	 * @param r The {@link Resource} of the REST service
	 * @param resource The resource of the core model
	 */
	protected void testResourceAttribute(final Resource r, final com.collibra.dgc.core.model.Resource resource) {

		assertEquals(resource.getId().toString(), r.getResourceId());
		assertEquals(resource.isLocked(), r.isLocked());
	}

	/* COMMUNITY */

	/**
	 * Test the attributes of a community.
	 * 
	 * @param cr The {@link CommunityReference} of the REST service.
	 * @param community
	 */
	protected void testCommunityAttribute(final CommunityReference cr,
			final com.collibra.dgc.core.model.community.Community community) {

		testResourceAttribute(cr, community);

		assertEquals(community.getName(), cr.getName());
		assertEquals(community.getUri(), cr.getUri());
		assertEquals(community.getLanguage(), cr.getLanguage());
		assertEquals(community.isSBVR(), cr.isSbvr());
		assertEquals(community.isMeta(), cr.isMeta());
	}

	/**
	 * Check if both community correspond.
	 * 
	 * @param cr
	 * @param community
	 * @param nullable Set if the community can be null (<code>true</code>) or not (<code>false</code>)
	 */
	protected void testCommunityReference(final CommunityReference cr,
			final com.collibra.dgc.core.model.community.Community community, final boolean nullable) {

		if (testNullOrNotNullObject(cr, community, nullable))
			return;

		testCommunityAttribute(cr, community);
	}

	/**
	 * Check if both collections of community correspond.
	 * 
	 * @param crs
	 * @param communities
	 */
	protected void testCommunityReferences(final CommunityReferences crs,
			final Collection<com.collibra.dgc.core.model.community.Community> communities) {

		testNullOrNotNullObject(crs, communities, false);

		assertNotNull(crs.getCommunityReferences());
		testCollectionsWithSameSize(crs.getCommunityReferences(), communities);

		final Map<String, CommunityReference> crMap = convertToMap(crs);

		for (final com.collibra.dgc.core.model.community.Community community : communities) {

			String key = community.getId().toString();
			assertTrue(crMap.containsKey(key));

			testCommunityReference(crMap.get(key), community, false);
		}
	}

	/**
	 * Convert {@link CommunityReferences} to a {@link Map} of {@link CommunityReference}.
	 * 
	 * @param crs
	 * @return
	 */
	protected Map<String, CommunityReference> convertToMap(CommunityReferences crs) {

		Map<String, CommunityReference> crMap = new HashMap<String, CommunityReference>();

		for (CommunityReference cr : crs.getCommunityReferences())
			crMap.put(cr.getResourceId(), cr);

		return crMap;
	}

	/**
	 * Check if both communities correspond.
	 * 
	 * @param c
	 * @param community
	 * @param nullable Set if the community can be null (<code>true</code>) or not (<code>false</code>)
	 */
	protected void testCommunity(final Community c, final com.collibra.dgc.core.model.community.Community community,
			final boolean nullable) {

		// Test all the attributes
		testCommunityReference(c, community, nullable);

		// Test the parent community
		testCommunityReference(c.getParentReference(), community.getParentCommunity(), true);

		// Test the sub communities
		testCommunityReferences(c.getSubCommunityReferences(), community.getSubCommunities());

		// Test the vocabularies
		testVocabularyReferences(c.getVocabularyReferences(), community.getVocabularies());
	}

	/**
	 * Check if both collections of community correspond.
	 * 
	 * @param crs
	 * @param communities
	 */
	protected void testCommunities(final Communities cs,
			final Collection<com.collibra.dgc.core.model.community.Community> communities) {

		testNullOrNotNullObject(cs, communities, false);

		assertNotNull(cs.getCommunities());
		testCollectionsWithSameSize(cs.getCommunities(), communities);

		final Map<String, Community> cMap = convertToMap(cs);

		for (final com.collibra.dgc.core.model.community.Community community : communities) {

			String key = community.getId().toString();
			assertTrue(cMap.containsKey(key));

			testCommunity(cMap.get(key), community, false);
		}
	}

	/**
	 * Convert {@link CommunityReferences} to a {@link Map} of {@link CommunityReference}.
	 * 
	 * @param crs
	 * @return
	 */
	protected Map<String, Community> convertToMap(Communities cs) {

		Map<String, Community> cMap = new HashMap<String, Community>();

		for (Community c : cs.getCommunities())
			cMap.put(c.getResourceId(), c);

		return cMap;
	}

	/* VOCABULARY */

	protected void testVocabularyAttribute(final VocabularyReference vr,
			final com.collibra.dgc.core.model.representation.Vocabulary vocabulary) {

		testResourceAttribute(vr, vocabulary);

		assertEquals(vocabulary.getName(), vr.getName());
		assertEquals(vocabulary.getUri(), vr.getUri());
		assertEquals(vocabulary.isMeta(), vr.isMeta());
	}

	/**
	 * Check if both vocabulary objects from the REST service and the public API (component) correspond.
	 * 
	 * @param vr The {@link VocabularyReference} object from the REST service
	 * @param vocabulary The {@link com.collibra.dgc.core.model.representation.Vocabulary} object from the core model
	 * @param nullable Set if both objects can be null (true) or not (false)
	 */
	protected void testVocabularyReference(final VocabularyReference vr,
			final com.collibra.dgc.core.model.representation.Vocabulary vocabulary, final boolean nullable) {

		// Assert if both objects are null or not
		if (testNullOrNotNullObject(vr, vocabulary, nullable))
			return;

		// Assert equals every attributes of both objects
		testVocabularyAttribute(vr, vocabulary);
	}

	/**
	 * Check if both collections of vocabulary objects correspond.
	 * 
	 * @param vrs The {@link VocabularyReferences} object from the REST service
	 * @param vocabularies The {@link Collection} of {@link com.collibra.dgc.core.model.representation.Vocabulary}
	 *            object from the core model
	 */
	protected void testVocabularyReferences(final VocabularyReferences vrs,
			final Collection<com.collibra.dgc.core.model.representation.Vocabulary> vocabularies) {

		// Assert if both objects are null or not
		testNullOrNotNullObject(vrs, vocabularies, false);

		// Assert list of VocabularyReference is not null and the same size as the speechCommunities collection
		assertNotNull(vrs.getVocabularyReferences());
		testCollectionsWithSameSize(vrs.getVocabularyReferences(), vocabularies);

		// Convert VocabularyReferences to a Map
		final Map<String, VocabularyReference> vrMap = convertToMap(vrs);

		// For each vocabulary test if object represent the same resource
		for (final com.collibra.dgc.core.model.representation.Vocabulary vocabulary : vocabularies) {

			String key = vocabulary.getId().toString();
			assertTrue(vrMap.containsKey(key));

			testVocabularyReference(vrMap.get(key), vocabulary, false);
		}
	}

	/**
	 * Convert {@link VocabularyReferences} to a map of ({@link String} id, {@link VocabularyReference}).
	 * 
	 * @param vrs {@link VocabularyReferences}
	 * @return A {@link Map} of ({@link String} id, {@link VocabularyReference})
	 */
	private Map<String, VocabularyReference> convertToMap(VocabularyReferences vrs) {

		Map<String, VocabularyReference> vrMap = new HashMap<String, VocabularyReference>();

		for (VocabularyReference vr : vrs.getVocabularyReferences())
			vrMap.put(vr.getResourceId(), vr);

		return vrMap;
	}

	/**
	 * Check if both vocabulary objects from the REST service and the public API (component) correspond.
	 * 
	 * @param vr The {@link Vocabulary} object from the REST service
	 * @param vocabulary The {@link com.collibra.dgc.core.model.representation.Vocabulary} object from the core model
	 * @param nullable Set if both objects can be null (true) or not (false)
	 */
	protected void testVocabulary(final Vocabulary v,
			final com.collibra.dgc.core.model.representation.Vocabulary vocabulary, final boolean nullable) {

		// Assert if both objects are null or not
		if (testNullOrNotNullObject(v, vocabulary, nullable))
			return;

		// Assert equals every attributes of both objects
		testVocabularyAttribute(v, vocabulary);

		// Test representations
		testTermReferences(v.getTermReferences(), vocabulary.getTerms());
		testBinaryFactTypeFormReferences(v.getBinaryFactTypeFormReferences(), vocabulary.getBinaryFactTypeForms());
		testCharacteristicFormReferences(v.getCharacteristicFormReferences(), vocabulary.getCharacteristicForms());
	}

	/**
	 * Check if both collections of vocabulary objects correspond.
	 * 
	 * @param sps The {@link Vocabulary} object from the REST service
	 * @param speechCommunities The {@link Collection} of {@link com.collibra.dgc.core.model.representation.Vocabulary}
	 *            object from the core model
	 */
	protected void testVocabularies(final Vocabularies vs,
			final Collection<com.collibra.dgc.core.model.representation.Vocabulary> vocabularies) {

		// Assert if both objects are null or not
		testNullOrNotNullObject(vs, vocabularies, false);

		// Assert list of Vocabulary is not null and the same size as the speechCommunities collection
		assertNotNull(vs.getVocabularies());
		testCollectionsWithSameSize(vs.getVocabularies(), vocabularies);

		// Convert Vocabularies to a Map
		final Map<String, Vocabulary> vMap = convertToMap(vs);

		// For each vocabulary test if object represent the same resource
		for (final com.collibra.dgc.core.model.representation.Vocabulary vocabulary : vocabularies) {

			String key = vocabulary.getId().toString();
			assertTrue(vMap.containsKey(key));

			testVocabulary(vMap.get(key), vocabulary, false);
		}
	}

	/**
	 * Convert {@link Vocabularies} to a map of ({@link String} id, {@link Vocabulary}).
	 * 
	 * @param sps {@link Vocabularies}
	 * @return A {@link Map} of ({@link String} id, {@link Vocabulary})
	 */
	private Map<String, Vocabulary> convertToMap(Vocabularies vs) {

		Map<String, Vocabulary> vMap = new HashMap<String, Vocabulary>();

		for (Vocabulary v : vs.getVocabularies())
			vMap.put(v.getResourceId(), v);

		return vMap;
	}

	/* REPRESENTATION */

	protected void testRepresentationReferenceAttribute(final RepresentationReference rr,
			final com.collibra.dgc.core.model.representation.Representation representation) {

		testResourceAttribute(rr, representation);

		assertEquals(representation.getIsPreferred(), rr.isPreferred());
	}

	protected void testRepresentationAttribute(final Representation r,
			final com.collibra.dgc.core.model.representation.Representation representation) {

		testRepresentationReferenceAttribute(r, representation);

		testTermReference(r.getStatusReference(), representation.getStatus(), false);
		testVocabularyReference(r.getVocabularyReference(), representation.getVocabulary(), false);
		assertEquals(representation.getMeaning().getId().toString(), r.getConceptRId());
		testSimpleTerm(r.getConceptType(),
				representationComponent.getConceptType(representation.getId().toString()), false);
		testAttributeReferences(r.getAttributeReferences(), representation.getAttributes());
	}

	protected void testRepresentationReference(final RepresentationReference rr,
			final com.collibra.dgc.core.model.representation.Representation representation, final boolean nullable) {

		if (testNullOrNotNullObject(rr, representation, nullable))
			return;

		if (representation instanceof com.collibra.dgc.core.model.representation.Term)
			testTermReference((TermReference) rr, (com.collibra.dgc.core.model.representation.Term) representation,
					nullable);

		else if (representation instanceof com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm)
			testCharacteristicFormReference((CharacteristicFormReference) rr,
					(com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm) representation,
					nullable);

		else if (representation instanceof com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm)
			testBinaryFactTypeFormReference((BinaryFactTypeFormReference) rr,
					(com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm) representation,
					nullable);

		else
			fail("The type of representation is unknown.");
	}

	protected void testRepresentationReferences(final RepresentationReferences rrs,
			final Collection<com.collibra.dgc.core.model.representation.Representation> representations) {

		// Assert if both objects are null or not
		testNullOrNotNullObject(rrs, representations, false);

		// Assert list of BinaryFactTypeFormReference is not null and the same size as the binaryFactTypeForms
		// collection
		assertNotNull(rrs.getRepresentationReferences());
		testCollectionsWithSameSize(rrs.getRepresentationReferences(), representations);

		// Convert TermReferences to a Map
		final Map<String, RepresentationReference> rrMap = convertToMap(rrs);

		// For each binary fact type form, test if object represent the same resource
		for (final com.collibra.dgc.core.model.representation.Representation representation : representations) {

			String key = representation.getId().toString();
			assertTrue(rrMap.containsKey(key));

			testRepresentationReference(rrMap.get(key), representation, false);
		}
	}

	private Map<String, RepresentationReference> convertToMap(RepresentationReferences rrs) {

		Map<String, RepresentationReference> rrMap = new HashMap<String, RepresentationReference>();

		for (RepresentationReference rr : rrs.getRepresentationReferences())
			rrMap.put(rr.getResourceId(), rr);

		return rrMap;
	}

	protected void testRepresentation(final Representation r,
			final com.collibra.dgc.core.model.representation.Representation representation, final boolean nullable) {

		if (testNullOrNotNullObject(r, representation, nullable))
			return;

		if (representation instanceof com.collibra.dgc.core.model.representation.Term)
			testTerm((Term) r, (com.collibra.dgc.core.model.representation.Term) representation, nullable);

		else if (representation instanceof com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm)
			testCharacteristicForm((CharacteristicForm) r,
					(com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm) representation,
					nullable);

		else if (representation instanceof com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm)
			testBinaryFactTypeForm((BinaryFactTypeForm) r,
					(com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm) representation,
					nullable);

		else
			fail("The type of representation is unknown.");
	}

	protected void testRepresentations(final Representations rs,
			final Collection<com.collibra.dgc.core.model.representation.Representation> representations) {

		// Assert if both objects are null or not
		testNullOrNotNullObject(rs, representations, false);

		// Assert list of BinaryFactTypeFormReference is not null and the same size as the binaryFactTypeForms
		// collection
		assertNotNull(rs.getRepresentations());
		testCollectionsWithSameSize(rs.getRepresentations(), representations);

		// Convert TermReferences to a Map
		final Map<String, Representation> rMap = convertToMap(rs);

		// For each binary fact type form, test if object represent the same resource
		for (final com.collibra.dgc.core.model.representation.Representation representation : representations) {

			String key = representation.getId().toString();
			assertTrue(rMap.containsKey(key));

			testRepresentation(rMap.get(key), representation, false);
		}
	}

	private Map<String, Representation> convertToMap(Representations rs) {

		Map<String, Representation> rMap = new HashMap<String, Representation>();

		for (Representation r : rs.getRepresentations())
			rMap.put(r.getResourceId(), r);

		return rMap;
	}

	/* TERM */

	protected void testTermReferenceAttribute(final TermReference tr,
			final com.collibra.dgc.core.model.representation.Term term) {

		testRepresentationReferenceAttribute(tr, term);

		assertEquals(term.getSignifier(), tr.getSignifier());
	}

	/**
	 * Check if both term objects from the REST service and the public API (component) correspond.
	 * 
	 * @param tr The {@link TermReference} object from the REST service
	 * @param term The {@link com.collibra.dgc.core.model.representation.Term} object from the core model
	 * @param nullable Set if both objects can be null (true) or not (false)
	 */
	protected void testTermReference(final TermReference tr,
			final com.collibra.dgc.core.model.representation.Term term, final boolean nullable) {

		// Assert if both objects are null or not
		if (testNullOrNotNullObject(tr, term, nullable))
			return;

		// Assert equals every attributes of both objects
		testTermReferenceAttribute(tr, term);
	}

	/**
	 * Check if both collections of term objects correspond.
	 * 
	 * @param trs The {@link TermReferences} object from the REST service
	 * @param terms The {@link Collection} of {@link com.collibra.dgc.core.model.representation.Term} object from the
	 *            core model
	 */
	protected void testTermReferences(final TermReferences trs,
			final Collection<com.collibra.dgc.core.model.representation.Term> terms) {

		// Assert if both objects are null or not
		testNullOrNotNullObject(trs, terms, false);

		// Assert list of BinaryFactTypeFormReference is not null and the same size as the binaryFactTypeForms
		// collection
		assertNotNull(trs.getTermReferences());
		testCollectionsWithSameSize(trs.getTermReferences(), terms);

		// Convert TermReferences to a Map
		final Map<String, TermReference> vrMap = convertToMap(trs);

		// For each binary fact type form, test if object represent the same resource
		for (final com.collibra.dgc.core.model.representation.Term term : terms) {

			String key = term.getId().toString();
			assertTrue(vrMap.containsKey(key));

			testTermReference(vrMap.get(key), term, false);
		}
	}

	/**
	 * Convert {@link TermReferences} to a map of ({@link String} id, {@link TermReference}).
	 * 
	 * @param trs {@link TermReferences}
	 * @return A {@link Map} of ({@link String} id, {@link TermReference})
	 */
	private Map<String, TermReference> convertToMap(TermReferences trs) {

		Map<String, TermReference> trMap = new HashMap<String, TermReference>();

		for (TermReference tr : trs.getTermReferences())
			trMap.put(tr.getResourceId(), tr);

		return trMap;
	}

	protected void testSimpleTerm(final SimpleTerm sp, com.collibra.dgc.core.model.representation.Term term,
			final boolean nullable) {

		if (testNullOrNotNullObject(sp, term, nullable))
			return;

		testTermReferenceAttribute(sp, term);
		testVocabularyReference(sp.getVocabularyReference(), term.getVocabulary(), false);
	}

	protected void testSimpleTerms(final SimpleTerms sps,
			final Collection<com.collibra.dgc.core.model.representation.Term> terms) {

		testNullOrNotNullObject(sps, terms, false);

		assertNotNull(sps.getSimpleTerms());
		testCollectionsWithSameSize(sps.getSimpleTerms(), terms);

		Map<String, SimpleTerm> spMap = convertToMap(sps);

		for (final com.collibra.dgc.core.model.representation.Term term : terms) {

			String key = term.getId().toString();
			assertTrue(spMap.containsKey(key));

			testSimpleTerm(spMap.get(key), term, false);
		}
	}

	private Map<String, SimpleTerm> convertToMap(SimpleTerms sps) {

		Map<String, SimpleTerm> spMap = new HashMap<String, SimpleTerm>();

		for (SimpleTerm sp : sps.getSimpleTerms())
			spMap.put(sp.getResourceId(), sp);

		return spMap;
	}

	protected void testTerm(final Term t, final com.collibra.dgc.core.model.representation.Term term,
			final boolean nullable) {

		if (testNullOrNotNullObject(t, term, nullable))
			return;

		testRepresentationAttribute(t, term);

		String rId = term.getId().toString();

		assertEquals(term.getSignifier(), t.getSignifier());
		testCharacteristicFormReferences(t.getCharacteristicFormReferences(),
				characteristicFormComponent.getCharacteristicFormsContainingTerm(rId));
		testBinaryFactTypeFormReferences(t.getBinaryFactTypeFormReferences(),
				binaryFactTypeFormComponent.getBinaryFactTypeFormsContainingTerm(rId));
		testSimpleTerm((SimpleTerm) t.getGeneralConceptReference(),
				(com.collibra.dgc.core.model.representation.Term) representationComponent.getGeneralConcept(rId), false);

		// Specialized concepts
		List<com.collibra.dgc.core.model.representation.Term> specializedConcepts = new ArrayList<com.collibra.dgc.core.model.representation.Term>();
		for (com.collibra.dgc.core.model.representation.Representation representation : representationComponent
				.getSpecializedConcepts(rId, 10))
			specializedConcepts.add((com.collibra.dgc.core.model.representation.Term) representation);

		SimpleTerms stSpecializeConcepts = factory.createSimpleTerms();
		List<SimpleTerm> stSpecializeConceptList = stSpecializeConcepts.getSimpleTerms();
		for (RepresentationReference rr : t.getSpecializedConceptReferences().getRepresentationReferences())
			stSpecializeConceptList.add((SimpleTerm) rr);

		testSimpleTerms(stSpecializeConcepts, specializedConcepts);

		// Synonyms
		List<com.collibra.dgc.core.model.representation.Term> synonyms = new ArrayList<com.collibra.dgc.core.model.representation.Term>();
		for (com.collibra.dgc.core.model.representation.Representation representation : representationComponent
				.getSynonyms(rId))
			synonyms.add((com.collibra.dgc.core.model.representation.Term) representation);

		SimpleTerms stSynonyms = factory.createSimpleTerms();
		List<SimpleTerm> stSynonymList = stSynonyms.getSimpleTerms();
		for (RepresentationReference rr : t.getSynonymReferences().getRepresentationReferences())
			stSynonymList.add((SimpleTerm) rr);

		testSimpleTerms(stSynonyms, synonyms);
	}

	protected void testTerms(final Terms ts, final Collection<com.collibra.dgc.core.model.representation.Term> terms) {

		testNullOrNotNullObject(ts, terms, false);

		assertNotNull(ts.getTerms());
		testCollectionsWithSameSize(ts.getTerms(), terms);

		Map<String, Term> tMap = convertToMap(ts);

		for (final com.collibra.dgc.core.model.representation.Term term : terms) {

			String key = term.getId().toString();
			assertTrue(tMap.containsKey(key));

			testTerm(tMap.get(key), term, false);
		}
	}

	private Map<String, Term> convertToMap(Terms ts) {

		Map<String, Term> tMap = new HashMap<String, Term>();

		for (Term t : ts.getTerms())
			tMap.put(t.getResourceId(), t);

		return tMap;
	}

	/* BINARY FACT TYPE FORM */

	/**
	 * Check if both binary fact type form objects from the REST service and the public API (component) correspond.
	 * 
	 * @param bftfr The {@link BinaryFactTypeFormReference} object from the REST service
	 * @param binaryFactTypeForm The {@link com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm}
	 *            object from the core model
	 * @param nullable Set if both objects can be null (true) or not (false)
	 */
	protected void testBinaryFactTypeFormReference(final BinaryFactTypeFormReference bftfr,
			final com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm binaryFactTypeForm,
			final boolean nullable) {

		// Assert if both objects are null or not
		if (testNullOrNotNullObject(bftfr, binaryFactTypeForm, nullable))
			return;

		testRepresentationReferenceAttribute(bftfr, binaryFactTypeForm);

		// Assert equals every attributes of both objects
		assertEquals(bftfr.getRole(), binaryFactTypeForm.getRole());
		assertEquals(bftfr.getCoRole(), binaryFactTypeForm.getCoRole());

		testSimpleTerm(bftfr.getHeadTerm(), binaryFactTypeForm.getHeadTerm(), false);
		testSimpleTerm(bftfr.getTailTerm(), binaryFactTypeForm.getTailTerm(), false);
	}

	/**
	 * Check if both collections of binary fact type form objects correspond.
	 * 
	 * @param bftfrs The {@link BinaryFactTypeFormReferences} object from the REST service
	 * @param binaryFactTypeForms The {@link Collection} of
	 *            {@link com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm} object from the
	 *            core model
	 */
	protected void testBinaryFactTypeFormReferences(
			final BinaryFactTypeFormReferences bftfrs,
			final Collection<com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm> binaryFactTypeForms) {

		// Assert if both objects are null or not
		testNullOrNotNullObject(bftfrs, binaryFactTypeForms, false);

		// Assert list of BinaryFactTypeFormReference is not null and the same size as the binaryFactTypeForms
		// collection
		assertNotNull(bftfrs.getBinaryFactTypeFormReferences());
		testCollectionsWithSameSize(bftfrs.getBinaryFactTypeFormReferences(), binaryFactTypeForms);

		// Convert BinaryFactTypeFormReferences to a Map
		final Map<String, BinaryFactTypeFormReference> bftfrMap = convertToMap(bftfrs);

		// For each binary fact type form, test if object represent the same resource
		for (final com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm binaryFactTypeForm : binaryFactTypeForms) {

			String key = binaryFactTypeForm.getId().toString();
			assertTrue(bftfrMap.containsKey(key));

			testBinaryFactTypeFormReference(bftfrMap.get(key), binaryFactTypeForm, false);
		}
	}

	/**
	 * Convert {@link BinaryFactTypeFormReferences} to a map of ({@link String} id, {@link BinaryFactTypeFormReference}
	 * ).
	 * 
	 * @param bftfrs {@link BinaryFactTypeFormReferences}
	 * @return A {@link Map} of ({@link String} id, {@link BinaryFactTypeFormReference})
	 */
	private Map<String, BinaryFactTypeFormReference> convertToMap(BinaryFactTypeFormReferences bftfrs) {

		Map<String, BinaryFactTypeFormReference> bftfrMap = new HashMap<String, BinaryFactTypeFormReference>();

		for (BinaryFactTypeFormReference bftfr : bftfrs.getBinaryFactTypeFormReferences())
			bftfrMap.put(bftfr.getResourceId(), bftfr);

		return bftfrMap;
	}

	protected void testBinaryFactTypeForm(final BinaryFactTypeForm bftf,
			final com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm binaryFactTypeForm,
			final boolean nullable) {

		// Assert if both objects are null or not
		if (testNullOrNotNullObject(bftf, binaryFactTypeForm, nullable))
			return;

		testRepresentationAttribute(bftf, binaryFactTypeForm);

		String rId = binaryFactTypeForm.getId().toString();

		// Assert equals every attributes of both objects
		assertEquals(bftf.getRole(), binaryFactTypeForm.getRole());
		assertEquals(bftf.getCoRole(), binaryFactTypeForm.getCoRole());

		testSimpleTerm(bftf.getHeadTerm(), binaryFactTypeForm.getHeadTerm(), false);
		testSimpleTerm(bftf.getTailTerm(), binaryFactTypeForm.getTailTerm(), false);
		testBinaryFactTypeFormReference((BinaryFactTypeFormReference) bftf.getGeneralConceptReference(),
				(com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm) representationComponent
						.getGeneralConcept(rId), false);

		// Specialized concepts
		List<com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm> specializedConcepts = new ArrayList<com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm>();
		for (com.collibra.dgc.core.model.representation.Representation representation : representationComponent
				.getSpecializedConcepts(rId, 10))
			specializedConcepts
					.add((com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm) representation);

		BinaryFactTypeFormReferences bftfrSpecializeConcepts = factory.createBinaryFactTypeFormReferences();
		List<BinaryFactTypeFormReference> bftfrSpecializeConceptList = bftfrSpecializeConcepts
				.getBinaryFactTypeFormReferences();
		for (RepresentationReference rr : bftf.getSpecializedConceptReferences().getRepresentationReferences())
			bftfrSpecializeConceptList.add((BinaryFactTypeFormReference) rr);

		testBinaryFactTypeFormReferences(bftfrSpecializeConcepts, specializedConcepts);

		// Synonyms
		List<com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm> synonyms = new ArrayList<com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm>();
		for (com.collibra.dgc.core.model.representation.Representation representation : representationComponent
				.getSynonyms(rId))
			synonyms.add((com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm) representation);

		BinaryFactTypeFormReferences bftfrSynonyms = factory.createBinaryFactTypeFormReferences();
		List<BinaryFactTypeFormReference> bftfrSynonymList = bftfrSynonyms.getBinaryFactTypeFormReferences();
		for (RepresentationReference rr : bftf.getSynonymReferences().getRepresentationReferences())
			bftfrSynonymList.add((BinaryFactTypeFormReference) rr);

		testBinaryFactTypeFormReferences(bftfrSynonyms, synonyms);
	}

	protected void testBinaryFactTypeForms(
			final BinaryFactTypeForms bftfs,
			final Collection<com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm> binaryFactTypeForms) {

		// Assert if both objects are null or not
		testNullOrNotNullObject(bftfs, binaryFactTypeForms, false);

		// Assert list of BinaryFactTypeFormReference is not null and the same size as the binaryFactTypeForms
		// collection
		assertNotNull(bftfs.getBinaryFactTypeForms());
		testCollectionsWithSameSize(bftfs.getBinaryFactTypeForms(), binaryFactTypeForms);

		// Convert BinaryFactTypeFormReferences to a Map
		final Map<String, BinaryFactTypeForm> bftfMap = convertToMap(bftfs);

		// For each binary fact type form, test if object represent the same resource
		for (final com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm binaryFactTypeForm : binaryFactTypeForms) {

			String key = binaryFactTypeForm.getId().toString();
			assertTrue(bftfMap.containsKey(key));

			testBinaryFactTypeForm(bftfMap.get(key), binaryFactTypeForm, false);
		}
	}

	private Map<String, BinaryFactTypeForm> convertToMap(BinaryFactTypeForms bftfs) {

		Map<String, BinaryFactTypeForm> bftfMap = new HashMap<String, BinaryFactTypeForm>();

		for (BinaryFactTypeForm bftf : bftfs.getBinaryFactTypeForms())
			bftfMap.put(bftf.getResourceId(), bftf);

		return bftfMap;
	}

	/* CHARACTERISTIC FORM */

	/**
	 * Check if both characteristic form objects from the REST service and the public API (component) correspond.
	 * 
	 * @param cfr The {@link CharacteristicFormReference} object from the REST service
	 * @param binaryFactTypeForm The {@link com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm}
	 *            object from the core model
	 * @param nullable Set if both objects can be null (true) or not (false)
	 */
	protected void testCharacteristicFormReference(final CharacteristicFormReference cfr,
			final com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm characteristicForm,
			final boolean nullable) {

		// Assert if both objects are null or not
		if (testNullOrNotNullObject(cfr, characteristicForm, nullable))
			return;

		testRepresentationReferenceAttribute(cfr, characteristicForm);

		// Assert equals every attributes of both objects
		assertEquals(cfr.getRole(), characteristicForm.getRole());

		testSimpleTerm(cfr.getTerm(), characteristicForm.getTerm(), false);
	}

	/**
	 * Check if both collections of characteristic form objects correspond.
	 * 
	 * @param cfrs The {@link CharacteristicFormReferences} object from the REST service
	 * @param binaryFactTypeForms The {@link Collection} of
	 *            {@link com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm} object from the
	 *            core model
	 */
	protected void testCharacteristicFormReferences(
			final CharacteristicFormReferences cfrs,
			final Collection<com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm> characteristicForms) {

		// Assert if both objects are null or not
		testNullOrNotNullObject(cfrs, characteristicForms, false);

		// Assert list of CharacteristicFormReference is not null and the same size as the binaryFactTypeForms
		// collection
		assertNotNull(cfrs.getCharacteristicFormReferences());
		testCollectionsWithSameSize(cfrs.getCharacteristicFormReferences(), characteristicForms);

		// Convert CharacteristicFormReferences to a Map
		final Map<String, CharacteristicFormReference> cfrMap = convertToMap(cfrs);

		// For each characteristic form, test if object represent the same resource
		for (final com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm characteristicForm : characteristicForms) {

			String key = characteristicForm.getId().toString();
			assertTrue(cfrMap.containsKey(key));

			testCharacteristicFormReference(cfrMap.get(key), characteristicForm, false);
		}
	}

	/**
	 * Convert {@link CharacteristicFormReferences} to a map of ({@link String} id, {@link CharacteristicFormReference}
	 * ).
	 * 
	 * @param cfrs {@link CharacteristicFormReferences}
	 * @return A {@link Map} of ({@link String} id, {@link CharacteristicFormReference})
	 */
	private Map<String, CharacteristicFormReference> convertToMap(CharacteristicFormReferences cfrs) {

		Map<String, CharacteristicFormReference> cfrMap = new HashMap<String, CharacteristicFormReference>();

		for (CharacteristicFormReference cfr : cfrs.getCharacteristicFormReferences())
			cfrMap.put(cfr.getResourceId(), cfr);

		return cfrMap;
	}

	protected void testCharacteristicForm(final CharacteristicForm cf,
			final com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm characteristicForm,
			final boolean nullable) {

		// Assert if both objects are null or not
		if (testNullOrNotNullObject(cf, characteristicForm, nullable))
			return;

		testRepresentationAttribute(cf, characteristicForm);

		String rId = characteristicForm.getId().toString();

		// Assert equals every attributes of both objects
		assertEquals(cf.getRole(), characteristicForm.getRole());

		testSimpleTerm(cf.getTerm(), characteristicForm.getTerm(), false);
		testCharacteristicFormReference((CharacteristicFormReference) cf.getGeneralConceptReference(),
				(com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm) representationComponent
						.getGeneralConcept(rId), false);

		// Specialized concepts
		List<com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm> specializedConcepts = new ArrayList<com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm>();
		for (com.collibra.dgc.core.model.representation.Representation representation : representationComponent
				.getSpecializedConcepts(rId, 10))
			specializedConcepts
					.add((com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm) representation);

		CharacteristicFormReferences cfrSpecializeConcepts = factory.createCharacteristicFormReferences();
		List<CharacteristicFormReference> cfrSpecializeConceptList = cfrSpecializeConcepts
				.getCharacteristicFormReferences();
		for (RepresentationReference rr : cf.getSpecializedConceptReferences().getRepresentationReferences())
			cfrSpecializeConceptList.add((CharacteristicFormReference) rr);

		testCharacteristicFormReferences(cfrSpecializeConcepts, specializedConcepts);

		// Synonyms
		List<com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm> synonyms = new ArrayList<com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm>();
		for (com.collibra.dgc.core.model.representation.Representation representation : representationComponent
				.getSynonyms(rId))
			synonyms.add((com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm) representation);

		CharacteristicFormReferences cfrSynonyms = factory.createCharacteristicFormReferences();
		List<CharacteristicFormReference> cfrSynonymList = cfrSynonyms.getCharacteristicFormReferences();
		for (RepresentationReference rr : cf.getSynonymReferences().getRepresentationReferences())
			cfrSynonymList.add((CharacteristicFormReference) rr);

		testCharacteristicFormReferences(cfrSynonyms, synonyms);
	}

	protected void testCharacteristicForms(
			final CharacteristicForms cfs,
			final Collection<com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm> characteristicForms) {

		// Assert if both objects are null or not
		testNullOrNotNullObject(cfs, characteristicForms, false);

		// Assert list of CharacteristicFormReference is not null and the same size as the binaryFactTypeForms
		// collection
		assertNotNull(cfs.getCharacteristicForms());
		testCollectionsWithSameSize(cfs.getCharacteristicForms(), characteristicForms);

		// Convert CharacteristicFormReferences to a Map
		final Map<String, CharacteristicForm> cfMap = convertToMap(cfs);

		// For each characteristic form, test if object represent the same resource
		for (final com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm characteristicForm : characteristicForms) {

			String key = characteristicForm.getId().toString();
			assertTrue(cfMap.containsKey(key));

			testCharacteristicForm(cfMap.get(key), characteristicForm, false);
		}
	}

	private Map<String, CharacteristicForm> convertToMap(CharacteristicForms cfs) {

		Map<String, CharacteristicForm> cfMap = new HashMap<String, CharacteristicForm>();

		for (CharacteristicForm cf : cfs.getCharacteristicForms())
			cfMap.put(cf.getResourceId(), cf);

		return cfMap;
	}

	/* ATTRIBUTE TYPE */

	protected void testAttributeType(final AttributeType at,
			final com.collibra.dgc.core.model.representation.Term term, final boolean nullable) {

		if (testNullOrNotNullObject(at, term, nullable))
			return;

		testTermReferenceAttribute(at, term);

		String rId = term.getId().toString();
		String kind = at.getKind();

		assertEquals(attributeTypeComponent.getAttributeTypeKind(rId), kind);
		testAttributeReference(at.getDescriptionReference(), attributeTypeComponent.getAttributeTypeDescription(rId),
				true);

		if (kind.equals(com.collibra.dgc.core.model.representation.SingleValueListAttribute.class.getSimpleName())
				|| kind.equals(com.collibra.dgc.core.model.representation.MultiValueListAttribute.class.getSimpleName())) {

			Collection<String> allowedValues = attributeTypeComponent.getAllowedValues(rId);

			if (allowedValues == null)
				assertNull(at.getAllowedValues());
			else {

				testCollectionsWithSameSize(allowedValues, at.getAllowedValues());

				for (String allowedValue : allowedValues)
					assertTrue(at.getAllowedValues().contains(allowedValue));
			}
		}
	}

	protected void testAttributeTypes(final AttributeTypes ats,
			final Collection<com.collibra.dgc.core.model.representation.Term> terms) {

		// Assert if both objects are null or not
		testNullOrNotNullObject(ats, terms, false);

		// Assert list of AttributeTypeReference is not null and the same size as the binaryFactTypeForms
		// collection
		assertNotNull(ats.getAttributeTypes());
		testCollectionsWithSameSize(ats.getAttributeTypes(), terms);

		// Convert AttributeTypeReferences to a Map
		final Map<String, AttributeType> atMap = convertToMap(ats);

		// For each attribute type, test if object represent the same resource
		for (final com.collibra.dgc.core.model.representation.Term term : terms) {

			String key = term.getId().toString();
			assertTrue(atMap.containsKey(key));

			testAttributeType(atMap.get(key), term, false);
		}
	}

	private Map<String, AttributeType> convertToMap(AttributeTypes ats) {

		Map<String, AttributeType> atMap = new HashMap<String, AttributeType>();

		for (AttributeType at : ats.getAttributeTypes())
			atMap.put(at.getResourceId(), at);

		return atMap;
	}

	/* ATTRIBUTE */

	protected void testAttributeReferenceAttribute(final AttributeReference ar,
			final com.collibra.dgc.core.model.representation.Attribute attribute) {

		testResourceAttribute(ar, attribute);

		assertEquals(attribute.getValue(), ar.getShortStringValue());

		testTermReference(ar.getLabelReference(), attribute.getLabel(), false);
	}

	protected void testAttributeAttribute(final Attribute a,
			final com.collibra.dgc.core.model.representation.Attribute attribute) {

		testAttributeReferenceAttribute(a, attribute);

		testRepresentationReference(a.getOwner(), attribute.getOwner(), false);
	}

	protected void testAttributeReference(final AttributeReference ar,
			final com.collibra.dgc.core.model.representation.Attribute attribute, final boolean nullable) {

		if (testNullOrNotNullObject(ar, attribute, nullable))
			return;

		if (attribute instanceof com.collibra.dgc.core.model.representation.StringAttribute)
			testStringAttributeReference((StringAttributeReference) ar,
					(com.collibra.dgc.core.model.representation.StringAttribute) attribute);

		else if (attribute instanceof com.collibra.dgc.core.model.representation.SingleValueListAttribute)
			testSingleValueListAttributeReference((SingleValueListAttributeReference) ar,
					(com.collibra.dgc.core.model.representation.SingleValueListAttribute) attribute);

		else if (attribute instanceof com.collibra.dgc.core.model.representation.MultiValueListAttribute)
			testMultiValueListAttributeReference((MultiValueListAttributeReference) ar,
					(com.collibra.dgc.core.model.representation.MultiValueListAttribute) attribute);

		else
			fail("The kind of attribute is unknown");
	}

	private void testStringAttributeReference(final StringAttributeReference sar,
			final com.collibra.dgc.core.model.representation.StringAttribute stringAttribute) {

		testAttributeReferenceAttribute(sar, stringAttribute);

		assertEquals(com.collibra.dgc.core.model.representation.StringAttribute.class.getSimpleName(), sar.getKind());
		assertEquals(stringAttribute.getLongExpression(), sar.getLongExpression());
	}

	private void testSingleValueListAttributeReference(final SingleValueListAttributeReference svlar,
			final com.collibra.dgc.core.model.representation.SingleValueListAttribute singleValueListAttribute) {

		testAttributeReferenceAttribute(svlar, singleValueListAttribute);

		assertEquals(com.collibra.dgc.core.model.representation.SingleValueListAttribute.class.getSimpleName(),
				svlar.getKind());
	}

	private void testMultiValueListAttributeReference(final MultiValueListAttributeReference mvlar,
			final com.collibra.dgc.core.model.representation.MultiValueListAttribute multiValueListAttribute) {

		testAttributeReferenceAttribute(mvlar, multiValueListAttribute);

		assertEquals(com.collibra.dgc.core.model.representation.MultiValueListAttribute.class.getSimpleName(),
				mvlar.getKind());

		List<String> allowedValues = multiValueListAttribute.getValues();

		if (allowedValues == null)
			assertNull(mvlar.getValues());
		else {

			testCollectionsWithSameSize(allowedValues, mvlar.getValues());

			for (String allowedValue : allowedValues)
				assertTrue(mvlar.getValues().contains(allowedValue));
		}
	}

	protected void testAttributeReferences(final AttributeReferences ars,
			final Collection<com.collibra.dgc.core.model.representation.Attribute> attributes) {

		// Assert if both objects are null or not
		testNullOrNotNullObject(ars, attributes, false);

		// Assert list of AttributeTypeReference is not null and the same size as the binaryFactTypeForms
		// collection
		assertNotNull(ars.getAttributeReferences());
		testCollectionsWithSameSize(ars.getAttributeReferences(), attributes);

		// Convert AttributeReferences to a Map
		Map<String, AttributeReference> arMap = convertToMap(ars);

		// For each attribute , test if object represent the same resource
		for (final com.collibra.dgc.core.model.representation.Attribute attribute : attributes) {

			String key = attribute.getId().toString();
			assertTrue(arMap.containsKey(key));

			testAttributeReference(arMap.get(key), attribute, false);
		}
	}

	private Map<String, AttributeReference> convertToMap(AttributeReferences ars) {

		Map<String, AttributeReference> arMap = new HashMap<String, AttributeReference>();

		for (AttributeReference ar : ars.getAttributeReferences())
			arMap.put(ar.getResourceId(), ar);

		return arMap;
	}

	protected void testAttribute(final Attribute a,
			final com.collibra.dgc.core.model.representation.Attribute attribute, final boolean nullable) {

		if (testNullOrNotNullObject(a, attribute, nullable))
			return;

		if (attribute instanceof com.collibra.dgc.core.model.representation.StringAttribute)
			testStringAttribute((StringAttribute) a,
					(com.collibra.dgc.core.model.representation.StringAttribute) attribute);

		else if (attribute instanceof com.collibra.dgc.core.model.representation.SingleValueListAttribute)
			testSingleValueListAttribute((SingleValueListAttribute) a,
					(com.collibra.dgc.core.model.representation.SingleValueListAttribute) attribute);

		else if (attribute instanceof com.collibra.dgc.core.model.representation.MultiValueListAttribute)
			testMultiValueListAttribute((MultiValueListAttribute) a,
					(com.collibra.dgc.core.model.representation.MultiValueListAttribute) attribute);

		else
			fail("The kind of attribute is unknown");
	}

	private void testStringAttribute(final StringAttribute sa,
			final com.collibra.dgc.core.model.representation.StringAttribute stringAttribute) {

		testAttributeAttribute(sa, stringAttribute);

		assertEquals(com.collibra.dgc.core.model.representation.StringAttribute.class.getSimpleName(), sa.getKind());
		assertEquals(stringAttribute.getLongExpression(), sa.getLongExpression());
	}

	private void testSingleValueListAttribute(final SingleValueListAttribute svla,
			final com.collibra.dgc.core.model.representation.SingleValueListAttribute singleValueListAttribute) {

		testAttributeAttribute(svla, singleValueListAttribute);

		assertEquals(com.collibra.dgc.core.model.representation.SingleValueListAttribute.class.getSimpleName(),
				svla.getKind());
	}

	private void testMultiValueListAttribute(final MultiValueListAttribute mvla,
			final com.collibra.dgc.core.model.representation.MultiValueListAttribute multiValueListAttribute) {

		testAttributeAttribute(mvla, multiValueListAttribute);

		assertEquals(com.collibra.dgc.core.model.representation.MultiValueListAttribute.class.getSimpleName(),
				mvla.getKind());

		List<String> allowedValues = multiValueListAttribute.getValues();

		if (allowedValues == null)
			assertNull(mvla.getValues());
		else {

			testCollectionsWithSameSize(allowedValues, mvla.getValues());

			for (String allowedValue : allowedValues)
				assertTrue(mvla.getValues().contains(allowedValue));
		}
	}

	protected void testAttributes(final Attributes as,
			final Collection<com.collibra.dgc.core.model.representation.Attribute> attributes) {

		// Assert if both objects ae null or not
		testNullOrNotNullObject(as, attributes, false);

		// Assert list of AttributeType is not null and the same size as the binayFactTypeForms
		// collection
		assertNotNull(as.getAttributes());
		testCollectionsWithSameSize(as.getAttributes(), attributes);

		// Convert Attributes to a Map
		Map<String, Attribute> aMap = convertToMap(as);

		// For each attribute , test if object represent the same resource
		for (final com.collibra.dgc.core.model.representation.Attribute attribute : attributes) {

			String key = attribute.getId().toString();
			assertTrue(aMap.containsKey(key));

			testAttribute(aMap.get(key), attribute, false);
		}
	}

	private Map<String, Attribute> convertToMap(Attributes as) {

		Map<String, Attribute> aMap = new HashMap<String, Attribute>();

		for (Attribute a : as.getAttributes())
			aMap.put(a.getResourceId(), a);

		return aMap;
	}

	/* COMMON METHOD */

	/**
	 * Assert if both objects are null and can be or if both objects are not null.
	 * 
	 * @param first The first object.
	 * @param second The second object.
	 * @param nullable Set if both objects can be null (true) or not (false)
	 * @return if both are null and can be null, it return true (the test can be skipped) and false otherwise
	 */
	protected boolean testNullOrNotNullObject(final Object first, final Object second, final boolean nullable) {

		if (nullable && first == null) {

			assertTrue(second == null);
			return true;
		}

		// Assert that both objects are not null
		assertNotNull(first);
		assertNotNull(second);

		return false;
	}

	/**
	 * Assert if both collections have the same size.
	 * 
	 * @param firstCollection The first collection
	 * @param secondCollection The second collection
	 */
	protected void testCollectionsWithSameSize(Collection<?> firstCollection, Collection<?> secondCollection) {

		assertEquals(firstCollection.size(), secondCollection.size());
	}

	/**
	 * Get a REST object from a XML {@link String}.
	 * 
	 * @param xmlString The XML {@link String} that correspond to a JAXB object
	 * @return The object or null if a {@link JAXBException} is caught during the conversion
	 */
	protected Object getRestObject(String xmlString) {

		JAXBContext jc;

		try {

			jc = JAXBContext.newInstance("com.collibra.dgc.rest.core.v1_0.dto");
			ByteArrayInputStream input = new ByteArrayInputStream(xmlString.getBytes());

			return jc.createUnmarshaller().unmarshal(input);

		} catch (JAXBException e) {

			e.printStackTrace();
		}

		return null;
	}
}
