package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/waterfallTiles")
public class WaterfallTileController {

	@Autowired
	WaterfallTileService waterfallTileService;
	
	@GetMapping()
	public String listadoWaterfallTile(ModelMap modelMap) {
		String vista="waterfallTiles/listadoWaterfallTiles";
		Iterable<WaterfallTile> waterfallTiles=waterfallTileService.findAllWaterfallTile();
		modelMap.addAttribute("waterfallTiles", waterfallTiles);
		return vista;
	}
}
