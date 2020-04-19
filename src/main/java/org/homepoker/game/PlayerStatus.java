package org.homepoker.game;

/**
 * The current status of a player within the context of a game.
 *
 * @author tyler.vangorder
 */
public enum PlayerStatus {
	ACTIVE, //Active player
	AWAY,   //Active player but has stepped away from the table.
	OUT		//Player is out of the game (no chips)
}
