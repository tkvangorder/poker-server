package org.homepoker.game;

import static org.homepoker.game.BlindScheduleBuilder.blindSchedule;

import org.homepoker.domain.game.BlindSchedule;
import org.homepoker.domain.game.Blinds;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
public class BlindScheduleBuilderTest {


	@Test
	@DisplayName("Test the defaults for the blind schedule builder.")
	public void testDefaultBlindSchedule() {
		BlindSchedule schedule = blindSchedule()
				.numberOfPlayers(24)
				.tournamentLengthHours(4)
				.build();

		int index = 0;
		for (Blinds blinds : schedule.getBlindLevels()) {
			System.out.println((index + 1) + " - Small Blind : " + blinds.getSmallBlind() + " - Big Blind : " + blinds.getBigBlind());
			index++;
		}
	}
}
