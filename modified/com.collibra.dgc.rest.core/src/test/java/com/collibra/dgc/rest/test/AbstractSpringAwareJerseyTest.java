package com.collibra.dgc.rest.test;

import org.junit.Ignore;

import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerException;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;

/**
 * Test class which will wire itelf into your the Spring context which is configured on the WebAppDecriptor built for
 * your tests. Ensure you configure annotation-aware support into your contexts, and annotate any auto-wire properties
 * on your test class
 * @author George McIntosh
 * 
 */
@Ignore
public abstract class AbstractSpringAwareJerseyTest extends JerseyTest {

	public AbstractSpringAwareJerseyTest(WebAppDescriptor wad) {
		super(wad);
	}

	@Override
	protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
		return new SpringAwareGrizzly2TestContainerFactory(this);
	}

}
