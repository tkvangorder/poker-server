package org.homepoker.security;

import java.util.Set;

import org.homepoker.domain.user.User;
import org.homepoker.user.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

				private static final Set<GrantedAuthority> adminAuthorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"));
				private static final Set<GrantedAuthority> userAuthorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));

				private final UserRepository userRepository;
				private final SecuritySettings securitySettings;


				public ReactiveMongoUserDetailsService(UserRepository userRepository, SecuritySettings securitySettings) {
								this.userRepository = userRepository;
								this.securitySettings = securitySettings;
				}

				@Override
				public Mono<UserDetails> findByUsername(String userLogin) {
								return userRepository.findByLoginId(userLogin)
										.flatMap(this::userToUserDetails);
				}

				private Mono<PokerUserDetails> userToUserDetails(User user) {

								Set<GrantedAuthority> roles = userAuthorities;
								if (securitySettings.adminUsers.contains(user.getLoginId())) {
												roles = adminAuthorities;
								}
								return Mono.just(new PokerUserDetails(user, roles));
				}
}
