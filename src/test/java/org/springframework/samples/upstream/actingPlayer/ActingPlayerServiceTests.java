package org.springframework.samples.upstream.actingPlayer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.piece.PieceService;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.player.PlayerService;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.round.RoundService;
import org.springframework.samples.upstream.round.RoundState;
import org.springframework.samples.upstream.tile.Tile;
import org.springframework.samples.upstream.tile.TileService;
import org.springframework.samples.upstream.tile.TileType;
import org.springframework.samples.upstream.user.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ActingPlayerServiceTests {
	@Autowired
	ActingPlayerService actingPlayerService;
	@Autowired
	RoundService roundService;
	@Autowired
	TileService tileService;
	@Autowired
	PieceService pieceService;
	@Autowired
	PlayerService playerService;
	
	private Player george;
    private User userGeorge;
    private Tile spawnTileOne;
    private Tile spawnTileTwo;
    private Tile nonSpawnTile;
    private Piece piece;
    
    @BeforeEach
    void setUp(){
    	Round round = this.roundService.findRoundById(2);
		george = new Player();
		userGeorge = new User();
		spawnTileOne = new Tile();
		spawnTileTwo = new Tile();
		nonSpawnTile = new Tile();
		piece = new Piece();
		george.setId(1);
		george.setFirstName("George");
		george.setLastName("Franklin");
		george.setEmail("ejemplo@gmail.com");
		userGeorge.setUsername("player1");
		userGeorge.setPassword("0wn3r");
		george.setUser(userGeorge);
		
		piece.setId(100);
		piece.setNumSalmon(2);
		piece.setPlayer(george);
		piece.setRound(round);
		piece.setStuck(false);
		
		spawnTileOne.setColumnIndex(2);
		spawnTileOne.setOrientation(1);
		spawnTileOne.setRowIndex(1);
		spawnTileOne.setSalmonEggs(1);
		spawnTileOne.setTileType(TileType.SPAWN);
		spawnTileOne.setId(100);
		spawnTileOne.setPieces(new ArrayList<Piece>());
		spawnTileOne.setRound(round);
		spawnTileOne.getPieces().add(piece);
		
		spawnTileTwo.setColumnIndex(2);
		spawnTileTwo.setOrientation(1);
		spawnTileTwo.setRowIndex(1);
		spawnTileTwo.setSalmonEggs(2);
		spawnTileTwo.setTileType(TileType.SPAWN);
		spawnTileTwo.setRound(round);
		spawnTileTwo.setId(101);
		spawnTileTwo.setPieces(new ArrayList<Piece>());
		
		nonSpawnTile.setSalmonEggs(0);
		nonSpawnTile.setId(102);
		nonSpawnTile.setTileType(TileType.WATER);
		nonSpawnTile.setColumnIndex(2);
		nonSpawnTile.setRowIndex(2);
		nonSpawnTile.setOrientation(1);
		nonSpawnTile.setRound(round);
		nonSpawnTile.setPieces(new ArrayList<Piece>());
		
		piece.setTile(spawnTileOne);
		tileService.saveTile(nonSpawnTile);
		tileService.saveTile(spawnTileOne);
		tileService.saveTile(spawnTileTwo);
    }
	
	@Test
	void shouldFindActingPlayerById() {
		ActingPlayer actingPlayer = this.actingPlayerService.findActingPlayerById(1);
		assertThat(actingPlayer.getId()).isEqualTo(1);
	}
	
	@Test
	void shouldFindActingPlayerByRound() {
		ActingPlayer actingPlayer = this.actingPlayerService.findActingPlayerByRound(1);
		assertThat(actingPlayer.getRound().getId()).isEqualTo(1);
	}
	
	@Test
	void shouldSaveActingPlayer() {
		Round round =  this.roundService.findRoundById(2);
		ActingPlayer actingPlayer = new ActingPlayer();
		actingPlayer.setFirstPlayer(0);
		actingPlayer.setPlayer(0);
		actingPlayer.setId(2);
		actingPlayer.setPoints(5);
		actingPlayer.setRound(round);
		actingPlayer.setTurn(1);
		this.actingPlayerService.saveActingPlayer(actingPlayer);
		actingPlayer = this.actingPlayerService.findActingPlayerById(2);
		assertThat(actingPlayer).isNotEqualTo(null);
	}
	
	@Test
	void shouldCreateActingPlayerToRound() {
		Round round = this.roundService.findRoundById(2);
		this.actingPlayerService.createActingPlayerToRound(round);
		assertThat(round.getActingPlayer()).isNotEqualTo(null);
	}
	
	@Test
	void shouldChangeTurn() {
		Round round = this.roundService.findRoundById(5);
		round.setNum_players(3);
		this.actingPlayerService.createActingPlayerToRound(round);
		ActingPlayer actingPlayer = round.getActingPlayer();
		this.tileService.createInitialTiles(round);
		this.actingPlayerService.changeTurn(actingPlayer);
		assertThat(actingPlayer.getPlayer()).isEqualTo(1);
		assertThat(actingPlayer.getTurn()).isEqualTo(1);
		
		actingPlayer.setPlayer(2);
		
		this.actingPlayerService.changeTurn(actingPlayer);
		assertThat(actingPlayer.getPlayer()).isEqualTo(1);
		assertThat(actingPlayer.getTurn()).isEqualTo(2);
		
		actingPlayer.setPlayer(0);
		
		List<Tile> startingTiles = this.tileService.findSeaTilesInRound(round.getId());
		assertThat(startingTiles.size()).isEqualTo(4);
		this.actingPlayerService.changeTurn(actingPlayer);
		startingTiles = this.tileService.findSeaTilesInRound(round.getId());
		assertThat(startingTiles.size()).isEqualTo(0);		
		
		actingPlayer.setPlayer(1);
		Integer lowestRow = this.tileService.findLowestRow(5);
		this.actingPlayerService.changeTurn(actingPlayer);
		Integer newLowestRow = this.tileService.findLowestRow(5);
		assertThat(newLowestRow).isEqualTo(lowestRow+1);
		
		actingPlayer.setTurn(8);
		actingPlayer.setPlayer(2);
		List<Tile> noSpawnTiles = this.tileService.findSpawnTilesInRound(5);
		this.actingPlayerService.changeTurn(actingPlayer);
		List<Tile> spawnTiles = this.tileService.findSpawnTilesInRound(5);
		assertThat(noSpawnTiles.size()).isEqualTo(0);
		assertThat(spawnTiles.size()).isEqualTo(5);
	}
	
	@Test
	void shouldChangeTurnTwoPlayers() {
		Round round = this.roundService.findRoundById(5);
		this.actingPlayerService.createActingPlayerToRound(round);
		ActingPlayer actingPlayer = round.getActingPlayer();
		this.tileService.createInitialTiles(round);
		this.actingPlayerService.changeTurnTwoPlayers(actingPlayer);
		assertThat(actingPlayer.getPlayer()).isEqualTo(1);
		assertThat(actingPlayer.getTurn()).isEqualTo(1);
		
		this.actingPlayerService.changeTurnTwoPlayers(actingPlayer);
		assertThat(actingPlayer.getPlayer()).isEqualTo(0);
		assertThat(actingPlayer.getTurn()).isEqualTo(2);
		
		actingPlayer.setPlayer(1);
		
		List<Tile> startingTiles = this.tileService.findSeaTilesInRound(round.getId());
		assertThat(startingTiles.size()).isEqualTo(4);
		this.actingPlayerService.changeTurnTwoPlayers(actingPlayer);
		startingTiles = this.tileService.findSeaTilesInRound(round.getId());
		assertThat(startingTiles.size()).isEqualTo(0);		
		
		actingPlayer.setPlayer(1);
		Integer lowestRow = this.tileService.findLowestRow(5);
		this.actingPlayerService.changeTurnTwoPlayers(actingPlayer);
		Integer newLowestRow = this.tileService.findLowestRow(5);
		assertThat(newLowestRow).isEqualTo(lowestRow+1);
		
		actingPlayer.setTurn(8);
		actingPlayer.setPlayer(1);
		List<Tile> noSpawnTiles = this.tileService.findSpawnTilesInRound(5);
		this.actingPlayerService.changeTurnTwoPlayers(actingPlayer);
		List<Tile> spawnTiles = this.tileService.findSpawnTilesInRound(5);
		assertThat(noSpawnTiles.size()).isEqualTo(0);
		assertThat(spawnTiles.size()).isEqualTo(5);
	}
	
	@Test
	void shouldAdvanceSpawnTiles() {
		Round round = this.roundService.findRoundById(2);
		Piece piece = this.pieceService.findPiecesInSpawnTiles(2).get(0);
		
		assertThat(piece.getTile().getSalmonEggs()).isEqualTo(1);
		
		this.actingPlayerService.advanceSpawnTilePieces(round);
		
		piece = this.pieceService.findPiecesInSpawnTiles(2).get(0);
		assertThat(piece.getTile().getSalmonEggs()).isEqualTo(2);
	}
	
	@Test
	void shouldCheckPiecesInSpawnTiles() {
		Round round = this.roundService.findRoundById(5);
		piece.setRound(round);
		round.getPieces().add(piece);
		Boolean flag = this.actingPlayerService.checkPiecesInSpawnTiles(round);
		assertThat(flag).isEqualTo(true);
		
		round.getPieces().remove(piece);
		piece.setTile(nonSpawnTile);
		round.getPieces().add(piece);
		flag = this.actingPlayerService.checkPiecesInSpawnTiles(round);
		assertThat(flag).isEqualTo(false);
	}
	
	@Test
	void shouldEndTheGame() {
		Round round = this.roundService.findRoundById(2);
		this.actingPlayerService.endTheGame(round);
		assertThat(round.getRound_state()).isEqualTo(RoundState.FINISHED);
	}
	
    @Test
    void shouldHaveMovablePieces() {
    	Player player = this.playerService.findPlayerById(11);
    	Boolean bool = this.actingPlayerService.checkMovablePieces(player);
    	assertThat(bool).isEqualTo(false);
    }
}
