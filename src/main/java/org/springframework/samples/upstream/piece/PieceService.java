package org.springframework.samples.upstream.piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.samples.upstream.actingPlayer.ActingPlayer;
import org.springframework.samples.upstream.actingPlayer.ActingPlayerService;
import org.springframework.samples.upstream.piece.exceptions.InvalidCapacityException;
import org.springframework.samples.upstream.piece.exceptions.InvalidCurrentBearException;
import org.springframework.samples.upstream.piece.exceptions.InvalidCurrentWaterfallException;
import org.springframework.samples.upstream.piece.exceptions.InvalidDirectionJumpException;
import org.springframework.samples.upstream.piece.exceptions.InvalidDirectionSwimException;
import org.springframework.samples.upstream.piece.exceptions.InvalidDistanceJumpException;
import org.springframework.samples.upstream.piece.exceptions.InvalidDistanceSwimException;
import org.springframework.samples.upstream.piece.exceptions.InvalidNewBearException;
import org.springframework.samples.upstream.piece.exceptions.InvalidNewWaterfallException;
import org.springframework.samples.upstream.piece.exceptions.InvalidPlayerException;
import org.springframework.samples.upstream.piece.exceptions.InvalidPositionException;
import org.springframework.samples.upstream.piece.exceptions.NoPointsException;
import org.springframework.samples.upstream.piece.exceptions.PieceStuckException;
import org.springframework.samples.upstream.piece.exceptions.RoundNotInCourseException;
import org.springframework.samples.upstream.piece.exceptions.SameTileException;
import org.springframework.samples.upstream.piece.exceptions.TileSpawnException;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.player.PlayerService;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.round.RoundService;
import org.springframework.samples.upstream.round.RoundState;
import org.springframework.samples.upstream.tile.Tile;
import org.springframework.samples.upstream.tile.TileRepository;
import org.springframework.samples.upstream.tile.TileService;
import org.springframework.samples.upstream.tile.TileType;
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
	
	@Transactional
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
			piece.setTile(tile);
			
			List<Player> players = new ArrayList<Player>(round.getPlayers());
			piece.setColor(Color.values()[players.indexOf(player)]);
			
			this.pieceRepository.save(piece);
			
			roundPieces.add(piece);
			playerPieces.add(piece);
			tilePieces.add(piece);
		}
		round.setPieces(roundPieces);
		roundService.saveRound(round);
		player.setPieces(playerPieces);
		playerService.savePlayer(player);
	}
	
	public void savePiece(Piece piece) throws DataAccessException {
		pieceRepository.save(piece);		
	}	
	
	public void swim(Piece piece, Tile oldTile, Tile newTile) throws DataAccessException, InvalidPositionException,InvalidPlayerException, InvalidDistanceSwimException, SameTileException, InvalidCapacityException, InvalidDirectionSwimException, NoPointsException, InvalidCurrentWaterfallException, InvalidCurrentBearException, InvalidNewWaterfallException, InvalidNewBearException, PieceStuckException, TileSpawnException, RoundNotInCourseException {
		if(checkUser(piece) && checkRoundState(piece.getRound()) && checkDistanceSwim(oldTile, newTile) && sameTile(oldTile, newTile)
			&& checkCapacity(newTile, piece.getRound()) && checkDirectionSwim(oldTile, newTile)
			&& checkSwimPoints(piece.getRound()) && checkCurrentWaterfall(oldTile, newTile)
			&& checkNewWaterfall(oldTile,newTile) && checkCurrentBear(oldTile, newTile) 
			&& checkNewBear(oldTile, newTile) && checkStuck(piece) && checkSpawn(oldTile, newTile)) {
					
			piece.setTile(newTile);
			newTile.getPieces().add(piece);
			oldTile.getPieces().remove(piece);
			tileService.saveTile(newTile);
			tileService.saveTile(oldTile);
			piece = checkRapids(piece, newTile);
			piece = checkWhirlpool(piece, newTile);
			piece = checkEagle(piece, newTile);
			pieceRepository.save(piece);
			
			if(piece.getNumSalmon() < 1) {
				newTile.getPieces().remove(piece);
				tileService.saveTile(newTile);
				pieceRepository.delete(piece);
			}
			substractMovementPointsSwim(piece.getRound());
		}
	}
	
	public void jump(Piece piece, Tile oldTile, Tile newTile) throws DataAccessException, InvalidPositionException,InvalidPlayerException, InvalidDistanceJumpException, SameTileException, InvalidCapacityException, InvalidDirectionJumpException, PieceStuckException, TileSpawnException, RoundNotInCourseException {
		if(checkUser(piece) && checkRoundState(piece.getRound()) && sameTile(oldTile, newTile) 
			&& checkDistanceJump(oldTile, newTile, piece.getRound()) && checkCapacity(newTile, piece.getRound()) 
			&& checkDirectionJump(oldTile, newTile) && checkStuck(piece)  && checkSpawn(oldTile, newTile)) {	
			
			
			piece = checkIntermediateBear(piece, oldTile, newTile);			
			piece.setTile(newTile);
			newTile.getPieces().add(piece);
			oldTile.getPieces().remove(piece);
			tileService.saveTile(newTile);
			tileService.saveTile(oldTile);
			piece = checkRapids(piece, newTile);
			piece = checkWhirlpool(piece, newTile);
			piece = checkBear(piece, oldTile, newTile);
			piece = checkEagle(piece, newTile);
			pieceRepository.save(piece);
			if(piece.getNumSalmon() < 1) {
				newTile.getPieces().remove(piece);
				tileService.saveTile(newTile);
				pieceRepository.delete(piece);
			}
			substractMovementPointsJump(piece.getRound(), oldTile, newTile);
		}
		
	}
	
	private Boolean checkRoundState(Round round) throws RoundNotInCourseException {
		if(round.getRound_state().equals(RoundState.IN_COURSE)) {
			return true;
		} else {
			throw new RoundNotInCourseException();
		}
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
	
	public Boolean checkDistanceSwim(Tile oldTile, Tile newTile) throws InvalidDistanceSwimException{
		Integer oldColumn = oldTile.getColumnIndex();
		Integer rowDistance = Math.abs(oldTile.getRowIndex() - newTile.getRowIndex());
		Integer columnDistance = Math.abs(oldTile.getColumnIndex() - newTile.getColumnIndex());
		if((rowDistance + columnDistance <= 1 && oldColumn == 2) || (rowDistance <= 1 && columnDistance <= 1 && oldColumn != 2)) {
			return true;
		} else {
			throw new InvalidDistanceSwimException();
		}
	}
	
	public Boolean checkDistanceJump(Tile oldTile, Tile newTile, Round round) throws InvalidPositionException, InvalidDistanceJumpException{
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
		if(movementPoints >= numTiles) {
			return true;
		} else {
			throw new InvalidDistanceJumpException();
		}
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
	
	public Boolean sameTile(Tile oldTile, Tile newTile) throws SameTileException {
		Integer oldRow = oldTile.getRowIndex();
		Integer oldColumn = oldTile.getColumnIndex();
		Integer newRow = newTile.getRowIndex();
		Integer newColumn = newTile.getColumnIndex();
		if(!(oldRow==newRow && oldColumn==newColumn)) {
			return true;
		} else {
			throw new SameTileException();
		}
	}
	
	public Boolean checkCapacity(Tile newTile, Round round) throws InvalidCapacityException {
		Integer capacity = newTile.getPieces().size();
		Integer numPlayers = round.getPlayers().size();
		TileType tipo = newTile.getTileType();
		if(tipo.equals(TileType.SPAWN)) {
			return true;
		}else if(tipo.equals(TileType.ROCK) && !round.getWhirlpools() && round.getNum_players() > 2) {
			if(!(capacity == numPlayers-1)) {
				return true;
			} else {
				throw new InvalidCapacityException();
			}
		}else {
			if(!(capacity == numPlayers)) {
				return true;
			} else {
				throw new InvalidCapacityException();
			}
		}
	}
	
	public Boolean checkDirectionSwim(Tile oldTile, Tile newTile) throws InvalidDirectionSwimException {
		Integer oldRow = oldTile.getRowIndex();
		Integer newRow = newTile.getRowIndex();
		if(newRow >= oldRow) {
			return true;
		} else {
			throw new InvalidDirectionSwimException();
		}
	}
	
	public Boolean checkDirectionJump(Tile oldTile, Tile newTile) throws InvalidDirectionJumpException {
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
		if(ahead && straightLine) {
			return true;
		} else {
			throw new InvalidDirectionJumpException();
		}
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
	
	public Boolean checkSwimPoints(Round round) throws NoPointsException {
		ActingPlayer player = round.getActingPlayer();
		if(player.getPoints() >= 1) {
			return true;
		} else {
			throw new NoPointsException();
		}
	}
	
	public Boolean checkCurrentWaterfall(Tile oldTile, Tile newTile) throws InvalidCurrentWaterfallException {
		TileType type = oldTile.getTileType();
		Integer rowMovement = newTile.getRowIndex() - oldTile.getRowIndex();
		Integer columnMovement = newTile.getColumnIndex() - oldTile.getColumnIndex();
		Boolean permission = true;
		if(type.equals(TileType.WATERFALL)) {
			if(columnMovement == 0) { //MOVIMIENTO VERTICAL
				permission = checkCurrentWaterfallVerticalMovement(oldTile);
			} else if(columnMovement == -1) { //MOVIMIENTO IZQUIERDA
				permission = checkCurrentWaterfallLeftMovement(rowMovement, oldTile);
			} else { //MOVIMIENTO DERECHA
				permission = checkCurrentWaterfallRightMovement(rowMovement, oldTile);
			}
		}
		if(permission) {
			return true;
		} else {
			throw new InvalidCurrentWaterfallException();
		}
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
	
	public Boolean checkCurrentBear(Tile oldTile, Tile newTile) throws InvalidCurrentBearException {
		TileType type = oldTile.getTileType();
		Integer rowMovement = newTile.getRowIndex() - oldTile.getRowIndex();
		Integer columnMovement = newTile.getColumnIndex() - oldTile.getColumnIndex();
		Boolean permission = true;
		if(type.equals(TileType.BEAR)) {
			if(columnMovement == 0) { //MOVIMIENTO VERTICAL
				permission = checkCurrentBearVerticalMovement(oldTile);
			} else if(columnMovement == -1) { //MOVIMIENTO IZQUIERDA
				permission = checkCurrentBearLeftMovement(rowMovement, oldTile);
			} else { //MOVIMIENTO DERECHA
				permission = checkCurrentBearRightMovement(rowMovement, oldTile);
			}
		}
		if(permission) {
			return true;
		} else {
			throw new InvalidCurrentBearException();
		}
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
	
	public Boolean checkNewWaterfall(Tile oldTile, Tile newTile) throws InvalidNewWaterfallException {
		TileType type = newTile.getTileType();
		Integer rowMovement = newTile.getRowIndex() - oldTile.getRowIndex();
		Integer columnMovement = newTile.getColumnIndex() - oldTile.getColumnIndex();
		Boolean permission = true;
		if(type.equals(TileType.WATERFALL)) {
			if(columnMovement == 0) { //MOVIMIENTO VERTICAL
				permission = checkNewWaterfallVerticalMovement(newTile);
			} else if(columnMovement == -1) { //MOVIMIENTO IZQUIERDA
				permission = checkNewWaterfallLeftMovement(rowMovement, oldTile, newTile);
			} else { //MOVIMIENTO DERECHA
				permission = checkNewWaterFallRightMovement(rowMovement, oldTile, newTile);				
			}
		}
		if(permission) {
			return true;
		} else {
			throw new InvalidNewWaterfallException();
		}
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
	
	public Boolean checkNewBear(Tile oldTile, Tile newTile) throws InvalidNewBearException {
		TileType type = newTile.getTileType();
		Integer rowMovement = newTile.getRowIndex() - oldTile.getRowIndex();
		Integer columnMovement = newTile.getColumnIndex() - oldTile.getColumnIndex();
		Boolean permission = true;
		if(type.equals(TileType.BEAR)) {
			if(columnMovement == 0) { //MOVIMIENTO VERTICAL
				permission = checkNewBearVerticalMovement(newTile);
			} else if(columnMovement == -1) { //MOVIMIENTO IZQUIERDA
				permission = checkNewBearLeftMovement(rowMovement, oldTile, newTile);
			} else { //MOVIMIENTO DERECHA
				permission = checkNewBearRightMovement(rowMovement, oldTile, newTile);				
			}
		}
		if(permission) {
			return true;
		} else {
			throw new InvalidNewBearException();
		}
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
		if(rowMovement == 0 && oldTile.getColumnIndex()==2) {
			return !(newTile.getOrientation()==1 || newTile.getOrientation()==2);
		} else if(rowMovement == 0 && oldTile.getColumnIndex()==1) {
			return !(newTile.getOrientation()==2 || newTile.getOrientation()==3);
		} else {
			return !(newTile.getOrientation()==1 || newTile.getOrientation()==2);
		}
	}
	
	public Boolean checkStuck(Piece piece) throws PieceStuckException {
		if(!piece.getStuck()) {
			return true;
		} else {
			throw new PieceStuckException();
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
	
	public Boolean checkSpawn(Tile oldTile, Tile newTile) throws TileSpawnException {
		if(!oldTile.getTileType().equals(TileType.SPAWN) && newTile.getSalmonEggs() < 2) {
			return true;
		} else {
			throw new TileSpawnException();
		}
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
		if(actingPlayer.getPoints()==0 || this.actingPlayerService.checkMovablePieces(round.getPlayers().get(actingPlayer.getPlayer()))) {
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
		if(actingPlayer.getPoints()==0 || this.actingPlayerService.checkMovablePieces(round.getPlayers().get(actingPlayer.getPlayer()))) {
			checkHeron(round);
			if(round.getNum_players() > 2) {
				actingPlayerService.changeTurn(actingPlayer);
			}else {
				actingPlayerService.changeTurnTwoPlayers(actingPlayer);
			}
		}
	}
	
	private void checkHeron(Round round) throws InvalidPositionException {
		String authenticatedUsername = getCurrentUsername();
		List<Tile> heronTiles = tileService.findHeronTilesInRound(round.getId());
		for(Tile heron:heronTiles) {
			for(Piece piece:heron.getPieces()) {
				String pieceUsername = piece.getPlayer().getUser().getUsername();
				if(pieceUsername.equals(authenticatedUsername)) {
					piece.setNumSalmon(piece.getNumSalmon()-1);
					pieceRepository.save(piece);				
					if(piece.getNumSalmon() < 1) {
						heron.getPieces().remove(piece);
						tileService.saveTile(heron);
						pieceRepository.delete(piece);
					}
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
		newTile = this.tileService.findByPositionRapids(newRow, newColumn, roundId);
		if(newTile != null) {
			if(newTile.getPieces().size()<piece.getRound().getNum_players()-1 && newTile.getTileType().equals(TileType.ROCK) && !piece.getRound().getWhirlpools() && piece.getRound().getNum_players() > 2) {
				piece.setTile(newTile);
			} else if(newTile.getPieces().size()<piece.getRound().getNum_players()) {
				piece.setTile(newTile);
				if(newTile.getTileType().equals(TileType.ROCK) && piece.getRound().getWhirlpools()) {
					piece.setStuck(true);
				}
			}
		}
		return piece;	
	}
	
	private Pair<Integer, Integer> checkRapidsColumn1(Tile newTile, Integer newRow, Integer newColumn) {
		if(newTile.getOrientation() == 2) {
			newRow = newRow + 1;
		}else if(newTile.getOrientation() == 3) {
			newRow = newRow + 1;
			newColumn = newColumn + 1;
		}
		Pair<Integer, Integer> tupla = Pair.of(newRow, newColumn);
		return tupla;
	}
	
	private Pair<Integer, Integer> checkRapidsColumn2(Tile newTile, Integer newRow, Integer newColumn) {
		if(newTile.getOrientation() == 1) {
			newColumn = newColumn - 1;
		}else if(newTile.getOrientation() == 2) {
			newRow = newRow + 1;
		}else if(newTile.getOrientation() == 3) {
			newColumn = newColumn + 1;
		}
		Pair<Integer, Integer> tupla = Pair.of(newRow, newColumn);
		return tupla;
	}
	
	private Pair<Integer, Integer> checkRapidsColumn3(Tile newTile, Integer newRow, Integer newColumn) {
		if(newTile.getOrientation() == 1) {
			newRow = newRow + 1;
			newColumn = newColumn - 1;
		}else if(newTile.getOrientation() == 2) {
			newRow = newRow + 1;
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
