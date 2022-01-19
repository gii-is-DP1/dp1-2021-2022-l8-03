package org.springframework.samples.upstream.piece.exceptions;

public class TileSpawnException extends Exception {
	
	public TileSpawnException() {
		super("You are in a spawn tile");
	}

}
