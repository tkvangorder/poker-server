package org.homepoker.game.domain;

public class TournamentGame extends Game {
	
	private int blindIntervalMinutes = 15;
	private int secondsUntilNextBlind = 0;

	/**
	 * The level at which rebuys are no longer allows and when an add-on may be applied.
	 */
	private int cliffLevel = 3;

	/**
	 * Number of allowed rebuys for each player.
	 */
	private int numberOfRebuys = 0;
	
	/**
	 * The amount of chips given for a re-buy.
	 */
	private int rebuyChipAmount = 0;
	
	/**
	 * Does this game allow add-ons?
	 */
	private boolean addOnAllowed = false;

	/**
	 * The amount of chips given if a player elects to add-on.
	 */
	private int addOnChipAmount = 0;

	/**
	 * The blind schedule for the tournament. The schedule will not be defined until the tournament has started because it
	 * is calculated using the number of players and the amount of chips in play.
	 */
	private BlindSchedule blindSchedule;

	@Override
	public GameFormat getGameFormat() {
		return GameFormat.TOURNAMENT;
	}

	@Override
	public Blinds getCurrentBlinds() {
		return blindSchedule.getBlinds();
	}

	public int getBlindIntervalMinutes() {
		return blindIntervalMinutes;
	}

	public void setBlindIntervalMinutes(int blindIntervalMinutes) {
		this.blindIntervalMinutes = blindIntervalMinutes;
	}

	public int getSecondsUntilNextBlind() {
		return secondsUntilNextBlind;
	}

	public void setSecondsUntilNextBlind(int secondsUntilNextBlind) {
		this.secondsUntilNextBlind = secondsUntilNextBlind;
	}

	public int getCliffLevel() {
		return cliffLevel;
	}

	public void setCliffLevel(int cliffLevel) {
		this.cliffLevel = cliffLevel;
	}

	public int getNumberOfRebuys() {
		return numberOfRebuys;
	}

	public void setNumberOfRebuys(int numberOfRebuys) {
		this.numberOfRebuys = numberOfRebuys;
	}

	public int getRebuyChipAmount() {
		return rebuyChipAmount;
	}

	public void setRebuyChipAmount(int rebuyAmount) {
		this.rebuyChipAmount = rebuyAmount;
	}

	public boolean isAddOnAllowed() {
		return addOnAllowed;
	}

	public void setAddOnAllowed(boolean addOnAllowed) {
		this.addOnAllowed = addOnAllowed;
	}

	public int getAddOnChipAmount() {
		return addOnChipAmount;
	}

	public void setAddOnChipAmount(int addOnAmount) {
		this.addOnChipAmount = addOnAmount;
	}

	public BlindSchedule getBlindSchedule() {
		return blindSchedule;
	}

	public void setBlindSchedule(BlindSchedule blindSchedule) {
		this.blindSchedule = blindSchedule;
	}

}
