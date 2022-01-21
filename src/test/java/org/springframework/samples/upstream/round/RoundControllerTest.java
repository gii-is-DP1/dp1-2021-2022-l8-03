package org.springframework.samples.upstream.round;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.upstream.actingPlayer.ActingPlayerService;
import org.springframework.samples.upstream.configuration.SecurityConfiguration;
import org.springframework.samples.upstream.piece.Color;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.piece.PieceService;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.player.PlayerService;
import org.springframework.samples.upstream.player.exceptions.NoPermissionException;
import org.springframework.samples.upstream.round.exceptions.FullRoundException;
import org.springframework.samples.upstream.round.exceptions.InvalidRoundException;
import org.springframework.samples.upstream.round.exceptions.NotYourRoundException;
import org.springframework.samples.upstream.round.exceptions.PlayerOtherRoundException;
import org.springframework.samples.upstream.salmonBoard.SalmonBoard;
import org.springframework.samples.upstream.salmonBoard.SalmonBoardService;
import org.springframework.samples.upstream.score.ScoreService;
import org.springframework.samples.upstream.tile.Tile;
import org.springframework.samples.upstream.tile.TileService;
import org.springframework.samples.upstream.tile.TileType;
import org.springframework.samples.upstream.user.User;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers=RoundController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class RoundControllerTest {
	
	private static final int TEST_ROUND_ID=1;
	private static final int TEST_PLAYER_ID=1;
	private static final int TEST_PLAYER_ID2=2;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private RoundService roundService;
	
	@MockBean
	private PlayerService playerService;
	
	@MockBean
	private TileService tileService;
	
	@MockBean
	private PieceService pieceService;
	
	@MockBean
	private ScoreService scoreService;
	
	@MockBean
	private SalmonBoardService salmonBoardService;
	
	@MockBean
	private ActingPlayerService actingPlayerService;
	
	private Round round;
	private Player george;
	private User userGeorge;
	private Player george2;
	private User userGeorge2;
	private Tile tile;
	private Piece piece;
	private SalmonBoard salmonBoard;
	
	@BeforeEach
	void setup() {
		george = new Player();
		userGeorge = new User();
		george.setId(TEST_PLAYER_ID);
		george.setFirstName("George");
		george.setLastName("Franklin");
		george.setEmail("ejemplo@gmail.com");
		george.setRound(this.roundService.findRoundById(TEST_ROUND_ID));
		userGeorge.setUsername("player1");
		userGeorge.setPassword("0wn3r");
		george.setUser(userGeorge);
		given(this.playerService.findPlayerById(TEST_PLAYER_ID)).willReturn(george);
		given(this.playerService.getPlayerColor(TEST_PLAYER_ID)).willReturn(Color.BLACK);
		given(this.playerService.findPlayerByUsername("player1")).willReturn(george);
		when(this.playerService.checkAdminAndInitiatedUserBoolean("player1")).thenReturn(true);
		
		george2 = new Player();
		userGeorge2 = new User();
		george2.setId(TEST_PLAYER_ID2);
		george2.setFirstName("George2");
		george2.setLastName("Franklin2");
		george2.setEmail("ejemplo2@gmail.com");
		userGeorge2.setUsername("player2");
		userGeorge2.setPassword("0wn3r2");
		george2.setUser(userGeorge2);
		given(this.playerService.findPlayerById(TEST_PLAYER_ID2)).willReturn(george2);
		when(this.playerService.checkAdminAndInitiatedUserBoolean("player2")).thenReturn(true);
		
		
		
		List<Player> players=new ArrayList<Player>();
		players.add(george);
		round=new Round();
		round.setId(TEST_ROUND_ID);
		round.setNum_players(3);
		round.setPlayer(george);
		round.setPlayers(players);
		round.setRapids(true);
		round.setWhirlpools(true);
		round.setRound_state(RoundState.CREATED);
		tileService.createInitialTiles(round);
		pieceService.createPlayerPieces(george, round);
		given(this.roundService.findRoundById(TEST_ROUND_ID)).willReturn(round);
		
		
		tile=new Tile();
		tile.setId(1);
		tile.setColumnIndex(1);
		tile.setOrientation(0);
		tile.setRound(round);
		tile.setRowIndex(1);
		tile.setSalmonEggs(0);
		tile.setTileType(TileType.SEA);
		
		piece=new Piece();
		piece.setId(1);
		piece.setNumSalmon(2);
		piece.setPlayer(george);
		piece.setRound(round);
		piece.setStuck(false);
		
		salmonBoard=new SalmonBoard();
		salmonBoard.setId(1);
		salmonBoard.setRound(round);
		given(this.salmonBoardService.findByRoundId(TEST_ROUND_ID)).willReturn(salmonBoard);
		
		Collection<Piece> pieces=new ArrayList<Piece>();
		Collection<Round> rounds=new ArrayList<Round>();
		Collection<Tile> tiles=new ArrayList<Tile>();
		
		tiles.add(tile);
		rounds.add(round);
		pieces.add(piece);
		
		piece.setTile(tile);
		tile.setPieces(pieces);
		round.setPieces(pieces);
		round.setTiles(tiles);
		george.setPieces(pieces);
		george.setRounds(rounds);
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/rounds/new"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("round"))
				.andExpect(view().name("rounds/createOrUpdateRoundForm"));
	}
	
	@WithMockUser(value = "spring",username="player1")
	@Test
	void testInitCreationFormException() throws Exception {
		Mockito.doThrow(PlayerOtherRoundException.class).when(this.roundService).checkPlayerInRound(george);
		mockMvc.perform(get("/rounds/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/rounds/new")
				.with(csrf())
				.param("Id", "2")
				.param("whirlpools", "true")
				.param("rapids", "true")
				.param("num_players", "3")
				.param("round_state","CREATED"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds/2"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/rounds/new")
				.with(csrf())
				.param("whirlpools", "true")
				.param("rapids", "true")
				.param("round_state","CREATED"))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("round"))
				.andExpect(model().attributeHasFieldErrors("round", "num_players"))
				.andExpect(view().name("rounds/createOrUpdateRoundForm"));
	}
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testProcessCreationFormException() throws Exception {
		Mockito.doThrow(PlayerOtherRoundException.class).when(this.roundService).checkPlayerInRound(george);
		mockMvc.perform(post("/rounds/new")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	@WithMockUser(value = "spring", username = "player1")
	@Test
	void testProcessFindForm() throws Exception {
		mockMvc.perform(get("/rounds"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rounds"))
				.andExpect(model().attributeExists("isFinished"))
				.andExpect(view().name("rounds/roundList"));
	}
	
	@WithMockUser(value = "spring", username = "player1")
	@Test
	void testProcessFindFormPlayerInRound() throws Exception {
		george.setRound(round);
		mockMvc.perform(get("/rounds"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds/"+TEST_ROUND_ID));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindInCourse() throws Exception {
		when(this.playerService.checkAdminBoolean()).thenReturn(true);
		mockMvc.perform(get("/rounds/inCourse"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rounds"))
				.andExpect(model().attributeExists("isFinished"))
				.andExpect(view().name("rounds/roundList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindInCourseNoAdmin() throws Exception {
		Mockito.doThrow(NoPermissionException.class).when(this.playerService).checkAdmin();
		mockMvc.perform(get("/rounds/inCourse"))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFinished() throws Exception {
		when(this.playerService.checkAdminBoolean()).thenReturn(true);
		mockMvc.perform(get("/rounds/finished"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rounds"))
				.andExpect(model().attributeExists("isFinished"))
				.andExpect(view().name("rounds/roundList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFinishedNoAdmin() throws Exception {
		Mockito.doThrow(NoPermissionException.class).when(this.playerService).checkAdmin();
		mockMvc.perform(get("/rounds/finished"))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testJoinRoundSuccess() throws Exception{
		mockMvc.perform(get("/rounds/join/{roundId}",TEST_ROUND_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds/"+TEST_ROUND_ID));
	}
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testJoinRoundHasErrors() throws Exception{
		mockMvc.perform(get("/rounds/join/{roundId}",99))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("exception"));
	}
	
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testJoinRoundExceptionInvalidRound() throws Exception{
		Mockito.doThrow(InvalidRoundException.class).when(this.roundService).checkRoundExist(null);
		mockMvc.perform(get("/rounds/join/{roundId}",99))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testJoinRoundExceptionFullRound() throws Exception{
		List<Player> players=round.getPlayers();
		players.add(this.playerService.findPlayerByUsername("mandommag"));players.add(this.playerService.findPlayerByUsername("sonlaumot"));
		this.roundService.saveRound(round);
		
		Mockito.doThrow(FullRoundException.class).when(this.roundService).checkRoundCapacity(round);
		mockMvc.perform(get("/rounds/join/{roundId}",TEST_ROUND_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testJoinRoundExceptionPlayerOtherRound() throws Exception{
		Mockito.doThrow(PlayerOtherRoundException.class).when(this.roundService).checkPlayerInRound(george);
		mockMvc.perform(get("/rounds/join/{roundId}",TEST_ROUND_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	
	
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testCreatorLeaveRoundSuccess() throws Exception{
		mockMvc.perform(get("/rounds/leave/{roundId}",TEST_ROUND_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds"));
	}
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testLeaveRoundSuccess() throws Exception{
		round.setPlayer(null);
		mockMvc.perform(get("/rounds/leave/{roundId}",TEST_ROUND_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds"));
	}
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testLeaveRoundFinishedSuccess() throws Exception{
		round.setRound_state(RoundState.FINISHED);
		mockMvc.perform(get("/rounds/leave/{roundId}",TEST_ROUND_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds"));
	}
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testLeaveRoundInvalidRoundException() throws Exception{
		round.setPlayers(new ArrayList<Player>());
		Mockito.doThrow(InvalidRoundException.class).when(this.roundService).checkRoundExist(null);
		mockMvc.perform(get("/rounds/leave/{roundId}",99))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testLeaveRoundNotYourRoundException() throws Exception{
		george.setRound(null);
		round.setPlayers(new ArrayList<Player>());
		Mockito.doThrow(NotYourRoundException.class).when(this.roundService).checkPlayerInRound(round, george);
		mockMvc.perform(get("/rounds/leave/{roundId}",TEST_ROUND_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testStartRoundSuccess() throws Exception{
		mockMvc.perform(get("/rounds/start/{roundId}",TEST_ROUND_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds/"+TEST_ROUND_ID));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testStartRoundSuccess2() throws Exception{
		List<Player> players=round.getPlayers();
		players.add(george2);
		this.roundService.saveRound(round);
		mockMvc.perform(get("/rounds/start/{roundId}",TEST_ROUND_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds/"+TEST_ROUND_ID));
	}
	
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testShowRoundCreated() throws Exception{
		mockMvc.perform(get("/rounds/{roundId}",TEST_ROUND_ID))
				.andExpect(model().attribute("round", hasProperty("id",is(TEST_ROUND_ID))))
				.andExpect(model().attribute("round", hasProperty("player",is(george))))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("rounds/roundWaitingRoom"));
	}
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testShowRoundInCourse() throws Exception{
		round.setRound_state(RoundState.IN_COURSE);
		mockMvc.perform(get("/rounds/{roundId}",TEST_ROUND_ID))
				.andExpect(model().attribute("player", hasProperty("id",is(TEST_PLAYER_ID))))
				.andExpect(model().attribute("player", hasProperty("firstName",is("George"))))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("rounds/roundDetails"));
	}
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testShowRoundInCourseWithoutPieces() throws Exception{
		round.setRound_state(RoundState.IN_COURSE);
		george.setPieces(new ArrayList<Piece>());
		mockMvc.perform(get("/rounds/{roundId}",TEST_ROUND_ID))
				.andExpect(model().attribute("player", hasProperty("id",is(TEST_PLAYER_ID))))
				.andExpect(model().attribute("player", hasProperty("firstName",is("George"))))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("rounds/roundDetails"));
	}
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testShowRoundFinished() throws Exception{
		round.setRound_state(RoundState.FINISHED);
		mockMvc.perform(get("/rounds/{roundId}",TEST_ROUND_ID))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("rounds/roundScore"));
	}
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testShowRoundInvalidRoundException() throws Exception{
		Mockito.doThrow(InvalidRoundException.class).when(this.roundService).checkRoundExist(null);
		mockMvc.perform(get("/rounds/{roundId}",99))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
}
