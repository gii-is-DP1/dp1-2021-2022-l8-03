package org.springframework.samples.upstream.actingPlayer;


import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.piece.PieceRepository;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.round.RoundService;
import org.springframework.samples.upstream.round.RoundState;
import org.springframework.samples.upstream.score.ScoreService;
import org.springframework.samples.upstream.tile.Tile;
import org.springframework.samples.upstream.tile.TileService;
import org.springframework.samples.upstream.tile.TileType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActingPlayerService {
	
	private ActingPlayerRepository actingPlayerRepository;
	private PieceRepository pieceRepository;
	private TileService tileService;
	private RoundService roundService;
	private ScoreService scoreService;
	
	@Autowired
	public ActingPlayerService(ActingPlayerRepository actingPlayerRepository, PieceRepository pieceRepository, 
			TileService tileService, RoundService roundService, ScoreService scoreService) {
		this.actingPlayerRepository = actingPlayerRepository;
		this.pieceRepository = pieceRepository;
		this.tileService = tileService;
		this.roundService = roundService;
		this.scoreService = scoreService;
	}
	
	@Transactional(readOnly = true)
	public ActingPlayer findActingPlayerById(int id) throws DataAccessException {
		return actingPlayerRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public ActingPlayer findActingPlayerByRound(int id) throws DataAccessException {
		return actingPlayerRepository.findByRound(id);
	}
	
	public void saveActingPlayer(ActingPlayer actingPlayer) throws DataAccessException {
		this.actingPlayerRepository.save(actingPlayer);
	}
	
	public void createActingPlayerToRound(Round round) {
		ActingPlayer actingPlayer=new ActingPlayer();
		actingPlayer.setFirstPlayer(0);
		actingPlayer.setPlayer(0);
		if(round.getNum_players()==2) {
			actingPlayer.setPoints(4);
		}
		else {
			actingPlayer.setPoints(5);
		}
		actingPlayer.setTurn(1);
		actingPlayer.setRound(round);
		saveActingPlayer(actingPlayer);
		round.setActingPlayer(actingPlayer);
		this.roundService.saveRound(round);
	}
	
	public void changeTurn(ActingPlayer actingPlayer) {
		Boolean turnChanged = false;
		Round round = actingPlayer.getRound();
		Integer numPlayers = actingPlayer.getRound().getNum_players();
		Integer currentPlayer = actingPlayer.getPlayer();
		Integer newPlayer = currentPlayer + 1;
		Integer firstPlayer = actingPlayer.getFirstPlayer();
		Integer turn = actingPlayer.getTurn();
		ActingPlayer newActingPlayer = actingPlayer;
		if(newPlayer == numPlayers) {
			newPlayer = 0;
		}
		if(newPlayer == firstPlayer) {
			firstPlayer += 1;
			if(firstPlayer == numPlayers) {
				firstPlayer = 0;
			}
			newPlayer = firstPlayer;
			turn = turn + 1;
			turnChanged = true;
		}
		newActingPlayer.setPlayer(newPlayer);
		newActingPlayer.setFirstPlayer(firstPlayer);
		newActingPlayer.setPoints(5);
		newActingPlayer.setTurn(turn);
		actingPlayerRepository.save(newActingPlayer);
		if(turn == 3 && turnChanged) {
			tileService.removeStartingTiles(round.getId());
		}else if(turn > 3 && turnChanged) {
			tileService.removeLowestTiles(round.getId());
		}
		if(turn < 9 && turnChanged) {
			tileService.addNewRow(round);
		}else if(turn == 9 && turnChanged) {
			tileService.addSpawnTiles(round);
		}
		if(turnChanged) {
			advanceSpawnTilePieces(round);
		}
		if(checkPiecesInSpawnTiles(round)) {
			endTheGame(round);
		}
	}
	
	public void changeTurnTwoPlayers(ActingPlayer actingPlayer) {
		Boolean turnChanged = false;
		Round round = actingPlayer.getRound();
		Integer currentPlayer = actingPlayer.getPlayer();
		Integer newPlayer = currentPlayer + 1;
		Integer firstPlayer = actingPlayer.getFirstPlayer();
		Integer turn = actingPlayer.getTurn();
		ActingPlayer newActingPlayer = actingPlayer;
		if(newPlayer == 1) {
			newActingPlayer.setPoints(5);
		}else if(newPlayer == 2) {
			newPlayer = 0;
			newActingPlayer.setPoints(5);
			turn = turn + 1;
			turnChanged = true;
		}
		newActingPlayer.setPlayer(newPlayer);
		newActingPlayer.setFirstPlayer(firstPlayer);
		newActingPlayer.setTurn(turn);
		actingPlayerRepository.save(newActingPlayer);
		if(turn == 3 && turnChanged) {
			tileService.removeStartingTiles(round.getId());
		}else if(turn > 3 && turnChanged) {
			tileService.removeLowestTiles(round.getId());
		}
		if(turn < 9 && turnChanged) {
			tileService.addNewRow(round);
		}else if(turn == 9 && turnChanged) {
			tileService.addSpawnTiles(round);
		}
		if(turnChanged) {
			advanceSpawnTilePieces(round);
		}
		if(checkPiecesInSpawnTiles(round)) {
			endTheGame(round);
		}
	}
	
	public void advanceSpawnTilePieces(Round round) {
		Collection<Piece> pieces = this.pieceRepository.findPiecesInSpawnTiles(round.getId());
		for(Piece piece : pieces) {
			Integer salmonEggs = piece.getTile().getSalmonEggs();
			if(salmonEggs < 5) {
				Tile newTile = this.tileService.findTileBySalmonEggs(salmonEggs + 1, round.getId());
				piece.setTile(newTile);
				this.pieceRepository.save(piece);
			}
		}
	}
	
	public Boolean checkPiecesInSpawnTiles(Round round) {
		Boolean flag = true;
		Collection<Piece> pieces = round.getPieces();
		for(Piece piece : pieces) {
			if(!piece.getTile().getTileType().equals(TileType.SPAWN)) {
				flag = false;
			}
		}
		return flag;
	}
	
	public void endTheGame(Round round) {
		round.setRound_state(RoundState.FINISHED);
		round.setMatch_end(new java.util.Date());
		this.roundService.saveRound(round);
		this.scoreService.setPlayerScores(round);
	}
}
