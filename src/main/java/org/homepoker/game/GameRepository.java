package org.homepoker.game;

import org.homepoker.domain.game.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * A repository for persisting and retrieving games.
 * 
 * @author tyler.vangorder
 */
@Repository
public interface GameRepository extends ReactiveMongoRepository<Game, String> {
}
