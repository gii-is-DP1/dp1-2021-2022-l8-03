package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tiles")
public class TileController {
	
	@Autowired
	TileService tileService;
	
	@GetMapping()
	public String listadoTile(ModelMap modelMap) {
		String vista="tiles/listadoTiles";
		Iterable<Tile> tiles=tileService.findAllTiles();
		modelMap.addAttribute("tiles", tiles);
		return vista;
	}
	
}
