package org.homepoker.game;

import org.homepoker.user.User;

/**
 * This class represents the state of a player in the game and is always linked with a user.
 * @author tyler.vangorder
 */
public class Player {

	String name;
	PlayerStatus status;
	Integer chipCount;
	User user;
}
