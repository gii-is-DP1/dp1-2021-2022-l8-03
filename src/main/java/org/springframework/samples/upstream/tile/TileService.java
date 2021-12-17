package org.springframework.samples.upstream.tile;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.upstream.round.Round;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TileService {
	@Autowired
	private TileRepository tileRepository;

	@Autowired
	private TileService tileService;

	@Autowired
	public TileService(TileRepository tileRepository) {
		this.tileRepository = tileRepository;
	}	

	@Transactional(readOnly = true)
	public Tile findTileById(int id) throws DataAccessException {
		return tileRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Tile findByPosition(int row, int column) throws DataAccessException {
		return tileRepository.findByPosition(row, column);
	}
	
	@Transactional(readOnly = true)
	public List<Tile> findTilesInRound(int roundId) throws DataAccessException {
		return tileRepository.findTilesInRound(roundId);
	}
	
	@Transactional(readOnly = true)
	public List<Tile> findSeaTilesInRound(int roundId) throws DataAccessException {
		return tileRepository.findSeaTilesInRound(roundId);
	}
	
	public void saveTile(Tile tile) throws DataAccessException {
		this.tileRepository.save(tile);
	}
	
	public void createSeaTiles(Round round) {
		for(Integer i=1;i<5;i++) {
			Tile seaTile=new Tile();
			seaTile.setOrientation(0);
			if(i==4) {
				seaTile.setColumnIndex(2);
				seaTile.setRowIndex(2);
			}else {
				seaTile.setColumnIndex(i);
				seaTile.setRowIndex(1);
			}
			seaTile.setSalmonEggs(0);
			seaTile.setTileType(TileType.SEA);
			seaTile.setRound(round);
			this.tileService.saveTile(seaTile);
		}
	}
	
}
