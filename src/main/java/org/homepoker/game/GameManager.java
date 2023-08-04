package org.homepoker.game;

import org.homepoker.command.Command;
import org.homepoker.user.User;

import java.util.Optional;

public interface GameManager {

  Optional<UserGameListener> getUserGameListener(User user);

  void addGameListener(GameListener listener);

  void removeGameListener(GameListener listener);

  void submitCommand(Command command);
}
