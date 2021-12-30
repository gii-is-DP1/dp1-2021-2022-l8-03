package org.springframework.samples.upstream.score;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.upstream.model.BaseEntity;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.round.Round;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "scores")
public class Score extends BaseEntity{
	
	@Column(name="value")
	@NotNull
	private Integer value;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "player_id")
	@NotNull
	private Player player;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "round_id")
	@NotNull
	private Round round;
}
