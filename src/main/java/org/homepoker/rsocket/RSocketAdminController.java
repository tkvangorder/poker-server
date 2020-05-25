package org.homepoker.rsocket;

import java.util.List;

import org.homepoker.game.GameManager;
import org.homepoker.game.GameServer;
import org.homepoker.game.domain.Game;
import org.homepoker.game.domain.GameCriteria;
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
	
	List<Game> findGames(GameCriteria criteria) {
		return gameServer.findGames(criteria);
		
	}
	GameManager getGameManger(String gameId) {
		return gameServer.getGameManger(gameId);
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
