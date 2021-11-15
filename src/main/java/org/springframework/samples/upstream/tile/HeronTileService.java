package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HeronTileService {
	@Autowired
	private HeronTileRepository heronTileRepo;
	
	@Transactional
	public int heronTileCount() {
		return (int) heronTileRepo.count();
	}
	
	@Transactional
	public Iterable<HeronTile> findAllHeronTile(){
		return heronTileRepo.findAll();
	}
}
