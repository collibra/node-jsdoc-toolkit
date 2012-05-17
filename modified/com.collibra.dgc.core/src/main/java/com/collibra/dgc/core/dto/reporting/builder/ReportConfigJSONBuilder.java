/**
 * 
 */
package com.collibra.dgc.core.dto.reporting.builder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONException;
import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.reporting.ReportConfigVisitor;
import com.collibra.dgc.core.dto.filters.PropertyFilter;
import com.collibra.dgc.core.dto.filters.PropertyFilter.FilterOperator;
import com.collibra.dgc.core.dto.reporting.ColumnConfig;
import com.collibra.dgc.core.dto.reporting.ReportConfig;
import com.collibra.dgc.core.dto.reporting.SingleValueListAttributeColumnConfig;
import com.collibra.dgc.core.dto.reporting.StatusColumnConfig;
import com.collibra.dgc.core.dto.reporting.StringAttributeColumnConfig;
import com.collibra.dgc.core.dto.reporting.TermColumnConfig;
import com.collibra.dgc.core.dto.reporting.VocabularyColumnConfig;
import com.collibra.dgc.core.dto.sorters.PropertyOrder;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.RepresentationService;

/**
 * @author fvdmaele
 * 
 */
@Service
public class ReportConfigJSONBuilder implements ReportConfigVisitor {

	@Autowired
	private RepresentationService representationService;

	private final JSONObject result = new JSONObject();
	private JSONObject parent = null;
	private TermColumnConfig tConfig = null;
	private HashMap<String, JSONObject> jsonMap;
	private final HashMap<TermColumnConfig, JSONObject> termColumnToJSONObjectMap = new HashMap<TermColumnConfig, JSONObject>();

	public String getJSON() {
		return result.toString();
	}

	public String toJSON(ReportConfig config) {
		config.accept(this);
		return result.toString();
	}

	public ReportConfig fromJSON(String jsonString) {
		jsonMap = new HashMap<String, JSONObject>();

		JSONObject json = new JSONObject(jsonString);
		JSONObject report = json.getJSONObject("reportconfig");

		JSONObject designationObject = report.getJSONObject("term");
		handleColumn("term", designationObject, null);

		ReportConfig reportConfig = new ReportConfig(tConfig);

		// display start
		Integer displayStart = report.optInt("displayStart");
		if (displayStart != null)
			reportConfig.setDisplayStart(displayStart);

		// display length
		Integer displayLength = report.optInt("displayLength");
		if (displayLength != null)
			reportConfig.setDisplayLength(displayLength);

		// column order
		JSONArray columns = report.optJSONArray("columnViewOrder");
		if (columns != null) {
			List<String> columnNames = new LinkedList<String>();
			for (int i = 0; i < columns.length(); i++)
				columnNames.add(columns.getString(i));
			reportConfig.setColumnViewOrder(columnNames);
		}
		return reportConfig;
	}

	protected void handleColumn(String key, JSONObject jsonColumn, TermColumnConfig parent) {
		ColumnConfig config = null;
		if (key.equals("vocabulary")) {
			config = new VocabularyColumnConfig(parent);
			JSONArray included = jsonColumn.optJSONArray("included");
			if (included != null) {
				for (int i = 0; i < included.length(); i++) {
					Vocabulary voc = representationService.findVocabularyByResourceId(included.getString(i));
					((VocabularyColumnConfig) config).getVocabularies().add(voc);
				}
			}
		}
		if (key.equals("term")) {
			config = new TermColumnConfig();
			tConfig = (TermColumnConfig) config;
			Iterator<String> keys = jsonColumn.keys();
			while (keys.hasNext()) {
				String childKey = keys.next();
				if (!childKey.equals("name")) {
					try {
						JSONObject columnObject = jsonColumn.getJSONObject(childKey);
						handleColumn(childKey, columnObject, tConfig);
					} catch (JSONException e) {

					}
				}
			}
		}
		if (key.equals("status")) {
			config = new StatusColumnConfig(parent);
			JSONArray included = jsonColumn.optJSONArray("included");
			if (included != null) {
				for (int i = 0; i < included.length(); i++) {
					Term t = representationService.findTermByResourceId(included.getString(i));
					((StatusColumnConfig) config).getStatusses().add(t);
				}
			}
		}
		if (key.equals("stringattribute")) {
			String attrTypeRID = jsonColumn.getString("attributetype");
			Term attrLabel = representationService.getTermWithError(attrTypeRID);
			config = new StringAttributeColumnConfig(parent, attrLabel);
		}
		if (key.equals("singlevaluelistattribute")) {
			String attrTypeRID = jsonColumn.getString("attributetype");
			Term attrLabel = representationService.getTermWithError(attrTypeRID);
			config = new SingleValueListAttributeColumnConfig(parent, attrLabel);
		}
		if (config != null) {
			String name = jsonColumn.optString("name");
			config.setName(name);

			try {
				// get sorting
				Boolean sortDESC = jsonColumn.getBoolean("sortDESC");
				config.setPropertyOrder(new PropertyOrder("", sortDESC));
			} catch (JSONException e) {
			}
			try {
				// get filtering
				JSONObject filter = jsonColumn.getJSONObject("filter");
				String filterValue = filter.getString("value");
				String operator = filter.getString("operator");
				config.setPropertyFilter(new PropertyFilter("", FilterOperator.valueOf(operator), filterValue));
			} catch (JSONException e) {
			}
		}
	}

