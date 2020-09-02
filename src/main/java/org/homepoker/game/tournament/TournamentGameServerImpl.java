package org.homepoker.game.tournament;

import org.homepoker.domain.game.Game;
import org.homepoker.domain.game.GameCriteria;
import org.homepoker.domain.game.TournamentGame;
import org.homepoker.domain.game.TournamentGameDetails;
import org.homepoker.game.GameManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TournamentGameServerImpl implements TournamentGameServer {

	TournamentGameRepository gameRepository;
	
	@Override
	public Flux<TournamentGameDetails> findGames(GameCriteria criteria) {
		return gameRepository.findAll().map(TournamentGameServerImpl::gameToGameDetails);
	}

	@Override
	public GameManager getGameManger(String gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<TournamentGameDetails> createGame(TournamentGameDetails gameDetails) {
		Assert.notNull(gameDetails, "The game configuration is required.");
		Assert.notNull(gameDetails.getName(), "The name is required when creating a game.");
		Assert.notNull(gameDetails.getType(), "The game type is required when creating a game.");
		Assert.notNull(gameDetails.getStartTimestamp(), "The start date/time is required when creating a game.");
		Assert.notNull(gameDetails.getBuyInChips(), "The buy-in chip stack size is required when creating a game.");
		Assert.notNull(gameDetails.getBuyInAmount(), "The buy-in amount is required when creating a game.");		
		Assert.notNull(gameDetails.getOwnerLoginId(), "The game owner is required when creating a game.");

		TournamentGame game = new TournamentGame();
		game.setName(gameDetails.getName());
		game.setType(gameDetails.getType());
		game.setStartTimestamp(gameDetails.getStartTimestamp());
		game.setStartingStack(gameDetails.getBuyInChips());
//		game.setOwner(gameDetails.getOwnerLoginId());
			
		if (gameDetails.getBlindIntervalMinutes() != null) {
			game.setBlindIntervalMinutes(gameDetails.getBlindIntervalMinutes());
		}
		if (gameDetails.getCliffLevel() != null) {
			game.setCliffLevel(gameDetails.getCliffLevel());
		}
		if (gameDetails.getNumberOfRebuys() != null) {
			game.setNumberOfRebuys(gameDetails.getNumberOfRebuys());
		}
		if (gameDetails.getRebuyChips() != null) {
			game.setRebuyChipAmount(gameDetails.getRebuyChips());
		}
		if (gameDetails.isAddOnAllowed()) {
			Assert.notNull(gameDetails.getAddOnChipAmount(), "The add on chip amount is required.");
			game.setAddOnAllowed(true);
			game.setAddOnChipAmount(gameDetails.getAddOnChipAmount());
		}
		return gameRepository
				.save(game)
				.map(TournamentGameServerImpl::gameToGameDetails);			
	}

	@Override
	public Mono<TournamentGameDetails> updateGame(final TournamentGameDetails configuration) {
		Mono<Game> game = gameRepository.findById(configuration.getId());
		return game
			.doOnNext(gameRepository::save)
			.map(TournamentGameServerImpl::gameToGameDetails);
	}

	@Override
	public Mono<Void> deleteGame(String gameId) {
		return gameRepository.deleteById(gameId);
	}
	
	private static TournamentGameDetails gameToGameDetails(Game game) {
		return TournamentGameDetails.builder()
			//.game(game)
			.build();
	}
}
