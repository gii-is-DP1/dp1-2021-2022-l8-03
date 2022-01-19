package org.springframework.samples.upstream.piece.exceptions;

public class PieceStuckException extends Exception {
	
	public PieceStuckException() {
		super("Your piece is stuck");
	}

}
