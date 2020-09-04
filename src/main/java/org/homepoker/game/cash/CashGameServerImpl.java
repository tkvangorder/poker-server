package org.homepoker.game.cash;

import java.util.Arrays;
import java.util.Date;

import org.homepoker.common.ValidationException;
import org.homepoker.domain.game.GameCriteria;
import org.homepoker.domain.game.GameStatus;
import org.homepoker.domain.game.GameType;
import org.homepoker.domain.game.Player;
import org.homepoker.domain.game.PlayerStatus;
import org.homepoker.domain.game.cash.CashGame;
import org.homepoker.domain.game.cash.CashGameDetails;
import org.homepoker.domain.user.User;
import org.homepoker.game.GameManager;
import org.homepoker.user.UserManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CashGameServerImpl implements CashGameServer {

	private final CashGameRepository gameRepository;
	private final UserManager userManager;
	
	public CashGameServerImpl(CashGameRepository gameRepository, UserManager userManager) {
		this.gameRepository = gameRepository;
		this.userManager = userManager;
	}

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
		Assert.notNull(gameDetails, "No game details provided.");
		Assert.notNull(gameDetails.getName(), "The name is required when creating a game.");
		Assert.notNull(gameDetails.getBuyInChips(), "The buy-in chip stack size is required when creating a game.");
		Assert.notNull(gameDetails.getBuyInAmount(), "The buy-in amount is required when creating a game.");		
		Assert.notNull(gameDetails.getOwnerLoginId(), "The game owner is required when creating a game.");
		Assert.notNull(gameDetails.getSmallBlind(), "The small blind must be defined for a cash game.");

		//If the a start date is not specified or is before the current date, we just default to
		//"now" and immediately transition game to a "paused" state. The owner can then choose when they want to
		//"un-pause" game.
		Date now = new Date();
		Date startTimestamp = gameDetails.getStartTimestamp();

		GameStatus status = GameStatus.SCHEDULED;
		if (startTimestamp == null || now.after(startTimestamp)) {
			startTimestamp = now;
			status = GameStatus.PAUSED;
		}
		
		//Default game type to Texas Hold'em.
		GameType gameType = gameDetails.getGameType();
		if (gameDetails.getGameType() == null) {
			gameType = GameType.TEXAS_HOLDEM;
		}

		//If big blind is not explicitly passed in, we just double the small blind.
		int bigBlind = gameDetails.getSmallBlind() * 2;
		if (gameDetails.getBigBlind() != null) {
			bigBlind = gameDetails.getBigBlind();
			if (bigBlind <= gameDetails.getSmallBlind()) {
				throw new ValidationException("The big blind must be larger then the small blind. Typically it should be double the small blind.");
			}
		}
						
		CashGame game = CashGame.builder()
			.name(gameDetails.getName())
			.gameType(gameType)
			.status(status)
			.startTimestamp(startTimestamp)
			.buyInChips(gameDetails.getBuyInChips())
			.buyInAmount(gameDetails.getBuyInAmount())
			.smallBlind(gameDetails.getSmallBlind())
			.bigBlind(bigBlind)
			.build();

		return
			//Convert game to mono
			Mono.just(game)
				//Combine the game mono with a mono for the owner. This is so we can convert the loginID to User instance
				.zipWith(getUser(gameDetails.getOwnerLoginId()), (g, user) -> {
					//Set the owner and add that user as a registered user of the game.
					g.setOwner(user);
					g.setPlayers(Arrays.asList(
						Player.builder()
							.user(user)
							.confirmed(true)
							.status(PlayerStatus.AWAY)
							.build()
						));
					return g;
				})
				//Save the game
				.flatMap(g -> gameRepository.save(g))
				//And map it back into a game details.
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
	
	private static CashGameDetails gameToGameDetails(CashGame game) {
		return CashGameDetails.builder()
			.id(game.getId())
			.name(game.getName())
			.gameType(game.getGameType())
			.startTimestamp(game.getStartTimestamp())
			.buyInChips(game.getBuyInChips())
			.buyInAmount(game.getBuyInAmount())
			.ownerLoginId(game.getOwner().getLoginId())
			.smallBlind(game.getSmallBlind())
			.bigBlind(game.getBigBlind())
			.numberOfPlayers(game.getPlayers() == null?0:game.getPlayers().size())
			.build();
	}

	/**
	 * Get the user from the user manager, the returned mono will terminate if the
	 * user ID does not map to a valid user.
	 * 
	 * @param userId user ID
	 * @return Either the user or an error termination if the user does not exist.
	 */
	private Mono<User> getUser(String userId) {
	
		return userManager
			.getUser(userId)
			.switchIfEmpty(Mono.error(new ValidationException("The user [" + userId + "] does not exist.")));	
	}
}
