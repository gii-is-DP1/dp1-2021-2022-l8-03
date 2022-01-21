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
import org.springframework.samples.upstream.piece.Color;
import org.springframework.samples.upstream.player.exceptions.InvalidPlayerEditException;
import org.springframework.samples.upstream.player.exceptions.NoPermissionException;
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
	
	public Color getPlayerColor(Integer id) {
		return playerRepository.getPlayerColor(id);
	}

	@Transactional(readOnly = true)
	public Collection<Player> findPlayerByLastName(String lastName) throws DataAccessException {
		return playerRepository.findByLastName(lastName);
	}
	
	@Transactional(readOnly = true)
    public Page<Player> findPlayerByLastNamePageable(String lastName, Pageable pageable) throws DataAccessException, NoPermissionException {
       
        if(checkAdmin()) {
        	return playerRepository.findByLastNamePageable(lastName, pageable);
		}else {
			throw new NoPermissionException();
		}
    }
	
	@Transactional(readOnly = true)
	public Player findPlayerByUsername(String username) throws DataAccessException {
		return playerRepository.findByUsername(username);
	}
	
	@Transactional(readOnly = true)
	public Collection<Object> auditByUsername(String username) throws DataAccessException, NoPermissionException {
		if(checkAdmin()) {
			return playerRepository.auditByUsername(username);
		}else {
			throw new NoPermissionException();
		}		
	}

	@Transactional
	public void savePlayer(Player player) throws DataAccessException {
		String username = findPlayerById(player.getId()).getUser().getUsername();
		if(checkAdminAndInitiatedUser(username)) {
			
			playerRepository.save(player);		
			
			userService.saveUser(player.getUser());
			
			authoritiesService.saveAuthorities(player.getUser().getUsername(), "player");
		}		
	}	
	
	@Transactional
	public void editPlayer(Player player) throws DataAccessException, InvalidPlayerEditException {
		String username = findPlayerById(player.getId()).getUser().getUsername();
		if(checkAdminAndInitiatedUser(username)) {
			
			playerRepository.save(player);		
			
			userService.saveUser(player.getUser());
			
			authoritiesService.saveAuthorities(player.getUser().getUsername(), "player");
		}else {
			throw new InvalidPlayerEditException();
		}
	}	
	
	@Transactional(readOnly = true)
    public Collection<Player> findAll() throws DataAccessException {
        return playerRepository.findAll();
    }
	
	@Transactional(readOnly = true)
    public Page<Player> findAllPageable(Pageable pageable) throws DataAccessException, NoPermissionException {
		if(checkAdmin()) {
			return playerRepository.findAllPageable(pageable);
		}else {
			throw new NoPermissionException();
		}       
    }
	
	@Transactional
	public void saveNewPlayer(Player player) throws DataAccessException {
			
			playerRepository.save(player);		
			
			userService.saveUser(player.getUser());
			
			authoritiesService.saveAuthorities(player.getUser().getUsername(), "player");		
	}	
	
	@Transactional
	public void delete(Player player) throws DataAccessException, NoPermissionException {
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
		}else {
			throw new NoPermissionException();
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
