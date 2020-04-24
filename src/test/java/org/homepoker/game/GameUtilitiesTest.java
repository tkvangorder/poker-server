package org.homepoker.game;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameUtilitiesTest {

	@Test
	@DisplayName("round chip value to nearest denomination of 50")
	public void testRoundToNearest50() {

		//Edge case for numbers lower than 25, should never be zero.
		assertThat(GameUtilities.roundToBigBlind(1)).isEqualTo(50);
		assertThat(GameUtilities.roundToBigBlind(37)).isEqualTo(50);

		assertThat(GameUtilities.roundToBigBlind(274)).isEqualTo(250);
		assertThat(GameUtilities.roundToBigBlind(275)).isEqualTo(300);
		assertThat(GameUtilities.roundToBigBlind(424)).isEqualTo(400);
		assertThat(GameUtilities.roundToBigBlind(425)).isEqualTo(450);
		assertThat(GameUtilities.roundToBigBlind(499)).isEqualTo(500);
	}

	@Test
	@DisplayName("round chip value to nearest denomination of 100")
	public void testRoundToNearest100() {

		assertThat(GameUtilities.roundToBigBlind(549)).isEqualTo(500);
		assertThat(GameUtilities.roundToBigBlind(550)).isEqualTo(600);
		assertThat(GameUtilities.roundToBigBlind(1249)).isEqualTo(1200);
		assertThat(GameUtilities.roundToBigBlind(1250)).isEqualTo(1300);
		assertThat(GameUtilities.roundToBigBlind(1949)).isEqualTo(1900);
	}

	@Test
	@DisplayName("round chip value to nearest denomination of 500")
	public void testRoundToNearest500() {
		assertThat(GameUtilities.roundToBigBlind(2249)).isEqualTo(2000);
		assertThat(GameUtilities.roundToBigBlind(2250)).isEqualTo(2500);
		assertThat(GameUtilities.roundToBigBlind(4449)).isEqualTo(4500);
		assertThat(GameUtilities.roundToBigBlind(4750)).isEqualTo(5000);
	}

	@Test
	@DisplayName("round chip value to nearest denomination of 1000")
	public void testRoundToNearest1000() {
		assertThat(GameUtilities.roundToBigBlind(5499)).isEqualTo(5000);
		assertThat(GameUtilities.roundToBigBlind(5500)).isEqualTo(6000);
		assertThat(GameUtilities.roundToBigBlind(9499)).isEqualTo(9000);
		assertThat(GameUtilities.roundToBigBlind(9500)).isEqualTo(10000);
		assertThat(GameUtilities.roundToBigBlind(19499)).isEqualTo(19000);
		assertThat(GameUtilities.roundToBigBlind(19500)).isEqualTo(20000);
	}

	@Test
	@DisplayName("round chip value to nearest denomination of 5000")
	public void testRoundToNearest5000() {
		assertThat(GameUtilities.roundToBigBlind(22499)).isEqualTo(20000);
		assertThat(GameUtilities.roundToBigBlind(22500)).isEqualTo(25000);
		assertThat(GameUtilities.roundToBigBlind(47499)).isEqualTo(45000);
		assertThat(GameUtilities.roundToBigBlind(47500)).isEqualTo(50000);
	}

	@Test
	@DisplayName("round chip value to nearest denomination of 10000")
	public void testRoundToNearest10000() {
		assertThat(GameUtilities.roundToBigBlind(54_999)).isEqualTo(50_000);
		assertThat(GameUtilities.roundToBigBlind(55_000)).isEqualTo(60_000);
		assertThat(GameUtilities.roundToBigBlind(104_999)).isEqualTo(100_000);
		assertThat(GameUtilities.roundToBigBlind(105_000)).isEqualTo(110_000);
		assertThat(GameUtilities.roundToBigBlind(194_999)).isEqualTo(190_000);
		assertThat(GameUtilities.roundToBigBlind(195_000)).isEqualTo(200_000);
	}

	@Test
	@DisplayName("round chip value to nearest denomination of 25000")
	public void testRoundToNearest25000() {
		assertThat(GameUtilities.roundToBigBlind(212_499)).isEqualTo(200_000);
		assertThat(GameUtilities.roundToBigBlind(212_500)).isEqualTo(225_000);
		assertThat(GameUtilities.roundToBigBlind(512_499)).isEqualTo(500_000);
		assertThat(GameUtilities.roundToBigBlind(512_500)).isEqualTo(525_000);
		assertThat(GameUtilities.roundToBigBlind(996_000)).isEqualTo(1_000_000);
	}

}
