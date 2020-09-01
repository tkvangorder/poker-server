package org.homepoker.user;

import javax.annotation.PostConstruct;

import org.homepoker.common.ValidationException;
import org.homepoker.domain.user.User;
import org.homepoker.domain.user.UserCriteria;
import org.homepoker.domain.user.UserInformationUpdate;
import org.homepoker.domain.user.UserPasswordChangeRequest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserManagerImpl implements UserManager {

	private final UserRepository userRepository;
	private final ReactiveMongoOperations mongoOperations; 
	private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
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
					new Index().on("loginId", Direction.ASC).unique()
				)
				.block();
		mongoOperations
		.indexOps(User.class)
			.ensureIndex(
				new Index().on("email", Direction.ASC).unique()
			)
			.block();
	}
	@Override
	public Mono<User> registerUser(User user) {
		
		Assert.notNull(user, "The user information cannot be null");
		Assert.isTrue(!StringUtils.hasText(user.getId()), "The ID must be null when registering a new user.");
		Assert.notNull(user.getLoginId(), "The user login ID is required");
		Assert.hasText(user.getPassword(), "The user password is required.");
		Assert.hasText(user.getEmail(), "The user email address is required.");
		Assert.hasText(user.getName(), "The user name is required.");
		Assert.hasText(user.getPhone(), "The user phone is required.");
		
		if (StringUtils.isEmpty(user.getAlias())) {
			//If no alias is provided, default it to the user's name
			user.setAlias(user.getName());
		}
		//Encode the user password (the default algorithm is Bcrypt)
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return
			userRepository.insert(user)
				.onErrorMap(
						//Map a duplicate key to a user-friendly message.
						e -> e instanceof DuplicateKeyException,
						e -> new ValidationException("There is already a user registered with that loginId or email.")
				)
				.map(u -> {
					u.setPassword(null);
					return u;
				});		
	}

	@Override
	public Mono<User> updateUserInformation(UserInformationUpdate user) {
		Assert.notNull(user, "User Information was not provided.");
		Assert.notNull(user.getLoginId(), "The user login ID is required");
		Assert.hasText(user.getEmail(), "The user email address is required.");
		Assert.hasText(user.getName(), "The user name is required.");
		Assert.hasText(user.getPhone(), "The user phone is required.");

		return userRepository
				.findByLoginId(user.getLoginId())
				.switchIfEmpty(Mono.error(new ValidationException("The user does not exist.")))
				.map(u -> {
					u.setEmail(user.getEmail());
					u.setName(user.getName());
					u.setPhone(user.getPhone());
					if (StringUtils.hasText(user.getAlias())) {
						u.setAlias(user.getAlias());
					} else {
						u.setAlias(user.getName());
					}
					return u;
				})
				.flatMap(userRepository::save)
				.map(u -> {
					u.setPassword(null);
					return u;
				});		
	}

	@Override
	public Mono<User> getUser(String loginId) {
		return userRepository.findByLoginId(loginId)
				.map(user -> {
					user.setPassword(null);
					return user;
				});
		}
	
	@Override
	public Flux<User> findUsers(UserCriteria criteria) {
		return userRepository.findAll()
			.map(user -> {
				user.setPassword(null);
				return user;
			});
	}

	@Override
	public Mono<Void> deleteUser(String loginId) {
		return userRepository
			.findByLoginId(loginId)
			.switchIfEmpty(Mono.error(new ValidationException("The user does not exist.")))
			.flatMap(userRepository::delete);		
	}

	@Override
	public Mono<Void> updateUserPassword(UserPasswordChangeRequest userPasswordChangeRequest) {
		return userRepository.findByLoginId(userPasswordChangeRequest.getLoginId())
		.switchIfEmpty(Mono.error(new SecurityException("Access Denied")))
	    .map(user -> {
			if (!passwordEncoder.matches(userPasswordChangeRequest.getUserChallenge(), user.getPassword())) {
				throw new ValidationException("Access denied");
			}
			user.setPassword(passwordEncoder.encode(userPasswordChangeRequest.getNewPassword()));
			return user;
	    })
		.flatMap(userRepository::save)
		.then(Mono.empty());		
	}

}
