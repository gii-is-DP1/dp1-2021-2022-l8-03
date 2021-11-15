package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/heronTiles")
public class HeronTileController {
	
	@Autowired
	HeronTileService heronTileService;
	
	@GetMapping()
	public String listadoHeronTile(ModelMap modelMap) {
		String vista="heronTiles/listadoHeronTiles";
		Iterable<HeronTile> heronTiles=heronTileService.findAllHeronTile();
		modelMap.addAttribute("heronTiles", heronTiles);
		return vista;
	}
}
