package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RockTileService {
	@Autowired
	private RockTileRepository rockTileRepo;
	
	@Transactional
	public int rockTileCount() {
		return (int) rockTileRepo.count();
	}
	
	@Transactional
	public Iterable<RockTile> findAllRockTiles(){
		return rockTileRepo.findAll();
	}
}
