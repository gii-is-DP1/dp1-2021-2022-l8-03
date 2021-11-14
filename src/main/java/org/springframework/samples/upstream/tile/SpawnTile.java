package org.springframework.samples.upstream.tile;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.round.Round;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SpawnTile extends Tile{
	private Integer salmonEggs;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "round_id")
	private Round round;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "spawnTile")
	private Collection<Piece> pieces;
}
