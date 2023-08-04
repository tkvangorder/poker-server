package org.homepoker.game;

import org.homepoker.common.Command;
import org.homepoker.domain.user.User;

import java.util.Optional;

public interface GameManager {

  Optional<UserGameListener> getUserGameListener(User user);

  void addGameListener(GameListener listener);

  void removeGameListener(GameListener listener);

  void submitCommand(Command command);
}
