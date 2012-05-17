/**
 * 
 */
package com.collibra.dgc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.collibra.dgc.core.util.HashUtil;

/**
 * @author dieterwachters
 * 
 */
public class TestHashing {
	@Test
	public void testHash() {
		final Set<String> salts = new HashSet<String>();
		tryPassword("admin1906", salts);
		tryPassword("mypassword123", salts);
		tryPassword("_This is my . special pa$$word_", salts);
		tryPassword("Now I'm really going cräzy", salts);
		tryPassword("134210942840293184230498214092842304572047823109", salts);
		tryPassword("&@é\"'(§è!çà)-", salts);
		tryPassword(
				"This is a very long sentence that I use for my password. I hope this works because it should be extremely secure.",
				salts);
	}

	private void tryPassword(String password, final Set<String> salts) {
		// Create the initial hash.
		String[] hashed = HashUtil.hash(password);
		// Check again
		String[] check = HashUtil.hash(password, hashed[1]);

		assertEquals(hashed[0], check[0]);
		assertEquals(hashed[1], check[1]);

		System.err.println("Password: " + password);
		System.err.println("\tHashed: " + hashed[0]);
		System.err.println("\tSalt used: " + hashed[1]);

		assertFalse(salts.contains(hashed[1]));
		salts.add(hashed[1]);
	}
}
