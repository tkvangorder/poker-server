package org.homepoker.user;

import org.homepoker.domain.user.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * A repository for persisting and retrieving users.
 * 
 * @author tyler.vangorder
 *
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

	
}
