package org.homepoker.game;

import java.util.List;
import java.util.Optional;

import org.homepoker.common.Command;
import org.homepoker.game.domain.Game;
import org.homepoker.game.domain.Player;
import org.homepoker.game.domain.Table;
import org.homepoker.user.domain.User;

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
