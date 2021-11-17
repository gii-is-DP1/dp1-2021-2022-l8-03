package org.springframework.samples.upstream.piece;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.samples.upstream.model.BaseEntity;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.round.Round;
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
//Piece
@Getter
@Setter
@Entity
public class Piece extends BaseEntity{
	private Integer numSalmon;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "player_id")
	private Player player;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "round_id")
	private Round round;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "rapidsTile_id")
	private RapidsTile rapidsTile;
	
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "rockTile_id")
	private RockTile rockTile;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "seaTile_id")
	private SeaTile seaTile;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "spawnTile_id")
	private SpawnTile spawnTile;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "bearTile_id")
	private BearTile bearTile;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "eagleTile_id")
	private EagleTile eagleTile;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "heronTile_id")
	private HeronTile heronTile;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "waterfallTile_id")
	private WaterfallTile waterfallTile;
	
	
	
}
