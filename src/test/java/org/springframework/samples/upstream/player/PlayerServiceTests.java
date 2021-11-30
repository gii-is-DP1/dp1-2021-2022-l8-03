/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.upstream.player;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.upstream.user.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided
 * by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning that we
 * don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * PlayerServiceTests#clinicService clinicService}</code> instance variable, which uses
 * autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is executed in
 * its own transaction, which is automatically rolled back by default. Thus, even if tests
 * insert or otherwise change database state, there is no need for a teardown or cleanup
 * script.
 * <li>An {@link org.springframework.context.ApplicationContext ApplicationContext} is
 * also inherited and can be used for explicit bean lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class PlayerServiceTests {                
        @Autowired
	protected PlayerService playerService;
        
    private Validator createValidator() {
    	LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
    	localValidatorFactoryBean.afterPropertiesSet();
    	return localValidatorFactoryBean;
    }

	@Test
	void shouldFindPlayersByLastName() {
		Collection<Player> players = this.playerService.findPlayerByLastName("Davis");
		assertThat(players.size()).isEqualTo(2);
	}
	
	@Test
	void shouldNotFindPlayersByLastName() {
		Collection<Player> players = this.playerService.findPlayerByLastName("Daviss");
		assertThat(players.isEmpty()).isTrue();
	}
	
	@Test
	void shouldFindPlayerById() {
		Player player = this.playerService.findPlayerById(1);
		assertThat(player.getUser().getUsername()).isEqualTo("player1");
	}
	
	@Test
	void shouldNotFindPlayerById() {
		Player player = this.playerService.findPlayerById(250);
		assertThat(player).isEqualTo(null);
	}
	
	@Test
	void shouldFindPlayerByUsername() {
		Player player = this.playerService.findPlayerByUsername("player1");
		assertThat(player.getUser().getUsername()).isEqualTo("player1");
	}
	
	@Test
	void shouldNotFindPlayerByUsername() {
		Player player = this.playerService.findPlayerByUsername("fekir");
		assertThat(player).isEqualTo(null);
	}
	
	@Test
	@Transactional
	@WithMockUser(authorities = "admin")
	public void shouldDeletePlayer() {               
        Player playerToDelete = playerService.findPlayerByUsername("cardelbec");
        assertThat(playerToDelete).isNotEqualTo(null);
        
        playerService.delete(playerToDelete);     

        Player deletedPlayer = playerService.findPlayerByUsername("cardelbec");
        assertThat(deletedPlayer).isEqualTo(null);
	}
	
	@Test
	@Transactional
	@WithMockUser(authorities = "player")
	public void shouldNotDeletePlayer() {
		Player playerToDelete = playerService.findPlayerByUsername("manlopalm");
		assertThat(playerToDelete).isNotEqualTo(null);
		
		playerService.delete(playerToDelete);
		
		Player deletedPlayer = playerService.findPlayerByUsername("manlopalm");
        assertThat(deletedPlayer).isNotEqualTo(null);
		
	}

	@Test
	@Transactional
	public void shouldInsertPlayer() {
		Collection<Player> players = this.playerService.findPlayerByLastName("Schultz");
		int found = players.size();

		Player player = new Player();
		player.setFirstName("Sam");
		player.setLastName("Schultz");
		player.setEmail("ejemplo@gmail.com");
                User user=new User();
                user.setUsername("Sam");
                user.setPassword("supersecretpassword");
                user.setEnabled(true);
                player.setUser(user);                
                
		this.playerService.saveNewPlayer(player);
		assertThat(player.getId().longValue()).isNotEqualTo(0);

		players = this.playerService.findPlayerByLastName("Schultz");
		assertThat(players.size()).isEqualTo(found + 1);
	}
	
	@Test
	@Transactional
	public void shouldNotInsertPlayer() {

		Player player = new Player();
		player.setFirstName("Sam");
		player.setLastName("Schultz");
		player.setEmail("");
		Validator validator = createValidator();
		Set<ConstraintViolation<Player>> constraintViolations = validator.validate(player);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Player> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
		assertThat(violation.getMessage()).isEqualTo("no puede estar vacío");

	}
	

//H14
	@Test
	@Transactional
	@WithMockUser(authorities = "admin")
	public void shouldInsertPlayerBeingAdmin() {
		Collection<Player> players = this.playerService.findPlayerByLastName("Swift");
		int found = players.size();

		Player player = new Player();
		player.setFirstName("Jonathan");
		player.setLastName("Swift");
		player.setEmail("jonas@gmail.com");
                User user=new User();
                user.setUsername("Jonathan");
                user.setPassword("1234");
                user.setEnabled(true);
                player.setUser(user);                
                
		this.playerService.saveNewPlayer(player);
		assertThat(player.getId().longValue()).isNotEqualTo(0);

		players = this.playerService.findPlayerByLastName("Swift");
		assertThat(players.size()).isEqualTo(found + 1);
	}

	@Test
	@Transactional
	@WithMockUser(username = "player1")
	void shouldUpdatePlayer() {
		Player player = this.playerService.findPlayerById(1);
		String oldLastName = player.getLastName();
		String newLastName = oldLastName + "X";
		
		
		player.setLastName(newLastName);
		this.playerService.savePlayer(player);

		// retrieving new name from database
		player = this.playerService.findPlayerById(1);
		assertThat(player.getLastName()).isEqualTo(newLastName);
	}
	
	@Test
	@Transactional
	@WithMockUser(authorities = "player")
	void shouldNotUpdatePlayer() {
		Player player = this.playerService.findPlayerById(1);
		String oldLastName = player.getLastName();
		String newLastName = oldLastName + "X";
		
		
		player.setLastName(newLastName);
		this.playerService.savePlayer(player);

		// retrieving new name from database
		player = this.playerService.findPlayerById(1);
		assertThat(player.getLastName()).isEqualTo(newLastName);
	}

	
//H14
	@Test
	@Transactional
	@WithMockUser(authorities = "admin")
	void shouldUpdatePlayerBeingAdmin() {
		Player player = this.playerService.findPlayerById(1);
		String oldLastName = player.getLastName();
		String newLastName = oldLastName + "X";
		
		
		player.setLastName(newLastName);
		this.playerService.savePlayer(player);

		// retrieving new name from database
		player = this.playerService.findPlayerById(1);
		assertThat(player.getLastName()).isEqualTo(newLastName);
	}


}
