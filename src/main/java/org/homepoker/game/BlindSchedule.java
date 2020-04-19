package org.homepoker.game;

import java.util.List;

import lombok.Data;

/**
 * A class to represent the state of a blind schedule. The blind levels and interval are immutable and the current
 * blind level will be managed/set by the GameManager.
 * @author tyler.vangorder
 */
@Data
public class BlindSchedule {
	private final int blindIntervalMinutes;
	private final List<Blinds> blindLevels;
	private int currentBlindLevel = 0;
}
