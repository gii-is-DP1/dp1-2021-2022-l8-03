package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpawnTileService {
	@Autowired
	private SpawnTileRepository spawnTileRepo;
	
	@Transactional
	public int spawnTileCount() {
		return (int) spawnTileRepo.count();
	}
	
	@Transactional
	public Iterable<SpawnTile> findAllSpawnTiles(){
		return spawnTileRepo.findAll();
	}
}


