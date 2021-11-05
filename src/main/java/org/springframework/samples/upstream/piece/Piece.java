package org.springframework.samples.upstream.piece;

import javax.persistence.Entity;

import org.springframework.samples.upstream.model.NamedEntity;

import lombok.Data;
//Piece
@Data
@Entity
public class Piece extends NamedEntity{
	private Integer numSalmon;
}
