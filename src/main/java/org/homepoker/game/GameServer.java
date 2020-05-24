package org.homepoker.game;

import java.util.List;

import org.homepoker.game.domain.Game;
import org.homepoker.game.domain.Player;
import org.homepoker.user.domain.User;

public interface GameServer {

	List<Game> findGames();
	Player registerPlayer(Integer gameId, User user);
	GameManager connectToGame(Client client, Integer gameId);


	//Admin:
	Game createGame(Game game);
	Game updateGame(Game game);
	void deleteGame(Integer gameId);

}
