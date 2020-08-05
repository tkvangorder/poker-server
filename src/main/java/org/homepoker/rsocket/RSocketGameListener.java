package org.homepoker.rsocket;

import org.homepoker.common.Event;
import org.homepoker.domain.user.User;
import org.homepoker.game.UserGameListener;
import org.homepoker.game.event.GameEvent;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

public class RSocketGameListener implements UserGameListener {

	private final User user;
	private final EmitterProcessor<Event> emitter;
	
	public RSocketGameListener(User user) {
		this.user = user;
		this.emitter = EmitterProcessor.<Event>create();
	}

	@Override
	public User getUser() {
		return user;
	}
	
	@Override
	public void gameEventPublished(GameEvent event) {
		emitter.onNext(event);
	}
	
	Flux<Event> getEventStream() {
		return emitter.log();
	}

}
