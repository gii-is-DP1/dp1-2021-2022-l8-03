package org.springframework.samples.upstream.score;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.piece.PieceService;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.round.Round;
import org.springframework.stereotype.Service;


@Service
public class ScoreService {
	@Autowired
	private ScoreRepository scoreRepo;
	
	public Score findByPlayer(int playerId, int roundId) {
		return scoreRepo.findByPlayer(playerId, roundId);
	}
	
	public void setPlayerScores(Round round) {
		List<Player> players = (List) round.getPlayers();
		for(Player player : players) {
			Score playerScore = findByPlayer(player.getId(),round.getId());
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
