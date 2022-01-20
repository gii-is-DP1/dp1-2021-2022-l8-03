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

import java.util.List;
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

	@GetMapping(value = "/players/find")
	public String initFindForm(Map<String, Object> model) {
		Boolean admin = this.playerService.checkAdmin();
		if(!admin) {
			return "noPermissionException";
		}
		model.put("player", new Player());
		return "players/findPlayers";
	}

	@GetMapping(value = "/players")
	public String processFindForm(Player player, BindingResult result, Map<String, Object> model,Pageable pageable) {
		Boolean admin = this.playerService.checkAdmin();
		if(!admin) {
			return "noPermissionException";
		}
		if (player.getLastName() == null) {
			player.setLastName(""); 
		}
		
		Page<Player> results=null;
		
		if(player.getLastName()=="") {
			results = this.playerService.findAllPageable(pageable);
		}else {
			results = this.playerService.findPlayerByLastNamePageable(player.getLastName(), pageable);
		}
		
        boolean esPrimera=results.isFirst();
        boolean esUltima=results.isLast();
		if (results.isEmpty()) {
			result.rejectValue("lastName", "notFound", "not found");
			return "players/findPlayers";
		}
		
		else if (results.getTotalElements() == 1) {
			player = results.iterator().next();
			return "redirect:/players/" + player.getId();
		}
		else {
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
			return "noPermissionException";
		} else {
			Boolean isNew=false;
			model.addAttribute(isNew);
			model.addAttribute(player);
			return VIEWS_PLAYER_CREATE_OR_UPDATE_FORM;
		}		
	}
	
	@PostMapping(value = "/players/{playerId}/edit")
	public String processUpdatePlayerForm(@Valid Player player, BindingResult result,
			@PathVariable("playerId") int playerId,Model model) {
		if (result.hasErrors()) {
			Boolean isNew=false;
			model.addAttribute(isNew);
			return VIEWS_PLAYER_CREATE_OR_UPDATE_FORM;
		}
		else {
			Player oldPlayer = this.playerService.findPlayerById(playerId);
			player.setId(playerId);
			player.getUser().setUsername(oldPlayer.getUser().getUsername());
			player.setPieces(oldPlayer.getPieces());
			player.setRound(oldPlayer.getRound());
			player.setScores(oldPlayer.getScores());
			this.playerService.savePlayer(player);
			return "redirect:/players/{playerId}";			
		}
	}
	
	@GetMapping(value = "/players/delete/{playerId}")
	public String deletePlayer(@PathVariable("playerId") int playerId, ModelMap model) {
		String view = "/players/playersList";
		Player player = this.playerService.findPlayerById(playerId);
		if(player!=null) {
			this.playerService.delete(player);
			model.addAttribute("message","Player successfully deleted");
		}
		else {
			model.addAttribute("message", "Player not found");
		}
		return view;
	}
	
	@GetMapping("/players/{playerId}")
	public ModelAndView showPlayer(@PathVariable("playerId") int playerId) {
		ModelAndView mav = new ModelAndView("players/playerDetails");
		Player player = this.playerService.findPlayerById(playerId);
		String username = player.getUser().getUsername();
		Boolean permission = !this.playerService.checkAdminAndInitiatedUser(username);
		Boolean admin = this.playerService.checkAdmin();
		if(permission) {
			ModelAndView exception = new ModelAndView("noPermissionException");
			return exception;
		}
		mav.addObject(player);
		mav.addObject("permission", !permission);
		mav.addObject("admin", admin);
		return mav;
	}
	
	@GetMapping("/players/{playerId}/audit")
	public String auditPlayerData(@PathVariable("playerId") int playerId, Map<String, Object> model) {
		Boolean admin = this.playerService.checkAdmin();
		if(!admin) {
			return "noPermissionException";
		}
		Player player = this.playerService.findPlayerById(playerId);
		List<Object> auditedData = (List<Object>) this.playerService.auditByUsername(player.getUser().getUsername());
		model.put("auditedData", auditedData);
		model.put("player", player);
		return "players/playerAudit";
	}
	
	@GetMapping("/players/playerDetails")
	public String showPlayerDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		int playerId = playerService.findPlayerByUsername(currentUsername).getId();
		return "redirect:/players/" + playerId;
	}

}
