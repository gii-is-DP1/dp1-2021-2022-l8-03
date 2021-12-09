package org.springframework.samples.upstream.piece;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.upstream.actingPlayer.ActingPlayer;
import org.springframework.samples.upstream.actingPlayer.ActingPlayerService;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.tile.Tile;
import org.springframework.samples.upstream.tile.TileService;
import org.springframework.samples.upstream.tile.TileType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//PieceService
@Service
public class PieceService {
	private PieceRepository pieceRepository;
	@Autowired
	private ActingPlayerService actingPlayerService;
	@Autowired
	private TileService tileService;
	
	@Autowired
	public PieceService(PieceRepository pieceRepo, ActingPlayerService actingPlayerService, TileService tileService) {
		this.pieceRepository = pieceRepo;
		this.actingPlayerService = actingPlayerService;
		this.tileService = tileService;
	}
	
	@Transactional(readOnly = true)
	public Piece findPieceById(int id) throws DataAccessException {
		return pieceRepository.findById(id);
	}
	
	public void savePiece(Piece piece) throws DataAccessException {
		//creating piece
		pieceRepository.save(piece);		
	}	
	
	public void swim(Piece piece, Tile oldTile, Tile newTile) throws DataAccessException {
		if(checkUser(piece) && checkDistance(oldTile, newTile) && sameTile(oldTile, newTile)
			&& checkCapacity(newTile, piece.getRound()) && checkDirection(oldTile, newTile)
			&& checkSwimPoints(piece.getRound()) && checkCurrentWaterfall(oldTile, newTile)
			&& checkNewWaterfall(oldTile,newTile)) {	//MOVIMIENTO VÁLIDO	
			
			piece = checkEagle(piece, newTile);
			substractMovementPointsSwim(piece.getRound());
			piece.setTile(newTile);
			pieceRepository.save(piece);
		}
	}
	
	public void jump(Piece piece, Tile oldTile, Tile newTile) throws DataAccessException {
		if(checkUser(piece) && sameTile(oldTile, newTile) && checkDistance(oldTile, newTile) 
			&& checkCapacity(newTile, piece.getRound()) && checkDirection(oldTile, newTile)
			&& checkJumpPoints(piece.getRound())) {
			
			piece = checkBear(piece, oldTile, newTile);
			substractMovementPointsJump(piece.getRound());
			piece.setTile(newTile);
			pieceRepository.save(piece);
			
		}
		
	}
	
	public Boolean checkUser(Piece piece) {
		Round round = piece.getRound();
		Integer actingPlayer = round.getActingPlayer().getPlayer();
		List<Player> players = (List<Player>) round.getPlayers();
		String actingUsername = players.get(actingPlayer).getUser().getUsername();
		String pieceUsername = piece.getPlayer().getUser().getUsername();
		String authenticatedUsername = getCurrentUsername();
		return actingUsername.equals(pieceUsername) && actingUsername.equals(authenticatedUsername);
	}
	
	public Boolean checkDistance(Tile oldTile, Tile newTile) {
		Integer rowDistance = Math.abs(oldTile.getRowIndex() - newTile.getRowIndex());
		Integer columnDistance = Math.abs(oldTile.getColumnIndex() - newTile.getColumnIndex());
		return rowDistance <= 1 || columnDistance <= 1;
	}
	
	public Boolean sameTile(Tile oldTile, Tile newTile) {
		Integer oldRow = oldTile.getRowIndex();
		Integer oldColumn = oldTile.getColumnIndex();
		Integer newRow = newTile.getRowIndex();
		Integer newColumn = newTile.getColumnIndex();
		return !(oldRow==newRow && oldColumn==newColumn);
	}
	
	public Boolean checkCapacity(Tile newTile, Round round) {
		Integer capacity = newTile.getPieces().size();
		Integer numPlayers = round.getNum_players();
		TileType tipo = newTile.getTileType();
		if(tipo.equals(TileType.ROCK)) {
			return !(capacity == numPlayers-1);
		}else {
			return !(capacity == numPlayers);
		}
	}
	
