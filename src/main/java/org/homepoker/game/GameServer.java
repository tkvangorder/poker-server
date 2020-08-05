package org.homepoker.game;

import org.homepoker.common.GameValidationException;
import org.homepoker.domain.game.GameCriteria;
import org.homepoker.domain.game.GameDetails;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameServer {

	/**
	 * Find games that have been persisted.
	 * 
	 * @param criteria The search criteria
	 * @return A list of games that match the criteria.
	 */
	Flux<GameDetails> findGames(GameCriteria criteria);
	
	/**
	 * Get the game manager for a given gameId.
	 * 
	 * @param gameId
	 * @return
	 */
	GameManager getGameManger(String gameId);

	/**
	 * Create/schedule a new game. 
	 * 
	 * The game state passed into this method will be validated.
	 * @param The passed in game state is used to persist a new game.
	 * @return The fully persisted game state. If the game's start date is past the current time-stamp, it will be marked as "PAUSED"
	 * @throws GameValidationException If a validation error occurs 
	 */
	Mono<GameDetails> createGame(GameDetails gameDetails);
	
	/**
	 * Update an existing game.
	 * 
	 * @param The game details to be applied to the game.
	 * @return The updated game state.
	 * 
	 * @throws GameValidationException If the game does not exist or a validation error occurs 
	 */
	Mono<GameDetails> updateGame(GameDetails configuration);
	
	/**
	 * Delete an existing game.
	 * 
	 * @param gameId The ID of the game
	 * @throws GameValidationException If the game cannot be deleted.
	 */
	Mono<Void> deleteGame(String gameId);

}
