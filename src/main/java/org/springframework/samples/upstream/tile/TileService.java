package org.springframework.samples.upstream.tile;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TileService {
	@Autowired
	private TileRepository tileRepo;
	
	@Transactional
	public int tileCount() {
		return (int) tileRepo.count();
	}
	
	@Transactional
	public Iterable<Tile> findAllTiles(){
		return tileRepo.findAll();
	}
	

}
