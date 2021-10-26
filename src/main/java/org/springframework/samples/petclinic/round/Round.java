package org.springframework.samples.petclinic.round;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.model.BaseEntity;

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
	
	@Column(name = "numPlayers")
	@NotNull
	private Integer numPlayers;

}
