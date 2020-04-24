package org.homepoker.game.domain;

import java.util.List;

import org.homepoker.game.GameUtilities;
import org.springframework.util.Assert;

import lombok.Data;

/**
 * A class to represent the state of a blind schedule. The blind interval  and blind level factor are immutable. The blind levels
 * can be automatically computed using the blind level factor, however, we store the blind levels to allow for customizations to be
 * made.
 *
 * @author tyler.vangorder
 */
@Data
public class BlindSchedule {
	private final int blindIntervalMinutes;
	private final int blindLevelFactor;
	private List<Blinds> blindLevels;
	private int currentBlindLevel = 0;

	public BlindSchedule(List<Blinds> blindLevels, int blindIntervalMinutes, int blindLevelFactor) {
		Assert.notNull(blindLevels, "You must provide the blind levels.");
		this.blindIntervalMinutes = blindIntervalMinutes;
		this.blindLevelFactor = blindLevelFactor;
		this.blindLevels = blindLevels;
	}

	public Blinds getBlinds() {
		if (currentBlindLevel < blindLevels.size()) {
			return blindLevels.get(currentBlindLevel);
		} else {
			//If the current blind level exceeds the pre-determined schedule, we fall back to calculating the level.
			//Note: computeBigBlindAtLevel is 1-based, that is why we add one.
			int bigBlind = GameUtilities.computeBigBlindAtLevel(currentBlindLevel + 1, blindLevelFactor);
			return new Blinds(bigBlind/2, bigBlind);
		}

	}
}
