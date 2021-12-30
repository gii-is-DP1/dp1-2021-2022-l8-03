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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.upstream.configuration.SecurityConfiguration;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.piece.PieceService;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.player.PlayerService;
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
	
	private Round round;
	private Player george;
	private User userGeorge;
	private Tile tile;
	private Piece piece;
	
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
		given(this.playerService.findPlayerByUsername("player1")).willReturn(george);
		when(this.playerService.checkAdminAndInitiatedUser("player1")).thenReturn(true);
		
		
		
		Collection<Player> players=new ArrayList<Player>();
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
	
	//Habría que buscar la forma de pasarle un player para asi poder cubrir el método completo
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/rounds/new")
				.with(csrf())
				.param("whirlpools", "true")
				.param("rapids", "true")
				.param("num_players", "3")
				.param("round_state","CREATED"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds/"));
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
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindForm() throws Exception {
		mockMvc.perform(get("/rounds"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rounds"))
				.andExpect(model().attributeExists("esFinished"))
				.andExpect(view().name("rounds/roundList"));
	}
	
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindInCourse() throws Exception {
		when(this.playerService.checkAdmin()).thenReturn(true);
		mockMvc.perform(get("/rounds/inCourse"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rounds"))
				.andExpect(model().attributeExists("esFinished"))
				.andExpect(view().name("rounds/roundList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindInCourseNoAdmin() throws Exception {
		mockMvc.perform(get("/rounds/inCourse"))
				.andExpect(status().is2xxSuccessful());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFinished() throws Exception {
		when(this.playerService.checkAdmin()).thenReturn(true);
		mockMvc.perform(get("/rounds/finished"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rounds"))
				.andExpect(model().attributeExists("esFinished"))
				.andExpect(view().name("rounds/roundList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFinishedNoAdmin() throws Exception {
		mockMvc.perform(get("/rounds/finished"))
				.andExpect(status().is2xxSuccessful());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateRoundForm() throws Exception {
		mockMvc.perform(get("/rounds/{roundId}/edit", TEST_ROUND_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("round"))
				.andExpect(model().attribute("round", hasProperty("rapids", is(true))))
				.andExpect(model().attribute("round", hasProperty("whirlpools", is(true))))
				.andExpect(model().attribute("round", hasProperty("round_state", is(RoundState.CREATED))))
				.andExpect(model().attribute("round", hasProperty("num_players", is(3))))
				.andExpect(model().attribute("round", hasProperty("player", is(george))))
				.andExpect(view().name("rounds/createOrUpdateRoundForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdatePlayerFormSuccess() throws Exception {
		mockMvc.perform(post("/rounds/{roundId}/edit", TEST_ROUND_ID)
				.with(csrf())
				.param("rapids", "false")
				.param("whirlpools", "true")
				.param("round_state", "CREATED")
				.param("num_players", "3"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdatePlayerFormHasErrors() throws Exception {
		mockMvc.perform(post("/rounds/{roundId}/edit", TEST_ROUND_ID)
				.with(csrf())
				.param("rapids", "false")
				.param("whirlpools", "true"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("round"))
				.andExpect(model().attributeHasFieldErrors("round", "num_players"))
				.andExpect(view().name("rounds/createOrUpdateRoundForm"));
	}
	

	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testJoinRoundSuccess() throws Exception{
		mockMvc.perform(get("/rounds/join/{roundId}",TEST_ROUND_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds/{roundId}"));
	}
	
	@WithMockUser(username="player1",value = "spring")
	@Test
	void testJoinRoundHasErrors() throws Exception{
		mockMvc.perform(get("/rounds/join/{roundId}",99))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds/oups"));
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
	void testLeaveRoundHasErrors() throws Exception{
		round.setPlayers(new ArrayList<Player>());
		mockMvc.perform(get("/rounds/leave/{roundId}",TEST_ROUND_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds/oups"));
	}
	
//	@WithMockUser(username="player1",value = "spring")
//	@Test
//	void testLeaveRoundHasErrors() throws Exception{
//		mockMvc.perform(get("/rounds/join/{roundId}",99))
//		.andExpect(status().is3xxRedirection())
//		.andExpect(view().name("redirect:/rounds/oups"));
//	}
	
	
	
	
}
