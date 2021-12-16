package org.springframework.samples.upstream.piece;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.upstream.piece.PieceService;
import org.springframework.stereotype.Service;

//PieceServiceTest
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PieceServiceTest {
    @Autowired
    protected PieceService pieceService;


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
}