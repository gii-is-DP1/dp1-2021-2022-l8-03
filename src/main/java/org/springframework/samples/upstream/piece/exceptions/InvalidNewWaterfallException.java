package org.springframework.samples.upstream.piece.exceptions;

public class InvalidNewWaterfallException extends Exception {
	
	public InvalidNewWaterfallException() {
		super("There is a waterfall tile in your way");
	}

}
