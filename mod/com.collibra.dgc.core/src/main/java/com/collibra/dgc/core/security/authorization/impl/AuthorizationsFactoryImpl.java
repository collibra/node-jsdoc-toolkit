package com.collibra.dgc.core.security.authorization.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.MultiValueMap;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

import com.collibra.dgc.core.security.authorization.AuthorizationsFactory;
import com.collibra.dgc.core.security.authorization.GlossaryRight;
import com.collibra.dgc.core.security.authorization.Right;
import com.collibra.dgc.core.security.authorization.RightCategory;
import com.collibra.dgc.core.util.CsvUtility;

/**
 * Factory that loads the predefined {@link Right}s defined by BSG.
 * 
 * @author amarnath
 * 
 */
@Service
public class AuthorizationsFactoryImpl implements AuthorizationsFactory {
	private static final Logger log = LoggerFactory.getLogger(AuthorizationsFactoryImpl.class);

	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String ACTIVE = "active";
	private static final String GLOBAL = "global";
	private static final String RIGHT_CATEGORY = "rightCategory";
	private static final String RIGHT = "right";
	private static final String SUPPORTED_BY_ROLES = "supportedByRoles";
	private static final String DEPENDS_ON = "dependsOn";
	private static final String IMPACT_ON_LOCK = "impactOnLock";

	private final Map<String, Right> idToRight = new HashMap<String, Right>();
	private final MultiValueMap nameToRight = new MultiValueMap();
	private final Map<String, RightCategory> idToRightCategory = new HashMap<String, RightCategory>();
	private final MultiValueMap nameToRightCategories = new MultiValueMap();
	private final List<RightCategory> categories = new ArrayList<RightCategory>();

	/**
	 * Private constructor for singleton
	 */
	protected AuthorizationsFactoryImpl() {
		initialize();
	}

	public Right getRight(String id) {
		return idToRight.get(id);
	}

	public Collection<Right> getRights(String name) {
		Collection<Right> rights = nameToRight.getCollection(name);
		if (rights == null) {
			return new LinkedList<Right>();
		}
		return Collections.unmodifiableCollection(rights);
	}

	public RightCategory getRightCategory(String id) {
		return idToRightCategory.get(id);
	}

	public Collection<RightCategory> getRightCategories() {
		return Collections.unmodifiableCollection(categories);
	}

	public Collection<RightCategory> getRightCategories(String name) {
		Collection<RightCategory> rightCategories = nameToRightCategories.getCollection(name);
		if (rightCategories == null) {
			return new LinkedList<RightCategory>();
		}
		return Collections.unmodifiableCollection(rightCategories);
	}

	/**
	 * Load all the rights
	 */
	private void initialize() {
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = builder.build(getClass().getResourceAsStream(
					"/com/collibra/dgc/core/authorization/rights.xml"));

			for (Object obj : document.getRootElement().getChildren()) {
				Element categoryElement = (Element) obj;
				RightCategory rightCategory = createCategory(categoryElement, null);
				interpret(categoryElement, rightCategory);
				categories.add(rightCategory);
			}
		} catch (Exception e) {
			log.error("Error while parsing rights.xml", e);
			throw new RuntimeException(e);
		}
	}

	private RightCategory createCategory(Element element, RightCategory parent) {
		String id = element.getAttributeValue(ID);
		String name = element.getAttributeValue(NAME);
		RightCategoryImpl rightCateroy = new RightCategoryImpl(id, name, parent);

		String activeValue = element.getAttributeValue(ACTIVE);
		boolean active = true;
		if (activeValue != null) {
			active = Boolean.parseBoolean(activeValue);
		}
		rightCateroy.setActive(active);

		if (parent != null) {
			((RightCategoryImpl) parent).add(rightCateroy);
		}

		// Set if the category is global.
		if (element.getAttributeValue(GLOBAL) != null) {
			((RightCategoryImpl) rightCateroy).setGlobal(Boolean.parseBoolean(element.getAttributeValue(GLOBAL)));
		}

		// Add to internal maps
		if (idToRightCategory.get(id) != null) {
			throw new RuntimeException("Duplicate RightCategory object with id : " + id);
		}

		idToRightCategory.put(id, rightCateroy);
		nameToRightCategories.put(name, rightCateroy);

		return rightCateroy;
	}

	private Right createRight(Element element, RightCategory parent) {
		String id = element.getAttributeValue(ID);
		String name = element.getAttributeValue(NAME);
		Right right = new RightImpl(id, name, parent);

		((RightCategoryImpl) parent).add(right);

		// Add to internal maps
		if (idToRight.get(id) != null) {
			throw new RuntimeException("Duplicate Right object with id : " + id);
		}

		idToRight.put(id, right);
		nameToRight.put(name, right);

		// This information needed for the roles bootstrapped by Collibra.
		Element child = element.getChild(SUPPORTED_BY_ROLES);
		if (child != null) {
			String supportedByRoles = child.getValue().trim();
			StringReader sr = new StringReader(supportedByRoles);
			CSVReader reader = new CSVReader(sr);
			try {
				List<String[]> newlist = reader.readAll();
				if (newlist.size() > 0) {
					((GlossaryRight) right).setSupportedByRoles(newlist.get(0));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Set if the right is global.
		if (element.getAttributeValue(GLOBAL) != null) {
			((RightImpl) right).setGlobal(Boolean.parseBoolean(element.getAttributeValue(GLOBAL)));
		}

		// Get the right on which this right depends.
		child = element.getChild(DEPENDS_ON);
		if (child != null) {
			((RightImpl) right).setDependsOnRights(CsvUtility.getList(child.getValue().trim()));
		}

		// Set the impact on lock information.
		boolean impactOnLock = Boolean.parseBoolean(element.getAttributeValue(IMPACT_ON_LOCK));
		((RightImpl) right).setImpactOnLock(impactOnLock);

		// FIXME : If by by default it is true then ensure you read the value
		// and check. Somehow default values from XSD
		// are not taken.
		// Set active status.
		String activeValue = element.getAttributeValue(ACTIVE);
		boolean active = true;
		if (activeValue != null) {
			active = Boolean.parseBoolean(activeValue);
		}
		((RightImpl) right).setActive(active);

		return right;
	}

	private void interpret(Element categoryElement, RightCategory rightCategory) {
		for (Object obj : categoryElement.getChildren()) {
			Element child = (Element) obj;
			if (child.getName().equals(RIGHT)) {
				createRight(child, rightCategory);
			} else if (child.getName().equals(RIGHT_CATEGORY)) {
				RightCategory childCategory = createCategory(child, rightCategory);
				interpret(child, childCategory);
			} else {
				throw new RuntimeException("Unknown element : " + child);
			}
		}
	}
}
