package com.collibra.dgc.api.status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.component.StatusComponent;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.representation.Term;

/**
 * {@link StatusComponent} API tests.
 * 
 * @author pmalarme
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestStatusApi extends AbstractDGCBootstrappedApiTest {

	@Test
	public void testAddStatus() {

		Term status = statusComponent.addStatus("Test Status");

		assertNotNull(status);
		assertEquals("Test Status", status.getSignifier());
	}

	@Test
	public void testChangeSignifier() {

		Term status = statusComponent.addStatus("Test Status");

		Term result = statusComponent.changeSignifier(status.getId().toString(), "Status Test");

		assertNotNull(result);
		assertEquals(status.getId(), result.getId());
		assertEquals("Status Test", result.getSignifier());
	}

	@Test
	public void testGetStatus() {

		Term status = statusComponent.addStatus("Test Status");

		Term result = statusComponent.getStatus(status.getId().toString());

		assertNotNull(result);
		assertEquals(status.getId(), result.getId());
	}

	@Test
	public void testGetStatusBySignifier() {

		Term status = statusComponent.addStatus("Test Status");

		Term result = statusComponent.getStatusBySignifier("Test Status");

		assertNotNull(result);
		assertEquals(status.getId(), result.getId());
	}

	@Test
	public void testGetStatuses() {

		Collection<Term> statuses = statusComponent.getStatuses();

		assertNotNull(statuses);
		assertEquals(9, statuses.size());
	}

	@Test
	public void testRemoveStatuses() {

		Term status = statusComponent.addStatus("Test Status");

		statusComponent.removeStatus(status.getId().toString());

		boolean doesntExists = false;

		try {
			statusComponent.getStatus(status.getId().toString());

		} catch (EntityNotFoundException ex) {

			doesntExists = true;
		}

		assertTrue(doesntExists);
	}
}
