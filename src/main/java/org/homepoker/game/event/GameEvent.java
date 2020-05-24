package org.homepoker.game.event;

import org.homepoker.common.Event;

/**
 * Interface for all game events.
 *
 * @author tyler.vangorder
 *
 */
public interface GameEvent extends Event {
	Integer getGameId();
}
