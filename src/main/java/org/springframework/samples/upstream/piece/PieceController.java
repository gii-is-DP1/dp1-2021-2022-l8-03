package org.springframework.samples.upstream.piece;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.upstream.piece.exceptions.InvalidCapacityException;
import org.springframework.samples.upstream.piece.exceptions.InvalidCurrentBearException;
import org.springframework.samples.upstream.piece.exceptions.InvalidCurrentWaterfallException;
import org.springframework.samples.upstream.piece.exceptions.InvalidDirectionJumpException;
import org.springframework.samples.upstream.piece.exceptions.InvalidDirectionSwimException;
import org.springframework.samples.upstream.piece.exceptions.InvalidDistanceJumpException;
import org.springframework.samples.upstream.piece.exceptions.InvalidDistanceSwimException;
import org.springframework.samples.upstream.piece.exceptions.InvalidNewBearException;
import org.springframework.samples.upstream.piece.exceptions.InvalidNewWaterfallException;
import org.springframework.samples.upstream.piece.exceptions.InvalidPlayerException;
import org.springframework.samples.upstream.piece.exceptions.InvalidPositionException;
import org.springframework.samples.upstream.piece.exceptions.NoPointsException;
import org.springframework.samples.upstream.piece.exceptions.PieceStuckException;
import org.springframework.samples.upstream.piece.exceptions.RoundNotInCourseException;
import org.springframework.samples.upstream.piece.exceptions.SameTileException;
import org.springframework.samples.upstream.piece.exceptions.TileSpawnException;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.round.RoundState;
import org.springframework.samples.upstream.tile.Tile;
import org.springframework.samples.upstream.tile.TileService;
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
	private TileService tileService;	
	
	@Autowired
	public PieceController(PieceService pieceService, TileService tileService) {
		this.pieceService = pieceService;
		this.tileService = tileService;
	}
	
	@GetMapping(value = "/piece/{pieceId}/edit")
	public String initUpdatePieceForm(@PathVariable("pieceId") int pieceId, Model model) {
		Piece piece = this.pieceService.findPieceById(pieceId);
		MovementTypeWrapper movementTypeWrapper = new MovementTypeWrapper(piece, false);
		Round round = piece.getRound();
		if(!(round.getRound_state().equals(RoundState.IN_COURSE))) {
			return "redirect:/rounds"; //NO SE PUEDE MODIFICAR UNA PIEZA SI LA PARTIDA NO HA EMPEZADO
		}
//		if(!(this.pieceService.checkUser(piece))){
//			return "redirect:/rounds";	//NO SE PUEDE MODIFICAR UNA PIEZA SI NO ES TUYA O NO ES TU TURNO
//		}
		else {
			model.addAttribute(movementTypeWrapper);
			return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
		}
	}

	@PostMapping(value = "/piece/{pieceId}/edit")
	public String processUpdatePieceForm(@Valid @ModelAttribute MovementTypeWrapper movementTypeWrapper,
			BindingResult result, @PathVariable("pieceId") int pieceId, ModelMap model) {
		Piece piece = movementTypeWrapper.getPiece();
		Boolean movementType = movementTypeWrapper.getMovementType();
		if(result.hasErrors()) {
			return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
		}
		else {
			Integer newRow =  piece.getTile().getRowIndex();
			Integer newColumn = piece.getTile().getColumnIndex();
			Piece pieceToUpdate = this.pieceService.findPieceById(pieceId);
			Integer roundId = pieceToUpdate.getRound().getId();
			try {
				Tile newTile = this.tileService.findByPosition(newRow, newColumn, roundId);
				if(movementType) {
					this.pieceService.jump(pieceToUpdate, pieceToUpdate.getTile(), newTile);
				}else {
					this.pieceService.swim(pieceToUpdate, pieceToUpdate.getTile(), newTile);
				}
				return "redirect:/rounds/"+roundId;
			}catch(InvalidPlayerException ex){
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(InvalidPositionException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(InvalidDistanceSwimException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(InvalidDistanceJumpException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(SameTileException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(InvalidCapacityException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(InvalidDirectionSwimException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(InvalidDirectionJumpException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(NoPointsException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(InvalidCurrentWaterfallException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(InvalidCurrentBearException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(InvalidNewWaterfallException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(InvalidNewBearException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(PieceStuckException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(TileSpawnException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}catch(RoundNotInCourseException ex) {
				model.addAttribute("message", ex.getMessage());
				return VIEWS_PIECE_CREATE_OR_UPDATE_FORM;
			}
			
		}
	}
}
