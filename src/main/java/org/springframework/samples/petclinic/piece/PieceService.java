package org.springframework.samples.petclinic.piece;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//PieceService
@Service
public class PieceService {
	@Autowired
	private PieceRepository pieceRepo;
	
	@Transactional
	public int pieceCount() {
		return (int) pieceRepo.count();
	}
}
