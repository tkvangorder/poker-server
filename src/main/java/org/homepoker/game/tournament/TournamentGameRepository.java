package org.homepoker.game.tournament;

import org.homepoker.domain.game.tournament.TournamentGame;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * A repository for persisting and retrieving games.
 * 
 * @author tyler.vangorder
 */
@Repository
public interface TournamentGameRepository extends ReactiveMongoRepository<TournamentGame, String> {
}
