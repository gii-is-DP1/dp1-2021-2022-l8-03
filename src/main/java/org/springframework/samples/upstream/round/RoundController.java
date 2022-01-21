package org.springframework.samples.upstream.round;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.upstream.actingPlayer.ActingPlayerService;
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
import org.springframework.samples.upstream.score.Score;
import org.springframework.samples.upstream.score.ScoreService;
import org.springframework.samples.upstream.tile.TileService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RoundController {
	
	private static final String VIEWS_ROUND_CREATE_OR_UPDATE_FORM = "rounds/createOrUpdateRoundForm";
	
	private final RoundService roundService;
	private PlayerService playerService;
	private TileService tileService;
	private PieceService pieceService;
	private ScoreService scoreService;
	private SalmonBoardService salmonBoardService;
	private ActingPlayerService actingPlayerService;
	
	@Autowired
	public RoundController(RoundService roundService, PlayerService playerService,TileService tileService,PieceService pieceService,ScoreService scoreService, SalmonBoardService salmonBoardService,ActingPlayerService actingPlayerService) {
		this.roundService = roundService;
		this.playerService = playerService;
		this.tileService = tileService;
		this.pieceService=pieceService;
		this.scoreService=scoreService;
		this.salmonBoardService = salmonBoardService;
		this.actingPlayerService=actingPlayerService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value="/rounds/new")
	public String initCreationForm(Map<String, Object> model) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User currentUser = (User)authentication.getPrincipal();
			String currentUsername = currentUser.getUsername();
			Player player=playerService.findPlayerByUsername(currentUsername);
			
			this.roundService.checkPlayerInRound(player);
			
			Round round = new Round();
			model.put("round", round);
			return VIEWS_ROUND_CREATE_OR_UPDATE_FORM;
			
		}catch(PlayerOtherRoundException ex) {
			model.put("message", ex.getMessage());
			return "exception";
		}

	}
	
	@PostMapping(value="/rounds/new")
	public String processCreationForm(@Valid Round round, BindingResult result,Map<String, Object> model) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User currentUser = (User)authentication.getPrincipal();
			String currentUsername = currentUser.getUsername();
			Player player=playerService.findPlayerByUsername(currentUsername);
			
			this.roundService.checkPlayerInRound(player);
			
			if(result.hasErrors()) {
				return VIEWS_ROUND_CREATE_OR_UPDATE_FORM;
			}
			else {
				round.setRound_state(RoundState.CREATED);
				round.setPlayer(player);

				
				List<Player> players=new ArrayList<Player>();
				players.add(player);
				round.setPlayers(players);
				this.roundService.saveRound(round);
				
				this.actingPlayerService.createActingPlayerToRound(round);
				
				this.tileService.createInitialTiles(round);
				
				this.pieceService.createPlayerPieces(player,round);
				
				player.setRound(round);
				this.playerService.savePlayer(player);
				
				Score score=new Score();
				score.setPlayer(player);
				score.setRound(round);
				score.setValue(0);
				this.scoreService.saveScore(score);
				
				SalmonBoard board = new SalmonBoard();
				board.setRound(round);
				this.salmonBoardService.saveBoard(board);
				
				return "redirect:/rounds/"+round.getId();
			}
			
		}catch(PlayerOtherRoundException ex) {
			model.put("message", ex.getMessage());
			return "exception";
		}
	}
	
	@GetMapping(value = "/rounds")
    public String processFindForm(ModelMap model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		Player player = playerService.findPlayerByUsername(currentUsername);
		if(player.getRound() != null) {
			return "redirect:/rounds/" + player.getRound().getId();
		}
        String vista = "rounds/roundList";
        Iterable<Round> rounds = roundService.findCreatedRounds();
        boolean isFinished=false;
        boolean isInCourse=false;
        model.addAttribute("rounds", rounds);
        model.addAttribute("isFinished",isFinished);
        model.addAttribute("isInCourse",isInCourse);
        return vista;
    }

    @GetMapping(value = "/rounds/inCourse")
    public String processFindInCourse(ModelMap model) {
    	try {
    		this.playerService.checkAdmin();
            String vista = "rounds/roundList";
            Iterable<Round> rounds = roundService.findInCourseRounds();
            boolean isFinished=false;
            boolean isInCourse=true;
            model.addAttribute("isInCourse",isInCourse);
            model.addAttribute("rounds", rounds);
            model.addAttribute("isFinished",isFinished);
            return vista;
    	}catch (NoPermissionException ex){
			model.addAttribute("message", ex.getMessage());
			return "exception";
    	}
    }

    @GetMapping(value = "/rounds/finished")
    public String processFindFinished(ModelMap model) {
    	try {
    		this.playerService.checkAdmin();
            String vista = "rounds/roundList";
            Iterable<Round> rounds = roundService.findFinishedRounds();
            boolean isFinished=true;
            boolean isInCourse=false;
            model.addAttribute("isInCourse",isInCourse);
            model.addAttribute("rounds", rounds);
            model.addAttribute("isFinished",isFinished);
            return vista;
    	}catch(NoPermissionException ex) {
			model.addAttribute("message", ex.getMessage());
			return "exception";
    	}
    }
	
	@GetMapping(value = "/rounds/join/{roundId}")
	public String joinRound(@PathVariable("roundId") int roundId,ModelMap model) {
		try {
			Round round=this.roundService.findRoundById(roundId);
			this.roundService.checkRoundExist(round);
			this.roundService.checkRoundCapacity(round);
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User currentUser = (User)authentication.getPrincipal();
			String currentUsername = currentUser.getUsername();
			Player player=playerService.findPlayerByUsername(currentUsername);
			
			this.roundService.checkPlayerInRound(player);
			
			player.setRound(round);
			Collection<Round> playerRounds=player.getRounds();
			if(playerRounds==null) {
				playerRounds=new ArrayList<Round>();
			}
			playerRounds.add(round);
			player.setRounds(playerRounds);
			this.playerService.savePlayer(player); 
			
			List<Player> players=round.getPlayers();
			players.add(player);
			round.setPlayers(players);
			this.roundService.saveRound(round);
			
			Score score=new Score();
			score.setPlayer(player);
			score.setRound(round);
			score.setValue(0);
			this.scoreService.saveScore(score);
			
			this.pieceService.createPlayerPieces(player, round);
			
			return "redirect:/rounds/"+round.getId();
			
		}catch(InvalidRoundException ex) {
			model.addAttribute("message", ex.getMessage());
			return "exception";
		}catch(FullRoundException ex) {
			model.addAttribute("message", ex.getMessage());
			return "exception";
		}catch(PlayerOtherRoundException ex) {
			model.addAttribute("message", ex.getMessage());
			return "exception";
		}
		


		
	}
	
	@GetMapping(value = "/rounds/leave/{roundId}")
	public String leaveRound(@PathVariable("roundId") int roundId, ModelMap model) {
		try {
			Round round=this.roundService.findRoundById(roundId);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User currentUser = (User)authentication.getPrincipal();
			String currentUsername = currentUser.getUsername();
			Player player=playerService.findPlayerByUsername(currentUsername);
			this.roundService.checkRoundExist(round);
			this.roundService.checkPlayerInRound(round, player);
			
			
			Collection<Player> players=round.getPlayers();
			Collection<Piece> pieces=round.getPieces();

			if(round.getPlayer()==player) {
				this.scoreService.deleteScore(this.scoreService.findByPlayerAndRound(player.getId(), roundId));
				player.setRound(null);
				player.getPieces().removeAll(player.getPieces());
				SalmonBoard salmonBoard=this.salmonBoardService.findByRoundId(round.getId());
				salmonBoard.setRound(null);
				this.salmonBoardService.delete(salmonBoard);
				if(round.getRound_state().equals(RoundState.FINISHED)) {	
					round.setPlayer(player);
					this.roundService.saveRound(round);
				}else {
					this.roundService.deleteRound(round);
				}
			}else {
				this.scoreService.deleteScore(this.scoreService.findByPlayerAndRound(player.getId(), roundId));
				player.setRound(null);
				Collection<Round> playerRounds=player.getRounds();
				playerRounds.remove(round);
				player.setRounds(playerRounds);
				this.playerService.savePlayer(player);
				if(!round.getRound_state().equals(RoundState.FINISHED)) {
					players.remove(player);
				}		
				if(round.getRound_state().equals(RoundState.IN_COURSE)) {
					round.setNum_players(round.getNum_players()-1);
				}		
				
				pieces.removeAll(player.getPieces());
				player.getPieces().removeAll(player.getPieces());

				this.roundService.saveRound(round);
			}
			return "redirect:/rounds";
		}catch(InvalidRoundException ex) {
			model.addAttribute("message", ex.getMessage());
			return "exception";
		}catch(NotYourRoundException ex) {
			model.addAttribute("message", ex.getMessage());
			return "exception";
		}

	}
	
	@GetMapping({"/rounds/start/{roundId}"})
	public String startRound(@PathVariable("roundId") int roundId) {
		Round round = this.roundService.findRoundById(roundId);
		if (round.getPlayers().size()>1){
			round.setRound_state(RoundState.IN_COURSE);
			round.setMatch_start(new java.util.Date());
			round.setNum_players(round.getPlayers().size());
			this.roundService.saveRound(round);
		}
		return  "redirect:/rounds/"+roundId;
	}
	
	@GetMapping({"/rounds/{roundId}"})
	public ModelAndView showRound(@PathVariable("roundId") int roundId, HttpServletResponse response) {
		try {
			Round round = this.roundService.findRoundById(roundId);
			this.roundService.checkRoundExist(round);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User currentUser = (User)authentication.getPrincipal();
			String currentUsername = currentUser.getUsername();
			Player player=playerService.findPlayerByUsername(currentUsername);
			if(this.roundService.findRoundById(roundId).getRound_state().equals(RoundState.CREATED)) {
				ModelAndView mav = new ModelAndView("rounds/roundWaitingRoom");
				response.addHeader("Refresh", "5");
				
				Player creator = round.getPlayer();

				Boolean permission = !(player==creator);
				mav.addObject("permission", !permission);
				mav.addObject("round", round);
				return mav;
			}
			else if(this.roundService.findRoundById(roundId).getRound_state().equals(RoundState.IN_COURSE)){
				SalmonBoard board=this.salmonBoardService.findByRoundId(round.getId());
				response.addHeader("Refresh", "5");
				boolean noPieces = false;
				Color color= this.playerService.getPlayerColor(player.getId());
				ModelAndView mav = new ModelAndView("rounds/roundDetails");
				if(player.getPieces().isEmpty()) {
					color = Color.BLACK;
					noPieces = true;
				}
				mav.addObject(noPieces);
				mav.addObject(player);
				mav.addObject(board);
				mav.addObject(round);
				mav.addObject(color);
				return mav;
			}else {
				ModelAndView mav = new ModelAndView("rounds/roundScore");
				List<Score> scores = this.scoreService.findByRound(roundId);
				mav.addObject(scores);
				mav.addObject(round);
				return mav;
			}
			
		}catch(InvalidRoundException ex) {
			ModelAndView mav = new ModelAndView("exception");
			mav.addObject("message", ex.getMessage());
			return mav;
		}
	  }	
}