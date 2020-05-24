package org.homepoker.game.domain;

import java.util.Date;

import lombok.Value;

@Value
public class GameCriteria {

	GameStatus status;
	Date startTimestamp;
	Date endTimestamp;
}
