package org.homepoker.game.cash;

import org.homepoker.domain.game.cash.CashGame;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * A repository for persisting and retrieving games.
 * 
 * @author tyler.vangorder
 */
@Repository
public interface CashGameRepository extends ReactiveMongoRepository<CashGame, String> {
}
