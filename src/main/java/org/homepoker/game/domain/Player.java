package org.homepoker.game.domain;

import org.homepoker.user.User;

import lombok.Data;

/**
 * This class represents the state of a player in the game and is always linked with a user.
 * @author tyler.vangorder
 */
@Data
public class Player {

	/**
	 * The Id of the player which is unique within the context of a game.
	 */
	final Integer id;

	/**
	 * The user linked to this player.
	 */
	final User user;

	/**
	 * The player has been confirmed by the game manager and can be assigned a seat at table.
	 */
	boolean confirmed;

	/**
	 * The player status to indicate if the player is active, away, or out.
	 */
	PlayerStatus status;

	/**
	 * The player's current chip count
	 */
	Integer chipCount;
}
