package org.springframework.samples.upstream.piece;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PieceService {
	@Autowired
	private PieceRepository pieceRepo;
	
	@Transactional
	public int pieceCount() {
		return (int) pieceRepo.count();
	}
	
	@Transactional
	public Iterable<Piece> findAllPieces(){
		return pieceRepo.findAll();
	}
}
