package org.homepoker.user;

import org.homepoker.user.domain.User;
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
