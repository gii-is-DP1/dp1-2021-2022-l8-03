package org.springframework.samples.upstream.player;

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
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.upstream.configuration.SecurityConfiguration;
import org.springframework.samples.upstream.user.AuthoritiesService;
import org.springframework.samples.upstream.user.User;
import org.springframework.samples.upstream.user.UserController;
import org.springframework.samples.upstream.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {PlayerController.class, UserController.class}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PlayerControllerTests {
	
	private static final int TEST_PLAYER_ID = 1;
	private static final int TEST_PLAYER_ID2 = 2;
	
	@MockBean
	private PlayerService playerService;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private AuthoritiesService authoritiesService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Player george;
	private Player george2;
	private User userGeorge;
	private User userGeorge2;
	private Pageable pageable;
	private Page<Player> page;
	private Page<Player> pageEmpty;
	private Page<Player> pageWithTwoElements;
	
	@BeforeEach
	void setup() {
		george = new Player();
		userGeorge = new User();
		george.setId(TEST_PLAYER_ID);
		george.setFirstName("George");
		george.setLastName("Franklin");
		george.setEmail("ejemplo@gmail.com");
		userGeorge.setUsername("player1");
		userGeorge.setPassword("0wn3r");
		george.setUser(userGeorge);
		given(this.playerService.findPlayerById(TEST_PLAYER_ID)).willReturn(george);
		given(this.playerService.findPlayerByUsername("player1")).willReturn(george);
		
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
		when(this.playerService.checkAdminAndInitiatedUser("player2")).thenReturn(true);
		
		pageable=PageRequest.of(0, 20);
		
		page=new PageImpl<Player>(List.of(george),pageable,0);
		pageEmpty=new PageImpl<Player>(new ArrayList<Player>(),pageable,0);
		pageWithTwoElements=new PageImpl<Player>(List.of(george,george2),pageable,0);

	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/users/new"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("player"))
				.andExpect(view().name("users/createPlayerForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/users/new")
				.param("firstName", "Juan")
				.param("lastName", "Diaz")
				.param("email", "test@gmail.com")
				.param("username", "testUsername")
				.param("password", "password")
				.with(csrf()))
				.andExpect(status().is3xxRedirection());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/users/new")
				.with(csrf())
				.param("firstName", "Juan")
				.param("lastName", "Diaz"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("player"))
				.andExpect(model().attributeHasFieldErrors("player", "email"))
				.andExpect(view().name("users/createPlayerForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitFindFormSuccess() throws Exception {
		when(this.playerService.checkAdmin()).thenReturn(true);
		mockMvc.perform(get("/players/find"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("player"))
				.andExpect(view().name("players/findPlayers"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitFindFormHasErrors() throws Exception {
		mockMvc.perform(get("/players/find"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("exception"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormNoAdmin() throws Exception {
		mockMvc.perform(get("/players"))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormOneElement() throws Exception {
		when(this.playerService.checkAdmin()).thenReturn(true);
		given(this.playerService.findAllPageable(pageable)).willReturn(page);
		mockMvc.perform(get("/players"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/players/" + TEST_PLAYER_ID));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormEmptyPage() throws Exception {
		when(this.playerService.checkAdmin()).thenReturn(true);
		given(this.playerService.findAllPageable(pageable)).willReturn(pageEmpty);
		mockMvc.perform(get("/players"))
				.andExpect(status().isOk())
				.andExpect(view().name("players/findPlayers"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormMoreThanOneElement() throws Exception {
		when(this.playerService.checkAdmin()).thenReturn(true);
		given(this.playerService.findAllPageable(pageable)).willReturn(pageWithTwoElements);
		mockMvc.perform(get("/players"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("esPrimera"))
				.andExpect(model().attributeExists("esUltima"))
				.andExpect(view().name("players/playersList"));
	}
		
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormByLastName() throws Exception {
		when(this.playerService.checkAdmin()).thenReturn(true);
		given(this.playerService.findPlayerByLastName(george.getLastName())).willReturn(Lists.newArrayList(george));
		given(this.playerService.findPlayerByLastNamePageable(george.getLastName(), pageable)).willReturn(page);
		mockMvc.perform(get("/players").param("lastName", "Franklin")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/players/" + TEST_PLAYER_ID));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdatePlayerForm() throws Exception {
		when(this.playerService.checkAdminAndInitiatedUser("player1")).thenReturn(true);
		mockMvc.perform(get("/players/{playerId}/edit", TEST_PLAYER_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("player"))
				.andExpect(model().attribute("player", hasProperty("lastName", is("Franklin"))))
				.andExpect(model().attribute("player", hasProperty("firstName", is("George"))))
				.andExpect(model().attribute("player", hasProperty("email", is("ejemplo@gmail.com"))))
				.andExpect(view().name("players/createOrUpdatePlayerForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdatePlayerFormNoPermission() throws Exception {
		mockMvc.perform(get("/players/{playerId}/edit", TEST_PLAYER_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdatePlayerFormSuccess() throws Exception {
		mockMvc.perform(post("/players/{playerId}/edit", TEST_PLAYER_ID).with(csrf()).param("firstName", "Juan")
				.param("lastName", "Diaz").param("email", "ejemplo2@gmail.com")
				.param("user.password", "password").param("user.username", "player1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/players/{playerId}"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdatePlayerFormHasErrors() throws Exception {
		mockMvc.perform(post("/players/{playerId}/edit", TEST_PLAYER_ID).with(csrf()).param("firstName", "Juan")
				.param("lastName", "Diaz")).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("player"))
				.andExpect(model().attributeHasFieldErrors("player", "email"))
				.andExpect(view().name("players/createOrUpdatePlayerForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testDeletePlayerSuccess() throws Exception {
		mockMvc.perform(get("/players/delete/{playerId}", TEST_PLAYER_ID)).andExpect(status().isOk())
				.andExpect(view().name("/players/playersList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testDeletePlayerNotFound() throws Exception {
		mockMvc.perform(get("/players/delete/{playerId}", 99))
				.andExpect(status().isOk())
				.andExpect(view().name("/players/playersList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowPlayer() throws Exception {
		when(this.playerService.checkAdminAndInitiatedUser("player1")).thenReturn(true);
		mockMvc.perform(get("/players/{playerId}", TEST_PLAYER_ID))
				.andExpect(status().isOk())
				.andExpect(model().attribute("player", hasProperty("lastName", is("Franklin"))))
				.andExpect(model().attribute("player", hasProperty("firstName", is("George"))))
				.andExpect(model().attribute("player", hasProperty("email", is("ejemplo@gmail.com"))))
				.andExpect(view().name("players/playerDetails"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowPlayerNoPermission() throws Exception {
		mockMvc.perform(get("/players/{playerId}", TEST_PLAYER_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("exception"));
	}
	
	@WithMockUser(value = "spring",username="player1")
	@Test
	void testShowPlayerDetails() throws Exception {
		mockMvc.perform(get("/players/playerDetails"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/players/" + TEST_PLAYER_ID));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowPlayerAuditNoAdmin() throws Exception {
		when(this.playerService.checkAdmin()).thenReturn(false);
		mockMvc.perform(get("/players/11/audit"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowPlayerAudit() throws Exception {
		when(this.playerService.checkAdmin()).thenReturn(true);
		mockMvc.perform(get("/players/" + TEST_PLAYER_ID + "/audit"))
			.andExpect(status().isOk())
			.andExpect(view().name("players/playerAudit"));
	}
}
