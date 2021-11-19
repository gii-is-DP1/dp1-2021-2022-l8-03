package org.springframework.samples.upstream.round;


import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.upstream.actingPlayer.ActingPlayer;
import org.springframework.samples.upstream.model.BaseEntity;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.score.Score;
import org.springframework.samples.upstream.tile.BearTile;
import org.springframework.samples.upstream.tile.EagleTile;
import org.springframework.samples.upstream.tile.HeronTile;
import org.springframework.samples.upstream.tile.RapidsTile;
import org.springframework.samples.upstream.tile.RockTile;
import org.springframework.samples.upstream.tile.SeaTile;
import org.springframework.samples.upstream.tile.SpawnTile;
import org.springframework.samples.upstream.tile.WaterfallTile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rounds")
public class Round extends BaseEntity {
	
	@Column(name = "duration")
	private Double duration;
	
	@Column(name = "rapids")
	@NotNull
	private Boolean rapids;
	
	@Column(name = "whirlpools")
	@NotNull
	private Boolean whirlpools;
	
	@Column(name = "num_players")
	@NotNull
	private Integer num_players;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "player_id")
    private Player player;
	
	@OneToOne(optional = false, mappedBy="round")
	private ActingPlayer actingPlayer;
	

	
	@Column(name = "match_start")
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm")
	private Date match_start;
	
	@Column(name = "match_end")
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm")
	private Date match_end;
	
	@Column(name = "turn_start")
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm")
	private Date turn_start;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "round")
	private Collection<Piece> pieces;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "round")
	private Collection<Score> scores;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "round")
	private Collection<RapidsTile> rapidsTiles;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "round")
	private Collection<RockTile> rockTiles;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "round")
	private Collection<SeaTile> seaTiles;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "round")
	private Collection<SpawnTile> spawnTiles;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "round")
	private Collection<BearTile> bearTiles;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "round")
	private Collection<HeronTile> heronTiles;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "round")
	private Collection<EagleTile> eagleTiles;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "round")
	private Collection<WaterfallTile> waterfallTiles;
}
