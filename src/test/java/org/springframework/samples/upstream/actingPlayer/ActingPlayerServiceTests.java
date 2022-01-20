package org.springframework.samples.upstream.actingPlayer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;


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
import org.springframework.security.test.context.support.WithMockUser;
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
	@WithMockUser(username = "admin")
	void shouldChangeTurn() {
		Round round = this.roundService.findRoundById(5);
		round.setNum_players(3);
		Player player1 = this.playerService.findPlayerById(11);
		Player player2 = this.playerService.findPlayerById(14);
		Player player3 = this.playerService.findPlayerById(15);
		player1.setRound(round);
		this.playerService.savePlayer(player1);
		player2.setRound(round);
		this.playerService.savePlayer(player2);
		player3.setRound(round);
		this.playerService.savePlayer(player3);
		this.actingPlayerService.createActingPlayerToRound(round);
		ActingPlayer actingPlayer = round.getActingPlayer();
		this.tileService.createInitialTiles(round);
		this.pieceService.createPlayerPieces(player1, round);
		this.pieceService.createPlayerPieces(player2, round);
		this.pieceService.createPlayerPieces(player3, round);
		this.actingPlayerService.changeTurn(actingPlayer);
		assertThat(actingPlayer.getPlayer()).isEqualTo(1);
		assertThat(actingPlayer.getTurn()).isEqualTo(1);
		
		actingPlayer.setPlayer(2);
		
		this.actingPlayerService.changeTurn(actingPlayer);
		assertThat(actingPlayer.getPlayer()).isEqualTo(1);
		assertThat(actingPlayer.getTurn()).isEqualTo(2);
	}
	
	@Test
	@WithMockUser(username = "admin")
	void shouldChangeTurnTwoPlayers() {
		Round round = this.roundService.findRoundById(5);
		Player player1 = this.playerService.findPlayerById(11);
		Player player2 = this.playerService.findPlayerById(14);
		player1.setRound(round);
		this.playerService.savePlayer(player1);
		player2.setRound(round);
		this.playerService.savePlayer(player2);
		this.actingPlayerService.createActingPlayerToRound(round);
		ActingPlayer actingPlayer = round.getActingPlayer();
		this.tileService.createInitialTiles(round);
		this.pieceService.createPlayerPieces(player1, round);
		this.pieceService.createPlayerPieces(player2, round);
		this.actingPlayerService.changeTurnTwoPlayers(actingPlayer);
		assertThat(actingPlayer.getPlayer()).isEqualTo(1);
		assertThat(actingPlayer.getTurn()).isEqualTo(1);
		
		this.actingPlayerService.changeTurnTwoPlayers(actingPlayer);
		assertThat(actingPlayer.getPlayer()).isEqualTo(0);
		assertThat(actingPlayer.getTurn()).isEqualTo(2);
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
    
    @Test
    void shouldUnstuckPieces() {
        Piece piece = this.pieceService.findPieceById(1);
        piece.setStuck(true);
        this.pieceService.save(piece);
        piece = this.pieceService.findPieceById(1);
        assertThat(piece.getStuck()).isEqualTo(true);

        this.actingPlayerService.unstuckPieces(piece.getRound());
        piece = this.pieceService.findPieceById(1);
        assertThat(piece.getStuck()).isEqualTo(false);
    }
}
