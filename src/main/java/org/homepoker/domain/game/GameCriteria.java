package org.homepoker.domain.game;

import java.util.Date;

import lombok.Value;

@Value
public class GameCriteria {

	GameStatus status;
	Date startTimestamp;
	Date endTimestamp;
}
