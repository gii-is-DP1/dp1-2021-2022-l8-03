package org.springframework.samples.upstream.tile.exceptions;

public class InvalidPositionException extends Exception{
	
    public InvalidPositionException() {
        super("This tile doesn't exist");
    }
}
