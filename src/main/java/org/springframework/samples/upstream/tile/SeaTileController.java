package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seaTiles")
public class SeaTileController {
	@Autowired
	SeaTileService seaTileService;
	
	@GetMapping()
	public String listadoSeaTile(ModelMap modelMap) {
		String vista="seaTiles/listadoSeaTiles";
		Iterable<SeaTile> seaTiles=seaTileService.findAllSeaTiles();
		modelMap.addAttribute("seaTiles", seaTiles);
		return vista;
	}

}
