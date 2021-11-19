package org.springframework.samples.upstream.piece;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.upstream.actingPlayer.ActingPlayer;
import org.springframework.samples.upstream.actingPlayer.ActingPlayerService;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.player.PlayerService;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.round.RoundService;
import org.springframework.samples.upstream.tile.Tile;
import org.springframework.samples.upstream.tile.TileService;
import org.springframework.samples.upstream.user.AuthoritiesService;
import org.springframework.samples.upstream.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PieceController {
	
	private static final String VIEWS_PIECE_CREATE_OR_UPDATE_FORM = "pieces/createOrUpdatePieceForm";
	
	private PieceService pieceService;
	private PlayerService playerService;
	private RoundService roundService;
	private TileService tileService;
	private ActingPlayerService actingPlayerService;
	
	@Autowired
	public PieceController(PieceService pieceService, PlayerService playerService, RoundService roundService, TileService tileService
			, ActingPlayerService actingPlayerService) {
		this.pieceService = pieceService;
		this.playerService = playerService;
		this.roundService = roundService;
		this.tileService = tileService;
		this.actingPlayerService = actingPlayerService;
	}
	
	@ModelAttribute("player")
	public Player findPlayer(@PathVariable("playerId") int playerId) {
		return this.playerService.findPlayerById(playerId);
	}
	
	@ModelAttribute("round")
	public Round findRound(@PathVariable("roundId") int roundId) {
		return this.roundService.findRoundById(roundId);
	}
	
	@ModelAttribute("tile")
	public Tile findTile(@PathVariable("tileId") int tileId) {
		return this.tileService.findTileById(tileId);
	}
	
	@GetMapping(value = "/piece/new")
	public String initCreationForm(Map<String, Object> model) {
		Piece piece = new Piece();
		model.put("piece", piece);
		return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/piece/new")
	public String processCreationForm(@Valid Piece piece, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.pieceService.savePiece(piece);
			
			return "redirect:/players/" + piece.getId();
		}
	}
	
	@GetMapping(value = "/pieces/{pieceId}/edit")
	public String initUpdatePieceForm(@PathVariable("pieceId") int pieceId, Player player, Round round, Tile tile, Model model) {
		String username = player.getUser().getUsername();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User)authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		Integer actingPlayer = actingPlayerService.findActingPlayerByRound(round.getId()).getPlayer();
		String actingUsername = playerService.findPlayerById(actingPlayer).getUser().getUsername();
		if(!(username.equals(currentUsername)&&username.equals(actingUsername)&&currentUsername.equals(actingUsername))){
			return "exception";		//HAY QUE HACER UN MENSAJE DE ERROR EN CONDICIONES
		}else {
			return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
		}
	}

	@PostMapping(value = "/pieces/{pieceId}/edit")
	public String processUpdatePieceForm(@Valid Piece piece, BindingResult result,
			@PathVariable("playerId") int playerId) {
		
		return null;
	}
}
