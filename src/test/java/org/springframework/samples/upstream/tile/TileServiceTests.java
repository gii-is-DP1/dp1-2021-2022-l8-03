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
	void shouldFindTileById(){
		Tile tile = this.tileService.findTileById(1);
		assertThat(tile.getColumnIndex()).isEqualTo(1);
	}
	
	@Test
	@Transactional
	void shouldNotFindTileById(){
		Tile tile = this.tileService.findTileById(99);
		assertThat(tile).isEqualTo(null);
	}
	
	@Test
	@Transactional
	void shouldFindByPosition(){
		Tile tile = this.tileService.findByPosition(1, 1, 1);
		assertThat(tile.getId()).isEqualTo(1);
	}
	
	@Test
	@Transactional
	void shouldNotFindByPosition(){
		Tile tile = this.tileService.findByPosition(1, 1, 99);
		assertThat(tile).isEqualTo(null);
	}
	
	@Test
	@Transactional
	void shouldFindByRow(){
		List<Tile> tiles = this.tileService.findByRow(1, 1);
		assertThat(tiles.size()).isEqualTo(3);
	}
	
	@Test
	@Transactional
	void shouldNotFindByRow(){
		List<Tile> tiles = this.tileService.findByRow(1, 99);
		assertThat(tiles.size()).isEqualTo(0);
	}
	
	@Test
	@Transactional
	void shouldFindSeaTilesInRound(){
		List<Tile> tiles = this.tileService.findSeaTilesInRound(1);
		assertThat(tiles.size()).isEqualTo(4);
	}
	
	@Test
	@Transactional
	void shouldNotFindSeaTilesInRound(){
		List<Tile> tiles = this.tileService.findSeaTilesInRound(99);
		assertThat(tiles.size()).isEqualTo(0);
	}
	
	@Test
	@Transactional
	void shouldFindHeronTilesInRound(){
		List<Tile> tiles = this.tileService.findHeronTilesInRound(1);
		boolean heronTiles=true;
		for(Tile t:tiles) {
			if(t.getTileType()!=TileType.HERON) {
				heronTiles=false;
				break;
			}
		}
		assertThat(heronTiles).isEqualTo(true);
	}
	
	@Test
	@Transactional
	void shouldNotFindHeronTilesInRound(){
		List<Tile> tiles = this.tileService.findHeronTilesInRound(99);
		assertThat(tiles.size()).isEqualTo(0);
	}
	
	@Test
	@Transactional
	void shouldFindSpawnTilesInRound(){
		List<Tile> tiles = this.tileService.findSpawnTilesInRound(1);
		boolean spawnTiles=true;
		for(Tile t:tiles) {
			if(t.getTileType()!=TileType.HERON) {
				spawnTiles=false;
				break;
			}
		}
		assertThat(spawnTiles).isEqualTo(true);
	}
	
	@Test
	@Transactional
	void shouldNotFindSpawnTilesInRound(){
		List<Tile> tiles = this.tileService.findSpawnTilesInRound(99);
		assertThat(tiles.size()).isEqualTo(0);
	}
	
	@Test
	@Transactional
	void shouldFindLowestRow() {
		Integer lowestRow = this.tileService.findLowestRow(1);
		assertThat(lowestRow).isEqualTo(1);
	}
	
	@Test
	@Transactional
	void shouldNotFindLowestRow() {
		Integer lowestRow = this.tileService.findLowestRow(99);
		assertThat(lowestRow).isEqualTo(null);
	}
	
	@Test
	@Transactional
	void shouldFindHighestRow() {
		Integer highestRow = this.tileService.findHighestRow(1);
		assertThat(highestRow).isEqualTo(4);
	}
	
	@Test
	@Transactional
	void shouldNotFindHighestRow() {
		Integer highestRow = this.tileService.findHighestRow(99);
		assertThat(highestRow).isEqualTo(null);
	}
	
	//Este test puede fallar si añadimos mas Tiles a la round 1
	@Test
	@Transactional
	void shouldFindTilesInRound() {
		List<Tile> tiles = this.tileService.findTilesInRound(1);
		assertThat(tiles.size()).isEqualTo(12);
	}
	
	@Test
	@Transactional
	void shouldNotFindTilesInRound() {
		List<Tile> tiles = this.tileService.findTilesInRound(99);
		assertThat(tiles.size()).isEqualTo(0);
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
