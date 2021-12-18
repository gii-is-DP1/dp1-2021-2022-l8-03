package org.springframework.samples.upstream.actingPlayer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.tile.TileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActingPlayerService {
	
	private ActingPlayerRepository actingPlayerRepository;
	private TileService tileService;
	
	@Autowired
	public ActingPlayerService(ActingPlayerRepository actingPlayerRepository, TileService tileService) {
		this.actingPlayerRepository = actingPlayerRepository;
		this.tileService = tileService;
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
	
	public void changeTurn(ActingPlayer actingPlayer) {
		Round round = actingPlayer.getRound();
		Integer numPlayers = actingPlayer.getRound().getNum_players();
		Integer currentPlayer = actingPlayer.getPlayer();
		Integer newPlayer = currentPlayer + 1;
		Integer firstPlayer = actingPlayer.getFirstPlayer();
		Integer turn = actingPlayer.getTurn();
		Integer newTurn = turn + 1;
		ActingPlayer newActingPlayer = actingPlayer;
		if(newPlayer == numPlayers) {
			newPlayer = 0;
		}
		if(newPlayer == firstPlayer) {
			firstPlayer += 1;
			newPlayer = firstPlayer;
		}
		newActingPlayer.setPlayer(newPlayer);
		newActingPlayer.setFirstPlayer(firstPlayer);
		newActingPlayer.setPoints(5);
		newActingPlayer.setTurn(newTurn);
		actingPlayerRepository.save(newActingPlayer);
		if(newTurn == 3) {
			tileService.removeStartingTiles(round.getId());
		}else if(newTurn > 3) {
			tileService.removeLowestTiles(round.getId());
		}
		if(newTurn < 9) {
			tileService.addNewRow(round);
		}else if(newTurn == 9) {
			tileService.addSpawnTiles(round);
		}
		//Falta final de partida y puntuación
	}
}
