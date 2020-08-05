package org.homepoker.domain.game;

public class CashGame extends Game {

	private final Blinds blinds;
	
	public CashGame(Blinds blinds) {
		this.blinds = blinds;
	}

	@Override
	public GameFormat getGameFormat() {
		return GameFormat.CASH;
	}

	@Override
	public Blinds getCurrentBlinds() {
		return blinds;
	}
}
