package org.homepoker.rsocket;

import lombok.Value;
import org.homepoker.common.Command;

@Value
public class GameCommand {
  String gameId;
  Command command;
}
