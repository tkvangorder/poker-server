package org.homepoker.rsocket;

import org.homepoker.domain.game.GameCriteria;
import org.homepoker.domain.game.cash.CashGameDetails;
import org.homepoker.game.cash.CashGameServer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class RSocketCashGameAdminController {

	private final CashGameServer cashGameServer;
	
	public RSocketCashGameAdminController(CashGameServer gameServer) {
		this.cashGameServer = gameServer;
	}
	
	@MessageMapping("admin-find-cash-games")
	Flux<CashGameDetails> findGames(GameCriteria criteria) {
		return cashGameServer.findGames(criteria);
	}

	@MessageMapping("admin-create-cash-game")
	Mono<CashGameDetails> createCashGame(CashGameDetails configuration, @AuthenticationPrincipal UserDetails user) {
		log.info(user.getUsername());
		configuration.setOwnerLoginId(user.getUsername());
		return cashGameServer.createGame(configuration);
	}
	
	@MessageMapping("admin-update-cash-game")
	Mono<CashGameDetails> updateGame(CashGameDetails configuration) { 
		return cashGameServer.updateGame(configuration);		
	}

	@MessageMapping("admin-delete-cash-game")
	Mono<Void> deleteGame(String gameId) {
		return cashGameServer.deleteGame(gameId);
	}	
}
