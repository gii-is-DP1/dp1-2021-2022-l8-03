package org.springframework.samples.upstream.piece;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.samples.upstream.model.BaseEntity;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.tile.Tile;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pieces")
public class Piece extends BaseEntity{
	
	@Column(name="numSalmon")
	@NotNull
	private Integer numSalmon;
	
	@Column(name="stuck")
	@NotNull
	private Boolean stuck;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "player_id")
	@NotNull
	private Player player;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "round_id")
	@NotNull
	private Round round;
		
	@ManyToOne(optional = false)
	@JoinColumn(name = "tile_id")
	private Tile tile;
}
