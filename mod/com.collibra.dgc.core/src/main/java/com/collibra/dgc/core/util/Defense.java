package com.collibra.dgc.core.util;

import java.util.Collection;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;

/**
 * Glossary defense throwing {@link IllegalArgumentException}s.
 */
public class Defense {

	/**
	 * Validate that the given object is not null.
	 * 
	 * @param object the object to check for null
	 * @param errorCode the name used in the message if object == null
	 * @throws IllegalArgumentException if the given object is null
	 * @param params The parameters for i18n
	 * @throws {@link com.collibra.dgc.core.exceptions.IllegalArgumentException}
	 */
	public static void notNull(final Object object, final String errorCode, final Object... params) {
		if (object == null) {
			throw new com.collibra.dgc.core.exceptions.IllegalArgumentException(errorCode, params);
		}
	}

	/**
	 * Validate that the given object is not null and not empty.
	 * 
	 * @param object the object to check for null and emptiness
	 * @param nullErrorCode the name used in the message if object is null
	 * @param emptyErrorCode the name used in the message if object is empty
	 * @param params The parameters for i18n
	 * @throws {@link com.collibra.dgc.core.exceptions.IllegalArgumentException}
	 */
	public static void notEmpty(final String object, final String nullErrorCode, final String emptyErrorCode,
			final Object... params) {

		notNull(object, nullErrorCode, params);

		if (object.trim().isEmpty())
			throw new com.collibra.dgc.core.exceptions.IllegalArgumentException(emptyErrorCode, params);
	}

	/**
	 * Validate that the given condition is true.
	 * 
	 * @param condition The condition that must be true
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param params The parameters for i18n
	 * @throws {@link com.collibra.dgc.core.exceptions.IllegalArgumentException}
	 */
	public static void assertTrue(final boolean condition, final String errorCode, final Object... params) {
		if (!condition)
			throw new com.collibra.dgc.core.exceptions.IllegalArgumentException(errorCode, params);
	}

	/**
	 * Validate that the given condition is false.
	 * 
	 * @param condition The condition that must be false
	 * @param errorCode The {@link DGCErrorCodes}
	 * @param params The parameters for i18n
	 * @throws {@link com.collibra.dgc.core.exceptions.IllegalArgumentException}
	 */
	public static void assertFalse(final boolean condition, final String errorCode, final Object... params) {
		if (condition)
			throw new com.collibra.dgc.core.exceptions.IllegalArgumentException(errorCode, params);
	}

	/**
	 * Validate that the given collection is not null and not empty.
	 * 
	 * @param collection the collection to check for emptiness
	 * @param nullErrorCode the name used in the message if the collection object is null
	 * @param emptyErrorCode the name used in the message if the collection is empty
	 * @param params The parameters for i18n
	 * @throws {@link com.collibra.dgc.core.exceptions.IllegalArgumentException}
	 */
	public static void notEmpty(final Collection<?> collection, final String nullErrorCode,
			final String emptyErrorCode, final Object... params) {

		notNull(collection, nullErrorCode);

		if (collection.isEmpty())
			throw new com.collibra.dgc.core.exceptions.IllegalArgumentException(emptyErrorCode);
	}

}
