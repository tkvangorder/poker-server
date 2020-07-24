package org.homepoker.game.domain;

import java.util.Date;

import org.homepoker.user.domain.User;

import lombok.Data;

/**
 * The game configuration is used to set the parameters for a given poker game.
 * 
 * @author tyler.vangorder
 */
@Data
public abstract class GameConfiguration {

	/**
	 * Unique Id of the game.
	 */
	private String id;

	/**
	 * A human readable name for the game.
	 */
	private String name;

	/**
	 * This is a simple enumeration for the format: CASH or TOURNAMENT 
	 * @return The game format.
	 */
	private GameFormat getGameFormat;

	/**
	 * What type of poker game? Texas Hold'em, Draw, etc.
	 */
	private GameType type;

	/**
	 * The scheduled/actual start time of the game.
	 */
	private Date startTime;

	/**
	 * The number of chips each player will start with.
	 */
	private int startingStack;
	
	/**
	 * User that created/owns the game.
	 */
	private User owner;

	/**
	 * If the game format is CASH, this is the small blind for the game.
	 */
	private int smallBlind;
	
	/**
	 * If the game format is CASH, this is the big blind for the game.
	 */
	private int bigBlind;

	// The rest of the attributes in this class are for defining parameter for a tournament.

	/**
	 * The time interval where the blinds go "up"
	 */
	private int blindIntervalMinutes;

	/**
	 * The estimated time that the tournament should end. This is used when calculating the blind schedule
	 * along with the number of players and chips in play.
	 */
	private int estimatedTournamentLengthHours;
	
	/**
	 * The level at which re-buys are no longer allows and when an add-on may be applied.
	 */
	private int cliffLevel = 3;

	/**
	 * Number of allowed rebuys for each player.
	 */
	private int numberOfRebuys = 0;
	
	/**
	 * The amount of chips given for a re-buy.
	 */
	private int rebuyAmount = 0;
	
	/**
	 * Does this game allow add-ons?
	 */
	private boolean addOnAllowed = false;

	/**
	 * The amount of chips given if a player elects to add-on.
	 */
	private int addOnAmount = 0;

}
