/**
 * 
 */
package com.collibra.dgc.service.resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.AuthorizationService;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * @author dieterwachters
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestResource extends AbstractServiceTest {
	@Autowired
	private AuthorizationService authService;

	@Test
	public void testResource() {
		final long t1 = System.currentTimeMillis();
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "Vocabulary 1");
		Term label = representationFactory.makeTerm(vocabulary, "AttribLabelTerm1Vocabulary");

		communityService.save(sp);
		resetTransaction();

		Assert.assertEquals("Admin", label.getCreatedBy());
		Assert.assertEquals("Admin", label.getLastModifiedBy());
		Assert.assertTrue(label.getCreatedOn() >= t1);
		Assert.assertTrue(label.getLastModified() >= t1);

		final long t2 = System.currentTimeMillis();

		representationService.changeSignifier(label, "AnotherLabel");
		label = (Term) representationService.save(label);
		resetTransaction();

		Assert.assertEquals("Admin", label.getCreatedBy());
		Assert.assertEquals("Admin", label.getLastModifiedBy());
		Assert.assertTrue(label.getCreatedOn() >= t1);
		Assert.assertTrue(label.getCreatedOn() < t2);
		Assert.assertTrue(label.getLastModified() >= t2);
		Assert.assertTrue(label.getLastModified() >= t1);
	}
}
