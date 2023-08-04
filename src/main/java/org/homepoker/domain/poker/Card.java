package org.homepoker.domain.poker;

import lombok.Value;

@Value
public class Card {

  CardValue value;
  CardSuit suit;

  public String toString() {
    return new String(new char[]{value.getValue(), suit.getValue()});
  }
}
