/**
 * 
 */
package com.collibra.dgc.service;

import org.junit.Before;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * Abstract super class for service test classes. It contains some helper methods to get started quickly.
 * @author dieterwachters
 */
public abstract class AbstractServiceTest extends AbstractBootstrappedServiceTest {
	protected Community sp;

	@Before
	public void init() {
		sp = communityFactory.makeCommunity("SP", "SPRUI");
	}

	protected Vocabulary createSampleVocabulary() {
		Vocabulary voc = representationFactory.makeVocabulary(sp, "uri", "My Voc");
		communityService.save(sp);
		return voc;
	}
}
