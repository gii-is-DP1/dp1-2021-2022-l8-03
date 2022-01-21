package org.springframework.samples.upstream.round;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.player.PlayerService;
import org.springframework.samples.upstream.player.exceptions.NoPermissionException;
import org.springframework.samples.upstream.round.exceptions.FullRoundException;
import org.springframework.samples.upstream.round.exceptions.InvalidRoundException;
import org.springframework.samples.upstream.round.exceptions.NotYourRoundException;
import org.springframework.samples.upstream.round.exceptions.PlayerOtherRoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoundService {
	
	private RoundRepository roundRepository;
	
	private PlayerService playerService;
	
	@Autowired
	public RoundService(RoundRepository roundRepository,PlayerService playerService) {
		this.roundRepository = roundRepository;
		this.playerService = playerService;
	}
	

	public Round findRoundById(int id) throws DataAccessException {
		return roundRepository.findById(id);
	}
	

	public Collection<Round> findAll() throws DataAccessException {
		return roundRepository.findAll();
	}
	
	@Transactional
	public void saveRound(Round round) throws DataAccessException {
		roundRepository.save(round);
	}
	
	@Transactional
	public void deleteRound(Round round) throws DataAccessException {
		for(Player player :round.getPlayers()) {
			player.setRound(null);
		}
		roundRepository.delete(round);
	}
	

    public Collection<Round> findCreatedRounds() throws DataAccessException {
        return roundRepository.findCreatedRounds();
    }


    public Collection<Round> findInCourseRounds() throws DataAccessException,NoPermissionException {
    	if(this.playerService.checkAdminBoolean()) {
    		return roundRepository.findInCourseRounds();
    	}else {
    		throw new NoPermissionException();
    	}
    }


    public Collection<Round> findFinishedRounds() throws DataAccessException,NoPermissionException {
    	if(this.playerService.checkAdminBoolean()) {
    		return roundRepository.findFinishedRounds();
		}else {
			throw new NoPermissionException();
		}
    }
    
    public void checkRoundExist(Round round) throws InvalidRoundException{
    	if(round==null) {
    		throw new InvalidRoundException();
    	}
    }
    
    public void checkRoundCapacity(Round round) throws FullRoundException{
    	if(round.getNum_players()==round.getPlayers().size()) {
    		throw new FullRoundException();
    	}
    }
    
    public void checkPlayerInRound(Round round,Player player) throws NotYourRoundException {
    	if(!round.getPlayers().contains(player)) {
    		throw new NotYourRoundException();
    	}
    }
    
    public void checkPlayerInRound(Player player) throws PlayerOtherRoundException{
    	if(player.getRound()!=null) {
    		throw new PlayerOtherRoundException();
    	}
    }
}
