package org.springframework.samples.upstream.round;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class RoundServiceTest {
    @Autowired
    protected RoundService roundService;
    
	@Test
	void shouldFindRoundById() {
		Round round = this.roundService.findRoundById(1);
		assertThat(round.getNum_players()).isEqualTo(3);
	}
	
	
}
