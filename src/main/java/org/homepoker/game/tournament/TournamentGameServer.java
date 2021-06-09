package org.homepoker.game.tournament;

import org.homepoker.common.ValidationException;
import org.homepoker.domain.game.GameCriteria;
import org.homepoker.domain.game.tournament.TournamentGameDetails;
import org.homepoker.game.GameManager;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TournamentGameServer {

				/**
				 * Find cash games that have been persisted.
				 * 
				 * @param criteria The search criteria
				 * @return A list of games that match the criteria.
				 */
				Flux<TournamentGameDetails> findGames(GameCriteria criteria);

				/**
				 * Get the game manager for a given gameId.
				 * 
				 * @param gameId
				 * @return
				 */
				Mono<GameManager> getGameManger(String gameId);

				/**
				 * Create/schedule a new game. 
				 * 
				 * The game state passed into this method will be validated.
				 * @param gameDetails The game details to be used to persist a new game.
				 * @return The fully persisted game state. If the game's start date is past the current time-stamp, it will be marked as "PAUSED"
				 * @throws ValidationException If a validation error occurs 
				 */
				Mono<TournamentGameDetails> createGame(TournamentGameDetails gameDetails);

				/**
				 * Update an existing game.
				 * 
				 * @param gameDetails The game details to be applied to the game.
				 * @return The updated game state or an error if the game does not exist or there is a validation error.
				 * 
				 * @throws ValidationException If the game does not exist or a validation error occurs 
				 */
				Mono<TournamentGameDetails> updateGame(TournamentGameDetails gameDetails);

				/**
				 * Retrieve the game details for an existing game.
				 * 
				 * @param gameId The Id of the game.
				 * @return The game details or an error if the game does not exist.
				 * 
				 * @throws ValidationException If the game does not exist or a validation error occurs 
				 */
				Mono<TournamentGameDetails> getGame(String gameId);

				/**
				 * Delete an existing game.
				 * 
				 * @param gameId The ID of the game
				 * @throws ValidationException If the game cannot be deleted.
				 */
				Mono<Void> deleteGame(String gameId);

}
