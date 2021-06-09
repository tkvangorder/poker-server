package org.homepoker.game.tournament;

import static org.springframework.data.mongodb.core.query.Query.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.homepoker.common.ValidationException;
import org.homepoker.domain.game.GameCriteria;
import org.homepoker.domain.game.GameStatus;
import org.homepoker.domain.game.GameType;
import org.homepoker.domain.game.Player;
import org.homepoker.domain.game.PlayerStatus;
import org.homepoker.domain.game.tournament.TournamentGame;
import org.homepoker.domain.game.tournament.TournamentGameDetails;
import org.homepoker.domain.user.User;
import org.homepoker.game.GameManager;
import org.homepoker.user.UserManager;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TournamentGameServerImpl implements TournamentGameServer {

				private final TournamentGameRepository gameRepository;
				private final UserManager userManager;
				private final ReactiveMongoOperations mongoOperations;

				public TournamentGameServerImpl(TournamentGameRepository gameRepository, UserManager userManager, ReactiveMongoOperations mongoOperations) {
								this.gameRepository = gameRepository;
								this.userManager = userManager;
								this.mongoOperations = mongoOperations;
				}

				@Override
				public Flux<TournamentGameDetails> findGames(GameCriteria criteria) {
								if (criteria == null ||
										(criteria.getStatus() == null && criteria.getStartDate() == null && criteria.getEndDate() == null)) {
												//No criteria provided, return all games.
												return gameRepository.findAll().map(TournamentGameServerImpl::gameToGameDetails);
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

								return mongoOperations.query(TournamentGame.class)
										.matching(query(mongoCriteria))
										.all()
										.map(TournamentGameServerImpl::gameToGameDetails);
				}

				@Override
				public Mono<GameManager> getGameManger(String gameId) {
								// TODO Auto-generated method stub
								return null;
				}

				@Override
				public Mono<TournamentGameDetails> createGame(TournamentGameDetails gameDetails) {

								//Create a new cash game and create a pipeline to apply the game details.
								TournamentGame game = TournamentGame.builder().build();
								return applyDetailsToGame(game, gameDetails)
										//Save the game
										.flatMap(gameRepository::save)
										//And map it back into a game details.
										.map(TournamentGameServerImpl::gameToGameDetails);
				}

				@Override
				public Mono<TournamentGameDetails> updateGame(final TournamentGameDetails details) {
								//Find the game by ID
								return gameRepository.findById(details.getId())
										.switchIfEmpty(Mono.error(new ValidationException("The tournament game [" + details.getId() + "] does not exist.")))
										//Apply the details
										.flatMap(g -> applyDetailsToGame(g, details))
										//Save
										.flatMap(gameRepository::save)
										//Map back to a details object.
										.map(TournamentGameServerImpl::gameToGameDetails);
				}

				@Override
				public Mono<TournamentGameDetails> getGame(String gameId) {
								return gameRepository.findById(gameId)
										.switchIfEmpty(Mono.error(new ValidationException("The tournament game [" + gameId + "] does not exist.")))
										.map(TournamentGameServerImpl::gameToGameDetails);
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
				private Mono<TournamentGame> applyDetailsToGame(TournamentGame game, TournamentGameDetails gameDetails) {
								Assert.notNull(gameDetails, "The game configuration is required.");
								Assert.notNull(gameDetails.getName(), "The name is required when creating a game.");
								Assert.notNull(gameDetails.getGameType(), "The game type is required when creating a game.");
								Assert.notNull(gameDetails.getBuyInChips(), "The buy-in chip stack size is required when creating a game.");
								Assert.notNull(gameDetails.getBuyInAmount(), "The buy-in amount is required when creating a game.");
								Assert.notNull(gameDetails.getOwnerLoginId(), "The game owner is required when creating a game.");

								//If the a start date is not specified or is before the current date, we just default to
								//"now" and immediately transition game to a "paused" state. The owner can then choose when they want to
								//"un-pause" game.
								LocalDateTime now = LocalDateTime.now();
								LocalDateTime startTimestamp = gameDetails.getStartTimestamp();

								GameStatus status = GameStatus.SCHEDULED;
								if (startTimestamp == null || startTimestamp.isAfter(now)) {
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
												addOnChips = gameDetails.getAddOnChips();
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
												cliffLevel = gameDetails.getCliffLevel() != null ? gameDetails.getCliffLevel() : 4;
								}

								game.setName(gameDetails.getName());
								game.setGameType(gameType);
								game.setStatus(status);
								game.setStartTimestamp(startTimestamp);
								game.setBuyInChips(gameDetails.getBuyInChips());
								game.setBuyInAmount(gameDetails.getBuyInAmount());
								game.setBlindIntervalMinutes(blindIntervalMinutes);
								game.setNumberOfRebuys(numberOfRebuys);
								game.setRebuyChips(rebuyChips);
								game.setRebuyAmount(rebuyAmount);
								game.setAddOnAllowed(addOnsAllowed);
								game.setAddOnChips(addOnChips);
								game.setAddOnAmount(addOnAmount);
								game.setCliffLevel(cliffLevel);

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

				private static TournamentGameDetails gameToGameDetails(TournamentGame game) {
								return TournamentGameDetails.builder()
										.id(game.getId())
										.name(game.getName())
										.gameType(game.getGameType())
										.startTimestamp(game.getStartTimestamp())
										.ownerLoginId(game.getOwner().getLoginId())
										.buyInChips(game.getBuyInChips())
										.buyInAmount(game.getBuyInAmount())
										.estimatedTournamentLengthHours(game.getEstimatedTournamentLengthHours())
										.blindIntervalMinutes(game.getBlindIntervalMinutes())
										.numberOfRebuys(game.getNumberOfRebuys())
										.rebuyChips(game.getRebuyChips())
										.rebuyAmount(game.getRebuyAmount())
										.addOnAllowed(game.isAddOnAllowed())
										.addOnChips(game.getAddOnChips())
										.addOnAmount(game.getAddOnAmount())
										.cliffLevel(game.getCliffLevel())
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
