package org.homepoker.rsocket;

import org.homepoker.game.GameServer;
import org.homepoker.game.domain.Game;
import org.homepoker.user.UserManager;
import org.springframework.stereotype.Controller;

@Controller
public class RSocketAdminController {

	private final GameServer gameServer;
	private final UserManager userManager;
	
	public RSocketAdminController(GameServer gameServer, UserManager userManager) {
		this.gameServer = gameServer;
		this.userManager = userManager;
	}
	
	Game createGame(Game game) {
		return gameServer.createGame(game);
	}

	Game updateGame(Game game) { 
		return gameServer.updateGame(game);		
	}
	void deleteGame(String gameId) {
		gameServer.deleteGame(gameId);
	}	
}
