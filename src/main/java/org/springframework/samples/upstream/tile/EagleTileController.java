package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/eagleTiles")
public class EagleTileController {
	
	@Autowired
	EagleTileService eagleTileService;
	
	@GetMapping()
	public String listadoEagleTile(ModelMap modelMap) {
		String vista="eagleTiles/listadoEagleTiles";
		Iterable<EagleTile> eagleTiles=eagleTileService.findAllEagleTile();
		modelMap.addAttribute("eagleTiles", eagleTiles);
		return vista;
	}
}
