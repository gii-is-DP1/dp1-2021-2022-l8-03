package org.springframework.samples.upstream.salmonBoard;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Positive;

import org.springframework.samples.upstream.model.BaseEntity;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.tile.Tile;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name="salmonboards")
public class SalmonBoard extends BaseEntity{
	String background;
    @Positive
    int width;
    @Positive
    int height;
   
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "round")
	private Round round;

    public SalmonBoard(){
        this.background="/resources/images/back_pattern.jpg"; //fondo del tablero
        this.width=800;
        this.height=800;
    }
    
    public List<Piece> getPieces(){
    	return new ArrayList<Piece>(round.getPieces()); 
    }
    
    public List<Tile> getTiles(){
    	return new ArrayList<Tile>(round.getTiles()); 
    }
}


