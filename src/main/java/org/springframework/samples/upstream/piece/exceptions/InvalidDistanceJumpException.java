package org.springframework.samples.upstream.piece.exceptions;

public class InvalidDistanceJumpException extends Exception {
	
	public InvalidDistanceJumpException() {
		super("You don't have enough movement points");
	}

}
