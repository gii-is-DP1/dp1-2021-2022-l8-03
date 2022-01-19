package org.springframework.samples.upstream.piece.exceptions;

public class InvalidNewBearException extends Exception {
	
	public InvalidNewBearException() {
		super("There is a bear tile in your way");
	}

}
