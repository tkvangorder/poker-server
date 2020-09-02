package org.homepoker.game.cash;

import org.homepoker.domain.game.Blinds;
import org.homepoker.domain.game.CashGame;
import org.homepoker.domain.game.CashGameDetails;
import org.homepoker.domain.game.Game;
import org.homepoker.domain.game.GameCriteria;
import org.homepoker.game.GameManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CashGameServerImpl implements CashGameServer {

	CashGameRepository gameRepository;
	
	@Override
	public Flux<CashGameDetails> findGames(GameCriteria criteria) {
		return gameRepository.findAll().map(CashGameServerImpl::gameToGameDetails);
	}

	@Override
	public GameManager getGameManger(String gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<CashGameDetails> createGame(CashGameDetails gameDetails) {
		Assert.notNull(gameDetails, "The game configuration is required.");
		Assert.notNull(gameDetails.getName(), "The name is required when creating a game.");
		Assert.notNull(gameDetails.getType(), "The game type is required when creating a game.");
		Assert.notNull(gameDetails.getStartTimestamp(), "The start date/time is required when creating a game.");
		Assert.notNull(gameDetails.getBuyInChips(), "The buy-in chip stack size is required when creating a game.");
		Assert.notNull(gameDetails.getBuyInAmount(), "The buy-in amount is required when creating a game.");		
		Assert.notNull(gameDetails.getOwnerLoginId(), "The game owner is required when creating a game.");
		Assert.notNull(gameDetails.getSmallBlind(), "The small blind must be defined for a cash game.");
		Assert.notNull(gameDetails.getBigBlind(), "The big blind must be defined for a cash game.");
		CashGame game = new CashGame(new Blinds(gameDetails.getSmallBlind(), gameDetails.getSmallBlind()));
		game.setName(gameDetails.getName());
		game.setType(gameDetails.getType());
		game.setStartTimestamp(gameDetails.getStartTimestamp());
//		game.setStartingStack(gameDetails.getStartingChipStack());
//		game.setOwner(gameDetails.getOwner());
		return gameRepository
				.save(game)
				.map(CashGameServerImpl::gameToGameDetails);			
	}

	@Override
	public Mono<CashGameDetails> updateGame(final CashGameDetails configuration) {
		Mono<CashGame> game = gameRepository.findById(configuration.getId());
		return game
			.doOnNext(gameRepository::save)
			.map(CashGameServerImpl::gameToGameDetails);
	}

	@Override
	public Mono<Void> deleteGame(String gameId) {
		return gameRepository.deleteById(gameId);
	}
	
	private static CashGameDetails gameToGameDetails(Game game) {
		return CashGameDetails.builder()
			//.game(game)
			.build();
	}
}