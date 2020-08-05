package org.homepoker.domain.game;

import java.util.Date;

import org.homepoker.domain.user.User;

import lombok.Builder;
import lombok.Data;

/**
 * The game configuration is used to set the parameters for a given poker game.
 * 
 * @author tyler.vangorder
 */
@Data
@Builder
public class GameDetails {

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
	private GameFormat gameFormat;

	/**
	 * What type of poker game? Texas Hold'em, Draw, etc.
	 */
	private GameType type;

	/**
	 * The scheduled/actual start time of the game.
	 */
	private Date startTimestamp;

	/**
	 * The number of chips each player will start with.
	 */
	private Integer startingChipStack;
	
	/**
	 * User that created/owns the game.
	 */
	private User owner;

	/**
	 * If the game format is CASH, this is the small blind for the game.
	 */
	private Integer smallBlind;
	
	/**
	 * If the game format is CASH, this is the big blind for the game.
	 */
	private Integer bigBlind;

	// The rest of the attributes in this class are for defining parameter for a tournament.

	/**
	 * The time interval where the blinds go "up"
	 */
	private Integer blindIntervalMinutes;

	/**
	 * The estimated time that the tournament should end. This is used when calculating the blind schedule
	 * along with the number of players and chips in play.
	 */
	private Integer estimatedTournamentLengthHours;
	
	/**
	 * The level at which re-buys are no longer allows and when an add-on may be applied.
	 */
	private Integer cliffLevel;

	/**
	 * Number of allowed rebuys for each player.
	 */
	private Integer numberOfRebuys;
	
	/**
	 * The amount of chips given for a re-buy.
	 */
	private Integer rebuyChipAmount;
	
	/**
	 * Does this game allow add-ons?
	 */
	private boolean addOnAllowed;

	/**
	 * The amount of chips given if a player elects to add-on.
	 */
	private Integer addOnChipAmount;

	public static class GameDetailsBuilder {
		public GameDetailsBuilder game(Game game) {
			this.id = game.getId();
			this.name = game.getName();

			this.gameFormat = game.getGameFormat();
			this.type = game.getType();
			this.startTimestamp = game.getStartTimestamp();
			this.startingChipStack = game.getStartingStack();
			this.owner = game.getOwner();
			if (game.getCurrentBlinds() != null) {
				this.smallBlind = game.getCurrentBlinds().getSmallBlind();
				this.bigBlind = game.getCurrentBlinds().getBigBlind();
			}

			// The rest of the attributes in this class are for defining parameter for a tournament.
			if (game instanceof TournamentGame) {
				TournamentGame tournamentGame = (TournamentGame)game; 
				this.blindIntervalMinutes = tournamentGame.getBlindIntervalMinutes();
				this.estimatedTournamentLengthHours = tournamentGame.getEstimatedTournamentLengthHours();
				this.cliffLevel = tournamentGame.getCliffLevel();
				this.numberOfRebuys = tournamentGame.getNumberOfRebuys();
				this.rebuyChipAmount = tournamentGame.getRebuyChipAmount();
				this.addOnAllowed = tournamentGame.isAddOnAllowed();
				this.addOnChipAmount = tournamentGame.getAddOnChipAmount();
			}
			return this;
		}
	}
}
