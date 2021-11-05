package org.springframework.samples.upstream.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScoreService {
	@Autowired
	private ScoreRepository scoreRepo;
	
	@Transactional
	public int scoreCount() {
		return (int) scoreRepo.count();
	}
	
	@Transactional
	public Iterable<Score> findAllScores(){
		return scoreRepo.findAll();
	}

}
