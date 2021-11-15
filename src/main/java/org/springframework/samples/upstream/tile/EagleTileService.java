package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EagleTileService {
	@Autowired
	private EagleTileRepository eagleTileRepo;
	
	@Transactional
	public int eagleTileCount() {
		return (int) eagleTileRepo.count();
	}
	
	@Transactional
	public Iterable<EagleTile> findAllEagleTile(){
		return eagleTileRepo.findAll();
	}
}
