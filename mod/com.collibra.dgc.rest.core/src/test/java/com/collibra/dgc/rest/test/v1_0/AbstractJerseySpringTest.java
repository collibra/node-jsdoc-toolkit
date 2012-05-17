package com.collibra.dgc.rest.test.v1_0;

import java.net.URI;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.spi.service.ServiceFinder;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.LowLevelAppDescriptor;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.client.ClientFactory;
import com.sun.jersey.test.framework.spi.container.TestContainer;
import com.sun.jersey.test.framework.spi.container.TestContainerException;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;

@Ignore
// @TestExecutionListeners(TransactionalTestExecutionListener.class)
// @TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
// @ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class AbstractJerseySpringTest {

	/**
	 * Holds the default test container factory class to be used for running the tests.
	 */
	private static Class<? extends TestContainerFactory> defaultTestContainerFactoryClass;

	/**
	 * The test container factory which creates an instance of the test container on which the tests would be run.
	 */
	private TestContainerFactory testContainerFactory;

	private ClientFactory clientFactory;

	/**
	 * The test container on which the tests would be run.
	 */
	private final TestContainer tc;

	/**
	 * Client instance for creating {@link WebResource} instances and configuring the properties of connections and
	 * requests.
	 */
	private final Client client;

	/**
	 * An extending class must implement the {@link #configure()} method to provide an application descriptor.
	 * 
	 * @throws TestContainerException if the default test container factory cannot be obtained, or the application
	 *             descriptor is not supported by the test container factory.
	 */
	public AbstractJerseySpringTest() throws TestContainerException {
		AppDescriptor ad = configure();
		this.tc = getContainer(ad, getTestContainerFactory());
		this.client = getClient(tc, ad);
	}

	/**
	 * Construct a new instance with a test container factory.
	 * <p>
	 * An extending class must implement the {@link #configure()} method to provide an application descriptor.
	 * 
	 * @param testContainerFactory the test container factory to use for testing.
	 * @throws TestContainerException if the application descriptor is not supported by the test container factory.
	 */
	public AbstractJerseySpringTest(TestContainerFactory testContainerFactory) {
		setTestContainerFactory(testContainerFactory);
		AppDescriptor ad = configure();
		this.tc = getContainer(ad, getTestContainerFactory());
		this.client = getClient(tc, ad);
	}

	/**
	 * Return an application descriptor that defines how the test container is configured.
	 * <p>
	 * If a constructor is utilized that does not supply an application descriptor then this method must be overridden
	 * to return an application descriptor, otherwise an {@link UnsupportedOperationException} exception will be thrown.
	 * <p>
	 * If a constructor is utilized that does supply an application descriptor then this method does not require to be
	 * overridden and will not be invoked.
	 * 
	 * @return the application descriptor.
	 */
	protected AppDescriptor configure() {
		throw new UnsupportedOperationException("The configure method must be implemented by the extending class");
	}

	/**
	 * Construct a new instance with an application descriptor that defines how the test container is configured.
	 * 
	 * @param ad an application descriptor describing how to configure the test container.
	 * @throws TestContainerException if the default test container factory cannot be obtained, or the application
	 *             descriptor is not supported by the test container factory.
	 */
	public AbstractJerseySpringTest(AppDescriptor ad) throws TestContainerException {
		this.tc = getContainer(ad, getTestContainerFactory());
		this.client = getClient(tc, ad);
	}

	/**
	 * Construct a new instance with an array or a colon separated list of package names which contain resource and
	 * provider classes.
	 * <p>
	 * This constructor builds an instance of {@link WebAppDescriptor} passing the package names to the constructor.
	 * 
	 * @param packages array or a colon separated list of package names which contain resource and provider classes.
	 * @throws TestContainerException if the default test container factory cannot be obtained, or the built application
	 *             descriptor is not supported by the test container factory.
	 */
	public AbstractJerseySpringTest(String... packages) throws TestContainerException {
		this(new WebAppDescriptor.Builder(packages).build());
	}

	/**
	 * Sets the test container factory to to be used for testing.
	 * 
	 * @param testContainerFactory the test container factory to to be used for testing.
	 */
	protected void setTestContainerFactory(TestContainerFactory testContainerFactory) {
		this.testContainerFactory = testContainerFactory;
	}

	/**
	 * Get the test container factory.
	 * <p>
	 * If the test container factory has not been explicit set with
	 * {@link #setTestContainerFactory(TestContainerFactory) } then the default test container factory will be obtained.
	 * <p>
	 * If the system property {@literal jersey.test.containerFactory} is set and the value is a fully qualified class
	 * name of a class that extends from {@link TestContainerFactory} then the default test container factory will be an
	 * instance of that class. The exception {@link TestContainerException} will be thrown if the class cannot be loaded
	 * or instantiated. If the system property {@literal jersey.test.containerFactory} is not set then the default test
	 * container factory will be an instance of
	 * {@link com.sun.jersey.test.framework.spi.container.grizzly.web.GrizzlyWebTestContainerFactory}. The exception
	 * {@link TestContainerException} will be thrown if this class cannot be loaded or instantiated.
	 * 
	 * 
	 * @return the test container factory.
	 * @throws TestContainerException if the default test container factory cannot be obtained.
	 */
	protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
		if (testContainerFactory == null)
			testContainerFactory = getDefaultTestContainerFactory();

		return testContainerFactory;
	}

	/**
	 * Create a web resource whose URI refers to the base URI the Web application is deployed at.
	 * 
	 * @return the created web resource
	 */
	public WebResource resource() {
		return client.resource(tc.getBaseUri());
	}

	/**
	 * Get the client that is configured for this test.
	 * 
	 * @return the configured client.
	 */
	public Client client() {
		return client;
	}

	/**
	 * Set up the test by invoking {@link TestContainer#start() } on the test container obtained from the test container
	 * factory.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		tc.start();
	}

	/**
	 * Tear down the test by invoking {@link TestContainer#stop() } on the test container obtained from the test
	 * container factory.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		tc.stop();
	}

	/**
	 * Creates an instance of {@link TestContainer} from the passed instances of {@link AppDescriptor} and
	 * {@link TestContainerFactory}. If the test container factory doesn't support the application descriptor, a
	 * {@link TestContainerException} is thrown.
	 * @param ad An instance of {@link AppDescriptor}
	 * @param tcf An instance of {@link TestContainerFactory}
	 * @return An instance of {@link TestContainer}
	 */
	private TestContainer getContainer(AppDescriptor ad, TestContainerFactory tcf) {
		if (ad == null)
			throw new IllegalArgumentException("The application descriptor cannot be null");

		Class<? extends AppDescriptor> adType = tcf.supports();
		if (adType == LowLevelAppDescriptor.class && ad.getClass() == WebAppDescriptor.class) {
			ad = LowLevelAppDescriptor.transform((WebAppDescriptor) ad);
		} else if (adType != ad.getClass()) {
			throw new TestContainerException("The application descriptor type, " + ad.getClass()
					+ ", is not supported by the test container factory, " + tcf);
		}

		return tcf.create(getBaseURI(), ad);
	}

	/**
	 * Creates an instance of the default test container factory.
	 * 
	 * @return An instance of {@link TestContainerFactory}
	 */
	private static TestContainerFactory getDefaultTestContainerFactory() {

		if (defaultTestContainerFactoryClass == null) {
			if ((System.getProperty(TEST_CONTAINER_FACTORY_PROPERTY_NAME) == null)
					&& (System.getProperty(TEST_CONTAINER_FACTORY_PROPERTY_NAME_LEGACY) == null)) {

				TestContainerFactory[] resultArray = ServiceFinder.find(TestContainerFactory.class).toArray();

				if (resultArray.length >= 1) {
					// if default factory is present, use it.
					for (int i = 0; i < resultArray.length; i++) {

						if (resultArray[i].getClass().getName().equals(DEFAULT_TEST_CONTAINER_FACTORY_CLASS_NAME)) {
							Logger.getLogger(TestContainerFactory.class.getName()).config(
									"Found multiple TestContainerFactory implementations, using default "
											+ resultArray[i].getClass().getName());

							defaultTestContainerFactoryClass = resultArray[i].getClass(); // is this necessary?
							return resultArray[i];
						}
					}

					if (resultArray.length != 1)
						Logger.getLogger(TestContainerFactory.class.getName()).warning(
								"Found multiple TestContainerFactory implementations, using "
										+ resultArray[0].getClass().getName());

					defaultTestContainerFactoryClass = resultArray[0].getClass();
					return resultArray[0];
				}
			} else {
				String tcfClassName = System.getProperty(TEST_CONTAINER_FACTORY_PROPERTY_NAME);

				if (tcfClassName == null)
					tcfClassName = System.getProperty(TEST_CONTAINER_FACTORY_PROPERTY_NAME_LEGACY,
							DEFAULT_TEST_CONTAINER_FACTORY_CLASS_NAME);

				try {
					defaultTestContainerFactoryClass = Class.forName(tcfClassName).asSubclass(
							TestContainerFactory.class);
				} catch (ClassNotFoundException ex) {
					throw new TestContainerException("The default test container factory class name, " + tcfClassName
							+ ", cannot be loaded", ex);
				} catch (ClassCastException ex) {
					throw new TestContainerException("The default test container factory class, " + tcfClassName
							+ ", is not an instance of TestContainerFactory", ex);
				}
			}
		}

		try {
			return defaultTestContainerFactoryClass.newInstance();
		} catch (Exception ex) {
			throw new TestContainerException("The default test container factory, " + defaultTestContainerFactoryClass
					+ ", could not be instantiated", ex);
		}
	}

	// Refer to the class name rather than the class so we do not introduce
	// a required runtime dependency for those that do not want to utilize
	// Grizzly.
	private static final String DEFAULT_TEST_CONTAINER_FACTORY_CLASS_NAME = "com.sun.jersey.test.framework.spi.container.grizzly2.web.GrizzlyWebTestContainerFactory";

	@Deprecated
	private static final String TEST_CONTAINER_FACTORY_PROPERTY_NAME_LEGACY = "test.containerFactory";

	private static final String TEST_CONTAINER_FACTORY_PROPERTY_NAME = "jersey.test.containerFactory";

	/**
	 * Creates an instance of {@link Client}.
	 * 
	 * Checks whether TestContainer provides client instance and if not, ClientFactory obtained through
	 * getClientFactory() will be used to create new client instance.
	 * 
	 * This method is called exactly once when JerseyTest is created.
	 * 
	 * @param tc instance of {@link TestContainer}
	 * @param ad instance of {@link AppDescriptor}
	 * @return A Client instance.
	 */
	protected Client getClient(TestContainer tc, AppDescriptor ad) {
		Client c = tc.getClient();

		if (c != null) {
			return c;
		} else {
			c = getClientFactory().create(ad.getClientConfig());
		}

		// check if logging is required
		boolean enableLogging = System.getProperty("enableLogging") != null;

		if (enableLogging) {
			c.addFilter(new LoggingFilter());
		}

		return c;
	}

	/**
	 * Get the ClientFactory.
	 * <p>
	 * If the client factory has not been explicit set with {@link #setClientFactory(ClientFactory) } then the default
	 * client factory will be obtained.
	 * 
	 * @return ClientFactory instance
	 */
	protected ClientFactory getClientFactory() {
		if (clientFactory == null)
			clientFactory = getDefaultClientFactory();

		return clientFactory;
	}

	private static ClientFactory getDefaultClientFactory() {
		return new ClientFactory() {
			@Override
			public Client create(ClientConfig cc) {
				return Client.create(cc);
			}
		};
	}

	protected void setClientFactory(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	/**
	 * Returns the base URI of the application.
	 * @return The base URI of the application
	 */
	protected URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost/").port(getPort(9998)).build();
	}

	/**
	 * Returns the port to be used in the base URI.
	 * @param defaultPort default port
	 * @return The HTTP port of the URI
	 */
	protected int getPort(int defaultPort) {
		String port = System.getProperty("jersey.test.port");
		if (null != port) {
			try {
				return Integer.parseInt(port);
			} catch (NumberFormatException e) {
				throw new TestContainerException("jersey.test.port with a " + "value of \"" + port
						+ "\" is not a valid integer.", e);
			}
		}

		port = System.getProperty("JERSEY_HTTP_PORT");
		if (null != port) {
			try {
				return Integer.parseInt(port);
			} catch (NumberFormatException e) {
				throw new TestContainerException("JERSEY_HTTP_PORT with a " + "value of \"" + port
						+ "\" is not a valid integer.", e);
			}
		}
		return defaultPort;
	}
}
