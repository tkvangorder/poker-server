package org.homepoker.rsocket;

import org.homepoker.common.Event;
import org.homepoker.common.InvalidGameException;
import org.homepoker.game.GameManager;
import org.homepoker.game.GameServer;
import org.homepoker.game.domain.Game;
import org.homepoker.game.domain.GameCriteria;
import org.homepoker.user.domain.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
public class RSocketUserController {

	private final GameServer gameServer;
	
	public RSocketUserController(GameServer gameServer) {
		this.gameServer = gameServer;
	}

	@MessageMapping("create-user")
	User createUser(User user) {
		return null;
	}

	@MessageMapping("find-games")
	Flux<Game> findGames(GameCriteria criteria) {
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
