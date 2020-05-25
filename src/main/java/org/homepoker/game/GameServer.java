package org.homepoker.game;

import java.util.List;

import org.homepoker.game.domain.Game;
import org.homepoker.game.domain.GameCriteria;

public interface GameServer {

	List<Game> findGames(GameCriteria criteria);
	GameManager getGameManger(String gameId);

	//Admin:
	Game createGame(Game game);
	Game updateGame(Game game);
	void deleteGame(String gameId);

}
