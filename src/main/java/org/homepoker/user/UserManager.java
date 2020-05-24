package org.homepoker.user;

import java.util.List;

import org.homepoker.user.domain.User;
import org.homepoker.user.domain.UserCriteria;

public interface UserManager {

	/**
	 * Create a new user, validating the correct data has been supplied.
	 * @param user
	 * @return
	 */
	User createUser(User user);
	
	/**
	 * Find existing users
	 * 
	 * @param criteria
	 * @return A list of matching users or an empty list if no users are found that match the criteria.
	 */
	List<User> finderUsers(UserCriteria criteria);
	
	/**
	 * Delete a user from the server.
	 * 
	 * @param user
	 */
	void deleteUser(User user);
}
