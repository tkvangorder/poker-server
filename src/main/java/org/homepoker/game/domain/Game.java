package org.homepoker.game.domain;

import java.util.Date;
import java.util.List;

import org.homepoker.user.domain.User;

import lombok.Data;

/**
 * This represents the current/persistent state of a poker game. A game is a scheduled (or adhoc) event that defines the format
 * (cash or tournament) and the rules for the game.
 *
 *
 * @author tyler.vangorder
 *
 */
@Data
public abstract class Game {

	/**
	 * Unique Id of the game.
	 */
	private String id;

	/**
	 * A human readable name for the game.
	 */
	private String name;

	/**
	 * What type of poker game? Texas Hold'em, Draw, etc.
	 */
	private GameType type;

	/**
	 * The scheduled/actual start time of the game.
	 */
	private Date startTimestamp;

	/**
	 * The end time is used only when persisting a game once it is over or has been ended
	 * by the game manager.
	 */
	private Date endTime;

	/**
	 * The number of chips each player will start with.
	 */
	private int startingStack;
	
	/**
	 * Current status of the game (useful when persisting the game to storage)
	 */
	private GameStatus status;

	/**
	 * User that created/owns the game.
	 */
	private User owner;

	/**
	 * The players registered/participating in the game.
	 */
	private List<Player> registeredPlayers;

	/**
	 * A game may have multiple tables depending on how many players are registered/participating in the game.
	 * Each table can hold up to nine players and as players come and go, the players may be moved to different tables.
	 */
	private List<Table> tables;

	/**
	 * This abstract method must be implemented by concrete implementations. A cash game will have a static blind schedule
	 * and a tournament's blinds will change based on the blind schedule.
	 * 
	 * @return The current small/big blinds
	 */
	public abstract Blinds getCurrentBlinds();
	
	/**
	 * This is a simple enumeration for the format: CASH or TOURNAMENT 
	 * @return The game format.
	 */
	public abstract GameFormat getGameFormat();

}
