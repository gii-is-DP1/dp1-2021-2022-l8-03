package org.springframework.samples.upstream.actingPlayer;



import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.upstream.model.BaseEntity;

import org.springframework.samples.upstream.round.Round;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "acting_players")
public class ActingPlayer extends BaseEntity {
	
	@Column(name = "player")
	@NotNull
	private Integer player;
	
	@Column(name = "points")
	@NotNull
	private Integer points;
	
	@Column(name = "firstPlayer")
	@NotNull
	private Integer firstPlayer;
	
	@Column(name = "turn")
	@NotNull
	private Integer turn;
	
	@OneToOne(optional = false,cascade=CascadeType.ALL)
	@JoinColumn(name = "round_id")
	private Round round;

}
