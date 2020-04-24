package org.homepoker.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.homepoker.game.BlindScheduleBuilder.blindSchedule;

import org.homepoker.game.domain.BlindSchedule;
import org.homepoker.game.domain.Blinds;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
public class BlindScheduleBuilderTest {


	@Test
	@DisplayName("Test the defaults for the blind schedule builder.")
	public void testDefaultBlindSchedule() {
		BlindSchedule schedule = blindSchedule()
				.numberOfPlayers(25)
				.tournamentLengthHours(4)
				.build();

		assertThat(schedule.getBlindLevels()).hasSize(12);

		for (Blinds blinds : schedule.getBlindLevels()) {
			System.out.println("Small Blind : " + blinds.getSmallBlind() + " - Big Blind : " + blinds.getBigBlind());
		}
	}
}
