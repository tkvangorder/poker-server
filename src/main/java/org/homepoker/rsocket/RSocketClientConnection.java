package org.homepoker.rsocket;

import java.util.Map;

import org.homepoker.common.CommandMessage;
import org.homepoker.common.Event;
import org.homepoker.game.Client;
import org.homepoker.game.ClientManager;
import org.homepoker.game.GameManager;
import org.homepoker.game.GameServer;
import org.homepoker.game.event.GameEvent;
import org.homepoker.user.domain.User;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

public class RSocketClientConnection implements Client {

	private final User user;
	private final ClientManager clientManager;
	private final GameServer gameServer;
	private final EmitterProcessor<Event> emitter;
	private Map<Integer, GameManager> gameManagers;
	
	public RSocketClientConnection(User user, Flux<CommandMessage> commandStream, ClientManager clientManager, GameServer gameServer) {
		this.user = user;
		commandStream.log().subscribe(this::processCommand);
		this.emitter = EmitterProcessor.<Event>create();
		this.clientManager = clientManager;
		this.gameServer = gameServer;
	}
	
	@Override
	public void gameEventPublished(GameEvent event) {
		emitter.onNext(event);
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void playerEventPublished(GameEvent event) {
		emitter.onNext(event);
	}
	
	Flux<Event> getEventStream() {
		return emitter.log();
	}
	
	private void processCommand(CommandMessage command) {
		
		
	}
}
