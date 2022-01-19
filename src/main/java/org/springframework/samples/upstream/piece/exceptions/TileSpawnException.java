package org.springframework.samples.upstream.piece.exceptions;

public class TileSpawnException extends Exception {
	
	public TileSpawnException() {
		super("You can't move a piece from a spawn tile, and you can't move a piece into a spawn tile with 2+ eggs");
	}

}
