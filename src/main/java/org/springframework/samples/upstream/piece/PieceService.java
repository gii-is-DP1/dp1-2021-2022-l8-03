package org.springframework.samples.upstream.piece;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.player.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//PieceService
@Service
public class PieceService {
	@Autowired
	private PieceRepository pieceRepository;
	
	@Autowired
	public PieceService(PieceRepository pieceRepository) {
		this.pieceRepository = pieceRepository;
	}	

	@Transactional(readOnly = true)
	public Piece findPieceById(int id) throws DataAccessException {
		return pieceRepository.findById(id);
	}
	
	public void savePiece(Piece piece) throws DataAccessException {
		//creating piece
		pieceRepository.save(piece);		
	}	
}
