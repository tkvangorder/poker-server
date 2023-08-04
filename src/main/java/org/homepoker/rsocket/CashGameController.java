package org.homepoker.rsocket;

import org.homepoker.common.Command;
import org.homepoker.domain.game.GameCriteria;
import org.homepoker.domain.game.cash.CashGameDetails;
import org.homepoker.game.cash.CashGameServer;
import org.homepoker.security.PokerUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CashGameController {

  private final CashGameServer gameServer;

  public CashGameController(CashGameServer gameServer) {
    this.gameServer = gameServer;
  }

  CashGameDetails createCashGame(CashGameDetails configuration, @AuthenticationPrincipal PokerUserDetails user) {
    configuration.setOwnerLoginId(user.getUsername());
    return gameServer.createGame(configuration);
  }

  CashGameDetails updateGameDetails(CashGameDetails configuration) {
    return gameServer.updateGameDetails(configuration);
  }

  void deleteGame(String gameId) {
    gameServer.deleteGame(gameId);
  }

  List<CashGameDetails> findGames(GameCriteria criteria) {
    return gameServer.findGames(criteria);
  }

  void registerForGame(String gameId, @AuthenticationPrincipal PokerUserDetails user) {
    gameServer.getGameManger(gameId).submitCommand(Command.asRegisterUser(user));
  }

  void joinGame(final String gameId, @AuthenticationPrincipal PokerUserDetails user) {
    //Unclear what to do here yet with WebSockets.
  }

  void gameCommand(GameCommand command, @AuthenticationPrincipal PokerUserDetails user) {
    gameServer.getGameManger(command.getGameId()).submitCommand(command.getCommand().withUser(user));
  }
}