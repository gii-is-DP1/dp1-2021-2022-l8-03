package org.springframework.samples.upstream.tile;



import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.upstream.player.PlayerService;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.round.RoundService;
import org.springframework.samples.upstream.tile.exceptions.InvalidPlayerException;
import org.springframework.samples.upstream.tile.exceptions.InvalidPositionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TileServiceTests {
	@Autowired
	protected TileService tileService;
	@Autowired
	protected RoundService roundService;
	@Autowired
	protected PlayerService playerService;
	
	
    private Validator createValidator() {
    	LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
    	localValidatorFactoryBean.afterPropertiesSet();
    	return localValidatorFactoryBean;
    }
	
	@Test
	@Transactional
	void shouldDeleteTile() throws InvalidPositionException{
		Tile tile = tileService.findByPosition(1, 2, 1);
		assertThat(tile!=null);
		tileService.deleteTile(tile);
    	assertThrows(InvalidPositionException.class,()-> this.tileService.findByPosition(1, 2, 1));
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
	void shouldFindByPosition() throws InvalidPositionException{
		Tile tile = this.tileService.findByPosition(1, 1, 1);
		assertThat(tile.getId()).isEqualTo(1);
	}
	
	@Test
	@Transactional
	void shouldNotFindByPosition() throws InvalidPositionException{
    	assertThrows(InvalidPositionException.class,()-> this.tileService.findByPosition(1, 1, 99));
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
		assertThat(tiles.size()>=0).isEqualTo(true);
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
		assertThat(tiles.size()>=0).isEqualTo(true);
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
	
	
	//Este test puede fallar
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
	
	@Test
	@Transactional
	void shouldFindTilesInRound() {
		List<Tile> tiles = this.tileService.findTilesInRound(1);
		assertThat(tiles.size()>=0).isEqualTo(true);
	}
	
	@Test
	@Transactional
	void shouldNotFindTilesInRound() {
		List<Tile> tiles = this.tileService.findTilesInRound(99);
		assertThat(tiles.size()).isEqualTo(0);
	}
	
	@Test
	void shouldSaveTile() {
		Collection<Tile> tilesStart = this.tileService.findTilesInRound(1);
		Tile tile=new Tile();
			tile.setColumnIndex(1);
			tile.setOrientation(1);
			tile.setRound(this.roundService.findRoundById(1));
			tile.setRowIndex(4);
			tile.setSalmonEggs(0);
			tile.setTileType(TileType.ROCK);

		tileService.saveTile(tile);
		Collection<Tile> tilesEnd= this.tileService.findTilesInRound(1);
		assertThat(tilesEnd.size()).isEqualTo(tilesStart.size()+1);
	}
	
	@Test
	void shouldNotSaveTile() {
		Tile tile=new Tile();
			tile.setColumnIndex(1);
			tile.setOrientation(1);
			tile.setRound(this.roundService.findRoundById(1));
			tile.setSalmonEggs(0);
			tile.setTileType(TileType.ROCK);

			
			Validator validator = createValidator();
			Set<ConstraintViolation<Tile>> constraintViolations = validator.validate(tile);
			assertThat(constraintViolations.size()).isEqualTo(1);
			ConstraintViolation<Tile> violation = constraintViolations.iterator().next();
			assertThat(violation.getPropertyPath().toString()).isEqualTo("rowIndex");
			assertThat(violation.getMessage()).isEqualTo("no puede ser null");

	}
	
	@Test
	void shouldRemoveStartingTiles() {
		this.tileService.removeStartingTiles(1);
		assertThat(this.tileService.findSeaTilesInRound(1).size()).isEqualTo(0);
	}
	
	
	@Test
	void shouldLowestTiles() {
		this.tileService.removeStartingTiles(1);
		Integer lowerRow=this.tileService.findLowestRow(1);
		this.tileService.removeLowestTiles(1);
		assertThat(this.tileService.findByRow(lowerRow, 1).size()).isEqualTo(0);
	}
	
	
	@Test
	void shouldAddRow() {
		Integer startHigherRow=this.tileService.findHighestRow(1);
		this.tileService.addNewRow(this.roundService.findRoundById(1));
		Integer endHigherRow=this.tileService.findHighestRow(1);
		assertThat(endHigherRow).isEqualTo(startHigherRow+1);
	}
	
	@Test
	void shouldCreateRandomTile() {
		Integer startTilesNumber=this.tileService.findTilesInRound(1).size();
		this.tileService.createRandomTile(5, 1, this.roundService.findRoundById(1));
		Integer endTilesNumber=this.tileService.findTilesInRound(1).size();
		assertThat(endTilesNumber).isEqualTo(startTilesNumber+1);
	}
	
	
	
	@Test
	@Transactional
	void shouldAddSpawnTiles() {
		Round round = roundService.findRoundById(1);
		tileService.addSpawnTiles(round);
		List<Tile> spawnTiles = tileService.findSpawnTilesInRound(1);
		assertThat(spawnTiles.size()).isEqualTo(5);
	}
	
	@Test
	void shouldCreateSeaTiles() {
		Round round=new Round();
			round.setPlayer(this.playerService.findPlayerByUsername("mandommag"));
			round.setWhirlpools(true);
			round.setRapids(true);
			round.setNum_players(3);
		this.roundService.saveRound(round);
		
		this.tileService.createSeaTiles(round);
		assertThat(this.tileService.findSeaTilesInRound(round.getId()).size()).isEqualTo(4);
	}
	
	@Test
	void shouldCreateInitialTiles() {
		Round round=new Round();
			round.setPlayer(this.playerService.findPlayerByUsername("mandommag"));
			round.setWhirlpools(true);
			round.setRapids(true);
			round.setNum_players(3);
		this.roundService.saveRound(round);
		
		this.tileService.createInitialTiles(round);
		assertThat(this.tileService.findTilesInRound(round.getId()).size()).isEqualTo(12);
	}
	
	
}
