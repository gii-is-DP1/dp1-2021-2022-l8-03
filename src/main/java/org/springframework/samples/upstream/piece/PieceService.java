package org.springframework.samples.upstream.piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.samples.upstream.actingPlayer.ActingPlayer;
import org.springframework.samples.upstream.actingPlayer.ActingPlayerService;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.player.PlayerService;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.round.RoundService;
import org.springframework.samples.upstream.round.RoundState;
import org.springframework.samples.upstream.tile.Tile;
import org.springframework.samples.upstream.tile.TileService;
import org.springframework.samples.upstream.tile.TileType;
import org.springframework.samples.upstream.tile.exceptions.InvalidPlayerException;
import org.springframework.samples.upstream.tile.exceptions.InvalidPositionException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PieceService {
	@Autowired
	private PieceRepository pieceRepository;
	
	@Autowired
	private ActingPlayerService actingPlayerService;
	
	@Autowired
	private TileService tileService;
	
	@Autowired
	private RoundService roundService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	public PieceService(PieceRepository pieceRepo, ActingPlayerService actingPlayerService, TileService tileService,RoundService roundService,PlayerService playerService) {
		this.pieceRepository = pieceRepo;
		this.actingPlayerService = actingPlayerService;
		this.tileService = tileService;
		this.roundService=roundService;
		this.playerService=playerService;
	}
	
	@Transactional(readOnly = true)
	public Piece findPieceById(int id) throws DataAccessException {
		return pieceRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public List<Piece> findPiecesInSpawnTiles(int roundId) throws DataAccessException {
		return pieceRepository.findPiecesInSpawnTiles(roundId);
	}
	
	@Transactional(readOnly = true)
	public void deletePiece(Piece piece) throws DataAccessException {
		pieceRepository.delete(piece);
	}
	
	@Transactional(readOnly = true)
	public List<Piece> findPiecesOfPlayer(int playerId) throws DataAccessException {
		return this.pieceRepository.findPiecesOfPlayer(playerId);
	}
	
	@Transactional(readOnly = true)
	public List<Piece> findPiecesInRound(int roundId) throws DataAccessException {
		return this.pieceRepository.findPiecesInRound(roundId);
	}
	
	public void createPlayerPieces(Player player,Round round) throws DataAccessException{
		Collection<Piece> roundPieces=round.getPieces();
		if(roundPieces==null) {
			roundPieces=new ArrayList<Piece>();
		}
		Collection<Piece> playerPieces=player.getPieces();
		if(playerPieces==null) {
			playerPieces=new ArrayList<Piece>();
		}
		List<Tile> seaTiles=this.tileService.findSeaTilesInRound(round.getId());
		for(Integer e=0;e<4;e++) {
			Tile tile=seaTiles.get(e);
			if(tile.getPieces()==null) {
				tile.setPieces(new ArrayList<Piece>());
			}
			Collection<Piece> tilePieces=tile.getPieces();
			Piece piece=new Piece();
			piece.setNumSalmon(2);
			piece.setPlayer(player);
			piece.setRound(round);
			piece.setStuck(false);
			tilePieces.add(piece);
			piece.setTile(tile);
			
			List<Player> players = new ArrayList<Player>(round.getPlayers());
			piece.setColor(Color.values()[players.indexOf(player)]);
			
			this.pieceRepository.save(piece);
			
			roundPieces.add(piece);
			playerPieces.add(piece);
		}
		round.setPieces(roundPieces);
		roundService.saveRound(round);
		player.setPieces(playerPieces);
		playerService.savePlayer(player);
	}
	
	public void savePiece(Piece piece) throws DataAccessException {
		pieceRepository.save(piece);		
	}	
	
	public void swim(Piece piece, Tile oldTile, Tile newTile) throws DataAccessException, InvalidPositionException,InvalidPlayerException {
		if(checkUser(piece) && checkRoundState(piece.getRound()) && checkDistanceSwim(oldTile, newTile) && sameTile(oldTile, newTile)
			&& checkCapacity(newTile, piece.getRound()) && checkDirectionSwim(oldTile, newTile)
			&& checkSwimPoints(piece.getRound()) && checkCurrentWaterfall(oldTile, newTile)
			&& checkNewWaterfall(oldTile,newTile) && checkCurrentBear(oldTile, newTile) 
			&& checkNewBear(oldTile, newTile) && checkStuck(piece) && checkSpawn(oldTile)) {	//MOVIMIENTO VÁLIDO	
			
			piece = checkWhirlpool(piece, newTile);
			piece = checkRapids(piece, newTile);
			piece = checkEagle(piece, newTile);
			piece.setTile(newTile);
			pieceRepository.save(piece);
			if(piece.getNumSalmon() < 1) {
				pieceRepository.delete(piece);
			}
			substractMovementPointsSwim(piece.getRound());
		}
	}
	
	public void jump(Piece piece, Tile oldTile, Tile newTile) throws DataAccessException, InvalidPositionException,InvalidPlayerException {
		if(checkUser(piece) && checkRoundState(piece.getRound()) && sameTile(oldTile, newTile) 
			&& checkDistanceJump(oldTile, newTile, piece.getRound()) && checkCapacity(newTile, piece.getRound()) 
			&& checkDirectionJump(oldTile, newTile) && checkStuck(piece)  && checkSpawn(oldTile)) {		//MOVIMIENTO VÁLIDO
			
			piece = checkWhirlpool(piece, newTile);
			piece = checkRapids(piece, newTile);
			piece = checkBear(piece, oldTile, newTile);
			piece = checkIntermediateBear(piece, oldTile, newTile);
			piece = checkEagle(piece, newTile);
			piece.setTile(newTile);
			pieceRepository.save(piece);
			if(piece.getNumSalmon() < 1) {
				pieceRepository.delete(piece);
			}
			substractMovementPointsJump(piece.getRound(), oldTile, newTile);
		}
		
	}
	
	private Boolean checkRoundState(Round round) {
		return round.getRound_state().equals(RoundState.IN_COURSE);
	}

	public Boolean checkUser(Piece piece) throws InvalidPositionException,InvalidPlayerException{
		
		Round round = piece.getRound();
		Integer actingPlayer = round.getActingPlayer().getPlayer();
		List<Player> players = (List<Player>) round.getPlayers();
		String actingUsername = players.get(actingPlayer).getUser().getUsername();
		String pieceUsername = piece.getPlayer().getUser().getUsername();
		String authenticatedUsername = getCurrentUsername();
		
		if(actingUsername.equals(pieceUsername) && actingUsername.equals(authenticatedUsername)) {
			return true;
		}else {
			throw new InvalidPlayerException();
		}
	}
	
	public Boolean checkDistanceSwim(Tile oldTile, Tile newTile) {
		Integer rowDistance = Math.abs(oldTile.getRowIndex() - newTile.getRowIndex());
		Integer columnDistance = Math.abs(oldTile.getColumnIndex() - newTile.getColumnIndex());
		return rowDistance <= 1 || columnDistance <= 1;
	}
	
	public Boolean checkDistanceJump(Tile oldTile, Tile newTile, Round round) throws InvalidPositionException{
		Integer row = oldTile.getRowIndex();
		Integer column = oldTile.getColumnIndex();
		Integer colDir = newTile.getColumnIndex() - oldTile.getColumnIndex();
		Integer rowDir = newTile.getRowIndex() - oldTile.getRowIndex();
		Integer numTiles = 2;
		Integer roundId = round.getId();
		if(oldTile.getColumnIndex()==1) {
			numTiles = checkDistanceJumpColumn1(colDir, rowDir, numTiles, row, column, roundId, newTile);
		} else if(oldTile.getColumnIndex()==2) {
			numTiles = checkDistanceJumpColumn2(colDir, rowDir, numTiles);
		} else if(oldTile.getColumnIndex()==3) {
			numTiles = checkDistanceJumpColumn3(colDir, rowDir, numTiles, row, column, roundId, newTile);
		}
		Integer movementPoints = round.getActingPlayer().getPoints();
		return movementPoints >= numTiles;
	}
	
	private Integer checkDistanceJumpColumn1(Integer colDir, Integer rowDir, Integer numTiles, Integer row, Integer column, Integer roundId, Tile newTile) throws InvalidPositionException{
		if(colDir == 0) {
			for(int i = 1; i < rowDir; i++) {
				numTiles += 1;
			}
		}else {
			Tile intermediateTile = this.tileService.findByPosition(row + 1, column + 1, roundId);
			if(!intermediateTile.equals(newTile)) {
				numTiles += 1;
			}
		}
		return numTiles;
	}
	
	private Integer checkDistanceJumpColumn2(Integer colDir, Integer rowDir, Integer numTiles) {
		if(colDir == 0) {
			for(int i = 1; i < rowDir; i++) {
				numTiles += 1;
			}
		}
		return numTiles;
	}
	
	private Integer checkDistanceJumpColumn3(Integer colDir, Integer rowDir, Integer numTiles, Integer row, Integer column, Integer roundId, Tile newTile) throws InvalidPositionException{
		if(colDir == 0) {
			for(int i = 1; i < rowDir; i++) {
				numTiles += 1;
			}
		}else {
			Tile intermediateTile = this.tileService.findByPosition(row + 1, column - 1, roundId);
			if(!intermediateTile.equals(newTile)) {
				numTiles += 1;
			}
		}
		return numTiles;
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
		Integer numPlayers = round.getPlayers().size();
		TileType tipo = newTile.getTileType();
		if(tipo.equals(TileType.ROCK) && !round.getWhirlpools()) {
			return !(capacity == numPlayers-1);
		}else {
			return !(capacity == numPlayers);
		}
	}
	
	public Boolean checkDirectionSwim(Tile oldTile, Tile newTile) {
		Integer oldRow = oldTile.getRowIndex();
		Integer newRow = newTile.getRowIndex();
		return newRow >= oldRow;
	}
	
	public Boolean checkDirectionJump(Tile oldTile, Tile newTile) {
		Integer oldRow = oldTile.getRowIndex();
		Integer newRow = newTile.getRowIndex();
		Boolean ahead = newRow >= oldRow;
		Integer colDir = newTile.getColumnIndex() - oldTile.getColumnIndex();
		Integer rowDir = newRow - oldRow;
		Boolean straightLine = false;
		if(oldTile.getColumnIndex()==1) {
			straightLine = straightLineColumn1(colDir, rowDir);
		}else if(oldTile.getColumnIndex()==2) {
			straightLine = straightLineColumn2(colDir, rowDir);
		}else if(oldTile.getColumnIndex()==3) {
			straightLine = straightLineColumn3(colDir, rowDir) ;
		}
		return ahead && straightLine;
	}
	
	private Boolean straightLineColumn1(Integer colDir, Integer rowDir) {
		return (colDir==0) || (colDir == 1 && rowDir == 1) || (colDir == 2 && rowDir == 1);
	}
	
	private Boolean straightLineColumn2(Integer colDir, Integer rowDir) {
		return (colDir == 0) || (colDir == 1 && rowDir == 0) || (colDir == -1 && rowDir == 0);
	}
	
	private Boolean straightLineColumn3(Integer colDir, Integer rowDir) {
		return (colDir == 0) || (colDir == -1 && rowDir == 1) || (colDir == -2 && rowDir == 1);
	}
	
	public Boolean checkSwimPoints(Round round) {
		ActingPlayer player = round.getActingPlayer();
		return player.getPoints() >= 1;
	}
	
	public Boolean checkCurrentWaterfall(Tile oldTile, Tile newTile) {
		TileType type = oldTile.getTileType();
		Integer rowMovement = newTile.getRowIndex() - oldTile.getRowIndex();
		Integer columnMovement = newTile.getColumnIndex() - oldTile.getColumnIndex();
		if(type.equals(TileType.WATERFALL)) {
			if(columnMovement == 0) { //MOVIMIENTO VERTICAL
				return checkCurrentWaterfallVerticalMovement(oldTile);
			} else if(columnMovement == -1) { //MOVIMIENTO IZQUIERDA
				return checkCurrentWaterfallLeftMovement(rowMovement, oldTile);
			} else { //MOVIMIENTO DERECHA
				return checkCurrentWaterfallRightMovement(rowMovement, oldTile);
			}
		}
		return true;
	}
	
	private Boolean checkCurrentWaterfallVerticalMovement(Tile oldTile) {
		return !(oldTile.getOrientation()==3 || oldTile.getOrientation()==4 || oldTile.getOrientation()==5);
	}
	
	private Boolean checkCurrentWaterfallLeftMovement(Integer rowMovement, Tile oldTile) {
		if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 1
			return !(oldTile.getOrientation()==2 || oldTile.getOrientation()==3 || oldTile.getOrientation()==4);
		} else if(rowMovement == 0 && oldTile.getColumnIndex()==3) { //COLUMNA  3 -> 2
			return !(oldTile.getOrientation()==1 || oldTile.getOrientation()==2 || oldTile.getOrientation()==3);
		} else { //COLUMNA 3 -> 2 Y SUBE FILA
			return !(oldTile.getOrientation()==2 || oldTile.getOrientation()==3 || oldTile.getOrientation()==4);
		}
	}
	
	private Boolean checkCurrentWaterfallRightMovement(Integer rowMovement, Tile oldTile) {
		if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 3
			return !(oldTile.getOrientation()==4 || oldTile.getOrientation()==5 || oldTile.getOrientation()==6);
		} else if(rowMovement == 0 && oldTile.getColumnIndex()==1) { //COLUMNA  1 -> 2
			return !(oldTile.getOrientation()==1 || oldTile.getOrientation()==5 || oldTile.getOrientation()==6);
		} else { //COLUMNA 1 -> 2 Y SUBE FILA
			return !(oldTile.getOrientation()==4 || oldTile.getOrientation()==5 || oldTile.getOrientation()==6);
		}		
	}
	
	public Boolean checkCurrentBear(Tile oldTile, Tile newTile) {
		TileType type = oldTile.getTileType();
		Integer rowMovement = newTile.getRowIndex() - oldTile.getRowIndex();
		Integer columnMovement = newTile.getColumnIndex() - oldTile.getColumnIndex();
		if(type.equals(TileType.BEAR)) {
			if(columnMovement == 0) { //MOVIMIENTO VERTICAL
				return checkCurrentBearVerticalMovement(oldTile);
			} else if(columnMovement == -1) { //MOVIMIENTO IZQUIERDA
				return checkCurrentBearLeftMovement(rowMovement, oldTile);
			} else { //MOVIMIENTO DERECHA
				return checkCurrentBearRightMovement(rowMovement, oldTile);
			}
		}
		return true;
	}
	
	private Boolean checkCurrentBearVerticalMovement(Tile oldTile) {
		return !(oldTile.getOrientation()==3 || oldTile.getOrientation()==4);
	}
	
	private Boolean checkCurrentBearLeftMovement(Integer rowMovement, Tile oldTile) {
		if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 1
			return !(oldTile.getOrientation()==2 || oldTile.getOrientation()==3);
		} else if(rowMovement == 0 && oldTile.getColumnIndex()==3) { //COLUMNA  3 -> 2
			return !(oldTile.getOrientation()==1 || oldTile.getOrientation()==2);
		} else { //COLUMNA 3 -> 2 Y SUBE FILA
			return !(oldTile.getOrientation()==2 || oldTile.getOrientation()==3);
		}
	}
	
	private Boolean checkCurrentBearRightMovement(Integer rowMovement, Tile oldTile) {
		if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 3
			return !(oldTile.getOrientation()==4 || oldTile.getOrientation()==5);
		} else if(rowMovement == 0 && oldTile.getColumnIndex()==1) { //COLUMNA  1 -> 2
			return !(oldTile.getOrientation()==5 || oldTile.getOrientation()==6);
		} else { //COLUMNA 1 -> 2 Y SUBE FILA
			return !(oldTile.getOrientation()==4 || oldTile.getOrientation()==5);
		}		
	}
	
	public Boolean checkNewWaterfall(Tile oldTile, Tile newTile) {
		TileType type = newTile.getTileType();
		Integer rowMovement = newTile.getRowIndex() - oldTile.getRowIndex();
		Integer columnMovement = newTile.getColumnIndex() - oldTile.getColumnIndex();
		if(type.equals(TileType.WATERFALL)) {
			if(columnMovement == 0) { //MOVIMIENTO VERTICAL
				return checkNewWaterfallVerticalMovement(newTile);
			} else if(columnMovement == -1) { //MOVIMIENTO IZQUIERDA
				return checkNewWaterfallLeftMovement(rowMovement, oldTile, newTile);
			} else { //MOVIMIENTO DERECHA
				return checkNewWaterFallRightMovement(rowMovement, oldTile, newTile);				
			}
		}
		return true;
	}
	
	private Boolean checkNewWaterfallVerticalMovement(Tile newTile) {
		return !(newTile.getOrientation()==1 || newTile.getOrientation()==2 || newTile.getOrientation()==6);
	}
	
	private Boolean checkNewWaterfallLeftMovement(Integer rowMovement, Tile oldTile, Tile newTile) {
		if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 1
			return !(newTile.getOrientation()==1 || newTile.getOrientation()==5 || newTile.getOrientation()==6);
		} else if(rowMovement == 0 && oldTile.getColumnIndex()==3) { //COLUMNA  3 -> 2
			return !(newTile.getOrientation()==4 || newTile.getOrientation()==5 || newTile.getOrientation()==6);
		} else { //COLUMNA 3 -> 2 Y SUBE FILA
			return !(newTile.getOrientation()==5 || newTile.getOrientation()==6 || newTile.getOrientation()==1);
		}
	}
	
	private Boolean checkNewWaterFallRightMovement(Integer rowMovement, Tile oldTile, Tile newTile) {
		if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 3
			return !(newTile.getOrientation()==1 || newTile.getOrientation()==2 || newTile.getOrientation()==3);
		} else if(rowMovement == 0 && oldTile.getColumnIndex()==1) { //COLUMNA  1 -> 2
			return !(newTile.getOrientation()==2 || newTile.getOrientation()==3 || newTile.getOrientation()==4);
		} else { //COLUMNA 1 -> 2 Y SUBE FILA
			return !(newTile.getOrientation()==1 || newTile.getOrientation()==2 || newTile.getOrientation()==3);
		}
	}
	
	public Boolean checkNewBear(Tile oldTile, Tile newTile) {
		TileType type = newTile.getTileType();
		Integer rowMovement = newTile.getRowIndex() - oldTile.getRowIndex();
		Integer columnMovement = newTile.getColumnIndex() - oldTile.getColumnIndex();
		if(type.equals(TileType.BEAR)) {
			if(columnMovement == 0) { //MOVIMIENTO VERTICAL
				return checkNewBearVerticalMovement(newTile);
			} else if(columnMovement == -1) { //MOVIMIENTO IZQUIERDA
				return checkNewBearLeftMovement(rowMovement, oldTile, newTile);
			} else { //MOVIMIENTO DERECHA
				return checkNewBearRightMovement(rowMovement, oldTile, newTile);				
			}
		}
		return true;
	}
	
	private Boolean checkNewBearVerticalMovement(Tile newTile) {
		return !(newTile.getOrientation()==1 || newTile.getOrientation()==6);
	}
	
	private Boolean checkNewBearLeftMovement(Integer rowMovement, Tile oldTile, Tile newTile) {
		if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 1
			return !(newTile.getOrientation()==5 || newTile.getOrientation()==6);
		} else if(rowMovement == 0 && oldTile.getColumnIndex()==3) { //COLUMNA  3 -> 2
			return !(newTile.getOrientation()==4 || newTile.getOrientation()==5);
		} else { //COLUMNA 3 -> 2 Y SUBE FILA
			return !(newTile.getOrientation()==5 || newTile.getOrientation()==6);
		}
	}
	
	private Boolean checkNewBearRightMovement(Integer rowMovement, Tile oldTile, Tile newTile) {
		if(rowMovement == 0 && oldTile.getColumnIndex()==2) { //COLUMNA 2 -> 3
			return !(newTile.getOrientation()==1 || newTile.getOrientation()==2);
		} else if(rowMovement == 0 && oldTile.getColumnIndex()==1) { //COLUMNA  1 -> 2
			return !(newTile.getOrientation()==2 || newTile.getOrientation()==3);
		} else { //COLUMNA 1 -> 2 Y SUBE FILA
			return !(newTile.getOrientation()==1 || newTile.getOrientation()==2);
		}
	}
	
	public Boolean checkStuck(Piece piece) {
		return !piece.getStuck();
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
	
	public Boolean checkSpawn(Tile oldTile) {
		return !oldTile.getTileType().equals(TileType.SPAWN);
	}
	
	public Piece checkIntermediateBear(Piece piece, Tile oldTile, Tile newTile) throws InvalidPositionException{
		Integer row = oldTile.getRowIndex();
		Integer column = oldTile.getColumnIndex();
		Integer colDir = newTile.getColumnIndex() - oldTile.getColumnIndex();
		Integer rowDir = newTile.getRowIndex() - oldTile.getRowIndex();
		Integer roundId = piece.getRound().getId();
		if(oldTile.getColumnIndex()==1) {
			piece = checkIntermediateBearColumn1(colDir, rowDir, piece, row, column, roundId);
		}else if(oldTile.getColumnIndex()==2) {
			piece = checkIntermediateBearColumn2(colDir, rowDir, piece, row, column, roundId);
		}else if(oldTile.getColumnIndex()==3) {
			piece = checkIntermediateBearColumn3(colDir, rowDir, piece, row, column, roundId);
		}
		return piece;
	}
	
	private Piece checkIntermediateBearColumn1(Integer colDir, Integer rowDir, Piece piece, Integer row, Integer column, Integer roundId) throws InvalidPositionException{
		if(colDir == 0) {
			for(int i = 1; i < rowDir; i++) {
				Tile intermediateTile = this.tileService.findByPosition(row + i, column, roundId);
				if(intermediateTile.getTileType().equals(TileType.BEAR)) {
					piece.setNumSalmon(piece.getNumSalmon() - 1);
				}
				if(piece.getNumSalmon() < 1) {
					break;
				}
			}
		}else {
			Tile intermediateTile = this.tileService.findByPosition(row + 1, column + 1, roundId);
			if(intermediateTile.getTileType().equals(TileType.BEAR)) {
				piece.setNumSalmon(piece.getNumSalmon() - 1);
			}
		}
		return piece;
	}
	
	private Piece checkIntermediateBearColumn2(Integer colDir, Integer rowDir, Piece piece, Integer row, Integer column, Integer roundId) throws InvalidPositionException{
		if(colDir == 0) {
			for(int i = 1; i < rowDir; i++) {
				Tile intermediateTile = this.tileService.findByPosition(row + i, column, roundId);
				if(intermediateTile.getTileType().equals(TileType.BEAR)) {
					piece.setNumSalmon(piece.getNumSalmon() - 1);
				}
				if(piece.getNumSalmon() < 1) {
					break;
				}
			}
		}
		return piece;
	}
	
	private Piece checkIntermediateBearColumn3(Integer colDir, Integer rowDir, Piece piece, Integer row, Integer column, Integer roundId) throws InvalidPositionException {
		if(colDir == 0) {
			for(int i = 1; i < rowDir; i++) {
				Tile intermediateTile = this.tileService.findByPosition(row + i, column, roundId);
				if(intermediateTile.getTileType().equals(TileType.BEAR)) {
					piece.setNumSalmon(piece.getNumSalmon() - 1);
				}
				if(piece.getNumSalmon() < 1) {
					break;
				}
			}
		}else {
			Tile intermediateTile = this.tileService.findByPosition(row + 1, column - 1, roundId);
			if(intermediateTile.getTileType().equals(TileType.BEAR)) {
				piece.setNumSalmon(piece.getNumSalmon() - 1);
			}
		}
		return piece;
	}
	
	public Integer countIntermediateTiles(Tile oldTile, Tile newTile, Round round) throws InvalidPositionException {
		Integer row = oldTile.getRowIndex();
		Integer column = oldTile.getColumnIndex();
		Integer colDir = newTile.getColumnIndex() - oldTile.getColumnIndex();
		Integer rowDir = newTile.getRowIndex() - oldTile.getRowIndex();
		Integer numTiles = 2;
		Integer roundId = round.getId();
		if(oldTile.getColumnIndex()==1) {
			numTiles = countIntermediateTilesColumn1(colDir, rowDir, numTiles, row, column, roundId, newTile);
		} else if(oldTile.getColumnIndex()==2) {
			numTiles = countIntermediateTilesColumn2(colDir, rowDir, numTiles);
		} else if(oldTile.getColumnIndex()==3) {
			numTiles = countIntermediateTilesColumn3(colDir, rowDir, numTiles, row, column, roundId, newTile);
		}
		return numTiles;
	}
	
	private Integer countIntermediateTilesColumn1(Integer colDir, Integer rowDir, Integer numTiles, Integer row, Integer column, Integer roundId, Tile newTile)throws InvalidPositionException {
		if(colDir == 0) {
			for(int i = 1; i < rowDir; i++) {
				numTiles += 1;
			}
		}else {
			Tile intermediateTile = this.tileService.findByPosition(row + 1, column + 1, roundId);
			if(!intermediateTile.equals(newTile)) {
				numTiles += 1;
			}
		}
		return numTiles;
	}
	
	private Integer countIntermediateTilesColumn2(Integer colDir, Integer rowDir, Integer numTiles) {
		if(colDir == 0) {
			for(int i = 1; i < rowDir; i++) {
				numTiles += 1;
			}
		}
		return numTiles;
	}
	
	private Integer countIntermediateTilesColumn3(Integer colDir, Integer rowDir, Integer numTiles, Integer row, Integer column, Integer roundId, Tile newTile) throws InvalidPositionException {
		if(colDir == 0) {
			for(int i = 1; i < rowDir; i++) {
				numTiles += 1;
			}
		}else {
			Tile intermediateTile = this.tileService.findByPosition(row + 1, column - 1, roundId);
			if(!intermediateTile.equals(newTile)) {
				numTiles += 1;
			}
		}
		return numTiles;
	}
	
	public void substractMovementPointsSwim(Round round) throws InvalidPositionException,InvalidPlayerException{
		ActingPlayer actingPlayer = round.getActingPlayer();
		actingPlayer.setPoints(actingPlayer.getPoints()-1);
		actingPlayerService.saveActingPlayer(actingPlayer);
		if(actingPlayer.getPoints()==0) {
			checkHeron(round);
			if(round.getNum_players() > 2) {
				actingPlayerService.changeTurn(actingPlayer);
			}else {
				actingPlayerService.changeTurnTwoPlayers(actingPlayer);
			}
		}
	}
	
	public void substractMovementPointsJump(Round round, Tile oldTile, Tile newTile) throws InvalidPositionException,InvalidPlayerException{
		ActingPlayer actingPlayer = round.getActingPlayer();
		Integer substraction = countIntermediateTiles(oldTile, newTile, round);
		actingPlayer.setPoints(actingPlayer.getPoints()-substraction);
		actingPlayerService.saveActingPlayer(actingPlayer);
		if(actingPlayer.getPoints()==0) {
			checkHeron(round);
			if(round.getNum_players() > 2) {
				actingPlayerService.changeTurn(actingPlayer);
			}else {
				actingPlayerService.changeTurnTwoPlayers(actingPlayer);
			}
		}
	}
	
	private void checkHeron(Round round) throws InvalidPositionException {
		List<Tile> heronTiles = tileService.findHeronTilesInRound(round.getId()); 	
		String authenticatedUsername = getCurrentUsername();
		for(Tile tile : heronTiles) {
			for(Piece piece : tile.getPieces()) {
				String pieceUsername = piece.getPlayer().getUser().getUsername();
				if(pieceUsername.equals(authenticatedUsername)) {
					piece.setNumSalmon(piece.getNumSalmon()-1);
					pieceRepository.save(piece);
				}
			}
		}
	}

	public Piece checkWhirlpool(Piece piece, Tile newTile) {
		Round round = piece.getRound();
		if(newTile.getTileType().equals(TileType.ROCK) && round.getWhirlpools()) {
			piece.setStuck(true);
		}
		return piece;
	}
	
	public Piece checkRapids(Piece piece, Tile newTile) throws InvalidPositionException{
		Integer newRow = newTile.getRowIndex();
		Integer newColumn = newTile.getColumnIndex();
		Integer roundId = piece.getRound().getId();
		Pair<Integer, Integer> tupla;
		if(newTile.getTileType().equals(TileType.RAPIDS)) {
			if(newTile.getColumnIndex() == 1) {
				tupla = checkRapidsColumn1(newTile, newRow, newColumn);
				newRow = tupla.getFirst();
				newColumn = tupla.getSecond();
			}else if(newTile.getColumnIndex() == 2) {
				tupla = checkRapidsColumn2(newTile, newRow, newColumn);
				newRow = tupla.getFirst();
				newColumn = tupla.getSecond();
			}else {
				tupla = checkRapidsColumn3(newTile, newRow, newColumn);
				newRow = tupla.getFirst();
				newColumn = tupla.getSecond();
			}
		}
		newTile = this.tileService.findByPosition(newRow, newColumn, roundId);
		piece.setTile(newTile);
		return piece;
	}
	
	private Pair<Integer, Integer> checkRapidsColumn1(Tile newTile, Integer newRow, Integer newColumn) {
		if(newTile.getOrientation() == 1) {
			newRow = newRow + 1;
		}else if(newTile.getOrientation() == 2) {
			newRow = newRow + 1;
			newColumn = newColumn + 1;
		}else if(newTile.getOrientation() == 3) {
			newColumn = newColumn + 1;
		}else if(newTile.getOrientation() == 4) {
			newRow = newRow - 1;
		}
		Pair<Integer, Integer> tupla = Pair.of(newRow, newColumn);
		return tupla;
	}
	
	private Pair<Integer, Integer> checkRapidsColumn2(Tile newTile, Integer newRow, Integer newColumn) {
		if(newTile.getOrientation() == 1) {
			newRow = newRow + 1;
		}else if(newTile.getOrientation() == 2) {
			newColumn = newColumn + 1;
		}else if(newTile.getOrientation() == 3) {
			newRow = newRow - 1;
			newColumn = newColumn + 1;
		}else if(newTile.getOrientation() == 4) {
			newRow = newRow - 1;
		}else if(newTile.getOrientation() == 5) {
			newRow = newRow - 1;
			newColumn = newColumn - 1;
		}else if(newTile.getOrientation() == 6) {
			newColumn = newColumn - 1;
		}
		Pair<Integer, Integer> tupla = Pair.of(newRow, newColumn);
		return tupla;
	}
	
	private Pair<Integer, Integer> checkRapidsColumn3(Tile newTile, Integer newRow, Integer newColumn) {
		if(newTile.getOrientation() == 1) {
			newRow = newRow + 1;
		}else if(newTile.getOrientation() == 4) {
			newRow = newRow - 1;
		}else if(newTile.getOrientation() == 5) {
			newColumn = newColumn - 1;
		}else if(newTile.getOrientation() == 6) {
			newRow = newRow + 1;
			newColumn = newColumn - 1;
		}
		Pair<Integer, Integer> tupla = Pair.of(newRow, newColumn);
		return tupla;
	}
	
	public String getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User)authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		return currentUsername;
	}

	public void save(Piece piece) {
		this.pieceRepository.save(piece);
		
	}
}
