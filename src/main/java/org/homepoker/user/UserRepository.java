package org.homepoker.user;

import org.homepoker.domain.user.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

/**
 * A repository for persisting and retrieving users.
 * 
 * @author tyler.vangorder
 *
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

				Mono<User> findByLoginId(String loginId);
}
