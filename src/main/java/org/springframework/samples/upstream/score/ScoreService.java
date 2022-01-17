package org.springframework.samples.upstream.score;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.round.Round;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ScoreService {
	@Autowired
	private ScoreRepository scoreRepo;
	
	@Transactional
	public void saveScore(Score score) throws DataAccessException {
		scoreRepo.save(score);
	}
	
	@Transactional
	public void deleteScore(Score score) throws DataAccessException {
		scoreRepo.delete(score);
	}
	
	public Score findByPlayerAndRound(int playerId, int roundId) {
		return scoreRepo.findByPlayerAndRound(playerId, roundId);
	}
	
	public List<Score> findByRound(int roundId) {
		return scoreRepo.findByRound(roundId);
	}
	
	public void setPlayerScores(Round round) {
		List<Player> players = (List) round.getPlayers();
		for(Player player : players) {
			Score playerScore = findByPlayerAndRound(player.getId(),round.getId());
			Integer scoreValue = playerScore.getValue();
			List<Piece> playerPieces = (List) player.getPieces();
			for(Piece piece : playerPieces) {
				scoreValue += piece.getNumSalmon();
				scoreValue += piece.getTile().getSalmonEggs();
			}
			playerScore.setValue(scoreValue);
			scoreRepo.save(playerScore);
		}
	}
	
}
