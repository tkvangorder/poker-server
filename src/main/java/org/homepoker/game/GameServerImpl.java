package org.homepoker.game;

import org.homepoker.game.domain.Blinds;
import org.homepoker.game.domain.CashGame;
import org.homepoker.game.domain.Game;
import org.homepoker.game.domain.GameConfiguration;
import org.homepoker.game.domain.GameCriteria;
import org.homepoker.game.domain.GameFormat;
import org.homepoker.game.domain.TournamentGame;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GameServerImpl implements GameServer {

	GameRepository gameRepository;
	
	@Override
	public Flux<Game> findGames(GameCriteria criteria) {
		
		return gameRepository.findAll();
	}

	@Override
	public GameManager getGameManger(String gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Game> createGame(GameConfiguration configuration) {
		Assert.notNull(configuration, "The game configuration is required.");
		Assert.notNull(configuration.getName(), "The name is required when creating a game.");
		Assert.notNull(configuration.getGameFormat(), "The game format is required when creating a game.");
		Assert.notNull(configuration.getType(), "The game type is required when creating a game.");
		Assert.notNull(configuration.getStartTimestamp(), "The start date/time is required when creating a game.");
		Assert.notNull(configuration.getStartingChipStack(), "The starting chip stack size is required when creating a game.");
		Assert.notNull(configuration.getOwner(), "The game owner is required when creating a game.");
		
		
		if (configuration.getGameFormat() == GameFormat.CASH) {
			Assert.notNull(configuration.getSmallBlind(), "The small blind must be defined for a cash game.");
			Assert.notNull(configuration.getBigBlind(), "The big blind must be defined for a cash game.");
			CashGame game = new CashGame(new Blinds(configuration.getSmallBlind(), configuration.getSmallBlind()));
			game.setName(configuration.getName());
			game.setType(configuration.getType());
			game.setStartTimestamp(configuration.getStartTimestamp());
			game.setStartingStack(configuration.getStartingChipStack());
			game.setOwner(configuration.getOwner());
			return gameRepository.save(game);			
		} else {
			TournamentGame game = new TournamentGame();
			game.setName(configuration.getName());
			game.setType(configuration.getType());
			game.setStartTimestamp(configuration.getStartTimestamp());
			game.setStartingStack(configuration.getStartingChipStack());
			game.setOwner(configuration.getOwner());
			
			if (configuration.getBlindIntervalMinutes() != null) {
				game.setBlindIntervalMinutes(configuration.getBlindIntervalMinutes());
			}
			if (configuration.getCliffLevel() != null) {
				game.setCliffLevel(configuration.getCliffLevel());
			}
			if (configuration.getNumberOfRebuys() != null) {
				game.setNumberOfRebuys(configuration.getNumberOfRebuys());
			}
			if (configuration.getRebuyChipAmount() != null) {
				game.setRebuyChipAmount(configuration.getRebuyChipAmount());
			}
			if (configuration.isAddOnAllowed()) {
				Assert.notNull(configuration.getAddOnChipAmount(), "The add on chip amount is required.");
				game.setAddOnAllowed(true);
				game.setAddOnChipAmount(configuration.getAddOnChipAmount());
			}
			return gameRepository.save(game);			
		}
	}

	@Override
	public Mono<Game> updateGame(final GameConfiguration configuration) {
		Mono<Game> game = gameRepository.findById(configuration.getId());
		return game.doOnNext((g) -> {
			gameRepository.save(g);
		});
	}

	@Override
	public Mono<Void> deleteGame(String gameId) {
		return gameRepository.deleteById(gameId);
	}
}
