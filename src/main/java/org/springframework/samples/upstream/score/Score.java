package org.springframework.samples.upstream.score;

import javax.persistence.Entity;

import org.springframework.samples.upstream.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Score extends NamedEntity{
	private Integer value;
}
