package org.springframework.samples.upstream.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.upstream.piece.PieceService;
import org.springframework.samples.upstream.player.PlayerService;
import org.springframework.samples.upstream.round.RoundService;
import org.springframework.samples.upstream.score.ScoreService;
import org.springframework.samples.upstream.tile.TileService;
import org.springframework.stereotype.Controller;

@Controller
public class SalmonBoardController {

	private SalmonBoardService salmonBoardService;
	
	@Autowired
	public SalmonBoardController(SalmonBoardService salmonBoardService) {
		this.salmonBoardService = salmonBoardService;
	}
}