	public Boolean checkDirection(Tile oldTile, Tile newTile) {
		Integer oldRow = oldTile.getRowIndex();
		Integer newRow = newTile.getRowIndex();
		return newRow >= oldRow;
	}
	
	public Boolean checkSwimPoints(Round round) {
		ActingPlayer player = round.getActingPlayer();
		return player.getPoints() >= 1;
	}
	
	public Boolean checkJumpPoints(Round round) {
		ActingPlayer player = round.getActingPlayer();
		return player.getPoints() >= 2;
	}
	
	public Boolean checkCurrentWaterfall(Tile oldTile, Tile newTile) {
		TileType type = oldTile.getTileType();
		Integer rowMovement = newTile.getRowIndex() - oldTile.getRowIndex();
		Integer columnMovement = newTile.getColumnIndex() - oldTile.getColumnIndex();
		if(type.equals(TileType.WATERFALL)) {
			if(columnMovement == 0) { //MOVIMIENTO VERTICAL
				return !(oldTile.getOrientation()==3 || oldTile.getOrientation()==4 || oldTile.getOrientation()==5);
			}else if(columnMovement == -1) { //MOVIMIENTO IZQUIERDA
				if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 1
					return !(oldTile.getOrientation()==2 || oldTile.getOrientation()==3 || oldTile.getOrientation()==4);
				}else if(rowMovement == 0 && oldTile.getColumnIndex()==3) { //COLUMNA  3 -> 2
					return !(oldTile.getOrientation()==1 || oldTile.getOrientation()==2 || oldTile.getOrientation()==3);
				}else { //COLUMNA 3 -> 2 Y SUBE FILA
					return !(oldTile.getOrientation()==2 || oldTile.getOrientation()==3 || oldTile.getOrientation()==4);
				}
			}else { //MOVIMIENTO DERECHA
				if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 3
					return !(oldTile.getOrientation()==4 || oldTile.getOrientation()==5 || oldTile.getOrientation()==6);
				}else if(rowMovement == 0 && oldTile.getColumnIndex()==1) { //COLUMNA  1 -> 2
					return !(oldTile.getOrientation()==1 || oldTile.getOrientation()==5 || oldTile.getOrientation()==6);
				}else { //COLUMNA 1 -> 2 Y SUBE FILA
					return !(oldTile.getOrientation()==4 || oldTile.getOrientation()==5 || oldTile.getOrientation()==6);
				}				
			}
		}else if(type.equals(TileType.BEAR)) {
			if(columnMovement == 0) { //MOVIMIENTO VERTICAL
				return !(oldTile.getOrientation()==3 || oldTile.getOrientation()==4);
			}else if(columnMovement == -1) { //MOVIMIENTO IZQUIERDA
				if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 1
					return !(oldTile.getOrientation()==2 || oldTile.getOrientation()==3);
				}else if(rowMovement == 0 && oldTile.getColumnIndex()==3) { //COLUMNA  3 -> 2
					return !(oldTile.getOrientation()==1 || oldTile.getOrientation()==2);
				}else { //COLUMNA 3 -> 2 Y SUBE FILA
					return !(oldTile.getOrientation()==2 || oldTile.getOrientation()==3);
				}
			}else { //MOVIMIENTO DERECHA
				if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 3
					return !(oldTile.getOrientation()==4 || oldTile.getOrientation()==5);
				}else if(rowMovement == 0 && oldTile.getColumnIndex()==1) { //COLUMNA  1 -> 2
					return !(oldTile.getOrientation()==5 || oldTile.getOrientation()==6);
				}else { //COLUMNA 1 -> 2 Y SUBE FILA
					return !(oldTile.getOrientation()==4 || oldTile.getOrientation()==5);
				}				
			}
		}else {
			return true;
		}
	}
	
	public Boolean checkNewWaterfall(Tile oldTile, Tile newTile) {
		TileType type = newTile.getTileType();
		Integer rowMovement = newTile.getRowIndex() - oldTile.getRowIndex();
		Integer columnMovement = newTile.getColumnIndex() - oldTile.getColumnIndex();
		if(type.equals(TileType.WATERFALL)) {
			if(columnMovement == 0) { //MOVIMIENTO VERTICAL
				return !(newTile.getOrientation()==1 || newTile.getOrientation()==2 || newTile.getOrientation()==6);
			}else if(columnMovement == -1) { //MOVIMIENTO IZQUIERDA
				if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 1
					return !(newTile.getOrientation()==1 || newTile.getOrientation()==5 || newTile.getOrientation()==6);
				}else if(rowMovement == 0 && oldTile.getColumnIndex()==3) { //COLUMNA  3 -> 2
					return !(newTile.getOrientation()==4 || newTile.getOrientation()==5 || newTile.getOrientation()==6);
				}else { //COLUMNA 3 -> 2 Y SUBE FILA
					return !(newTile.getOrientation()==5 || newTile.getOrientation()==6 || newTile.getOrientation()==1);
				}
			}else { //MOVIMIENTO DERECHA
				if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 3
					return !(newTile.getOrientation()==1 || newTile.getOrientation()==2 || newTile.getOrientation()==3);
				}else if(rowMovement == 0 && oldTile.getColumnIndex()==1) { //COLUMNA  1 -> 2
					return !(newTile.getOrientation()==2 || newTile.getOrientation()==3 || newTile.getOrientation()==4);
				}else { //COLUMNA 1 -> 2 Y SUBE FILA
					return !(newTile.getOrientation()==1 || newTile.getOrientation()==2 || newTile.getOrientation()==3);
				}				
			}
		}else if(type.equals(TileType.BEAR)) {
			if(columnMovement == 0) { //MOVIMIENTO VERTICAL
				return !(newTile.getOrientation()==1 || newTile.getOrientation()==6);
			}else if(columnMovement == -1) { //MOVIMIENTO IZQUIERDA
				if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 1
					return !(newTile.getOrientation()==5 || newTile.getOrientation()==6);
				}else if(rowMovement == 0 && oldTile.getColumnIndex()==3) { //COLUMNA  3 -> 2
					return !(newTile.getOrientation()==4 || newTile.getOrientation()==5);
				}else { //COLUMNA 3 -> 2 Y SUBE FILA
					return !(newTile.getOrientation()==5 || newTile.getOrientation()==6);
				}
			}else { //MOVIMIENTO DERECHA
				if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 3
					return !(newTile.getOrientation()==1 || newTile.getOrientation()==2);
				}else if(rowMovement == 0 && oldTile.getColumnIndex()==1) { //COLUMNA  1 -> 2
					return !(newTile.getOrientation()==2 || newTile.getOrientation()==3);
				}else { //COLUMNA 1 -> 2 Y SUBE FILA
					return !(newTile.getOrientation()==1 || newTile.getOrientation()==2);
				}				
			}
		}else {
			return true;
		}
	}
	
	public Piece checkEagle(Piece piece, Tile newTile) {
		Integer numSalmon = piece.getNumSalmon();
		if(newTile.getTileType().equals(TileType.EAGLE)) {
			numSalmon += -1;
			piece.setNumSalmon(numSalmon);
			newTile.setTileType(TileType.WATER);
			this.tileService.saveTile(newTile);
		}
		return piece;
	}
	
	public Piece checkBear(Piece piece, Tile oldTile, Tile newTile) {
		Integer numSalmon = piece.getNumSalmon();
		if(oldTile.getTileType().equals(TileType.BEAR)) {
			numSalmon += -1;
			piece.setNumSalmon(numSalmon);
		} else if (newTile.getTileType().equals(TileType.BEAR)) {
			numSalmon += -1;
			piece.setNumSalmon(numSalmon);
		}
		return piece;
	}
	
	public void substractMovementPointsSwim(Round round) {
		ActingPlayer actingPlayer = round.getActingPlayer();
		actingPlayer.setPoints(actingPlayer.getPoints()-1);
		actingPlayerService.saveActingPlayer(actingPlayer);		
	}
	
	public void substractMovementPointsJump(Round round) {
		ActingPlayer actingPlayer = round.getActingPlayer();
		actingPlayer.setPoints(actingPlayer.getPoints()-2);
		actingPlayerService.saveActingPlayer(actingPlayer);
	}
	
	public String getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User)authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		return currentUsername;
	}
}
