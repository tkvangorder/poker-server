package org.homepoker.security;

import org.homepoker.domain.user.User;
import org.homepoker.user.UserRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import reactor.core.publisher.Mono;

/**
 * An implementation of Spring Security's ReactiveUserDetailsService which is used
 * to validate a user during authentication.
 *  
 * @author tyler.vangorder
 */
public class ReactiveMongoUserDetailsService implements ReactiveUserDetailsService {

	private final UserRepository userRepository;
	private final SecuritySettings securitySettings;
	
	
	public ReactiveMongoUserDetailsService(UserRepository userRepository, SecuritySettings securitySettings) {
		this.userRepository = userRepository;
		this.securitySettings = securitySettings;
	}

	@Override
	public Mono<UserDetails> findByUsername(String userLogin) {
		return userRepository.findByLoginId(userLogin)
//			.switchIfEmpty(Mono.error(new ValidationException("Login Failed")))
			.flatMap(this::userToUserDetails);
	}

	private Mono<UserDetails> userToUserDetails(User user) {

		String[] roles = new String[] {"user"};
		if (securitySettings.adminUsers.contains(user.getLoginId())) {
			roles = new String[] {"USER","ADMIN"};
		}
		return Mono.just(
			org.springframework.security.core.userdetails.User.builder()
			.username(user.getLoginId())
			.password(user.getPassword())
			.roles(roles)
			.build());
	}
	
}
