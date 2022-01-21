package org.springframework.samples.upstream.round;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.springframework.dao.DataAccessException;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.player.PlayerService;
import org.springframework.samples.upstream.player.exceptions.NoPermissionException;
import org.springframework.samples.upstream.round.exceptions.FullRoundException;
import org.springframework.samples.upstream.round.exceptions.InvalidRoundException;
import org.springframework.samples.upstream.round.exceptions.NotYourRoundException;
import org.springframework.samples.upstream.round.exceptions.PlayerOtherRoundException;
import org.springframework.security.test.context.support.WithMockUser;
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
	@WithMockUser(authorities = ("admin"))
	void shouldFindInCourse() throws DataAccessException, NoPermissionException {
		Collection<Round> rounds = this.roundService.findInCourseRounds();
		assertThat(rounds.size()).isEqualTo(4);
	}
	
	@Test
	@WithMockUser(authorities = ("player"))
	void shouldNotFindInCourse() throws DataAccessException, NoPermissionException {
		assertThrows(NoPermissionException.class, ()->this.roundService.findInCourseRounds());
	}
	
	@Test
	@WithMockUser(authorities = ("admin"))
	void shouldFindFinished() throws DataAccessException, NoPermissionException {
		Collection<Round> rounds = this.roundService.findFinishedRounds();
		assertThat(rounds.size()).isEqualTo(1);
	}
	
	@Test
	@WithMockUser(authorities = ("player"))
	void shouldNotFindFinished() throws DataAccessException, NoPermissionException {
		assertThrows(NoPermissionException.class, ()->this.roundService.findFinishedRounds());
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
	
	@Test
	void shouldCheckRoundExist() throws InvalidRoundException {
		List<Player> players=new ArrayList<Player>();
		Round round=new Round();
		round.setPlayer(this.playerService.findPlayerByUsername("mandommag"));
		round.setWhirlpools(true);
		round.setRapids(true);
		round.setNum_players(3);
		round.setPlayers(players);
		this.roundService.checkRoundExist(round);
	}
	
	@Test
	void shouldNotCheckRoundExist() {
		assertThrows(InvalidRoundException.class, ()->this.roundService.checkRoundExist(null));
	}
	
	@Test
	void shouldCheckRoundCapacity () throws FullRoundException {
		List<Player> players=new ArrayList<Player>();
		players.add(this.playerService.findPlayerByUsername("player1"));
		players.add(this.playerService.findPlayerByUsername("mandommag"));
		Round round=new Round();
		round.setPlayer(this.playerService.findPlayerByUsername("mandommag"));
		round.setWhirlpools(true);
		round.setRapids(true);
		round.setNum_players(3);
		round.setPlayers(players);
		this.roundService.checkRoundCapacity(round);
	}
	
	@Test
	void shouldNotCheckRoundCapacity ()throws FullRoundException{
		List<Player> players=new ArrayList<Player>();
		players.add(this.playerService.findPlayerByUsername("player1"));
		players.add(this.playerService.findPlayerByUsername("mandommag"));
		players.add(this.playerService.findPlayerByUsername("celhersot"));
		Round round=new Round();
		round.setPlayer(this.playerService.findPlayerByUsername("mandommag"));
		round.setWhirlpools(true);
		round.setRapids(true);
		round.setNum_players(3);
		round.setPlayers(players);
		
		assertThrows(FullRoundException.class, ()->this.roundService.checkRoundCapacity(round));
	}
	
	@Test
	void shouldCheckPlayerInRound () throws DataAccessException, NotYourRoundException {
		List<Player> players=new ArrayList<Player>();
		players.add(this.playerService.findPlayerByUsername("mandommag"));
		Round round=new Round();
		round.setPlayer(this.playerService.findPlayerByUsername("mandommag"));
		round.setWhirlpools(true);
		round.setRapids(true);
		round.setNum_players(3);
		round.setPlayers(players);
		this.roundService.checkPlayerInRound(round,this.playerService.findPlayerByUsername("mandommag"));
	}
	
	@Test
	void shouldNotCheckPlayerInRound () throws NotYourRoundException {
		List<Player> players=new ArrayList<Player>();
		players.add(this.playerService.findPlayerByUsername("player1"));
		players.add(this.playerService.findPlayerByUsername("mandommag"));
		Round round=new Round();
		round.setPlayer(this.playerService.findPlayerByUsername("mandommag"));
		round.setWhirlpools(true);
		round.setRapids(true);
		round.setNum_players(3);
		round.setPlayers(players);
		assertThrows(NotYourRoundException.class, ()->this.roundService.checkPlayerInRound(round,this.playerService.findPlayerByUsername("celhersot")));
	}
	
	@Test
	void shouldCheckPlayerInRound2 () throws DataAccessException, PlayerOtherRoundException {
		List<Player> players=new ArrayList<Player>();
		players.add(this.playerService.findPlayerByUsername("mandommag"));
		Round round=new Round();
		round.setPlayer(this.playerService.findPlayerByUsername("mandommag"));
		round.setWhirlpools(true);
		round.setRapids(true);
		round.setNum_players(3);
		round.setPlayers(players);
		this.roundService.checkPlayerInRound(this.playerService.findPlayerByUsername("mandommag"));
	}
	
	@Test
	@WithMockUser(username="mandommag")
	void shouldNotCheckPlayerInRound2 () throws PlayerOtherRoundException {
		List<Player> players=new ArrayList<Player>();
		players.add(this.playerService.findPlayerByUsername("player1"));
		players.add(this.playerService.findPlayerByUsername("mandommag"));
		Round round=new Round();
		round.setPlayer(this.playerService.findPlayerByUsername("mandommag"));
		round.setWhirlpools(true);
		round.setRapids(true);
		round.setNum_players(3);
		round.setPlayers(players);
		this.roundService.saveRound(round);
		
		Player player=this.playerService.findPlayerByUsername("mandommag");
		player.setRound(round);
		this.playerService.savePlayer(player);
		
		assertThrows(PlayerOtherRoundException.class, ()->this.roundService.checkPlayerInRound(player));
	}
	
}
