package org.springframework.samples.petclinic.piece;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pieces")
public class PieceController {
	@Autowired
	PieceService pieceService;
	
	@GetMapping()
	public String listadoPiece(ModelMap modelMap) {
		String vista="pieces/listadoPieces";
		Iterable<Piece> pieces=pieceService.findAllPieces();
		modelMap.addAttribute("pieces", pieces);
		return vista;
	}
	
}
