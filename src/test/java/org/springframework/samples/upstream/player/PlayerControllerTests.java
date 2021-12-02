package org.springframework.samples.upstream.player;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.springframework.samples.upstream.user.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.upstream.configuration.SecurityConfiguration;
import org.springframework.samples.upstream.user.AuthoritiesService;
import org.springframework.samples.upstream.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PlayerController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PlayerControllerTests {
	
	private static final int TEST_PLAYER_ID = 1;
	
	@Autowired
	private PlayerController playerController;
	
	@MockBean
	private PlayerService playerService;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private AuthoritiesService authoritiesService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Player george;
	private User userGeorge;
	
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
		when(this.playerService.checkAdminAndInitiatedUser("player1")).thenReturn(true);
		when(this.playerService.checkAdmin()).thenReturn(true);
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/players/new")).andExpect(status().isOk()).andExpect(model().attributeExists("player"))
				.andExpect(view().name("players/createOrUpdatePlayerForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/players/new").param("firstName", "Juan").param("lastName", "Diaz").with(csrf())
				.param("email", "test@gmail.com")).andExpect(status().is3xxRedirection());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/players/new").with(csrf()).param("firstName", "Juan").param("lastName", "Diaz"))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("player"))
				.andExpect(model().attributeHasFieldErrors("player", "email"))
				.andExpect(view().name("players/createOrUpdatePlayerForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitFindForm() throws Exception {
		mockMvc.perform(get("/players/find")).andExpect(status().isOk()).andExpect(model().attributeExists("player"))
				.andExpect(view().name("players/findPlayers"));
	}
	
	@Disabled
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormSuccess() throws Exception {
		given(this.playerService.findPlayerByLastName("")).willReturn(Lists.newArrayList(george, new Player()));
		mockMvc.perform(get("/players")).andExpect(status().isOk()).andExpect(view().name("players/playersList"));
	}
	
	@Disabled
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormByLastName() throws Exception {
		given(this.playerService.findPlayerByLastName(george.getLastName())).willReturn(Lists.newArrayList(george));
		mockMvc.perform(get("/players").param("lastName", "Franklin")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/players/" + TEST_PLAYER_ID));
	}
	
	@Disabled
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormNoPlayersFound() throws Exception {
		mockMvc.perform(get("/players").param("lastName", "Unknown Surname")).andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors("player", "lastName"))
				.andExpect(model().attributeHasFieldErrorCode("player", "lastName", "notFound"))
				.andExpect(view().name("players/findPlayers"));
	}
	
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdatePlayerForm() throws Exception {
		mockMvc.perform(get("/players/{playerId}/edit", TEST_PLAYER_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("player"))
				.andExpect(model().attribute("player", hasProperty("lastName", is("Franklin"))))
				.andExpect(model().attribute("player", hasProperty("firstName", is("George"))))
				.andExpect(model().attribute("player", hasProperty("email", is("ejemplo@gmail.com"))))
				.andExpect(view().name("players/createOrUpdatePlayerForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdatePlayerFormSuccess() throws Exception {
		mockMvc.perform(post("/players/{playerId}/edit", TEST_PLAYER_ID).with(csrf()).param("firstName", "Juan")
				.param("lastName", "Diaz").param("email", "ejemplo2@gmail.com"))
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
	void testShowPlayer() throws Exception {
		mockMvc.perform(get("/players/{playerId}", TEST_PLAYER_ID)).andExpect(status().isOk())
				.andExpect(model().attribute("player", hasProperty("lastName", is("Franklin"))))
				.andExpect(model().attribute("player", hasProperty("firstName", is("George"))))
				.andExpect(model().attribute("player", hasProperty("email", is("ejemplo@gmail.com"))))
				.andExpect(view().name("players/playerDetails"));
	}
	
	@Disabled
	@WithMockUser(value = "spring")
	@Test
	void testDeletePlayer() throws Exception {
		mockMvc.perform(delete("/players/delete/{playerId}", TEST_PLAYER_ID)).andExpect(status().isOk());
	}

}
