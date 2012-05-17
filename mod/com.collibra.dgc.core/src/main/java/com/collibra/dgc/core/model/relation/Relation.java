package com.collibra.dgc.core.model.relation;

import java.util.Date;

import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.Verbalisable;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;

public interface Relation extends Resource, Verbalisable {

	BinaryFactTypeForm getType();

	Term getSource();

	Term getTarget();

	Representation getStatusRepresentation();

	Term getStatus();

	Date getStartingDate();

	Date getEndingDate();

	Boolean getIsGenerated();

	boolean isSourceToTargetDirection();

}
