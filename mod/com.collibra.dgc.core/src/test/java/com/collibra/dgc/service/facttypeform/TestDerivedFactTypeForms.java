package com.collibra.dgc.service.facttypeform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * Tests for {@link BinaryFactsDerivator}.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestDerivedFactTypeForms extends AbstractServiceTest {

	/**
	 * Person drives / driven by Car.
	 * <p>
	 * Driver is a Person.
	 * <p>
	 * Car is a Auto.
	 * <p>
	 * Taxi is a Car.
	 * <p>
	 * This implies the following BFTFs can be derived for entity 'Driver':
	 * <p>
	 * Driver drives / driven by Car.
	 * <p>
	 * Driver drives / driven by Taxi.
	 * <p>
	 * This implies the following BFTFs can be derived for entity 'Person' as substitution:
	 * <p>
	 * Person drives / driven by Taxi.
	 */
	@Test
	public void testPersonDriverCar() {
		Community sc = communityFactory.makeCommunity("SC", "SC URI");
		Community sp = communityFactory.makeCommunity(sc, "SP", "SP URI");
		Vocabulary vocabulary = representationFactory
				.makeVocabulary(sp, "Derived Fact Types URI", "Derived Fact Types");

		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term driver = representationFactory.makeTerm(vocabulary, "Driver");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term auto = representationFactory.makeTerm(vocabulary, "Auto");
		Term taxi = representationFactory.makeTerm(vocabulary, "Taxi");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);

		driver.getObjectType().setGeneralConcept(person.getObjectType());
		car.getObjectType().setGeneralConcept(auto.getObjectType());
		taxi.getObjectType().setGeneralConcept(car.getObjectType());

		communityService.save(sc);

		resetTransaction();

		driver = representationService.findTermByResourceId(driver.getId());
		assertNotNull(driver);

		Collection<BinaryFactTypeForm> derived = representationService.findDerivedFacts(driver);
		assertEquals(2, derived.size());

		assertBFTFPresence(derived, driver.getSignifier(), "drives", "driven by", car.getSignifier());
		assertBFTFPresence(derived, driver.getSignifier(), "drives", "driven by", taxi.getSignifier());

		person = representationService.findTermByResourceId(person.getId());
		derived = representationService.findDerivedFacts(person);
		assertEquals(1, derived.size());
		assertBFTFPresence(derived, person.getSignifier(), "drives", "driven by", taxi.getSignifier());
	}

	/**
	 * Person drives / driven by Car.
	 * <p>
	 * Driver is a Person.
	 * <p>
	 * Car is a Auto.
	 * <p>
	 * Taxi is a Car.
	 * <p>
	 * Driver drives / driven by Taxi.
	 * <p>
	 * Special Driver is a Driver.
	 * <p>
	 * This implies the following BFTFs can be derived for entity 'Driver':
	 * <p>
	 * Driver drives / driven by Car.
	 * <p>
	 * Driver drives / driven by Taxi. However this BFTF will be filtered as user has already defined it.
	 * <p>
	 * This implies the following BFTFs can be derived for entity 'Special Driver':
	 * <p>
	 * Special Driver drives / driven by Car.
	 * <p>
	 * Special Driver drives / driven by Taxi.
	 */
	@Test
	public void testPersonDriverCarWithFilter() {
		Community sc = communityFactory.makeCommunity("SC", "SC URI");
		Community sp = communityFactory.makeCommunity(sc, "SP", "SP URI");
		Vocabulary vocabulary = representationFactory
				.makeVocabulary(sp, "Derived Fact Types URI", "Derived Fact Types");

		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term driver = representationFactory.makeTerm(vocabulary, "Driver");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term auto = representationFactory.makeTerm(vocabulary, "Auto");
		Term taxi = representationFactory.makeTerm(vocabulary, "Taxi");
		Term specialDriver = representationFactory.makeTerm(vocabulary, "Special Driver");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, driver, "drives", "driven by", taxi);

		driver.getObjectType().setGeneralConcept(person.getObjectType());

		communityService.save(sc);

		resetTransaction();

		// FIXME : Taxonomy is not working fine if added all in transaction.

		car = representationService.findTermByResourceId(car.getId());
		auto = representationService.findTermByResourceId(auto.getId());
		taxi = representationService.findTermByResourceId(taxi.getId());
		driver = representationService.findTermByResourceId(driver.getId());
		specialDriver = representationService.findTermByResourceId(specialDriver.getId());

		car.getObjectType().setGeneralConcept(auto.getObjectType());
		taxi.getObjectType().setGeneralConcept(car.getObjectType());
		specialDriver.getObjectType().setGeneralConcept(driver.getObjectType());

		Collection<BinaryFactTypeForm> derived = representationService.findDerivedFacts(driver);
		assertTrue(derived.size() == 1);

		assertBFTFPresence(derived, driver.getSignifier(), "drives", "driven by", car.getSignifier());

		derived = representationService.findDerivedFacts(specialDriver);
		assertNotNull(derived);
		assertEquals(2, derived.size());
		assertBFTFPresence(derived, specialDriver.getSignifier(), "drives", "driven by", car.getSignifier());
		assertBFTFPresence(derived, specialDriver.getSignifier(), "drives", "driven by", taxi.getSignifier());
	}

	/**
	 * Person drives / driven by Car.
	 * <p>
	 * Driver is a Person.
	 * <p>
	 * This implies the following BFTFs can be derived for entity 'Car':
	 * <p>
	 * Car driven by / drives Car.
	 */
	@Test
	public void testPersonDriverCarForOnlySubstitutedFacts() {
		Community sc = communityFactory.makeCommunity("SC", "SC URI");
		Community sp = communityFactory.makeCommunity(sc, "SP", "SP URI");
		Vocabulary vocabulary = representationFactory
				.makeVocabulary(sp, "Derived Fact Types URI", "Derived Fact Types");

		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term driver = representationFactory.makeTerm(vocabulary, "Driver");
		Term car = representationFactory.makeTerm(vocabulary, "Car");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);

		driver.getObjectType().setGeneralConcept(person.getObjectType());

		communityService.save(sc);

		resetTransaction();

		car = representationService.findTermByResourceId(car.getId());
		assertNotNull(car);

		Collection<BinaryFactTypeForm> derived = representationService.findDerivedFacts(car);
		assertEquals(1, derived.size());

		assertBFTFPresence(derived, car.getSignifier(), "driven by", "drives", driver.getSignifier());
	}

	private void assertBFTFPresence(Collection<BinaryFactTypeForm> derived, String head, String role, String corole,
			String tail) {

		for (BinaryFactTypeForm bftf : derived) {
			if (bftf.getHeadTerm().getSignifier().equals(head) && bftf.getRole().equals(role)
					&& bftf.getCoRole().equals(corole) && bftf.getTailTerm().getSignifier().equals(tail)) {
				return;
			}
		}

		fail("Failed to find: " + head + " " + role + "/" + corole + " " + tail);
	}
}
