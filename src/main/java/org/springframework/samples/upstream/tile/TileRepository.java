package org.springframework.samples.upstream.tile;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TileRepository extends CrudRepository<Tile, Integer>{

	@Query("SELECT tile FROM Tile tile WHERE tile.id =:id")
	public Tile findById(@Param("id") int id);
	
	@Query("SELECT tile FROM Tile tile WHERE tile.rowIndex=:row AND tile.columnIndex=:column AND tile.round.id=:round")
	public Tile findByPosition(@Param("row") int row, @Param("column") int column, @Param("round") int round);
	
	@Query("SELECT tile FROM Tile tile WHERE tile.rowIndex=:row AND tile.round.id=:round")
	public List<Tile> findByRow(@Param("row") int row, @Param("round") int round);
	
	@Query("SELECT MIN(tile.rowIndex) FROM Tile tile WHERE tile.round.id=:round")
	public Integer findLowestRow(@Param("round") int round);
	
	@Query("SELECT MAX(tile.rowIndex) FROM Tile tile WHERE tile.round.id=:round")
	public Integer findHighestRow(@Param("round") int round);
	
	@Query("SELECT tile FROM Tile tile WHERE tile.tileType=8 AND tile.round.id=:round_id")
	public List<Tile> findSeaTilesInRound(@Param("round_id") int round_id);
	
	@Query("SELECT tile FROM Tile tile WHERE tile.tileType=2 AND tile.round.id=:round_id")
	public List<Tile> findHeronTilesInRound(@Param("round_id") int round_id);
	
	@Query("SELECT tile FROM Tile tile WHERE tile.tileType=7 AND tile.round.id=:round_id")
	public List<Tile> findSpawnTilesInRound(@Param("round_id") int round_id);

	@Query("SELECT tile FROM Tile tile WHERE tile.round.id=:round_id")
	public List<Tile> findTilesInRound(@Param("round_id") int round_id);
}