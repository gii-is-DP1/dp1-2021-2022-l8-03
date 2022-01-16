package org.springframework.samples.upstream.piece;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.samples.upstream.model.BaseEntity;
import org.springframework.samples.upstream.player.Player;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.tile.Tile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Piece extends BaseEntity{
	private Integer numSalmon;
	private Boolean stuck;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "player_id")
	private Player player;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "round_id")
	private Round round;
		
	@ManyToOne(optional = false)
	@JoinColumn(name = "tile_id")
	private Tile tile;
	
    Color color;
    
    public Integer getPositionXInPixels(Integer size) {
    	List<Piece> pieces = new ArrayList<Piece>(tile.getPieces());
    	Integer position = tile.getPositionXInPixels(size);
    	Piece p = this;
    	if(pieces.indexOf(p)==0) {
    		position+=10;
    	}
    	else if(pieces.indexOf(p)==1) {
    		position+=10;
    	}
    	else if(pieces.indexOf(p)==2) {
    		position+=50;
    	}
    	else if(pieces.indexOf(p)==3) {
    		position+=50;
    	}
    	else {
    		position+=75;
    	}
    	return position;
    }
    
    public Integer getPositionYInPixels(Integer size) {
    	List<Piece> pieces = new ArrayList<Piece>(tile.getPieces());
    	Piece p = this;
    	Integer position = tile.getPositionYInPixels(size);
    	if(pieces.indexOf(p)==0) {
    		position+=26;
    	}
    	else if(pieces.indexOf(p)==1) {
    		position+=68;
    	}
    	else if(pieces.indexOf(p)==2) {
    		position+=8;
    	}
    	else if(pieces.indexOf(p)==3) {
    		position+=86;
    	}
    	else {
    		position+=47;
    	}
    	return position;
    }
}
