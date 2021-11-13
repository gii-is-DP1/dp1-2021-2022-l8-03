package org.springframework.samples.upstream.tile;


import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.springframework.samples.upstream.model.BaseEntity;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.round.Round;



@MappedSuperclass

public class Tile extends BaseEntity {
	private Integer rowIndex;
	private Integer columnIndex;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "round_id")
	private Round round;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tile")
	private Collection<Piece> pieces;
}


