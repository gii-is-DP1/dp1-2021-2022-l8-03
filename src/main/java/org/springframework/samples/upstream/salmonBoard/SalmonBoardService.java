package org.springframework.samples.upstream.salmonBoard;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

@Service
public class SalmonBoardService {
	@Autowired 
	SalmonBoardRepository boardRepo;
	
	public List<SalmonBoard> findAll(){
		return this.boardRepo.findAll();
	}
		
	public Optional<SalmonBoard> findById(Integer id){
		return this.boardRepo.findById(id);
	}
	
	public SalmonBoard findByRoundId(Integer roundId) {
		return this.boardRepo.findBoardInRound(roundId);
	}
	
	@Transactional
	public void delete(SalmonBoard salmonboard) {
		this.boardRepo.delete(salmonboard);
	}
	
	@Transactional
	public void saveBoard(SalmonBoard board) throws DataAccessException {
		this.boardRepo.save(board);
	}
}
