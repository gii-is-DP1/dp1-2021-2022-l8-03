package org.springframework.samples.upstream.board;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Positive;

import org.springframework.samples.upstream.model.BaseEntity;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.tile.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
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


