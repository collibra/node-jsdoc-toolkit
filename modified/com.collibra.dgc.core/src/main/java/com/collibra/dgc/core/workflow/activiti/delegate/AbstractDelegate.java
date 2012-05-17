package com.collibra.dgc.core.workflow.activiti.delegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.FixedValue;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;

import com.collibra.dgc.core.AppContext;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.user.Member;
import com.collibra.dgc.core.util.CsvUtility;

/**
 * Provides the methods for all classes implementing {@link ActivityBehavior} or {@link JavaDelegate} in BSG to avoid
 * code repetition.
 * @author amarnath
 * 
 */
public abstract class AbstractDelegate implements JavaDelegate {
	protected String targetState;

	public void setTargetState(FixedValue value) {
		this.targetState = value.getExpressionText();
	}

	protected String getTargetState() {
		return targetState;
	}

	// TODO Port: this used to return the user names, but now returns user and group id's
	protected final String getUsers(Collection<Member> members) {
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		for (Member member : members) {
			sb.append("user(").append(member.getOwnerId()).append(")");
			if (counter < members.size() - 1) {
				sb.append(",");
			}

			counter++;
		}

		return sb.toString();
	}

	// TODO Port: this used to return the user names, but now returns user and group id's
	protected final String getMembersAsCsv(Collection<Member> members) {
		List<String> values = new ArrayList<String>(members.size());
		for (Member m : members) {
			values.add(m.getOwnerId());
		}

		return CsvUtility.getCSV(values);
	}

	protected void changeState(Representation representation) {
		changeState(representation, targetState);
	}

	protected void changeState(Representation representation, String stateSignifier) {
		if (stateSignifier == null) {
			return;
		}

		for (Representation statusRep : AppContext.getRepresentationService().findStatusTerms(representation)) {
			if (stateSignifier.equals(((Term) statusRep).getSignifier())) {
				AppContext.getRepresentationService().changeStatus(representation, (Term) statusRep);
				break;
			}
		}
	}
}
