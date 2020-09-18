package org.homepoker.game.cash;

import org.homepoker.domain.game.cash.CashGame;
import org.homepoker.game.AbstractGameManagerImpl;

public class CashGameManager extends AbstractGameManagerImpl {

	private CashGame game;

	public CashGameManager(CashGame game) {
		super(game);
		this.game = game;
	}
	
}
