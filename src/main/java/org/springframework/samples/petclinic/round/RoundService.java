package org.springframework.samples.petclinic.round;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoundService {
	
	private RoundRepository roundRepository;
	
	@Autowired
	public RoundService(RoundRepository roundRepository) {
		this.roundRepository = roundRepository;
	}
	
	@Transactional(readOnly = true)
	public Round findRoundById(int id) throws DataAccessException {
		return roundRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Collection<Round> findAll() throws DataAccessException {
		return roundRepository.findAll();
	}
	
	@Transactional
	public void saveRound(Round round) throws DataAccessException {
		roundRepository.save(round);
	}
}
