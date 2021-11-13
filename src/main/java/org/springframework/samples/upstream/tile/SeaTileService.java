package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeaTileService {
	@Autowired
	private SeaTileRepository seaTileRepo;
	
	@Transactional
	public int seaTileCount() {
		return (int) seaTileRepo.count();
	}
	
	@Transactional
	public Iterable<SeaTile> findAllSeaTiles(){
		return seaTileRepo.findAll();
	}
}
