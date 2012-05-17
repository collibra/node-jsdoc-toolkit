package com.collibra.dgc.core.model;

/**
 * Classes implementing this can print their contents in such a way that a parser could reconstruct the object.
 * 
 * @author dtrog
 * 
 */
public interface Verbalisable {

	/**
	 * 
	 * @return A string that is both human readable and machine parsable.
	 */
	String verbalise();
}
