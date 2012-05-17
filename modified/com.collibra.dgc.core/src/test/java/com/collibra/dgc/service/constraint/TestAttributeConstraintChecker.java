package com.collibra.dgc.service.constraint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.DateTimeAttribute;
import com.collibra.dgc.core.model.representation.MultiValueListAttribute;
import com.collibra.dgc.core.model.representation.SingleValueListAttribute;
import com.collibra.dgc.core.model.representation.StringAttribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.impl.DateTimeAttributeImpl;
import com.collibra.dgc.core.model.representation.impl.MultiValueListAttributeImpl;
import com.collibra.dgc.core.model.representation.impl.SingleValueListAttributeImpl;
import com.collibra.dgc.core.model.representation.impl.StringAttributeImpl;
import com.collibra.dgc.core.service.impl.ConstraintChecker;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * 
 * @author pmalarme
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestAttributeConstraintChecker extends AbstractServiceTest {

	@Autowired
	private ConstraintChecker constraintChecker;

	/* String Attribute */

	@Test
	public void testStringAttributeConstraintsOk() {

		try {

			Attribute attribute = createStringAttributes(false, false, false);
			constraintChecker.checkConstraints(attribute);

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testStringAttributeOwnerLockedConstraints() {

		try {

			Attribute attribute = createStringAttributes(false, false, false);
			((ResourceImpl) attribute.getOwner()).setLock(true);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.REPRESENTATION_LOCKED, e.getErrorCode());
		}
	}

	@Test
	public void testStringAttributeWithSameContentAlreadyExsitsConstraintNullContent() {

		try {

			Attribute attribute = createStringAttributes(false, true, false);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.STRING_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS, e.getErrorCode());
		}
	}

	@Test
	public void testStringAttributeWithSameContentAlreadyExsitsConstraint() {

		try {

			Attribute attribute = createStringAttributes(true, false, false);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.STRING_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS, e.getErrorCode());
		}
	}

	@Test
	public void testStringAttributeWithWrongLabelConceptType() {

		try {

			Attribute attribute = createStringAttributes(false, false, true);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: Add the right DGCErrorCodes
		}
	}

	@Test
	public void testStringAttributeWithNullLabelConceptType() {

		try {

			Attribute attribute = createStringAttributes(false, false, false);
			attribute.getLabel().getObjectType().setType(null);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: Add the right DGCErrorCodes
		}
	}

	/* Single Value List Attribute */

	@Test
	public void testSingleValueListAttributeConstraintsOk() {

		try {

			Attribute attribute = createSingleValueListAttributes(false, false, false);
			constraintChecker.checkConstraints(attribute);

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testSingleValueListAttributeOwnerLockedConstraints() {

		try {

			Attribute attribute = createSingleValueListAttributes(false, false, false);
			((ResourceImpl) attribute.getOwner()).setLock(true);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.REPRESENTATION_LOCKED, e.getErrorCode());
		}
	}

	@Test
	public void testSingleValueListAttributeWithSameContentAlreadyExsitsConstraintNullContent() {

		try {

			Attribute attribute = createSingleValueListAttributes(false, true, false);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.SVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS, e.getErrorCode());
		}
	}

	@Test
	public void testSingleValueListAttributeWithSameContentAlreadyExsitsConstraint() {

		try {

			Attribute attribute = createSingleValueListAttributes(true, false, false);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.SVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS, e.getErrorCode());
		}
	}

	@Test
	public void testSingleValueListAttributeWithWrongLabelConceptType() {

		try {

			Attribute attribute = createSingleValueListAttributes(false, false, true);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: Add the right DGCErrorCodes
		}
	}

	@Test
	public void testSingleValueListAttributeWithNullLabelConceptType() {

		try {

			Attribute attribute = createSingleValueListAttributes(false, false, false);
			attribute.getLabel().getObjectType().setType(null);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: Add the right DGCErrorCodes
		}
	}

	/* Multiple Value List Attribute */

	@Test
	public void testMultiValueListAttributeConstraintsOkNullContent() {

		try {

			Attribute attribute = createMultiValueListAttributes(null, createList("1", "2"), false);
			constraintChecker.checkConstraints(attribute);

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testMultiValueListAttributeConstraintsOkContentNull() {

		try {

			Attribute attribute = createMultiValueListAttributes(createList("1", "2"), null, false);
			constraintChecker.checkConstraints(attribute);

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testMultiValueListAttributeConstraintsOkSameSize() {

		try {

			Attribute attribute = createMultiValueListAttributes(createList("1", "2"), createList("3", "4"), false);
			constraintChecker.checkConstraints(attribute);

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testMultiValueListAttributeConstraintsOkDifferentSize() {

		try {

			Attribute attribute = createMultiValueListAttributes(createList("1", "2"), createList("1", "2", "3"), false);
			constraintChecker.checkConstraints(attribute);

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testMultiValueListAttributeConstraintsOkEmpty() {

		try {

			Attribute attribute = createMultiValueListAttributes(createList("1", "2"), createList(), false);
			constraintChecker.checkConstraints(attribute);

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testMultiValueListAttributeOwnerLockedConstraints() {

		try {

			Attribute attribute = createMultiValueListAttributes(createList("1", "2"), createList(), false);
			((ResourceImpl) attribute.getOwner()).setLock(true);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.REPRESENTATION_LOCKED, e.getErrorCode());
		}
	}

	@Test
	public void testMultiValueListAttributeWithSameContentAlreadyExsitsConstraint() {

		try {

			Attribute attribute = createMultiValueListAttributes(createList("1", "2"), createList("1", "2"), false);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.MVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS, e.getErrorCode());
		}
	}

	@Test
	public void testMultiValueListAttributeWithSameContentAlreadyExsitsConstraintNull() {

		try {

			Attribute attribute = createMultiValueListAttributes(null, null, false);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.MVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS, e.getErrorCode());
		}
	}

	@Test
	public void testMultiValueListAttributeWithSameContentAlreadyExsitsConstraintNullEmpty() {

		try {

			Attribute attribute = createMultiValueListAttributes(null, createList(), false);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.MVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS, e.getErrorCode());
		}
	}

	@Test
	public void testMultiValueListAttributeWithSameContentAlreadyExsitsConstraintEmptyNull() {

		try {

			Attribute attribute = createMultiValueListAttributes(createList(), null, false);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.MVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS, e.getErrorCode());
		}
	}

	@Test
	public void testMultiValueListAttributeWithSameContentAlreadyExsitsConstraintEmpty() {

		try {

			Attribute attribute = createMultiValueListAttributes(createList(), createList(), false);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.MVL_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS, e.getErrorCode());
		}
	}

	@Test
	public void testMultiValueListAttributeWithWrongLabelConceptType() {

		try {

			Attribute attribute = createMultiValueListAttributes(null, createList("1", "2"), true);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: Add the right DGCErrorCodes
		}
	}

	@Test
	public void testMultiValueListAttributeWithNullLabelConceptType() {

		try {

			Attribute attribute = createMultiValueListAttributes(null, createList("1", "2"), false);
			attribute.getLabel().getObjectType().setType(null);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: Add the right DGCErrorCodes
		}
	}

	/* Date-Time Attribute */

	@Test
	public void testDateTimeAttributeConstraintsOk() {

		try {

			Attribute attribute = createDateTimeAttributes(false, false, false);
			constraintChecker.checkConstraints(attribute);

		} catch (Exception e) {

			fail();
		}
	}

	@Test
	public void testDateTimeAttributeOwnerLockedConstraints() {

		try {

			Attribute attribute = createDateTimeAttributes(false, false, false);
			((ResourceImpl) attribute.getOwner()).setLock(true);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.REPRESENTATION_LOCKED, e.getErrorCode());
		}
	}

	@Test
	public void testDateTimeAttributeWithSameContentAlreadyExsitsConstraintNullContent() {

		try {

			Attribute attribute = createDateTimeAttributes(false, true, false);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.DT_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS, e.getErrorCode());
		}
	}

	@Test
	public void testDateTimeAttributeWithSameContentAlreadyExsitsConstraint() {

		try {

			Attribute attribute = createDateTimeAttributes(true, false, false);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			assertEquals(DGCErrorCodes.DT_ATTRIBUTE_WITH_SAME_CONTENT_EXISTS, e.getErrorCode());
		}
	}

	@Test
	public void testDateTimeAttributeWithWrongLabelConceptType() {

		try {

			Attribute attribute = createDateTimeAttributes(false, false, true);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: Add the right DGCErrorCodes
		}
	}

	@Test
	public void testDateTimeAttributeWithNullLabelConceptType() {

		try {

			Attribute attribute = createDateTimeAttributes(false, false, false);
			attribute.getLabel().getObjectType().setType(null);
			constraintChecker.checkConstraints(attribute);
			fail();

		} catch (IllegalArgumentException e) {

			// TODO Pierre: Add the right DGCErrorCodes
		}
	}

	/* Utils */

	// TODO Pierre: when the save will be implemented, the constraint will be checked automatically thus the constraint
	// checker call will not be needed
	private StringAttribute createStringAttributes(boolean sameContent, boolean nullContent,
			boolean wrongLabelConceptType) {

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");
		Vocabulary vocabulary = representationFactory.makeVocabulary(community, "http://vocabulary.com", "Vocabulary");
		communityService.save(community);
		resetTransaction();

		Term labelTerm = representationFactory.makeTermOfType(vocabularyDao.findAttributeTypesVocabulary(), "Test AT",
				meaningFactory.makeObjectType());

		if (wrongLabelConceptType)
			labelTerm.getObjectType().setType(meaningFactory.makeObjectType());
		else
			labelTerm.getObjectType().setType(attributeService.findMetaStringAttributeTypeLabel().getObjectType());

		representationService.save(labelTerm);
		resetTransaction();

		String value1;
		String value2;

		if (nullContent) {

			value1 = null;
			value2 = value1;

		} else {

			value1 = "Test value 1";

			if (sameContent)
				value2 = value1;
			else
				value2 = "Test value 2";
		}

		Term owner = representationFactory.makeTerm(vocabulary, "Test Owner");
		@SuppressWarnings("unused")
		StringAttribute sa1 = representationFactory.makeStringAttribute(labelTerm, owner, value1);
		representationService.save(owner);
		resetTransaction();

		// TODO Pierre: use factory and save
		StringAttribute sa2 = new StringAttributeImpl(labelTerm, owner, value2);

		return sa2;
	}

	// TODO Pierre: when the svlave will be implemented, the constraint will be checked automatically thus the
	// constraint
	// checker call will not be needed
	private SingleValueListAttribute createSingleValueListAttributes(boolean sameContent, boolean nullContent,
			boolean wrongLabelConceptType) {

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");
		Vocabulary vocabulary = representationFactory.makeVocabulary(community, "http://vocabulary.com", "Vocabulary");
		communityService.save(community);
		resetTransaction();

		Term labelTerm = representationFactory.makeTermOfType(vocabularyDao.findAttributeTypesVocabulary(), "Test AT",
				meaningFactory.makeObjectType());

		if (wrongLabelConceptType)
			labelTerm.getObjectType().setType(meaningFactory.makeObjectType());
		else
			labelTerm.getObjectType().setType(attributeService.findMetaSingleStaticListType().getObjectType());

		representationService.save(labelTerm);
		resetTransaction();

		String value1;
		String value2;

		if (nullContent) {

			value1 = null;
			value2 = value1;

		} else {

			value1 = "Test value 1";

			if (sameContent)
				value2 = value1;
			else
				value2 = "Test value 2";
		}

		Term owner = representationFactory.makeTerm(vocabulary, "Test Owner");
		@SuppressWarnings("unused")
		SingleValueListAttribute svla1 = representationFactory.makeSingleValueListAttribute(labelTerm, owner, value1);
		representationService.save(owner);
		resetTransaction();

		// TODO Pierre: use factory and svlave
		SingleValueListAttribute svla2 = new SingleValueListAttributeImpl(labelTerm, owner, value2);

		return svla2;
	}

	// TODO Pierre: when the mvlave will be implemented, the constraint will be checked automatically thus the
	// constraint
	// checker call will not be needed
	private MultiValueListAttribute createMultiValueListAttributes(List<String> values1, List<String> values2,
			boolean wrongLabelConceptType) {

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");
		Vocabulary vocabulary = representationFactory.makeVocabulary(community, "http://vocabulary.com", "Vocabulary");
		communityService.save(community);
		resetTransaction();

		Term labelTerm = representationFactory.makeTermOfType(vocabularyDao.findAttributeTypesVocabulary(), "Test AT",
				meaningFactory.makeObjectType());

		if (wrongLabelConceptType)
			labelTerm.getObjectType().setType(meaningFactory.makeObjectType());
		else
			labelTerm.getObjectType().setType(attributeService.findMetaMultiStaticListType().getObjectType());

		representationService.save(labelTerm);
		resetTransaction();

		Term owner = representationFactory.makeTerm(vocabulary, "Test Owner");
		@SuppressWarnings("unused")
		MultiValueListAttribute mvla1 = representationFactory.makeMultiValueListAttribute(labelTerm, owner, values1);
		representationService.save(owner);
		resetTransaction();

		// TODO Pierre: use factory and mvlave
		MultiValueListAttribute mvla2 = new MultiValueListAttributeImpl(labelTerm, owner, values2);

		return mvla2;
	}

	private List<String> createList(String... values) {

		List<String> list = new ArrayList<String>();

		for (String value : values)
			list.add(value);

		return list;
	}

	private DateTimeAttribute createDateTimeAttributes(boolean sameContent, boolean nullContent,
			boolean wrongLabelConceptType) {

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");
		Vocabulary vocabulary = representationFactory.makeVocabulary(community, "http://vocabulary.com", "Vocabulary");
		communityService.save(community);
		resetTransaction();

		Term labelTerm = representationFactory.makeTermOfType(vocabularyDao.findAttributeTypesVocabulary(), "Test AT",
				meaningFactory.makeObjectType());

		if (wrongLabelConceptType)
			labelTerm.getObjectType().setType(meaningFactory.makeObjectType());
		else
			labelTerm.getObjectType().setType(attributeService.findMetaDateTimeAttributeTypeLabel().getObjectType());

		representationService.save(labelTerm);
		resetTransaction();

		Calendar value1;
		Calendar value2;

		if (nullContent) {

			value1 = null;
			value2 = value1;

		} else {

			value1 = Calendar.getInstance();
			value1.setTimeInMillis(0);

			if (sameContent)
				value2 = value1;
			else {
				value2 = Calendar.getInstance();
				value2.setTimeInMillis(1);
			}
		}

		Term owner = representationFactory.makeTerm(vocabulary, "Test Owner");
		@SuppressWarnings("unused")
		DateTimeAttribute dta1 = representationFactory.makeDateTimeAttribute(labelTerm, owner, value1);
		representationService.save(owner);
		resetTransaction();

		// TODO Pierre: use factory and save
		DateTimeAttribute dta2 = new DateTimeAttributeImpl(labelTerm, owner, value2);

		return dta2;
	}
}
