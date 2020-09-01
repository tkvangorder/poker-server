package org.homepoker.rsocket;

import org.homepoker.domain.game.GameDetails;
import org.homepoker.game.GameServer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Mono;

@Controller
public class RSocketAdminController {

	private final GameServer gameServer;
	
	public RSocketAdminController(GameServer gameServer) {
		this.gameServer = gameServer;
	}
	
	@MessageMapping("game-create")
	Mono<GameDetails> createGame(GameDetails configuration) {
		return gameServer.createGame(configuration);
	}

	@MessageMapping("game-update")
	Mono<GameDetails> updateGame(GameDetails configuration) { 
		return gameServer.updateGame(configuration);		
	}

	@MessageMapping("game-delete")
	Mono<Void> deleteGame(String gameId) {
		return gameServer.deleteGame(gameId);
	}	
}
