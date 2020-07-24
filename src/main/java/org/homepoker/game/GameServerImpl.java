package org.homepoker.game;

import org.homepoker.game.domain.Game;
import org.homepoker.game.domain.GameConfiguration;
import org.homepoker.game.domain.GameCriteria;
import org.springframework.stereotype.Service;

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
		Game game = null;		
		return gameRepository.save(game);
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
