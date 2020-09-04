package org.homepoker.game.tournament;

import java.math.BigDecimal;
import java.util.Date;

import org.homepoker.domain.game.Game;
import org.homepoker.domain.game.GameCriteria;
import org.homepoker.domain.game.GameStatus;
import org.homepoker.domain.game.GameType;
import org.homepoker.domain.game.tournament.TournamentGame;
import org.homepoker.domain.game.tournament.TournamentGameDetails;
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
		Assert.notNull(gameDetails.getGameType(), "The game type is required when creating a game.");
		Assert.notNull(gameDetails.getBuyInChips(), "The buy-in chip stack size is required when creating a game.");
		Assert.notNull(gameDetails.getBuyInAmount(), "The buy-in amount is required when creating a game.");		
		Assert.notNull(gameDetails.getOwnerLoginId(), "The game owner is required when creating a game.");

		//If the a start date is not specified or is before the current date, we just default to
		//"now" and immediately transition game to a "paused" state. The owner can then choose when they want to
		//"un-pause" game.
		Date now = new Date();
		Date startTimestamp = gameDetails.getStartTimestamp();

		GameStatus status = GameStatus.SCHEDULED;
		if (startTimestamp == null || startTimestamp.after(now)) {
			startTimestamp = now;
			status = GameStatus.PAUSED;
		}

		//Default game type to Texas Hold'em.		
		GameType gameType = gameDetails.getGameType();
		if (gameDetails.getGameType() == null) {
			gameType = GameType.TEXAS_HOLDEM;
		}

		//If blind intervals is not explicitly set, default to 15 minutes.
		int blindIntervalMinutes = 15;
		if (gameDetails.getBlindIntervalMinutes() != null) {
			blindIntervalMinutes = gameDetails.getBlindIntervalMinutes();
		}

		//Re-buys are "enabled" if number of re-buys is greater then 0.
		//If a re-buy chip amount is not provided, we default it to the buy-in chip amount.
		//If a re-buy amount is not provided, we default it to the buy amount. 
		int numberOfRebuys = 0;
		Integer rebuyChips = null;
		BigDecimal rebuyAmount = null;
		
		if (gameDetails.getNumberOfRebuys() != null) {
			numberOfRebuys = gameDetails.getNumberOfRebuys();
			if (numberOfRebuys > 0) {
				rebuyChips = gameDetails.getRebuyChips();
				if (rebuyChips == null) {
					rebuyChips = gameDetails.getBuyInChips();
				}
				rebuyAmount = gameDetails.getRebuyAmount();
				if (rebuyAmount == null) {
					rebuyAmount = gameDetails.getBuyInAmount();
				}
			}
		}
		
		//If add-ons are "enabled":
		//  If a add-on chip amount is not provided, we default it to the buy-in chip amount.
		//  If a add-on amount is not provided, we default it to the buy amount. 
		boolean addOnsAllowed = gameDetails.isAddOnAllowed();
		Integer addOnChips = null;
		BigDecimal addOnAmount = null;
		
		if (addOnsAllowed) {
			addOnChips = gameDetails.getAddOnChipAmount();
			if (addOnChips == null) {
				addOnChips = gameDetails.getBuyInChips();
			}
			addOnAmount = gameDetails.getAddOnAmount();
			if (addOnAmount == null) {
				addOnAmount = gameDetails.getBuyInAmount();
			}
		}

		//We need a cliff where the re-buys are no longer allowed and also the point where
		//add-ons are applied. If the game has re-buys OR add-ons and a cliff has not been defined,
		//we default to 4.
		//So if the blind interval is 15 minutes: the cliff is applied on the 4th blind level (1 hour into the tournament) 
		int cliffLevel = 0;
		if (numberOfRebuys > 0 || addOnsAllowed) {
			cliffLevel = gameDetails.getCliffLevel() != null?gameDetails.getCliffLevel():4;
		}
		
		TournamentGame game = TournamentGame.builder()
			.name(gameDetails.getName())
			.gameType(gameType)
			.status(status)
			.startTimestamp(startTimestamp)
			.buyInChips(gameDetails.getBuyInChips())
			.buyInAmount(gameDetails.getBuyInAmount())
			.blindIntervalMinutes(blindIntervalMinutes)
			.numberOfRebuys(numberOfRebuys)
			.rebuyChipAmount(rebuyChips)
			.rebuyAmount(rebuyAmount)
			.addOnAllowed(addOnsAllowed)
			.addOnChipAmount(addOnChips)
			.addOnAmount(addOnAmount)
			.cliffLevel(cliffLevel)
			.build();

		//TODO Need to resolve user ID to user object prior to saving.
		
		//Save the game.
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
