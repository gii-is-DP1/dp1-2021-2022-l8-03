package org.springframework.samples.upstream.tile;



import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.round.RoundService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TileServiceTests {
	@Autowired
	protected TileService tileService;
	@Autowired
	protected RoundService roundService;
	
	@Test
	@Transactional
	void shouldDeleteTile(){
		Tile tile = tileService.findByPosition(1, 2, 1);
		tileService.deleteTile(tile);
		tile = tileService.findByPosition(1, 2, 1);
		assertThat(tile).isEqualTo(null);
	}
	
	@Test
	@Transactional
	void shouldAddSpawnTiles() {
		Round round = roundService.findRoundById(1);
		tileService.addSpawnTiles(round);
		List<Tile> spawnTiles = tileService.findSpawnTilesInRound(1);
		assertThat(spawnTiles.size()).isEqualTo(5);
	}
}
