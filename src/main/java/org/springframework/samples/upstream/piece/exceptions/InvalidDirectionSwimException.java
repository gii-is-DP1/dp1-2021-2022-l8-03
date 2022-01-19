package org.springframework.samples.upstream.piece.exceptions;

public class InvalidDirectionSwimException extends Exception {
	
	public InvalidDirectionSwimException() {
		super("You can't swim backwards");
	}

}
