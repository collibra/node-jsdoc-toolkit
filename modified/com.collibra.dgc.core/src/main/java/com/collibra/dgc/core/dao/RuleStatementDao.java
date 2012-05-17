package com.collibra.dgc.core.dao;

import java.util.List;

import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.rules.RuleStatement;

public interface RuleStatementDao extends AbstractDao<RuleStatement> {

	/**
	 * 
	 * @param ss
	 * @return
	 */
	List<RuleStatement> find(SimpleStatement ss);

	/**
	 * 
	 * @param bftf
	 * @return
	 */
	List<RuleStatement> find(BinaryFactTypeForm bftf);

	/**
	 * 
	 * @param term
	 * @return
	 */
	List<RuleStatement> find(Term term);

	// /**
	// * Check if the rule statement is persisted or not.
	// * @param ruleStatement The {@link RuleStatement}
	// * @return {@code true} if it is persisted, {@code false} otherwise
	// */
	// boolean isPersisted(final RuleStatement ruleStatement);
}
