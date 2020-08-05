package org.homepoker.game;

import org.homepoker.domain.game.Blinds;
import org.homepoker.domain.game.CashGame;
import org.homepoker.domain.game.Game;
import org.homepoker.domain.game.GameCriteria;
import org.homepoker.domain.game.GameDetails;
import org.homepoker.domain.game.GameFormat;
import org.homepoker.domain.game.TournamentGame;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GameServerImpl implements GameServer {

	GameRepository gameRepository;
	
	@Override
	public Flux<GameDetails> findGames(GameCriteria criteria) {
		return gameRepository.findAll().map(GameServerImpl::gameToGameDetails);
	}

	@Override
	public GameManager getGameManger(String gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<GameDetails> createGame(GameDetails gameDetails) {
		Assert.notNull(gameDetails, "The game configuration is required.");
		Assert.notNull(gameDetails.getName(), "The name is required when creating a game.");
		Assert.notNull(gameDetails.getGameFormat(), "The game format is required when creating a game.");
		Assert.notNull(gameDetails.getType(), "The game type is required when creating a game.");
		Assert.notNull(gameDetails.getStartTimestamp(), "The start date/time is required when creating a game.");
		Assert.notNull(gameDetails.getStartingChipStack(), "The starting chip stack size is required when creating a game.");
		Assert.notNull(gameDetails.getOwner(), "The game owner is required when creating a game.");
		
		
		if (gameDetails.getGameFormat() == GameFormat.CASH) {
			Assert.notNull(gameDetails.getSmallBlind(), "The small blind must be defined for a cash game.");
			Assert.notNull(gameDetails.getBigBlind(), "The big blind must be defined for a cash game.");
			CashGame game = new CashGame(new Blinds(gameDetails.getSmallBlind(), gameDetails.getSmallBlind()));
			game.setName(gameDetails.getName());
			game.setType(gameDetails.getType());
			game.setStartTimestamp(gameDetails.getStartTimestamp());
			game.setStartingStack(gameDetails.getStartingChipStack());
			game.setOwner(gameDetails.getOwner());
			return gameRepository
					.save(game)
					.map(GameServerImpl::gameToGameDetails);			
		} else {
			TournamentGame game = new TournamentGame();
			game.setName(gameDetails.getName());
			game.setType(gameDetails.getType());
			game.setStartTimestamp(gameDetails.getStartTimestamp());
			game.setStartingStack(gameDetails.getStartingChipStack());
			game.setOwner(gameDetails.getOwner());
			
			if (gameDetails.getBlindIntervalMinutes() != null) {
				game.setBlindIntervalMinutes(gameDetails.getBlindIntervalMinutes());
			}
			if (gameDetails.getCliffLevel() != null) {
				game.setCliffLevel(gameDetails.getCliffLevel());
			}
			if (gameDetails.getNumberOfRebuys() != null) {
				game.setNumberOfRebuys(gameDetails.getNumberOfRebuys());
			}
			if (gameDetails.getRebuyChipAmount() != null) {
				game.setRebuyChipAmount(gameDetails.getRebuyChipAmount());
			}
			if (gameDetails.isAddOnAllowed()) {
				Assert.notNull(gameDetails.getAddOnChipAmount(), "The add on chip amount is required.");
				game.setAddOnAllowed(true);
				game.setAddOnChipAmount(gameDetails.getAddOnChipAmount());
			}
			return gameRepository
					.save(game)
					.map(GameServerImpl::gameToGameDetails);			
		}
	}

	@Override
	public Mono<GameDetails> updateGame(final GameDetails configuration) {
		Mono<Game> game = gameRepository.findById(configuration.getId());
		return game
			.doOnNext(gameRepository::save)
			.map(GameServerImpl::gameToGameDetails);
	}

	@Override
	public Mono<Void> deleteGame(String gameId) {
		return gameRepository.deleteById(gameId);
	}
	
	private static GameDetails gameToGameDetails(Game game) {
		return GameDetails.builder()
			.game(game)
			.build();
	}
}
