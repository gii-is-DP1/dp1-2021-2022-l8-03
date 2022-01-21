package org.springframework.samples.upstream.round.exceptions;

public class InvalidRoundException extends Exception{
	
	public InvalidRoundException() {
		super("This round doesn't exist");
	}
}
