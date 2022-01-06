package org.springframework.samples.upstream.piece;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.upstream.configuration.SecurityConfiguration;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.round.RoundState;
import org.springframework.samples.upstream.tile.Tile;
import org.springframework.samples.upstream.tile.TileService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PieceController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PieceControllerTest {
	
	private static final int TEST_ROUND_ID = 4;
	private static final int TEST_PIECE_ID = 22;
	
	@MockBean
	private PieceService pieceService;
	
	@MockBean
	private TileService tileService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Piece piece;
	private Round round;
	private Tile tile;
	private MovementTypeWrapper movement;
	
	@BeforeEach
	void setup() {
		piece = new Piece();
		round = new Round();
		tile = new Tile();
		tile.setColumnIndex(2);
		tile.setRowIndex(1);
		tile.setId(14);
		round.setRound_state(RoundState.IN_COURSE);
		round.setId(TEST_ROUND_ID);
		piece.setColor(Color.YELLOW);
		piece.setId(TEST_PIECE_ID);
		piece.setNumSalmon(2);
		piece.setStuck(false);
		piece.setRound(round);
		piece.setTile(tile);
		movement = new MovementTypeWrapper(piece, false);
		given(this.pieceService.findPieceById(TEST_PIECE_ID)).willReturn(piece);
		when(this.pieceService.checkUser(piece)).thenReturn(true);
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/piece/{pieceId}/edit", TEST_PIECE_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("movementTypeWrapper"))
				.andExpect(model().attribute("movementTypeWrapper", hasProperty("piece", is(piece))))
				.andExpect(model().attribute("movementTypeWrapper", hasProperty("movementType", is(movement.getMovementType()))))
				.andExpect(view().name("pieces/createOrUpdatePieceForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testSwim() throws Exception {
		mockMvc.perform(post("/piece/{pieceId}/edit", TEST_PIECE_ID)
				.with(csrf())
				.param("piece.tile.rowIndex", "2")
				.param("piece.tile.columnIndex", "1")
				.param("movementType", "false"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds/" + TEST_ROUND_ID));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testJump() throws Exception {
		mockMvc.perform(post("/piece/{pieceId}/edit", TEST_PIECE_ID)
				.with(csrf())
				.param("piece.tile.rowIndex", "2")
				.param("piece.tile.columnIndex", "1")
				.param("movementType", "true"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/rounds/" + TEST_ROUND_ID));
	}
}
