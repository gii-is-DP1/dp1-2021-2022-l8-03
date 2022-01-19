package org.springframework.samples.upstream.piece.exceptions;

public class InvalidDistanceSwimException extends Exception {
	
	public InvalidDistanceSwimException() {
		super("You can swim just one tile");
	}

}
