package org.homepoker.user;

import org.homepoker.domain.user.User;
import org.homepoker.domain.user.UserCriteria;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserManager {

	/**
	 * Register a new user, validating the correct data has been supplied.
	 * @param user
	 * @return
	 */
	Mono<User> registerUser(User user);

	/**
	 * Update an existing user
	 * @param user
	 * @return
	 */
	Mono<User> updateUser(User user);

	/**
	 * Find existing users
	 * 
	 * @param criteria
	 * @return A list of matching users or an empty list if no users are found that match the criteria.
	 */
	Flux<User> finderUsers(UserCriteria criteria);
	
	/**
	 * Delete a user from the server.
	 * 
	 * @param userId
	 */
	Mono<Void> deleteUser(String userId);
}
