/**
 * 
 */
package com.collibra.dgc.core.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHashRequest;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

/**
 * @author dieterwachters
 * 
 */
public class HashUtil {
	// The private salt part to use when hashing. For extra security.
	private static final String PRIVATE_SALT = "Collibra_Salt_";

	/**
	 * This will calculate a secure hash using SHA-512 with 10000 iterations.
	 * 
	 * @param input
	 *            The input to hash
	 * @param salt
	 *            The salt to use. Leave this null for generating a new hash.
	 *            Fill in for check an existing hash.
	 * @return A String array with the hash and the used salt.
	 */
	public static String[] hash(String input, ByteSource salt) {
		DefaultHashService hashService = new DefaultHashService();
		hashService.setPrivateSalt(new SimpleByteSource(PRIVATE_SALT));
		final Hash hash = hashService.computeHash(new SimpleHashRequest(null,
				new SimpleByteSource(input), salt, 10000));
		return new String[] { hash.toBase64(), hash.getSalt().toBase64() };
	}

	/**
	 * This will calculate a secure hash using SHA-512 with 10000 iterations.
	 * 
	 * @param input
	 *            The input to hash
	 * @param salt
	 *            The salt to use. Leave this null for generating a new hash.
	 *            Fill in for check an existing hash.
	 * @return A String array with the hash and the used salt.
	 */
	public static String[] hash(String input, String salt) {
		return hash(input,
				salt == null ? null : new SimpleByteSource(Base64.decode(salt)));
	}

	/**
	 * This will calculate a secure hash using SHA-512 with 10000 iterations and
	 * a random salt (with private part). This is the same as calling
	 * HashUtil.hash(input, null)
	 * 
	 * @param input
	 *            The input to hash
	 * @return A String array with the hash and the used salt.
	 */
	public static String[] hash(String input) {
		return hash(input, (ByteSource) null);
	}
}
