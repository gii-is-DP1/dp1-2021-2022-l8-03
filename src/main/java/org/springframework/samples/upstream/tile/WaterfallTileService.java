package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WaterfallTileService {
	@Autowired
	private WaterfallTileRepository waterfallTileRepo;
	
	@Transactional
	public int waterfallTileCount() {
		return (int) waterfallTileRepo.count();
	}
	
	@Transactional
	public Iterable<WaterfallTile> findAllWaterfallTile(){
		return waterfallTileRepo.findAll();
	}
}
