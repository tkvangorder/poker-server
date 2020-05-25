package org.homepoker.game;

import org.homepoker.user.domain.User;

public interface UserGameListener extends GameListener {
	public User getUser();	
}
