package org.homepoker.game;

import java.util.List;
import java.util.Optional;

import org.homepoker.common.Command;
import org.homepoker.domain.game.Player;
import org.homepoker.domain.user.User;

public interface GameManager {

				Optional<UserGameListener> getUserGameListener(User user);

				void addGameListener(GameListener listener);

				void removeGameListener(GameListener listener);

				void submitCommand(Command command);

}
