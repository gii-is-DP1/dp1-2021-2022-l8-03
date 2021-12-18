package org.springframework.samples.upstream.round;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.player.PlayerService;
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
public class RoundController {
	
	private static final String VIEWS_ROUND_CREATE_OR_UPDATE_FORM = "rounds/createOrUpdateRoundForm";
	
	private final RoundService roundService;
	private PlayerService playerService;

	
	@Autowired
	public RoundController(RoundService roundService, PlayerService playerService) {
		this.roundService = roundService;
		this.playerService = playerService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value="/rounds/new")
	public String initCreationForm(Map<String, Object> model) {
		Round round = new Round();
		model.put("round", round);
		return VIEWS_ROUND_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value="/rounds/new")
	public String processCreationForm(@Valid Round round, BindingResult result) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User)authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		Player player=playerService.findPlayerByUsername(currentUsername);
		if(result.hasErrors()) {
			return VIEWS_ROUND_CREATE_OR_UPDATE_FORM;
		}
		else {
			round.setPlayer(player);
			round.setRound_state(RoundState.CREATED);
			Collection<Player> players=new ArrayList<Player>();
			players.add(player);
			round.setPlayers(players);
			this.roundService.saveRound(round);
			player.setRound(round);
			this.playerService.savePlayer(player);
			return "redirect:/rounds/";
		}
	}
	
	@GetMapping(value = "/rounds")
    public String processFindForm(ModelMap model) {
        String vista = "rounds/roundList";
        Iterable<Round> rounds = roundService.findCreatedRounds();
        boolean esFinished=false;
        model.addAttribute("rounds", rounds);
        model.addAttribute("esFinished",esFinished);
        return vista;
    }

    @GetMapping(value = "/rounds/inCourse")
    public String processFindInCourse(ModelMap model) {
    	Boolean admin = this.playerService.checkAdmin();
		if(!admin) {
			return "exception";
		}
    	
        String vista = "rounds/roundList";
        Iterable<Round> rounds = roundService.findInCourseRounds();
        boolean esFinished=false;
        model.addAttribute("rounds", rounds);
        model.addAttribute("esFinished",esFinished);
        return vista;
    }

    @GetMapping(value = "/rounds/finished")
    public String processFindfinished(ModelMap model) {
    	Boolean admin = this.playerService.checkAdmin();
		if(!admin) {
			return "exception";
		}
        String vista = "rounds/roundList";
        Iterable<Round> rounds = roundService.findFinishedRounds();
        boolean esFinished=true;
        model.addAttribute("rounds", rounds);
        model.addAttribute("esFinished",esFinished);
        return vista;
    }
	
	@GetMapping(value = "/rounds/{roundId}/edit")
	public String initUpdateRoundForm(@PathVariable("roundId") int roundId, Model model) {
		Round round = this.roundService.findRoundById(roundId);
		model.addAttribute(round);
		return VIEWS_ROUND_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/rounds/{roundId}/edit")
	public String processUpdateRoundForm(@Valid Round round, BindingResult result,Player player,
			@PathVariable("roundId") int roundId,ModelMap model) {
		if (result.hasErrors()) {
			model.put("round",round);
			return VIEWS_ROUND_CREATE_OR_UPDATE_FORM;
		}
		else {
			Round roundToUpdate=this.roundService.findRoundById(roundId);
			BeanUtils.copyProperties(round, roundToUpdate,"id","player");
			this.roundService.saveRound(round);
			return "redirect:/rounds";
		}
	}
	
	@GetMapping(value = "/rounds/join/{roundId}")
	public String joinRound(@PathVariable("roundId") int roundId, ModelMap model) {
		Round round=this.roundService.findRoundById(roundId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User)authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		Player player=playerService.findPlayerByUsername(currentUsername);
		if(round!=null && round.getPlayers().size()<round.getNum_players()) {
			player.setRound(round);
			this.playerService.savePlayer(player);
			Collection<Player> players=round.getPlayers();
			players.add(player);
			round.setPlayers(players);
			this.roundService.saveRound(round);
			return "redirect:/rounds/{roundId}";
		}
		else {
			return "redirect:/rounds/oups";
		}
		
	}
	
	@GetMapping(value = "/rounds/leave/{roundId}")
	public String leaveRound(@PathVariable("roundId") int roundId, ModelMap model) {
		Round round=this.roundService.findRoundById(roundId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User)authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		Player player=playerService.findPlayerByUsername(currentUsername);
		Collection<Player> players=round.getPlayers();
		if(round!=null && players.contains(player)) {
//			if(player==round.getPlayer()) {
//				this.roundService.
//			}
			player.setRound(null);
			this.playerService.savePlayer(player);
			players.remove(player);
			round.setPlayers(players);
			this.roundService.saveRound(round);
			return "redirect:/rounds";
		}
		else {
			return "redirect:/rounds/oups";
		}
		
	}
	
	@GetMapping("/rounds/{roundId}")
	public ModelAndView showRound(@PathVariable("roundId") int roundId) {
		ModelAndView mav = new ModelAndView("rounds/roundDetails");
		mav.addObject(this.roundService.findRoundById(roundId));
		return mav;
	}
	
}
