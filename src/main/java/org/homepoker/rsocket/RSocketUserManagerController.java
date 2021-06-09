package org.homepoker.rsocket;

import org.homepoker.domain.user.User;
import org.homepoker.domain.user.UserCriteria;
import org.homepoker.domain.user.UserInformationUpdate;
import org.homepoker.domain.user.UserPasswordChangeRequest;
import org.homepoker.user.UserManager;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class RSocketUserManagerController {

				private final UserManager userManager;

				public RSocketUserManagerController(UserManager userManager) {
								this.userManager = userManager;
				}

				@MessageMapping(RSocketRoutes.ROUTE_USER_MANAGER_REGISTER_USER)
				Mono<User> registerUser(User user) {
								return userManager.registerUser(user);
				}

				@MessageMapping(RSocketRoutes.ROUTE_USER_MANAGER_GET_USER)
				Mono<User> getUser(String loginId) {
								return userManager.getUser(loginId);
				}

				@MessageMapping(RSocketRoutes.ROUTE_USER_MANAGER_FIND_USERS)
				//For now, disabling authorization to make dev/testings easier.
				//@PreAuthorize("hasRole('ROLE_ADMIN')")
				Flux<User> findUsers(UserCriteria criteria) {
								return userManager.findUsers(criteria);
				}

				@MessageMapping(RSocketRoutes.ROUTE_USER_MANAGER_UPDATE_USER)
				Mono<User> updateUser(UserInformationUpdate update) {
								return userManager.updateUserInformation(update);
				}

				@MessageMapping(RSocketRoutes.ROUTE_USER_MANAGER_UPDATE_PASSWORD)
				Mono<Void> updateUserPassword(UserPasswordChangeRequest passwordRequest) {
								return userManager.updateUserPassword(passwordRequest);
				}

				@MessageMapping(RSocketRoutes.ROUTE_USER_MANAGER_DELETE_USER)
				Mono<Void> deleteUser(String loginId) {
								return userManager.deleteUser(loginId);
				}
}
