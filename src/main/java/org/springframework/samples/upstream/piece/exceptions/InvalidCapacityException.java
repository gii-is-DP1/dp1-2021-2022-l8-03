package org.springframework.samples.upstream.piece.exceptions;

public class InvalidCapacityException extends Exception {
	
	public InvalidCapacityException() {
		super("The capacity of the tile is full");
	}

}
