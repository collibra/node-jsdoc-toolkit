/**
 * 
 */
package com.collibra.dgc.ui.core.model;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.collibra.dgc.ui.core.pagedefinition.model.MatchingRule;
import com.collibra.dgc.ui.core.pagedefinition.model.ModuleConfiguration;
import com.collibra.dgc.ui.core.pagedefinition.model.Node;
import com.collibra.dgc.ui.core.pagedefinition.model.PageDefinition;
import com.collibra.dgc.ui.core.pagedefinition.model.PageDefinitionParser;
import com.collibra.dgc.ui.core.pagedefinition.model.Region;

/**
 * @author dieterwachters
 * 
 */
public class TestPageDefinitionParsing {
	@Test
	public void testPageDefinitionsParsing() throws Exception {
		final PageDefinition def1 = PageDefinitionParser.parsePageDefinition(TestPageDefinitionParsing.class
				.getResourceAsStream("/com/collibra/dgc/ui/core/model/page-definitions1.xml"));

		Assert.assertEquals("PageDefinition1", def1.getName());
		Assert.assertEquals("This is the description of page definition 1", def1.getDescription());

		final ModuleConfiguration module = def1.getModuleConfiguration();
		Assert.assertNotNull(module);
		Assert.assertEquals("pages/blank", module.getName());

		Assert.assertEquals(1, module.getRegions().size());

		final Region contentRegion = module.getRegion("content");
		Assert.assertNotNull(contentRegion);
		Assert.assertEquals(1, contentRegion.getModuleConfigurations().size());

		final ModuleConfiguration tabbarModule = contentRegion.getModuleConfigurations().get(0);
		Assert.assertEquals("widgets/tabbar", tabbarModule.getName());

		Assert.assertEquals("1", ((Map) ((Map) tabbarModule.getProperty("tabs")).get("tab1")).get("index"));
		Assert.assertEquals("overview", ((Map) ((Map) tabbarModule.getProperty("tabs")).get("tab1")).get("id"));
		Assert.assertEquals("another", ((Map) ((Map) tabbarModule.getProperty("tabs")).get("tab2")).get("id"));

		final Region tab1Region = tabbarModule.getRegion("tab1");
		Assert.assertNotNull(tab1Region);
		Assert.assertEquals(1, tab1Region.getModuleConfigurations().size());

		final ModuleConfiguration twoColumnsModule = tab1Region.getModuleConfigurations().get(0);
		Assert.assertEquals("pages/two-columns", twoColumnsModule.getName());

		Assert.assertEquals("75%", twoColumnsModule.getProperty("left-column-width"));
		Assert.assertEquals("25%", twoColumnsModule.getProperty("right-column-width"));

		Assert.assertEquals(2, twoColumnsModule.getRegions().size());

		final Region leftColumnRegion = twoColumnsModule.getRegion("left-column");
		Assert.assertNotNull(leftColumnRegion);
		Assert.assertEquals(2, leftColumnRegion.getModuleConfigurations().size());
		Assert.assertEquals("groupX", leftColumnRegion.getModuleConfigurations().get(0).getProperty("group"));
		Assert.assertNull(leftColumnRegion.getModuleConfigurations().get(0).getProperty("attribute"));
		Assert.assertNotNull(leftColumnRegion.getModuleConfigurations().get(1).getProperty("attribute"));
		Assert.assertNull(leftColumnRegion.getModuleConfigurations().get(1).getProperty("group"));

		final Region rightColumnRegion = twoColumnsModule.getRegion("right-column");
		Assert.assertNotNull(rightColumnRegion);

		Assert.assertEquals(1, def1.getMatchingRules().size());
		final MatchingRule rule = def1.getMatchingRules().get(0);
		Assert.assertEquals("term", rule.getPath());
		Assert.assertEquals("00000000-0000-0000-0000-000000005033", rule.getType());
	}

	@Test
	public void testPathCreation() throws Exception {
		final PageDefinition def = PageDefinitionParser.parsePageDefinition(TestPageDefinitionParsing.class
				.getResourceAsStream("/com/collibra/dgc/ui/core/model/page-definitions1.xml"));

		final Node node1 = def.getModuleConfiguration().getRegion("content").getModuleConfigurations().get(0);
		System.out.println(node1.getPath());
		final Node found1 = def.findMatchingNode(node1.getPath());
		Assert.assertEquals(node1, found1);

		final Node node2 = def.getModuleConfiguration().getRegion("content").getModuleConfigurations().get(0)
				.getRegion("tab1").getModuleConfigurations().get(0).getRegion("right-column").getModuleConfigurations()
				.get(0);
		System.out.println(node2.getPath());
		final Node found2 = def.findMatchingNode(node2.getPath());
		Assert.assertEquals(node2, found2);
	}
}
