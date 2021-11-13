package org.springframework.samples.upstream.tile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class RapidsTileServiceTest {
	
	@Autowired
	private RapidsTileService rapidsTileService;	
	
	@Test
	public void testCountWithInititalData() {
		int count=rapidsTileService.rapidsTileCount();
		assertEquals(count,0);
	}

}