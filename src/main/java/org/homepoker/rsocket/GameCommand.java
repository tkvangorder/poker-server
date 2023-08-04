package org.homepoker.rsocket;

import lombok.Value;
import org.homepoker.command.Command;

@Value
public class GameCommand {
  String gameId;
  Command command;
}
