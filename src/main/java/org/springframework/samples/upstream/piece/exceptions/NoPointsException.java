package org.springframework.samples.upstream.piece.exceptions;

public class NoPointsException extends Exception {
	
	public NoPointsException() {
		super("You don't have movements points");
	}

}
