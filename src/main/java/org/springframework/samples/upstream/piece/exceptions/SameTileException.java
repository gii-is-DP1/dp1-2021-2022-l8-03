package org.springframework.samples.upstream.piece.exceptions;

public class SameTileException extends Exception {
	
	public SameTileException() {
		super("You can't stay in the same tile");
	}

}
