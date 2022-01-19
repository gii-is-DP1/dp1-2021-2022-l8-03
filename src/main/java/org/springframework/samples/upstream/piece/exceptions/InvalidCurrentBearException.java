package org.springframework.samples.upstream.piece.exceptions;

public class InvalidCurrentBearException extends Exception {
	
	public InvalidCurrentBearException() {
		super("You are in a bear tile");
	}

}
