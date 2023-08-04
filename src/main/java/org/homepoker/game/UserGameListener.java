package org.homepoker.game;

import org.homepoker.user.User;

public interface UserGameListener extends GameListener {
  User getUser();
}
