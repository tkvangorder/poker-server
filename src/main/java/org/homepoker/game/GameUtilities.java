package org.homepoker.game;

/**
 * Some useful utilities for managing a poker game.
 *
 * @author tyler.vangorder
 *
 */
public class GameUtilities {


	public static int computeBlindLevelFactor(int estimatedTotalChips, int numberOfBlindLevels) {
		int maxBigBlind = roundToNearestChipDenomination(estimatedTotalChips / 10);
		return (int) (maxBigBlind/(Math.pow(1.5, numberOfBlindLevels)));
	}

	public static int computeBigBlindAtLevel(int level, int blindLevelFactor) {
		int bigBlind = roundToNearestChipDenomination((int) (blindLevelFactor * Math.pow(1.5, level)));
		if (bigBlind < 50) {
			return 50;
		} else {
			return bigBlind;
		}
	}

	public static int roundToNearestChipDenomination(int numberOfChips) {
		if (numberOfChips < 37) {
			return 25;
		} else if (numberOfChips <= 300) {
			return Math.round((numberOfChips/25f * 25));
		} else if (numberOfChips < 1000) {
			return Math.round((numberOfChips/50f * 50));
		} else if (numberOfChips < 4000) {
			return Math.round((numberOfChips/100f * 100));
		} else if (numberOfChips < 10000) {
			return Math.round((numberOfChips/500f * 500));
		} else if (numberOfChips < 20000) {
			return Math.round((numberOfChips/1000f * 1000));
		} else {
			return Math.round((numberOfChips/5000f * 5000));
		}
	}
}
