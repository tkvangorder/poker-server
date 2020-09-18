package org.homepoker.game;

import java.util.List;
import java.util.Optional;

import org.homepoker.common.Command;
import org.homepoker.domain.game.Game;
import org.homepoker.domain.game.Player;
import org.homepoker.domain.user.User;

import reactor.core.publisher.Mono;

public interface GameManager {
	
	Player registerPlayer(User user);
	List<Player> getRegisteredPlayers();
	Optional<UserGameListener> getUserGameListener(User user);
	
	Mono<Void> addGameListener(GameListener listener);
	Mono<Void> removeGameListener(GameListener listener);	
	Mono<Void> submitUserCommand(User user, Command command);
	
	Game startGame();
	Game pauseGame();
	
}
