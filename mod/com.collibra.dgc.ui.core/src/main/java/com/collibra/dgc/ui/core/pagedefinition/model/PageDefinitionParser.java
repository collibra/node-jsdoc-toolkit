/**
 * 
 */
package com.collibra.dgc.ui.core.pagedefinition.model;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses page definitions from an XML file.
 * @author dieterwachters
 */
public class PageDefinitionParser extends DefaultHandler {
	private static final String MODULE = "module";
	private static final String REGIONS = "regions";
	private static final String PAGE_DEFINITION = "page-definition";
	private static final String CONFIGURATION = "configuration";
	private static final String MATCHING_RULES = "matching-rules";
	private static final String RULE = "rule";

	private final Stack<Object> nodeStack = new Stack<Object>();
	private PageDefinition pageDefinition;
	private final Stack<String> currentPropertyName = new Stack<String>();

	/**
	 * Parse the page definition in the given {@link InputStream}.
	 * @param input The inputstream to read the page definitions from.
	 * @return The read page definition.
	 * @throws PageDefinitionException When something goes wrong while parsing the definitions file.
	 */
	public static PageDefinition parsePageDefinition(final InputStream input) throws PageDefinitionException {
		try {
			final SAXParserFactory factory = SAXParserFactory.newInstance();
			final SAXParser saxParser = factory.newSAXParser();
			final PageDefinitionParser parser = new PageDefinitionParser();
			saxParser.parse(input, parser);
			return parser.pageDefinition;
		} catch (Exception e) {
			throw new PageDefinitionException(e.getLocalizedMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String,
	 * org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (PAGE_DEFINITION.equals(qName) && nodeStack.isEmpty()) {
			final String name = attributes.getValue("name");
			final String description = attributes.getValue("description");
			pageDefinition = new PageDefinition(name, description);
			nodeStack.push(pageDefinition);
		} else if (MATCHING_RULES.equals(qName) && !nodeStack.isEmpty() && nodeStack.peek() instanceof PageDefinition) {
			nodeStack.push(MATCHING_RULES);
		} else if (RULE.equals(qName) && !nodeStack.isEmpty() && nodeStack.peek().equals(MATCHING_RULES)) {
			final String path = attributes.getValue("path");
			final String type = attributes.getValue("type");
			final MatchingRule rule = new MatchingRule(path, type);
			final PageDefinition pd = (PageDefinition) nodeStack.get(nodeStack.size() - 2);
			pd.addMatchingRule(rule);
			nodeStack.push(rule);
		} else if (CONFIGURATION.equals(qName) && nodeStack.peek() instanceof PageDefinition) {
			nodeStack.push(CONFIGURATION);
		} else if (MODULE.equals(qName) && !nodeStack.isEmpty()) {
			final String name = attributes.getValue("name");
			if (name == null) {
				throw new SAXException("The 'name' attribute of a module must always be specified.");
			}

			final ModuleConfiguration module;
			if (CONFIGURATION.equals(nodeStack.peek())) {
				final PageDefinition parent = (PageDefinition) nodeStack.get(nodeStack.size() - 2);
				module = new ModuleConfiguration(pageDefinition, parent, name);
				parent.setModuleConfiguration(module);
			} else if (nodeStack.peek() instanceof Region) {
				final Region parent = (Region) nodeStack.peek();
				module = new ModuleConfiguration(pageDefinition, parent, name);
				parent.addModuleConfiguration(module);
			} else {
				throw new SAXException("Unexpected 'module' element at this location.");
			}

			if (attributes.getLength() > 1) {
				final Map<String, Object> properties = new HashMap<String, Object>();
				for (int i = 0; i < attributes.getLength(); i++) {
					if (!attributes.getQName(i).equals("name")) {
						properties.put(attributes.getQName(i), attributes.getValue(i));
					}
				}
				module.setProperties(properties);
			}

			nodeStack.push(module);
		} else if (REGIONS.equals(qName)) {
			if (!(nodeStack.peek() instanceof ModuleConfiguration)) {
				throw new SAXException("No 'regions' element is allowed when not inside a module.");
			}
			nodeStack.push(REGIONS);
		} else if (nodeStack.isEmpty()) {
			throw new SAXException("Unexpected element '" + qName + "' found.");
		} else if (nodeStack.peek() instanceof PropertiesNode) {
			final PropertiesNode propsNode = (PropertiesNode) nodeStack.peek();
			Map<String, Object> props = propsNode.getProperties();
			if (props == null) {
				props = new HashMap<String, Object>();
				propsNode.setProperties(props);
			}
			currentPropertyName.push(qName);
			nodeStack.push(props);
		} else if (nodeStack.peek() instanceof Map) {
			final Map<String, Object> parentProps = (Map) nodeStack.peek();

			final Map<String, Object> props;
			if (parentProps.containsKey(currentPropertyName.peek())) {
				props = (Map) parentProps.get(currentPropertyName.peek());
			} else {
				props = new HashMap<String, Object>();
				parentProps.put(currentPropertyName.peek(), props);
			}

			if (attributes.getLength() > 0) {
				final Map<String, Object> childProps = new HashMap<String, Object>();
				for (int i = 0; i < attributes.getLength(); i++) {
					childProps.put(attributes.getQName(i), attributes.getValue(i));
				}
				props.put(qName, childProps);
			}

			currentPropertyName.push(qName);
			nodeStack.push(props);
		} else if (REGIONS.equals(nodeStack.peek())) {
			final ModuleConfiguration module = (ModuleConfiguration) nodeStack.get(nodeStack.size() - 2);
			final Region region = new Region(pageDefinition, module, qName);
			module.addRegion(region);
			nodeStack.push(region);
		} else {
			throw new SAXException("Unexpected element '" + qName + "' found.");
		}

		super.startElement(uri, localName, qName, attributes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		final Object popped = nodeStack.pop();
		if (popped instanceof Map && !currentPropertyName.isEmpty()) {
			currentPropertyName.pop();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (!nodeStack.isEmpty() && nodeStack.peek() instanceof Map) {
			final String val = new String(ch, start, length);
			if (!val.trim().isEmpty()) {
				((Map<String, Object>) nodeStack.peek()).put(currentPropertyName.peek(), val.trim());
			}
		}
	}
}
