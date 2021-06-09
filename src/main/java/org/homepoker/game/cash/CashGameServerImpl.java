package org.homepoker.game.cash;

import static org.springframework.data.mongodb.core.query.Query.query;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
import org.jctools.maps.NonBlockingHashMap;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CashGameServerImpl implements CashGameServer {

				private final CashGameRepository gameRepository;
				private final UserManager userManager;
				private final ReactiveMongoOperations mongoOperations;

				//I am using a non-blocking variant of ConcurrentHashMap to I can use an atomic computeIfAbsent
				//without blocking the event loop.
				private final Map<String, Mono<GameManager>> gameManagerMap = new NonBlockingHashMap<>();

				public CashGameServerImpl(CashGameRepository gameRepository, UserManager userManager, ReactiveMongoOperations mongoOperations) {
								this.gameRepository = gameRepository;
								this.userManager = userManager;
								this.mongoOperations = mongoOperations;
				}

				@Override
				public Flux<CashGameDetails> findGames(GameCriteria criteria) {

								if (criteria == null ||
										(criteria.getStatus() == null && criteria.getStartDate() == null && criteria.getEndDate() == null)) {
												//No criteria provided, return all games.
												return gameRepository.findAll().map(CashGameServerImpl::gameToGameDetails);
								}

								Criteria mongoCriteria = new Criteria();

								if (criteria.getStatus() != null) {
												mongoCriteria.and("status").is(criteria.getStatus());
								}
								if (criteria.getStartDate() != null) {
												mongoCriteria.and("startTimestamp").gte(criteria.getStartDate());
								}
								if (criteria.getEndDate() != null) {
												//The end date is intended to include any timestamp in that day, we just add one to the
												//day to insure we get all games on the end date.
												mongoCriteria.and("endTimestamp").lte(criteria.getEndDate().plusDays(1));
								}

								return mongoOperations.query(CashGame.class)
										.matching(query(mongoCriteria))
										.all()
										.map(CashGameServerImpl::gameToGameDetails);

				}

				@Override
				public Mono<GameManager> getGameManger(String gameId) {
								//There is a map of eagerly fetched Mono<GameManager> instances. If the game manager is
								//present we pass a new mono to the subscriber (via the defer)
		
								return Mono.defer(() -> {
												return gameManagerMap.computeIfAbsent(gameId,
														(id) -> {
																		//If the game manager is not yet in memory, we retrieve the game from
																		//the database and materialize the game manager. (Note the cache()) on the end, 
																		//we want to cache the mono retrieved from the database, so we dont keep
																		//retrieving it on subsequent calls.
																		return gameRepository
																				.findById(gameId)
																				.doOnSuccess(g -> g.setName("Excellent"))
																				.switchIfEmpty(Mono.error(new ValidationException("The cash game [" + gameId + "] does not exist.")))
																				.flatMap(g -> Mono.just((GameManager) new CashGameManager(g))).cache();
														});
								});
				}

				@Override
				public Mono<CashGameDetails> createGame(CashGameDetails gameDetails) {

								//Create a new cash game and create a pipeline to apply the game details.
								CashGame game = CashGame.builder().build();
								return applyDetailsToGame(game, gameDetails)
										//Save the game
										.flatMap(gameRepository::save)
										//And map it back into a game details.
										.map(CashGameServerImpl::gameToGameDetails);
				}

				@Override
				public Mono<CashGameDetails> updateGame(final CashGameDetails details) {
								//Find the game by ID
								return gameRepository.findById(details.getId())
										.switchIfEmpty(Mono.error(new ValidationException("The cash game [" + details.getId() + "] does not exist.")))
										//Apply the details
										.flatMap(g -> applyDetailsToGame(g, details))
										//Save
										.flatMap(gameRepository::save)
										//Map back to a details object.
										.map(CashGameServerImpl::gameToGameDetails);
				}

				@Override
				public Mono<CashGameDetails> getGame(String gameId) {
								return gameRepository.findById(gameId)
										.switchIfEmpty(Mono.error(new ValidationException("The cash game [" + gameId + "] does not exist.")))
										.map(CashGameServerImpl::gameToGameDetails);
				}

				@Override
				public Mono<Void> deleteGame(String gameId) {
								return gameRepository.deleteById(gameId);
				}

				/**
				 * This method will apply the game details to the game and return a mono for the cash game.
				 * 
				 * @param game The game that will have the details applied to it.
				 * @param gameDetails The game details.
				 * @return A mono of the CashGame
				 */
				private Mono<CashGame> applyDetailsToGame(CashGame game, CashGameDetails gameDetails) {

								if (game.getStatus() != GameStatus.SCHEDULED) {
												throw new ValidationException("You can only update the details of the game prior to it starting");
								}
								Assert.notNull(gameDetails.getName(), "The name is required when creating a game.");
								Assert.notNull(gameDetails.getBuyInChips(), "The buy-in chip stack size is required when creating a game.");
								Assert.notNull(gameDetails.getBuyInAmount(), "The buy-in amount is required when creating a game.");
								Assert.notNull(gameDetails.getOwnerLoginId(), "The game owner is required when creating a game.");
								Assert.notNull(gameDetails.getSmallBlind(), "The small blind must be defined for a cash game.");

								//If the a start date is not specified or is before the current date, we just default to
								//"now" and immediately transition game to a "paused" state. The owner can then choose when they want to
								//"un-pause" game.
								LocalDateTime now = LocalDateTime.now();
								LocalDateTime startTimestamp = gameDetails.getStartTimestamp();

								GameStatus status = GameStatus.SCHEDULED;
								if (startTimestamp == null || now.isAfter(startTimestamp)) {
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

								game.setName(gameDetails.getName());
								game.setGameType(gameType);
								game.setStatus(status);
								game.setStartTimestamp(startTimestamp);
								game.setBuyInChips(gameDetails.getBuyInChips());
								game.setBuyInAmount(gameDetails.getBuyInAmount());
								game.setSmallBlind(gameDetails.getSmallBlind());
								game.setBigBlind(bigBlind);

								return Mono
										.just(game)
										//Combine the game mono with a mono for the owner. This is so we can convert the loginID to User instance
										.zipWith(getUser(gameDetails.getOwnerLoginId()), (g, user) -> {
														//Set the owner and add that user as a registered user of the game.
														g.setOwner(user);
														if (g.getPlayers() == null) {
																		g.setPlayers(new HashMap<>());
														}
														if (!g.getPlayers().containsKey(user.getLoginId())) {
																		Player player = Player.builder().user(user).confirmed(true).status(PlayerStatus.AWAY).build();
																		g.getPlayers().put(user.getLoginId(), player);
														}
														return g;
										});
				}

				/**
				 * Method to convert a cash game into a cash game details.
				 * 
				 * @param game The cash game
				 * @return The details for the cash game.
				 */
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
										.numberOfPlayers(game.getPlayers() == null ? 0 : game.getPlayers().size())
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
