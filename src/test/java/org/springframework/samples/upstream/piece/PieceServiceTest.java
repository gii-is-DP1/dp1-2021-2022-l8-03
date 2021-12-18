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
    	Piece piece = this.pieceService.findPieceById(1);
    	assertThat(piece.getPlayer().getUser().getUsername()).isEqualTo("cardelbec");
    }
    
    @Test
    void shouldNotFindPieceById() {
    	Piece piece = this.pieceService.findPieceById(250);
    	assertThat(piece).isEqualTo(null);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "cardelbec")
    void shouldSwim() {
    	Piece piece = this.pieceService.findPieceById(1);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(3);
    	newTile.setTileType(TileType.WATER);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "manlopalm")
    void shouldNotSwimDifferentUser() {
    	Piece piece = this.pieceService.findPieceById(1);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(3);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "cardelbec")
    void shouldNotSwimNextBear() {
    	Piece piece = this.pieceService.findPieceById(1);
    	Tile oldTile = piece.getTile();
    	Tile newTile = this.tileService.findTileById(3);
    	
    	this.pieceService.swim(piece, oldTile, newTile);
    }
    
}