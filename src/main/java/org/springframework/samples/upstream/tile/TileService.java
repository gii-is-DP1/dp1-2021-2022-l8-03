package org.springframework.samples.upstream.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.upstream.piece.Piece;

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
	public void deleteTile(Tile tile) throws DataAccessException {
		tileRepository.delete(tile);
	}

	@Transactional(readOnly = true)
	public Tile findTileById(int id) throws DataAccessException {
		return tileRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Tile findByPosition(int row, int column, int round) throws DataAccessException {
		return tileRepository.findByPosition(row, column, round);
	}
	
	@Transactional(readOnly = true)
	public List<Tile> findByRow(int row, int round) throws DataAccessException {
		return tileRepository.findByRow(row, round);
	}
	
	@Transactional(readOnly = true)
	public List<Tile> findSeaTilesInRound(int roundId) throws DataAccessException {
		return tileRepository.findSeaTilesInRound(roundId);
	}
	
	@Transactional(readOnly = true)
	public List<Tile> findHeronTilesInRound(int roundId) throws DataAccessException {
		return tileRepository.findHeronTilesInRound(roundId);
	}
	
	@Transactional(readOnly = true)
	public List<Tile> findSpawnTilesInRound(int roundId) throws DataAccessException {
		return tileRepository.findSpawnTilesInRound(roundId);
	}
	
	@Transactional(readOnly = true)
	public Integer findLowestRow(int roundId) throws DataAccessException {
		return tileRepository.findLowestRow(roundId);
	}
	
	@Transactional(readOnly = true)
	public Integer findHighestRow(int roundId) throws DataAccessException {
		return tileRepository.findHighestRow(roundId);
	}
	
	@Transactional(readOnly = true)
	public List<Tile> findTilesInRound(int roundId) throws DataAccessException {
		return tileRepository.findTilesInRound(roundId);
	}
	
	public void saveTile(Tile tile) throws DataAccessException {
		this.tileRepository.save(tile);
	}
	
	public void removeStartingTiles(Integer round) {
		List<Tile> startingTiles = tileService.findSeaTilesInRound(round);
		for(Tile tile : startingTiles) {
			this.tileService.deleteTile(tile);
		}
	}
	
	public void removeLowestTiles(Integer round) {
		Integer lowestRow = tileService.findLowestRow(round);
		for(int i=1;i<4;i++) {
			if(i==2) {
				Tile tile = tileService.findByPosition(lowestRow+1, i, round);
				if(!tile.getTileType().equals(TileType.SPAWN)) {
					tileService.deleteTile(tile);
				}
			}else {
				Tile tile = tileService.findByPosition(lowestRow, i, round);
				if(!tile.getTileType().equals(TileType.SPAWN)) {
					tileService.deleteTile(tile);
				}
			}
		}
	}

	public void addNewRow(Round round) {
		Integer highestRow = tileService.findHighestRow(round.getId());
		for(int i=1;i<4;i++) {
			tileService.createRandomTile(highestRow + 1, i, round);		
		}
	}

	private void createRandomTile(int row, int column, Round round) {
		Tile tile = new Tile();
		tile.setPieces(new ArrayList<Piece>());
		tile.setRound(round);
		tile.setColumnIndex(column);
		tile.setRowIndex(row);
		tile.setSalmonEggs(0);
		tile.setOrientation(ThreadLocalRandom.current().nextInt(1, 7));
		if(round.getRapids()) {
			tile.setTileType(TileType.values()[ThreadLocalRandom.current().nextInt(0, 7)]);
		}else {
			tile.setTileType(TileType.values()[ThreadLocalRandom.current().nextInt(0, 6)]);			
		}
		tileRepository.save(tile);
	}

	public void addSpawnTiles(Round round) {
		for(int i = 1; i < 6; i++) {
			Tile tile = new Tile();
			tile.setOrientation(1);
			tile.setRound(round);
			tile.setColumnIndex(2);
			tile.setPieces(new ArrayList<Piece>());
			tile.setTileType(TileType.SPAWN);
			tile.setRowIndex(13 + i);
			tile.setSalmonEggs(i);
			tileRepository.save(tile);
		}
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
