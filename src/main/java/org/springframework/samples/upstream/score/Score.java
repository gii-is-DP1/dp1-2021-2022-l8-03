package org.springframework.samples.upstream.score;

import javax.persistence.Entity;

import org.springframework.samples.upstream.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Score extends BaseEntity{
	private Integer value;
}
