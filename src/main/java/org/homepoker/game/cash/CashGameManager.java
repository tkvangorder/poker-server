package org.homepoker.game.cash;

import org.homepoker.domain.game.Game;
import org.homepoker.domain.game.cash.CashGame;
import org.homepoker.game.AbstractGameManagerImpl;

public class CashGameManager extends AbstractGameManagerImpl {

	private CashGame game;

	public CashGameManager(CashGame game) {
		this.game = game;
	}

	@Override
	protected Game getGame() {
		return game;
	}
	
}
