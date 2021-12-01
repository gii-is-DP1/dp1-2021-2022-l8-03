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

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.upstream.user.AuthoritiesService;
import org.springframework.samples.upstream.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class PlayerController {

	private static final String VIEWS_PLAYER_CREATE_OR_UPDATE_FORM = "players/createOrUpdatePlayerForm";

	private final PlayerService playerService;

	@Autowired
	public PlayerController(PlayerService playerService, UserService userService, AuthoritiesService authoritiesService) {
		this.playerService = playerService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/players/new")
	public String initCreationForm(Map<String, Object> model) {
		Player player = new Player();
		model.put("player", player);
		return VIEWS_PLAYER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/players/new")
	public String processCreationForm(@Valid Player player, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_PLAYER_CREATE_OR_UPDATE_FORM;
		}
		else {
			//creating player, user and authorities
			this.playerService.saveNewPlayer(player);
			
			return "redirect:/players/" + player.getId();
		}
	}

	@GetMapping(value = "/players/find")
	public String initFindForm(Map<String, Object> model) {
		Boolean admin = this.playerService.checkAdmin();
		if(!admin) {
			return "exception";
		}
		model.put("player", new Player());
		return "players/findPlayers";
	}

	@GetMapping(value = "/players")
	public String processFindForm(Player player, BindingResult result, Map<String, Object> model,Pageable pageable) {
		Boolean admin = this.playerService.checkAdmin();
		if(!admin) {
			return "exception";
		}
		// allow parameterless GET request for /players to return all records
		if (player.getLastName() == null) {
			player.setLastName(""); // empty string signifies broadest possible search
		}
		
		Page<Player> results=null;
		
		if(player.getLastName()=="") {
			results = this.playerService.findAllPageable(pageable);
		}else {
			results = this.playerService.findPlayerByLastNamePageable(player.getLastName(), pageable);
		}
		
        boolean esPrimera=results.isFirst();
        boolean esUltima=results.isLast();
        // find players by last name
		if (results.isEmpty()) {
			// no players found
			result.rejectValue("lastName", "notFound", "not found");
			return "players/findPlayers";
		}
		
		else if (results.getTotalElements() == 1) {
			// 1 player found
			player = results.iterator().next();
			return "redirect:/players/" + player.getId();
		}
		else {
			// multiple players found
			model.put("selections", results);
			model.put("esPrimera",!esPrimera);
			model.put("esUltima",!esUltima);
			return "players/playersList";
		}
	}

	@GetMapping(value = "/players/{playerId}/edit")
	public String initUpdatePlayerForm(@PathVariable("playerId") int playerId, Model model) {
		Player player = this.playerService.findPlayerById(playerId);
		String username = player.getUser().getUsername();
		Boolean permission = !this.playerService.checkAdminAndInitiatedUser(username);
		if(permission) {
			return "exception";
		} else {
			model.addAttribute(player);
			return VIEWS_PLAYER_CREATE_OR_UPDATE_FORM;
		}
		
	}
	
	

	@PostMapping(value = "/players/{playerId}/edit")
	public String processUpdatePlayerForm(@Valid Player player, BindingResult result,
			@PathVariable("playerId") int playerId) {
		if (result.hasErrors()) {
			return VIEWS_PLAYER_CREATE_OR_UPDATE_FORM;
		}
		else {
			player.setId(playerId);
			this.playerService.savePlayer(player);
			return "redirect:/players/{playerId}";
			
		}
	}
	
	@GetMapping(value = "/players/delete/{playerId}")
	public String deletePlayer(@PathVariable("playerId") int playerId, ModelMap model) {
		String view = "/players/playersList";
		Player player = this.playerService.findPlayerById(playerId);
		if(!player.equals(null)) {
			this.playerService.delete(player);
			model.addAttribute("message","Player successfully deleted");
		}
		else {
			model.addAttribute("message", "Player not found");
		}
		return view;
	}
	
	/**
	 * Custom handler for displaying an player.
	 * @param playerId the ID of the player to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/players/{playerId}")
	public ModelAndView showPlayer(@PathVariable("playerId") int playerId) {
		ModelAndView mav = new ModelAndView("players/playerDetails");
		Player player = this.playerService.findPlayerById(playerId);
		String username = player.getUser().getUsername();
		Boolean permission = !this.playerService.checkAdminAndInitiatedUser(username);
		if(permission) {
			ModelAndView exception = new ModelAndView("exception");
			return exception;
		}
		mav.addObject(player);
		mav.addObject("permission", !permission);
		return mav;
	}
	
	@GetMapping("/players/playerDetails")
	public String showPlayerDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User)authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		int playerId = playerService.findPlayerByUsername(currentUsername).getId();
		return "redirect:/players/" + playerId;
	}

}
