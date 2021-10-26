package org.springframework.samples.petclinic.round;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RoundController {
	
	private static final String VIEWS_ROUND_CREATE_OR_UPDATE_FORM = "rounds/createOrUpdateRoundForm";
	
	private final RoundService roundService;
	
	@Autowired
	public RoundController(RoundService roundService) {
		this.roundService = roundService;
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
		if(result.hasErrors()) {
			return VIEWS_ROUND_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.roundService.saveRound(round);
			
			return "redirect:/rounds/" + round.getId();
		}
	}
	
	@GetMapping(value = "/rounds")
	public String processFindForm(Map<String, Object> model) {
		String vista = "rounds/roundList";
		Iterable<Round> rounds = roundService.findAll();
		model.put("rounds", rounds);
		return vista;
	}
}
