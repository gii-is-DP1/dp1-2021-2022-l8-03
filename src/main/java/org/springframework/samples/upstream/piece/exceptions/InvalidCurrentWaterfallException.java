package org.springframework.samples.upstream.piece.exceptions;

public class InvalidCurrentWaterfallException extends Exception {
	
	public InvalidCurrentWaterfallException() {
		super("You are in a waterfall tile");
	}

}
