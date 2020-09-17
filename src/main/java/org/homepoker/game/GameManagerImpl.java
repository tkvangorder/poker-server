package org.homepoker.game;

import java.util.List;
import java.util.Optional;

import org.homepoker.common.Command;
import org.homepoker.domain.game.Game;
import org.homepoker.domain.game.Player;
import org.homepoker.domain.game.Table;
import org.homepoker.domain.user.User;

public class GameManagerImpl implements GameManager {

	private final Game game;

	public GameManagerImpl(Game game) {
		this.game = game;
	}

	@Override
	public Game getGame() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Table> getTables() {
		// TODO Auto-generated method stub
		return null;
	}

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
	public void addGameListener(GameListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeGameListener(GameListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<UserGameListener> getUserGameListener(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void submitUserCommand(User user, Command command) {
		// TODO Auto-generated method stub
		
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
}
