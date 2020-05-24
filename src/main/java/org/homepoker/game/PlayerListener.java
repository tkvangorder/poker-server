package org.homepoker.game;

import org.homepoker.game.event.GameEvent;
import org.homepoker.user.domain.User;

public interface PlayerListener {

	User getUser();
	void playerEventPublished(GameEvent event);
}
