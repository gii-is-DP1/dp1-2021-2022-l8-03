package org.springframework.samples.upstream.board;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.upstream.round.Round;

@Service
public class SalmonBoardService {
	@Autowired 
	SalmonBoardRepository boardRepo;
	
	public Optional<SalmonBoard> findById(Integer id){
		return boardRepo.findById(id);
	}
	
	@Transactional
	public void saveBoard(SalmonBoard board) throws DataAccessException {
		boardRepo.save(board);
	}
}
