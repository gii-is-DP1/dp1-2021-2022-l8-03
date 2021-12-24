package org.springframework.samples.upstream.score;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.upstream.round.RoundService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ScoreServiceTest {
	
	@Autowired
	private ScoreService scoreService;
	@Autowired
	private RoundService roundService;
	
	@Test
	void shouldFindByPlayer() {
		Score score = scoreService.findByPlayerAndRound(11, 1);
		assertThat(score.getValue()).isEqualTo(0);
	}
	
	@Test
	void shouldNotFindByPlayer() {
		Score score = scoreService.findByPlayerAndRound(11, 2);
		assertThat(score).isEqualTo(null);
	}
	
	@Test
	void shouldSetPlayerScores() {
		scoreService.setPlayerScores(roundService.findRoundById(1));
		Score score = scoreService.findByPlayerAndRound(11, 1);
		assertThat(score.getValue()).isEqualTo(2);
	}
}
