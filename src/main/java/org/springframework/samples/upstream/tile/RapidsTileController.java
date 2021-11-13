package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rapidsTiles")
public class RapidsTileController {
	
	@Autowired
	RapidsTileService rapidsTileService;
	
	@GetMapping()
	public String listadoRapidsTile(ModelMap modelMap) {
		String vista="rapidsTiles/listadoRapidsTiles";
		Iterable<RapidsTile> rapidsTiles=rapidsTileService.findAllRapidsTile();
		modelMap.addAttribute("rapidsTiles", rapidsTiles);
		return vista;
	}
	
}
