package org.homepoker.rsocket;

import org.homepoker.game.GameServer;
import org.homepoker.game.domain.Game;
import org.homepoker.game.domain.GameConfiguration;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Mono;

@Controller
public class RSocketAdminController {

	private final GameServer gameServer;
	
	public RSocketAdminController(GameServer gameServer) {
		this.gameServer = gameServer;
	}
	
	Mono<Game> createGame(GameConfiguration configuration) {
		return gameServer.createGame(configuration);
	}

	Mono<Game> updateGame(GameConfiguration configuration) { 
		return gameServer.updateGame(configuration);		
	}
	Mono<Void> deleteGame(String gameId) {
		return gameServer.deleteGame(gameId);
	}	
}
