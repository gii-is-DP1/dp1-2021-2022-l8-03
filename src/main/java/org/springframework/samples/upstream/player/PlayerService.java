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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.round.RoundRepository;
import org.springframework.samples.upstream.round.RoundState;
import org.springframework.samples.upstream.salmonBoard.SalmonBoardRepository;
import org.springframework.samples.upstream.user.AuthoritiesService;
import org.springframework.samples.upstream.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Upstream controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class PlayerService {

	private PlayerRepository playerRepository;
	private RoundRepository roundRepository;
	private SalmonBoardRepository salmonboardRepository;
		
		

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public PlayerService(PlayerRepository playerRepository,RoundRepository roundRepository,SalmonBoardRepository salmonboardRepository) {
		this.playerRepository = playerRepository;
		this.roundRepository = roundRepository;
		this.salmonboardRepository=salmonboardRepository;
	}	

	@Transactional(readOnly = true)
	public Player findPlayerById(int id) throws DataAccessException {
		return playerRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Player> findPlayerByLastName(String lastName) throws DataAccessException {
		return playerRepository.findByLastName(lastName);
	}
	
	@Transactional(readOnly = true)
    public Page<Player> findPlayerByLastNamePageable(String lastName, Pageable pageable) throws DataAccessException {
        return playerRepository.findByLastNamePageable(lastName, pageable);
    }
	
	@Transactional(readOnly = true)
	public Player findPlayerByUsername(String username) throws DataAccessException {
		return playerRepository.findByUsername(username);
	}

	@Transactional
	public void savePlayer(Player player) throws DataAccessException {
		String username = player.getUser().getUsername();
		if(checkAdminAndInitiatedUser(username)) {
			//creating player
			playerRepository.save(player);		
			//creating user
			userService.saveUser(player.getUser());
			//creating authorities
			authoritiesService.saveAuthorities(player.getUser().getUsername(), "player");
		}
		
	}	
	
	@Transactional(readOnly = true)
    public Collection<Player> findAll() throws DataAccessException {
        return playerRepository.findAll();
    }
	
	@Transactional(readOnly = true)
    public Page<Player> findAllPageable(Pageable pageable) throws DataAccessException {
        return playerRepository.findAllPageable(pageable);
    }
	
	@Transactional
	public void saveNewPlayer(Player player) throws DataAccessException {
			//creating player
			playerRepository.save(player);		
			//creating user
			userService.saveUser(player.getUser());
			//creating authorities
			authoritiesService.saveAuthorities(player.getUser().getUsername(), "player");		
	}	
	
	@Transactional
	public void delete(Player player) throws DataAccessException {
		Collection<Round> rounds=this.roundRepository.findRoundByPlayerId(player.getId());
		if(checkAdmin()) {
			if(!rounds.isEmpty()) {
				for(Round r:rounds) {
					if(this.salmonboardRepository.findBoardInRound(r.getId())!=null) {
						this.salmonboardRepository.delete(this.salmonboardRepository.findBoardInRound(r.getId()));
					}
					if(r.getRound_state()!=RoundState.FINISHED) {
						for(Player p:r.getPlayers()) {
							p.setRound(null);
						}
					}
				}
			}
			this.playerRepository.delete(player);
		}
	}


  public Boolean checkAdminAndInitiatedUser(String username) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User)authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		if(username.equals(currentUsername)) {
			return true;
		}
		Collection<GrantedAuthority> authorities = currentUser.getAuthorities();
		for(GrantedAuthority g : authorities) {
			if(g.toString().equals("admin")) {
				return true;
			}
		}
		return false;
	}
  
  public Boolean checkAdmin() {
	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	  User currentUser = (User)authentication.getPrincipal(); 
	  Collection<GrantedAuthority> authorities = currentUser.getAuthorities();
	  for(GrantedAuthority g : authorities) {
		  if(g.toString().equals("admin")) {
			  return true;
		  }
	  }
	  return false;
  }

}
