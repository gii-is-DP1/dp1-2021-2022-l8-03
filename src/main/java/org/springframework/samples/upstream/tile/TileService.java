package org.springframework.samples.upstream.tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.upstream.piece.Piece;

import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.round.RoundRepository;
import org.springframework.samples.upstream.tile.exceptions.InvalidPositionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TileService {
	@Autowired
	private TileRepository tileRepository;
	
	@Autowired
	private RoundRepository roundRepository;

	@Autowired
	public TileService(TileRepository tileRepository,RoundRepository roundRepository) {
		this.tileRepository = tileRepository;
		this.roundRepository= roundRepository;
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

	public Tile findTileBySalmonEggs(int salmoneggs, int round_id) throws DataAccessException {
		return tileRepository.findTileBySalmonEggs(salmoneggs, round_id);
	}
	
	@Transactional(readOnly = true)
	public Tile findByPosition(int row, int column, int round_id) throws DataAccessException,InvalidPositionException {
		Tile tile=tileRepository.findByPosition(row, column, round_id);
		if(tile==null) {
			throw new InvalidPositionException();
		}else {
			return tile;
		}
	}
	
	@Transactional(readOnly = true)
	public List<Tile> findByRow(int row, int roundId) throws DataAccessException {
		return tileRepository.findByRow(row, roundId);
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
	
	@Transactional
	public void saveTile(Tile tile) throws DataAccessException {
		this.tileRepository.save(tile);
	}
	
	public void removeStartingTiles(Integer roundId) {
		List<Tile> startingTiles = findSeaTilesInRound(roundId);
		for(Tile tile : startingTiles) {
			deleteTile(tile);
		}
	}
	

	public void removeLowestTiles(Integer roundId) {
		Integer lowestRow = findLowestRow(roundId);
		for(int i=1;i<4;i++) {
			if(i==2) {
				try {
					Tile tile = findByPosition(lowestRow+1, i, roundId);
					if(!tile.getTileType().equals(TileType.SPAWN)) {
						deleteTile(tile);
					}
				}catch(InvalidPositionException ex) {
					
				}
				
			}else {
				try {
					Tile tile = findByPosition(lowestRow, i, roundId);
					if(!tile.getTileType().equals(TileType.SPAWN)) {
						deleteTile(tile);
					}
				}catch(InvalidPositionException ex) {
					
				}

			}
		}
	}

	public void addNewRow(Round round) {
		Integer highestRow = findHighestRow(round.getId());
		for(int i=1;i<4;i++) {
			createRandomTile(highestRow + 1, i, round);		
		}
	}

	public void createRandomTile(int row, int column, Round round) {
		Collection<Tile> roundTiles=round.getTiles();
		if(roundTiles==null) {
			roundTiles = new ArrayList<Tile>();
		}
		
		Tile tile = new Tile();
		tile.setPieces(new ArrayList<Piece>());
		tile.setRound(round);
		tile.setColumnIndex(column);
		tile.setRowIndex(row);
		tile.setSalmonEggs(0);
		
		if(round.getRapids()) {
			tile.setTileType(TileType.values()[ThreadLocalRandom.current().nextInt(0, 7)]);
		}else {
			tile.setTileType(TileType.values()[ThreadLocalRandom.current().nextInt(0, 6)]);			
		}
		
		TileType type = tile.getTileType();
		if(type.equals(TileType.BEAR)||type.equals(TileType.WATERFALL)) {
			tile.setOrientation(ThreadLocalRandom.current().nextInt(1, 7));
		}
		else if(type.equals(TileType.EAGLE)||type.equals(TileType.HERON)||type.equals(TileType.WATER)||type.equals(TileType.ROCK)){
			tile.setOrientation(0);
		}
		else {
			if(tile.getColumnIndex()==1) {
				tile.setOrientation(ThreadLocalRandom.current().nextInt(1, 3));
			}
			else if(tile.getColumnIndex()==3) {
				tile.setOrientation(ThreadLocalRandom.current().nextInt(1, 3));
			}
			else {
				tile.setOrientation(ThreadLocalRandom.current().nextInt(1, 4));
			}
			
		}
		tileRepository.save(tile);
		roundTiles.add(tile);
		round.setTiles(roundTiles);
		roundRepository.save(round);
	}	

	public void addSpawnTiles(Round round) {
		Collection<Tile> roundTiles=round.getTiles();
		for(int i = 1; i < 6; i++) {
			Tile tile = new Tile();
			tile.setOrientation(i-1);
			tile.setRound(round);
			tile.setColumnIndex(2);
			tile.setPieces(new ArrayList<Piece>());
			tile.setTileType(TileType.SPAWN);
			tile.setRowIndex(13 + i);
			tile.setSalmonEggs(i);
			tileRepository.save(tile);
			roundTiles.add(tile);
		}
		round.setTiles(roundTiles);
		roundRepository.save(round);
	}

	public void createSeaTiles(Round round) {
		Collection<Tile> roundTiles=round.getTiles();
		for(Integer i=1;i<5;i++) {
			Tile seaTile=new Tile();
			seaTile.setOrientation(i);
			if(i==3) {
				seaTile.setOrientation(4);
			}
			if(i==4) {
				seaTile.setOrientation(3);
				seaTile.setColumnIndex(2);
				seaTile.setRowIndex(2);
			}else {
				seaTile.setColumnIndex(i);
				seaTile.setRowIndex(1);
			}
			seaTile.setSalmonEggs(0);
			seaTile.setTileType(TileType.SEA);
			seaTile.setRound(round);
			saveTile(seaTile);

		}
		round.setTiles(roundTiles);
		roundRepository.save(round);
	}
	
	public void createInitialTiles(Round round) {
		createSeaTiles(round);
		createRandomTile(2, 1, round);
		createRandomTile(2, 3, round);
		addNewRow(round);
		addNewRow(round);

		if(round.getTiles()==null) {
			round.setTiles(new ArrayList<Tile>());
			this.roundRepository.save(round);
		}
	}	
}
