package org.homepoker.rsocket;

import org.homepoker.common.Event;
import org.homepoker.common.InvalidGameException;
import org.homepoker.domain.game.GameCriteria;
import org.homepoker.domain.game.GameDetails;
import org.homepoker.domain.user.User;
import org.homepoker.domain.user.UserCriteria;
import org.homepoker.domain.user.UserInformationUpdate;
import org.homepoker.domain.user.UserPasswordChangeRequest;
import org.homepoker.game.GameManager;
import org.homepoker.game.GameServer;
import org.homepoker.user.UserManager;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class RSocketUserController {

	private final GameServer gameServer;
	private final UserManager userManager;
	
	public RSocketUserController(GameServer gameServer, UserManager userManager) {
		this.gameServer = gameServer;
		this.userManager = userManager;
	}

	@MessageMapping("user-register")
	Mono<User> registerUser(User user) {
		return userManager.registerUser(user);
	}

	@MessageMapping("user-get")
	Mono<User> getUser(String loginId) {
		return userManager.getUser(loginId);
	}

	@MessageMapping("user-find")
	//For now, disabling authorization to make dev/testings easier.
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	Flux<User> findUsers(UserCriteria criteria) {
		return userManager.findUsers(criteria);
	}
	
	@MessageMapping("user-update")
	Mono<User> updateUser(UserInformationUpdate update) {
		return userManager.updateUserInformation(update);
	}

	@MessageMapping("user-update-password")
	Mono<Void> updateUserPassword(UserPasswordChangeRequest passwordRequest) {
		return userManager.updateUserPassword(passwordRequest);
	}
	
	@MessageMapping("user-delete")
	Mono<Void> deleteUser(String loginId) {
		return userManager.deleteUser(loginId);
	}

	@MessageMapping("find-games")
	Flux<GameDetails> findGames(GameCriteria criteria) {
		return gameServer.findGames(criteria);
	}

	@MessageMapping("game-command")
	void gameCommand(GameCommand command) {
		//TODO resolve user via spring security principal
		User user = getUser();
		getGameManager(command.getGameId()).submitUserCommand(user, command.getCommand());		
	}
	
	@MessageMapping("game-connect")
	Flux<Event> gameConnect(final String gameId) {
		//TODO resolve user via spring security principal
		User user = getUser();
		//Create an RSocket game listener and register it with the game manager.
		RSocketGameListener listener = new RSocketGameListener(user);
		getGameManager(gameId).addGameListener(listener);
		return listener.getEventStream();
		
	}

	private GameManager getGameManager(String gameId) {
		GameManager manager = gameServer.getGameManger(gameId);
		if (manager == null) {
			throw new InvalidGameException("The game ID [" + gameId + "] does not exist.");
		}
		return manager;
	}
	private User getUser() {
		return User.builder()
				.id("1")
				.email("test@test.com")
				.alias("Fred")
				.name("Fred Jones")
				.phone("123 123 1234")
				.build();
	}
	
}
