package org.springframework.samples.petclinic.round;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
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
	public String processFindForm(ModelMap model) {
		String vista = "rounds/roundList";
		Iterable<Round> rounds = roundService.findAll();
		model.addAttribute("rounds", rounds);
		return vista;
	}
	
	@GetMapping(value = "/rounds/{roundId}/edit")
	public String initUpdateRoundForm(@PathVariable("roundId") int roundId, Model model) {
		Round round = this.roundService.findRoundById(roundId);
		model.addAttribute(round);
		return VIEWS_ROUND_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/rounds/{roundId}/edit")
	public String processUpdateRoundForm(@Valid Round round, BindingResult result,
			@PathVariable("roundId") int roundId) {
		if (result.hasErrors()) {
			return VIEWS_ROUND_CREATE_OR_UPDATE_FORM;
		}
		else {
			round.setId(roundId);
			this.roundService.saveRound(round);
			return "redirect:/rounds";
		}
	}

	/**
	 * Custom handler for displaying an owner.
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/rounds/{roundId}")
	public ModelAndView showRound(@PathVariable("roundId") int roundId) {
		ModelAndView mav = new ModelAndView("rounds/roundDetails");
		mav.addObject(this.roundService.findRoundById(roundId));
		return mav;
	}
}
