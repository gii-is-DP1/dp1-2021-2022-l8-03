package org.springframework.samples.upstream.round;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.upstream.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rounds")
public class Round extends BaseEntity {
	
	@Column(name = "duration")
	@NotNull
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
	
	@Column(name = "match_start")
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm")
	@NotNull
	private Date match_start;
	
	@Column(name = "match_end")
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm")
	@NotNull
	private Date match_end;
	
	@Column(name = "turn_start")
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm")
	@NotNull
	private Date turn_start;
}