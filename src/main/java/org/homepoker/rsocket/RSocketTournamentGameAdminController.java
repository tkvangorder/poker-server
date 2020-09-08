package org.homepoker.rsocket;

import org.homepoker.domain.game.GameCriteria;
import org.homepoker.domain.game.tournament.TournamentGameDetails;
import org.homepoker.game.tournament.TournamentGameServer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class RSocketTournamentGameAdminController {

	private final TournamentGameServer tournamentGameServer;
	
	public RSocketTournamentGameAdminController(TournamentGameServer gameServer) {
		this.tournamentGameServer = gameServer;
	}
	
	@MessageMapping("admin-find-tournament-games")
	Flux<TournamentGameDetails> findGames(GameCriteria criteria) {
		return tournamentGameServer.findGames(criteria);
	}

	@MessageMapping("admin-create-tournament-game")
	Mono<TournamentGameDetails> createGame(TournamentGameDetails configuration, @AuthenticationPrincipal UserDetails user) {
		configuration.setOwnerLoginId(user.getUsername());
		return tournamentGameServer.createGame(configuration);
	}
	
	@MessageMapping("admin-update-tournament-game")
	Mono<TournamentGameDetails> updateGame(TournamentGameDetails configuration) { 
		return tournamentGameServer.updateGame(configuration);		
	}

	@MessageMapping("admin-delete-tournament-game")
	Mono<Void> deleteGame(String gameId) {
		return tournamentGameServer.deleteGame(gameId);
	}	
}
