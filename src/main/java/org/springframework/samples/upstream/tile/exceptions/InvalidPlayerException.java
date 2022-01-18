package org.springframework.samples.upstream.tile.exceptions;

public class InvalidPlayerException extends Exception{
	
    public InvalidPlayerException() {
        super("Is not your turn");
    }
}
