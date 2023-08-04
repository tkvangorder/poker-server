package org.homepoker.domain.game;

import lombok.Value;

import java.time.LocalDate;

@Value
public class GameCriteria {

  String name;
  GameStatus status;
  LocalDate startDate;
  LocalDate endDate;
}
