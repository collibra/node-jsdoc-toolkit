/**
 * 
 */
package com.collibra.dgc.core.model.configuration;

import java.util.List;

import com.collibra.dgc.core.model.representation.Representation;

/**
 * @author fvdmaele
 *
 */
public interface AttributeAndRelationTypesConfigurationCategory extends ConfigurationCategory {

	public List<Representation> getAttributeAndRelationTypes();

	void addAttributeOrRelationType(Representation r);

	void addAttributeOrRelationType(int index, Representation r);

	boolean changeAttributeOrRelationTypeOrder(Representation r, int index);
	
//	public List<Term> getAttributeTypes();
//	
//	public List<Term> getRelationTypes();
}
