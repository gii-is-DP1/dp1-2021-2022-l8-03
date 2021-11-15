package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BearTileService {
	@Autowired
	private BearTileRepository bearTileRepo;
	
	@Transactional
	public int bearTileCount() {
		return (int) bearTileRepo.count();
	}
	
	@Transactional
	public Iterable<BearTile> findAllBearTile(){
		return bearTileRepo.findAll();
	}
}
