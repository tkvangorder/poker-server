package org.homepoker.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.homepoker.common.PokerUtilities.parseCards;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PokerRankerTests {
	
	PokerRanker ranker = new PokerRanker();
	
	@Test
	@DisplayName("Test Five Card Hand Results") 
	public void testFiveCardHandResults() {

		HandResult result =ranker.rankHand(parseCards("AC TC QC KC JC")); 
		assertThat(result.getRank()).isEqualTo(HandRank.STRAIGHT_FLUSH);
		assertThat(result.getCardValues()).containsExactly(CardValue.ACE, CardValue.KING,  CardValue.QUEEN, CardValue.JACK, CardValue.TEN);
		result = ranker.rankHand(parseCards("AC AH JC AS AD"));
		assertThat(result.getRank()).isEqualTo(HandRank.FOUR_OF_A_KIND);
		assertThat(result.getCardValues()).containsExactly(CardValue.ACE, CardValue.ACE,  CardValue.ACE, CardValue.ACE, CardValue.JACK);
		
		result = ranker.rankHand(parseCards("TC TH TS QD QC"));
		assertThat(result.getRank()).isEqualTo(HandRank.FULL_HOUSE);
		assertThat(result.getCardValues()).containsExactly(CardValue.TEN, CardValue.TEN,  CardValue.TEN, CardValue.QUEEN, CardValue.QUEEN);

		result = ranker.rankHand(parseCards("AC 2C QC 7C 3C"));
		assertThat(result.getRank()).isEqualTo(HandRank.FLUSH);
		assertThat(result.getCardValues()).containsExactly(CardValue.ACE, CardValue.QUEEN,  CardValue.SEVEN, CardValue.THREE, CardValue.TWO);

		result = ranker.rankHand(parseCards("AC 2D 4C 5H 3C"));
		assertThat(result.getRank()).isEqualTo(HandRank.STRAIGHT);
		assertThat(result.getCardValues()).containsExactly(CardValue.FIVE, CardValue.FOUR,  CardValue.THREE, CardValue.TWO, CardValue.ACE);

		result = ranker.rankHand(parseCards("JH QC KD TS AC"));
		assertThat(result.getRank()).isEqualTo(HandRank.STRAIGHT);
		assertThat(result.getCardValues()).containsExactly(CardValue.ACE, CardValue.KING,  CardValue.QUEEN, CardValue.JACK, CardValue.TEN);
		
		result = ranker.rankHand(parseCards("3C 3H QD JC 3S"));
		assertThat(result.getRank()).isEqualTo(HandRank.THREE_OF_A_KIND);
		assertThat(result.getCardValues()).containsExactly(CardValue.THREE, CardValue.THREE,  CardValue.THREE, CardValue.QUEEN, CardValue.JACK);

		result = ranker.rankHand(parseCards("3C 4H QD 4C 3S"));
		assertThat(result.getRank()).isEqualTo(HandRank.TWO_PAIR);
		assertThat(result.getCardValues()).containsExactly(CardValue.FOUR, CardValue.FOUR,  CardValue.THREE, CardValue.THREE, CardValue.QUEEN);

		result = ranker.rankHand(parseCards("3C 4H QD JC 3S"));
		assertThat(result.getRank()).isEqualTo(HandRank.PAIR);
		assertThat(result.getCardValues()).containsExactly(CardValue.THREE, CardValue.THREE,  CardValue.QUEEN, CardValue.JACK, CardValue.FOUR);


		result = ranker.rankHand(parseCards("8H QS 4D AC KC"));
		assertThat(result.getRank()).isEqualTo(HandRank.HIGH_CARD);
		assertThat(result.getCardValues()).containsExactly(CardValue.ACE, CardValue.KING,  CardValue.QUEEN, CardValue.EIGHT, CardValue.FOUR);
		
	}
	
	
	@Test
	@DisplayName("Test Seven Card Hand Results") 
	public void testSevenCardHandResults() {

		HandResult result =ranker.rankHand(parseCards("2C AH AC TC QC KC JC")); 
		assertThat(result.getRank()).isEqualTo(HandRank.STRAIGHT_FLUSH);
		assertThat(result.getCardValues()).containsExactly(CardValue.ACE, CardValue.KING,  CardValue.QUEEN, CardValue.JACK, CardValue.TEN);
		result = ranker.rankHand(parseCards("AC JH AH JC AS JS AD"));
		assertThat(result.getRank()).isEqualTo(HandRank.FOUR_OF_A_KIND);
		assertThat(result.getCardValues()).containsExactly(CardValue.ACE, CardValue.ACE,  CardValue.ACE, CardValue.ACE, CardValue.JACK);
		
		result = ranker.rankHand(parseCards("TC KC TH TS 4D QD QC"));
		assertThat(result.getRank()).isEqualTo(HandRank.FULL_HOUSE);
		assertThat(result.getCardValues()).containsExactly(CardValue.TEN, CardValue.TEN,  CardValue.TEN, CardValue.QUEEN, CardValue.QUEEN);

		result = ranker.rankHand(parseCards("AC 2C 8C QC 7C 3C 8H"));
		assertThat(result.getRank()).isEqualTo(HandRank.FLUSH);
		assertThat(result.getCardValues()).containsExactly(CardValue.ACE, CardValue.QUEEN,  CardValue.EIGHT, CardValue.SEVEN, CardValue.THREE);

		result = ranker.rankHand(parseCards("AC 2D 4H 7H 4C 5H 3C"));
		assertThat(result.getRank()).isEqualTo(HandRank.STRAIGHT);
		assertThat(result.getCardValues()).containsExactly(CardValue.FIVE, CardValue.FOUR,  CardValue.THREE, CardValue.TWO, CardValue.ACE);

		result = ranker.rankHand(parseCards("JH QC KD TD 8H TS AC"));
		assertThat(result.getRank()).isEqualTo(HandRank.STRAIGHT);
		assertThat(result.getCardValues()).containsExactly(CardValue.ACE, CardValue.KING,  CardValue.QUEEN, CardValue.JACK, CardValue.TEN);
		
		result = ranker.rankHand(parseCards("3C 3H QD JC 2D 9C 3S"));
		assertThat(result.getRank()).isEqualTo(HandRank.THREE_OF_A_KIND);
		assertThat(result.getCardValues()).containsExactly(CardValue.THREE, CardValue.THREE,  CardValue.THREE, CardValue.QUEEN, CardValue.JACK);

		result = ranker.rankHand(parseCards("3C 4H QD 4C 3S 2D 2H"));
		assertThat(result.getRank()).isEqualTo(HandRank.TWO_PAIR);
		assertThat(result.getCardValues()).containsExactly(CardValue.FOUR, CardValue.FOUR,  CardValue.THREE, CardValue.THREE, CardValue.QUEEN);

		result = ranker.rankHand(parseCards("3C 4H QD JC AS KD 3S"));
		assertThat(result.getRank()).isEqualTo(HandRank.PAIR);
		assertThat(result.getCardValues()).containsExactly(CardValue.THREE, CardValue.THREE,  CardValue.ACE, CardValue.KING, CardValue.QUEEN);


		result = ranker.rankHand(parseCards("2D 8H QS 4D AC KC 3C"));
		assertThat(result.getRank()).isEqualTo(HandRank.HIGH_CARD);
		assertThat(result.getCardValues()).containsExactly(CardValue.ACE, CardValue.KING,  CardValue.QUEEN, CardValue.EIGHT, CardValue.FOUR);
		
	}
	

	
	
}
