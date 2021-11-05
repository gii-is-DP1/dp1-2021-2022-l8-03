package org.springframework.samples.petclinic.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/scores")
public class ScoreController {
	
	@Autowired
	ScoreService scoreService;
	
	@GetMapping()
	public String listadoScore(ModelMap modelMap) {
		String vista="scores/listadoScores";
		Iterable<Score> scores=scoreService.findAllScores();
		modelMap.addAttribute("scores", scores);
		return vista;
	}
	
}
