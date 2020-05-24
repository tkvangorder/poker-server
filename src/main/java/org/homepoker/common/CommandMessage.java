package org.homepoker.common;

import lombok.Value;

@Value
public class CommandMessage {

	private Command command;
	private String payload;
}
