package org.homepoker.user;

import org.homepoker.domain.user.User;
import org.homepoker.domain.user.UserCriteria;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserManagerImpl implements UserManager {

	UserRepository userRepository;
	
	public UserManagerImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public Mono<User> createUser(User user) {
		return userRepository.insert(user);
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
	public Mono<Void> deleteUser(User user) {
		return userRepository.delete(user);
	}

}
