package org.homepoker.game;

import java.time.Duration;

import org.homepoker.game.domain.BlindSchedule;

/**
 * This builder provides a fluent DSL for creating a blind schedule for a tournament based on number of factors. The idea is
 * to come up with a reasonable blind schedule based on how long the tournament will last and how many players are in the
 * tournament.
 *
 * @author tyler.vangorder
 *
 */
public class BlindScheduleBuilder {

	int numberOfPlayers = 9;
	int startingStack = 5000;
	Duration blindInterval = Duration.ofMinutes(20);
	Duration tournamentLength = Duration.ofHours(4);
	int addOnAmount = 0;
	int rebuyAmount = 0;

	public static BlindScheduleBuilder blindSchedule() {
		return new BlindScheduleBuilder();
	}

	private BlindScheduleBuilder() {
	}

	BlindScheduleBuilder numberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
		return this;
	}
	BlindScheduleBuilder startingStack(int startingStack) {
		this.startingStack = startingStack;
		return this;
	}
	BlindScheduleBuilder blindInternalMinutes(int minutes) {
		this.blindInterval = Duration.ofMinutes(20);
		return this;
	}
	BlindScheduleBuilder tournamentLengthHours(int hours) {
		this.tournamentLength = Duration.ofHours(hours);
		return this;
	}
	BlindScheduleBuilder addOnAmount(int addOnAmount) {
		this.addOnAmount = addOnAmount;
		return this;
	}
	BlindScheduleBuilder rebuyAmount(int rebuyAmount) {
		this.rebuyAmount = rebuyAmount;
		return this;
	}

	public BlindSchedule build() {

		int estimatedTotalChips = numberOfPlayers * startingStack;
		if (rebuyAmount > 0) {
			estimatedTotalChips = estimatedTotalChips + (int) (numberOfPlayers * .2 * rebuyAmount);
		}
		if (addOnAmount > 0) {
			estimatedTotalChips = estimatedTotalChips + (int) (numberOfPlayers * .7 * addOnAmount);
		}

		int numberofIntervals = (int)tournamentLength.dividedBy(blindInterval);
		int blindLevelFactor = GameUtilities.computeBlindLevelFactor(estimatedTotalChips, numberofIntervals);
		return new BlindSchedule((int)blindInterval.toMinutes(), blindLevelFactor);
	}

}
