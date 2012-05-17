package com.collibra.dgc.core.security.authorization;

/**
 * Interface for {@link Right} implementation that has information specific to glossary {@link Right}s and roles.
 * @author amarnath
 * 
 */
public interface GlossaryRight extends Right {
	/**
	 * Get the role names supported.
	 * @return The role names.
	 */
	String[] getSupportedByRoles();

	/**
	 * Set the role names supported.
	 * @param supportedByRoles The role names.
	 */
	void setSupportedByRoles(String[] supportedByRoles);
}
