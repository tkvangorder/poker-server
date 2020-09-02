package org.homepoker.domain.game;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * The game configuration is used to set the parameters for a given poker game.
 * 
 * @author tyler.vangorder
 */
@Data
@Builder
public class CashGameDetails {

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
	 * The number of chips a player receives for the buy-in amount. 
	 */
	private Integer buyInChips;
	
	/**
	 * The buy-in amount in dollars
	 */
	private BigDecimal buyInAmount;

	/**
	 * The user's loginId that created/owns the game.
	 */
	private String ownerLoginId;

	/**
	 * The number of chips for the small blind.
	 */
	private Integer smallBlind;
	
	/**
	 * The number of chips for the big blind (typically 2Xsmall blind)
	 */
	private Integer bigBlind;
}
