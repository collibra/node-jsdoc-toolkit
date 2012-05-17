package com.collibra.dgc.core.model.community;

import org.springframework.stereotype.Service;

/**
 * Factory methods for creating {@link Community}s.
 * @author dtrog
 * 
 */
@Service
public interface CommunityFactory {
	/**
	 * Create a new {@link Community}.
	 * @param name The name for the {@link Community}
	 * @param uri URI for the {@link Community}
	 * @return The new {@link Community}
	 */
	Community makeCommunity(String name, String uri);

	/**
	 * Create a new {@link Community}.
	 * @param name The name for the {@link Community}
	 * @param uri URI for the {@link Community}
	 * @param language The language of the {@link Community}
	 * @return The new {@link Community}
	 */
	Community makeCommunity(String name, String uri, String language);

	/**
	 * Create a new {@link Community} with Language set to default English.
	 * @param community {@link Community} that owns the {@link Community}
	 * @param name The name for the {@link Community}
	 * @param uri URI for the {@link Community}
	 * @return The new {@link Community}
	 */
	Community makeCommunity(Community community, String name, String uri);

	/**
	 * Create a new {@link Community}.
	 * @param community {@link Community} that owns the {@link Community}
	 * @param name The name for the {@link Community}
	 * @param uri URI for the {@link Community}
	 * @param language The language of the {@link Community}
	 * @return The new {@link Community}
	 */
	Community makeCommunity(Community community, String name, String uri, String language);
}
