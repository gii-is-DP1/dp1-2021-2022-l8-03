package org.springframework.samples.upstream.tile;



import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.samples.upstream.model.BaseEntity;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.round.Round;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tiles")
public class Tile extends BaseEntity {
	
	@Column(name = "rowIndex")
	@NotNull
	private Integer rowIndex;
	
	@Column(name = "columnIndex")
	@NotNull
	private Integer columnIndex;
	
	@Column(name = "orientation")
	@NotNull
	private Integer orientation;
	
	@Column(name = "salmonEggs")
	@NotNull
	private Integer salmonEggs;
	
	@Column(name = "tileType")
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	private TileType tileType;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "round_id")
	@NotNull
	private Round round;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tile", orphanRemoval = true)
	private Collection<Piece> pieces;
}


