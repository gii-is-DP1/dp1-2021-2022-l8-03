package org.springframework.samples.upstream.piece;

import static org.assertj.core.api.Assertions.assertThat;

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
    	Piece piece = this.pieceService.findPieceById(2);
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
    void shouldSwim() {
    	Piece piece = this.pieceService.findPieceById(2);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(16);
    	newTile.setTileType(TileType.WATER);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldJump() {
    	Piece piece = this.pieceService.findPieceById(2);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(17);
    	newTile.setTileType(TileType.WATER);
    	
    	this.pieceService.jump(piece, oldTile, newTile);
    }
    
    
    @Test
    @Transactional
    @WithMockUser(username = "player6")
    void shouldNotSwimDifferentUser() {
    	Piece piece = this.pieceService.findPieceById(2);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(16);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "player5")
    void shouldNotSwimNextBear() {
    	Piece piece = this.pieceService.findPieceById(2);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(16);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
}