package org.springframework.samples.upstream.tile;



import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.samples.upstream.model.BaseEntity;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.round.Round;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Tile extends BaseEntity {
	private Integer rowIndex;
	private Integer columnIndex;
	private Integer orientation;
	private Integer salmonEggs;
	
	@Enumerated(EnumType.ORDINAL)
	private TileType tileType;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "round_id")
	private Round round;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tile")
	private Collection<Piece> pieces;
}


