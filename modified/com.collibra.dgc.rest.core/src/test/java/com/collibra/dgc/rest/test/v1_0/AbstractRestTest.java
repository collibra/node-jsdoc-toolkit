package com.collibra.dgc.rest.test.v1_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoaderListener;

import com.collibra.dgc.core.component.CommunityComponent;
import com.collibra.dgc.core.component.SearchComponent;
import com.collibra.dgc.core.component.VocabularyComponent;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeFormReference;
import com.collibra.dgc.rest.core.v1_0.dto.BinaryFactTypeFormReferences;
import com.collibra.dgc.rest.core.v1_0.dto.CharacteristicFormReference;
import com.collibra.dgc.rest.core.v1_0.dto.CharacteristicFormReferences;
import com.collibra.dgc.rest.core.v1_0.dto.Communities;
import com.collibra.dgc.rest.core.v1_0.dto.Community;
import com.collibra.dgc.rest.core.v1_0.dto.CommunityReference;
import com.collibra.dgc.rest.core.v1_0.dto.CommunityReferences;
import com.collibra.dgc.rest.core.v1_0.dto.Resource;
import com.collibra.dgc.rest.core.v1_0.dto.SearchResultItem;
import com.collibra.dgc.rest.core.v1_0.dto.SearchResultItems;
import com.collibra.dgc.rest.core.v1_0.dto.TermReference;
import com.collibra.dgc.rest.core.v1_0.dto.TermReferences;
import com.collibra.dgc.rest.core.v1_0.dto.Vocabularies;
import com.collibra.dgc.rest.core.v1_0.dto.Vocabulary;
import com.collibra.dgc.rest.core.v1_0.dto.VocabularyReference;
import com.collibra.dgc.rest.core.v1_0.dto.VocabularyReferences;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

@Ignore
public class AbstractRestTest extends JerseyTest {

	public AbstractRestTest() {

		super(new WebAppDescriptor.Builder("com.collibra.dgc.rest.core").contextPath("dgc-test")
				.contextParam("contextConfigLocation", "/com/collibra/dgc/applicationContext-test.xml")
				.servletClass(SpringServlet.class).contextListenerClass(ContextLoaderListener.class).build());
	}

	// All the autowired components

	@Autowired
	protected CommunityComponent communityComponent;

	@Autowired
	protected SearchComponent searchComponent;

	@Autowired
	protected VocabularyComponent vocabularyComponent;

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
		assertEquals(vr.getResourceId(), vocabulary.getId().toString());
		assertEquals(vr.getName(), vocabulary.getName());
		assertEquals(vr.getUri(), vocabulary.getUri());
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
		assertEquals(v.getResourceId(), vocabulary.getId().toString());
		assertEquals(v.getName(), vocabulary.getName());
		assertEquals(v.getUri(), vocabulary.getUri());

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

	/* TERM */

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
		assertEquals(tr.getResourceId(), term.getId().toString());
		assertEquals(tr.getSignifier(), term.getSignifier());
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

		// Convert BinaryFactTypeFormReferences to a Map
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

		// Assert equals every attributes of both objects
		assertEquals(bftfr.getResourceId(), binaryFactTypeForm.getId().toString());
		assertEquals(bftfr.getRole(), binaryFactTypeForm.getRole());
		assertEquals(bftfr.getCoRole(), binaryFactTypeForm.getCoRole());
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
		final Map<String, BinaryFactTypeFormReference> vrMap = convertToMap(bftfrs);

		// For each binary fact type form, test if object represent the same resource
		for (final com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm binaryFactTypeForm : binaryFactTypeForms) {

			String key = binaryFactTypeForm.getId().toString();
			assertTrue(vrMap.containsKey(key));

			testBinaryFactTypeFormReference(vrMap.get(key), binaryFactTypeForm, false);
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

	/* CHARACTERISTIC FORM */

	/**
	 * Check if both characteristic form objects from the REST service and the public API (component) correspond.
	 * 
	 * @param cfr The {@link CharacteristicFormReference} object from the REST service
	 * @param characteristicForm The {@link com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm}
	 *            object from the core model
	 * @param nullable Set if both objects can be null (true) or not (false)
	 */
	protected void testCharacteristicFormReference(final CharacteristicFormReference cfr,
			final com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm characteristicForm,
			final boolean nullable) {

		// Assert if both objects are null or not
		if (testNullOrNotNullObject(cfr, characteristicForm, nullable))
			return;

		// Assert equals every attributes of both objects
		assertEquals(cfr.getResourceId(), characteristicForm.getId().toString());
		assertEquals(cfr.getRole(), characteristicForm.getRole());
	}

	/**
	 * Check if both collections of characteristic form objects correspond.
	 * 
	 * @param cfrs The {@link CharacteristicFormReferences} object from the REST service
	 * @param characteristicForms The {@link Collection} of
	 *            {@link com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm} object from the
	 *            core model
	 */
	protected void testCharacteristicFormReferences(
			final CharacteristicFormReferences cfrs,
			final Collection<com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm> characteristicForms) {

		// Assert if both objects are null or not
		testNullOrNotNullObject(cfrs, characteristicForms, false);

		// Assert list of BinaryFactTypeFormReference is not null and the same size as the characteristicForms
		// collection
		assertNotNull(cfrs.getCharacteristicFormReferences());
		testCollectionsWithSameSize(cfrs.getCharacteristicFormReferences(), characteristicForms);

		// Convert CharacteristicFormReferences to a Map
		final Map<String, CharacteristicFormReference> vrMap = convertToMap(cfrs);

		// For each characteristic form, test if object represent the same resource
		for (final com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm characteristicForm : characteristicForms) {

			String key = characteristicForm.getId().toString();
			assertTrue(vrMap.containsKey(key));

			testCharacteristicFormReference(vrMap.get(key), characteristicForm, false);
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

	protected void testTerm(Term t, com.collibra.dgc.rest.core.v1_0.dto.Term rt) {
		assertEquals(t.getSignifier(), rt.getSignifier());
		assertEquals(t.getId().toString(), rt.getResourceId());
		assertEquals(t.getVocabulary().getId().toString(), rt.getVocabularyReference().getResourceId());
		assertEquals(t.getVocabulary().getName(), rt.getVocabularyReference().getName());

		Set<String> termAttributeRIDs = new HashSet<String>();
		for (Attribute a : t.getAttributes())
			termAttributeRIDs.add(a.getId().toString());

		// assertEquals(t.getAttributes().size(), rt.getAttributes().getAttributes().size());

		// for (com.collibra.dgc.rest.core.v1_0.dto.Attribute a : rt.getAttributes().getAttributes())
		// assertTrue(termAttributeRIDs.contains(a.getResourceId()));
	}

}