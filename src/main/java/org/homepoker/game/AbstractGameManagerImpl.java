package org.homepoker.game;

import java.util.List;
import java.util.Optional;

import org.homepoker.common.Command;
import org.homepoker.domain.game.Game;
import org.homepoker.domain.game.Player;
import org.homepoker.domain.user.User;

import reactor.core.publisher.Mono;

public abstract class AbstractGameManagerImpl implements GameManager {

	@Override
	public Player registerPlayer(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Player> getRegisteredPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<UserGameListener> getUserGameListener(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Void> addGameListener(GameListener listener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Void> removeGameListener(GameListener listener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Void> submitUserCommand(User user, Command command) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Game startGame() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Game pauseGame() {
		// TODO Auto-generated method stub
		return null;
	}

	protected abstract Game getGame();
}
