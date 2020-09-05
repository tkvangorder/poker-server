package org.homepoker.domain.game;

import java.util.List;
import java.util.Map;

import org.homepoker.domain.user.User;

/**
 * An interface to define common properties and operations regardless of the game format : CASH/TOURNAMENT
 *
 * @author tyler.vangorder
 *
 */
public interface Game {

	/**
	 * Unique Id of the game.
	 */
	public String getId();
		
	/**
	 * A human readable name for the game.
	 */
	public String getName();

	/**
	 * This is a simple enumeration for the format: CASH or TOURNAMENT 
	 * @return The game format.
	 */
	public GameFormat getGameFormat();

	/**
	 * What type of poker game? Texas Hold'em, Draw, etc.
	 */
	public GameType getGameType();
	
	/**
	 * Current status of the game (useful when persisting the game to storage)
	 */
	public GameStatus getStatus();

	/**
	 * User that created/owns the game.
	 */
	public User getOwner();

	/**
	 * The players registered/participating in the game.
	 */	
	public Map<String,Player> getPlayers();

	/**
	 * A game may have multiple tables depending on how many players are registered/participating in the game.
	 * Each table can hold up to nine players and as players come and go, the players may be moved to different tables.
	 */
	public List<Table> getTables();

	

}