	@Override
	public void visit(ReportConfig config) {
		JSONObject root = new JSONObject();
		result.put("reportconfig", root);
		root.put("displayStart", config.getDisplayStart());
		root.put("displayLength", config.getDisplayLength());
		root.put("columnViewOrder", config.getColumnViewOrder());

		parent = root;
	}

	@Override
	public void visit(TermColumnConfig config) {
		// parent is null, so this is the root object
		if (parent == null) {
			JSONObject root = new JSONObject();
			result.put("reportconfig", root);

			parent = root;
		}

		JSONObject newObject = getJSONObjectToAppend(config);
		parent.put("term", newObject);
		setName(config, newObject);
		newObject.put("filter", getFilterObject(config));
		addSorting(newObject, config);
	}

	@Override
	public void visit(VocabularyColumnConfig config) {

		JSONObject parentObject = getJSONObjectToAppend(config.getParent());

		JSONObject thisColumn = new JSONObject();
		setName(config, thisColumn);
		thisColumn.put("filter", getFilterObject(config));
		addSorting(thisColumn, config);
		if (!config.getVocabularies().isEmpty()) {
			List<String> vocList = new LinkedList<String>();
			for (Vocabulary v : config.getVocabularies())
				vocList.add(v.getId().toString());

			thisColumn.put("included", vocList);
		}

		parentObject.put("vocabulary", thisColumn);
	}

	@Override
	public void visit(StatusColumnConfig config) {

		JSONObject parentObject = getJSONObjectToAppend(config.getParent());

		JSONObject thisColumn = new JSONObject();
		setName(config, thisColumn);
		thisColumn.put("filter", getFilterObject(config));
		addSorting(thisColumn, config);
		if (!config.getStatusses().isEmpty()) {
			List<String> statusList = new LinkedList<String>();
			for (Term t : config.getStatusses())
				statusList.add(t.getId().toString());

			thisColumn.put("included", statusList);
		}

		parentObject.put("status", thisColumn);
	}

	@Override
	public void visit(StringAttributeColumnConfig config) {
		JSONObject parentObject = getJSONObjectToAppend(config.getParent());

		JSONObject thisColumn = new JSONObject();
		setName(config, thisColumn);
		thisColumn.put("attributetype", config.getAttributeLabel().getId().toString());
		thisColumn.put("filter", getFilterObject(config));
		addSorting(thisColumn, config);

		parentObject.put("stringattribute", thisColumn);
	}

	@Override
	public void visit(SingleValueListAttributeColumnConfig config) {
		JSONObject parentObject = getJSONObjectToAppend(config.getParent());

		JSONObject thisColumn = new JSONObject();
		setName(config, thisColumn);
		thisColumn.put("attributetype", config.getAttributeLabel().getId().toString());
		thisColumn.put("filter", getFilterObject(config));
		addSorting(thisColumn, config);

		parentObject.put("singlevaluelistattribute", thisColumn);
	}

	protected JSONObject getFilterObject(ColumnConfig config) {
		JSONObject filter = new JSONObject();
		if (config.isFiltered()) {
			filter.put("operator", config.getPropertyFilter().getFilterOperator().toString());
			filter.put("value", config.getPropertyFilter().getValue());
		}

		return filter;
	}

	protected void addSorting(JSONObject object, ColumnConfig config) {
		if (config.isOrdered())
			object.put("sortDESC", config.getPropertyOrder().sortDesc());
	}

	protected void setName(ColumnConfig config, JSONObject jsonObj) {
		if (config.getName() != null)
			jsonObj.put("name", config.getName());
	}

	protected JSONObject getJSONObjectToAppend(TermColumnConfig config) {
		if (config == null)
			return null;

		JSONObject result = termColumnToJSONObjectMap.get(config);

		if (result == null) {
			result = new JSONObject();
			termColumnToJSONObjectMap.put(config, result);
		}
		return result;
	}
}
