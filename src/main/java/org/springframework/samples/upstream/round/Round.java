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
	
	@Column(name = "round_state")
	private RoundState round_state;
	
	@ManyToOne(optional = true,cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private Player player;
	
	@OneToOne(optional = true, mappedBy="round",cascade = CascadeType.ALL)
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
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "round",orphanRemoval=true)
	private Collection<Piece> pieces;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "round",orphanRemoval=true)
	private Collection<Score> scores;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "round",orphanRemoval=true)
    private Collection<Player> players;

}
