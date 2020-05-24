package org.homepoker.rsocket;

import org.homepoker.common.CommandMessage;
import org.homepoker.common.Event;
import org.homepoker.game.ClientManager;
import org.homepoker.game.GameServer;
import org.homepoker.user.domain.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
public class RSocketUserController {

	private final ClientManager clientManager;
	private final GameServer gameServer;
	
	public RSocketUserController(ClientManager clientManager, GameServer gameServer) {
		this.clientManager = clientManager;
		this.gameServer = gameServer;
	}

	@MessageMapping("create-user")
	User createUser(User user) {
		return null;
	}
	
	@MessageMapping("client-channel")
	Flux<Event> clientChannel(final Flux<CommandMessage> commandStream) {
		//TODO resolve user via spring security principal
		User user = User.builder()
			.id("1")
			.email("test@test.com")
			.alias("Fred")
			.name("Fred Jones")
			.phone("123 123 1234")
			.build();
		
		RSocketClientConnection client = new RSocketClientConnection(user, commandStream, clientManager, gameServer);
		clientManager.registerClient(client);
		return client.getEventStream();
		
	}
	
}
