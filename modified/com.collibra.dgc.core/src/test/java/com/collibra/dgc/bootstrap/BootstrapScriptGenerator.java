/**
 * 
 */
package com.collibra.dgc.bootstrap;

import java.io.File;
import java.io.FileInputStream;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.collibra.dgc.core.application.Application;
import com.collibra.dgc.core.component.CommunityComponent;
import com.collibra.dgc.core.component.UserComponent;
import com.collibra.dgc.core.component.VocabularyComponent;
import com.collibra.dgc.core.component.attribute.AttributeComponent;
import com.collibra.dgc.core.component.representation.TermComponent;
import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.core.service.bootstrapper.BootstrapService;
import com.collibra.dgc.core.service.job.IJobProgressHandler;

/**
 * @author dieterwachters
 * 
 */
public class BootstrapScriptGenerator {
	private static CommunityComponent communityComponent;
	private static VocabularyComponent vocabularyComponent;
	private static TermComponent termComponent;
	private static AttributeComponent attributeComponent;
	private static UserComponent userComponent;
	private static BootstrapService bootstrapper;
	private static PlatformTransactionManager txManager;
	private static ConfigurationService configurationService;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final long t1 = System.currentTimeMillis();
		ApplicationContext context = new ClassPathXmlApplicationContext("/com/collibra/dgc/applicationContext-test.xml");

		// Set the security manager.
		SecurityUtils.setSecurityManager((org.apache.shiro.mgt.SecurityManager) context.getBean("securityManager"));

		// Disabling security
		System.setProperty(Constants.DISABLE_SECURITY, "true");

		// Getting everything we need
		communityComponent = context.getBean(CommunityComponent.class);
		vocabularyComponent = context.getBean(VocabularyComponent.class);
		termComponent = context.getBean(TermComponent.class);
		attributeComponent = context.getBean(AttributeComponent.class);
		userComponent = context.getBean(UserComponent.class);
		bootstrapper = context.getBean(BootstrapService.class);
		txManager = context.getBean(PlatformTransactionManager.class);
		configurationService = context.getBean(ConfigurationService.class);

		File output = new File(Application.USER_HOME, "basic.xml");
		File dtdOutput = new File(Application.USER_HOME, "structure.dtd");

		try {
			// Now we bootstrap the database from xmi
			new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					bootstrapper.bootstrap(new IJobProgressHandler() {
						@Override
						public void reportProgress(double progress, String message) {
						}

						@Override
						public void reportError(String message) {
						}

						@Override
						public void reportCompleted(String message) {
						}

						@Override
						public boolean isCanceled() {
							return false;
						}
					});
				}
			});

			BootstrapScriptExtractor.extractScript(output, dtdOutput, configurationService);

			// Boostrap from Scratch to start filling demo1
			bootstrapBasic(output, dtdOutput);
			fillDemo1();
			final File d1Output = new File(Application.USER_HOME, "demo1.xml");
			BootstrapScriptExtractor.extractScript(d1Output, null, configurationService);

			// Boostrap from Scratch to start filling demo2
			bootstrapBasic(output, dtdOutput);
			fillDemo2();
			final File d2Output = new File(Application.USER_HOME, "demo2.xml");
			BootstrapScriptExtractor.extractScript(d2Output, null, configurationService);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final long t2 = System.currentTimeMillis();
		System.out.println("Creating the bootstrap scripts took " + (t2 - t1) + "ms.");

		System.exit(0);
	}

	private static final void fillDemo1() {
		final Community community = communityComponent.addCommunity("Demo 1 Community",
				"http://www.collibra.com/demo1Community");

		final Vocabulary vocabulary = vocabularyComponent.addVocabulary(community.getId().toString(),
				"Demo 1 Vocabulary", "http://www.collibra.com/demo1Vocabulary");

		final Term car = termComponent.addTerm(vocabulary.getId().toString(), "Car");
		attributeComponent.addDefinition(car.getId().toString(), "A car is a 4-wheel vehicle for driving.");

		final Term driver = termComponent.addTerm(vocabulary.getId().toString(), "Driver");
		attributeComponent.addDefinition(driver.getId().toString(), "A human person driving a car.");
		attributeComponent.addDefinition(driver.getId().toString(), "This is another description for driver.");
		attributeComponent.addDefinition(driver.getId().toString(),
				"And there is even a third description of a driver.");
		attributeComponent.addExample(driver.getId().toString(), "You");
		attributeComponent.addExample(driver.getId().toString(), "Me");

		final Term insurance = termComponent.addTerm(vocabulary.getId().toString(), "Insurance");
		attributeComponent.addDefinition(insurance.getId().toString(),
				"The insurance a driver must have in order to drive a car.");

		userComponent.addUser("user", "user1906");
	}

	private static final void fillDemo2() {
		final Community community = communityComponent.addCommunity("Demo 2 Community",
				"http://www.collibra.com/demo2Community");

		final Vocabulary vocabulary = vocabularyComponent.addVocabulary(community.getId().toString(), "Space stuff",
				"http://www.collibra.com/demo2Vocabulary");

		final Term star = termComponent.addTerm(vocabulary.getId().toString(), "Star");
		attributeComponent.addDefinition(star.getId().toString(), "A star is a burning ball of gas.");

		final Term planet = termComponent.addTerm(vocabulary.getId().toString(), "Planet");
		attributeComponent.addDefinition(planet.getId().toString(),
				"A planet is orbits around a star and can be either gas or solid.");

		final Term moon = termComponent.addTerm(vocabulary.getId().toString(), "Moon");
		attributeComponent.addDefinition(moon.getId().toString(), "A moon orbits around a planet and is always solid.");

		userComponent.addUser("user", "user1906");
	}

	private static final void bootstrapBasic(File dataInput, File dtdInput) throws Exception {
		final FileInputStream di = new FileInputStream(dataInput);
		final FileInputStream dtdi = new FileInputStream(dtdInput);
		try {
			bootstrapper.bootstrap("basic", di, dtdi, new IJobProgressHandler() {
				@Override
				public boolean isCanceled() {
					return false;
				}

				@Override
				public void reportProgress(double progress, String message) {
				}

				@Override
				public void reportCompleted(String message) {
				}

				@Override
				public void reportError(String message) {
					System.err.println(message);
				}
			});
		} finally {
			di.close();
			dtdi.close();
		}
	}
}
