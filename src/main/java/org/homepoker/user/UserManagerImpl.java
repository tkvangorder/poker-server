package org.homepoker.user;

import javax.annotation.PostConstruct;

import org.homepoker.common.ValidationException;
import org.homepoker.domain.user.User;
import org.homepoker.domain.user.UserCriteria;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserManagerImpl implements UserManager {

	private final UserRepository userRepository;

	private final ReactiveMongoOperations mongoOperations; 

	public UserManagerImpl(UserRepository userRepository, ReactiveMongoOperations mongoOperations) {
		this.userRepository = userRepository;
		this.mongoOperations = mongoOperations;
	}

	@PostConstruct
	public void setup() {
		//Create a unique index on the email.
		mongoOperations
			.indexOps(User.class)
				.ensureIndex(
					new Index().on("email", Direction.ASC).unique()
				).block();
	}
	@Override
	public Mono<User> registerUser(User user) {
		
		Assert.notNull(user, "The user information cannot be null");
		Assert.isTrue(!StringUtils.hasText(user.getId()), "The user ID must be null when registering a new user.");
		Assert.hasText(user.getEmail(), "The user email address is required.");
		Assert.hasText(user.getName(), "The user name is required.");
		Assert.hasText(user.getPassword(), "The user password is required.");
		Assert.hasText(user.getPhone(), "The user phone is required.");
		
		if (StringUtils.isEmpty(user.getAlias())) {
			//If no alias is provided, default it to the user's name
			user.setAlias(user.getName());
		}
		
		return
			userRepository.insert(user)
				.onErrorMap(
						//Map a duplicate key to a user-friendly message.
						e -> e instanceof DuplicateKeyException,
						e -> new ValidationException("There is already a user registered with that email.")
				);
	}

	@Override
	public Mono<User> updateUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public Flux<User> finderUsers(UserCriteria criteria) {
		return userRepository.findAll();
	}

	@Override
	public Mono<Void> deleteUser(String userId) {
		return userRepository.deleteById(userId);
	}

}
