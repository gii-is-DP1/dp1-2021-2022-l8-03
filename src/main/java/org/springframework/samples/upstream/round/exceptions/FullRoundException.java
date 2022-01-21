package org.springframework.samples.upstream.round.exceptions;

public class FullRoundException extends Exception{
	public FullRoundException() {
		super("This round is full");
	}
}
