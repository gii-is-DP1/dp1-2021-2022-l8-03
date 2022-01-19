package org.springframework.samples.upstream.piece.exceptions;

public class InvalidPositionException extends Exception{
	
    public InvalidPositionException() {
        super("This tile doesn't exist");
    }
}
