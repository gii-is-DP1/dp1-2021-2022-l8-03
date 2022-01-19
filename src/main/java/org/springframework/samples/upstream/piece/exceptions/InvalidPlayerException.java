package org.springframework.samples.upstream.piece.exceptions;

public class InvalidPlayerException extends Exception{
	
    public InvalidPlayerException() {
        super("It's not your turn");
    }
}
