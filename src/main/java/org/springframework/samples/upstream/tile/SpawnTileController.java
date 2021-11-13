package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/spawnTiles")
public class SpawnTileController {
	@Autowired
	SpawnTileService spawnTileService;
	
	@GetMapping
	public String listadoSpawnTile(ModelMap modelMap) {
		String vista="spawnTiles/listadoSpawnTiles";
		Iterable<SpawnTile> spawnTiles=spawnTileService.findAllSpawnTiles();
		modelMap.addAttribute("spawnTiles", spawnTiles);
		return vista;
	}

}
