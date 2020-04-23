package org.homepoker.game.domain;

import org.homepoker.game.GameUtilities;

import lombok.Data;

/**
 * A class to represent the state of a blind schedule. The blind levels and interval are immutable and the current
 * blind level will be managed/set by the GameManager.
 * @author tyler.vangorder
 */
@Data
public class BlindSchedule {
	private final int blindIntervalMinutes;
	private final int blindLevelFactor;
	private int currentBlindLevel = 0;

	public Blinds getBlinds() {
		int bigBlind = GameUtilities.computeBigBlindAtLevel(currentBlindLevel, blindLevelFactor);
		return new Blinds(bigBlind/2, bigBlind);
	}
}
