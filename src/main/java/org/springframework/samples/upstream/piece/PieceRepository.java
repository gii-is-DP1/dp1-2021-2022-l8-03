package org.springframework.samples.upstream.piece;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

//PieceRepository
public interface PieceRepository extends CrudRepository<Piece, Integer>{
	
	@Query("SELECT piece FROM Piece piece WHERE piece.id =:id")
	public Piece findById(@Param("id") int id);
	
}
