package org.homepoker.game;

import java.util.List;
import java.util.Optional;

import org.homepoker.common.Command;
import org.homepoker.domain.game.Game;
import org.homepoker.domain.game.Player;
import org.homepoker.domain.game.Table;
import org.homepoker.domain.user.User;

public interface GameManager {

	Game getGame();
	List<Table> getTables();
	
	Player registerPlayer(User user);
	List<Player> getRegisteredPlayers();
	
	void addGameListener(GameListener listener);
	void removeGameListener(GameListener listener);	

	Optional<UserGameListener> getUserGameListener(User user);
	void submitUserCommand(User user, Command command);

	
	Game startGame();
	Game pauseGame();
}
