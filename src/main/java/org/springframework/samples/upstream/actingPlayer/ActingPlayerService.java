package org.springframework.samples.upstream.actingPlayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActingPlayerService {
	
	private ActingPlayerRepository actingPlayerRepository;
	
	@Autowired
	public ActingPlayerService(ActingPlayerRepository actingPlayerRepository) {
		this.actingPlayerRepository = actingPlayerRepository;
	}
	
	public void swim(Integer id) {
		ActingPlayer jugador = this.actingPlayerRepository.findByRound(id);
		jugador.setPoints((jugador.getPoints()-1));
	}
	
	public void jump(Integer id) {
		ActingPlayer jugador = this.actingPlayerRepository.findByRound(id);
		jugador.setPoints((jugador.getPoints()-2));
	}

}
