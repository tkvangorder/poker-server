package org.homepoker.game;

import java.util.List;

import org.homepoker.game.domain.Game;
import org.homepoker.game.domain.Player;
import org.homepoker.game.domain.Table;
import org.homepoker.user.domain.User;

public interface GameManager {

	Game getGame();
	Player registerPlayer(User user);
	List<Player> getRegisteredPlayers();
	List<Player> getLobbyPlayers();
	void connect(Client user);
	List<Table> getTables();
	Game startGame();
	Game pauseGame();


}
