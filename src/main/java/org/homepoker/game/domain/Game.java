package org.homepoker.game.domain;

import java.util.Date;
import java.util.List;

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
public class Game {

	/**
	 * Unique Id of the game.
	 */
	private String id;

	/**
	 * A human readable name for the game.
	 */
	private String name;

	/**
	 * The game format can be either cash or tournament.
	 */
	private GameFormat format;

	/**
	 * What type of poker game? Texas Hold'em, Draw, etc.
	 */
	private GameType type;

	/**
	 * Current status of the game (useful when persisting the game to storage)
	 */
	private GameStatus status;

	//NOTE: We are avoiding polymorphism here. The current blind level is always recorded with the game.
	/**
	 * The current blinds for the game
	 */
	private Blinds blinds;

	/**
	 * The blind schedule when the format of the game is {@link GameFormat#TOURNAMENT}
	 */
	private BlindSchedule blindSchedule;

	/**
	 * The scheduled/actual start time of the game.
	 */
	private Date startTime;

	/**
	 * The end time is used only when persisting a game once it is over or has been ended
	 * by the game manager.
	 */
	private Date endTime;

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
	 * Users/players
	 */
	private List<Player> lobby;

}
