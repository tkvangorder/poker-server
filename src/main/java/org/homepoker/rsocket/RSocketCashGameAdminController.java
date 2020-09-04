package org.homepoker.rsocket;

import org.homepoker.domain.game.cash.CashGameDetails;
import org.homepoker.game.cash.CashGameServer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Mono;

@Controller
public class RSocketCashGameAdminController {

	private final CashGameServer gameServer;
	
	public RSocketCashGameAdminController(CashGameServer gameServer) {
		this.gameServer = gameServer;
	}
	
	@MessageMapping("admin-create-cash-game")
	Mono<CashGameDetails> createCashGame(CashGameDetails configuration) {
		return gameServer.createGame(configuration);
	}
	
	@MessageMapping("admin-update-cash-game")
	Mono<CashGameDetails> updateGame(CashGameDetails configuration) { 
		return gameServer.updateGame(configuration);		
	}

	@MessageMapping("admin-delete-cash-game")
	Mono<Void> deleteGame(String gameId) {
		return gameServer.deleteGame(gameId);
	}	
}
