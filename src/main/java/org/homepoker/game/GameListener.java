package org.homepoker.game;

import org.homepoker.game.event.GameEvent;

public interface GameListener {
  void gameEventPublished(GameEvent event);
}
