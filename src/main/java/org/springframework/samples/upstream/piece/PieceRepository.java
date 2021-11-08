package org.springframework.samples.upstream.piece;

import org.springframework.data.repository.CrudRepository;
//PieceRepository
public interface PieceRepository extends CrudRepository<Piece, Integer>{
	
}
