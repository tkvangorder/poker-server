package org.homepoker.game;

import org.homepoker.game.event.GameEvent;

public interface GameListener {

	public void gameEventPublished(GameEvent event);

}
