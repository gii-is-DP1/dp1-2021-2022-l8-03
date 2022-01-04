package org.springframework.samples.upstream.piece;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.upstream.tile.Tile;
import org.springframework.samples.upstream.tile.TileService;
import org.springframework.samples.upstream.tile.TileType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//PieceServiceTest
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PieceServiceTest {
    @Autowired
    protected PieceService pieceService;
    
    @Autowired
    protected TileService tileService;


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
    void shouldNotJumpPointsNeeded() {
    	Piece piece = this.pieceService.findPieceById(22);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(35);
    	
    	this.pieceService.jump(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldNotMoveSameTile() {
    	Piece piece = this.pieceService.findPieceById(22);
    	Tile oldTile = piece.getTile();
    	Tile newTile = piece.getTile();
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldSwim() {
    	Piece piece = this.pieceService.findPieceById(22);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(16);
    	newTile.setTileType(TileType.WATER);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldJump() {
    	Piece piece = this.pieceService.findPieceById(22);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(17);
    	newTile.setTileType(TileType.WATER);
    	
    	this.pieceService.jump(piece, oldTile, newTile);
    }
    
    
    @Test
    @Transactional
    @WithMockUser(username = "player6")
    void shouldNotSwimDifferentUser() {
    	Piece piece = this.pieceService.findPieceById(22);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(16);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldNotSwimCurrentBear() {
    	Piece piece = this.pieceService.findPieceById(24);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(24);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldNotSwimNextBear() {
    	Piece piece = this.pieceService.findPieceById(22);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(18);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldNotSwimCurrentWaterfallVertical() {
    	Piece piece = this.pieceService.findPieceById(23);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(22);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldNotSwimCurrentWaterfallRight() {
    	Piece piece = this.pieceService.findPieceById(25);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(26);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldNotSwimNewWaterfall() {
    	Piece piece = this.pieceService.findPieceById(28);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(36);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldCheckEagle() {
    	Piece piece = this.pieceService.findPieceById(22);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(17);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldCheckRapids() {
    	Piece piece = this.pieceService.findPieceById(26);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(29);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldCheckBear() {
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

}