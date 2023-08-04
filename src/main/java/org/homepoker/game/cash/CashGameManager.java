package org.homepoker.game.cash;

import org.homepoker.game.Game;
import org.homepoker.game.AbstractGameManagerImpl;

public class CashGameManager extends AbstractGameManagerImpl {

  private final CashGame game;

  public CashGameManager(CashGame game) {
    this.game = game;
  }

  @Override
  protected Game getGame() {
    return game;
  }
}
