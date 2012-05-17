package com.collibra.dgc.service.constraint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.impl.CommunityImpl;
import com.collibra.dgc.core.service.impl.ConstraintChecker;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * 
 * @author pmalarme
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCommunityConstraintChecker extends AbstractServiceTest {

	@Autowired
	private ConstraintChecker constraintChecker;

	@Test
	public void testCommunityConstraintsOk() {

		try {

			constraintChecker.checkConstraints(createCommunities(false, false));

		} catch (Exception e) {

			fail();
		}

	}

	@Test
	public void testCommunityWithNameAlreadyExistsConstraint() {

		try {

			constraintChecker.checkConstraints(createCommunities(true, false));
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.COMMUNITY_WITH_NAME_ALREADY_EXISTS, e.getErrorCode());
		}
	}

	@Test
	public void testCommunityWithUriAlreadyExistsConstraint() {

		try {

			constraintChecker.checkConstraints(createCommunities(false, true));
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.COMMUNITY_WITH_URI_ALREADY_EXISTS, e.getErrorCode());
		}
	}

	// TODO Pierre: when the save will be implemented, the constraint will be checked automatically thus the constraint
	// checker call will not be needed
	private Community createCommunities(boolean sameName, boolean sameUri) {

		String community1Name = "Test Community 1 Name";
		String community2Name;

		if (sameName)
			community2Name = community1Name;
		else
			community2Name = "Test Community 2 Name";

		String community1Uri = "http://community1.com";
		String community2Uri;

		if (sameUri)
			community2Uri = community1Uri;
		else
			community2Uri = "http://community2.com";

		Community community1 = communityFactory.makeCommunity(community1Name, community1Uri);
		communityService.save(community1);
		resetTransaction();

		Community community2 = new CommunityImpl(community2Name, community2Uri);

		return community2;
		// TODO Pierre: add the save
		// communityService.save(community2);
		// resetTransaction();
	}
}
