package org.homepoker.common;

import lombok.Value;

@Value
public class Command {

	private CommandId commandId;
	private String payload;
}
