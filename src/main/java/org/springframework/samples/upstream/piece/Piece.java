package org.springframework.samples.upstream.piece;

import javax.persistence.Entity;

import org.springframework.samples.upstream.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Piece extends NamedEntity{
	private Integer numSalmon;
}
