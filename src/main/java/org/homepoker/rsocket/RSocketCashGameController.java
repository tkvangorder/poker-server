package org.homepoker.rsocket;

import org.homepoker.common.InvalidGameException;
import org.homepoker.domain.game.GameCriteria;
import org.homepoker.domain.game.cash.CashGameDetails;
import org.homepoker.game.GameManager;
import org.homepoker.game.cash.CashGameServer;
import org.homepoker.game.event.GameEvent;
import org.homepoker.security.PokerUserDetails;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Controller
public class RSocketCashGameController {

	private final CashGameServer gameServer;
	
	public RSocketCashGameController(CashGameServer gameServer) {
		this.gameServer = gameServer;
	}
	
	@MessageMapping(RSocketRoutes.ROUTE_CASH_CREATE_GAME)
	Mono<CashGameDetails> createCashGame(CashGameDetails configuration, @AuthenticationPrincipal PokerUserDetails user) {
		configuration.setOwnerLoginId(user.getUsername());
		return gameServer.createGame(configuration);
	}
	
	@MessageMapping(RSocketRoutes.ROUTE_CASH_UPDATE_GAME)
	Mono<CashGameDetails> updateGame(CashGameDetails configuration) { 
		return gameServer.updateGame(configuration);		
	}

	@MessageMapping(RSocketRoutes.ROUTE_CASH_DELETE_GAME)
	Mono<Void> deleteGame(String gameId) {
		return gameServer.deleteGame(gameId);
	}	

	@MessageMapping(RSocketRoutes.ROUTE_CASH_FIND_GAMES)
	Flux<CashGameDetails> findGames(GameCriteria criteria) {
		return gameServer.findGames(criteria);
	}
	
	@MessageMapping(RSocketRoutes.ROUTE_CASH_REGISTER_FOR_GAME)
	Mono<Void> registerForGame(String gameId, @AuthenticationPrincipal PokerUserDetails user) {
		return Mono.fromCallable(() -> getGameManager(gameId))
			.subscribeOn(Schedulers.elastic())
			.doOnSuccess(gm -> gm.registerPlayer(user))
			.then();
	}

	@MessageMapping(RSocketRoutes.ROUTE_CASH_JOIN_GAME)
	Flux<GameEvent> joinGame(final String gameId, @AuthenticationPrincipal PokerUserDetails user) {
		//Create an RSocket game listener and register it with the game manager.
		RSocketGameListener listener = new RSocketGameListener(user);
		return Mono.fromCallable(() -> getGameManager(gameId))
			.subscribeOn(Schedulers.elastic())
			.flatMapMany(gm -> {
				gm.addGameListener(listener);
				return listener.getEventStream();
			});
	}
	
	@MessageMapping(RSocketRoutes.ROUTE_CASH_GAME_COMMAND)
	Mono<Void> gameCommand(GameCommand command, @AuthenticationPrincipal PokerUserDetails user) {
		return Mono.fromCallable(() -> getGameManager(command.getGameId()))
				.subscribeOn(Schedulers.elastic())
				.doOnSuccess(gm -> gm.submitUserCommand(user, command.getCommand()))
				.then();

	}
	
	private GameManager getGameManager(String gameId) {
		GameManager manager = gameServer.getGameManger(gameId);
		if (manager == null) {
			throw new InvalidGameException("The game ID [" + gameId + "] does not exist.");
		}
		return manager;
	}	
}
