package org.springframework.samples.upstream.piece;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.samples.upstream.model.NamedEntity;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.round.Round;

import lombok.Data;
//Piece
@Data
@Entity
public class Piece extends NamedEntity{
	private Integer numSalmon;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "player_id")
	private Player player;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "round_id")
	private Round round;
	
}
