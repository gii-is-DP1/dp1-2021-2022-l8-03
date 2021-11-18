package org.springframework.samples.upstream.actingPlayer;



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
	
	@Column(name = "points")
	@NotNull
	private Integer points;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "round_id")
	private Round round;

}
