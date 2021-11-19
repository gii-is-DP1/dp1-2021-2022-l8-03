package org.springframework.samples.upstream.tile;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.upstream.piece.Piece;

public interface TileRepository extends CrudRepository<Tile, Integer>{

	@Query("SELECT tile FROM Tile tile WHERE tile.id =:id")
	public Tile findById(@Param("id") int id);
	
}