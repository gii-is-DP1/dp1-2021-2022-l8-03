package org.springframework.samples.petclinic.piece;

import javax.persistence.Entity;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Data;
//Piece
@Data
@Entity
public class Piece extends NamedEntity{
	private Integer numSalmon;
}
