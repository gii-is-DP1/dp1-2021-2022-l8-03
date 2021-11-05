package org.springframework.samples.petclinic.score;

import javax.persistence.Entity;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Score extends NamedEntity{
	private Integer value;
}
