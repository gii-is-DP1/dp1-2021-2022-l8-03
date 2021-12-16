package org.springframework.samples.upstream.piece;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.samples.upstream.model.BaseEntity;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.tile.Tile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Piece extends BaseEntity{
	private Integer numSalmon;
	private Boolean stuck;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "player_id")
	private Player player;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "round_id")
	private Round round;
		
	@ManyToOne(optional = false)
	@JoinColumn(name = "tile_id")
	private Tile tile;
}
