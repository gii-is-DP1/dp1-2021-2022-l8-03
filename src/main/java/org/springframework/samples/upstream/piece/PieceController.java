package org.springframework.samples.upstream.piece;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.player.PlayerService;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.round.RoundService;
import org.springframework.samples.upstream.tile.Tile;
import org.springframework.samples.upstream.tile.TileService;
import org.springframework.samples.upstream.tile.TileType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
	
	@Autowired
	public PieceController(PieceService pieceService, PlayerService playerService, RoundService roundService, TileService tileService) {
		this.pieceService = pieceService;
		this.playerService = playerService;
		this.roundService = roundService;
		this.tileService = tileService;
	}
	
//	@ModelAttribute("player")
//	public Player findPlayer(@PathVariable("playerId") int playerId) {
//		return this.playerService.findPlayerById(playerId);
//	}
//	
//	@ModelAttribute("round")
//	public Round findRound(@PathVariable("roundId") int roundId) {
//		return this.roundService.findRoundById(roundId);
//	}
//	
//	@ModelAttribute("tile")
//	public Tile findTile(@PathVariable("tileId") int tileId) {
//		return this.tileService.findTileById(tileId);
//	}
	
	@GetMapping(value = "/piece/{pieceId}/edit")
	public String initUpdatePieceForm(@PathVariable("pieceId") int pieceId, Model model) {
		Piece piece = this.pieceService.findPieceById(pieceId);
		model.addAttribute(piece);
//		Round round = piece.getRound();
//		Integer actingPlayer = round.getActingPlayer().getPlayer();
//		List<Player> players = (List<Player>) round.getPlayers();
//		String actingUsername = players.get(actingPlayer).getUser().getUsername();
//		String pieceUsername = piece.getPlayer().getUser().getUsername();
//		String authenticatedUsername = getCurrentUsername();
//		if(!(round.getRound_state().equals(RoundState.IN_COURSE))) {
//			return "exception"; //NO SE PUEDE MODIFICAR UNA PIEZA SI LA PARTIDA NO HA EMPEZADO
//		}
//		if(!(actingUsername.equals(pieceUsername)&&actingUsername.equals(authenticatedUsername))){
//			return "exception";	//NO SE PUEDE MODIFICAR UNA PIEZA SI NO ES TUYA O NO ES TU TURNO
//		}else {
			return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
//		}
	}

	@PostMapping(value = "/piece/{pieceId}/edit")
	public String processUpdatePieceForm(@Valid @ModelAttribute Piece piece, BindingResult result,
			@PathVariable("pieceId") int pieceId, ModelMap model) {
		if(result.hasErrors()) {
			return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
		}
		else {
			Integer newRow =  piece.getTile().getRowIndex();
			Integer newColumn = piece.getTile().getColumnIndex();
			Piece pieceToUpdate = this.pieceService.findPieceById(pieceId);
			Tile newTile = this.tileService.findByPosition(newRow, newColumn);
			piece.setId(pieceToUpdate.getId());
			piece.setNumSalmon(pieceToUpdate.getNumSalmon());
			piece.setPlayer(pieceToUpdate.getPlayer());
			piece.setRound(pieceToUpdate.getRound());
			piece.setTile(newTile);
			this.pieceService.savePiece(piece);
			return "redirect:/rounds";
		}
	}
	
	public String getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User)authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		return currentUsername;
	}
}
