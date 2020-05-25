package org.homepoker.rsocket;

import org.homepoker.common.Command;

import lombok.Value;

@Value
public class GameCommand {
	
	private String gameId;
	private Command command;
}
