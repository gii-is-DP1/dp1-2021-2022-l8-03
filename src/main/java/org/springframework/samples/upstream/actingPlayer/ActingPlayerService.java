package org.springframework.samples.upstream.actingPlayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActingPlayerService {
	
	private ActingPlayerRepository actingPlayerRepository;
	
	@Autowired
	public ActingPlayerService(ActingPlayerRepository actingPlayerRepository) {
		this.actingPlayerRepository = actingPlayerRepository;
	}
	
	@Transactional(readOnly = true)
	public ActingPlayer findActingPlayerById(int id) throws DataAccessException {
		return actingPlayerRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public ActingPlayer findActingPlayerByRound(int id) throws DataAccessException {
		return actingPlayerRepository.findByRound(id);
	}
}
