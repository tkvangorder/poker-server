package org.homepoker.game.tournament;

/**
 * Some useful utilities for managing a poker game.
 *
 * @author tyler.vangorder
 *
 */
public class TournamentUtilities {

	/**
	 * This method takes an estimate of the total chips in play for a tournament and the desired number of blind levels and calculates a constant
	 * called "blind level factor" (BLF) that can then be used to compute the big blind of any blind level.
	 *
	 * A good formula for calculating a blind schedule is Big Blind = BLF * (1.5 ^ blind level).
	 *
	 * The number of blind levels is desired time for a tournament to run (example 4 hours) / divided by the blind interface (example 20 minutes) == 12 intervals.
	 *
	 * A tournament will generally complete quickly once the big blind reaches 1/10 of the total chips available.
	 *
	 * The BLF can be computed by computing by:
	 * 1) Calculate the big blind (for the last interval) estimatedChips /10
	 * 2) Divide the big blind by (1.5 ^ number of blind levels)
	 *
	 * @param estimatedTotalChips
	 * @param numberOfBlindLevels
	 * @return The Blind Level Factor (BLF) is a constant that can be used to compute blinds at any blind leve.
	 */
	public static int computeBlindLevelFactor(int estimatedTotalChips, int numberOfBlindLevels) {
		int maxBigBlind = roundToBigBlind(estimatedTotalChips / 10);
		return (int) (maxBigBlind/(Math.pow(1.5, numberOfBlindLevels)));
	}

	/**
	 * This method takes an a current blind level and the blind level factor (BLF) and returns what the big blind should be.
	 *
	 * The formula for computing the big blind is Big Blind = BLF * (1.5 ^ blind level).
	 *
	 * After a value is calculated, it will be rounded to the closest chip denomination.
	 *
	 * @param level The blind level
	 * @param blindLevelFactor (a constant that can be computed using the number of chips in the game and the total number of blind intervals. See {@link TournamentUtilities#computeBlindLevelFactor(int, int)}
	 * @return The value of the big blind.
	 */
	public static int computeBigBlindAtLevel(int level, int blindLevelFactor) {
		if (level == 0) {
			//initial big blind is always 50.
			return 50;
		} else if (level == 1) {
			return 100;
		} else if (level == 2) {
			return 200;
		}

		int bigBlind = roundToBigBlind((int) (blindLevelFactor * Math.pow(1.5, level)));

		while (bigBlind < 200) {
			level++;
			bigBlind = roundToBigBlind((int) (blindLevelFactor * Math.pow(1.5, level)));
		}

		return bigBlind;
	}

	/**
	 * This method will round an integer representing a number of chips to the nearest chip denomination.
	 * This is used when calculating blinds to a "nice" value.
	 *
	 * Each denomination will be phased out as the number increases in value.
	 *
	 * The chip denominations rounded to along with the ceiling in which that denomination is phased out to the next level.
	 *
	 * <PRE>
	 * Denomination   Phased Out
	 * ------------   ----------
	 *           50 |      500
	 *          100 |    1,000
	 *          500 |    5,000
	 *         1000 |   10,000
	 *         5000 |   50,000
	 *        10000 |  100,000
	 *        25000 | >100,000
	 * </PRE>
	 *
	 * We could add more, but I think for most "small" games, these should be fine.
	 *
	 * @param numberOfChips
	 * @return The closest denomination depending where the numberOfChips falls within the phase-out table.
	 */
	public static int roundToBigBlind(int numberOfChips) {
		if (numberOfChips < 25) {
			return 50;
		}
		if (numberOfChips < 500) {
			return Math.round(numberOfChips/50f) * 50;
		} else if (numberOfChips < 2000) {
			return Math.round(numberOfChips/100f) * 100;
		} else if (numberOfChips < 5000) {
			return Math.round(numberOfChips/500f) * 500;
		} else if (numberOfChips < 20_000) {
			return Math.round(numberOfChips/1000f) * 1000;
		} else if (numberOfChips < 50_000) {
			return Math.round(numberOfChips/5000f) * 5000;
		} else if (numberOfChips < 200_000) {
			return Math.round(numberOfChips/10_000f) * 10_000;
		} else {
			return Math.round(numberOfChips/25_000f) * 25_000;
		}
	}
}
