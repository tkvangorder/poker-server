package org.homepoker.rsocket;

import org.homepoker.common.Event;
import org.homepoker.common.InvalidGameException;
import org.homepoker.domain.game.GameCriteria;
import org.homepoker.domain.game.GameDetails;
import org.homepoker.domain.user.User;
import org.homepoker.game.GameManager;
import org.homepoker.game.GameServer;
import org.homepoker.user.UserManager;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class RSocketUserController {

	private final GameServer gameServer;
	private final UserManager userManager;
	
	public RSocketUserController(GameServer gameServer, UserManager userManager) {
		this.gameServer = gameServer;
		this.userManager = userManager;
	}

	@MessageMapping("register-user")
	Mono<User> registerUser(User user) {
		return userManager.registerUser(user);
	}

	@MessageMapping("delete-user")
	Mono<Void> deleteUser(String emailAddress) {
		return userManager.deleteUser(emailAddress);
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
