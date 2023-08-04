package org.homepoker.game;

import org.homepoker.domain.user.User;

public interface UserGameListener extends GameListener {
  User getUser();
}
