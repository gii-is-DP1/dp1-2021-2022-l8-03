package org.springframework.samples.upstream.piece;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

//PieceRepository
public interface PieceRepository extends CrudRepository<Piece, Integer>{
	
	@Query("SELECT piece FROM Piece piece WHERE piece.id =:id")
	public Piece findById(@Param("id") int id);
	
	@Query("SELECT piece FROM Piece piece WHERE piece.player.id=:player_id")
	public List<Piece> findPiecesOfPlayer(@Param("player_id") int player_id);
	
	@Query("SELECT piece FROM Piece piece WHERE piece.round.id=:round_id")
	public List<Piece> findPiecesInRound(@Param("round_id") int round_id);
}
