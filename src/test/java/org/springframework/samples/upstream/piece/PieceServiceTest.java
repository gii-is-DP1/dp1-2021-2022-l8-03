package org.springframework.samples.upstream.piece;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.upstream.piece.PieceService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PieceServiceTest {
	@Autowired
	private PieceService pieceService;

	@Test //importada con jUnit5
	public void testCountWithInitialData(){
		int count = pieceService.pieceCount();
		assertEquals(count,1); //importar el tercero
	}
}
