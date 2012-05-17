package com.collibra.dgc.core.model.meaning;

import java.util.Set;

import com.collibra.dgc.core.model.representation.SimpleStatement;

public interface SimpleProposition extends Proposition {
	Set<SimpleStatement> getSimpleStatements();

	void addSimpleStatement(SimpleStatement ss);
}
