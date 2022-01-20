package org.springframework.samples.upstream.round;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.player.PlayerService;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class RoundServiceTest {
    @Autowired
    protected RoundService roundService;
    
    @Autowired
    protected PlayerService playerService;
    
    private Validator createValidator() {
    	LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
    	localValidatorFactoryBean.afterPropertiesSet();
    	return localValidatorFactoryBean;
    }
    
	@Test
	void shouldFindRoundById() {
		Round round = this.roundService.findRoundById(1);
		assertThat(round.getNum_players()).isEqualTo(3);
	}
	
	@Test
	void shouldFindCreated() {
		Collection<Round> rounds = this.roundService.findCreatedRounds();
		assertThat(rounds.size()).isEqualTo(0);
	}
	
	@Test
	void shouldFindInCourse() {
		Collection<Round> rounds = this.roundService.findInCourseRounds();
		assertThat(rounds.size()).isEqualTo(4);
	}
	
	@Test
	void shouldFindFinished() {
		Collection<Round> rounds = this.roundService.findFinishedRounds();
		assertThat(rounds.size()).isEqualTo(1);
	}
	
	
	@Test
	void shouldNotFindRoundById() {
		Round round = this.roundService.findRoundById(99);
		assertThat(round).isEqualTo(null);
	}
	
	@Test
	void shouldSaveRound() {
		Collection<Round> roundsStart = this.roundService.findAll();
		Round round=new Round();
			round.setPlayer(this.playerService.findPlayerByUsername("mandommag"));
			round.setWhirlpools(true);
			round.setRapids(true);
			round.setNum_players(3);
		roundService.saveRound(round);
		Collection<Round> roundsEnd= this.roundService.findAll();
		assertThat(roundsEnd.size()).isEqualTo(roundsStart.size()+1);
	}
	
	@Test
	void shouldNotSaveRound() {
		Round round=new Round();
			round.setPlayer(this.playerService.findPlayerByUsername("mandommag"));
			round.setWhirlpools(true);
			round.setNum_players(3);
			
			Validator validator = createValidator();
			Set<ConstraintViolation<Round>> constraintViolations = validator.validate(round);
			assertThat(constraintViolations.size()).isEqualTo(1);
			ConstraintViolation<Round> violation = constraintViolations.iterator().next();
			assertThat(violation.getPropertyPath().toString()).isEqualTo("rapids");
			assertThat(violation.getMessage()).isEqualTo("no puede ser null");

	}
	
	@Test
	void shouldDeleteRound() {
		List<Player> players=new ArrayList<Player>();
		Round round=new Round();
			round.setPlayer(this.playerService.findPlayerByUsername("mandommag"));
			round.setWhirlpools(true);
			round.setRapids(true);
			round.setNum_players(3);
			round.setPlayers(players);
		this.roundService.saveRound(round);
		Collection<Round> roundsStart = this.roundService.findAll();
		this.roundService.deleteRound(round);
		Collection<Round> roundsEnd= this.roundService.findAll();
		assertThat(roundsEnd.size()).isEqualTo(roundsStart.size()-1);
	}	
}
