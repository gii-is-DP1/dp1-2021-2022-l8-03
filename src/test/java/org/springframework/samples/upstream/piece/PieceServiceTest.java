package org.springframework.samples.upstream.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
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
import org.springframework.samples.upstream.tile.Tile;
import org.springframework.samples.upstream.tile.TileService;
import org.springframework.samples.upstream.tile.TileType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PieceServiceTest {
    @Autowired
    protected PieceService pieceService;
    
    @Autowired
    protected TileService tileService;
    
    @Autowired
    protected PlayerService playerService;


    @Test
    void shouldFindPieceById() {
    	Piece piece = this.pieceService.findPieceById(22);
    	assertThat(piece.getPlayer().getUser().getUsername()).isEqualTo("player5");
    }
    
    @Test
    void shouldNotFindPieceById() {
    	Piece piece = this.pieceService.findPieceById(250);
    	assertThat(piece).isEqualTo(null);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldCreatePlayerPiece() {
    	Player player = this.playerService.findPlayerByUsername("player5");
    	Round round = this.pieceService.findPieceById(22).getRound();
    	
    	this.pieceService.createPlayerPieces(player, round);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldNotJumpPointsNeeded() throws InvalidPositionException,InvalidPlayerException, DataAccessException, InvalidDistanceJumpException, SameTileException, InvalidCapacityException, InvalidDirectionJumpException, PieceStuckException, TileSpawnException, RoundNotInCourseException{
    	Piece piece = this.pieceService.findPieceById(22);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(35);
    	
    	assertThrows(InvalidDistanceJumpException.class, () -> this.pieceService.jump(piece, oldTile, newTile));
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldNotMoveSameTile() throws InvalidPositionException,InvalidPlayerException, DataAccessException, InvalidDistanceSwimException, SameTileException, InvalidCapacityException, InvalidDirectionSwimException, NoPointsException, InvalidCurrentWaterfallException, InvalidCurrentBearException, InvalidNewWaterfallException, InvalidNewBearException, PieceStuckException, TileSpawnException, RoundNotInCourseException{
    	Piece piece = this.pieceService.findPieceById(22);
    	Tile oldTile = piece.getTile();
    	Tile newTile = piece.getTile();
    	
    	assertThrows(SameTileException.class, () -> this.pieceService.swim(piece, oldTile, newTile));
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldSwim() throws InvalidPositionException,InvalidPlayerException, DataAccessException, InvalidDistanceSwimException, SameTileException, InvalidCapacityException, InvalidDirectionSwimException, NoPointsException, InvalidCurrentWaterfallException, InvalidCurrentBearException, InvalidNewWaterfallException, InvalidNewBearException, PieceStuckException, TileSpawnException, RoundNotInCourseException{
    	Piece piece = this.pieceService.findPieceById(22);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(13);
    	newTile.setTileType(TileType.WATER);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldJump() throws InvalidPositionException,InvalidPlayerException, DataAccessException, InvalidDistanceJumpException, SameTileException, InvalidCapacityException, InvalidDirectionJumpException, PieceStuckException, TileSpawnException, RoundNotInCourseException{
    	Piece piece = this.pieceService.findPieceById(22);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(17);
    	newTile.setTileType(TileType.WATER);
    	
    	this.pieceService.jump(piece, oldTile, newTile);
    }
    
    
    @Test
    @Transactional
    @WithMockUser(username = "player6")
    void shouldNotSwimDifferentUser() throws InvalidPositionException,InvalidPlayerException{
    	Piece piece = this.pieceService.findPieceById(22);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(16);
    	
    	
    	assertThrows(InvalidPlayerException.class,()-> this.pieceService.swim(piece, oldTile, newTile));
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldNotSwimCurrentBear() throws InvalidPositionException,InvalidPlayerException, DataAccessException, InvalidDistanceSwimException, SameTileException, InvalidCapacityException, InvalidDirectionSwimException, NoPointsException, InvalidCurrentWaterfallException, InvalidCurrentBearException, InvalidNewWaterfallException, InvalidNewBearException, PieceStuckException, TileSpawnException, RoundNotInCourseException{
    	Piece piece = this.pieceService.findPieceById(24);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(24);
    	
    	assertThrows(InvalidCurrentBearException.class, () -> this.pieceService.swim(piece, oldTile, newTile));
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldNotSwimNextBear() throws InvalidPositionException,InvalidPlayerException, DataAccessException, InvalidDistanceSwimException, SameTileException, InvalidCapacityException, InvalidDirectionSwimException, NoPointsException, InvalidCurrentWaterfallException, InvalidCurrentBearException, InvalidNewWaterfallException, InvalidNewBearException, PieceStuckException, TileSpawnException, RoundNotInCourseException{
    	Piece piece = this.pieceService.findPieceById(22);
    	piece.setTile(this.tileService.findTileById(17));
    	this.pieceService.save(piece);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(18);
    	
    	assertThrows(InvalidNewBearException.class, () -> this.pieceService.swim(piece, oldTile, newTile));
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldNotSwimCurrentWaterfallVertical() throws InvalidPositionException,InvalidPlayerException, DataAccessException, InvalidDistanceSwimException, SameTileException, InvalidCapacityException, InvalidDirectionSwimException, NoPointsException, InvalidCurrentWaterfallException, InvalidCurrentBearException, InvalidNewWaterfallException, InvalidNewBearException, PieceStuckException, TileSpawnException, RoundNotInCourseException{
    	Piece piece = this.pieceService.findPieceById(23);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(22);
    	
    	assertThrows(InvalidCurrentWaterfallException.class, () -> this.pieceService.swim(piece, oldTile, newTile));
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldCheckHeron() throws InvalidPositionException,InvalidPlayerException, DataAccessException, InvalidDistanceJumpException, SameTileException, InvalidCapacityException, InvalidDirectionJumpException, PieceStuckException, TileSpawnException, RoundNotInCourseException{
    	Piece piece = this.pieceService.findPieceById(30);
    	Tile oldTile = piece.getTile();
    	piece.getRound().getActingPlayer().setPoints(2);
    	Tile newTile = this.tileService.findTileById(33);
    	
    	this.pieceService.jump(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldNotSwimCurrentWaterfallRight() throws InvalidPositionException,InvalidPlayerException, DataAccessException, InvalidDistanceSwimException, SameTileException, InvalidCapacityException, InvalidDirectionSwimException, NoPointsException, InvalidCurrentWaterfallException, InvalidCurrentBearException, InvalidNewWaterfallException, InvalidNewBearException, PieceStuckException, TileSpawnException, RoundNotInCourseException{
    	Piece piece = this.pieceService.findPieceById(25);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(26);
    	
    	assertThrows(InvalidCurrentWaterfallException.class, () -> this.pieceService.swim(piece, oldTile, newTile));
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldNotSwimNewWaterfall() throws InvalidPositionException,InvalidPlayerException, DataAccessException, InvalidDistanceSwimException, SameTileException, InvalidCapacityException, InvalidDirectionSwimException, NoPointsException, InvalidCurrentWaterfallException, InvalidCurrentBearException, InvalidNewWaterfallException, InvalidNewBearException, PieceStuckException, TileSpawnException, RoundNotInCourseException{
    	Piece piece = this.pieceService.findPieceById(28);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(36);
    	
    	assertThrows(InvalidNewWaterfallException.class, () -> this.pieceService.swim(piece, oldTile, newTile));
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldCheckEagle() throws InvalidPositionException,InvalidPlayerException, DataAccessException, InvalidDistanceSwimException, SameTileException, InvalidCapacityException, InvalidDirectionSwimException, NoPointsException, InvalidCurrentWaterfallException, InvalidCurrentBearException, InvalidNewWaterfallException, InvalidNewBearException, PieceStuckException, TileSpawnException, RoundNotInCourseException{
    	Piece piece = this.pieceService.findPieceById(22);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(17);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldCheckRapids() throws InvalidPositionException,InvalidPlayerException, DataAccessException, InvalidDistanceSwimException, SameTileException, InvalidCapacityException, InvalidDirectionSwimException, NoPointsException, InvalidCurrentWaterfallException, InvalidCurrentBearException, InvalidNewWaterfallException, InvalidNewBearException, PieceStuckException, TileSpawnException, RoundNotInCourseException{
    	Piece piece = this.pieceService.findPieceById(26);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(29);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldCheckBear() throws InvalidPositionException,InvalidPlayerException, DataAccessException, InvalidDistanceJumpException, SameTileException, InvalidCapacityException, InvalidDirectionJumpException, PieceStuckException, TileSpawnException, RoundNotInCourseException{
    	Piece piece = this.pieceService.findPieceById(29);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(35);
    	
    	this.pieceService.jump(piece, oldTile, newTile);
    	
    }
    
    @Test
    void shouldFindPiecesOfPlayer() {
    	List<Piece> pieces = this.pieceService.findPiecesOfPlayer(11);
    	assertThat(pieces.size()).isEqualTo(8);
    }
    
    @Test
    void shouldDeletePiece() {
    	Piece pieceToDelete = this.pieceService.findPieceById(6);
    	assertThat(pieceToDelete).isNotEqualTo(null);
    	
    	this.pieceService.deletePiece(pieceToDelete);
    	
    	Piece deletedPiece = this.pieceService.findPieceById(6);
    	assertThat(deletedPiece).isEqualTo(null);
    }
    
    @Test
    void shouldFindPiecesInRound() {
    	List<Piece> pieces = this.pieceService.findPiecesInRound(1);
    	assertThat(pieces.size()).isEqualTo(20);
    }
    
    @Test
    void shouldFindPiecesInSpawnTiles()  {
    	List<Piece> pieces = this.pieceService.findPiecesInSpawnTiles(1);
    	assertThat(pieces.size()).isEqualTo(0);
    }
}