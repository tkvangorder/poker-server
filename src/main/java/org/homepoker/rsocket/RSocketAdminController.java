package org.homepoker.rsocket;

import org.homepoker.game.GameServer;
import org.homepoker.game.domain.Game;
import org.homepoker.game.domain.GameConfiguration;
import org.springframework.stereotype.Controller;

@Controller
public class RSocketAdminController {

	private final GameServer gameServer;
	
	public RSocketAdminController(GameServer gameServer) {
		this.gameServer = gameServer;
	}
	
	Game createGame(GameConfiguration configuration) {
		return gameServer.createGame(configuration);
	}

	Game updateGame(GameConfiguration configuration) { 
		return gameServer.updateGame(configuration);		
	}
	void deleteGame(String gameId) {
		gameServer.deleteGame(gameId);
	}	
}
