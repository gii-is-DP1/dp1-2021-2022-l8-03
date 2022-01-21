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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.upstream.piece.Color;
import org.springframework.samples.upstream.player.exceptions.InvalidPlayerEditException;
import org.springframework.samples.upstream.player.exceptions.NoPermissionException;
import org.springframework.samples.upstream.user.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class PlayerServiceTests {                
    @Autowired
	protected PlayerService playerService;
    
    
    private Player george;
    private User userGeorge;
    
    @BeforeEach
    void setUp(){
		george = new Player();
		userGeorge = new User();
		george.setId(1);
		george.setFirstName("George");
		george.setLastName("Franklin");
		george.setEmail("ejemplo@gmail.com");
		userGeorge.setUsername("player1");
		userGeorge.setPassword("0wn3r");
		george.setUser(userGeorge);
    }
        
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
	void shouldFindColor() {
		Color color = this.playerService.getPlayerColor(28);
		assertThat(color).isEqualTo(Color.YELLOW);
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
	@WithMockUser(authorities = "admin")
	void shouldFindPlayerByLastnamePageable() throws DataAccessException, NoPermissionException {
		Pageable pageable=PageRequest.of(0, 6);
		Page<Player> pageExpected=new PageImpl<Player>(List.copyOf(this.playerService.findPlayerByLastName("Dominguez")),pageable,0);
		Page<Player> page=this.playerService.findPlayerByLastNamePageable("Dominguez", pageable);
		assertThat(page).isEqualTo(pageExpected);
	}
	
	@Test
	@WithMockUser(authorities = "player")
	void shouldFindPlayerByLastNamePageableNoAuthority() throws DataAccessException, NoPermissionException {
		Pageable pageable=PageRequest.of(0, 6);
		assertThrows(NoPermissionException.class, () -> this.playerService.findPlayerByLastNamePageable("Dominguez", pageable));
	}
	
	@Test
	@WithMockUser(authorities = "admin")
	void shouldNotFindPlayerByLastnamePageable() throws DataAccessException, NoPermissionException {
		Pageable pageable=PageRequest.of(0, 6);
		Page<Player> pageExpected=new PageImpl<Player>(new ArrayList<Player>(),pageable,0);
		Page<Player> page=this.playerService.findPlayerByLastNamePageable("EsteApellidoNoExiste", pageable);
		assertThat(page).isEqualTo(pageExpected);
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
	@WithMockUser(username="player1")
	void shouldEditPlayerBeingThePlayer() throws DataAccessException, InvalidPlayerEditException {
		Player george=this.playerService.findPlayerByUsername("player1");
		george.setEmail("george@gmail.com");
                
		this.playerService.editPlayer(george);
		assertThat(this.playerService.findPlayerByUsername("player1").getEmail()).isEqualTo("george@gmail.com");
	}
	
	@Test
	@Transactional
	@WithMockUser(authorities="admin")
	void shouldEditPlayerBeingAdmin() throws DataAccessException, InvalidPlayerEditException {
		Player george=this.playerService.findPlayerByUsername("player1");
		george.setEmail("george@gmail.com");
                
		this.playerService.editPlayer(george);
		assertThat(this.playerService.findPlayerByUsername("player1").getEmail()).isEqualTo("george@gmail.com");
	}
	
	@Test
	@Transactional
	@WithMockUser(username="mandommag")
	void shouldNotEditPlayerBeingOtherPlayer() throws DataAccessException, InvalidPlayerEditException {
		Player george=this.playerService.findPlayerByUsername("player1");
		george.setEmail("george@gmail.com");
                
		assertThrows(InvalidPlayerEditException.class, () -> this.playerService.editPlayer(george));
	}
	
	@Test
	@Transactional
	void shouldNotSavePlayer() {
		Player george=this.playerService.findPlayerByUsername("player1");
		george.setEmail("");
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Player>> constraintViolations = validator.validate(george);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Player> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
		assertThat(violation.getMessage()).isEqualTo("no puede estar vacío");

	}
	
	@Test
	@Transactional
	void shouldSaveNewPlayer() {
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
	void shouldNotSaveNewPlayer() {

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

	@Test
	void shouldFindAll() {
		Collection<Player> allPlayers=this.playerService.findAll();
		assertThat(allPlayers.contains(this.playerService.findPlayerByUsername("mandommag"))).isEqualTo(true);
		assertThat(allPlayers.contains(this.playerService.findPlayerById(4))).isEqualTo(true);
	}
	
	@Test
	@WithMockUser(authorities = "admin")
	void shouldFindAllPageable() throws DataAccessException, NoPermissionException {
		Pageable pageable=PageRequest.of(0, 6);
		Page<Player> allPlayersFirstPage=this.playerService.findAllPageable(pageable);
		Pageable pageable2=allPlayersFirstPage.getPageable().next();
		Page<Player> allPlayersSecondPage=this.playerService.findAllPageable(pageable2);
		assertThat(allPlayersFirstPage.getPageable().getPageSize()).isEqualTo(6);
		assertThat(allPlayersFirstPage.getContent().contains(this.playerService.findPlayerById(4))).isEqualTo(true);
		assertThat(allPlayersSecondPage.getContent().contains(this.playerService.findPlayerByUsername("sonlaumot"))).isEqualTo(true);
	}
	
	@Test
	@WithMockUser(authorities = "player")
	void shouldFindAllPageableNoAuthority() throws DataAccessException, NoPermissionException {
		Pageable pageable=PageRequest.of(0, 6);
		assertThrows(NoPermissionException.class, () -> this.playerService.findAllPageable(pageable));
	}
	
	@Test
	@Transactional
	@WithMockUser(authorities = "admin")
	void shouldDeletePlayerRoundsEmpty() throws DataAccessException, NoPermissionException {               
        Player playerToDelete = playerService.findPlayerByUsername("player1");
        assertThat(playerToDelete).isNotEqualTo(null);
        
        playerService.delete(playerToDelete);     

        Player deletedPlayer = playerService.findPlayerByUsername("player1");
        assertThat(deletedPlayer).isEqualTo(null);
	}
	
	@Test
	@Transactional
	@WithMockUser(authorities = "admin")
	void shouldDeletePlayerRoundsNotEmpty() throws DataAccessException, NoPermissionException {               
        Player playerToDelete = playerService.findPlayerByUsername("celhersot");
        assertThat(playerToDelete).isNotEqualTo(null);
        
        playerService.delete(playerToDelete);     

        Player deletedPlayer = playerService.findPlayerByUsername("celhersot");
        assertThat(deletedPlayer).isEqualTo(null);
	}
	
	@Test
	@Transactional
	@WithMockUser(authorities = "player")
	void shouldNotDeletePlayer() throws DataAccessException, NoPermissionException {
		Player playerToDelete = playerService.findPlayerByUsername("manlopalm");
		
		assertThrows(NoPermissionException.class, () -> this.playerService.delete(playerToDelete));
	}

	@Test
	@Transactional
	@WithMockUser(authorities = "admin")
	void shouldInsertPlayerBeingAdmin() {
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
	@WithMockUser(username="mandommag")
	void shouldCheckAdminAndInitiatedUserBoolean() {
		Boolean result=this.playerService.checkAdminAndInitiatedUserBoolean("mandommag");
		assertThat(result).isEqualTo(true);
	}
	
	@Test
	@WithMockUser(username="admin1",authorities = "admin")
	void shouldCheckAdminAndInitiatedUserBooleanBeingAdmin() {
		Boolean result=this.playerService.checkAdminAndInitiatedUserBoolean("mandommag");
		assertThat(result).isEqualTo(true);
	}
	
	@Test
	@WithMockUser(username="player1")
	void shouldCheckAdminAndInitiatedUserBooleanBeingOtherUser() {
		Boolean result=this.playerService.checkAdminAndInitiatedUserBoolean("mandommag");
		assertThat(result).isEqualTo(false);
	}
	
	@Test
	@WithMockUser(authorities = "admin")
	void shouldCheckAdminBeingAdminBoolean() {
		Boolean result=this.playerService.checkAdminBoolean();
		assertThat(result).isEqualTo(true);
	}
	
	@Test
	@WithMockUser(authorities = "player")
	void shouldCheckAdminNotBeingAdminBoolean() {
		Boolean result=this.playerService.checkAdminBoolean();
		assertThat(result).isEqualTo(false);
	}
	
	@Test
	@WithMockUser(authorities = "admin")
	void shouldCheckAdmin() throws NoPermissionException{
		this.playerService.checkAdmin();
		
	}
	
	@Test
	@WithMockUser(authorities = "player")
	void shouldCheckAdminNotBeingAdmin() throws NoPermissionException{
		assertThrows(NoPermissionException.class, ()->this.playerService.checkAdmin());
		
	}
	
	@Test
	@WithMockUser(username="mandommag")
	void shouldCheckAdminAndInitiatedUser() throws NoPermissionException{
		this.playerService.checkAdminAndInitiatedUser("mandommag");
	}
	
	@Test
	@WithMockUser(username="admin1",authorities = "admin")
	void shouldCheckAdminAndInitiatedUserBeingAdmin() throws NoPermissionException{
		this.playerService.checkAdminAndInitiatedUser("mandommag");
	}
	
	@Test
	@WithMockUser(username="player1")
	void shouldCheckAdminAndInitiatedUserBeingOtherUser() throws NoPermissionException{
		assertThrows(NoPermissionException.class, ()->this.playerService.checkAdminAndInitiatedUser("mandommag"));
	}
	
}
