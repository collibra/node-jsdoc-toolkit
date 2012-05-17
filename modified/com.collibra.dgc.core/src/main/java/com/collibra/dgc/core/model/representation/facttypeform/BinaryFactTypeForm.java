package com.collibra.dgc.core.model.representation.facttypeform;

import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * The {@link Representation} for a {@link BinaryFactType}. Corresponds with a Lexon in our platform common code.
 * 
 * @author dtrog
 * 
 */
public interface BinaryFactTypeForm extends Representation {

	/**
	 * 
	 * @return The head {@link Term}.
	 */
	Term getHeadTerm();

	/**
	 * @param headTerm The head {@link Term}.
	 */
	void setHeadTerm(Term headTerm);

	/**
	 * 
	 * @return The tail {@link Term}.
	 */
	Term getTailTerm();

	/**
	 * @param tailTerm The tail {@link Term}.
	 */
	void setTailTerm(Term tailTerm);

	/**
	 * 
	 * @return The signifier of the role.
	 */
	String getRole();

	/**
	 * 
	 * @return The signifier of the coRole.
	 */
	String getCoRole();

	/**
	 * @return the {@link BinaryFactType} that this {@link BinaryFactTypeForm} represents.
	 */
	BinaryFactType getBinaryFactType();

	/**
	 * 
	 * @return <code>true</code> if this {@link BinaryFactTypeForm} is the preferred one in its {@link Vocabulary} for
	 *         the {@link BinaryFactType}.
	 */
	@Override
	Boolean getIsPreferred();

	/**
	 * Set the new role.
	 * @param role The signifier of the role.
	 */
	void setRole(String role);

	/**
	 * Set the new roles.
	 * @param role The new role expression
	 * @param coRole The new coRole expression
	 */
	void setRoles(String role, String coRole);

	/**
	 * Set the new coRole.
	 * @param coRole The signifier of the corole.
	 */
	void setCoRole(String coRole);

	/**
	 * Set the new {@link BinaryFactType}.
	 * @param binaryFactType the binaryFactType to use
	 */
	void setBinaryFactType(BinaryFactType binaryFactType);

	/**
	 * @return The left {@link ReadingDirection} of this {@link BinaryFactTypeForm}.
	 */
	ReadingDirection getLeftPlaceHolder();

	/**
	 * @return The right {@link ReadingDirection} of this {@link BinaryFactTypeForm}.
	 */
	ReadingDirection getRightPlaceHolder();

	/**
	 * To change the {@link BinaryFactTypeForm}.
	 * @param head The head {@link Term}
	 * @param role Role
	 * @param coRole Corole
	 * @param tail The tail {@link Term}
	 */
	void update(Term head, String role, String coRole, Term tail);
}
