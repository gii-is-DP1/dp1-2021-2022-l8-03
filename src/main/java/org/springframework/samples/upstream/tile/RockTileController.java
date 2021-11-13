package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rockTiles")
public class RockTileController {
	@Autowired
	RockTileService rockTileService;
	
	@GetMapping()
	public String listadoRockTile(ModelMap modelMap) {
		String vista="rockTiles/listadoRockTiles";
		Iterable<RockTile> rockTiles=rockTileService.findAllRockTiles();
		modelMap.addAttribute("rockTiles", rockTiles);
		return vista;
	}

}
