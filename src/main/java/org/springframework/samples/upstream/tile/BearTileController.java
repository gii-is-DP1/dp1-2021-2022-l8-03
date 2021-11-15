package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bearTiles")
public class BearTileController {
	
	@Autowired
	BearTileService bearTileService;
	
	@GetMapping()
	public String listadoRapidsTile(ModelMap modelMap) {
		String vista="bearTiles/listadoBearTiles";
		Iterable<BearTile> bearTiles=bearTileService.findAllBearTile();
		modelMap.addAttribute("bearTiles", bearTiles);
		return vista;
	}

}
