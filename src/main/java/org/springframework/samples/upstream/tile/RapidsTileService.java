package org.springframework.samples.upstream.tile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RapidsTileService {
	@Autowired
	private RapidsTileRepository rapidsTileRepo;
	
	@Transactional
	public int rapidsTileCount() {
		return (int) rapidsTileRepo.count();
	}
	
	@Transactional
	public Iterable<RapidsTile> findAllRapidsTile(){
		return rapidsTileRepo.findAll();
	}
}
